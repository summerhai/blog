package canghailongyin.blog.designpattern.factorymethod;

import canghailongyin.blog.designpattern.car.Audi;
import canghailongyin.blog.designpattern.car.Car;

/**
 * Created by mingl on 2017-8-14.
 */
public class AudiDriver extends Driver {

    @Override
    public Car getCar() {
        return new Audi();
    }
}
