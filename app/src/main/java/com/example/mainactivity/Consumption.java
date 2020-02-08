package com.example.mainactivity;

public class Consumption {
    private Integer consumptionid;
    private String consumptionData;
    private String servingQuantity;
    private Appuser userid;
    private Food foodid;

    public Consumption(Integer consumptionid, String consumptionData, String servingQuantity, Appuser userid, Food foodid) {
        this.consumptionid = consumptionid;
        this.consumptionData = consumptionData;
        this.servingQuantity = servingQuantity;
        this.userid = userid;
        this.foodid = foodid;
    }

    public Integer getConsumptionid() {
        return consumptionid;
    }

    public void setConsumptionid(Integer consumptionid) {
        this.consumptionid = consumptionid;
    }

    public String getConsumptionData() {
        return consumptionData;
    }

    public void setConsumptionData(String consumptionData) {
        this.consumptionData = consumptionData;
    }

    public String getServingQuantity() {
        return servingQuantity;
    }

    public void setServingQuantity(String servingQuantity) {
        this.servingQuantity = servingQuantity;
    }

    public Appuser getUserid() {
        return userid;
    }

    public void setUserid(Appuser userid) {
        this.userid = userid;
    }

    public Food getFoodid() {
        return foodid;
    }

    public void setFoodid(Food foodid) {
        this.foodid = foodid;
    }
}
