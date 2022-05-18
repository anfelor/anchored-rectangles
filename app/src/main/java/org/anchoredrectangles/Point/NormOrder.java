/*
 * Order by distance in a p-norm.
 */
package org.anchoredrectangles.Point;

import java.util.Comparator;

public class NormOrder implements Comparator<Point> {
    double norm = 1;
    public NormOrder(double p) {
        this.norm = p;
    }
    @Override
    public int compare(Point p, Point q) {
        double sumP = 0;
        for(int i = 0; i < p.dimension(); i++) {
            sumP += Math.pow(p.get(i), norm);
        }
        double sumQ = 0;
        for(int i = 0; i < q.dimension(); i++) {
            sumQ += Math.pow(q.get(i), norm);
        }
        return Double.compare(sumP, sumQ);
    }
}
