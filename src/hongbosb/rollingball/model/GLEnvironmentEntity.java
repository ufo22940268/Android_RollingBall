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
    protected int mProgram; 

    private FloatBuffer mPosBuffer;
    private FloatBuffer mTexCoordBuffer;

    private int maVertexPosHandler;
    private int maTexCoordHandler;
    private int muSamplerHandler;
    private int mTextureHandler;

    static public final float TRIANGLE_POS_ARRAY[] = {
        -0.5f  , 0.5f  ,
        0.5f , 0.5f ,
        -0.5f  , -0.5f ,
        0.5f  , -0.5f ,
    };

    static public final float TEXTURE_COORD_ARRAY[] = {
        0.0f  , 0.0f  ,
        1.0f , 0.0f ,
        0.0f  , 1.0f ,
        1.0f  , 1.0f ,
    };

    public GLEnvironmentEntity(Context context, int width, int height) {
        mContext = context;

        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        //Init program.
        ProgramLoader pLoader = new ProgramLoader(context, "environment_vertex_shader.glsl", "environment_fragment_shader.glsl");
        mProgram = pLoader.load();

        maVertexPosHandler = GLES20.glGetAttribLocation(mProgram, "a_VertexPos");
        maTexCoordHandler = GLES20.glGetAttribLocation(mProgram, "a_TexCoord");

        mPosBuffer = ByteBuffer.allocateDirect(TRIANGLE_POS_ARRAY.length*FLOAT_SIZE)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPosBuffer.put(TRIANGLE_POS_ARRAY).position(0);

        //Init texture.
        TextureLoader tLoader = new TextureLoader(context, R.drawable.wood_texture);
        mTextureHandler = tLoader.load();

        muSamplerHandler = GLES20.glGetUniformLocation(mProgram, "u_Sampler");
        mTexCoordBuffer = ByteBuffer.allocateDirect(TEXTURE_COORD_ARRAY.length*FLOAT_SIZE)
                .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mTexCoordBuffer.put(TEXTURE_COORD_ARRAY).position(0);
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);

        GLES20.glVertexAttribPointer(maTexCoordHandler,
                2, GLES20.GL_FLOAT, false, 0, mTexCoordBuffer);
        GLES20.glEnableVertexAttribArray(maTexCoordHandler);
        GLES20.glUniform1i(muSamplerHandler, 0);
        GLES20.glVertexAttribPointer(maVertexPosHandler,
                2, GLES20.GL_FLOAT, false, 0, mPosBuffer);
        GLES20.glEnableVertexAttribArray(maVertexPosHandler);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, 0, 4);

        Utils.checkGLError();
    }
}
