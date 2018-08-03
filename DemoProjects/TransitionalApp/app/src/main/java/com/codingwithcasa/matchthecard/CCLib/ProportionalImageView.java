package com.codingwithcasa.matchthecard.CCLib;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/**
 * The following class is an extension of an ImageView
 * With the exception it fills w/e space it is contained in
 */
public class ProportionalImageView extends ImageView {

    private int mRowCol = 3;
    private int mImageId;
    private int mOrginalId;

    public ProportionalImageView(Context context) {
        super(context);
    }
    public ProportionalImageView(Context context, int rowCol) {
        super(context);
        mRowCol = rowCol;
    }

    public ProportionalImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ProportionalImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        Drawable d = getDrawable();
        if (d != null) {
            int w = MeasureSpec.getSize(widthMeasureSpec);
            int h = w * d.getIntrinsicHeight() / d.getIntrinsicWidth();
            setMeasuredDimension(w/mRowCol, h/mRowCol);
        }
        else super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    public void setImageId(int id){
        mImageId = id;
    }

    public int getImageId(){
        return mImageId;
    }

    public void setOriginalId(int id){
        mOrginalId = id;
    }

    public int getOriginalId(){
        return mOrginalId;
    }
}