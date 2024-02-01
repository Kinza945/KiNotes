package com.example.kinotes.Adapter;

import androidx.cardview.widget.CardView;

import com.example.kinotes.Models.Notes;

public interface NotesClickListener {


    void onClick (Notes notes);
    void onLongClick (Notes notes, CardView cardView);
}
