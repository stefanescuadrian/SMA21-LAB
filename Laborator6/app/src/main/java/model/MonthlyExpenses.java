package model;

public class MonthlyExpenses {
    public String month;
    private String income, expenses;

    public MonthlyExpenses(){

    }

    public MonthlyExpenses(String month, String income, String expenses){
        this.month = month;
        this.income = income;
        this.expenses = expenses;
    }

    public String getMonth(){
        return month;
    }

    public String getIncome() {
        return income;
    }

    public String getExpenses() {
        return expenses;
    }


}
