package com.example.aptech.greenfox;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.ViewHolder;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecyclerFirebaseADapter extends FirebaseRecyclerAdapter<itemStore,RecyclerFirebaseADapter.myviewholder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    Context context;
    ArrayList<itemStore> items_list;
    public RecyclerFirebaseADapter(@NonNull FirebaseRecyclerOptions<itemStore> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final RecyclerFirebaseADapter.myviewholder holder,final int position, @NonNull itemStore model) {

        holder.t_name.setText("Name: "+items_list.get(position).getItemName());
        holder.t_price.setText("Price: "+items_list.get(position).getPrice());
        Picasso.get().load(items_list.get(position).getImage()).into(holder.img);

        //Glide.with(holder.img.getContext()).load(model.getImage()).into(holder.img);


        holder.b_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final DialogPlus dialogPlus=DialogPlus.newDialog(holder.img.getContext())
                        .setContentHolder(new ViewHolder(R.layout.edit_dialog_content))
                        .setExpanded(true,1100)
                        .create();

                View myview=dialogPlus.getHolderView();
                final EditText iurl=myview.findViewById(R.id.uimgurl);
                final EditText name=myview.findViewById(R.id.item_name_update);
                final EditText price=myview.findViewById(R.id.item_price_update);
                final Button submit=myview.findViewById(R.id.btn_submit_update);
                final EditText postid=myview.findViewById(R.id.postid);
                final  EditText categoryid=myview.findViewById(R.id.categoryID);


                iurl.setText(items_list.get(position).getImage());
                name.setText(items_list.get(position).getItemName());
                price.setText(items_list.get(position).getPrice());
                postid.setText(items_list.get(position).getPostID());
                categoryid.setText(items_list.get(position).getCategory());


                dialogPlus.show();

                final String cat=categoryid.getText().toString();
                final String POSTID=postid.getText().toString();

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        final FirebaseDatabase mFirebaseDatabase = FirebaseDatabase.getInstance();
                        final DatabaseReference databaseReference = mFirebaseDatabase.getReference().child(cat);
                        //String postID=items_list.get(position).getPostID();
                        //DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("1");

                        Map<String,Object> map=new HashMap<>();
                        map.put("Image",iurl.getText().toString());
                        map.put("ItemName",name.getText().toString());
                        map.put("Price",price.getText().toString());
                        map.put("postID",postid.getText().toString());
                        map.put("Category",categoryid.getText().toString());

                        final  String POSTID=postid.getText().toString();

                        FirebaseDatabase.getInstance().getReference().child(cat)
                                .child(getRef(position).getKey()).updateChildren(map)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        dialogPlus.dismiss();
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        dialogPlus.dismiss();
                                    }
                                });
                    }
                });


            }
        });


        holder.b_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder=new AlertDialog.Builder(holder.img.getContext());
                builder.setTitle("Delete Panel");
                builder.setMessage("Delete...?");

                final String cat1=items_list.get(position).getCategory().toString();
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseDatabase.getInstance().getReference().child(cat1)
                                .child(getRef(position).getKey()).removeValue();
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

    @NonNull
    @Override
    public RecyclerFirebaseADapter.myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_list_row,parent,false);
        return new myviewholder(view);
    }

    public class myviewholder extends RecyclerView.ViewHolder {

        TextView t_name,t_price,t_post;
        ImageView img;
        ImageButton b_edit,b_delete;
        public myviewholder(@NonNull View itemView) {
            super(itemView);
            t_name=(TextView) itemView.findViewById(R.id.item_name_in_recycle);
            t_price=(TextView) itemView.findViewById(R.id.item_price_in_recycle);
            img=(ImageView) itemView.findViewById(R.id.image_recycle);

            b_edit=(ImageButton) itemView.findViewById(R.id.btn_edit_record);
            b_delete=(ImageButton) itemView.findViewById(R.id.btn_delete_record);
        }
    }
}
