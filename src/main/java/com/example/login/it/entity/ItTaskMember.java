package com.example.login.it.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "N_IT_TASKMEMBER")
public class ItTaskMember {

    @Id

    @Column(name = "EMPNIK")
    private String empnik;

    @Column(name = "TSKFCT")
    private String tskfct;

    @Column(name = "TSKGDG")
    private String tskgdg;

    @Column(name = "TSKMSL")
    private String tskmsl;

    @Column(name = "TSKSLS")
    private String tsksls;

    @Column(name = "TSKSTS")
    private String tsksts;

    @Column(name = "TSKTGM")
    private String tsktgm;

    @Column(name = "TSKJMS")
    private String tskjms;

    @Column(name = "UPDTGL")
    private String updtgl;

    public ItTaskMember() {
    }

    public String getEmpnik() {
        return empnik;
    }

    public void setEmpnik(String empnik) {
        this.empnik = empnik;
    }

    public String getTsksts() {
        return tsksts;
    }

    public void setTsksts(String tsksts) {
        this.tsksts = tsksts;
    }

    public String getTskfct() {
        return tskfct;
    }

    public void setTskfct(String tskfct) {
        this.tskfct = tskfct;
    }

    public String getTskgdg() {
        return tskgdg;
    }

    public void setTskgdg(String tskgdg) {
        this.tskgdg = tskgdg;
    }

    public String getTskmsl() {
        return tskmsl;
    }

    public void setTskmsl(String tskmsl) {
        this.tskmsl = tskmsl;
    }

    public String getTsktgm() {
        return tsktgm;
    }

    public void setTsktgm(String tsktgm) {
        this.tsktgm = tsktgm;
    }

    public String getTskjms() {
        return tskjms;
    }

    public void setTskjms(String tskjms) {
        this.tskjms = tskjms;
    }

    public String getUpdtgl() {
        return updtgl;
    }

    public void setUpdtgl(String updtgl) {
        this.updtgl = updtgl;
    }

    public String getTsksls() {
        return tsksls;
    }

    public void setTsksls(String tsksls) {
        this.tsksls = tsksls;
    }
}
    
