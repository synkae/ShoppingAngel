package com.codepath.synkae.shoppingangel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class UserMainActivity extends AppCompatActivity {

    private EditText adjustBudget;
    private Button adjustBtn;
    private Button logoutBtn;
    private TextView currBudget;
    private TextView remainingBudget;

    private Button addItemBtn;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main);


    }
}