package Data;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.movieapi.moviedirectory.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import Activities.MovieDetailsActivity;
import Model.Movie;

public class MovieRecyclerViewAdapter extends RecyclerView.Adapter<MovieRecyclerViewAdapter.ViewHolder> {
   private Context context;
   private List<Movie> movieList;
    public MovieRecyclerViewAdapter(Context context , List<Movie> movies) {
    this.context = context;
    movieList = movies;
    }

    @NonNull
    @Override
    public MovieRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_row,parent,false);

        return new ViewHolder(view,context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
     Movie movie = movieList.get(position);
     String poster_link = movie.getPoster();
     holder.title.setText(movie.getTitle());
     holder.category.setText(movie.getMovieType());
        Picasso.get().load(poster_link).placeholder(android.R.drawable.ic_btn_speak_now).into(holder.image);
        holder.year.setText(movie.getYear());

    }

    @Override
    public int getItemCount() {
        return movieList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title,year,category;
        ImageView image;
        public ViewHolder(@NonNull View itemView , final Context ctx) {
            super(itemView);
            context = ctx;
            title = (TextView) itemView.findViewById(R.id.movieTitleID);
            year = (TextView) itemView.findViewById(R.id.movieReleaseId);
            category = (TextView) itemView.findViewById(R.id.movieCategoryId);
            image = (ImageView) itemView.findViewById(R.id.movie_icon);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie movie = movieList.get(getAdapterPosition());
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    intent.putExtra("movie",movie);
                    ctx.startActivity(intent);

                }
            });
        }

        @Override
        public void onClick(View v) {

        }
    }
}
