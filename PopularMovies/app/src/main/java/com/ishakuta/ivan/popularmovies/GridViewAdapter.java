package com.ishakuta.ivan.popularmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class GridViewAdapter extends ArrayAdapter<Movie> {

    private final Context context;
    private List<Movie> movies = new ArrayList<Movie>();
    private boolean isDebugImageCache = false;

    private Picasso mPicasso = null;

    public GridViewAdapter(Context context, ArrayList<Movie> movies) {
        super(context, 0, movies);

        this.context = context;
        this.movies = movies;
    }

    public void enableImageCacheDebug(boolean flag) {
        isDebugImageCache = flag;
    }

    @Override
    public int getCount() {
        return movies.size();
    }

    @Override
    public Movie getItem(int position) {
        return movies.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.grid_cell, viewGroup, false);
        }

        ImageView view = (ImageView) convertView.findViewById(R.id.image_view);;

        Movie movie = getItem(position);

        if (mPicasso == null) {
            mPicasso = Picasso.with(context);
            mPicasso.setIndicatorsEnabled(isDebugImageCache);
        }

        mPicasso.load(movie.getImageUrl())
//                .placeholder(R.drawable.placeholder)
//                .error(R.drawable.error)
                .tag(context) //
                .into(view);

        view.setAdjustViewBounds(true);

        return view;
    }
}
