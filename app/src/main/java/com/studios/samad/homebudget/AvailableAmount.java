package com.studios.samad.homebudget;

/**
 * Created by Azam on 4/1/2016.
 */
public class AvailableAmount  {

    private static AvailableAmount   _instance;

    private AvailableAmount()
    {

    }
    synchronized static AvailableAmount getInstance()
    {
        if (_instance == null)
        {
            _instance = new AvailableAmount();
        }
        return _instance;
    }


    public Double calculateAvailableAmount(String expenses, String income, String budget )
    {

        // remove  $ sign to convert in double
        expenses = expenses.substring(1);
        income = income.substring(1);
        budget = budget.substring(1);
        Double availableAmount;
        if(Double.parseDouble(budget) != 0 )
        {
            availableAmount =  Double.parseDouble(budget) - Double.parseDouble(expenses);

        }
        else
        {
            availableAmount = Double.parseDouble(income) - Double.parseDouble(expenses);
        }
        return  availableAmount;




    }

    public boolean availableAmountStatus(Double amount)
    {
        if(amount < 0 )
        {
            return true;
        }
        else
        {
            return false;
        }

    }
}
