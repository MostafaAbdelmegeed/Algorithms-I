/* *****************************************************************************
 *  Name:           Mostafa As'ad
 *  Date:           02-05-2020
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private static final short MIN_SIZE = 8;
    private LineSegment[] lines;
    private int tail;


    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] points) {
        if (points == null || repeatedPoint(points)) throw new IllegalArgumentException();
        Arrays.sort(points);
        lines = new LineSegment[MIN_SIZE];
        tail = 0;
        extractLines(points);
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

    private LineSegment[] slice() {
        LineSegment[] copy = new LineSegment[tail];
        for (int i = 0; i < tail; i++) {
            copy[i] = lines[i];
        }
        return copy;
    }

    // the number of line segments
    public int numberOfSegments() {
        return tail;
    }

    // the line segments
    public LineSegment[] segments() {
        return slice();
    }

    private void extractLines(Point[] points) {
        for (int i = 0; i < points.length; i += 4) {
            Point endPoint = null;
            Comparator<Point> comparator = points[i].slopeOrder();
            for (int j = i + 1; j < points.length; j++) {
                for (int u = j + 1; u < points.length; u++) {
                    if (comparator.compare(points[j], points[u]) == 0) {
                        for (int v = u + 1; v < points.length; v++) {
                            if (areCollinear(points[u], points[v], comparator)) {
                                endPoint = points[v];
                            }
                        }
                    }
                }
            }
            if (endPoint != null) {
                lines[tail++] = new LineSegment(points[i], endPoint);
                if (tail == lines.length) {
                    extend();
                }
            }
        }
    }


    private void extend() {
        LineSegment[] copy = new LineSegment[lines.length + lines.length];
        for (int i = 0; i < lines.length; i++) {
            copy[i] = lines[i];
        }
        lines = copy;
    }

    private boolean areCollinear(Point p1, Point p2, Comparator<Point> comparator) {
        return (comparator.compare(p1, p2) == 0);
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
