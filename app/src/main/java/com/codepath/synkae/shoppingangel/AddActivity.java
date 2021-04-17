package com.codepath.synkae.shoppingangel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.text.NumberFormat;
import java.util.Locale;

public class AddActivity extends AppCompatActivity {
    TextView tvItemName;
    ImageView ivItemImage;
    TextView tvPrice;
    Button btnCancel;
    Button btnConfirm;
    String itemName;
    double price;
    Bitmap itemImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        String objectId = getIntent().getStringExtra("objectId");

        tvItemName = findViewById(R.id.tvItemName);
        ivItemImage = findViewById(R.id.ivItemImage);
        tvPrice = findViewById(R.id.tvPrice);
        btnCancel = findViewById(R.id.btnCancel);
        btnConfirm = findViewById(R.id.btnConfirm);

        getItemInfo(objectId);

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goMainActivity();
            }
        });

    }

    private void getItemInfo(String objectId) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Item");
        query.whereEqualTo("objectId", objectId);
        query.getFirstInBackground(new GetCallback<ParseObject>() {
            public void done(ParseObject item, ParseException e) {
                if (e == null) {
                    itemName = item.getString("itemName");
                    tvItemName.setText(itemName);

                    price = item.getDouble("price");
                    String convertedPrice = NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(price);
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
                } else {
                    // No items found
                    tvItemName.setText("No items found");
                    tvPrice.setText("");
                }
            }
        });
    }

    private void goMainActivity() {
        Intent i = new Intent(AddActivity.this, MainActivity.class);
        startActivity(i);
        finish();
    }
}