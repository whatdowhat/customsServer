package com.keepgo.whatdo.service.inbound.impl;
import java.io.IOException;
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

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;

@Component
public class InboundServiceImpl implements InboundService {

	
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
	@Override
	public List<?> getInboundMaster(){


//		FileUpload t;
//		t.getInboundMaster().getId()
//		
		List<?> list = _inboundMasterRepository.findAll().stream()
				

				
				.sorted(Comparator.comparing(InboundMaster::getUpdateDt).reversed())
				
				.map(item -> {
					

					List<CommonRes> ll= item.getFileUploads().stream()
					.filter(t-> t.getInboundMaster().getId() == item.getId())
					.map(t->t.getFileuploadType())
					.map(t->CommonRes.builder()
							.id(t.getId())
							.value(t.getValue())
							.value2(t.getValue2())
							.build()).collect(Collectors.toList());
							
//					CommonRes res;
//					
//					ll.stream().filter(t->t.getId() == new Long(296)).count();
//					List<InboundRes> li = ;
					
					
					Double ff = item.getInbounds().stream().mapToDouble(t->t.getBoxCount()).sum();
					InboundMasterRes dto = InboundMasterRes.builder()
					
					
					.id(item.getId()).masterBlNo(item.getMasterBlNo())
					.aTypeCount(ll.stream().filter(t->t.getId().equals(new Long(296))).count())
					.bTypeCount(ll.stream().filter(t->t.getId().equals(new Long(297))).count())
					.cTypeCount(ll.stream().filter(t->t.getId().equals(new Long(298))).count())
					.dTypeCount(ll.stream().filter(t->t.getId().equals(new Long(299))).count())
					.eTypeCount(ll.stream().filter(t->t.getId().equals(new Long(300))).count())
					.aTypeNm(_commonRepository.findById(new Long(296)).get().getValue())
					.bTypeNm(_commonRepository.findById(new Long(297)).get().getValue())
					.cTypeNm(_commonRepository.findById(new Long(298)).get().getValue())
					.dTypeNm(_commonRepository.findById(new Long(299)).get().getValue())
					.eTypeNm(_commonRepository.findById(new Long(300)).get().getValue())
					
					
					
					.totalItemCount(item.getInbounds().stream().mapToDouble(t-> (t.getItemCount() == null) ? 0 : t.getItemCount()).sum())
					.totalBoxCount(item.getInbounds().stream().mapToDouble(t-> (t.getBoxCount() == null) ? 0 : t.getBoxCount()).sum())
					.totalWeight(item.getInbounds().stream().mapToDouble(t-> (t.getWeight() == null) ? 0 : t.getWeight()).sum())
					.totalCbm(item.getInbounds().stream().mapToDouble(t-> (t.getCbm() == null) ? 0 : t.getCbm()).sum())
					.finalTotalPrice(item.getInbounds().stream().mapToDouble(t->(t.getTotalPrice() == null) ? 0 : t.getTotalPrice()).sum())
					.export(item.getExport()).companyNm(item.getCompanyNm())	
					.incomDt(item.getIncomDt()).workDate(item.getWorkDate()).updateDt(item.getUpdateDt()).createDt(item.getCreateDt())
					.companyInfoId(item.getCompanyInfo().getId()).commonId(item.getWorkType().getId())
//					.cargo(item.getCargo()).toHarbor(item.getToHarbor())
//					.fromHarbor(item.getFromHarbor()).containerNo(item.getContainerNo())
//					.realNo(item.getRealNo()).hangmyung(item.getHangmyung()).hangcha(item.getHangcha())
//					.workTypeId(item.getWorkType().getNm()).chinaSanggumYn(item.isChinaSanggumYn())
//					
//					.userId(item.getUser().getId())					
					.build();
					
			return dto;
		}).collect(Collectors.toList());

		return list;

//// 보완 필요		
		
//		File f;
//		f.getFileuploadType().getId().equals(new Long(296))).get
		
//		item.getFileUploads()
//		.stream().filter(t->t.getUploadType().equals("A")).count();
		
		
		
//		int aTypeCount = _fileUploadRepository.findByInboundMasterAndUploadType(item, "A").size();
//		int bTypeCount = _fileUploadRepository.findByInboundMasterAndUploadType(item, "B").size();
//		int cTypeCount = _fileUploadRepository.findByInboundMasterAndUploadType(item, "C").size();
//		int dTypeCount = _fileUploadRepository.findByInboundMasterAndUploadType(item, "D").size();
//		int eTypeCount = _fileUploadRepository.findByInboundMasterAndUploadType(item, "E").size();
					
//					.aTypeCount(_fileUploadRepository.findByInboundMasterAndUploadType(item, "A").size())
//					.bTypeCount(_fileUploadRepository.findByInboundMasterAndUploadType(item, item.getFileUploads(). ).size())
//					.cTypeCount(_fileUploadRepository.findByInboundMasterAndUploadType(item, "C").size())
//					.dTypeCount(_fileUploadRepository.findByInboundMasterAndUploadType(item, "D").size())
//					.eTypeCount(_fileUploadRepository.findByInboundMasterAndUploadType(item, "E").size())
					
					
//					
					
//					.files(m)
//					.aTypeNm(item.getFileUploads().stream().filter(t->t.getFileuploadType().getId().equals(new Long(296)))
//							.findFirst()
//							.orElse(FileUpload.builder().fileNam2("").build()).getFileuploadType().getValue())
//					.bTypeNm(item.getFileUploads().stream().filter(t->t.getFileuploadType().getId().equals(new Long(297))).findFirst().orElse(FileUpload.builder().fileNam2("").build().getFileNam2())
//					.bTypeNm(item.getFileUploads().stream().filter(t->t.getFileuploadType().getId().equals(new Long(297))).findFirst().get().getFileuploadType().getValue())
//					.cTypeNm(item.getFileUploads().stream().filter(t->t.getFileuploadType().getId().equals(new Long(298))).findFirst().get().getFileuploadType().getValue())
//					.dTypeNm(item.getFileUploads().stream().filter(t->t.getFileuploadType().getId().equals(new Long(299))).findFirst().get().getFileuploadType().getValue())
//					.eTypeNm(item.getFileUploads().stream().filter(t->t.getFileuploadType().getId().equals(new Long(300))).findFirst().get().getFileuploadType().getValue())
					
					
	}
	
	@Override
	public InboundMasterRes addInboundMaster(InboundMasterReq inboundMasterReq) {
		InboundMaster inboundMaster = new InboundMaster();
		CompanyInfo companyInfo = _companyInfoRepository.findById(inboundMasterReq.getCompanyInfoId())
				.orElse(CompanyInfo.builder().build());
		CompanyInfoExport companyInfoExport = _companyInfoExportRepository.findById(inboundMasterReq.getCompanyInfoExportId())
				.orElse(CompanyInfoExport.builder().build());
		
		Common common = companyInfoExport.getCommon();
		
		
		inboundMaster.setWorkDate(inboundMasterReq.getWorkDate());
		inboundMaster.setMasterBlNo(inboundMasterReq.getMasterBlNo());
		inboundMaster.setExport(inboundMasterReq.getExport());
		inboundMaster.setCreateDt(new Date());
		inboundMaster.setUpdateDt(new Date());
		inboundMaster.setCompanyInfo(companyInfo);
		inboundMaster.setIsUsing(true);
		inboundMaster.setCompanyInfo(companyInfo);
		inboundMaster.setCompanyNm(companyInfo.getCoNm());
		inboundMaster.setWorkTypeMemo(common);		
		inboundMaster.setWorkType(common);
		inboundMaster.setExport(common.getValue());
		
		
		_inboundMasterRepository.save(inboundMaster);
		
		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		 return inboundMasterRes;
	}
	
	@Override
	public InboundMasterRes updateInboundMaster(InboundMasterReq inboundMasterReq) {
		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterReq.getId())
				.orElse(InboundMaster.builder().build());
		CompanyInfo companyInfo = _companyInfoRepository.findById(inboundMasterReq.getCompanyInfoId())
				.orElse(CompanyInfo.builder().build());
		CompanyInfoExport companyInfoExport = _companyInfoExportRepository.findById(inboundMasterReq.getCompanyInfoExportId())
				.orElse(CompanyInfoExport.builder().build());
		
		Common common = companyInfoExport.getCommon();
		inboundMaster.setId(inboundMasterReq.getId());
		inboundMaster.setWorkDate(inboundMasterReq.getWorkDate());
		inboundMaster.setMasterBlNo(inboundMasterReq.getMasterBlNo());
		inboundMaster.setExport(inboundMasterReq.getExport());
		inboundMaster.setCreateDt(new Date());
		inboundMaster.setUpdateDt(new Date());
		inboundMaster.setCompanyInfo(companyInfo);
		inboundMaster.setIsUsing(true);
		inboundMaster.setCompanyInfo(companyInfo);
		inboundMaster.setCompanyNm(companyInfo.getCoNm());
		inboundMaster.setWorkTypeMemo(common);		
		inboundMaster.setWorkType(common);
		inboundMaster.setExport(common.getValue());
		
		_inboundMasterRepository.save(inboundMaster);
		
		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		 return inboundMasterRes;
	}
	
	@Override
	public InboundMasterRes addInboundMasterCompany(InboundMasterReq inboundMasterReq) {
		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterReq.getId())
				.orElse(InboundMaster.builder().build());
		CompanyInfo companyInfo = _companyInfoRepository.findById(inboundMasterReq.getCompanyInfoId())
				.orElse(CompanyInfo.builder().build());
		
		
		inboundMaster.setCompanyInfo(companyInfo);
		inboundMaster.setCompanyNm(companyInfo.getCoNm());
		
		
		_inboundMasterRepository.save(inboundMaster);
		
		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		 return inboundMasterRes;
	}
	
	@Override
	public InboundMasterRes addInboundMasterExport(InboundMasterReq inboundMasterReq) {
		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterReq.getId())
				.orElse(InboundMaster.builder().build());
		
		CompanyInfoExport companyInfoExport = _companyInfoExportRepository.findById(inboundMasterReq.getCompanyInfoExportId())
				.orElse(CompanyInfoExport.builder().build());
		
		Common common = companyInfoExport.getCommon();
		
		inboundMaster.setWorkTypeMemo(common);		
		inboundMaster.setWorkType(common);
		inboundMaster.setExport(common.getValue());
		
		_inboundMasterRepository.save(inboundMaster);
		
		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		 return inboundMasterRes;
	}
	
	
	@Override
	public List<InboundRes> getInboundByInboundMasterId(InboundReq inboundReq) {
		
		
		
		List<InboundRes> result = _inboundRepository.findByInboundMasterId(inboundReq.getInboundMasterId()).stream()
				.sorted(Comparator.comparing(Inbound::getOrderNo))
				.map(item->InboundRes.builder()
						.id(item.getId())
						.companyNm(item.getCompanyNm())
						.marking(item.getMarking())
						.korNm(item.getKorNm())
						.itemCount(item.getItemCount())
						.boxCount(item.getBoxCount())
						.weight(item.getWeight())
						.cbm(item.getCbm())
						.reportPrice(item.getReportPrice())
						.memo1(item.getMemo1())
						.itemNo(item.getItemNo())
						.hsCode(item.getHsCode())
						.orderNo(item.getOrderNo())
//						.coCode(item.getCoCode())
//						.coCode(item.getCo().getValue())
//						.coId( item.getCo().getId() )
						.coId( (item.getCo() != null) ? item.getCo().getId() : null )
						.memo2(item.getMemo2())
						.memo3(item.getMemo3())
						.totalPrice(item.getTotalPrice())
						.engNm(item.getEngNm())
						.inboundMasterId(item.getInboundMaster().getId())
						
						
						.build())
		.collect(Collectors.toList());
		return result;
	}
	
	
	@Override
	public InboundRes excelCommitInboundData(InboundReq inboundReq) {
		int orderNo = 1;
		
		List<Inbound> inboundList = _inboundRepository.findByInboundMasterId(inboundReq.getInboundMasterId());
		for(int i = 0; i < inboundList.size(); i++) {
			_inboundRepository.delete(inboundList.get(i));
		}
		
		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundReq.getInboundMasterId())
				.orElse(InboundMaster.builder().build());
//		Common common = _commonRepository.findById(inboundReq.getCoId()).orElse(Common.builder().build());
//		Common common = _commonRepository.findById(
//				
//				(inboundReq.getCoId() !=null ) ? inboundReq.getCoId() : new Long(0) 
//				
//				).orElse(null);

		
		List<InboundReq> list = inboundReq.getInboundReqData();
		for (int i = 0; i < list.size(); i++) {
			
			
			Common common = _commonRepository.findByValue(
					
					(list.get(i).getCoCode() !=null ) ? list.get(i).getCoCode() : "" );
			
			Inbound inbound = new Inbound();
			inbound.setCompanyNm(list.get(i).getCompanyNm());
			inbound.setMarking(list.get(i).getMarking());
			inbound.setKorNm(list.get(i).getKorNm());
			inbound.setItemCount(list.get(i).getItemCount());
			inbound.setBoxCount(list.get(i).getBoxCount());
			inbound.setWeight(list.get(i).getWeight());
			inbound.setCbm(list.get(i).getCbm());
			inbound.setReportPrice(list.get(i).getReportPrice());
			inbound.setMemo1(list.get(i).getMemo1());
			inbound.setItemNo(list.get(i).getItemNo());
			inbound.setHsCode(list.get(i).getHsCode());
			inbound.setCoCode(list.get(i).getCoCode());
//			if(com)
			inbound.setCo(common);
			inbound.setMemo2(list.get(i).getMemo2());
			inbound.setMemo3(list.get(i).getMemo3());
			inbound.setTotalPrice(list.get(i).getTotalPrice());
			inbound.setEngNm(list.get(i).getEngNm());
			inbound.setOrderNo(orderNo);
			inbound.setInboundMaster(inboundMaster);
			_inboundRepository.save(inbound);
			orderNo=orderNo+1;
		}
		
		
		return InboundRes.builder().inboundMasterId(inboundReq.getInboundMasterId()).build();
	}
	@Override
	public InboundRes inboundCommit(InboundReq inboundReq) {
		int orderNo = 1;
		List<Inbound> inboundList = _inboundRepository.findByInboundMasterId(inboundReq.getInboundMasterId());
		for(int i = 0; i < inboundList.size(); i++) {
			_inboundRepository.delete(inboundList.get(i));
		}
		
		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundReq.getInboundMasterId())
				.orElse(InboundMaster.builder().build());
//		Common common = _commonRepository.findById(inboundReq.getCoId()).orElse(Common.builder().build());
//		Common common = _commonRepository.findById(
//				
//				(inboundReq.getCoId() !=null ) ? inboundReq.getCoId() : new Long(0) 
//				
//				).orElse(null);

		
		List<InboundReq> list = inboundReq.getInboundReqData();
		for (int i = 0; i < list.size(); i++) {
			
			
			Common common = _commonRepository.findById(
					
					(list.get(i).getCoId()!=null ) ? list.get(i).getCoId(): new Long(0) ).orElse(null);
			
			Inbound inbound = new Inbound();
			inbound.setCompanyNm(list.get(i).getCompanyNm());
			inbound.setMarking(list.get(i).getMarking());
			inbound.setKorNm(list.get(i).getKorNm());
			inbound.setItemCount(list.get(i).getItemCount());
			inbound.setBoxCount(list.get(i).getBoxCount());
			inbound.setWeight(list.get(i).getWeight());
			inbound.setCbm(list.get(i).getCbm());
			inbound.setReportPrice(list.get(i).getReportPrice());
			inbound.setMemo1(list.get(i).getMemo1());
			inbound.setItemNo(list.get(i).getItemNo());
			inbound.setHsCode(list.get(i).getHsCode());
			inbound.setCoCode(list.get(i).getCoCode());
			inbound.setCo(common);
			inbound.setMemo2(list.get(i).getMemo2());
			inbound.setMemo3(list.get(i).getMemo3());
			inbound.setTotalPrice(list.get(i).getTotalPrice());
			inbound.setEngNm(list.get(i).getEngNm());
			inbound.setOrderNo(orderNo);
			inbound.setInboundMaster(inboundMaster);
			_inboundRepository.save(inbound);
			orderNo=orderNo+1;
		}
		
		
		return InboundRes.builder().inboundMasterId(inboundReq.getInboundMasterId()).build();
	}
	
	@Override
	public InboundRes deleteInbound(InboundReq inboundReq) {
		List<InboundReq> inboundList = inboundReq.getInboundReqData();
		for(int i = 0; i < inboundList.size(); i++) {
			_inboundRepository.delete(_inboundRepository.findById(inboundList.get(i).getId()).orElse(Inbound.builder().build()));
		}
		return InboundRes.builder().inboundMasterId(inboundReq.getInboundMasterId()).build();
	}
	
	@Override
	public List<?> excelRead(MultipartFile file,InboundReq Req) throws IOException {
		
		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		// 첫번째 시트
		XSSFSheet sheet = workbook.getSheetAt(0);
		//마지막 row 숫자
		int rowCount = sheet.getLastRowNum();
		List<InboundRes> list = new ArrayList<>();
		//i=1   rowCount+1 해야 마지막 row 데이터까지 읽어옴
		for(int i=1; i<rowCount+1;i++) {
			XSSFRow row = sheet.getRow(i);
			InboundRes inboundRes = new InboundRes();
			row.cellIterator().forEachRemaining(cell->{
				int cellIndex = cell.getColumnIndex();
				switch (cell.getCellTypeEnum().name()) {
				
				case "STRING":
					String string1 = cell.getStringCellValue();
					excelUploadProcess02(cellIndex,string1,inboundRes);
					break;
				case "NUMERIC":
					cell.setCellType( HSSFCell.CELL_TYPE_STRING );
					String numberToString = cell.getStringCellValue();
					cellIndex = cell.getColumnIndex();		
					excelUploadProcess02(cellIndex,numberToString,inboundRes);
					break;
				case "FORMULA":
					String formula1 = cell.getCellFormula();
					cell.setCellFormula(formula1);
					break;
				default:
					break;
				}
				
			});
			
				list.add(inboundRes);
		}
		
		return list;
	}

	public void excelUploadProcess02(int cellIndex,String value,InboundRes inboundRes) {
		//cellIndex는 1개 row의 순차적 cell 의미 , value는 cellIndex의 value
		//commonid 입력값 없으면 insert 후보
		if(cellIndex == 0 ) {

			inboundRes.setCompanyNm(value);
			
		}
		if(cellIndex == 1) {
			inboundRes.setMarking(value);
		}
		if(cellIndex == 2) {
			inboundRes.setKorNm(value);
		}
		if(cellIndex == 3) {
			inboundRes.setItemCount(new Double(value));
		}
		if(cellIndex == 4) {
			inboundRes.setBoxCount(new Double(value));
		}
		if(cellIndex == 5) {
			inboundRes.setWeight(new Double(value));
		}
		if(cellIndex == 6) {
			inboundRes.setCbm(new Double(value));
		}
		if(cellIndex == 7) {
			inboundRes.setReportPrice(new Double(value));
		}
		if(cellIndex == 8) {
			inboundRes.setMemo1(value);
		}
		if(cellIndex == 9) {
			inboundRes.setItemNo(value);
		}
		if(cellIndex == 10) {
			inboundRes.setHsCode(value);
		}
		if(cellIndex == 11) {
			inboundRes.setCoCode(value);
		}
		if(cellIndex == 12) {
			inboundRes.setMemo2(value);
		}
		if(cellIndex == 13) {
			inboundRes.setMemo3(value);
		}
		if(cellIndex == 14) {
			inboundRes.setTotalPrice(new Double(value));
		}
		if(cellIndex == 15) {
			inboundRes.setEngNm(value);
		}
	}

	
}