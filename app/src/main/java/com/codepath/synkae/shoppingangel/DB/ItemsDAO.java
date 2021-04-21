package com.codepath.synkae.shoppingangel.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Update;

import com.codepath.synkae.shoppingangel.Item;


@Dao
public interface ItemsDAO {
    @Insert
    void insert(Item... item);

    @Update
    void update(Item... item);

    @Delete
    void delete(Item item);
}
