package tw.com.ischool.popularmoviesstage1;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    private final String LOG_TAG = DetailActivity.class.getName() ;

    public static String TARGET_MOVIE = "DetailActivity.movie";

    private Movie targetMovie ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Intent intent = getIntent();
        if (intent != null) {
            targetMovie = intent.getParcelableExtra(TARGET_MOVIE);

            showMovie(targetMovie);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
    }

    private void showMovie(Movie movie) {
        Log.d(LOG_TAG, movie.getTitle());
        if (movie == null) {
            return ;
        }

        ImageView imgView = (ImageView)findViewById(R.id.detailPoster);
        Picasso.with(DetailActivity.this).load(movie.getPosterUrl()).into(imgView);

        TextView txtView = (TextView)findViewById(R.id.detailMovieTitle);
        txtView.setText(movie.getTitle());

        TextView txtReleaseDate = (TextView)findViewById(R.id.detailMovieReleaseDate);
        txtReleaseDate.setText(movie.getRelease_date());

        TextView txtRating = (TextView)findViewById(R.id.detailMovieTopRating);
        txtRating.setText(movie.getVote_average());

        TextView txtOverview = (TextView)findViewById(R.id.detailMovieOverview);
        txtOverview.setText(movie.getOverview());
    }
}
