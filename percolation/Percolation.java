

/* *****************************************************************************
 *  Name:   Mostafa Ahmed Asaad
 *  Date:   5/4/2020
 *  Description:    Percolation class performs the Percolation algorithm
 *  takes n as an input to construct n x n boolean grid. True means opened site
 *  False means closed site.
 *  As for Filling of the sites, it mainly depend on the connection to the top
 *  virtual node.
 *  the system percolates if and only if virtual Bot node connects with virtual
 *  top node.
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] grid;
    private WeightedQuickUnionUF weightedQuickUnionUF;
    private final int virtualTopNode;
    private final int virtualBotNode;
    private int openSitesCount;
    public Percolation(int n) {
        if (n <= 0)
            throw new IllegalArgumentException();
        else {
            weightedQuickUnionUF = new WeightedQuickUnionUF((n * n) + 2);
            grid = new boolean[n][n];
            virtualTopNode = 0;
            virtualBotNode = (n * n) + 1;
            openSitesCount = 0;
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    grid[i][j] = false;
                }
            }
        }
    }
    public void open(int row, int col) {
        if (validateIndices(row, col))
            throw new IllegalArgumentException();
        else if (!isOpen(row, col)) {
            grid[mapIndex(row)][mapIndex(col)] = true;
            connectToNeighbours(row, col);
            openSitesCount += 1;
        }
    }
    private void connectToNeighbours(int row, int col) {
            if (row == 1) {
                weightedQuickUnionUF.union(virtualTopNode, ij2i(row, col));
            } else if (row == grid.length) {
                weightedQuickUnionUF.union(virtualBotNode, ij2i(row, col));
            }
            if (row - 1 >= 1 && grid[mapIndex(row - 1)][mapIndex(col)]) {
                weightedQuickUnionUF.union(ij2i(row - 1, col), ij2i(row, col));
            }
            if (col + 1 <= grid.length && grid[mapIndex(row)][mapIndex(col + 1)]) {
                weightedQuickUnionUF.union(ij2i(row, col + 1), ij2i(row, col));
            }
            if (row + 1 <= grid.length && grid[mapIndex(row + 1)][mapIndex(col)]) {
                weightedQuickUnionUF.union(ij2i(row + 1, col), ij2i(row, col));
            }
            if (col - 1 >= 1 && grid[mapIndex(row)][mapIndex(col - 1)]) {
                weightedQuickUnionUF.union(ij2i(row, col - 1), ij2i(row, col));
            }
        }
    public boolean isOpen(int row, int col) {
        if (validateIndices(row, col))
            throw new IllegalArgumentException();
        else {
            return grid[mapIndex(row)][mapIndex(col)];
        }
    }
    public boolean isFull(int row, int col) {
        if (validateIndices(row, col))
            throw new IllegalArgumentException();
        else {
            return weightedQuickUnionUF
                    .connected(virtualTopNode, ij2i(row, col));
        }
    }
    public int numberOfOpenSites() {
        return openSitesCount;
    }
    public boolean percolates() {
        return weightedQuickUnionUF.connected(weightedQuickUnionUF.find(virtualTopNode),
                                              weightedQuickUnionUF.find(virtualBotNode)
        );
    }
    private int mapIndex(int index) {
        return index - 1;
    }
    private int ij2i(int i, int j) {
        return (i - 1) * grid.length + j;
    }
    private boolean validateIndices(int row, int col) {
        return (row < 1 || row > grid.length || col < 1 || col > grid.length);
    }
}
