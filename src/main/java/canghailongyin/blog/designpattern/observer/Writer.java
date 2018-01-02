package canghailongyin.blog.designpattern.observer;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by mingl on 2018-1-2.
 */
public class Writer implements MyWriter {
    private String writerName;
    private String newBookName;
    private Set<Reader> readerSet;

    public Writer(String writerName,String newBookName){
        this.writerName = writerName;
        this.newBookName = newBookName;
        readerSet = new HashSet<Reader>();
    }

    public String getWriterName() {
        return writerName;
    }

    public void setWriterName(String writerName) {
        this.writerName = writerName;
    }

    public String getNewBookName() {
        return newBookName;
    }

    public void setNewBookName(String newBookName) {
        this.newBookName = newBookName;
    }

    @Override
    public void addReader(Reader reader) {
        readerSet.add(reader);
    }

    @Override
    public void removeReader(Reader reader) {
        if(readerSet.contains(reader)){
            readerSet.remove(reader);
        }
    }

    @Override
    public void notifyReaders() {
        for(Reader reader : readerSet){
            reader.update(this);
        }
    }
}
