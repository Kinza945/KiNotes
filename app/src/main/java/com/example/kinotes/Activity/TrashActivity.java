package com.example.kinotes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kinotes.Adapter.TrashListAdapter;
import com.example.kinotes.DataBase.RoomDB;
import com.example.kinotes.Menu.DrawerItemClickListener;
import com.example.kinotes.Models.Notes;
import com.example.kinotes.R;
import com.example.kinotes.Util.BurgerButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;


public class TrashActivity extends AppCompatActivity {

    private DrawerLayout drawer_layout;
    private FloatingActionButton burger_button;
    private DrawerItemClickListener drawerItemClickListener;
    private ListView listViewTrash;
    private TrashListAdapter trashListAdapter;

    private boolean notesRestored = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trash);

        drawer_layout = findViewById(R.id.drawer_layout);
        drawerItemClickListener = new DrawerItemClickListener(this, drawer_layout);

        burger_button = findViewById(R.id.burger_button);
        BurgerButton.setupBurgerButton(burger_button, drawerItemClickListener);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(drawerItemClickListener);

        listViewTrash = findViewById(R.id.listViewTrash);

        trashListAdapter = new TrashListAdapter(this, R.layout.trash_list, RoomDB.getInstance(this).mainDao().getTrash(), new ArrayList<>());

        listViewTrash.setAdapter(trashListAdapter);


        listViewTrash.setOnItemClickListener((parent, view, position, id) -> {
            Notes selectedNote = trashListAdapter.getItem(position);
            if (selectedNote != null) {
                restoreNoteFromTrash(selectedNote);
            }
        });

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void restoreNoteFromTrash(Notes note) {

        RoomDB.getInstance(this).mainDao().deleteNoteById(note.getID());

        trashListAdapter.remove(note);
        trashListAdapter.notifyDataSetChanged();

        notesRestored = true;

        Toast.makeText(this, "Note restored: " + note.getTitle(), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("notesRestored", notesRestored);
        setResult(Activity.RESULT_OK, intent);

        super.onBackPressed();
    }
}
