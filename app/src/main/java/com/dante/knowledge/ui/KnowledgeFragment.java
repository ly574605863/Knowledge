package com.dante.knowledge.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.dante.knowledge.R;
import com.dante.knowledge.bean.StoriesEntity;
import com.dante.knowledge.bean.TopStoriesEntity;
import com.dante.knowledge.news.impl.NewsPresenterImpl;
import com.dante.knowledge.news.inter.NewsPresenter;
import com.dante.knowledge.news.inter.NewsView;
import com.dante.knowledge.ui.dummy.DummyContent;
import com.dante.knowledge.ui.dummy.DummyContent.DummyItem;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;



public class KnowledgeFragment extends BaseFragment implements NewsView, SwipeRefreshLayout.OnRefreshListener {
    @Bind(R.id.swipe_refresh)
    SwipeRefreshLayout swipeRefresh;
    private List<String> networkImages;
    private String[] images = {"http://img2.imgtn.bdimg.com/it/u=3093785514,1341050958&fm=21&gp=0.jpg",
            "http://img2.3lian.com/2014/f2/37/d/40.jpg",
            "http://d.3987.com/sqmy_131219/001.jpg",
            "http://img2.3lian.com/2014/f2/37/d/39.jpg",
            "http://www.8kmm.com/UploadFiles/2012/8/201208140920132659.jpg",
            "http://f.hiphotos.baidu.com/image/h%3D200/sign=1478eb74d5a20cf45990f9df460b4b0c/d058ccbf6c81800a5422e5fdb43533fa838b4779.jpg",
            "http://f.hiphotos.baidu.com/image/pic/item/09fa513d269759ee50f1971ab6fb43166c22dfba.jpg"
    };
    @Bind(R.id.convenientBanner)
    ConvenientBanner convenientBanner;
    @Bind(R.id.list)
    RecyclerView mRecyclerView;

    private OnListFragmentInteractionListener mListener;
    private NewsPresenter mNewsPresenter;

    @Override
    protected void initData() {
        networkImages = Arrays.asList(images);
        convenientBanner.setScrollDuration(500);
        convenientBanner.startTurning(5000);
        convenientBanner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
            @Override
            public NetworkImageHolderView createHolder() {
                return new NetworkImageHolderView();
            }
        }, networkImages);

        mNewsPresenter = new NewsPresenterImpl(this);
        GetData();
    }

    private void GetData() {

    }

    @Override
    protected void initLayoutId() {
        layoutId = R.layout.fragment_knowledge_list;
    }

    @Override
    protected void initViews() {
        Context context = rootView.getContext();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        mRecyclerView.setAdapter(new KnowledgeRecyclerViewAdapter(DummyContent.ITEMS, mListener));
        swipeRefresh.setColorSchemeColors(R.color.colorPrimary,
                R.color.colorPrimaryDark, R.color.colorAccent);
        swipeRefresh.setOnRefreshListener(this);

    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnListFragmentInteractionListener) {
            mListener = (OnListFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void showProgress() {
        
    }

    @Override
    public void setTop(List<TopStoriesEntity> storiesEntities) {

    }

    @Override
    public void addNews(List<StoriesEntity> storiesEntities) {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void showLoadFailed(String msg) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onRefresh() {

    }


    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(DummyItem item);
    }
}
