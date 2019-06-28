package com.example.flicks;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.flicks.models.Movie;

import java.util.ArrayList;

public class movieAdapter extends RecyclerView.Adapter<movieAdapter.ViewHolder>{
    //list of movies
    ArrayList <com.example.flicks.models.Movie> movies;

    //initialize with list
    public movieAdapter(ArrayList<com.example.flicks.models.Movie> movies){
        this.movies = movies;
    }

    //creates and inflates a new view
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //get the context and create the inflater
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        //create the view using item_movie layout
        View movieView = inflater.inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    //binds an inflated view to a new item
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //get the movie data at the specified position
        Movie movie = movies.get(position);
        // populate the view with the movie data
        holder.tvTitle.setText(movie.getTitle());
        holder.tvOverview.setText(movie.getOverview());

        //TODO- set image with Glide



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
