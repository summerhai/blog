package canghailongyin.blog.designpattern.observer;

import java.util.Observable;
import java.util.Observer;

/**
 * Created by mingl on 2018-1-2.
 */
public class Reader implements MyReader{
    private String name;

    public Reader(String name){
        super();
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public void update(Writer writer) {
        System.out.println(name+"得到了通知，"+writer.getWriterName()+"发布了新书:"+writer.getNewBookName());
    }
}
