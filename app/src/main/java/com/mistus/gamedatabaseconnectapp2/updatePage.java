package com.mistus.gamedatabaseconnectapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mistus on 2016/07/18.
 */
public class updatePage extends AppCompatActivity {

    String number ="";
    String question = "";
    String answer = "";
    String correct = "";
    String wrong = "";

    public Button okButton;
    public Button cancelButton;
    public TextView questionEdit;
    public TextView answerEdit;
    public TextView correctEdit;
    public TextView wrongEdit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.updatepage);

        //取得元件
        okButton = (Button)findViewById(R.id.okButton);
        cancelButton = (Button)findViewById(R.id.cancelButton);
        questionEdit = (TextView)findViewById(R.id.questionEdit);
        answerEdit = (TextView)findViewById(R.id.answerEdit);
        correctEdit = (TextView)findViewById(R.id.correctEdit);
        wrongEdit = (TextView)findViewById(R.id.wrongEdit);


        //取得打包資料
        Intent inetnt = this.getIntent();
        Bundle bundle = inetnt.getExtras();
        String[] DataSplit = bundle.getString("command").split(",");

//        TextView.setText(DataSplit[0] +"+"+DataSplit[1]+"+"+DataSplit[2]+"+"+DataSplit[3]+"+"+DataSplit[4]);
//                +"+"+DataSplit[5]);
        number = DataSplit[0].substring(1,DataSplit[0].length()-1);
        Log.e("#################",number);
         question = DataSplit[1];
         answer = DataSplit[2];
         correct = DataSplit[3].substring(4);
         wrong = DataSplit[4].substring(4);

        questionEdit.setText(question);
        answerEdit.setText(answer);
        correctEdit.setText(correct);
        wrongEdit.setText(wrong);


        //定義ClickL okButton.setOnClickListener(okButtonClick);
        okButton.setOnClickListener(okButtonClick);
        cancelButton.setOnClickListener(cancelButtonClick);
    }

    private  Button.OnClickListener okButtonClick = new Button.OnClickListener() {
        public void onClick(View v) {
            Thread thread = new Thread() {

                public void run() {
                    question = questionEdit.getText().toString().replaceAll(" ", "");
                    answer = answerEdit.getText().toString().replaceAll(" ", "");
                    correct = correctEdit.getText().toString().replaceAll(" ", "");
                    wrong = wrongEdit.getText().toString().replaceAll(" ", "");


                    String Command = "update QuestionVO set question = '" + question +
                            "' , answer = '" + answer +
                            "' , correct = '" + correct +
                            "' , wrong ='" + wrong +
                            "' where questionNumber ='" + number + "'";


                    Command = "update QuestionVO set question = '" + question + "', answer = '" + answer + "', correct = '" + correct + "', wrong ='" + wrong + "' where questionNumber ='" + number + "'";


                    Log.e("Command", Command);

                    try {
                        String url = "http://183.181.20.96/myFirstHomePage/Hangman/QusetionServlet.do";
                        HttpClient httpclient = new DefaultHttpClient();
                        HttpPost httppost = new HttpPost(url);

                        List<NameValuePair> params = new ArrayList<NameValuePair>();
                        params.add(new BasicNameValuePair("action", "SelectCommand"));
                        params.add(new BasicNameValuePair("Command", Command));
                        UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                        httppost.setEntity(ent);
                        Log.e("system", "test");
//                HttpResponse response = httpclient.execute(httppost);
//                HttpResponse response =
                        httpclient.execute(httppost);
//                response.getEntity();
                        Log.e("system", "結束");
                    } catch (Exception e) {
                        Log.e("system", "失敗");
                    }

                }};

            thread.start();

            //等待執行緒
            try {
                thread.join();
            }catch (Exception e){
                Log.e("system", "thread.join() 失敗");
            }
            
            finish();
        }
    };

    private  Button.OnClickListener cancelButtonClick = new Button.OnClickListener() {
        public void onClick(View v) {
            finish();
        }
    };

}
