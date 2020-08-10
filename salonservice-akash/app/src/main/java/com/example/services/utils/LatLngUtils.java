package com.example.services.utils;

import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

/**
 * Created by dhimanabhishek2525 on 19/7/2016.
 */
public class LatLngUtils {

    private LatLngUtils() {
    }

    public static float distanceBetween(LatLng first, LatLng second) {
        float[] distance = new float[1];
        Location.distanceBetween(first.latitude, first.longitude, second.latitude, second.longitude, distance);
        return distance[0];
    }

    public static LatLng fromLocation(Location location) {
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    public static int getNearestPointIdx(List<LatLng> mPath, LatLng a) {
        return 0;
    }

    public static double calculatePathDistance(List<LatLng> mPath, int mFromIdx, int mToIdx) {
        return 0;
    }

    public static double distance(LatLng a, LatLng latLng) {
        return 0;
    }
}
