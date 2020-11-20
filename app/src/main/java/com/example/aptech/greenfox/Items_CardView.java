package com.example.aptech.greenfox;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class Items_CardView extends AppCompatActivity {

    CardView breakfast,lunch,weekends,drinks,smothies,sides;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_items__card_view);

        breakfast=(CardView) findViewById(R.id.card_breakfast);
        lunch=(CardView) findViewById(R.id.card_Lunch);
        weekends=(CardView) findViewById(R.id.card_Weekends);
        drinks=(CardView) findViewById(R.id.card_drinks_and_retails);
        smothies=(CardView) findViewById(R.id.card_smothies);
        sides=(CardView) findViewById(R.id.card_side);



        breakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),ItemListRecycler.class);
                startActivity(i);
            }
        });

        lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RecycleLunch.class);
                startActivity(i);
                finish();
            }
        });

        weekends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RecycleWeekends.class);
                startActivity(i);
                finish();
            }
        });

        sides.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RecycleSideDish.class);
                startActivity(i);
                finish();
            }
        });
        drinks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RecycleDrinks.class);
                startActivity(i);
                finish();
            }
        });
        smothies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getApplicationContext(),RecycleSmoothies.class);
                startActivity(i);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {

        Intent n=new Intent(getApplicationContext(),Admin_dashboard.class);
        startActivity(n);
        finish();
        super.onBackPressed();
    }
}
