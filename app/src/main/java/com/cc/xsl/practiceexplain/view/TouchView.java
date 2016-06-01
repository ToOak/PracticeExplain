package com.cc.xsl.practiceexplain.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by xushuailong on 2016/6/1.
 */
public class TouchView extends View{
    private String TAG = "oak_TouchView";
    private float left = 20;
    private float right = 60;
    private float top = 20;
    private float bottom = 60;
    public TouchView(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Paint p = new Paint();
        p.setColor(Color.BLUE);
        canvas.drawRect(left,top,right,bottom,p);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        left = event.getX() - 20;
        right = event.getX() + 20;
        top = event.getY() - 20;
        bottom = event.getY() + 20;
        invalidate();
//        if (event.getAction() == MotionEvent.ACTION_MOVE ){
//            Log.d(TAG, "true : " + event);
//            return true;
//        }else {
//            Log.d(TAG,"super : "+event);
//            return super.onTouchEvent(event);
//        }
        return true;
    }
}
