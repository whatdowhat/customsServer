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
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
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
	public List<?> getAll(FinalInboundReq req) {

		List<?> list = _finalInboundRepository.findAll().stream()
				.sorted(Comparator.comparing(FinalInbound::getUpdateDt).reversed()).map(item -> {

					FinalInboundRes dto = FinalInboundRes.builder()


							.id(item.getId()).departDtStr(item.getDepartDtStr())
							
							
							.finalMasterBl(item.getFinalMasterBl())
							.containerNo(item.getContainerNo())
							.containerPrice(item.getContainerPrice())
							.deliveryNm(item.getDeliveryNm())
							.hangName(item.getHangName())
							.silNo(item.getSilNo())
							.cargoName(item.getCargoName())
							.departPort(item.getDepartPort())
							.hangCha(item.getHangCha())
							.containerSizeStr(item.getContainerSizeStr())
							.weatherCondition(item.getWeatherCondition())
							.createDt(item.getCreateDt()).updateDt(item.getUpdateDt())
							.build();
//					if(item.getInboundMasters()!=null ){
//						dto.setInboundMasters(item.getInboundMasters().stream().map(sub_item -> {
//							Map<String, Object> f = new HashMap<>();
//							f.put("workDate", sub_item.getInboundMaster().getWorkDate());
//							f.put("blNo", sub_item.getInboundMaster().getBlNo());
//							f.put("coNum", sub_item.getInboundMaster().getCompanyInfo().getCoNum());
//							f.put("companyNm", sub_item.getInboundMaster().getCompanyInfo().getCoNm());
//							f.put("id", sub_item.getInboundMaster().getId());
//
//							return f;
//						}).collect(Collectors.toList()));
//					}
					
					
					
//							.inboundMasters(item.getInboundMasters().stream().map(sub_item -> {
//								Map<String, Object> f = new HashMap<>();
//								f.put("workDate", sub_item.getInboundMaster().getWorkDate());
//								f.put("blNo", sub_item.getInboundMaster().getBlNo());
//								f.put("coNum", sub_item.getInboundMaster().getCompanyInfo().getCoNum());
//								f.put("companyNm", sub_item.getInboundMaster().getCompanyInfo().getCoNm());
//								f.put("id", sub_item.getInboundMaster().getId());
//
//								return f;
//							}).collect(Collectors.toList()))

							

					return dto;
				}).collect(Collectors.toList());

		return list;
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
	public FinalInboundRes addFinalInbound(FinalInboundReq req) {
		FinalInbound target = FinalInbound.builder().cargoName(req.getCargoName())

				// todo 사용자 세션 아이디로 수정해야됨.
				.user(User.builder().id(new Long(1)).build())

				.isUsing(true).createDt(new Date()).updateDt(new Date())

				.build();
		target = _finalInboundRepository.save(target);


		return FinalInboundRes.builder().id(_finalInboundRepository.findByCargoName(req.getCargoName()).getId()).cargoName(req.getCargoName()).build();
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
	public boolean deleteFinalInboundMasterItems(FinalInboundReq req) {
		
		
		
		
		_finalInboundInboundMasterRepository.deleteFinalInboundInboundMaster(req.getId(), req.getInboundMasterId());
		
		
		
		
		return true;
	 
	}
	@Override
	public boolean addFinalInboundMasterItems(FinalInboundReq req) {
		
		int preperOrder = 1;
		FinalInbound finalInbound = _finalInboundRepository.findById(req.getId()).orElse(FinalInbound.builder().build());
		
//		_finalInboundInboundMasterRepository.deleteFinalInboundInboundMaster(finalInbound.getId());
		
		List<InboundMasterReq> list = req.getInboundMasterData();
		
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
	
	@Override
	public boolean addDataFinalInboundMasterItems(FinalInboundReq req) {
		
		int preperOrder = 0;
		FinalInbound finalInbound = _finalInboundRepository.findById(req.getId()).orElse(FinalInbound.builder().build());
		InboundMaster inboundMaster = _inboundMasterRepository.findById(req.getInboundMasterId()).orElse(InboundMaster.builder().build());

			
			
			FinalInboundInboundMaster finalInboundInboundMaster= new FinalInboundInboundMaster();
			
			
			finalInboundInboundMaster.setFinalInbound(finalInbound);
			finalInboundInboundMaster.setInboundMaster(inboundMaster);
			finalInboundInboundMaster.setPreperOrder(preperOrder);
			
		_finalInboundInboundMasterRepository.save(finalInboundInboundMaster);
			

		
		
		
		return true;
	 
	}

	@Override
	public FinalInboundRes getOne(Long id) {
		
		FinalInbound r = _finalInboundRepository.findById(id).get();
		
		FinalInboundRes dto = 
				
				FinalInboundRes.builder()


				.id(r.getId()).departDtStr(r.getDepartDtStr())
				
				
				.finalMasterBl(r.getFinalMasterBl())
				.containerNo(r.getContainerNo())
				.containerPrice(r.getContainerPrice())
				.deliveryNm(r.getDeliveryNm())
				.hangName(r.getHangName())
				.silNo(r.getSilNo())
				.cargoName(r.getCargoName())
				.departPort(r.getDepartPort())
				.hangCha(r.getHangCha())
				.containerSizeStr(r.getContainerSizeStr())
				.weatherCondition(r.getWeatherCondition())
				.createDt(r.getCreateDt())
				.updateDt(r.getUpdateDt())
				.build();
//		if(r.getInboundMasters()!=null ){
//			dto.setInboundMasters(r.getInboundMasters().stream().map(sub_item -> {
//				Map<String, Object> f = new HashMap<>();
//				f.put("workDate", sub_item.getInboundMaster().getWorkDate());
//				f.put("blNo", sub_item.getInboundMaster().getBlNo());
//				f.put("coNum", sub_item.getInboundMaster().getCompanyInfo().getCoNum());
//				f.put("companyNm", sub_item.getInboundMaster().getCompanyInfo().getCoNm());
//				f.put("id", sub_item.getInboundMaster().getId());
//
//				return f;
//			}).collect(Collectors.toList()));
//		}
		
//			.inboundMasters(r.getInboundMasters().stream().map(sub_item -> {
//					Map<String, Object> f = new HashMap<>();
//					f.put("workDate", sub_item.getInboundMaster().getWorkDate());
//					f.put("blNo", sub_item.getInboundMaster().getBlNo());
//					f.put("coNum", sub_item.getInboundMaster().getCompanyInfo().getCoNum());
//					f.put("companyNm", sub_item.getInboundMaster().getCompanyInfo().getCoNm());
//					f.put("id", sub_item.getInboundMaster().getId());
//
//					return f;
//				}).collect(Collectors.toList()))
				
				

		return dto;
	}


}
