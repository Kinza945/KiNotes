package com.example.kinotes.MainActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.appcompat.widget.SearchView;
import androidx.cardview.widget.CardView;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.kinotes.Activity.NotesTakerActivity;
import com.example.kinotes.Adapter.NotesClickListener;
import com.example.kinotes.Adapter.NotesListAdapter;
import com.example.kinotes.Adapter.TrashListAdapter;
import com.example.kinotes.Adapter.ArchiveListAdapter;
import com.example.kinotes.DataBase.RoomDB;
import com.example.kinotes.Models.Notes;
import com.example.kinotes.Menu.DrawerItemClickListener;
import com.example.kinotes.R;
import com.example.kinotes.Util.BurgerButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener, NavigationView.OnNavigationItemSelectedListener {

    RecyclerView recyclerView;
    FloatingActionButton fab_add, burger_button;
    NotesListAdapter notesListAdapter;
    TrashListAdapter trashListAdapter;
    ArchiveListAdapter archiveListAdapter;
    RoomDB database;
    SearchView searchView_home;
    Notes selectedNote;
    NavigationView navigationView;
    List<Notes> notes = new ArrayList<>();
    List<Notes> listTrash;
    List<Notes> listArchive;
    private DrawerLayout drawerLayout;
    private DrawerItemClickListener drawerItemClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.burget);
        }

        recyclerView = findViewById(R.id.recycler_home);
        fab_add = findViewById(R.id.fab_add);
        searchView_home = findViewById(R.id.searchView_home);
        drawerLayout = findViewById(R.id.drawer_layout);
        burger_button = findViewById(R.id.burger_button);
        navigationView = findViewById(R.id.navigation_view);
        database = RoomDB.getInstance(this);
        notes = database.mainDao().getAll();
        listTrash = database.mainDao().getTrash();
        drawerItemClickListener = new DrawerItemClickListener(this, drawerLayout);

        listTrash = database.mainDao().getTrash();
        trashListAdapter = new TrashListAdapter(this, R.layout.trash_list, listTrash, notes);

        listArchive = database.mainDao().getArchive();
        archiveListAdapter = new ArchiveListAdapter(this, R.layout.archive_list, listArchive, notes);

        updateRecycle(notes);

        BurgerButton.setupBurgerButton(burger_button, drawerItemClickListener);

        fab_add.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            startActivityForResult(intent, 101, null);
        });

        searchView_home.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return true;
            }
        });

        navigationView.setNavigationItemSelectedListener(this);
    }

    private void filter(String newText) {
        List<Notes> filteredList = new ArrayList<>();
        for (Notes singleNote : notes) {
            if (singleNote.getTitle().toLowerCase().contains(newText.toLowerCase()) ||
                    singleNote.getNotes().toLowerCase().contains(newText.toLowerCase())) {
                filteredList.add(singleNote);
            }
        }
        notesListAdapter.filterList(filteredList);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            Notes new_notes = (Notes) data.getSerializableExtra("notes");
            database.mainDao().insert(new_notes);
            updateRecycle(database.mainDao().getAll());
        }

        if (requestCode == 102 && resultCode == Activity.RESULT_OK) {
            Notes new_notes = (Notes) data.getSerializableExtra("notes");
            database.mainDao().update(new_notes.getID(), new_notes.getTitle(), new_notes.getNotes());
            updateRecycle(database.mainDao().getAll());
        }
    }

    private void updateRecycle(List<Notes> notes) {
        List<Notes> filteredNotes = new ArrayList<>();
        for (Notes note : notes) {
            if (!note.getInTrash() && (note.getInArchive() == null || !note.getInArchive())) {
                filteredNotes.add(note);
            }
        }

        if (notesListAdapter == null) {
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

            notesListAdapter = new NotesListAdapter(MainActivity.this, filteredNotes, notesClickListener);
            recyclerView.setAdapter(notesListAdapter);
        } else {
            notesListAdapter.clear();
            notesListAdapter.addAll(filteredNotes);
            notesListAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.pin) {
            if (selectedNote.isPinned()) {
                database.mainDao().pin(selectedNote.getID(), false);
                Toast.makeText(MainActivity.this, "Заметка откреплена", Toast.LENGTH_SHORT).show();
            } else {
                database.mainDao().pin(selectedNote.getID(), true);
                Toast.makeText(MainActivity.this, "Заметка закреплена", Toast.LENGTH_SHORT).show();
            }
            notes.clear();
            notes.addAll(database.mainDao().getAll());
            notesListAdapter.notifyDataSetChanged();
            return true;
        }
        if (item.getItemId() == R.id.delete) {
            trashListAdapter.moveToTrash(selectedNote.getID(), true);
            Toast.makeText(MainActivity.this, "Заметка перемещена в корзину", Toast.LENGTH_SHORT).show();

            notes.clear();
            notes.addAll(database.mainDao().getAll());
            notesListAdapter.notifyDataSetChanged();

            listTrash.clear();
            listTrash.addAll(database.mainDao().getTrash());
            trashListAdapter.notifyDataSetChanged();

            updateRecycle(notes);

            return true;
        }
        if (item.getItemId() == R.id.archive) {
            archiveListAdapter.moveToArchive(selectedNote.getID(), true);

            notes.clear();
            notes.addAll(database.mainDao().getAll());
            notesListAdapter.notifyDataSetChanged();

            listArchive.clear();
            listArchive.addAll(database.mainDao().getArchive());
            archiveListAdapter.notifyDataSetChanged();

            updateRecycle(notes);

            return true;
        }

        return false;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(navigationView);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerItemClickListener.onNavigationItemSelected(item);
        drawerLayout.closeDrawers();
        return true;
    }

    private final NotesClickListener notesClickListener = new NotesClickListener() {
        @Override
        public void onClick(Notes notes) {
            Intent intent = new Intent(MainActivity.this, NotesTakerActivity.class);
            intent.putExtra("old_note", notes);
            startActivityForResult(intent, 102);
        }

        @Override
        public void onLongClick(Notes notes, CardView cardView) {
            selectedNote = notes;
            showPopUp(cardView);
        }
    };

    private void showPopUp(CardView cardView) {
        PopupMenu popupMenu = new PopupMenu(this, cardView);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }
}
