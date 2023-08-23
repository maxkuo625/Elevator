package com.lpx.Elevator_IF;

import java.util.List;

public interface Elevator_Service {
    //正序排序
    public List listSort(List list);

    //倒序排序
    // public List listSort01(List list);

    //隨機方法
    // public int random(int i);

    //體重增加
    public double weightadd();

    //體重減少
    public double weightcut(double weight);
}
