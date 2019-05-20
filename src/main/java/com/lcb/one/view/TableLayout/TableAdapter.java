package com.lcb.one.view.TableLayout;

/**
 * Description:自定义Tablelayout的适配器
 * AUTHOR: Champion Dragon
 * created at 2019/5/10
 **/
public interface TableAdapter {
    int getColumnCount();
    String[] getColumnContent(int position);
}

