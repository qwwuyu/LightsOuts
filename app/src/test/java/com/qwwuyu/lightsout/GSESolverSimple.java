package com.qwwuyu.lightsout;

import org.junit.Test;

/**
 * 使用高斯去消法解决点灯游戏,获取修改后的矩阵解题<br/>
 * <br/>
 * 解题思路总来源:https://zhuanlan.zhihu.com/p/21265602 <br/>
 * 高斯消元来源于网络:http://dacoolbaby.iteye.com/blog/1818768 <br/>
 * 矩阵 和 求解:http://www.jaapsch.net/puzzles/lomath.htm#refs <br/>
 * 其他相似算法:https://github.com/LyricalMaestro/LightsOutSolver 结构复杂.使用了jar.使用int类型,不能超过30.<br/>
 * Description 50=41ms 100=225ms 200=3376ms(具体根据电脑决定) 300好像需要900M内存,内存溢出
 *
 * @author qw
 */
public class GSESolverSimple {
    /** 需要解题的矩阵宽 */
    private static int n = 10;
    /** 需要解题的矩阵大小n^2 */
    private static int n2 = n * n;

    @Test
    public void test() {
        long time = System.currentTimeMillis();
        // 获取需要消元的矩阵,特殊处理:更换矩阵位子
        boolean[][] matrixA = getMatrixA();
        System.out.println(matrixA.length);
        // 获取结果集
        boolean[] result = getResult();
        System.out.println(result.length);
        sys(matrixA, result, "初始矩阵");
        // 高斯消元,特殊处理:从需要消元地方开始处理
        elimination(matrixA, result);
        back(matrixA, result);
        sys(result);
        System.out.println("消耗的时间:" + (System.currentTimeMillis() - time) + "ms");
    }

    /** 高斯消元 */
    public static void elimination(boolean[][] matrix, boolean[] result) {
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
    private static void wrap(int line, boolean[][] a, boolean[] result) {
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

    /** 回代 */
    public static void back(boolean[][] matrix, boolean[] result) {
        sys(matrix, result, "消元后的矩阵:");
        for (int i = n2 - 1; i >= 0; i--) {
            if (matrix[i][i]) {
                System.out.println("共有" + Math.pow(2, n2 - i - 1) + "个解");
                break;
            }
        }
        for (int i = n2 - 1; i >= 0; i--) {
            for (int j = i + 1; j < n2; j++) {
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

    /** 打印结果 */
    public static void sys(boolean[] result) {
        System.out.println("打印其中一种结果:");
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

    /** 获取需要达到结果的矩阵 */
    public static boolean[] getResult() {
        boolean[] result = new boolean[n * n];
        for (int i = 0; i < result.length; i++) {
            result[i] = true;
        }
        return result;
    }

    /** 获取需要消元的矩阵 */
    public static boolean[][] getMatrixA() {
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
}
