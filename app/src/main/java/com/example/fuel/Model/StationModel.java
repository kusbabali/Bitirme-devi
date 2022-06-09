package com.example.fuel.Model;

public class StationModel {

    //getireceğimiz verinin tipini ve ismini belirlemek için yazarız
    String brand;//marka
    String stationAdress;//istasyon adresi
    String price;//ücret

    public StationModel(String brand, String stationAdress, String price) {
        this.brand = brand;
        this.stationAdress = stationAdress;
        this.price = price;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getStationAdress() {
        return stationAdress;
    }

    public void setStationAdress(String stationAdress) {
        this.stationAdress = stationAdress;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
