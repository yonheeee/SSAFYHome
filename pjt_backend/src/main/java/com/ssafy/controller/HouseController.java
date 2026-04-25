package com.ssafy.controller;

import java.io.*;
import java.util.*;

import com.ssafy.dto.*;
import com.ssafy.service.*;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import tools.jackson.databind.ObjectMapper;

@WebServlet("/house")
public class HouseController extends HttpServlet implements ControllerHelper {

	private HouseService hService = new HouseService();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = getActionParameter(request, response);

		switch (action) {
		case "search", "search-form" -> searchHouseList(request, response);
		case "detail" -> showHouseDetail(request, response);
		default -> response.sendError(HttpServletResponse.SC_NOT_FOUND);
		}
	}

	private void showHouseDetail(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String houseName = request.getParameter("houseName");
		String dealType = request.getParameter("dealType");
		if (dealType == null || dealType.isEmpty())
			dealType = "apt-deal";

		// 1. 검색 시와 동일한 데이터 파일 로드
		String path = getServletContext().getRealPath("/data/" + dealType + ".json");
		File file = new File(path);
		List<HouseInfoDto> allList = hService.getHouseList(file);

		// 2. 클릭한 건물명(houseName)과 일치하는 데이터만 필터링
		List<HouseInfoDto> detailList = new ArrayList<>();
		if (allList != null) {
			for (HouseInfoDto h : allList) {
				if (h.getHouseName() != null && h.getHouseName().trim().equals(houseName.trim())) {
					detailList.add(h);
				}
			}
		}

		// 3. 결과 전달
		request.setAttribute("detailList", detailList);
		request.setAttribute("targetHouseName", houseName);

		// 조각 JSP로 포워딩
		forward(request, response, "/pages/houseDetail.jsp");
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void searchHouseList(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String sido = request.getParameter("sido");
		String sidoName = request.getParameter("sidoName");
		String gugun = request.getParameter("gugun"); // 구군 코드 (예: 11110)
		String gugunName = request.getParameter("gugunName");
		String dong = request.getParameter("dong");
		String dongName = request.getParameter("dongName"); // 동 이름 (예: 익선동)

		// 서버 콘솔 출력
		System.out.println("========= [검색 디버깅] =========");
		System.out.println("선택 지역: " + gugunName + " " + dongName);
		System.out.println("지역 코드: " + gugun);
		// 만약 위경도 파라미터도 있다면 함께 출력 가능
		System.out.println("================================");

		String dealType = request.getParameter("dealType");
		if (dealType == null || dealType.isEmpty())
			dealType = "apt-deal";

		// 1. JSON 파일 읽기
		String path = getServletContext().getRealPath("/data/" + dealType + ".json");
		File file = new File(path);
		List<HouseInfoDto> list = hService.getHouseList(file);

		// 2. 필터링 리스트 생성
		List<HouseInfoDto> filteredList = new ArrayList<>();

		// 3. 자바 로직으로 정밀 비교 (중복 이름 방지)
		if (list != null) {
			for (HouseInfoDto house : list) {

				String areaCodeInJson = house.getAreaCode() != null ? house.getAreaCode().trim() : "";
				String dongNameInJson = house.getDongName() != null ? house.getDongName().trim() : "";

				boolean isCodeMatch = areaCodeInJson.equals(gugun);
				boolean isNameMatch = dongNameInJson.equals(dongName.trim());

				if (isCodeMatch && isNameMatch) {
					filteredList.add(house);
				}
			}
		}

		// 4. 결과값 세팅
		request.setAttribute("houseList", filteredList);
		request.setAttribute("selectedSido", sido);
		request.setAttribute("selectedSidoName", sidoName);
		request.setAttribute("selectedGugun", gugun);
		request.setAttribute("selectedGugunName", gugunName);
		request.setAttribute("selectedDong", dong);
		request.setAttribute("selectedDongName", dongName);
		request.setAttribute("selectedType", dealType);

		forward(request, response, "/pages/search.jsp");
	}

}
