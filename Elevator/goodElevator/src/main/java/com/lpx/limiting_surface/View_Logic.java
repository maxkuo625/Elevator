package com.lpx.limiting_surface;


import com.lpx.Elevator_IF.Elevator_Service;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.text.ParseException;
import java.util.HashSet;
import java.util.Set;

import java.util.concurrent.TimeUnit;

import static java.lang.Thread.sleep;

public class View_Logic extends JFrame {
    private JTextField show01,show02;
    private JPanel jPanel;
    private int floor=1;
    private boolean flag=true;
    JButton jButton[] =new JButton[12];
    double weight=0;
    private double floorWeight=500;
    //總樓層
    private int storey=10;
    //存放乘客選擇停靠樓層的集合
    public List<Integer> list=new ArrayList<Integer>();
    Set<Integer> intSet = new HashSet<Integer>();
    //計算程式執行秒數開頭
    long startTime = System.nanoTime();
    String stringToSearch;

    public void init() {
       init_v();
    }

    public void init_v() {
        //文字顯示
        show01=new JTextField("當前樓層:"+floor + "樓");
        show01.setFont(new Font("fangsong",Font.BOLD,16));
        show01.setBackground(Color.WHITE);
        show01.setBounds(20,15,300,80);
        add(show01);

        show02=new JTextField("輸入樓層:");
        show02.setFont(new Font("fangsong",Font.BOLD,16));
        show02.setBackground(Color.WHITE);
        add(show02);
        show02.setBounds(20,110,300,80);

        
        //建立電梯按鈕區塊
        this.setLayout(null);
        jPanel=new JPanel();
        jPanel.setBounds(20,200,300,500);
        this.add(jPanel);
        GridLayout gridLayout = new GridLayout(7,2,50,5);

        ApplicationContext ac = new ClassPathXmlApplicationContext("mySpring.xml");
        Elevator_Service service = (Elevator_Service) ac.getBean("service");

        for(int i=0;i<=jButton.length;i++){
            int q=i+1;
            if (i<=9){
                jButton[i] =new JButton(String.valueOf(q));
                jPanel.add(jButton[i]);
                int finalI = i;
                //為按鈕建立監聽器
                jButton[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        jButton[finalI].setBackground(Color.white);
                       //忽略重複的輸入(沒成功)
                        boolean k = Arrays.asList(list).contains(q);
                        if(k == false){
                            list.add(q);
                        }else{
                            show02.setText("樓層重複選取");
                        }
                        
                        //boolean if_in=false;
                        //for(i=0;i<list.length;i++){
                        //    if( list[i]==q ){
                        //      if_in=true;
                        //    }
                        //  }
                        weight=weight+service.weightadd();
                        System.out.println(String.format("當前重量"+"%.2f", weight)+"KG");
                        show02.setText("電梯即將停靠" + String.valueOf(list) + "樓");
                        if(list.get(list.size()-1)<=floor) {

                            flag = false;
                        }else if(list.get(list.size()-1)>=floor) {

                            flag = true;
                        }
                    }
                });
            //電梯開門按鈕控制
            }else if (i==10){
                jButton[i] =new JButton("<>");
                jPanel.add(jButton[i]);
                jButton[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                            show01.setText("電梯開門");
                            try {
                                    sleep(1000);
                                } catch (InterruptedException e1) {
                                    e1.printStackTrace();
                                }
                            show01.setText("電梯門要關了");
                    }
                });
            //電梯關門按鈕控制
            }else if(i==11) {
                jButton[i] =new JButton("><");
                jPanel.add(jButton[i]);
                jButton[i].addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //將乘客所選的樓層進行排序
                        service.listSort(list);
                        show02.setText(String.valueOf(list));
                        show01.setText("電梯關門");
                        if (weight <= floorWeight){
                            if (list.size() > 0) {
                                if (flag == true) {
                                    for (int m = floor; m <= storey; m++) {

                                        // show01.setText("當前到達樓層"+m);
                                        System.out.println("當前樓層" + m);
                                        try {
                                            sleep(1000);
                                            // jButton[m-1].setBackground(Color.white);
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        }

                                        System.out.println("*****************");
                                        //電梯上樓,若樓層重複選取則會略過
                                        for (int n = 0; n < list.size(); n++) {
                                            if (m == list.get(n)) {
                                                weight = service.weightcut(weight);
//                                                if (weight<0){
//                                                    System.out.println("當前樓層"+0.00+"KG");
//                                                }
                                                if (weight > 0 && m != list.get(list.size() - 1)) {
                                                    System.out.println(String.format("當前重量" + "%.2f", weight) + "KG");
                                                }
                                                jButton[m - 1].setBackground(null);
                                                show01.setText("電梯開門-當前樓層為" + list.get(n) + "樓");
                                                JOptionPane.showMessageDialog(null, "到達樓層：" + m + "樓,電梯開門", "樓層到達訊息", JOptionPane.PLAIN_MESSAGE);
                                                //當電梯抵達頂樓之後將會清空陣列
                                                if (m == list.get(list.size() - 1)) {
                                                    jButton[m - 1].setBackground(null);
                                                    show01.setText("電梯開門-當前樓層為" + list.get(n) + "樓");
                                                    weight = 0;
                                                    System.out.println("當前重量" + 0.00 + "KG");
                                                    list.clear();
                                                    //將最終停靠樓層指派給所在樓層的變數
                                                    floor = m;
                                                    flag = true;
                                                    try {
                                                        sleep(2000);
                                                    } catch (InterruptedException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    show01.setText("當前電梯在第：" + floor + " 樓");
                                                    show02.setText("當前電梯已經沒人了");
                                                    return;
                                                }
                                                break;
                                            }

                                        }
                                    }
                                } else {
                                    for (int m = floor; m >= 1; m--) {

                                        // show01.setText("當前到達樓層"+m);
                                        try {
                                            sleep(1000);
                                            // jButton[m-1].setBackground(Color.white);
                                        } catch (InterruptedException e1) {
                                            e1.printStackTrace();
                                        }
                                        System.out.println(". . . . . .");
                                        System.out.println("當前樓層" + m + "樓");
                                        //電梯下樓,若樓層重複選取則會略過
                                        for (int n = list.size() - 1; n >= 0; n--) {
                                            if (m == list.get(n)) {
                                                weight = service.weightcut(weight);

                                                if (weight > 0 && m != list.get(0)) {
                                                    System.out.println(String.format("當前重量" + "%.2f", weight) + "KG");
                                                }

                                                jButton[m - 1].setBackground(null);
                                                show01.setText("電梯開門-當前樓層為" + list.get(n) + "樓");
                                                JOptionPane.showMessageDialog(null, "到達樓層：" + m + "樓,電梯開門", "樓層到達訊息", JOptionPane.PLAIN_MESSAGE);

                                                //當電梯抵達一樓之後將會清空陣列
                                                if (m == list.get(0)) {
                                                    jButton[m - 1].setBackground(null);
                                                    show01.setText("電梯開門-當前樓層為" + list.get(n) + "樓");
                                                    weight = 0;
                                                    System.out.println("當前重量" + 0.00 + "KG");
                                                    list.clear();
                                                    //將最終停靠樓層指派給所在樓層的變數
                                                    floor = m;
                                                    flag = true;
                                                    try {
                                                        sleep(2000);
                                                    } catch (InterruptedException e1) {
                                                        e1.printStackTrace();
                                                    }
                                                    show01.setText("當前電梯在第：" + floor + " 樓");
                                                    show02.setText("當前電梯已經沒人了");
                                                    return;
                                                }
                                                break;
                                                
                                            }

                                        }
                                    }
                                }
                            } else {
                                show01.setText("未選擇樓層！");
                            }
                        }else {
                            show01.setText("電梯超重！");
                            list.clear();
                            for (int i=0;i<=11;i++){
                                jButton[i].setBackground(null);
                            }
                            weight=0;
                            return;
                        }
                    }
                });
            }
            jPanel.setLayout(gridLayout);
        }
        //計算程式執行秒數結尾
        long endTime=System.nanoTime();
        //結束時秒數-開始時秒數得到執行時間
        System.out.println("執行時間： "+(endTime-startTime)+" NS ");
        //設置邊框
        jPanel.setBorder(BorderFactory.createTitledBorder("電梯按鈕"));
        //設置視窗標題
        this.setTitle("電梯管理系統");
        // this.setSize(300, 700);
        this.setBounds(800,150,350,700);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);

        //設置窗體大小是否可以更改
        this.setResizable(false);


    }
}