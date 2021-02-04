package com.example.MovieDB.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.MovieDB.R;
import com.example.MovieDB.data.Movie;
import com.example.MovieDB.fragment.MovieDetailFragment;
import java.util.ArrayList;


/**
 * @author arun
 */public class MovieListAdapter extends RecyclerView.Adapter<MovieListAdapter.RecyclerViewHolders>{

    private ArrayList<Movie> mMovieList;
    private Context mContext;

    //constructor
    public MovieListAdapter(Context context, ArrayList<Movie> itemList) {
        this.mContext = context;
        this.mMovieList = itemList;
    }
    public void addMovieList(ArrayList<Movie> itemList){
        this.mMovieList.addAll(itemList);
    }
    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_item, parent, false);
        RecyclerViewHolders holder =new RecyclerViewHolders(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewHolders holder, final int position) {

        //포스터만 출력하자.
        String url = "https://image.tmdb.org/t/p/w500" + mMovieList.get(position).getPoster_path();
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageView);
        holder.userrating.setText("평점: "+ mMovieList.get(position).getVote_average());//나중에 레이팅바로변경
        holder.textView.setText(mMovieList.get(position).getTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("test", mMovieList.get(position).toString());
                MovieDetailFragment movieDetailFragment =
                        new MovieDetailFragment(mMovieList.get(position));
                FragmentTransaction transaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, movieDetailFragment).commitAllowingStateLoss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mMovieList.size();
    }


    //뷰홀더 - 따로 클래스 파일로 만들어도 된다.//
    public static class RecyclerViewHolders extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public TextView userrating;
        public RecyclerViewHolders(View itemView) {
            super(itemView);
            Log.d("www","www2");
            imageView = (ImageView) itemView.findViewById(R.id.movie_poster);
            userrating = itemView.findViewById(R.id.vote_average);
            textView = itemView.findViewById(R.id.movie_title);
        }
    }
}
