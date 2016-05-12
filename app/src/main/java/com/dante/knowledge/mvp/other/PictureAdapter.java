package com.dante.knowledge.mvp.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dante.knowledge.R;
import com.dante.knowledge.libraries.ArrayRecyclerAdapter;
import com.dante.knowledge.libraries.RatioImageView;
import com.dante.knowledge.mvp.model.Image;
import com.dante.knowledge.utils.Imager;

/**
 * Adapt image data to pictures waterfall
 */
public abstract class PictureAdapter extends ArrayRecyclerAdapter<Image, PictureAdapter.ViewHolder> {

    private Context context;

    public PictureAdapter(Context context) {
        this.context = context;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return get(position).hashCode();
    }

    protected abstract void onItemClick(View v, int position);

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.picture_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Image image = get(position);
        holder.imageView.setOriginalSize(image.getWidth(), image.getHeight());
        Imager.load(holder.itemView.getContext(), image.getUrl(), holder.imageView);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public RatioImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (RatioImageView) itemView.findViewById(R.id.picture);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onItemClick(view, getAdapterPosition());
                }
            });
        }
    }
}
