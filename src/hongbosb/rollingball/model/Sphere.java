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
import java.io.*;
import java.nio.*;

//This code is copied from http://stackoverflow.com/questions/6072308/problem-drawing-a-sphere-in-opengl-es
public class Sphere {

    static private FloatBuffer sphereVertex;
    static private FloatBuffer sphereNormal;
    static float sphere_parms[]=new float[3];

    double mRaduis;
    double mStep;
    float mVertices[];
    private static double DEG = Math.PI/180;
    int mPoints;

    /**
     * The value of step will define the size of each facet as well as the number of facets
     *  
     * @param radius
     * @param step
     */

    public Sphere( float radius, double step) {
        this.mRaduis = radius;
        this.mStep = step;
        //sphereVertex = FloatBuffer.allocate(40000);
        sphereVertex = ByteBuffer.allocateDirect(40000).order(ByteOrder.nativeOrder()).asFloatBuffer();
        mPoints = build();
    }

    public void draw(int vertexHandler) {
        //gl.glFrontFace(GL10.GL_CW);
        //gl.glEnableClientState(GL10.GL_VERTEX_ARRAY);
        //gl.glVertexPointer(3, GL10.GL_FLOAT, 0, sphereVertex);
        //gl.glDrawArrays(GL10.GL_POINTS, 0, mPoints);

        GLES20.glFrontFace(GLES20.GL_CW);
        GLES20.glVertexAttribPointer(vertexHandler,
                3, GLES20.GL_FLOAT, false, 0, sphereVertex);
        GLES20.glEnableVertexAttribArray(vertexHandler);
        GLES20.glDrawArrays(GLES20.GL_POINTS, 0, mPoints);
    }

    private int build() {

        /**
         * x = p * sin(phi) * cos(theta)
         * y = p * sin(phi) * sin(theta)
         * z = p * cos(phi)
         */
        double dTheta = mStep * DEG;
        double dPhi = dTheta;
        int points = 0;

        for(double phi = -(Math.PI/2); phi <= Math.PI/2; phi+=dPhi) {
            //for each stage calculating the slices
            for(double theta = 0.0; theta <= (Math.PI * 2); theta+=dTheta) {
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.cos(theta)) );
                sphereVertex.put((float) (mRaduis * Math.sin(phi) * Math.sin(theta)) );
                sphereVertex.put((float) (mRaduis * Math.cos(phi)) );
                points++;

            }
        }
        sphereVertex.position(0);
        return points;
    }
}
