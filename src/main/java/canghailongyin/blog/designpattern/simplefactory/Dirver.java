package canghailongyin.blog.designpattern.simplefactory;

import canghailongyin.blog.designpattern.car.Audi;
import canghailongyin.blog.designpattern.car.BMW;
import canghailongyin.blog.designpattern.car.Benz;
import canghailongyin.blog.designpattern.car.Car;

/**
 * Created by mingl on 2017-8-14.
 * 工厂类
 */
public class Dirver {
    public static Car getCar(String type) throws Exception{
        if(type.equals("Benz")){
            return new Benz();
        }else if(type.equals("Audi")){
            return new Audi();
        }else if(type.equals("Bmw")){
            return new BMW();
        }else{
            throw new Exception();
        }
    }
}
