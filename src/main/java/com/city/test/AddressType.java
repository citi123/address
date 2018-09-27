package com.city.test;

public enum AddressType {
    Province(1),
    City(2),
    Country(3),
    Town(4),
    Village(5);
    private int code;
    AddressType(int code){
        this.code = code;
    }

    public int getCode() {
        return code;
    }

}
