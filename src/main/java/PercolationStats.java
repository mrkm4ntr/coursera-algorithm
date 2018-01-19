import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private final int n;
    private final double sqrtT;
    private final double[] fractions;
    private double mean = -1;
    private double stddev = -1;

    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        sqrtT = Math.sqrt(trials);
        fractions = new double[trials];
        for (int i = 0; i < trials; i++) {
            fractions[i] = ((double) trial()) / (n * n);
        }
    }

    private class Cell {
        int row;
        int col;
        public Cell(int row, int col) {
            this.row = row;
            this.col = col;
        }
    }

    private int trial() {
        Percolation p = new Percolation(n);
        Cell[] cells = new Cell[n * n];
        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < n + 1; j++) {
                cells[(i - 1) * n + (j - 1)] = new Cell(i, j);
            }
        }
        StdRandom.shuffle(cells);
        int i;
        for (i = 0; !p.percolates(); i++) {
            Cell cell = cells[i];
            p.open(cell.row, cell.col);
        }
        return i;
    }

    public double mean() {
        if (mean < 0) {
            mean = StdStats.mean(fractions);
        }
        return mean;
    }

    public double stddev() {
        if (stddev < 0) {
            stddev = StdStats.stddev(fractions);
        }
        return stddev;
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / sqrtT;
    }

    public double confidenceHi() {
        return mean() + 1.96d * stddev() / sqrtT;
    }

    public static void main(String[] args) {
        PercolationStats ps = new PercolationStats(200, 100);
        System.out.println("mean=" + ps.mean());
        System.out.println("stddev=" + ps.stddev());
        System.out.println("95% confidence interval=[" + ps.confidenceLo() + ", " + ps.confidenceHi() + "]");
    }
}
