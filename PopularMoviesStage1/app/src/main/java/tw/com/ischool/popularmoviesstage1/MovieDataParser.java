package tw.com.ischool.popularmoviesstage1;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kevinhuang on 2016/9/29.
 */

public class MovieDataParser {

    private final static String LOG_TAG = FetchMoviesTask.class.getName() ;

    private static final String RESULTS = "results";
    private static final String TITLE = "title";
    private static final String POSTER_PATH = "poster_path";
    private static final String OVERVIEW = "overview";
    private static final String RELEASE_DATE = "release_date";
    private static final String POLULARITY = "popularity";
    private static final String VOTE_AVERAGE = "vote_average";


    public static List<Movie> getMovies(String  jsonString) throws JSONException {
        List<Movie> result = new ArrayList<>();

        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray ary = jsonObject.getJSONArray(RESULTS);
            for(int i=0; i<ary.length(); i++) {
                JSONObject jsonMovie = ary.getJSONObject(i);
                Movie movie = new Movie();
                movie.setTitle(jsonMovie.getString(TITLE));
                movie.setPosterPath(jsonMovie.getString(POSTER_PATH).replace("/",""));
                movie.setOverview(jsonMovie.getString(OVERVIEW));
                movie.setRelease_date(jsonMovie.getString(RELEASE_DATE));
                movie.setPopularity(jsonMovie.getString(POLULARITY));
                movie.setVote_average(jsonMovie.getString(VOTE_AVERAGE));
                result.add(movie);
            }
        }
        catch(Exception ex) {
            Log.d(LOG_TAG, "parse movie json error : " + ex.getLocalizedMessage());
        }
        return result ;
    }
}
