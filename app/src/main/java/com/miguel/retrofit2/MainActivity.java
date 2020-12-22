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
    JsonPlaceHolderApi jsonPlaceHolderApi;
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
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

         getPosts(3, "id", "desc");
        //getComments(3);
    }
    private void getComments(int postId){
        Call<List<Comment>> commentList = jsonPlaceHolderApi.getComments(postId);

        commentList.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {
                if(!response.isSuccessful()){
                    mTextView.setText("Code: " + response.code());
                }

                List<Comment> comments = response.body();

                for(Comment comment : comments){
                    String content = "";
                    content += "Comment ID: " + comment.getCommentId() + "\n";
                    content += "Post ID: " + comment.getPostId() + "\n";
                    content += "Email: " + comment.getEmail() + "\n";
                    content += "Title: " + comment.getTitle() + "\n";
                    content += "Text: " + comment.getText() + "\n\n";

                    mTextView.append(content);
                }
            }
            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                 mTextView.setText(t.getMessage());
            }
        });
    }
    private void getPosts(int userId, String sortBy, String order){
        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(userId, sortBy, order);

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