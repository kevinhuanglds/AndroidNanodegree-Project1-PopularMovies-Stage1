package tw.com.ischool.popularmoviesstage1;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by kevinhuang on 2016/9/29.
 */


public  class FetchMoviesTask extends AsyncTask<String, Void, String> {

    private final String SCHEME = "http";
    private final String SERVICE_HOST = "api.themoviedb.org";
    private final String VERSION_NO = "3";
    private final String PATH = "movie";
    private final String BY_POPULARITY = "popular";
    private final String BY_RATING = "top_rated";

    private final String LOG_TAG = FetchMoviesTask.class.getName() ;
    private final String QUERYPARAM_API_KEY = "api_key";

    private OnGetMoviesListener listener ;
    private Exception ex ;


    public FetchMoviesTask(OnGetMoviesListener listener) {
        this.listener = listener ;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;

        // Will contain the raw JSON response as a string.
        String movieJsonStr = null;

        try {
            String sortBy = BY_POPULARITY ;  //default path
            if (params.length > 0) {
                sortBy = params[0];
            }

            //Create Url to fetch movie list.
            Uri.Builder builder = new Uri.Builder();
            builder.scheme(SCHEME).authority(SERVICE_HOST)
                    .appendPath(VERSION_NO)
                    .appendPath(PATH)
                    .appendPath(sortBy)
                    .appendQueryParameter(QUERYPARAM_API_KEY, BuildConfig.THE_MOVIE_DB_API_KEY );

            String strURL = builder.build().toString();
            Log.v(LOG_TAG, strURL);

            URL url = new URL(strURL);
            // Create the request to OpenWeatherMap, and open the connection
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // Read the input stream into a String
            InputStream inputStream = urlConnection.getInputStream();
            StringBuffer buffer = new StringBuffer();
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
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            movieJsonStr = buffer.toString();
            Log.v(LOG_TAG, movieJsonStr);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            this.ex = e ;
            return null;
        } finally{
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

        return movieJsonStr ;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        if (this.listener != null) {
            if (this.ex != null) {
                this.listener.afterGet(null, this.ex);
            }
            else {
                this.listener.afterGet(s, null);
            }
        }
    }

    public interface OnGetMoviesListener {
        void afterGet(String jsonString, Exception ex) ;
    }
}

