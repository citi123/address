package com.city.test.processer;

import com.alibaba.fastjson.JSONObject;
import com.city.test.*;

import java.util.List;

public class ProvinceProcessor {
    static String provinceUrl = "http://www.stats.gov.cn/tjsj/tjbz/tjyqhdmhcxhfdm/2017/index.html";

    public static void province() throws Exception {
        String content = Address.getContent(provinceUrl);
        content = Address.filter(content, Address.provincePatternStart, Address.provincePatternEnd);
        content = Address.filter(content, "<a href=", "</a>");
        List<AddressBO> addressList = Address.extraProvince(content, provinceUrl);

        for(AddressBO addressBO : addressList){
            AddressInfo addressInfo = AddressInfoDao.add(addressBO,AddressType.Province,null);
            AddressTransferBO transferBO = new AddressTransferBO();
            transferBO.setAddressBO(addressBO);
            transferBO.setAddressInfo(addressInfo);
            RabbitMQHelper.sendMsg(RabbitMQHelper.Province_Exchange,JSONObject.toJSONString(transferBO));
        }

    }
}
