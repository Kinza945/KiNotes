    package com.example.kinotes.Models;

    import androidx.room.ColumnInfo;
    import androidx.room.Entity;
    import androidx.room.PrimaryKey;

    import java.io.Serializable;

    @Entity(tableName = "notes")
    public class Notes implements Serializable {

        @PrimaryKey(autoGenerate = true)
        int ID = 0;

        @ColumnInfo(name = "title")
        String title = "";

        @ColumnInfo(name = "notes")
        String notes = "";

        @ColumnInfo(name = "date")
        String date = "";

        @ColumnInfo(name = "createdAt")
        long createdAt;

        @ColumnInfo(name = "pinned")
        Boolean pinned = false;

        @ColumnInfo(name = "inTrash")
        private Boolean inTrash;

        @ColumnInfo(name = "inArchive")
        private Boolean inArchive;

        public Boolean getInTrash() {
            return inTrash;
        }

        public void setInTrash(Boolean inTrash) {
            this.inTrash = inTrash;
        }

        public Boolean getInArchive() {
            return inArchive;
        }

        public void setInArchive(Boolean inArchive) {
            this.inArchive = inArchive;
        }

        public int getID() {
            return ID;
        }

        public void setID(int ID) {
            this.ID = ID;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getNotes() {
            return notes;
        }

        public void setNotes(String notes) {
            this.notes = notes;
        }

        public long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(long createdAt) {
            this.createdAt = createdAt;
        }

        public Boolean isPinned() {
            return pinned;
        }

        public void setPinned(Boolean pinned) {
            this.pinned = pinned;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }
    }
