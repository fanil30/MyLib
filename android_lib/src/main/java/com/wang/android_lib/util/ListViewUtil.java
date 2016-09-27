package com.wang.android_lib.util;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ListAdapter;
import android.widget.ListView;

/**
 * by 王荣俊 on 2016/6/7.
 */
public class ListViewUtil {
    /**
     * http://blog.csdn.net/wulianghuan/article/details/8627958
     * 解决ScrollView中嵌套ListView滚动效果冲突问题
     * http://bbs.anzhuo.cn/thread-982250-1-1.html
     * 四种方案解决ScrollView嵌套ListView问题
     * <p/>
     * 重新计算ListView高度，解决ScrollView和ListView两个View都有滚动的效果，在嵌套使用时冲突的问题
     * 注意1：子ListView的每个Item必须是LinearLayout，不能是其他的，
     * 因为其他的Layout(如RelativeLayout)没有重写onMeasure()，所以会在onMeasure()时抛出异常。
     * 注意2：有时会失效，如EBook的BookInfoActivity就失效了，原因不明。
     */
    public static void setListViewHeight(ListView listView) {

        // 获取ListView对应的Adapter    
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0, len = listAdapter.getCount(); i < len; i++) { // listAdapter.getCount()返回数据项的数目    
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0); // 计算子项View 的宽高    
            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度    
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 记录当前可见的List顶端的一行的位置
     */
    public static int scrollPosition;
    /**
     * 记录当前可见的List顶端的一行在ListView顶端的位置
     */
    public static int scrollTop;

    /**
     * 初始化时调用，为listView设置监听滑动位置的监听器，在listView.setAdapter后使用
     * http://www.xuebuyuan.com/613381.html 精确记录和恢复ListView滑动位置
     */
    public static void setRecordScrollPositionListener(final ListView listView) {
        listView.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == AbsListView.OnScrollListener.SCROLL_STATE_IDLE) {
                    // scrollPos记录当前可见的List顶端的一行的位置 
                    scrollPosition = listView.getFirstVisiblePosition();
                }

                if (listView.getAdapter() != null) {//若listView已加载adapter
                    View v = listView.getChildAt(0);
                    scrollTop = v == null ? 0 : v.getTop();//若listView至少有1项，则getTop，否则0
                }

                System.out.println("scrollPosition=" + scrollPosition + "   scrollTop=" + scrollTop);

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                                 int totalItemCount) {
            }

        });

    }

    /**
     * 用法：在adapter.notifyDataSetChanged() 之后调用，恢复位置。
     * <p/>
     * 前提是先在listView初始化时已调用setRecordScrollPositionListener()。
     */
    public static void recoverScrollPosition(ListView listView) {
        listView.setSelectionFromTop(scrollPosition, scrollTop);
    }

}
