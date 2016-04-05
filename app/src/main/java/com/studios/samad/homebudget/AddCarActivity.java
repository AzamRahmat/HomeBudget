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

public class AddCarActivity extends AppCompatActivity {

    ArrayList<AddCarActivity> data = new ArrayList<AddCarActivity>();

    private AddCarActivity objLoc;
    private int Car_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtCarName;
    EditText txtCarAmount;
    EditText txtCarDate;

    private Preferences p;

    private int car_id;
    private String  str_car_name;
    private String  str_car_amount;
    private String str_car_date;


    public int getCar_id() {

        return car_id;
    }

    public void setCar_id(int car_id) {

        this.car_id = car_id;
    }

    public String getCar_amount() {

        return str_car_amount;
    }

    public void setCar_amount(String car_amount) {
        this.str_car_amount = car_amount;
    }

    public String getCar_date() {
        return str_car_date;
    }

    public void setCar_date(String car_date) {
        this.str_car_date = car_date;
    }

    public String getCar_name() {
        return str_car_name;
    }

    public void setCar_name(String car_name) {
        this.str_car_name = car_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_cardate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtCarDate.requestFocus();

            Car_id =  Integer.parseInt(p.getPrefVal("Car", "0"));
            if(Car_id > 0)
            {
                txtCarName.setEnabled(false);
                objLoc =  dbHandler.getCar(car_id);
                txtCarName.setText(objLoc.getCar_name());
                txtCarAmount.setText(objLoc.getCar_amount());
                txtCarDate.setText(objLoc.getCar_date());
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
        setContentView(R.layout.activity_add_car);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddCarActivity.this);

        txtCarName = (EditText) findViewById(R.id.car_edit_type);
        txtCarAmount = (EditText) findViewById(R.id.car_edit_amount);
        txtCarDate = (EditText) findViewById(R.id.car_select_date);
        txtCarDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.car_btn_save);
        btnCancel = (Button) findViewById(R.id.car_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddCar();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddCarActivity.this, ViewCarActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtCarName.setText("");
        txtCarAmount.setText("");
        txtCarDate.setText("");
        p.setPrefVal("Car", "0");
    }





    private void AddCar()
    {

        try {

            // Adding values to variables then in database

            str_car_name = txtCarName.getText().toString();
            str_car_amount = txtCarAmount.getText().toString();
            str_car_date = txtCarDate.getText().toString();

            // checking for existing location
            if(! dbHandler.CheckCarEXIST(AddCarActivity.this)
                    ){
                dbHandler.AddCar(AddCarActivity.this);

                // Display a success messege
                Toast.makeText(AddCarActivity.this,
                        "Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddCarActivity.this,
                        " Already exists. ", Toast.LENGTH_SHORT).show();
                txtCarName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddCarActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateCar()
    {

        try {

            // Adding values to variables then in database

            str_car_name = txtCarName.getText().toString();
            str_car_amount = txtCarAmount.getText().toString();
            str_car_date = txtCarDate.getText().toString();

            //Update db
            dbHandler.UpdateCar(AddCarActivity.this);

            // Display a success messege
            Toast.makeText(AddCarActivity.this,
                    "Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddCarActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePciker
    public void showDialogOnButtonClick(){

        txt_cardate = (EditText)findViewById(R.id.car_select_date);

        txt_cardate.setOnClickListener(new View.OnClickListener() {
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

                    txt_cardate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
