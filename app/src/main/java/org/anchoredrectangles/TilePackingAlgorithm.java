/*
 * The Tile Packing Algorithm.
 */
package org.anchoredrectangles;

import java.util.ArrayList;
import java.util.List;

import org.anchoredrectangles.Point.Point;

import java.util.Collections;
import java.util.Comparator;

public class TilePackingAlgorithm implements LLARPSolver {
    private Comparator<Point> comp;

    public TilePackingAlgorithm(Comparator<Point> comp) {
        this.comp = comp;
    }

    public GreedyStepSet solve(Instance in) {
        GreedyStepSet g = new GreedyStepSet(in);
        List<Integer> p = new ArrayList<Integer>();
        for(int i = 0; i < in.getSize(); i++) {
            p.add(i);
        }
        Comparator<Point> comp = this.comp;
        Collections.sort(p, new Comparator<Integer>() {
            public int compare(Integer i, Integer j) {
                return comp.compare(in.getPoints()[j], in.getPoints()[i]);
            }
        });
        for(Integer i : p) {
            g.tileStep(i);
        }
        g.stepOrigin();
        return g;
    }
}
