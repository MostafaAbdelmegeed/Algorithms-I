/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

import java.util.ArrayList;

public class PointSET {

    private final SET<Point2D> set;

    // construct an empty set of points
    public PointSET() {
        set = new SET<>();
    }

    // is the set empty?
    public boolean isEmpty() {
        return set.isEmpty();
    }

    // number of points in the set
    public int size() {
        return set.size();
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        else set.add(p);
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        else return set.contains(p);

    }

    // draw all points to standard draw
    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        else {
            ArrayList<Point2D> points = new ArrayList<>();
            for (Point2D p : set) {
                if (p.x() <= rect.xmax() && p.x() >= rect.xmin() && p.y() >= rect.ymin()
                        && p.y() <= rect.ymax()) points.add(p);
            }
            return points;
        }
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        else {
            if (isEmpty()) return null;
            else {
                double min = Double.POSITIVE_INFINITY;
                Point2D nearest = set.min();
                for (Point2D point : set) {
                    double distance = point.distanceSquaredTo(p);
                    if (distance < min) {
                        min = distance;
                        nearest = point;
                    }
                }
                return nearest;
            }
        }
    }

    public static void main(String[] args) {
        // Doumented!
    }
}
