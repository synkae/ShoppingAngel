package com.codepath.synkae.shoppingangel;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    Button btn_logout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn_logout = findViewById(R.id.btn_logout);

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logout();
            }
        });
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
}