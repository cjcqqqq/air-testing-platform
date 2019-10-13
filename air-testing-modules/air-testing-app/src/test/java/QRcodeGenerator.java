import java.awt.image.BufferedImage;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import java.util.Hashtable;

import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.university.shenyang.air.testing.common.util.AESUtil;

/**
 * 二维码的生成需要借助MatrixToImageWriter类，该类是由Google提供的，可以将该类直接拷贝到源码中使用
 */
public class QRcodeGenerator {
    private static final int BLACK = 0xFF000000;
    private static final int WHITE = 0xFFFFFFFF;

    private QRcodeGenerator() {
    }

    public static BufferedImage toBufferedImage(BitMatrix matrix) {
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, matrix.get(x, y) ? BLACK : WHITE);
            }
        }
        return image;
    }

    public static void writeToFile(BitMatrix matrix, String format, File file)
            throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, file)) {
            throw new IOException("Could not write an image of format "
                    + format + " to " + file);
        }
    }

    public static void writeToStream(BitMatrix matrix, String format,
            OutputStream stream) throws IOException {
        BufferedImage image = toBufferedImage(matrix);
        if (!ImageIO.write(image, format, stream)) {
            throw new IOException("Could not write an image of format " + format);
        }
    }

    public static void main(String[] args) throws Exception {
        String aesPassword = "aa7889d3-435b-4ed2-99f9-08035661eda9";

        int width = 300; // 二维码图片宽度
        int height = 300; // 二维码图片高度
        String format = "jpg";// 二维码的图片格式

//        String[] deviceCodes = {"deviceCode0000001", "deviceCode0000002", "deviceCode0000003", "deviceCode0000004", "deviceCode0000005", "deviceCode0000006", "deviceCode0000007", "deviceCode0000008", "deviceCode0000009", "deviceCode0000010", "deviceCode0000011", "deviceCode0000012", "deviceCode0000013", "deviceCode0000014", "deviceCode0000015", "deviceCode0000016", "deviceCode0000017", "deviceCode0000018", "deviceCode0000019", "deviceCode0000020", "deviceCode0000021", "deviceCode0000022", "deviceCode0000023", "deviceCode0000024", "deviceCode0000025", "deviceCode0000026", "deviceCode0000027", "deviceCode0000028", "deviceCode0000029", "deviceCode0000030", "deviceCode0000031", "deviceCode0000032", "deviceCode0000033", "deviceCode0000034", "deviceCode0000035", "deviceCode0000036", "deviceCode0000037", "deviceCode0000038", "deviceCode0000039", "deviceCode0000040", "deviceCode0000041", "deviceCode0000042", "deviceCode0000043", "deviceCode0000044", "deviceCode0000045", "deviceCode0000046", "deviceCode0000047", "deviceCode0000048", "deviceCode0000049", "deviceCode0000050", "deviceCode0000051", "deviceCode0000052", "deviceCode0000053", "deviceCode0000054", "deviceCode0000055", "deviceCode0000056", "deviceCode0000057", "deviceCode0000058", "deviceCode0000059", "deviceCode0000060", "deviceCode0000061", "deviceCode0000062", "deviceCode0000063", "deviceCode0000064", "deviceCode0000065", "deviceCode0000066", "deviceCode0000067", "deviceCode0000068", "deviceCode0000069", "deviceCode0000070", "deviceCode0000071", "deviceCode0000072", "deviceCode0000073", "deviceCode0000074", "deviceCode0000075", "deviceCode0000076", "deviceCode0000077", "deviceCode0000078", "deviceCode0000079", "deviceCode0000080", "deviceCode0000081", "deviceCode0000082", "deviceCode0000083", "deviceCode0000084", "deviceCode0000085", "deviceCode0000086", "deviceCode0000087", "deviceCode0000088", "deviceCode0000089", "deviceCode0000090", "deviceCode0000091", "deviceCode0000092", "deviceCode0000093", "deviceCode0000094", "deviceCode0000095", "deviceCode0000096", "deviceCode0000097", "deviceCode0000098", "deviceCode0000099", "deviceCode0000100", "deviceCode0000101", "deviceCode0000102", "deviceCode0000103", "deviceCode0000104", "deviceCode0000105", "deviceCode0000106", "deviceCode0000107", "deviceCode0000108", "deviceCode0000109", "deviceCode0000110", "deviceCode0000111", "deviceCode0000112", "deviceCode0000113", "deviceCode0000114", "deviceCode0000115", "deviceCode0000116", "deviceCode0000117", "deviceCode0000118", "deviceCode0000119", "deviceCode0000120", "deviceCode0000121", "deviceCode0000122", "deviceCode0000123", "deviceCode0000124", "deviceCode0000125", "deviceCode0000126", "deviceCode0000127", "deviceCode0000128", "deviceCode0000129", "deviceCode0000130", "deviceCode0000131", "deviceCode0000132", "deviceCode0000133", "deviceCode0000134", "deviceCode0000135", "deviceCode0000136", "deviceCode0000137", "deviceCode0000138", "deviceCode0000139", "deviceCode0000140", "deviceCode0000141", "deviceCode0000142", "deviceCode0000143", "deviceCode0000144", "deviceCode0000145", "deviceCode0000146", "deviceCode0000147", "deviceCode0000148", "deviceCode0000149", "deviceCode0000150", "deviceCode0000151", "deviceCode0000152", "deviceCode0000153", "deviceCode0000154", "deviceCode0000155", "deviceCode0000156", "deviceCode0000157", "deviceCode0000158", "deviceCode0000159", "deviceCode0000160"};
        String[] deviceCodes = {"deviceCode0000201"};
        for (String filename:deviceCodes
             ) {
            String qrCodeContent = AESUtil.encrypt(filename, aesPassword);
            System.out.println("deviceCode:"+filename+"  qrCodeContent:"+qrCodeContent);
            qrCodeImageGeneration(qrCodeContent, width, height, filename, format);
        }
    }

    private static void qrCodeImageGeneration(String text, int width, int height, String filename, String imgFmt) throws WriterException, IOException {
        Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8"); // 内容所使用字符集编码

        BitMatrix bitMatrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, width, height, hints);
        // 生成二维码
        File outputFile = new File("d://QRcode//" + filename + "." + imgFmt);
        QRcodeGenerator.writeToFile(bitMatrix, imgFmt, outputFile);
    }
}