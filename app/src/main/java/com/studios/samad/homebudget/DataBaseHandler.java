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

    private static final int DATABASE_VERSION = 17;
    private static final String DATABASE_NAME = "HomeBudget";

    private static final String Expenses = "Expenses";
    private static final String Bills = "Bills";
    private static final String Income = "Income";
    private static final String Budget = "Budget";
    private static final String Totals = "Totals";
    private static final String Account = "Account";

    public DataBaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS Expenses (ExpensesID INTEGER PRIMARY KEY,  ExpensesName  TEXT, ExpensesAmount  INT,	ExpensesDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Bills (BillsID INTEGER PRIMARY KEY,  BillsName  TEXT, BillsDate  TEXT, BillsAmount  DOUBLE )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Income (IncomeID INTEGER PRIMARY KEY, IncomeName  TEXT, IncomeAmount  INT,	IncomeDate  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Totals (TotalID INTEGER PRIMARY KEY, TotalExpense  DOUBLE, TotalBudget DOUBLE, TotalBill DOUBLE, TotalIncome  DOUBLE, TotalAccount  DOUBLE  , TotalAvailable  DOUBLE )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Budget (BudgetID INTEGER PRIMARY KEY, BudgetCatg  TEXT, BudgetAmount INT, BudgetDate  TEXT, BudgetDiscr  TEXT )");

        db.execSQL("CREATE TABLE IF NOT EXISTS Account (AccountID INTEGER PRIMARY KEY, AccountName  TEXT, AccountAmount  INT,	AccountDate  TEXT )");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS Expenses");
        db.execSQL("DROP TABLE IF EXISTS Bills");
        db.execSQL("DROP TABLE IF EXISTS Income");
        db.execSQL("DROP TABLE IF EXISTS Totals");
        db.execSQL("DROP TABLE IF EXISTS Budget");
        db.execSQL("DROP TABLE IF EXISTS Account");
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
        values.put("TotalAccount", String.valueOf(obj.getTotalAccount()));
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
        values.put("TotalAccount", String.valueOf(obj.getTotalAccount()));
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



    public Double setTotalAccount() {

        Double sum = 0.0;
        try {

            String selectQuery = "SELECT SUM(AccountAmount) FROM Account";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    sum = cursor.getDouble(0);


                } while (cursor.moveToNext());
            }
            TotalAmount obj = TotalAmount.getInstance();
            obj.setTotalAccount(sum);
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


    public Double getTotalBudgetCategory(String category) {

        Double sum = 0.0;
        try {

            String selectQuery = "SELECT SUM(BudgetAmount) FROM Budget where BudgetCatg='"+category+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    sum = cursor.getDouble(0);


                } while (cursor.moveToNext());
            }

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





    // ******************** Adding Account ********************
    public void AddAccounts(AddAccountActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Account, "AccountID");
        ContentValues values = new ContentValues();
        values.put("AccountID", id);
        values.put("AccountName", String.valueOf(obj.getAccounts_name()));
        values.put("AccountAmount", String.valueOf(obj.getAccounts_amount()));
        values.put("AccountDate", String.valueOf(obj.getAccounts_date()));

        db.insert(Account, null, values);

        db.close();
        setTotalAccount();
        updateTotal();

    }


    // ******************** Update Account ********************

    public void UpdateAccounts(AddAccountActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("AccountName", String.valueOf(obj.getAccounts_name()));
        values.put("AccountAmount", String.valueOf(obj.getAccounts_amount()));
        values.put("AccountDate", String.valueOf(obj.getAccounts_date()));

        db.update(Account, values, "AccountID = ?", new String[]{String.valueOf(obj.getAccounts_id())});

        db.close();
        setTotalAccount();
        updateTotal();

    }

    public boolean CheckAccountsEXIST(AddAccountActivity obj) {

        return CHK_EXIST(Account, "AccountName", obj.getAccounts_name());

    }



    // **************** Get All Account *******************
    public ArrayList<AddAccountActivity> VewAllAccount() {

        ArrayList<AddAccountActivity> lstLoc = new ArrayList<AddAccountActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Account;
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddAccountActivity object = new AddAccountActivity();
                    object.setAccounts_id(cursor.getInt(cursor.getColumnIndex("AccountID")));
                    object.setAccounts_name(cursor.getString(cursor.getColumnIndex("AccountName")));
                    object.setAccounts_amount(cursor.getString(cursor.getColumnIndex("AccountAmount")));
                    object.setAccounts_date(cursor.getString(cursor.getColumnIndex("AccountDate")));

                    lstLoc.add(object);

                } while (cursor.moveToNext());
            }

            cursor.close();
            db.close();

        } catch (Exception e) {
        }

        return lstLoc;

    }


    // **************** Get Account By ID*******************

    public AddAccountActivity getAccounts(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        AddAccountActivity object = new AddAccountActivity();


        try {

            String selectQuery = "SELECT  * FROM " + Account + " Where AccountID = " + id + " ";
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {

                object.setAccounts_id(cursor.getInt(cursor
                        .getColumnIndex("AccountID")));
                object.setAccounts_name(cursor.getString(cursor
                        .getColumnIndex("AccountName")));
                object.setAccounts_amount(cursor.getString(cursor
                        .getColumnIndex("AccountAmount")));
                object.setAccounts_date(cursor.getString(cursor
                        .getColumnIndex("AccountDate")));


            }

            cursor.close();
            db.close();

        } catch (Exception e) {

        }

        return object;
    }


    //**************** Delete Account *******************
    public void DeleteAccount(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(Account, "AccountID = ?", new String[]{String.valueOf(id)});
        db.close();
        setTotalAccount();
        updateTotal();
    }







    // ******************** Adding Budget ********************
    public void AddBudget(AddBudgetActivity obj) {
        SQLiteDatabase db = this.getWritableDatabase();
        Long id = selectMaxID(Budget, "BudgetID");
        ContentValues values = new ContentValues();
        values.put("BudgetID", id);
        values.put("BudgetCatg", String.valueOf(obj.getBudget_catg()));
        values.put("BudgetAmount", String.valueOf(obj.getBudget_amount()));
        values.put("BudgetDate", String.valueOf(obj.getBudget_date()));
        values.put("BudgetDiscr", String.valueOf(obj.getBudget_discr()));
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
        values.put("BudgetDiscr", String.valueOf(obj.getBudget_discr()));
        db.update(Budget, values, "BudgetID = ?", new String[]{String.valueOf(obj.getBudget_id())});

        db.close();
        setTotalBudget();
        updateTotal();


    }

    public boolean CheckBudgetEXIST(AddBudgetActivity obj) {

        return CHK_EXIST(Budget, "BudgetName", obj.getBudget_catg());

    }



    // **************** Get All Budget *******************
    public ArrayList<AddBudgetActivity> VewAllBudget(String category) {

        ArrayList<AddBudgetActivity> lstLoc = new ArrayList<AddBudgetActivity>();

        try {

            String selectQuery = "SELECT * FROM " + Budget +" where BudgetCatg='"+category+"'";
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {

                    AddBudgetActivity object = new AddBudgetActivity();
                    object.setBudget_id(cursor.getInt(cursor.getColumnIndex("BudgetID")));
                    object.setBudget_catg(cursor.getString(cursor.getColumnIndex("BudgetCatg")));
                    object.setBudget_amount(cursor.getString(cursor.getColumnIndex("BudgetAmount")));
                    object.setBudget_date(cursor.getString(cursor.getColumnIndex("BudgetDate")));
                    object.setBudget_discr(cursor.getString(cursor.getColumnIndex("BudgetDiscr")));
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


            }object.setBudget_discr(cursor.getString(cursor
                    .getColumnIndex("BudgetDiscr")));

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