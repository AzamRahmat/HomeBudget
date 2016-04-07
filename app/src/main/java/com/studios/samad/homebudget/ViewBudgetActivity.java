package com.studios.samad.homebudget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ViewBudgetActivity extends AppCompatActivity {


    private ListView view;
    DataBaseHandler db;
    ArrayList<AddBudgetActivity> detailed_arr = new ArrayList<AddBudgetActivity>();
    private Adapter_Class lstAdapter;
    private AlertDialog.Builder Notify;
    private AddBudgetActivity obj;
    private String ActivityType;
    private Preferences p;
    private Button add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_budget_view);

        p = new Preferences(this);
        db = new DataBaseHandler(this);
        view = (ListView) findViewById(R.id.bgt_custom_list);
        view.setItemsCanFocus(false);


        try {
            ActivityType = getIntent().getStringExtra("Type");
        } catch (Exception e) {
            // TODO: handle exception
        }


        add = (Button) findViewById(R.id.btn_add_budget);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getApplicationContext(), AddBudgetActivity.class);
                startActivity(myIntent);
            }
        });





        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
                createDialog();
                Notify.show();
                obj =  detailed_arr.get(arg2);
                //Log.v("obj value", obj.getName() +"");
                return true;
            }
        });


        // Set_Refresh_Data();

    }



    public void createDialog()

    {
        Notify = new AlertDialog.Builder(this);

        Notify.setTitle("Remove Budget ?");

        Notify.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.DeleteBudget(obj.getBudget_id());
                        detailed_arr.remove(obj);

                        lstAdapter.notifyDataSetChanged();
                        Log.v("obj to delete", obj.getBudget_id() + "");
                    }

                });
    }



    public void Set_Refresh_Data() {
        detailed_arr.clear();
        AddBudgetActivity obj = new AddBudgetActivity();
        String category = obj.getSelected_Budget_catg();
        ArrayList<AddBudgetActivity> arr = db.VewAllBudget(category);


        for (int i = 0; i < arr.size(); i++) {
          //  AddBudgetActivity obj = new AddBudgetActivity();
            obj.setBudget_id(arr.get(i).getBudget_id());
            obj.setBudget_catg(arr.get(i).getBudget_catg());
            // Toast.makeText(getApplicationContext(),arr.get(i).getBudget_catg(),Toast.LENGTH_LONG).show();
            obj.setBudget_amount(arr.get(i).getBudget_amount());
            // Toast.makeText(getApplicationContext(),arr.get(i).getBudget_amount(),Toast.LENGTH_LONG).show();
            obj.setBudget_date(arr.get(i).getBudget_date());
            //  Toast.makeText(getApplicationContext(),arr.get(i).getBudget_date(),Toast.LENGTH_LONG).show();

            detailed_arr.add(obj);
        }

        db.close();
        lstAdapter = new Adapter_Class(ViewBudgetActivity.this, R.layout.activity_budget_list_row,
                detailed_arr);
        view.setAdapter(lstAdapter);
        lstAdapter.notifyDataSetChanged();

    }




    class Holder_Class {
        TextView lbl_name;
        TextView lbl_amount;
        TextView lbl_date;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Set_Refresh_Data();

    }

    class Adapter_Class extends ArrayAdapter<AddBudgetActivity> {
        Activity activity;
        int layoutResourceId;
        AddBudgetActivity act;
        ArrayList<AddBudgetActivity> data = new ArrayList<AddBudgetActivity>();
        //private TextView user_ID;

        public Adapter_Class(Activity act, int layoutResourceId,
                             ArrayList<AddBudgetActivity> data) {
            super(act, layoutResourceId, data);
            this.layoutResourceId = layoutResourceId;
            this.activity = act;
            this.data = data;
            // notifyDataSetChanged();
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View row = convertView;
            Holder_Class holder = null;

            if (row == null) {
                LayoutInflater inflater = LayoutInflater.from(activity);

                row = inflater.inflate(layoutResourceId, parent, false);
                holder = new Holder_Class();
                holder.lbl_name = (TextView) row.findViewById(R.id.bgt_list_name);
                holder.lbl_amount = (TextView) row.findViewById(R.id.bgt_list_amount);
                holder.lbl_date = (TextView) row.findViewById(R.id.bgt_list_date);
                row.setTag(holder);

            } else {
                holder = (Holder_Class) row.getTag();
            }

            act = data.get(position);

            holder.lbl_name.setText(act.getBudget_catg());
            holder.lbl_amount.setText(act.getBudget_amount());
            holder.lbl_date.setText(act.getBudget_date());

            return row;

        }


    }





}
