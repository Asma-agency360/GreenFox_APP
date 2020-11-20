package com.example.aptech.greenfox;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecycleDrinks extends AppCompatActivity {

    DatabaseReference ref;
    RecyclerView rec;
    ArrayList<itemStore> list;
    MyRecyclerAdapter adapter1;
    itemStore eachitem;

    static Urls urls = new Urls();
    private static final String URL_PROCESSING = urls.url_processing();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycle_drinks);
        rec=(RecyclerView) findViewById(R.id.id_recycler_drink);
        rec.setLayoutManager(new LinearLayoutManager(this));
        list=new ArrayList<itemStore>();

        getitems_usingVolley("get_enabled_items", "6", list);


      /*  ref= FirebaseDatabase.getInstance().getReference().child("6");
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for(DataSnapshot snap:snapshot.getChildren())
                {
                    itemStore i=snap.getValue(itemStore.class);
                    list.add(i);
                }
                adapter1=new MyRecyclerAdapter(RecycleDrinks.this,list);

                rec.setAdapter(adapter1);
                adapter1.notifyDataSetChanged();
                rec.invalidate();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(RecycleDrinks.this, "Opsssssss.......", Toast.LENGTH_SHORT).show();

            }
        });

       */
    }

    @Override
    public void onBackPressed() {

        Intent n=new Intent(getApplicationContext(),Items_CardView.class);
        startActivity(n);
        finish();
        super.onBackPressed();
    }

    private void getitems_usingVolley(final String action, final String category_id, final ArrayList<itemStore> list1) {

        //volley code
        itemStore eachItem = new itemStore();

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

                            //Log.v("response", String.valueOf(jsonArray_response_data));

                            if (response_status.equals("success")) {
                                for (int i = 0; i < jsonArray_response_data.length(); i++) {
                                    JSONObject jsonObject_suborder = jsonArray_response_data.getJSONObject(i);
                                    String item_auto_id = jsonObject_suborder.optString("item_auto_id");
                                    String item_name = jsonObject_suborder.optString("item_name");
                                    String item_image = jsonObject_suborder.optString("item_image");
                                    String item_price = jsonObject_suborder.optString("item_price");
                                    //       CreateCardViewsForItem(item_auto_id, item_name, "€", item_price, item_image);
                                    eachitem = new itemStore(item_image, item_name, item_price, item_auto_id, "Drinks and Retails");
                                    list1.add(eachitem);

                                }
                                adapter1 = new MyRecyclerAdapter(RecycleDrinks.this, list1);
                                rec.setAdapter(adapter1);
                                Log.v("List of Items" + list1.size(), "itemslist");

                                //Log.v("jsonObject_table1", String.valueOf(arrayList_sub_orders));
                            } else {
                                // show_dialog_message("No Items", "No Enabled Items");
                                Toast.makeText(RecycleDrinks.this, "No items !!!", Toast.LENGTH_SHORT).show();
                                //Log.v("response", "Invalid Credentials");
                            }
                        } catch (Exception ex) {
                            //show_dialog_message("Network Error", "Check network connection");
                            Toast.makeText(RecycleDrinks.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(RecycleDrinks.this, "Network Error2", Toast.LENGTH_SHORT).show();
                        //Log.v("Network Error", error.getMessage());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", action);
                params.put("category_id", category_id);
                // params.put("List",list1);
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
