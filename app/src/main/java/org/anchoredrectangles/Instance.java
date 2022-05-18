/*
 * Instance specification 
 * Parsing and output to a DIMACS-like format is provided.
 */
package org.anchoredrectangles;

import javax.sound.midi.SysexMessage;

import org.anchoredrectangles.Point.Point;

public class Instance {
    /*
     * Create a new instance.
     * All points should have the same dimension and
     * be contained in the (hyper-)rectangle spanned
     * by the origin and the given corner.
     * The origin should not be included in the point array.
     */
    public Instance(Point corner, Point[] points) {
        this.corner = corner; this.points = points;
    }

    /*
     * Read in a DIMACS-like instance. See 'toString'.
     */
    public Instance(String in) {
        String lines[] = in.split("\\R");
        int j = -1;
        for(int i = 0; i < lines.length; i++) {
            if(lines[i].charAt(0) == 'c') continue;
            if(lines[i].charAt(0) == 'p') {
                String words[] = lines[i].split("\s");
                if(words[0].equals("p") && words[1].equals("llarp")) {
                    this.points = new Point[Integer.parseInt(words[2])];
                    this.corner = Point.ones(Integer.parseInt(words[3]));
                } else {
                    throw new IllegalArgumentException();
                }
            } else if(this.points != null) {
                String words[] = lines[i].split("\s");
                if(words.length != this.corner.dimension()) {
                    throw new IllegalArgumentException();
                }
                double coords[] = new double[words.length];
                for(int k = 0; k < words.length; k++) {
                    coords[k] = Double.parseDouble(words[k]);
                }
                if(j == -1) {
                    this.corner = new Point(coords);
                } else {
                    this.points[j] = new Point(coords);
                }
                j++;
            } else {
                throw new IllegalArgumentException();
            }
        }
    }

    /*
     * Return a DIMACS-like instance.
     */
    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append("c Instance for the Lower Left Anchored Rectangle Problem\n");
        s.append("c The first line gives the number of points and dimension.\n");
        s.append("c The second line gives the upper right corner of the rectangle.\n");
        s.append("c The list of points (excluding the origin) follows.\n");
        s.append("p llarp ");
        s.append(getSize()); s.append(" ");
        s.append(getDimension()); s.append("\n");
        s.append(corner.toString());
        s.append("\n");
        for(Point p : points) {
            s.append(p.toString());
            s.append("\n");
        }
        return s.toString();
    }

    public Point getCorner() {
        return corner;
    }

    public Point[] getPoints() {
        return points;
    }

    public int getDimension() {
        return corner.dimension();
    }

    public int getSize() {
        return points.length;
    }

    private Point corner;
    private Point[] points;
}
