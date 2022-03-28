package com.keepgo.whatdo.service.inbound.impl;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;

@Component
public class InboundServiceImpl implements InboundService {

	
	@Autowired
	InboundRepository _inboundRepository;
	
	@Autowired
	FileUploadRepository _fileUploadRepository;

	@Autowired
	UtilService _UtilService;
	
	@Override
	public List<InboundRes> getList(InboundReq inboundReq) {
		
		Comparator<Inbound> com = Comparator.comparing(Inbound::getOrderNo);
		List<InboundRes> list =  _inboundRepository.findAll().stream()
				.sorted(com)
				.map(item->{
			 InboundRes inboundRes = 
					 InboundRes.builder()
					 .blNo(item.getBlNo())
					 .companyNm(item.getCompanyNm())
					 .memo1(item.getMemo1())
					 .marking(item.getMarking())
					 .orderNo(item.getOrderNo())
					 .build();
			 
			 return inboundRes;
			 
		 }).collect(Collectors.toList());
		
		
		return _UtilService.changeExcelFormatNew(list);
//		return null;
//		return list;
	}
	
	

	
}