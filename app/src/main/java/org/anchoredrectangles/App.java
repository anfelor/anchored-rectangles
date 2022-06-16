/*
 * Main application point
 */
package org.anchoredrectangles;

import javax.swing.*;

import java.nio.file.Files;
import java.io.File;
import java.io.IOException;
import java.time.*;

import org.anchoredrectangles.Point.MaxOrder;
import org.anchoredrectangles.Point.MinOrder;
import org.anchoredrectangles.Point.NormOrder;
import org.anchoredrectangles.Point.Point;

public class App {

    public static void main(String[] args) {
        DraggableGui.main(args);
        // doComparisons(500000);
        // timeSolver(11000, 10, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy");
        // timeSolver(80, 1, new DynProgAlgorithm(), "DynProg");
        // timeSolver(90, 1, new DijkstraDynProgAlgorithm(), "DijkstraDynProg");
        // showNormPerformance();
        // findBadInstance(10, 100000, "../instances/worst/optimal10.llarp", new DynProgAlgorithm());

        // testCoverage();
    }

    public static void testCoverage() {
        int tries = 100; int n = 5;
        int start = 100; int end = 1001;
        double[][] sums = new double[end][n];
        for(int i = start; i < end; i += 10) {
            for(int j = 0; j < n; j++) { sums[i][j] = 0; }
            for(int j = 0; j < tries; j++) {
                Instance in = UniformGenerator.generate(i, new Point(1,1));
                sums[i][0] += (new GreedyAlgorithm(true, new NormOrder(0.7))).solve(in).getCoverage();
                sums[i][1] += (new GreedyAlgorithm(true, new NormOrder(0.8))).solve(in).getCoverage();
                sums[i][2] += (new GreedyAlgorithm(true, new NormOrder(0.9))).solve(in).getCoverage();
                sums[i][3] += (new GreedyAlgorithm(true, new NormOrder(1))).solve(in).getCoverage();
                sums[i][4] += (new GreedyAlgorithm(true, new NormOrder(1.1))).solve(in).getCoverage();
            }
        }
        for(int j = 0; j < n; j++) {
            System.out.println("Solver: " + j + ", ");
            for(int i = start; i < end; i += 10) {
                System.out.println("(" + i + ", " + (sums[i][j] / tries) + ")");
            }
            System.out.println("-----");
        }
    }

    public static void printSolution(Instance in, SolutionSet s) {
        for(int i = 0; i < in.getSize(); i++) {
            System.out.println(in.getPoints()[i]);
            System.out.println(s.getCorners()[i]);
        }
    }

    public static double averageCoverage(int n, int tries, LLARPSolver solver) {
        double sum = 0;
        for(int i = 0; i < tries; i++) {
            Instance in = UniformGenerator.generate(n, new Point(1,1));
            SolutionSet s = solver.solve(in);
            sum += s.getCoverage();
        }
        return sum / tries;
    }

    public static void timeSolver(int n, int tries, LLARPSolver solver, String name) {
        long sum = 0;
        System.out.println(name + ": ");
        for(int i = 0; i < tries; i++) {
            Instance in = UniformGenerator.generate(n, new Point(1,1));
            Instant before = Instant.now();
            solver.solve(in);
            Instant after = Instant.now();
            sum += Duration.between(before, after).toMillis();
            System.out.println(Duration.between(before, after).toMillis());
        }
        // System.out.println(name + " (ms):" + (sum / tries));
    }

    public static void findBadInstance(int points, int tries, String filename, LLARPSolver solver) {
        double cworst = 2;
        Instance iworst = null;
        for(int i = 0; i < tries; i++) {
            Instance in = UniformGenerator.generate(points, new Point(1,1));
            SolutionSet s = solver.solve(in);
            if(s.getCoverage() <= cworst) {
                cworst = s.getCoverage();
                iworst = in;
            }
        }
        try {
            System.out.println("Worst coverage found: "+ cworst);
            Files.writeString((new File(filename)).toPath(), iworst.toString());
        } catch(Exception e) {}
    }
    
    public static void doComparisons(int tries) {
        Instance[][] comparisons = new Instance[4][4];
        for(int n = 0; n < tries; n++) {
            Instance in = UniformGenerator.generate(10, new Point(1,1));
            double[] s = new double[4];
            s[0] = (new GreedyAlgorithm(true, new NormOrder(1))).solve(in).getCoverage();
            s[1] = (new GreedyAlgorithm(true, new NormOrder(2))).solve(in).getCoverage();
            s[2] = (new GreedyAlgorithm(true, new MinOrder())).solve(in).getCoverage();
            s[3] = (new GreedyAlgorithm(true, new MaxOrder())).solve(in).getCoverage();
            for(int i = 0; i < 4; i++) {
                for(int j = 0; j < 4; j++) {
                    if(s[i] > s[j] + 0.1)  {
                        comparisons[i][j] = in;
                    }
                }
            }
        }
        int count = 0;
        for(int i = 0; i < 4; i++) {
            for(int j = 0; j < 4; j++) {
                if(comparisons[i][j] != null) {
                    count++;
                    System.out.println(i + " is better than " + j + " on the instance:\n" + comparisons[i][j].toString());
                }
            }
        }
        System.out.println(count);
    }

    public static void showNormPerformance() {
        double av = averageCoverage(100, 1000, new GreedyAlgorithm(true, new MinOrder()));
        System.out.println("-infty: " + av);
        for(int i = 1; i <= 20; i++) {
            double p = ((double) i) / 10;
            av = averageCoverage(100, 1000, new GreedyAlgorithm(true, new NormOrder(p)));
            System.out.println(p + ": " + av);
        }
        for(int i = 3; i <= 10; i++) {
            double p = ((double) i);
            av = averageCoverage(100, 1000, new GreedyAlgorithm(true, new NormOrder(p)));
            System.out.println(p + ": " + av);
        }
        av = averageCoverage(100, 1000, new GreedyAlgorithm(true, new MaxOrder()));
        System.out.println("infty: " + av);
    }
}
