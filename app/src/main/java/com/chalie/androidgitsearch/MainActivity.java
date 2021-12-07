package com.chalie.androidgitsearch;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mListView = findViewById(R.id.data_list);

        //use retrofit builder api
        Retrofit.Builder builder = new Retrofit.Builder()
                .baseUrl("https://api.github.com/")
                //add a json to java object converter
                .addConverterFactory(GsonConverterFactory.create());

        //create the retrofit object
        Retrofit retrofit = builder.build();

        //create a request
        GithubClient client = retrofit.create(GithubClient.class);
        //call a method on the client "user repos"
        Call<List<GithubRepo>> call = client.userRepositories("Chal13W1zz");

        //use an async method for the requests
        call.enqueue(new Callback<List<GithubRepo>>() {
            @Override
            public void onResponse(Call<List<GithubRepo>> call, Response<List<GithubRepo>> response) {
                List<GithubRepo> reposList = response.body();
                String[] repositories = new String[reposList.size()];

                for (int i = 0; i < repositories.length; i++) {
                    repositories[i] = reposList.get(i).getName();
                }

                mListView.setAdapter(new ArrayAdapter<>(MainActivity.this, android.R.layout.simple_list_item_1, repositories));
            }

            @Override
            public void onFailure(Call<List<GithubRepo>> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Something went wrong :(", Toast.LENGTH_LONG).show();
            }
        });
    }
}