package com.studios.samad.homebudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * Created by Omer on 31-Mar-16.
 */
public class BudgetMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_budget);

        RelativeLayout expenses_layout = (RelativeLayout) findViewById(R.id.rent_layout);
        expenses_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(BudgetMenuActivity.this, ViewBudgetActivity.class);
                startActivity(myIntent);

            }
        });

        RelativeLayout food_layout = (RelativeLayout) findViewById(R.id.food_layout);
        food_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(BudgetMenuActivity.this, AddBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout dep_layout = (RelativeLayout) findViewById(R.id.dep_layout);
        dep_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(BudgetMenuActivity.this, AddBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout ent_layout = (RelativeLayout) findViewById(R.id.ent_layout);
        ent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(BudgetMenuActivity.this, AddBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout car_layout = (RelativeLayout) findViewById(R.id.car_layout);
        car_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(BudgetMenuActivity.this, AddBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout med_layout = (RelativeLayout) findViewById(R.id.med_layout);
        med_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(BudgetMenuActivity.this, AddBudgetActivity.class);
                startActivity(myIntent);

            }
        });
    }
}
