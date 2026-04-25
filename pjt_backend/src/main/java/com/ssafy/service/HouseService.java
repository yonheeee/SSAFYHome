package com.ssafy.service;

import java.io.*;
import java.util.*;

import com.ssafy.dto.HouseInfoDto;
import tools.jackson.databind.*;

public class HouseService {

	// JSON과 자바 객체를 연결해주는 객체
	private ObjectMapper mapper = new ObjectMapper();
	
	public List<HouseInfoDto> getHouseList(File file) {
		mapper = new ObjectMapper();
		List<HouseInfoDto> list = new ArrayList<>();
		
		try {
			// 1. JSON 파일을 읽어서 List<HouseInfoDto>로 변환
			JsonNode root = mapper.readTree(file);
			
			JsonNode itemNode = root.path("response").path("body").path("items").path("item");
			
			if(itemNode.isArray()) {
				for(JsonNode node : itemNode) {
					HouseInfoDto house = new HouseInfoDto();
					
					String name = "";
					if(node.has("아파트")) {
						name = node.path("아파트").toString();
					} else if (node.has("연립다세대")) {
						name = node.path("연립다세대").toString();
					}
					house.setHouseName(name.replaceAll("\"", ""));
					
					house.setDeposit(safeString(node, "보증금액"));
					house.setMonthlyRent(safeString(node, "월세금액"));
					house.setDealPrice(safeString(node, "거래금액"));
					house.setBuildYear(safeString(node, "건축년도"));
					house.setDealYear(safeString(node, "년"));
					house.setDealMonth(safeString(node, "월"));
					house.setDealDay(safeString(node, "일"));
					house.setDongName(safeString(node, "법정동"));
					house.setArea(safeString(node, "전용면적"));
					house.setJibun(safeString(node, "지번"));
					house.setAreaCode(safeString(node, "지역코드"));
					house.setFloor(safeString(node, "층"));
					
					list.add(house);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return new ArrayList<>();
		}
		
		return list;
	}
	
	private String safeString(JsonNode node, String key) {
		JsonNode res = node.path(key);
		
		if(res.isMissingNode() || res.isNull()) {
			return "";
		}
		
		return res.toString().replace("\"", "");
	}

}
