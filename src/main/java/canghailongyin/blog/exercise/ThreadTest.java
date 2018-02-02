package canghailongyin.blog.exercise;

import java.util.ArrayList;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by mingl on 2018-1-3.
 */
public class ThreadTest {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private Lock lock = new ReentrantLock();    //注意这个地方

    public static void main(String[] args) throws InterruptedException {
        String s1 = "Programming";
        String s2 = new String("Programming");
        String s3 = "Program" + "ming";
        System.out.println(s1 == s2);
        System.out.println(s1 == s3);
        System.out.println(s1 == s1.intern());
        //1.手动创建线程
//        normalThread();
        //2.利用线程池创建线程
//        useThreadPool();
//        testLock();

    }

    private static void testLock() {

        final ThreadTest test = new ThreadTest();
        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();

        new Thread(){
            public void run() {
                test.insert(Thread.currentThread());
            };
        }.start();
    }

    public void insert(Thread thread) {
        if(lock.tryLock()) {
            try {
                System.out.println(thread.getName()+"得到了锁");
                for(int i=0;i<5;i++) {
                    arrayList.add(i);
                }
            } catch (Exception e) {
                // TODO: handle exception
            }finally {
                System.out.println(thread.getName()+"释放了锁");
                lock.unlock();
            }
        } else {
            System.out.println(thread.getName()+"获取锁失败");
        }
    }

//    private static void useThreadPool() {
//        //自动配置
//        ThreadPoolExecutor executor = (ThreadPoolExecutor) Executors.newFixedThreadPool(5);
//        //手动配置
////        ThreadPoolExecutor executor = new ThreadPoolExecutor(5, 10, 200, TimeUnit.MILLISECONDS,
////                new ArrayBlockingQueue<Runnable>(5));
//        long startTime = System.currentTimeMillis();
//
//        for(int i=0;i<3;i++){
//            MyThread myThread = new MyThread("thread"+i);
//            executor.execute(myThread);
//            System.out.println("线程池中线程数目："+executor.getPoolSize()+"，队列中等待执行的任务数目："+
//                    executor.getQueue().size()+"，已执行玩别的任务数目："+executor.getCompletedTaskCount());
//        }
//        executor.shutdown();
//    }

    private static void normalThread() {
        int all = 10000000;
        int threadNum = Runtime.getRuntime().availableProcessors();
        for(int i=0;i<threadNum;i++){
            MyThread myThread = new MyThread("thread"+i,all/threadNum,"log10000000_index");
            Thread thread = new Thread(myThread);
            //让线程动起来
            thread.start();
//            thread.join();
        }
    }
}
