package com.wang.android_lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class SlidingView extends HorizontalScrollView {

    private LinearLayout mWapper;
    private ViewGroup mMenu;
    private ViewGroup mContent;
    private int mMenuWidth;
    private int mScreenWidth;
    private boolean once = true;
    public boolean isMenuOpen = false;

    // dp
    private int mMenuRightPaddingDp = 100;
    // px
    private int mMenuRightPadding;

    public interface OnMenuSlidingListener {
        public void onMenuSliding(float scale);
    }

    private OnMenuSlidingListener onMenuSlidingListener;

    public void setOnMenuSlidingListener(OnMenuSlidingListener listener) {
        onMenuSlidingListener = listener;
    }

    public SlidingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        // 获取屏幕宽度
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        mScreenWidth = outMetrics.widthPixels;

        // COMPLEX_UNIT_DIP:dp转换为px（像素）, COMPLEX_UNIT_SP:sp
        mMenuRightPadding = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, mMenuRightPaddingDp, context
                        .getResources().getDisplayMetrics());

        setHorizontalScrollBarEnabled(false);
        setOverScrollMode(ScrollView.OVER_SCROLL_NEVER);//去除末端蓝色回弹光
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        long preTime = System.nanoTime();
        if (once) {
            mWapper = (LinearLayout) getChildAt(0);
            mMenu = (ViewGroup) mWapper.getChildAt(0);
            mContent = (ViewGroup) mWapper.getChildAt(1);
            mMenu.getLayoutParams().width = mMenuWidth = mScreenWidth
                    - mMenuRightPadding;
            mContent.getLayoutParams().width = mScreenWidth;
            once = false;
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        System.out.println("onMeasure: " + (System.nanoTime() - preTime) / 1000);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        long preTime = System.nanoTime();
        super.onLayout(changed, l, t, r, b);
        if (changed) {
            scrollTo(mMenuWidth, 0);
        }
//        System.out.println("onLayout: " + (System.nanoTime() - preTime) / 1000);
    }

    @Override
    public void fling(int velocityX) {
        super.fling(velocityX / 3);
    }

    private float xDown;//留在onTouchEvent中使用
    private float xDistance, yDistance, xLast, yLast;
    private final float k = 0.5f;//k为区分左右和上下滑动的直线的斜率

    /**
     * http://m.blog.csdn.net/article/details?id=41082063
     * 修改ScrollView的滑动速度和解决ScrollView与ViewPager的冲突
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        long preTime = System.nanoTime();
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            xDown = xLast = ev.getX();
            yLast = ev.getY();
            xDistance = yDistance = 0;
        } else if (ev.getAction() == MotionEvent.ACTION_MOVE) {
            final float xCurr = ev.getX();
            final float yCurr = ev.getY();
            xDistance += Math.abs(xCurr - xLast);
            yDistance += Math.abs(yCurr - yLast);
            xLast = xCurr;
            yLast = yCurr;

            if (k * xDistance < yDistance) {
                return false;
            }
        }
//        System.out.println("onInterceptTouchEvent: " + (System.nanoTime() - preTime) / 1000);
        return super.onInterceptTouchEvent(ev);
    }

    private final float xMinDistance = 100;//触发菜单切换的最小水平滑动距离

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        long preTime = System.nanoTime();
        switch (ev.getAction()) {

            case MotionEvent.ACTION_UP:
                float xDistance = ev.getX() - xDown;//大于0,向右滑。小于0，向左滑
                if (xDistance > xMinDistance) {
                    openMenu();
                } else if (xDistance < -xMinDistance) {
                    closeMenu();
                } else if (xDistance > 0 && !isMenuOpen) {
                    closeMenu();
                } else if (xDistance < 0 && isMenuOpen) {
                    openMenu();
                }
                return true;
        }
//        System.out.println("onTouchEvent: " + (System.nanoTime() - preTime) / 1000);
        return super.onTouchEvent(ev);
    }

    private void openMenu() {
        smoothScrollTo(0, 0);
        isMenuOpen = true;
    }

    private void closeMenu() {
        smoothScrollTo(mMenuWidth, 0);
        isMenuOpen = false;
    }

    public void toggleMenu() {
        if (isMenuOpen) {
            closeMenu();
        } else {
            openMenu();
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        long preTime = System.nanoTime();
        // 以下所有注释的规模变化都假设是拉出菜单的过程

        // scale从1到0
        float scale = l * 1f / mMenuWidth;

        if (onMenuSlidingListener != null) {
            onMenuSlidingListener.onMenuSliding(scale);
        }

        // 水平移位X应该从mMenuWidth到0
        mMenu.setTranslationX(mMenuWidth * scale);

        // 对菜单的透明度进行更改，从0到1(3次方增长)
        mMenu.setAlpha((1 - scale) * (1 - scale) * (1 - scale));

        // 对菜单的透明度进行更改，从0到255(3次方增长)
        // 参数： 0为完全透明，255为不透明
//        getBackground().setAlpha((int) ((1 - scale) * (1 - scale) * (1 - scale) * 255));

        // 对菜单进行放大，从0.5到1
        mMenu.setPivotX(-mMenuWidth / 2);
        mMenu.setPivotY(mMenu.getHeight() / 2);
        mMenu.setScaleX((float) (0.5 + 0.5 * (1 - scale)));
        mMenu.setScaleY((float) (0.5 + 0.5 * (1 - scale)));

        // 对内容区进行缩放，规模从1到0.7
        mContent.setPivotX(0);
        mContent.setPivotY(mContent.getHeight() / 2);
        mContent.setScaleX((float) (0.7 + 0.3 * scale));
        mContent.setScaleY((float) (0.7 + 0.3 * scale));
        
//        System.out.println("onScrollChanged: " + (System.nanoTime() - preTime) / 1000);
    }


}
