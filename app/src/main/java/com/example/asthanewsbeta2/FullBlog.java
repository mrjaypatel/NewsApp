package com.example.asthanewsbeta2;

import android.os.Handler;
import android.os.Looper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.asthanewsbeta2.Modules.FeaturePostAdapter;
import com.example.asthanewsbeta2.Modules.GetPost;
import com.example.asthanewsbeta2.Modules.GetPostFromLocal;
import com.example.asthanewsbeta2.Modules.MainFeedAdapter;
import com.example.asthanewsbeta2.Modules.MngData;
import com.example.asthanewsbeta2.OfflineDataManager.SQLHelper;
import com.example.asthanewsbeta2.Services.CacheRequest;
import com.example.asthanewsbeta2.Services.GoogleTranslate;
import com.example.asthanewsbeta2.Services.WebServiceProvider;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

public class FullBlog extends AppCompatActivity {
    private TextView postTitle, postDetails;
    private ImageView postImage;
    private GestureDetector gd;
    private RecyclerView featurePost;
    private List<GetPostFromLocal> postData;

    private ShimmerFrameLayout fullBlogPlace;
    private ShimmerFrameLayout featurePostPlace;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String id = getIntent().getStringExtra("id");//Get This data from main feed Intent
        String postCode = getIntent().getStringExtra("postCode");//Get This data from main feed Intent

        String API = "http://durgaplacements.com/Api/FullBlog.php?key=madHash456@@&id=" + id;
        String FPOST_API = "http://durgaplacements.com/Api/RecentPost.php?key=madHash456@@&postCode=" + postCode;
        setContentView(R.layout.activity_full_blog);


        fullBlogPlace = findViewById(R.id.fullBlogPlaceholder);
        featurePostPlace = findViewById(R.id.featurepostPlaceholder);

        postTitle = findViewById(R.id.postTitle);
        postDetails = findViewById(R.id.postDetails);
        postImage = findViewById(R.id.postImg);


        featurePost = findViewById(R.id.featurePost);
        featurePost.setHasFixedSize(true);
        featurePost.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));

        //loadSinglePost(API);
        String lng = MngData.getData(getApplicationContext(),"language", "lng");

        //Load Offline Post From SQLITE
        loadPost(Integer.parseInt(id),lng);

        //Load Offline Feature Post From SQLITE
        loadOfflineFeaturePost();


        //loadFeaturePost(FPOST_API);

    }

    private void loadPost(int id,String lng) {
        SQLHelper sh = new SQLHelper(getApplicationContext());
        String title = sh.getCellByPostId(2, id, lng);
        String details = sh.getCellByPostId(4, id, lng);
        final String imgUrl = sh.getCellByPostId(3,id, lng);
        //String data = MngData.mngString(getApplicationContext(), details);

        postTitle.setText(title);
        postDetails.setText(details);
        fullBlogPlace.setVisibility(View.GONE);
        Picasso.with(getApplicationContext()).load(imgUrl).networkPolicy(NetworkPolicy.OFFLINE).placeholder(R.drawable.ic_launcher_background).error(R.drawable.ic_launcher_foreground).into(postImage, new Callback() {
            @Override
            public void onSuccess() {
                Log.d("OUTPOT", "onSuccess: +++++++++++++ Image is offline Loaded");
            }

            @Override
            public void onError() {
                Log.d("OUTPOT", "onSuccess: +++++++++++++ Image From Online API");
                Picasso.with(getApplicationContext()).load(imgUrl).placeholder(R.drawable.ic_launcher_background).into(postImage);
            }
        });
        View fullBlog = findViewById(R.id.singlePost);
        fullBlog.setVisibility(View.VISIBLE);
    }


    private void loadOfflineFeaturePost() {
        postData = new ArrayList<>();
        SQLHelper sh = new SQLHelper(getApplicationContext());
        String lng = MngData.getData(getApplicationContext(), "language", "lng");
        String postCode = MngData.getData(getApplicationContext(), "postCode", "code");
        postData = sh.getOffPostList(postCode, lng);

        FeaturePostAdapter fp = new FeaturePostAdapter(postData, getApplicationContext());
        featurePost.setAdapter(fp);

        featurePostPlace.setVisibility(View.GONE);
        View featurePost = findViewById(R.id.featurePosts);
        featurePost.setVisibility(View.VISIBLE);
    }


/*

    private void loadFeaturePost(final String fpost_api) {
        final List<GetPost> postData = new ArrayList<>();
        CacheRequest cacheRequest = new CacheRequest(0, fpost_api, new Response.Listener<NetworkResponse>() {
            @Override
            public void onResponse(NetworkResponse response) {
                try {
                    final String jsonString = new String(response.data,
                            HttpHeaderParser.parseCharset(response.headers));
                    try {
                        JSONObject jo = new JSONObject(jsonString);
                        JSONArray array = jo.getJSONArray("mainfeed");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject o = array.getJSONObject(i);
                            GetPost item = new GetPost(
                                    o.getString("id"),
                                    o.getString("title"),
                                    o.getString("imgUrl"),
                                    o.getString("details"),
                                    o.getString("date"),
                                    o.getString("views"),
                                    o.getString("postcode")
                            );
                            postData.add(item);
                        }
                        FeaturePostAdapter fp = new FeaturePostAdapter(postData, getApplicationContext());
                        featurePost.setAdapter(fp);

                        featurePostPlace.setVisibility(View.GONE);
                        View featurePost = findViewById(R.id.featurePosts);
                        featurePost.setVisibility(View.VISIBLE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        WebServiceProvider.getInstace(getApplicationContext()).addToRequestQueue(cacheRequest);


    }
*/


    @Override
    protected void onResume() {
        super.onResume();

        fullBlogPlace.startShimmer();
        featurePostPlace.startShimmer();
    }

    @Override
    protected void onPause() {
        super.onPause();
        fullBlogPlace.stopShimmer();
        featurePostPlace.stopShimmer();
    }
}
