package com.qwwuyu.lightsout;

/**
 * Created by qiwei on 2018/7/9 10:39
 * Description .
 */
public class SolveUtils {
    private int n, n2;
    private boolean[][] matrixA;
    private boolean[] result;
    private boolean[] backResult;
    private long num;

    public SolveUtils(int n) {
        this.n = n;
        this.n2 = n * n;
        matrixA = getMatrixA();
        result = getInitResult();
        backResult = getInitResult();
        elimination(matrixA, result);
        num = getNum();
    }

    /** 获取需要消元的矩阵 */
    private boolean[][] getMatrixA() {
        int n2 = n * n;
        boolean[][] matrix = new boolean[n2][n2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = (i * n + j - n + n2) % n2;
                int index2 = i * n + j;
                matrix[index][index2] = true;
                if (0 < j) {
                    matrix[index][index2 - 1] = true;
                }
                if (j < n - 1) {
                    matrix[index][index2 + 1] = true;
                }
                if (0 < i) {
                    matrix[index][index2 - n] = true;
                }
                if (i < n - 1) {
                    matrix[index][index2 + n] = true;
                }
            }
        }
        return matrix;
    }

    /** 获取需要达到结果的矩阵 */
    private boolean[] getInitResult() {
        boolean[] result = new boolean[n * n];
        for (int i = 0; i < result.length; i++) {
            result[i] = true;
        }
        return result;
    }

    /** 高斯消元 */
    private void elimination(boolean[][] matrix, boolean[] result) {
        int n2 = n * n;
        for (int i = n2 - n; i < n2; i++) {
            for (int j = 0; j < n2 - n; j++) {
                if (matrix[i][j]) {
                    matrix[i][j] = false;
                    for (int k = j + 1; k < n2; k++) {
                        matrix[i][k] = matrix[i][k] ^ matrix[j][k];
                    }
                    result[i] = result[i] ^ result[j];
                }
            }
        }
        for (int i = n2 - n; i < n2; i++) {
            wrap(i, matrix, result);
            for (int j = i + 1; j < n2; j++) {
                if (matrix[j][i]) {
                    matrix[j][i] = false;
                    for (int k = i + 1; k < n2; k++) {
                        matrix[j][k] = matrix[j][k] ^ matrix[i][k];
                    }
                    result[j] = result[j] ^ result[i];
                }
            }
        }
    }

    /** 换行 */
    private void wrap(int line, boolean[][] a, boolean[] result) {
        int wrapLine = line;
        for (int i = line; i < n2; i++) {// 找到要交换的行
            if (a[i][line]) {
                wrapLine = i;
                break;
            }
        }
        // 交换行
        if (wrapLine != line) {
            for (int i = line; i < n2; i++) {
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

    /** 获取解的数量 */
    private long getNum() {
        for (int i = n2 - 1; i >= 0; i--) {
            if (matrixA[i][i]) {
                return (long) Math.pow(2, n2 - i - 1);
            }
        }
        return 0;
    }

    /** 回代 */
    public boolean[] back(long position) {
        System.arraycopy(result, 0, backResult, 0, backResult.length);

        long index = position % num;
        long bit = 0b1;
        for (int i = n2 - 1; i >= 0; i--) {
            if (matrixA[i][i]) {
                break;
            } else {
                backResult[i] = (index & bit) == bit;
            }
            bit = bit << 1;
        }
        for (int i = n2 - 1; i >= 0; i--) {
            for (int j = i + 1; j < n2; j++) {
                if (matrixA[i][j]) {
                    backResult[i] = backResult[i] != backResult[j];
                }
            }
        }
        return backResult;
    }

    public long getResultNum() {
        return num;
    }
}
