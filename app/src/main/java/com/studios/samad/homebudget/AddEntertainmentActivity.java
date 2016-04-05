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
public class AddEntertainmentActivity extends AppCompatActivity {

    ArrayList<AddEntertainmentActivity> data = new ArrayList<AddEntertainmentActivity>();

    private AddEntertainmentActivity objLoc;
    private int entr_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtEntName;
    EditText txtEntAmount;
    EditText txtEntDate;

    private Preferences p;

    private int ent_id;
    private String  str_ent_name;
    private String  str_ent_amount;
    private String str_ent_date;


    public int getEnt_id() {

        return ent_id;
    }

    public void setEnt_id(int ent_id) {

        this.ent_id = ent_id;
    }

    public String getEnt_amount() {

        return str_ent_amount;
    }

    public void setEnt_amount(String ent_amount) {
        this.str_ent_amount = ent_amount;
    }

    public String getEnt_date() {
        return str_ent_date;
    }

    public void setEnt_date(String ent_date) {
        this.str_ent_date = ent_date;
    }

    public String getEnt_name() {
        return str_ent_name;
    }

    public void setEnt_name(String ent_name) {
        this.str_ent_name = ent_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_entdate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtEntDate.requestFocus();

            entr_id =  Integer.parseInt(p.getPrefVal("Ent", "0"));
            if(entr_id > 0)
            {
                txtEntName.setEnabled(false);
                objLoc =  dbHandler.getEnt(ent_id);
                txtEntName.setText(objLoc.getEnt_name());
                txtEntAmount.setText(objLoc.getEnt_amount());
                txtEntDate.setText(objLoc.getEnt_date());
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
        setContentView(R.layout.activity_add_ent);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddEntertainmentActivity.this);

        txtEntName = (EditText) findViewById(R.id.ent_edit_type);
        txtEntAmount = (EditText) findViewById(R.id.ent_edit_amount);
        txtEntDate = (EditText) findViewById(R.id.ent_select_date);
        txtEntDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.ent_btn_save);
        btnCancel = (Button) findViewById(R.id.ent_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddEnt();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddEntertainmentActivity.this, ViewEntertainmentActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtEntName.setText("");
        txtEntAmount.setText("");
        txtEntDate.setText("");
        p.setPrefVal("Ent", "0");
    }





    private void AddEnt()
    {

        try {

            // Adding values to variables then in database

            str_ent_name = txtEntName.getText().toString();
            str_ent_amount = txtEntAmount.getText().toString();
            str_ent_date = txtEntDate.getText().toString();

            // checking for existing Ent
            if(! dbHandler.CheckEntEXIST(AddEntertainmentActivity.this)
                    ){
                dbHandler.AddEnt(AddEntertainmentActivity.this);

                // Display a success messege
                Toast.makeText(AddEntertainmentActivity.this,
                        "Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddEntertainmentActivity.this,
                        " Already exists. ", Toast.LENGTH_SHORT).show();
                txtEntName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddEntertainmentActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateEnt()
    {

        try {

            // Adding values to variables then in database

            str_ent_name = txtEntName.getText().toString();
            str_ent_amount = txtEntAmount.getText().toString();
            str_ent_date = txtEntDate.getText().toString();

            //Update db
            dbHandler.UpdateEnt(AddEntertainmentActivity.this);

            // Display a success messege
            Toast.makeText(AddEntertainmentActivity.this,
                    "Ent Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddEntertainmentActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePciker
    public void showDialogOnButtonClick(){

        txt_entdate = (EditText)findViewById(R.id.ent_select_date);

        txt_entdate.setOnClickListener(new View.OnClickListener() {
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

                    txt_entdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
