package hongbosb.rollingball;

import android.app.Activity;
import android.os.Bundle;
import android.view.*;

public class MainActivity extends Activity
{
    private MyGLSurfaceView mGlView;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        mGlView = new MyGLSurfaceView(this);
        setContentView(mGlView);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_VOLUME_UP || keyCode == KeyEvent.KEYCODE_VOLUME_DOWN) {
            mGlView.onKeyDown(keyCode);
            return true;
        }

        return false;
    }
}
