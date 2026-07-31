package com.example.login.adm.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "GX_EMPLOYE")
public class Employee {

	@Id
	@Column(name = "EMPNIK")
	private String empnik;

	@Column(name = "EMPNAM")
	private String empname;

	@Column(name = "DPTKOD")
	private String dptkod;

	@Column(name = "DPTNAM")
	private String dptnam;

	@Column(name = "FCTNAM")
	private String fctnam;

	@Column(name = "EMPSTS")
	private String empsts;

	@Column(name = "EMPKEL")
	private String empkel;

    @Column(name = "EMPLTP")
	private String empltp;

	@Column(name = "EMPTMT")
	private String emptmt;

	public Employee() {
	}

	public String getEmpnik() {
		return empnik;
	}

	public void setEmpnik(String empnik) {
		this.empnik = empnik;
	}

	public String getEmpname() {
		return empname;
	}

	public void setEmpname(String empname) {
		this.empname = empname;
	}

	public String getDptkod() {
		return dptkod;
	}

	public void setDptkod(String dptkod) {
		this.dptkod = dptkod;
	}

	public String getDptnam() {
		return dptnam;
	}

	public void setDptnam(String dptnam) {
		this.dptnam = dptnam;
	}

	public String getFctnam() {
		return fctnam;
	}

	public void setFctnam(String fctnam) {
		this.fctnam = fctnam;
	}

	public String getEmpsts() {
		return empsts;
	}

	public void setEmpsts(String empsts) {
		this.empsts = empsts;
	}

	public String getEmpkel() {
		return empkel;
	}

	public void setEmpkel(String empkel) {
		this.empkel = empkel;
	}

    public String getEmpltp() {
		return empltp;
	}

	public void setEmpltp(String empltp) {
		this.empltp = empltp;
	}

	public String getEmptmt() {
		return emptmt;
	}

	public void setEmptmt(String emptmt) {
		this.emptmt = emptmt;
	}


}
