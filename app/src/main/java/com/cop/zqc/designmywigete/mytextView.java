package com.cop.zqc.designmywigete;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

public class mytextView extends AppCompatTextView {


    private final int mParm1;
    private final String mParm2;

    public mytextView(Context context, AttributeSet attrs) {


        super(context, attrs);

        TypedArray save = context.getTheme().obtainStyledAttributes(attrs,R.styleable.mytextView,0,0);

        mParm1 = save.getInteger(R.styleable.mytextView_parm1, 0);
        mParm2 = save.getString(R.styleable.mytextView_parm2);


    }


    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);

        Paint textpaint = new Paint();

        textpaint.setTextSize(100);

        Bitmap drawbitmap = BitmapFactory.decodeResource(this.getContext().getResources(),R.drawable.bitmaptest);

        canvas.drawBitmap(drawbitmap,null,new Rect(0,0,getWidth(),getHeight()),null);

        canvas.drawText(mParm2,getWidth()/2,getHeight()/2,textpaint);

    }
}
