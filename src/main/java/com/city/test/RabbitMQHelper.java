package com.city.test;

import com.alibaba.fastjson.JSONObject;
import com.city.test.processer.CityProcesser;
import com.city.test.processer.CountryProcesser;
import com.city.test.processer.TownProcesser;
import com.city.test.processer.VillageProcesser;
import com.rabbitmq.client.*;

import java.io.IOException;

public class RabbitMQHelper {

    public static String Province_Exchange = "exchange.province";
    public static String Province_Queue = "queue.exchange.province";

    public static String City_Excahnge = "exchange.city";
    public static String City_Queuue = "queue.exchange.city";

    public static String Country_Excahnge = "exchange.country";
    public static String Country_Queuue = "queue.exchange.country";

    public static String Town_Excahnge = "exchange.town";
    public static String Town_Queuue = "queue.exchange.town";

    public static String Village_Excahnge = "exchange.village";
    public static String Village_Queuue = "queue.exchange.village";

    static ConnectionFactory connectionFactory;
    static Channel sendChannel;

    static {
        connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("10.100.32.88");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("guest");
        connectionFactory.setPassword("guest");

        try {
            Connection connection = connectionFactory.newConnection();
            sendChannel = connection.createChannel();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("mq启动失败");
        }
    }

    public static void sendMsg(String exchange, String msg) throws Exception {
        sendChannel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true);
        sendChannel.basicPublish(exchange, "", MessageProperties.PERSISTENT_BASIC, msg.getBytes("UTF-8"));
    }

    public static void receiveMsg(String exchange, String queueName) {
        try {
            Connection connection = connectionFactory.newConnection();
            Channel receiveChannel = connection.createChannel();

            receiveChannel.exchangeDeclare(exchange, BuiltinExchangeType.FANOUT, true);
            receiveChannel.queueDeclare(queueName, false, false, false, null);
            receiveChannel.queueBind(queueName, exchange, "");

            Consumer consumer = new DefaultConsumer(receiveChannel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String message = new String(body, "UTF-8");
                    AddressTransferBO addressTransferBO = JSONObject.parseObject(message, AddressTransferBO.class);
                    try{
                        if (addressTransferBO.getAddressInfo().getType() == AddressType.Province.getCode()) {
                            CityProcesser.deal(addressTransferBO.getAddressBO(),addressTransferBO.getAddressInfo());
                        }else if(addressTransferBO.getAddressInfo().getType() == AddressType.City.getCode()){
                            CountryProcesser.deal(addressTransferBO.getAddressBO(),addressTransferBO.getAddressInfo());
                        }else if(addressTransferBO.getAddressInfo().getType() == AddressType.Country.getCode()){
                            TownProcesser.deal(addressTransferBO.getAddressBO(),addressTransferBO.getAddressInfo());
                        }else if(addressTransferBO.getAddressInfo().getType() == AddressType.Town.getCode()) {
                            VillageProcesser.deal(addressTransferBO.getAddressBO(),addressTransferBO.getAddressInfo());
                        }
                    }catch (Exception e){
                        System.out.println("ERROR: " + e.getMessage());
                    }
                }
            };
            receiveChannel.basicConsume(queueName, true, consumer);
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("mq启动失败");
        }
    }

    public static void main(String[] args) throws Exception {

        sendMsg(Province_Exchange, "wo qu le");

        receiveMsg(Province_Exchange, Province_Queue);

        System.in.read();
    }

}
