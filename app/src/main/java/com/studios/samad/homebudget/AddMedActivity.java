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

public class AddMedActivity extends AppCompatActivity {

    ArrayList<AddMedActivity> data = new ArrayList<AddMedActivity>();

    private AddMedActivity objLoc;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtMedName;
    EditText txtMedDate;
    EditText txtMedAmount;

    private Preferences p;

    private int med_id;
    private String str_med_name;
    private String str_med_date;
    private String str_med_amount;


    public String getMed_amount() {
        return str_med_amount;
    }

    public void setMed_amount(String med_amount) {
        this.str_med_amount = med_amount;
    }

    public int getMed_id() {
        return med_id;
    }

    public void setMed_id(int med_id) {
        this.med_id = med_id;
    }

    public String getMed_date() {
        return str_med_date;
    }

    public void setMed_date(String med_date) {
        this.str_med_date = med_date;
    }

    public String getMed_name() {
        return str_med_name;
    }

    public void setMed_name(String med_name) {
        this.str_med_name = med_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_meddate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtMedDate.requestFocus();

            med_id =  Integer.parseInt(p.getPrefVal("Med", "0"));
            if(med_id > 0)
            {
                txtMedName.setEnabled(false);
                objLoc =  dbHandler.getMed(med_id);
                txtMedName.setText(objLoc.getMed_name());
                txtMedDate.setText(objLoc.getMed_date());
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
        setContentView(R.layout.activity_add_med);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddMedActivity.this);

        txtMedName = (EditText) findViewById(R.id.med_edit_type);
        txtMedAmount = (EditText) findViewById(R.id.med_edit_amount);
        txtMedDate = (EditText) findViewById(R.id.med_select_date);
        txtMedDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.med_btn_save);
        btnCancel = (Button) findViewById(R.id.med_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddMed();
                clear();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddMedActivity.this, ViewMedActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtMedName.setText("");
        txtMedDate.setText("");
        txtMedAmount.setText("");
        p.setPrefVal("Med", "0");
    }





    private void AddMed()
    {

        try {

            // Adding values to variables then in database

            str_med_name = txtMedName.getText().toString();
            str_med_date = txtMedDate.getText().toString();
            str_med_amount = txtMedAmount.getText().toString();

            // checking for existing location
            if(! dbHandler.CheckMedEXIST(AddMedActivity.this)
                    ){
                dbHandler.AddMed(AddMedActivity.this);

                // Display a success message
                Toast.makeText(AddMedActivity.this,
                        "Bill Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddMedActivity.this,
                        " Bill already exists. ", Toast.LENGTH_SHORT).show();
                txtMedName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddMedActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateMed()
    {

        try {

            // Adding values to variables then in database

            str_med_name = txtMedName.getText().toString();
            str_med_date = txtMedDate.getText().toString();
            str_med_amount = txtMedAmount.getText().toString();

            //Update db
            dbHandler.UpdateMed(AddMedActivity.this);

            // Display a success message
            Toast.makeText(AddMedActivity.this,
                    "Med Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddMedActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePicker
    public void showDialogOnButtonClick(){

        txt_meddate = (EditText)findViewById(R.id.med_select_date);

        txt_meddate.setOnClickListener(new View.OnClickListener() {
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

                    txt_meddate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}