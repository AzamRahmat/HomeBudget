package com.studios.samad.homebudget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class TotalAmount extends AppCompatActivity {


    private Double  totalExpense;
    private Double totalBudget;
    private Double totalBill;
    private Double totalAvailable;
    private int totals_id;
    DataBaseHandler dbHandler;



    private static TotalAmount   _instance;
    private Double totalIncome;


    private Double totalAccount;

    private TotalAmount()
    {

    }
    synchronized static TotalAmount getInstance()
    {
        if (_instance == null)
        {
            _instance = new TotalAmount();
        }
        return _instance;
    }


    public Double getTotalExpense() {

        if (totalExpense==null) {
            totalExpense = 0.0;
        }
        return totalExpense;
    }
    public Double getTotalBudget() {

        if (totalBudget == null)
       {
            totalBudget = 0.0;
        }
        return totalBudget;
    }

    public Double getTotalAvailable() {

        if (totalAvailable==null) {
            totalAvailable = 0.0;
        }
        return totalAvailable;
    }
    public void setTotalAvailable(Double totalAvailable) {

        this.totalAvailable = totalAvailable;

    }
    public void setTotalBudget(Double totalBudget) {

        this.totalBudget = totalBudget;
    }

    public void setTotalExpense(Double totalExpense) {

        this.totalExpense = totalExpense;
    }

    public Double getTotalBill() {

        if (totalBill == null)
        {
            totalBill = 0.0;
        }
        return totalBill;
    }
    public void setTotalBill(Double totalBill) {

        this.totalBill = totalBill;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_total_amount);
        dbHandler = new DataBaseHandler(this);
    }
    public int getTotals_id() {

        return totals_id;
    }

    public void setTotals_id(int totals_id) {

        this.totals_id = totals_id;
    }

    public void setTotalIncome(Double totalIncome) {

        this.totalIncome = totalIncome;
    }
    public Double getTotalIncome() {

        if (totalIncome == null) {
           totalIncome = 0.0;
        }
        return totalIncome;
    }


    public void setTotalAccount(Double totalAccount) {
        this.totalAccount = totalAccount;
    }

    public Double getTotalAccount() {
        return totalAccount;
    }

}
