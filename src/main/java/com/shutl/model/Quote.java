package com.shutl.model;

import com.shutl.enums.Vehicle;
import org.json.simple.JSONArray;

public class Quote {

    private String pickupPostcode;
    private String deliveryPostcode;
    /**
     * BigDecimal type can be used for price var instead of Long (since BigDecimal is more precise), as suggested by
     * official Oracle Java docs - https://docs.oracle.com/javase/tutorial/java/nutsandbolts/datatypes.html
     * A different type may be used to improve space complexity.
     */
    private Long price;
    private Vehicle vehicle;
    private JSONArray priceList;

    public Quote() {
    }

    public Quote(String pickupPostcode, String deliveryPostcode) {
        this.setPickupPostcode(pickupPostcode);
        this.setDeliveryPostcode(deliveryPostcode);
    }

    public Quote(String pickupPostcode, String deliveryPostcode, Long price) {
        this.setPickupPostcode(pickupPostcode);
        this.setDeliveryPostcode(deliveryPostcode);
        this.setPrice(price);
    }

    public Quote(String pickupPostcode, String deliveryPostcode, Long price, String vehicle) {
        this.setPickupPostcode(pickupPostcode);
        this.setDeliveryPostcode(deliveryPostcode);
        this.setPrice(price);
        this.setVehicle(Vehicle.valueOf(vehicle));
    }

    public Quote(String pickupPostcode, String deliveryPostcode, Long price, String vehicle, JSONArray priceList) {
        this.setPickupPostcode(pickupPostcode);
        this.setDeliveryPostcode(deliveryPostcode);
        this.setPrice(price);
        this.setVehicle(Vehicle.valueOf(vehicle));
        this.setPriceList(priceList);
    }

    public String getPickupPostcode() {
        return pickupPostcode;
    }

    public void setPickupPostcode(String pickupPostcode) {
        // Get rid of the spaces
        this.pickupPostcode = pickupPostcode.replaceAll("\\s+", "");
    }

    public String getDeliveryPostcode() {
        return deliveryPostcode;
    }

    public void setDeliveryPostcode(String deliveryPostcode) {
        // Get rid of the spaces
        this.deliveryPostcode = deliveryPostcode.replaceAll("\\s+", "");
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        this.price = price;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public JSONArray getPrice_list() {
        return priceList;
    }

    public void setPriceList(JSONArray priceList) {
        this.priceList = priceList;
    }
}
