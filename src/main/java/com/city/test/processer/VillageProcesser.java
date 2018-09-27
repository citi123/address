package com.city.test.processer;

import com.alibaba.fastjson.JSONObject;
import com.city.test.*;

import java.util.List;

public class VillageProcesser {

    public static void deal(AddressBO addressBO, AddressInfo addressInfo) throws Exception{
        // get city
        String content = Address.getContent(addressBO.getChildrenUrl());
        content = Address.filter(content,Address.villagePatternStart,Address.villagePatternEnd);
        content = Address.filter(content,"<td>", "</td>");
        List<AddressBO> villages = Address.extraVillage(content);

        for(AddressBO village : villages){
            AddressInfo info = AddressInfoDao.add(village,AddressType.Village,addressInfo.getId());
            AddressTransferBO transferBO = new AddressTransferBO();
            transferBO.setAddressBO(village);
            transferBO.setAddressInfo(info);
            RabbitMQHelper.sendMsg(RabbitMQHelper.Village_Excahnge,JSONObject.toJSONString(transferBO));
        }
    }

}
