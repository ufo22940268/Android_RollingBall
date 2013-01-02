package hongbosb.rollingball.model;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.opengl.*;

import java.util.*;
import java.nio.*;

import hongbosb.rollingball.util.*;
import hongbosb.rollingball.*;
import hongbosb.rollingball.data.*;

import static hongbosb.rollingball.NaturalConstants.*;

public class GLEnvironmentEntity extends GLEntity {
    
    static public final float ROTATE_SCALE_FACTOR = 0.5f;

    private Context mContext;

    private float mPreviousX;
    private float mPreviousY;

    protected int mProgram; 

    private FloatBuffer mPosBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mColorBuffer;
    private FloatBuffer mTexCoordBuffer;

    private int maPosHandler;
    private int maTexCoordHandler;
    private int maColorHandler;
    private int maNormalHandler;
    private int muMVPMatrixHandler;
    private int muMVMatrixHandler;
    private int muSamplerHandler;
    private int muLightPosHandler;
    private int mTextureHandler;

    private float[] mMVPMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];

    private float mViewX = 0.0f;
    private float mViewY = 0.0f;
    private float mViewZ = 200.0f;

    //Vertical rotate is the rotation around y axis.
    private float mVerticalRotateAngle = 0.0f;

    //Horizontal rotate is the rotation around x axis.
    private float mHorizontalRotateAngle = 0.0f;

    private float mLightRotateAngle = 0.0f;

    private Sphere mSphere;

    public GLEnvironmentEntity(Context context, int width, int height) {
        mContext = context;
        SharedData.mWindowWidth = width;
        SharedData.mWindowHeight = height;

        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //Init program.
        String[] attribs = {"a_VertexPos", "a_TexCoord", "a_Color", "a_Normal",};
        ProgramLoader pLoader = new ProgramLoader(context,
                "environment_vertex_shader.glsl", "environment_fragment_shader.glsl", attribs );
        mProgram = pLoader.load();

        maPosHandler = GLES20.glGetAttribLocation(mProgram, "a_VertexPos");
        maTexCoordHandler = GLES20.glGetAttribLocation(mProgram, "a_TexCoord");
        maColorHandler = GLES20.glGetAttribLocation(mProgram, "a_Color");
        maNormalHandler = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        muMVPMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        muMVMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        muSamplerHandler = GLES20.glGetUniformLocation(mProgram, "u_Sampler");
        muLightPosHandler = GLES20.glGetUniformLocation(mProgram, "u_LightPos");

        mPosBuffer = ByteBuffer.allocateDirect(EnvironmentData.TRIANGLE_POS_ARRAY.length*FLOAT_SIZE)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPosBuffer.put(EnvironmentData.TRIANGLE_POS_ARRAY).position(0);

        mNormalBuffer = ByteBuffer.allocateDirect(EnvironmentData.NORMAL_ARRAY.length*FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNormalBuffer.put(EnvironmentData.NORMAL_ARRAY).position(0);

        mColorBuffer = ByteBuffer.allocateDirect(EnvironmentData.VERTEX_COLOR_ARRAY.length*FLOAT_SIZE)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mColorBuffer.put(EnvironmentData.VERTEX_COLOR_ARRAY).position(0);

        mProjectionMatrix = CommonUtils.initProjectionMatrix();
        mViewMatrix = CommonUtils.initViewMatrix();

        //Init texture.
        TextureLoader tLoader = new TextureLoader(context, R.drawable.wood_texture);
        mTextureHandler = tLoader.load();

        mTexCoordBuffer = ByteBuffer.allocateDirect(EnvironmentData.TEXTURE_COORD_ARRAY.length*FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexCoordBuffer.put(EnvironmentData.TEXTURE_COORD_ARRAY).position(0);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        //startTimer();
        //TODO
        mSphere = new Sphere(10, 3);
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mViewX += 0.3;
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mViewX -= 0.3;
            return true;
        }
        return false;
    }

    private void rotate(float dVert, float dHori) {
        mVerticalRotateAngle += dVert;
        mHorizontalRotateAngle += dHori;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;
            rotate(dx*ROTATE_SCALE_FACTOR, dy*ROTATE_SCALE_FACTOR);
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private TimerTask mUpdatePosTimer = new TimerTask() {
        @Override
        public void run() {
            updateData();
        }

        private void updateData() {
            //Actually, the unit of angle is degree. But because the short interval
            //of timer task, so the animation appears just fine.
            //mVerticalRotateAngle -= Math.PI/6;

            mLightRotateAngle -= 0.5;
        }
    };

    private void startTimer() {
        Timer timer = new Timer("view data timer"); 
        timer.schedule(mUpdatePosTimer, 0, 10);
    }

    @Override
    public void draw() {
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glUseProgram(mProgram);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, mVerticalRotateAngle, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, mHorizontalRotateAngle, 1.0f, 0.0f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVMatrixHandler, 1, false, mMVPMatrix, 0);
        
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandler, 1, false, mMVPMatrix, 0);

        GLES20.glUniform3fv(muLightPosHandler, 1, CommonUtils.initLightVector(), 0);

        //Draw arrays.
        GLES20.glVertexAttribPointer(maTexCoordHandler,
                2, GLES20.GL_FLOAT, false, 0, mTexCoordBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoordHandler);
        GLES20.glUniform1i(muSamplerHandler, 0);
        
        GLES20.glVertexAttribPointer(maColorHandler,
                4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        GLES20.glEnableVertexAttribArray(maColorHandler);
        
        GLES20.glVertexAttribPointer(maPosHandler,
                3, GLES20.GL_FLOAT, false, 0, mPosBuffer);
        GLES20.glEnableVertexAttribArray(maPosHandler);
        
        GLES20.glVertexAttribPointer(maNormalHandler,
                3, GLES20.GL_FLOAT, false, 0, mNormalBuffer);
        GLES20.glEnableVertexAttribArray(maNormalHandler);

        int offset = 0;
        for (int count : EnvironmentData.ITEMS_VERTEX_COUNT) {
            GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, offset, count);
            offset += count;
        }
        mSphere.draw(maPosHandler, maNormalHandler, maColorHandler);
    }
}
