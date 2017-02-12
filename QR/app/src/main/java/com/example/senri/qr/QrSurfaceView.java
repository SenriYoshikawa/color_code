package com.example.senri.qr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;

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
    private ImageView pictureView;
    private int pictureWidth, pictureHeight;
    private float xMaskOffset, yMaskOffset;

    private int[] picPixcls;

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

        qrBitmap = Bitmap.createBitmap(64, 64, Bitmap.Config.ARGB_4444);//ダミー
        pictureView = (ImageView) findViewById(R.id.pictureView);
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

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if(qrBitmap == null) return true;
        mTranslationGestureDetector.onTouch(v, event);
        mScaleGestureDetector.onTouchEvent(event);
        qrDraw();
        //if(event.getAction() == MotionEvent.ACTION_UP) setColor();
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
        canvas.drawBitmap(qrBitmap, qrDrawMatrix, null);
        mHolder.unlockCanvasAndPost(canvas);
    }

    public void setQrBitmap(Bitmap _qrBitmap)
    {
        qrBitmap = _qrBitmap;
    }

    public void setColor()
    {
        Canvas canvas = mHolder.lockCanvas();
        float maskSize;
        float canvas2pic;
        if(pictureWidth / canvas.getWidth() * canvas.getHeight() > pictureHeight)
        {
            //横幅で合わせた場合
            maskSize = pictureWidth * qrBitmap.getWidth() * qrScale / canvas.getWidth();
            yMaskOffset = (canvas.getHeight() - ((float)canvas.getWidth() / pictureWidth * pictureHeight)) / 2;
            xMaskOffset = 0;
            canvas2pic = (float)pictureWidth / canvas.getWidth();
        }
        else
        {
            //縦幅で合わせた場合
            maskSize = pictureHeight * qrBitmap.getHeight() * qrScale / canvas.getHeight();
            xMaskOffset = (canvas.getWidth() - ((float)canvas.getHeight() / pictureHeight  * pictureWidth)) / 2;
            yMaskOffset = 0;
            canvas2pic = (float)pictureHeight / canvas.getHeight();
        }

        /*
        Log.d("picX1", " " + ((qrPositionX - qrBitmap.getWidth() / 2 - xMaskOffset) * canvas2pic));
        Log.d("picY1", " " + ((qrPositionY - qrBitmap.getHeight() / 2 - yMaskOffset) * canvas2pic));
        Log.d("picX2", " " + ((qrPositionX - qrBitmap.getWidth() / 2 - xMaskOffset) * canvas2pic + maskSize));
        Log.d("picY2", " " + ((qrPositionY - qrBitmap.getHeight() / 2 - yMaskOffset) * canvas2pic + maskSize));
        */

        mHolder.unlockCanvasAndPost(canvas);
        qrDraw();
    }

    public void setPicPixels(Bitmap pictureBitmap)
    {
        //Log.d("qrSizeX"," " + pictureBitmap.getWidth());
        //Log.d("qrSizeY"," " + pictureBitmap.getHeight());
        int picWidth = pictureBitmap.getWidth();
        int picHeight = pictureBitmap.getHeight();
        pictureWidth = pictureBitmap.getWidth();
        pictureHeight = pictureBitmap.getHeight();



              /*
        int picBaseMaskWidth = 0;
        int picBaseMaskHeight = 0;
        picPixcels = new int[picBaseMaskHeight * picBaseMaskWidth];
        pictureBitmap.getPixels(picPixcels, //受け取る配列
                0, //picPixcelsの先頭位置
                picWidth, //picPixcelsの列数
                0,//pictureを切り取る矩形の左上のx要素
                0,//pictureを切り取る矩形の左上のy要素
                picBaseMaskWidth,//pictureを切り取る矩形の右下のx要素
                picBaseMaskHeight);//pictureを切り取る矩形の右下のy要素
        */
    }

}
