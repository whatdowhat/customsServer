package com.keepgo.whatdo.service.finalInbound.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.service.finalInbound.FinalInboundService;

@Service
public class FinalInboundServiceImpl implements FinalInboundService {

	@Autowired
	CompanyInfoRepository _companyInfoRepository;

	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;

	@Autowired
	CompanyInfoManageRepository _companyInfoManageRepository;

	@Autowired
	CommonRepository _commonRepository;

	@Autowired
	FinalInboundRepository _finalInboundRepository;
	
	@Autowired
	FinalInboundInboundMasterRepository _finalInboundInboundMasterRepository;
	
	@Autowired
	InboundMasterRepository _inboundMasterRepository;

	@Override
	public List<?> getAll(CompanyInfoReq req) {

		List<?> list = _companyInfoRepository.findAll().stream()
				.sorted(Comparator.comparing(CompanyInfo::getUpdateDt).reversed()).map(item -> {

					CompanyInfoRes dto = CompanyInfoRes.builder()
//						.id(item.getId())
//					.id(item.getId()).coAddress(item.getCoAddress()).coNm(item.getCoNm()).coNum(item.getCoNum())
//					.coInvoice(item.getCoInvoice()).updateDt(item.getUpdateDt())
//					.manages(item.getManages().stream().map(subitem -> subitem.getCommon())
//							.collect(Collectors.toMap(Common::getId, t -> t.getValue())))
//					.exports(item.getExports().stream().map(subitem -> subitem.getCommon())
////							.collect(Collectors.toMap(Common::getId, t -> t.getValue())))
////							.collect(Collectors.toMap(Common::getId, t -> t.getValue() ,Common::getId, t -> t.getValue() )))
//					.build();

							.id(item.getId()).coAddress(item.getCoAddress()).coNm(item.getCoNm()).coNum(item.getCoNum())
							.coInvoice(item.getCoInvoice()).updateDt(item.getUpdateDt()).coNmEn(item.getCoNmEn())
							.isUsing(item.getIsUsing()).createDt(item.getCreateDt()).consignee(item.getConsignee())
							.manager(item.getManager())
//					.managers(item.getManages().stream().map(sub_item->{
//						Map<String,Object> f = new HashMap<>();
//						f.put("comNm", sub_item.getCommon().getNm());
//						f.put("comValue", sub_item.getCommon().getValue());
//						f.put("comValue2", sub_item.getCommon().getValue2());
//						f.put("preperOrder", sub_item.getPreperOrder());
//						f.put("id", sub_item.getId());
//						
//						return f;
//					}).collect(Collectors.toList()) )
							.exports(item.getExports().stream().map(sub_item -> {
								Map<String, Object> f = new HashMap<>();
								f.put("comNm", sub_item.getCommon().getNm());
								f.put("comValue", sub_item.getCommon().getValue());
								f.put("comValue2", sub_item.getCommon().getValue2());
								f.put("preperOrder", sub_item.getPreperOrder());
								f.put("id", sub_item.getCommon().getId());

								return f;
							}).collect(Collectors.toList()))
//					.exports(item.getExports().stream().map(subitem -> subitem.getCommon())
//							.collect(Collectors.toMap(Common::getId, t -> t.getValue())))
//							.collect(Collectors.toMap(Common::getId, t -> t.getValue() ,Common::getId, t -> t.getValue() )))
							.build();

					return dto;
				}).collect(Collectors.toList());

		return list;
	}

	@Override
	public boolean removeCompanyInfo(CompanyInfoReq req) {
		req.getIds().stream().forEach(item -> _companyInfoRepository.delete(CompanyInfo.builder().id(item).build()));
		return true;
	}

	@Override
	public boolean createFinalInbound(FinalInboundReq req) {
		FinalInbound target = FinalInbound.builder().departDtStr(req.getDepartDtStr())
				.finalMasterBl(req.getFinalMasterBl()).containerNo(req.getContainerNo())
				.containerPrice(req.getContainerPrice()).deliveryNm(req.getDeliveryNm()).hangName(req.getHangName())
				.silNo(req.getSilNo()).cargoName(req.getCargoName()).departPort(req.getDepartPort())
				.hangCha(req.getHangCha()).containerSizeStr(req.getContainerSizeStr())
				.weatherCondition(req.getWeatherCondition())

				// todo 사용자 세션 아이디로 수정해야됨.
				.user(User.builder().id(new Long(1)).build())

				.isUsing(true).createDt(new Date()).updateDt(new Date())

				.build();
		target = _finalInboundRepository.save(target);
//		final Long companyId = target.getId();
//		AtomicInteger index_m = new AtomicInteger(1);
//		AtomicInteger index_e = new AtomicInteger(1);
//		
//
//		req.getExports().stream().forEach(item->_companyInfoExportRepository.save(CompanyInfoExport.builder()
//				.preperOrder(index_e.getAndIncrement())
//				.common(Common.builder().id(item).build())
//				.companInfoy(CompanyInfo.builder().id(companyId).build())
//				.build()));

		return true;
	}
	
	@Override
	public boolean updateFinalInbound(FinalInboundReq req) {
		
		FinalInbound finalInbound = _finalInboundRepository.findById(req.getId())
				.orElse(FinalInbound.builder().build());
		
		finalInbound.setId(req.getId());
		finalInbound.setDepartDtStr(req.getDepartDtStr());
		finalInbound.setFinalMasterBl(req.getFinalMasterBl());
		finalInbound.setContainerNo(req.getContainerNo());
		finalInbound.setContainerPrice(req.getContainerPrice());
		finalInbound.setDeliveryNm(req.getDeliveryNm());
		finalInbound.setHangName(req.getHangName());
		finalInbound.setSilNo(req.getSilNo());
		finalInbound.setCargoName(req.getCargoName());
		finalInbound.setDepartPort(req.getDepartPort());
		finalInbound.setHangCha(req.getHangCha());
		finalInbound.setContainerSizeStr(req.getContainerSizeStr());
		finalInbound.setWeatherCondition(req.getWeatherCondition());
		finalInbound.setIsUsing(true);
		finalInbound.setUpdateDt(new Date());
		finalInbound.setCreateDt(new Date());
		finalInbound.setUser(User.builder().id(new Long(1)).build());
		
		
		 _finalInboundRepository.save(finalInbound);
//		final Long companyId = target.getId();
//		AtomicInteger index_m = new AtomicInteger(1);
//		AtomicInteger index_e = new AtomicInteger(1);
//		
//
//		req.getExports().stream().forEach(item->_companyInfoExportRepository.save(CompanyInfoExport.builder()
//				.preperOrder(index_e.getAndIncrement())
//				.common(Common.builder().id(item).build())
//				.companInfoy(CompanyInfo.builder().id(companyId).build())
//				.build()));

		return true;
	}
	
	@Override
	public boolean addFinalInboundMasterItems(FinalInboundReq req) {
		
		int preperOrder = 1;
		FinalInbound finalInbound = _finalInboundRepository.findById(req.getId()).orElse(FinalInbound.builder().build());
		
		_finalInboundInboundMasterRepository.deleteFinalInboundInboundMaster(finalInbound.getId());
		
		List<InboundMaster> list = req.getInboundMasterData();
		
		for (int i = 0; i < list.size(); i++) {
			
			
			
			FinalInboundInboundMaster finalInboundInboundMaster= new FinalInboundInboundMaster();
			InboundMaster inboundMaster = _inboundMasterRepository.findById(list.get(i).getId()).orElse(InboundMaster.builder().build());
			
			finalInboundInboundMaster.setFinalInbound(finalInbound);
			finalInboundInboundMaster.setInboundMaster(inboundMaster);
			finalInboundInboundMaster.setPreperOrder(preperOrder);
			
			
			

			_finalInboundInboundMasterRepository.save(finalInboundInboundMaster);
			preperOrder=preperOrder+1;

		}
		
		
		return true;
	 
	}


}
