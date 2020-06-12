/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private static final short MIN_SIZE = 8;
    private static final short MIN_STRIDE = 3;
    private LineSegment[] lines = new LineSegment[MIN_SIZE];
    private int tail = 0;

    // finds all line segments containing 4 or more points
    public FastCollinearPoints(Point[] points) {
        if (points == null || repeatedPoint(points)) throw new IllegalArgumentException();
        extractLines(points);
    }


    private void extractLines(Point[] unsortedPoints) {
        // First sort by natural order to maintain min-max respectivity
        Point[] nSortedPoints = unsortedPoints.clone();
        Arrays.sort(nSortedPoints);
        // Now to iterate over the sorted array
        for (int i = 0; i < nSortedPoints.length - MIN_STRIDE; i++) {
            Point origin = nSortedPoints[i];
            // Get a comparator
            Comparator<Point> c = origin.slopeOrder();
            Point[] sSortedPoints = nSortedPoints.clone();
            // Sort with respect to slopes to the origin
            Arrays.sort(sSortedPoints, i, sSortedPoints.length, c);
            double[] slopes = slopes(origin, sSortedPoints);
            int stride;
            for (int j = i + 1; j < sSortedPoints.length; j += stride) {
                stride = 1;
                while (j + stride < sSortedPoints.length
                        && c.compare(sSortedPoints[j], sSortedPoints[j + stride])
                        == 0) {
                    if (isPivotal(j, slopes)) {
                        stride++;
                    }
                    else {
                        break;
                    }
                }
                if (stride >= MIN_STRIDE) {
                    lines[tail++] = new LineSegment(origin, sSortedPoints[j + stride - 1]);
                    if (tail == lines.length) extend();
                }
            }
        }
    }

    private boolean isPivotal(int index, double[] slopes) {
        if (index == 0) return true;
        if (slopes[index - 1] == slopes[index + 1]) return false;
        double slope = slopes[index + 1];
        for (int i = 0; i < index; i++) {
            if (slopes[i] == slope) return false;
        }
        return true;
    }


    private double[] slopes(Point origin, Point[] ordered) {
        double[] slopes = new double[ordered.length];
        for (int i = 0; i < ordered.length; i++) {
            slopes[i] = origin.slopeTo(ordered[i]);
        }
        return slopes;
    }


    // the number of line segments
    public int numberOfSegments() {
        return tail;
    }

    // the line segments
    public LineSegment[] segments() {
        return slice(lines, 0, tail);
    }

    private boolean repeatedPoint(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points.length; j++) {
                if (points[i] == null || points[j] == null) throw new IllegalArgumentException();
                if (i == j) continue;
                if (points[i].compareTo(points[j]) == 0) return true;
            }
        }
        return false;
    }

    private void extend() {
        LineSegment[] copy = new LineSegment[lines.length + lines.length];
        for (int i = 0; i < lines.length; i++) {
            copy[i] = lines[i];
        }
        lines = copy;
    }

    private LineSegment[] slice(LineSegment[] a, int lo, int hi) {
        LineSegment[] copy = new LineSegment[hi - lo];
        for (int i = 0; i < copy.length; i++) {
            copy[i] = a[lo + i];
        }
        return copy;
    }

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
