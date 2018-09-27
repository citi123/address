package com.city.test.processer;
import com.city.test.AddressInfo;
import com.city.test.AddressBO;

import com.alibaba.fastjson.JSONObject;
import com.city.test.*;

import java.util.List;

public class CityProcesser {

    public static void deal(AddressBO addressBO, AddressInfo addressInfo) throws Exception{
        // get city
        String content = Address.getContent(addressBO.getChildrenUrl());
        content = Address.filter(content,Address.cityPatternStart,Address.cityPatternEnd);
        content = Address.filter(content,"<a href=", "</a>");
        List<AddressBO> citys = Address.extraCity(content, addressBO.getChildrenUrl());

        for(AddressBO city : citys){
            AddressInfo info = AddressInfoDao.add(city,AddressType.City,addressInfo.getId());
            AddressTransferBO transferBO = new AddressTransferBO();
            transferBO.setAddressBO(city);
            transferBO.setAddressInfo(info);
            RabbitMQHelper.sendMsg(RabbitMQHelper.City_Excahnge,JSONObject.toJSONString(transferBO));
        }
    }

}
