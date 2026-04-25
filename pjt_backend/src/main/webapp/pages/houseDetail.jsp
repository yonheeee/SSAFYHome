<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<div class="p-3">
    <%-- 제목: URL 파라미터로 넘어온 값을 직접 사용하거나 속성 사용 --%>
    <h5 class="fw-bold text-primary mb-4 border-bottom pb-2">
        <c:out value="${targetHouseName}" default="상세 정보" />
    </h5>
    
    <c:if test="${not empty detailList}">
        <c:forEach var="h" items="${detailList}" varStatus="vs">
            <div class="card mb-3 shadow-sm">
                <div class="card-header bg-light fw-bold py-2">
                    정보 #${vs.count}
                </div>
                <div class="card-body p-0">
                    <table class="table table-sm mb-0">
                        <colgroup>
                            <col style="width: 40%">
                            <col style="width: 60%">
                        </colgroup>
                        <tbody>
                            <tr>
                                <th class="table-light ps-3">거래 금액</th>
                                <td class="fw-bold text-danger">
                                    <%-- DTO의 getter 이름이 getDealPrice/getDeposit이 맞는지 확인 필요 --%>
                                    <c:out value="${h.dealPrice}" default="0" /> / <c:out value="${h.deposit}" default="0" /> 만원
                                </td>
                            </tr>
                            <tr>
                                <th class="table-light ps-3">전용 면적</th>
                                <td><c:out value="${h.area}" />㎡</td>
                            </tr>
                            <tr>
                                <th class="table-light ps-3">층수</th>
                                <td><c:out value="${h.floor}" />층</td>
                            </tr>
                            <tr>
                                <th class="table-light ps-3">건축 년도</th>
                                <td><c:out value="${h.buildYear}" default="-" />년</td>
                            </tr>
                            <tr>
                                <th class="table-light ps-3">법정동</th>
                                <td><c:out value="${h.dongName}" /></td>
                            </tr>
                            <tr>
                                <th class="table-light ps-3">지번</th>
                                <td><c:out value="${h.jibun}" /></td>
                            </tr>
                        </tbody>
                    </table>
                </div>
            </div>
        </c:forEach>
    </c:if>

    <c:if test="${empty detailList}">
        <div class="text-center py-5">
            <p class="text-muted">데이터가 존재하지 않거나 불러올 수 없습니다.</p>
        </div>
    </c:if>
</div>