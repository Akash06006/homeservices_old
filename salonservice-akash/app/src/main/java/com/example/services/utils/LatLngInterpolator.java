package com.example.services.utils;

import android.util.Log;

import com.google.android.gms.maps.model.LatLng;


import java.util.List;

import static java.lang.Math.asin;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

/**
 * Created by dhimanabhishek2525 on 19/7/2016.
 */
public interface LatLngInterpolator {
    public LatLng interpolate(float fraction, LatLng a, LatLng b);

    public class Linear implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
            double lng = (b.longitude - a.longitude) * fraction + a.longitude;
            return new LatLng(lat, lng);
        }
    }

    public class LinearFixed implements LatLngInterpolator {
        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            double lat = (b.latitude - a.latitude) * fraction + a.latitude;
            double lngDelta = b.longitude - a.longitude;

            // Take the shortest path across the 180th meridian.
            if (Math.abs(lngDelta) > 180) {
                lngDelta -= Math.signum(lngDelta) * 360;
            }
            double lng = lngDelta * fraction + a.longitude;
            return new LatLng(lat, lng);
        }
    }

    public class Spherical implements LatLngInterpolator {

        /* From github.com/googlemaps/android-maps-utils */
        @Override
        public LatLng interpolate(float fraction, LatLng from, LatLng to) {
            // http://en.wikipedia.org/wiki/Slerp
            double fromLat = toRadians(from.latitude);
            double fromLng = toRadians(from.longitude);
            double toLat = toRadians(to.latitude);
            double toLng = toRadians(to.longitude);
            double cosFromLat = cos(fromLat);
            double cosToLat = cos(toLat);

            // Computes Spherical interpolation coefficients.
            double angle = computeAngleBetween(fromLat, fromLng, toLat, toLng);
            double sinAngle = sin(angle);
            if (sinAngle < 1E-6) {
                return from;
            }
            double a = sin((1 - fraction) * angle) / sinAngle;
            double b = sin(fraction * angle) / sinAngle;

            // Converts from polar to vector and interpolate.
            double x = a * cosFromLat * cos(fromLng) + b * cosToLat * cos(toLng);
            double y = a * cosFromLat * sin(fromLng) + b * cosToLat * sin(toLng);
            double z = a * sin(fromLat) + b * sin(toLat);

            // Converts interpolated vector back to polar.
            double lat = atan2(z, sqrt(x * x + y * y));
            double lng = atan2(y, x);
            return new LatLng(toDegrees(lat), toDegrees(lng));
        }

        private double computeAngleBetween(double fromLat, double fromLng, double toLat, double toLng) {
            // Haversine's formula
            double dLat = fromLat - toLat;
            double dLng = fromLng - toLng;
            return 2 * asin(sqrt(pow(sin(dLat / 2), 2) +
                    cos(fromLat) * cos(toLat) * pow(sin(dLng / 2), 2)));
        }
    }

    public class Path implements LatLngInterpolator {

        private List<LatLng> mPath;
        private int mFromIdx;
        private int mToIdx;
        private float mFromFraction;
        private float mToFraction;
        private LinearFixed mOffPathInterpolator = new LinearFixed();
        private boolean mComputed;

        public Path(List<LatLng> path) {
            mPath = path;
            mComputed = false;
        }

        @Override
        public LatLng interpolate(float fraction, LatLng a, LatLng b) {
            if (!mComputed) {
                compute(a, b);
                mComputed = true;
            }

            if (fraction < mFromFraction) {
                // linear interpolation from
                // origin to first point on path
                return mOffPathInterpolator.interpolate(fraction / mFromFraction, a, mPath.get(mFromIdx));
            } else //
                if (fraction > mToFraction) {
                    // linear interpolation
                    // off path to destination
                    return mOffPathInterpolator.interpolate((fraction - mToFraction) / (1f - mToFraction), mPath.get(mToIdx), b);
                } else {
                    int idx = Math.round((fraction - mFromFraction) * (mToIdx - mFromIdx));
                    return mPath.get(idx);
                }
        }

        private void compute(LatLng a, LatLng b) {
            mFromIdx = LatLngUtils.getNearestPointIdx(mPath, a);
            mToIdx = LatLngUtils.getNearestPointIdx(mPath, b);

            int distancePath = (int) LatLngUtils.calculatePathDistance(mPath, mFromIdx, mToIdx);
            int distanceA = (int) LatLngUtils.distance(a, mPath.get(mFromIdx));
            int distanceB = (int) LatLngUtils.distance(b, mPath.get(mToIdx));
            int distance = distanceA + distancePath + distanceB;

            mFromFraction = (float) (((double) distanceA) / ((double) distance));
            mToFraction = (float) (1f - ((double) distanceB) / ((double) distance));

            Log.d("LatLngInterpolator", "compute: mFromIdx=" + mFromIdx + ", mToIdx=" + mToIdx +
                    ", distancePath=" + distancePath + ", distanceA=" + distanceA +
                    ", distanceB=" + distanceB + ", distance" + distance +
                    ", mFromFraction=" + mFromFraction + ", mToFraction=" + mToFraction);
        }
    }
}
