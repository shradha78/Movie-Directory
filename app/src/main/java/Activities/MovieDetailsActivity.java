package Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.net.sip.SipSession;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.movieapi.moviedirectory.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import Model.Movie;
import Util.Constants;

public class MovieDetailsActivity extends AppCompatActivity {
    private Movie movie;
    private TextView movieTitle , releaseYear, category,directedby,rating,runtime,actors,writers,plot,boxoffice;
    private ImageView movieIcon;
    public RequestQueue requestQueue;
    private String movieId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        requestQueue = Volley.newRequestQueue(this);
        movie = (Movie) getIntent().getSerializableExtra("movie");
        assert movie != null;
        movieId = movie.getImdbId();

        setUpUI();
        Log.i("ID : :** ", movieId);
        getMovieDetails(movieId);
    }

    private void getMovieDetails(String id) {
        String url = Constants.URL_Left_Details + id + Constants.URL_Right;
        Log.i("URL ** ", url);
        Log.i("ID ***", id);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.has("Ratings")) {
                        JSONArray array = response.getJSONArray("Ratings");
                        String source = null;
                        String value = null;
                        if (array.length() > 0) {
                            JSONObject mratings = array.getJSONObject(array.length() - 1);
                            source = mratings.getString("Source");
                            value = mratings.getString("Value");
                            rating.setText(source + " : " + value);
                        } else {
                            rating.setText("Ratings : " + "N/A");
                        }
                        movieTitle.setText(response.getString("Title"));
                        category.setText("Category : " +response.getString("Type"));
                        releaseYear.setText("Year Released : " + response.getString("Year"));
                        directedby.setText("Directed By : " + response.getString("Director"));
                        writers.setText("Writers : " + response.getString("Writer"));
                        plot.setText("Plot : " + response.getString("Plot"));
                        runtime.setText("Runtime : " + response.getString("Runtime"));
                        actors.setText("Actors: " + response.getString("Actors"));
                        boxoffice.setText("BoxOffice : " + response.getString("BoxOffice"));
                        Picasso.get().load(response.getString("Poster")).into(movieIcon);

                    }

                } catch (JSONException e) {
                    Log.e("Error catch: ******* ", e.getMessage());
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Error ***** ", error.getMessage());
            }
        });

        requestQueue.add(jsonObjectRequest);
    }

    public void setUpUI(){
        movieTitle = (TextView) findViewById(R.id.movie_title_DetailsID);
        releaseYear = (TextView) findViewById(R.id.year_release_DetailsID);
        category = (TextView) findViewById(R.id.movieCatIDDet);
        directedby = (TextView) findViewById(R.id.directedByDet);
        rating = (TextView) findViewById(R.id.movieRatingIDDet);
        runtime = (TextView) findViewById(R.id.runtimeDet);
        actors = (TextView) findViewById(R.id.actorsDet);
        writers = (TextView) findViewById(R.id.writersDet);
        plot = (TextView) findViewById(R.id.plotDet);
        boxoffice = (TextView) findViewById(R.id.boxOfficeDet);
        movieIcon = (ImageView) findViewById(R.id.movie_icon_DetailsID);
    }
}
