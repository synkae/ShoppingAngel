package com.codepath.synkae.shoppingangel.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Toast;

import com.codepath.synkae.shoppingangel.R;
import com.codepath.synkae.shoppingangel.models.Item;
import com.codepath.synkae.shoppingangel.models.ShoppingList;
import com.codepath.synkae.shoppingangel.models.ShoppingListAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ShoppingListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ShoppingListFragment extends Fragment {
    AutoCompleteTextView actvItem;
    ArrayAdapter<String> autoCompleteAdapter;
    private ArrayList<ShoppingList> shoppingListItems;
    ArrayList<String> itemNames = new ArrayList<>();
    private RecyclerView rvShoppingListItems;
    private ShoppingListAdapter shoppingListAdapter;
    private Button btnSave;
    String itemName = "";

    public static final String TAG = "ShoppingListFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ShoppingListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ShoppingListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ShoppingListFragment newInstance(String param1, String param2) {
        ShoppingListFragment fragment = new ShoppingListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_shopping_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        actvItem = view.findViewById(R.id.actvItem);
        btnSave = view.findViewById(R.id.btnSave);

        rvShoppingListItems = view.findViewById(R.id.rvShoppingListItems);
        shoppingListItems = new ArrayList<>();
        shoppingListAdapter = new ShoppingListAdapter(getActivity(), shoppingListItems);
        rvShoppingListItems.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvShoppingListItems.setAdapter(shoppingListAdapter);

        queryShoppingListItems();

        queryAutoCompleteItems();

        autoCompleteAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, itemNames);
        actvItem.setThreshold(1);
        actvItem.setAdapter(autoCompleteAdapter);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemName = actvItem.getText().toString();
                addItemToShoppingList(itemName);
            }
        });
    }

    public void addItemToShoppingList(String itemName){
        // Configure Query
        ParseObject shoppingListItem = ParseObject.create("ShoppingList");
        // Store an object
        shoppingListItem.put("user", ParseUser.getCurrentUser());
        shoppingListItem.put("itemName", itemName);
        shoppingListItem.put("checked", false);
        // Saving object
        shoppingListItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success
                    Toast.makeText(getActivity(), shoppingListItem.get("itemName") + " successfully added to cart", Toast.LENGTH_SHORT).show();

                    shoppingListAdapter.notifyDataSetChanged();
                    shoppingListAdapter.notifyItemInserted(-1);
                    queryShoppingListItems();
                    rvShoppingListItems.setAdapter(shoppingListAdapter);

                } else {
                    // Error
                    Toast.makeText(getActivity(), "Please enter an item to add", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    protected void queryShoppingListItems() {
        ParseQuery<ShoppingList> query = new ParseQuery<ShoppingList>(ShoppingList.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<ShoppingList>() {
            @Override
            public void done(List<ShoppingList> items, ParseException e) {
                if (e != null){
                    Log.e(TAG, "error", e);
                    return;
                }
                for (ShoppingList i : items){
//                    Log.d(TAG, i.getItemName() + " " + i.getUser() + ", " + i.getChecked());
                }
                shoppingListItems.clear();
                shoppingListItems.addAll(items);
                shoppingListAdapter.notifyDataSetChanged();
            }
        });
    }

    protected void queryAutoCompleteItems() {
        ParseQuery<Item> query = ParseQuery.getQuery("Item");
        query.selectKeys(Arrays.asList("itemName"));
        query.findInBackground(new FindCallback<Item>() {
            @Override
            public void done(List<Item> items, ParseException e) {
                if (e == null) {
                    for(Item item : items){
                        itemNames.add(item.getItemName());
                    }
                } else {
                    Toast.makeText(getActivity(), "query error: " + e, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}