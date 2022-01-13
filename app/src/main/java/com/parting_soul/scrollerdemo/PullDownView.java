package com.parting_soul.scrollerdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;
import android.widget.Scroller;

import androidx.annotation.Nullable;

/**
 * @author parting_soul
 * @date 2022/1/12
 */
public class PullDownView extends LinearLayout {
    private Scroller mScroller;
    private int mDownX;
    private int mLastY;

    // 下拉的最大距离
    private int MAX_PULL_DIS = 200;

    public PullDownView(Context context) {
        this(context, null);
    }

    public PullDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(ev.getY() - mDownX) > 30) {
                    return true;
                }
                break;
        }
        //在拦截后 该方法就不会被调用
        mLastY = (int) ev.getY();
        return super.onInterceptTouchEvent(ev);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int dy = (int) (mLastY - event.getY());
                // MAX_PULL_DIS <= scrollX <=0
                if (dy + getScrollY() > 0) {
                    scrollTo(0, 0);
                } else if (getScrollY() + dy < -MAX_PULL_DIS) {
                    scrollTo(0, -MAX_PULL_DIS);
                } else {
                    scrollBy(0, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                mScroller.startScroll(0, getScrollY(), 0, -getScrollY());
                invalidate();
                break;
        }
        mLastY = (int) event.getY();
        return true;
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            invalidate();
        }
    }
}
