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
public class ExcelInpackRes {

	
	//excel 파일 이름
	String fileNm;
	
	//1. 쉬퍼(수출자) 이름+주소
	String data01;
	//2. 고정
	
	String data02;
	//3. 인보이스번호
	String data03;
	//4. CO발급일자
	@Builder.Default
	String data04="2022-01-01";
	//5. 컨사이니명+사업자번호+주소
	String data05;
	//6. 출항일
	String data06;
	//7. 항명/항차
	String data07;
	//8. 출항 항구
	String data08;
	//9. 입항 항구
	String data09;
//	//10. Unit price
	String data10 ;
	//11. Amount
	String data11 ;
	//12. CTNS
	String data12;
//	//11. 수량
//	Double data11;
//	//12. 인보이스 번호/신청일
//	String data12;
	
	String totalCountEng;
	Long inboundMasterId;
	Boolean coYn;
	String invoice;
	String noCoDt;
	Integer countPCS;
	Integer countM;
	Integer countKG;
	Integer countYD;
	Integer countSET;
	Integer countDOZ;
	Integer countPAIR;
	//품목 리스트
	List<ExcelInpackSubRes>  subItem;
}
