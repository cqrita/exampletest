package com.example.MovieDB.adapter;

import android.content.Context;
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
import com.example.MovieDB.data.Cast;
import com.example.MovieDB.fragment.ActorDetailFragment;

import java.util.ArrayList;

public class CastListAdapter extends RecyclerView.Adapter<CastListAdapter.RecyclerViewHolders> {
    private ArrayList<Cast> mCastList;
    private Context mContext;

    //constructor
    public CastListAdapter(Context context, ArrayList<Cast> itemList) {
        this.mContext = context;
        this.mCastList = itemList;
    }
    public void addCastList(ArrayList<Cast> itemList){
        this.mCastList.addAll(itemList);
    }
    @NonNull
    @Override
    public CastListAdapter.RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cast_item, parent, false);
        CastListAdapter.RecyclerViewHolders holder =new CastListAdapter.RecyclerViewHolders(view);
        holder.setIsRecyclable(false);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CastListAdapter.RecyclerViewHolders holder, final int position) {

        //포스터만 출력하자.
        String url = "https://image.tmdb.org/t/p/w500" + mCastList.get(position).getProfile_path();
        Glide.with(mContext)
                .load(url)
                .centerCrop()
                .error(R.drawable.ic_launcher_background)
                .into(holder.imageCastProfile);
        holder.textCastName.setText(mCastList.get(position).getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ActorDetailFragment castDetailFragment = new ActorDetailFragment(Integer.parseInt(mCastList.get(position).getId()));
                FragmentTransaction transaction = ((FragmentActivity)mContext).getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.fragment, castDetailFragment).commitAllowingStateLoss();
            }
        });
    }

    @Override
    public int getItemCount() {
        return this.mCastList.size();
    }


    //뷰홀더 - 따로 클래스 파일로 만들어도 된다.//
    public static class RecyclerViewHolders extends RecyclerView.ViewHolder {
        public ImageView imageCastProfile;
        public TextView textCastName;
        public RecyclerViewHolders(View itemView) {
            super(itemView);
            imageCastProfile = itemView.findViewById(R.id.cast_poster);
            textCastName =itemView.findViewById(R.id.cast_name);
        }
    }
}
