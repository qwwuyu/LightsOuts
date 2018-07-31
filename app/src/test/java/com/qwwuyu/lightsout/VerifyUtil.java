package com.qwwuyu.lightsout;

import org.junit.Test;

/**
 * 验证工具
 * @author qw
 */
public class VerifyUtil {
    @Test
    public void test() {
        //测试矩阵200的解是否正确
        long time = System.currentTimeMillis();
//        boolean result = verify(strToBls("11000010001111000011101110110101111101001101111010101111110000010011010001111001100110100100011101000010111000100101100110011110001011001000001111110101011110110010111110101101110111000011110001000011"));
        //测试矩阵1000的解是否正确
        boolean result = verify(strToBls("11110100100"));
        System.out.println("结果:" + result + " time:" + (System.currentTimeMillis() - time) + "ms");
    }

    /**
     * 通过第一行验证结果是否正确
     * @param firstLine 第一行的点击,true为点击,false为不点击
     * @return true结果正确
     */
    public static boolean verify(boolean[] firstLine) {
        int n = firstLine.length;
        boolean[][] matrix = new boolean[n][n];
        // 计算前2行数据
        for (int i = 0; i < n; i++) {
            if (i == 0) {
                matrix[0][i] = firstLine[i] ^ firstLine[i + 1];
            } else if (i == n - 1) {
                matrix[0][i] = firstLine[i - 1] ^ firstLine[i];
            } else {
                matrix[0][i] = firstLine[i - 1] ^ firstLine[i] ^ firstLine[i + 1];
            }
            matrix[1][i] = firstLine[i];
        }
        return verify(matrix);
    }

    /**
     * 从第2行开始,通过补齐上一行,判断最后一行是否正确
     * @param matrix true为亮 false为暗
     * @return true最后一行全亮
     */
    public static boolean verify(boolean[][] matrix) {
        int n = matrix[0].length;// 行数
        int m = matrix.length;// 列数
        for (int i = 1; i < n; i++) {// 从第2行开始
            for (int j = 0; j < m; j++) {
                if (!matrix[i - 1][j]) {
                    matrix[i][j] = !matrix[i][j];
                    if (j == 0) {
                        matrix[i][j + 1] = !matrix[i][j + 1];
                    } else if (j == m - 1) {
                        matrix[i][j - 1] = !matrix[i][j - 1];
                    } else {
                        matrix[i][j + 1] = !matrix[i][j + 1];
                        matrix[i][j - 1] = !matrix[i][j - 1];
                    }
                    if (i != n - 1) {
                        matrix[i + 1][j] = !matrix[i + 1][j];
                    }
                }
                matrix[i - 1][j] = true;
            }
        }
        for (int i = 0; i < n; i++) {
            if (!matrix[m - 1][i]) {
                return false;
            }
        }
        return true;
    }

    /** 用过int数组转boolean数组 */
    public static boolean[] intToBls(int[] ints) {
        boolean[] bls = new boolean[ints.length];
        for (int i = 0; i < bls.length; i++) {
            bls[i] = (ints[i] == 1);
        }
        return bls;
    }

    /** 用过String组转boolean数组 */
    public static boolean[] strToBls(String str) {
        boolean[] bls = new boolean[str.length()];
        for (int i = 0; i < bls.length; i++) {
            bls[i] = (str.charAt(i) == '1');
        }
        return bls;
    }
}
