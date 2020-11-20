package com.example.aptech.greenfox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class WaiterDashboard extends AppCompatActivity {

    CardView w_add,w_list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiter_dashboard);
        w_add=(CardView) findViewById(R.id.card_add_waiter);
        w_list=(CardView) findViewById(R.id.card_list_of_Waiter);

        w_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent n=new Intent(getApplicationContext(),List_of_waiter.class);
                startActivity(n);
            }
        });

        w_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent n=new Intent(getApplicationContext(),AddNewWaiter.class);
                startActivity(n);

            }
        });
    }
}
