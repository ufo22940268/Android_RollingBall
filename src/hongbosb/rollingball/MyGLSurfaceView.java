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
import hongbosb.rollingball.data.*;

public class MyGLSurfaceView extends GLSurfaceView implements GLInputable {

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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mRender.onTouch(event);
    }

    public class MyRenderer implements GLSurfaceView.Renderer, GLInputable {

        private Context mContext;

        private GLEnvironmentEntity mEntity;

        public MyRenderer(Context context) {
            mContext = context;
        }

        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        }

        public void onSurfaceChanged(GL10 gl, int width, int height) {
            SharedData.mWindowWidth = width;
            SharedData.mWindowHeight = height;

            GLES20.glViewport(0, 0, width, height);
            GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
            mEntity = new GLEnvironmentEntity(mContext);
        }

        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
            mEntity.draw();
        }

        @Override
        public boolean onKeyDown(int keyCode) {
            if (mEntity != null) {
                return mEntity.onKeyDown(keyCode);
            }
            return false;
        }

        public boolean onTouch(MotionEvent event) {
            if (mEntity != null) {
                return mEntity.onTouch(event);
            }
            return false;
        }
    }
}


