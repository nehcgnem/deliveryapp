package com.plusbueno.plusbueno.data;

public class ServerOrder {
    private long id;
    private String orderName;
    private String orderedProduct1;
    private int orderQuantity1 = 0;
    private String orderedProduct2;
    private int orderQuantity2 = 0;
    private String orderedProduct3;
    private int orderQuantity3 = 0;
    private String orderedProduct4;
    private int orderQuantity4 = 0;
    private String orderedProduct5;
    private int orderQuantity5 = 0;
    private String orderedProduct6;
    private int orderQuantity6 = 0;
    private String orderedProduct7;
    private int orderQuantity7 = 0;
    private String orderedProduct8;
    private int orderQuantity8 = 0;
    private String orderedProduct9;
    private int orderQuantity9 = 0;
    private String status;
    private String totalPrice;
    private int user;

    public String statusText() {
        switch (status) {
            case "0" : return "PROCESSING";
            case "1" : return "outforpickup".toUpperCase();
            case "2" : return "outfordelivery".toUpperCase();
            case "3" : return "delivered".toUpperCase();
            default: return "";
        }
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }

    public String getOrderedProduct1() {
        return orderedProduct1;
    }

    public void setOrderedProduct1(String orderedProduct1) {
        this.orderedProduct1 = orderedProduct1;
    }

    public int getOrderQuantity1() {
        return orderQuantity1;
    }

    public void setOrderQuantity1(int orderQuantity1) {
        this.orderQuantity1 = orderQuantity1;
    }

    public String getOrderedProduct2() {
        return orderedProduct2;
    }

    public void setOrderedProduct2(String orderedProduct2) {
        this.orderedProduct2 = orderedProduct2;
    }

    public int getOrderQuantity2() {
        return orderQuantity2;
    }

    public void setOrderQuantity2(int orderQuantity2) {
        this.orderQuantity2 = orderQuantity2;
    }

    public String getOrderedProduct3() {
        return orderedProduct3;
    }

    public void setOrderedProduct3(String orderedProduct3) {
        this.orderedProduct3 = orderedProduct3;
    }

    public int getOrderQuantity3() {
        return orderQuantity3;
    }

    public void setOrderQuantity3(int orderQuantity3) {
        this.orderQuantity3 = orderQuantity3;
    }

    public String getOrderedProduct4() {
        return orderedProduct4;
    }

    public void setOrderedProduct4(String orderedProduct4) {
        this.orderedProduct4 = orderedProduct4;
    }

    public int getOrderQuantity4() {
        return orderQuantity4;
    }

    public void setOrderQuantity4(int orderQuantity4) {
        this.orderQuantity4 = orderQuantity4;
    }

    public String getOrderedProduct5() {
        return orderedProduct5;
    }

    public void setOrderedProduct5(String orderedProduct5) {
        this.orderedProduct5 = orderedProduct5;
    }

    public int getOrderQuantity5() {
        return orderQuantity5;
    }

    public void setOrderQuantity5(int orderQuantity5) {
        this.orderQuantity5 = orderQuantity5;
    }

    public String getOrderedProduct6() {
        return orderedProduct6;
    }

    public void setOrderedProduct6(String orderedProduct6) {
        this.orderedProduct6 = orderedProduct6;
    }

    public int getOrderQuantity6() {
        return orderQuantity6;
    }

    public void setOrderQuantity6(int orderQuantity6) {
        this.orderQuantity6 = orderQuantity6;
    }

    public String getOrderedProduct7() {
        return orderedProduct7;
    }

    public void setOrderedProduct7(String orderedProduct7) {
        this.orderedProduct7 = orderedProduct7;
    }

    public int getOrderQuantity7() {
        return orderQuantity7;
    }

    public void setOrderQuantity7(int orderQuantity7) {
        this.orderQuantity7 = orderQuantity7;
    }

    public String getOrderedProduct8() {
        return orderedProduct8;
    }

    public void setOrderedProduct8(String orderedProduct8) {
        this.orderedProduct8 = orderedProduct8;
    }

    public int getOrderQuantity8() {
        return orderQuantity8;
    }

    public void setOrderQuantity8(int orderQuantity8) {
        this.orderQuantity8 = orderQuantity8;
    }

    public String getOrderedProduct9() {
        return orderedProduct9;
    }

    public void setOrderedProduct9(String orderedProduct9) {
        this.orderedProduct9 = orderedProduct9;
    }

    public int getOrderQuantity9() {
        return orderQuantity9;
    }

    public void setOrderQuantity9(int orderQuantity9) {
        this.orderQuantity9 = orderQuantity9;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getUser() {
        return user;
    }

    public void setUser(int user) {
        this.user = user;
    }
}
