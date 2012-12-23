package hongbosb.rollingball;

import android.app.*;
import android.content.*;
import android.database.*;
import android.net.*;
import android.opengl.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;

import java.util.*;
import javax.microedition.khronos.egl.*;
import javax.microedition.khronos.opengles.*;

import hongbosb.rollingball.model.*;

public class MyGLSurfaceView extends GLSurfaceView implements GLEventListener {

    private MyRenderer mRender;

    public MyGLSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(2);
        mRender = new MyRenderer(context);
        setRenderer(mRender);
    }

    public boolean onKeyDown(int keyCode) {
        mRender.onKeyDown(keyCode);    
        return true;
    }

    public class MyRenderer implements GLSurfaceView.Renderer, GLEventListener {

        private Context mContext;

        private GLEnvironmentEntity mEntity;

        public MyRenderer(Context context) {
            mContext = context;
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            mEntity = new GLEnvironmentEntity(mContext, width, height);
        }

        public void onDrawFrame(GL10 gl) {
            mEntity.draw();
        }

        @Override
        public boolean onKeyDown(int keyCode) {
            mEntity.onKeyDown(keyCode);
            return true;
        }
    }
}


