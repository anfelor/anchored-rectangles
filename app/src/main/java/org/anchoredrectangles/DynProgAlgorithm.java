/*
 * Algorithm 7 from Jonathan Harder's bachelor thesis.
 * We consider the sets in a different order, but that
 * does not change the final result.
 */
package org.anchoredrectangles;

import java.math.BigInteger;
import java.util.TreeMap;

import java.util.ArrayList;
import java.util.Map;

public class DynProgAlgorithm implements LLARPSolver {
    public DynProgStepSet solve(Instance in) {
        DominanceDAG dag = new DominanceDAG(in);
        BigInteger all = BigInteger.ZERO;
        for(int i = 0; i < in.getSize(); i++) {
            all = all.setBit(i);
        }

        DynProgStepSet initialPacking = new DynProgStepSet(dag, in);
        BigInteger initialSet = initialPacking.stepRoots();
        initialPacking.stepOrigin();
        
        ArrayList<TreeMap<BigInteger, DynProgStepSet>> subsets = new ArrayList<>();
        for(int i = 0; i < in.getSize(); i++) {
            subsets.add(new TreeMap());
        }

        int current = 0;
        subsets.get(current).put(initialSet, initialPacking);

        while(subsets.get(current).size() != 1 || !subsets.get(current).firstKey().equals(all)) {
            for(Map.Entry<BigInteger,DynProgStepSet> entry : subsets.get(current).entrySet()) {
                for(Integer n : entry.getValue().getFrontier()) {
                    DynProgStepSet s = new DynProgStepSet(entry.getValue());
                    BigInteger stepped = s.stepWithImplieds(n);
                    BigInteger b = stepped.or(entry.getKey());
                    int next = current + countSetBits(stepped);
                    DynProgStepSet old = subsets.get(next).get(b);
                    if(old == null || old.getCoverage() < s.getCoverage()) {
                        subsets.get(next).put(b, s);
                    }
                }
            }
            subsets.get(current).clear();
            current++;
        }

        DynProgStepSet result = subsets.get(current).get(all);
        return result;
    }

    // Brian Kernighan’s algorithm
    private int countSetBits(BigInteger b) {
        int set = 0;
        while(!b.equals(BigInteger.ZERO)) {
            b = b.and(b.subtract(BigInteger.ONE));
            set++;
        }
        return set;
    }
}
