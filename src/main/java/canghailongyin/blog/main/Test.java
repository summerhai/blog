package canghailongyin.blog.main;

import java.util.Arrays;
import java.util.Random;

/**
 * Created by mingl on 2017-10-23.
 */
public class Test {
    public static void main(String[] args) {
        int[] num1 = {1,2,3,4,5};
        int[] num2 = num1.clone();
        num1[0] = 3;
        System.out.println(Arrays.toString(num2));
    }
}
