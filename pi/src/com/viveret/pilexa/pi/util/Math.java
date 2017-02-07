package com.viveret.pilexa.pi.util;

/**
 * Created by viveret on 2/4/17.
 */
public class Math {
    // using LevenshteinDistance, thank you https://en.wikipedia.org/wiki/Levenshtein_distance
    public static int strRawDiff(String s, String t)
    {
        // degenerate cases
        if (s.equals(t)) return 0;
        if (s.length() == 0) return t.length();
        if (t.length() == 0) return s.length();

        // create two work vectors of integer distances
        int[] v0 = new int[t.length() + 1];
        int[] v1 = new int[t.length() + 1];

        // initialize v0 (the previous row of distances)
        // this row is A[0][i]: edit distance for an empty s
        // the distance is just the number of characters to delete from t
        for (int i = 0; i < v0.length; i++)
            v0[i] = i;

        for (int i = 0; i < s.length(); i++)
        {
            // calculate v1 (current row distances) from the previous row v0

            // first element of v1 is A[i+1][0]
            //   edit distance is delete (i+1) chars from s to match empty t
            v1[0] = i + 1;

            // use formula to fill in the rest of the row
            for (int j = 0; j < t.length(); j++)
            {
                int cost = (s.charAt(i) == t.charAt(j)) ? 0 : 1;
                v1[j + 1] = min(v1[j] + 1, v0[j + 1] + 1, v0[j] + cost);
            }

            // copy v1 (current row) to v0 (previous row) for next iteration
            for (int j = 0; j < v0.length; j++)
                v0[j] = v1[j];
        }

        return v1[t.length()];
    }

    public static double strDiff(String s, String t) {
        double tmp = 1.0 - strRawDiff(s, t) / (.5 * (s.length() + t.length()));
        if (tmp > 1)
            tmp = 1;
        else if (tmp < 0)
            tmp = 0;

        tmp = tmp * tmp;
        return  tmp;
    }

    public static int min(int a, int b, int c) {
        return java.lang.Math.min(java.lang.Math.min(a, b), c);
    }
}
