package com.plusbueno.plusbueno.data;

import android.util.Log;
import android.util.Pair;
import com.plusbueno.plusbueno.parser.LoginManager;
import com.plusbueno.plusbueno.parser.UniversalParser;
import com.plusbueno.plusbueno.parser.exception.AuthorizationException;
import com.plusbueno.plusbueno.parser.exception.NetworkErrorException;
import com.plusbueno.plusbueno.parser.util.HttpUtil;

import java.util.*;

/**
 * Created by LZMA on 2018/11/7.
 */
public class LocalCart{
    private static ArrayList<LineItem> cartItems = new ArrayList<>();

    public static String checkout() throws NetworkErrorException, AuthorizationException {
        if (cartItems.size() > 9) {
            return null;
        }
        ServerOrder order = new ServerOrder();
        Date now = new Date();
        order.setOrderName(""+now.getTime());
        order.setStatus("0");
        order.setTotalPrice(""+getPrice());

        if (cartItems.size() >= 1) {
            order.setOrderedProduct1(cartItems.get(0).getProductId());
            order.setOrderQuantity1(cartItems.get(0).getQty());
        }
        if (cartItems.size() >= 2) {
            order.setOrderedProduct2(cartItems.get(1).getProductId());
            order.setOrderQuantity2(cartItems.get(1).getQty());
        }
        if (cartItems.size() >= 3) {
            order.setOrderedProduct3(cartItems.get(2).getProductId());
            order.setOrderQuantity3(cartItems.get(2).getQty());
        }
        if (cartItems.size() >= 4) {
            order.setOrderedProduct4(cartItems.get(3).getProductId());
            order.setOrderQuantity4(cartItems.get(3).getQty());
        }
        if (cartItems.size() >= 5) {
            order.setOrderedProduct5(cartItems.get(4).getProductId());
            order.setOrderQuantity5(cartItems.get(4).getQty());
        }
        if (cartItems.size() >= 6) {
            order.setOrderedProduct6(cartItems.get(5).getProductId());
            order.setOrderQuantity6(cartItems.get(5).getQty());
        }
        if (cartItems.size() >= 7) {
            order.setOrderedProduct7(cartItems.get(6).getProductId());
            order.setOrderQuantity7(cartItems.get(6).getQty());
        }
        if (cartItems.size() >= 8) {
            order.setOrderedProduct8(cartItems.get(7).getProductId());
            order.setOrderQuantity8(cartItems.get(7).getQty());
        }
        if (cartItems.size() >= 9) {
            order.setOrderedProduct9(cartItems.get(8).getProductId());
            order.setOrderQuantity9(cartItems.get(8).getQty());
        }

        HttpUtil.post(UniversalParser.BASE_URL_RESTFUL+"/createorder/" + LoginManager.getUsername() + "/",
                order, ServerOrder.class);

        cartItems.clear();
        return order.getOrderName();
    }

    public static void addItem(String storeName, String productName, int qty) {
        for(LineItem lineItem : cartItems) {
            if (storeName.equals(lineItem.getStoreId())   && productName .equals(lineItem.getProductId()) ) {
                lineItem.setQty(lineItem.getQty() + qty);
                return;
            }
        }
        cartItems.add(new LineItem(storeName, productName, qty));
    }

    public static void removeItem(String storeName, String productName) {
        for(LineItem lineItem : cartItems) {
            if (storeName .equals(lineItem.getStoreId())   && productName.equals(lineItem.getProductId())) {
                cartItems.remove(lineItem);
                return;
            }
        }

    }

    public static int getPrice() throws NetworkErrorException, AuthorizationException {
        Map<Store, List<LineItemDetail>> detailMap = getGroupedProduct();
        int result = 0;

        for (Store store : detailMap.keySet()) {
            for (LineItemDetail detail : detailMap.get(store)) {
                result += detail.getProduct().getPrice() * detail.getQty();
            }
        }
//        for(LineItem lineItem : cartItems) {
//            Product product = UniversalParser.get(UniversalParser.BASE_URL_RESTFUL+"/store/"+lineItem.getStoreId()+"/"+lineItem.getProductId(),
//                    Product.class);
//            result += product.getPrice();
//        }
        return result;
    }

    private static void sendOrder() throws NetworkErrorException, AuthorizationException {
        Date date = new Date();  // now

    }

    public static Map<Store, List<LineItemDetail>> getGroupedProduct() throws NetworkErrorException, AuthorizationException {
        Map<Store, List<LineItemDetail>> result = new HashMap<>();
        for(LineItem item : cartItems) {
            //Product product = UniversalParser.get(UniversalParser.BASE_URL_RESTFUL + "/store/" + item.getStoreId() + "/" + item.getProductId(), Product.class);
            Store store = UniversalParser.get(UniversalParser.BASE_URL_RESTFUL + "/store/" + item.getStoreId(), Store.class);
            Product product = null;
            for(Product p : store.getProducts()) {
                if (p.getName() .equals(item.getProductId()) ){
                    product = p;
                    break;
                }
            }
            if (product == null) {
                Log.e("LocalCart", "Product id "+ item.getProductId()+ " not found in store id" + item.getStoreId());
                throw new NetworkErrorException();
            }
            if (result.containsKey(store)) {
                result.get(store).add(new LineItemDetail(product, store.getName(), item.getQty()));
            } else {
                ArrayList<LineItemDetail> list = new ArrayList<>();
                list.add(new LineItemDetail(product, store.getName(), item.getQty()));
                result.put(store, list);
            }

        }
        return result;
    }

    public static List<Object> getGroupedProductAsList() throws  NetworkErrorException, AuthorizationException {
        Map<Store, List<LineItemDetail>> map = getGroupedProduct();
        List<Object> result = new ArrayList<>();
        for (Store store : map.keySet()) {
            result.add(store);
            result.addAll(map.get(store));
        }

        return result;
    }

    public static void plusOne(LineItemDetail detail) {
        changeQty(detail.getProduct().getName(), detail.getStoreId(), 1);
    }

    public static void minusOne(LineItemDetail detail) {
        if (detail.getQty() > 0)
            changeQty(detail.getProduct().getName(), detail.getStoreId(), -1);
    }

    private static void changeQty(String productId, String storeId, int offset) {
        for(LineItem lineItem : cartItems) {
            if (storeId .equals(lineItem.getStoreId())   && productId .equals(lineItem.getProductId()) ) {
                lineItem.setQty(lineItem.getQty() + offset);
                return;
            }
        }
    }

    public static int getSize() {
        return cartItems.size();
    }
}
