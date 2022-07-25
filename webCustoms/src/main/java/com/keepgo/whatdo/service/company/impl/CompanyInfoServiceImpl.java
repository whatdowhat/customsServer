package com.keepgo.whatdo.service.company.impl;

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

import com.keepgo.whatdo.define.AmountType;
import com.keepgo.whatdo.define.CorpType;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

	@Autowired
	CompanyInfoRepository _companyInfoRepository;

	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;

	@Autowired
	CompanyInfoManageRepository _companyInfoManageRepository;

	@Autowired
	CommonRepository _commonRepository;

	@Override
	public List<?> getAll(CompanyInfoReq req) {

		List<?> list = _companyInfoRepository.findAll().stream()
//				.sorted(Comparator.comparing(CompanyInfo::getUpdateDt).reversed())
				.sorted(Comparator.comparing(CompanyInfo::getCorpType).thenComparing(CompanyInfo::getCoNm))
//				.sorted(Comparator.comparing(CompanyInfo::getCoNm)
				.map(item -> {
//				.sorted(Comparator.comparing(CompanyInfo::getId).reversed()).map(item -> {
					CompanyInfoRes dto = CompanyInfoRes.builder()

							.id(item.getId()).coAddress(item.getCoAddress()).coNm(item.getCoNm())
							.coInvoice(item.getCoInvoice()).updateDt(item.getUpdateDt()).coNmEn(item.getCoNmEn())
							.isUsing(item.getIsUsing()).createDt(item.getCreateDt()).consignee(item.getConsignee())
							.manager(item.getManager()).forwarding(item.getForwarding())
							
//							.exports(item.getExports().stream().map(sub_item -> {
//								Map<String, Object> f = new HashMap<>();
//								f.put("comNm", sub_item.getCommon().getNm());
//								f.put("comValue", sub_item.getCommon().getValue());
//								f.put("comValue2", sub_item.getCommon().getValue2());
//								f.put("preperOrder", sub_item.getPreperOrder());
//								f.put("id", sub_item.getCommon().getId());
//
//								return f;
//							})
//									
//									.collect(Collectors.toList()))
							.build();
					StringBuffer sb = new StringBuffer();
					String companyNum=item.getCoNum();
					sb.append(companyNum);
					sb.insert(3, "-");
					sb.insert(6, "-");
					dto.setCoNum(sb.toString());
					if(item.getCorpType()==null||item.getCorpType()==0) {
						dto.setCorpId(new Integer(1));
						dto.setCorpShowName("KMJP");
					}else {
						for(int k=0; k<CorpType.getList().size();k++) {
							if(CorpType.getList().get(k).getId()==item.getCorpType()) {
								dto.setCorpId(CorpType.getList().get(k).getId());
								dto.setCorpShowName(CorpType.getList().get(k).getShowName());
							}else {

							}
						}
					}
						
						
					

					return dto;
				}).collect(Collectors.toList());
//		l.sort(Comparator.comparing(FinalInboundInboundMaster::getId));
		return list;
	}
	
	@Override
	public List<?> getCompanyByCorpType(CompanyInfoReq req) {

		List<?> list = _companyInfoRepository.findByCorpType(req.getCorpId()).stream()
				.sorted(Comparator.comparing(CompanyInfo::getCorpType).thenComparing(CompanyInfo::getCoNm))
				.map(item -> {
//				.sorted(Comparator.comparing(CompanyInfo::getId).reversed()).map(item -> {
					CompanyInfoRes dto = CompanyInfoRes.builder()

							.id(item.getId()).coAddress(item.getCoAddress()).coNm(item.getCoNm())
							.coInvoice(item.getCoInvoice()).updateDt(item.getUpdateDt()).coNmEn(item.getCoNmEn())
							.isUsing(item.getIsUsing()).createDt(item.getCreateDt()).consignee(item.getConsignee())
							.manager(item.getManager()).corpId(item.getCorpType())
							
//							.exports(item.getExports().stream().map(sub_item -> {
//								Map<String, Object> f = new HashMap<>();
//								f.put("comNm", sub_item.getCommon().getNm());
//								f.put("comValue", sub_item.getCommon().getValue());
//								f.put("comValue2", sub_item.getCommon().getValue2());
//								f.put("preperOrder", sub_item.getPreperOrder());
//								f.put("id", sub_item.getCommon().getId());
//
//								return f;
//							})
//									
//									.collect(Collectors.toList()))
							.build();
					StringBuffer sb = new StringBuffer();
					String companyNum=item.getCoNum();
					sb.append(companyNum);
					sb.insert(3, "-");
					sb.insert(6, "-");
					dto.setCoNum(sb.toString());

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
	public boolean createCompanyInfo(CompanyInfoReq req) {
		CompanyInfo target = CompanyInfo.builder().coNum(req.getCoNum()).coNm(req.getCoNm())
				.coAddress(req.getCoAddress()).coInvoice(req.getCoInvoice()).isUsing(true)
//				.manages(Arrays.asList(null))
				.manager(req.getManager()).createDt(new Date()).updateDt(new Date())

				.build();
		target = _companyInfoRepository.save(target);
		final Long companyId = target.getId();
		AtomicInteger index_m = new AtomicInteger(1);
		AtomicInteger index_e = new AtomicInteger(1);

//		req.getManages().stream().forEach(item->_companyInfoManageRepository.save(CompanyInfoManage.builder()
//				.preperOrder(index_m.getAndIncrement())
//				.common(Common.builder().id(item).build())
//				.companInfoy(CompanyInfo.builder().id(companyId).build())
//				.build()));
		req.getExports().stream()
				.forEach(item -> _companyInfoExportRepository.save(CompanyInfoExport.builder()
						.preperOrder(index_e.getAndIncrement()).common(Common.builder().id(item).build())
						.companInfoy(CompanyInfo.builder().id(companyId).build()).build()));

		return true;
	}

	@Override
	public CompanyInfoRes deleteCompanyInfo(CompanyInfoReq companyInfoReq) {
		List<Long> list = companyInfoReq.getIds();
		for (int i = 0; i < list.size(); i++) {

			CompanyInfo companyInfo = _companyInfoRepository.findById(list.get(i).longValue())
					.orElse(CompanyInfo.builder().build());

			_companyInfoRepository.delete(companyInfo);
			int deleteCompanyInfoExport = _companyInfoExportRepository.deleteCompanyinfoExport(companyInfo.getId());
//			_companyInfoExportRepository.deleteCompanyinfoExport(companyInfo.getId());
			int deleteCompanyInfoManage = _companyInfoManageRepository.deleteCompanyinfoManage(companyInfo.getId());
//			_companyInfoManageRepository.deleteCompanyinfoManage(companyInfo.getId());
		}

		CompanyInfoRes companyInfoRes = new CompanyInfoRes();
		return companyInfoRes;
	}

	@Override
	public boolean addCompanyInfo(CompanyInfoReq companyInfoReq) {
		CompanyInfo companyInfo = new CompanyInfo();

		companyInfo.setCoAddress(companyInfoReq.getCoAddress());
		companyInfo.setCoInvoice(companyInfoReq.getCoInvoice());
		companyInfo.setCoNm(companyInfoReq.getCoNm());
		companyInfo.setCoNum(companyInfoReq.getCoNum());
		companyInfo.setCreateDt(new Date());
		companyInfo.setUpdateDt(new Date());
		companyInfo.setCoNmEn(companyInfoReq.getCoNmEn());
		companyInfo.setConsignee(companyInfoReq.getConsignee());
		
		companyInfo.setManager(companyInfoReq.getManager());
		companyInfo.setForwarding(companyInfoReq.getForwarding());
//		companyInfo.setUser(User.builder().build());

		Boolean isUsing = Boolean.valueOf(companyInfoReq.getIsUsing());
		companyInfo.setIsUsing(isUsing);
		if(companyInfoReq.getCorpId()==null||companyInfoReq.getCorpId()==0) {
			companyInfo.setCorpType(new Integer(1));
		}else {
			companyInfo.setCorpType(companyInfoReq.getCorpId());
		}
		
		List<CompanyInfo> list = _companyInfoRepository.findByCorpTypeAndCoNum(companyInfoReq.getCorpId(), companyInfoReq.getCoNum());
		if(list.size()==0) {
			_companyInfoRepository.save(companyInfo);
			return true;
			
		}else {
			return false;
		}
		
		
		
		
	}

	@Override
	public CompanyInfoRes updateCompanyInfo(CompanyInfoReq companyInfoReq) {
		CompanyInfo companyInfo = _companyInfoRepository.findById(companyInfoReq.getId())
				.orElse(CompanyInfo.builder().build());

		companyInfo.setId(companyInfoReq.getId());
		companyInfo.setCoAddress(companyInfoReq.getCoAddress());
		companyInfo.setCoInvoice(companyInfoReq.getCoInvoice());
		companyInfo.setCoNm(companyInfoReq.getCoNm());
		companyInfo.setCoNum(companyInfoReq.getCoNum());
		companyInfo.setCreateDt(new Date());
		companyInfo.setUpdateDt(new Date());
		companyInfo.setManager(companyInfoReq.getManager());
		companyInfo.setCoNmEn(companyInfoReq.getCoNmEn());
		companyInfo.setConsignee(companyInfoReq.getConsignee());
		companyInfo.setForwarding(companyInfoReq.getForwarding());
		companyInfo.setCorpType(companyInfoReq.getCorpId());
		Boolean isUsing = Boolean.valueOf(companyInfoReq.getIsUsing());
		companyInfo.setIsUsing(isUsing);

		_companyInfoRepository.save(companyInfo);

		CompanyInfoRes companyInfoRes = new CompanyInfoRes();
		return companyInfoRes;
	}

	@Override
	public CompanyInfoRes addCompanyInfoExports(CompanyInfoReq companyInfoReq) {

		CompanyInfo companyInfo = _companyInfoRepository.findById(companyInfoReq.getId())
				.orElse(CompanyInfo.builder().build());

		_companyInfoExportRepository.deleteCompanyinfoExport(companyInfo.getId());

		List<CommonReq> list = companyInfoReq.getCompanyExportData();

		for (int i = 0; i < list.size(); i++) {

			CompanyInfoExport companyInfoExport = new CompanyInfoExport();
			Common common = _commonRepository.findById(list.get(i).getId()).orElse(Common.builder().build());

			companyInfoExport.setPreperOrder(list.get(i).getPreperOrder());
			companyInfoExport.setCommon(common);
			companyInfoExport.setCompanInfoy(companyInfo);

			_companyInfoExportRepository.save(companyInfoExport);

		}

		CompanyInfoRes companyInfoRes = new CompanyInfoRes();
		return companyInfoRes;

	}

	@Override
	public CompanyInfoRes addCompanyInfoManages(CompanyInfoReq companyInfoReq) {

		CompanyInfo companyInfo = _companyInfoRepository.findById(companyInfoReq.getId())
				.orElse(CompanyInfo.builder().build());
//		List<Common> list = companyInfoReq.getCompanyManageData();
		List<Common> list = new ArrayList<>();

		for (int i = 0; i < list.size(); i++) {

			CompanyInfoManage companyInfoManage = new CompanyInfoManage();
			Common common = _commonRepository.findById(list.get(i).getId()).orElse(Common.builder().build());

			companyInfoManage.setPreperOrder(0);
			companyInfoManage.setCommon(common);
			companyInfoManage.setCompanInfoy(companyInfo);

			_companyInfoManageRepository.save(companyInfoManage);

		}

		CompanyInfoRes companyInfoRes = new CompanyInfoRes();
		return companyInfoRes;

	}

	@Override
	public List<?> excelUpload(MultipartFile Multifile, CompanyInfoReq companyInfoReq) throws IOException {

		String extension = FilenameUtils.getExtension(Multifile.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}

		XSSFWorkbook workbook = new XSSFWorkbook(Multifile.getInputStream());

		// 첫번째 시트
		XSSFSheet sheet = workbook.getSheetAt(0);

		// 마지막 row 숫자
		int rowCount = sheet.getLastRowNum();
		List<CompanyInfo> list = new ArrayList<>();
		// i=1 rowCount+1 해야 마지막 row 데이터까지 읽어옴
		for (int i = 1; i < rowCount + 1; i++) {
			XSSFRow row = sheet.getRow(i);
			CompanyInfo companyInfo = new CompanyInfo();
			companyInfo.setIsUsing(true);
			companyInfo.setCreateDt(new Date());
			companyInfo.setUpdateDt(new Date());

			row.cellIterator().forEachRemaining(cell -> {

				int cellIndex = cell.getColumnIndex();
				switch (cell.getCellTypeEnum().name()) {

				case "STRING":
					String string1 = cell.getStringCellValue();
					excelUploadProcess01(cellIndex, string1, companyInfo);

					break;
				case "NUMERIC":
					cell.setCellType(HSSFCell.CELL_TYPE_STRING);
					String numberToString = cell.getStringCellValue();
					cellIndex = cell.getColumnIndex();

					excelUploadProcess01(cellIndex, numberToString, companyInfo);

					break;
				case "FORMULA":
					String formula1 = cell.getCellFormula();
					cell.setCellFormula(formula1);
					break;

				default:
					break;
				}

			});

			list.add(companyInfo);
			_companyInfoRepository.save(companyInfo);

		}

		return list;
	}

	public void excelUploadProcess01(int cellIndex, String value, CompanyInfo companyInfo) {
		// cellIndex는 1개 row의 순차적 cell 의미 , value는 cellIndex의 value
		// CompanyInfoId 입력값 없으면 insert 후보
		if (cellIndex == 0) {
			companyInfo.setId(new Long(value));
		}
		if (cellIndex == 1) {
			companyInfo.setManager(value);
		}
		if (cellIndex == 2) {
			companyInfo.setCoInvoice(value);
		}
		if (cellIndex == 3) {
			companyInfo.setCoNm(value);
		}
		if (cellIndex == 4) {
			companyInfo.setCoNmEn(value);
		}
		if (cellIndex == 5) {
			companyInfo.setCoNum(value);
		}
		if (cellIndex == 6) {
			companyInfo.setCoAddress(value);
		}
		if (cellIndex == 7) {
			companyInfo.setConsignee(value);
		}

	}

	@Override
	public CompanyInfoRes getCompanyInfoExports(CompanyInfoReq companyInfoReq) {
		
		CompanyInfoRes res = _companyInfoRepository.findById(companyInfoReq.getId())
				
				.map(item -> {

					CompanyInfoRes dto = CompanyInfoRes.builder()

							.id(item.getId()).coAddress(item.getCoAddress()).coNm(item.getCoNm()).coNum(item.getCoNum())
							.coInvoice(item.getCoInvoice()).updateDt(item.getUpdateDt()).coNmEn(item.getCoNmEn())
							.isUsing(item.getIsUsing()).createDt(item.getCreateDt()).consignee(item.getConsignee())
							.manager(item.getManager())
							
							.exports(item.getExports().stream().map(sub_item -> {
								Map<String, Object> f = new HashMap<>();
								f.put("comNm", sub_item.getCommon().getNm());
								f.put("comValue", sub_item.getCommon().getValue());
								f.put("comValue2", sub_item.getCommon().getValue2());
								f.put("preperOrder", sub_item.getPreperOrder());
								f.put("id", sub_item.getCommon().getId());

								return f;
							})
//									
									.collect(Collectors.toList()))
							.build();

					return dto;
				}).get();
		return res;
	}
	
	@Override
	public CompanyInfoRes getOne(Long id) {
		CompanyInfo res = _companyInfoRepository.findById(id).get();
		CompanyInfoRes dto = CompanyInfoRes.builder()

				.id(res.getId())
				.coAddress(res.getCoAddress())
				.coNm(res.getCoNm())
				.coNum(res.getCoNum())
				.coInvoice(res.getCoInvoice())
				.updateDt(res.getUpdateDt())
				.coNmEn(res.getCoNmEn())
				.isUsing(res.getIsUsing())
				.createDt(res.getCreateDt())
				.consignee(res.getConsignee())
				.manager(res.getManager())
				.build();
			if(res.getExports().size()>0) {
				dto.setExports(res.getExports().stream().map(sub_item->{
					Map<String, Object> f = new HashMap<>();
					f.put("comNm", sub_item.getCommon().getNm());
					f.put("comValue", sub_item.getCommon().getValue());
					f.put("comValue2", sub_item.getCommon().getValue2());
					f.put("comValue3", sub_item.getCommon().getValue3());
					f.put("preperOrder", sub_item.getPreperOrder());
					f.put("id", sub_item.getCommon().getId());
					return f;
				}).collect(Collectors.toList()));
			}
		return dto;
	}
	

}
