/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private int size;
    private Node root;

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() { // is the set empty?
        return size == 0;
    }

    public int size() { // number of points in the set
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        else {
            if (size == 0) {
                root = new Node(p, Node.VERTICAL, new RectHV(0, 0, 1, 1));
                size++;
            }
            else {
                Node x = root;
                while (x != null) {
                    int cmp;
                    if (x.orientation == Node.VERTICAL) {
                        cmp = Double.compare(p.x(), x.p.x());
                    }
                    else cmp = Double.compare(p.y(), x.p.y());
                    if (cmp == 0 && p.equals(x.p)) return;
                    if (cmp == -1) {
                        if (x.lb == null) {
                            x.lb = put(x, p);
                            size++;
                            return;
                        }
                        else x = x.lb;
                    }
                    else {
                        if (x.rt == null) {
                            x.rt = put(x, p);
                            size++;
                            return;
                        }
                        else x = x.rt;
                    }
                }
            }
        }
    }

    private Node put(Node parent, Point2D p) {
        boolean orien = !parent.orientation;
        if (orien == Node.HORIZONTAL) {
            if (p.x() >= parent.p.x())
                return new Node(p, orien,
                                new RectHV(parent.p.x(), parent.rect.ymin(), parent.rect.xmax(),
                                           parent.rect.ymax()));
            else return new Node(p, orien, new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                                      parent.p.x(), parent.rect.ymax()));
        } else {
            if (p.y() >= parent.p.y())
                return new Node(p, orien,
                                new RectHV(parent.rect.xmin(), parent.p.y(), parent.rect.xmax(),
                                           parent.rect.ymax()));
            else return new Node(p, orien, new RectHV(parent.rect.xmin(), parent.rect.ymin(),
                                                      parent.rect.xmax(), parent.p.y()));
        }
    }

    public boolean contains(Point2D p) { // does the set contain point p?
        if (p == null) throw new IllegalArgumentException();
        else {
            Node x = root;
            while (x != null) {
                int cmp;
                if (x.orientation == Node.VERTICAL) {
                    cmp = Double.compare(p.x(), x.p.x());
                }
                else cmp = Double.compare(p.y(), x.p.y());
                if (cmp == 0 && p.equals(x.p)) return true;
                else if (cmp == -1) x = x.lb;
                else x = x.rt;
            }
            return false;
        }
    }


    public void draw() { // draw all points to standard draw
        Queue<Node> nodes = new Queue<>();
        nodes.enqueue(root);
        while (!nodes.isEmpty()) {
            Node node = nodes.dequeue();
            myDraw(node);
            if (node.lb != null) nodes.enqueue(node.lb);
            if (node.rt != null) nodes.enqueue(node.rt);
        }
    }

    private void myDraw(Node node) {
        if (node != null) {
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            StdDraw.point(node.p.x(), node.p.y());
            if (node.orientation == Node.VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.setPenRadius();
                StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            }
            else {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.setPenRadius();
                StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
            }
        }
    }

    // all points that are inside the rectangle (or on the boundary)
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        else {
            Stack<Point2D> points = new Stack<>();
            inorderRange(root, rect, points);
            return points;
        }
    }

    private void inorderRange(Node h, RectHV rect, Stack<Point2D> points) {
        if (h == null) return;
        if (intersects(h, rect)) {
            inorderRange(h.lb, rect, points);
            if (spans(h, rect)) points.push(h.p);
            inorderRange(h.rt, rect, points);
        }
    }

    private boolean spans(Node node, RectHV rect) {
        return node.p.x() >= rect.xmin() && node.p.x() <= rect.xmax() && node.p.y() >= rect.ymin()
                && node.p.y() <= rect.ymax();
    }

    private boolean intersects(Node node, RectHV rect) {
        boolean c1 = rect.xmin() <= node.rect.xmax() && rect.ymin() <= node.rect.ymax();
        boolean c2 = rect.xmax() >= node.rect.xmin() && rect.ymax() >= node.rect.ymin();
        return c1 && c2;
    }

    // a nearest neighbor in the set to point p; null if the set is empty
    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        else {
            if (size == 0) return null;
            else {
                Node x = root;
                Point2D nn = x.p;
                return inorderNN(x, p, nn);
            }
        }
    }

    private Point2D inorderNN(Node h, Point2D p, Point2D min) {
        if (h == null) return min;
        min = inorderNN(h.lb, p, min);
        if (h.p.distanceSquaredTo(p) < min.distanceSquaredTo(p)) min = h.p;
        min = inorderNN(h.rt, p, min);
        return min;
    }


    private static class Node {
        public static final boolean VERTICAL = true;
        public static final boolean HORIZONTAL = false;
        private final Point2D p;
        private final RectHV rect;
        private Node lb;
        private Node rt;
        private final boolean orientation;

        private Node(Point2D p, boolean orientation, RectHV rect) {
            this.p = p;
            this.orientation = orientation;
            this.rect = rect;
        }

    }

    public static void main(String[] args) {
        // Documented
        KdTree kdTree = new KdTree();
        Point2D p1 = new Point2D(0.5, 0.5);
        Point2D p2 = new Point2D(0.4, 0.4);
        Point2D p3 = new Point2D(0.6, 0.6);
        Point2D p4 = new Point2D(0.45, 0.45);
        Point2D p5 = new Point2D(0.55, 0.55);
        kdTree.insert(p1);
        kdTree.insert(p2);
        kdTree.insert(p3);
        kdTree.insert(p4);
        kdTree.insert(p5);
        System.out.println(kdTree.size());
    }
}
