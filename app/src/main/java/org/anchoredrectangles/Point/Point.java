/*
 * N-dimensional Points
 */
package org.anchoredrectangles.Point;

public class Point {
    private double[] data;
    public Point (double... members) { this.data = members; }
    public Point (Point p) {
        this.data = new double[p.data.length];
        System.arraycopy(p.data, 0, this.data, 0, p.data.length);
    }
    public double get(int index) { return data[index]; }
    public void set(int index, double val) { data[index] = val; }
    public int dimension() { return data.length; }

    public static Point origin(int dim) {
        double point[] = new double[dim];
        for(int i = 0; i < dim; i++) {
            point[i] = 0;
        }
        return new Point(point);
    }
    
    public static Point ones(int dim) {
        double point[] = new double[dim];
        for(int i = 0; i < dim; i++) {
            point[i] = 1;
        }
        return new Point(point);
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        if(this.data.length > 0) {
            for(int i = 0; i + 1 < this.data.length; i++) {
                sb.append(this.data[i]); sb.append(" ");
            }
            sb.append(this.data[this.data.length - 1]);
        }
        return sb.toString();
    }
}
