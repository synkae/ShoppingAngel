package com.codepath.synkae.shoppingangel.models;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.synkae.shoppingangel.R;

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
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private ImageView ivImage;
        private TextView tvName;
        private TextView tvPrice;
        private Button btnAdd;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivImage = itemView.findViewById(R.id.ivImage);
            tvName = itemView.findViewById(R.id.tvName);
            tvPrice = itemView.findViewById(R.id.tvPrice);
            btnAdd = itemView.findViewById(R.id.btnAdd);
        }

        public void bind(Item item) {
            // need to get image from item
            //ivImage = ...
            tvName.setText(item.getItemName());
            tvPrice.setText("$" + item.getPrice());
            ivImage.setImageResource(R.drawable.ic_launcher_background);
        }
    }
}
