package org.anchoredrectangles;

import org.anchoredrectangles.Point.Point;

interface SolutionSet {
    public Point[] getCorners();
    public double getCoverage();
    public double getWhiteSpace();
}
