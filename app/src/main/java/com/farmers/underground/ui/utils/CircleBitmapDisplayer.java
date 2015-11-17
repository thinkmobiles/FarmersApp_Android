package com.farmers.underground.ui.utils;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Rect;

import com.nostra13.universalimageloader.core.assist.LoadedFrom;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

/**
 * on 16-Nov-15.
 */
public class CircleBitmapDisplayer  extends RoundedBitmapDisplayer {


    public CircleBitmapDisplayer() {
        super(0);
    }

    @Override
    public void display(Bitmap bitmap, ImageAware imageAware, LoadedFrom loadedFrom) {
        if (!(imageAware instanceof ImageViewAware)) {
            throw new IllegalArgumentException("ImageAware should wrap ImageView. ImageViewAware is expected.");
        }

        imageAware.setImageDrawable(new CircleDrawable(bitmap, margin));
    }

    public static class CircleDrawable extends RoundedDrawable {

        private Bitmap mBitmap;

        public CircleDrawable(Bitmap bitmap, int margin) {
            super(bitmap, 0, margin);
            this.mBitmap = bitmap;
        }

        @Override
        public void draw(Canvas canvas) {
            int radius;
            if(mBitmap.getWidth() > mBitmap.getHeight()) {
                radius = mBitmap.getHeight() / 2;
            }else {
                radius = mBitmap.getWidth() / 2;
            }
            canvas.drawRoundRect(mRect, radius, radius, paint);
        }

        @Override
        protected void onBoundsChange(Rect bounds) {
            super.onBoundsChange(bounds);
            mRect.set(margin, margin, bounds.width() - margin, bounds.height() - margin);

            // Resize the original bitmap to fit the new bound
            Matrix shaderMatrix = new Matrix();
            shaderMatrix.setRectToRect(mBitmapRect, mRect, Matrix.ScaleToFit.CENTER);
            bitmapShader.setLocalMatrix(shaderMatrix);

        }
    }
}