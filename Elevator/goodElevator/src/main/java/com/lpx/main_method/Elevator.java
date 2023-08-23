package com.lpx.main_method;


import com.lpx.limiting_surface.View_Logic;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class Elevator {
    public static void main(String [] args){
        //運用Spring來獲取View對象
        ApplicationContext ac = new ClassPathXmlApplicationContext("mySpring.xml");
        View_Logic view = ac.getBean(View_Logic.class);
        view.init();

    }
}
