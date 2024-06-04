package com.droiduino.smarthome;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ListItemAdapter extends RecyclerView.Adapter<ListItemAdapter.ViewHolder> {
    private final OnItemClick mCallback;
    private Context context;
    private ArrayList<Room> listItems;

    public ListItemAdapter(Context context, ArrayList<Room> listItems, OnItemClick listener) {
        this.context = context;
        this.listItems = listItems;
        this.mCallback = listener;
    }

    @NonNull
    @Override
    public ListItemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListItemAdapter.ViewHolder holder, int position) {
        Room item = listItems.get(position);
        holder.textViewName.setText(item.getName());
        holder.buttonToggle.setChecked(item.isChecked());

        holder.buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Remove item from list and notify adapter
                int itemPosition = holder.getAdapterPosition();
                if (itemPosition != RecyclerView.NO_POSITION) {
                    listItems.remove(itemPosition);
                    notifyItemRemoved(itemPosition);
                }
            }
        });

        holder.buttonToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mCallback.onItemClickHandler(String.format("%s %d_", item.getPin(), item.isChecked() ? 1 : 0));
                item.setChecked(!item.isChecked());
                holder.buttonToggle.setChecked(item.isChecked());
                Log.d("listItems",listItems.toString());
                Log.d("itemPin",item.getPin());

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView buttonDelete;
        TextView textViewName;
        ToggleButton buttonToggle;

        public ViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.text_view_name);
            buttonToggle = itemView.findViewById(R.id.button_toggle);
            buttonDelete = itemView.findViewById(R.id.button_delete);
        }
    }
}