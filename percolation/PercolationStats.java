/* *****************************************************************************
 *  Name:   Mostafa Asaad
 *  Date:   10/4/2020
 *  Dependencies: Percolation.java
 *  Description:
 *      This program does the statistics for your Percolation trial runs
 *      it takes the n and the trials as arguments from the command line
 *      and does number of test runs = trials on a n x n grid.
 *      calculates the mean, standard deviation and the limits of
 *      95% confidence level.
 *
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private double[] runs;

    public PercolationStats(int n, int trials) {
        if (n <= 0 || trials <= 0) {
            throw new IllegalArgumentException();
        }
        else {
            runs = new double[trials];
            for (int i = 0; i < trials; i++) {
                Percolation perc = new Percolation(n);
                while (!perc.percolates()) {
                    perc.open(StdRandom.uniform(1, n+1), StdRandom.uniform(1, n+1));
                }
                runs[i] = (double) perc.numberOfOpenSites() / (n * n);
            }
        }
    }

    public double mean() {
        return StdStats.mean(runs);
    }

    public double stddev() {
        return StdStats.stddev(runs);
    }

    public double confidenceLo() {
        return StdStats.mean(runs) - 1.96 * (StdStats.stddev(runs)/Math.sqrt(runs.length));
    }

    public double confidenceHi() {
        return StdStats.mean(runs) + 1.96 * (StdStats.stddev(runs)/Math.sqrt(runs.length));
    }

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println(String.format("mean\t= %f", percolationStats.mean()));
        System.out.println(String.format("stddev\t= %f", percolationStats.stddev()));
        System.out.println(
                String.format("95%% confidence interval = [%f, %f]",
                              percolationStats.confidenceLo(),
                              percolationStats.confidenceHi()));
    }
}
