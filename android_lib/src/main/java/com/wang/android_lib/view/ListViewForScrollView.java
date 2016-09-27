package com.wang.android_lib.view;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * by 王荣俊 on 2016/9/1.
 * <p/>
 * http://bbs.anzhuo.cn/thread-982250-1-1.html 四种方案解决ScrollView嵌套ListView问题
 * <p/>
 * 默认显示的首项是ListView，需要手动把ScrollView滚动至最顶端：
 * scrollView.smoothScrollTo(0, 0)
 */
public class ListViewForScrollView extends ListView {

    public ListViewForScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }

}
