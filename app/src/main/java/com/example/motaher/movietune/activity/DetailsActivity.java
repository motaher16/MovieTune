package com.example.motaher.movietune.activity;
import android.content.pm.ActivityInfo;

import android.graphics.drawable.Drawable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.motaher.movietune.R;
import com.example.motaher.movietune.adapter.MovieDetailsAdapter;
import com.example.motaher.movietune.global.CommonData;
import com.example.motaher.movietune.helper.ItemClickNotifier;
import com.example.motaher.movietune.helper.PreferenceHelper;
import com.example.motaher.movietune.model.MovieInfo;
import com.example.motaher.movietune.model.SingleMovieInfo;
import com.example.motaher.movietune.webService.ApiCommunicator;
import com.example.motaher.movietune.webService.ApiDetails;
import com.example.motaher.movietune.webService.ApiResponse;
import com.example.motaher.movietune.webService.CommonService;
import com.google.gson.Gson;

public class DetailsActivity extends AppCompatActivity implements ApiResponse, ItemClickNotifier {

    Integer movie_id;
    PreferenceHelper preferenceHelper;
    ApiCommunicator apiCommunicator;
    Integer returned_data;

    SingleMovieInfo singleMovieInfo;
    MovieInfo similarMovieInfo;
    CommonService commonService;


    private Toolbar mToolbar;
    private CollapsingToolbarLayout mCollapsingToolbar;
    private AppBarLayout mAppBarLayout;
    ImageView headerImage;
    private RecyclerView mRecylerView;
    private MovieDetailsAdapter movieDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            movie_id = extras.getInt("id");
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupToolBar();
        initSetup();


        if(commonService.isNetworkAvailable()){
            buildAndLoadDataFromURLS(movie_id);
        }
        else {
            commonService.popupAlertDialog("Please enable your internet connection!");
        }
    }


    private void setupToolBar(){

        mToolbar = (Toolbar) findViewById(R.id.detail_anim_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });


        mCollapsingToolbar = (CollapsingToolbarLayout) findViewById(R.id.detail_collapsing_toolbar);


        mAppBarLayout = (AppBarLayout) findViewById(R.id.detail_appbar);
        mAppBarLayout.addOnOffsetChangedListener(new   AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

            }
        });
    }

    private void initSetup(){
        headerImage = (ImageView) findViewById(R.id.detail_headerImage);
        preferenceHelper = new PreferenceHelper(this);
        apiCommunicator = new ApiCommunicator(this);
        commonService = new CommonService(this);
        returned_data = 0;
        mRecylerView = (RecyclerView) findViewById(R.id.detail_recycleview);
        mRecylerView.setLayoutManager(new LinearLayoutManager(this));
        mRecylerView.setItemAnimator(new DefaultItemAnimator());

    }

    private void setupBanner(String banner_path){

        String url = ApiDetails.IMAGE_BASE_URL + banner_path + "?api_key=" + CommonData.API_KEY;
        Drawable currentBanner = headerImage.getDrawable();
        Glide
                .with(getApplicationContext())
                .load(url)
                .placeholder(currentBanner)
                .crossFade()
                .into(headerImage);
    }

    private void buildAndLoadDataFromURLS(int movieId){

        String movie_details = ApiDetails.BASE_URL + movieId + "?api_key=" + CommonData.API_KEY;
        String similar_movie_list = ApiDetails.BASE_URL + movieId + ApiDetails.GET_SIMILAR_MOVIES_URL + "api_key=" + CommonData.API_KEY;
        loadMovieDetails(movie_details);
        loadSimilarMovies(similar_movie_list);
    }

    private void loadMovieDetails(String url){
        apiCommunicator.getDataByGET(url, CommonData.FIRST_METHOD, false);
    }


    private void loadSimilarMovies(String url){
        apiCommunicator.getDataByGET(url, CommonData.SECOND_METHOD, false);
    }

    private void getMovieDetails(String result){
        Gson gson = new Gson();
        singleMovieInfo = gson.fromJson(result, SingleMovieInfo.class);
        String backdrop_path = singleMovieInfo.getBackdropPath();
        setupBanner(backdrop_path);
        movieDetailsAdapter = new MovieDetailsAdapter(this, R.layout.movie_info_card, singleMovieInfo, similarMovieInfo);
        mRecylerView.setAdapter(movieDetailsAdapter);
    }


    private void getSimilarMovies(String result){

        Gson gson = new Gson();
        similarMovieInfo = gson.fromJson(result, MovieInfo.class);
        movieDetailsAdapter = new MovieDetailsAdapter(this, R.layout.movie_info_card, singleMovieInfo, similarMovieInfo);
        mRecylerView.setAdapter(movieDetailsAdapter);
    }



    @Override
    public void taskCompleted(String result, int methodTag) {

        if(methodTag == CommonData.FIRST_METHOD){
            getMovieDetails(result);
        }

        else if(methodTag == CommonData.SECOND_METHOD){
            getSimilarMovies(result);
        }
    }

    @Override
    public void itemClicked(int movieId) {
        buildAndLoadDataFromURLS(movieId);
    }


}
