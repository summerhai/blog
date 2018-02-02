package canghailongyin.blog.leetcode.dynamic;

import java.util.Arrays;

/**
 * Created by mingl on 2018-2-1.
 */
public class KingGoldQuestion {
    public static void main(String[] args) {
        //金矿数量
        int gNum = 5;
        //工人数量
        int pNum = 10;
        //金矿包含的金数量
        int[] goldNum = {400,500,200,300,350};
        //每座金矿需要的工人数
        int[] personNum = {5,5,3,4,3};
        int mostGold = getMostGold(gNum,pNum,goldNum,personNum);
        System.out.println(mostGold);
    }

    private static int getMostGold(int gNum, int pNum, int[] goldNum, int[] personNum) {
        int[] lastLineResult = new int[pNum+1];
        int[] realResult = new int[pNum+1];
        //处理边界值，也就是只有一个矿的情况
        for(int i=1;i<=pNum;i++){
            //如果当前的人手不够挖第一个矿
            if(i<personNum[0]){
                lastLineResult[i] = 0;
            }else{
                //如果当前的人手够挖第一个矿
                lastLineResult[i] = goldNum[0];
            }
        }
        System.out.println("first line:"+ Arrays.toString(lastLineResult));
        //对每个矿进行判断
        for(int i=1;i<gNum;i++){
            //判断每个矿在不同人数下收益情况
            for(int j=1;j<=pNum;j++){
                if(j<personNum[i]){
                    //如果当前人数不够当前矿需要的人数，则只能依赖之前的结果
                    realResult[j] = lastLineResult[j];
                }else{
                    //如果当前人数已经可以挖多个矿，那么就要判断：不挖这个矿的收益和挖了这个矿的收益哪个大
                    //lastLineResult[i]就是不挖这个矿
                    //lastLineResult[j-personNum[i]]+goldNum[i]就是挖这个矿，去除这个矿需要的人手，加上这个矿的产出
                    realResult[j] = Math.max(lastLineResult[j],lastLineResult[j-personNum[i]]+goldNum[i]);
                }
            }
            System.out.println((i+1)+"座金矿对应不同工人的产出：");
            System.out.println("curr line:"+ Arrays.toString(realResult));
//            System.out.println("last line:"+ Arrays.toString(lastLineResult));
            //每次要更新上一行
            lastLineResult = realResult.clone();
//            System.out.println("after update:"+Arrays.toString(lastLineResult));
        }
        //最终最后一个值就是最大收益

        return realResult[0];
    }
}
