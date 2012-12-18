package hongbosb.rollingball;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;

import java.util.*;

public abstract class GLDrawable {

    private Context mContext;

    private int mEnvProgram; 

    public GLDrawable(Context context, int width, int height) {
        mContext = context;

        mEnvProgram = Utils.loadProgram(context, "environment_vertex_shader.glsl", "environment_fragment_shader.glsl");
    }
}

