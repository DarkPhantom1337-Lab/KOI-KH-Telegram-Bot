import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

public class QRcode {

    private OutputStream generatedFileStream;
    private File logoFile = new File("Logo.png");

    public static String generateQRCode(String data) throws FileNotFoundException {
        QRcode generator = new QRcode();
        String fname = "qrcode_" + DataBase.getUFileds(6,"val") + ".png";
       //DataBase.setUFields(6, "val", (DataBase.getUFileds(6,"val") + 1));
        generator.setGeneratedFileStream( new FileOutputStream(new File(fname)));
        generator.createQrCode( data, 300, "png");
        try {
            generator.getGeneratedFileStream().close();
        } catch (IOException ignored) {
        }
        return fname;
    }


    private void createQrCode(String content, int qrCodeSize, String imageFormat){
        try {
            // Correction level - HIGH - more chances to recover message
            Hashtable<EncodeHintType, ErrorCorrectionLevel> hintMap =
                    new Hashtable<EncodeHintType, ErrorCorrectionLevel>();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            // Generate QR-code
            QRCodeWriter qrCodeWriter = new QRCodeWriter();
            BitMatrix bitMatrix = qrCodeWriter.encode(content,
                    BarcodeFormat.QR_CODE, qrCodeSize, qrCodeSize, hintMap);

            // Start work with picture
            int matrixWidth = bitMatrix.getWidth();
            BufferedImage image = new BufferedImage(matrixWidth, matrixWidth,
                    BufferedImage.TYPE_INT_RGB);
            image.createGraphics();
            Graphics2D graphics = (Graphics2D) image.getGraphics();
            Font font = new Font(Font.SANS_SERIF, Font.PLAIN, 18);
            graphics.setFont(font);
            graphics.setColor(Color.white);
            graphics.fillRect(0, 0, matrixWidth, matrixWidth);
            Color mainColor = new Color(0, 0, 0);
            graphics.setColor(mainColor);
            // Write message under the QR-code
           // graphics.drawString( content, 30, image.getHeight() - graphics.getFont().getSize());

            //Write Bit Matrix as image
            for (int i = 0; i < matrixWidth; i++) {
                for (int j = 0; j < matrixWidth; j++) {
                    if (bitMatrix.get(i, j)) {
                        graphics.fillRect(i, j, 1, 1);
                    }
                }
            }

            // Add logo to QR code
            BufferedImage logo = ImageIO.read( this.getLogoFile());

            //scale logo image and insert it to center of QR-code
            double scale = calcScaleRate(image, logo);
            logo = getScaledImage( logo,
                    (int)( logo.getWidth() * scale),
                    (int)( logo.getHeight() * scale) );
            graphics.drawImage( logo,
                    image.getWidth()/2 - logo.getWidth()/2,
                    image.getHeight()/2 - logo.getHeight()/2,
                    image.getWidth()/2 + logo.getWidth()/2,
                    image.getHeight()/2 + logo.getHeight()/2,
                    0, 0, logo.getWidth(), logo.getHeight(), null);
            // Check correctness of QR-code
            if ( isQRCodeCorrect(content, image)) {
                ImageIO.write(image, imageFormat, this.getGeneratedFileStream());
                System.out.println("Your QR-code was succesfully generated.");
            } else {
                System.out.println("Sorry, your logo has broke QR-code. ");
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            ex.printStackTrace();
        }
    }

    private double calcScaleRate(BufferedImage image, BufferedImage logo){
        double scaleRate = logo.getWidth() / image.getWidth();
        if (scaleRate > 0.3){
            scaleRate = 0.3;
        } else {
            scaleRate = 1;
        }
        return scaleRate;
    }

    private boolean isQRCodeCorrect(String content, BufferedImage image){
        boolean result = false;
        Result qrResult = decode(image);
        if (qrResult != null && content != null && content.equals(qrResult.getText())){
            result = true;
        }
        return result;
    }

    private Result decode(BufferedImage image){
        if (image == null) {
            return null;
        }
        try {
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
            Result result = new MultiFormatReader().decode(bitmap, Collections.EMPTY_MAP);
            return result;
        } catch (NotFoundException nfe) {
            nfe.printStackTrace();
            return null;
        }
    }

    private BufferedImage getScaledImage(BufferedImage image, int width, int height) throws IOException {
        int imageWidth  = image.getWidth();
        int imageHeight = image.getHeight();

        double scaleX = (double)width/imageWidth;
        double scaleY = (double)height/imageHeight;
        AffineTransform scaleTransform = AffineTransform.getScaleInstance(scaleX, scaleY);
        AffineTransformOp bilinearScaleOp = new AffineTransformOp(
                scaleTransform, AffineTransformOp.TYPE_BILINEAR);

        return bilinearScaleOp.filter(
                image,
                new BufferedImage(width, height, image.getType()));
    }

    public OutputStream getGeneratedFileStream() {
        return generatedFileStream;
    }

    public void setGeneratedFileStream(OutputStream generatedFileStream) {
        this.generatedFileStream = generatedFileStream;
    }
    public static String readQRCode2(String filePath, String charset, Map hintMap)
            throws FileNotFoundException, IOException, NotFoundException {
        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap);
        return qrCodeResult.getText();
    }

    public File getLogoFile() {
        return logoFile;
    }

    public void setLogoFile(File logoFile) {
        this.logoFile = logoFile;
    }

    public static String readQRCode(String filePath, String charset, Map s)
            throws FileNotFoundException, IOException, NotFoundException {

        BinaryBitmap binaryBitmap = new BinaryBitmap(new HybridBinarizer(
                new BufferedImageLuminanceSource(
                        ImageIO.read(new FileInputStream(filePath)))));
        Result qrCodeResult = new MultiFormatReader().decode(binaryBitmap, s);
        return qrCodeResult.getText();
    }

}


