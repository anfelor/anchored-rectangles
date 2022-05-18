/*
 * Algorithm 7 from Jonathan Harder's bachelor thesis.
 * We consider the sets in a different order, but that
 * does not change the final result.
 */
package org.anchoredrectangles;

import java.math.BigInteger;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.ArrayList;
import java.util.Map;
import java.util.PriorityQueue;

public class DijkstraDynProgAlgorithm implements LLARPSolver {
    private double[] lowerBounds;
    private Instance in;

    public DynProgStepSet solve(Instance in) {
        DominanceDAG dag = new DominanceDAG(in);
        BigInteger all = BigInteger.ZERO;
        for(int i = 0; i < in.getSize(); i++) {
            all = all.setBit(i);
        }

        this.in = in;
        this.lowerBounds = new double[in.getSize()];
        GreedyStepSet lowerBoundSet = new GreedyStepSet(in);
        for(int i = 0; i < in.getSize(); i++) {
            lowerBounds[i] = lowerBoundSet.lowerBoundWhitespace(i);
        }

        DynProgStepSet initialPacking = new DynProgStepSet(dag, in);
        BigInteger initialSet = initialPacking.stepRoots();
        initialPacking.stepOrigin();

        PriorityQueue<PQTuple> heap = new PriorityQueue();
        TreeMap<BigInteger, DynProgStepSet> map = new TreeMap(); 
        TreeSet<BigInteger> deleted = new TreeSet<>();

        heap.add(new PQTuple(initialSet, initialPacking.getWhiteSpace() + lowerBound(initialSet)));
        map.put(initialSet, initialPacking);

        while(true) {
            PQTuple t = heap.poll();
            DynProgStepSet d = map.get(t.key);
            if(d == null) continue; // already considered t.key
            if(t.key.equals(all)) break;
            map.remove(t.key);
            deleted.add(t.key);
            for(Integer n : d.getFrontier()) {
                DynProgStepSet s = new DynProgStepSet(d);
                BigInteger stepped = s.stepWithImplieds(n);
                BigInteger b = stepped.or(t.key);
                DynProgStepSet old = map.get(b);
                if(old == null || old.getWhiteSpace() > s.getWhiteSpace()) {
                    if(!deleted.contains(b)) {
                        map.put(b, s);
                        heap.add(new PQTuple(b, s.getWhiteSpace() + lowerBound(b)));
                    }
                }
            }
        }

        DynProgStepSet result = map.get(all);
        return result;
    }

    private double lowerBound(BigInteger b) {
        double whitespace = 0;
        for(int i = 0; i < this.in.getSize(); i++) {
            if(!b.testBit(i)) {
                whitespace += lowerBounds[i];
            }
        }
        return whitespace;
    }
}

class PQTuple implements Comparable<PQTuple> {
    public BigInteger key;
    public double whitespace;

    public PQTuple(BigInteger key, double whitespace) {
        this.key = key;
        this.whitespace = whitespace;
    }

    public int compareTo(PQTuple t) {
        if(whitespace == t.whitespace) {
            return 0;
        } else if(whitespace < t.whitespace) {
            return -1;
        } else {
            return 1;
        }
    }

    public boolean equals(Object o) {
        if(o instanceof PQTuple) {
            PQTuple p = (PQTuple) o;
            return key.equals(p.key) && whitespace == p.whitespace;
        } else {
            return false;
        }
    }
}