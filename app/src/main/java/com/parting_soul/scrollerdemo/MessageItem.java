package com.parting_soul.scrollerdemo;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.Scroller;
import android.widget.Toast;

/**
 * @author parting_soul
 * @date 2022/1/13
 */
public class MessageItem extends ViewGroup {
    private Scroller mScroller;
    private int maxScrollX = 0;
    private boolean isPullLeft;
    private int mTouchSlop;

    public MessageItem(Context context) {
        this(context, null);
    }

    public MessageItem(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MessageItem(final Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        View content = LayoutInflater.from(context).inflate(R.layout.merge_message_item,
                this, true);
        content.setBackgroundColor(Color.WHITE);


        content.findViewById(R.id.tv_top)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "置顶", Toast.LENGTH_SHORT).show();
                    }
                });

        content.findViewById(R.id.tv_delete)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show();
                    }
                });

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            LayoutParams params = child.getLayoutParams();

            int childWidthMeasureSpec;
            if (params.width == LayoutParams.MATCH_PARENT) {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            } else if (params.width == LayoutParams.WRAP_CONTENT) {
                child.measure(0, 0);
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(child.getMeasuredWidth(), MeasureSpec.EXACTLY);
            } else {
                childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(params.width, MeasureSpec.EXACTLY);
            }

            child.measure(childWidthMeasureSpec,
                    MeasureSpec.makeMeasureSpec(200, MeasureSpec.EXACTLY));
        }
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), 200);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int childCount = getChildCount();
        int offsetX = 0;
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            child.layout(offsetX, 0, offsetX + child.getMeasuredWidth(), child.getMeasuredHeight());
            offsetX += child.getMeasuredWidth();
            if (i != 0) {
                maxScrollX += child.getWidth();
            }
        }
    }

    private int mLastX;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) ev.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownX - ev.getX()) > mTouchSlop) {
                    return true;
                }
                break;
        }
        mLastX = (int) ev.getX();
        return super.onInterceptTouchEvent(ev);
    }

    private int mDownX;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownX = (int) event.getX();
                break;
            case MotionEvent.ACTION_MOVE:
                int dx = (int) (mLastX - event.getX());
                // 0  <= scrollX <= maxScrollX
                if (dx <= 0 && dx + getScrollX() < 0) {
                    scrollTo(0, 0);
                } else if (dx >= 0 && dx + getScrollX() > maxScrollX) {
                    scrollTo(maxScrollX, 0);
                } else {
                    scrollBy(dx, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                isPullLeft = mDownX - event.getX() >= 0;
                int disX = 0;
                if (isPullLeft) {
                    //左划
                    if (getScrollX() >= maxScrollX / 2) {
                        disX = maxScrollX - getScrollX();
                    } else {
                        disX = -getScrollX();
                    }
                } else {
                    // 右划
                    if (getScrollX() >= maxScrollX / 2) {
                        disX = maxScrollX - getScrollX();
                    } else {
                        disX = -getScrollX();
                    }
                }
                mScroller.startScroll(getScrollX(), 0, disX, 0);
                invalidate();
                break;
        }
        mLastX = (int) event.getX();
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
