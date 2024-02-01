package com.example.kinotes.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.kinotes.DataBase.RoomDB;
import com.example.kinotes.Models.Notes;
import com.example.kinotes.R;

import java.util.Iterator;
import java.util.List;

public class TrashListAdapter extends ArrayAdapter<Notes> {

    private Context context;
    private List<Notes> trashList;
    private List<Notes> notes;

    public TrashListAdapter(@NonNull Context context, int resource, @NonNull List<Notes> trashList, @NonNull List<Notes> emptyList) {
        super(context, resource, trashList);
        this.context = context;
        this.trashList = trashList;
        this.notes = emptyList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.trash_list, parent, false);
        }

        TextView textViewTitle = convertView.findViewById(R.id.textView_title_trash);
        TextView textViewDate = convertView.findViewById(R.id.textView_date_trash);
        TextView textViewNotes = convertView.findViewById(R.id.textView_notes_trash);
        ImageView imageViewPin = convertView.findViewById(R.id.imageView_pin);

        Notes note = getItem(position);

        if (note != null) {
            textViewTitle.setText(note.getTitle());
            textViewDate.setText(note.getDate());
            textViewNotes.setText(note.getNotes());

            if (note.isPinned() != null && note.isPinned()) {
                imageViewPin.setImageResource(R.drawable.pin_icon);
            } else {
                imageViewPin.setImageResource(0);
            }
        } else {
            Log.e("TrashListAdapter", "Note is null at position " + position);
        }

        return convertView;
    }

    public void moveToTrash(int noteId, boolean inTrash) {
        try {
            RoomDB.database.mainDao().moveToTrash(noteId, inTrash);

            // Используйте Iterator или removeIf для безопасного удаления
            Iterator<Notes> iterator = notes.iterator();
            while (iterator.hasNext()) {
                Notes currentNote = iterator.next();
                if (currentNote.getID() == noteId && !currentNote.isPinned()) {
                    iterator.remove();
                }
            }

            notifyDataSetChanged();
            Log.d("MainActivity", "Item moved to trash successfully");
        } catch (Exception e) {
            Log.e("MainActivity", "Error moving item to trash: " + e.getMessage());
        }
    }
}
