package com.city.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Observable;

public class Center extends Observable {

    public void input() throws Exception{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true){
            System.out.print("I'm Center,please input some words:");
            String line = reader.readLine();

            if(Objects.equals(line,"exit")){
                setChanged();
                notifyObservers("exit");
                break;
            }else{
                setChanged();
                notifyObservers(line);
            }
        }
    }
}
