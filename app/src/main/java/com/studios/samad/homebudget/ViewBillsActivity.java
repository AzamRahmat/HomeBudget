package com.studios.samad.homebudget;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Omer on 24-Mar-16.
 */
public class ViewBillsActivity extends Activity {


    private ListView view;
    DataBaseHandler db;
    ArrayList<AddBillsActivity> detailed_arr = new ArrayList<>();
    private Adapter_Class lstAdapter;
    private AlertDialog.Builder Notify;
    private AddBillsActivity obj;
    private String ActivityType;
    private Preferences p;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bills_view);

        p = new Preferences(this);
        db = new DataBaseHandler(this);
        view = (ListView) findViewById(R.id.bills_custom_list);
        view.setItemsCanFocus(false);


        try {
            ActivityType = getIntent().getStringExtra("Type");
        } catch (Exception e) {
            // TODO: handle exception
        }



        view.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
                                    long arg3) {
                // TODO Auto-generated method stub
                obj =  detailed_arr.get(arg2);
                p.setPrefVal(ActivityType,obj.getBills_id()+"" );
                ViewBillsActivity.this.finish();
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


        Set_Refresh_Data();

    }



    public void createDialog()

    {
        Notify = new AlertDialog.Builder(this);

        Notify.setTitle("Remove Bill ?");

        Notify.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.DeleteExpenses(obj.getBills_id());
                        detailed_arr.remove(obj);

                        lstAdapter.notifyDataSetChanged();
                        Log.v("obj to delete", obj.getBills_id() + "");
                    }

                });
    }



    public void Set_Refresh_Data() {
        detailed_arr.clear();

        ArrayList<AddBillsActivity> arr = db.VewAllBills();

        for (int i = 0; i < arr.size(); i++) {
            AddBillsActivity obj = new AddBillsActivity();
            obj.setBills_id(arr.get(i).getBills_id());
            obj.setBills_name(arr.get(i).getBills_name());
            obj.setBills_date(arr.get(i).getBills_date());
            detailed_arr.add(obj);
        }

        db.close();
        lstAdapter = new Adapter_Class(ViewBillsActivity.this, R.layout.activity_bills_list_row,
                detailed_arr);
        view.setAdapter(lstAdapter);
        lstAdapter.notifyDataSetChanged();

    }




    class Holder_Class {
        TextView bills_lbl_name;
        TextView bills_lbl_date;
    }




    class Adapter_Class extends ArrayAdapter<AddBillsActivity> {
        Activity activity;
        int layoutResourceId;
        AddBillsActivity act;
        ArrayList<AddBillsActivity> data = new ArrayList<AddBillsActivity>();
        //private TextView user_ID;

        public Adapter_Class(Activity act, int layoutResourceId,
                             ArrayList<AddBillsActivity> data) {
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
                holder.bills_lbl_name = (TextView) row.findViewById(R.id.bills_list_name);
                holder.bills_lbl_date = (TextView) row.findViewById(R.id.bills_list_date);
                row.setTag(holder);

            } else {
                holder = (Holder_Class) row.getTag();
            }

            act = data.get(position);

            holder.bills_lbl_name.setText(act.getBills_name());
            holder.bills_lbl_date.setText(act.getBills_date());

            return row;

        }


    }

}