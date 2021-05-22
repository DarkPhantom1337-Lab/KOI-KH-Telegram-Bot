package ua.darkphantom1337.koi.kh.qr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

public class DarkQRWriter {

    public static File createQRCode(Long qrdata) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, 500);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(qrdata.toString(), BarcodeFormat.QR_CODE, codeWidth, 500, null);
            MatrixToImageWriter.writeToFile(bitMatrix, "png", new File("qr_" + qrdata + ".png"));
            BufferedImage image = ImageIO.read(new File("qr_" + qrdata + ".png"));
            Graphics2D g = image.createGraphics();
            Font font = new Font(Font.SANS_SERIF, Font.BOLD, 35);
            g.setFont(font);
            g.setColor(Color.BLACK);
            g.drawString(""+qrdata, image.getWidth()/2-10, image.getHeight()-25);
            g.dispose();
            ImageIO.write(image, "png", new File("qr_" + qrdata + ".png"));
            return new File("qr_" + qrdata + ".png");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new File("qr_error.png");
    }
}
