package com.shymanovich.simplebrush;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

public class CanvasView extends View {
    public static final int PEN = 1;
    public static final int LINE = 2;
    public static final int RECTANGLE = 3;
    public static final int SQUARE = 5;
    public static final int CIRCLE = 6;
    public static final float TOUCH_TOLERANCE = 4;
    private boolean isDrawing = false;

    private int widthView;
    private int heightView;
    private int bottomBarHeight;
    private int currentShape;
    private float strokeWidth = 25;

    private Paint paint;
    private Path path;
    private Canvas mCanvas;
    private Bitmap canvasBitmap;

    private float mStartX;
    private float mStartY;

    private float mx;
    private float my;

    public CanvasView(Context context, @Nullable AttributeSet attr) {
        super(context, attr);
        setUpDrawingTools();
        setDrawPaintColor(Color.BLACK);
    }

    public void setUpDrawingTools() {
        this.currentShape = 1;
        this.path = new Path();
        this.paint = new Paint(Paint.DITHER_FLAG);
        this.paint.setColor(0xFFFFFFFF);
        this.paint.setAntiAlias(true);
        this.paint.setStrokeWidth(strokeWidth);
        this.paint.setStyle(Paint.Style.STROKE);
        this.paint.setStrokeJoin(Paint.Join.ROUND);
        this.paint.setStrokeCap(Paint.Cap.ROUND);
    }

    public void setDrawPaintColor(int paintColor) {
        paint.setColor(paintColor);
    }

    public void setDrawShape(int shape) {
        this.currentShape = shape;
    }

    public void setBitmap(Bitmap bitmapPic) {
        canvasBitmap = bitmapPic.copy(Bitmap.Config.ARGB_8888, true);
        mCanvas = new Canvas(canvasBitmap);
        invalidate();
    }

    public Bitmap getCanvasBitmap() {
        return this.canvasBitmap;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        widthView = w;
        heightView = h - bottomBarHeight;
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(canvasBitmap);
        mCanvas.drawColor(0xFFFFFFFF);
    }

    @Override
    protected void onDraw(android.graphics.Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, paint);

        switch (currentShape) {
            case PEN:
                onDrawPen();
                break;
            case LINE:
                onDrawLine(canvas);
                break;
            case CIRCLE:
                onDrawCircle(canvas);
                break;
            case RECTANGLE:
                onDrawRectangle(canvas);
                break;
            case SQUARE:
                onDrawSquare(canvas);
                break;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        invalidate();
        mx = event.getX();
        my = event.getY();

        switch (currentShape)
        {
            case PEN:
                onTouchEventPen(event);
                break;
            case LINE:
                onTouchEventLine(event);
                break;
            case CIRCLE:
                onTouchEventCircle(event);
                break;
            case RECTANGLE:
                onTouchEventRectangle(event);
                break;
            case SQUARE:
                onTouchEventSquare(event);
                break;
        }
        return true;
    }

    private void onDrawPen() {
        mCanvas.drawPath(path, paint);
    }

    private void onDrawCircle(Canvas canvas) {
        canvas.drawCircle(mStartX, mStartY, calculateRadius(mStartX, mStartY, mx, my), paint);
    }

    private void onDrawRectangle(Canvas canvas) {
        drawRectangle(canvas, paint);
    }

    private void onDrawSquare(Canvas canvas) {
        onDrawRectangle(canvas);
    }

    private void onDrawLine(Canvas canvas) {
        float dx = Math.abs(mx - mStartX);
        float dy = Math.abs(my - mStartY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            canvas.drawLine(mStartX, mStartY, mx, my, paint);
        }
    }


    private void drawRectangle(Canvas canvas, Paint paint) {
        float right = Math.max(mStartX, mx);
        float left = Math.min(mStartX, mx);
        float bottom = Math.max(mStartY, my);
        float top = Math.min(mStartY, my);
        canvas.drawRect(left, top , right, bottom, paint);
    }

    protected float calculateRadius(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private void onTouchEventPen(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                path.moveTo(mx, my);
                break;
            case MotionEvent.ACTION_MOVE:
                path.lineTo(mx, my);
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                path.lineTo(mx, my);
                mCanvas.drawPath(path, paint);
                path.reset();
                break;
        }
        invalidate();
    }

    private void onTouchEventLine(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawLine(mStartX, mStartY, mx, my, paint);
                break;
        }
        invalidate();
    }

    private void onTouchEventCircle(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                mCanvas.drawCircle(mStartX, mStartY, calculateRadius(mStartX,mStartY,mx,my), paint);
                break;
        }
        invalidate();
    }

    private void onTouchEventRectangle(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                drawRectangle(mCanvas, paint);
                break;
        }
        invalidate();
    }

    private void onTouchEventSquare(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDrawing = true;
                mStartX = mx;
                mStartY = my;
                break;
            case MotionEvent.ACTION_MOVE:
                adjustSquare(mx, my);
                break;
            case MotionEvent.ACTION_UP:
                isDrawing = false;
                adjustSquare(mx, my);
                drawRectangle(mCanvas, paint);
                break;
        }
        invalidate();
    }

    private void adjustSquare(float x, float y) {
        float deltaX = Math.abs(mStartX - x);
        float deltaY = Math.abs(mStartY - y);

        float max = Math.max(deltaX, deltaY);

        mx = mStartX - x < 0 ? mStartX + max : mStartX - max;
        my = mStartY - y < 0 ? mStartY + max : mStartY - max;
    }

    public void setStrokeWidth(float v) {
        this.strokeWidth = v;
        this.paint.setStrokeWidth(v);
    }

    public int getWidthView() {
        return widthView;
    }

    public int getHeightView() {
        return heightView;
    }
}
