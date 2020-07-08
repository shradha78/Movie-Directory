package Activities;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Response.ErrorListener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.movieapi.moviedirectory.R;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.JsonReader;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import Data.MovieRecyclerViewAdapter;
import Model.Movie;
import Util.Constants;
import Util.Prefs;

public class MainActivity extends AppCompatActivity {
public RecyclerView recyclerView;
public MovieRecyclerViewAdapter movieRecyclerViewAdapter;
public List<Movie> movieList;
public List<Movie> defaultList;
public RequestQueue queue;
private AlertDialog.Builder alertDialogBuilder;
private AlertDialog alertDialog;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         queue = Volley.newRequestQueue(this);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                 showInputDialog();
            }
        });
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Prefs prefs = new Prefs(MainActivity.this);
        String search = prefs.getSearch();
        movieList = new ArrayList<>();
        defaultList =  new ArrayList<>();
        defaultList = getMovieList("Batman");
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, defaultList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
        movieRecyclerViewAdapter.notifyDataSetChanged();

        movieList = getMovieList(search);
        movieRecyclerViewAdapter = new MovieRecyclerViewAdapter(this, movieList);
        recyclerView.setAdapter(movieRecyclerViewAdapter);
      movieRecyclerViewAdapter.notifyDataSetChanged();

    }
    public List<Movie> getMovieList(String searchTerm){
        movieList.clear();
        String url = Constants.URL_Left + searchTerm + Constants.URL_Right;
//        String url = "https://www.omdbapi.com/?s=Batman&apikey=e0cbc80a";
        Log.i("URL: ", url);
        JsonObjectRequest objectRequest = new JsonObjectRequest(Request.Method.GET, url,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try{
                    JSONArray moviesArray = response.getJSONArray("Search");
                    for(int i = 0 ; i < moviesArray.length() ; i++){
                        JSONObject movieObj = moviesArray.getJSONObject(i);
                        Movie movie = new Movie();
                        movie.setTitle(movieObj.getString("Title"));
                        movie.setYear(movieObj.getString("Year"));
                        movie.setImdbId(movieObj.getString("imdbID"));
                        movie.setMovieType(movieObj.getString("Type"));
                        movie.setPoster(movieObj.getString("Poster"));

                       // Log.d("Movies: " , movie.getTitle());
                         movieList.add(movie);
                    }
                } catch (JSONException e){
                    //Log.d("Not processed","*******");
                    e.printStackTrace();
                    Log.e("while getting JsonObj: ", e.getMessage());
                }

                movieRecyclerViewAdapter.notifyDataSetChanged();


            }
        },  new ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("Not processed:: ",error.getMessage());
            }
        });
        queue.add(objectRequest);
        return  movieList;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.new_search) {
            showInputDialog();
           // return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void showInputDialog(){
        alertDialogBuilder = new AlertDialog.Builder(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_view,null);
        final EditText editText = (EditText) view.findViewById(R.id.edit_text);
        Button submitButton = (Button) view.findViewById(R.id.submit_button);
        alertDialogBuilder.setView(view);
        alertDialog = alertDialogBuilder.create();
        alertDialog.show();
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Prefs prefs =  new Prefs(MainActivity.this);
                if(!editText.getText().toString().isEmpty()){
                    String search = editText.getText().toString();
                    prefs.setSearch(search);
                    movieList.clear();
                    getMovieList(search);
                    movieRecyclerViewAdapter.notifyDataSetChanged();
                }
                alertDialog.dismiss();
            }
        });



    }
}
