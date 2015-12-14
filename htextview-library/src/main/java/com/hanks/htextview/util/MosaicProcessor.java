package com.hanks.htextview.util;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Rect;

/**
 * mosaic processor
 *
 * @author sky
 */
public class MosaicProcessor {

    public static final int min_mosaic_block_size = 4;

    private MosaicProcessor() {
    }

    /**
     * @param bitmap
     * @param targetRect
     * @param blockSize  {@link #min_mosaic_block_size}
     * @return
     * @throws Exception
     */
    public static Bitmap makeMosaic(Bitmap bitmap, Rect targetRect, int blockSize) throws OutOfMemoryError {
        if (bitmap == null || bitmap.getWidth() == 0 || bitmap.getHeight() == 0 || bitmap.isRecycled()) {
            throw new RuntimeException("bad bitmap to add mosaic");
        }
        if (blockSize < min_mosaic_block_size) {
            blockSize = min_mosaic_block_size;
        }
        if (targetRect == null) {
            targetRect = new Rect();
        }
        int bw = bitmap.getWidth();
        int bh = bitmap.getHeight();
        if (targetRect.isEmpty()) {
            targetRect.set(0, 0, bw, bh);
        }
        //
        int rectW = targetRect.width();
        int rectH = targetRect.height();
        int[] bitmapPxs = new int[bw * bh];
        // fetch bitmap pxs
        bitmap.getPixels(bitmapPxs, 0, bw, 0, 0, bw, bh);
        //
        int rowCount = (int) Math.ceil((float) rectH / blockSize);
        int columnCount = (int) Math.ceil((float) rectW / blockSize);
        int maxX = bw;
        int maxY = bh;
        for (int r = 0; r < rowCount; r++) { // row loop
            for (int c = 0; c < columnCount; c++) {// column loop
                int startX = targetRect.left + c * blockSize + 1;
                int startY = targetRect.top + r * blockSize + 1;
                dimBlock(bitmapPxs, startX, startY, blockSize, maxX, maxY);
            }
        }
        return Bitmap.createBitmap(bitmapPxs, bw, bh, Config.ARGB_8888);
    }

    /**
     * 从块内取样，并放大，从而达到马赛克的模糊效果
     *
     * @param pxs
     * @param startX
     * @param startY
     * @param blockSize
     * @param maxX
     * @param maxY
     */
    private static void dimBlock(int[] pxs, int startX, int startY, int blockSize, int maxX, int maxY) {
        int stopX = startX + blockSize - 1;
        int stopY = startY + blockSize - 1;
        if (stopX > maxX) {
            stopX = maxX;
        }
        if (stopY > maxY) {
            stopY = maxY;
        }
        //
        int sampleColorX = startX + blockSize / 2;
        int sampleColorY = startY + blockSize / 2;
        //
        if (sampleColorX > maxX) {
            sampleColorX = maxX;
        }
        if (sampleColorY > maxY) {
            sampleColorY = maxY;
        }
        int colorLinePosition = (sampleColorY - 1) * maxX;
        int sampleColor = pxs[colorLinePosition + sampleColorX - 1];// 像素从1开始，但是数组层0开始
        for (int y = startY; y <= stopY; y++) {
            int p = (y - 1) * maxX;
            for (int x = startX; x <= stopX; x++) {
                // 像素从1开始，但是数组层0开始
                pxs[p + x - 1] = sampleColor;
            }
        }
    }

}

