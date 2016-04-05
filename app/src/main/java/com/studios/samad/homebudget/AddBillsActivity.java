package com.studios.samad.homebudget;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Omer on 24-Mar-16.
 */
public class AddBillsActivity extends AppCompatActivity {

    ArrayList<AddBillsActivity> data = new ArrayList<AddBillsActivity>();

    private AddBillsActivity objLoc;

    Button btnSave;
    Button btnCancel;

    private DataBaseHandler dbHandler;

    EditText txtBillsName;
    EditText txtBillsDate;
    EditText txtBillsAmount;
    private Preferences p;

    private int bills_id;
    private String  str_bills_name;
    private String str_bills_date;
    private String str_bills_Amount;
    private Switch reminderBtn;


    public int getBills_id() {
        return bills_id;
    }

    public void setBills_id(int bills_id) {
        this.bills_id = bills_id;
    }

    public String getBills_date() {
        return str_bills_date;
    }
    public String getBills_amount() {
        return str_bills_Amount;
    }
    public void setBills_date(String bills_date) {
        this.str_bills_date = bills_date;
    }
    public void setBills_amount(String bills_Amount) {
        this.str_bills_date = bills_Amount;
    }
    public String getBills_name() {
        return str_bills_name;
    }

    public void setBills_name(String bills_name) {
        this.str_bills_name = bills_name;
    }

    int year_x, month_x, day_x;
    static final int Dialog_Id = 0;

    EditText txt_billsdate;


    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        //Toast.makeText(LocationActivity.this.getActivity()," location ", Toast.LENGTH_SHORT).show();

        try
        {

            txtBillsDate.requestFocus();

            bills_id =  Integer.parseInt(p.getPrefVal("Bills", "0"));
            if(bills_id > 0)
            {
                txtBillsName.setEnabled(false);
                objLoc =  dbHandler.getBills(bills_id);
                txtBillsName.setText(objLoc.getBills_name());
                txtBillsDate.setText(objLoc.getBills_date());
                txtBillsAmount.setText(objLoc.getBills_amount());
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
        setContentView(R.layout.activity_add_bills);
        showDialogOnButtonClick();


        final Calendar cal = Calendar.getInstance();
        year_x = cal.get(Calendar.YEAR);
        month_x = cal.get(Calendar.MONTH);
        day_x = cal.get(Calendar.DAY_OF_MONTH);


        p = new Preferences(AddBillsActivity.this);

        txtBillsName = (EditText) findViewById(R.id.bills_edit_type);
        txtBillsDate = (EditText) findViewById(R.id.bills_select_date);
        txtBillsAmount = (EditText) findViewById(R.id.bills_select_amount);
        txtBillsDate.requestFocus();

        dbHandler = new DataBaseHandler(this);

        btnSave = (Button) findViewById(R.id.bills_btn_save);
        btnCancel = (Button) findViewById(R.id.bills_btn_cancel);

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AddBills();
                clear();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(AddBillsActivity.this, ViewBillsActivity.class);
                startActivity(myIntent);

            }
        });
    }

    private void clear()
    {

        txtBillsName.setText("");
        txtBillsDate.setText("");
        txtBillsAmount.setText("");
        p.setPrefVal("Bills", "0");
    }





    private void AddBills()
    {

        try {

            // Adding values to variables then in database

            str_bills_name = txtBillsName.getText().toString();
            str_bills_date = txtBillsDate.getText().toString();
            str_bills_Amount = txtBillsAmount.getText().toString();
            reminderBtn = (Switch)findViewById(R.id.bills_reminder_btn);

            // checking for existing location
            if(! dbHandler.CheckBillsEXIST(AddBillsActivity.this )
                    ){
                if(year_x >= 2016)
                {
                    dbHandler.AddBills(AddBillsActivity.this);
                    if (reminderBtn.isChecked()) {
                        setNotificationDate(this, str_bills_name);
                    }

                    year_x = 0;

                    // Display a success message
                    Toast.makeText(AddBillsActivity.this,
                            "Bill Added", Toast.LENGTH_LONG).show();
                }
                else
                {
                    Toast.makeText(AddBillsActivity.this,
                            " Check Date. ", Toast.LENGTH_SHORT).show();
                    txt_billsdate.selectAll();
                }


            }
            else{
                Toast.makeText(AddBillsActivity.this,
                        " Bill already exists. ", Toast.LENGTH_SHORT).show();
                txtBillsName.selectAll();

            }



        } catch (Exception ex) {
            Toast.makeText(AddBillsActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }



    private void setNotificationDate(Context context, String reminder )
    {
        Intent intent = new Intent(context , NotificationService.class);
        intent.putExtra("reminder",reminder);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);

        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        cal.clear();
        cal.set(year_x, month_x, day_x,1,1);
        AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        //Toast.makeText(AddBillsActivity.this,year_x+"-"+ month_x+"-" +day_x, Toast.LENGTH_LONG).show();
        //Toast.makeText(AddBillsActivity.this,System.currentTimeMillis()+"", Toast.LENGTH_LONG).show();
        //Toast.makeText(AddBillsActivity.this,cal.getTimeInMillis()+"a", Toast.LENGTH_LONG).show();
        am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis() , pendingIntent);
        Log.d("ME", "Alarm started");
    }



    private void UpdateBills()
    {

        try {

            // Adding values to variables then in database

            str_bills_name = txtBillsName.getText().toString();
            str_bills_date = txtBillsDate.getText().toString();
            str_bills_Amount = txtBillsAmount.getText().toString();

            //Update db
            dbHandler.UpdateBills(AddBillsActivity.this);

            // Display a success message
            Toast.makeText(AddBillsActivity.this,
                    "Bills Updated !", Toast.LENGTH_SHORT).show();
            // clear();


        } catch (Exception ex) {
            Toast.makeText(AddBillsActivity.this,
                    ex.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }






    // DatePicker
    public void showDialogOnButtonClick(){

        txt_billsdate = (EditText)findViewById(R.id.bills_select_date);

        txt_billsdate.setOnClickListener(new View.OnClickListener() {
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

                    txt_billsdate.setText(day_x + "/" + month_x + "/" + year_x);

                }
            };

}
