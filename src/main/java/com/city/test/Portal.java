package com.city.test;

import com.city.test.processer.ProvinceProcessor;

public class Portal {
    static {
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Province_Exchange,RabbitMQHelper.Province_Queue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Province_Exchange,RabbitMQHelper.Province_Queue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Province_Exchange,RabbitMQHelper.Province_Queue);

        RabbitMQHelper.receiveMsg(RabbitMQHelper.City_Excahnge,RabbitMQHelper.City_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.City_Excahnge,RabbitMQHelper.City_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.City_Excahnge,RabbitMQHelper.City_Queuue);

        RabbitMQHelper.receiveMsg(RabbitMQHelper.Country_Excahnge,RabbitMQHelper.Country_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Country_Excahnge,RabbitMQHelper.Country_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Country_Excahnge,RabbitMQHelper.Country_Queuue);

        RabbitMQHelper.receiveMsg(RabbitMQHelper.Town_Excahnge,RabbitMQHelper.Town_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Town_Excahnge,RabbitMQHelper.Town_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Town_Excahnge,RabbitMQHelper.Town_Queuue);

        RabbitMQHelper.receiveMsg(RabbitMQHelper.Village_Excahnge,RabbitMQHelper.Village_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Village_Excahnge,RabbitMQHelper.Village_Queuue);
        RabbitMQHelper.receiveMsg(RabbitMQHelper.Village_Excahnge,RabbitMQHelper.Village_Queuue);
    }
    public static void main(String[] args) throws Exception{
        ProvinceProcessor.province();
        while (true){
            Thread.sleep(1000 * 60);
        }
    }
}
