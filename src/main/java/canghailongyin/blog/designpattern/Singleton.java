package canghailongyin.blog.designpattern;

/**
 * Created by mingl on 2017-7-25.
 * 设计模式之--单例模式
 * 懒汉，饿汉，双重校验锁，枚举和静态内部类
 */
public class Singleton {

    //1.懒汉式 线程不安全 多线程下会出问题
    /**
    private Singleton(){
    }
    private static Singleton instance;
    public static Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }
    */

    //2.懒汉式 线程安全 效率很低
    /**
    private Singleton(){
    }
    private static Singleton instance;
    public static synchronized Singleton getInstance(){
        if(instance == null){
            instance = new Singleton();
        }
        return instance;
    }
    */

    //3.饿汉式 线程不安全 不一定正确，因为先进行了instance初始化
    /**
    private Singleton(){
    }
    private static Singleton instance = new Singleton();
    public static Singleton getInstance(){
        return instance;
    }
    */

    //4.饿汉式 将instance初始化放在static中，保证被执行
    /**
    private Singleton(){
    }
    private static Singleton instance = null;
    static {
        instance = new Singleton();
    }
    public static Singleton getInstance(){
        return instance;
    }
    */

    //5.静态内部类
    /**
    private Singleton(){
    }
    private static class SingletonHelper{
        private static final Singleton INSTANCE = new Singleton();
    }
    public static Singleton getInstance(){
        return SingletonHelper.INSTANCE;
    }
    */

    //6.枚举 自由序列化，线程安全，保证单例
    //详见EnumSingleton类
    public static void main(String[] args){
        EnumSingleton obj1 = EnumSingleton.getInstance();
        EnumSingleton obj2 = EnumSingleton.getInstance();
        //输出结果：obj1==obj2?true
        System.out.println("obj1==obj2?" + (obj1==obj2));
    }

    //7.双重校验
    /**
    private Singleton(){
    }
    private volatile static Singleton instance;
    public static synchronized Singleton getInstance(){
        if(instance == null){
            synchronized (Singleton.class){
                if(instance == null){
                    instance = new Singleton();
                }
            }
        }
        return instance;
    }
    */
    /**
     * 总结
     * 有两个问题需要注意：
     1.如果单例由不同的类装载器装入，那便有可能存在多个单例类的实例。假定不是远端存取，例如一些servlet容器对每个servlet使用完全
     不同的类装载器，这样的话如果有两个servlet访问一个单例类，它们就都会有各自的实例。
     对第一个问题修复的办法是：
     */
    /**
    private static Class getClass(String classname)
            throws ClassNotFoundException {
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();

        if(classLoader == null)
            classLoader = Singleton.class.getClassLoader();

        return (classLoader.loadClass(classname));
    }
     */
    /**
     * 2.如果Singleton实现了java.io.Serializable接口，那么这个类的实例就可能被序列化和复原。不管怎样，如果你序列化一个单例类的对象，
     * 接下来复原多个那个对象，那你就会有多个单例类的实例。
     * 解决办法，可以实现readResolve方法
     */
    /**
    public class Singleton implements java.io.Serializable {
        public static Singleton INSTANCE = new Singleton();

        protected Singleton() {

        }
        private Object readResolve() {
            return INSTANCE;
        }
    }
     */
    //推荐静态内部类、枚举、双重校验
}

