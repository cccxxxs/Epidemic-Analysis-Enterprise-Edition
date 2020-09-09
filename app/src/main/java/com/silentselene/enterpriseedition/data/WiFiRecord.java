package com.silentselene.enterpriseedition.data;


public class WiFiRecord {
    private int ID = -1;
    private String ALIAS;
    private String MAC;

    public WiFiRecord() {
    }

    public WiFiRecord(String ALIAS, String MAC) {
        this.ALIAS = ALIAS;
        this.MAC = MAC;
    }

    // 用于Debug页面的显示
    @Override
    public String toString() {
        String result = " ";
        result += "ID: " + this.ID + "; ";
        result += "MAC: " + this.MAC + "; ";
        result += "ALIAS: " + this.ALIAS + "; ";
        return result;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getALIAS() {
        return ALIAS;
    }

    public void setALIAS(String ALIAS) {
        this.ALIAS = ALIAS;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }
}
