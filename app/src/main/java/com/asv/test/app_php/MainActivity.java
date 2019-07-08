package com.asv.test.app_php;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue mQueue;
    private final static String mUrl = "http://192.168.1.6:80/AndroidConnectDB/conn.php";
    private final static String mUrl_S = "http://192.168.1.6:80/AndroidConnectDB/get.php";

    private TextView msg;
    private Button mGetMsgBtn;
    private Button mSendMsgBtn;
    private String TAG = "ONCONNECT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        msg = findViewById(R.id.textView);
        mGetMsgBtn =findViewById(R.id.button);
        mSendMsgBtn =findViewById(R.id.button2);
        mQueue = Volley.newRequestQueue(getApplicationContext());

        mGetMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg.setText("");
                volley_JsonObjectRequestPOST();

            }
        });

        mSendMsgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                msg.setText("");
                volley_JsonObjectRequestPOST_Insert();
                msg.setText("OK");
            }
        });
    }


    /***
     * JsonObjectRequest
     * JsonObjectRequest — To send and receive JSON Object from the Server
     *
     * POST方式請求伺服器
     */
    private void volley_JsonObjectRequestPOST(){
        JsonObjectRequest getRequest = new JsonObjectRequest(Request.Method.POST, mUrl,null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: Check what the php will show ===>  " + response.toString());
                try {
                    JSONArray data = response.getJSONArray("data");
                    for (int i = 0; i < data.length(); i++) {
                        JSONObject jsondata = data.getJSONObject(i);
                        String id = jsondata.getString("id");
                        String name = jsondata.getString("name");
                        String score = jsondata.getString("score");
                        msg.append(id + " " + name + " " + score + " " + "\n");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
            }
        });
        mQueue.add(getRequest);
    }


    /***
     * JsonObjectRequest
     * JsonObjectRequest — To send and receive JSON Object from the Server
     *
     * POST方式請求伺服器
     */
    private void volley_JsonObjectRequestPOST_Insert(){
        /**
         * 用map.put來增加JSON內資料
         *
         * */
        Map<String,String> map =new HashMap<String, String>();
        map.put("id","10527088");
        map.put("score","15");
        map.put("name","WWWXD");
        JSONObject data_send = new JSONObject(map);

        JsonObjectRequest postRequest = new JsonObjectRequest(Request.Method.POST, mUrl_S,data_send, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d(TAG, "onResponse: "+response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: "+error.toString());
            }
        })  {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String,String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        postRequest.setTag("volley_JsonObjectRequestPost");
        mQueue.add(postRequest);
    }



}
