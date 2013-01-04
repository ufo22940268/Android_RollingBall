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

public class GLEnvironmentEntity extends GLEntity implements GLInputable {
    
    static public final float ROTATE_SCALE_FACTOR = 0.5f;
    static public final float TRANSLATE_SCALE_FACTOR = 0.5f;

    static public final int BALL_TIMER_UPDATE_INTERVAL = 10;

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

    private GLBallEntity mBallEntity;

    public GLEnvironmentEntity(Context context) {
        mContext = context;

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

        mBallEntity = new GLBallEntity(mContext);
        addEntity(mBallEntity);

        startTimer();
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
        super.onTouch(event);
        float x = event.getX();
        float y = event.getY();
        if (event.getAction() == MotionEvent.ACTION_MOVE) {
            float dx = x - mPreviousX;
            float dy = y - mPreviousY;
            //Don't rotate board when testing ball rotate.
            //rotate(dx*ROTATE_SCALE_FACTOR, dy*ROTATE_SCALE_FACTOR);
            
            mBallEntity.translate(dx*TRANSLATE_SCALE_FACTOR, -dy*TRANSLATE_SCALE_FACTOR);
        }
        mPreviousX = x;
        mPreviousY = y;
        return true;
    }

    private TimerTask mUpdatePosTimer = new TimerTask() {

        static public final int DIRECTION_DOWN   = 1;
        static public final int DIRECTION_UP     = -1;
        static public final int DIRECTION_IDLE = 0;

        private float mBallAbsVelocity = 0;
        private int mDirection = 1;

        @Override
        public void run() {
            updateBallPosition();
        }

        //This method is called every time the timer update.
        //The interval is defined in BALL_TIMER_UPDATE_INTERVAL.
        private void updateBallPosition() {
            //Convert the unit of timer to second.
            float interval = (float)BALL_TIMER_UPDATE_INTERVAL/1000;

            float distance = interval*mBallAbsVelocity;
            mBallEntity.fall(distance*mDirection);   

            int newDirection = getDirection();
            if (newDirection == DIRECTION_IDLE) {
                cancel();
                return;
            } else if (newDirection != mDirection) {
                evalVelocityLoss();
                mDirection = newDirection;
            }

            mBallAbsVelocity = mBallAbsVelocity + mDirection*interval*NaturalConstants.GRAVITY;
        }

        private void evalVelocityLoss() {
            mBallAbsVelocity = mBallAbsVelocity*3/4;
        }

        private int getDirection() {
            if (mBallAbsVelocity == 0 && mBallEntity.isMeetEdge()) {
                return DIRECTION_IDLE;
            } else if (mBallEntity.isMeetEdge() || mBallAbsVelocity == 0) {
                return -mDirection;
            } else {
                return mDirection;
            }
        }
        
    };

    private void startTimer() {
        Timer timer = new Timer("ball fallen timer"); 
        timer.schedule(mUpdatePosTimer, 0, BALL_TIMER_UPDATE_INTERVAL);
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(mProgram);

        Matrix.setIdentityM(mModelMatrix, 0);
        Matrix.rotateM(mModelMatrix, 0, mVerticalRotateAngle, 0.0f, 1.0f, 0.0f);
        Matrix.rotateM(mModelMatrix, 0, mHorizontalRotateAngle, 1.0f, 0.0f, 0.0f);

        Matrix.multiplyMM(mMVPMatrix, 0, mViewMatrix, 0, mModelMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVMatrixHandler, 1, false, mMVPMatrix, 0);
        
        Matrix.multiplyMM(mMVPMatrix, 0, mProjectionMatrix, 0, mMVPMatrix, 0);
        GLES20.glUniformMatrix4fv(muMVPMatrixHandler, 1, false, mMVPMatrix, 0);

        GLES20.glUniform3fv(muLightPosHandler, 1, CommonUtils.initLightPositionVector(), 0);

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
        mBallEntity.draw();
    }
}
