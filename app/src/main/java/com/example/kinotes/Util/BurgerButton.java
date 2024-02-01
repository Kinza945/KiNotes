package com.example.kinotes.Util;

import com.example.kinotes.Menu.DrawerItemClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class BurgerButton {
    public static void setupBurgerButton(FloatingActionButton burgerButton, final DrawerItemClickListener drawerItemClickListener) {
        if (burgerButton != null) {
            burgerButton.setOnClickListener(v -> {
                if (drawerItemClickListener != null) {
                    drawerItemClickListener.openDrawer();
                }
            });
        }
    }
}
