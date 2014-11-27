/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transportproblem;

import java.util.Arrays;

/**
 *
 * @author Роман
 */
public class ArrayUtils {

    public static void nanToZero(float[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (Float.isNaN(array[i][j])) {
                    array[i][j] = 0;
                }
            }
        }
    }

    public static void zeroToNan(float[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == 0) {
                    array[i][j] = Float.NaN;
                }
            }
        }
    }

    public static boolean isAllPositive(float[][] array) {
        for (int i = 0; (i < array.length); i++) {
            for (int j = 0; (j < array[i].length); j++) {
                if (array[i][j] < 0) {
                    return false;
                }
            }
        }
        return true;
    }

    public static boolean isAllValuesTrue(boolean[] array) {
        for (boolean x : array) {
            if (x == false) {
                return false;
            }
        }
        return true;
    }

    public static int getNoZeroValuesCount(float[][] array) {
        int count = 0;
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (!Float.isNaN(array[i][j]) && array[i][j] != 0) {
                    count++;
                }
            }
        }
        return count;
    }

    public static boolean isAllEmpty(float[] array) {
        for (float x : array) {
            if (x != 0) {
                return false;
            }
        }
        return true;
    }

    public static float getNumberSumInArray(float[] array) {
        float sum = 0;
        for (float x : array) {
            sum += x;
        }
        return sum;
    }

    public static void printArray(float[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                System.out.print(array[i][j] + "\t");
            }
            System.out.println();
        }
    }

    public static void printArray(float[] array) {
        System.out.println(Arrays.toString(array));
    }
}
