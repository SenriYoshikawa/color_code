package com.example.senri.qr;

        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.view.View;
        import android.widget.Button;
        import android.widget.ImageView;
        import android.widget.EditText;
        import android.widget.SeekBar;
        import android.widget.TextView;
        import android.widget.Toast;
        import com.google.zxing.WriterException;
        import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


public class MainActivity extends AppCompatActivity {
    private ImageView qrView;
    private TextView textView;
    private ImageView overQrView;
    private SeekBar sizeBar;
    private EditText editText;
    private Button setPicButton;
    private QrEncoder qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("Create QR Code");

        qrView = (ImageView) findViewById(R.id.qrView);
        textView = (TextView) findViewById(R.id.textView);
        overQrView = (ImageView) findViewById(R.id.overQrView);
        sizeBar = (SeekBar)findViewById(R.id.sizeBar);
        editText = (EditText) findViewById(R.id.edit_text);
        setPicButton = (Button) findViewById(R.id.setPicButton);
        setPicButton.setEnabled(false);

        sizeBar.setOnSeekBarChangeListener(
                new SeekBar.OnSeekBarChangeListener() {
                    //ツマミがドラッグされると呼ばれる
                    @Override
                    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                        //textView.setText(String.valueOf(progress)+" %");
                        overQrView.setScaleX(Float.valueOf(progress) / 50);
                        overQrView.setScaleY(Float.valueOf(progress) / 50);
                    }
                    //ツマミがタッチされた時に呼ばれる
                    public void onStartTrackingTouch(SeekBar seekBar) {
                    }

                    //ツマミがリリースされた時に呼ばれる
                    public void onStopTrackingTouch(SeekBar seekBar) {
                    }

                });
    }

    //QRCode作成
    public void onClickQRCodeCreate(View view) {
        try {
            qr = new QrEncoder(editText.getText().toString(), ErrorCorrectionLevel.L, java.nio.charset.StandardCharsets.UTF_8, 300);
            qrView.setImageBitmap(qr.getBitmap());
            textView.setText(qr.getString());
            setPicButton.setEnabled(true);
        } catch (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    public void onClickSetPic(View view)
    {
        ImageView imageView = (ImageView)findViewById(R.id.imageView);
        imageView.setImageResource(R.drawable.sample2);

        try {
            overQrView.setImageBitmap(qr.getBitmap());
        } catch  (Exception ex) {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
    }
}
