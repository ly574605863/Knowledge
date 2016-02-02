package com.dante.knowledge.news.other;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.dante.knowledge.R;
import com.dante.knowledge.news.model.LatestNews;
import com.dante.knowledge.news.model.StoryEntity;
import com.dante.knowledge.news.model.TopStoryEntity;
import com.dante.knowledge.news.view.NetworkImageHolderView;
import com.dante.knowledge.utils.ImageUtil;
import com.dante.knowledge.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;


public class NewsListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_BANNER = 0;
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 2;
    private static final int TYPE_FOOTER = 3;
    public static final String STORY = "Story";
    private List<StoryEntity> storyEntities = new ArrayList<>();
    private List<TopStoryEntity> topEntities = new ArrayList<>();
    private final OnListFragmentInteractionListener mListener;
    private boolean hasFooter;
    private boolean showHeader;
    private LatestNews news;
    List<StoryEntity> newStories;
    private int lastStoriesSize;
    public boolean isShowHeader() {
        return showHeader;
    }

    public void setShowHeader(boolean showHeader) {
        this.showHeader = showHeader;
    }

    public boolean isHasFooter() {
        return hasFooter;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
        notifyDataSetChanged();
    }

    public NewsListAdapter(OnListFragmentInteractionListener listener) {
        mListener = listener;
    }

    public void addNews(LatestNews news) {
        this.news = news;
        newStories = news.getStories();

        if (null!=news.getTop_stories()){
            topEntities.clear();
            topEntities.addAll(news.getTop_stories());
        }
        setupHeaderFooter();
        storyEntities.addAll(newStories);
        notifyDataSetChanged();
    }

    private void setupHeaderFooter() {
        if (showHeader){
            StoryEntity entity=new StoryEntity();
            entity.setId(1);
            newStories.add(0, entity);
        }
        setHasFooter(newStories.size() != 0);

    }

    public void clear() {
        showHeader = false;
        storyEntities.clear();
    }

    public void scrollToTop(){

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == TYPE_BANNER) {
            View view = inflater.inflate(R.layout.fragment_news_banner, parent, false);
            return new BannerViewHolder(view);

        } else if (viewType ==TYPE_HEADER){
            View view = inflater.inflate(R.layout.fragment_news_header, parent, false);
            return new HeaderViewHolder(view);

        } else if (viewType == TYPE_FOOTER) {
            View view = inflater.inflate(R.layout.footer_loading, parent, false);
            return new FooterViewHolder(view);

        } else {
            View view = inflater.inflate(R.layout.fragment_news_item, parent, false);
            return new ViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ViewHolder) {
            final ViewHolder itemHolder = (ViewHolder) holder;

            if (position==1){
                itemHolder.header.setVisibility(View.VISIBLE);
                itemHolder.mItem.setVisibility(View.GONE);
                return;
            }else {
                itemHolder.story = storyEntities.get(position - 2);//这里position=0,1时，被banner,header占用了
                if (itemHolder.story.getId()==1){
                    String date= StringUtil.parseDate(news.getDate());
                    itemHolder.header.setText(date);
                    itemHolder.header.setVisibility(View.VISIBLE);
                    itemHolder.mItem.setVisibility(View.GONE);
                    return;
                }else {
                    itemHolder.header.setVisibility(View.GONE);
                    itemHolder.mItem.setVisibility(View.VISIBLE);
                }
            }

            showHeader=true;
            ImageUtil.load(itemHolder.itemView.getContext(), itemHolder.story.getImages().get(0), itemHolder.mImage);
            itemHolder.mTitle.setText(itemHolder.story.getTitle());
            itemHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(itemHolder);
                    }

                }
            });
        } else if (holder instanceof BannerViewHolder) {
            final BannerViewHolder itemHolder = (BannerViewHolder) holder;
            itemHolder.banner.setPages(new CBViewHolderCreator<NetworkImageHolderView>() {
                @Override
                public NetworkImageHolderView createHolder() {
                    return new NetworkImageHolderView();
                }
            }, topEntities);
            mListener.onTopLoad();
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        }  else if (hasFooter && position ==  storyEntities.size() + 1) {
            //因为第一个position（0）是banner，所以count要减去一个
            //就是footer应该出现的位置
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        //story数量+banner+header+footer
        if (hasFooter) {
            return storyEntities.size() + 2;
        }
        return storyEntities.size() + 1;
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {

        public FooterViewHolder(View view) {
            super(view);
        }

    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder{
        public final TextView headerText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerText = (TextView) itemView.findViewById(R.id.story_header);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public final ConvenientBanner banner;

        public BannerViewHolder(View view) {
            super(view);
            banner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
        }

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView header;
        public final ImageView mImage;
        public final TextView mTitle;
        public final View  mItem;
        public StoryEntity story;

        public ViewHolder(View view) {
            super(view);
            header = (TextView) view.findViewById(R.id.story_header);
            mImage = (ImageView) view.findViewById(R.id.story_img);
            mTitle = (TextView) view.findViewById(R.id.story_title);
            mItem=view.findViewById(R.id.story_item);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
