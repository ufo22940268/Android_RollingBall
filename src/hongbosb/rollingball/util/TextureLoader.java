package hongbosb.rollingball.util;

import android.util.*;
import android.widget.*;
import android.view.*;
import android.content.*;
import android.app.*;
import android.os.*;
import android.database.*;
import android.graphics.*;
import android.opengl.*;

import java.util.*;

public class TextureLoader implements Loadable {

    private Context mContext;
    private int mRes;

    public TextureLoader(Context context, int res) {
        mContext = context;
        mRes = res;
    }

    @Override
    public int load() {
        int textureHandlers[] = new int[1];
        GLES20.glGenTextures(1, textureHandlers, 0);
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0);
        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureHandlers[0]);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_NEAREST);

        loadTextureResource();
        return textureHandlers[0];
    }

    private void loadTextureResource() {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        Bitmap bitmap = BitmapFactory.decodeResource(mContext.getResources(), mRes, options);
        GLUtils.texImage2D(GLES20.GL_TEXTURE_2D, 0, bitmap, 0);
    }
}

