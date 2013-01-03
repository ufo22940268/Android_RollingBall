package hongbosb.rollingball.model;

import android.content.Context;
import hongbosb.rollingball.*;
import android.view.*;

import java.util.*;

public abstract class GLEntity implements GLTouchable  {

    private List<GLEntity> mChildEntities = new ArrayList<GLEntity>();

    public abstract void draw();

    public void addEntity(GLEntity child) {
        mChildEntities.add(child);
    }

    @Override
    public boolean onTouch(MotionEvent event) {
        for (GLEntity entity : mChildEntities) {
            entity.onTouch(event);
        }
        return false;
    }
}

