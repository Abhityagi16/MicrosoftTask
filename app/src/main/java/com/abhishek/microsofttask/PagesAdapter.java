package com.abhishek.microsofttask;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by abhishek.tyagi1 on 17/06/16.
 */
public class PagesAdapter extends RecyclerView.Adapter<PagesAdapter.PageViewHolder>{
    private ArrayList<Page> mPages;
    private Context mContext;

    private int lastPosition = -1;

    public PagesAdapter(Context context) {
        this.mContext = context;
    }


    @Override
    public PageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout,
                parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PageViewHolder holder, int position) {
        String imageUrl = mPages.get(position).getImageUrl();
        if(imageUrl == null) {
            Picasso.with(mContext).load(R.mipmap.wiki_placeholder).into(holder.pageImage);
        }
        else {
            Picasso.with(mContext).load(imageUrl).into(holder.pageImage);
        }
        setAnimation(holder.rootView, position);
    }

    public void setPageList(ArrayList<Page> pages) {
        this.mPages = pages;
    }

    @Override
    public int getItemCount() {
        if(mPages == null) return 0;
        return mPages.size();
    }

    @Override
    public void onViewDetachedFromWindow(PageViewHolder holder) {
        holder.clearAnimation();
    }


    class PageViewHolder extends RecyclerView.ViewHolder {
        public ImageView pageImage;
        public View rootView;

        public PageViewHolder(View itemView) {
            super(itemView);
            rootView = itemView;
            pageImage = (ImageView) itemView.findViewById(R.id.page_image);
        }

        public void clearAnimation()
        {
            rootView.clearAnimation();
        }
    }

    private void setAnimation(View viewToAnimate, int position)
    {
        if (position > lastPosition)
        {

            Animation animation;
            if(position%2 == 0) {
                animation = AnimationUtils.loadAnimation(mContext, android.R.anim.slide_in_left);
            }
            else animation = AnimationUtils.loadAnimation(mContext, R.anim.slide_in_right);

            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}
