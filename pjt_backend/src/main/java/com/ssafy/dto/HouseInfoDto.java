package com.ssafy.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class HouseInfoDto {
	@JsonAlias({"아파트", "연립다세대"})
	@JsonProperty("houseName")
	private String houseName;	// 건물 이름
	@JsonProperty("보증금액")
	private String deposit;		// 보증금액
	@JsonProperty("월세금액")
	private String monthlyRent;	// 월세금액
	@JsonProperty("거래금액")
	private String dealPrice;	// 거래금액
	@JsonProperty("건축년도")
	private String buildYear;	// 건축년도
	@JsonProperty("년")
	private String dealYear;	// 거래 연도
	@JsonProperty("월")
	private String dealMonth;	// 거래 월
	@JsonProperty("일")
	private String dealDay;		// 거래 일
	@JsonProperty("법정동")
	private String dongName;	// 법정동
	@JsonProperty("전용면적")
	private String area;		// 전용면적
	@JsonProperty("지번")
	private String jibun;		// 지번
	@JsonProperty("지역코드")
	private String areaCode;	// 지역코드
	@JsonProperty("층")
	private String floor;		// 층
	
	public HouseInfoDto() {
		super();
	}
	public HouseInfoDto(String houseName, String deposit, String monthlyRent, String dealPrice, String buildYear,
			String dealYear, String dealMonth, String dealDay, String dongName, String area, String jibun,
			String areaCode, String floor) {
		super();
		this.houseName = houseName;
		this.deposit = deposit;
		this.monthlyRent = monthlyRent;
		this.dealPrice = dealPrice;
		this.buildYear = buildYear;
		this.dealYear = dealYear;
		this.dealMonth = dealMonth;
		this.dealDay = dealDay;
		this.dongName = dongName;
		this.area = area;
		this.jibun = jibun;
		this.areaCode = areaCode;
		this.floor = floor;
	}
	public String getHouseName() {
		return houseName;
	}
	public void setHouseName(String houseName) {
		if(houseName == null || houseName.trim().isEmpty()) {
			this.houseName = (this.dongName != null ? this.dongName : "지역 미상") + " 연립다세대";
		} else {
			this.houseName = houseName.trim();
		}
	}
	public String getDeposit() {
		return deposit;
	}
	public void setDeposit(String deposit) {
		this.deposit = deposit;
	}
	public String getMonthlyRent() {
		return monthlyRent;
	}
	public void setMonthlyRent(String monthlyRent) {
		this.monthlyRent = monthlyRent;
	}
	public String getDealPrice() {
		return dealPrice;
	}
	public void setDealPrice(String dealPrice) {
		this.dealPrice = dealPrice;
	}
	public String getBuildYear() {
		return buildYear;
	}
	public void setBuildYear(String buildYear) {
		this.buildYear = buildYear;
	}
	public String getDealYear() {
		return dealYear;
	}
	public void setDealYear(String dealYear) {
		this.dealYear = dealYear;
	}
	public String getDealMonth() {
		return dealMonth;
	}
	public void setDealMonth(String dealMonth) {
		this.dealMonth = dealMonth;
	}
	public String getDealDay() {
		return dealDay;
	}
	public void setDealDay(String dealDay) {
		this.dealDay = dealDay;
	}
	public String getDongName() {
		return dongName;
	}
	public void setDongName(String dongName) {
		this.dongName = dongName;
	}
	public String getArea() {
		return area;
	}
	public void setArea(String area) {
		this.area = area;
	}
	public String getJibun() {
		return jibun;
	}
	public void setJibun(String jibun) {
		this.jibun = jibun;
	}
	public String getAreaCode() {
		return areaCode;
	}
	public void setAreaCode(String areaCode) {
		this.areaCode = areaCode;
	}
	public String getFloor() {
		return floor;
	}
	public void setFloor(String floor) {
		this.floor = floor;
	}
	@Override
	public String toString() {
		return "HouseInfoDto [houseName=" + houseName + ", deposit=" + deposit + ", monthlyRent=" + monthlyRent
				+ ", dealPrice=" + dealPrice + ", buildYear=" + buildYear + ", dealYear=" + dealYear + ", dealMonth="
				+ dealMonth + ", dealDay=" + dealDay + ", dongName=" + dongName + ", area=" + area + ", jibun=" + jibun
				+ ", areaCode=" + areaCode + ", floor=" + floor + "]";
	}
}
