package com.example.nikolakaloyanov.myapplication.question;

import java.util.List;

import retrofit.Call;
import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.POST;

/**
 * Created by nikola.kaloyanov on 2/11/2016.
 */
public interface QuestionService {

    @GET("questions/")
    Call<List<Question>> getQuestions();

    @POST("questions/")
    Call<Question> postQuestion(@Body Question question);

}
