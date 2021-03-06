package com.codepath.synkae.shoppingangel.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

@ParseClassName("Cart")
@Parcel(analyze = Cart.class)
public class Cart extends ParseObject {
    public static final String KEY_USER_ID = "user";
    public static final String KEY_ITEM_ID = "item";
//    public static final String KEY_NAME = "name";
//    public static final String KEY_TOTAL = "total";

    public Cart(){}

    public ParseObject getUser(){
        return getParseObject(KEY_USER_ID);
    }
    public void setUser(ParseObject user){
        put(KEY_USER_ID, user);
    }
    public Item getItem(){
        return (Item)getParseObject(KEY_ITEM_ID);
    }
    public void setItem(ParseObject item){
        put(KEY_ITEM_ID, item);
    }
//    public String getItemName(){
//        return getString(KEY_NAME);
//    }
//    public void setName(String name) {
//        put(KEY_NAME, name);
//    }
//    public double getTotal(){
//        return getDouble(KEY_TOTAL);
//    }
//    public void setTotal(double total){
//        put(KEY_TOTAL, total);
//    }
}
