package com.codepath.synkae.shoppingangel.models;

import com.parse.ParseClassName;
import com.parse.ParseObject;

import org.parceler.Parcel;

@ParseClassName("ShoppingList")
@Parcel(analyze = ShoppingList.class)
public class ShoppingList extends ParseObject {
    public static final String KEY_USER_ID = "user";
    public static final String KEY_ITEM_NAME = "itemName";
    public static final String KEY_CHECKED = "checked";

    public ShoppingList(){}

    public ParseObject getUser(){
        return getParseObject(KEY_USER_ID);
    }
    public void setUser(ParseObject user){
        put(KEY_USER_ID, user);
    }
    public String getItemName(){
        return getString(KEY_ITEM_NAME);
    }
    public void setItemName(String itemName){
        put(KEY_ITEM_NAME, itemName);
    }
    public boolean getChecked(){
        return getBoolean(KEY_CHECKED);
    }
    public void setChecked(Boolean checked) {
        put(KEY_CHECKED, checked);
    }
}
