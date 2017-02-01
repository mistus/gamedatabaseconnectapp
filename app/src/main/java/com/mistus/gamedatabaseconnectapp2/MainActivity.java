package com.mistus.gamedatabaseconnectapp2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.HttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.HttpResponse;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public ListView ListView;
    public Button searchButton;
    public EditText EditText;

//    String[] SqlData = new String[] {"[01], Eclipes, 開發程式, 正確數:0, 錯誤數:0","[02], CoCo, 飲料店, 正確數:0, 錯誤數:0"};
        String[] SqlData = new String[] {"[-], -, -, 正確數:-, 錯誤數:-"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

         ListView = (ListView)findViewById(R.id.listView);
         searchButton = (Button)findViewById(R.id.searchButton);
         EditText = (EditText)findViewById(R.id.editText);

        EditText.setText("SelectAll");
//        EditText.setText("[sql]update QuestionVO set question = '588', answer = 'ouse', correct = '0', wrong ='0' where questionNumber ='1'");

        ArrayAdapter<String> adapterSqlData = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SqlData);
        ListView.setAdapter(adapterSqlData);

        //searchButton事件綁定
        searchButton.setOnClickListener(searchButtonClick);

        //底
    }


    //點擊Listener 讀取資料庫 沒事不要開
    public void searchClick(){

        //連接資料庫

        Thread thread = new Thread() {

            public void run() {

                //SearchAll or SQL
                String Comand = EditText.getText().toString();
                boolean sqlFlag = false;
                try {
                    if(Comand.substring(0,5).equals("[sql]")){
                        sqlFlag = true;
                        Comand = Comand.substring(5);

                    }
                }catch (Exception e){

                }

                String url = "http://183.181.20.96/myFirstHomePage/Hangman/QusetionServlet.do";
//                String url = "http://localhost:8080/myFirstHomePage/Hangman/QusetionServlet.do";
                JSONObject jArray = null;

                try {
//                    URI url = new URI("http", "127.0.0.1", "8080", "/myFirstHomePage/Hangman/QusetionServlet.do");

                    //呼叫post方法取json
                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(url);

                    //判斷使用的Servlet
                    List<NameValuePair> params = new ArrayList<NameValuePair>();
                    if (sqlFlag == true){
                        params.add(new BasicNameValuePair("action", "SelectCommand"));
                        params.add(new BasicNameValuePair("Command", Comand));
                    }else{
                        params.add(new BasicNameValuePair("action", "SelectCommand"));
                        params.add(new BasicNameValuePair("Command", "from QuestionVO order by questionNumber"));
                    }


                    UrlEncodedFormEntity ent = new UrlEncodedFormEntity(params, HTTP.UTF_8);
                    httppost.setEntity(ent);

                    HttpResponse response = httpclient.execute(httppost);

                    //取資料
                    HttpEntity httpEntity = response.getEntity();
                    String responseString = EntityUtils.toString(httpEntity, "UTF-8");
                    JSONTokener jsonTokener = new JSONTokener(responseString);
                    JSONArray jsons = (JSONArray) jsonTokener.nextValue();
                    Log.e("EEEEEEE",(String) jsons.getJSONObject(0).get("Answer"));
                    //範例
                    //(String) jsons.getJSONObject(0).get("Question"));
                    //(String) jsons.getJSONObject(0).get("Answer"));

                    SqlData = new String[jsons.length()];
                    for(int i = 0; i < jsons.length(); i++){
                        SqlData[i] = "["+jsons.getJSONObject(i).get("QuestionNumber")+"],"+
                                jsons.getJSONObject(i).get("Question")+","+
                                jsons.getJSONObject(i).get("Answer")+",正確數:"+
                                jsons.getJSONObject(i).get("Correct")+",錯誤數:" +
                                jsons.getJSONObject(i).get("Wrong");

                    }

                } catch (Exception e) {
                    Log.e("*******Log******", "連接失敗 " + e.toString());
                    return;
                }

            }
        };
        thread.start();

        //等待執行緒
        try {
            thread.join();
        }catch (Exception e){

        }

        ArrayAdapter<String> adapterSqlData = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, SqlData);
        ListView.setAdapter(adapterSqlData);
        ListView.setOnItemClickListener(listViewItemClickListener);
        Toast toast = Toast.makeText(this, "成功", Toast.LENGTH_SHORT);
        toast.show();


    }

    //查詢Button的ClickListener
    private  Button.OnClickListener searchButtonClick = new Button.OnClickListener() {
        public void onClick(View v) {
            searchClick();
        }
    };

    //ListView的ClickListener
    private ListView.OnItemClickListener listViewItemClickListener = new ListView.OnItemClickListener(){
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int postition, long id){
            Intent intent = new Intent();
            intent.setClass(MainActivity.this,updatePage.class);

            //攜家帶眷
            Bundle bundle = new Bundle();
            String sel = parent.getItemAtPosition(postition).toString();
            bundle.putString("command", sel);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    };

}
