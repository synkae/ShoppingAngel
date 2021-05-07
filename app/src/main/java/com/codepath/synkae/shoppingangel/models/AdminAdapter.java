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

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.synkae.shoppingangel.R;
import com.parse.GetDataCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class AdminAdapter extends RecyclerView.Adapter<AdminAdapter.ViewHolder> {
    private Context context;
    private List<Item> items;

    public AdminAdapter(Context context, List<Item> items){
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public AdminAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AdminAdapter.ViewHolder holder, int position) {
        Item item = items.get(position);
        holder.bind(item);
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAdminItem(item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public void deleteAdminItem(ParseObject item){
        // Delete Admin Item
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
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

        public void bind(Item item) {
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
    }
}
