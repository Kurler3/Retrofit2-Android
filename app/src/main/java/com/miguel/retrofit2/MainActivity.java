package com.miguel.retrofit2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
   TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Retrofit will handle the code for the function of getting all the posts
        JsonPlaceHolderApi jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        Call<List<Post>> call = jsonPlaceHolderApi.getPosts();

        // Calls for data in a different thread (handled by retrofit again)
        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                // OnResponse doesn't mean the result was successful

                if(!response.isSuccessful()){
                    mTextView.setText("Code: " + response.code());
                    return;
                }

                //If the response was successful then get the data
                List<Post> postList = response.body();

                for(Post post : postList){
                    String content = "";
                    content += "Post ID: " + post.getPostId() + "\n";
                    content += "User ID: " + post.getUserId() + "\n";
                    content += "Title: " + post.getTitle() + "\n";
                    content += "Post: " + post.getDescription() + "\n\n";

                    mTextView.append(content);
                }


            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                mTextView.setText(t.getMessage());
            }
        });
    }
}