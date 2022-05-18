/*
 * A generator for instances that samples the points
 * uniformly from the rectangle.
 */
package org.anchoredrectangles;

import java.util.Random;

import org.anchoredrectangles.Point.Point;

public class UniformGenerator {
    /*
     * Generate n points (not including the origin).
     * With extremely high probability no points will be duplicated
     * and there is no point at the origin, but this
     * is not checked.
     */
    public static Instance generate(int n, Point corner) {
        Random r = new Random();
        Point points[] = new Point[n];
        for(int i = 0; i < n; i++) {
            double[] point = new double[corner.dimension()];
            for(int d = 0; d < corner.dimension(); d++) {
                point[d] = r.nextDouble() * corner.get(d);
                point[d] = (double) Math.round(point[d] * 1000) / 1000;
            }
            points[i] = new Point(point);
        }
        return new Instance(corner, points);
    }
}
