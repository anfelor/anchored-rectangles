/*
 * Lexicographical ordering, but we consider it starting from axis d.
 */
package org.anchoredrectangles.Point;

import java.util.Comparator;

public class AxisOrder implements Comparator<Point> {
    private int d;
    public AxisOrder(int d) {
        this.d = d;
    }

    @Override
    public int compare(Point p, Point q) {
        int minD = Math.min(p.dimension(), q.dimension());
        for(int j = d; j < d + minD; j++) {
            int i = j % minD;
            if(p.get(i) != q.get(i)) {
                if(p.get(i) < q.get(i)) {
                    return -1;
                } else {
                    return 1;
                }
            }
        }
        if(p.dimension() <= q.dimension()) {
            if(p.dimension() < q.dimension()) {
                return -1;
            } else {
                return 0;
            }
        } else {
            return 1;
        }
    }
}
