package com.codepath.synkae.shoppingangel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

public class AddActivity extends AppCompatActivity {
    String itemId;
    TextView tvItemName;
    ImageView ivItemImage;
    TextView tvPrice;
    Button btnCancel;
    Button btnConfirm;
    String itemName;
    double itemPrice;
    Bitmap itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        itemId = getIntent().getStringExtra("itemId");

        tvItemName = findViewById(R.id.tvItemName);
        ivItemImage = findViewById(R.id.ivItemImage);
        tvPrice = findViewById(R.id.tvPrice);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

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
                            Toast.makeText(AddActivity.this, "Add to cart canceled", Toast.LENGTH_SHORT).show();
                            goMainActivity();
                        }
                    });

                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addItemToCart(item);
                            goMainActivity();
                        }
                    });

                } else {
                    // item not found   
                    tvItemName.setText("Item Not Found");
                    tvPrice.setText("");
                    btnCancel.setText("Retry");
                    btnConfirm.setText("My Cart");
                    // go back to scan page
                    btnCancel.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goScanActivity();
                        }
                    });
                    // go to main cart page
                    btnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            goMainActivity();
                        }
                    });
                }
            }
        });
    }

    private void goScanActivity() {
        Intent i = new Intent(AddActivity.this, ScanActivity.class);
        startActivity(i);
        finish();
    }

    private void goMainActivity() {
        Intent i = new Intent(AddActivity.this, MainActivity.class);
        startActivity(i);
        finish();
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
                    Toast.makeText(AddActivity.this, "Item successfully added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    // Error
                    Toast.makeText(AddActivity.this, "Add to cart failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}