package com.mindex.challenge.data;

import org.springframework.data.mongodb.core.mapping.DBRef;

import java.math.BigDecimal;
import java.time.ZonedDateTime;

public class Compensation {

    private Employee employee;

    private BigDecimal salary;

    private ZonedDateTime effectiveDate;

    public Compensation(Employee employee, BigDecimal salary, ZonedDateTime effectiveDate) {
        this.employee = employee;
        this.salary = salary;
        this.effectiveDate = effectiveDate;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public BigDecimal getSalary() {
        return salary;
    }

    public void setSalary(BigDecimal salary) {
        this.salary = salary;
    }

    public ZonedDateTime getEffectiveDate() {
        return effectiveDate;
    }

    public void setEffectiveDate(ZonedDateTime effectiveDate) {
        this.effectiveDate = effectiveDate;
    }
}
