package com.parting_soul.scrollerdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;

/**
 * @author parting_soul
 * @date 2022/1/12
 */
public class CustomScrollerLayout extends ViewGroup {
    private int leftBorder;
    private int rightBorder;
    private int mTouchSlop;

    private Scroller mScroller;

    public CustomScrollerLayout(Context context) {
        this(context, null);
    }

    public CustomScrollerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomScrollerLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        // 获取TouchSlop值
        mTouchSlop = configuration.getScaledTouchSlop();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        measureChildren(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int offset = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(offset, 0, offset + child.getMeasuredWidth(), child.getMeasuredHeight());
            offset += child.getMeasuredWidth();
            if (i == 0) {
                leftBorder = child.getLeft();
            } else if (i == childCount - 1) {
                rightBorder = child.getRight();
            }
        }
    }

    int xDown;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xDown = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(xDown - ev.getX()) > mTouchSlop) {
                    return true;
                }
                break;
        }
        mLastX = (int) ev.getX();
        return super.onInterceptTouchEvent(ev);
    }

    private int mLastX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                int distance = (int) (mLastX - event.getX());
                //  0 <= scrollX <=  rightBorder - leftBorder - width
                if (distance >= 0 && getScrollX() + distance > (rightBorder - leftBorder - getWidth())) {
                    scrollTo(rightBorder - leftBorder - getWidth(), 0);
                } else if (distance <= 0 && getScrollX() + distance < leftBorder) {
                    scrollTo(leftBorder, 0);
                } else {
                    scrollBy(distance, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                int index = (getScrollX() + getWidth() / 2) / getWidth();
                int dx = index * getWidth() - getScrollX();
                mScroller.startScroll(getScrollX(), 0, dx, 0);
                invalidate();
                break;
        }
        mLastX = (int) event.getX();
        return true;
    }

    @Override
    public void computeScroll() {
        // 是否滑动结束
        if (mScroller.computeScrollOffset()) {
            // 未结束则继续滑动
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();
        }
    }
}
