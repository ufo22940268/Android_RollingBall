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

public class GLBallEntity extends GLEntity {

    static public final float BALL_RADIUS = 10;

    protected int mProgram; 

    private FloatBuffer mPosBuffer;
    private FloatBuffer mNormalBuffer;
    private FloatBuffer mColorBuffer;
    private FloatBuffer mTexCoordBuffer;

    private int maPosHandler;
    private int maColorHandler;
    private int maNormalHandler;
    private int muMVPMatrixHandler;
    private int muMVMatrixHandler;
    private int muLightPosHandler;

    private float[] mMVPMatrix = new float[16];
    private float[] mLightMVMatrix = new float[16];
    private float[] mViewMatrix = new float[16];
    private float[] mModelMatrix = new float[16];
    private float[] mProjectionMatrix = new float[16];

    private float mTranslateX = 0f;
    private float mTranslateY = 0f;
    
    private Context mContext;

    private Sphere mSphere;

    public GLBallEntity(Context context) {
        mContext = context;
        //Init program.
        String[] attribs = {"a_VertexPos", "a_Color", "a_Normal"};
        ProgramLoader pLoader = new ProgramLoader(context,
                "sphere_vertex_shader.glsl", "sphere_fragment_shader.glsl", attribs );
        mProgram = pLoader.load();

        maPosHandler = GLES20.glGetAttribLocation(mProgram, "a_VertexPos");
        maColorHandler = GLES20.glGetAttribLocation(mProgram, "a_Color");
        maNormalHandler = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        muMVPMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        muMVMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        muLightPosHandler = GLES20.glGetUniformLocation(mProgram, "u_LightPos");

        mProjectionMatrix = CommonUtils.initProjectionMatrix();
        mViewMatrix = CommonUtils.initViewMatrix();
        //Move ball upper to make the ball set upon the surface of 
        //maze floor.
        Matrix.translateM(mModelMatrix, 0, 0f, 0f, BALL_RADIUS);

        mSphere = new Sphere(BALL_RADIUS, 8);

        GLES20.glEnable(GLES20.GL_DEPTH_TEST);
        GLES20.glEnable(GLES20.GL_CULL_FACE);
    }

    public void translate(float deltaX, float deltaY) {
        mTranslateX += deltaX;
        mTranslateY += deltaY;
    }

    public void draw() {
        GLES20.glUseProgram(mProgram);

        Matrix.setIdentityM(mModelMatrix, 0);

        GLES20.glUniform3fv(muLightPosHandler, 1, CommonUtils.initLightPositionVector(), 0);
        Matrix.multiplyMM(mLightMVMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVMatrixHandler, 1, false, mLightMVMatrix, 0);

        Matrix.translateM(mModelMatrix, 0, mTranslateX, mTranslateY, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandler, 1, false, mMVPMatrix, 0);

        mSphere.draw(maPosHandler, maNormalHandler, maColorHandler);
    }
}
