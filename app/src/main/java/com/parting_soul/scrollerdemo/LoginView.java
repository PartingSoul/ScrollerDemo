package com.parting_soul.scrollerdemo;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * @author parting_soul
 * @date 2022/1/12
 */
public class LoginView extends RelativeLayout {
    private Scroller mScroller;
    private int mTouchSlop;

    public LoginView(Context context) {
        this(context, null);
    }

    public LoginView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoginView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mScroller = new Scroller(context);
        mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT);
        View content = LayoutInflater.from(context).inflate(R.layout.rl_login, null, false);
        addView(content, params);

        content.findViewById(R.id.iv_close)
                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideLoginView();
                    }
                });
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        scrollTo(0, -getHeight());
    }

    private int mDownY;

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (Math.abs(mDownY - ev.getY()) > mTouchSlop) {
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        mLastY = (int) ev.getY();
        return super.onInterceptTouchEvent(ev);
    }

    private int mLastY;
    private boolean isPullUp;
    private int ANIMATION_DURATION_TIME = 1000;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                // -height <= scrollY  <= 0
                int dy = (int) (mLastY - event.getY());
                if (dy >= 0 && getScrollY() + dy > 0) {
                    scrollTo(0, 0);
                } else if (dy <= 0 && getScrollY() + dy < -getHeight()) {
                    scrollTo(0, -getHeight());
                } else {
                    scrollBy(0, dy);
                }
                break;
            case MotionEvent.ACTION_UP:
                isPullUp = mDownY - event.getY() >= 0;
                int disy;
                if (isPullUp) {
                    disy = -getScrollY();
                } else {
                    disy = -(getHeight() + getScrollY());
                }
                startAnim(disy);
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

    public void showLoginView() {
        int dy = -getScrollY();
        startAnim(dy);
    }

    public void hideLoginView() {
        int dy = -(getHeight() + getScrollY());
        startAnim(dy);
    }

    private void startAnim(int dy) {
        int duration = (int) (Math.abs(dy) * 1.0f / getHeight() * ANIMATION_DURATION_TIME);
        mScroller.startScroll(0, getScrollY(), 0, dy, duration);
        invalidate();
    }

}
