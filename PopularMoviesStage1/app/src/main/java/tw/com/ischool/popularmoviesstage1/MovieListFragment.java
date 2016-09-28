package tw.com.ischool.popularmoviesstage1;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class MovieListFragment extends Fragment {

    private final String LOG_TAG = FetchMoviesTask.class.getName() ;

    private GridView gridView ;
    private MovieListAdapter adp ;
    private List<Movie> movies ;
    private String jsonStringMovies ;

    public MovieListFragment() {
        // Required empty public constructor
        this.movies = new ArrayList<>();
        this.setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_movie_list, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.gridView = (GridView)view.findViewById(R.id.gridView);
        this.adp = new MovieListAdapter(getActivity(), this.movies);
        this.gridView.setAdapter(this.adp);
        this.gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Movie movie = adp.getItem(i);
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra(DetailActivity.TARGET_MOVIE, movie);
                getActivity().startActivity(intent);
            }
        });
    }


    @Override
    public void onStart() {
        super.onStart();
        loadMovies();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_movie_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:
                loadMovies();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void loadMovies() {

        String sort_by = getSortingFromPreference() ;

        FetchMoviesTask task = new FetchMoviesTask(new FetchMoviesTask.OnGetMoviesListener() {
            @Override
            public void afterGet(String jsonString, Exception ex) {
                if (ex == null) {
                    try {
                        jsonStringMovies = jsonString;
                        movies = MovieDataParser.getMovies(jsonString);
                        adp.clear();
                        adp.addAll(movies);
                        adp.notifyDataSetChanged();
                    }
                    catch (Exception e) {

                    }
                }
                else {
                    Log.d(LOG_TAG, "Fetch movies failed ! " + ex.getLocalizedMessage());
                }
            }
        });

        task.execute(sort_by);
    }

    private String getSortingFromPreference() {
        String pref_key = getActivity().getString(R.string.pref_sorting_key);
        String pref_default_sorting = getActivity().getString(R.string.pref_sorting_popular);
        String result = PreferenceManager.getDefaultSharedPreferences(getActivity()).getString(pref_key, pref_default_sorting);
        return result ;
    }


    public  class MovieListAdapter extends ArrayAdapter<Movie> {

        private Context context ;
        private List<Movie> movies ;
        public MovieListAdapter(Context context, List<Movie> movies) {
            super(context, movies.size());
            this.context = context;
            this.movies = movies ;
        }

        @NonNull
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.grid_movie_item, parent, false);
            }
            //get movie object
            Movie movie = this.getItem(position);

            //load image and put to image view
            ImageView imgView = (ImageView)convertView.findViewById(R.id.imgPoster);

            String posterUrl = movie.getPosterUrl();
            Log.d(LOG_TAG, posterUrl);
            Picasso.with(context).load(posterUrl).into(imgView);

            //put movie title to textview
            TextView txtView = (TextView)convertView.findViewById(R.id.txtMovieTitle);
            txtView.setText(movie.getTitle());

            return convertView ;
        }
    }


}
