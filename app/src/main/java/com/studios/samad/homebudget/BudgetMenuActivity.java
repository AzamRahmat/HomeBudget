package com.studios.samad.homebudget;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by Omer on 31-Mar-16.
 */
public class BudgetMenuActivity extends AppCompatActivity {

    private Double[] total = new Double[6];

    String[] category = {"Home","Food","Departmental","Entertainment","Car","Medical"};
    TextView[] textCategory = new TextView[6];
    DataBaseHandler dataBaseHandler ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_budget);

        final AddBudgetActivity addBudgetActivity = new AddBudgetActivity();

        RelativeLayout expenses_layout = (RelativeLayout) findViewById(R.id.rent_layout);
        expenses_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBudgetActivity.setSelected_Budget_catg("Home");
                Intent myIntent = new Intent(BudgetMenuActivity.this, ViewBudgetActivity.class);
                startActivity(myIntent);

            }
        });

        RelativeLayout food_layout = (RelativeLayout) findViewById(R.id.food_layout);
        food_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBudgetActivity.setSelected_Budget_catg("Food");
                Intent myIntent = new Intent(BudgetMenuActivity.this, ViewBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout dep_layout = (RelativeLayout) findViewById(R.id.dep_layout);
        dep_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBudgetActivity.setSelected_Budget_catg("Departmental");
                Intent myIntent = new Intent(BudgetMenuActivity.this, ViewBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout ent_layout = (RelativeLayout) findViewById(R.id.ent_layout);
        ent_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBudgetActivity.setSelected_Budget_catg("Entertainment");
                Intent myIntent = new Intent(BudgetMenuActivity.this, ViewBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout car_layout = (RelativeLayout) findViewById(R.id.car_layout);
        car_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBudgetActivity.setSelected_Budget_catg("Car");
                Intent myIntent = new Intent(BudgetMenuActivity.this, ViewBudgetActivity.class);
                startActivity(myIntent);

            }
        });
        RelativeLayout med_layout = (RelativeLayout) findViewById(R.id.med_layout);
        med_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addBudgetActivity.setSelected_Budget_catg("Medical");
                Intent myIntent = new Intent(BudgetMenuActivity.this, ViewBudgetActivity.class);
                startActivity(myIntent);

            }
        });



        dataBaseHandler = new DataBaseHandler(this.getApplicationContext());

        textCategory[0]= (TextView)findViewById(R.id.slct_bgt_home_amount);
        textCategory[1]= (TextView)findViewById(R.id.slct_bgt_food_amount);
        textCategory[2]= (TextView)findViewById(R.id.slct_bgt_dept_amount);
        textCategory[3]= (TextView)findViewById(R.id.slct_bgt_ent_amount);
        textCategory[4]= (TextView)findViewById(R.id.slct_bgt_car_amount);
        textCategory[5]= (TextView)findViewById(R.id.slct_bgt_med_amount);


    }


    public Double getTotal(int i) {
        if (total[i]==null) {
            total[i] = 0.0;
        }
        return total[i];
    }

    public void setTotal(Double total,int i) {
        this.total[i] = total;
    }

    @Override
    protected void onResume() {
        super.onResume();
       for(int i=0; i<6; i++) {
           setTotal(dataBaseHandler.getTotalBudgetCategory(category[i]),i);
           textCategory[i].setText("$" + getTotal(i).toString());
       }
    }
}
