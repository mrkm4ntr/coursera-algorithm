public class Percolation {

    private static final int TOP = 0;
    private static final int OFFSET = 1;
    private final int[] sites;
    private final boolean[] opened;
    private final int[] sizes;
    private final int n;

    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException();
        }
        this.n = n;
        sites = new int[n * n + OFFSET];
        sizes = new int[n * n + OFFSET];
        opened = new boolean[n * n + OFFSET];
        for (int i = 0; i < sites.length; i++) {
            sites[i] = i;
            sizes[i] = 1;
            opened[i] = false;
        }
        opened[TOP] = true;
    }

    private int[] candidates(int row, int col) {
        int[] c = {-1, -1, -1, -1};
        int i = 0;
        if (row > 1) {
            if (isOpen(row - 1, col)) {
                c[i++] = index(row - 1, col);
            }
        } else {
            c[i++] = TOP;
        }
        if (row < n) {
            if (isOpen(row + 1, col)) {
                c[i++] = index(row + 1, col);
            }
        }
        if (col > 1 && isOpen(row, col - 1)) {
            c[i++] = index(row, col - 1);
        }
        if (col < n && isOpen(row, col + 1)) {
            c[i++] = index(row, col + 1);
        }
        return c;
    }

    private int index(int row, int col) {
        return (row - 1) * n + (col - 1) + OFFSET;
    }

    private int parent(int index) {
        int i = index;
        while (sites[i] != i) {
            i = parent(sites[i]);
        }
        return i;
    }

    private int max(int a, int b) {
        if (a > b) {
            return a;
        }
        return b;
    }

    private void validate(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException();
        }
    }


    public void open(int row, int col) {
        validate(row, col);
        opened[index(row, col)] = true;
        for (int i : candidates(row, col)) {
            if (i < 0) {
                continue;
            }
            int left = parent(index(row, col));
            int lSize = sizes[left];
            int right = parent(i);
            int rSize = sizes[right];
            if (lSize > rSize) {
                sites[right] = left;
                sizes[left] = max(lSize, rSize + 1);
            } else {
                sites[left] = right;
                sizes[right] = max(rSize, lSize + 1);
            }
        };
    }

    public boolean isOpen(int row, int col) {
        validate(row, col);
        return opened[index(row, col)];
    }

    public boolean isFull(int row, int col) {
        validate(row, col);
        return isOpen(row, col) && parent(index(row, col)) == parent(TOP);
    }

    public int numberOfOpenSites() {
        int count = 0;
        for (boolean b : opened) {
            if (b) {
                count++;
            }
        }
        return count - OFFSET;
    }

    public boolean percolates() {
        for (int i = 1; i <= n; i++) {
            if (parent(TOP) == parent(index(n, i))) {
                return true;
            }
        }
        return false;
    }

    public static void main(String[] args) {
        System.out.println(new Percolation(5).isFull(1, 1));
    }
}
