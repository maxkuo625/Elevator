package com.lpx.Elevator_IF;

import java.util.Collections;
import java.util.List;

public class Elevator_ServiceImpl implements Elevator_Service {

    @Override
    public List listSort(List list) {
        Collections.sort(list);
        return list;
    }

    // @Override
    // public List listSort01(List list) {
    //     Collections.sort(list,Collections.reverseOrder());
    //     return list;
    // }

    // public int random(int i){
    //     int num = 0;
    //     for (int m=1;m<=i;m++){
    //         num+=(Math.random()*80)+20;
    //     }
    //     return num;
    // }

    public double weightadd(){
        return  ((Math.random()*80)+20);
    }

    public double weightcut(double weight){
        weight= weight-((Math.random()*80)+20);
        return weight;
    }
}
