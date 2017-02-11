package com.example.senri.qr;

        import android.content.Intent;
        import android.graphics.Bitmap;
        import android.graphics.BitmapFactory;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.MotionEvent;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.Toast;
        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

        import java.io.InputStream;


public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button createQrButton;
    private Button setPicButton;
    private Button setColorButton;
    private ImageView pictureView;
    private QrEncoder qr;
    private QrSurfaceView qrSurfaceView;
    private static final int REQUEST_GALLERY_GET_IMAGE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Set Picture");

        editText = (EditText) findViewById(R.id.edit_text);
        setPicButton = (Button) findViewById(R.id.setPicButton);
        setColorButton = (Button) findViewById(R.id.setColorButton);
        createQrButton = (Button) findViewById(R.id.createQrButton);
        setColorButton.setEnabled(false);
        createQrButton.setEnabled(false);
        pictureView = (ImageView) findViewById(R.id.pictureView);
        qrSurfaceView = (QrSurfaceView) findViewById(R.id.QrSurfaceView);
    }

    //写真選択
    public void onClickSetPic(View view)
    {
        final Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, REQUEST_GALLERY_GET_IMAGE);

        createQrButton.setEnabled(true);
        setTitle("Create QR code");
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_GALLERY_GET_IMAGE && resultCode == RESULT_OK) {
            {
                try {
                    InputStream in = getContentResolver().openInputStream(data.getData());
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 選択した画像を表示
                    //qrSurfaceView.setBackground();
                    pictureView.setImageBitmap(img);
                } catch (Exception e) {
                }
            }
        }
    }


    //QRCode作成
    public void onClickQRCodeCreate(View view) {
        try {
            qr = new QrEncoder(editText.getText().toString(), ErrorCorrectionLevel.L, java.nio.charset.StandardCharsets.UTF_8, 300);
            qrSurfaceView.setQrBitmap(qr.getBitmap());
            qrSurfaceView.qrDraw();
            InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodMgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
            setTitle("Move and resize QR");
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSetColor(View view)
    {
        /*
        final int[] anchorPos = new int[2];
        overQrView.getLocationInWindow(anchorPos);
        System.out.printf("width = %d height = %d x = %d y = %d\n", overQrView.getWidth(), overQrView.getHeight(), anchorPos[0],anchorPos[1]);
        */
    }
}
