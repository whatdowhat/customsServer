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
public class ExcelContainerRes {

	
	//excel 파일 이름
	String fileNm;
	
	String hblnum;
	@Builder.Default
	String mblnum="";
	@Builder.Default
	String refnum="";
	@Builder.Default
	String hblios="I";
	@Builder.Default
	String srqnum="";
	@Builder.Default
	String invnum="";
	@Builder.Default
	String lccnum="";
	@Builder.Default
	String lccdte="";
	@Builder.Default
	String vescod="";
	
	String vesnam;
	String voynum;
	String dteonb;
	String dtearr;
	String hshcod;
	String hshnam;
	
	String hshadd;
	String hcncod;
	String hcnnam;
	String hcnadd;
	String hnfnam;
	@Builder.Default
	String hnfadd="";
	String hfwnam;
	String hfwadd;
	@Builder.Default
	String hprcod="";
	@Builder.Default
	String hprnam="";
	@Builder.Default
	String hprsta="";
	String hplcod;
	String hplnam;
	@Builder.Default
	String hplsta="";
	String hpdcod;
	
	String hpdnam;
	@Builder.Default
	String hpdsta="";
	String hpecod;
	String hpenam;
	@Builder.Default
	String hpesta="";
	
	String hfncod;
	String hfnnam;
	@Builder.Default
	String hfnsta="";
	String hpkqty;
	String hpkunt;
	
	String hwegwt;
	String hwecbm;
	@Builder.Default
	String hcgtyp="I";
	String hfclcl;
	String hbltyp;
	
	String hsetem;
	String hfttem;
	@Builder.Default
	String hbrcod="KRSEL";
	@Builder.Default
	String hbrnam="SEOUL, KOREA";
	@Builder.Default
	String pacdec="";
	
	String hblsay;
	@Builder.Default
	String hslcls="L";
	@Builder.Default
	String hblcur="USD";
	@Builder.Default
	String hblexr="1";
	String haccod;
	
	String hacnam;
	@Builder.Default
	String hpicod="JPCHI";
	@Builder.Default
	String hpinam="CHINA";
	@Builder.Default
	String hpycod="";
	@Builder.Default
	String hpynam="";
	@Builder.Default
	String hdccod="";
	@Builder.Default
	String hdcnam="";
	@Builder.Default
	String hapcod="YINGEER";
	@Builder.Default
	String hapnam="SHENZHEN YINGEER TRADING CO.,LTD";
	@Builder.Default
	String slscod="";
	@Builder.Default
	String slsnam="";
	@Builder.Default
	String cntdec="";
	@Builder.Default
	String hponum="";
	String mrkmrk;
	String hecdec;
	@Builder.Default
	String hctcod="";
	@Builder.Default
	String hctqty="";
	String hblseq;
	String hblatn;
	String hisdte;
	@Builder.Default
	String shpcid="";
	String cnecid;
	String nfycid;
	@Builder.Default
	String temcod="";
	@Builder.Default
	String svccls="";
	@Builder.Default
	String prjcod="";
	@Builder.Default
	String pftdat="";
	

	
	
	
}
