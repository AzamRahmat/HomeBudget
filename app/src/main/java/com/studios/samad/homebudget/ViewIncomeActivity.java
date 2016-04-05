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
public class ViewIncomeActivity extends Activity {


    private ListView view;
    DataBaseHandler db;
    ArrayList<AddIncomeActivity> detailed_arr = new ArrayList<>();
    private Adapter_Class lstAdapter;
    private AlertDialog.Builder Notify;
    private AddIncomeActivity obj;
    private String ActivityType;
    private Preferences p;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income_view);

        p = new Preferences(this);
        db = new DataBaseHandler(this);
        view = (ListView) findViewById(R.id.inc_custom_list);
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
                obj = detailed_arr.get(arg2);
                p.setPrefVal(ActivityType, obj.getIncome_id() + "");
                ViewIncomeActivity.this.finish();
            }

        });


        view.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           int arg2, long arg3) {
                // TODO Auto-generated method stub
                createDialog();
                Notify.show();
                obj = detailed_arr.get(arg2);
                //Log.v("obj value", obj.getName() +"");
                return true;
            }
        });


        Set_Refresh_Data();

    }


    public void createDialog()

    {
        Notify = new AlertDialog.Builder(this);

        Notify.setTitle("Remove Income ?");

        Notify.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.DeleteIncome(obj.getIncome_id());
                        detailed_arr.remove(obj);

                        lstAdapter.notifyDataSetChanged();
                        Log.v("obj to delete", obj.getIncome_id() + "");
                    }

                });
    }


    public void Set_Refresh_Data() {
        detailed_arr.clear();

        ArrayList<AddIncomeActivity> arr = db.VewAllIncome();

        for (int i = 0; i < arr.size(); i++) {
            AddIncomeActivity obj = new AddIncomeActivity();
            obj.setIncome_id(arr.get(i).getIncome_id());
            obj.setIncome_name(arr.get(i).getIncome_name());
            obj.setIncome_amount(arr.get(i).getIncome_amount());
            obj.setIncome_date(arr.get(i).getIncome_date());
            detailed_arr.add(obj);
        }

        db.close();
        lstAdapter = new Adapter_Class(ViewIncomeActivity.this, R.layout.activity_income_list_row,
                detailed_arr);
        view.setAdapter(lstAdapter);
        lstAdapter.notifyDataSetChanged();

    }


    class Holder_Class {
        TextView lbl_name;
        TextView lbl_amount;
        TextView lbl_date;
    }


    class Adapter_Class extends ArrayAdapter<AddIncomeActivity> {
        Activity activity;
        int layoutResourceId;
        AddIncomeActivity act;
        ArrayList<AddIncomeActivity> data = new ArrayList<AddIncomeActivity>();
        //private TextView user_ID;

        public Adapter_Class(Activity act, int layoutResourceId,
                             ArrayList<AddIncomeActivity> data) {
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
                holder.lbl_name = (TextView) row.findViewById(R.id.inc_list_name);
                holder.lbl_amount = (TextView) row.findViewById(R.id.inc_list_amount);
                holder.lbl_date = (TextView) row.findViewById(R.id.inc_list_date);
                row.setTag(holder);

            } else {
                holder = (Holder_Class) row.getTag();
            }

            act = data.get(position);

            holder.lbl_name.setText(act.getIncome_name());
            holder.lbl_amount.setText(act.getIncome_amount());
            holder.lbl_date.setText(act.getIncome_date());

            return row;

        }


    }
}

