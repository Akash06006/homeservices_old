package com.example.services.utils;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Build;

public class Res extends Resources {
    Integer color;

    public Res(Resources original,Integer color) {

        super(original.getAssets(), original.getDisplayMetrics(), original.getConfiguration());
        this.color =color;
    }

    @Override public int getColor(int id) throws NotFoundException {
        return getColor(id, null);
    }

    @Override public int getColor(int id, Theme theme) throws NotFoundException {
        switch (getResourceEntryName(id)) {
            case "colorPrimary":
                // You can change the return value to an instance field that loads from SharedPreferences.
                return color; // used as an example. Change as needed.
            default:
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return super.getColor(id, theme);
                }
                return super.getColor(id);
        }
    }
}