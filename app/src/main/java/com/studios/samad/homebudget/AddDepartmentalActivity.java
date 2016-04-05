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

public class AddDepartmentalActivity extends AppCompatActivity {

    ArrayList<AddDepartmentalActivity> data = new ArrayList<AddDepartmentalActivity>();

    private AddDepartmentalActivity objLoc;
    private int depart_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtDepName;
    EditText txtDepAmount;
    EditText txtDepDate;

    private Preferences p;

    private int dep_id;
    private String  str_dep_name;
    private String  str_dep_amount;
    private String str_dep_date;


    public int getDep_id() {

        return dep_id;
    }

    public void setDep_id(int dep_id) {

        this.dep_id = dep_id;
    }

    public String getDep_amount() {

        return str_dep_amount;
    }

    public void setDep_amount(String dep_amount) {
        this.str_dep_amount = dep_amount;
    }

    public String getDep_date() {
        return str_dep_date;
    }

    public void setDep_date(String dep_date) {
        this.str_dep_date =dep_date;
    }

    public String getDep_name() {
        return str_dep_name;
    }

    public void setDep_name(String dep_name) {
        this.str_dep_name = dep_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_depdate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtDepDate.requestFocus();

            depart_id =  Integer.parseInt(p.getPrefVal("Dep", "0"));
            if(depart_id > 0)
            {
                txtDepName.setEnabled(false);
                objLoc =  dbHandler.getDep(dep_id);
                txtDepName.setText(objLoc.getDep_name());
                txtDepAmount.setText(objLoc.getDep_amount());
                txtDepDate.setText(objLoc.getDep_date());
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
        setContentView(R.layout.activity_add_departmental);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddDepartmentalActivity.this);

        txtDepName = (EditText) findViewById(R.id.dep_edit_type);
        txtDepAmount = (EditText) findViewById(R.id.dep_edit_amount);
        txtDepDate = (EditText) findViewById(R.id.dep_select_date);
        txtDepDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.dep_btn_save);
        btnCancel = (Button) findViewById(R.id.dep_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddDep();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddDepartmentalActivity.this, ViewDepartmentalActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtDepName.setText("");
        txtDepAmount.setText("");
        txtDepDate.setText("");
        p.setPrefVal("Dep", "0");
    }





    private void AddDep()
    {

        try {

            // Adding values to variables then in database

            str_dep_name = txtDepName.getText().toString();
            str_dep_amount = txtDepAmount.getText().toString();
            str_dep_date = txtDepDate.getText().toString();

            // checking for existing location
            if(! dbHandler.CheckDepEXIST(AddDepartmentalActivity.this)
                    ){
                dbHandler.AddDep(AddDepartmentalActivity.this);

                // Display a success messege
                Toast.makeText(AddDepartmentalActivity.this,
                        " Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddDepartmentalActivity.this,
                        " Already exists. ", Toast.LENGTH_SHORT).show();
                txtDepName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddDepartmentalActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateExpenses()
    {

        try {

            // Adding values to variables then in database

            str_dep_name = txtDepName.getText().toString();
            str_dep_amount = txtDepAmount.getText().toString();
            str_dep_date = txtDepDate.getText().toString();

            //Update db
            dbHandler.UpdateDep(AddDepartmentalActivity.this);

            // Display a success messege
            Toast.makeText(AddDepartmentalActivity.this,
                    "Location Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddDepartmentalActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePciker
    public void showDialogOnButtonClick(){

        txt_depdate = (EditText)findViewById(R.id.exp_select_date);

        txt_depdate.setOnClickListener(new View.OnClickListener() {
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

                    txt_depdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
