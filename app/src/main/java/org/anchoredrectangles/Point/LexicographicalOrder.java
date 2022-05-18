/*
 * Lexicographical ordering
 */
package org.anchoredrectangles.Point;

import java.util.Comparator;

public class LexicographicalOrder implements Comparator<Point> {
    @Override
    public int compare(Point p, Point q) {
        return new AxisOrder(0).compare(p, q);
    }
}
