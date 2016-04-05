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
 * Created by Omer on 31-Mar-16.
 */
public class ViewEntertainmentActivity extends Activity {


    private ListView view;
    DataBaseHandler db;
    ArrayList<AddEntertainmentActivity> detailed_arr = new ArrayList<>();
    private Adapter_Class lstAdapter;
    private AlertDialog.Builder Notify;
    private AddEntertainmentActivity obj;
    private String ActivityType;
    private Preferences p;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ent_view);

        p = new Preferences(this);
        db = new DataBaseHandler(this);
        view = (ListView) findViewById(R.id.ent_custom_list);
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
                p.setPrefVal(ActivityType,obj.getEnt_id()+"" );
                ViewEntertainmentActivity.this.finish();
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

        Notify.setTitle("Remove Ent ?");

        Notify.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.DeleteExpenses(obj.getEnt_id());
                        detailed_arr.remove(obj);

                        lstAdapter.notifyDataSetChanged();
                        Log.v("obj to delete", obj.getEnt_id() + "");
                    }

                });
    }



    public void Set_Refresh_Data() {
        detailed_arr.clear();

        ArrayList<AddEntertainmentActivity> arr = db.VewAllEnt();

        for (int i = 0; i < arr.size(); i++) {
            AddEntertainmentActivity obj = new AddEntertainmentActivity();
            obj.setEnt_id(arr.get(i).getEnt_id());
            obj.setEnt_name(arr.get(i).getEnt_name());
            obj.setEnt_amount(arr.get(i).getEnt_amount());
            obj.setEnt_date(arr.get(i).getEnt_date());
            detailed_arr.add(obj);
        }

        db.close();
        lstAdapter = new Adapter_Class(ViewEntertainmentActivity.this, R.layout.activity_ent_list_row,
                detailed_arr);
        view.setAdapter(lstAdapter);
        lstAdapter.notifyDataSetChanged();

    }




    class Holder_Class {
        TextView ent_lbl_name;
        TextView ent_lbl_date;
        TextView ent_lbl_amount;
    }




    class Adapter_Class extends ArrayAdapter<AddEntertainmentActivity> {
        Activity activity;
        int layoutResourceId;
        AddEntertainmentActivity act;
        ArrayList<AddEntertainmentActivity> data = new ArrayList<AddEntertainmentActivity>();
        //private TextView user_ID;

        public Adapter_Class(Activity act, int layoutResourceId,
                             ArrayList<AddEntertainmentActivity> data) {
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
                holder.ent_lbl_name = (TextView) row.findViewById(R.id.ent_list_name);
                holder.ent_lbl_amount = (TextView) row.findViewById(R.id.ent_list_amount);
                holder.ent_lbl_date = (TextView) row.findViewById(R.id.ent_list_date);
                row.setTag(holder);

            } else {
                holder = (Holder_Class) row.getTag();
            }

            act = data.get(position);

            holder.ent_lbl_name.setText(act.getEnt_name());
            holder.ent_lbl_date.setText(act.getEnt_date());
            holder.ent_lbl_amount.setText(act.getEnt_amount());

            return row;

        }


    }

}