package com.keepgo.whatdo.service.util;

import java.util.List;

import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.response.InboundRes;

@Service
public interface UtilService {

	public List<InboundRes> changeExcelFormatNew(List<InboundRes> list);
}
