package hongbosb.rollingball.util;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.opengl.*;

import java.util.*;
import java.io.*;

import hongbosb.rollingball.*;

public class ProgramLoader implements Loadable {
    public final String TAG = "Utils";

    static public final boolean DEBUG = false;

    private String mVertexFile;
    private String mFragmentFile;
    private Context mContext;
    private String[] mAttribNames;

    public ProgramLoader(Context context, String vertexFile, String fragmentFile, String[] attribs) {
        mContext = context;
        mVertexFile = vertexFile;
        mFragmentFile = fragmentFile;
        mAttribNames = attribs;
    }

    private void bindAttribs(int program) {
        for (int i = 0; i < mAttribNames.length; i ++) {
            String attrib = mAttribNames[i];
            GLES20.glBindAttribLocation(program, i, attrib);
        }
    }

    @Override
    public int load() {
        int program = GLES20.glCreateProgram();
        if (program == 0) {
            throw new GLException(0);
        }

        bindAttribs(program);

        int vertexHandler = loadShader(program, mVertexFile, GLES20.GL_VERTEX_SHADER);
        int fragmentHandler = loadShader(program, mFragmentFile, GLES20.GL_FRAGMENT_SHADER);
        GLES20.glLinkProgram(program);
        checkLinkError(program);
        return program;
    }

    private int loadShader(int program, String file, int type) {
        int shaderHandler = GLES20.glCreateShader(type);
        if (shaderHandler == 0) {
            throw new GLException(0);
        }

        String source = loadSource(file);
        GLES20.glAttachShader(program, shaderHandler);
        GLES20.glShaderSource(shaderHandler, source);
        GLES20.glCompileShader(shaderHandler);
        checkCompileError(shaderHandler, type);
        return shaderHandler;
    }

    private void checkCompileError(int handler, int type) {
        int[] result = new int[1];
        GLES20.glGetShaderiv(handler, GLES20.GL_COMPILE_STATUS, result, 0);
        if (result[0] == GLES20.GL_FALSE) {
            //Print log.
            String typeStr = type == GLES20.GL_VERTEX_SHADER ? "vertex" : "fragment";
            String log = GLES20.glGetShaderInfoLog(handler);
            Log.d(TAG, typeStr + " compile error:\n" + log);
            GLES20.glDeleteShader(handler);
            throw new GLException(0);
        }
    }

    private void checkLinkError(int program) {
        int result[] = new int[1];
        GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, result, 0);
        if (result[0] == GLES20.GL_FALSE) {
            Log.d(TAG, "link error:\n" + GLES20.glGetProgramInfoLog(program));
            GLES20.glDeleteProgram(program);
            throw new GLException(0);
        }
    }

    private String loadSource(String fileName) {
        try {
            InputStream in = mContext.getAssets().open(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuffer sb = new StringBuffer();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append('\n');
            }

            if (DEBUG) {
                Log.d(TAG, "source: " + sb.toString());
            }
            return sb.toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

