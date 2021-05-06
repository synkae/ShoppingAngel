package com.codepath.synkae.shoppingangel.models;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.codepath.synkae.shoppingangel.R;
import com.parse.DeleteCallback;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.List;

public class ShoppingListAdapter extends RecyclerView.Adapter<ShoppingListAdapter.ViewHolder> {
    private Context context;
    private List<ShoppingList> shoppingListItems;

    public ShoppingListAdapter(Context context, List<ShoppingList> shoppingListItems){
        this.context = context;
        this.shoppingListItems = shoppingListItems;
    }

    @NonNull
    @Override
    public ShoppingListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.shopping_list_items, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ShoppingListAdapter.ViewHolder holder, int position) {
        ShoppingList shoppingListItem = shoppingListItems.get(position);
        holder.bind(shoppingListItem);

        holder.cbShoppingListItem.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                saveCheckedStatus(shoppingListItem);
            }
        });

        holder.cbShoppingListItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                confirmDelete(shoppingListItem, position);

                return false;
            }
        });
    }

    public void confirmDelete(ShoppingList shoppingListItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Please Confirm");
        builder.setMessage("Are you sure you want to delete \"" + shoppingListItem.getItemName() + "\"?");
        // add the buttons
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // CONFIRM
                deleteLongClickedItem(shoppingListItem);

                shoppingListItems.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, shoppingListItems.size());
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // CANCEL
                Toast.makeText(context, shoppingListItem.getItemName() + " was not deleted", Toast.LENGTH_SHORT).show();
            }
        });
        // create and show the alert dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    public void saveCheckedStatus(ShoppingList shoppingListItem) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("ShoppingList");
        // Retrieve the object by id
        query.getInBackground(shoppingListItem.getObjectId(), new GetCallback<ParseObject>() {
            public void done(ParseObject item, ParseException e) {
                if (e == null) {
                    if (item.getBoolean("checked")) { // checked
                        item.put("checked", false);
                    } else { // not checked
                        item.put("checked", true);
                    }
                    item.saveInBackground();
                } else {
                    // Failed
                }
            }
        });
    }

    public void deleteLongClickedItem(ShoppingList shoppingListItem) {
        ParseQuery<ParseObject> shoppingList = ParseQuery.getQuery("ShoppingList");
        // Query parameters based on the item name
        shoppingList.whereEqualTo("objectId", shoppingListItem.getObjectId());
        shoppingList.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(final List<ParseObject> item, ParseException e) {
                if (e == null) {
                    item.get(0).deleteInBackground(new DeleteCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e == null) {
                                // Success
                                Toast.makeText(context, item.get(0).getString("itemName") + " was successfully deleted", Toast.LENGTH_SHORT).show();
                            } else {
                                // Failed
                            }
                        }
                    });
                } else {
                    // Something is wrong
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return shoppingListItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        private CheckBox cbShoppingListItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cbShoppingListItem = itemView.findViewById(R.id.cbShoppingListItem);
        }

        public void bind(ShoppingList shoppingListItem) {
            cbShoppingListItem.setText(shoppingListItem.getItemName());
            cbShoppingListItem.setChecked(shoppingListItem.getChecked());
        }
    }
}
