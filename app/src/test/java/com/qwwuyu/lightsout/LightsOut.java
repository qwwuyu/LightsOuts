package com.qwwuyu.lightsout;

import org.junit.Test;

/**
 * Created by qiwei on 2018/7/31 16:59.
 * Description C++翻译过来的java版,有bug,O(N^3)..
 * https://github.com/njpipeorgan/LightsOut/tree/master
 */
public class LightsOut {
    @Test
    public void test() {
        solve(5);
    }

    public static boolean[] solve(int n) {
        final int N_ROW = n;
        final int N_COL = n / 16 + 1;

        System.out.println("N_ROW=" + N_ROW + " N_COL=" + N_COL);

        int[] a = new int[N_ROW * N_COL];
        int[] b = new int[N_ROW * N_COL];
        boolean[] result = new boolean[n];
        int[] c;

        for (int i = 0; i < a.length; i++) {
            a[i] = b[i] = 0;
        }
        for (int i = 0; i < result.length; i++) {
            result[i] = false;
        }

        for (int i = 0; i < N_ROW; i++) {
            int j1 = (i + 1) >> 4;
            int j2 = (i + 1) & 0b1111;
            a[i * N_COL + j1] = 1 << j2;
            b[i * N_COL] = 1;
        }

        s("begin", a, b);

        for (int t = 0; t < N_ROW; t++) {
            iterate(N_ROW, N_COL, a, b);
            c = a;
            a = b;
            b = c; // swap a and b
        }

        s("end", a, b);

        gauss(N_ROW, N_COL, a, result);

        for (boolean aResult : result) {
            System.out.print((aResult ? "1" : "0"));
        }
        System.out.println();
        return result;
    }

    private static void iterate(final int N_ROW, final int N_COL, final int[] a, final int[] b) {
        for (int j1 = 0; j1 < N_COL; j1++) // first loop
            b[0 * N_COL + j1] ^= a[0 * N_COL + j1] ^ a[1 * N_COL + j1];

        for (int i = 1; i < N_ROW - 1; i++) // middle loops
            for (int j1 = 0; j1 < N_COL; j1++)
                b[i * N_COL + j1] ^= a[(i - 1) * N_COL + j1] ^ a[i * N_COL + j1] ^ a[(i + 1) * N_COL + j1];

        for (int j1 = 0; j1 < N_COL; j1++) // last loop
            b[(N_ROW - 1) * N_COL + j1] ^= a[(N_ROW - 2) * N_COL + j1] ^ a[(N_ROW - 1) * N_COL + j1];

        for (int i = 0; i < N_ROW; i++) // update a
            a[i * N_COL] ^= 1;
    }

    private static void gauss(final int N_ROW, final int N_COL, final int[] a, final boolean[] result) {
        // forward
        for (int i = 0; i < N_ROW; i++) {
            int d1 = (i + 1) >> 4;
            int d2 = (i + 1) & 0b1111;
            int mask = 1 << d2;

            if (i == N_ROW - 1)
                if ((a[i * N_COL + d1] & mask) != 0)
                    break;
                else
                    return;

            // reduce and swap
            boolean found = false;
            for (int k = i; k < N_ROW; k++) {
                if ((a[k * N_COL + d1] & mask) != 0) {
                    // reduce
                    for (int r = k + 1; r < N_ROW; r++) {
                        if ((a[r * N_COL + d1] & mask) != 0)
                            for (int j1 = 0; j1 < N_COL; j1++)
                                a[r * N_COL + j1] ^= a[k * N_COL + j1];
                    }
                    // swap
                    if (k > i)
                        for (int j1 = 0; j1 < N_COL; j1++) {
                            int swap = a[k * N_COL + j1];
                            a[k * N_COL + j1] = a[i * N_COL + j1];
                            a[i * N_COL + j1] = swap;
                        }
                    found = true;
                }
                if (found)
                    break;
            }
            if (!found)
                return;
        }

        // backward
        for (int i = N_ROW - 1; i > 0; i--) {
            int d1 = (i + 1) >> 4;
            int d2 = (i + 1) & 0b1111;
            int mask = 1 << d2;
            for (int r = 0; r < i; r++)
                if ((a[r * N_COL + d1] & mask) != 0)
                    for (int j1 = 0; j1 < N_COL; j1++)
                        a[r * N_COL + j1] ^= a[i * N_COL + j1];
        }

        // write result
        for (int i = 0; i < N_ROW; i++)
            result[i] = (result[i] ^ (a[i * N_COL] & 1) != 0);

    }

    private static void s(Object tag, int[]... iss) {
        System.out.println(tag);
        for (int[] is : iss) {
            for (int i1 : is) {
                System.out.print(Integer.toBinaryString(i1) + " ");
            }
            System.out.println();
        }
    }
}
