package hongbosb.rollingball;

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

import static hongbosb.rollingball.NaturalConstants.*;

public class GLEnvironmentEntity extends GLEntity {
    
    private Context mContext;
    protected int mProgram; 

    private FloatBuffer mPosBuffer;

    private int maVertexPosHandler;

    static public final float TRIANGLE_POS_ARRAY[] = {
        0.0f  , 0.5f  ,
        -0.5f , -0.5f ,
        0.5f  , -0.5f ,
    };

    public GLEnvironmentEntity(Context context, int width, int height) {
        mContext = context;

        GLES20.glViewport(0, 0, width, height);
        GLES20.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);

        //Init program.
        ProgramLoader pLoader = new ProgramLoader(context, "environment_vertex_shader.glsl", "environment_fragment_shader.glsl");
        mProgram = pLoader.load();

        maVertexPosHandler = GLES20.glGetAttribLocation(mProgram, "a_VertexPos");

        mPosBuffer = ByteBuffer.allocateDirect(4*2*FLOAT_SIZE)
            .order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPosBuffer.put(TRIANGLE_POS_ARRAY).position(0);

        //Load texture.
        TextureLoader tLoader = new TextureLoader(context, R.drawable.wood_texture);
    }

    @Override
    public void draw() {
        GLES20.glUseProgram(mProgram);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        GLES20.glVertexAttribPointer(maVertexPosHandler,
                2, GLES20.GL_FLOAT, false, 0, mPosBuffer);
        GLES20.glEnableVertexAttribArray(maVertexPosHandler);
        GLES20.glDrawArrays(GLES20.GL_TRIANGLES, 0, 3);
    }
}
