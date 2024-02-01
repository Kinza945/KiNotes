package com.example.kinotes.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.kinotes.Models.Notes;
import com.example.kinotes.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class NotesTakerActivity extends AppCompatActivity {

    EditText editText_title, editText_notes;
    ImageView imageView_save;
    Notes notes;
    boolean isOldNote = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notes_taker);

        editText_title = findViewById(R.id.editText_title);
        editText_notes = findViewById(R.id.editText_notes);
        imageView_save = findViewById(R.id.imageView_save);

        Intent intent = getIntent();
        if (intent.hasExtra("old_note")) {
            Object data = intent.getSerializableExtra("old_note");

            if (data instanceof Notes) {
                notes = (Notes) data;
                editText_title.setText(notes.getTitle());
                editText_notes.setText(notes.getNotes());
                isOldNote = true;
            } else if (data instanceof ArrayList) {
                ArrayList<Notes> notesList = (ArrayList<Notes>) data;
                if (!notesList.isEmpty()) {
                    notes = notesList.get(0);
                    editText_title.setText(notes.getTitle());
                    editText_notes.setText(notes.getNotes());
                    isOldNote = true;
                }
            }
        } else {
            notes = new Notes();
            notes.setInTrash(false);
        }

        imageView_save.setOnClickListener(v -> {
            String title = editText_title.getText().toString();
            String description = editText_notes.getText().toString();

            if (description.isEmpty()) {
                Toast.makeText(NotesTakerActivity.this, "Введите текст", Toast.LENGTH_SHORT).show();
                return;
            }

            @SuppressLint("SimpleDateFormat") SimpleDateFormat formatter = new SimpleDateFormat("dd MMMM yyyy hh:mm:ss");
            Date date = new Date();

            if (!isOldNote) {
                notes = new Notes();
                notes.setInTrash(false);
            }

            notes.setTitle(title);
            notes.setNotes(description);
            notes.setDate(formatter.format(date));

            Intent resultIntent = new Intent();
            resultIntent.putExtra("notes", notes);

            if (isOldNote) {
                resultIntent.putExtra("old_note_index", intent.getIntExtra("old_note_index", -1));
            }

            setResult(Activity.RESULT_OK, resultIntent);
            finish();
        });
    }
}
