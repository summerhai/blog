package canghailongyin.blog.main;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by mingl on 2017-10-23.
 */
public class Test {
    public static void main(String[] args) {
        int[] a = new int[10];
        Random random = new Random();
        for(int i=0;i<10;i++){
            a[i] = random.nextInt();
            System.out.println(a[i]);
        }
        long start = System.currentTimeMillis();
//        Arrays.sort();
        long end = System.currentTimeMillis();
        System.out.println("1:"+(end-start));
    }
}
