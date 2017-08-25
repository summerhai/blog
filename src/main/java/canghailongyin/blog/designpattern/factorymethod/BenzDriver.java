package canghailongyin.blog.designpattern.factorymethod;

import canghailongyin.blog.designpattern.car.Benz;
import canghailongyin.blog.designpattern.car.Car;

/**
 * Created by mingl on 2017-8-14.
 */
public class BenzDriver extends Driver{
    @Override
    public Car getCar() {
        return new Benz();
    }
}
