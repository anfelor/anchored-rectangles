/*
 * Algorithm 7 from Jonathan Harder's bachelor thesis.
 * We consider the sets in a different order, but that
 * does not change the final result.
 */
package org.anchoredrectangles;

public class DynProgAlgorithmOld implements LLARPSolver {
    public GreedyStepSet solve(Instance in) {
        GreedyStepSet[] gs = new GreedyStepSet[1 << in.getSize()];
        for(int i = 0; i < in.getSize(); i++) {
            gs[1 << i] = new GreedyStepSet(in);
            gs[1 << i].step(i);
        }

        for(int i = 3; i < (1 << in.getSize()); i++) {
            GreedyStepSet best = gs[i];
            double bestCoverage = 0;
            for(int j = 0; j < in.getSize(); j++) {
                if((i & (1 << j)) != 0 && (i - (1 << j) != 0)) {
                    GreedyStepSet g = new GreedyStepSet(gs[i - (1 << j)]);
                    g.step(j);
                    if(g.getCoverage() >= bestCoverage) {
                        bestCoverage = g.getCoverage();
                        best = g;
                    }
                }
            }
            gs[i] = best;
        }

        gs[gs.length - 1].stepOrigin();
        return gs[gs.length - 1];
    }
}
