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

public class AddFoodActivity extends AppCompatActivity {

    ArrayList<AddFoodActivity> data = new ArrayList<AddFoodActivity>();

    private AddFoodActivity objLoc;
    private int fd_id;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtFoodName;
    EditText txtFoodAmount;
    EditText txtFoodDate;

    private Preferences p;

    private int food_id;
    private String  str_food_name;
    private String  str_food_amount;
    private String str_food_date;


    public int getFood_id() {

        return food_id;
    }

    public void setFood_id(int food_id) {

        this.food_id = food_id;
    }

    public String getFood_amount() {

        return str_food_amount;
    }

    public void setFood_amount(String food_amount) {
        this.str_food_amount = food_amount;
    }

    public String getFood_date() {
        return str_food_date;
    }

    public void setFood_date(String food_date) {
        this.str_food_date = food_date;
    }

    public String getFood_name() {
        return str_food_name;
    }

    public void setFood_name(String food_name) {
        this.str_food_name = food_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_fooddate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtFoodDate.requestFocus();

            fd_id =  Integer.parseInt(p.getPrefVal("Food", "0"));
            if(fd_id > 0)
            {
                txtFoodName.setEnabled(false);
                objLoc =  dbHandler.getFood(food_id);
                txtFoodName.setText(objLoc.getFood_name());
                txtFoodAmount.setText(objLoc.getFood_amount());
                txtFoodDate.setText(objLoc.getFood_date());
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
        setContentView(R.layout.activity_add_food);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddFoodActivity.this);

        txtFoodName = (EditText) findViewById(R.id.food_edit_type);
        txtFoodAmount = (EditText) findViewById(R.id.food_edit_amount);
        txtFoodDate = (EditText) findViewById(R.id.food_select_date);
        txtFoodDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.food_btn_save);
        btnCancel = (Button) findViewById(R.id.food_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddFood();
                clear();


            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddFoodActivity.this, ViewFoodActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtFoodName.setText("");
        txtFoodAmount.setText("");
        txtFoodDate.setText("");
        p.setPrefVal("Food", "0");
    }





    private void AddFood()
    {

        try {

            // Adding values to variables then in database

            str_food_name = txtFoodName.getText().toString();
            str_food_amount = txtFoodAmount.getText().toString();
            str_food_date = txtFoodDate.getText().toString();

            // checking for existing location
            if(! dbHandler.CheckFoodEXIST(AddFoodActivity.this)
                    ){
                dbHandler.AddFood(AddFoodActivity.this);

                // Display a success messege
                Toast.makeText(AddFoodActivity.this,
                        "Expense Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                Toast.makeText(AddFoodActivity.this,
                        " Location already exists. ", Toast.LENGTH_SHORT).show();
                txtFoodName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddFoodActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }


    private void UpdateFood()
    {

        try {

            // Adding values to variables then in database

            str_food_name = txtFoodName.getText().toString();
            str_food_amount = txtFoodAmount.getText().toString();
            str_food_date = txtFoodDate.getText().toString();

            //Update db
            dbHandler.UpdateFood(AddFoodActivity.this);

            // Display a success messege
            Toast.makeText(AddFoodActivity.this,
                    "Location Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddFoodActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePciker
    public void showDialogOnButtonClick(){

        txt_fooddate = (EditText)findViewById(R.id.food_select_date);

        txt_fooddate.setOnClickListener(new View.OnClickListener() {
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

                    txt_fooddate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };


}
