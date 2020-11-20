package com.example.aptech.greenfox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import libs.mjn.prettydialog.PrettyDialog;
import libs.mjn.prettydialog.PrettyDialogCallback;

public class Admin_dashboard extends AppCompatActivity {

    CardView c_add_item,c_list_view,c_wiater_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        c_add_item=findViewById(R.id.card_add);
        c_list_view=findViewById(R.id.card_list);
        c_wiater_add=findViewById(R.id.card_add_new_waiter);


        c_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Insert_menu_items.class);
                startActivity(i);
                finish();
            }
        });


        c_list_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),Items_CardView.class);
                startActivity(i);
                finish();
            }
        });

        c_wiater_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),WaiterDashboard.class);
                startActivity(i);
                finish();

            }
        });

    }

    @Override
    public void onBackPressed() {
        new PrettyDialog(this)
                .setTitle("Exit ")
                .setIcon(R.drawable.greenfoxlogo)
                .setMessage("Are you sure you want to exit!!!!")
                .addButton("YES",
                        R.color.pdlg_color_white,  // button text color
                        R.color.pdlg_color_green,  // button background color
                        new PrettyDialogCallback() {  // button OnClick listener
                            @Override
                            public void onClick() {

                                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                                homeIntent.addCategory( Intent.CATEGORY_HOME );
                                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(homeIntent);
                                finishAffinity();
                                System.exit(0);
                            }
                        }).addButton("No",
                R.color.pdlg_color_white,  // button text color
                R.color.pdlg_color_green,  // button background color
                new PrettyDialogCallback() {  // button OnClick listener
                    @Override
                    public void onClick() {
                        Intent n=new Intent(getApplicationContext(),Admin_dashboard.class);
                        n.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                        startActivity(n);
                        finish();



                    }
                }).show();

        //super.onBackPressed();
    }
}
