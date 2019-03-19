package com.plusbueno.plusbueno.data;
import com.stripe.android.model.Token;


public class PaymentInfo {
    private Token token;
    private int price;

    public Token getToken() {
        return token;
    }

    public void setToken(Token token) {
        this.token = token;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
