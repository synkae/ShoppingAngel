package com.codepath.synkae.shoppingangel.fragments;

import android.app.Application;

import com.codepath.synkae.shoppingangel.R;
import com.codepath.synkae.shoppingangel.models.Cart;
import com.codepath.synkae.shoppingangel.models.Item;
import com.codepath.synkae.shoppingangel.models.ShoppingList;
import com.parse.Parse;
import com.parse.ParseObject;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

public class ParseApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // Use for troubleshooting -- remove this line for production
        Parse.setLogLevel(Parse.LOG_LEVEL_DEBUG);
        ParseObject.registerSubclass(Item.class);
        ParseObject.registerSubclass(Cart.class);
        ParseObject.registerSubclass(ShoppingList.class);
        // Use for monitoring Parse OkHttp traffic
        // Can be Level.BASIC, Level.HEADERS, or Level.BODY
        // See https://square.github.io/okhttp/3.x/logging-interceptor/ to see the options.
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        builder.networkInterceptors().add(httpLoggingInterceptor);

        // set applicationId, and server server based on the values in the back4app settings.
        // any network interceptors must be added with the Configuration Builder given this syntax
        Parse.initialize(new Parse.Configuration.Builder(this)
                .applicationId(getString(R.string.back4app_app_id)) // should correspond to Application Id env variable
                .clientKey(getString(R.string.back4app_client_key))  // should correspond to Client key env variable
                        .server("https://parseapi.back4app.com").build());

    }
}

