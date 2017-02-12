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
    private Bitmap pictureBitmap;
    private Matrix qrDrawMatrix;
    private float qrPositionX, qrPositionY;
    private float qrScale;
    private ScaleGestureDetector mScaleGestureDetector;
    private TranslationGestureDetector mTranslationGestureDetector;
    private float mPrevX, mPrevY;
    private ImageView pictureView;
    private int pictureWidth, pictureHeight;
    private float xMaskOffset, yMaskOffset;
    public int qrBackColor;

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
        int maskSize;
        float canvas2pic;
        if(pictureWidth / canvas.getWidth() * canvas.getHeight() > pictureHeight)
        {
            //横幅で合わせた場合
            maskSize = (int)(pictureWidth * qrBitmap.getWidth() * qrScale / canvas.getWidth());
            yMaskOffset = (canvas.getHeight() - ((float)canvas.getWidth() / pictureWidth * pictureHeight)) / 2;
            xMaskOffset = 0;
            canvas2pic = (float)pictureWidth / canvas.getWidth();
        }
        else
        {
            //縦幅で合わせた場合
            maskSize = (int)(pictureHeight * qrBitmap.getHeight() * qrScale / canvas.getHeight());
            xMaskOffset = (canvas.getWidth() - ((float)canvas.getHeight() / pictureHeight  * pictureWidth)) / 2;
            yMaskOffset = 0;
            canvas2pic = (float)pictureHeight / canvas.getHeight();
        }

        int luX = (int)((qrPositionX - qrBitmap.getWidth() * qrScale / 2 - xMaskOffset) * canvas2pic);
        int luY = (int)((qrPositionY - qrBitmap.getHeight() * qrScale / 2 - yMaskOffset) * canvas2pic);
        //int rlX = luX + maskSize;
        //int rlY = luY + maskSize;

        Log.d("luX", " " + luX);
        Log.d("luY", " " + luY);
        //Log.d("rlX", " " + rlX);
        //Log.d("rlY", " " + rlY);

        picPixcls = new int[maskSize * maskSize];
        Log.d("create array", "success");
        pictureBitmap.getPixels(picPixcls, //受け取る配列
                0, //picPixelsの先頭位置
                maskSize, //picPixelsの列数
                luX,//pictureを切り取る矩形の左上のx要素
                luY,//pictureを切り取る矩形の左上のy要素
                maskSize,//pictureを切り取る矩形の右下のx要素
                maskSize);//pictureを切り取る矩形の右下のy要素

        Log.d("getPixels", "success");

        int tempRgbArray[][] = new int[maskSize][3];
        for(int i = 0; i < maskSize; i++)
        {
            int forAverage = 0;
            for(int j = 0; j < maskSize; j++)
            {
                forAverage += picPixcls[i];
            }
            tempRgbArray[i][0] = (forAverage & 0x00FF0000) >> 16;
            tempRgbArray[i][1] = (forAverage & 0x0000FF00) >> 8;
            tempRgbArray[i][2] = forAverage & 0x000000FF;
        }
        int forRgbAverage[] = {0,0,0};
        for(int i = 0; i < maskSize; i++) for(int j = 0; j < 3; j++)
        {
            forRgbAverage[j] += tempRgbArray[i][j] / maskSize;
        }
        for(int i = 0; i < 3; i++) forRgbAverage[i] /= maskSize;
        qrBackColor = 0xFF000000 | forRgbAverage[0] << 16 | forRgbAverage[1] << 8 | forRgbAverage[2];

        mHolder.unlockCanvasAndPost(canvas);
    }

    public void setPictureBitmap(Bitmap pictureBitmap_)
    {
        pictureWidth = pictureBitmap_.getWidth();
        pictureHeight = pictureBitmap_.getHeight();
        pictureBitmap = pictureBitmap_;
    }

}
