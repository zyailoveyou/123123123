package com.cop.zqc.designmywigete;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;


public class MyViewGroup extends ViewGroup implements TypeEvaluator,Clone {

    private float mRadios;
    private ImageView mImageCenter;
    private ImageView mImageViewIcon;
    private int mImageCenterStartLocationX;
    private int mImageCenterStartLocationY;
    private int mImageCenterWidth;
    private int mImageCenterHeight;
    private boolean IsOpening;
    private int mImageIconStartLocationX;
    private int mImageIconStartLocationY;
    private ImageView mChildImageView;
    private ArrayList<ImageView> mMenuImageViewList;
    private ArrayList<Position> mPositionToAnimatorList;
    private Position mAnimatorCenterPosition;
    private int mParentWidth;
    private int mParentHeight;
    private int mAdapterCenterWidth;
    private int mAdapterCenterHeight;
    private int mDuration;
    private int mCenterIconRotaion;
    private int mListIconRotaion;
    private Context mContext;

    public MyViewGroup(Context context) {
        super(context);
    }

    public MyViewGroup(Context context, AttributeSet attrs) {

        super(context, attrs);

        mContext = context;

        TypedArray BuildImageData = context.obtainStyledAttributes(attrs,R.styleable.MyViewGroup);
        mRadios = BuildImageData.getFloat(R.styleable.MyViewGroup_Radios,0);
        mDuration = BuildImageData.getInteger(R.styleable.MyViewGroup_Duration,0);
        mCenterIconRotaion = BuildImageData.getInteger(R.styleable.MyViewGroup_CenterIconRotation,0);
        mListIconRotaion = BuildImageData.getInteger(R.styleable.MyViewGroup_ListIconRotation,0);
        mPositionToAnimatorList = new ArrayList<>();
        IsOpening = false;

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


        mImageCenter = (ImageView) getChildAt(getChildCount()-1);

        mImageCenterWidth = mImageCenter.getMeasuredWidth();
        mImageCenterHeight = mImageCenter.getMeasuredHeight();

        mParentWidth = getMeasuredWidth();
        mParentHeight = getMeasuredHeight();

        mImageCenterStartLocationX = mParentWidth /2 - mImageCenterWidth/2;
        mImageCenterStartLocationY = mParentHeight - mImageCenterHeight;

        layoutList();
        layoutCenter();
//

    }



    private void layoutCenter() {

        mImageCenter.layout(mImageCenterStartLocationX,
                            mImageCenterStartLocationY,
                         mImageCenterStartLocationX +mImageCenterWidth,
                         mImageCenterStartLocationY +mImageCenterHeight);

        mImageCenter.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {

                CenterOnClick();

            }
        });

    }


    private void layoutList() {

        for (int i = 0; i < getChildCount()-1; i++) {

            int k = i+1;

            mImageViewIcon = (ImageView) getChildAt(i);

            mImageViewIcon.setVisibility(INVISIBLE);

            int mImageIconWidth = mImageViewIcon.getMeasuredWidth();
            int mImageIconHeight = mImageViewIcon.getMeasuredHeight();

            mAdapterCenterWidth = mImageIconWidth;
            mAdapterCenterHeight = mImageIconHeight;

//重点：坐标反推，有点复杂 主要是横坐标反推
            mImageIconStartLocationX = mImageCenterStartLocationX + mImageCenterWidth/2 - (int)(Math.cos((Math.PI/4)*k)*mRadios)- mImageIconWidth/2;
            mImageIconStartLocationY = mImageCenterStartLocationY - (int)(Math.sin((Math.PI/4)*k)*mRadios);
//重点：坐标反推，有点复杂 主要是横坐标反推

//设置所有子list的发散点坐标
            Position AnimatorCenterPosition = new Position();
            AnimatorCenterPosition.setX(mImageIconStartLocationX);
            AnimatorCenterPosition.setY(mImageIconStartLocationY);
            mPositionToAnimatorList.add(AnimatorCenterPosition);
//设置所有子list的发散点坐标

            mImageViewIcon.layout(mImageIconStartLocationX,
                    mImageIconStartLocationY,
                    mImageIconStartLocationX+mImageIconWidth,
                    mImageIconStartLocationY+mImageIconHeight);

            mImageViewIcon.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {

                    ListOnClick(view);

                }
            });

        }

//设置动画的中心坐标
        mAnimatorCenterPosition = new Position();
        mAnimatorCenterPosition.setX(mParentWidth/2-mAdapterCenterWidth/2);
        mAnimatorCenterPosition.setY(mParentHeight- mImageCenterHeight/2-mAdapterCenterHeight/2);
//设置动画的中心坐标

    }

    private void ListOnClick(View view) {

        switch (view.getId()) {

            case R.id.icon1:

                Toast.makeText(mContext,"孤儿1",Toast.LENGTH_SHORT).show();

                break;
            case R.id.icon2:

                Toast.makeText(mContext,"孤儿2",Toast.LENGTH_SHORT).show();

                break;

            case R.id.icon3:

                Toast.makeText(mContext,"孤儿3",Toast.LENGTH_SHORT).show();


                break;


        }
    }


    public void CenterOnClick(){

            MeumAnimation();

    }



    private void MeumAnimation() {

        mMenuImageViewList = new ArrayList<>();

        for (int i = 0; i < getChildCount()-1; i++) {

            mChildImageView = (ImageView) getChildAt(i);

            mMenuImageViewList.add(mChildImageView);

            mChildImageView.setVisibility(VISIBLE);

        }

        ValueAnimator OpenMenuAnimation = ValueAnimator.ofObject(this,mPositionToAnimatorList,mAnimatorCenterPosition);
        OpenMenuAnimation.setDuration(mDuration);
        OpenMenuAnimation.setInterpolator(new OvershootInterpolator());

        OpenMenuAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                for (int i = 0; i < mMenuImageViewList.size(); i++) {

                    mMenuImageViewList.get(i).setX(((ArrayList<Position>) valueAnimator.getAnimatedValue()).get(i).getX());
                    mMenuImageViewList.get(i).setY(((ArrayList<Position>) valueAnimator.getAnimatedValue()).get(i).getY());

                }

            }

        });


        OpenMenuAnimation.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

                mImageCenter.setEnabled(false);

            }

            @Override
            public void onAnimationEnd(Animator animator) {

                if(IsOpening == true)
                {
                    IsOpening = false;
                }
                else
                {
                    IsOpening = true;
                }

                mImageCenter.setEnabled(true);
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });


        float CenterStartRotationArc = 0;
        float CenterEndRotationArc = mCenterIconRotaion;

        if(IsOpening == true)
        {
            CenterStartRotationArc = mImageCenter.getRotation();
            CenterEndRotationArc = 0;

        }

        //中心旋转动画
        ValueAnimator RotationCenterAnimation = ValueAnimator.ofFloat(CenterStartRotationArc,CenterEndRotationArc);
        RotationCenterAnimation.setDuration(mDuration);
        RotationCenterAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                mImageCenter.setRotation((Float) valueAnimator.getAnimatedValue());

            }
        });



        float ListStartRotationArc = 0;
        float ListEndRotationArc = mListIconRotaion;

        if(IsOpening == true)
        {
            ListStartRotationArc = mMenuImageViewList.get(0).getRotation();
            ListEndRotationArc = 0;

        }


        //list旋转动画
        ValueAnimator RotationListAnimation = ValueAnimator.ofFloat(ListStartRotationArc,ListEndRotationArc);
        RotationListAnimation.setDuration(mDuration);
        RotationListAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {

                for (int i = 0; i < mMenuImageViewList.size(); i++) {

                    mMenuImageViewList.get(i).setRotation((Float) valueAnimator.getAnimatedValue());

                }

            }
        });




        AnimatorSet AnimationGroup = new AnimatorSet();
        AnimationGroup.playTogether(OpenMenuAnimation,RotationCenterAnimation,RotationListAnimation);
        AnimationGroup.start();


    }


    @Override
    public Object evaluate(float fraction, Object StartLocationList, Object EndLocationList) {

        ArrayList<Position> ReturnArrayLocationLisht = new ArrayList<>();

        ReturnArrayLocationLisht = this.CloneArrayList((ArrayList<Position>)StartLocationList);

        if (IsOpening == false) {

            for (int i = 0; i < mMenuImageViewList.size(); i++) {

                int NowLocationX = ReturnArrayLocationLisht.get(i).getX();
                int NowLocationY = ReturnArrayLocationLisht.get(i).getY();

                int EndPositionX = ((Position)EndLocationList).getX();
                int EndPositionY = ((Position)EndLocationList).getY();

                int AnimatorLocationX = EndPositionX - (int)(fraction*(EndPositionX-NowLocationX));
                int AnimatorLocationY = EndPositionY - (int)(fraction*(EndPositionY-NowLocationY));

                ReturnArrayLocationLisht.get(i).setX(AnimatorLocationX);
                ReturnArrayLocationLisht.get(i).setY(AnimatorLocationY);

            }

        }

        if (IsOpening == true){

            for (int i = 0; i < mMenuImageViewList.size(); i++) {

                int NowLocationX = ReturnArrayLocationLisht.get(i).getX();
                int NowLocationY = ReturnArrayLocationLisht.get(i).getY();

                int EndPositionX = ((Position)EndLocationList).getX();
                int EndPositionY = ((Position)EndLocationList).getY();

                int AnimatorLocationX = NowLocationX + (int)(fraction*(EndPositionX-NowLocationX));
                int AnimatorLocationY = NowLocationY + (int)(fraction*(EndPositionY-NowLocationY));

                ReturnArrayLocationLisht.get(i).setX(AnimatorLocationX);
                ReturnArrayLocationLisht.get(i).setY(AnimatorLocationY);

            }

        }

        return ReturnArrayLocationLisht;

    }

    @Override
    public ArrayList<Position> CloneArrayList(ArrayList<Position> Targat) {

        ArrayList<Position> Cloned = new ArrayList<>();

        for (int i = 0; i < Targat.size(); i++) {


            int x = Targat.get(i).getX();
            int y = Targat.get(i).getY();

            Position xy = new Position();

            xy.setX(x);
            xy.setY(y);

            Cloned.add(xy);

        }

        return Cloned;

    }
}






