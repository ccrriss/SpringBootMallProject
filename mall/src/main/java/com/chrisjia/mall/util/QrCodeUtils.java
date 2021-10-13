package com.chrisjia.mall.util;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;

public class QrCodeUtils {
    public static void generateQrCode(String content, int width, int height, String path) throws WriterException, IOException {
        QRCodeWriter writer = new QRCodeWriter();
        BitMatrix bitMatrix = writer.encode(content, BarcodeFormat.QR_CODE, width, height);
        Path filePath = FileSystems.getDefault().getPath(path);
        MatrixToImageWriter.writeToPath(bitMatrix, "PNG", filePath);
    }
}
