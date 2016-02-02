package com.dante.knowledge.news.view;

import com.dante.knowledge.R;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.dante.knowledge.news.model.TopStoryEntity;
import com.dante.knowledge.news.other.NewsListAdapter;
import com.dante.knowledge.utils.ImageUtil;

public class NetworkImageHolderView implements Holder<TopStoryEntity> {
    private View view;

    @Override
    public View createView(Context context) {
        view= LayoutInflater.from(context).inflate(R.layout.card_item_big, null);
        return view;
    }

    @Override
    public void UpdateUI(final Context context, int position, final TopStoryEntity entity) {
        ImageView imageView = (ImageView) view.findViewById(R.id.story_img);
        TextView textView = (TextView) view.findViewById(R.id.story_title);
        ImageUtil.load(context, entity.getImage(), imageView);
        textView.setText(entity.getTitle());
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, NewsDetailActivity.class);
                intent.putExtra(NewsListAdapter.STORY, entity);
                context.startActivity(intent);
            }
        });

    }
}