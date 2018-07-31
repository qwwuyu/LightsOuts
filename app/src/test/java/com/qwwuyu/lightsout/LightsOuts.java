package com.qwwuyu.lightsout;

import org.junit.Test;

/**
 * Created by qiwei on 2018/7/31 16:59.
 * Description 未完成,准备解决O(N^3).
 */
public class LightsOuts {
    @Test
    public void test() {
        boolean[] result = solve(10);
        System.out.println();
        for (boolean aResult : result) {
            System.out.print((aResult ? "1" : "0"));
        }
        System.out.println();
    }

    public static boolean[] solve(int n) {
        if (n == 1) return new boolean[]{true};
        boolean[][] matrixA = new boolean[n][n];
        boolean[] a = new boolean[n];
        boolean[] b = new boolean[n];
        boolean[] c;
        boolean[] result = new boolean[n];
        for (int i = 0; i < result.length; i++) {
            result[i] = true;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < a.length; j++) {
                a[j] = false;
                b[j] = false;
            }
            a[i] = true;
            if (i > 0) a[i - 1] = true;
            if (i < a.length - 1) a[i + 1] = true;
            b[i] = true;
            for (int j = 1; j < n; j++) {
                b[0] = b[0] ^ a[0] ^ a[1];
                for (int k = 1, max = b.length - 1; k < max; k++) {
                    b[k] = b[k] ^ a[k] ^ a[k - 1] ^ a[k + 1];
                }
                b[n - 1] = b[n - 1] ^ a[n - 1] ^ a[n - 2];
                c = a;
                a = b;
                b = c;
            }
            System.arraycopy(a, 0, matrixA[i], 0, n);
        }
        sys(matrixA, result, "预处理后的矩阵:");
        elimination(matrixA, result);
        sys(matrixA, result, "消元后的矩阵:");
        back(0, matrixA, result);
        sys(matrixA, result, "回代后的矩阵:");
        return result;
    }

    /** 高斯消元 */
    public static void elimination(boolean[][] matrix, boolean[] result) {
        int n = matrix.length;
        for (int i = 0; i < n; i++) {
            wrap(i, matrix, result);
            for (int j = i + 1; j < n; j++) {
                if (matrix[j][i]) {
                    matrix[j][i] = false;
                    for (int k = i + 1; k < n; k++) {
                        matrix[j][k] = matrix[j][k] ^ matrix[i][k];
                    }
                    result[j] = result[j] ^ result[i];
                }
            }
        }
    }

    /** 换行 */
    private static void wrap(int line, boolean[][] a, boolean[] result) {
        int wrapLine = line;
        int n = a.length;
        for (int i = line; i < n; i++) {// 找到要交换的行
            if (a[i][line]) {
                wrapLine = i;
                break;
            }
        }
        // 交换行
        if (wrapLine != line) {
            for (int i = line; i < n; i++) {
                if (a[line][i] != a[wrapLine][i]) {
                    a[line][i] = !a[line][i];
                    a[wrapLine][i] = !a[wrapLine][i];
                }
            }
            if (result[line] != result[wrapLine]) {
                result[line] = !result[line];
                result[wrapLine] = !result[wrapLine];
            }
        }
    }

    /** 回代 */
    public static void back(long position, boolean[][] matrix, boolean[] result) {
//        long index = position;
//        long bit = 0b1;
//        for (int i = result.length - 1; i >= 0; i--) {
//            if (matrix[i][i]) {
//                break;
//            } else {
//                result[i] = (index & bit) == bit;
//            }
//            bit = bit << 1;
//        }
        for (int i = result.length - 1; i >= 0; i--) {
            if (matrix[i][i]) {
                System.out.println("共有" + Math.pow(2, result.length - i - 1) + "个解");
                break;
            } else if (i == 0) {
                System.out.println("共有" + Math.pow(2, result.length - i) + "个解");
                break;
            }
        }
        int n = result.length;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j]) {
                    result[i] = result[i] != result[j];
                }
            }
        }
    }

    /** 打印矩阵 */
    public static void sys(boolean[] result) {
        for (boolean aResult : result) {
            System.out.print(" " + (aResult ? 1 : 0));
        }
        System.out.println();
    }

    /** 打印矩阵 */
    public static void sys(boolean[][] matrix, boolean[] result, String tag) {
        System.out.println(tag + " 打印数组:" + matrix.length + " * " + matrix[0].length);
        if (matrix.length > 100) {
            System.out.println("矩阵太大,无法正常打印");
            return;
        }
        for (int i = 0; i < matrix.length; i++) {
            for (int j = 0; j < matrix[i].length; j++) {
                System.out.print(matrix[i][j] ? 1 : 0);
            }
            System.out.print(" " + (result[i] ? 1 : 0));
            System.out.println();
        }
    }
}
