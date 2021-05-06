//package com.codepath.synkae.shoppingangel.models;
//
//import android.content.Context;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ArrayAdapter;
//import android.widget.Filter;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//
//import com.codepath.synkae.shoppingangel.R;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class AutoCompleteAdapter extends ArrayAdapter<AutoCompleteItem> {
//    private List<AutoCompleteItem> autoListFull;
//
//    public AutoCompleteAdapter(@NonNull Context context, @NonNull List<AutoCompleteItem> autoList) {
//        super(context, 0, autoList);
//        autoListFull = new ArrayList<>(autoList);
//    }
//
//    @NonNull
//    @Override
//    public Filter getFilter() {
//        return autoFilter;
//    }
//
//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//        if (convertView == null) {
//            convertView = LayoutInflater.from(getContext()).inflate(
//                    R.layout.auto_complete_row, parent, false
//            );
//        }
//
//        TextView tvAuto = convertView.findViewById(R.id.tvAuto);
//        ImageView ivAuto = convertView.findViewById(R.id.ivAuto);
//
//        AutoCompleteItem autoItem = getItem(position);
//
//        if (autoItem != null) {
//            tvAuto.setText(autoItem.getAutoName());
//            ivAuto.setImageBitmap(autoItem.getAutoImage());
////            ivAuto.setImageResource(autoItem.getAutoImage());
//        }
//
//        return convertView;
//    }
//
//    private Filter autoFilter = new Filter() {
//        @Override
//        protected FilterResults performFiltering(CharSequence constraint) {
//            FilterResults results = new FilterResults();
//            List<AutoCompleteItem> suggestions = new ArrayList<>();
//
//            if (constraint == null || constraint.length() == 0) {
//                suggestions.addAll(autoListFull);
//            } else {
//                String filterPattern = constraint.toString().toLowerCase().trim();
//
//                for (AutoCompleteItem item : autoListFull) {
//                    if (item.getAutoName().toLowerCase().contains(filterPattern)) {
//                        suggestions.add(item);
//                    }
//                }
//            }
//
//            results.values = suggestions;
//            results.count = suggestions.size();
//
//            return results;
//        }
//
//        @Override
//        protected void publishResults(CharSequence constraint, FilterResults results) {
//            clear();
//            addAll((List) results.values);
//            notifyDataSetChanged();
//        }
//
//        @Override
//        public CharSequence convertResultToString(Object resultValue) {
//            return ((AutoCompleteItem) resultValue).getAutoName();
//        }
//    };
//}
