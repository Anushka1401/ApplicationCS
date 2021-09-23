package com.example.applicationcs;

import android.content.Intent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;

public class tableviewClass {
    String date, empid, empname, temperature;

    public tableviewClass() {

    }

    public tableviewClass(String date, String empid, String empname, String temperature) {
        this.date = date;
        this.empid = empid;
        this.empname = empname;
        this.temperature = temperature;
    }

    public static Comparator<tableviewClass> EmpNameAscComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            return e1.getEmpname().compareTo(e2.getEmpname());
        }
    };

    public static Comparator<tableviewClass> EmpNameDescComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            return e2.getEmpname().compareTo(e1.getEmpname());
        }
    };

    public static Comparator<tableviewClass> TempAscComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            int temp1 = Integer.parseInt(e1.getTemperature());
            int temp2 = Integer.parseInt(e2.getTemperature());
            return temp1 - temp2;
        }
    };

    public static Comparator<tableviewClass> TempDescComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            int temp1 = Integer.parseInt(e1.getTemperature());
            int temp2 = Integer.parseInt(e2.getTemperature());
            return temp2 - temp1;
        }
    };

    public static Comparator<tableviewClass> EmpIdDescComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            int id1 = Integer.parseInt(e1.getEmpid());
            int id2 = Integer.parseInt(e2.getEmpid());
            return id2 - id1;
        }
    };

    public static Comparator<tableviewClass> EmpIdAscComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            int id1 = Integer.parseInt(e1.getEmpid());
            int id2 = Integer.parseInt(e2.getEmpid());
            return id1 - id2;
        }
    };

    public static Comparator<tableviewClass> DateAscComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            String date1 = e1.getdate();
            String date2 = e2.getdate();
            Date strDate1=new Date();
            Date strDate2=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                strDate1 = sdf.parse(date1);
                strDate2 = sdf.parse(date2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return strDate1.compareTo(strDate2);
        }
    };

    public static Comparator<tableviewClass> DateDescComparator = new Comparator<tableviewClass>() {
        @Override
        public int compare(tableviewClass e1, tableviewClass e2) {
            String date1 = e1.getdate();
            String date2 = e2.getdate();
            Date strDate1=new Date();
            Date strDate2=new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            try {
                strDate1 = sdf.parse(date1);
                strDate2 = sdf.parse(date2);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return strDate2.compareTo(strDate1);
        }
    };

    public String getdate() {
        return date;
    }

    public void setdate(String date) {
        this.date = date;
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

}