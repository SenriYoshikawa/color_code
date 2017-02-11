package com.example.senri.qr;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.view.inputmethod.InputMethodManager;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.Toast;
        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class MainActivity extends AppCompatActivity {
    private EditText editText;
    private Button setPicButton;
    private QrEncoder qr;
    private QrSurfaceView qrSurfaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Create QR Code");

        editText = (EditText) findViewById(R.id.edit_text);
        setPicButton = (Button) findViewById(R.id.setPicButton);
        setPicButton.setEnabled(false);
        qrSurfaceView = (QrSurfaceView) findViewById(R.id.QrSurfaceView);
    }


    //QRCode作成
    public void onClickQRCodeCreate(View view) {
        try {
            qr = new QrEncoder(editText.getText().toString(), ErrorCorrectionLevel.L, java.nio.charset.StandardCharsets.UTF_8, 300);
            qrSurfaceView.setQrBitmap(qr.getBitmap());
            qrSurfaceView.qrDraw();
            setPicButton.setEnabled(true);
            InputMethodManager inputMethodMgr = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            inputMethodMgr.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSetPic(View view)
    {
        /*
        imageView.setImageResource(R.drawable.sample2);
        try {
            overQrView.setImageBitmap(qr.getBitmap());
        } catch  (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
        */
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
