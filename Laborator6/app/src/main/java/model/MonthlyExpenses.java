package model;

public class MonthlyExpenses {
    public String month;

    public void setIncome(String income) {
        this.income = income;
    }

    public void setExpenses(String expenses) {
        this.expenses = expenses;
    }

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
