package com.miguel.retrofit2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
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

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient httpClient = new OkHttpClient.Builder()
                .addInterceptor(new Interceptor() {
                    @NotNull
                    @Override
                    public okhttp3.Response intercept(@NotNull Chain chain) throws IOException {
                        Request request = chain.request();
                        Request newRequest = request.newBuilder()
                                .header("Interceptor-Header", "xyz")
                                .build();

                        return chain.proceed(newRequest);
                    }
                })
                .addInterceptor(loggingInterceptor)
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient)
                .build();

        // Retrofit will handle the code for all the functions in this interface
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        // -----------------------------------------------------------------------------------------
        // User API
        //getPosts(3, "id", "desc");
        //getComments(3);
        Post post = new Post(23, "Example Title", "Example description");
        //createPost(post);
        //putPost(2, post);
        //patchPost(2, post);
        deletePost(1);
        //------------------------------------------------------------------------------------------
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
    private void createPost(Post post){
        Call<Post> call = jsonPlaceHolderApi.createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if (!response.isSuccessful()) {
                    mTextView.setText("Code: " + response.code());
                    return;
                }

                //If the response was successful then get the data
                Post post = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "Post ID: " + post.getPostId() + "\n";
                content += "User ID: " + post.getUserId() + "\n";
                content += "Title: " + post.getTitle() + "\n";
                content += "Post: " + post.getDescription() + "\n\n";

                mTextView.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                mTextView.setText(t.getMessage());
            }
        });

    }
    private void putPost(int postId, Post post){
        Call<Post> call = jsonPlaceHolderApi.putPost("dynamic boi" ,postId, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    mTextView.setText("Code: " + response.code());
                    return;
                }

                Post postBack = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "Post ID: " + postBack.getPostId() + "\n";
                content += "User ID: " + postBack.getUserId() + "\n";
                content += "Title: " + postBack.getTitle() + "\n";
                content += "Post: " + postBack.getDescription() + "\n\n";

                mTextView.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                mTextView.setText(t.getMessage());
            }
        });
    }
    private void patchPost(int postId, Post post){
        Call<Post> call = jsonPlaceHolderApi.patchPost(postId, post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                if(!response.isSuccessful()){
                    mTextView.setText("Code: " + response.code());
                    return;
                }

                Post postBack = response.body();

                String content = "";
                content += "Code: " + response.code() + "\n";
                content += "Post ID: " + postBack.getPostId() + "\n";
                content += "User ID: " + postBack.getUserId() + "\n";
                content += "Title: " + postBack.getTitle() + "\n";
                content += "Post: " + postBack.getDescription() + "\n\n";

                mTextView.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                mTextView.setText(t.getMessage());
            }
        });
    }
    private void deletePost(int postId){
        Call<Void> call = jsonPlaceHolderApi.deletePost(postId);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                mTextView.setText("Code: " + response.code());
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t){
                mTextView.setText(t.getMessage());
            }
        });
    }
}