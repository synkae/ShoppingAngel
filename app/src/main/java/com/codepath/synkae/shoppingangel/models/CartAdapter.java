package com.codepath.synkae.shoppingangel.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.synkae.shoppingangel.R;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.w3c.dom.Text;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private Context context;
    private List<Cart> cartList;
    public static final String TAG = "CartAdapter";

    public CartAdapter(Context context, List<Cart> cartList){
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_items, parent, false);
        CartAdapter.ViewHolder viewHolder = new CartAdapter.ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Cart cart = cartList.get(position);
        holder.bind(cart);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cart.deleteInBackground();
                cartList.remove(position);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvPrice;
        private Button btnDelete;
        private Bitmap itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        public void bind(Cart cart) {
            ParseQuery<Item> query = ParseQuery.getQuery(Item.class);
            Log.d(TAG, "cart id: " + cart.getObjectId());
            Log.d(TAG, "cart item id: " + cart.getItem().toString());
            query.getInBackground(cart.getItem().getObjectId(), new GetCallback<Item>() {
                @Override
                public void done(Item item, ParseException e) {
                    if (e != null){
                        Log.e(TAG, "ERROR", e);
                        return;
                    }
                    tvName.setText(item.getItemName());
                    tvPrice.setText(NumberFormat.getCurrencyInstance(new Locale("en", "US")).format(item.getPrice()));
                    ParseFile itemImageFile = item.getParseFile("itemImage");
                    // itemImage found
                    if (itemImageFile != null) {
                        itemImageFile.getDataInBackground(new GetDataCallback() {
                            @Override
                            public void done(byte[] data, ParseException e) {
                                itemImage = BitmapFactory.decodeByteArray(data, 0, data.length);
                                ivImage.setImageBitmap(itemImage);
                            }
                        });
                    } else { // if item has no image
                        ivImage.setImageResource(R.drawable.ic_launcher_background);
                    }
                }
            });
        }
    }
}
