package com.keepgo.whatdo.service.inbound.impl;

import java.io.IOException;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
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

import com.keepgo.whatdo.define.AmountType;
import com.keepgo.whatdo.define.BackgroundColorType;
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
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
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
		List<InboundRes> list = _inboundRepository.findAll().stream().sorted(com).map(item -> {
			InboundRes inboundRes = InboundRes.builder().blNo(item.getBlNo()).companyNm(item.getCompanyNm())
					.memo1(item.getMemo1()).marking(item.getMarking()).orderNo(item.getOrderNo()).build();

			return inboundRes;

		}).collect(Collectors.toList());

		return _UtilService.changeExcelFormatNew(list);
//		return null;
//		return list;
	}

	@Override
	public List<?> getInboundMaster() {

//		FileUpload t;
//		t.getInboundMaster().getId()
//		
		List<?> list = _inboundMasterRepository.findAll().stream()

				.sorted(Comparator.comparing(InboundMaster::getUpdateDt).reversed())

				.map(item -> {

//					List<FileTypeRes> ll = FileType.getList();
					List<FileTypeRes> ll = item.getFileUploads().stream()
							.filter(t -> t.getInboundMaster().getId() == item.getId()).map(t -> {

								return FileTypeRes.builder()

										.id(t.getFileType()).build();
							}).collect(Collectors.toList());

//					CommonRes res;
//					
//					ll.stream().filter(t->t.getId() == new Long(296)).count();
//					List<InboundRes> li = ;

					Double ff = item.getInbounds().stream().mapToDouble(t -> t.getBoxCount()).sum();
					InboundMasterRes dto = InboundMasterRes.builder()

//					.id(item.getId()).masterBlNo(item.getMasterBlNo())
							.aTypeCount(ll.stream()
									.filter(t -> t.getId() == (new Integer(FileType.A.getId()).intValue())).count())
							.bTypeCount(ll.stream()
									.filter(t -> t.getId() == (new Integer(FileType.B.getId()).intValue())).count())
							.cTypeCount(ll.stream()
									.filter(t -> t.getId() == (new Integer(FileType.C.getId()).intValue())).count())
							.dTypeCount(ll.stream()
									.filter(t -> t.getId() == (new Integer(FileType.D.getId()).intValue())).count())
							.eTypeCount(ll.stream()
									.filter(t -> t.getId() == (new Integer(FileType.E.getId()).intValue())).count())

							.aTypeNm(FileType.A.getName()).aTypeNm(FileType.B.getName()).aTypeNm(FileType.C.getName())
							.aTypeNm(FileType.D.getName()).aTypeNm(FileType.E.getName())

							.totalItemCount(item.getInbounds().stream()
									.mapToDouble(t -> (t.getItemCount() == null) ? 0 : t.getItemCount()).sum())
							.totalBoxCount(item.getInbounds().stream()
									.mapToDouble(t -> (t.getBoxCount() == null) ? 0 : t.getBoxCount()).sum())
							.totalWeight(item.getInbounds().stream()
									.mapToDouble(t -> (t.getWeight() == null) ? 0 : t.getWeight()).sum())
							.totalCbm(item.getInbounds().stream()
									.mapToDouble(t -> (t.getCbm() == null) ? 0 : t.getCbm()).sum())
							.finalTotalPrice(item.getInbounds().stream()
									.mapToDouble(t -> (t.getTotalPrice() == null) ? 0 : t.getTotalPrice()).sum())
//					.export(item.getExport()).companyNm(item.getCompanyNm())	
//					.incomDt(item.getIncomDt()).workDate(item.getWorkDate()).updateDt(item.getUpdateDt()).createDt(item.getCreateDt())
//					.companyInfoId(item.getCompanyInfo().getId()).commonId(item.getWorkType().getId())
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
		CompanyInfoExport companyInfoExport = _companyInfoExportRepository.findById(inboundMasterReq.getExportId())
				.orElse(CompanyInfoExport.builder().build());

		Common common = companyInfoExport.getCommon();

//		inboundMaster.setWorkDate(inboundMasterReq.getWorkDate());
//		inboundMaster.setMasterBlNo(inboundMasterReq.getMasterBlNo());
//		inboundMaster.setExport(inboundMasterReq.getExport());
//		inboundMaster.setCreateDt(new Date());
//		inboundMaster.setUpdateDt(new Date());
//		inboundMaster.setCompanyInfo(companyInfo);
//		inboundMaster.setIsUsing(true);
//		inboundMaster.setCompanyInfo(companyInfo);
//		inboundMaster.setCompanyNm(companyInfo.getCoNm());
//		inboundMaster.setWorkTypeMemo(common);		
//		inboundMaster.setWorkType(common);
//		inboundMaster.setExport(common.getValue());

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
		CompanyInfoExport companyInfoExport = _companyInfoExportRepository.findById(inboundMasterReq.getExportId())
				.orElse(CompanyInfoExport.builder().build());

		Common common = companyInfoExport.getCommon();
		inboundMaster.setId(inboundMasterReq.getId());
//		inboundMaster.setWorkDate(inboundMasterReq.getWorkDate());
//		inboundMaster.setMasterBlNo(inboundMasterReq.getMasterBlNo());
//		inboundMaster.setExport(inboundMasterReq.getExport());
//		inboundMaster.setCreateDt(new Date());
//		inboundMaster.setUpdateDt(new Date());
//		inboundMaster.setCompanyInfo(companyInfo);
//		inboundMaster.setIsUsing(true);
//		inboundMaster.setCompanyInfo(companyInfo);
//		inboundMaster.setCompanyNm(companyInfo.getCoNm());
//		inboundMaster.setWorkTypeMemo(common);		
//		inboundMaster.setWorkType(common);
//		inboundMaster.setExport(common.getValue());

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
//		inboundMaster.setCompanyNm(companyInfo.getCoNm());

		_inboundMasterRepository.save(inboundMaster);

		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		return inboundMasterRes;
	}

	@Override
	public InboundMasterRes addInboundMasterExport(InboundMasterReq inboundMasterReq) {
		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterReq.getId())
				.orElse(InboundMaster.builder().build());

		CompanyInfoExport companyInfoExport = _companyInfoExportRepository.findById(inboundMasterReq.getExportId())
				.orElse(CompanyInfoExport.builder().build());

		Common common = companyInfoExport.getCommon();

//		inboundMaster.setWorkTypeMemo(common);		
//		inboundMaster.setWorkType(common);
//		inboundMaster.setExport(common.getValue());

		_inboundMasterRepository.save(inboundMaster);

		InboundMasterRes inboundMasterRes = new InboundMasterRes();
		return inboundMasterRes;
	}

	@Override
	public List<InboundRes> getInboundByInboundMasterId(InboundReq inboundReq)  {
		
		
		List<Integer> index = new ArrayList<Integer>();
		InboundMaster inboundMaster=_inboundMasterRepository.findById(inboundReq.getInboundMasterId()).get();
		List<InboundRes> result = _inboundRepository.findByInboundMasterId(inboundReq.getInboundMasterId()).stream()
				.sorted(Comparator.comparing(Inbound::getOrderNo))
				.map(item->{
					
					
					
					InboundRes rt = InboundRes.builder()
				
						
						.id(item.getId())
						.companyNm(item.getCompanyNm())
						.orderNoStr(item.getOrderNoStr())
						.workDateStr(item.getWorkDateStr())
						.jejil(item.getJejil())
						.companyNm(item.getCompanyNm())
						.marking(item.getMarking())
						.korNm(item.getKorNm())
						.itemCount(item.getItemCount())
						.boxCount(item.getBoxCount())
						.jejil(item.getJejil())
						.weight(item.getWeight())
						.cbm(item.getCbm())
						.reportPrice(item.getReportPrice())
						.memo1(item.getMemo1())
						.itemNo(item.getItemNo())
						.hsCode(item.getHsCode())
						.orderNo(item.getOrderNo())
						.memo2(item.getMemo2())
						.memo3(item.getMemo3())
						.totalPrice(item.getTotalPrice())
						.engNm(item.getEngNm())
						
						.amountType(item.getAmountType()==null?"PCS":item.getAmountType())
						.currencyType(inboundMaster.getCurrencyType())
						
						.companyNmYn(item.getCompanyNmYn()==null ? "" : item.getCompanyNmYn())
						.markingYn(item.getMarkingYn()==null ? "" : item.getMarkingYn())
						.workDateStrYn(item.getWorkDateStrYn()==null ? "" : item.getWorkDateStrYn())
						.korNmYn(item.getKorNmYn()==null ? "" : item.getKorNmYn())
						.itemCountYn(item.getItemCountYn()==null ? "" : item.getItemCountYn())
						.boxCountYn(item.getBoxCountYn()==null ? "" : item.getBoxCountYn())
						.weightYn(item.getWeightYn()==null ? "" : item.getWeightYn())
						.cbmYn(item.getCbmYn()==null ? "" : item.getCbmYn())
						.reportPriceYn(item.getReportPriceYn()==null ? "" : item.getReportPriceYn())
						.memo1Yn(item.getMemo1Yn()==null ? "" : item.getMemo1Yn())
						.memo2Yn(item.getMemo2Yn()==null ? "" : item.getMemo2Yn())
						.itemNoYn(item.getItemNoYn()==null ? "" : item.getItemNoYn())
						.jejilYn(item.getJejilYn()==null ? "" : item.getJejilYn())
						.hsCodeYn(item.getHsCodeYn()==null ? "" : item.getHsCodeYn())
						.totalPriceYn(item.getTotalPriceYn()==null ? "" : item.getTotalPriceYn())
						.engNmYn(item.getEngNmYn()==null ? "" : item.getEngNmYn())
						.coIdYn(item.getCoIdYn()==null ? "" : item.getCoIdYn())
//						.companyNmYn(item.)
//						.markingYn(item.getMarkingYn()))
//						.color(item.getColorCode())
//						.color(ColorType.getList().stream().filter(type->type.getId() == item.getColor()).findFirst().get().getShowName())
//						.colorCode(item.getColor())
						.inboundMasterId(item.getInboundMaster().getId())
//						.coId(Long.valueOf(item.getCoId()))
//						.coCode(CoType.getList().stream().filter(t->t.getId() == item.getCoId()).findFirst().get().getName())
						
						.build();
						
					if(item.getWorkDateStr()!=null && !(item.getWorkDateStr().replaceAll(" ", "").equals(""))) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
						SimpleDateFormat format2 =  new SimpleDateFormat("MM월 dd일");
						format2.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
						Date d=null;
						try {
							d=format.parse(item.getWorkDateStr());
							String forViewWorkDateStr = format2.format(d);
							rt.setForViewWorkDateStr(forViewWorkDateStr);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(item.getCoId() != null) {
						rt.setCoId(Long.valueOf(item.getCoId()));
						for(int i=0; i<CoType.getList().size();i++) {
							if(CoType.getList().get(i).getId() ==item.getCoId() ) {
								rt.setCoCode(CoType.getList().get(i).getName());							
							}
						}
//						rt.setCoCode(CoType.getList().stream().filter(t->t.getId() == item.getCoId()).findFirst().get().getName());)
					}
					if(item.getColor() !=null ) {
						rt.setColorId(Long.valueOf(item.getColor()));
						for(int i=0; i<ColorType.getList().size();i++) {
							if(ColorType.getList().get(i).getId() ==item.getColor() ) {
								rt.setColorCode(ColorType.getList().get(i).getCode());
								rt.setColor(ColorType.getList().get(i).getShowName());
							}
						}
					}
					if(item.getBackgroundColor() !=null ) {
						rt.setBackgroundColorId(Long.valueOf(item.getBackgroundColor()));
						for(int i=0; i<BackgroundColorType.getList().size();i++) {
							if(BackgroundColorType.getList().get(i).getId() ==item.getBackgroundColor() ) {
								rt.setBackgroundColorCode(BackgroundColorType.getList().get(i).getCode());
								rt.setBackgroundColor(BackgroundColorType.getList().get(i).getShowName());
							}
						}
					}
					if(item.getAmountType() !=null ) {
						rt.setAmountType(item.getAmountType());
						
					}
					if(index.size() == 0) {
//						rt.setMasterCompany("ARPWU B A C ZZZZ EW KALD FLWEF");
//						rt.setFreight(item.getInboundMaster().getFreight());
						rt.setFreight(FreightType.getList().stream().filter(type->type.getId() ==item.getInboundMaster().getFreight()).findFirst().get().getName());
						rt.setFreightCode(item.getInboundMaster().getFreight());
//						rt.setManagerNm("홍길동 과장");
//						rt.setMasterCompanyNumber("sdfsadfSDF");)

						if(item.getInboundMaster().getCompanyInfo()!=null) {
							StringBuffer sb = new StringBuffer();
							String companyNum=item.getInboundMaster().getCompanyInfo().getCoNum();
							sb.append(companyNum);
							sb.insert(3, "-");
							sb.insert(6, "-");
							rt.setMasterCompany(item.getInboundMaster().getCompanyInfo().getCoNm()+"\n"+sb.toString());
							if(item.getInboundMaster().getManager()!=null) {
								rt.setManagerNm(item.getInboundMaster().getManager());
							}else {
								rt.setManagerNm(item.getInboundMaster().getCompanyInfo().getManager());
							}
							
//							rt.setCompanyNm(item.getInboundMaster().getCompanyInfo().getCoNm());
							
						}
//						rt.setMasterExport("EXLFLDKE FLAKD .D  VLA ");
//						rt.setMasterExportAddr("DKEIPQ FPA V Z Z EIWWJF ALDLK BV C ALSLD FKS DLF SLDKF ");
						if(item.getInboundMaster().getComExport()!=null) {
							rt.setMasterExport(item.getInboundMaster().getComExport().getValue()+"\n"+item.getInboundMaster().getComExport().getValue2());	
						}
						
						rt.setWorkDate(item.getInboundMaster().getWorkDate());
//						String forViewWorkDate = DateFormatUtils.format(item.getInboundMaster().getWorkDate(), "MM월 dd일");
//						rt.setForViewWorkDate(forViewWorkDate);
						rt.setBlNo(item.getInboundMaster().getBlNo());
					}
//					if(index.size() == 1) {
//						if(item.getInboundMaster().getCompanyInfo()!=null) {
//							rt.setMasterCompany(item.getInboundMaster().getCompanyInfo().getCoNum());	
//						}
//						if(item.getInboundMaster().getComExport()!=null) {
//							rt.setMasterExport(item.getInboundMaster().getComExport().getValue2());	
//						}
//					}
					index.add(1);

					
					
					return rt;
						
				})
		.collect(Collectors.toList());
		if(result.size()>0) {
			result.get(0).setMasterCompanyNumber("123512351");

			result.get(0).setBoxCountSum(result.stream().filter(t->t.getBoxCount()!= null).mapToDouble(t->t.getBoxCount()).sum());
			result.get(0).setItemCountSum(result.stream().filter(t->t.getItemCount()!= null).mapToDouble(t->t.getItemCount()).sum());
			result.get(0).setWeightSum(result.stream().filter(t->t.getWeight()!= null).mapToDouble(t->t.getWeight()).sum());
			result.get(0).setCbmSum(result.stream().filter(t->t.getCbm()!= null).mapToDouble(t->t.getCbm()).sum());
//			result.get(0).setTotalPriceSum(result.stream().filter(t->t.getTotalPrice()!= null).mapToDouble(t->t.getTotalPrice()).sum());
			Double totalPriceSum = null;
			for(int i=0; i<result.size(); i++) {
				if(result.get(i).getTotalPrice()!=null) {
					if(totalPriceSum == null ) totalPriceSum = new Double(0);
					totalPriceSum += result.get(i).getTotalPrice();
				}
				
			}
			result.get(0).setTotalPriceSum(totalPriceSum);
		}
		
		return result;
	}
	@Override
	public List<InboundRes> getInboundByMasterId(Long id)  {
		
		
		List<Integer> index = new ArrayList<Integer>();
		InboundMaster inboundMaster=_inboundMasterRepository.findById(id).get();
		List<InboundRes> result = _inboundRepository.findByInboundMasterId(id).stream()
				.sorted(Comparator.comparing(Inbound::getOrderNo))
				.map(item->{
					
					
					
					InboundRes rt = InboundRes.builder()
				
						
						.id(item.getId())
						.orderNoStr(item.getOrderNoStr())
						.workDateStr(item.getWorkDateStr())
						.jejil(item.getJejil())
						.companyNm(item.getCompanyNm())
						.marking(item.getMarking())
						.korNm(item.getKorNm())
						.itemCount(item.getItemCount())
						.boxCount(item.getBoxCount())
						.jejil(item.getJejil())
//						.weight(item.getWeight() == null ? 0 : item.getWeight())
//						.cbm(item.getCbm() == null ? 0 : item.getCbm())
						.weight(item.getWeight())
						.cbm(item.getCbm())
						.reportPrice(item.getReportPrice())
						.memo1(item.getMemo1())
						.itemNo(item.getItemNo())
						.hsCode(item.getHsCode())
						.orderNo(item.getOrderNo())
						.memo2(item.getMemo2())
						.memo3(item.getMemo3())
						.totalPrice(item.getTotalPrice())
						.engNm(item.getEngNm())
						.amountType(item.getAmountType()==null?"PCS":item.getAmountType())
						.currencyType(inboundMaster.getCurrencyType())
						.companyNmYn(item.getCompanyNmYn()==null ? "" : item.getCompanyNmYn())
						.markingYn(item.getMarkingYn()==null ? "" : item.getMarkingYn())
						.workDateStrYn(item.getWorkDateStrYn()==null ? "" : item.getWorkDateStrYn())
						.korNmYn(item.getKorNmYn()==null ? "" : item.getKorNmYn())
						.itemCountYn(item.getItemCountYn()==null ? "" : item.getItemCountYn())
						.boxCountYn(item.getBoxCountYn()==null ? "" : item.getBoxCountYn())
						.weightYn(item.getWeightYn()==null ? "" : item.getWeightYn())
						.cbmYn(item.getCbmYn()==null ? "" : item.getCbmYn())
						.reportPriceYn(item.getReportPriceYn()==null ? "" : item.getReportPriceYn())
						.memo1Yn(item.getMemo1Yn()==null ? "" : item.getMemo1Yn())
						.memo2Yn(item.getMemo2Yn()==null ? "" : item.getMemo2Yn())
						.itemNoYn(item.getItemNoYn()==null ? "" : item.getItemNoYn())
						.jejilYn(item.getJejilYn()==null ? "" : item.getJejilYn())
						.hsCodeYn(item.getHsCodeYn()==null ? "" : item.getHsCodeYn())
						.totalPriceYn(item.getTotalPriceYn()==null ? "" : item.getTotalPriceYn())
						.engNmYn(item.getEngNmYn()==null ? "" : item.getEngNmYn())
						.coIdYn(item.getCoIdYn()==null ? "" : item.getCoIdYn())
								
						//						.color(item.getColorCode())
//						.color(ColorType.getList().stream().filter(type->type.getId() == item.getColor()).findFirst().get().getShowName())
//						.colorCode(item.getColor())
						.inboundMasterId(item.getInboundMaster().getId())
//						.coId(Long.valueOf(item.getCoId()))
//						.coCode(CoType.getList().stream().filter(t->t.getId() == item.getCoId()).findFirst().get().getName())
						
						.build();
						
					if(item.getWorkDateStr()!=null && !(item.getWorkDateStr().replaceAll(" ", "").equals(""))) {
						SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
						format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
						SimpleDateFormat format2 =  new SimpleDateFormat("MM월 dd일");
						format2.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));
						Date d=null;
						try {
							d=format.parse(item.getWorkDateStr());
							String forViewWorkDateStr = format2.format(d);
							rt.setForViewWorkDateStr(forViewWorkDateStr);
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					if(item.getCoId() != null) {
						rt.setCoId(Long.valueOf(item.getCoId()));
						for(int i=0; i<CoType.getList().size();i++) {
							if(CoType.getList().get(i).getId() ==item.getCoId() ) {
								rt.setCoCode(CoType.getList().get(i).getName());							
							}
						}
//						rt.setCoCode(CoType.getList().stream().filter(t->t.getId() == item.getCoId()).findFirst().get().getName());)
					}
					if(item.getColor() !=null ) {
						rt.setColorId(Long.valueOf(item.getColor()));
						for(int i=0; i<ColorType.getList().size();i++) {
							if(ColorType.getList().get(i).getId() ==item.getColor() ) {
								rt.setColorCode(ColorType.getList().get(i).getCode());
								rt.setColor(ColorType.getList().get(i).getShowName());
							}
						}
//						rt.setCoCode(CoType.getList().stream().filter(t->t.getId() == item.getCoId()).findFirst().get().getName());)
					}
					if(item.getBackgroundColor() !=null ) {
						rt.setBackgroundColorId(Long.valueOf(item.getBackgroundColor()));
						for(int i=0; i<BackgroundColorType.getList().size();i++) {
							if(BackgroundColorType.getList().get(i).getId() ==item.getBackgroundColor() ) {
								rt.setBackgroundColorCode(BackgroundColorType.getList().get(i).getCode());
								rt.setBackgroundColor(BackgroundColorType.getList().get(i).getShowName());
							}
						}
					}else {
						rt.setBackgroundColorId(new Long(0));
					}
					if(index.size() == 0) {
//						rt.setMasterCompany("ARPWU B A C ZZZZ EW KALD FLWEF");
//						rt.setFreight(item.getInboundMaster().getFreight());
						rt.setFreight(FreightType.getList().stream().filter(type->type.getId() ==item.getInboundMaster().getFreight()).findFirst().get().getName());
						rt.setFreightCode(item.getInboundMaster().getFreight());
//						rt.setManagerNm("홍길동 과장");
//						rt.setMasterCompanyNumber("sdfsadfSDF");)

						if(item.getInboundMaster().getCompanyInfo()!=null) {
							StringBuffer sb = new StringBuffer();
							String companyNum=item.getInboundMaster().getCompanyInfo().getCoNum();
							sb.append(companyNum);
							sb.insert(3, "-");
							sb.insert(6, "-");
							rt.setMasterCompany(item.getInboundMaster().getCompanyInfo().getCoNm()+"\n"+sb.toString());
							if(item.getInboundMaster().getManager()!=null) {
								rt.setManagerNm(item.getInboundMaster().getManager());
							}else {
								rt.setManagerNm(item.getInboundMaster().getCompanyInfo().getManager());
							}
							
//							rt.setCompanyNm(item.getInboundMaster().getCompanyInfo().getCoNm());
							
						}
//						rt.setMasterExport("EXLFLDKE FLAKD .D  VLA ");
//						rt.setMasterExportAddr("DKEIPQ FPA V Z Z EIWWJF ALDLK BV C ALSLD FKS DLF SLDKF ");
						if(item.getInboundMaster().getComExport()!=null) {
							rt.setMasterExport(item.getInboundMaster().getComExport().getValue()+"\n"+item.getInboundMaster().getComExport().getValue2());	
						}
						
						rt.setWorkDate(item.getInboundMaster().getWorkDate());
//						String forViewWorkDate = DateFormatUtils.format(item.getInboundMaster().getWorkDate(), "MM월 dd일");
//						rt.setForViewWorkDate(forViewWorkDate);
						rt.setBlNo(item.getInboundMaster().getBlNo());
					}
//					if(index.size() == 1) {
//						if(item.getInboundMaster().getCompanyInfo()!=null) {
//							rt.setMasterCompany(item.getInboundMaster().getCompanyInfo().getCoNum());	
//						}
//						if(item.getInboundMaster().getComExport()!=null) {
//							rt.setMasterExport(item.getInboundMaster().getComExport().getValue2());	
//						}
//					}
					index.add(1);

					
					
					return rt;
						
				})
		.collect(Collectors.toList());
		if(result.size()>0) {
			result.get(0).setMasterCompanyNumber("123512351");

			result.get(0).setBoxCountSum(result.stream().filter(t->t.getBoxCount()!= null).mapToDouble(t->t.getBoxCount()).sum());
			result.get(0).setItemCountSum(result.stream().filter(t->t.getItemCount()!= null).mapToDouble(t->t.getItemCount()).sum());
			result.get(0).setWeightSum(result.stream().filter(t->t.getWeight()!= null).mapToDouble(t->t.getWeight()).sum());
			result.get(0).setCbmSum(result.stream().filter(t->t.getCbm()!= null).mapToDouble(t->t.getCbm()).sum());
//			result.get(0).setTotalPriceSum(result.stream().filter(t->t.getTotalPrice()!= null).mapToDouble(t->t.getTotalPrice()).sum());
			
			Double totalPriceSum = null;
			for(int i=0; i<result.size(); i++) {
				if(result.get(i).getTotalPrice()!=null) {
					
					if(totalPriceSum == null ) totalPriceSum = new Double(0);
					totalPriceSum += result.get(i).getTotalPrice();
				}
				
			}
			result.get(0).setTotalPriceSum(totalPriceSum);
		}
		
		return result;
	}
	@Override
	public InboundRes excelCommitInboundData(InboundReq inboundReq) {
		int orderNo = 1;

		List<Inbound> inboundList = _inboundRepository.findByInboundMasterId(inboundReq.getInboundMasterId());
		for (int i = 0; i < inboundList.size(); i++) {
			_inboundRepository.delete(inboundList.get(i));
		}

		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundReq.getInboundMasterId())
				.orElse(InboundMaster.builder().build());

		
		List<InboundReq> list = inboundReq.getInboundReqData();
		for (int i = 0; i < list.size(); i++) {



			int index = i;

			Inbound inbound = new Inbound();
			inbound.setWorkDateStr(list.get(i).getWorkDateStr());
			inbound.setJejil(list.get(i).getJejil());
			inbound.setOrderNoStr(list.get(i).getOrderNoStr());
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
			if(list.get(i).getColor()!=null) {
				for(int j=0; j<ColorType.getList().size();j++) {
					if(ColorType.getList().get(j).getShowName().equals(list.get(i).getColor())) {
						inbound.setColor(ColorType.getList().get(j).getId());
					}else {
//						inbound.setColor(Integer.valueOf(0));
					}
				}
				
			}else {
				inbound.setColor(Integer.valueOf(0));
			}
			if(list.get(i).getBackgroundColor()!=null) {
				for(int j=0; j<BackgroundColorType.getList().size();j++) {
					if(BackgroundColorType.getList().get(j).getName().equals(list.get(i).getBackgroundColor())) {
						inbound.setBackgroundColor(BackgroundColorType.getList().get(j).getId());
					}else {
					}
				}
//				inbound.setBackgroundColor(Integer.valueOf(1));
			}else {
				inbound.setBackgroundColor(Integer.valueOf(0));
			}
			inbound.setCoId(list.get(i).getCoId().intValue());
			if(list.get(i).getAmountType()!=null) {
				for(int j=0; j<AmountType.getList().size();j++) {
					if(AmountType.getList().get(j).getName().equals(list.get(i).getAmountType())) {
						inbound.setAmountType(AmountType.getList().get(j).getName());
					}else {

					}
				}
				
			}else {
				inbound.setAmountType("PCS");
			}

			inbound.setMemo2(list.get(i).getMemo2());
			inbound.setMemo3(list.get(i).getMemo3());
			inbound.setTotalPrice(list.get(i).getTotalPrice());
			inbound.setEngNm(list.get(i).getEngNm());
			inbound.setOrderNo(orderNo);
			inbound.setInboundMaster(inboundMaster);
			if(inbound.getCoId()==null) {
				inbound.setCoId(Integer.valueOf(3));
			}
			if(inbound.getBackgroundColor()==null) {
				inbound.setBackgroundColor(Integer.valueOf(0));
			}
			_inboundRepository.save(inbound);
			orderNo = orderNo + 1;
		}

		return InboundRes.builder().inboundMasterId(inboundReq.getInboundMasterId()).build();
	}
	
	

	@Override
	public InboundRes inboundCommit(InboundReq inboundReq) {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		int orderNo = 1;
		List<Inbound> inboundList = _inboundRepository.findByInboundMasterId(inboundReq.getInboundMasterId());
		for (int i = 0; i < inboundList.size(); i++) {
			_inboundRepository.delete(inboundList.get(i));
		}

		InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundReq.getInboundMasterId())
				.orElse(InboundMaster.builder().build());
		List<InboundReq> list = inboundReq.getInboundReqData();
		List<Inbound> finalList = new ArrayList<>();
		for (int i = 0; i < list.size(); i++) {

			
			Inbound inbound = new Inbound();
			inbound.setWorkDateStr(list.get(i).getWorkDateStr());
			inbound.setOrderNoStr(list.get(i).getOrderNoStr());
			inbound.setJejil(list.get(i).getJejil());
			inbound.setCompanyNm(list.get(i).getCompanyNm());
			inbound.setMarking(list.get(i).getMarking());
			
			//color
			inbound.setCompanyNmYn(list.get(i).getCompanyNmYn());
			inbound.setMarkingYn(list.get(i).getMarkingYn());
			inbound.setWorkDateStrYn(list.get(i).getWorkDateStrYn());
			inbound.setKorNmYn(list.get(i).getKorNmYn());
			inbound.setItemCountYn(list.get(i).getItemCountYn());
			inbound.setBoxCountYn(list.get(i).getBoxCountYn());
			inbound.setWeightYn(list.get(i).getWeightYn());
			inbound.setCbmYn(list.get(i).getCbmYn());
			inbound.setReportPriceYn(list.get(i).getReportPriceYn());
			inbound.setMemo1Yn(list.get(i).getMemo1Yn());
			inbound.setMemo2Yn(list.get(i).getMemo2Yn());
			inbound.setItemNoYn(list.get(i).getItemNoYn());
			inbound.setJejilYn(list.get(i).getJejilYn());
			inbound.setHsCodeYn(list.get(i).getHsCodeYn());
			inbound.setTotalPriceYn(list.get(i).getTotalPriceYn());
			inbound.setEngNmYn(list.get(i).getEngNmYn());
//20220810
			inbound.setCoIdYn(list.get(i).getCoIdYn());
			//color
			
			inbound.setKorNm(list.get(i).getKorNm());
			inbound.setItemCount( (list.get(i).getItemCount() ==  null ? new Double(0) : list.get(i).getItemCount()));
			inbound.setBoxCount(list.get(i).getBoxCount());
			inbound.setWeight(list.get(i).getWeight());
			inbound.setCbm(list.get(i).getCbm());
			inbound.setReportPrice((list.get(i).getReportPrice() ==  null ? null : list.get(i).getReportPrice()));
			inbound.setMemo1(list.get(i).getMemo1());
			inbound.setItemNo(list.get(i).getItemNo());
			inbound.setHsCode(list.get(i).getHsCode());
			if(list.get(i).getCoId()!=null) {
				inbound.setCoId(list.get(i).getCoId().intValue());	
			}
			inbound.setCoCode(list.get(i).getCoCode());
			
			if(list.get(i).getColor()!=null) {
				for(int j=0; j<ColorType.getList().size();j++) {
					if(ColorType.getList().get(j).getShowName().equals(list.get(i).getColor())) {
						inbound.setColor(ColorType.getList().get(j).getId());
					}else {

					}
				}
				
			}else {
				inbound.setColor(Integer.valueOf(0));
			}
			if(list.get(i).getBackgroundColor()!=null) {
				for(int j=0; j<BackgroundColorType.getList().size();j++) {
					if(BackgroundColorType.getList().get(j).getShowName().equals(list.get(i).getBackgroundColor())) {
						inbound.setBackgroundColor(BackgroundColorType.getList().get(j).getId());
					}else {

					}
				}
				
			}else {
				inbound.setBackgroundColor(Integer.valueOf(0));
			}
			
			if(list.get(i).getAmountType()!=null) {
				for(int j=0; j<AmountType.getList().size();j++) {
					if(AmountType.getList().get(j).getName().equals(list.get(i).getAmountType())) {
						inbound.setAmountType(AmountType.getList().get(j).getName());
					}else {

					}
				}
				
			}else {
				inbound.setAmountType("PCS");
			}
			
			
			
			inbound.setMemo2(list.get(i).getMemo2());
			inbound.setMemo3(list.get(i).getMemo3());
			if(inbound.getReportPrice()==null||inbound.getItemCount()==null) {
				inbound.setTotalPrice(null);
			}else {
//				BigDecimal bigNumber1 = new BigDecimal(decimalFormat.format(inbound.getReportPrice()));
//				BigDecimal bigNumber2 = new BigDecimal(decimalFormat.format(inbound.getItemCount()));
//				BigDecimal totalPrice = bigNumber1.multiply(bigNumber2);
				
				
//				inbound.setTotalPrice(totalPrice.doubleValue());
				inbound.setTotalPrice(inbound.getReportPrice() * inbound.getItemCount());
//				System.out.println(inbound.getTotalPrice()+"<<<<<<<<<<<TotalPrice()");
			}
			
			inbound.setEngNm(list.get(i).getEngNm());
			inbound.setOrderNo(orderNo);
			inbound.setInboundMaster(inboundMaster);
//			inbound.setColor(list.get(i).getColor());
			finalList.add(inbound);
			
			orderNo = orderNo + 1;
		}
		
		
		Function<Inbound, Inbound> fn = (item)->{
			

//			if(item.getReportPrice() == null || item.getReportPrice().equals("")) {
//					
//			}else {
//				item.setTotalPrice(new Double(String.format("%.2f",item.getReportPrice() * item.getItemCount())));
//			}
			
			return item;
			
		};
		
		finalList = finalList.stream()
		.map(fn).collect(Collectors.toList());
		finalList.forEach(t->{
			_inboundRepository.save(t);
		});
		
		
		

		return InboundRes.builder().inboundMasterId(inboundReq.getInboundMasterId()).build();
	}

	@Override
	public InboundRes deleteInbound(InboundReq inboundReq) {
		List<InboundReq> inboundList = inboundReq.getInboundReqData();
		for (int i = 0; i < inboundList.size(); i++) {
			_inboundRepository
					.delete(_inboundRepository.findById(inboundList.get(i).getId()).orElse(Inbound.builder().build()));
		}
		return InboundRes.builder().inboundMasterId(inboundReq.getInboundMasterId()).build();
	}

	@Override
	public List<?> excelRead(MultipartFile file, InboundReq Req) throws IOException {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		// 첫번째 시트
		XSSFSheet sheet = workbook.getSheetAt(0);
		String sheetName = workbook.getSheetAt(0).getSheetName();
		// 마지막 row 숫자
		int rowCount = sheet.getLastRowNum();
		List<InboundRes> list = new ArrayList<>();
		Map<String,Boolean> validations = new HashedMap<>();
		// i=1 rowCount+1 해야 마지막 row 데이터까지 읽어옴
		for (int i = 1; i < rowCount + 1; i++) {
			XSSFRow row = sheet.getRow(i);
//			boolean validation = true;
			InboundRes inboundRes = new InboundRes();
			row.cellIterator().forEachRemaining(cell -> {
				int cellIndex = cell.getColumnIndex();
				switch (cell.getCellTypeEnum().name()) {

				case "STRING":
					String string1 = cell.getStringCellValue();
					try {
						excelUploadProcess02(cellIndex, string1, inboundRes);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					break;
				case "NUMERIC":
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					String numberToString = cell.getStringCellValue();
					cellIndex = cell.getColumnIndex();
					try {
						excelUploadProcess02(cellIndex, numberToString, inboundRes);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					break;
				case "FORMULA":
					String formula1 = cell.getCellFormula();
					cell.setCellFormula(formula1);
					break;
				default:
					break;
				}

			});

			if(inboundRes.getEngNm()==null||inboundRes.getEngNm().equals("")) {
				
			}else {
//				if(inboundRes.getBoxCount()==null||inboundRes.getBoxCount().equals("")) {
//					inboundRes.setBoxCount(new Double(0));
//				}
				if(inboundRes.getReportPrice()==null||inboundRes.getReportPrice().equals("")) {
					inboundRes.setReportPrice(null);
				}
				list.add(inboundRes);
			}
			
		}
		list = excelUploadProcess03(list);
		return list;
	}

	public void excelUploadProcess02(int cellIndex, String value, InboundRes inboundRes) throws ParseException {
		// cellIndex는 1개 row의 순차적 cell 의미 , value는 cellIndex의 value
		// commonid 입력값 없으면 insert 후보
		
		if (cellIndex == 0) {
			
			inboundRes.setOrderNoStr(value);
			
		}
		if (cellIndex == 1) {

			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
			format1.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			format2.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
			Date d=null;
			d=format1.parse(value);
			String b= format2.format(d);
			inboundRes.setWorkDateStr(b);
			
		}
		
		if (cellIndex == 2) {

			inboundRes.setCompanyNm(value);
		}
		if (cellIndex == 3) {
			inboundRes.setMarking(value);
		}
		if (cellIndex == 4) {
			inboundRes.setKorNm(value);
		}
		if (cellIndex == 5) {
			inboundRes.setItemCount(new Double(value));
		}
		if (cellIndex == 6) {
			inboundRes.setBoxCount(new Double(value));
		}
		if (cellIndex == 7) {
			inboundRes.setWeight(new Double(value));
		}	
		if (cellIndex == 8) {
			inboundRes.setCbm(new Double(value));
		}
		if (cellIndex == 9) {
			inboundRes.setReportPrice(new Double(value));
		}
		if (cellIndex == 10) {
			inboundRes.setMemo1(value);
		}
		if (cellIndex == 11) {
			inboundRes.setMemo2(value);
		}
//		if (cellIndex == 12) {
//			inboundRes.setMemo3(value);
//		}
		if (cellIndex == 12) {
			inboundRes.setItemNo(value);
		}
		if (cellIndex == 13) {
			inboundRes.setJejil(value);
		}
		if (cellIndex == 14) {
			inboundRes.setHsCode(value);
		}
		if (cellIndex == 15) {
			inboundRes.setCoCode(value);
		}
		if (cellIndex == 16) {
				inboundRes.setEngNm(value);	
		}
		if (cellIndex == 17) {
			inboundRes.setColor(value);
		}
		if(cellIndex == 18) {
			inboundRes.setAmountType(value);
		}
		if(cellIndex == 19) {
			inboundRes.setBackgroundColor(value);
		}
		
	}
	
	public List<InboundRes> excelUploadProcess03(List<InboundRes> list) {
		// cellIndex는 1개 row의 순차적 cell 의미 , value는 cellIndex의 value
		// commonid 입력값 없으면 insert 후보
			
		Function<InboundRes, InboundRes> fn = (item)->{
			
			//박수 무게 계산 박스 수 * 무게상수
//			Double d = new Double(0);
//			if(item.getBoxCount() != null) {
//				d = new Double(String.format("%.3f", new Double(item.getBoxCount() * item.getWeight())));	
//			}else {
//				
//			}
			
//			item.setWeight(d);
			

			
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);
			
			if(item.getCoCode() == null) {
				item.setCoId(Long.valueOf(3));
			}else {
				
				boolean isExist = CoType.getList().stream().filter(tt->tt.getName().equals(item.getCoCode())).findFirst().isPresent();
				
				if(isExist) {
//				if(null == CoType.getList().stream().filter(tt->tt.getName().equals(item.getCoCode())).findFirst().orElseGet(null)) {
					item.setCoId(Long.valueOf(CoType.getList().stream().filter(tt->tt.getName().equals(item.getCoCode())).findFirst().get().getId()));
				}else {
					item.setCoId(Long.valueOf(3));	
				}
//				Long coId = Long.valueOf(CoType.getList().stream().filter(tt->tt.getName().equals(item.getCoCode())).findFirst().get();
//				
//				item.setCoId(Long.valueOf(CoType.getList().stream().filter(tt->tt.getName().equals(item.getCoCode())).findFirst().ifPresent(t->). );
			}
			
			if(item.getAmountType() == null) {
				item.setAmountType("PCS");
			}else {
				
				boolean isExist = AmountType.getList().stream().filter(tt->tt.getName().equals(item.getAmountType())).findFirst().isPresent();
				
				if(isExist) {
					item.setAmountType(AmountType.getList().stream().filter(tt->tt.getName().equals(item.getAmountType())).findFirst().get().getName());
				}else {
					item.setAmountType("PCS");
				}
			}
			if(item.getBackgroundColor() == null) {
				item.setBackgroundColor("WHITE");
			}else {
				
				item.setBackgroundColor("SALGU");			
			}
					
			//cbm 문자 곱 * 박스 갯수
			if(item.getCbmStr() == null || item.getCbmStr().equals("")) {
				
			}else{
				String items[] = item.getCbmStr().split("\\*");
				List<Double> t= Stream.of(items)
						.map(t_item -> new Double("0."+t_item))
						.collect(Collectors.toList());
//						;
//				System.out.println(t);
				Double r = t.stream().reduce( (a,b)->a*b).get();
			
				item.setCbm(r * item.getBoxCount());
			}
			
			//total  = 수량 * 신고단가 
			//소수 2째자리 
//			item.setReportPrice(new Double(String.format("%.2f",item.getReportPrice() * item.getItemCount())));
			if(item.getReportPrice() == null || item.getReportPrice().equals("")||item.getItemCount()==null||item.getItemCount().equals("")) {
				item.setReportPrice(null);
				item.setTotalPrice(null);
			}else {
				Double reportPrice = item.getReportPrice();
//				Double result =  Math.round(reportPrice*1000)/1000.0;
				item.setReportPrice(reportPrice);
				item.setTotalPrice(item.getItemCount()*item.getReportPrice());
			}
			
			
			return item;
			
			
			
		};
		
		list = list.stream().map(fn).collect(Collectors.toList());
		;
		
		return list;
	
	}
	
	@Override
	public InboundViewRes changeInbound(List<InboundRes> list)  {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		DecimalFormat decimalFormat3 = new DecimalFormat("#,##0.00");
		InboundViewRes inboundViewRes= new InboundViewRes();
		if(list.size() > 0) {
			Double itemCountSum= (list.get(0).getItemCountSum() == null ? 0d :list.get(0).getItemCountSum());
			Double boxCountSum=(list.get(0).getBoxCountSum());
			Double weightSum=(list.get(0).getWeightSum() == null ? 0d : list.get(0).getWeightSum());
			Double cbmSum = (list.get(0).getCbmSum() == null ? 0d : list.get(0).getCbmSum());
			Double totalPriceSum = (list.get(0).getTotalPriceSum() == null ? null :list.get(0).getTotalPriceSum()) ;		
			
			
			
			inboundViewRes.setCurrencyType(list.get(0).getCurrencyType());
			inboundViewRes.setItemCountSumD(itemCountSum);
			inboundViewRes.setBoxCountSumD(boxCountSum);
			inboundViewRes.setCbmSumD(cbmSum);
			inboundViewRes.setWeightSumD(weightSum);;
			inboundViewRes.setItemCountSum(getStringResult(itemCountSum));
			inboundViewRes.setBoxCountSum(getStringResult(boxCountSum));
			inboundViewRes.setWeightSum(getStringResult(weightSum));
			inboundViewRes.setCbmSum(decimalFormat.format(cbmSum));
			if(totalPriceSum==null) {
				inboundViewRes.setTotalPriceSum(" ");
			}else if(totalPriceSum==0) {
				inboundViewRes.setTotalPriceSum("-");
			}else {
				inboundViewRes.setTotalPriceSum(decimalFormat.format(totalPriceSum));
			}
			
			inboundViewRes.setFreight(list.get(0).getFreight());
			inboundViewRes.setManagerNm(list.get(0).getManagerNm());
			
			
			inboundViewRes.setInbounds(list.stream().map(sub_item -> {
					Map<String, Object> f = new HashMap<>();
//					
					if(sub_item.getForViewWorkDate()!= null) {
						f.put("forViewWorkDate", sub_item.getForViewWorkDate());
					}
					if(sub_item.getForViewWorkDateStr()!= null) {
						f.put("forViewWorkDateStr", sub_item.getForViewWorkDateStr());
					}
					if(sub_item.getOrderNoStr()!= null) {
						f.put("orderNoStr", sub_item.getOrderNoStr());
					}
					if(sub_item.getWorkDateStr()!= null) {
						f.put("workDateStr", sub_item.getWorkDateStr());
					}
					if(sub_item.getCompanyNm()!= null) {
						f.put("companyNm", sub_item.getCompanyNm());
					}
					if( sub_item.getMasterExport()!= null) {
						f.put("masterExport", sub_item.getMasterExport());
					}
					if( sub_item.getMasterExportAddr()!= null) {
						f.put("masterExportAddr", sub_item.getMasterExportAddr());
					}
					if( sub_item.getMasterCompany()!= null) {
						f.put("masterCompany", sub_item.getMasterCompany());
					}
					if( sub_item.getMasterCompanyNumber()!= null) {
						f.put("masterCompanyNumber", sub_item.getMasterCompanyNumber());
					}
					if( sub_item.getBlNo()!= null) {
						f.put("blNo", sub_item.getBlNo());
					}
				
					f.put("orderNo", sub_item.getOrderNo());
					f.put("korNm", sub_item.getKorNm());
					
					f.put("itemCount",  getStringResult(sub_item.getItemCount()));
					
					
//					if(sub_item.getBoxCount() == null||sub_item.getBoxCount() == 0) {
//						f.put("boxCount",  "0");
//					}else {
//						f.put("boxCount", getStringResult(sub_item.getBoxCount()));
//					}
//					
//					if(sub_item.getWeight() == null) {
//						f.put("weight",  "0");
//					}else {
//						f.put("weight", getStringResult(sub_item.getWeight()));	
//					}
//					
//					if(sub_item.getCbm() == null||sub_item.getCbm() == 0) {
//						f.put("cbm",  "0");
//					}else {
////						f.put("cbm", decimalFormat.format(sub_item.getCbm()));	
//						f.put("cbm", getStringResult2(sub_item.getCbm()));	
//					}
					//20220821
					
					
					if(sub_item.getBoxCount() == null) {
						f.put("boxCount",  "");
					}else {
						f.put("boxCount", getStringResult(sub_item.getBoxCount()));
					}
					
					if(sub_item.getWeight() == null) {
						f.put("weight",  "");
					}else {
						f.put("weight", getStringResult(sub_item.getWeight()));	
					}
					
					if(sub_item.getCbm() == null) {
						f.put("cbm",  "");
					}else {
						f.put("cbm", decimalFormat.format(sub_item.getCbm()));	
//						f.put("cbm", getStringResult2(sub_item.getCbm()));	
					}
					//20220821	
					
					
					
					if(sub_item.getAmountType()==null||sub_item.getAmountType().equals("PCS")) {
						f.put("amountType",  "");
					}else {
						f.put("amountType", sub_item.getAmountType());	
					}
					if(sub_item.getCurrencyType()==null) {
						f.put("currencyType",  "$");
					}else {
						f.put("currencyType", sub_item.getCurrencyType());	
					}
					
					
					//0518
//					f.put("reportPrice", decimalFormat.format(sub_item.getReportPrice()));
//					f.put("totalPrice", decimalFormat.format(sub_item.getTotalPrice()));
					
					
					if(sub_item.getReportPrice() == null) {
						f.put("reportPrice",  " ");
					}else if(sub_item.getReportPrice() == 0) {
						f.put("reportPrice", "-");
					}else {
						f.put("reportPrice", decimalFormat.format(sub_item.getReportPrice()));
					}
					if(sub_item.getTotalPrice() == null) {
						f.put("totalPrice",  " ");
					}else if(sub_item.getTotalPrice() == 0) {
						f.put("totalPrice", "-");
					}else {
						f.put("totalPrice", decimalFormat.format(sub_item.getTotalPrice()));
					}
					
					
					if(sub_item.getMemo1()!= null) {
						f.put("memo1", sub_item.getMemo1());
					}
					if(sub_item.getMemo2()!= null) {
						f.put("memo2", sub_item.getMemo2());
					}
					if(sub_item.getMemo3()!= null) {
						f.put("memo3", sub_item.getMemo3());
					}
								
					f.put("itemNo", sub_item.getItemNo());
					f.put("hsCode", sub_item.getHsCode());
					if(sub_item.getCoCode()!= null) {
						f.put("coCode", sub_item.getCoCode());
					}
					if(sub_item.getColorCode()!= null) {
						f.put("colorCode", sub_item.getColorCode());
					}
					if(sub_item.getBackgroundColorCode()!= null) {
						f.put("backgroundColorCode", sub_item.getBackgroundColorCode());
					}
					if(sub_item.getMarking()!= null) {
						f.put("marking", sub_item.getMarking());
					}
					if(sub_item.getJejil()!= null) {
						f.put("jejil", sub_item.getJejil());
					}
					
					
					f.put("engNm", sub_item.getEngNm());
					
					f.put("companyNmSpan", sub_item.getCompanyNmSpan());
					f.put("markingSpan", sub_item.getMarkingSpan());
					f.put("korNmSpan", sub_item.getKorNmSpan());
					f.put("itemCountSpan", sub_item.getItemCountSpan());
					f.put("boxCountSpan", sub_item.getBoxCountSpan());
					f.put("weightSpan", sub_item.getWeightSpan());
					f.put("cbmSpan", sub_item.getCbmSpan());
					f.put("reportPriceSpan", sub_item.getReportPriceSpan());
					f.put("memo1Span", sub_item.getMemo1Span());
					f.put("memo2Span", sub_item.getMemo2Span());
					f.put("memo3Span", sub_item.getMemo3Span());
					f.put("itemNoSpan", sub_item.getItemNoSpan());
					f.put("hsCodeSpan", sub_item.getHsCodeSpan());
					f.put("workDateSpan", sub_item.getWorkDateSpan());
					f.put("blNoSpan", sub_item.getBlNoSpan());
					f.put("masterCompanySpan", sub_item.getMasterCompanySpan());
					f.put("masterExportSpan", sub_item.getMasterExportSpan());
					f.put("exportNmSpan", sub_item.getExportNmSpan());
					f.put("coCodeSpan", sub_item.getCoCodeSpan());
					f.put("coIdSpan", sub_item.getCoIdSpan());
					f.put("totalPriceSpan", sub_item.getTotalPriceSpan());
					f.put("engNmSpan", sub_item.getEngNmSpan());
					f.put("workDateStrSpan", sub_item.getWorkDateStrSpan());
					f.put("orderNoStrSpan", sub_item.getOrderNoStrSpan());
					f.put("jejelSpan", sub_item.getJejilSpan());
					if(sub_item.getCompanyNmYn()==null||sub_item.getCompanyNmYn().equals("")) {
						f.put("companyNmYn", "");
					}else {
						f.put("companyNmYn", sub_item.getCompanyNmYn());
					}
					if(sub_item.getMarkingYn()==null||sub_item.getMarkingYn().equals("")) {
						f.put("markingYn", "");
					}else {
						f.put("markingYn", sub_item.getMarkingYn());
					}
					if(sub_item.getWorkDateStrYn()==null||sub_item.getWorkDateStrYn().equals("")) {
						f.put("workDateStrYn", "");
					}else {
						f.put("workDateStrYn", sub_item.getWorkDateStrYn());
					}
					if(sub_item.getKorNmYn()==null||sub_item.getKorNmYn().equals("")) {
						f.put("korNmYn", "");
					}else {
						f.put("korNmYn", sub_item.getKorNmYn());
					}
					if(sub_item.getItemCountYn()==null||sub_item.getItemCountYn().equals("")) {
						f.put("itemCountYn", "");
					}else {
						f.put("itemCountYn", sub_item.getItemCountYn());
					}
					if(sub_item.getBoxCountYn()==null||sub_item.getBoxCountYn().equals("")) {
						f.put("boxCountYn", "");
					}else {
						f.put("boxCountYn", sub_item.getBoxCountYn());
					}
					if(sub_item.getWeightYn()==null||sub_item.getWeightYn().equals("")) {
						f.put("weightYn", "");
					}else {
						f.put("weightYn", sub_item.getWeightYn());
					}
					if(sub_item.getCbmYn()==null||sub_item.getCbmYn().equals("")) {
						f.put("cbmYn", "");
					}else {
						f.put("cbmYn", sub_item.getCbmYn());
					}
					if(sub_item.getReportPriceYn()==null||sub_item.getReportPriceYn().equals("")) {
						f.put("reportPriceYn", "");
					}else {
						f.put("reportPriceYn", sub_item.getReportPriceYn());
					}
					if(sub_item.getMemo1Yn()==null||sub_item.getMemo1Yn().equals("")) {
						f.put("memo1Yn", "");
					}else {
						f.put("memo1Yn", sub_item.getMemo1Yn());
					}
					if(sub_item.getMemo2Yn()==null||sub_item.getMemo2Yn().equals("")) {
						f.put("memo2Yn", "");
					}else {
						f.put("memo2Yn", sub_item.getMemo2Yn());
					}
					if(sub_item.getItemNoYn()==null||sub_item.getItemNoYn().equals("")) {
						f.put("itemNoYn", "");
					}else {
						f.put("itemNoYn", sub_item.getItemNoYn());
					}
					if(sub_item.getJejilYn()==null||sub_item.getJejilYn().equals("")) {
						f.put("jejilYn", "");
					}else {
						f.put("jejilYn", sub_item.getJejilYn());
					}
					if(sub_item.getHsCodeYn()==null||sub_item.getHsCodeYn().equals("")) {
						f.put("hsCodeYn", "");
					}else {
						f.put("hsCodeYn", sub_item.getHsCodeYn());
					}
					if(sub_item.getTotalPriceYn()==null||sub_item.getTotalPriceYn().equals("")) {
						f.put("totalPriceYn", "");
					}else {
						f.put("totalPriceYn", sub_item.getTotalPriceYn());
					}
					if(sub_item.getEngNmYn()==null||sub_item.getEngNmYn().equals("")) {
						f.put("engNmYn", "");
					}else {
						f.put("engNmYn", sub_item.getEngNmYn());
					}
					if(sub_item.getCoIdYn()==null||sub_item.getCoIdYn().equals("")) {
						f.put("coIdYn", "");
					}else {
						f.put("coIdYn", sub_item.getCoIdYn());
					}
					
					
					return f;
				}).collect(Collectors.toList()));
		}else {
			
		}

		
			
		 return inboundViewRes;
	}
	@Override
	public InboundViewRes changeInbound2(List<InboundRes> list)  {
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		DecimalFormat decimalFormat3 = new DecimalFormat("#,##0.00");
		InboundViewRes inboundViewRes= new InboundViewRes();
		if(list.size() > 0) {
			Double itemCountSum= (list.get(0).getItemCountSum() == null ? 0d :list.get(0).getItemCountSum());
			Double boxCountSum=(list.get(0).getBoxCountSum());
			Double weightSum=(list.get(0).getWeightSum() == null ? 0d : list.get(0).getWeightSum());
			Double cbmSum = (list.get(0).getCbmSum() == null ? 0d : list.get(0).getCbmSum());
			Double totalPriceSum = (list.get(0).getTotalPriceSum() == null ? null :list.get(0).getTotalPriceSum()) ;		
			
			
			
			
			inboundViewRes.setItemCountSumD(itemCountSum);
			inboundViewRes.setBoxCountSumD(boxCountSum);
			inboundViewRes.setCbmSumD(cbmSum);
			inboundViewRes.setWeightSumD(weightSum);;
			inboundViewRes.setItemCountSum(getStringResult(itemCountSum));
			inboundViewRes.setBoxCountSum(getStringResult(boxCountSum));
			inboundViewRes.setWeightSum(decimalFormat2.format(weightSum));
			inboundViewRes.setCbmSum(decimalFormat.format(cbmSum));
			if(totalPriceSum==null) {
				inboundViewRes.setTotalPriceSum(" ");
			}else if(totalPriceSum==0) {
				inboundViewRes.setTotalPriceSum("-");
			}else {
				inboundViewRes.setTotalPriceSum(decimalFormat.format(totalPriceSum));
			}
			
			inboundViewRes.setFreight(list.get(0).getFreight());
			inboundViewRes.setManagerNm(list.get(0).getManagerNm());
			inboundViewRes.setInboundsForPreview(list);
			
		}

		else {
			
		}

		
			
		 return inboundViewRes;
	}	
	public String getStringResult(Double param) {
		try {
			boolean finalCheck = false;
			String[] arr = String.valueOf(param).split("\\.");
			char[] texts = arr[1].toCharArray();
			Boolean checkPoint1[] = new Boolean[texts.length];
			
			for(int i = 0; i<texts.length;i++) {
//				System.out.println(texts[i]);
				if(texts[i] == '0' ) {
					checkPoint1[i] = true;
				}else {
					checkPoint1[i] = false;
				}
			}
			
			for(int i = 0; i<checkPoint1.length;i++) {
				if(i == 0 ) {
					finalCheck = checkPoint1[i];
					
				}else {
					finalCheck = (finalCheck && checkPoint1[i]);
				}
			}
			
			
			if(finalCheck) {
				DecimalFormat decimalFormat = new DecimalFormat("#,###");
				return decimalFormat.format(Double.parseDouble(arr[0])) ;
			}else {
				DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
				return decimalFormat.format(param);
			}
		}catch (Exception e) {
			return String.valueOf(param);
		}
		
	}
	public String getStringResult2(Double param) {
		try {
			boolean finalCheck = false;
			String[] arr = String.valueOf(param).split("\\.");
			char[] texts = arr[1].toCharArray();
			Boolean checkPoint1[] = new Boolean[texts.length];
			
			for(int i = 0; i<texts.length;i++) {
//				System.out.println(texts[i]);
				if(texts[i] == '0' ) {
					checkPoint1[i] = true;
				}else {
					checkPoint1[i] = false;
				}
			}
			
			for(int i = 0; i<checkPoint1.length;i++) {
				if(i == 0 ) {
					finalCheck = checkPoint1[i];
					
				}else {
					finalCheck = (finalCheck && checkPoint1[i]);
				}
			}
			
			
			if(finalCheck) {
				DecimalFormat decimalFormat = new DecimalFormat("#,###");
				return decimalFormat.format(Double.parseDouble(arr[0])) ;
			}else {
				DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
				return decimalFormat.format(param);
			}
		}catch (Exception e) {
			return String.valueOf(param);
		}
		
	}

}