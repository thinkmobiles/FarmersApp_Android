package com.farmers.underground.ui.utils;

import android.graphics.*;
import android.widget.ImageView;
import com.farmers.underground.R;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

/**
 * Created by omar on 10/2/15.
 */
public class CropsImageLoader {


    public static void loadImage(final ImageView imageView, String imagePath){
        Round_Corners transformation = new Round_Corners(0,ResourceRetriever.retrievePX(imageView.getContext(), R
                .dimen.crops_card_corner_radius));
        Picasso.with(imageView.getContext()).load(imagePath)
                .transform(transformation)
                 .into(imageView);
    }

    private static class Round_Corners implements Transformation {
        private int Round;

        Round_Corners(int margin, int Round) {
            this.Round = Round;

        }

        @Override
        public String key() {
            return "Round" + Round;
        }

        @Override
        public Bitmap transform(Bitmap arg0) {
            // TODO Auto-generated method stub
            return getRoundedTopLeftCornerBitmap(arg0);
        }

        public Bitmap getRoundedTopLeftCornerBitmap(Bitmap bitmap) {
            Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                    bitmap.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(output);

            final int color = 0xff424242;
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
            final RectF rectF = new RectF(rect);
            final float Px = Round;

            final Rect bottomRect = new Rect(0, bitmap.getHeight() / 2,
                    bitmap.getWidth(), bitmap.getHeight());

            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(color);
            canvas.drawRoundRect(rectF, Px, Px, paint);
            // Fill in upper right corner
            // canvas.drawRect(topRightRect, paint);
            // Fill in bottom corners
            canvas.drawRect(bottomRect, paint);

            paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
            canvas.drawBitmap(bitmap, rect, rect, paint);
            if (bitmap != output) {
                bitmap.recycle();
            }
            return output;
        }

    }
}
