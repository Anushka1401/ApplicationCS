package com.example.applicationcs;

public class HelperClass {
    String empid, empname, temperature;

    public HelperClass() {

    }

    public HelperClass(String empid, String empname, String temperature) {
        this.empid = empid;
        this.empname = empname;
        this.temperature = temperature;
    }

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
    }

    public String getEmpname() {
        return empname;
    }

    public void setEmpname(String empname) {
        this.empname = empname;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    @Override
    public String toString(){
        return empid + " has a temperature of " + temperature + "F";
    }
}
