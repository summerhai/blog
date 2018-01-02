package canghailongyin.blog.designpattern.observer;

/**
 * Created by mingl on 2018-1-2.
 */
public interface MyWriter {
    void addReader(Reader reader);
    void removeReader(Reader reader);
    void notifyReaders();
}
