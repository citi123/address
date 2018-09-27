package com.city.test;

import org.apache.ibatis.session.SqlSession;

public class AddressInfoDao {

    public static AddressInfo add(AddressBO addressBO,AddressType type,Integer parentId){
        AddressInfo addressInfo = new AddressInfo();

        addressInfo.setType(type.getCode());
        addressInfo.setCode(addressBO.getCode());
        addressInfo.setName(addressBO.getName());
        addressInfo.setParentId(parentId);
        SqlSession sqlSession = MyBatisHelper.getSession();
        int rows = sqlSession.insert("AddressDao.insert",addressInfo);
        if(rows != 1){
            throw new RuntimeException("DB error");
        }
        sqlSession.commit();
        return addressInfo;
    }

}
