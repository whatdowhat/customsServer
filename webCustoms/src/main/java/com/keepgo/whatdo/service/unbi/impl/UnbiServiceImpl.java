package com.keepgo.whatdo.service.unbi.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.define.ColorType;
import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.define.FileTypeRes;
import com.keepgo.whatdo.define.FreightType;
import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.Unbi;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UnbiReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.repository.UnbiRepository;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;
import com.keepgo.whatdo.service.unbi.UnbiService;

@Component
public class UnbiServiceImpl implements UnbiService {

	@Autowired
	InboundRepository _inboundRepository;

	@Autowired
	InboundMasterRepository _inboundMasterRepository;

	@Autowired
	FileUploadRepository _fileUploadRepository;

	@Autowired
	UtilService _UtilService;

	@Autowired
	CommonRepository _commonRepository;

	@Autowired
	CompanyInfoRepository _companyInfoRepository;
	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;
	@Autowired
	UnbiRepository _unbiRepository;
	@Autowired
	FinalInboundRepository _finalInboundRepository;
	
	@Override
	public List<UnbiRes> getUnbiByMasterId(UnbiReq unbiReq)  {
		
		
		List<Integer> index = new ArrayList<Integer>();
		
		FinalInbound finalInbound = _finalInboundRepository.findById(unbiReq.getFinalInboundId()).get();		
		List<UnbiRes> result = _unbiRepository.findByFinalInboundId(unbiReq.getFinalInboundId()).stream()
				.sorted(Comparator.comparing(Unbi::getOrderNo))
				.map(item->{
					
					
					UnbiRes rt = UnbiRes.builder()
				
						.id(item.getId())
 						.orderNo(item.getOrderNo())
						.no(0)
						.noStr(item.getNoStr())
						.workDateStr(item.getWorkDateStr())
						.companyNm(item.getCompanyNm())
						.marking(item.getMarking())
						.korNm(item.getKorNm())
						.departPort(item.getDepartPort())
						.unbi(item.getUnbi())
						.pickupCost(item.getPickupCost())						
						.sanghachaCost(item.getSanghachaCost())
						.officeName(item.getOfficeName())
						.etcCost(item.getEtcCost())
						.hacksodanCost(item.getHacksodanCost())
						.coCost(item.getCoCost())
						.hwajumiUnbi(item.getHwajumiUnbi())
						.hwajumiPickupCost(item.getHwajumiPickupCost())
						.containerWorkCost(item.getContainerWorkCost())
						.containerWorkCost2(item.getContainerWorkCost2())
						.containerMoveCost(item.getContainerMoveCost())
						.memo1(item.getMemo1())
						.memo2(item.getMemo2())
						.type(item.getType())
						.finalInboundId(item.getFinalInbound().getId())
//						.totalSum(rt.getCoCost()+rt.getPickupCost()+rt.getSanghachaCost()+rt.getEtcCost()+rt.getHacksodanCost()+rt.getCoCost()+rt.getHwajumiUnbi()+rt.getHwajumiPickupCost()+rt.getContainerMoveCost()+rt.getContainerWorkCost()+rt.getContainerWorkCost2())
						.build();
						rt.setTotalSum(rt.getCoCost()+rt.getPickupCost()+rt.getSanghachaCost()+rt.getEtcCost()+rt.getHacksodanCost()+rt.getCoCost()+rt.getHwajumiUnbi()+rt.getHwajumiPickupCost()+rt.getContainerMoveCost()+rt.getContainerWorkCost()+rt.getContainerWorkCost2());
					return rt;
						
				})
		.collect(Collectors.toList());
			
			
		
		
			result.get(0).setUnbiSum(result.stream().filter(t->t.getUnbi()!= null).mapToDouble(t->t.getUnbi()).sum());
			result.get(0).setPickupCostSum(result.stream().filter(t->t.getPickupCost()!= null).mapToDouble(t->t.getPickupCost()).sum());
			result.get(0).setSanghachaCostSum(result.stream().filter(t->t.getSanghachaCost()!= null).mapToDouble(t->t.getSanghachaCost()).sum());
			result.get(0).setEtcCostSum(result.stream().filter(t->t.getSanghachaCost()!= null).mapToDouble(t->t.getSanghachaCost()).sum());
			result.get(0).setHacksodanCostSum(result.stream().filter(t->t.getHacksodanCost()!= null).mapToDouble(t->t.getHacksodanCost()).sum());
			result.get(0).setCoCostSum(result.stream().filter(t->t.getCoCost()!= null).mapToDouble(t->t.getCoCost()).sum());
			result.get(0).setHwajumiUnbiSum(result.stream().filter(t->t.getHwajumiUnbi()!= null).mapToDouble(t->t.getHwajumiUnbi()).sum());
			result.get(0).setHwajumiPickupCostSum(result.stream().filter(t->t.getHwajumiPickupCost()!= null).mapToDouble(t->t.getHwajumiPickupCost()).sum());
			result.get(0).setContainerWorkCostSum(result.stream().filter(t->t.getContainerWorkCost()!= null).mapToDouble(t->t.getContainerWorkCost()).sum());
			result.get(0).setContainerWorkCost2Sum(result.stream().filter(t->t.getContainerWorkCost2()!= null).mapToDouble(t->t.getContainerWorkCost2()).sum());
			result.get(0).setContainerMoveCostSum(result.stream().filter(t->t.getContainerMoveCost()!= null).mapToDouble(t->t.getContainerMoveCost()).sum());
			result.get(0).setTotalSumFinal(result.stream().filter(t->t.getTotalSum()!= null).mapToDouble(t->t.getTotalSum()).sum());
		
		return result;
	}
	
	@Override
	public boolean commitUnbiData(UnbiReq unbiReq) {
		

		
		List<Unbi> unbiList = _unbiRepository.findByFinalInboundId(unbiReq.getFinalInboundId());
		for (int i = 0; i < unbiList.size(); i++) {
			_unbiRepository.delete(unbiList.get(i));
		}

	
		FinalInbound finalInbound = _finalInboundRepository.findById(unbiReq.getFinalInboundId()).get();
		
		List<UnbiReq> list = unbiReq.getUnbiReqData();
		for (int i = 0; i < list.size(); i++) {



			Unbi unbi = new Unbi();
			unbi.setOrderNo(list.get(i).getOrderNo());
			unbi.setNo(0);
			unbi.setWorkDateStr(list.get(i).getWorkDateStr());
			unbi.setNoStr(list.get(i).getWorkDateStr());
			unbi.setCompanyNm(list.get(i).getCompanyNm());
			unbi.setMarking(list.get(i).getMarking());
			unbi.setKorNm(list.get(i).getKorNm());
			unbi.setDepartPort(list.get(i).getDepartPort());		
			if(list.get(i).getUnbi()==null||list.get(i).getUnbi().equals("")) {
				unbi.setUnbi(new Double(0));
			}else {
				unbi.setUnbi(Double.valueOf(list.get(i).getUnbi()));
			}
			if(list.get(i).getPickupCost()==null||list.get(i).getPickupCost().equals("")) {
				unbi.setPickupCost(new Double(0));
			}else {
				unbi.setPickupCost(Double.valueOf(list.get(i).getPickupCost()));
			}
			
			if(list.get(i).getSanghachaCost()==null||list.get(i).getSanghachaCost().equals("")) {
				unbi.setSanghachaCost(new Double(0));
			}else {
				unbi.setSanghachaCost(Double.valueOf(list.get(i).getSanghachaCost()));
			}
			
			if(list.get(i).getEtcCost()==null||list.get(i).getEtcCost().equals("")) {
				unbi.setEtcCost(new Double(0));
			}else {
				unbi.setEtcCost(Double.valueOf(list.get(i).getEtcCost()));
			}
			
			if(list.get(i).getHacksodanCost()==null||list.get(i).getHacksodanCost().equals("")) {
				unbi.setHacksodanCost(new Double(0));
			}else {
				unbi.setHacksodanCost(Double.valueOf(list.get(i).getHacksodanCost()));
			}
			
			if(list.get(i).getCoCost()==null||list.get(i).getCoCost().equals("")) {
				unbi.setCoCost(new Double(0));
			}else {
				unbi.setCoCost(Double.valueOf(list.get(i).getCoCost()));
			}
			
			if(list.get(i).getHwajumiUnbi()==null||list.get(i).getHwajumiUnbi().equals("")) {
				unbi.setHwajumiUnbi(new Double(0));
			}else {
				unbi.setHwajumiUnbi(Double.valueOf(list.get(i).getHwajumiUnbi()));
			}
			
			if(list.get(i).getHwajumiPickupCost()==null||list.get(i).getHwajumiPickupCost().equals("")) {
				unbi.setHwajumiPickupCost(new Double(0));
			}else {
				unbi.setHwajumiPickupCost(Double.valueOf(list.get(i).getHwajumiPickupCost()));
			}
			
			if(list.get(i).getContainerWorkCost()==null||list.get(i).getContainerWorkCost().equals("")) {
				unbi.setContainerWorkCost(new Double(0));
			}else {
				unbi.setContainerWorkCost(Double.valueOf(list.get(i).getContainerWorkCost()));
			}
			
			if(list.get(i).getContainerWorkCost2()==null||list.get(i).getContainerWorkCost2().equals("")) {
				unbi.setContainerWorkCost2(new Double(0));
			}else {
				unbi.setContainerWorkCost2(Double.valueOf(list.get(i).getContainerWorkCost2()));
			}
			
			if(list.get(i).getContainerMoveCost()==null||list.get(i).getContainerMoveCost().equals("")) {
				unbi.setContainerMoveCost(new Double(0));
			}else {
				unbi.setContainerMoveCost(Double.valueOf(list.get(i).getContainerMoveCost()));
			}
			
			
			
			unbi.setOfficeName(list.get(i).getOfficeName());
			unbi.setMemo1(list.get(i).getMemo1());
			unbi.setMemo2(list.get(i).getMemo2());
			unbi.setType(list.get(i).getType());
			unbi.setFinalInbound(finalInbound);
			
			
			_unbiRepository.save(unbi);
			
		}

		return true;
	}

}