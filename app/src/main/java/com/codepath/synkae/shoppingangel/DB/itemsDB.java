package com.codepath.synkae.shoppingangel.DB;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import com.codepath.synkae.shoppingangel.Item;


@Database(entities= {Item.class},version=1)
public abstract class itemsDB extends RoomDatabase {
    public static final String ITEMS_DATABASE_ = "ITEMS_DATABASE";
    public static final String ITEM_TABLE = "ITEM_TABLE";

    public abstract ItemsDAO getItemsDAO();
}
