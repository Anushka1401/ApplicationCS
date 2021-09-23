package com.example.applicationcs;

public class Employees {
    private String empId;
    private String empName;
    private String temperature;

    public  Employees(){

    }

    public Employees(String empId, String empName, String temperature) {
        this.empId = empId;
        this.empName = empName;
        this.temperature = temperature;
    }

    public String getEmpId() {
        return empId;
    }

    public String getEmpName() {
        return empName;
    }

    public String getTemperature() {
        return temperature;
    }



}
