package com.example.aptech.greenfox;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class UploadActivity extends AppCompatActivity {


    ImageButton img_upload_new_image;
    Button btn_new_pic_upload;
    String post,cate,imageurl,name_it, price_it;
    private static final int Gallery_Code=1;
    Uri image_url=null;
    itemStore itemlist;

    final String link="";
    EditText post_new,category_new,img_url_new,item_price_new,item_name_new;

    ProgressDialog progressDialog;
    static Urls urls = new Urls();
    private static final String URL_PROCESSING = urls.url_processing();
    FirebaseDatabase mDatabase;
    DatabaseReference mRef;
    FirebaseStorage mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);

        Intent i=getIntent();
        post=i.getStringExtra("Postd");
        cate=i.getStringExtra("Category");
        imageurl=i.getStringExtra("ImageURL");
        name_it=i.getStringExtra("Item_name");
        price_it=i.getStringExtra("Item_price");

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading please wait...");
        progressDialog.setCancelable(false);

       // Toast.makeText(this, "Post ID: "+post, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "Category: "+cate, Toast.LENGTH_SHORT).show();


        post_new=(EditText) findViewById(R.id.postid_new);
        category_new=(EditText) findViewById(R.id.category_id_new);
        img_url_new=(EditText) findViewById(R.id.image_url_new);
        item_price_new=(EditText) findViewById(R.id.item_price_new);
        item_name_new=(EditText) findViewById(R.id.item_name_new);




        post_new.setText(post);
        category_new.setText(cate);
        item_name_new.setText(name_it);
        item_price_new.setText(price_it);
        img_url_new.setText(imageurl);
        img_upload_new_image=(ImageButton) findViewById(R.id.btn_image);
        btn_new_pic_upload=(Button) findViewById(R.id.btn_up_Pic);


        //mDatabase=FirebaseDatabase.getInstance();
        //mRef=mDatabase.getReference().child(category_new.getText().toString());
        mStorage= FirebaseStorage.getInstance();


        img_upload_new_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(Intent.ACTION_GET_CONTENT);
                i.setType("image/*");
                startActivityForResult(i,Gallery_Code);
            }
        });

        //Toast.makeText(this, "POST ID: "+post, Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==Gallery_Code && resultCode==RESULT_OK) {

            image_url = data.getData();
            img_upload_new_image.setImageURI(image_url);

        }

            btn_new_pic_upload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    progressDialog.setMessage("Please Wait...");
                    progressDialog.show();
                    final String name_item=item_name_new.getText().toString().trim();
                    final String price_item=item_price_new.getText().toString().trim();

                    //final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                    //final DatabaseReference databaseReference = mFirebaseDatabase.getReference().child(cate);



                        StorageReference filepath=FirebaseStorage.getInstance().getReference().child("1").child(String.valueOf(new Date(String.valueOf(java.util.Calendar.getInstance().getTime()))));
                        filepath.putFile(image_url).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                Task<Uri> downloadUrl=taskSnapshot.getStorage().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Uri> task) {
                                        String pic_url=task.getResult().toString();
                                        img_url_new.setText(pic_url);

/*
                                        Map<String,Object> map=new HashMap<>();
                                        map.put("Image",pic_url);
                                        map.put("ItemName",item_name_new.getText().toString());
                                        map.put("Price",item_price_new.getText().toString());
                                        map.put("postID",post_new.getText().toString());
                                        map.put("Category",category_new.getText().toString());

 */


                                        String item_pic=pic_url;
                                        String item_n=item_name_new.getText().toString();
                                        String item_p=item_price_new.getText().toString();
                                        String item_post=post_new.getText().toString();
                                        String item_cate=category_new.getText().toString();
                                        //final String action,final String Post1, final String category1, final String item1name, final String item1price, final String url_image,final Boolean status1)

                                        progressDialog.setMessage("Please Wait...");
                                        progressDialog.show();

                                        new_order_or_suborder_usingVolley("update_items",item_post,item_cate,item_n,item_p,item_pic,true);

                                        if(cate.equals("BreakFast"))
                                        {
                                            Intent ip1=new Intent(getApplicationContext(),ItemListRecycler.class);
                                            startActivity(ip1);
                                           /* ip1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            ip1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                                        }
                                        else if(cate.equals("Lunch"))
                                        {
                                            Intent ip2=new Intent(getApplicationContext(),RecycleLunch.class);
                                            startActivity(ip2);
                                            /*ip2.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            ip2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                                        }
                                        else if(cate.equals("Weekends"))
                                        {
                                            Intent ip3=new Intent(getApplicationContext(),RecycleWeekends.class);
                                            startActivity(ip3);
                                            /*ip3.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            ip3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                                        }
                                        else if(cate.equals("SideDish"))
                                        {
                                            Intent ip4=new Intent(getApplicationContext(),RecycleSideDish.class);
                                            startActivity(ip4);
                                            /*ip4.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            ip4.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/


                                        }
                                        else if(cate.equals("Smoothies"))
                                        {
                                            Intent ip5=new Intent(getApplicationContext(),RecycleSmoothies.class);
                                            startActivity(ip5);
                                            /*ip5.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            ip5.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*/
                                        }

                                        else if(cate.equals("Drinks and Retails"))
                                        {
                                            Intent ip6=new Intent(getApplicationContext(),RecycleDrinks.class);
                                            startActivity(ip6);
                                           /* ip6.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                            ip6.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
*/
                                        }





/*
                                        DatabaseReference newPost=mRef.child(post);
                                        newPost.updateChildren(map).addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                Toast.makeText(UploadActivity.this, "Data Updated Successfully!!!!", Toast.LENGTH_SHORT).show();
                                                if(cate.equals("1"))
                                                {
                                                    Intent ip=new Intent(getApplicationContext(),ItemListRecycler.class);
                                                    startActivity(ip);
                                                }
                                                if(cate.equals("2"))
                                                {
                                                    Intent ip=new Intent(getApplicationContext(),RecycleLunch.class);
                                                    startActivity(ip);
                                                }
                                                if(cate.equals("3"))
                                                {
                                                    Intent ip=new Intent(getApplicationContext(),RecycleWeekends.class);
                                                    startActivity(ip);
                                                }
                                                if(cate.equals("4"))
                                                {
                                                    Intent ip=new Intent(getApplicationContext(),RecycleSideDish.class);
                                                    startActivity(ip);
                                                }
                                                if(cate.equals("5"))
                                                {
                                                    Intent ip=new Intent(getApplicationContext(),RecycleSmoothies.class);
                                                    startActivity(ip);
                                                }

                                                if(cate.equals("6"))
                                                {
                                                    Intent ip=new Intent(getApplicationContext(),RecycleDrinks.class);
                                                    startActivity(ip);
                                                }

                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {

                                            }
                                        });

 */



                                    }
                                });
                            }
                        });




                    }

            });
               }

    private void new_order_or_suborder_usingVolley(final String action, final String Post1, final String category1, final String item1name, final String item1price, final String url_image, final Boolean status1)
    {
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

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
                        progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        progressDialog.dismiss();
                        //  show_dialog_message("Network Error", "Check network connection");
                        Toast.makeText(UploadActivity.this, "Network Error!!!", Toast.LENGTH_SHORT).show();
                        //Log.v("Network Error", error.networkResponse.data.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "update_items");
                //params.put("item_auto_id", "1");
                params.put("post", Post1);
                params.put("item_name", item1name);
                params.put("item_price",item1price);
                params.put("item_category",category1);
                params.put("item_image",url_image);
                params.put("item_status",status1.toString());
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
