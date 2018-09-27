package com.city.test.processer;

import com.alibaba.fastjson.JSONObject;
import com.city.test.*;

import java.util.List;

public class CountryProcesser {

    public static void deal(AddressBO addressBO, AddressInfo addressInfo) throws Exception{
        // get city
        String content = Address.getContent(addressBO.getChildrenUrl());
        content = Address.filter(content,Address.countryPatternStart,Address.countryPatternEnd);
        content = Address.filter(content,"<a href=", "</a>");
        List<AddressBO> countrys = Address.extraCity(content, addressBO.getChildrenUrl());

        for(AddressBO country : countrys){
            AddressInfo info = AddressInfoDao.add(country,AddressType.Country,addressInfo.getId());
            AddressTransferBO transferBO = new AddressTransferBO();
            transferBO.setAddressBO(country);
            transferBO.setAddressInfo(info);
            RabbitMQHelper.sendMsg(RabbitMQHelper.City_Excahnge,JSONObject.toJSONString(transferBO));
        }
    }

}
