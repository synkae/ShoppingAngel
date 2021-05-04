package com.codepath.synkae.shoppingangel.models;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.synkae.shoppingangel.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

public class AddItemAdapter extends RecyclerView.Adapter<AddItemAdapter.ViewHolder> {
    private Context context;
    private List<Item> items;

    public AddItemAdapter(Context context, List<Item> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AddItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.all_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AddItemAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addItemToCart(item);

                //this doesnt work
                //MainActivity.rvCart.getAdapter().notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void addItemToCart(ParseObject item){
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
                    Toast.makeText(context, item.get("itemName") + " successfully added to cart", Toast.LENGTH_SHORT).show();
                } else {
                    // Error
                    Toast.makeText(context, "Add to cart failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvPrice;
        private Button btnAdd;
        private Bitmap itemImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }

        public void bind(Item item) {
            tvName.setText(item.getItemName());
            tvPrice.setText("$" + item.getPrice());
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
    }
}
