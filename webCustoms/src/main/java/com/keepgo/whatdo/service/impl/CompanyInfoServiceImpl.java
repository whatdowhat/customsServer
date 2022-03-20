package com.keepgo.whatdo.service.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoReq;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.service.CompanyInfoService;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

	@Autowired
	CompanyInfoRepository _companyInfoRepository;

	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;

	@Autowired
	CompanyInfoManageRepository _companyInfoManageRepository;

	@Override
	public List<?> getAll(CompanyInfoReq req) {


		List<?> list = _companyInfoRepository.findAll().stream().map(item -> {

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
					.coInvoice(item.getCoInvoice()).updateDt(item.getUpdateDt())
					.manages(item.getManages().stream().map(sub_item->{
						Map<String,Object> f = new HashMap<>();
						f.put("comNm", sub_item.getCommon().getNm());
						f.put("comValue", sub_item.getCommon().getValue());
						f.put("preperOrder", sub_item.getPreperOrder());
						f.put("id", sub_item.getId());
						
						return f;
					}).collect(Collectors.toList()) )
					.exports(item.getExports().stream().map(sub_item->{
						Map<String,Object> f = new HashMap<>();
						f.put("comNm", sub_item.getCommon().getNm());
						f.put("comValue", sub_item.getCommon().getValue());
						f.put("preperOrder", sub_item.getPreperOrder());
						f.put("id", sub_item.getId());
						
						return f;
					}).collect(Collectors.toList()) )							
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
		req.getIds().stream().forEach(item->
		_companyInfoRepository.delete(CompanyInfo.builder().id(item).build())
		);
		return true;
	}


	@Override
	public boolean createCompanyInfo(CompanyInfoReq req) {
		CompanyInfo target = CompanyInfo.builder()
				.coNum(req.getCoNum())
				.coNm(req.getCoNm())
				.coAddress(req.getCoAddress())
				.coInvoice(req.getCoInvoice())
				.isUsing(true)
//				.manages(Arrays.asList(null))
				.createDt(new Date())
				.updateDt(new Date())
				
				.build();
		target = _companyInfoRepository.save(target);
		final Long companyId = target.getId();
		AtomicInteger index_m = new AtomicInteger(1);
		AtomicInteger index_e = new AtomicInteger(1);
		req.getManages().stream().forEach(item->_companyInfoManageRepository.save(CompanyInfoManage.builder()
				.preperOrder(index_m.getAndIncrement())
				.common(Common.builder().id(item).build())
				.companInfoy(CompanyInfo.builder().id(companyId).build())
				.build()));
		req.getExports().stream().forEach(item->_companyInfoExportRepository.save(CompanyInfoExport.builder()
				.preperOrder(index_e.getAndIncrement())
				.common(Common.builder().id(item).build())
				.companInfoy(CompanyInfo.builder().id(companyId).build())
				.build()));
		
		
//		CompanyInfoRes result = Stream.of(_companyInfoRepository.save(target)).map(item-> {
//			CompanyInfoRes dto = CompanyInfoRes.builder()
////					.id(item.getId())
//					.id(item.getId())
//					.coAddress(item.getCoAddress())
//					.coNm(item.getCoNm())
//					.coNum(item.getCoNum())
//					.coInvoice(item.getCoInvoice())
//					.updateDt(item.getUpdateDt())
//					.build();
////			if(true) {
//			if(Optional.ofNullable(item.getManages()).isPresent()) {
//				dto.setManages(item.getManages().stream().filter(t->Optional.of(t.getCommon()).isPresent()).map(subitem->subitem.getCommon()).collect(Collectors.toMap(Common::getId, t->t.getValue())));
//			}
//			if(Optional.ofNullable(item.getExports()).isPresent()) {
//				dto.setExports(item.getExports().stream().filter(t->Optional.of(t.getCommon()).isPresent()).map(subitem->subitem.getCommon()).collect(Collectors.toMap(Common::getId, t->t.getValue())));
//			}
//			
//				return dto;
//		}).findAny().orElse(null);
		return true;
	}
}
