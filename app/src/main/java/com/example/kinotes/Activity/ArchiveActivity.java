//TODO:при перемещение заметки с архива на главный экран, она пропадает из архива только после обновления экрана
package com.example.kinotes.Activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kinotes.Adapter.ArchiveListAdapter;
import com.example.kinotes.DataBase.RoomDB;
import com.example.kinotes.Menu.DrawerItemClickListener;
import com.example.kinotes.Models.Notes;
import com.example.kinotes.R;
import com.example.kinotes.Util.BurgerButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;

public class ArchiveActivity extends AppCompatActivity {

    // Адаптер для отображения архивных заметок
    private ArchiveListAdapter archiveListAdapter;

    // Флаг для отслеживания восстановленных заметок
    private boolean notesRestored = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_archive);

        // Инициализация компонентов навигации
        DrawerLayout drawer_layout = findViewById(R.id.drawer_layout);
        DrawerItemClickListener drawerItemClickListener = new DrawerItemClickListener(this, drawer_layout);

        FloatingActionButton burger_button = findViewById(R.id.burger_button);
        BurgerButton.setupBurgerButton(burger_button, drawerItemClickListener);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(drawerItemClickListener);

        // Инициализация списка архивных заметок
        ListView listViewArchive = findViewById(R.id.listViewArchive);

        // Инициализация адаптера для списка архивных заметок
        archiveListAdapter = new ArchiveListAdapter(this, R.layout.archive_list, RoomDB.getInstance(this).mainDao().getArchive(), new ArrayList<>());

        // Установка адаптера для списка
        listViewArchive.setAdapter(archiveListAdapter);

        // Обработка кликов по элементам списка
        listViewArchive.setOnItemClickListener((adapterView, view, position, id) -> {
            // Получение выбранной заметки
            Notes selectedNote = archiveListAdapter.getItem(position);
            if (selectedNote != null) {
                // Вызов метода восстановления заметки из архива
                archiveListAdapter.restoreFromArchive(selectedNote);
                // Установка флага, если заметка была восстановлена
                notesRestored = true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        // Создание интента с информацией о восстановленных заметках
        Intent intent = new Intent();
        intent.putExtra("notesRestored", notesRestored);
        // Установка результата и закрытие активити
        setResult(Activity.RESULT_OK, intent);
        super.onBackPressed();
    }
}
