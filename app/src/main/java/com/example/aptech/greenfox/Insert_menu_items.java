package com.example.aptech.greenfox;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Insert_menu_items extends AppCompatActivity {

    String ItemName, ItemPrice, Category;
    private Context context;
    ProgressDialog progressDialog;

    static Urls urls = new Urls();
    private static final String URL_PROCESSING = urls.url_processing();

    ImageView iv_selected_image;

    private static final String IMAGE_DIRECTORY = "/demonuts";

    Uri fileUri;

    Button btn_Attachments;

    EditText et_item_name, et_price;

    Button button2;


    String category;

    ArrayList arrayList = new ArrayList();

    String[] items = {"Breakfast", "Lunch", "Weekends", "sides", "Smoothies", "Drinks and retails"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_menu_items);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Uploading please wait...");
        progressDialog.setCancelable(false);

        final AutoCompleteTextView autoComplete = (AutoCompleteTextView) findViewById(R.id.autoComplete);
        autoComplete.clearListSelection();

        et_item_name = findViewById(R.id.et_item_name);
        et_price = findViewById(R.id.et_price);
        btn_Attachments = findViewById(R.id.btn_Attachments);
        button2 = findViewById(R.id.btn_registration);

        btn_Attachments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ///====>>>> intent to image cropper library<<===///
                Intent intent = CropImage.activity().getIntent(Insert_menu_items.this);
                startActivityForResult(intent, CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE);
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, items);
        //Getting the instance of AutoCompleteTextView

        autoComplete.setThreshold(0);//will start working from first character
        autoComplete.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        autoComplete.setTextColor(Color.RED);


        autoComplete.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                autoComplete.showDropDown();
                return false;
            }
        });

        autoComplete.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {


                if (autoComplete.getText().toString() .equals("Breakfast")  )
                {
                    category = "1";
                }
                else if (autoComplete.getText().toString() .equals("Lunch")   )
                {
                  category = "2";
                }
                else if (autoComplete.getText().toString() .equals("Weekends")   )
                {
                    category = "3";
                }
                else if (autoComplete.getText().toString() .equals("sides")   )
                {
                    category = "4";
                }
                else if (autoComplete.getText().toString() .equals("Smoothies")   )
                {
                    category = "5";
                }
                else if (autoComplete.getText().toString() .equals("Drinks and retails")   )
                {
                    category = "6";
                }
                else
                    {
                        category = autoComplete.getText().toString();
                        Global.category = autoComplete.getText().toString();
                    }


            }
        });


        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if ((Global.itemDetailsModelClass.getItemPic() != null) &&

                        !et_item_name.getText().toString().isEmpty() &&
                        !et_price.getText().toString().isEmpty()) {
                    progressDialog.show();
                    uploadImagetoFirebaseStorage(fileUri);
                } else {

                    btn_Attachments.setError("Select Image first");
                    btn_Attachments.requestFocus();

                    autoComplete.setError("Select category");
                    autoComplete.requestFocus();

                    et_item_name.setError("Enter Model number");
                    et_item_name.requestFocus();

                    et_price.setError("Enter Price");
                    et_price.requestFocus();

                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);

            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();

                Global.itemDetailsModelClass.ItemPic = fileUri;
                btn_Attachments.setVisibility(View.VISIBLE);
                btn_Attachments.setText(Global.itemDetailsModelClass.ItemPic.toString());
                //  Picasso.get().load(Global.chairModel.getChairPic()).into(iv_selected_image);

                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), fileUri);
                    String path = saveImage(bitmap);
                    //  frg_newsFeed_listItem_circleImageViiew.setImageBitmap(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {

            Toast.makeText(getApplicationContext(), "An error occured", Toast.LENGTH_SHORT).show();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(
                Environment.getExternalStorageDirectory() + IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists()) {
            wallpaperDirectory.mkdirs();
        }
        try {
            File f = new File(wallpaperDirectory, Calendar.getInstance()
                    .getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(this,
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();
            Log.d("TAG", "File Saved::--->" + f.getAbsolutePath());
            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }


    private void uploadImagetoFirebaseStorage(Uri fileUri) {

        if (Global.itemDetailsModelClass.getItemPic() != null) {

            //  progressDialog.show();
            StorageReference ref = FirebaseStorage.getInstance().getReference().child("1").child(String.valueOf(new Date(String.valueOf(java.util.Calendar.getInstance().getTime()))));
            ref.putFile(Global.itemDetailsModelClass.getItemPic())
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> result = taskSnapshot.getMetadata().getReference().getDownloadUrl();
                            result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {

                                    UploadPostToDatabase(uri);
                                    //   register(Global.userData.name, Global.userData.dob,  Global.userData.phoneNumber, Global.userData.gender, Global.userData.pin, uri.toString());

                                }
                            });

                            //postData(taskSnapshot);
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            //progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                    .getTotalByteCount());
                            //  progressDialog.setMessage("Uploading Blog...");
                        }
                    });
        }
    }

    public void UploadPostToDatabase(final Uri uri) {
/*
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM 'at' HH:mm");
        String postID = FirebaseDatabase.getInstance().getReference(category).push().getKey();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference(category).child(postID);

 */

        ItemName = et_item_name.getText().toString();
        ItemPrice = et_price.getText().toString();

        HashMap<String, Object> hashMap = new HashMap<String, Object>();

        hashMap.put("Category", category);
        hashMap.put("Price", ItemPrice);
        hashMap.put("Image", uri.toString());
        //hashMap.put("postID", postID);
        hashMap.put("ItemName", ItemName);
        Log.v("Item Category",category);
        Log.v("Item Name",ItemName);
        Log.v("Image URL",uri.toString());

       new_order_or_suborder_usingVolley("add_items",category, ItemName,ItemPrice,uri.toString(),true);


        //reference.updateChildren(hashMap);

        progressDialog.dismiss();

        new PrettyDialog(this)
                .setTitle("Item Uploaded!")
                .setIcon(R.drawable.greenfoxlogo)
                .setMessage("Successfully uploaded!")
                .addButton("OK",
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_green,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {

                                Intent intent = new Intent(Insert_menu_items.this, Insert_menu_items.class);
                                startActivity(intent);
                                progressDialog.dismiss();
                                finish();

                            }
                        }).show();

    }

    private void new_order_or_suborder_usingVolley(final String action, final String category1, final String item1name, final String item1price, final String url_image,final Boolean status1)
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
                        Toast.makeText(context, "Network Error!!!", Toast.LENGTH_SHORT).show();
                        //Log.v("Network Error", error.networkResponse.data.toString());
                    }
                })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String, String> params = new HashMap<>();
                params.put("action", "add_items");
                //params.put("item_auto_id", "1");
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

