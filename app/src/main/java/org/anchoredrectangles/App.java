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
        // timeSolver(11000, 10, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy");
        // timeSolver(80, 1, new DynProgAlgorithm(), "DynProg");
        // timeSolver(90, 1, new DijkstraDynProgAlgorithm(), "DijkstraDynProg");
        // showNormPerformance();
        // findBadInstance(10000, "../instances/worst/tile5.llarp", new TilePackingAlgorithm(new NormOrder(1)));

        // timeSolver(10, 100, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy 10");
        // timeSolver(50, 100, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy 50");
        // timeSolver(100, 100, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy 100");
        // timeSolver(500, 100, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy 500");
        // timeSolver(1000, 100, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy 1000");
        // timeSolver(5000, 100, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy 5000");
        // timeSolver(10000, 100, new GreedyAlgorithm(false, new NormOrder(1)), "Greedy 10000");

        // Instance in = UniformGenerator.generate(25, new Point(1,1));
        // SolutionSet s1 = (new DijkstraDynProgAlgorithm()).solve(in);
        // SolutionSet s2 = (new DynProgAlgorithm()).solve(in);
        // System.out.println(s1.getCoverage());
        // System.out.println(s2.getCoverage());
        // System.out.println(in.toString());
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
            // System.out.print(timings[i]);
        }
        // System.out.println(name + " (ms):" + (sum / tries));
    }

    public static void findBadInstance(int tries, String filename, LLARPSolver solver) {
        double cworst = 2;
        Instance iworst = null;
        for(int i = 0; i < tries; i++) {
            Instance in = UniformGenerator.generate(20, new Point(1,1));
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