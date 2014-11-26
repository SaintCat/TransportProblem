/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transportproblem;

import java.awt.Point;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Роман
 */
public class TransportProblem {

    private float[] dilers;
    private float[] customers;
    private float[][] prices;
    private float[][] supportPlan;
    private Point[] allowedPoints;

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        float[] dilers = new float[]{50, 23, 10};
        float[] customers = new float[]{20, 20, 43};
        float[][] prices = new float[][]{{10,5,4}, {6,4,5}, {7,3,6}};
        TransportProblem tp = new TransportProblem(dilers, customers, prices, SupportPlan.METHOD_NORTWEST_ANGLE);
        float[][] ff = tp.solveProblem();
        System.out.println("Оптимальный план: ");
        ArrayUtils.printArray(ff);
        System.out.println(tp.getCost(ff));
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
//            for (int i = 0; i < pricesCopy.length - 1; i++) {
//                System.arraycopy(pricesCopy[i], 0, prices[i], 0, pricesCopy[i].length);
//            }
        } else if (diff < 0) {
            float[] arrCopy = dilers;
            dilers = new float[arrCopy.length + 1];
            System.arraycopy(arrCopy, 0, dilers, 0, arrCopy.length);
            dilers[dilers.length - 1] = Math.abs(diff);

            float[][] pricesCopy = prices;
            prices = new float[dilers.length][customers.length];
//            for (int i = 0; i < pricesCopy.length; i++) {
//                System.arraycopy(pricesCopy[i], 0, prices[i], 0, pricesCopy[i].length);
//            }
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
        ArrayUtils.zeroToNan(result);
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
        ArrayUtils.zeroToNan(result);
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

    public float[][] solveProblem() {
        int i = 0, j = 0;
        float[][] helpMatr = new float[dilers.length][customers.length];
        for (i = 0; i < dilers.length; i++) {
            for (j = 0; j < customers.length; j++) {
                if (supportPlan[i][j] == supportPlan[i][j]) {
                    helpMatr[i][j] = prices[i][j];
                } else {
                    helpMatr[i][j] = Float.NaN;
                }
            }
        }
        float[] U = new float[dilers.length];
        float[] V = new float[customers.length];
        findUV(U, V, helpMatr);
//        ArrayUtils.printArray(U);
//        ArrayUtils.printArray(V);
        
        float[][] SMatr = makeSMatr(helpMatr, U, V);
        ArrayUtils.printArray(SMatr);
        
        while (!ArrayUtils.isAllPositive(SMatr)) {
            System.out.println("ASDSDSd");
            roll(supportPlan, SMatr);
            for (i = 0; i < dilers.length; i++) {
                for (j = 0; j < customers.length; j++) {
                    if (supportPlan[i][j] == Float.POSITIVE_INFINITY) {
                        helpMatr[i][j] = prices[i][j];
                        supportPlan[i][j] = 0;
                        continue;
                    }
                    if (!Float.isNaN(supportPlan[i][j])) {
                        helpMatr[i][j] = prices[i][j];
                    } else {
                        helpMatr[i][j] = Float.NaN;
                    }
                }
            }
            findUV(U, V, helpMatr);
            SMatr = makeSMatr(helpMatr, U, V);
        }

        return supportPlan;
    }

    private void roll(float[][] m, float[][] sm) {
        Point minInd = new Point();
        float min = Float.MAX_VALUE;
        int k = 0;
        allowedPoints = new Point[dilers.length + customers.length];
        for (int d = 0; d < allowedPoints.length; d++) {
            allowedPoints[d] = new Point();
        }
        for (int i = 0; i < dilers.length; i++) {
            for (int j = 0; j < customers.length; j++) {
                if (!Float.isNaN(m[i][j])) {
                    allowedPoints[k].x = i;
                    allowedPoints[k].y = j;
                    k++;
                }
                // заодно ищем макс по модулю отр элемент
                if (sm[i][j] < min) {
                    min = sm[i][j];
                    minInd.x = i;
                    minInd.y = j;
                }
            }
        }
        allowedPoints[allowedPoints.length - 1] = minInd;
        Point[] cycle = getCycle(minInd.x, minInd.y);
        float[] cycles = new float[cycle.length];
        boolean[] bCycles = new boolean[cycle.length];
        for (int i = 0; i < bCycles.length; i++) {
            bCycles[i] = i != bCycles.length - 1;
        }
        min = Float.MAX_VALUE;
        for (int i = 0; i < cycle.length; i++) {
            cycles[i] = m[cycle[i].x][cycle[i].y];
            if ((i % 2 == 0) && (cycles[i] == cycles[i]) && (cycles[i] < min)) {
                min = cycles[i];
                minInd = cycle[i];
            }
            if (cycles[i] != cycles[i]) {
                cycles[i] = 0;
            }
        }
        for (int i = 0; i < cycle.length; i++) {
            if (i % 2 == 0) {
                cycles[i] -= min;
                m[cycle[i].x][cycle[i].y] -= min;
            } else {
                cycles[i] += min;
                if (Float.isNaN(m[cycle[i].x][cycle[i].y])) {
                    m[cycle[i].x][cycle[i].y] = 0;
                }
                m[cycle[i].x][cycle[i].y] += min;
            }
        }
        m[minInd.x][minInd.y] = Float.NaN;
    }

    private Point[] getCycle(int x, int y) {
        Point begin = new Point(x, y);
        FindWay fw = new FindWay(x, y, true, allowedPoints, begin, null);
        fw.BuildTree();
        Point[] way = findAllValidPoint(allowedPoints);
        return way;
    }

    private Point[] findAllValidPoint(Point[] points) {
        List<Point> p = new ArrayList<>();
        for (Point pp : points) {
            if (pp.x != -1 && pp.y != -1) {
                p.add(pp);
            }
        }
        return p.toArray(new Point[p.size()]);
    }

    private float[][] makeSMatr(float[][] M, float[] U, float[] V) {
        float[][] resultMatr = new float[dilers.length][customers.length];
        for (int i = 0; i < dilers.length; i++) {
            for (int j = 0; j < customers.length; j++) {
                resultMatr[i][j] = M[i][j];
                if (Float.isNaN(resultMatr[i][j])) {
                    resultMatr[i][j] = prices[i][j] - (U[i] + V[j]);
                }
            }
        }
        return resultMatr;
    }

    private void findUV(float[] U, float[] V, float[][] helpMatr) {
        boolean[] U1 = new boolean[dilers.length];
        boolean[] U2 = new boolean[dilers.length];
        boolean[] V1 = new boolean[customers.length];
        boolean[] V2 = new boolean[customers.length];
        while (!(ArrayUtils.isAllValuesTrue(V1) && ArrayUtils.isAllValuesTrue(U1))) {
            int i = -1;
            int j = -1;
            for (int i1 = customers.length - 1; i1 >= 0; i1--) {
                if (V1[i1] && !V2[i1]) {
                    i = i1;
                }
            }
            for (int j1 = dilers.length - 1; j1 >= 0; j1--) {
                if (U1[j1] && !U2[j1]) {
                    j = j1;
                }
            }

            if ((j == -1) && (i == -1)) {
                for (int i1 = customers.length - 1; i1 >= 0; i1--) {
                    if (!V1[i1] && !V2[i1]) {
                        i = i1;
                        V[i] = 0;
                        V1[i] = true;
                        break;
                    }
                }
            }
            if ((j == -1) && (i == -1)) {
                for (int j1 = dilers.length - 1; j1 >= 0; j1--) {
                    if (!U1[j1] && !U2[j1]) {
                        j = j1;
                        U[j] = 0;
                        U1[j] = true;
                        break;
                    }
                }
            }

            if (i != -1) {
                for (int j1 = 0; j1 < dilers.length; j1++) {
                    if (!U1[j1]) {
                        U[j1] = helpMatr[j1][i] - V[i];
                    }
                    if (U[j1] == U[j1]) {
                        U1[j1] = true;
                    }
                }
                V2[i] = true;
            }

            if (j != -1) {
                for (int i1 = 0; i1 < customers.length; i1++) {
                    if (!V1[i1]) {
                        V[i1] = helpMatr[j][i1] - U[j];
                    }
                    if (V[i1] == V[i1]) {
                        V1[i1] = true;
                    }
                }
                U2[j] = true;
            }
        }
    }

    private float getCost(float[][] plan) {
        float sum = 0;

        for (int i = 0; i < prices.length; i++) {
            for (int j = 0; j < prices[i].length; j++) {
                if (!Float.isNaN(plan[i][j])) {
                    sum += prices[i][j] * plan[i][j];
                }
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
