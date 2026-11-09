import java.util.*;

/**
 * Mission 5. Dynamic programming on the tree of infections. Root the tree in person 0.
 * Input.Tree.neighbours() gives the neighbours of every person. The number of people is at most about 100, so a recursive DFS is fine here.
 */
public class Outbreak {

    /**
     * Mission 5a. Choose people to test so that for every link (somebody infected somebody) at least one of the two is tested,
     * and the total cost of testing is as small as possible. cost[v] is the price of testing person v.
     * Returns tested[v] = true if person v is tested.
     *
     * State for every person v (looking only at v and everything below it): the smallest cost when v IS tested, and
     * the smallest cost when v is NOT tested. What must then be true about the children of v in each case?
     * Do not forget to rebuild the answer (who exactly is tested), not only the cost: the picture needs it.
     * Main calls this twice: with all costs equal to 1 (the line "Tests needed") and with the real costs ("Cheapest testing").
     */
    public static boolean[] cheapestTests(int n, int[][] nb, int[] cost) {
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        List<Integer> order = new ArrayList<>();
        Queue<Integer> q = new LinkedList<>();

        q.add(0);
        parent[0] = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : nb[u]) {
                if (parent[v] == -1) {
                    parent[v] = u;
                    q.add(v);
                }
            }
        }

        long[] take = new long[n];
        long[] skip = new long[n];

        for (int i = order.size() - 1; i >= 0; i--) {
            int u = order.get(i);
            take[u] += cost[u];
            if (u != 0) {
                int p = parent[u];
                take[p] += Math.min(take[u], skip[u]);
                skip[p] += take[u];
            }
        }

        boolean[] tested = new boolean[n];
        tested[0] = take[0] <= skip[0];
        for (int i = 1; i < order.size(); i++) {
            int u = order.get(i);
            int p = parent[u];
            if (!tested[p]) {
                tested[u] = true;
            } else {
                tested[u] = take[u] <= skip[u];
            }
        }

        return tested;
    }

    /**
     * Mission 5b. The number of links on the longest chain of infections, that is the longest path in the tree (counted in links).
     * A path goes up from one person and down to another; for every person you need the two longest ways down.
     * Example: a line 0-1-2-3 has chain 3; a star with centre 0 and leaves 1, 2, 3 has chain 2.
     */
    public static int longestChain(int n, int[][] nb) {
        int[] d1 = new int[n]; 
        int[] d2 = new int[n];
        int[] parent = new int[n];
        Arrays.fill(parent, -1);
        List<Integer> order = new ArrayList<>();
        Queue<Integer> q = new LinkedList<>();

        q.add(0);
        parent[0] = 0;
        while (!q.isEmpty()) {
            int u = q.poll();
            order.add(u);
            for (int v : nb[u]) {
                if (parent[v] == -1) {
                    parent[v] = u;
                    q.add(v);
                }
            }
        }

        int maxChain = 0;
        for (int i = order.size() - 1; i >= 0; i--) {
            int u = order.get(i);
            maxChain = Math.max(maxChain, d1[u] + d2[u]);
            if (u != 0) {
                int p = parent[u];
                int h = d1[u] + 1;
                if (h > d1[p]) {
                    d2[p] = d1[p];
                    d1[p] = h;
                } else if (h > d2[p]) {
                    d2[p] = h;
                }
            }
        }

        return maxChain;
    }
}