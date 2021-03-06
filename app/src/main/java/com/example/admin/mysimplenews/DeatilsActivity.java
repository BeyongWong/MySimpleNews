package com.example.admin.mysimplenews;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.jaeger.library.StatusBarUtil;


import org.json.JSONException;
import org.json.JSONObject;

public class DeatilsActivity extends AppCompatActivity {

    private android.widget.ImageView iv;
    private android.support.v7.widget.Toolbar toolbar;
    private android.support.design.widget.CollapsingToolbarLayout collpaseBar;
    private TextView tv;
    private android.support.design.widget.CoordinatorLayout activitydeatils;
    private String image;
    private String title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deatils);

        this.activitydeatils = (CoordinatorLayout) findViewById(R.id.activity_deatils);
        this.tv = (TextView) findViewById(R.id.tv);
        this.collpaseBar = (CollapsingToolbarLayout) findViewById(R.id.collpaseBar);
        this.toolbar = (Toolbar) findViewById(R.id.toolbar);
        this.iv = (ImageView) findViewById(R.id.iv);
        collpaseBar.setExpandedTitleColor(Color.WHITE);
        collpaseBar.setCollapsedTitleTextColor(Color.WHITE);
        toolbar.setNavigationIcon(R.mipmap.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        Intent intent = getIntent();
        String url = intent.getStringExtra("url");
        String postId = intent.getStringExtra("postId");
        image = intent.getStringExtra("image");
        title = intent.getStringExtra("title");
        loadData(url, postId);
    }

    private void loadData(String url, final String postId) {
        HttpUtils.getInstance().loadGetData(url, new HttpUtils.OnLoadDataListener() {
            @Override
            public void success(String con) {
                try {
                    JSONObject jsonObject = new JSONObject(con).getJSONObject(postId);
//                    String title = jsonObject.getString("title");
                    String body = jsonObject.getString("body");
//                    String imagePath = deatailsBean.getC5E28H0I0008856R().getImg().get(0).getSrc();
                    Glide.with(DeatilsActivity.this).load(image).asBitmap().into(new SimpleTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                            iv.setImageBitmap(resource);
                            Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                                @Override
                                public void onGenerated(Palette palette) {
                                    int darkVibrantColor = palette.getDarkVibrantColor(Color.parseColor("#3F51B5"));
                                    int lightMutedColor = palette.getLightMutedColor(getResources().getColor(R.color.colorPrimaryDark));
                                    StatusBar.setColor(DeatilsActivity.this,lightMutedColor);
//                                    StatusBarUtil.setColor(DeatilsActivity.this,lightMutedColor);
                                    collpaseBar.setContentScrimColor(darkVibrantColor);
                                }
                            });
                        }
                    });

                    collpaseBar.setTitle(title);
//                    tv.loadData(Html.fromHtml(body).toString(),"text/html","UTF-8");
//                    TextView tvl;
                    tv.setText(Html.fromHtml(body));

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void faild(String msg) {
                Toast.makeText(DeatilsActivity.this, "请求失败", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
