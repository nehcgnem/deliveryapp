package com.plusbueno.plusbueno.data;


public class LineItem {
    private String storeId;
    private String productId;
    private int qty;

    public LineItem(String storeId, String productId, int qty) {
        this.storeId = storeId;
        this.productId = productId;
        this.qty = qty;
    }

    public LineItem() {
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
