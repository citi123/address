package com.city.test.processer;

import com.alibaba.fastjson.JSONObject;
import com.city.test.*;

import java.util.List;

public class TownProcesser {

    public static void deal(AddressBO addressBO, AddressInfo addressInfo) throws Exception{
        // get city
        String content = Address.getContent(addressBO.getChildrenUrl());
        content = Address.filter(content,Address.townPatternStart,Address.townPatternEnd);
        content = Address.filter(content,"<a href=", "</a>");
        List<AddressBO> towns = Address.extraCity(content, addressBO.getChildrenUrl());

        for(AddressBO town : towns){
            AddressInfo info = AddressInfoDao.add(town,AddressType.Town,addressInfo.getId());
            AddressTransferBO transferBO = new AddressTransferBO();
            transferBO.setAddressBO(town);
            transferBO.setAddressInfo(info);
            RabbitMQHelper.sendMsg(RabbitMQHelper.City_Excahnge,JSONObject.toJSONString(transferBO));
        }
    }

}
