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

public class MainFeedAdapter extends RecyclerView.Adapter<MainFeedAdapter.ViewHolder> {
    private List<GetPost> listItems;
    private Context context;

    public MainFeedAdapter(List<GetPost> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.main_feed_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final GetPost listItem = listItems.get(i);

        final String lng = MngData.getData(context, "language", "lng");
        new Thread(new Runnable() {
            @Override
            public void run() {
                final String title;
                try {
                    title = GoogleTranslate.translate(lng,listItem.getTitle());

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.title.setText(title);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        //viewHolder.details.setText(listItem.getDetails());
        viewHolder.date.setText(listItem.getDate());
        viewHolder.views.setText(listItem.getViews());
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
       public TextView title ,details, date, views;
        public ImageView imageView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            title.setText("");
           // details = itemView.findViewById(R.id.details);
            date = itemView.findViewById(R.id.date);
            views = itemView.findViewById(R.id.views);
            imageView =itemView.findViewById(R.id.imgView);
        }
    }
}
