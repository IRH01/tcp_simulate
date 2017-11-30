package com.irh.material.basics.netty.chapter10_3.pojo;

/**
 * Created by Iritchie.ren on 2017/3/6.
 */
public class Order{

    private long orderNumber;
    private Customer customer;
    private Address address;
    private Shipping shipping;
    private Address shipTo;
    private Float total;

    public Order(){
    }

    public long getOrderNumber(){
        return orderNumber;
    }

    public void setOrderNumber(long orderNumber){
        this.orderNumber = orderNumber;
    }

    public Customer getCustomer(){
        return customer;
    }

    public void setCustomer(Customer customer){
        this.customer = customer;
    }

    public Address getAddress(){
        return address;
    }

    public void setAddress(Address address){
        this.address = address;
    }

    public Shipping getShipping(){
        return shipping;
    }

    public void setShipping(Shipping shipping){
        this.shipping = shipping;
    }

    public Address getShipTo(){
        return shipTo;
    }

    public void setShipTo(Address shipTo){
        this.shipTo = shipTo;
    }

    public Float getTotal(){
        return total;
    }

    public void setTotal(Float total){
        this.total = total;
    }
}
