package com.example.senri.qr;

        import android.graphics.Bitmap;
        import android.graphics.Color;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.zxing.BarcodeFormat;
        import com.google.zxing.common.BitMatrix;
        import com.google.zxing.qrcode.QRCodeWriter;
        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
        import com.google.zxing.qrcode.encoder.Encoder;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //QRCode作成
    public void onClickQRCodeCreate(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_text);

        qrEncoder qr = new qrEncoder(editText.getText().toString());

        ImageView imageView = (ImageView) findViewById(R.id.imageView);
        imageView.setImageBitmap(qr.bitmap);
        TextView textView = (TextView) findViewById(R.id.textView);
        textView.setText(qr.string);

    }
}