package id.ac.ui.cs.mobileprogramming.razaqadhafin.rclass.opengl;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLSplashSurfaceView extends GLSurfaceView {

    GLRender myRender;

    private static final float TOUCH_SCALE_FACTOR = 0.00015f;
    private float mPreviousX;
    private float mPreviousY;

    public GLSplashSurfaceView(Context context) {
        super(context);
        setEGLContextClientVersion(3);

        super.setEGLConfigChooser(8, 8, 8, 8, 16, 0);

        myRender = new GLRender(context);
        setRenderer(myRender);
        setRenderMode(GLSurfaceView.RENDERMODE_CONTINUOUSLY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        float x = e.getX();
        float y = e.getY();

        switch (e.getAction()) {
            case MotionEvent.ACTION_MOVE:

                float dx = x - mPreviousX;
                myRender.setX(myRender.getX() - (dx * TOUCH_SCALE_FACTOR));

                float dy = y - mPreviousY;
                myRender.setY(myRender.getY() - (dy * TOUCH_SCALE_FACTOR));
        }

        mPreviousX = x;
        mPreviousY = y;
        return true;
    }
}