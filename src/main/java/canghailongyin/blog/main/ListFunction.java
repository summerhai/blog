package canghailongyin.blog.main;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by mingl on 2017-6-27.
 */
public class ListFunction {

    public static void main(String[] args) {
        //test1 删除列表中的偶数
//        removeListEvens();
        //test2 约瑟夫环问题
        josephusProblem();

    }

    private static void josephusProblem() {
        int N = 100, M = 6;//N表示人数，M表示要被淘汰的数字
        List<Integer> linkList = generateJosephusList(N);
        int winNumber1 = runJosephusProblem1(linkList, M);
        int winNumber2 = runJosephusProblem2(N, M);
        System.out.println(winNumber1 + "," + winNumber2);
    }

    //抽象为数学问题：f(n)=(f(n-1)+M)%N,f(1)=0
    private static int runJosephusProblem2(int n, int m) {
        int all = 0;
        if (n < 1 || m < 1) {
            return -1;
        }
        for (int i = 2; i <= n; i++) {
            all = (all + m) % i;
        }
        return all + 1;
    }

    private static int runJosephusProblem1(List<Integer> linkList, int m) {
        // 每次计数开始的索引
        int startIndex = 0;
        // 删除的位置
        int removeIndex;
        //循环删除
        while (true) {
            // 【起始位置】+【报号数】-1 =【len】
            int len = startIndex + m - 1;
            // 如果 len > list.size(),相当于转了一圈，又重新开始;
            if (len > linkList.size() - 1) {
                removeIndex = len % linkList.size();
            } else {
                removeIndex = len;
            }
            linkList.remove(removeIndex);
            startIndex = removeIndex;
            //只有最后一位时，返回
            if (linkList.size() == 1) {
                return linkList.get(0);
            }
        }

    }

    private static List<Integer> generateJosephusList(int n) {
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 1; i <= n; i++) {
            list.add(i);
        }
        return list;
    }

    private static void removeListEvens() {
        int listSize = 2000000;
        List<Integer> arrList = generateArrayList(listSize);
        List<Integer> linkList = generateLinkedList(listSize);

        long start1 = System.currentTimeMillis();
        removeEvens3(arrList);
        long end1 = System.currentTimeMillis();
        System.out.println("方法1去除大小为" + listSize + "的ArrayList，花费时间为:" + (end1 - start1) / 1000 + "秒");

        long start2 = System.currentTimeMillis();
        removeEvens3(linkList);
        long end2 = System.currentTimeMillis();
        System.out.println("方法1去除大小为" + listSize + "的ArrayList，花费时间为:" + (end2 - start2) / 1000 + "秒");
    }

    private static List<Integer> generateLinkedList(int listSize) {
        long start = System.currentTimeMillis();
        List<Integer> list = new LinkedList<Integer>();
        for (int i = 0; i < listSize; i++) {
            list.add(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("创建" + listSize + "条记录的LinkedList用时:" + (end - start) / 1000 + "秒");
        return list;
    }

    private static List<Integer> generateArrayList(int listSize) {
        long start = System.currentTimeMillis();
        List<Integer> list = new ArrayList<Integer>();
        for (int i = 0; i < listSize; i++) {
            list.add(i);
        }
        long end = System.currentTimeMillis();
        System.out.println("创建" + listSize + "条记录的ArrayList用时:" + (end - start) / 1000 + "秒");
        return list;
    }

    /**
     * removeListEvens方法1
     * 该方法的问题：如果是ArrayList，那么删除操作效率低；如果是LinkedList，get的效率低，都是N2
     *
     * @param list
     */
    public static void removeEvens1(List<Integer> list) {
        int index = 0;
        while (index < list.size()) {
            if (list.get(index) % 2 == 0) {
                list.remove(index);
            } else {
                index++;
            }
        }
    }

    /**
     * removeListEvens方法2，会抛出ConcurrentModificationException，原因：增强型的for循环是使用迭代器，如果删除当前数据项，会导致迭代器非法
     *
     * @param list
     */
    public static void removeEvens2(List<Integer> list) {
        for (Integer x : list) {
            if (x % 2 == 0) {
                list.remove(x);
            }
        }
    }

    /**
     * removeListEvens方法3，使用Iterator迭代器，用迭代器删除数据项，对ArrayList是N2，对LinkedList是线性
     *
     * @param list
     */
    public static void removeEvens3(List<Integer> list) {
        Iterator<Integer> iterator = list.iterator();
        while (iterator.hasNext()) {
            if (iterator.next() % 2 == 0) {
                iterator.remove();
            }
        }
    }


}
