package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.opengl;

import android.graphics.Color;

public class GLColor {
    static float[] darkBlue() {
        return new float[]{
                Color.red(Color.BLUE-100) / 255f,
                Color.green(Color.BLUE-100) / 255f,
                Color.blue(Color.BLUE-100) / 255f,
                1.0f
        };
    }

    static float[] blue() {
        return new float[]{
                Color.red(Color.BLUE) / 255f,
                Color.green(Color.BLUE) / 255f,
                Color.blue(Color.BLUE) / 255f,
                1.0f
        };
    }
}