package hongbosb.rollingball;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.net.*;
import android.opengl.*;

import java.util.*;
import javax.microedition.khronos.opengles.*;
import javax.microedition.khronos.egl.*;

public class MyRenderer implements GLSurfaceView.Renderer {

    Context mContext;

    public MyRenderer(Context context) {
        mContext = context;
    }

    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
    }

    public void onSurfaceChanged(GL10 gl, int width, int height) {
    }

    public void onDrawFrame(GL10 gl) {
    }
}
