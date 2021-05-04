package com.codepath.synkae.shoppingangel.previous;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;

import com.codepath.synkae.shoppingangel.R;
import com.codepath.synkae.shoppingangel.fragments.LoginActivity;
import com.codepath.synkae.shoppingangel.models.Cart;
import com.codepath.synkae.shoppingangel.models.CartAdapter;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    TextView tvCurrent;
    TextView tvRemaining;
    EditText etNewBudget;
    Button btnScan;
    Button btnAdd;
    Button btnAdjust;
    Double currentBudget;
    Double newBudget;
    ParseUser currentUser = ParseUser.getCurrentUser();
    public static RecyclerView rvCart;
    CartAdapter cartAdapter;
    List<Cart> cartList;
    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvCurrent = findViewById(R.id.tvCurrent);
        tvRemaining = findViewById(R.id.tvRemaining);
        etNewBudget = findViewById(R.id.etNewBudget);
        btnScan = findViewById(R.id.btnScan);
        btnAdd = findViewById(R.id.btnAdd);
        btnAdjust = findViewById(R.id.btnAdjust);
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(this, cartList);
        rvCart = findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(this));
        rvCart.setAdapter(cartAdapter);

        queryCartItems();
        displayCurrentBudget();

        btnScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, ScanActivity.class);
                startActivity(i);
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MainActivity.this, AddItemActivity.class);
                startActivity(i);
            }
        });

        btnAdjust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adjustCurrentBudget();
            }
        });
    }

    public void queryCartItems() {
        cartList.clear();
        ParseQuery<Cart> query = new ParseQuery<Cart>(Cart.class);
        query.whereEqualTo("user", ParseUser.getCurrentUser());
        query.findInBackground(new FindCallback<Cart>() {
            @Override
            public void done(List<Cart> carts, ParseException e) {
                if (e != null){
                    Log.e(TAG, "error", e);
                    return;
                }
                for (Cart c : carts){
                    Log.d(TAG, c.getObjectId() + " " + c.getUser() + ", " + c.getItem());
                }
                cartList.addAll(carts);
                cartAdapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.menu_logout) {
            logout();
        }
        return super.onOptionsItemSelected(item);
    }

    private void logout() {
        final ProgressDialog progressDialog = new ProgressDialog(getApplicationContext());
        ParseUser.logOutInBackground(e -> {
            progressDialog.dismiss();
            if (e == null) {
                Toast.makeText(MainActivity.this, "You have successfully logged out", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }

    private void adjustCurrentBudget() {
        try {
            // get new budget from EditText
            newBudget = Double.parseDouble(etNewBudget.getText().toString());
            // round new budget to 2 decimal places
            DecimalFormat df = new DecimalFormat("#.##");
            newBudget = Double.valueOf(df.format(newBudget));
            Log.d("newBudget: ", newBudget.toString());

            // update currentUser's budget with newBudget
            currentUser.put("budget", newBudget);
            currentUser.saveInBackground();

            displayCurrentBudget();

        } catch (NumberFormatException e){
            // empty new budget
            Log.e("adjust budget failed: ", e.toString());
        }
    }

    private void displayCurrentBudget() {
        // get current budget(Double) from parse
        currentBudget = currentUser.getDouble("budget");

        // convert Double to String $xx.xx
        String convertedCurrentBudget = convertDoubleToDollar(currentBudget);
        Log.d("changed: ", convertedCurrentBudget);

        // display current budget in TextView
        tvCurrent.setText("Current Budget: " + convertedCurrentBudget);
    }

    // convert Double to String $xx.xx
    private String convertDoubleToDollar(Double budget) {
        return NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(budget);
    }
}