package com.example.senri.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

/**
 * Created by senri on 2017/01/19.
 */

public class QrSurfaceView extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener{

    private SurfaceHolder mHolder;
    private Bitmap qrBitmap;
    private Matrix qrDrawMatrix;
    private float qrPositionX, qrPositionY;
    private float qrScale;
    private ScaleGestureDetector mScaleGestureDetector;
    private TranslationGestureDetector mTranslationGestureDetector;
    private float mPrevX, mPrevY;

    private TranslationGestureListener mTranslationListener = new TranslationGestureListener() {
        @Override
        public void onTranslationEnd(TranslationGestureDetector detector) {
        }

        @Override
        public void onTranslationBegin(TranslationGestureDetector detector) {
            mPrevX = detector.getX();
            mPrevY = detector.getY();
        }

        @Override
        public void onTranslation(TranslationGestureDetector detector) {
            float deltaX = detector.getX() - mPrevX;
            float deltaY = detector.getY() - mPrevY;
            qrPositionX += deltaX;
            qrPositionY += deltaY;
            mPrevX = detector.getX();
            mPrevY = detector.getY();
        }
    };


    public QrSurfaceView(Context context) {
        super(context);
    }


    public QrSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        qrBitmap =  Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_4444);
        qrDrawMatrix = new Matrix();

        qrScale = 1.0f;
        mScaleGestureDetector = new ScaleGestureDetector(context, mOnScaleListener);
        mTranslationGestureDetector = new TranslationGestureDetector(mTranslationListener);


        getHolder().addCallback(this);
        getHolder().setFormat(PixelFormat.TRANSLUCENT);
        setZOrderOnTop(true);

        setOnTouchListener(this);
        
    }

    public QrSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        mHolder = holder;
        qrPositionX = width / 2;
        qrPositionY = height / 2;
        qrDraw();
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
    }

    private ScaleGestureDetector.SimpleOnScaleGestureListener mOnScaleListener = new ScaleGestureDetector.SimpleOnScaleGestureListener() {
        @Override
        public boolean onScaleBegin(ScaleGestureDetector detector) {
            return super.onScaleBegin(detector);
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector detector) {
            super.onScaleEnd(detector);
        }

        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            qrScale *= detector.getScaleFactor();
            return true;
        };
    };

    public boolean onTouch(View v, MotionEvent event) {
        if(qrBitmap == null) return false;
        mTranslationGestureDetector.onTouch(v, event);
        mScaleGestureDetector.onTouchEvent(event);
        qrDraw();
        return true;
    }

    public void qrDraw()
    {
        Canvas canvas = mHolder.lockCanvas();
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        qrDrawMatrix.reset();
        qrDrawMatrix.postScale(qrScale, qrScale);
        qrDrawMatrix.postTranslate(-qrBitmap.getWidth() / 2 * qrScale, -qrBitmap.getHeight() / 2 * qrScale);
        qrDrawMatrix.postTranslate(qrPositionX, qrPositionY);
        //canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(qrBitmap, qrDrawMatrix, null);
        mHolder.unlockCanvasAndPost(canvas);
    }

    public void setQrBitmap(Bitmap _qrBitmap)
    {
        qrBitmap = _qrBitmap;
    }
}
