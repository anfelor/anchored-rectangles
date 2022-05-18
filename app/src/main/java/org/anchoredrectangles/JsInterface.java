/*
 * Provides an object for javascript to call the solvers.
 */

package org.anchoredrectangles;

import org.anchoredrectangles.Point.Point;

import java.nio.file.Files;

import org.anchoredrectangles.Point.NormOrder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;
import netscape.javascript.JSObject;

public class JsInterface {
    public JsInterface(Stage stage) {
      this.stage = stage;
    }

    public SolutionSet solve(String solver, JSObject pointsObj) {
        Instance in = getInstance(pointsObj);
        SolutionSet s = null;
        switch(solver) {
            case "opt":
              s = (new DynProgAlgorithm()).solve(in);
              break;
            case "dijkstra":
              s = (new DijkstraDynProgAlgorithm()).solve(in);
              break;
            case "greedy":
              s = (new GreedyAlgorithm(true, new NormOrder(1))).solve(in);
              break;
            case "tile":
              s = (new TilePackingAlgorithm(new NormOrder(1))).solve(in);
              break;
            default:
              break;
        }
        return s;
    }

    public void saveFile(JSObject pointsObj) {
        Instance in = getInstance(pointsObj);
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("LLARP Instances", "*.llarp"));
        File file = fileChooser.showSaveDialog(this.stage);
        if(file != null) {
          try {
            Files.writeString(file.toPath(), in.toString());
          } catch(IOException e) {}
        }
    }
    
    public Instance loadFile() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Load");
        fileChooser.getExtensionFilters().addAll(new ExtensionFilter("LLARP Instances", "*.llarp"));
        File file = fileChooser.showOpenDialog(this.stage);
        if(file != null) {
          try {
            String s = Files.readString(file.toPath());
            return new Instance(s);
          } catch(Exception e) {
            System.out.println(e.toString());
          }
        }
        return null;
    }

    private Instance getInstance(JSObject pointsObj) {
        Integer length = (Integer) pointsObj.getMember("length");
        int len = (int) length;
        Point[] points = new Point[len];
        for(int i = 0; i < len; i++) {
            JSObject obj = (JSObject) pointsObj.getSlot(i);
            Double x = (Double) obj.getSlot(0);
            Double y = (Double) obj.getSlot(1);
            points[i] = new Point((double) x, (double) y);
        }
        return new Instance(Point.ones(2), points);
    }

    Stage stage;
}
