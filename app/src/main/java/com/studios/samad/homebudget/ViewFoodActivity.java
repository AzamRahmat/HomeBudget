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
public class ViewFoodActivity extends Activity {


    private ListView view;
    DataBaseHandler db;
    ArrayList<AddFoodActivity> detailed_arr = new ArrayList<>();
    private Adapter_Class lstAdapter;
    private AlertDialog.Builder Notify;
    private AddFoodActivity obj;
    private String ActivityType;
    private Preferences p;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_view);

        p = new Preferences(this);
        db = new DataBaseHandler(this);
        view = (ListView) findViewById(R.id.food_custom_list);
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
                p.setPrefVal(ActivityType,obj.getFood_id()+"" );
                ViewFoodActivity.this.finish();
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

        Notify.setTitle("Remove Food ?");

        Notify.setPositiveButton("Remove",
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        db.DeleteExpenses(obj.getFood_id());
                        detailed_arr.remove(obj);

                        lstAdapter.notifyDataSetChanged();
                        Log.v("obj to delete", obj.getFood_id() + "");
                    }

                });
    }



    public void Set_Refresh_Data() {
        detailed_arr.clear();

        ArrayList<AddFoodActivity> arr = db.VewAllFood();

        for (int i = 0; i < arr.size(); i++) {
            AddFoodActivity obj = new AddFoodActivity();
            obj.setFood_id(arr.get(i).getFood_id());
            obj.setFood_name(arr.get(i).getFood_name());
            obj.setFood_amount(arr.get(i).getFood_amount());
            obj.setFood_date(arr.get(i).getFood_date());
            detailed_arr.add(obj);
        }

        db.close();
        lstAdapter = new Adapter_Class(ViewFoodActivity.this, R.layout.activity_food_list_row,
                detailed_arr);
        view.setAdapter(lstAdapter);
        lstAdapter.notifyDataSetChanged();

    }




    class Holder_Class {
        TextView food_lbl_name;
        TextView food_lbl_date;
        TextView food_lbl_amount;
    }




    class Adapter_Class extends ArrayAdapter<AddFoodActivity> {
        Activity activity;
        int layoutResourceId;
        AddFoodActivity act;
        ArrayList<AddFoodActivity> data = new ArrayList<AddFoodActivity>();
        //private TextView user_ID;

        public Adapter_Class(Activity act, int layoutResourceId,
                             ArrayList<AddFoodActivity> data) {
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
                holder.food_lbl_name = (TextView) row.findViewById(R.id.food_list_name);
                holder.food_lbl_amount = (TextView) row.findViewById(R.id.food_list_amount);
                holder.food_lbl_date = (TextView) row.findViewById(R.id.food_list_date);
                row.setTag(holder);

            } else {
                holder = (Holder_Class) row.getTag();
            }

            act = data.get(position);

            holder.food_lbl_name.setText(act.getFood_name());
            holder.food_lbl_date.setText(act.getFood_date());
            holder.food_lbl_amount.setText(act.getFood_amount());

            return row;

        }


    }

}