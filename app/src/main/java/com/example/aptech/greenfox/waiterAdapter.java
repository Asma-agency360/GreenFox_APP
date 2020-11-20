package com.example.aptech.greenfox;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class waiterAdapter extends RecyclerView.Adapter<waiterAdapter.viewHolder> {



    Context context;
    ArrayList<waiterStore> waiter_list;

    public waiterAdapter(Context c,ArrayList<waiterStore> list_item)
    {
        context=c;
        waiter_list=list_item;
    }



    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new waiterAdapter.viewHolder(LayoutInflater.from(context).inflate(R.layout.waiter_list_row,parent,false));

    }
    @Override
    public void onBindViewHolder(@NonNull final waiterAdapter.viewHolder holder,final int position) {

        holder.t_name.setText("Waiter Name: "+waiter_list.get(position).getAcc_name());
        Log.v("Name of waiter: ",waiter_list.get(position).getAcc_name());
        holder.t_email.setText("Email: "+waiter_list.get(position).getAcc_email());

        //-------------------------------------------------For EDIT WAITER-----------------------------------------------------------//
        holder.b_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.v("Name of waiter",holder.t_name.getText().toString());
                Log.v("Email of waiter",holder.t_name.getText().toString());


                String name_w=waiter_list.get(position).getAcc_name();
                String email_w=waiter_list.get(position).getAcc_email();
                String pass_w=waiter_list.get(position).getAcc_pass();

                Intent myIntent = new Intent(view.getContext(),Update_waiter.class);
                myIntent.putExtra("NameWaiter",name_w);
                myIntent.putExtra("EmailWaiter",email_w);
                myIntent.putExtra("PassWaiter",pass_w);
                Log.v("PAssword of Waiter: ",pass_w);
                view.getContext().startActivity(myIntent);
            }
        });



        //------------------------------------------------For Delete Waiter----------------------------------------------------------//
        holder.b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder=new AlertDialog.Builder(holder.t_email.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete.....?");

                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        final String email_of_waiter=waiter_list.get(position).getAcc_email();
                        delete_waiter_from_volley("delete_user",email_of_waiter);

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
        return waiter_list.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView t_name,t_email;
        ImageButton b_edit,b_delete;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            t_name=(TextView) itemView.findViewById(R.id.waiter_name_in_recycle);
            t_email=(TextView) itemView.findViewById(R.id.waiter_email_in_recycler);

            b_edit=(ImageButton) itemView.findViewById(R.id.btn_edit_waiter);
            b_delete=(ImageButton) itemView.findViewById(R.id.btn_delete_waiter);
        }
    }



    private void delete_waiter_from_volley(final String id, final String waiterEmail)
    {
        //progressDialog.setMessage("Please Wait...");
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
                        }
                        catch(Exception ex)
                        {
                            //show_dialog_message("Network Error", "Check network connection");
                            Toast.makeText(context, "Network Error", Toast.LENGTH_SHORT).show();
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
                params.put("action", "delete_user");
                //params.put("item_auto_id", "1");
                params.put("user_id", id);
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



