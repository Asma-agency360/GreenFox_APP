package com.example.aptech.greenfox;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.provider.ContactsContract;
import android.provider.MediaStore;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Gallery;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.itextpdf.text.factories.GreekAlphabetFactory;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyRecyclerAdapter extends RecyclerView.Adapter<MyRecyclerAdapter.MyViewHolder> {

   Context context;
   ArrayList<itemStore>  items_list;

   public MyRecyclerAdapter(Context c,ArrayList list_item)
   {
       context=c;
       items_list=list_item;
   }

    @NonNull
    @Override
    public MyRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.recycler_list_row,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final MyRecyclerAdapter.MyViewHolder holder,final int position) {

       holder.t_name.setText("Name: "+items_list.get(position).getItemName());
       holder.t_price.setText("Price: "+items_list.get(position).getPrice());
       Picasso.get().load(items_list.get(position).getImage()).into(holder.img);
       final String pos;



        //update the recycler item
        holder.b_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String url_i=items_list.get(position).getImage();
                String name_i= items_list.get(position).getItemName();
                String price_i=items_list.get(position).getPrice();
                String post_i=items_list.get(position).getPostID();
                String category_i=items_list.get(position).getCategory();

                Intent myIntent = new Intent(view.getContext(),UploadActivity.class);
                myIntent.putExtra("ImageURL",url_i);
                myIntent.putExtra("Postd",post_i);
                myIntent.putExtra("Category",category_i);
                myIntent.putExtra("Item_name",name_i);
                myIntent.putExtra("Item_price",price_i);
                view.getContext().startActivity(myIntent);
                ((Activity)context).finish();

            }
        });

        holder.b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {

                final AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete.....?");


                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        final String cat1=items_list.get(position).getCategory().toString();
                        final String p_post=items_list.get(position).getPostID().toString();

                        Log.v("POST ID",p_post);

                        new_order_or_suborder_usingVolley("delete_items",p_post);

                        if(cat1.equals("BreakFast"))
                        {

                            view.getContext().startActivity(new Intent(context, ItemListRecycler.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            ((Activity)context).finish();

                        }
                        if(cat1.equals("Lunch"))
                        {
                            view.getContext().startActivity(new Intent(context, RecycleLunch.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            ((Activity)context).finish();
                        }
                        if(cat1.equals("Weekends"))
                        {
                            view.getContext().startActivity(new Intent(context, RecycleWeekends.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            ((Activity)context).finish();
                        }
                        if(cat1.equals("SideDish"))
                        {
                            view.getContext().startActivity(new Intent(context, RecycleSideDish.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            ((Activity)context).finish();
                        }
                        if(cat1.equals("Smoothies"))
                        {
                            view.getContext().startActivity(new Intent(context, RecycleSmoothies.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            ((Activity)context).finish();
                        }
                        if(cat1.equals("Drinks and Retails"))
                        {
                            view.getContext().startActivity(new Intent(context, RecycleDrinks.class).addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                            ((Activity)context).finish();
                        }


                    }
                });

                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                builder.show();
            }
        });

    }


    @Override
    public int getItemCount() {
        return items_list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

       TextView t_name,t_price,t_post;
       ImageView img;
       ImageButton b_edit,b_delete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            t_name=(TextView) itemView.findViewById(R.id.item_name_in_recycle);
            t_price=(TextView) itemView.findViewById(R.id.item_price_in_recycle);
            img=(ImageView) itemView.findViewById(R.id.image_recycle);

            b_edit=(ImageButton) itemView.findViewById(R.id.btn_edit_record);
            b_delete=(ImageButton) itemView.findViewById(R.id.btn_delete_record);


        }
    }

    private void new_order_or_suborder_usingVolley(final String action, final String itempost)
    {
       // progressDialog.setMessage("Please Wait...");
        //progressDialog.show();

        //volley code
         Urls urls = new Urls();
         final String URL_PROCESSING = urls.url_processing();

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

                                Toast.makeText(context, response_msg, Toast.LENGTH_SHORT).show();
                                //Log.v("jsonObject_table1", String.valueOf(arrayList_sub_orders));
                            }
                            else
                            {
                                // show_dialog_message("No Categories", "No Enabled Categories");
                                Toast.makeText(context, "No categories", Toast.LENGTH_SHORT).show();
                                Log.v("response", "Invalid Credentials");
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
                            //Toast.makeText(, "Network Error", Toast.LENGTH_SHORT).show();
                            Log.v("catch err", ex.getMessage());
                        }
                      // progressDialog.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                      //  progressDialog.dismiss();
                        //  show_dialog_message("Network Error", "Check network connection");
                        Toast.makeText(context, "Network Error!!!", Toast.LENGTH_SHORT).show();
                        //Log.v("Network Error", error.networkResponse.data.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "delete_items");
                //params.put("item_auto_id", "1");
                params.put("item_id", itempost);
                return params;
            }
        };

        DefaultRetryPolicy retryPolicy = new DefaultRetryPolicy(0,-1,DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue requestQueue = Volley.newRequestQueue(context);
        requestQueue.getCache().clear();
        requestQueue.add(stringRequest);
    }
}
