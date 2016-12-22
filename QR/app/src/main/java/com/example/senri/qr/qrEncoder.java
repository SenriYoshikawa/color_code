package com.example.senri.qr;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;

import static android.app.PendingIntent.getActivity;

/**
 * Created by senri on 2016/12/22.
 */

public class qrEncoder
{
    private int size;
    public String string = null;
    public Bitmap bitmap = null;
    public String returnMsg = null;


    qrEncoder(String contents)
    {
        this.string = this.createQRString(contents);
        this.bitmap = this.createQRCode(contents);
        if(returnMsg == null) returnMsg = "Create QRcode success.";
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
            returnMsg = "Encoding failure.";
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
            this.size = Encoder.encode(contents, ErrorCorrectionLevel.L).getVersion().getVersionNumber() * 4 + 17;

            QRCodeWriter qrcodewriter = new QRCodeWriter();
            BitMatrix qrBitMatrix = qrcodewriter.encode(contents, BarcodeFormat.QR_CODE, this.size, this.size);
            str = qrBitMatrix.toString();
            //System.out.println(str);
        }
        catch(Exception ex)
        {
            returnMsg = "Encoding failure.";
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
