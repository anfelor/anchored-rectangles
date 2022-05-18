/*
 * N-dimensional Hyper-Rectangle
 */
package org.anchoredrectangles;

import org.anchoredrectangles.Point.Point;

class Rectangle {

    /*
     * Create a rectangle by giving two points of equal dimension.
     * We assume that ll (lower left) <= ur (upper right).
     */
    public Rectangle(Point ll, Point ur) {
        this.ll = ll; this.ur = ur;
    }

    /*
     * Check if the interiors of two rectangles intersect.
     */
    public boolean intersects(Rectangle r) {
        boolean intersects = true;
        for(int d = 0; d < ll.dimension(); d++) {
            intersects = intersects && (
                   (ll.get(d) < r.ll.get(d) && r.ll.get(d) < ur.get(d))
                || (ll.get(d) < r.ur.get(d) && r.ur.get(d) < ur.get(d))
                || (r.ll.get(d) < ll.get(d) && ll.get(d) < r.ur.get(d))
                || (r.ll.get(d) < ur.get(d) && ur.get(d) < r.ur.get(d))
            );
        }
        return intersects;
    }

    public double getSize() {
        double size = 1;
        for(int d = 0; d < ll.dimension(); d++) {
            size *= Math.abs(ll.get(d) - ur.get(d));
        }
        return size;
    }

    private Point ll;
    private Point ur;
}
