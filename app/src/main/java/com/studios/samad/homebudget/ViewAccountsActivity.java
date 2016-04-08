package com.studios.samad.homebudget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class ViewAccountsActivity extends AppCompatActivity {


    private ListView view;
    DataBaseHandler db;
    ArrayList<AddAccountActivity> detailed_arr = new ArrayList<AddAccountActivity>();
    private Adapter_Class lstAdapter;
    private AlertDialog.Builder Notify;
    private AddAccountActivity obj;
    private String ActivityType;
    private Preferences p;
    private Button add;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accounts);

        p = new Preferences(this);
        db = new DataBaseHandler(this);
        view = (ListView) findViewById(R.id.acnt_custom_list);
        view.setItemsCanFocus(false);


        try {
            ActivityType = getIntent().getStringExtra("Type");
        } catch (Exception e) {
            // TODO: handle exception
        }


        add = (Button) findViewById(R.id.btn_add_accounts);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(getApplicationContext(), AddAccountActivity.class);
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

        Notify.setTitle("Remove Accounts ?");

        Notify.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.DeleteAccount(obj.getAccounts_id());
                        detailed_arr.remove(obj);

                        lstAdapter.notifyDataSetChanged();
                        Log.v("obj to delete", obj.getAccounts_id() + "");
                    }

                });
    }



    public void Set_Refresh_Data() {
        detailed_arr.clear();

        ArrayList<AddAccountActivity> arr = db.VewAllAccount();

        for (int i = 0; i < arr.size(); i++) {
            AddAccountActivity obj = new AddAccountActivity();
            obj.setAccounts_id(arr.get(i).getAccounts_id());
            obj.setAccounts_name(arr.get(i).getAccounts_name());
            obj.setAccounts_amount(arr.get(i).getAccounts_amount());
            obj.setAccounts_date(arr.get(i).getAccounts_date());
            detailed_arr.add(obj);
        }

        db.close();
        lstAdapter = new Adapter_Class(ViewAccountsActivity.this, R.layout.activity_accounts_list_row,
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

    class Adapter_Class extends ArrayAdapter<AddAccountActivity> {
        Activity activity;
        int layoutResourceId;
        AddAccountActivity act;
        ArrayList<AddAccountActivity> data = new ArrayList<AddAccountActivity>();
        //private TextView user_ID;

        public Adapter_Class(Activity act, int layoutResourceId,
                             ArrayList<AddAccountActivity> data) {
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
                holder.lbl_name = (TextView) row.findViewById(R.id.acnt_list_name);
                holder.lbl_amount = (TextView) row.findViewById(R.id.acnt_list_amount);
                holder.lbl_date = (TextView) row.findViewById(R.id.acnt_list_date);
                row.setTag(holder);

            } else {
                holder = (Holder_Class) row.getTag();
            }

            act = data.get(position);

            holder.lbl_name.setText(act.getAccounts_name());
            holder.lbl_amount.setText(act.getAccounts_amount());
            holder.lbl_date.setText(act.getAccounts_date());

            return row;

        }


    }



}
