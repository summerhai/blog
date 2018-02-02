package canghailongyin.blog.leetcode.array;

/**
 * Created by mingl on 2018-1-16.
 */
public class FindInArray {
    public static void main(String[] args) {
        int[][] array = new int[10][10];

    }

    /**
     * 有序二维数组，查找一个值是否存在，那么从左下角或者右上角开始，因为左下角向右是递增，向上是递减，可以判断进行循环
     * @param target
     * @param array
     * @return
     */
    public boolean find(int target, int [][] array) {
        if(array == null){
            return false;
        }
        int len = array.length - 1;
        int i = 0;
        int curValue = 0;
        while(len>=0 && i<array[0].length){
            curValue = array[len][i];//左下角的值
            if(curValue > target){
                len--;
            }else if(curValue < target){
                i++;
            }else{
                return true;
            }
        }
        return false;
    }
}
