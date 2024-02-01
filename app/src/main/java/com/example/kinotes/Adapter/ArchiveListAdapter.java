package com.example.kinotes.Adapter;

import android.content.Context;
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

import java.util.List;

public class ArchiveListAdapter extends ArrayAdapter<Notes> {

    // Список заметок
    private final List<Notes> notes;

    // Конструктор адаптера
    public ArchiveListAdapter(@NonNull Context context, int resource, @NonNull List<Notes> archiveList, @NonNull List<Notes> emptyList) {
        super(context, resource, archiveList);
        this.notes = emptyList;
    }

    // Получение визуального представления элемента списка
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.archive_list, parent, false);
        }

        // Инициализация элементов макета
        TextView textViewTitle = convertView.findViewById(R.id.textView_title_archive);
        TextView textViewDate = convertView.findViewById(R.id.textView_date_archive);
        TextView textViewNotes = convertView.findViewById(R.id.textView_notes_archive);
        ImageView imageViewPin = convertView.findViewById(R.id.imageView_pin_archive);

        // Получение заметки по позиции
        Notes note = getItem(position);

        // Установка значений элементов макета на основе заметки
        if (note != null) {
            textViewTitle.setText(note.getTitle());
            textViewDate.setText(note.getDate());
            textViewNotes.setText(note.getNotes());

            if (note.isPinned()) {
                imageViewPin.setImageResource(R.drawable.pin_icon);
            } else {
                imageViewPin.setImageResource(0);
            }
        }

        return convertView;
    }

    // Метод для перемещения заметки в/из архива
    public void moveToArchive(int noteId, boolean inArchive) {
        try {
            // Обновление статуса архивирования в базе данных
            RoomDB.database.mainDao().moveToArchive(noteId, inArchive);

            // Удаление заметки из списка, если она не закреплена
            for (int i = 0; i < notes.size(); i++) {
                if (notes.get(i).getID() == noteId && !notes.get(i).isPinned()) {
                    notes.remove(i);
                    i--;
                }
            }

            // Уведомление адаптера об изменениях
            notifyDataSetChanged();
        } catch (Exception e) {
            // Обработка ошибки
        }
    }

    // Метод для восстановления заметки из архива
    public void restoreFromArchive(Notes selectedNote) {
        // Обновление статуса архивирования в базе данных
        RoomDB.database.mainDao().moveToArchive(selectedNote.getID(), false);

        // Уведомление адаптера об изменениях
        notifyDataSetChanged();
    }
}
