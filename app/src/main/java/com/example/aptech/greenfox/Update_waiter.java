package com.example.aptech.greenfox;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Update_waiter extends AppCompatActivity {

    EditText e_up_name,e_up_email,e_up_pass;
    Button btn_update_waiter;

    String nw,ew,pw;
    ProgressDialog progressDialog;
    static Urls urls = new Urls();
    private static final String URL_PROCESSING = urls.url_processing();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_waiter);

        e_up_email=(EditText) findViewById(R.id.et_email_update_waiter);
        e_up_name=(EditText) findViewById(R.id.et_name_update_Waiter);
        e_up_pass=(EditText) findViewById(R.id.et_password_update_waiter);
        btn_update_waiter=(Button) findViewById(R.id.btn_update_waiter);


        Intent i=getIntent();
        nw=i.getStringExtra("NameWaiter");
        ew=i.getStringExtra("EmailWaiter");
        pw=i.getStringExtra("PassWaiter");


        e_up_name.setText(nw.toString());
        e_up_email.setText(ew.toString());
        e_up_pass.setText(pw.toString());




        btn_update_waiter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String name,email,pass;

                name=e_up_name.getText().toString();
                email=e_up_email.getText().toString();
                pass=e_up_pass.getText().toString();

                update_waiter_using_volley("update_user",email,name,pass,true);

            }
        });



    }


    private void update_waiter_using_volley(final String action,final String wemail, final String wname, final String wpass,final Boolean status1)
    {


        //volley code

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROCESSING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try
                        {
                            Log.v("response", response);

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject_data = jsonObject.getJSONObject("data");
                            String response_status = jsonObject_data.getString("response_status");
                            String response_msg = jsonObject_data.getString("response_msg");



                            if(response_status.equals("success"))
                            {

                                Toast.makeText(getApplicationContext(), response_msg, Toast.LENGTH_SHORT).show();
                                //Log.v("jsonObject_table1", String.valueOf(arrayList_sub_orders));
                            }
                            else
                            {
                                // show_dialog_message("No Categories", "No Enabled Categories");
                                Toast.makeText(getApplicationContext(), "No categories", Toast.LENGTH_SHORT).show();
                                //Log.v("response", "Invalid Credentials");
                            }


                        }
                        catch(Exception ex)
                        {
                            //show_dialog_message("Network Error", "Check network connection");
                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                            Log.v("catch err", ex.getMessage());
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //  show_dialog_message("Network Error", "Check network connection");
                        Toast.makeText(Update_waiter.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
                        //Log.v("Network Error", error.networkResponse.data.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "update_user");
                //params.put("item_auto_id", "1");
                params.put("waiter_name", wname);
                params.put("waiter_email",wemail);
                params.put("waiter_pass",wpass);
                params.put("waiter_status",status1.toString());
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }

}
