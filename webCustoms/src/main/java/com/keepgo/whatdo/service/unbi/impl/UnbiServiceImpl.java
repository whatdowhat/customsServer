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
		
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		List<Integer> index = new ArrayList<Integer>();
		
		FinalInbound finalInbound = _finalInboundRepository.findById(unbiReq.getFinalInboundId()).get();
//		List<UnbiRes> _unbiRepository.findByFinalInboundId
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
						.memo1(item.getMemo1() == null ? "" : item.getMemo1())
						.memo2(item.getMemo2() == null ? "" : item.getMemo2())
						.type(item.getType())
						
						.finalInboundId(item.getFinalInbound().getId())
						.build();
//						rt.setOfficeName((item.getOfficeName() == null ? "" : String.valueOf(item.getOfficeName())));
						rt.setOfficeName((item.getOfficeName() == null ? "" : decimalFormat.format(item.getOfficeName())));
						rt.setOfficeNameD((item.getOfficeName() == null ? new Double(0) : item.getOfficeName()));
						rt.setUnbi((item.getUnbi() == null ? "" : decimalFormat.format(item.getUnbi())));
						rt.setUnbiD((item.getUnbi() == null ? new Double(0) : item.getUnbi()));	
						rt.setPickupCost((item.getPickupCost() == null ? "" : decimalFormat.format(item.getPickupCost())));
						rt.setPickupCostD((item.getPickupCost() == null ? new Double(0) : item.getPickupCost()));
						rt.setSanghachaCost((item.getSanghachaCost() == null ? "" : decimalFormat.format(item.getSanghachaCost())));
						rt.setSanghachaCostD((item.getSanghachaCost() == null ? new Double(0) : item.getSanghachaCost()));
						rt.setEtcCost((item.getEtcCost() == null ? "" : decimalFormat.format(item.getEtcCost())));
						rt.setEtcCostD((item.getEtcCost() == null ? new Double(0) : item.getEtcCost()));
						rt.setHacksodanCost((item.getHacksodanCost() == null ? "" : decimalFormat.format(item.getHacksodanCost())));
						rt.setHacksodanCostD((item.getHacksodanCost() == null ? new Double(0) : item.getHacksodanCost()));
						rt.setCoCost((item.getCoCost() == null ? "" : decimalFormat.format(item.getCoCost())));
						rt.setCoCostD((item.getCoCost() == null ? new Double(0) : item.getCoCost()));
						rt.setHwajumiUnbi((item.getHwajumiUnbi() == null ? "" : decimalFormat.format(item.getHwajumiUnbi())));
						rt.setHwajumiUnbiD((item.getHwajumiUnbi() == null ? new Double(0) : item.getHwajumiUnbi()));
						rt.setHwajumiPickupCost((item.getHwajumiPickupCost() == null ? "" : decimalFormat.format(item.getHwajumiPickupCost())));
						rt.setHwajumiPickupCostD((item.getHwajumiPickupCost() == null ? new Double(0) : item.getHwajumiPickupCost()));
						rt.setContainerWorkCost((item.getContainerWorkCost() == null ? "" : decimalFormat.format(item.getContainerWorkCost())));
						rt.setContainerWorkCostD((item.getContainerWorkCost() == null ? new Double(0) : item.getContainerWorkCost()));
						rt.setContainerWorkCost2((item.getContainerWorkCost2() == null ? "" : decimalFormat.format(item.getContainerWorkCost2())));
						rt.setContainerWorkCost2D((item.getContainerWorkCost2() == null ? new Double(0) : item.getContainerWorkCost2()));
						rt.setContainerMoveCost((item.getContainerMoveCost() == null ? "" : decimalFormat.format(item.getContainerMoveCost())));
						rt.setContainerMoveCostD((item.getContainerMoveCost() == null ? new Double(0) : item.getContainerMoveCost()));
						rt.setTypebTitle((item.getTypebTitle() == null ? "" : item.getTypebTitle()));
						
						rt.setUnbiYn((item.getUnbiYn() == null ? "N" : item.getUnbiYn()));
						rt.setPickupCostYn((item.getPickupCostYn() == null ? "N" : item.getPickupCostYn()));
						rt.setSanghachaCostYn((item.getSanghachaCostYn() == null ? "N" : item.getSanghachaCostYn()));
						rt.setOfficeNameYn((item.getOfficeNameYn() == null ? "N" : item.getOfficeNameYn()));
						rt.setHacksodanCostYn((item.getHacksodanCostYn() == null ? "N" : item.getHacksodanCostYn()));
						rt.setCoCostYn((item.getCoCostYn() == null ? "N" : item.getCoCostYn()));					
						if(item.getColor() !=null ) {
							
							for(int i=0; i<ColorType.getList().size();i++) {
								if(ColorType.getList().get(i).getId() ==item.getColor() ) {
									rt.setColorCode(ColorType.getList().get(i).getCode());
									
								}
							}
//							rt.setCoCode(CoType.getList().stream().filter(t->t.getId() == item.getCoId()).findFirst().get().getName());)
						}
					
						
						rt.setTotalSum(rt.getOfficeNameD()+rt.getUnbiD()+rt.getPickupCostD()+rt.getSanghachaCostD()+rt.getEtcCostD()+rt.getHacksodanCostD()+rt.getCoCostD()+rt.getHwajumiUnbiD()+rt.getHwajumiPickupCostD()+rt.getContainerMoveCostD()+rt.getContainerWorkCostD()+rt.getContainerWorkCost2D());
					return rt;
						
				})
		.collect(Collectors.toList());
			
			if(result.size()>0) { //운비가 등록된 경우에만 total sum 셋팅.
				result.get(0).setOfficeNameSum(result.stream().filter(t->t.getOfficeNameD()!= null).mapToDouble(t->t.getOfficeNameD()).sum());
				result.get(0).setUnbiSum(result.stream().filter(t->t.getUnbiD()!= null).mapToDouble(t->t.getUnbiD()).sum());
				result.get(0).setPickupCostSum(result.stream().filter(t->t.getPickupCostD()!= null).mapToDouble(t->t.getPickupCostD()).sum());
				result.get(0).setSanghachaCostSum(result.stream().filter(t->t.getSanghachaCostD()!= null).mapToDouble(t->t.getSanghachaCostD()).sum());
				result.get(0).setEtcCostSum(result.stream().filter(t->t.getEtcCostD()!= null).mapToDouble(t->t.getEtcCostD()).sum());
				result.get(0).setHacksodanCostSum(result.stream().filter(t->t.getHacksodanCostD()!= null).mapToDouble(t->t.getHacksodanCostD()).sum());
				result.get(0).setCoCostSum(result.stream().filter(t->t.getCoCostD()!= null).mapToDouble(t->t.getCoCostD()).sum());
				result.get(0).setHwajumiUnbiSum(result.stream().filter(t->t.getHwajumiUnbiD()!= null).mapToDouble(t->t.getHwajumiUnbiD()).sum());
				result.get(0).setHwajumiPickupCostSum(result.stream().filter(t->t.getHwajumiPickupCostD()!= null).mapToDouble(t->t.getHwajumiPickupCostD()).sum());
				result.get(0).setContainerWorkCostSum(result.stream().filter(t->t.getContainerWorkCostD()!= null).mapToDouble(t->t.getContainerWorkCostD()).sum());
				result.get(0).setContainerWorkCost2Sum(result.stream().filter(t->t.getContainerWorkCost2D()!= null).mapToDouble(t->t.getContainerWorkCost2D()).sum());
				result.get(0).setContainerMoveCostSum(result.stream().filter(t->t.getContainerMoveCostD()!= null).mapToDouble(t->t.getContainerMoveCostD()).sum());
				result.get(0).setTotalSumFinal(result.stream().filter(t->t.getTotalSum()!= null).mapToDouble(t->t.getTotalSum()).sum());	
			}
		
			
		
		return result;
	}
	@Override
	public List<UnbiRes> getUnbiByMasterIdForPreView(InboundReq inboundReq)  {
		
		DecimalFormat decimalFormat = new DecimalFormat("#,###");
		List<Integer> index = new ArrayList<Integer>();
		
		FinalInbound finalInbound = _finalInboundRepository.findById(inboundReq.getFinalInboundId()).get();
//		List<UnbiRes> _unbiRepository.findByFinalInboundId
		List<UnbiRes> result = _unbiRepository.findByFinalInboundId(inboundReq.getFinalInboundId()).stream()
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
						.memo1(item.getMemo1() == null ? "" : item.getMemo1())
						.memo2(item.getMemo2() == null ? "" : item.getMemo2())
						.type(item.getType())
						
						.finalInboundId(item.getFinalInbound().getId())
						.build();
//						rt.setOfficeName((item.getOfficeName() == null ? "" : String.valueOf(item.getOfficeName())));
						rt.setOfficeName((item.getOfficeName() == null ? "" : decimalFormat.format(item.getOfficeName())));
						rt.setOfficeNameD((item.getOfficeName() == null ? new Double(0) : item.getOfficeName()));
						rt.setUnbi((item.getUnbi() == null ? "" : decimalFormat.format(item.getUnbi())));
						rt.setUnbiD((item.getUnbi() == null ? new Double(0) : item.getUnbi()));	
						rt.setPickupCost((item.getPickupCost() == null ? "" : decimalFormat.format(item.getPickupCost())));
						rt.setPickupCostD((item.getPickupCost() == null ? new Double(0) : item.getPickupCost()));
						rt.setSanghachaCost((item.getSanghachaCost() == null ? "" : decimalFormat.format(item.getSanghachaCost())));
						rt.setSanghachaCostD((item.getSanghachaCost() == null ? new Double(0) : item.getSanghachaCost()));
						rt.setEtcCost((item.getEtcCost() == null ? "" : decimalFormat.format(item.getEtcCost())));
						rt.setEtcCostD((item.getEtcCost() == null ? new Double(0) : item.getEtcCost()));
						rt.setHacksodanCost((item.getHacksodanCost() == null ? "" : decimalFormat.format(item.getHacksodanCost())));
						rt.setHacksodanCostD((item.getHacksodanCost() == null ? new Double(0) : item.getHacksodanCost()));
						rt.setCoCost((item.getCoCost() == null ? "" : decimalFormat.format(item.getCoCost())));
						rt.setCoCostD((item.getCoCost() == null ? new Double(0) : item.getCoCost()));
						rt.setHwajumiUnbi((item.getHwajumiUnbi() == null ? "" : decimalFormat.format(item.getHwajumiUnbi())));
						rt.setHwajumiUnbiD((item.getHwajumiUnbi() == null ? new Double(0) : item.getHwajumiUnbi()));
						rt.setHwajumiPickupCost((item.getHwajumiPickupCost() == null ? "" : decimalFormat.format(item.getHwajumiPickupCost())));
						rt.setHwajumiPickupCostD((item.getHwajumiPickupCost() == null ? new Double(0) : item.getHwajumiPickupCost()));
						rt.setContainerWorkCost((item.getContainerWorkCost() == null ? "" : decimalFormat.format(item.getContainerWorkCost())));
						rt.setContainerWorkCostD((item.getContainerWorkCost() == null ? new Double(0) : item.getContainerWorkCost()));
						rt.setContainerWorkCost2((item.getContainerWorkCost2() == null ? "" : decimalFormat.format(item.getContainerWorkCost2())));
						rt.setContainerWorkCost2D((item.getContainerWorkCost2() == null ? new Double(0) : item.getContainerWorkCost2()));
						rt.setContainerMoveCost((item.getContainerMoveCost() == null ? "" : decimalFormat.format(item.getContainerMoveCost())));
						rt.setContainerMoveCostD((item.getContainerMoveCost() == null ? new Double(0) : item.getContainerMoveCost()));
						rt.setTypebTitle((item.getTypebTitle() == null ? "" : item.getTypebTitle()));
						
						rt.setUnbiYn((item.getUnbiYn() == null ? "N" : item.getUnbiYn()));
						rt.setPickupCostYn((item.getPickupCostYn() == null ? "N" : item.getPickupCostYn()));
						rt.setSanghachaCostYn((item.getSanghachaCostYn() == null ? "N" : item.getSanghachaCostYn()));
						rt.setOfficeNameYn((item.getOfficeNameYn() == null ? "N" : item.getOfficeNameYn()));
						rt.setHacksodanCostYn((item.getHacksodanCostYn() == null ? "N" : item.getHacksodanCostYn()));
						rt.setCoCostYn((item.getCoCostYn() == null ? "N" : item.getCoCostYn()));					
						if(item.getColor() !=null ) {
							
							for(int i=0; i<ColorType.getList().size();i++) {
								if(ColorType.getList().get(i).getId() ==item.getColor() ) {
									rt.setColorId(Long.valueOf(item.getColor()));
									
									rt.setColorCode(ColorType.getList().get(i).getCode());
									
								}
							}
//							rt.setCoCode(CoType.getList().stream().filter(t->t.getId() == item.getCoId()).findFirst().get().getName());)
						}
					
						
						rt.setTotalSum(rt.getOfficeNameD()+rt.getUnbiD()+rt.getPickupCostD()+rt.getSanghachaCostD()+rt.getEtcCostD()+rt.getHacksodanCostD()+rt.getCoCostD()+rt.getHwajumiUnbiD()+rt.getHwajumiPickupCostD()+rt.getContainerMoveCostD()+rt.getContainerWorkCostD()+rt.getContainerWorkCost2D());
					return rt;
						
				})
		.collect(Collectors.toList());
			
			if(result.size()>0) { //운비가 등록된 경우에만 total sum 셋팅.
				result.get(0).setOfficeNameSum(result.stream().filter(t->t.getOfficeNameD()!= null).mapToDouble(t->t.getOfficeNameD()).sum());
				result.get(0).setUnbiSum(result.stream().filter(t->t.getUnbiD()!= null).mapToDouble(t->t.getUnbiD()).sum());
				result.get(0).setPickupCostSum(result.stream().filter(t->t.getPickupCostD()!= null).mapToDouble(t->t.getPickupCostD()).sum());
				result.get(0).setSanghachaCostSum(result.stream().filter(t->t.getSanghachaCostD()!= null).mapToDouble(t->t.getSanghachaCostD()).sum());
				result.get(0).setEtcCostSum(result.stream().filter(t->t.getEtcCostD()!= null).mapToDouble(t->t.getEtcCostD()).sum());
				result.get(0).setHacksodanCostSum(result.stream().filter(t->t.getHacksodanCostD()!= null).mapToDouble(t->t.getHacksodanCostD()).sum());
				result.get(0).setCoCostSum(result.stream().filter(t->t.getCoCostD()!= null).mapToDouble(t->t.getCoCostD()).sum());
				result.get(0).setHwajumiUnbiSum(result.stream().filter(t->t.getHwajumiUnbiD()!= null).mapToDouble(t->t.getHwajumiUnbiD()).sum());
				result.get(0).setHwajumiPickupCostSum(result.stream().filter(t->t.getHwajumiPickupCostD()!= null).mapToDouble(t->t.getHwajumiPickupCostD()).sum());
				result.get(0).setContainerWorkCostSum(result.stream().filter(t->t.getContainerWorkCostD()!= null).mapToDouble(t->t.getContainerWorkCostD()).sum());
				result.get(0).setContainerWorkCost2Sum(result.stream().filter(t->t.getContainerWorkCost2D()!= null).mapToDouble(t->t.getContainerWorkCost2D()).sum());
				result.get(0).setContainerMoveCostSum(result.stream().filter(t->t.getContainerMoveCostD()!= null).mapToDouble(t->t.getContainerMoveCostD()).sum());
				result.get(0).setTotalSumFinal(result.stream().filter(t->t.getTotalSum()!= null).mapToDouble(t->t.getTotalSum()).sum());
				
//				result.get(0).setOfficeNameSums( decimalFormat.format(result.get(0).getOfficeNameSum()));
//				result.get(0).setUnbiSums(decimalFormat.format(result.get(0).getUnbiSum()));
//				result.get(0).setPickupCostSums(decimalFormat.format(result.get(0).getPickupCostSum()));
//				result.get(0).setSanghachaCostSums(decimalFormat.format(result.get(0).getSanghachaCostSum()));
//				result.get(0).setEtcCostSums(decimalFormat.format(result.get(0).getEtcCostSum()));
//				result.get(0).setHacksodanCostSums(decimalFormat.format(result.get(0).getHacksodanCostSum()));
//				result.get(0).setCoCostSums(decimalFormat.format(result.get(0).getCoCostSum()));
//				result.get(0).setHwajumiUnbiSums(decimalFormat.format(result.get(0).getHwajumiUnbiSum()));
//				result.get(0).setHwajumiPickupCostSums(decimalFormat.format(result.get(0).getHwajumiPickupCostSum()));
//				result.get(0).setContainerWorkCostSums(decimalFormat.format(result.get(0).getContainerWorkCostSum()));
//				result.get(0).setContainerWorkCost2Sums(decimalFormat.format(result.get(0).getContainerWorkCost2Sum()));
//				result.get(0).setContainerMoveCostSums(decimalFormat.format(result.get(0).getContainerMoveCostSum()));
//				result.get(0).setTotalSumFinals(decimalFormat.format(result.get(0).getTotalSumFinal()));
				
			}
			Integer typeACount= 0;
			Integer typeBCount= 0;
			if(result.size()>0) {
				for (int i=0; i<result.size();i++) {
					if(result.get(i).getType().equals("A")) {
						typeACount = typeACount+1;
					}else if(result.get(i).getType().equals("B")) {
						typeBCount = typeBCount+1;
					}
					
				}
			}
			result.get(0).setTypeACount(typeACount);
			result.get(0).setTypeBCount(typeBCount);
		
		return result;
	}
	@Override
	public boolean commitUnbiData(UnbiReq unbiReq) {
		

		
		List<Unbi> unbiList = _unbiRepository.findByFinalInboundId(unbiReq.getFinalInboundId());
		for (int i = 0; i < unbiList.size(); i++) {
			_unbiRepository.delete(unbiList.get(i));
		}

	
		FinalInbound finalInbound = _finalInboundRepository.findById(unbiReq.getFinalInboundId()).get();
		
		finalInbound.setUnbiDefine1(unbiReq.getUnbiDefine1());
		finalInbound.setUnbiDefine2(unbiReq.getUnbiDefine2());
		finalInbound.setUnbiDefine3(unbiReq.getUnbiDefine3());
		finalInbound.setUnbiDefine4(unbiReq.getUnbiDefine4());
		
		List<UnbiReq> list = unbiReq.getUnbiReqData();
		for (int i = 0; i < list.size(); i++) {



			Unbi unbi = new Unbi();
			unbi.setOrderNo(list.get(i).getOrderNo());
			unbi.setNo(0);
			unbi.setTypebTitle(list.get(i).getTypebTitle());
			unbi.setWorkDateStr(list.get(i).getWorkDateStr());
			unbi.setNoStr(list.get(i).getNoStr());
			unbi.setCompanyNm(list.get(i).getCompanyNm());
			unbi.setMarking(list.get(i).getMarking());
			unbi.setKorNm(list.get(i).getKorNm());
			unbi.setDepartPort(list.get(i).getDepartPort());		
//			unbi.setUnbi(Double.valueOf(list.get(i).getUnbi()));
			if(list.get(i).getUnbi()==null||list.get(i).getUnbi().equals("")) {
				unbi.setUnbi(null);
			}else {
				unbi.setUnbi(Double.valueOf(list.get(i).getUnbi()));
			}
			if(list.get(i).getPickupCost()==null||list.get(i).getPickupCost().equals("")) {
				unbi.setPickupCost(null);
			}else {
				unbi.setPickupCost(Double.valueOf(list.get(i).getPickupCost()));
			}
			
			if(list.get(i).getSanghachaCost()==null||list.get(i).getSanghachaCost().equals("")) {
				unbi.setSanghachaCost(null);
			}else {
				unbi.setSanghachaCost(Double.valueOf(list.get(i).getSanghachaCost()));
			}
			
			if(list.get(i).getEtcCost()==null||list.get(i).getEtcCost().equals("")) {
				unbi.setEtcCost(null);
			}else {
				unbi.setEtcCost(Double.valueOf(list.get(i).getEtcCost()));
			}
			
			if(list.get(i).getHacksodanCost()==null||list.get(i).getHacksodanCost().equals("")) {
				unbi.setHacksodanCost(null);
			}else {
				unbi.setHacksodanCost(Double.valueOf(list.get(i).getHacksodanCost()));
			}
			
			if(list.get(i).getCoCost()==null||list.get(i).getCoCost().equals("")) {
				unbi.setCoCost(null);
			}else {
				unbi.setCoCost(Double.valueOf(list.get(i).getCoCost()));
			}
			
			if(list.get(i).getHwajumiUnbi()==null||list.get(i).getHwajumiUnbi().equals("")) {
				unbi.setHwajumiUnbi(null);
			}else {
				unbi.setHwajumiUnbi(Double.valueOf(list.get(i).getHwajumiUnbi()));
			}
			
			if(list.get(i).getHwajumiPickupCost()==null||list.get(i).getHwajumiPickupCost().equals("")) {
				unbi.setHwajumiPickupCost(null);
			}else {
				unbi.setHwajumiPickupCost(Double.valueOf(list.get(i).getHwajumiPickupCost()));
			}
			
			if(list.get(i).getContainerWorkCost()==null||list.get(i).getContainerWorkCost().equals("")) {
				unbi.setContainerWorkCost(null);
			}else {
				unbi.setContainerWorkCost(Double.valueOf(list.get(i).getContainerWorkCost()));
			}
			
			if(list.get(i).getContainerWorkCost2()==null||list.get(i).getContainerWorkCost2().equals("")) {
				unbi.setContainerWorkCost2(null);
			}else {
				unbi.setContainerWorkCost2(Double.valueOf(list.get(i).getContainerWorkCost2()));
			}
			
			if(list.get(i).getContainerMoveCost()==null||list.get(i).getContainerMoveCost().equals("")) {
				unbi.setContainerMoveCost(null);
			}else {
				unbi.setContainerMoveCost(Double.valueOf(list.get(i).getContainerMoveCost()));
			}
			if(list.get(i).getOfficeName()==null||list.get(i).getOfficeName().equals("")) {
				unbi.setOfficeName(null);
			}else {
				unbi.setOfficeName(Double.valueOf(list.get(i).getOfficeName()));
			}
			
			
			
			
			unbi.setMemo1( (list.get(i).getMemo1() == null) ? "" : list.get(i).getMemo1()  );
			unbi.setMemo2((list.get(i).getMemo2() == null) ? "" : list.get(i).getMemo2() );
			unbi.setType(list.get(i).getType());
			
			unbi.setFinalInbound(finalInbound);
			//색상코드
			if(list.get(i).getColor()!=null) {
				for(int j=0; j<ColorType.getList().size();j++) {
					if(ColorType.getList().get(j).getShowName().equals(list.get(i).getColor())) {
						unbi.setColor(ColorType.getList().get(j).getId());
					}else {

					}
				}
				
			}else {
				unbi.setColor(Integer.valueOf(0));
			}
			
			if(list.get(i).getUnbiYn()==null||list.get(i).getUnbiYn().equals("")) {
				unbi.setUnbiYn("N");
			}else {
				unbi.setUnbiYn(list.get(i).getUnbiYn());
			}
			if(list.get(i).getPickupCostYn()==null||list.get(i).getPickupCostYn().equals("")) {
				unbi.setPickupCostYn("N");
			}else {
				unbi.setPickupCostYn(list.get(i).getPickupCostYn());
			}
			if(list.get(i).getSanghachaCostYn()==null||list.get(i).getSanghachaCostYn().equals("")) {
				unbi.setSanghachaCostYn("N");
			}else {
				unbi.setSanghachaCostYn(list.get(i).getSanghachaCostYn());
			}
			if(list.get(i).getOfficeNameYn()==null||list.get(i).getOfficeNameYn().equals("")) {
				unbi.setOfficeNameYn("N");
			}else {
				unbi.setOfficeNameYn(list.get(i).getOfficeNameYn());
			}
			if(list.get(i).getHacksodanCostYn()==null||list.get(i).getHacksodanCostYn().equals("")) {
				unbi.setHacksodanCostYn("N");
			}else {
				unbi.setHacksodanCostYn(list.get(i).getHacksodanCostYn());
			}
			if(list.get(i).getCoCostYn()==null||list.get(i).getCoCostYn().equals("")) {
				unbi.setCoCostYn("N");
			}else {
				unbi.setCoCostYn(list.get(i).getCoCostYn());
			}
			
			
			_unbiRepository.save(unbi);
			
		}

		return true;
	}

}