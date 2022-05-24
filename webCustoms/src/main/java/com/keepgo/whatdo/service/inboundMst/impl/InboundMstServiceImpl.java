package com.keepgo.whatdo.service.inboundMst.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.define.FreightType;
import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundInboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.inboundMst.InboundMstService;
import com.keepgo.whatdo.service.util.UtilService;

@Component
public class InboundMstServiceImpl implements InboundMstService {

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
	FinalInboundInboundMasterRepository _finalInboundInboundMasterRepository;
	

	@Override
	public List<?> getInboundMaster() {

//		FileUpload t;
//		t.getInboundMaster().getId()
//		
		List<?> list = _inboundMasterRepository.findAll().stream()

				.sorted(Comparator.comparing(InboundMaster::getUpdateDt).reversed())

				.map(item -> {

					InboundMasterRes dto = new InboundMasterRes();
					dto.setId(item.getId());
					dto.setBlNo(item.getBlNo());
					dto.setWorkDate(item.getWorkDate());

					if (item.getCompanyInfo() != null) {
						dto.setCompanyInfoId(item.getCompanyInfo().getId());
						dto.setCompanyNm(item.getCompanyInfo().getCoNm());
						dto.setCoNmEn(item.getCompanyInfo().getCoNmEn());
						dto.setConsignee(item.getCompanyInfo().getConsignee());
						dto.setConInvoice(item.getCompanyInfo().getCoInvoice());
						dto.setCoNum(item.getCompanyInfo().getCoNum());
					}
					
					if (item.getComExport()!= null) {
						dto.setExportName(item.getComExport().getValue());
						dto.setExportAddress(item.getComExport().getValue2());
						dto.setExportId(item.getComExport().getId());
					}
					if (item.getFileUploads().size() > 0) {
						dto.setATypeCount(
								item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.A.getId()).count());
						
						dto.setBTypeCount(
								item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.B.getId()).count());
						
						dto.setCTypeCount(
								item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.C.getId()).count());
						
						dto.setDTypeCount(
								item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.D.getId()).count());
						
						dto.setETypeCount(
								item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.E.getId()).count());
					}else {
						dto.setATypeCount(new Long(0));
						dto.setBTypeCount(new Long(0));
						dto.setCTypeCount(new Long(0));
						dto.setDTypeCount(new Long(0));
						dto.setETypeCount(new Long(0));
						
					}

					dto.setATypeNm(FileType.A.getName());
					dto.setBTypeNm(FileType.B.getName());
					dto.setCTypeNm(FileType.C.getName());
					dto.setDTypeNm(FileType.D.getName());
					dto.setETypeNm(FileType.E.getName());

					dto.setTotalItemCount(item.getInbounds().stream()
							.mapToDouble(t -> (t.getItemCount() == null) ? 0 : t.getItemCount()).sum());
					dto.setTotalBoxCount(item.getInbounds().stream()
							.mapToDouble(t -> (t.getBoxCount() == null) ? 0 : t.getBoxCount()).sum());
					dto.setTotalWeight(item.getInbounds().stream()
							.mapToDouble(t -> (t.getWeight() == null) ? 0 : t.getWeight()).sum());
					dto.setTotalCbm(
							item.getInbounds().stream().mapToDouble(t -> (t.getCbm() == null) ? 0 : t.getCbm()).sum());
					dto.setFinalTotalPrice(item.getInbounds().stream()
							.mapToDouble(t -> (t.getTotalPrice() == null) ? 0 : t.getTotalPrice()).sum());

//					Double ff = item.getInbounds().stream().mapToDouble(t->t.getBoxCount()).sum();
//					InboundMasterRes dto = InboundMasterRes.builder()
//					
//					
//					.id(item.getId()).masterBlNo(item.getMasterBlNo())
//					.aTypeCount(ll.stream().filter(t->t.getId().equals(new Long(296))).count())
//					.bTypeCount(ll.stream().filter(t->t.getId().equals(new Long(297))).count())
//					.cTypeCount(ll.stream().filter(t->t.getId().equals(new Long(298))).count())
//					.dTypeCount(ll.stream().filter(t->t.getId().equals(new Long(299))).count())
//					.eTypeCount(ll.stream().filter(t->t.getId().equals(new Long(300))).count())
//					.aTypeNm(_commonRepository.findById(new Long(296)).get().getValue())
//					.bTypeNm(_commonRepository.findById(new Long(297)).get().getValue())
//					.cTypeNm(_commonRepository.findById(new Long(298)).get().getValue())
//					.dTypeNm(_commonRepository.findById(new Long(299)).get().getValue())
//					.eTypeNm(_commonRepository.findById(new Long(300)).get().getValue())
//					
//					
//					
//					.totalItemCount(item.getInbounds().stream().mapToDouble(t-> (t.getItemCount() == null) ? 0 : t.getItemCount()).sum())
//					.totalBoxCount(item.getInbounds().stream().mapToDouble(t-> (t.getBoxCount() == null) ? 0 : t.getBoxCount()).sum())
//					.totalWeight(item.getInbounds().stream().mapToDouble(t-> (t.getWeight() == null) ? 0 : t.getWeight()).sum())
//					.totalCbm(item.getInbounds().stream().mapToDouble(t-> (t.getCbm() == null) ? 0 : t.getCbm()).sum())
//					.finalTotalPrice(item.getInbounds().stream().mapToDouble(t->(t.getTotalPrice() == null) ? 0 : t.getTotalPrice()).sum())
//					.export(item.getExport()).companyNm(item.getCompanyNm())	
//					.incomDt(item.getIncomDt()).workDate(item.getWorkDate()).updateDt(item.getUpdateDt()).createDt(item.getCreateDt())
//					.companyInfoId(item.getCompanyInfo().getId()).commonId(item.getWorkType().getId())
//					.cargo(item.getCargo()).toHarbor(item.getToHarbor())
//					.fromHarbor(item.getFromHarbor()).containerNo(item.getContainerNo())
//					.realNo(item.getRealNo()).hangmyung(item.getHangmyung()).hangcha(item.getHangcha())
//					.workTypeId(item.getWorkType().getNm()).chinaSanggumYn(item.isChinaSanggumYn())
//					
//					.userId(item.getUser().getId())					
//					.build();

					return dto;
				}).collect(Collectors.toList());

		return list;

	}

	@Override
	public InboundMasterRes getOne(Long id) {
		
		InboundMaster r = _inboundMasterRepository.findById(id).get();
		
		InboundMasterRes dto = 
				
				InboundMasterRes.builder()
				.id(r.getId())
				.blNo(r.getBlNo())
				.freight(FreightType.getList().stream().filter(type->type.getId() == r.getFreight()).findFirst().get().getName())
				.freightCode(r.getFreight())
				.createDt(r.getCreateDt())
				.updateDt(r.getUpdateDt())
				.build();

				

		return dto;
	}
	@Override
	public InboundMasterRes getInboundMaster(Long inboundMstId) {

		InboundMaster item = _inboundMasterRepository.findById(inboundMstId).get();
		InboundMasterRes dto = new InboundMasterRes();

		dto.setId(item.getId());
		dto.setBlNo(item.getBlNo());
		dto.setWorkDate(item.getWorkDate());
		dto.setManager(item.getManager());
		dto.setCurrencyType(item.getCurrencyType());
		dto.setPackingType(item.getPackingType());
		if(item.getFreight() != 0) {
			dto.setFreight(FreightType.getList().stream().filter(type->type.getId() == item.getFreight()).findFirst().get().getName());
			dto.setFreightCode(item.getFreight());
		}
		
		if (item.getCompanyInfo() != null) {
			dto.setCompanyInfoId(item.getCompanyInfo().getId());
			dto.setCompanyNm(item.getCompanyInfo().getCoNm());
			dto.setCoNmEn(item.getCompanyInfo().getCoNmEn());
			dto.setConsignee(item.getCompanyInfo().getConsignee());
			dto.setConInvoice(item.getCompanyInfo().getCoInvoice());
			dto.setCoNum(item.getCompanyInfo().getCoNum());
			
		}
		if (item.getComExport()!= null) {
			dto.setExportName(item.getComExport().getValue());
			dto.setExportAddress(item.getComExport().getValue2());
			dto.setExportId(item.getComExport().getId());;
		}

		if (item.getFileUploads().size() > 0) {
			dto.setATypeCount(
					item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.A.getId()).count());
			
			dto.setBTypeCount(
					item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.B.getId()).count());
			
			dto.setCTypeCount(
					item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.C.getId()).count());
			
			dto.setDTypeCount(
					item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.D.getId()).count());
			
			dto.setETypeCount(
					item.getFileUploads().stream().filter(tt -> tt.getFileType() == FileType.E.getId()).count());
		}else {
			dto.setATypeCount(new Long(0));
			dto.setBTypeCount(new Long(0));
			dto.setCTypeCount(new Long(0));
			dto.setDTypeCount(new Long(0));
			dto.setETypeCount(new Long(0));
			
		}

		dto.setATypeNm(FileType.A.getName());
		dto.setBTypeNm(FileType.B.getName());
		dto.setCTypeNm(FileType.C.getName());
		dto.setDTypeNm(FileType.D.getName());
		dto.setETypeNm(FileType.E.getName());
		
		dto.setTotalItemCount(
				item.getInbounds().stream().mapToDouble(t -> (t.getItemCount() == null) ? 0 : t.getItemCount()).sum());
		dto.setTotalBoxCount(
				item.getInbounds().stream().mapToDouble(t -> (t.getBoxCount() == null) ? 0 : t.getBoxCount()).sum());
		dto.setTotalWeight(
				item.getInbounds().stream().mapToDouble(t -> (t.getWeight() == null) ? 0 : t.getWeight()).sum());
		dto.setTotalCbm(item.getInbounds().stream().mapToDouble(t -> (t.getCbm() == null) ? 0 : t.getCbm()).sum());
		dto.setFinalTotalPrice(item.getInbounds().stream()
				.mapToDouble(t -> (t.getTotalPrice() == null) ? 0 : t.getTotalPrice()).sum());

		return dto;
	}
	

	@Override
	public InboundMasterRes addInboundMaster(InboundMasterReq inboundMasterReq) {
		InboundMaster inboundMaster = new InboundMaster();

		SimpleDateFormat foramt1 = new SimpleDateFormat("yyyy-MM-dd");

		try {
			inboundMaster.setWorkDate((inboundMasterReq.getWorkDate() == null) ? new Date()
					: foramt1.parse(inboundMasterReq.getWorkDate()));
		} catch (ParseException e) {
			inboundMaster.setWorkDate(new Date());
		}

		//todo 사용자 세션 아이디로 수정해야됨.
		inboundMaster.setUser(User.builder().id(new Long(1)).build());
		inboundMaster.setBlNo(inboundMasterReq.getBlNo());
		inboundMaster.setCreateDt(new Date());
		inboundMaster.setUpdateDt(new Date());
		inboundMaster.setIsUsing(true);
		inboundMaster.setFreight(inboundMasterReq.getFreight());
		inboundMaster.setCurrencyType(inboundMasterReq.getCurrencyType());
		inboundMaster.setPackingType(inboundMasterReq.getPackingType());
				if (inboundMasterReq.getCompanyInfoId() != null) {
			CompanyInfo companyInfo = _companyInfoRepository.findById(inboundMasterReq.getCompanyInfoId())
					.orElse(CompanyInfo.builder().build());
			inboundMaster.setCompanyInfo(companyInfo);
		}

		if (inboundMasterReq.getExportId() != null) {
			CompanyInfoExport companyInfoExport = _companyInfoExportRepository
					.findById(inboundMasterReq.getExportId()).orElse(CompanyInfoExport.builder().build());

			Common common = companyInfoExport.getCommon();
		}

		InboundMaster r = _inboundMasterRepository.save(inboundMaster);

		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		inboundMasterRes.setId(r.getId());
		inboundMasterRes.setWorkDate(r.getWorkDate());
		inboundMasterRes.setBlNo(r.getBlNo());
		return inboundMasterRes;
	}
	
	@Override
	public InboundMasterRes createInboundMaster(InboundMasterReq req) {
		InboundMaster target = InboundMaster.builder()
				.blNo(req.getBlNo())
				.freight(req.getFreight())

				// todo 사용자 세션 아이디로 수정해야됨.
				.user(User.builder().id(new Long(1)).build())
		
				.isUsing(true).createDt(new Date()).updateDt(new Date())

				.build();
		target = _inboundMasterRepository.save(target);


		return InboundMasterRes.builder().id(_inboundMasterRepository.findByBlNo(req.getBlNo()).getId()).build();
	}

	@Override
	public InboundMasterRes updateInboundMaster(InboundMasterReq inboundMasterReq) throws ParseException {
		InboundMaster inboundMaster = new InboundMaster();
		
//		String workDt=inboundMasterReq.getWorkDate();
//		SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyyMMdd");
//		Date tempDate = null;
//		tempDate = beforeFormat.parse(workDt);
//		SimpleDateFormat foramt1 = new SimpleDateFormat("yyyy-MM-dd");
//		
//		
		InboundMaster target =  _inboundMasterRepository.findById(inboundMasterReq.getId()).get();
//		inboundMaster.setWorkDate((inboundMasterReq.getWorkDate()== null) ? new Date()
//				: tempDate);
		
		target.setBlNo(inboundMasterReq.getBlNo());
		target.setUpdateDt(new Date());
//		target.setWorkDate(inboundMaster.getWorkDate());
		target.setIsUsing(true);
		target.setFreight(inboundMasterReq.getFreight());
		target.setManager(inboundMasterReq.getManager());
		target.setCurrencyType(inboundMasterReq.getCurrencyType());
		target.setPackingType(inboundMasterReq.getPackingType());
		//todo 사용자 세션 아이디로 수정해야됨.
		inboundMaster.setUser(User.builder().id(new Long(1)).build());
		if(inboundMasterReq.getCompanyInfoId() != null) {
			target.setCompanyInfo(_companyInfoRepository.findById(inboundMasterReq.getCompanyInfoId()).get());	
		}
		if(inboundMasterReq.getExportId() != null) {
			target.setComExport(_commonRepository.findById(inboundMasterReq.getExportId()).get());	
		}		
		
		
		

		_inboundMasterRepository.save(target);

		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		return inboundMasterRes;
	}
	
	@Override
	public FinalInboundInboundMasterRes getMappingInfo(FinalInboundInboundMasterReq req) {
		
		 FinalInboundInboundMaster r = _finalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getFinalInboundId(), req.getInboundMasterId());
		
		 FinalInboundInboundMasterRes dto = 
				
				 FinalInboundInboundMasterRes.builder()
				.id(r.getId())
				
				.build();

				

		return dto;
	}

}