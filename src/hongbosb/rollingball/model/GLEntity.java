package hongbosb.rollingball.model;

import android.content.Context;
import hongbosb.rollingball.*;

public abstract class GLEntity implements GLEventListener {
    public abstract void draw();
    public boolean onKeyDown(int keyCode) {
        return false;
    }
}

