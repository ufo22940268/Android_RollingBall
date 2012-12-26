package hongbosb.rollingball.model;

import android.content.Context;
import hongbosb.rollingball.*;
import android.view.*;

public abstract class GLEntity implements GLInputable, GLTouchable {
    public abstract void draw();

    @Override
    public boolean onKeyDown(int keyCode) {
        return false;
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        return false;
    }
}

