package com.keepgo.whatdo.service.util;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.response.ExcelFTASubRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;

@Service
public interface UtilService {

	public List<InboundRes> changeExcelFormatNew(List<InboundRes> list);
	
	
	public Map<Long,String> getMakingForFTA(List<InboundRes> list);
	
	public Map<Long,String> getMakingForRCEP(List<InboundRes> list);
	
	public Map<Long,String> getMakingForYATAI(List<InboundRes> list);
	
}
