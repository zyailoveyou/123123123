package com.cop.zqc.designmywigete;

import android.content.Context;
import android.content.res.TypedArray;
import android.media.Image;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;

public class MyViewGroup extends ViewGroup {

    private  int mImageIDcenter;

    private ArrayList<ImageView> mImageViewsDataList;
    private float mRadios;
    private ImageView mImageCenter;
    private ImageView mImageViewIcon;
    private int mImageCenterStartLocationX;
    private int mImageCenterStartLocationY;
    private int mImageCenterWidth;
    private int mImageCenterHeight;


    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {

        super(context, attrs);

        TypedArray BuildImageData = context.obtainStyledAttributes(attrs,R.styleable.MyViewGroup);

        mRadios = BuildImageData.getFloat(R.styleable.MyViewGroup_Radios,0);

    }

    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

//    public MyViewGroup(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);

            measureChild(child, widthMeasureSpec, heightMeasureSpec);
        }

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int width, int Height) {

        layoutCenter();
        layoutList();

    }



    private void layoutCenter() {

        mImageCenter = (ImageView) getChildAt(0);

        mImageCenterWidth = mImageCenter.getMeasuredWidth();
        mImageCenterHeight = mImageCenter.getMeasuredHeight();

        int ParentWidth = getMeasuredWidth();
        int ParentHeight = getMeasuredHeight();

        mImageCenterStartLocationX = ParentWidth/2 - mImageCenterWidth/2;
        mImageCenterStartLocationY = ParentHeight - mImageCenterHeight;

        mImageCenter.layout(mImageCenterStartLocationX,
                mImageCenterStartLocationY,
                         mImageCenterStartLocationX +mImageCenterWidth,
                         mImageCenterStartLocationY +mImageCenterHeight);


    }


    private void layoutList() {


        for (int i = 1; i < getChildCount(); i++) {

            mImageViewIcon = (ImageView) getChildAt(i);

            int mImageIconWidth = mImageViewIcon.getMeasuredWidth();
            int mImageIconHeight = mImageViewIcon.getMeasuredHeight();


            //重点：坐标反推，有点复杂 主要是横坐标反推
            int mImageIconStartLocationX = mImageCenterStartLocationX + mImageCenterWidth/2 - (int)(Math.cos((Math.PI/4)*i)*mRadios)- mImageIconWidth/2;

            int mImageIconStartLocationY = mImageCenterStartLocationY - (int)(Math.sin((Math.PI/4)*i)*mRadios);

            mImageViewIcon.layout(mImageIconStartLocationX,mImageIconStartLocationY,mImageIconStartLocationX+mImageIconWidth,mImageIconStartLocationY+mImageIconHeight);

        }

    }

}






