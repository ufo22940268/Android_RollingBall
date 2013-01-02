package hongbosb.rollingball.model;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.net.*;
import android.opengl.*;
import android.graphics.*;

import java.util.*;

public class GLSphereEntity {

    //static public final double TO_RADIANS = Math.PI/180;

    //private int mPointCount;
    //private FloatBuffer mPointBuffer;
    //private FloatBuffer mNormalBuffer;
    //private FloatBuffer mColorBuffer;

    //public GLSphereEntity(float radius, int degreeStep) {
        //String[] attribs = {"a_VertexPos", "a_TexCoord", "a_Color", "a_Normal",};
        //ProgramLoader pLoader = new ProgramLoader(context,
                //"sphere_vertex_shader.glsl", "sphere_fragment_shader.glsl", attribs);
        //mProgram = pLoader.load();

        //maPosHandler = GLES20.glGetAttribLocation(mProgram, "a_VertexPos");
        //maTexCoordHandler = GLES20.glGetAttribLocation(mProgram, "a_TexCoord");
        //mColorHandler = GLES20.glGetAttribLocation(mProgram, "a_Color");
        //maNormalHandler = GLES20.glGetAttribLocation(mProgram, "a_Normal");

        //muMVPMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVPMatrix");
        //muMVMatrixHandler = GLES20.glGetUniformLocation(mProgram, "u_MVMatrix");
        //muSamplerHandler = GLES20.glGetUniformLocation(mProgram, "u_Sampler");
        //muLightPosHandler = GLES20.glGetUniformLocation(mProgram, "u_LightPos");

        //build(radius, degreeStep);
    //}

    /**
     * x = rho*sin(phi)*cos(theta)
     * y = rho*sin(phi)*sin(theta)
     * z = rho*cos(phi)
     */
    //private void build(float radius, int degreeStep) {
        //float radiansStep = (float)(degreeStep*TO_RADIANS);
        ////int bufferSize = ((int)(Math.PI/radiansStep + 1) + (int)(Math.PI*2/radiansStep + 1))*3;
        //int bufferSize = 400000;
        //mPointBuffer = ByteBuffer.allocateDirect(bufferSize)
            //.order(ByteOrder.nativeOrder()).asFloatBuffer();
        //mNormalBuffer = ByteBuffer.allocateDirect(bufferSize).
            //order(ByteOrder.nativeOrder()).asFloatBuffer();
        //mColorBuffer = ByteBuffer.allocateDirect(bufferSize*4/3)
            //.order(ByteOrder.nativeOrder()).asFloatBuffer();

        //int count = 0;
        //for (float phi = 0; phi <= Math.PI; phi += radiansStep) {
            //for (float theta = 0; theta <= 2*Math.PI; theta += radiansStep) {
                //mPointBuffer.put((float)(radius*Math.sin(phi)*Math.cos(theta)));   //x
                //mPointBuffer.put((float)(radius*Math.sin(phi)*Math.sin(theta)));   //y
                //mPointBuffer.put((float)(radius*Math.cos(phi)));                   //z

                //mNormalBuffer.put(80*(float)(Math.sin(phi)*Math.cos(theta)));      //x
                //mNormalBuffer.put(80*(float)(Math.sin(phi)*Math.sin(theta)));      //y
                //mNormalBuffer.put(80*(float)(Math.cos(phi)));                      //z

                //mColorBuffer.put((float)0x66/0xff);
                //mColorBuffer.put((float)0xcc/0xff);
                //mColorBuffer.put((float)0xff/0xff);
                //mColorBuffer.put((float)1.0);

                //count += 1;
            //}
        //}

        //mPointBuffer.position(0);
        //mNormalBuffer.position(0);
        //mColorBuffer.position(0);
        //mPointCount = count;
    //}

    //public void draw() {
        //GLES20.glVertexAttribPointer(maPosHandler,
                //3, GLES20.GL_FLOAT, false, 0, mPointBuffer);
        //GLES20.glEnableVertexAttribArray(maPosHandler);

        //GLES20.glVertexAttribPointer(maNormalHandler,
                //3, GLES20.GL_FLOAT, false, 0, mNormalBuffer);
        //GLES20.glEnableVertexAttribArray(maNormalHandler);

        //GLES20.glVertexAttribPointer(mColorHandler,
                //4, GLES20.GL_FLOAT, false, 0, mColorBuffer);
        //GLES20.glEnableVertexAttribArray(mColorHandler);

        //GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, 0, mPointCount);
    /*}*/
}
