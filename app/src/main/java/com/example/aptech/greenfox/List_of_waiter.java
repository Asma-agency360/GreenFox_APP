package com.example.aptech.greenfox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.database.DatabaseReference;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class List_of_waiter extends AppCompatActivity {

    DatabaseReference ref;
    RecyclerView rec;
    ArrayList<waiterStore> list;
    waiterAdapter adapter1;
    waiterStore eachitem;

    static Urls urls = new Urls();
    private static final String URL_PROCESSING = urls.url_processing();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_waiter);

        rec=(RecyclerView) findViewById(R.id.recycler_wiater_List);
        rec.setLayoutManager(new LinearLayoutManager(this));

        list=new ArrayList<waiterStore>();

        getwaiters_usingvolley("get_waiters", list);
        Log.v("List size ","Listsize"+list.size());

    }



    private void getwaiters_usingvolley(final String action, final ArrayList<waiterStore> list1) {

        //volley code
        eachitem= new waiterStore();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_PROCESSING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //Log.v("response", response);

                            JSONObject jsonObject = new JSONObject(response);
                            JSONObject jsonObject_data = jsonObject.getJSONObject("data");

                            String response_status = jsonObject_data.getString("response_status");
                            String response_msg = jsonObject_data.getString("response_msg");
                            JSONArray jsonArray_response_data = jsonObject_data.getJSONArray("response_data");
                            if (response_status.equals("success")) {
                                for (int i = 0; i < jsonArray_response_data.length(); i++) {
                                    JSONObject jsonObject_suborder = jsonArray_response_data.getJSONObject(i);
                                    final String waiter_name = jsonObject_suborder.optString("acc_name");
                                    String waiter_email = jsonObject_suborder.optString("acc_email");
                                    String waiter_pass = jsonObject_suborder.optString("acc_pass");

                                    Log.v("Password of waiter: ",waiter_pass);
                                    eachitem = new waiterStore(waiter_name,waiter_email,waiter_pass);
                                    list1.add(eachitem);

                                }

                                    adapter1 = new waiterAdapter(List_of_waiter.this, list1);
                                    rec.setAdapter(adapter1);
                                    Log.v("List of Items" + list1.size(), "itemslist");


                                //Log.v("jsonObject_table1", String.valueOf(arrayList_sub_orders));
                            } else {
                                // show_dialog_message("No Items", "No Enabled Items");
                                Toast.makeText(List_of_waiter.this, "No Waiter !!!", Toast.LENGTH_SHORT).show();
                                //Log.v("response", "Invalid Credentials");
                            }
                        } catch (Exception ex) {
                            //show_dialog_message("Network Error", "Check network connection");
                            Toast.makeText(List_of_waiter.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
                            Log.v("catch err", ex.getMessage());
                        }
                        // progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        //progressDialog.dismiss();
                        //show_dialog_message("Network Error", "Check network connection");
                        Toast.makeText(List_of_waiter.this, "Network Error2", Toast.LENGTH_SHORT).show();
                        //Log.v("Network Error", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "get_waiters");
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0, -1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
