package hongbosb.rollingball.util;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.graphics.*;
import android.opengl.*;
import android.opengl.Matrix;

import java.util.*;

import hongbosb.rollingball.data.*;

public class CommonUtils {

    static private final float[] LIGHT_POSITION = {0f, 0f, 100f, 1f};

    public static float[] initProjectionMatrix() {
        float[] matrix = new float[16];
        Matrix.orthoM(matrix, 0,
                -250.0f*((float)SharedData.mWindowWidth/SharedData.mWindowHeight),
                250.0f*((float)SharedData.mWindowWidth/SharedData.mWindowHeight),
                -250.0f,
                250.0f,
                0.5f, 300.0f);
        return matrix;
    }

    public static float[] initLightVector() {
        return LIGHT_POSITION;
    }

    public static float[] initViewMatrix() {
        float[] matrix = new float[16];
        Matrix.setLookAtM(matrix, 0,
                0f, 0f, 200f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);
        return matrix;
    }
}
