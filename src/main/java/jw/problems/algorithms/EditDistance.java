package jw.problems.algorithms;

public class EditDistance {

    public static void main(String[] args) {
        System.out.println(editDistance("xy", "xabcy"));
    }

    public static final int SUBSTITUTION_COST = 1;
    public static final int INSERT_COST = 1;
    public static final int DELETE_COST = 1;

    // grid[i][j] = cost to move from s1.substring(0..i) to s2.substring(0..j)
    public static int editDistance(String s1, String s2) {
        if (s1 == null || s2 == null)
            return Integer.MAX_VALUE;
        int[][] grid = new int[s1.length() + 1][s2.length() + 1];
        for (int i = 0; i < grid.length; i++)
            for (int j = 0; j < grid[i].length; j++)
                grid[i][j] = 0;

        for (int i = 1; i < grid.length; i++)
            grid[i][0] = i * DELETE_COST;
        for (int j = 1; j < grid[0].length; j++)
            grid[0][j] = j * INSERT_COST;

        for (int i = 1; i < grid.length; i++)
            for (int j = 1; j < grid[i].length; j++)
                if (s1.charAt(i - 1) == s2.charAt(j - 1))
                    grid[i][j] = grid[i - 1][j - 1];
                else {
                    int v1 = grid[i - 1][j - 1] + SUBSTITUTION_COST;
                    int v2 = grid[i][j - 1] + INSERT_COST;
                    int v3 = grid[i - 1][j] + DELETE_COST;
                    grid[i][j] = Math.min(v1, Math.min(v2, v3));
                }
        return grid[s1.length()][s2.length()];
    }

}
