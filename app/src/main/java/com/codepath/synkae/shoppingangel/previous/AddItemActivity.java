package com.codepath.synkae.shoppingangel.previous;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

import com.codepath.synkae.shoppingangel.R;
import com.codepath.synkae.shoppingangel.models.AddItemAdapter;
import com.codepath.synkae.shoppingangel.models.Item;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class AddItemActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private ArrayList<Item> allItems;
    private RecyclerView rvItems;
    private AddItemAdapter addItemAdapter;
    public static final String TAG = "AddItemActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);
        toolbar = findViewById(R.id.toolbar);
        rvItems = findViewById(R.id.rvItems);
        setSupportActionBar(toolbar);
        allItems = new ArrayList<>();

        addItemAdapter = new AddItemAdapter(this, allItems);
        rvItems.setLayoutManager(new LinearLayoutManager(this));
        rvItems.setAdapter(addItemAdapter);
        // get all items from parse
        queryItems();
    }

    protected void queryItems() {
        ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e != null){
                    Log.e(TAG, "SOMETHING WENT WRONG", e);
                    return;
                }
                // printing out to see if it works...
                for (Item i : items){
                    Log.d(TAG, i.getItemName());
                }
                allItems.addAll(items);
                addItemAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}