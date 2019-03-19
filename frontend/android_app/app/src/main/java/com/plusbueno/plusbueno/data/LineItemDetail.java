package com.plusbueno.plusbueno.data;

/**
 * Created by LZMA on 2018/11/17.
 */
public class LineItemDetail {
    private Product product;
    private String storeId;
    private int qty;

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public LineItemDetail(Product product, String storeId, int qty) {
        this.product = product;
        this.storeId = storeId;
        this.qty = qty;
    }

    public LineItemDetail() {
    }
}
