package edu.gatech.hackathon.verve;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class RealmItem extends RealmObject {

    @PrimaryKey
    long id;

    String name;
    int days;
    double cost;

    public RealmItem() {

    }

    public RealmItem(String name, int days, double cost) {
        this.name = name;
        this.days = days;
        this.cost = cost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDays(int days) {
        this.days = days;
    }

    public void setCost(double cost) {
        this.cost = cost;
    }
}
