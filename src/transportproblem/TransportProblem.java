/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transportproblem;

import java.awt.Point;

/**
 *
 * @author Роман
 */
public class TransportProblem {

    private float[] dilers;
    private float[] customers;
    private float[][] prices;
    private float[][] supportPlan;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        float[] dilers = new float[]{4, 5, 4, 5};
        float[] customers = new float[]{3, 8, 4, 3};
        float[][] prices = new float[][]{{3, 4, 1, 2}, {2, 2, 4, 3}, {1, 1, 2, 1}, {1, 1, 1, 1}};
        TransportProblem tp = new TransportProblem(dilers, customers, prices, SupportPlan.METHOD_MINIMUM_ELEMENT);
        tp.solveProblem();
    }

    public TransportProblem(float[] nDilers, float[] nCustomers, float[][] nPrices, SupportPlan plan) {
        this.dilers = nDilers;
        this.customers = nCustomers;
        this.prices = nPrices;

        float dilersSum = ArrayUtils.getNumberSumInArray(nDilers);
        float customersSum = ArrayUtils.getNumberSumInArray(nCustomers);
        System.err.println(dilersSum);
        System.err.println(customersSum);
        float diff = dilersSum - customersSum;
        if (diff > 0) {
            float[] arrCopy = customers;
            customers = new float[arrCopy.length + 1];
            System.arraycopy(arrCopy, 0, customers, 0, arrCopy.length);
            customers[customers.length - 1] = diff;

            float[][] pricesCopy = prices;
            prices = new float[dilers.length][customers.length];
            for (int i = 0; i < pricesCopy.length - 1; i++) {
                System.arraycopy(pricesCopy[i], 0, prices[i], 0, pricesCopy[i].length);
            }
        } else if (diff < 0) {
            float[] arrCopy = dilers;
            dilers = new float[arrCopy.length + 1];
            System.arraycopy(arrCopy, 0, dilers, 0, arrCopy.length);
            dilers[dilers.length - 1] = Math.abs(diff);

            float[][] pricesCopy = prices;
            prices = new float[dilers.length][customers.length];
            for (int i = 0; i < pricesCopy.length; i++) {
                System.arraycopy(pricesCopy[i], 0, prices[i], 0, pricesCopy[i].length);
            }
        }
        ArrayUtils.printArray(dilers);
        ArrayUtils.printArray(customers);
        ArrayUtils.printArray(prices);
        if (plan.equals(SupportPlan.METHOD_NORTWEST_ANGLE)) {
            supportPlan = nordWestAngle();
        } else {
            supportPlan = minElementMethod();
        }
        System.out.println("Начальный опорный план: ");
        ArrayUtils.printArray(supportPlan);
        System.out.println(getCost(supportPlan));
        System.out.println("m + n - 1: " + (dilers.length + customers.length - 1));
        System.out.println("Число базисных клеток: " + ArrayUtils.getNoZeroValuesCount(supportPlan));
    }

    private float[][] nordWestAngle() {
        float[] dilersCopy = dilers;
        float[] customersCopy = customers;
        int i = 0, j = 0;
        float[][] result = new float[dilers.length][customers.length];
        while (!(ArrayUtils.isAllEmpty(dilersCopy) && ArrayUtils.isAllEmpty(customersCopy))) {
            float diff = Math.min(dilersCopy[i], customersCopy[j]);
            result[i][j] = diff;
            dilersCopy[i] -= diff;
            customersCopy[j] -= diff;
            if (dilersCopy[i] == 0) {
                i++;
            }
            if (customersCopy[j] == 0) {
                j++;
            }
            if (i >= dilers.length || j >= customers.length) {
                break;
            }
        }
        return result;
    }

    private float[][] minElementMethod() {
        float[] dilersCopy = dilers;
        float[] customersCopy = customers;
        float[][] result = new float[dilers.length][customers.length];
        int i = 0, j = 0;
        boolean[][] notYetChecked = new boolean[dilers.length][customers.length];
        trueForAllValues(notYetChecked);
        while (!(ArrayUtils.isAllEmpty(dilersCopy) && ArrayUtils.isAllEmpty(customersCopy))) {
            Point point = findMinElement(notYetChecked, prices);
            i = point.x;
            j = point.y;
            float dif = Math.min(dilersCopy[i], customersCopy[j]);
            result[i][j] += dif;
            dilersCopy[i] -= dif;
            customersCopy[j] -= dif;
            if (dilersCopy[i] == 0) {
                int k = 0;
                while (k < customers.length) {
                    notYetChecked[i][k] = false;
                    k++;
                }
            }
            if (customersCopy[j] == 0) {
                int k = 0;
                while (k < dilers.length) {
                    notYetChecked[k][j] = false;
                    k++;
                }
            }
        }

        return result;
    }

    private Point findMinElement(boolean[][] notYetChecked, float[][] prices) {

        //firstly find no zero min value
        float min = Float.MAX_VALUE;
        int indexI = -1;
        int indexJ = -1;
        for (int i = 0; i < prices.length; i++) {
            for (int j = 0; j < prices[i].length; j++) {
                if (notYetChecked[i][j] && prices[i][j] != 0 && prices[i][j] < min) {
                    min = prices[i][j];
                    indexI = i;
                    indexJ = j;
                }
            }
        }
        //if indedI,J == -1 we only have zero values
        if (indexI == -1) {
            for (int i = 0; i < prices.length; i++) {
                for (int j = 0; j < prices[i].length; j++) {
                    if (notYetChecked[i][j] && prices[i][j] == 0) {
                        indexI = i;
                        indexJ = j;
                        break;
                    }
                }
            }
        }

        return new Point(indexI, indexJ);
    }

    private void trueForAllValues(boolean[][] array) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                array[i][j] = true;
            }
        }
    }

    public void solveProblem() {
    }

    private float getCost(float[][] plan) {
        float sum = 0;

        for (int i = 0; i < prices.length; i++) {
            for (int j = 0; j < prices[i].length; j++) {
                sum += prices[i][j] * plan[i][j];
            }
        }
        return sum;
    }

    private void generateTestData() {
        dilers = new float[]{600, 180, 220};
        customers = new float[]{270, 800, 110};
        prices = new float[][]{{40, 30, 18}, {45, 28, 22}, {50, 22, 14}};
    }
}
