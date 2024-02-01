package com.example.kinotes.DataBase;

import static androidx.room.OnConflictStrategy.REPLACE;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.kinotes.Models.Notes;

import java.util.List;

@Dao
public interface MainDao {

    @Insert(onConflict = REPLACE)
    void insert(Notes notes);

    @Query("SELECT * FROM notes WHERE inTrash = 0 ORDER BY pinned DESC, createdAt ASC")
    List<Notes> getAll();
    @Query("SELECT * FROM notes WHERE inTrash = 1 ORDER BY createdAt ASC")
    List<Notes> getTrash();
    @Query("SELECT * FROM notes WHERE inArchive = 1")
    List<Notes> getArchive();
    @Query("UPDATE notes SET title = :title, notes = :notes WHERE ID = :id")
    void update(int id, String title, String notes);

    @Query("UPDATE notes SET pinned = :pin WHERE ID = :id")
    void pin(int id, boolean pin);

    @Query("UPDATE notes SET inTrash = :inTrash WHERE ID = :id")
    void moveToTrash(int id, boolean inTrash);
    @Query("UPDATE notes SET inArchive = :inArchive WHERE ID = :id")
    void moveToArchive(int id, boolean inArchive);
    @Query("DELETE FROM notes WHERE ID = :id")
    void deleteNoteById(int id);
}
