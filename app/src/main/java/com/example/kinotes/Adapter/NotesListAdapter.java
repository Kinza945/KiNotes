package com.example.kinotes.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kinotes.Models.Notes;
import com.example.kinotes.R;

import java.util.List;

public class NotesListAdapter extends RecyclerView.Adapter<NotesViewHolder> {

    Context context;
    List<Notes> list;
    private Activity activity;

    public NotesListAdapter(Context context, List<Notes> list, NotesClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
        this.activity = (Activity) context;
    }

    NotesClickListener listener;

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NotesViewHolder(LayoutInflater.from(context).inflate(R.layout.notes_list, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        holder.textView_title.setText(list.get(position).getTitle());
        holder.textView_title.setSelected(true);

        holder.textView_notes.setText(list.get(position).getNotes());

        holder.textView_date.setText(list.get(position).getDate());
        holder.textView_date.setSelected(true);

        if (list.get(position).isPinned()) {
            holder.imageView_pin.setImageResource(R.drawable.pin_icon);
            moveItemToTop(position);
        } else {
            holder.imageView_pin.setImageResource(0);
        }

        int colorCode = context.getResources().getColor(R.color.white);
        holder.notes_container.setCardBackgroundColor(colorCode);

        holder.notes_container.setOnClickListener(v -> {
            listener.onClick(list.get(holder.getAdapterPosition()));
        });

        holder.notes_container.setOnLongClickListener(v -> {
            listener.onLongClick(list.get(holder.getAdapterPosition()), holder.notes_container);
            return true;
        });
    }

    private void moveItemToTop(int position) {
        if (!list.get(position).isPinned()) {
            Notes movedNote = list.remove(position);
            int insertIndex = 0;

            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).isPinned()) {
                    insertIndex = i + 1;
                }
            }

            list.add(insertIndex, movedNote);

            final int finalPosition = position;
            final int finalInsertIndex = insertIndex;

            activity.findViewById(R.id.recycler_home).post(() -> notifyItemMoved(finalPosition, finalInsertIndex));

        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void filterList(List<Notes> filteredList) {
        list.clear();
        list.addAll(filteredList);
        notifyDataSetChanged();
    }


    public void clear() {
        list.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Notes> notes) {
        list.addAll(notes);
        notifyDataSetChanged();
    }
}

class NotesViewHolder extends RecyclerView.ViewHolder {

    CardView notes_container;
    TextView textView_title, textView_notes, textView_date;
    ImageView imageView_pin;

    public NotesViewHolder(@NonNull View itemView) {
        super(itemView);

        notes_container = itemView.findViewById(R.id.notes_container);
        textView_title = itemView.findViewById(R.id.textView_title);
        textView_notes = itemView.findViewById(R.id.textView_notes);
        textView_date = itemView.findViewById(R.id.textView_date);
        imageView_pin = itemView.findViewById(R.id.imageView_pin);
    }
}
