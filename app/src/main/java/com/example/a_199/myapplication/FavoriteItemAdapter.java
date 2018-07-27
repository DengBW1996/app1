package com.example.a_199.myapplication;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by a_199 on 2018/7/26.
 */

public class FavoriteItemAdapter extends RecyclerView.Adapter<FavoriteItemAdapter.ViewHolder> {
    private Context mContext;

    private List<FavoriteItem> mFavoriteItemList;

    static class ViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        ImageView favoriteItemImage;
        TextView favoriteItemName;

        public ViewHolder(View view){
            super(view);
            cardView = (CardView) view;
            favoriteItemImage = (ImageView) view.findViewById(R.id.favorite_image);
            favoriteItemName = (TextView) view.findViewById(R.id.favorite_name);
        }
    }

    public FavoriteItemAdapter(List<FavoriteItem> favoriteItemList){
        mFavoriteItemList = favoriteItemList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        if (mContext == null){
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.favorite_item,
                parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FavoriteItem favoriteItem = mFavoriteItemList.get(position);
        holder.favoriteItemName.setText(favoriteItem.getName());
        Glide.with(mContext).load(favoriteItem.getImageId()).into(holder.favoriteItemImage);
    }

    @Override
    public int getItemCount() {
        return mFavoriteItemList.size();
    }
}
