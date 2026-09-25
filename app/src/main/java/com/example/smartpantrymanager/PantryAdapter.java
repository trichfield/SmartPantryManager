package com.example.smartpantrymanager;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class PantryAdapter extends RecyclerView.Adapter<PantryAdapter.ViewHolder> {

    private Context context;
    private List<PantryItem> itemList;

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
        holder.tvDetails.setText(item.getQuantity() + " " + item.getUnit() + " | Exp: " + item.getExpiryDate());

        // LONG PRESS FOR EDIT / DELETE - PDF requires full CRUD
        holder.itemView.setOnLongClickListener(v -> {
            new androidx.appcompat.app.AlertDialog.Builder(context)
                    .setTitle(item.getName())
                    .setItems(new String[]{"Edit", "Delete"}, (dialog, which) -> {
                        if (which == 0) {
                            // EDIT
                            Intent intent = new Intent(context, AddEditActivity.class);
                            intent.putExtra("id", item.getId());
                            intent.putExtra("name", item.getName());
                            intent.putExtra("qty", item.getQuantity());
                            intent.putExtra("unit", item.getUnit());
                            intent.putExtra("expiry", item.getExpiryDate());
                            context.startActivity(intent);
                        } else {
                            // DELETE
                            DatabaseHelper db = new DatabaseHelper(context);
                            db.deletePantryItem(item.getId());
                            itemList.remove(holder.getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    })
                    .show();
            return true;
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