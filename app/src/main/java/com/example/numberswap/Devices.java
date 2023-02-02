package com.example.numberswap;

public class Devices {
    String id,deviceName;

    public Devices(String id, String deviceName) {
        this.id = id;
        this.deviceName = deviceName;
    }

    public Devices() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }
}
