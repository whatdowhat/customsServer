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
public class ExcelCountDetailRes {

	
	//excel 파일 이름
	String fileNm;

	String blNo;
	String marking;
	Double boxCount;
	Double itemCount;
	String engNm;
	String jejil;
	
	
	
	@Builder.Default
	private int markingSpan = 1;
	@Builder.Default
	private int engNmSpan=1;
	
	
}
