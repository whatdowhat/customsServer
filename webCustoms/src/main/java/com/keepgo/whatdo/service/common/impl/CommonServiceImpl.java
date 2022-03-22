package com.keepgo.whatdo.service.common.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.request.CommonMasterReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.response.CommonMasterRes;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.common.CommonService;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	CommonRepository _commonRepository;
	@Autowired
	CommonMasterRepository _commonMasterRepository;
	
	@Autowired
	UserRepository _userRepository;

	@Override
	public List<?> getMasterAll(CommonMasterReq commonMasterReq) {

		List<?> result = _commonMasterRepository.findAll().stream()
				.map(item->CommonMasterRes.builder()
						.id(item.getId())
						.name(item.getNm())
						.build())
		.collect(Collectors.toList());
		
		return result;
	}

	@Override
	public List<?> getCommonAll(CommonReq commonReq) {
		
		List<?> result = _commonRepository.findAll().stream()
				.map(item->CommonRes.builder()
						.id(item.getId())
						.value(item.getValue())
						.commonMasterId(item.getCommonMaster().getId())
						.isUsing(item.getIsUsing())
						.createDt(item.getCreateDt())
						.updateDt(item.getUpdateDt())
						.name(item.getNm())
						.build())
		.collect(Collectors.toList());
		
		return result;
	}

	@Override
	public List<?> getCommonByMaster(CommonReq commonReq) {
		
		
		
		List<?> result = _commonRepository.findByCommonMaster(_commonMasterRepository.findById(commonReq.getCommonMasterId()).orElse(CommonMaster.builder().build())).stream()
				.map(item->CommonRes.builder()
						.id(item.getId())
						.value(item.getValue())
						.value2(item.getValue2())
						.commonMasterId(item.getCommonMaster().getId())
						.isUsing(item.getIsUsing())
						.createDt(item.getCreateDt())
						.updateDt(item.getUpdateDt())
						.name(item.getNm())
						.build())
		.collect(Collectors.toList());
		return result;
	}

	
}
