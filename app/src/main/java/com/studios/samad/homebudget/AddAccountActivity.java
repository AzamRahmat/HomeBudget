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

public class AddAccountActivity extends AppCompatActivity {

    ArrayList<AddAccountActivity> data = new ArrayList<AddAccountActivity>();

    private AddAccountActivity objLoc;
    private int acnt_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtAcntName;
    EditText txtAcntAmount;
    EditText txtAcntDate;

    private Preferences p;

    private int acntenses_id;
    private String  str_acntenses_name;
    private String  str_acntenses_amount;
    private String str_acntenses_date;


    public int getAccounts_id() {

        return acntenses_id;
    }

    public void setAccounts_id(int acntenses_id) {

        this.acntenses_id = acntenses_id;
    }

    public String getAccounts_amount() {

        return str_acntenses_amount;
    }

    public void setAccounts_amount(String acntenses_amount) {
        this.str_acntenses_amount = acntenses_amount;
    }

    public String getAccounts_date() {
        return str_acntenses_date;
    }

    public void setAccounts_date(String acntenses_date) {
        this.str_acntenses_date = acntenses_date;
    }

    public String getAccounts_name() {
        return str_acntenses_name;
    }

    public void setAccounts_name(String acntenses_name) {
        this.str_acntenses_name = acntenses_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_acntdate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtAcntDate.requestFocus();

            acnt_id =  Integer.parseInt(p.getPrefVal("Accounts", "0"));
            if(acnt_id > 0)
            {
                txtAcntName.setEnabled(false);
                objLoc =  dbHandler.getAccounts(acntenses_id);
                txtAcntName.setText(objLoc.getAccounts_name());
                txtAcntAmount.setText(objLoc.getAccounts_amount());
                txtAcntDate.setText(objLoc.getAccounts_date());
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
        setContentView(R.layout.activity_add_account);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddAccountActivity.this);

        txtAcntName = (EditText) findViewById(R.id.acnt_edit_type);
        txtAcntAmount = (EditText) findViewById(R.id.acnt_edit_amount);
        txtAcntDate = (EditText) findViewById(R.id.acnt_select_date);
        txtAcntDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.acnt_btn_save);
        btnCancel = (Button) findViewById(R.id.acnt_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddAccounts();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddAccountActivity.this, ViewAccountsActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtAcntName.setText("");
        txtAcntAmount.setText("");
        txtAcntDate.setText("");
        p.setPrefVal("Accounts", "0");
    }





    private void AddAccounts()
    {

        try {

            // Adding values to variables then in database

            str_acntenses_name = txtAcntName.getText().toString();
            str_acntenses_amount = txtAcntAmount.getText().toString();
            str_acntenses_date = txtAcntDate.getText().toString();

            // checking for existing location
            if(! dbHandler.CheckAccountsEXIST(AddAccountActivity.this)
                    ){
                dbHandler.AddAccounts(AddAccountActivity.this);

                // Display a success messege
                Toast.makeText(AddAccountActivity.this,
                        "Acntense Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddAccountActivity.this,
                        " Location already exists. ", Toast.LENGTH_SHORT).show();
                txtAcntName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddAccountActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateAccounts()
    {

        try {

            // Adding values to variables then in database

            str_acntenses_name = txtAcntName.getText().toString();
            str_acntenses_amount = txtAcntAmount.getText().toString();
            str_acntenses_date = txtAcntDate.getText().toString();

            //Update db
            dbHandler.UpdateAccounts(AddAccountActivity.this);

            // Display a success messege
            Toast.makeText(AddAccountActivity.this,
                    "Location Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddAccountActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePciker
    public void showDialogOnButtonClick(){

        txt_acntdate = (EditText)findViewById(R.id.acnt_select_date);

        txt_acntdate.setOnClickListener(new View.OnClickListener() {
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

                    txt_acntdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
