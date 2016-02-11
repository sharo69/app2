package com.example.nikolakaloyanov.myapplication;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.example.nikolakaloyanov.myapplication.question.Question;
import com.example.nikolakaloyanov.myapplication.question.QuestionService;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Retrofit retrofit;

    QuestionService service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        retrofit = new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl("http://ec2-52-87-228-241.compute-1.amazonaws.com:8080/")
                .build();
        service = retrofit.create(QuestionService.class);

        TextView tv = (TextView) findViewById(R.id.textView2);
        tv.setText("Hi :)");

        Button ask = (Button) findViewById(R.id.button);
        ask.setOnClickListener(this);
        Button refresh = (Button) findViewById(R.id.button2);
        refresh.setOnClickListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.button:
                EditText et = (EditText) findViewById(R.id.editText);
                Question question = new Question();
                question.text = et.getText().toString();
                service.postQuestion(question).enqueue(new Callback<Question>() {
                    @Override
                    public void onResponse(Response<Question> response, Retrofit retrofit) {
                        TextView tv = (TextView) findViewById(R.id.textView2);
                        tv.setText("New question: \n" + response.body().text);
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        TextView tv = (TextView) findViewById(R.id.textView2);
                        tv.setText("ne6to se s4upi");
                    }
                });
                break;
            case R.id.button2:
                service.getQuestions().enqueue(new Callback<List<Question>>() {
                    @Override
                    public void onResponse(Response<List<Question>> response, Retrofit retrofit) {
                        TextView tv = (TextView) findViewById(R.id.textView2);
                        StringBuilder sb = new StringBuilder();
                        List<Question> questions = response.body();
                        Collections.sort(questions, new Comparator<Question>() {
                            @Override
                            public int compare(Question lhs, Question rhs) {
                                return rhs.id.intValue() - lhs.id.intValue();
                            }
                        });

                        for(Question q : questions.subList(0, 5))
                        {
                            sb.append(q.id).append(" - ").append(q.text).append("\n");
                        }
                        tv.setText("last 5 questions: \n" + sb.toString());
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        TextView tv = (TextView) findViewById(R.id.textView2);
                        tv.setText("ne6to se s4upi");
                    }
                });
                break;
        }
    }
}
