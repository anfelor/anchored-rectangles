/*
 * An abstraction for the step procedures.
 */
package org.anchoredrectangles;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.BitSet;
import java.util.HashSet;

import org.anchoredrectangles.Point.LexicographicalOrder;
import org.anchoredrectangles.Point.Point;

public class DynProgStepSet implements SolutionSet {
    public DynProgStepSet(DominanceDAG dag, Instance in) {
        this.dag = dag;
        this.in = in;
        this.gs = new GreedyStepSet(in);
        this.frontier = Arrays.copyOf(dag.getIndegrees(), in.getSize());
    }

    public DynProgStepSet(DynProgStepSet old) {
        this.dag = old.dag;
        this.in = old.in;
        this.gs = new GreedyStepSet(old.gs);
        this.frontier = Arrays.copyOf(old.frontier, in.getSize());
    }

    public ArrayList<Integer> getFrontier() {
        ArrayList<Integer> res = new ArrayList();
        for(int i = 0; i < in.getSize(); i++) {
            if(this.frontier[i] == 0) {
                res.add(i);
            }
        }
        return res;
    }

    public double getCoverage() {
        return gs.getCoverage();
    }
    
    public double getWhiteSpace() {
        return gs.getWhiteSpace();
    }
    
    public Point[] getCorners() {
        return gs.getCorners();
    }

    public void stepOrigin() {
        this.gs.stepOrigin();
    }

    public BigInteger stepRoots() {
        ArrayList<Integer> roots = getFrontier();
        BigInteger stepped = BigInteger.ZERO;
        for(Integer root : roots) {
            step(root);
            stepped = stepped.setBit(root);
        }
        return stepImplieds().or(stepped);
    }

    public BigInteger stepWithImplieds(int newPoint) {
        step(newPoint);
        return stepImplieds().setBit(newPoint);
    }

    private BigInteger stepImplieds() {
        BigInteger stepped = BigInteger.ZERO;
        HashSet<Integer> tried = new HashSet();
        boolean didStep = true;

        while(didStep) {
            didStep = false;
            ArrayList<Integer> frontier = getFrontier();
            for(int i = 0; i < frontier.size(); i++) {
                int p = frontier.get(i);
                if(!tried.contains(p) && gs.hasEmptyTile(p)) {
                    step(p);
                    stepped = stepped.setBit(p);
                    didStep = true;
                    break;
                } else {
                    tried.add(p);
                }
            }
        }
        return stepped;
    }

    public void step(int newPoint) {
        this.gs.tileStep(newPoint);
        this.frontier[newPoint] = -1;
        for(Integer w : this.dag.descendants(newPoint)) {
            this.frontier[w] -= 1;
        }
    }

    Instance in;
    GreedyStepSet gs;
    DominanceDAG dag;
    int[] frontier;
}