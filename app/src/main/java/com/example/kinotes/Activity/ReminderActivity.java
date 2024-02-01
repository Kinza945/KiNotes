package com.example.kinotes.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.kinotes.Menu.DrawerItemClickListener;
import com.example.kinotes.R;
import com.example.kinotes.Util.BurgerButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

public class ReminderActivity extends AppCompatActivity {
    private DrawerLayout drawer_layout;
    private FloatingActionButton burger_button;
    private DrawerItemClickListener drawerItemClickListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reminder);

        drawer_layout = findViewById(R.id.drawer_layout);
        drawerItemClickListener = new DrawerItemClickListener(this, drawer_layout);

        burger_button = findViewById(R.id.burger_button);
        BurgerButton.setupBurgerButton(burger_button, drawerItemClickListener);

        NavigationView navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(drawerItemClickListener);

        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}