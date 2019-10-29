package com.example.ourgraduationproject;

public class couponClass {

    public int copid;
    public String coptype;
    public String copowner;
    public String date;
    public String vaidation;

    public couponClass()
    {
        copid=0;
        coptype="";
        copowner="";
        date="";
        vaidation="";


    }
    public couponClass(int copid,String coptype,String copowner,String date,String vaidation)
    {
        this.copid=copid;
        this.coptype=coptype;
        this.copowner=copowner;
        this.date=date;
        this.vaidation=vaidation;

    }

}
