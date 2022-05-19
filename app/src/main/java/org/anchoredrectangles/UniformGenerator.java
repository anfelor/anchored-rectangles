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
        Instance in; int exactness = (int) Math.pow(10, Math.log10((double) n) + 2);
        do {
            Random r = new Random();
            Point points[] = new Point[n];
            for(int i = 0; i < n; i++) {
                double[] point = new double[corner.dimension()];
                for(int d = 0; d < corner.dimension(); d++) {
                    point[d] = r.nextDouble() * corner.get(d);
                    point[d] = (double) Math.round(point[d] * exactness) / exactness;
                }
                points[i] = new Point(point);
            }
            in = new Instance(corner, points);
        } while(in.hasDuplicates());
        return in;
    }
}
