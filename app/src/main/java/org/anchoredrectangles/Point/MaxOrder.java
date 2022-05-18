/*
 * Order by distance in the infinity norm.
 */
package org.anchoredrectangles.Point;

import java.util.Comparator;

public class MaxOrder implements Comparator<Point> {
    
    @Override
    public int compare(Point p, Point q) {
        double maxP = 0;
        for(int i = 0; i < p.dimension(); i++) {
            maxP = Math.max(maxP, p.get(i));
        }
        double maxQ = 0;
        for(int i = 0; i < q.dimension(); i++) {
            maxQ = Math.max(maxQ, q.get(i));
        }
        return Double.compare(maxP, maxQ);
    }
}
