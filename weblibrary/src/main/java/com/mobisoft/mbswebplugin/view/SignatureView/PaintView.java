package com.mobisoft.mbswebplugin.view.SignatureView;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by smile on 2017/6/20.
 */

public class PaintView extends View {
    private Paint paint;
    private Path path;
    private Bitmap cachebBitmap;
    private Canvas cacheCanvas;
    private boolean isPaint =false;
    public Bitmap getCachebBitmap() {
        return cachebBitmap;

    }

    public PaintView(Context context) {
        super(context);
        init();
    }

    private void init() {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(5);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLACK);
        path = new Path();
        cachebBitmap = Bitmap.createBitmap(300, (int) (200 * 0.8),
                Bitmap.Config.ARGB_8888);
        cacheCanvas = new Canvas();
        cacheCanvas.setDrawFilter(new PaintFlagsDrawFilter(0,
                Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG));
        cacheCanvas.drawColor(Color.WHITE);
    }

    public void clear() {
        isPaint  = false;
        if (cacheCanvas != null) {
            paint.setColor(Color.WHITE);
            cacheCanvas.drawPaint(paint);
            paint.setColor(Color.BLACK);
            cacheCanvas.drawColor(Color.WHITE);
            invalidate();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        canvas.drawBitmap(cachebBitmap, 0, 0, null);
        canvas.drawPath(path, paint);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        int curW = cachebBitmap != null ? cachebBitmap.getWidth() : 0;
        int curH = cachebBitmap != null ? cachebBitmap.getHeight() : 0;
        if (curW >= w && curH >= h) {
            return;
        }
        if (curW < w)
            curW = w;
        if (curH < h)
            curH = h;
        Bitmap newBitmap = Bitmap.createBitmap(curW, curH,
                Bitmap.Config.ARGB_8888);
        Canvas newCanvas = new Canvas();
        newCanvas.setBitmap(newBitmap);
        if (cachebBitmap != null) {
            newCanvas.drawBitmap(cachebBitmap, 0, 0, null);
        }
        cachebBitmap = newBitmap;
        cacheCanvas = newCanvas;
    }

    private float cur_x, cur_y;

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: {
                isPaint = true;
                cur_x = x;
                cur_y = y;
                path.moveTo(cur_x, cur_y);
                break;
            }
            case MotionEvent.ACTION_MOVE: {
                path.quadTo(cur_x, cur_y, x, y);
                cur_x = x;
                cur_y = y;
                break;
            }
            case MotionEvent.ACTION_UP: {
                cacheCanvas.drawPath(path, paint);
                path.reset();
                break;
            }
        }
        invalidate();
        return true;
    }
    public  boolean isPaint(){
        return  isPaint;
    }
}
