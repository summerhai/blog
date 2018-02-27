package canghailongyin.blog.exercise;

import java.util.Date;

/**
 * Created by mingl on 2018-2-6.
 */
public class SynThread implements Runnable {

    private volatile Integer count = 0;
    private Object lock = new Object();

    public void run() {
        while(count<1000){
            synchronized(lock){
                if(count<1000){
                    count++;
                    System.out.println("Thread"+Thread.currentThread().getName()+"operates " +
                            "the count,count now is"+count+","+new Date());
                }
                try{
                    lock.wait(5);
                }catch(Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
