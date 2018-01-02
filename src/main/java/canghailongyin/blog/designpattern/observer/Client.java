package canghailongyin.blog.designpattern.observer;

/**
 * Created by mingl on 2018-1-2.
 */
public class Client {
    public static void main(String[] args) {
        Writer writer = new Writer("zhangsan","haha");
        Reader reader1 = new Reader("lisi");
        Reader reader2 = new Reader("lisi1");
        writer.addReader(reader1);
        writer.addReader(reader2);
        writer.notifyReaders();
        writer.removeReader(reader2);
        writer.setNewBookName("yes");
        writer.notifyReaders();
    }

}
