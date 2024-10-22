package com.mindex.challenge.data;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;

public class Compensation {

    private String employeeId;
    private int salary;

    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate effectiveDate;

    public Compensation() {
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public int getSalary() {
        return this.salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public LocalDate getEffectiveDate() {
        return this.effectiveDate;
    }

    public void setEffectiveDate(LocalDate effectiveDate) {
        this.effectiveDate = effectiveDate;
    }

}
