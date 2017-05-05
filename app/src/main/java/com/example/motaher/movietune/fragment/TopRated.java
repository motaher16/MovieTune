package com.example.motaher.movietune.fragment;

/**
 * Created by motaher on 5/4/2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.example.motaher.movietune.R;
import com.example.motaher.movietune.activity.DetailsActivity;
import com.example.motaher.movietune.adapter.MovieListAdapter;
import com.example.motaher.movietune.global.CommonData;
import com.example.motaher.movietune.helper.GridSpacingItemDecoration;
import com.example.motaher.movietune.helper.ItemClickSupport;
import com.example.motaher.movietune.model.MovieInfo;
import com.example.motaher.movietune.model.MovieList;
import com.example.motaher.movietune.helper.PreferenceHelper;
import com.example.motaher.movietune.webService.ApiCommunicator;
import com.example.motaher.movietune.webService.ApiDetails;
import com.example.motaher.movietune.webService.ApiResponse;
import com.example.motaher.movietune.webService.CommonService;
import com.google.gson.Gson;
import java.util.ArrayList;
import java.util.List;

public class TopRated extends Fragment implements ApiResponse {

    int page_number;
    Boolean hasNext;
    PreferenceHelper preferenceHelper;
    ApiCommunicator apiCommunicator;
    MovieListAdapter movieListAdapter;
    private List<MovieList> movieList;
    RecyclerView recyclerView;
    CommonService commonService;
    private View rootView;

    public TopRated() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        commonService = new CommonService(getContext());
        preferenceHelper = new PreferenceHelper(getContext());
        apiCommunicator = new ApiCommunicator(getContext(), this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_toprated, container, false);
        initView(rootView);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.post(new Runnable() {
            @Override
            public void run() {
                initSetup();
                initView(rootView);
            }
        });
    }

    private void initSetup() {
        page_number = 1;
        hasNext = false;
        movieList = new ArrayList<MovieList>();
        if (commonService.isNetworkAvailable()) {
            loadNowPlayingMovies(page_number);
        } else {
            commonService.popupAlertDialog("Please enable your internet connection!");
        }
    }

    private void initView(View rootView) {

        GridLayoutManager gridLayoutManager;

        gridLayoutManager = new GridLayoutManager(getActivity(), 2);
        recyclerView = (RecyclerView) rootView.findViewById(R.id.scrollableview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.addItemDecoration(new GridSpacingItemDecoration(CommonData.SPAN_COUNT, CommonData.SPACING, CommonData.INCLUDE_EDGE));

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(RecyclerView recyclerview, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                if (dy > 0) {

                    final int visibleThreshold = 2;
                    GridLayoutManager layoutManager = (GridLayoutManager) recyclerview.getLayoutManager();
                    int lastItem = layoutManager.findLastCompletelyVisibleItemPosition();
                    int currentTotalCount = layoutManager.getItemCount();

                    if (currentTotalCount <= lastItem + visibleThreshold) {
                        if (hasNext) {
                            page_number = page_number + 1;
                            loadNowPlayingMovies(page_number);
                        }
                    }
                }
            }
        });

        ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {

            @Override
            public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                int id = movieList.get(position).getId();
                Intent detailActivity = new Intent(getActivity(), DetailsActivity.class);
                detailActivity.putExtra("id", id);
                startActivity(detailActivity);
            }
        });
    }

    private void loadNowPlayingMovies(int page) {

        String url = ApiDetails.BASE_URL + ApiDetails.NEWRELEASE_MOVIES_URL + "api_key=" + CommonData.API_KEY + "&page=" + page;
        apiCommunicator.getDataByGET(url, CommonData.FIRST_METHOD, false);
    }


    public void taskCompleted(String result, int methodTag) {

        if (methodTag == CommonData.FIRST_METHOD) {
            if (movieList.isEmpty()) {
                preferenceHelper.saveInfo(CommonData .BANNER_HOLDER_DATA, result);
                movieList = new ArrayList<MovieList>();
                Gson gson = new Gson();
                MovieInfo movieInfo = gson.fromJson(result, MovieInfo.class);
                int totalPages = movieInfo.getTotalPages();
                if (page_number < totalPages) {
                    hasNext = true;
                } else {
                    hasNext = false;
                }
                movieList = movieInfo.getResults();
                if (movieList.size() > 0) {
                    movieListAdapter = new MovieListAdapter(getContext(), R.layout.movie_poster_card, movieList);
                    recyclerView.setAdapter(movieListAdapter);
                }
            } else {
                Gson gson = new Gson();
                MovieInfo movieInfo = gson.fromJson(result, MovieInfo.class);
                int totalPages = movieInfo.getTotalPages();
                if (page_number < totalPages) {
                    hasNext = true;
                } else {
                    hasNext = false;
                }
                movieList.addAll(movieInfo.getResults());
                movieListAdapter.notifyDataSetChanged();
            }
        }
    }
}