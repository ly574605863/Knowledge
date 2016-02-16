package com.dante.knowledge.news.view;


import android.support.v7.widget.RecyclerView;

import com.dante.knowledge.ui.BaseFragment;

/**
 * All fragments have recyclerView must implement this.
 */
public abstract class RecyclerFragment extends BaseFragment {
    //    @Bind(R.id.list)
//    RecyclerView recyclerView;
//    @Bind(R.id.swipe_refresh)
//    SwipeRefreshLayout swipeRefresh;
    abstract RecyclerView getRecyclerView();
//    public RecyclerView getRecyclerView() {
//        return recyclerView;
//    }
}
