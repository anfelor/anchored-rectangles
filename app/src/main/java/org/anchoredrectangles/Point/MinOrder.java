/*
 * Order by distance in the negative infinity "norm".
 */
package org.anchoredrectangles.Point;

import java.util.Comparator;

public class MinOrder implements Comparator<Point> {
    
    @Override
    public int compare(Point p, Point q) {
        double minP = 0;
        for(int i = 0; i < p.dimension(); i++) {
            minP = Math.min(minP, p.get(i));
        }
        double minQ = 0;
        for(int i = 0; i < q.dimension(); i++) {
            minQ = Math.min(minQ, q.get(i));
        }
        return Double.compare(minP, minQ);
    }
}
