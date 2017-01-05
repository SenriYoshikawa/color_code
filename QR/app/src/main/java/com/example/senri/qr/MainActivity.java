package com.example.senri.qr;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.EditText;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class MainActivity extends AppCompatActivity {
    private ImageView qrView;
    private TextView textView;
    private EditText editText;
    private Button nextButton;
    private QrEncoder qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Create QR Code");

        qrView = (ImageView) findViewById(R.id.qrView);
        textView = (TextView) findViewById(R.id.textView);
        editText = (EditText) findViewById(R.id.edit_text);
        nextButton = (Button) findViewById(R.id.nextButton);
        nextButton.setEnabled(false);
    }

    //QRCode作成
    public void onClickQRCodeCreate(View view) {
        try {
            qr = new QrEncoder(editText.getText().toString(), ErrorCorrectionLevel.L, java.nio.charset.StandardCharsets.UTF_8, 300);
            qrView.setImageBitmap(qr.getBitmap());
            textView.setText(qr.getString());
            nextButton.setEnabled(true);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickNext(View view) {
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.sample);
    }
}
