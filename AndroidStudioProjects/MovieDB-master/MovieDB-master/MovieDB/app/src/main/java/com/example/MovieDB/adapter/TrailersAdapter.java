package com.example.MovieDB.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.MovieDB.R;
import com.example.MovieDB.data.Trailer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.List;

/**
 * @author Yassin Ajdi.
 */
public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.RecyclerViewHolders>  {
    private List<Trailer> trailerList;
    public Context context;
    private YouTubePlayerView videoTrailerView;
    private com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer videoTrailer;
    public class RecyclerViewHolders extends RecyclerView.ViewHolder {
        public TextView trailerName;
        private String currentVideoId;
        public RecyclerViewHolders(View view) {
            super(view);
            videoTrailerView = view.findViewById(R.id.videoTrailer);
            trailerName =view.findViewById(R.id.trailerName);
            videoTrailerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                public void onReady(@NonNull YouTubePlayer initializedYouTubePlayer) {
                    videoTrailer = initializedYouTubePlayer;
                    videoTrailer.cueVideo(currentVideoId, 0);
                }
            });
        }

        void cueVideo(String videoId) {
            currentVideoId = videoId;
            if(videoTrailer == null)
                return;
            videoTrailer.cueVideo(videoId, 0);
        }
    }


    public TrailersAdapter(Context context, List<Trailer> trailerList) {
        this.trailerList = trailerList;
        this.context =context;
    }

    public void setTrailerList(List<Trailer> trailerList) {
        this.trailerList = trailerList;
    }



    @NonNull
    @Override
    public RecyclerViewHolders onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.trailer, parent, false);
        return new RecyclerViewHolders(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailersAdapter.RecyclerViewHolders holder, int position) {
        final Trailer trailer = trailerList.get(position);
        Log.d("trailer","https://www.youtube.com/watch?v="+trailer.getKey());
        holder.cueVideo(trailerList.get(position).getKey());
        holder.trailerName.setText(trailer.getName());
    }

    @Override
    public int getItemCount() {
        return trailerList.size();
    }
}
