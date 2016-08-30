package com.dante.knowledge.mvp.other;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.ConvenientBanner;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.OnListFragmentInteract;
import com.dante.knowledge.mvp.model.ZhihuJson;
import com.dante.knowledge.mvp.model.ZhihuStory;
import com.dante.knowledge.mvp.model.ZhihuTop;
import com.dante.knowledge.mvp.view.BannerView;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;
import com.dante.knowledge.utils.DateUtil;
import com.dante.knowledge.utils.Imager;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Zhihu news' recyclerView adapter
 */

public class ZhihuListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements RealmChangeListener {
    private static final int TYPE_BANNER = 0;
    /**
     * header is a title to display date
     */
    private static final int TYPE_HEADER = 1;
    private static final int TYPE_ITEM = 2;
    /**
     * footer is to show load more hint
     */
    private static final int TYPE_FOOTER = 3;
    public static int textGrey;
    public static int textDark;
    public ConvenientBanner<ZhihuTop> banner;
    private RealmResults<ZhihuJson> zhihuJson;
    private List<ZhihuStory> zhihuStories;
    private List<ZhihuTop> tops;
    private Realm mRealm;
    private OnListFragmentInteract mListener;

    public ZhihuListAdapter(OnListFragmentInteract listener, BaseActivity activity) {
        mListener = listener;
        mRealm = activity.mRealm;
        zhihuJson = DB.findAllDateSorted(mRealm, ZhihuJson.class);
        zhihuStories = new ArrayList<>();
        for (int i = 0; i < zhihuJson.size(); i++) {
            zhihuStories.addAll(zhihuJson.get(i).getStories());
        }
        //// TODO: 16/8/30 排序问题 
//        zhihuStories = DB.findAll(mRealm, ZhihuStory.class);
        tops = DB.findAll(mRealm, ZhihuTop.class);
        mRealm.addChangeListener(this);
    }

    public void addNews(ZhihuJson news) {
        notifyDataSetChanged();
    }

    public void clear() {
        banner.stopTurning();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == TYPE_BANNER) {
            View view = inflater.inflate(R.layout.fragment_news_banner, parent, false);
            return new BannerViewHolder(view);
        } else if (viewType == TYPE_HEADER) {
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
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        Context context = holder.itemView.getContext();
        textGrey = ContextCompat.getColor(context, R.color.darker_gray);
        textDark = ContextCompat.getColor(context, android.support.design.R.color.abc_primary_text_material_light);

        if (holder instanceof ViewHolder) {
            final ViewHolder viewHolder = (ViewHolder) holder;
            if (position == 1) {
                viewHolder.header.setText(context.getString(R.string.hottest));
                viewHolder.header.setVisibility(View.VISIBLE);
                viewHolder.mItem.setVisibility(View.GONE);
                viewHolder.mItem.setClickable(false);
                return;
            } else {
                viewHolder.zhihuStory = zhihuStories.get(position - 2);//position=0, 1 are occupied with banner, header
                //type == 1 means this item is added by me, so it's a header to show date.
                if (viewHolder.zhihuStory.getType() == 1) {
                    String date = DateUtil.getDisplayDate(viewHolder.zhihuStory.getId() + "");
                    viewHolder.header.setText(date);
                    viewHolder.header.setVisibility(View.VISIBLE);
                    viewHolder.header.setClickable(false);
                    viewHolder.mItem.setVisibility(View.GONE);
                    return;
                } else {
                    viewHolder.header.setVisibility(View.GONE);
                    viewHolder.mItem.setVisibility(View.VISIBLE);
                }
            }

            Imager.load(context, viewHolder.zhihuStory.getImages().get(0).getVal(), viewHolder.mImage);
            viewHolder.mTitle.setText(viewHolder.zhihuStory.getTitle());
            viewHolder.mTitle.setTextColor(textDark);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (null != mListener) {
                        mListener.onListFragmentInteraction(viewHolder);
                    }

                }
            });

        } else if (holder instanceof BannerViewHolder) {
            final BannerViewHolder itemHolder = (BannerViewHolder) holder;
            itemHolder.banner.setPages(new CBViewHolderCreator<BannerView>() {
                @Override
                public BannerView createHolder() {
                    return new BannerView();
                }
            }, tops);
            banner = itemHolder.banner;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_BANNER;
        } else if (position == zhihuStories.size() + 1) {
            // position 0 is banner so
            // the footer appears the size+1 position
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        //items + banner + footer
        return zhihuStories.size() + 2;
    }

    @Override
    public void onChange() {
        if (null != banner) {
            banner.notifyDataSetChanged();
        }
        zhihuStories.clear();
        zhihuJson = DB.findAllDateSorted(mRealm, ZhihuJson.class);
        for (int i = 0; i < zhihuJson.size(); i++) {
            zhihuStories.addAll(zhihuJson.get(i).getStories());
        }
        notifyDataSetChanged();
    }

    public class FooterViewHolder extends RecyclerView.ViewHolder {
        public FooterViewHolder(View view) {
            super(view);
        }
    }

    public class HeaderViewHolder extends RecyclerView.ViewHolder {
        public final TextView headerText;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            headerText = (TextView) itemView.findViewById(R.id.story_header);
        }
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        public final ConvenientBanner<ZhihuTop> banner;

        public BannerViewHolder(View view) {
            super(view);
            banner = (ConvenientBanner) view.findViewById(R.id.convenientBanner);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView header;
        public final ImageView mImage;
        public final TextView mTitle;
        public final View mItem;
        public ZhihuStory zhihuStory;

        public ViewHolder(View view) {
            super(view);
            header = (TextView) view.findViewById(R.id.story_header);
            mImage = (ImageView) view.findViewById(R.id.story_img);
            mTitle = (TextView) view.findViewById(R.id.news_title);
            mItem = view.findViewById(R.id.news_item);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
