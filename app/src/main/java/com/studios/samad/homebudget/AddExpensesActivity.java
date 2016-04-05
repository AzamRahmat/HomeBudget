package com.studios.samad.homebudget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddExpensesActivity extends AppCompatActivity {

    ArrayList<AddExpensesActivity> data = new ArrayList<AddExpensesActivity>();

    private AddExpensesActivity objLoc;
    private int exp_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtExpName;
    EditText txtExpAmount;
    EditText txtExpDate;

    private Preferences p;

    private int expenses_id;
    private String  str_expenses_name;
    private String  str_expenses_amount;
    private String str_expenses_date;


    public int getExpenses_id() {

        return expenses_id;
    }

    public void setExpenses_id(int expenses_id) {

        this.expenses_id = expenses_id;
    }

    public String getExpenses_amount() {

        return str_expenses_amount;
    }

    public void setExpenses_amount(String expenses_amount) {
        this.str_expenses_amount = expenses_amount;
    }

    public String getExpenses_date() {
        return str_expenses_date;
    }

    public void setExpenses_date(String expenses_date) {
        this.str_expenses_date = expenses_date;
    }

    public String getExpenses_name() {
        return str_expenses_name;
    }

    public void setExpenses_name(String expenses_name) {
        this.str_expenses_name = expenses_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_expdate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtExpDate.requestFocus();

            exp_id =  Integer.parseInt(p.getPrefVal("Expenses", "0"));
            if(exp_id > 0)
            {
                txtExpName.setEnabled(false);
                objLoc =  dbHandler.getExpenses(expenses_id);
                txtExpName.setText(objLoc.getExpenses_name());
                txtExpAmount.setText(objLoc.getExpenses_amount());
                txtExpDate.setText(objLoc.getExpenses_date());
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
        setContentView(R.layout.activity_add_expenses);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddExpensesActivity.this);

        txtExpName = (EditText) findViewById(R.id.exp_edit_type);
        txtExpAmount = (EditText) findViewById(R.id.exp_edit_amount);
        txtExpDate = (EditText) findViewById(R.id.exp_select_date);
        txtExpDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.exp_btn_save);
        btnCancel = (Button) findViewById(R.id.exp_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddExpenses();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddExpensesActivity.this, ViewExpensesActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtExpName.setText("");
        txtExpAmount.setText("");
        txtExpDate.setText("");
        p.setPrefVal("Expenses", "0");
    }





        private void AddExpenses()
        {

            try {

                // Adding values to variables then in database

                str_expenses_name = txtExpName.getText().toString();
                str_expenses_amount = txtExpAmount.getText().toString();
                str_expenses_date = txtExpDate.getText().toString();

                // checking for existing location
                if(! dbHandler.CheckExpensesEXIST(AddExpensesActivity.this)
                        ){
                    dbHandler.AddExpenses(AddExpensesActivity.this);

                    // Display a success messege
                    Toast.makeText(AddExpensesActivity.this,
                            "Expense Added !", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(AddExpensesActivity.this,
                            " Location already exists. ", Toast.LENGTH_SHORT).show();
                    txtExpName.selectAll();

                }



            } catch (Exception ex) {
                Toast.makeText(AddExpensesActivity.this,
                        ex.getMessage(), Toast.LENGTH_SHORT).show();
            }



        }


    private void UpdateExpenses()
    {

        try {

            // Adding values to variables then in database

            str_expenses_name = txtExpName.getText().toString();
            str_expenses_amount = txtExpAmount.getText().toString();
            str_expenses_date = txtExpDate.getText().toString();

            //Update db
            dbHandler.UpdateExpenses(AddExpensesActivity.this);

            // Display a success messege
            Toast.makeText(AddExpensesActivity.this,
                    "Location Updated !", Toast.LENGTH_SHORT).show();
           // clear();


        } catch (Exception ex) {
            Toast.makeText(AddExpensesActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






// DatePciker
    public void showDialogOnButtonClick(){

        txt_expdate = (EditText)findViewById(R.id.exp_select_date);

        txt_expdate.setOnClickListener(new View.OnClickListener() {
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

                    txt_expdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
