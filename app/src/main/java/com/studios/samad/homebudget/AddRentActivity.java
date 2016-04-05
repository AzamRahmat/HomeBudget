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
 * Created by Omer on 31-Mar-16.
 */

public class AddRentActivity extends AppCompatActivity {

    ArrayList<AddRentActivity> data = new ArrayList<AddRentActivity>();

    private AddRentActivity objLoc;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtRentName;
    EditText txtRentDate;
    EditText txtRentAmount;

    private Preferences p;

    private int rent_id;
    private String str_rent_name;
    private String str_rent_date;
    private String str_rent_amount;


    public String getRent_amount() {
        return str_rent_amount;
    }

    public void setRent_amount(String rent_amount) {
        this.str_rent_amount = rent_amount;
    }

    public int getRent_id() {
        return rent_id;
    }

    public void setRent_id(int rent_id) {
        this.rent_id = rent_id;
    }

    public String getRent_date() {
        return str_rent_date;
    }

    public void setRent_date(String rent_date) {
        this.str_rent_date = rent_date;
    }

    public String getRent_name() {
        return str_rent_name;
    }

    public void setRent_name(String rent_name) {
        this.str_rent_name = rent_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_rentdate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtRentDate.requestFocus();

            rent_id =  Integer.parseInt(p.getPrefVal("Rent", "0"));
            if(rent_id > 0)
            {
                txtRentName.setEnabled(false);
                objLoc =  dbHandler.getRent(rent_id);
                txtRentName.setText(objLoc.getRent_name());
                txtRentDate.setText(objLoc.getRent_date());
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
        setContentView(R.layout.activity_add_rent);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddRentActivity.this);

        txtRentName = (EditText) findViewById(R.id.rent_edit_type);
        txtRentAmount = (EditText) findViewById(R.id.rent_edit_amount);
        txtRentDate = (EditText) findViewById(R.id.rent_select_date);
        txtRentDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.rent_btn_save);
        btnCancel = (Button) findViewById(R.id.rent_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddRent();
                clear();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddRentActivity.this, ViewRentActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtRentName.setText("");
        txtRentDate.setText("");
        txtRentAmount.setText("");
        p.setPrefVal("Rent", "0");
    }





    private void AddRent()
    {

        try {

            // Adding values to variables then in database

            str_rent_name = txtRentName.getText().toString();
            str_rent_date = txtRentDate.getText().toString();
            str_rent_amount = txtRentAmount.getText().toString();

            // checking for existing location
            if(! dbHandler.CheckRentEXIST(AddRentActivity.this)
                    ){
                dbHandler.AddRent(AddRentActivity.this);

                // Display a success message
                Toast.makeText(AddRentActivity.this,
                        "Bill Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddRentActivity.this,
                        " Bill already exists. ", Toast.LENGTH_SHORT).show();
                txtRentName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddRentActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateRent()
    {

        try {

            // Adding values to variables then in database

            str_rent_name = txtRentName.getText().toString();
            str_rent_date = txtRentDate.getText().toString();
            str_rent_amount = txtRentAmount.getText().toString();

            //Update db
            dbHandler.UpdateRent(AddRentActivity.this);

            // Display a success message
            Toast.makeText(AddRentActivity.this,
                    "Rent Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddRentActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePicker
    public void showDialogOnButtonClick(){

        txt_rentdate = (EditText)findViewById(R.id.rent_select_date);

        txt_rentdate.setOnClickListener(new View.OnClickListener() {
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

                    txt_rentdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}