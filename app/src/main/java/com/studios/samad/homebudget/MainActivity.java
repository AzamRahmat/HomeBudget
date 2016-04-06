package com.studios.samad.homebudget;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {




    AvailableAmount availableAmount = AvailableAmount.getInstance();
    DataBaseHandler dataBaseHandler ;
    TextView availableAmountText ,expensesText ,incomeText , budgetText, billsText ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        RelativeLayout expenses_layout = (RelativeLayout) findViewById(R.id.expenses_layout);
        expenses_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, ViewExpensesActivity.class);
                startActivity(myIntent);

            }
        });


        RelativeLayout bills_layout = (RelativeLayout) findViewById(R.id.bills_layout);
        bills_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, ViewBillsActivity.class);
                startActivity(myIntent);

            }
        });

        RelativeLayout income_layout = (RelativeLayout) findViewById(R.id.income_layout);
        income_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, ViewIncomeActivity.class);
                startActivity(myIntent);

            }
        });



        RelativeLayout budget_layout = (RelativeLayout) findViewById(R.id.budget_layout);
        budget_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(MainActivity.this, BudgetMenuActivity.class);
                startActivity(myIntent);

            }
        });

        dataBaseHandler = new DataBaseHandler(this.getApplicationContext());
        dataBaseHandler.setTotalExpenses();
        dataBaseHandler.setTotalIncome();
        dataBaseHandler.setTotalBudget();
        dataBaseHandler.setTotalBills();
        dataBaseHandler.setTotalAvailable();


    }

    @Override
    protected void onResume() {
        super.onResume();
        availableAmountText = (TextView)findViewById(R.id.textView2);
        expensesText  = (TextView)findViewById(R.id.expenses_edit_text);
        incomeText  = (TextView)findViewById(R.id.income_edit_text);
        budgetText  = (TextView)findViewById(R.id.budget_edit_text);
        billsText = (TextView)findViewById(R.id.bills_edit_text);
        displayTotals();


    }

    public void displayTotals() {
        dataBaseHandler.setTotalAvailable();
        TotalAmount obj = TotalAmount.getInstance();
        String expenses = obj.getTotalExpense().toString();

        expensesText.setText(expenses);

        String budget = obj.getTotalBudget().toString();

        budgetText.setText(budget);
        String income = obj.getTotalIncome().toString();

        incomeText.setText(income);
        String bill = obj.getTotalBill().toString();

       billsText.setText(bill);

        availableAmountText.setText(displayAvailableAmount());

    }

    public String displayAvailableAmount() {
        TotalAmount obj = TotalAmount.getInstance();

        Double amount = obj.getTotalAvailable();
        if(amount < 0) {
            amount = -amount;
            return "$" + amount.toString();
        }
        else
        {
            return "$"+amount.toString();
        }
    }

}
