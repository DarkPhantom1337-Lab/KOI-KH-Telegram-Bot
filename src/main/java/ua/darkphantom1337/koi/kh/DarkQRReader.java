package ua.darkphantom1337.koi.kh;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.awt.image.RescaleOp;
import java.io.File;
import java.io.IOException;

/**
 * ZXing barcode encoding / decoding
 */
public class DarkQRReader {

    /**
     * Bar code encoding
     *
     * @param contents
     * @param width
     * @param height
     * @param imgPath
     */
    public static void encode(String contents, int width, int height, String imgPath) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.EAN_8, codeWidth, height, null);
            MatrixToImageWriter.writeToFile(bitMatrix, "png", new File(imgPath));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Barcode decoding
     *
     * @param imgPath
     * @return String
     */
    public static String decode(String imgPath) throws IOException, NotFoundException {
        BufferedImage image = null;
        Result result = null;
        image = ImageIO.read(new File(imgPath));
        if (image == null) {
            System.out.println("the decode image may be not exit.");
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        result = new MultiFormatReader().decode(bitmap, null);
        return result.getText();
    }

    public static String decodeFromImage(BufferedImage image) throws IOException, NotFoundException {
        Result result = null;
        if (image == null) {
            System.out.println("the decode image may be not exit.");
        }
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
        result = new MultiFormatReader().decode(bitmap, null);
        return result.getText();
    }

    /**
     * @param args
     */
    public static void main(String[] args) throws IOException, WriterException, NotFoundException {
        String imgPath = "C:\\Users\\DarkPhantom1337\\Desktop\\ForTest\\test34.jpg";
        String contents = "0001337";
        //MatrixToImageWriter.writeToFile(new MultiFormatWriter().encode("99999", BarcodeFormat.QR_CODE, 600, 600), "png", new File("C:\\Users\\DarkPhantom1337\\Desktop\\ForTest\\QR600600.png"));
        int width = 105, height = 35;
        // encode(contents, width, height, imgPath);
        //System.out.print("Zoom " +tryDecode(getZoomImage("test21.jpg")));
        //String decodeContent = decode(imgPath);
        tryAllDecode();
        //System.out.println("Decode the content as followsbb:" + decodeContent + " Zoom");
        /*BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\DarkPhantom1337\\Desktop\\ForTest\\test.png"));
        originalImage.createGraphics();
        Graphics2D graphics = (Graphics2D) originalImage.getGraphics();
        Font font = new Font(Font.MONOSPACED, Font.PLAIN, 11);
        graphics.setFont(font);
        graphics.setColor(Color.white);
        graphics.fillRect(27, 25, 51, 35);
        Color mainColor = new Color(0, 0, 0);
        graphics.setColor(mainColor);
        graphics.drawString( "0001337", 29, originalImage.getHeight() -1);
        int type = originalImage.getType() == 0? BufferedImage.TYPE_INT_ARGB : originalImage.getType();
        ImageIO.write(originalImage, "png", new File("C:\\Users\\DarkPhantom1337\\Desktop\\testnum.png"));*/
        //tryAllDecode();
    }

    public static void tryAllDecode() throws IOException {
        int succes = 0, fail = 0, zommed = 0, resucces = 0, cb = 0, recb = 0;
        for (File f : new File("C:\\Users\\DarkPhantom1337\\Desktop\\ForTest").listFiles()) {
            String original_image_path = "C:\\Users\\DarkPhantom1337\\Desktop\\ForTest\\" + f.getName();
            try {
                String result = decode(original_image_path);
                System.out.print("\n" + f.getName() + "|ORIGINAL|SUCCESS| ✅ ");
                succes++;
                continue;
            } catch (NotFoundException e) {
                System.out.print("\n" + f.getName() + "|ORIGINAL|FAIL| ❌ ");
                fail++;
                BufferedImage zoomed = getZoomImage(f.getName());
                Boolean val = tryDecode(zoomed);
                if (val) {
                    System.out.print("|ZOOMED|SUCCESS| ✅ ");
                    zommed++;
                    succes++;
                    fail--;
                    continue;
                } else {
                    System.out.print("|ZOOMED|FAIL| ❌ ");
                }
                BufferedImage image = ImageIO.read(new File(original_image_path)), notrotated = image;
                Boolean istrue = false;
                for (int i = 1; i <= 4; i++) {
                    Boolean value = tryDecode(image);
                    if (value) {
                        succes++;
                        resucces++;
                        fail--;
                        istrue = true;
                        System.out.print("|ROTATED|SUCCESS| ✅ ");
                        break;
                    } else {
                        image = rotateClockwise90(image);
                    }
                }
                if (!istrue) {
                    System.out.print("|ROTATED|FAIL| ❌ ");
                    notrotated = getBlackWhiteImage(notrotated);
                    ImageIO.write(notrotated, "png", new File("C:\\Users\\DarkPhantom1337\\Desktop\\ЧБ_" + f.getName() + ".png"));
                    Boolean val1 = tryDecode(notrotated);
                    if (val1) {
                        succes++;
                        cb++;
                        fail--;
                        System.out.print("|BLACK&WHITE|SUCCESS| ✅ ");
                        continue;
                    } else {
                        System.out.print("|BLACK&WHITE|FAIL| ❌ ");
                        Boolean is = false;
                        for (int i = 1; i <= 4; i++) {
                            Boolean value = tryDecode(notrotated);
                            notrotated = rotateClockwise90(notrotated);
                            if (value) {
                                succes++;
                                recb++;
                                fail--;
                                System.out.print("|BLACK&WHITE-ROTATED|SUCCESS| ✅ ");
                                is = true;
                                break;
                            }
                        }
                        if (!is) {
                            System.out.print("|BLACK&WHITE-ROTATED|FAIL| ❌ ");
                        }
                    }
                }
            }
        }
        System.out.println("Statistic: Success: " + succes + " Fail: " + fail + " Zoomed: " + zommed + " Rotated: " + resucces + " Black&White: " + cb + " Black&White-Rotated: " + recb);
    }

    public static Boolean tryDecode(BufferedImage image) {
        try {
            String s = decodeFromImage(image);
            return true;
        } catch (NotFoundException e) {
            return false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String tryDecodeQR(BufferedImage image) {
        try {
            return decodeFromImage(image);
        } catch (Exception e) {
            return "NotFound";
        }
    }

    public static BufferedImage getBlackWhiteImage(BufferedImage image) throws IOException {
        float[] scales = {2f, 2f, 2f};
        float[] offsets = new float[4];
        RescaleOp rop = new RescaleOp(scales, offsets, null);
        final BufferedImage scaledImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        Graphics2D g = scaledImage.createGraphics();
        g.drawImage(image, rop, 0, 0);
        BufferedImage blackAndWhiteImage = new BufferedImage(
                image.getWidth(),
                image.getHeight(),
                BufferedImage.TYPE_BYTE_BINARY);
        g = blackAndWhiteImage.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return blackAndWhiteImage;
    }

    public static BufferedImage getZoomImage(String filename) throws IOException {
        BufferedImage originalImage = ImageIO.read(new File("C:\\Users\\DarkPhantom1337\\Desktop\\ForTest\\" + filename));
        BufferedImage dstinoimagencambiadabuffer = new BufferedImage(originalImage.getWidth() - 200, originalImage.getHeight() - 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dstinoimagencambiadabuffer.createGraphics();
        AffineTransform httpswwwmbajavacom = AffineTransform.getScaleInstance(1, 1);
        g.drawRenderedImage(originalImage, httpswwwmbajavacom);
        ImageIO.write(dstinoimagencambiadabuffer, "JPG", new File("C:\\Users\\DarkPhantom1337\\Desktop\\ForTest\\" + "Zoomed" + filename + ".jpg"));
        return dstinoimagencambiadabuffer;
    }

    public static BufferedImage getZoomImage(BufferedImage originalImage, String filename) throws IOException {
        BufferedImage dstinoimagencambiadabuffer = new BufferedImage(originalImage.getWidth() - 200, originalImage.getHeight() - 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dstinoimagencambiadabuffer.createGraphics();
        AffineTransform httpswwwmbajavacom = AffineTransform.getScaleInstance(1, 1);
        g.drawRenderedImage(originalImage, httpswwwmbajavacom);
        ImageIO.write(dstinoimagencambiadabuffer, "JPG", new File("C:\\Users\\DarkPhantom1337\\Desktop\\ForTest\\" + "ZoomedBlackWhite" + filename + ".jpg"));
        return dstinoimagencambiadabuffer;
    }

    public static BufferedImage getZoomedQRImage(BufferedImage originalImage) throws IOException {
        BufferedImage dstinoimagencambiadabuffer = new BufferedImage(originalImage.getWidth() - 200, originalImage.getHeight() - 300, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = dstinoimagencambiadabuffer.createGraphics();
        AffineTransform httpswwwmbajavacom = AffineTransform.getScaleInstance(1, 1);
        g.drawRenderedImage(originalImage, httpswwwmbajavacom);
        return dstinoimagencambiadabuffer;
    }


    private static BufferedImage resizeImageWithHint(BufferedImage originalImage, int type) {
        BufferedImage resizedImage = new BufferedImage(60, 60, type);
        Graphics2D g = resizedImage.createGraphics();
        g.drawImage(originalImage, 0, 0, 60, 60, null);
        g.dispose();
        g.setComposite(AlphaComposite.Src);
        g.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
                RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g.setRenderingHint(RenderingHints.KEY_RENDERING,
                RenderingHints.VALUE_RENDER_QUALITY);
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        return resizedImage;
    }

    public static BufferedImage rotateClockwise90(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();

        BufferedImage dest = new BufferedImage(height, width, src.getType());

        Graphics2D graphics2D = dest.createGraphics();
        graphics2D.translate((height - width) / 2, (height - width) / 2);
        graphics2D.rotate(Math.PI / 2, height / 2, width / 2);
        graphics2D.drawRenderedImage(src, null);

        return dest;
    }

    public static String readQRCode(File qrphoto) {
        try {
            BufferedImage qrimage = ImageIO.read(qrphoto),rotatedimage;
            String result = tryDecodeQR(qrimage);
            if (result.equals("NotFound")) {
                result = tryDecodeQR(getZoomedQRImage(qrimage));
                if (result.equals("NotFound")) {
                    result = tryDecodeQR(getBlackWhiteImage(qrimage));
                    if (result.equals("NotFound")) {
                        rotatedimage = rotateClockwise90(qrimage);
                        for (int i = 0; i <= 4; i++) {
                            result = tryDecodeQR(rotatedimage);
                            if (result.equals("NotFound")) {
                                rotatedimage = rotateClockwise90(rotatedimage);
                                continue;
                            } else return result;
                        }
                        rotatedimage = getBlackWhiteImage(qrimage);
                        for (int i = 0; i <= 4; i++) {
                            result = tryDecodeQR(rotatedimage);
                            if (result.equals("NotFound")) {
                                rotatedimage = rotateClockwise90(rotatedimage);
                                continue;
                            } else return result;
                        }
                        return "NotFound";
                    } else return result;
                } else return result;
            } else return result;
        } catch (Exception e) {
            return "NotFound";
        }
    }

}

