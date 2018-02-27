package canghailongyin.blog.exercise;

/**
 * Created by mingl on 2018-2-6.
 */
public class TestAtomic {

    public static void main(String args[]){

        Runnable r = new SynThread();
        for(int i=1;i<100;i++){
            new Thread(r).start();
        }
    }

}
