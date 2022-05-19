/*
 * The dominance DAG
 */
package org.anchoredrectangles;

import java.util.HashSet;

import org.anchoredrectangles.Point.Point;

public class DominanceDAG {
    Graph g;
    int[] indegree;

    public DominanceDAG(Instance in) throws IllegalArgumentException {
        if(in.hasDuplicates()) {
            throw new IllegalArgumentException("Invalid instance: contains duplicated points!");
        }

        this.g = new Graph(in.getSize());
        this.indegree = new int[in.getSize()];
        for(int i = 0; i < in.getSize(); i++) {
            this.indegree[i] = 0;
        }

        Graph gi = new Graph(in.getSize());
        for(int i = 0; i < in.getSize(); i++) {
            Point p = in.getPoints()[i];
            for(int j = 0; j < in.getSize(); j++) {
                Point q = in.getPoints()[j];
                if(p.get(0) <= q.get(0) && p.get(1) <= q.get(1) && p != q) {
                    gi.addEdge(i, j);
                }
            }
        }

        for(int u = 0; u < in.getSize(); u++) {
            for(Integer w : gi.descendants(u)) {
                boolean shouldAdd = true;
                for(int v = 0; v < in.getSize(); v++) {
                    if(gi.hasEdge(u, v) && gi.hasEdge(v, w)) {
                        shouldAdd = false;
                        break;
                    }
                }
                if(shouldAdd) {
                    g.addEdge(w, u);
                    this.indegree[u] += 1;
                }
            }
        }
    }

    public int[] getIndegrees() {
        return this.indegree;
    }

    public HashSet<Integer> descendants(int v) {
        return this.g.descendants(v);
    }
}