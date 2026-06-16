package com.example.experiment2.entity;

import java.math.BigDecimal;

public class BorrowRule {

    private Long id;
    private String ruleName;
    private Integer borrowDays;
    private Integer renewDays;
    private Integer maxRenewCount;
    private BigDecimal finePerDay;
    private String status;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public Integer getBorrowDays() {
        return borrowDays;
    }

    public void setBorrowDays(Integer borrowDays) {
        this.borrowDays = borrowDays;
    }

    public Integer getRenewDays() {
        return renewDays;
    }

    public void setRenewDays(Integer renewDays) {
        this.renewDays = renewDays;
    }

    public Integer getMaxRenewCount() {
        return maxRenewCount;
    }

    public void setMaxRenewCount(Integer maxRenewCount) {
        this.maxRenewCount = maxRenewCount;
    }

    public BigDecimal getFinePerDay() {
        return finePerDay;
    }

    public void setFinePerDay(BigDecimal finePerDay) {
        this.finePerDay = finePerDay;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
