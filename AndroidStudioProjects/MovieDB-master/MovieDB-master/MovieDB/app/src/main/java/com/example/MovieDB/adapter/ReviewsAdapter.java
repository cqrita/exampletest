package com.example.MovieDB.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MovieDB.R;
import com.example.MovieDB.data.Review;

import java.util.List;


public class ReviewsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context context;
    private List<Review> reviewList;
    private TextView author;
    private TextView content;


    public class MyViewHolder extends RecyclerView.ViewHolder {
        private MyViewHolder(View view) {
            super(view);
            author =view.findViewById(R.id.textAuthor);
            content =view.findViewById(R.id.textContent);
        }
    }

    public ReviewsAdapter(Context context, List<Review> reviewList) {
        this.context = context;
        this.reviewList = reviewList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.review, parent, false);
        return new ReviewsAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        Review review = reviewList.get(position);
        author.setText(review.getAuthor());
        content.setText(review.getContent());
    }

    @Override
    public int getItemCount() {
        return reviewList != null ? reviewList.size() : 0;
    }
}

