package com.plusbueno.plusbueno.data;

import com.plusbueno.plusbueno.parser.exception.AuthorizationException;
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException;

public interface Cart {
    boolean checkout() throws NetworkErrorException, AuthorizationException;
    void addItem(long storeId, long productId, int qty);
    void removeItem(long storeId, long productId);
    int getPrice() throws NetworkErrorException, AuthorizationException;

}
