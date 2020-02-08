package com.example.mainactivity;

public class Report {
    private Integer reportid;
    private String reportDate;
    private String totalCalorieConsumed;
    private String totalCalorieBurned;
    private String totalStepsTaken;
    private String calorieGoalToday;
    private Appuser userid;

    public Report(Integer reportid, String reportDate, String totalCalorieConsumed, String totalCalorieBurned, String totalStepsTaken, String calorieGoalToday, Appuser userid) {
        this.reportid = reportid;
        this.reportDate = reportDate;
        this.totalCalorieConsumed = totalCalorieConsumed;
        this.totalCalorieBurned = totalCalorieBurned;
        this.totalStepsTaken = totalStepsTaken;
        this.calorieGoalToday = calorieGoalToday;
        this.userid = userid;
    }

    public Integer getReportid() {
        return reportid;
    }

    public void setReportid(Integer reportid) {
        this.reportid = reportid;
    }

    public String getReportDate() {
        return reportDate;
    }

    public void setReportDate(String reportDate) {
        this.reportDate = reportDate;
    }

    public String getTotalCalorieConsumed() {
        return totalCalorieConsumed;
    }

    public void setTotalCalorieConsumed(String totalCalorieConsumed) {
        this.totalCalorieConsumed = totalCalorieConsumed;
    }

    public String getTotalCalorieBurned() {
        return totalCalorieBurned;
    }

    public void setTotalCalorieBurned(String totalCalorieBurned) {
        this.totalCalorieBurned = totalCalorieBurned;
    }

    public String getTotalStepsTaken() {
        return totalStepsTaken;
    }

    public void setTotalStepsTaken(String totalStepsTaken) {
        this.totalStepsTaken = totalStepsTaken;
    }

    public String getCalorieGoalToday() {
        return calorieGoalToday;
    }

    public void setCalorieGoalToday(String calorieGoalToday) {
        this.calorieGoalToday = calorieGoalToday;
    }

    public Appuser getUserid() {
        return userid;
    }

    public void setUserid(Appuser userid) {
        this.userid = userid;
    }
}
