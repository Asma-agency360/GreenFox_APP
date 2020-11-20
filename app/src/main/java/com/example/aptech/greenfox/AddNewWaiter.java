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

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class AddNewWaiter extends AppCompatActivity {

    EditText w_name,w_email,w_password;
    Button register;

    ProgressDialog progressDialog;

    static Urls urls = new Urls();
    private static final String URL_PROCESSING = urls.url_processing();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_waiter);

        w_name=(EditText) findViewById(R.id.et_name);
        w_email=(EditText) findViewById(R.id.et_email_waiter);
        w_password=(EditText) findViewById(R.id.et_password_waiter);
        register=(Button) findViewById(R.id.btn_sub_waiter);




        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

              /*  if(w_name.getText().toString().isEmpty() || w_email.getText().toString().isEmpty() || w_password.getText().toString().isEmpty())
                {
                    new PrettyDialog(AddNewWaiter.this)
                            .setTitle("Fill All Fields")
                            .setIcon(R.drawable.greenfoxlogo)
                            .setMessage("Don't Leave the Field Empty!!!!")
                            .addButton("OK",
                                    R.color.pdlg_color_white,  // button text color
                                    R.color.pdlg_color_green,  // button background color
                                    new PrettyDialogCallback() {  // button OnClick listener
                                        @Override
                                        public void onClick() {

                                            Intent intent = new Intent(AddNewWaiter.this, Insert_menu_items.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                                            startActivity(intent);
                                            progressDialog.dismiss();

                                        }
                                    }).show();
                }
                else {*/
                    String n_w = w_name.getText().toString();
                    String e_w = w_email.getText().toString();
                    String p_w = w_password.getText().toString();


                    Log.v("Waiter_Name: ",n_w);
                    Log.v("Waiter_Email: ",e_w);
                    Log.v("Waiter_Pass: ",p_w);

                    add_new_waiter_using_volley("add_users", e_w, n_w, p_w, "enabled");
            }
        });

    }


    private void add_new_waiter_using_volley(final String action, final String wai_email, final String wai_name , final String wai_pass,final String status1)
    {
       //progressDialog.setMessage("Please Wait...");
       //progressDialog.show();

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
                                //show_dialog_message("No Categories", "No Enabled Categories");
                                Toast.makeText(getApplicationContext(), "No categories", Toast.LENGTH_SHORT).show();
                                //Log.v("response", "Invalid Credentials");
                            }

                            /*
                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject_data = jsonObject.getJSONObject("data");

                            String response_status = jsonObject_data.getString("response_status");
                            String response_msg = jsonObject_data.getString("response_msg");
                            JSONArray jsonArray_response_data = jsonObject_data.getJSONArray("response_data");

                            Log.v("response", String.valueOf(jsonArray_response_data));

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
                            finish();

                             */
                        }
                        catch(Exception ex)
                        {
                            //show_dialog_message("Network Error", "Check network connection");
                            Toast.makeText(getApplicationContext(), "Network Error", Toast.LENGTH_SHORT).show();
                            Log.v("catch err", ex.getMessage());
                        }
                        //progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //progressDialog.dismiss();
                        //  show_dialog_message("Network Error", "Check network connection");
                        Toast.makeText(AddNewWaiter.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
                        //Log.v("Network Error", error.networkResponse.data.toString());
                    }
                })


        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "add_users");
                //params.put("item_auto_id", "1");
                params.put("waiter_name", wai_name);
                params.put("waiter_email",wai_email);
                params.put("waiter_pass",wai_pass);
                params.put("waiter_status",status1);
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
