/*
 * Algorithm 1 from Jonathan Harder's bachelor thesis.
 */
package org.anchoredrectangles;

import java.util.ArrayList;
import java.util.List;

import org.anchoredrectangles.Point.Point;

import java.util.Collections;
import java.util.Comparator;

public class GreedyAlgorithm implements LLARPSolver {
    private boolean deterministic;
    private Comparator<Point> comp;

    public GreedyAlgorithm(boolean deterministic, Comparator<Point> comp) {
        this.deterministic = deterministic;
        this.comp = comp;
    }

    public GreedyStepSet solve(Instance in) {
        GreedyStepSet g = new GreedyStepSet(in);
        List<Integer> p = new ArrayList<Integer>();
        for(int i = 0; i < in.getSize(); i++) {
            p.add(i);
        }
        Comparator<Point> comp = this.comp;
        if(deterministic) {
            Collections.sort(p, new Comparator<Integer>() {
                public int compare(Integer i, Integer j) {
                    return comp.compare(in.getPoints()[j], in.getPoints()[i]);
                }
            });
        } else {
            Collections.shuffle(p);
        }
        for(Integer i : p) {
            g.step(i);
        }
        g.stepOrigin();
        return g;
    }
}
