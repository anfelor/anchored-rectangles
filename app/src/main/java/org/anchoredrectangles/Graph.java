/*
 * A simple directed graph datastructure.
 */
package org.anchoredrectangles;

import java.util.ArrayList;
import java.util.HashSet;

public class Graph {
    ArrayList<HashSet<Integer>> nodes; // descendants for each node.
    int edges;

    public Graph(Graph g) {
        this.nodes = g.nodes;
        this.edges = g.edges;
    }

    public Graph(int n) {
        this.nodes = new ArrayList(n);
        for(int i = 0; i < n; i++) {
            this.nodes.add(new HashSet<Integer>());
        }
        this.edges = 0;
    }

    public void addEdge(int v, int w) {
        this.nodes.get(v).add((Integer) w);
        this.edges += 1;
    }

    public boolean hasEdge(int v, int w) {
        return this.nodes.get(v).contains((Integer) w);
    }

    public void removeEdge(int v, int w) {
        this.nodes.get(v).remove((Integer) w);
        this.edges -= 1;
    }

    public int degree(int v) {
        return this.nodes.get(v).size();
    }

    public HashSet<Integer> descendants(int v) {
        return this.nodes.get(v);
    }
}
