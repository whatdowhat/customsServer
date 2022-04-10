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
public class ExcelFTARes {

	
	//excel 파일 이름
	String fileNm;
	
	//1. 쉬퍼(수출자) 이름+주소
	String data01;
	//2. 고정
	@Builder.Default
	String data02 = "AVAILABLE TO CUSTOMS UPON REQUEST";
	//3. 통관사업자(컨사이니) 이름+주소
	String data03;
	//4. 출항일/항명항차/출항항구/입항항구  순
	String data04;
	//5. 1번과 동일
	String data05;
	//6. 한 품목당 하나의 숫자 부여. 5개품목이면 1~ 5
	String data06;
	//7. 마킹
	String data07;
	//8. 품명 / 맨 아래에 해당 하우스비엘에서 해당CO를 받는 박스수량
	String data08;
	//9. 세번
	String data09;
	//10. 고정
	@Builder.Default
	String data10 = "WP";
	//11. 수량
	Double data11;
	//12. 인보이스 번호/신청일
	String data12;
	
	String totalCountEng;
	
	//품목 리스트
	List<ExcelFTASubRes>  subItem;
}
