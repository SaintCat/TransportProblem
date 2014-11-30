/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package transportproblem;

import java.awt.Point;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Роман
 */
public class TransportProblem {

    private float[] dilers;
    private float[] customers;
    private float[][] prices;
    private float[][] supportPlan;
    private float[][] supportPlanOrigional;
    private Point[] allowedPoints;
    private boolean isSeachingForMaximum;

//    float[] dilers = new float[]{4, 5, 4, 5};
//        float[] customers = new float[]{3, 8, 4, 3};
//        float[][] prices = new float[][]{{3, 4, 1, 2}, {2, 2, 4, 3}, {1, 1, 2, 1}, {1, 1, 1, 1}};
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws FileNotFoundException {
        String[] rows = new Scanner(new File("task.txt")).useDelimiter("\\Z").next().split("\n");
        if (rows.length != 3) {
            return;
        }
        float[] dil = null;
        float[] customers = null;
        float[][] pr = null;
        for (int i = 0; i < rows.length; i++) {
            String[] splitted = rows[i].split(" ");
            float[] tmp = new float[splitted.length];
            for (int k = 0; k < splitted.length; k++) {
                Float integ = Float.valueOf(splitted[k]);
                tmp[k] = integ;
            }
            if (i == 0) {
                dil = tmp;
            }
            if (i == 1) {
                customers = tmp;
            }
            if (i == 2) {
                if (tmp.length != dil.length * customers.length) {
                    return;
                }
                pr = new float[dil.length][];
                for (int z = 0; z < dil.length; z++) {
                    pr[z] = new float[customers.length];
                    for (int j = 0; j < customers.length; j++) {
                        pr[z][j] = tmp[z * customers.length + j];
                    }
                }
            }
        }

        TransportProblem tp = new TransportProblem(dil, customers, pr, SupportPlan.METHOD_MINIMUM_ELEMENT, true);
        float[][] ff = tp.solveProblem();
//
        System.out.println(
                "Оптимальный план: ");
        ArrayUtils.printArray(ff, null);

        System.out.println(tp.getCost(tp.getSupportPlane()));
        System.out.println(tp.getCost(ff));
    }

    public TransportProblem(float[] nDilers, float[] nCustomers, float[][] nPrices, SupportPlan plan, boolean isSeachingForMaximum) {
        this.dilers = nDilers;
        this.customers = nCustomers;
        this.prices = nPrices;
        this.isSeachingForMaximum = isSeachingForMaximum;

        float dilersSum = ArrayUtils.getNumberSumInArray(nDilers);
        float customersSum = ArrayUtils.getNumberSumInArray(nCustomers);
        float diff = dilersSum - customersSum;
        if (diff > 0) {
            float[] arrCopy = customers;
            customers = new float[arrCopy.length + 1];
            System.arraycopy(arrCopy, 0, customers, 0, arrCopy.length);
            customers[customers.length - 1] = diff;

            float[][] pricesCopy = prices;
            prices = new float[dilers.length][customers.length];
            for (int i = 0; i < pricesCopy.length; i++) {
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
        ArrayUtils.printArray(dilers, "Дилеры");
        ArrayUtils.printArray(customers, "Кастомеры");
        ArrayUtils.printArray(prices, "Цены");
        if (plan.equals(SupportPlan.METHOD_NORTWEST_ANGLE)) {
            supportPlan = nordWestAngle();
        } else {
            supportPlan = minMaxElementMethod();
        }
        supportPlanOrigional = new float[supportPlan.length][supportPlan[0].length];
        for (int i = 0; i < supportPlan.length; i++) {
            System.arraycopy(supportPlan[i], 0, supportPlanOrigional[i], 0, supportPlan[i].length);
        }
        ArrayUtils.printArray(supportPlan, "Начальный план");
        System.out.println(getCost(supportPlan));
        System.out.println("m + n - 1: " + (dilers.length + customers.length - 1));
        System.out.println("Число базисных клеток: " + ArrayUtils.getNoNaNValuesCount(supportPlan));
    }

    public float[][] getSupportPlane() {
        return supportPlanOrigional;
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
        zeroLoad(result);
        return result;
    }

    private float[][] minMaxElementMethod() {
        float[] dilersCopy = dilers;
        float[] customersCopy = customers;
        float[][] result = new float[dilers.length][customers.length];
        int i = 0, j = 0;
        boolean[][] notYetChecked = new boolean[dilers.length][customers.length];
        trueForAllValues(notYetChecked);
        while (!(ArrayUtils.isAllEmpty(dilersCopy) && ArrayUtils.isAllEmpty(customersCopy))) {
            Point point = findElement(notYetChecked, prices);
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
        zeroLoad(result);
        return result;
    }

    private void zeroLoad(float[][] result) {
        int count = ArrayUtils.getNoNaNValuesCount(result);
        int difference = (dilers.length + customers.length - 1) - count;

        float[][] helpMatr = new float[dilers.length][customers.length];
        for (int i = 0; i < dilers.length; i++) {
            for (int j = 0; j < customers.length; j++) {
                if (result[i][j] == result[i][j]) {
                    helpMatr[i][j] = prices[i][j];
                } else {
                    helpMatr[i][j] = Float.NaN;
                }
            }
        }
        float[] U = new float[dilers.length];
        float[] V = new float[customers.length];
        findUV(U, V, helpMatr);
        float[][] SMatr = makeSMatr(helpMatr, U, V);

        List<Point> minElements = new ArrayList<>();
        while (difference-- > 0) {
            minElements = getElements(SMatr, minElements);
        }

        for (Point p : minElements) {
            System.out.println(p);
            if (p.x != -1) {
                result[p.x][p.y] = 0;
            }
        }
    }

    private List<Point> getElements(float[][] matr, List<Point> already) {
        int indexI = -1;
        int indexJ = -1;
        float min;
        min = isSeachingForMaximum ? Float.MIN_VALUE : Float.MAX_VALUE;
        for (int i = 0; i < matr.length; i++) {
            for (int j = 0; j < matr[i].length; j++) {
                if (isSeachingForMaximum) {
                    if ((matr[i][j] == matr[i][j]) && (matr[i][j] > min) && (!already.contains(new Point(i, j)))) {
                        min = matr[i][j];
                        indexI = i;
                        indexJ = j;
                    }
                } else {
                    if ((matr[i][j] == matr[i][j]) && (matr[i][j] < min) && (!already.contains(new Point(i, j)))) {
                        min = matr[i][j];
                        indexI = i;
                        indexJ = j;
                    }
                }

            }
        }
        already.add(new Point(indexI, indexJ));
        return already;
    }

    private Point findElement(boolean[][] notYetChecked, float[][] prices) {

        //firstly find no zero min value
        float element;
        if (isSeachingForMaximum) {
            element = Float.MIN_VALUE;
        } else {
            element = Float.MAX_VALUE;
        }

        int indexI = -1;
        int indexJ = -1;
        for (int i = 0; i < prices.length; i++) {
            for (int j = 0; j < prices[i].length; j++) {
                if (!isSeachingForMaximum) {
                    if (notYetChecked[i][j] && prices[i][j] != 0 && prices[i][j] <= element) {
                        element = prices[i][j];
                        indexI = i;
                        indexJ = j;
                    }
                } else {
                    if (notYetChecked[i][j] && prices[i][j] != 0 && prices[i][j] >= element) {
                        element = prices[i][j];
                        indexI = i;
                        indexJ = j;
                    }
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
        float[][] SMatr = makeSMatr(helpMatr, U, V);
        if (isSeachingForMaximum) {
            while (!ArrayUtils.isAllNegative(SMatr)) {
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
        } else {
            while (!ArrayUtils.isAllPositive(SMatr)) {
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
        }

        return supportPlan;
    }

    private void roll(float[][] m, float[][] sm) {
        Point minInd = new Point();
        float min = isSeachingForMaximum ? Float.MIN_VALUE : Float.MAX_VALUE;
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
                if (isSeachingForMaximum) {
                    if (sm[i][j] >= min) {
                        min = sm[i][j];
                        minInd.x = i;
                        minInd.y = j;
                    }
                } else {
                    if (sm[i][j] < min) {
                        min = sm[i][j];
                        minInd.x = i;
                        minInd.y = j;
                    }
                }
            }
        }
        allowedPoints[allowedPoints.length - 1] = minInd;
        Point[] cycle = getCycle(minInd.x, minInd.y);
        float[] cycles = new float[cycle.length];
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
                } else {
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
                for (int j1 = 0; j1 < dilers.length; j1++) {
                    if (!U1[j1] && !U2[j1]) {
                        j = j1;
                        U[j] = 0;
                        U1[j] = true;
                        break;
                    }
                }
            }
            if ((j == -1) && (i == -1)) {
                for (int i1 = 0; i1 < customers.length; i1++) {
                    if (!V1[i1] && !V2[i1]) {
                        i = i1;
                        V[i] = 0;
                        V1[i] = true;
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

    public float getCost(float[][] plan) {
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
}
