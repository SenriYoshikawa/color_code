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

public class MainActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    //QRCode作成
    public void onClickQRCodeCreate(View view)
    {
        //テキスト入力を読み込み
        EditText editText = (EditText) findViewById(R.id.edit_text);

        // QRCodeの作成
        Bitmap qrCodeBitmap = this.createQRCode(editText.getText().toString());
        String qrCodeString = this.createQRString(editText.getText().toString());

        // QRCodeの作成に成功した場合
        if (qrCodeBitmap != null)
        {
            // 結果をImageViewに表示
            ImageView imageView = (ImageView) findViewById(R.id.imageView);
            imageView.setImageBitmap(qrCodeBitmap);
        }

        //テキストを書き出し
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(qrCodeString);
    }

    private Bitmap createQRCode(String contents)
    {
        Bitmap qrBitmap = null;
        try {
            // QRコードの生成
            QRCodeWriter qrcodewriter = new QRCodeWriter();
            BitMatrix qrBitMatrix = qrcodewriter.encode(contents, BarcodeFormat.QR_CODE, 300, 300);

            qrBitmap = Bitmap.createBitmap(300, 300, Bitmap.Config.ARGB_8888);
            qrBitmap.setPixels(this.createDot(qrBitMatrix), 0, 300, 0, 0, 300, 300);
        }
        catch(Exception ex)
        {
            // エンコード失敗
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            return qrBitmap;
        }
    }

    private String createQRString(String contents)
    {
        String str = "";
        try
        {
            int size = Encoder.encode(contents, ErrorCorrectionLevel.L).getVersion().getVersionNumber();
            QRCodeWriter qrcodewriter = new QRCodeWriter();
            BitMatrix qrBitMatrix = qrcodewriter.encode(contents, BarcodeFormat.QR_CODE, size, size);
            str = qrBitMatrix.toString();
        }
        catch(Exception ex)
        {
            Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
        }
        finally
        {
            return str;
        }
    }

    // ドット単位の判定
    private int[] createDot(BitMatrix qrBitMatrix)
    {
        // 縦幅・横幅の取得
        int width = qrBitMatrix.getWidth();
        int height = qrBitMatrix.getHeight();
        // 枠の生成
        int[] pixels = new int[width * height];

        // データが存在するところを黒にする
        for (int y = 0; y < height; y++)
        {
            // ループ回数盤目のOffsetの取得
            int offset = y * width;
            for (int x = 0; x < width; x++)
            {
                // データが存在する場合
                if (qrBitMatrix.get(x, y))
                {
                    pixels[offset + x] = Color.BLACK;
                }
                else
                {
                    pixels[offset + x] = Color.WHITE;
                }
            }
        }
        // 結果を返す
        return pixels;
    }
}