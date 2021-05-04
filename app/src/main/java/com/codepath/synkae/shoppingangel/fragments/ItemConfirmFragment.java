package com.codepath.synkae.shoppingangel.fragments;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.codepath.synkae.shoppingangel.R;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ItemConfirmFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ItemConfirmFragment extends Fragment {
    String itemId;
    TextView tvItemName;
    ImageView ivItemImage;
    TextView tvPrice;
    Button btnCancel;
    Button btnConfirm;
    String itemName;
    double itemPrice;
    Bitmap itemImage;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ItemConfirmFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ItemConfirmFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ItemConfirmFragment newInstance(String param1, String param2) {
        ItemConfirmFragment fragment = new ItemConfirmFragment();
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
        return inflater.inflate(R.layout.fragment_item_confirm, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

//        itemId = getIntent().getStringExtra("itemId");
        Bundle bundle = this.getArguments();
        if (bundle != null) {
            itemId = bundle.getString("itemId", "");
        }

        tvItemName = view.findViewById(R.id.tvItemName);
        ivItemImage = view.findViewById(R.id.ivItemImage);
        tvPrice = view.findViewById(R.id.tvPrice);
        btnCancel = view.findViewById(R.id.btnCancel);
        btnConfirm = view.findViewById(R.id.btnConfirm);

        getItemInfo(itemId);
    }

    private void getItemInfo(String itemId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Item");
        query.whereEqualTo("objectId", itemId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject item, ParseException e) {
                if (e == null) {
                    itemName = item.getString("itemName");
                    tvItemName.setText(itemName);

                    itemPrice = item.getDouble("price");
                    String convertedPrice = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(itemPrice);
                    tvPrice.setText(convertedPrice);

                    ParseFile itemImageFile = item.getParseFile("itemImage");
                    // itemImage found
                    if (itemImageFile != null) {
                        itemImageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                itemImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                                ivItemImage.setImageBitmap(itemImage);
                            }
                        });
                    } else { // if item has no image
                        ivItemImage.setImageResource(R.drawable.ic_baseline_no_photography_24);
                    }

                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Toast.makeText(getActivity(), "Add to cart canceled", Toast.LENGTH_SHORT).show();
                            goHomeFragment();
                        }
                    });

                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addItemToCart(item);
                            goHomeFragment();
                        }
                    });
                // item not found
                } else {
                    tvItemName.setText("Item Not Found");
                    tvPrice.setText("");
                    btnCancel.setText("Retry");
                    btnConfirm.setText("My Cart");

                    // go back to scan page
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goScanFragment();
                        }
                    });

                    // go to main cart page
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goHomeFragment();
                        }
                    });
                }
            }
        });
    }

    private void goScanFragment() {
        Fragment fragment = new ScanFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void goHomeFragment() {
        Fragment fragment = new HomeFragment();
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void addItemToCart(ParseObject item) {
        // Configure Query
        ParseObject cartItem = ParseObject.create("Cart");
        // Store an object
        cartItem.put("user", ParseUser.getCurrentUser());
        cartItem.put("item", item);
        // Saving object
        cartItem.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    // Success
                    Toast.makeText(getActivity(), "Item successfully added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    // Error
                    Toast.makeText(getActivity(), "Add to cart failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}