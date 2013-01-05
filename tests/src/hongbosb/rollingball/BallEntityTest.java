package hongbosb.rollingball;

import android.test.AndroidTestCase;
import hongbosb.rollingball.model.*;
import hongbosb.rollingball.data.*;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class hongbosb.rollingball.MainActivityTest \
 * hongbosb.rollingball.tests/android.test.InstrumentationTestRunner
 */
public class BallEntityTest extends AndroidTestCase {

    private GLBallEntity mBall;

    protected void setUp() {
        mBall = new GLBallEntity();
    }

    public void testFixEdge() throws Exception {
        mBall.mTranslateX = 50.0f;
        mBall.mTranslateY = 0.0f;
        mBall.fixEdgePosition();
        assertEquals(50.0f, mBall.mTranslateX);
        assertEquals(0.0f, mBall.mTranslateY);

        mBall.mTranslateX = 110.0f;
        mBall.mTranslateY = 0.0f;
        mBall.fixEdgePosition();
        assertEquals(GLBallEntity.ABS_MAX_X, mBall.mTranslateX);
        assertEquals(0.0f, mBall.mTranslateY);

        mBall.mTranslateX = 110.0f;
        mBall.mTranslateY = 120.0f;
        mBall.fixEdgePosition();
        assertEquals(GLBallEntity.ABS_MAX_X, mBall.mTranslateX);
        assertEquals(GLBallEntity.ABS_MAX_Y, mBall.mTranslateY);

        mBall.mTranslateX = -110.0f;
        mBall.mTranslateY = -120.0f;
        mBall.fixEdgePosition();
        assertEquals(-GLBallEntity.ABS_MAX_X, mBall.mTranslateX);
        assertEquals(-GLBallEntity.ABS_MAX_Y, mBall.mTranslateY);
    }
}
