package com.qwwuyu.lightsout;

import org.junit.Test;

/**
 * 使用高斯去消法解决点灯游戏,获取正常的矩阵解题,通过特殊处理可减少代码算法量,详见GSESolverSimple <br/>
 * <br/>
 * 解题思路总来源:https://zhuanlan.zhihu.com/p/21265602 <br/>
 * 高斯消元来源于网络:http://dacoolbaby.iteye.com/blog/1818768 <br/>
 * 矩阵 和 求解:http://www.jaapsch.net/puzzles/lomath.htm#refs <br/>
 * 其他相似算法:https://github.com/LyricalMaestro/LightsOutSolver 结构复杂.使用了jar.使用int类型,不能超过30.<br/>
 * Description 50=54ms 100=973ms 200=38871ms(具体根据电脑决定) 300好像需要900M内存,内存溢出
 * @author qw
 */
public class GSESolver {
    @Test
    public void test() {
        long time = System.currentTimeMillis();
        // 需要解题的矩阵宽
        final int n = 5;
        // 获取需要消元的矩阵
        boolean[][] matrixA = getMatrixA(n);
        // 获取结果集
        boolean[] result = getResult(n);
        sys(matrixA, result, "初始矩阵");
        // 高斯消元
        elimination(matrixA, result);
        back(matrixA, result);
        sys(result);
        System.out.println("消耗的时间:" + (System.currentTimeMillis() - time) + "ms");
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
    public static void back(boolean[][] matrix, boolean[] result) {
        int n = matrix.length;
        sys(matrix, result, "消元后的矩阵:");
        for (int i = n - 1; i >= 0; i--) {
            if (matrix[i][i]) {
                System.out.println("共有" + Math.pow(2, n - i - 1) + "个解");
                break;
            }
        }
        for (int i = n - 1; i >= 0; i--) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j]) {
                    result[i] = result[i] != result[j];
                }
            }
        }
    }

    /** 打印矩阵 */
    public static void sys(boolean[][] matrix, boolean[] result, String tag) {
        System.out.println(tag + " 打印数组:" + matrix.length + " * " + matrix[0].length);
        if (matrix.length > 100) {
            System.out.println(tag + ":矩阵太大,无法正常打印");
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

    /** 打印结果 */
    public static void sys(boolean[] result) {
        System.out.println("打印其中一种结果:");
        int n = (int) Math.sqrt(result.length);
        if (n > 100) {
            System.out.println("矩阵太大,无法正常打印");
            for (int j = 0; j < n; j++) {
                System.out.print(result[0 * n + j] ? 1 : 0);
            }
            System.out.println();
            return;
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(result[i * n + j] ? 1 : 0);
            }
            System.out.println();
        }
    }

    /**
     * 获取需要达到结果的矩阵
     * @param n 需要的矩阵宽
     * @return 详细请见打印
     */
    public static boolean[] getResult(int n) {
        boolean[] result = new boolean[n * n];
        for (int i = 0; i < result.length; i++) {
            result[i] = true;
        }
        return result;
    }

    /**
     * 获取需要消元的矩阵
     * @param n 需要的矩阵宽
     * @return 详细请见打印
     */
    public static boolean[][] getMatrixA(int n) {
        boolean[][] matrix = new boolean[n * n][n * n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int index = i * n + j;
                matrix[index][index] = true;
                if (0 < j) {
                    matrix[index][index - 1] = true;
                }
                if (j < n - 1) {
                    matrix[index][index + 1] = true;
                }
                if (0 < i) {
                    matrix[index][index - n] = true;
                }
                if (i < n - 1) {
                    matrix[index][index + n] = true;
                }
            }
        }
        return matrix;
    }
}
