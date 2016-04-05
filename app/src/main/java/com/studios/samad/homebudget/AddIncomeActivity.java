package com.studios.samad.homebudget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Omer on 24-Mar-16.
 */
public class AddIncomeActivity extends AppCompatActivity {

    ArrayList<AddIncomeActivity> data = new ArrayList<AddIncomeActivity>();

    private AddIncomeActivity objLoc;
    private int inc_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtIncName;
    EditText txtIncAmount;
    EditText txtIncDate;

    private Preferences p;

    private int income_id;
    private String  str_income_name;
    private String  str_income_amount;
    private String str_income_date;


    public int getIncome_id() {

        return income_id;
    }

    public void setIncome_id(int income_id) {

        this.income_id = income_id;
    }

    public String getIncome_amount() {

        return str_income_amount;
    }

    public void setIncome_amount(String income_amount) {
        this.str_income_amount = income_amount;
    }

    public String getIncome_date() {
        return str_income_date;
    }

    public void setIncome_date(String income_date) {
        this.str_income_date = income_date;
    }

    public String getIncome_name() {
        return str_income_name;
    }

    public void setIncome_name(String income_name) {
        this.str_income_name = income_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_incdate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtIncDate.requestFocus();

            inc_id =  Integer.parseInt(p.getPrefVal("Income", "0"));
            if(inc_id > 0)
            {
                txtIncName.setEnabled(false);
                objLoc =  dbHandler.getIncome(income_id);
                txtIncName.setText(objLoc.getIncome_name());
                txtIncAmount.setText(objLoc.getIncome_amount());
                txtIncDate.setText(objLoc.getIncome_date());
                //  isupdate = true;
            }
            else
            {
                //   isupdate = false;
            }


        }
        catch(Exception ex)
        {

        }

        super.onResume();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_income);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddIncomeActivity.this);

        txtIncName = (EditText) findViewById(R.id.inc_edit_type);
        txtIncAmount = (EditText) findViewById(R.id.inc_edit_amount);
        txtIncDate = (EditText) findViewById(R.id.inc_select_date);
        txtIncDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.inc_btn_save);
        btnCancel = (Button) findViewById(R.id.inc_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddIncome();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddIncomeActivity.this, ViewIncomeActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtIncName.setText("");
        txtIncAmount.setText("");
        txtIncDate.setText("");
        p.setPrefVal("Income", "0");
    }





    private void AddIncome()
    {

        try {

            // Adding values to variables then in database

            str_income_name = txtIncName.getText().toString();
            str_income_amount = txtIncAmount.getText().toString();
            str_income_date = txtIncDate.getText().toString();

            // checking for existing income
            if(! dbHandler.CheckIncomeEXIST(AddIncomeActivity.this)
                    ){
                dbHandler.AddIncome(AddIncomeActivity.this);

                // Display a success messege
                Toast.makeText(AddIncomeActivity.this,
                        "Income Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddIncomeActivity.this,
                        " Income already exists. ", Toast.LENGTH_SHORT).show();
                txtIncName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddIncomeActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateIncome()
    {

        try {

            // Adding values to variables then in database

            str_income_name = txtIncName.getText().toString();
            str_income_amount = txtIncAmount.getText().toString();
            str_income_date = txtIncDate.getText().toString();

            //Update db
            dbHandler.UpdateIncome(AddIncomeActivity.this);

            // Display a success messege
            Toast.makeText(AddIncomeActivity.this,
                    "Income Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddIncomeActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePciker
    public void showDialogOnButtonClick(){

        txt_incdate = (EditText)findViewById(R.id.inc_select_date);

        txt_incdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogOnButtonClick();

                showDialog(Dialog_Id);
            }
        });

    }
    @Override
    protected Dialog onCreateDialog(int id){

        if (id == Dialog_Id) {
            return new DatePickerDialog(this, dpickerListener, year_x, month_x, day_x);
        }
        return  null;
    }

    private DatePickerDialog.OnDateSetListener dpickerListener =
            new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                    year_x = year;
                    month_x = monthOfYear + 1;
                    day_x = dayOfMonth;

                    txt_incdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
