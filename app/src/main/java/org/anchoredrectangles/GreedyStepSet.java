/*
 * An abstraction for the step and tileStep procedures.
 * Initialization is O(n*log(n)) and a single greedy step is O(n).
 * In total, we only use O(n) storage space.
 * We do not follow the procedure described by Jonathan Harder,
 * but implement our own procedure that uses way less space
 * (and is probably faster due to better memory access patterns).
 * TODO: Only works in 2D for now.
 */
package org.anchoredrectangles;

import java.util.Arrays;
import java.util.function.Function;

import org.anchoredrectangles.Point.LexicographicalOrder;
import org.anchoredrectangles.Point.Point;

public class GreedyStepSet implements SolutionSet {
    public GreedyStepSet(Instance in) {
        this.coverage = 0;
        this.in = in;
        this.sortedPoints = new Point[in.getSize()];
        System.arraycopy(in.getPoints(), 0, this.sortedPoints, 0, in.getPoints().length);
        Arrays.sort(this.sortedPoints, new LexicographicalOrder());
        this.corners = new Point[in.getSize() + 1];
    }

    public GreedyStepSet(GreedyStepSet s) {
        this.coverage = s.coverage;
        this.whitespace = s.whitespace;
        this.in = s.in;
        this.sortedPoints = s.sortedPoints;
        this.corners = new Point[in.getSize() + 1];
        System.arraycopy(s.corners, 0, this.corners, 0, s.corners.length);
    }

    /*
     * Add a new rectangle anchored at the given point to the solution.
     * The point is understood as an index into the provided instance.
     */
    public void step(int point) {
        Point tileCorner = findTileRectangle(point);
        applyRectangleSolution(point,
          findRectangle(in.getPoints()[point], point, tileCorner));
    }
    
    /*
     * Add a new rectangle anchored at the given point to the solution.
     * The point is understood as an index into the provided instance.
     */
    public void tileStep(int point) {
        Point tileCorner = findTiledRectangle(point);
        applyRectangleSolution(point,
          findRectangle(in.getPoints()[point], point, tileCorner));
    }

    public void stepOrigin() {
        Point tileCorner = Point.ones(in.getDimension());
        applyRectangleSolution(corners.length - 1,
          findRectangle(Point.origin(in.getDimension()), corners.length - 1, tileCorner));
    }

    // First, we find out what the biggest rectangle would be
    // if we ignore other points and only take into account already
    // placed rectangles.
    // This gives us the smallest rectangle which contains the tile.
    private Point findTileRectangle(int point) {
        return findTileRectangleWithTest(point, i -> q -> tileRect ->
          corners[i] != null && tileRect.intersects(new Rectangle(q, corners[i])));
    }

    // Alternatively, for TilePacking we find out what the biggest rectangle
    // would be if we ignore other points and only take into account already
    // placed _tiles_.
    // This gives us the smallest rectangle which contains the tile.
    private Point findTiledRectangle(int point) {
        return findTileRectangleWithTest(point, i -> q -> tileRect ->
          corners[i] != null && tileRect.intersects(new Rectangle(q, in.getCorner())));
    }

    // Then, we consider the tile. We visit all points that are in the
    // tile rectangle from above and consider the corners that appear
    // when subtracting their best rectangle from ours.
    private RectangleSolution findRectangle(Point p, int idx, Point tileCorner) {
        Point maxCorner = p;
        double size = 0;
        double whitespace = 0;
        double lastx = p.get(0);
        for(int i = 0; i < sortedPoints.length; i++) {
            Point q = sortedPoints[i];
            assert(in.getDimension() == 2);
            if(q.get(0) < p.get(0) || q.get(0) > tileCorner.get(0)
            || q.get(1) < p.get(1) || q.get(1) > tileCorner.get(1)
            || q.equals(p)) { continue; }
            Point newCorner = new Point(q.get(0), tileCorner.get(1));
            double newSize = new Rectangle(p, newCorner).getSize();
            if(newSize > size) {
                maxCorner = newCorner; size = newSize;
            }
            whitespace += (newCorner.get(1) - p.get(1)) * (newCorner.get(0) - lastx);
            lastx = q.get(0);
            tileCorner.set(1, q.get(1));
        }
        whitespace += (tileCorner.get(1) - p.get(1)) * (tileCorner.get(0) - lastx);
        double lastSize = new Rectangle(p, tileCorner).getSize();
        if(lastSize > size) {
            maxCorner = tileCorner;
        }

        RectangleSolution s = new RectangleSolution();
        s.corner = maxCorner;
        s.coverage = new Rectangle(p, maxCorner).getSize();
        s.whitespace = whitespace - s.coverage;
        return s;
    }

    private void applyRectangleSolution(int idx, RectangleSolution s) {
        corners[idx] = s.corner;
        this.whitespace += s.whitespace;
        this.coverage += s.coverage;
    }

    // Find out if there is any point in the tile of the given point.
    public boolean hasEmptyTile(int point) {
        Point tileCorner = findTileRectangle(point);
        Point p = in.getPoints()[point];
        for(int i = 0; i < sortedPoints.length; i++) {
            Point q = sortedPoints[i];
            if(q.get(0) < p.get(0) || q.get(0) > tileCorner.get(0)
            || q.get(1) < p.get(1) || q.get(1) > tileCorner.get(1)
            || q.equals(p)) { continue; }
            return false;
        }
        return true;
    }

    public double lowerBoundWhitespace(int point) {
        Point tileCorner = findTileRectangleWithTest(point, i -> q -> tileRect ->
          tileRect.intersects(new Rectangle(q, in.getCorner())));
        return findRectangle(in.getPoints()[point], point, tileCorner).whitespace;
    }

    private Point findTileRectangleWithTest(int point, Function<Integer, Function<Point, Function<Rectangle, Boolean>>> test) {
        Point p = in.getPoints()[point];
        Point tileCorner = new Point(in.getCorner());
        Rectangle tileRect = new Rectangle(p, tileCorner);
        for(int i = 0; i < in.getPoints().length; i++) {
            Point q = in.getPoints()[i];
            if(point != i && test.apply(i).apply(q).apply(tileRect)) {
                assert(in.getDimension() == 2);
                if(q.get(0) <= p.get(0)) {
                    assert(p.get(1) > q.get(1));
                    assert(tileCorner.get(1) > q.get(1));
                    tileCorner.set(1, q.get(1));
                }
                if(q.get(1) <= p.get(1)) {
                    assert(p.get(0) > q.get(0));
                    assert(tileCorner.get(0) > q.get(0));
                    tileCorner.set(0, q.get(0));
                }
            }
        }
        return tileCorner;
    }

    public Point[] getCorners() {
        return this.corners;
    }

    public double getCoverage() {
        return coverage;
    }
 
    // Whitespace is only guaranteed to be right for TilePacking.
    // With GreedyPacking we count some space more than once.
    public double getWhiteSpace() {
        return whitespace;
    }

    private double coverage;
    private double whitespace;
    private Instance in;
    private Point[] sortedPoints; // The points of the instance sorted lexicographically.
    private Point[] corners; // Corners for the points we have already considered.  
}

class RectangleSolution {
    public double coverage;
    public double whitespace;
    public Point corner;
}