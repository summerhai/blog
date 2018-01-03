package canghailongyin.blog.exercise;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by mingl on 2018-1-3.
 */
public class ThreadTest {

    public static void main(String[] args) throws InterruptedException {
//        normalThread();
//        useThreadPool();
    }

    private static void useThreadPool() {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<Runnable>(5));

        for(int i=0;i<3;i++){
            MyThread myThread = new MyThread("thread"+i);
            executor.execute(myThread);
            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
                    executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
        }
        executor.shutdown();
    }

    private static void normalThread() {
        int threadNum = 3;
        for(int i=0;i<threadNum;i++){
            MyThread myThread = new MyThread("thread"+i);
            Thread thread = new Thread(myThread);
            thread.start();
//            thread.join();
        }
    }
}
