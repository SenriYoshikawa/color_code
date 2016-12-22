package com.example.senri.qr;

import android.graphics.Bitmap;
import android.graphics.Color;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.google.zxing.qrcode.encoder.Encoder;

import java.nio.charset.Charset;
import java.util.EnumMap;
import java.util.Map;
import java.util.Objects;

import static android.app.PendingIntent.getActivity;

/**
 * Created by senri on 2016/12/22.
 */

public class qrEncoder
{
    private String _string = null;
    private Bitmap _bitmap = null;
    private String _returnMsg = null;
    final private int _cellNum;

    final private String _contents;
    final private ErrorCorrectionLevel _level;
    final private Charset _charset;
    final private int _size;


    qrEncoder(String contents, ErrorCorrectionLevel level, Charset charset, int size) throws WriterException {
        // 初期値検証
        Objects.requireNonNull(contents);
        Objects.requireNonNull(level);
        Objects.requireNonNull(charset);
        if (!charset.canEncode()) {
            throw new IllegalArgumentException("cannot encode: " + charset.displayName());
        }

        // 初期化
        _contents = contents;
        _level = level;
        _charset = charset;
        _size = size;

        // セル数を計算して保存
        _cellNum = Encoder.encode(contents, ErrorCorrectionLevel.L).getVersion().getVersionNumber() * 4 + 17;
    }

    public Bitmap getBitmap() throws WriterException {
        if(_bitmap != null) return _bitmap;

        Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
        hints.put(EncodeHintType.ERROR_CORRECTION, _level);
        hints.put(EncodeHintType.CHARACTER_SET, _charset.displayName());
        hints.put(EncodeHintType.MARGIN, 4);

        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix matrix = writer.encode(_contents, BarcodeFormat.QR_CODE, _size, _size, hints);
        final int size = matrix.getHeight();
        _bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
        _bitmap.setPixels(this.createDot(matrix), 0, size, 0, 0, size, size);

        return _bitmap;
    }

    public String getString() throws WriterException {
        if(_string != null) return _string;

        QRCodeWriter qrcodewriter = new QRCodeWriter();
        BitMatrix qrBitMatrix = qrcodewriter.encode(_contents, BarcodeFormat.QR_CODE, _cellNum, _cellNum);
        _string = qrBitMatrix.toString();
        return _string;
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
