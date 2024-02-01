package com.example.kinotes.Menu;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kinotes.Activity.AboutActivity;
import com.example.kinotes.Activity.ArchiveActivity;
import com.example.kinotes.MainActivity.MainActivity;
import com.example.kinotes.R;
import com.example.kinotes.Activity.ReminderActivity;
import com.example.kinotes.Activity.SettingsActivity;
import com.example.kinotes.Activity.TrashActivity;
import com.google.android.material.navigation.NavigationView;

public class DrawerItemClickListener implements NavigationView.OnNavigationItemSelectedListener {

    private Context context;
    private DrawerLayout drawerLayout;

    public DrawerItemClickListener(Context context, DrawerLayout drawerLayout) {
        this.context = context;
        this.drawerLayout = drawerLayout;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.nav_notes) {
            openMainActivity();
            return true;
        } else if (itemId == R.id.nav_reminders) {
            openReminder();
            return true;
        } else if (itemId == R.id.nav_archive) {
            openArchive();
            return true;
        } else if (itemId == R.id.nav_trash) {
            openTrash();
            return true;
        } else if (itemId == R.id.nav_settings) {
            openSettingsActivity();
            return true;
        } else if (itemId == R.id.nav_about) {
            openAboutActivity();
            return true;
        }

        return false;
    }

    private void openArchive() {
        Intent intent = new Intent(context, ArchiveActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    private void openReminder() {
        Intent intent = new Intent(context, ReminderActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void openTrash() {
        Intent intent = new Intent(context, TrashActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void openMainActivity() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void openAboutActivity() {
        Intent intent = new Intent(context, AboutActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    private void openSettingsActivity() {
        Intent intent = new Intent(context, SettingsActivity.class);
        context.startActivity(intent);
        ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void openDrawer() {
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
            ((Activity) context).overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        }
    }
}