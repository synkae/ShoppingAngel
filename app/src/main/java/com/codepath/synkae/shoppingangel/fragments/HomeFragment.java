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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.codepath.synkae.shoppingangel.R;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {
    TextView tvCurrent;
    TextView tvRemaining;
    EditText etNewBudget;
    Button btnAdjust;
    Double currentBudget;
    Double newBudget;
    ParseUser currentUser = ParseUser.getCurrentUser();
    public static RecyclerView rvCart;
    CartAdapter cartAdapter;
    List<Cart> cartList;
    public static final String TAG = "HomeFragment";

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvCurrent = view.findViewById(R.id.tvCurrent);
        tvRemaining = view.findViewById(R.id.tvRemaining);
        etNewBudget = view.findViewById(R.id.etNewBudget);
        btnAdjust = view.findViewById(R.id.btnAdjust);
        cartList = new ArrayList<>();
        cartAdapter = new CartAdapter(getActivity(), cartList);
        rvCart = view.findViewById(R.id.rvCart);
        rvCart.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvCart.setAdapter(cartAdapter);

        queryCartItems();
        displayCurrentBudget();

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

    @Override
    public void onResume() {
        super.onResume();
        //queryCartItems();
    }
}