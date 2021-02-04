package com.example.MovieDB.fragment;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.MovieDB.R;
import com.example.MovieDB.data.Api;
import com.example.MovieDB.data.Cast;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class ActorDetailFragment extends Fragment {
    private Cast cast;
    private ImageView poster;
    private TextView name;
    private TextView castYear;
    private TextView biography;
    private int castInt;
    private ProgressDialog progressDialog;
    public ActorDetailFragment(int castInt)
    {
        this.castInt = castInt;
    }


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    private CastAsyncTask.HttpCallback httpCallback = new CastAsyncTask.HttpCallback() {
        @Override
        public void onResult(Cast cast1) {
            progressDialog.dismiss();
            cast = cast1;
            Glide.with(Objects.requireNonNull(getContext()))
                    .load("https://image.tmdb.org/t/p/w500" + cast.getProfile_path())
                    .centerCrop()
                    .into(poster);
            name.setText(cast.getName());
            biography.setText(cast.getBiography());
            if (cast.getDeathday() == null && cast.getBirthday() != null) {
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date date = null;
                try {
                    date = formatter.parse(cast.getBirthday());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                Calendar cal = Calendar.getInstance();
                assert date != null;
                cal.setTime(date);
                Calendar today = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);
                cal.set(year, month + 1, day);
                int age = today.get(Calendar.YEAR) - cal.get(Calendar.YEAR);
                if (today.get(Calendar.DAY_OF_YEAR) < cal.get(Calendar.DAY_OF_YEAR)) {
                    age--;
                }
                castYear.setText(String.valueOf(age));
            }
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        return inflater.inflate(R.layout.castdetails, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        progressDialog = new ProgressDialog(getContext());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("\t로딩중...");
        progressDialog.show();
        super.onViewCreated(view, savedInstanceState);
        poster = view.findViewById(R.id.cast_poster);
        name = view.findViewById(R.id.cast_name);
        castYear = view.findViewById(R.id.cast_year);
        biography = view.findViewById(R.id.cast_biography);
        CastAsyncTask castAsyncTask = new CastAsyncTask(httpCallback, castInt);
        castAsyncTask.execute();
    }

    public static class CastAsyncTask extends AsyncTask<Void, Void, Cast> {
        private HttpCallback httpCallback;
        private int ints;

        CastAsyncTask(HttpCallback httpCallback, int ints) {
            this.httpCallback = httpCallback;
            this.ints = ints;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Cast cast1) {
            super.onPostExecute(cast1);
            if (this.httpCallback != null) {
                this.httpCallback.onResult(cast1);
            }

        }

        @Override
        protected Cast doInBackground(Void... args) {
            String m_id = String.valueOf(ints);
            OkHttpClient client = new OkHttpClient();
            Request request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/person/" + m_id + "?api_key=" + Api.apikey2)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                Gson gson = new Gson();
                assert response.body() != null;
                JsonElement rootObject = JsonParser.parseReader(Objects.requireNonNull(response.body()).charStream())
                        .getAsJsonObject();
                return gson.fromJson(rootObject, Cast.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        interface HttpCallback {
            void onResult(Cast cast1);
        }
    }
    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}


