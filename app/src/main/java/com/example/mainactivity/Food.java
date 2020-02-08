package com.example.mainactivity;

public class Food {
    private Integer foodid;
    private String foodname;
    private String foodCategory;
    private String calorieAmount;
    private String servingUnit;
    private String servingAmount;
    private String fat;

    public Food(Integer foodid, String foodname, String foodCategory, String calorieAmount, String servingUnit, String servingAmount, String fat) {
        this.foodid = foodid;
        this.foodname = foodname;
        this.foodCategory = foodCategory;
        this.calorieAmount = calorieAmount;
        this.servingUnit = servingUnit;
        this.servingAmount = servingAmount;
        this.fat = fat;
    }

    public Integer getFoodid() {
        return foodid;
    }

    public void setFoodid(Integer foodid) {
        this.foodid = foodid;
    }

    public String getFoodname() {
        return foodname;
    }

    public void setFoodname(String foodname) {
        this.foodname = foodname;
    }

    public String getFoodCategory() {
        return foodCategory;
    }

    public void setFoodCategory(String foodCategory) {
        this.foodCategory = foodCategory;
    }

    public String getCalorieAmount() {
        return calorieAmount;
    }

    public void setCalorieAmount(String calorieAmount) {
        this.calorieAmount = calorieAmount;
    }

    public String getServingUnit() {
        return servingUnit;
    }

    public void setServingUnit(String servingUnit) {
        this.servingUnit = servingUnit;
    }

    public String getServingAmount() {
        return servingAmount;
    }

    public void setServingAmount(String servingAmount) {
        this.servingAmount = servingAmount;
    }

    public String getFat() {
        return fat;
    }

    public void setFat(String fat) {
        this.fat = fat;
    }
}
