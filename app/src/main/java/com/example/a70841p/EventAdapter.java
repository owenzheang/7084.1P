package com.example.a70841p;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {

    private List<Event> eventList;
    private OnDeleteClickListener deleteClickListener;
    private OnEditClickListener editClickListener;

    public  interface OnDeleteClickListener{
        void onDeleteClick(int position);
    }

    public EventAdapter(List<Event> eventList, OnDeleteClickListener deleteClickListener, OnEditClickListener editClickListener) {
        this.eventList = eventList;
        this.deleteClickListener = deleteClickListener;
        this.editClickListener = editClickListener;
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventTitle, tvCategory, tvLocation, tvDateTime;
        ImageButton btnEdit, btnDelete;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);

            tvEventTitle = itemView.findViewById(R.id.tvEventTitle);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            tvLocation = itemView.findViewById(R.id.tvLocation);
            tvDateTime = itemView.findViewById(R.id.tvDateTime);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);

        holder.tvEventTitle.setText(event.getTitle());
        holder.tvCategory.setText(event.getCategory());
        holder.tvLocation.setText(event.getLocation());
        holder.tvDateTime.setText(event.getDateTime());

        holder.btnEdit.setOnClickListener(v -> {
            if(editClickListener != null){
                editClickListener.onEditClick(position);
            }
        });

        holder.btnDelete.setOnClickListener(v ->{
            if(deleteClickListener != null){
                deleteClickListener.onDeleteClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public interface OnEditClickListener{
        void onEditClick(int position);
    }

}