package hongbosb.rollingball.util;

import android.util.*;
import android.opengl.GLES20;

public class Utils {
    static public final String TAG = "Utils";
    public static void checkGLError() {
        int errorCode = GLES20.glGetError();
        if (errorCode != GLES20.GL_NO_ERROR) {
            Log.d(TAG, "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^error! code " + errorCode + "^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
        }
    }
}
