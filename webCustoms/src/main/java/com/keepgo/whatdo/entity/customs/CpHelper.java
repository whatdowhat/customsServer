package com.keepgo.whatdo.entity.customs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

//@Entity
//@Table(name = "web_unbi")
@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class CpHelper {

	private int rowNumber;
	private int rowStart;
	private int rowEnd;
	private int columnIndexStart;
	private int columnIndexEnd;
	private String format;

	
	
}
