package com.ishakuta.ivan.popularmovies;

import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {
    private final String SORT_POPULAR = "popularity";
    private final String SORT_RATED = "vote_average";
    private final String SORT_ORDER_ASC = "asc";
    private final String SORT_ORDER_DESC = "desc";

    final boolean isDebugImageCache = true;

    private GridViewAdapter mGridViewAdapter;

    public MainActivityFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.grid_view);

        mGridViewAdapter = new GridViewAdapter(getActivity(), new ArrayList<Movie>());
        mGridViewAdapter.enableImageCacheDebug(isDebugImageCache);

        gridView.setAdapter(mGridViewAdapter);

        return rootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        updateMovies();
    }

    private void updateMovies() {
        String sortOrder = SORT_POPULAR.concat("." + SORT_ORDER_DESC);

        new FetchMoviesTask().execute(sortOrder);
    }

    public class FetchMoviesTask extends AsyncTask<String, Void, List<Movie>> {

        private final String LOG_TAG = FetchMoviesTask.class.getSimpleName();

        private final String BASE_IMAGE_URL = "http://image.tmdb.org/t/p/";
        private final String IMAGE_SIZE = "w185";
        private final String BASE_URL = "http://api.themoviedb.org/3/discover/movie";
        private final String PARAM_API = "api_key";
        private final String PARAM_SORT = "sort_by";

        private String getUrlContents(URL url) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String resultString = null;

            try {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer stringBuffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    stringBuffer.append(line + "\n");
                }

                if (stringBuffer.length() == 0) {
                    return null;
                }

                resultString = stringBuffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);

                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return resultString;
        }

        private List<Movie> getMoviesFromJson(String jsonString, int moviesLimit) throws JSONException {
            final String MDB_LIST = "results";
            final String MDB_MOVIE_ID = "id";
            final String MDB_MOVIE_NAME = "title";
            final String MDB_MOVIE_POPULARITY = "popularity";
            final String MDB_MOVIE_VOTES = "vote_average";
            final String MDB_MOVIE_URI = "poster_path";

            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray moviesArray = jsonObject.getJSONArray(MDB_LIST);

            List<Movie> movies = new ArrayList<Movie>();
            for (int index = 0; index < moviesArray.length(); index++) {

                JSONObject movieJsonObject = moviesArray.getJSONObject(index);

                int movieId = movieJsonObject.getInt(MDB_MOVIE_ID);
                String movieName = movieJsonObject.getString(MDB_MOVIE_NAME);
                double moviePopularity = movieJsonObject.getDouble(MDB_MOVIE_POPULARITY);
                double movieVotes = movieJsonObject.getDouble(MDB_MOVIE_VOTES);
                String movieUri = movieJsonObject.getString(MDB_MOVIE_URI);

                Movie movie = new Movie(movieId, movieName, moviePopularity, movieVotes, movieUri);

                // set full URL to the image poster
                movie.setBaseImageUrl(BASE_IMAGE_URL.concat(IMAGE_SIZE));

                movies.add(movie);
            }

            return movies;
        }

        @Override
        protected List<Movie> doInBackground(String... strings) {
            if (strings.length == 0) {
                return null;
            }

            String sortBy = strings[0];
            URL url = null;
            try {
                Uri builtUri = Uri
                        .parse(BASE_URL)
                        .buildUpon()
                        .appendQueryParameter(PARAM_API, BuildConfig.MOVIE_DB_API_KEY)
                        .appendQueryParameter(PARAM_SORT, sortBy)
                        .build();

                url = new URL(builtUri.toString());

                Log.d(LOG_TAG, url.toString());
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error building API URL", e);

                // didn't successfully built URL, nothing to parse
                return null;
            }

            String jsonStr = getUrlContents(url);
            if (jsonStr == null) {
                return null;
            }

            try {
                return getMoviesFromJson(jsonStr, 100);
            } catch (JSONException e) {
                Log.e(LOG_TAG, "Could not parse weather JSON", e);
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(List<Movie> moviesList) {
            mGridViewAdapter.clear();
            mGridViewAdapter.addAll(moviesList);
        }
    }
}
