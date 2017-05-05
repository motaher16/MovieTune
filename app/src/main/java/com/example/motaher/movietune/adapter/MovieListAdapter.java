package com.example.motaher.movietune.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.motaher.movietune.R;
import com.bumptech.glide.Glide;
import com.example.motaher.movietune.global.CommonData;
import com.example.motaher.movietune.model.MovieList;
import com.example.motaher.movietune.webService.ApiDetails;

import java.util.List;

/**
 * Created by motaher on 5/4/2017.
 */

public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.ViewHolder> {

    private List<MovieList> movieList;
    private int rowLayout;
    private Context mContext;

    public MovieListAdapter(Context context, int rowLayout, List<MovieList> movieList){
        this.mContext = context;
        this.rowLayout = rowLayout;
        this.movieList = movieList;
    }

    @Override
    public MovieListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MovieListAdapter.ViewHolder holder, int position) {

        MovieList result = movieList.get(position);
        String poster_path = result.getPosterPath();
        String url = ApiDetails.IMAGE_BASE_URL + poster_path + "?api_key=" + CommonData.API_KEY;
        Drawable currentPoster = holder.moviePoster.getDrawable();

        if(currentPoster != null){
            Glide
                    .with(mContext)
                    .load(url)
                    .placeholder(currentPoster)
                    .crossFade()
                    .into(holder.moviePoster);
        }
        else{
            Glide
                    .with(mContext)
                    .load(url)
                    .crossFade()
                    .into(holder.moviePoster);
        }
    }

    @Override
    public int getItemCount() {
        return movieList == null ? 0 : movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView moviePoster;

        public ViewHolder(View itemView) {
            super(itemView);
            moviePoster = (ImageView) itemView.findViewById(R.id.movie_poster);
        }
    }
}