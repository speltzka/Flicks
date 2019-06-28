package com.example.flicks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flicks.models.Movie;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

public class MovieAdapter3 extends RecyclerView.Adapter<MovieAdapter3.ViewHolder>{
    //list of movies
    ArrayList <com.example.flicks.models.Movie> movies;

    com.example.flicks.models.Config config;
    //context for rendering
    Context context;
    //initialize with list
    public MovieAdapter3(ArrayList<com.example.flicks.models.Movie> movies){
        this.movies = movies;
        //config needed for image urls
        com.example.flicks.models.Config config;

    }

    public com.example.flicks.models.Config getConfig() {
        return config;
    }

    public void setConfig(com.example.flicks.models.Config config) {
        this.config = config;
    }

    //creates and inflates a new view
    @NonNull
    @Override
    public com.example.flicks.MovieAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //get the context and create the inflater
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new com.example.flicks.MovieAdapter3.ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull com.example.flicks.MovieAdapter3.ViewHolder holder, int position) {
        //get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());
        //build url for poster image
        String imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
        //load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(R.drawable.flicks_movie_placeholder)
                .error(R.drawable.flicks_movie_placeholder)
                .into(holder.ivPosterImage);
    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public static class ViewHolder extends RecyclerView.ViewHolder {
        //track view objects
        ImageView ivPosterImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView){
            super(itemView);
            //lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.mvImage);
            tvOverview = (TextView) itemView.findViewById(R.id.description);
            tvTitle = (TextView) itemView.findViewById(R.id.movieTitle);
        }
    }
}
