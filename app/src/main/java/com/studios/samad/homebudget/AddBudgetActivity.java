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

public class AddBudgetActivity extends AppCompatActivity {

    ArrayList<AddBudgetActivity> data = new ArrayList<AddBudgetActivity>();

    private AddBudgetActivity objLoc;
    private int bgt_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtBgtName;
    EditText txtBgtAmount;
    EditText txtBgtDate;

    private Preferences p;

    private int Budget_id;
    private String str_Budget_catg;
    private String  str_Budget_amount;
    private String str_Budget_date;
    private static String selected_Budget_catg;


    public String getSelected_Budget_catg() {
        return selected_Budget_catg;
    }

    public void setSelected_Budget_catg(String selected_Budget_catg) {
        this.selected_Budget_catg = selected_Budget_catg;
    }




    public int getBudget_id() {

        return Budget_id;
    }
    public void setBudget_id() {


    }
    public void setBudget_id(int Budget_id) {

        this.Budget_id = Budget_id;
    }

    public String getBudget_amount() {

        return str_Budget_amount;
    }

    public void setBudget_amount(String Budget_amount) {
        this.str_Budget_amount = Budget_amount;
    }

    public String getBudget_date() {
        return str_Budget_date;
    }

    public void setBudget_date(String Budget_date) {
        this.str_Budget_date = Budget_date;
    }

    public String getBudget_catg() {
        return str_Budget_catg;
    }

    public void setBudget_catg(String Budget_name) {
        this.str_Budget_catg = Budget_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_bgtdate;




    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtBgtDate.requestFocus();

            bgt_id =  Integer.parseInt(p.getPrefVal("Budget", "0"));
            if(bgt_id > 0)
            {
                txtBgtName.setEnabled(false);
                objLoc =  dbHandler.getBudget(Budget_id);
                txtBgtName.setText(objLoc.getBudget_catg());
                txtBgtAmount.setText(objLoc.getBudget_amount());
                txtBgtDate.setText(objLoc.getBudget_date());
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
        setContentView(R.layout.activity_add_budget);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddBudgetActivity.this);

        txtBgtName = (EditText) findViewById(R.id.bgt_edit_type);
        txtBgtAmount = (EditText) findViewById(R.id.bgt_edit_amount);
        txtBgtDate = (EditText) findViewById(R.id.bgt_select_date);
        txtBgtDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.bgt_btn_save);
        btnCancel = (Button) findViewById(R.id.bgt_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddBudget();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // activityFinish();

                Intent myIntent = new Intent(AddBudgetActivity.this, BudgetMenuActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtBgtName.setText("");
        txtBgtAmount.setText("");
        txtBgtDate.setText("");
        p.setPrefVal("Budget", "0");
    }





    private void AddBudget()
    {

        try {

            // Adding values to variables then in database

            str_Budget_catg = txtBgtName.getText().toString();
            str_Budget_amount = txtBgtAmount.getText().toString();
            str_Budget_date = txtBgtDate.getText().toString();

            // checking for existing location
            if(! dbHandler.CheckBudgetEXIST(AddBudgetActivity.this)
                    ){
                dbHandler.AddBudget(AddBudgetActivity.this);

                // Display a success message
                Toast.makeText(AddBudgetActivity.this,
                        "Bgtense Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddBudgetActivity.this,
                        " Location already exists. ", Toast.LENGTH_SHORT).show();
                txtBgtName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddBudgetActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateBudget()
    {

        try {

            // Adding values to variables then in database

            str_Budget_catg = txtBgtName.getText().toString();
            str_Budget_amount = txtBgtAmount.getText().toString();
            str_Budget_date = txtBgtDate.getText().toString();

            //Update db
            dbHandler.UpdateBudget(AddBudgetActivity.this);

            // Display a success message
            Toast.makeText(AddBudgetActivity.this,
                    "Location Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddBudgetActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePicker
    public void showDialogOnButtonClick(){

        txt_bgtdate = (EditText)findViewById(R.id.bgt_select_date);

        txt_bgtdate.setOnClickListener(new View.OnClickListener() {
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

                    txt_bgtdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
