import java.util.*;

/**
 * Missions 3 and 4. Find a word in a long text.
 */
public class Marker {

    /**
     * Mission 3a. The prefix function of p. pi[i] = the length of the longest proper prefix of p[0..i] that is
     * also a suffix of p[0..i] ("proper" means shorter than p[0..i] itself).
     * Example: p = "ACGAC" gives [0, 0, 0, 1, 2]. The last number is the "Border length" line.
     */
    public static int[] prefixFunction(String p) {
        int m = p.length();
        int[] pi = new int[m];
        int k = 0;
        
        for (int i = 1; i < m; i++) {
            while (k > 0 && p.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            if (p.charAt(i) == p.charAt(k)) {
                k++;
            }
            pi[i] = k;
        }
        return pi;
    }

    /**
     * Mission 3b. KMP. All start positions (counting from 0, in increasing order) where p occurs in text.
     * Overlapping occurrences count: "ACG" occurs in "ACGACG" at 0 and 3, and "AA" in "AAA" at 0 and 1.
     * Use the prefix function: the text pointer never goes back. The number of positions is "Marker hits",
     * the first one is "First hit" (-1 when there is none).
     */
    public static int[] findAll(String text, String p) {
        if (p.isEmpty()) return new int[0];
        
        int[] pi = prefixFunction(p);
        List<Integer> matches = new ArrayList<>();
        int n = text.length();
        int m = p.length();
        int k = 0;

        for (int i = 0; i < n; i++) {
            while (k > 0 && text.charAt(i) != p.charAt(k)) {
                k = pi[k - 1];
            }
            if (text.charAt(i) == p.charAt(k)) {
                k++;
            }
            if (k == m) {
                matches.add(i - m + 1);
                k = pi[k - 1];
            }
        }

        int[] result = new int[matches.size()];
        for (int i = 0; i < matches.size(); i++) {
            result[i] = matches.get(i);
        }
        return result;
    }

    /**
     * Mission 4. Rabin-Karp. The number of positions i in a such that the word of k letters starting at i
     * (a[i..i+k-1]) also occurs somewhere in b. Count positions, not different words. This is the "Shared words" line.
     * The idea: give every window of k letters a number (a rolling hash, updated in O(1) when the window moves by one),
     * look the number up among the windows of b, and when it is found COMPARE THE LETTERS, because two different words
     * can have the same hash.
     * Example: a = "ACGTAC", b = "TTACGT", k = 4: the words of a are ACGT, CGTA, GTAC; only ACGT is in b, so the answer is 1.
     */
    public static int sharedWords(String a, String b, int k) {
        if (a.length() < k || b.length() < k) return 0;

        long MOD = 1_000_000_007L;
        long BASE = 131L;

        long pow = 1;
        for (int i = 1; i < k; i++) {
            pow = (pow * BASE) % MOD;
        }

        Set<Long> bHashes = new HashSet<>();
        long currentHashB = 0;
        for (int i = 0; i < k; i++) {
            currentHashB = (currentHashB * BASE + b.charAt(i)) % MOD;
        }
        bHashes.add(currentHashB);

        for (int i = 1; i <= b.length() - k; i++) {
            currentHashB = (currentHashB - (b.charAt(i - 1) * pow) % MOD + MOD) % MOD;
            currentHashB = (currentHashB * BASE + b.charAt(i + k - 1)) % MOD;
            bHashes.add(currentHashB);
        }

        int sharedCount = 0;
        long currentHashA = 0;
        for (int i = 0; i < k; i++) {
            currentHashA = (currentHashA * BASE + a.charAt(i)) % MOD;
        }

        for (int i = 0; i <= a.length() - k; i++) {
            if (i > 0) {
                currentHashA = (currentHashA - (a.charAt(i - 1) * pow) % MOD + MOD) % MOD;
                currentHashA = (currentHashA * BASE + a.charAt(i + k - 1)) % MOD;
            }

            if (bHashes.contains(currentHashA)) {
                String subA = a.substring(i, i + k);
                if (existsInB(b, subA)) {
                    sharedCount++;
                }
            }
        }
        return sharedCount;
    }

    private static boolean existsInB(String b, String target) {
        int k = target.length();
        for (int j = 0; j <= b.length() - k; j++) {
            boolean match = true;
            for (int idx = 0; idx < k; idx++) {
                if (b.charAt(j + idx) != target.charAt(idx)) {
                    match = false;
                    break;
                }
            }
            if (match) return true;
        }
        return false;
    }
}