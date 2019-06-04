package com.example.asthanewsbeta2.Modules;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.asthanewsbeta2.FullBlog;
import com.example.asthanewsbeta2.R;
import com.example.asthanewsbeta2.Services.GoogleTranslate;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class FeaturePostAdapter extends RecyclerView.Adapter<FeaturePostAdapter.ViewHolder> {
    private List<GetPostFromLocal> listItems;
    private Context context;

    public FeaturePostAdapter(List<GetPostFromLocal> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.feature_post_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final GetPostFromLocal listItem = listItems.get(i);
        final String lng = MngData.getData(context, "language", "lng");

        viewHolder.title.setText(listItem.getTitle());

        Picasso.with(context).load(listItem.getImgUrl()).placeholder(R.drawable.ic_launcher_background).into(viewHolder.imageView);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, FullBlog.class).putExtra("id", listItem.getId()).putExtra("postCode", listItem.getPostCode()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, details, date, views;
        public ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.resTitle);
            imageView = itemView.findViewById(R.id.resImg);
        }
    }
}
