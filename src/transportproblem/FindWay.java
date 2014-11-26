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
public class FindWay {

    FindWay father;
    Point root;
    FindWay[] childrens;
    Point[] mAllowed;
    Point beginning;
    boolean flag;

    public FindWay(int x, int y, boolean _flag, Point[] _mAllowed, Point _Beg, FindWay _Father) {
        beginning = _Beg;
        flag = _flag;
        root = new Point(x, y);
        mAllowed = _mAllowed;
        father = _Father;
    }

    public boolean BuildTree() {
        Point[] ps = new Point[mAllowed.length];
        for(int s = 0 ; s < ps.length; s++) {
            ps[s] = new Point();
        }
        int Count = 0;
        for (int i = 0; i < mAllowed.length; i++) {
            if (flag) {
                if (root.y == mAllowed[i].y) {
                    Count++;
                    ps[Count - 1] = mAllowed[i];
                }

            } else if (root.x == mAllowed[i].x) {
                Count++;
                ps[Count - 1] = mAllowed[i];
            }
        }

        FindWay fwu = this;
        childrens = new FindWay[Count];
        int k = 0;
        for (int i = 0; i < Count; i++) {
            if (ps[i].equals(root)) {
                continue;
            }
            if (ps[i].equals(beginning)) {
                while (fwu != null) {
                    mAllowed[k] = fwu.root;
                    fwu = fwu.father;
                    k++;
                };
                for (; k < mAllowed.length; k++) {
                    mAllowed[k] = new Point(-1, -1);
                }
                return true;
            }

            if (!isAllStartPoints(ps)) {
                childrens[i] = new FindWay(ps[i].x, ps[i].y, !flag, mAllowed, beginning, this);
                Boolean result = childrens[i].BuildTree();
                if (result) {
                    return true;
                }
            }
        }
        return false;
    }

    private static boolean isAllStartPoints(Point[] points) {
        for (Point point : points) {
                if (point.x != 0 || point.y != 0) {
                    return false;
                }
        }
        return true;
    }
}
