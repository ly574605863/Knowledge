package com.dante.knowledge.mvp.view;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dante.knowledge.R;
import com.dante.knowledge.mvp.interf.OnListFragmentInteract;
import com.dante.knowledge.mvp.model.HItem;
import com.dante.knowledge.net.DB;
import com.dante.knowledge.ui.BaseActivity;

import java.util.List;

/**
 * Created by yons on 16/3/1.
 */
public class HPostAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<HItem> items;
    private OnListFragmentInteract mListener;

    public HPostAdapter(OnListFragmentInteract listener, BaseActivity activity) {
        mListener = listener;
        items = DB.findAllDateSorted(activity.mRealm, HItem.class);
    }

    public void addNews() {
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_h_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.hItem = items.get(position);

//        viewHolder.mTitle.setText(viewHolder.hItem.getTitle());
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    mListener.onListFragmentInteraction(viewHolder);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final TextView mTitle;
        public final View mItem;
        public HItem hItem;

        public ViewHolder(View view) {
            super(view);
            mTitle = (TextView) view.findViewById(R.id.news_title);
            mItem = view.findViewById(R.id.news_item);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mTitle.getText() + "'";
        }
    }
}
