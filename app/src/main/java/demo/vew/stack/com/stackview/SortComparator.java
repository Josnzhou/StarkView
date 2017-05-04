package demo.vew.stack.com.stackview;


import java.util.Comparator;

/**
 * Created by zhouwan on 2017/3/29.
 */

public class SortComparator implements Comparator {
    @Override public int compare(Object o1, Object o2) {
        StackViewData stackViewData = (StackViewData) o1;
        StackViewData stackViewData2 = (StackViewData) o2;
        return (int) (stackViewData2.getSumTotal() - stackViewData.getSumTotal());
    }
}
