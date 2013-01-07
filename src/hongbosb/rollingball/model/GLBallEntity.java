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
    static public final int DIRECTION_IDLE = 0;
    static public final float INTERVAL_IN_SECONDS = (float)GLEnvironmentEntity.BALL_TIMER_UPDATE_INTERVAL/1000;
    static public final float ABS_MAX_X = EnvironmentData.BOARD_SIZE - BALL_RADIUS;
    static public final float ABS_MAX_Y = EnvironmentData.BOARD_SIZE - BALL_RADIUS;

    static public final float VELOCITY_STATIC_THRESHOLD = 4;

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

    private float mVerticalRotateAngle = 0.0f;
    private float mHorizontalRotateAngle = 0.0f;

    public float mTranslateX = 0f;
    public float mTranslateY = 0f;
    public float mVelocityX = 0f;
    public float mVelocityY = 0f;

    //The bottom when the ball fall because of gravity.
    private float mFallBottom = -EnvironmentData.BOARD_SIZE;

    private Context mContext;

    private Sphere mSphere;

    //Only for test use.
    public GLBallEntity() {
    }

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

    public boolean isMeetEdge() {
        if (Math.abs(mTranslateX) >= ABS_MAX_X ||
                Math.abs(mTranslateY) >= ABS_MAX_Y) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isMeetEdge(float deltaTranslateX, float deltaTranslateY) {
        if (Math.abs(mTranslateX + deltaTranslateX) >= ABS_MAX_X ||
                Math.abs(mTranslateY + deltaTranslateY) >= ABS_MAX_Y) {
            return true;
        } else {
            return false;
        }
    }

    public boolean canMove(float deltaVelocityX, float deltaVelocityY) {
        float dx = INTERVAL_IN_SECONDS*(mVelocityX + deltaVelocityX*INTERVAL_IN_SECONDS);
        float dy = INTERVAL_IN_SECONDS*(mVelocityY + deltaVelocityY*INTERVAL_IN_SECONDS);
        if (Math.abs(mTranslateX + dx) >= ABS_MAX_X ||
                Math.abs(mTranslateY + dy) >= ABS_MAX_Y) {
            return false;
        } else {
            return true;
        }
    }

    public void fixEdgePosition() {
        if (Math.abs(mTranslateX) - (double)ABS_MAX_X > 0) {
            mTranslateX = ABS_MAX_X*(mTranslateX > 0 ? 1 : -1);
        }

        if (Math.abs(mTranslateY) - (double)ABS_MAX_Y > 0) {
            mTranslateY = ABS_MAX_Y*(mTranslateY > 0 ? 1 : -1);
        }
    }

    public void resetPosition() {
        mTranslateX = 0;
        mTranslateY = 0;
    }

    public void translate(float deltaX, float deltaY) {
        if (!isMeetEdge(deltaX, deltaY)) {
            mTranslateX += deltaX;
            mTranslateY += deltaY;
        }
    }

    //This method is called every time the timer update.
    //The INTERVAL_IN_SECONDS is defined in BALL_TIMER_UPDATE_INTERVAL.
    public boolean updatePosition(float tiltingAngle) {
        fixEdgePosition();

        float dx = INTERVAL_IN_SECONDS*mVelocityX;
        float dy = INTERVAL_IN_SECONDS*mVelocityY;
        translate(dx, dy);   
        updateVolecity(tiltingAngle);
        return true;
    }

    private void updateVolecity(float tiltingAngle) {
        float dv = NaturalConstants.GRAVITY*INTERVAL_IN_SECONDS;    
        float dvx = (float)(dv*Math.cos(tiltingAngle));
        float dvy = (float)(dv*Math.sin(tiltingAngle));
        System.out.println("++++++++++++++++++++dvx:" + dvx + "\tdvy:" + dvy + "++++++++++++++++++++");
        if (canMove(dvx, dvy)) {
            mVelocityX += dvx;
            mVelocityY += dvy;
        }
    }

    //TODO
    public void reset() {
        mVerticalRotateAngle = 0.0f;
        mHorizontalRotateAngle = 0.0f;
        mTranslateX = 0f;
        mTranslateY = 0f;
    }

    //TODO
    private void evalVelocityLoss() {
        //mBallAbsVelocity = mBallAbsVelocity*3/4;
    }

    public void rotate(float dVert, float dHori) {
        mVerticalRotateAngle += dVert;
        mHorizontalRotateAngle += dHori;
    }

    public void draw() {
        GLES20.glUseProgram(mProgram);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, mVerticalRotateAngle, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, mHorizontalRotateAngle, 1.0f, 0.0f, 0.0f);

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
