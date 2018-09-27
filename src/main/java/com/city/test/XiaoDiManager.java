package com.city.test;

import java.util.Observable;
import java.util.Observer;

public class XiaoDiManager {

    public static void main(String[] args) throws Exception {
        XiaoDi xiaoDi1 = new XiaoDi("xiao lao di 1");
        XiaoDi xiaoDi2 = new XiaoDi("xiao lao di 2");
        XiaoDi xiaoDi3 = new XiaoDi("xiao lao di 3");
        XiaoDi xiaoDi4 = new XiaoDi("xiao lao di 4");
        XiaoDi xiaoDi5 = new XiaoDi("xiao lao di 5");

        Center center = new Center();


        center.addObserver(xiaoDi1);
        center.addObserver(xiaoDi2);
        center.addObserver(xiaoDi3);
        center.addObserver(xiaoDi4);
        center.addObserver(xiaoDi5);
        center.input();
        
        while (true) {
            if (!xiaoDi1.isGo()
                    && !xiaoDi2.isGo()
                    && !xiaoDi3.isGo()
                    && !xiaoDi4.isGo()
                    && !xiaoDi5.isGo()) {
                break;
            } else {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        System.out.println("全部结束了。。。。。 ");
    }

}

class XiaoDi implements Observer {

    private boolean go;

    private String name;

    public XiaoDi(String name) {
        this.name = name;
    }

    @Override
    public void update(Observable o, Object arg) {
        if ("exit".equals(arg)) {
            go = false;
        }
        System.out.println("I'm observer " + name + " ," + arg);
    }

    public boolean isGo() {
        return go;
    }

    public void setGo(boolean go) {
        this.go = go;
    }
}
