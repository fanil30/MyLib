package com.wang.android_lib.util;

/**
 * by Administrator on 2016/2/29.
 * 需要用到core-3.1.0.jar
 */
public class QrCodeUtil {/*
    //    中间图标的大小。注意，图标大到一定程度，会无法识别
    private static final int IMAGE_HALFWIDTH = 20;

    public static Bitmap createTwoCodeBitmap(Bitmap middleBitmap, String text) throws WriterException {

//        制造二维码中间的图标
//        Bitmap mBitmap = ((BitmapDrawable) context.getResources().getDrawable(
//                middleBitmapId)).getBitmap();

//        Bitmap bitmap=Bitmap.createBitmap()

        Matrix m = new Matrix();
        float sx = (float) 2 * IMAGE_HALFWIDTH / middleBitmap.getWidth();
        float sy = (float) 2 * IMAGE_HALFWIDTH / middleBitmap.getHeight();
        m.setScale(sx, sy);

//        制造中间图标之外的黑白码
        middleBitmap = Bitmap.createBitmap(middleBitmap, 0, 0, middleBitmap.getWidth(),
                middleBitmap.getHeight(), m, false);

//        是否需要转码，视具体情况而定
//        try {
//            str = new String(str.getBytes(), "ISO-8859-1");
//        } catch (UnsupportedEncodingException e) {
//            e.printStackTrace();
//        }

        BitMatrix matrix = new MultiFormatWriter().encode(text,
                BarcodeFormat.QR_CODE, 300, 300);
        int width = matrix.getWidth();
        int height = matrix.getHeight();
        int halfW = width / 2;
        int halfH = height / 2;
        int[] pixels = new int[width * height];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x > halfW - IMAGE_HALFWIDTH && x < halfW + IMAGE_HALFWIDTH && y > halfH - IMAGE_HALFWIDTH
                        && y < halfH + IMAGE_HALFWIDTH) {
                    pixels[y * width + x] = middleBitmap.getPixel(x - halfW + IMAGE_HALFWIDTH, y
                            - halfH + IMAGE_HALFWIDTH);
                } else {
                    if (matrix.get(x, y)) {
                        pixels[y * width + x] = 0xff000000;
                    }
                }

            }
        }

        Bitmap bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height);

        return bitmap;
    }

    public static void saveBitmapAsJpeg(Bitmap bitmap, String filePath) {
        try {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
*/
}
