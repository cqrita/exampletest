package com.example.MovieDB.fragment;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MovieDB.R;
import com.example.MovieDB.adapter.MovieListAdapter;
import com.example.MovieDB.data.Movie;
import com.example.MovieDB.database.FavoriteDBHelper;

import java.util.ArrayList;

public class FavoriteFragment extends Fragment {
    private RecyclerView recyclerView;
    private ArrayList<Movie> movieList = new ArrayList<>();
    private MovieListAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.favorite, container, false);
        Log.d("seungrok","FavoriteFragment");
        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("\t로딩중...");
        progressDialog.show();
        recyclerView = view.findViewById(R.id.favorite_recycler_view) ;
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),4));
        FavoriteDBHelper favoriteDbHelper=  new FavoriteDBHelper(getActivity());
        movieList= favoriteDbHelper.getAllFavorite();
        adapter = new MovieListAdapter(getContext(), movieList);
        recyclerView.setAdapter(adapter);
        progressDialog.dismiss();
        return view;
    }

}
