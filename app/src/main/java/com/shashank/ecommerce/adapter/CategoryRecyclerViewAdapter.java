package com.shashank.ecommerce.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.shashank.ecommerce.R;

import java.util.ArrayList;

public class CategoryRecyclerViewAdapter extends RecyclerView.Adapter<CategoryRecyclerViewAdapter.ViewHolder> {

    private static final String TAG = "CategoryRecyclerViewAda";
    private ArrayList<String> categoryNames = new ArrayList<>();
    private ArrayList<String> categoryImageUrl = new ArrayList<>();
    private Context mContext;

    public CategoryRecyclerViewAdapter(Context mContext, ArrayList<String> categoryNames, ArrayList<String> categoryImageUrl) {
        this.categoryNames = categoryNames;
        this.categoryImageUrl = categoryImageUrl;
        this.mContext = mContext;
     }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_category,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Log.d(TAG, "onBindViewHolder:Called ");
        RequestOptions requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_launcher_background);

        Glide.with(mContext)
                .load(categoryImageUrl.get(position))
                .apply(requestOptions)
                .into(viewHolder.categoryImage);

        viewHolder.CategoryName.setText(categoryNames.get(position));
    }

    @Override
    public int getItemCount() {
        return categoryImageUrl.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView categoryImage;
        TextView CategoryName;

        public ViewHolder(View itemView)
        {
            super(itemView);
            this.categoryImage = itemView.findViewById(R.id.category_image);
            this.CategoryName = itemView.findViewById(R.id.category_name);
        }
    }
}
