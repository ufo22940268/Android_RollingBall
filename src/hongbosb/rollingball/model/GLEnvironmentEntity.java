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

import static hongbosb.rollingball.NaturalConstants.*;

public class GLEnvironmentEntity extends GLEntity {
    
    private Context mContext;
    private int mWindowWidth;
    private int mWindowHeight;

    protected int mProgram; 

    private FloatBuffer mPosBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mColorBuffer;
    private FloatBuffer mTexCoordBuffer;

    private int maVertexPosHandler;
    private int maTexCoordHandler;
    private int maVertexColorHandler;
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
    private float[] mLightModelMatrix = new float[16];

    private float mViewX = 0.0f;
    private float mViewY = 0.0f;
    private float mViewZ = 3.0f;

    private float mRotateAngle = 0;

    static public final float TRIANGLE_POS_ARRAY[] = {
        //Font face.
        -0.5f , 0.5f  , 0.5f ,
        -0.5f , -0.5f , 0.5f ,
        0.5f  , 0.5f  , 0.5f ,
        0.5f  , -0.5f  , 0.5f ,

        //Right face.
        0.5f , 0.5f  , 0.5f ,
        0.5f , -0.5f , 0.5f ,
        0.5f , 0.5f  , -0.5f,
        0.5f , -0.5f , -0.5f,

        //Background face.
        0.5f  , 0.5f  , -0.5f ,
        0.5f  , -0.5f , -0.5f ,
        -0.5f , 0.5f  , -0.5f ,
        -0.5f , -0.5f , -0.5f ,

        //Left face.
        -0.5f , 0.5f  , -0.5f ,
        -0.5f , -0.5f , -0.5f ,
        -0.5f , 0.5f  , 0.5f  ,
        -0.5f , -0.5f , 0.5f  ,
    };

    static public final float NORMAL_ARRAY[] = {
        //Font face.
        0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.5f,
        0.0f, 0.0f, 0.5f,

        //Right face.
        0.5f, 0.0f, 0.0f,
        0.5f, 0.0f, 0.0f,
        0.5f, 0.0f, 0.0f,
        0.5f, 0.0f, 0.0f,

        //Background face.
        0.0f, 0.0f, -0.5f,
        0.0f, 0.0f, -0.5f,
        0.0f, 0.0f, -0.5f,
        0.0f, 0.0f, -0.5f,

        //Left face.
        -0.5f, 0.0f, 0.0f,
        -0.5f, 0.0f, 0.0f,
        -0.5f, 0.0f, 0.0f,
        -0.5f, 0.0f, 0.0f,
    };

    static public final float VERTEX_COLOR_ARRAY[] = {
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,
        1.0f, 0.0f, 0.0f, 1.0f,

        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,
        0.0f, 1.0f, 0.0f, 1.0f,

        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,
        0.0f, 0.0f, 1.0f, 1.0f,

        1.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f,
        1.0f, 1.0f, 0.0f, 1.0f,
    };

    static public final float TEXTURE_COORD_ARRAY[] = {
        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,

        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,

        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,

        0.0f , 0.0f ,
        0.0f , 1.0f ,
        1.0f , 0.0f ,
        1.0f , 1.0f ,
    };

    private float mLightPosInModelSpace[] = {0.0f, 0.0f, 0.0f, 1.0f};
    private float mLightPosInEyeSpace[] = new float[4];
    private float mLightPosInWorldSpace[] = new float[4];

    public GLEnvironmentEntity(Context context, int width, int height) {
        mContext = context;
        mWindowWidth = width;
        mWindowHeight = height;

        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);

        //Init program.
        ProgramLoader pLoader = new ProgramLoader(context, "environment_vertex_shader.glsl", "environment_fragment_shader.glsl");
        mProgram = pLoader.load();

        maVertexPosHandler = GLES20.glGetAttribLocation(mProgram, "a_VertexPos");
        maTexCoordHandler = GLES20.glGetAttribLocation(mProgram, "a_TexCoord");
        maVertexColorHandler = GLES20.glGetAttribLocation(mProgram, "a_Color");
        maNormalHandler = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        muMVPMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        muMVMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        muSamplerHandler = GLES20.glGetUniformLocation(mProgram, "u_Sampler");
        muLightPosHandler = GLES20.glGetUniformLocation(mProgram, "u_LightPos");

        mPosBuffer = ByteBuffer.allocateDirect(TRIANGLE_POS_ARRAY.length*FLOAT_SIZE)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPosBuffer.put(TRIANGLE_POS_ARRAY).position(0);

        mNormalBuffer = ByteBuffer.allocateDirect(NORMAL_ARRAY.length*FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mNormalBuffer.put(NORMAL_ARRAY).position(0);

        mColorBuffer = ByteBuffer.allocateDirect(VERTEX_COLOR_ARRAY.length*FLOAT_SIZE)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mColorBuffer.put(VERTEX_COLOR_ARRAY).position(0);

        Matrix.setLookAtM(mViewMatrix, 0,
                mViewX, mViewY, mViewZ, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f);

        //Init texture.
        TextureLoader tLoader = new TextureLoader(context, R.drawable.wood_texture);
        mTextureHandler = tLoader.load();

        mTexCoordBuffer = ByteBuffer.allocateDirect(TEXTURE_COORD_ARRAY.length*FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexCoordBuffer.put(TEXTURE_COORD_ARRAY).position(0);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);

        startTimer();
    }

    @Override
    public boolean onKeyDown(int keyCode) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP) {
            mViewX += 0.3;
            System.out.println("++++++++++++++++++++mViewX:" + mViewX + "++++++++++++++++++++");
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mViewX -= 0.3;
            System.out.println("++++++++++++++++++++mViewX:" + mViewX + "++++++++++++++++++++");
            return true;
        }
        return false;
    }

    private TimerTask mUpdatePosTimer = new TimerTask() {
        @Override
        public void run() {
            updateData();
        }

        private void updateData() {
            mRotateAngle -= Math.PI/6;
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
        Matrix.setRotateM(mModelMatrix, 0, mRotateAngle, 0.0f, 1.0f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVMatrixHandler, 1, false, mMVPMatrix, 0);
        
        Matrix.orthoM(mProjectionMatrix, 0,
                -2.0f*((float)mWindowWidth/mWindowHeight), 2.0f*((float)mWindowWidth/mWindowHeight), -2.0f, 2.0f,
                1.0f, 10.0f);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandler, 1, false, mMVPMatrix, 0);

        Matrix.setIdentityM(mLightModelMatrix, 0);
        Matrix.translateM(mLightModelMatrix, 0, 0.0f, 0.0f, 4.8f);
        Matrix.multiplyMV(mLightPosInWorldSpace, 0, mLightModelMatrix, 0, mLightPosInModelSpace, 0);
        Matrix.multiplyMV(mLightPosInEyeSpace, 0, mViewMatrix, 0, mLightPosInWorldSpace, 0);
        GLES20.glUniform3f(muLightPosHandler,
                mLightPosInEyeSpace[0],
                mLightPosInEyeSpace[1],
                mLightPosInEyeSpace[2]);

        //Draw arrays.
        GLES20.glVertexAttribPointer(maTexCoordHandler,
                2, GLES20.GL_FLOAT, false, 0, mTexCoordBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoordHandler);
        GLES20.glUniform1i(muSamplerHandler, 0);
        
        GLES20.glVertexAttribPointer(maVertexColorHandler,
                4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        GLES20.glEnableVertexAttribArray(maVertexColorHandler);
        
        GLES20.glVertexAttribPointer(maVertexPosHandler,
                3, GLES20.GL_FLOAT, false, 0, mPosBuffer);
        GLES20.glEnableVertexAttribArray(maVertexPosHandler);
        
        GLES20.glVertexAttribPointer(maNormalHandler,
                3, GLES20.GL_FLOAT, false, 0, mNormalBuffer);
        GLES20.glEnableVertexAttribArray(maNormalHandler);

        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 4, 4);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 8, 4);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 12, 4);
    }
}
