package com.olokart.users;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.RequestQueue;

public class AppNotification extends AppCompatActivity {

    private RequestQueue mRequestQue;
    private String URL = "https://fcm.googleapis.com/fcm/send";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
//        setContentView(R.layout.activity_app_notification);
//
//
//        if (getIntent().hasExtra("category")){
//            Intent intent = new Intent(AppNotification.this,ReceiveNotification.class);
//            intent.putExtra("category",getIntent().getStringExtra("category"));
//            intent.putExtra("brandId",getIntent().getStringExtra("brandId"));
//            startActivity(intent);
//        }
//        Button button = findViewById(R.id.btn);
//        mRequestQue = Volley.newRequestQueue(this);
//
//
//        button.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                sendNotification();
//            }
//        });
//
//    }
//
//
//
//
//    private void sendNotification() {
//
//        JSONObject json = new JSONObject();
//        try {
//            json.put("to","/topics/"+"news");
//            JSONObject notificationObj = new JSONObject();
//            notificationObj.put("title","any title");
//            notificationObj.put("body","any body");
//
//            JSONObject extraData = new JSONObject();
//            extraData.put("brandId","puma");
//            extraData.put("category","Shoes");
//
//
//
//            json.put("notification",notificationObj);
//            json.put("data",extraData);
//
//
//            JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, URL,
//                    json,
//                    new Response.Listener<JSONObject>() {
//                        @Override
//                        public void onResponse(JSONObject response) {
//
//                            Log.d("MUR", "onResponse: ");
//                        }
//                    }, new Response.ErrorListener() {
//                @Override
//                public void onErrorResponse(VolleyError error) {
//                    Log.d("MUR", "onError: "+error.networkResponse);
//                }
//            }
//            ){
//                @Override
//                public Map<String, String> getHeaders() throws AuthFailureError {
//                    Map<String,String> header = new HashMap<>();
//                    header.put("content-type","application/json");
//                    header.put("authorization","key=AAAA2vX6iik:APA91bFj3SPKHBN8dmzXpuBOcCv3flSGLWpV17OlDdsix56fiu4UWwbhX4fnvef9HkU3-A90Ts1wsDtFG1QweW_kVvDLPxb_P5jwORDyX5zLeGHZYibabo8Qsdzfcht3N4GjEELsvX9b");
//                    return header;
//                }
//            };
//            mRequestQue.add(request);
//        }
//        catch (JSONException e)
//
//        {
//            e.printStackTrace();
//        }
//    }
}