package com.studios.samad.homebudget;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Omer on 21-Mar-16.
 */
public class DataBaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 11;
    private static final String DATABASE_NAME = "HomeBudget";

    private static final String Expenses = "Expenses";
    private static final String Bills = "Bills";
    private static final String Income = "Income";
    private static final String Rent = "Rent";
    private static final String Food = "Food";
    private static final String Dep = "Dep";
    private static final String Ent = "Ent";
    private static final String Car = "Car";
    private static final String Med = "Med";
    private static final String Budget = "Budget";
    private static final String Totals = "Totals";


    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS Expenses (ExpensesID INTEGER PRIMARY KEY,  ExpensesName  TEXT, ExpensesAmount  INT,	ExpensesDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Bills (BillsID INTEGER PRIMARY KEY,  BillsName  TEXT, BillsDate  TEXT, BillsAmount  DOUBLE )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Income (IncomeID INTEGER PRIMARY KEY, IncomeName  TEXT, IncomeAmount  INT,	IncomeDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Rent (RentID INTEGER PRIMARY KEY, RentName  TEXT, RentAmount  INT,	RentDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Food (FoodID INTEGER PRIMARY KEY, FoodName  TEXT, FoodAmount  INT,	FoodDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Dep (DepID INTEGER PRIMARY KEY, DepName  TEXT, DepAmount  INT,	DepDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Ent (EntID INTEGER PRIMARY KEY, EntName  TEXT, EntAmount  INT,	EntDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Car (CarID INTEGER PRIMARY KEY, CarName  TEXT, CarAmount  INT,	CarDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Med (MedID INTEGER PRIMARY KEY, MedName  TEXT, MedAmount  INT,	MedDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Totals (TotalID INTEGER PRIMARY KEY, BudgetDate  TEXT, TotalExpense  DOUBLE, TotalBudget DOUBLE, TotalBill DOUBLE,TotalIncome  DOUBLE , TotalAvailable  DOUBLE )");
        db.execSQL("CREATE TABLE IF NOT EXISTS Budget (BudgetID INTEGER PRIMARY KEY, BudgetCatg  TEXT, BudgetAmount INT, BudgetDate  TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS Expenses");
        db.execSQL("DROP TABLE IF EXISTS Bills");
        db.execSQL("DROP TABLE IF EXISTS Income");
        db.execSQL("DROP TABLE IF EXISTS Rent");
        db.execSQL("DROP TABLE IF EXISTS Food");
        db.execSQL("DROP TABLE IF EXISTS Dep");
        db.execSQL("DROP TABLE IF EXISTS Car");
        db.execSQL("DROP TABLE IF EXISTS Med");
        db.execSQL("DROP TABLE IF EXISTS Totals");
        db.execSQL("DROP TABLE IF EXISTS Budget");
//haha bs krty jao drop :/

        onCreate(db);

    }

     //Select Max

    public long selectMaxID(String table, String col) {
        SQLiteDatabase db = this.getReadableDatabase();
        long lastId = (long) 0;
        try {
            String query = "SELECT MAX(" + col + ") from " + table;//+ " order by "+ col + " DESC limit 1";
            Cursor c = db.rawQuery(query, null);
            if (c != null && c.moveToFirst()) {
                lastId = c.getLong(0); // The 0 is the column index, we only
                // have 1 column, so the index is 0
            }
            c.close();
            //db.close();
        } catch (Exception e) {
            lastId = (long) 0;
        }

        return ++lastId;

    }

    // ***************** Check Exist **************************
    private boolean CHK_EXIST(String table, String col, String val) {
        SQLiteDatabase db = this.getReadableDatabase();
        boolean isExist = false;

        try {

            String selectQuery = "SELECT  * FROM " + table + " Where "+ col +" = '"+val+"' ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst() ) {

                isExist = true;

            }

            cursor.close();
            db.close();
        } catch (Exception e) {

        }

        return isExist;
    }



    // ******************** Adding Expenses ********************
    public void AddExpenses(AddExpensesActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Expenses, "ExpensesID");
        ContentValues values = new ContentValues();
        values.put("ExpensesID", id);
        values.put("ExpensesName", String.valueOf(obj.getExpenses_name()));
        values.put("ExpensesAmount", String.valueOf(obj.getExpenses_amount()));
        values.put("ExpensesDate", String.valueOf(obj.getExpenses_date()));

        db.insert(Expenses, null, values);
        db.close();
       setTotalExpenses();
        updateTotal();

    }


    public void updateTotal()
    {

        try {

            // Adding values to variables then in database

            // checking for existing location
            if(! CheckTotalsEXIST()
                    ){
                TotalAmount obj = TotalAmount.getInstance();
                addTotals();

                // Display a success messege
                // Toast.makeText(AddExpensesActivity.this,"Expense Added !", Toast.LENGTH_SHORT).show();

            }
            else{
                // Toast.makeText(AddExpensesActivity.this,  " Location already exists. ", Toast.LENGTH_SHORT).show();
                updateTotals();

            }



        } catch (Exception ex) {
            //Toast.makeText(AddExpensesActivity.this,ex.getMessage(), Toast.LENGTH_SHORT).show();
        }



    }



    // ******************** Adding Expenses ********************
    public void addTotals() {
        SQLiteDatabase db = this.getWritableDatabase();
      //  Long id = selectMaxID(Expenses, "ExpensesID");
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int monthYear  = month * 10000 + year;

        TotalAmount obj = TotalAmount.getInstance();

        ContentValues values = new ContentValues();
        values.put("TotalID", monthYear);
        values.put("TotalExpense", String.valueOf(obj.getTotalBudget()));
        values.put("TotalBudget", String.valueOf(obj.getTotalExpense()));
        values.put("TotalBill", String.valueOf(obj.getTotalBill()));
        values.put("TotalAvailable", String.valueOf(obj.getTotalAvailable()));
        values.put("TotalIncome", String.valueOf(obj.getTotalIncome()));
        db.insert(Totals, null, values);
        db.close();

    }



    // ******************** Update Totals ********************
    public void updateTotals() {
        SQLiteDatabase db = this.getWritableDatabase();



        ContentValues values = new ContentValues();
        TotalAmount obj = TotalAmount.getInstance();
        values.put("TotalExpense", String.valueOf(obj.getTotalBudget()));
        values.put("TotalBudget", String.valueOf(obj.getTotalExpense()));
        values.put("TotalBill", String.valueOf(obj.getTotalBill()));
        values.put("TotalAvailable", String.valueOf(obj.getTotalAvailable()));
        values.put("TotalIncome", String.valueOf(obj.getTotalIncome()));
        db.update(Totals, values, "TotalID=" + getDateForTotalsTable(), null);


        db.close();


    }
    // **************** Get Expenses By ID*******************

    public Double setTotalExpenses() {

        Double sum = 0.0;
        try {

            String selectQuery = "SELECT SUM(ExpensesAmount) FROM Expenses";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    sum = cursor.getDouble(0);


                } while (cursor.moveToNext());
            }
            TotalAmount obj = TotalAmount.getInstance();
            obj.setTotalExpense(sum);
            addTotals();
            cursor.close();
            db.close();

        } catch (Exception e) {

            //Toast.makeText(this,e,Toast.LENGTH_SHORT).show();
            Log.d("catch", "IN Catch");
        }



        return sum;
    }

    // **************** Get Expenses By ID*******************

    public Double setTotalIncome() {

        Double sum = 0.0;
        try {

            String selectQuery = "SELECT SUM(IncomeAmount) FROM Income";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    sum = cursor.getDouble(0);


                } while (cursor.moveToNext());
            }
            TotalAmount obj = TotalAmount.getInstance();
            obj.setTotalIncome(sum);
            addTotals();
            cursor.close();
            db.close();

        } catch (Exception e) {

            //Toast.makeText(this,e, Toast.LENGTH_SHORT).show();
            Log.d("catch", "IN Catch");
        }

        return sum;
    }

    public Double setTotalBills() {

        Double sum = 0.0;
        try {

            String selectQuery = "SELECT SUM(BillsAmount) FROM Bills";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    sum = cursor.getDouble(0);


                } while (cursor.moveToNext());
            }
            TotalAmount obj = TotalAmount.getInstance();
            obj.setTotalBill(sum);
            addTotals();
            cursor.close();
            db.close();

        } catch (Exception e) {

            //Toast.makeText(this,e,Toast.LENGTH_SHORT).show();
            Log.d("catch", "IN Catch");
        }



        return sum;
    }


    // **************** Get total Budget*******************

    public Double setTotalBudget() {

        Double sum = 0.0;
        try {

            String selectQuery = "SELECT SUM(BudgetAmount) FROM Budget";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    sum = cursor.getDouble(0);


                } while (cursor.moveToNext());
            }
            TotalAmount obj = TotalAmount.getInstance();
            obj.setTotalBudget(sum);
            addTotals();
            cursor.close();
            db.close();

        } catch (Exception e) {

            //Toast.makeText(this,e,Toast.LENGTH_SHORT).show();
            Log.d("catch", "IN Catch");
        }



        return sum;
    }


// **************** Get total Available*******************

    public Double setTotalAvailable() {

        Double sum = 0.0;
        try {


            TotalAmount obj = TotalAmount.getInstance();
            if(obj.getTotalBudget() != 0) {
                sum = obj.getTotalBudget() - obj.getTotalExpense();
            }
            else{
                sum = obj.getTotalIncome() - obj.getTotalExpense();
            }

            obj.setTotalAvailable(sum);
            updateTotal();


        } catch (Exception e) {

            //Toast.makeText(this,e,Toast.LENGTH_SHORT).show();
            Log.d("catch", "IN Catch");
        }



        return sum;
    }




    // ******************** Update Expenses ********************
    public void UpdateExpenses(AddExpensesActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("ExpensesName", String.valueOf(obj.getExpenses_name()));
        values.put("ExpensesAmount", String.valueOf(obj.getExpenses_amount()));
        values.put("ExpensesDate", String.valueOf(obj.getExpenses_date()));

        db.update(Expenses, values, "ExpensesID = ?", new String[]{String.valueOf(obj.getExpenses_id())});


        db.close();
        setTotalExpenses();
        updateTotal();

    }



    public boolean CheckExpensesEXIST(AddExpensesActivity obj) {

        return CHK_EXIST(Expenses, "ExpensesName", obj.getExpenses_name());

    }

    private String getDateForTotalsTable()
    {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int monthYear  = month * 10000 + year;
        return monthYear + "";
    }
    public boolean CheckTotalsEXIST() {

        return CHK_EXIST(Expenses, "TotalsID", getDateForTotalsTable());

    }



    // **************** Get All Expenses *******************
    public ArrayList<AddExpensesActivity> VewAllExpenses() {

        ArrayList<AddExpensesActivity> lstLoc = new ArrayList<AddExpensesActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Expenses;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddExpensesActivity object = new AddExpensesActivity();
                    object.setExpenses_id(cursor.getInt(cursor.getColumnIndex("ExpensesID")));
                    object.setExpenses_name(cursor.getString(cursor.getColumnIndex("ExpensesName")));
                    object.setExpenses_amount(cursor.getString(cursor.getColumnIndex("ExpensesAmount")));
                    object.setExpenses_date(cursor.getString(cursor.getColumnIndex("ExpensesDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }
/*
    // **************** Get All Totals *******************
    public ArrayList<TotalAmount> VewAllTotals() {

        ArrayList<AddExpensesActivity> lstLoc = new ArrayList<AddExpensesActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Expenses;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddExpensesActivity object = new AddExpensesActivity();
                    object.setExpenses_id(cursor.getInt(cursor.getColumnIndex("ExpensesID")));
                    object.setExpenses_name(cursor.getString(cursor.getColumnIndex("ExpensesName")));
                    object.setExpenses_amount(cursor.getString(cursor.getColumnIndex("ExpensesAmount")));
                    object.setExpenses_date(cursor.getString(cursor.getColumnIndex("ExpensesDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        //return lstLoc;

    }
*/
    // **************** Get Expenses By ID*******************

    public AddExpensesActivity getExpenses(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddExpensesActivity object = new AddExpensesActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Expenses + " Where ExpensesID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setExpenses_id(cursor.getInt(cursor
                        .getColumnIndex("ExpensesID")));
                object.setExpenses_name(cursor.getString(cursor
                        .getColumnIndex("ExpensesName")));
                object.setExpenses_amount(cursor.getString(cursor
                        .getColumnIndex("ExpensesAmount")));
                object.setExpenses_date(cursor.getString(cursor
                        .getColumnIndex("ExpensesDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Expenses *******************
    public void DeleteExpenses(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Expenses, "ExpensesID = ?", new String[]{String.valueOf(id)});

        db.close();
        setTotalExpenses();
        updateTotal();
    }


    // Bills

    // ******************** Adding Bills ********************
    public void AddBills(AddBillsActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Bills, "BillsID");
        ContentValues values = new ContentValues();
        values.put("BillsID", id);
        values.put("BillsName", String.valueOf(obj.getBills_name()));
        values.put("BillsDate", String.valueOf(obj.getBills_date()));
        values.put("BillsAmount", String.valueOf(obj.getBills_amount()));

        db.insert(Bills, null, values);

        db.close();
        setTotalBills();
        updateTotal();

    }


    // ******************** Update Bills ********************
    public void UpdateBills(AddBillsActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("BillsName", String.valueOf(obj.getBills_name()));
        values.put("BillsDate", String.valueOf(obj.getBills_date()));
        values.put("BillsDate", String.valueOf(obj.getBills_amount()));
        db.update(Bills, values, "BillsID = ?", new String[]{String.valueOf(obj.getBills_id())});

        db.close();
        setTotalBills();
        updateTotal();

    }

    public boolean CheckBillsEXIST(AddBillsActivity obj) {

        return CHK_EXIST(Bills, "BillsName", obj.getBills_name());

    }



    // **************** Get All Bills *******************
    public ArrayList<AddBillsActivity> VewAllBills() {

        ArrayList<AddBillsActivity> lstLoc = new ArrayList<AddBillsActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Bills;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddBillsActivity object = new AddBillsActivity();
                    object.setBills_id(cursor.getInt(cursor.getColumnIndex("BillsID")));
                    object.setBills_name(cursor.getString(cursor.getColumnIndex("BillsName")));
                    object.setBills_date(cursor.getString(cursor.getColumnIndex("BillsDate")));
                    object.setBills_amount(cursor.getString(cursor.getColumnIndex("BillsAmount")));
                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Bills By ID*******************

    public AddBillsActivity getBills(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddBillsActivity object = new AddBillsActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Bills + " Where BillsID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setBills_id(cursor.getInt(cursor
                        .getColumnIndex("BillsID")));
                object.setBills_name(cursor.getString(cursor
                        .getColumnIndex("BillsName")));
                object.setBills_date(cursor.getString(cursor
                        .getColumnIndex("BillsDate")));
                object.setBills_amount(cursor.getString(cursor
                        .getColumnIndex("BillsAmount")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Bills *******************
    public void DeleteBills(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Bills, "BillsID = ?", new String[]{String.valueOf(id)});
        db.close();
    }



    // ******************** Adding Income ********************
    public void AddIncome(AddIncomeActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Income, "IncomeID");
        ContentValues values = new ContentValues();
        values.put("IncomeID", id);
        values.put("IncomeName", String.valueOf(obj.getIncome_name()));
        values.put("IncomeAmount", String.valueOf(obj.getIncome_amount()));
        values.put("IncomeDate", String.valueOf(obj.getIncome_date()));

        db.insert(Income, null, values);

        db.close();
        setTotalIncome();
        updateTotal();

    }


    // ******************** Update Income ********************

    public void UpdateIncome(AddIncomeActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("IncomeName", String.valueOf(obj.getIncome_name()));
        values.put("IncomeAmount", String.valueOf(obj.getIncome_amount()));
        values.put("IncomeDate", String.valueOf(obj.getIncome_date()));

        db.update(Income, values, "IncomeID = ?", new String[]{String.valueOf(obj.getIncome_id())});

        db.close();
        setTotalIncome();
        updateTotal();

    }

    public boolean CheckIncomeEXIST(AddIncomeActivity obj) {

        return CHK_EXIST(Income, "IncomeName", obj.getIncome_name());

    }



    // **************** Get All Income *******************
    public ArrayList<AddIncomeActivity> VewAllIncome() {

        ArrayList<AddIncomeActivity> lstLoc = new ArrayList<AddIncomeActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Income;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddIncomeActivity object = new AddIncomeActivity();
                    object.setIncome_id(cursor.getInt(cursor.getColumnIndex("IncomeID")));
                    object.setIncome_name(cursor.getString(cursor.getColumnIndex("IncomeName")));
                    object.setIncome_amount(cursor.getString(cursor.getColumnIndex("IncomeAmount")));
                    object.setIncome_date(cursor.getString(cursor.getColumnIndex("IncomeDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Income By ID*******************

    public AddIncomeActivity getIncome(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddIncomeActivity object = new AddIncomeActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Income + " Where IncomeID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setIncome_id(cursor.getInt(cursor
                        .getColumnIndex("IncomeID")));
                object.setIncome_name(cursor.getString(cursor
                        .getColumnIndex("IncomeName")));
                object.setIncome_amount(cursor.getString(cursor
                        .getColumnIndex("IncomeAmount")));
                object.setIncome_date(cursor.getString(cursor
                        .getColumnIndex("IncomeDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Income *******************
    public void DeleteIncome(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Income, "IncomeID = ?", new String[]{String.valueOf(id)});
        db.close();
        setTotalIncome();
        updateTotal();
    }




    // ******************** Adding Rent ********************


    public void AddRent(AddRentActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Rent, "RentID");
        ContentValues values = new ContentValues();
        values.put("RentID", id);
        values.put("RentName", String.valueOf(obj.getRent_name()));
        values.put("RentAmount", String.valueOf(obj.getRent_amount()));
        values.put("RentDate", String.valueOf(obj.getRent_date()));

        db.insert(Rent, null, values);
        db.close();

    }


    // ******************** Update Rent ********************

    public void UpdateRent(AddRentActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("RentName", String.valueOf(obj.getRent_name()));
        values.put("RentAmount", String.valueOf(obj.getRent_amount()));
        values.put("RentDate", String.valueOf(obj.getRent_date()));

        db.update(Rent, values, "RentID = ?", new String[]{String.valueOf(obj.getRent_id())});

        db.close();

    }

    public boolean CheckRentEXIST(AddRentActivity obj) {

        return CHK_EXIST(Rent, "RentName", obj.getRent_name());

    }



    // **************** Get All Rent *******************

    public ArrayList<AddRentActivity> VewAllRent() {

        ArrayList<AddRentActivity> lstLoc = new ArrayList<AddRentActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Rent;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddRentActivity object = new AddRentActivity();
                    object.setRent_id(cursor.getInt(cursor.getColumnIndex("RentID")));
                    object.setRent_name(cursor.getString(cursor.getColumnIndex("RentName")));
                    object.setRent_amount(cursor.getString(cursor.getColumnIndex("RentAmount")));
                    object.setRent_date(cursor.getString(cursor.getColumnIndex("RentDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Rent By ID*******************

    public AddRentActivity getRent(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddRentActivity object = new AddRentActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Rent + " Where RentID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setRent_id(cursor.getInt(cursor
                        .getColumnIndex("RentID")));
                object.setRent_name(cursor.getString(cursor
                        .getColumnIndex("RentName")));
                object.setRent_amount(cursor.getString(cursor
                        .getColumnIndex("RentAmount")));
                object.setRent_date(cursor.getString(cursor
                        .getColumnIndex("RentDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Rent *******************

    public void DeleteRent(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Rent, "RentID = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ******************** Adding Food ********************
    public void AddFood(AddFoodActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Food, "FoodID");
        ContentValues values = new ContentValues();
        values.put("FoodID", id);
        values.put("FoodName", String.valueOf(obj.getFood_name()));
        values.put("FoodAmount", String.valueOf(obj.getFood_amount()));
        values.put("FoodDate", String.valueOf(obj.getFood_date()));

        db.insert(Food, null, values);
        db.close();

    }


    // ******************** Update Food ********************
    public void UpdateFood(AddFoodActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("FoodName", String.valueOf(obj.getFood_name()));
        values.put("FoodAmount", String.valueOf(obj.getFood_amount()));
        values.put("FoodDate", String.valueOf(obj.getFood_date()));

        db.update(Food, values, "FoodID = ?", new String[]{String.valueOf(obj.getFood_id())});

        db.close();

    }

    public boolean CheckFoodEXIST(AddFoodActivity obj) {

        return CHK_EXIST(Food, "FoodName", obj.getFood_name());

    }



    // **************** Get All Food *******************

    public ArrayList<AddFoodActivity> VewAllFood() {

        ArrayList<AddFoodActivity> lstLoc = new ArrayList<AddFoodActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Food;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddFoodActivity object = new AddFoodActivity();
                    object.setFood_id(cursor.getInt(cursor.getColumnIndex("FoodID")));
                    object.setFood_name(cursor.getString(cursor.getColumnIndex("FoodName")));
                    object.setFood_amount(cursor.getString(cursor.getColumnIndex("FoodAmount")));
                    object.setFood_date(cursor.getString(cursor.getColumnIndex("FoodDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Food By ID*******************

    public AddFoodActivity getFood(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddFoodActivity object = new AddFoodActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Food + " Where FoodID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setFood_id(cursor.getInt(cursor
                        .getColumnIndex("FoodID")));
                object.setFood_name(cursor.getString(cursor
                        .getColumnIndex("FoodName")));
                object.setFood_amount(cursor.getString(cursor
                        .getColumnIndex("FoodAmount")));
                object.setFood_date(cursor.getString(cursor
                        .getColumnIndex("FoodDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Food *******************
    public void DeleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Food, "FoodID = ?", new String[]{String.valueOf(id)});
        db.close();
    }



    // ******************** Adding Dep ********************
    public void AddDep(AddDepartmentalActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Dep, "DepID");
        ContentValues values = new ContentValues();
        values.put("DepID", id);
        values.put("DepName", String.valueOf(obj.getDep_name()));
        values.put("DepAmount", String.valueOf(obj.getDep_amount()));
        values.put("DepDate", String.valueOf(obj.getDep_date()));

        db.insert(Dep, null, values);
        db.close();

    }


    // ******************** Update Dep ********************

    public void UpdateDep(AddDepartmentalActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("DepName", String.valueOf(obj.getDep_name()));
        values.put("DepAmount", String.valueOf(obj.getDep_amount()));
        values.put("DepDate", String.valueOf(obj.getDep_date()));

        db.update(Dep, values, "DepID = ?", new String[]{String.valueOf(obj.getDep_id())});

        db.close();

    }

    public boolean CheckDepEXIST(AddDepartmentalActivity obj) {

        return CHK_EXIST(Dep, "DepName", obj.getDep_name());

    }



    // **************** Get All Dep *******************

    public ArrayList<AddDepartmentalActivity> VewAllDep() {

        ArrayList<AddDepartmentalActivity> lstLoc = new ArrayList<AddDepartmentalActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Dep;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddDepartmentalActivity object = new AddDepartmentalActivity();
                    object.setDep_id(cursor.getInt(cursor.getColumnIndex("DepID")));
                    object.setDep_name(cursor.getString(cursor.getColumnIndex("DepName")));
                    object.setDep_amount(cursor.getString(cursor.getColumnIndex("DepAmount")));
                    object.setDep_date(cursor.getString(cursor.getColumnIndex("DepDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Dep By ID*******************

    public AddDepartmentalActivity getDep(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddDepartmentalActivity object = new AddDepartmentalActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Dep + " Where DepID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setDep_id(cursor.getInt(cursor
                        .getColumnIndex("DepID")));
                object.setDep_name(cursor.getString(cursor
                        .getColumnIndex("DepName")));
                object.setDep_amount(cursor.getString(cursor
                        .getColumnIndex("DepAmount")));
                object.setDep_date(cursor.getString(cursor
                        .getColumnIndex("DepDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Dep *******************
    public void DeleteDep(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Dep, "DepID = ?", new String[]{String.valueOf(id)});
        db.close();
    }



    // ******************** Adding Ent ********************
    public void AddEnt(AddEntertainmentActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Ent, "EntID");
        ContentValues values = new ContentValues();
        values.put("EntID", id);
        values.put("EntName", String.valueOf(obj.getEnt_name()));
        values.put("EntAmount", String.valueOf(obj.getEnt_amount()));
        values.put("EntDate", String.valueOf(obj.getEnt_date()));

        db.insert(Ent, null, values);
        db.close();

    }


    // ******************** Update Ent ********************

    public void UpdateEnt(AddEntertainmentActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("EntName", String.valueOf(obj.getEnt_name()));
        values.put("EntAmount", String.valueOf(obj.getEnt_amount()));
        values.put("EntDate", String.valueOf(obj.getEnt_date()));

        db.update(Ent, values, "EntID = ?", new String[]{String.valueOf(obj.getEnt_id())});

        db.close();

    }

    public boolean CheckEntEXIST(AddEntertainmentActivity obj) {

        return CHK_EXIST(Ent, "EntName", obj.getEnt_name());

    }



    // **************** Get All Ent *******************

    public ArrayList<AddEntertainmentActivity> VewAllEnt() {

        ArrayList<AddEntertainmentActivity> lstLoc = new ArrayList<AddEntertainmentActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Ent;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddEntertainmentActivity object = new AddEntertainmentActivity();
                    object.setEnt_id(cursor.getInt(cursor.getColumnIndex("EntID")));
                    object.setEnt_name(cursor.getString(cursor.getColumnIndex("EntName")));
                    object.setEnt_amount(cursor.getString(cursor.getColumnIndex("EntAmount")));
                    object.setEnt_date(cursor.getString(cursor.getColumnIndex("EntDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Ent By ID*******************

    public AddEntertainmentActivity getEnt(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddEntertainmentActivity object = new AddEntertainmentActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Ent + " Where EntID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setEnt_id(cursor.getInt(cursor
                        .getColumnIndex("EntID")));
                object.setEnt_name(cursor.getString(cursor
                        .getColumnIndex("EntName")));
                object.setEnt_amount(cursor.getString(cursor
                        .getColumnIndex("EntAmount")));
                object.setEnt_date(cursor.getString(cursor
                        .getColumnIndex("EntDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Ent *******************
    public void DeleteEnt(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Ent, "EntID = ?", new String[]{String.valueOf(id)});
        db.close();
    }




    // ******************** Adding Car ********************

    public void AddCar(AddCarActivity obj){
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Car, "CarID");
        ContentValues values = new ContentValues();
        values.put("CarID", id);
        values.put("CarName", String.valueOf(obj.getCar_name()));
        values.put("CarAmount", String.valueOf(obj.getCar_amount()));
        values.put("CarDate", String.valueOf(obj.getCar_date()));

        db.insert(Car, null, values);
        db.close();

    }


    // ******************** Update Car ********************

    public void UpdateCar(AddCarActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("CarName", String.valueOf(obj.getCar_name()));
        values.put("CarAmount", String.valueOf(obj.getCar_amount()));
        values.put("CarDate", String.valueOf(obj.getCar_date()));

        db.update(Car, values, "CarID = ?", new String[]{String.valueOf(obj.getCar_id())});

        db.close();

    }

    public boolean CheckCarEXIST(AddCarActivity obj) {

        return CHK_EXIST(Car, "CarName", obj.getCar_name());

    }



    // **************** Get All Car *******************

    public ArrayList<AddCarActivity> VewAllCar() {

        ArrayList<AddCarActivity> lstLoc = new ArrayList<AddCarActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Car;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddCarActivity object = new AddCarActivity();
                    object.setCar_id(cursor.getInt(cursor.getColumnIndex("CarID")));
                    object.setCar_name(cursor.getString(cursor.getColumnIndex("CarName")));
                    object.setCar_amount(cursor.getString(cursor.getColumnIndex("CarAmount")));
                    object.setCar_date(cursor.getString(cursor.getColumnIndex("CarDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Car By ID*******************

    public AddCarActivity getCar(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddCarActivity object = new AddCarActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Car + " Where CarID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setCar_id(cursor.getInt(cursor
                        .getColumnIndex("CarID")));
                object.setCar_name(cursor.getString(cursor
                        .getColumnIndex("CarName")));
                object.setCar_amount(cursor.getString(cursor
                        .getColumnIndex("CarAmount")));
                object.setCar_date(cursor.getString(cursor
                        .getColumnIndex("CarDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Car *******************
    public void DeleteCar(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Car, "CarID = ?", new String[]{String.valueOf(id)});
        db.close();
    }

    // ******************** Adding Med ********************

    public void AddMed(AddMedActivity obj){
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Med, "MedID");
        ContentValues values = new ContentValues();
        values.put("MedID", id);
        values.put("MedName", String.valueOf(obj.getMed_name()));
        values.put("MedAmount", String.valueOf(obj.getMed_amount()));
        values.put("MedDate", String.valueOf(obj.getMed_date()));

        db.insert(Med, null, values);
        db.close();

    }


    // ******************** Update Med ********************

    public void UpdateMed(AddMedActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("MedName", String.valueOf(obj.getMed_name()));
        values.put("MedAmount", String.valueOf(obj.getMed_amount()));
        values.put("MedDate", String.valueOf(obj.getMed_date()));

        db.update(Med, values, "MedID = ?", new String[]{String.valueOf(obj.getMed_id())});

        db.close();

    }

    public boolean CheckMedEXIST(AddMedActivity obj) {

        return CHK_EXIST(Med, "MedName", obj.getMed_name());

    }



    // **************** Get All Med *******************

    public ArrayList<AddMedActivity> VewAllMed() {

        ArrayList<AddMedActivity> lstLoc = new ArrayList<AddMedActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Med;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddMedActivity object = new AddMedActivity();
                    object.setMed_id(cursor.getInt(cursor.getColumnIndex("MedID")));
                    object.setMed_name(cursor.getString(cursor.getColumnIndex("MedName")));
                    object.setMed_amount(cursor.getString(cursor.getColumnIndex("MedAmount")));
                    object.setMed_date(cursor.getString(cursor.getColumnIndex("MedDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Med By ID*******************

    public AddMedActivity getMed(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddMedActivity object = new AddMedActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Med + " Where MedID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setMed_id(cursor.getInt(cursor
                        .getColumnIndex("MedID")));
                object.setMed_name(cursor.getString(cursor
                        .getColumnIndex("MedName")));
                object.setMed_amount(cursor.getString(cursor
                        .getColumnIndex("MedAmount")));
                object.setMed_date(cursor.getString(cursor
                        .getColumnIndex("MedDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Med *******************
    public void DeleteMed(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Med, "MedID = ?", new String[]{String.valueOf(id)});
        db.close();
    }




    // ******************** Adding Budget ********************
    public void AddBudget(AddBudgetActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Budget, "BudgetID");
        ContentValues values = new ContentValues();
        values.put("BudgetID", id);
        values.put("BudgetCatg", String.valueOf(obj.getBudget_name()));
        values.put("BudgetAmount", String.valueOf(obj.getBudget_amount()));
        values.put("BudgetDate", String.valueOf(obj.getBudget_date()));

        db.insert(Budget, null, values);
        db.close();
        setTotalBudget();
        updateTotal();

    }





    // ******************** Update Budget ********************
    public void UpdateBudget(AddBudgetActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("BudgetCatg", String.valueOf(obj.getBudget_catg()));
        values.put("BudgetAmount", String.valueOf(obj.getBudget_amount()));
        values.put("BudgetDate", String.valueOf(obj.getBudget_date()));

        db.update(Budget, values, "BudgetID = ?", new String[]{String.valueOf(obj.getBudget_id())});

        db.close();
        setTotalBudget();
        updateTotal();


    }

    public boolean CheckBudgetEXIST(AddBudgetActivity obj) {

        return CHK_EXIST(Budget, "BudgetName", obj.getBudget_catg());

    }



    // **************** Get All Budget *******************
    public ArrayList<AddBudgetActivity> VewAllBudget() {

        ArrayList<AddBudgetActivity> lstLoc = new ArrayList<AddBudgetActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Budget;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddBudgetActivity object = new AddBudgetActivity();
                    object.setBudget_id(cursor.getInt(cursor.getColumnIndex("BudgetID")));
                    object.setBudget_catg(cursor.getString(cursor.getColumnIndex("BudgetCatg")));
                    object.setBudget_amount(cursor.getString(cursor.getColumnIndex("BudgetAmount")));
                    object.setBudget_date(cursor.getString(cursor.getColumnIndex("BudgetDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Budget By ID*******************

    public AddBudgetActivity getBudget(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddBudgetActivity object = new AddBudgetActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Budget + " Where BudgetID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setBudget_id(cursor.getInt(cursor
                        .getColumnIndex("BudgetID")));
                object.setBudget_catg(cursor.getString(cursor
                        .getColumnIndex("BudgetCatg")));
                object.setBudget_amount(cursor.getString(cursor
                        .getColumnIndex("BudgetAmount")));
                object.setBudget_date(cursor.getString(cursor
                        .getColumnIndex("BudgetDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Budget *******************
    public void DeleteBudget(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Budget, "BudgetID = ?", new String[]{String.valueOf(id)});
        db.close();
        setTotalBudget();
        updateTotal();

    }
}