package com.upt.cti.smartwallet.model;

public class MonthlyExpenses {
    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public float getExpenses() {
        return expenses;
    }

    public void setExpenses(float expenses) {
        this.expenses = expenses;
    }

    public String month;
    private float income, expenses;

    public MonthlyExpenses(){

    }

    public MonthlyExpenses(String month, float income, float expenses){
        this.month = month;
        this.income = income;
        this.expenses = expenses;
    }
}
