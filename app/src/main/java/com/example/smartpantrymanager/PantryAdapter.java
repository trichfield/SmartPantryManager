package com.example.smartpantrymanager;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    Context context;
    List<PantryItem> itemList;

    public PantryAdapter(Context context, List<PantryItem> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_pantry, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PantryItem item = itemList.get(position);
        holder.tvName.setText(item.getName());
        String details = item.getQuantity() + " " + item.getUnit();
        if (item.getExpiry() != null && !item.getExpiry().isEmpty()) {
            details += " - Exp: " + item.getExpiry();
        }
        holder.tvDetails.setText(details);

        holder.itemView.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle(item.getName())
                    .setMessage("What do you want to do?")
                    .setPositiveButton("Edit", (DialogInterface d, int w) -> {
                        Intent i = new Intent(context, AddEditActivity.class);
                        i.putExtra("id", item.getId());
                        i.putExtra("name", item.getName());
                        i.putExtra("qty", item.getQuantity());
                        i.putExtra("unit", item.getUnit());
                        i.putExtra("expiry", item.getExpiry());
                        context.startActivity(i);
                    })
                    .setNegativeButton("Delete", (DialogInterface d, int w) -> {
                        DatabaseHelper db = new DatabaseHelper(context);
                        db.deletePantryItem(item.getId());
                        itemList.remove(position);
                        notifyDataSetChanged();
                    })
                    .setNeutralButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvDetails;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvItemName);
            tvDetails = itemView.findViewById(R.id.tvItemDetails);
        }
    }
}