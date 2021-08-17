package hu.schdesign.solarboat.model;

import java.util.ArrayList;

public class AllSponsors {
    ArrayList<Sponsor> main = new ArrayList<>();
    ArrayList<Sponsor> top = new ArrayList<>();
    ArrayList<Sponsor> other = new ArrayList<>();
    ArrayList<Sponsor> partner = new ArrayList<>();
    ArrayList<Sponsor> uni = new ArrayList<>();

    public ArrayList<Sponsor> getMain() {
        return main;
    }

    public void setMain(ArrayList<Sponsor> main) {
        this.main = main;
    }

    public ArrayList<Sponsor> getTop() {
        return top;
    }

    public void setTop(ArrayList<Sponsor> top) {
        this.top = top;
    }

    public ArrayList<Sponsor> getOther() {
        return other;
    }

    public void setOther(ArrayList<Sponsor> other) {
        this.other = other;
    }

    public ArrayList<Sponsor> getPartner() {
        return partner;
    }

    public void setPartner(ArrayList<Sponsor> partner) {
        this.partner = partner;
    }

    public ArrayList<Sponsor> getUni() {
        return uni;
    }

    public void setUni(ArrayList<Sponsor> uni) {
        this.uni = uni;
    }
}
