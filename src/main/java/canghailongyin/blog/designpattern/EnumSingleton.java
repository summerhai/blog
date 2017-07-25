package canghailongyin.blog.designpattern;

/**
 * Created by mingl on 2017-7-25.
 */
public class EnumSingleton {
    private EnumSingleton() {
    }

    public static EnumSingleton getInstance() {
        return Singleton.INSTANCE.getInstance();
    }

    private static enum Singleton {
        INSTANCE;

        private EnumSingleton singleton;

        //JVM会保证此方法绝对只调用一次
        private Singleton() {
            singleton = new EnumSingleton();
        }

        public EnumSingleton getInstance() {
            return singleton;
        }
    }
}
