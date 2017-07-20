package canghailongyin.blog.basic_structure;

/**
 * Created by mingl on 2017-6-15.
 * ArrayList是基于动态数组的实现，查找和修改快，但是插入和删除慢
 * LinkedList是基于双链表的实现，插入和删除快，但是查找和修改慢
 */

public class MyArrayList {
    public static void main(String[] args){
        //如果整型字面量的值在-128到127之间，那么不会new新的Integer对象，
        // 而是直接引用常量池中的Integer对象，所以上面的面试题中f1==f2的结果是true，而f3==f4的结果是false。
        Integer f1 = 100, f2 = 100, f3 = 150, f4 = 150;

        System.out.println(f1 == f2);
        System.out.println(f3 == f4);

        String s1 = new StringBuilder("go")
                .append("od").toString();
        System.out.println(s1);
        System.out.println(s1.intern() == s1);
        String s2 = new StringBuilder("ja")
                .append("va").toString();
        System.out.println(s2);
        System.out.println(s2.intern() == s2);
    }
}
