package com.example.motaher.movietune.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.motaher.movietune.R;
import com.example.motaher.movietune.global.CommonData;
import com.example.motaher.movietune.helper.ImageViewWithCornerRadius;
import com.example.motaher.movietune.helper.ItemClickNotifier;
import com.example.motaher.movietune.model.MovieInfo;
import com.example.motaher.movietune.model.MovieList;
import com.example.motaher.movietune.model.SingleMovieInfo;
import com.example.motaher.movietune.webService.ApiDetails;
import com.example.motaher.movietune.webService.CommonService;

import java.util.List;

/**
 * Created by motaher on 5/5/2017.
 */

public class MovieDetailsAdapter extends RecyclerView.Adapter<MovieDetailsAdapter.ViewHolder>{

    private SingleMovieInfo singleMovieInfo;
    private MovieInfo similarMovieInfo;
    private int rowLayout;
    private Context mContext;
    private CommonService commonService;
    private ItemClickNotifier itemClickNotifier;

    public MovieDetailsAdapter(Context context, int rowLayout, SingleMovieInfo singleMovieInfo, MovieInfo similarMovieInfo){
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.singleMovieInfo = singleMovieInfo;
        this.similarMovieInfo = similarMovieInfo;
        itemClickNotifier = (ItemClickNotifier) context;
        commonService = new CommonService(mContext);
    }

    @Override
    public MovieDetailsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieDetailsAdapter.ViewHolder holder, int position) {

        if(similarMovieInfo != null){
            manageSimilarMovieList(holder.recommandationMovieList, similarMovieInfo.getResults());
        }
        if(singleMovieInfo != null){

            String release_date = singleMovieInfo.getReleaseDate();
            String released_year = commonService.dateSeparator(release_date);
            String titleWithYear = singleMovieInfo.getTitle() + " (" + released_year + ")";
            holder.movieTitle.setText(titleWithYear);

            String genres = commonService.getMergedNames(singleMovieInfo.getGenres(), null, null, null);
            String productionCompanies = commonService.getMergedNames(null, singleMovieInfo.getProductionCompanies(), null, null);
            String productionCountries = commonService.getMergedNames(null, null, singleMovieInfo.getProductionCountries(), null);
            String languages = commonService.getMergedNames(null, null, null, singleMovieInfo.getSpokenLanguages());

            holder.movieGenres.setText(genres);
            holder.movieProductionCompany.setText(productionCompanies);
            holder.movieProductionCountries.setText(productionCountries);
            holder.movieLanguage.setText(languages);
            holder.movieRating.setText(String.valueOf(singleMovieInfo.getVoteAverage()));

            Integer pop = singleMovieInfo.getPopularity().intValue();
            holder.moviePopularity.setText(String.valueOf(pop) + "%");
            holder.movieOverview.setText(singleMovieInfo.getOverview());
            holder.movieBudget.setText("$" + String.valueOf(singleMovieInfo.getBudget()));
        }

    }

    @Override
    public int getItemCount() {
        return 1;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public TextView movieTitle;
        public TextView movieGenres;
        public TextView movieRating;
        public TextView moviePopularity;
        public TextView movieOverview;
        public TextView movieProductionCompany;
        public TextView movieProductionCountries;
        public TextView movieLanguage;
        public TextView movieBudget;
        public TextView reviewLabel;
        public TextView popularityLabel;
        public LinearLayout recommandationMovieList;

        public ViewHolder(View itemView) {
            super(itemView);

            movieTitle = (TextView) itemView.findViewById(R.id.movie_title);
            movieGenres = (TextView) itemView.findViewById(R.id.movie_genres);
            movieRating = (TextView) itemView.findViewById(R.id.movie_rating);
            moviePopularity = (TextView) itemView.findViewById(R.id.movie_popularity);
            movieOverview = (TextView) itemView.findViewById(R.id.movie_overview);
            movieProductionCompany = (TextView) itemView.findViewById(R.id.movie_production_company);
            movieProductionCountries = (TextView) itemView.findViewById(R.id.movie_production_countries);
            movieLanguage = (TextView) itemView.findViewById(R.id.movie_language);
            movieBudget = (TextView) itemView.findViewById(R.id.movie_budget);
            reviewLabel = (TextView) itemView.findViewById(R.id.user_review_label);
            popularityLabel = (TextView) itemView.findViewById(R.id.movie_popularity_label);
            recommandationMovieList = (LinearLayout) itemView.findViewById(R.id.recommandation_movie_list);
        }
    }

    private void manageSimilarMovieList(LinearLayout parentView, final List<MovieList> resultList){

        int size = resultList.size();
        for(int i = 0 ; i < size; i++) {

            ImageViewWithCornerRadius recommandation = new ImageViewWithCornerRadius(mContext);

            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT);

            params.width = commonService.convertDpToPx(75);
            params.leftMargin = commonService.convertDpToPx(10);
            params.topMargin = commonService.convertDpToPx(5);
            params.bottomMargin = commonService.convertDpToPx(5);

            if (i == size - 1) {
                params.rightMargin = commonService.convertDpToPx(10);
            }
            else{
                params.rightMargin = 0;
            }

            recommandation.setLayoutParams(params);
            recommandation.setId(i);
            recommandation.setScaleType(ImageView.ScaleType.CENTER_CROP);

            String profilePic = resultList.get(i).getPosterPath();
            String url = ApiDetails.IMAGE_BASE_URL + profilePic + "?api_key=" + CommonData.API_KEY;

            Glide
                    .with(mContext)
                    .load(url)
                    .placeholder(R.drawable.background_image)
                    .crossFade()
                    .into(recommandation);

            recommandation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int id = v.getId();
                    int movie_id = resultList.get(id).getId();
                    itemClickNotifier.itemClicked(movie_id);
                }
            });

            parentView.addView(recommandation);
        }
    }
}
