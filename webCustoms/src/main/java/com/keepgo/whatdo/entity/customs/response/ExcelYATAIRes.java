package com.keepgo.whatdo.entity.customs.response;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExcelYATAIRes {

	
	//excel 파일 이름
	String fileNm;
	
	//1. 쉬퍼(수출자) 이름
	String data01;
	
	//2. 통관업체명+주소
	String data02;
	//3. 출항항구
	String data03;
	//4. 입항항구
	String data04;
	//5.세번
	String data05;
	//6. 마킹
	String data06;
	//7. 품명 / 맨 아래에  박스수량 합
	String data07;
		
	//8. 고정
	@Builder.Default
	String data08 = "A";
	//9. 수량
	Double data09;
	//10. 인보이스 번호/신청일
	String data10;
	
	
	String totalBoxCountEng;
	// 포장단위
	String packingType;
	
	
	//품목 리스트
	List<ExcelYATAISubRes>  subItem;
}
