/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;

import java.util.Comparator;


public class Solver {

    private final Stack<Board> sol;
    private Board init;
    private final boolean solvable;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException("Initial board is null!");
        }
        else {
            // Parameters assignment
            init = initial;
            MinPQ<Node> minPQ = new MinPQ<>(new BoardComparator());
            sol = new Stack<>();
            // Starting with the initial board
            minPQ.insert(new Node(initial, 0, null));
            solvable = isSolvable();
            while (solvable) {
                Node currentNode = minPQ.delMin();
                Board currentBoard = currentNode.getBoard();
                if (currentBoard.isGoal()) {
                    generateSolution(currentNode);
                    break;
                }
                else {
                    for (Board neighbour : currentBoard.neighbors()) {
                        if (currentNode.getPrevNode() != null && neighbour
                                .equals(currentNode.getPrevNode().getBoard())) continue;
                        else minPQ.insert(
                                new Node(neighbour, currentNode.getMoves() + 1, currentNode));

                    }
                }
            }
        }
    }

    private void generateSolution(Node goal) {
        Node current = goal;
        sol.push(current.getBoard());
        while (!init.equals(current.getBoard())) {
            current = current.getPrevNode();
            sol.push(current.getBoard());
        }
    }

    // is the initial board solvable?
    public boolean isSolvable() {
        MinPQ<Node> originalPQ = new MinPQ<>(new BoardComparator());
        MinPQ<Node> twinPQ = new MinPQ<>(new BoardComparator());
        Node currentOriginalNode = new Node(init, 0, null);
        Node currentTwinNode = new Node(currentOriginalNode.getBoard().twin(), 0,
                                        null);
        originalPQ.insert(currentOriginalNode);
        twinPQ.insert(currentTwinNode);
        while (!originalPQ.isEmpty() && !twinPQ.isEmpty()) {
            currentOriginalNode = originalPQ.delMin();
            currentTwinNode = twinPQ.delMin();
            Board currentOriginalBoard = currentOriginalNode.getBoard();
            Board currentTwinBoard = currentTwinNode.getBoard();
            if (currentOriginalBoard.isGoal()) {
                return true;
            }
            if (currentTwinBoard.isGoal()) {
                return false;
            }
            for (Board neighbour : currentOriginalBoard.neighbors()) {
                if (currentOriginalNode.getPrevNode() != null && neighbour
                        .equals(currentOriginalNode.getPrevNode().getBoard())) {
                    continue;
                }
                else originalPQ.insert(new Node(neighbour, currentOriginalNode.getMoves() + 1, currentOriginalNode));

            }
            for (Board neighbour : currentTwinBoard.neighbors()) {
                if (currentTwinNode.getPrevNode() != null && neighbour
                        .equals(currentTwinNode.getPrevNode().getBoard())) continue;
                else twinPQ.insert(new Node(neighbour, currentTwinNode.getMoves() + 1, currentTwinNode));
            }
        }
        return false;
    }

    // min number of moves to solve initial board
    public int moves() {
        if (solvable) {
            return sol.size() - 1;
        }
        else return -1;
    }

    // sequence of boards in a shortest solution
    public Iterable<Board> solution() {
        if (solvable) {
            return sol;
        }
        else return null;
    }

    private class BoardComparator implements Comparator<Node> {
        public int compare(Node o1, Node o2) {
            if (o1.getManPriority() > o2.getManPriority()) return 1;
            if (o1.getManPriority() < o2.getManPriority()) return -1;
            if (o1.getBoard().manhattan() > o2.getBoard().manhattan()) return 1;
            if (o1.getBoard().manhattan() < o2.getBoard().manhattan()) return -1;
            return Integer.compare(o1.getBoard().hamming(), o2.getBoard().hamming());
        }
    }

    private class Node {
        private final Board board;
        private final int manPriority;
        private final Node prevNode;
        private final int moves;

        public Node(Board board, int moves, Node prevNode) {
            this.board = board;
            this.moves = moves;
            this.manPriority = board.manhattan() + moves;
            this.prevNode = prevNode;
        }

        public int getMoves() {
            return moves;
        }

        public Node getPrevNode() {
            return this.prevNode;
        }

        public Board getBoard() {
            return board;
        }

        public int getManPriority() {
            return manPriority;
        }
    }

    public static void main(String[] args) {
        int[][] tiles = new int[3][3];
        tiles[0][0] = 1;
        tiles[0][1] = 2;
        tiles[0][2] = 3;
        tiles[1][0] = 0;
        tiles[1][1] = 7;
        tiles[1][2] = 6;
        tiles[2][0] = 5;
        tiles[2][1] = 4;
        tiles[2][2] = 8;
        Board myBoard = new Board(tiles);
        Solver solver = new Solver(myBoard);
        Iterable<Board> solution = solver.solution();
        for (Board sol : solution) {
            System.out.println(sol.toString());
        }
        System.out.println(solver.moves());
    }
}
