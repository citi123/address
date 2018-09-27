package com.city.test;

import java.io.Serializable;

public class AddressTransferBO implements Serializable {
    private AddressBO addressBO;
    private AddressInfo addressInfo;

    public AddressBO getAddressBO() {
        return addressBO;
    }

    public void setAddressBO(AddressBO addressBO) {
        this.addressBO = addressBO;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }
}
