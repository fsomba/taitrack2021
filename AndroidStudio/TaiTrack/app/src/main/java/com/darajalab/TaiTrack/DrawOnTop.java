package com.darajalab.TaiTrack;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

class DrawOnTop extends View {
    int screenCenterX = 0;
    int screenCenterY = 0;
    final int radius = 224;
    public DrawOnTop(Context context, int screenCenterX, int screenCenterY) {
        super(context);
        this.screenCenterX = screenCenterX;
        this.screenCenterY = screenCenterY;
    }
    //

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // set up some constants
        int w = getWidth();
        int h = getHeight();

        // the outer fill color
        //int outerFillColor = 0x77000000;

        // first create a separate off-screen bitmap and its canvas
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas auxCanvas = new Canvas(bitmap);

        // then fill the bitmap with the desired outside color
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(Color.GREEN);
        paint.setStyle(Paint.Style.FILL);
        paint.setAlpha(30);
        auxCanvas.drawPaint(paint);

        /* then punch a transparent hole in the canvas
        the hole should be same size as the oval to be drawn
        */
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        auxCanvas.drawOval(50, 100,
                650, screenCenterY + radius,paint);

        // then draw the green oval border (being sure to get rid of the xfer mode!)
        paint.setXfermode(null);
        paint.setColor(Color.GREEN);
        //use a dotted line
        DashPathEffect dashPath = new DashPathEffect(new float[]{5,5}, (float)1.0);
        paint.setPathEffect(dashPath);
        //define density of the oval border
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(8);
        auxCanvas.drawOval(50, 100,
                650, screenCenterY + radius,paint);

        // finally, draw the whole thing to the original canvas
        canvas.drawBitmap(bitmap, 0, 0, paint);
    }
}
