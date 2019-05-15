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
import android.widget.Button;

import com.example.asthanewsbeta2.MainActivity;
import com.example.asthanewsbeta2.R;
import com.example.asthanewsbeta2.Services.GoogleTranslate;

import java.io.IOException;
import java.util.List;

public class MenuItemAdapter extends RecyclerView.Adapter<MenuItemAdapter.ViewHolder> {
    private List<GetMenu> listItems;
    private Context context;

    public MenuItemAdapter(List<GetMenu> listItems, Context context) {
        this.listItems = listItems;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.menu_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {
        final GetMenu listItem = listItems.get(i);
        final String lng = MngData.getData(context, "language", "lng");


        new Thread(new Runnable() {
            @Override
            public void run() {
                final String title;
                try {
                    title = GoogleTranslate.translate(lng, listItem.getTitle());

                    new Handler(Looper.getMainLooper()).post(new Runnable() {
                        @Override
                        public void run() {
                            viewHolder.menuBtn.setText(title);
                        }
                    });
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();

        viewHolder.menuBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String cat = listItem.getCat();
                MainActivity.API_URL = "http://durgaplacements.com/Api/mainFeedApi.php?key=madHash456@@&postCode=" + cat;
                context.startActivity(new Intent(context, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));

            }
        });
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public Button menuBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            menuBtn = itemView.findViewById(R.id.menuBtn);

        }
    }
}
