package com.keepgo.whatdo.service.finalInbound.impl;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
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
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.keepgo.whatdo.controller.FinalInbound.FinalInboundSpecification;
import com.keepgo.whatdo.define.AmountType;
import com.keepgo.whatdo.define.BackgroundColorType;
import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.define.ColorType;
import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.entity.customs.CheckImport;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
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
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.repository.CheckImportRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.repository.UserRepository;
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
	
	@Autowired
	
	InboundRepository _inboundRepository;
	
	@Autowired
	CheckImportRepository _checkImportRepository;
	
	@Autowired
	UserRepository _userRepository;

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
				.silNo(req.getSilNo()).cargoName(req.getCargoName()).departPort(req.getDepartPortId())
				.hangCha(req.getHangCha()).containerSizeStr(req.getContainerSizeStr())
				.weatherCondition(req.getWeatherCondition()).memo(req.getMemo())

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
	public boolean deleteFinalInbound(FinalInboundReq finalInboundReq) {
		List<Long> finalInboundList = finalInboundReq.getIds();
		for (int i = 0; i < finalInboundList.size(); i++) {
			FinalInbound finalInbound = _finalInboundRepository.findById(finalInboundList.get(i).longValue()).get();
			_finalInboundRepository.delete(finalInbound);
		}
		return true;
	}
	@Override
	public FinalInboundRes addFinalInbound(FinalInboundReq req) throws ParseException {
//		String depart=req.getDepartDtStr();
//		String income=req.getIncomeDt();
		SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyymmdd");
	
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-MM-dd");
		Date tempDate = req.getDepartDate();
		Date tempDate2 = req.getIncomeDate();
		Date tempDate3 =new Date();
//		tempDate = beforeFormat.parse(depart);
//		tempDate2 = beforeFormat.parse(income);
		
		FinalInbound target = FinalInbound.builder()
				.title(req.getTitle())
				.gubun(req.getGubun())
				.corpId((req.getCorpId()==null||req.getCorpId()==0 ? 1 : req.getCorpId() ))
				.incomeDt(afterFormat.format(tempDate2))
				.departDtStr(afterFormat.format(tempDate))
				.workDepartDt(afterFormat.format(tempDate3))
				.incomePort(new Long(1))
				.departPort(new Long(1))
				// todo 사용자 세션 아이디로 수정해야됨.
				.user(User.builder().id(new Long(1)).build())

				.isUsing(true).createDt(new Date()).updateDt(new Date())

				.build();
		target = _finalInboundRepository.save(target);
		
		
		FinalInboundRes result = FinalInboundRes
				.builder()
				.id(target.getId())
//				.title(target.getTitle())
				.title(target.getTitle())
				.build();		

		return result;
//		return FinalInboundRes.builder().id(_finalInboundRepository.findById(target.getId()).ti findByTitle(req.getTitle()).getId()).build();
//		return FinalInboundRes.builder().id(_finalInboundRepository.findByTitle(req.getTitle()).getId()).build();
	}
	
	@Override
	public boolean updateFinalInbound(FinalInboundReq req) {
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat afterFormat2 = new SimpleDateFormat("MM월 dd일");
		Date tempDate = req.getDepartDate();
		Date tempDate2 = req.getIncomeDate();
		Date tempDate3 = req.getWorkDepartDate();
		
		
		FinalInbound finalInbound = _finalInboundRepository.findById(req.getId())
				.orElse(FinalInbound.builder().build());
		
		finalInbound.setId(req.getId());
		finalInbound.setIncomeDt(afterFormat.format(tempDate2));
		finalInbound.setDepartDtStr(afterFormat.format(tempDate));
		
		finalInbound.setCargoName(req.getCargoName());
		finalInbound.setDepartPort(req.getDepartPortId());
		finalInbound.setIncomePort(req.getIncomePortId());
		finalInbound.setFinalMasterBl(req.getFinalMasterBl());
		finalInbound.setContainerNo(req.getContainerNo());
		finalInbound.setSilNo(req.getSilNo());
		finalInbound.setHangName(req.getHangName());
		finalInbound.setHangCha(req.getHangCha());
		finalInbound.setGubun(req.getGubun());	
		finalInbound.setChinaSanggumYn(req.getChinaSanggumYn());
		finalInbound.setDepartDelayYn(req.getDepartDelayYn());
		finalInbound.setGwanriYn(req.getGwanriYn());;
		
		finalInbound.setContainerSizeStr(req.getContainerSizeStr());
		finalInbound.setContainerCost(req.getContainerCost());
		finalInbound.setWeatherCondition(req.getWeatherCondition());
		finalInbound.setWorkDepartDt(afterFormat.format(tempDate3));
		finalInbound.setDeliveryNm(req.getDeliveryNm());
		finalInbound.setChulhangPort(req.getChulhangPort());		
		finalInbound.setMemo(req.getMemo());		
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
		
		List<InboundMasterReq> list = req.getInboundMasterData();
		for (int i = 0; i < list.size(); i++) {

			

			Long inboundMasterId=list.get(i).getId();
			InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterId).get();
			
			List<Inbound> inboundList = _inboundRepository.findByInboundMasterId(inboundMasterId);
			for (int j = 0; j < inboundList.size(); j++) {
				_inboundRepository.delete(inboundList.get(j));
			}
			CheckImport checkImport = _checkImportRepository.findByInboundMasterId(inboundMasterId);
			if(checkImport!=null) {
				_checkImportRepository.delete(checkImport);
			}
			
			_inboundMasterRepository.delete(inboundMaster);
			_finalInboundInboundMasterRepository.deleteFinalInboundInboundMaster(req.getId(), inboundMasterId);

		}
		
		
		
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
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd");
		
		Date d=null;
		Date d2=null;
		String forViewDepartDateStr="";
		String workDepartDt="";
		try {
			d=format1.parse(r.getDepartDtStr());
			forViewDepartDateStr = DateFormatUtils.format(d, "MM월 dd일");
			if(r.getWorkDepartDt()!=null) {
				d2=format1.parse(r.getWorkDepartDt());
				workDepartDt=DateFormatUtils.format(d2, "MM월 dd일");
			}
			
			
			
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		FinalInboundRes dto = 
				
				FinalInboundRes.builder()

				
				.id(r.getId())
				.incomeDt(r.getIncomeDt())
				.cargoName(r.getCargoName())
				.finalMasterBl(r.getFinalMasterBl())
				.gubun(GubunType.getList().stream().filter(type->type.getId() == r.getGubun()).findFirst().get().getName())
				.gubunCode(r.getGubun())
				.containerNo(r.getContainerNo())
				.containerPrice(r.getContainerPrice())
				.silNo(r.getSilNo())
				
				.hangName(r.getHangName())
				.hangCha(r.getHangCha())
				.chinaSanggumYn(r.getChinaSanggumYn())
				.departDelayYn(r.getDepartDelayYn())
				.gwanriYn(r.getGwanriYn())
				.departDtStr(r.getDepartDtStr())
				.forViewDepartDateStr(forViewDepartDateStr)
				.title(r.getTitle())
				.unbiDefine1(r.getUnbiDefine1())
				.unbiDefine2(r.getUnbiDefine2())
				.unbiDefine3(r.getUnbiDefine3())
				.unbiDefine4(r.getUnbiDefine4())
				
				.containerSizeStr(r.getContainerSizeStr())
				.containerCost(r.getContainerCost()).containerCost(r.getContainerCost())
				.weatherCondition(r.getWeatherCondition())
				.deliveryNm(r.getDeliveryNm())
				.workDepartDt(r.getWorkDepartDt())
				.forViewWorkDepartDt(workDepartDt)
				.chulhangPort(r.getChulhangPort())				
				.createDt(r.getCreateDt())
				.updateDt(r.getUpdateDt())
				.corpId(r.getCorpId())
				.build();
		dto.setDepartPort((r.getDepartPort() == null ? "" : _commonRepository.findById(r.getDepartPort()).get().getValue2()));
		dto.setDepartPortId((r.getDepartPort() == null ? new Long(1) : r.getDepartPort()));
		dto.setIncomePort((r.getIncomePort() == null ? "" : _commonRepository.findById(r.getIncomePort()).get().getValue2()));
		dto.setIncomePortId((r.getIncomePort() == null ? new Long(1) : r.getIncomePort()));
		dto.setMemo((r.getMemo() == null ? "" : r.getMemo()));
		if(r.getInboundMasters().size()>0){
			dto.setInboundMasters(r.getInboundMasters().stream()
					
					.sorted(Comparator.comparing(FinalInboundInboundMaster::getPreperOrder ))
					.map(sub_item -> {
				Map<String, Object> f = new HashMap<>();
//				f.put("workDate", sub_item.getInboundMaster().getWorkDate());
				if(sub_item.getInboundMaster().getWorkDate()!= null) {
					f.put("workDate", format1.format(sub_item.getInboundMaster().getWorkDate()));	
				}
				f.put("blNo", sub_item.getInboundMaster().getBlNo());
				if(sub_item.getInboundMaster().getCompanyInfo()!= null) {
					f.put("coNum", sub_item.getInboundMaster().getCompanyInfo().getCoNum());
					f.put("companyNm", sub_item.getInboundMaster().getCompanyInfo().getCoNm());
				}
				
				
				f.put("id", sub_item.getInboundMaster().getId());

				return f;
			}).collect(Collectors.toList()));
		}
		
				
				

		return dto;
	}

	
	
	@Override
	public List<FinalInboundRes> getAllCondition(FinalInboundReq req) {
		
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("incomeDt", req.getIncomeDt());
		condition.put("incomeYearMonth", req.getIncomeYearMonth());
		condition.put("departDtStr", req.getDepartDtStr());
		condition.put("gubun", req.getGubun());
		
		List<FinalInboundRes>  r = _finalInboundRepository.findAll(FinalInboundSpecification.withCondition(condition))
				.stream()
				.sorted(Comparator.comparing(FinalInbound::getIncomeDt))
				.map(t->{
					
						
					
					return  FinalInboundRes.builder()
							.id(t.getId())
							.gubun(GubunType.getList().stream().filter(type->type.getId() == t.getGubun()).findFirst().get().getName())
							.gubunCode(t.getGubun())
							.departDtStr(t.getDepartDtStr())
							.title(t.getTitle())
							.incomeDt(t.getIncomeDt())
							.build();
				}).collect(Collectors.toList());

		return r;
	}
	@Override
	public List<FinalInboundRes> getFIList(FinalInboundReq req) {
		
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat afterFormat2 = new SimpleDateFormat("MM월 dd일");
		Date tempDate = req.getStartDate();
		Date tempDate2 = req.getEndDate();
		String startDt = afterFormat.format(tempDate);
		String endDt =  afterFormat.format(tempDate2);
		
		if(req.getDateType()==null||req.getDateType()==0) {
			List<FinalInboundRes>  r = _finalInboundRepository.findByDepartDtStrBetween(startDt,endDt)
					.stream()
					.sorted(Comparator.comparing(FinalInbound::getDepartDtStr))
					.map(t->{
						
							
						
						return  FinalInboundRes.builder()
								.id(t.getId())
								.gubun(GubunType.getList().stream().filter(type->type.getId() == t.getGubun()).findFirst().get().getName())
								.gubunCode(t.getGubun())
								.departDtStr(t.getDepartDtStr())
								.title(t.getTitle())
								.incomeDt(t.getIncomeDt())
								.build();
					}).collect(Collectors.toList());

			return r;
		}else {
			List<FinalInboundRes>  r = _finalInboundRepository.findByIncomeDtBetween(startDt,endDt)
					.stream()
					.sorted(Comparator.comparing(FinalInbound::getIncomeDt))
					.map(t->{
						
							
						
						return  FinalInboundRes.builder()
								.id(t.getId())
								.gubun(GubunType.getList().stream().filter(type->type.getId() == t.getGubun()).findFirst().get().getName())
								.gubunCode(t.getGubun())
								.departDtStr(t.getDepartDtStr())
								.title(t.getTitle())
								.incomeDt(t.getIncomeDt())
								.build();
					}).collect(Collectors.toList());

			return r;
		}
		
		
	}
	
	@Override
	public boolean excelRead(MultipartFile file, InboundReq Req, String id, String loginId) throws IOException {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}
		XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
		
		//시트갯수
		int sheetNum = workbook.getNumberOfSheets();
		
		// 첫번째 시트
		int orderNo = 1;
		
		
		//List<InboundRes> list = new ArrayList<>();
		
		for(int j=0;j<sheetNum;j++) {
			//시트이름
			List<Inbound> list = new ArrayList<>();
			String sheetName = workbook.getSheetAt(j).getSheetName();
			InboundMaster target = InboundMaster.builder()
					.blNo(sheetName)
					

					// todo 사용자 세션 아이디로 수정해야됨.
					.user(User.builder().id(new Long(1)).build())
					.freight(1)
					.currencyType("$")
					.packingType("CTN")
					.isUsing(true).createDt(new Date()).updateDt(new Date())

					.build();
//			User user = _userRepository.findByLoginId(loginId);
//			target.setUser(user);
			target = _inboundMasterRepository.save(target);
			
			
			FinalInbound finalInbound = _finalInboundRepository.findById(Long.valueOf(id)).orElse(FinalInbound.builder().build());
//			InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterId).orElse(InboundMaster.builder().build());
			FinalInboundInboundMaster finalInboundInboundMaster= new FinalInboundInboundMaster();
			
			
			finalInboundInboundMaster.setFinalInbound(finalInbound);
			finalInboundInboundMaster.setInboundMaster(target);
			finalInboundInboundMaster.setPreperOrder(j+1);
			

			_finalInboundInboundMasterRepository.save(finalInboundInboundMaster);
			
			
			//시트
			XSSFSheet sheet = workbook.getSheetAt(j);
			// 마지막 row 숫자
			int rowCount = sheet.getLastRowNum();
			// i=1 rowCount+1 해야 마지막 row 데이터까지 읽어옴
			for (int i = 1; i < rowCount + 1; i++) {
				XSSFRow row = sheet.getRow(i);
//				boolean validation = true;
				Inbound inbound = new Inbound();
				row.cellIterator().forEachRemaining(cell -> {
					int cellIndex = cell.getColumnIndex();
					switch (cell.getCellTypeEnum().name()) {

					case "STRING":
						String string1 = cell.getStringCellValue();
						try {
							excelUploadProcess02(cellIndex, string1, inbound);
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
							excelUploadProcess02(cellIndex, numberToString, inbound);
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
				
				
				if(inbound.getEngNm()==null||inbound.getEngNm().equals("")) {
					
				}else {
					if(inbound.getColorName()!=null) {
						for(int k=0; k<ColorType.getList().size();k++) {
							if(ColorType.getList().get(k).getShowName().equals(inbound.getColorName())) {
								inbound.setColor(ColorType.getList().get(k).getId());
							}else {

							}
						}
						
					}else {
						inbound.setColor(Integer.valueOf(0));
					}
					
					if(inbound.getBackgroundColorName()!=null) {
//						for(int k=0; k<BackgroundColorType.getList().size();k++) {
//							if(BackgroundColorType.getList().get(k).getShowName().equals(inbound.getBackgroundColorName())) {
//								inbound.setBackgroundColor(BackgroundColorType.getList().get(k).getId());
//							}else {
//
//							}
//						}
						inbound.setBackgroundColor(Integer.valueOf(1));
					}else {
						inbound.setBackgroundColor(Integer.valueOf(0));
					}
					
					
					
					if(inbound.getCoCode()!=null) {
						for(int k=0; k<CoType.getList().size();k++) {
							if(CoType.getList().get(k).getName().equals(inbound.getCoCode())) {
								inbound.setCoId(CoType.getList().get(k).getId());
							}else {

							}
						}
						
					}else if(inbound.getCoCode()==null||inbound.getCoCode().equals("")) {
						inbound.setCoId(Integer.valueOf(3));
					}else {
						inbound.setCoId(Integer.valueOf(3));
					}
					
					if(inbound.getAmountType()!=null) {
						for(int k=0; k<AmountType.getList().size();k++) {
							if(AmountType.getList().get(k).getName().equals(inbound.getAmountType())) {
								inbound.setAmountType(AmountType.getList().get(k).getName());
							}else {

							}
						}
						
					}else if(inbound.getAmountType()==null||inbound.getAmountType().equals("")){
						inbound.setAmountType("PCS");
					}else {
						inbound.setAmountType("PCS");
					}
//					if(inbound.getBoxCount()==null||inbound.getBoxCount().equals("")) {
//						inbound.setBoxCount(new Double(0));
//					}
					// 20220706
					if(inbound.getItemCount()==null||inbound.getItemCount().equals("")) {
					inbound.setItemCount(new Double(0));
				}
					
					
//					if(inbound.getReportPrice()==null||inbound.getReportPrice().equals("")) {
//						inbound.setReportPrice(new Double(0));
//					}
//					
//					inbound.setTotalPrice(inbound.getItemCount()*inbound.getReportPrice());
					if(inbound.getReportPrice()==null||inbound.getReportPrice().equals("")||inbound.getItemCount()==null||inbound.getItemCount().equals("")) {
						inbound.setReportPrice(null);
						inbound.setTotalPrice(null);
					}else {
						Double reportPrice = inbound.getReportPrice();
						Double result =  Math.round(reportPrice*1000)/1000.0;
						inbound.setReportPrice(result);
						inbound.setTotalPrice(inbound.getItemCount()*inbound.getReportPrice());
					}
					
					inbound.setInboundMaster(target);
					inbound.setOrderNo(orderNo);
					list.add(inbound);
					orderNo = orderNo + 1;
				}
				
			}
			
			saveItems(list);
			// function list for save
			//
			//for list
			
		}
		
		//excel sheet 2
		//for t ->{
		//
		// get sheetname ;
		// id = save()
		//  1~4 
		// list<inbound> enetiy  
		// save - 
		// for  - list<inbound>
		// save 
		//}
		
		
		
		return true;
	}

	
	
	
	public void excelUploadProcess02(int cellIndex, String value, Inbound inbound) throws ParseException {
		// cellIndex는 1개 row의 순차적 cell 의미 , value는 cellIndex의 value
		// commonid 입력값 없으면 insert 후보
		
		if (cellIndex == 0) {
			
			inbound.setOrderNoStr(value);
			
		}
		if (cellIndex == 1) {

			SimpleDateFormat format1 = new SimpleDateFormat("yyyyMMdd");
			SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
			Date d=null;
			d=format1.parse(value);
			String b= format2.format(d);
			inbound.setWorkDateStr(b);
			
		}
		
		if (cellIndex == 2) {

			inbound.setCompanyNm(value);
		}
		if (cellIndex == 3) {
			inbound.setMarking(value);
		}
		if (cellIndex == 4) {
			inbound.setKorNm(value);
		}
		if (cellIndex == 5) {
			inbound.setItemCount(new Double(value));
		}
		if (cellIndex == 6) {
			inbound.setBoxCount(new Double(value));
		}
		if (cellIndex == 7) {
			inbound.setWeight(new Double(value));
		}	
		if (cellIndex == 8) {
			inbound.setCbm(new Double(value));
		}
		if (cellIndex == 9) {
			inbound.setReportPrice(new Double(value));
		}
		if (cellIndex == 10) {
			inbound.setMemo1(value);
		}
		if (cellIndex == 11) {
			inbound.setMemo2(value);
		}
//		if (cellIndex == 12) {
//			inbound.setMemo3(value);
//		}
		if (cellIndex == 12) {
			inbound.setItemNo(value);
		}
		if (cellIndex == 13) {
			inbound.setJejil(value);
		}
		if (cellIndex == 14) {
			inbound.setHsCode(value);
		}
		if (cellIndex == 15) {
			inbound.setCoCode(value);
		}
		if (cellIndex == 16) {
			inbound.setEngNm(value);	
		}
		if (cellIndex == 17) {
			inbound.setColorName(value);
		}
		if(cellIndex == 18) {
			inbound.setAmountType(value);
		}
		if(cellIndex == 19) {
			inbound.setBackgroundColorName(value);
		}
		
	}
	
	
	public void saveItems(List<Inbound> l ) {
		for(int k=0; k<l.size();k++) {
			Inbound inbound = new Inbound();
			inbound=l.get(k);
			inbound=_inboundRepository.save(inbound);
		}
		
	}

}
