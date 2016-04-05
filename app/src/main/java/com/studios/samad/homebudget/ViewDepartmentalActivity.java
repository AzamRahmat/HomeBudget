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
 * Created by Omer on 21-Mar-16.
 */
public class ViewDepartmentalActivity extends Activity {


    private ListView view;
    DataBaseHandler db;
    ArrayList<AddDepartmentalActivity> detailed_arr = new ArrayList<AddDepartmentalActivity>();
    private Adapter_Class lstAdapter;
    private AlertDialog.Builder Notify;
    private AddDepartmentalActivity obj;
    private String ActivityType;
    private Preferences p;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_departmental_view);

        p = new Preferences(this);
        db = new DataBaseHandler(this);
        view = (ListView) findViewById(R.id.dep_custom_list);
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
                p.setPrefVal(ActivityType,obj.getDep_id()+"" );
                ViewDepartmentalActivity.this.finish();
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

        Notify.setTitle("Remove ?");

        Notify.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.DeleteDep(obj.getDep_id());
                        detailed_arr.remove(obj);

                        lstAdapter.notifyDataSetChanged();
                        Log.v("obj to delete", obj.getDep_id() + "");
                    }

                });
    }



    public void Set_Refresh_Data() {
        detailed_arr.clear();

        ArrayList<AddDepartmentalActivity> arr = db.VewAllDep();

        for (int i = 0; i < arr.size(); i++) {
            AddDepartmentalActivity obj = new AddDepartmentalActivity();
            obj.setDep_id(arr.get(i).getDep_id());
            obj.setDep_name(arr.get(i).getDep_name());
            obj.setDep_amount(arr.get(i).getDep_amount());
            obj.setDep_date(arr.get(i).getDep_date());
            detailed_arr.add(obj);
        }

        db.close();
        lstAdapter = new Adapter_Class(ViewDepartmentalActivity.this, R.layout.activity_departmental_list_row,
                detailed_arr);
        view.setAdapter(lstAdapter);
        lstAdapter.notifyDataSetChanged();

    }




    class Holder_Class {
        TextView lbl_name;
        TextView lbl_amount;
        TextView lbl_date;
    }




    class Adapter_Class extends ArrayAdapter<AddDepartmentalActivity> {
        Activity activity;
        int layoutResourceId;
        AddDepartmentalActivity act;
        ArrayList<AddDepartmentalActivity> data = new ArrayList<AddDepartmentalActivity>();
        //private TextView user_ID;

        public Adapter_Class(Activity act, int layoutResourceId,
                             ArrayList<AddDepartmentalActivity> data) {
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
                holder.lbl_name = (TextView) row.findViewById(R.id.dep_list_name);
                holder.lbl_amount = (TextView) row.findViewById(R.id.dep_list_amount);
                holder.lbl_date = (TextView) row.findViewById(R.id.dep_list_date);
                row.setTag(holder);

            } else {
                holder = (Holder_Class) row.getTag();
            }

            act = data.get(position);

            holder.lbl_name.setText(act.getDep_name());
            holder.lbl_amount.setText(act.getDep_amount());
            holder.lbl_date.setText(act.getDep_date());

            return row;

        }


    }



}
