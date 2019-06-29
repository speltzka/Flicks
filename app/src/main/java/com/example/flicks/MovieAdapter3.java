package com.example.flicks;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.flicks.models.Movie;

import org.parceler.Parcels;

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

        //determine orientation
        boolean isPortrait = context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
        int placeholderId;
        ImageView imageView = null;

        String imageUrl = null;
        if (isPortrait
        ) {
            //build url for poster image
            imageUrl = config.getImageUrl(config.getPosterSize(), movie.getPosterPath());
            placeholderId = R.drawable.flicks_movie_placeholder;
            imageView = holder.ivPosterImage;
        } else {
            //build url for landscape image
            imageUrl = config.getImageUrl(config.getBackdropSize(), movie.getBackdropPath());
            placeholderId = R.drawable.flicks_backdrop_placeholder;
            imageView = holder.ivBackdropImage;
        }
        //load image using Glide
        Glide.with(context)
                .load(imageUrl)
                .bitmapTransform(new RoundedCornersTransformation(context, 25, 0))
                .placeholder(placeholderId)
                .error(placeholderId)
                .into(imageView);
    }

    //returns the total number of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    //create the viewholder as a static inner class
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        //track view objects
        ImageView ivPosterImage;
        ImageView ivBackdropImage;
        TextView tvTitle;
        TextView tvOverview;

        public ViewHolder(View itemView){
            super(itemView);
            //lookup view objects by id
            ivPosterImage = (ImageView) itemView.findViewById(R.id.ivPosterImage);
            ivBackdropImage = (ImageView) itemView.findViewById(R.id.ivBackdropImage);
            tvOverview = (TextView) itemView.findViewById(R.id.description);
            tvTitle = (TextView) itemView.findViewById(R.id.movieTitle);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            // gets position
            int position = getAdapterPosition();
            if (position!= RecyclerView.NO_POSITION){
                Movie movie = movies.get(position);
                Intent intent = new Intent(context, MovieDetailsActivity.class);
                intent.putExtra(Movie.class.getSimpleName(), Parcels.wrap(movie));
                context.startActivity(intent);
            }

        }
    }
}
