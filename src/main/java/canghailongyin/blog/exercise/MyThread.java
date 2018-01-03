package canghailongyin.blog.exercise;

/**
 * Created by mingl on 2018-1-3.
 * 自定义的线程类，为具体业务生成run方法
 */
public class MyThread implements Runnable {
    private String threadName;

    public MyThread(String threadName){
        this.threadName = threadName;
    }

    @Override
    public void run() {
        for(int i=0;i<60;i++){
            System.out.println("i am " + threadName + ",i'm printing " + (i+1));
        }
    }
}
