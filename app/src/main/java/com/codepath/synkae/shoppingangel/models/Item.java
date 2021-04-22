package com.codepath.synkae.shoppingangel.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

@Parcel(analyze = Item.class)
@ParseClassName("Item")
public class Item extends ParseObject {
    public static final String KEY_ITEM_NAME = "itemName";
    public static final String KEY_PRICE = "price";

    public Item(){}

    public String getItemName(){
        return getString(KEY_ITEM_NAME);
    }
    public void setItemName(String itemName){
        put(KEY_ITEM_NAME, itemName);
    }
    public double getPrice(){
        return getDouble(KEY_PRICE);
    }
    public void setPrice(double price){
        put(KEY_PRICE, price);
    }

}
