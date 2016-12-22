package com.example.senri.qr;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.ImageView;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MainActivity extends AppCompatActivity {
    ImageView imageView;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = (ImageView) findViewById(R.id.imageView);
        textView = (TextView) findViewById(R.id.textView);

    }

    //QRCode作成
    public void onClickQRCodeCreate(View view) {
        EditText editText = (EditText) findViewById(R.id.edit_text);

        try {
            String encoding = "UTF-8";
            qrEncoder qr = new qrEncoder(editText.getText().toString(), ErrorCorrectionLevel.L, java.nio.charset.StandardCharsets.UTF_8, 300);
            imageView.setImageBitmap(qr.getBitmap());
            textView.setText(qr.getString());

        }catch (Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
