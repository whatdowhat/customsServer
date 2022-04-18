package com.keepgo.whatdo.service.common.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.request.CommonMasterReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.response.CommonMasterRes;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CommonbyCompanyRes;
import com.keepgo.whatdo.mapper.CommonMapper;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.common.CommonService;

@Service
public class CommonServiceImpl implements CommonService {

	@Autowired
	CommonRepository _commonRepository;
	@Autowired
	CommonMasterRepository _commonMasterRepository;
	
	@Autowired
	UserRepository _userRepository;
	
	@Autowired
	CommonMapper _commonMapper;

	@Override
	public List<?> getMasterAll(CommonMasterReq commonMasterReq) {

		List<?> result = _commonMasterRepository.findAll().stream()
				.map(item->CommonMasterRes.builder()
						.id(item.getId())
						.name(item.getName())
						.build())
		.collect(Collectors.toList());
		
		return result;
	}

	@Override
	public List<?> getCommonAll(CommonReq commonReq) {
//		Comparator<Common> byLastName = Comparator.comparing(Common::getUpdateDt);
		List<?> result = _commonRepository.findAll().stream()
				
//				.sorted(byLastName.reversed())
				.sorted(Comparator.comparing(Common::getUpdateDt).reversed())
				.map(item->CommonRes.builder()
						.id(item.getId())
						.value(item.getValue())
						.commonMasterId(item.getCommonMaster().getId())
						.isUsing(item.getIsUsing())
						.createDt(item.getCreateDt())
						.updateDt(item.getUpdateDt())
						.name(item.getNm())
						.build())
				
		.collect(Collectors.toList());
		
		return result;
	}

	@Override
	public List<?> getCommonByMaster(CommonReq commonReq) {
		
		
		
		List<?> result = _commonRepository.findByCommonMaster(_commonMasterRepository.findById(commonReq.getCommonMasterId()).orElse(CommonMaster.builder().build())).stream()
				.sorted(Comparator.comparing(Common::getUpdateDt).reversed())
				.map(item->CommonRes.builder()
						.id(item.getId())
						.value(item.getValue())
						.value2(item.getValue2())
						.value3(item.getValue3())
						.commonMasterId(item.getCommonMaster().getId())
						.isUsing(item.getIsUsing())
						.createDt(item.getCreateDt())
						.updateDt(item.getUpdateDt())
						.name(item.getNm())
						.build())
		.collect(Collectors.toList());
		return result;
	}
	
	@Override
	public List<?> getCommonByCompanyInfoExport(CommonReq commonReq) {
		
			
		List<?> result = _commonMapper.checkedCompanyinfoExport(commonReq.getCompanInfoyId());
		return result;
	}
	
	@Override
	public List<?> getCommonByCompanyInfoManage(CommonReq commonReq) {
		
		
		
		List<?> result = _commonMapper.checkedCompanyinfoManage(commonReq.getCompanInfoyId());

		return result;
	}
	
	
	@Override
	public CommonRes deleteCommonData(CommonReq commonReq) {
		List<CommonReq> list = commonReq.getCommonReqData();
		for (int i = 0; i < list.size(); i++) {

			Common common = _commonRepository.findById(list.get(i).getId()).orElse(Common.builder().build());

			_commonRepository.delete(common);
		}
		
		
		return CommonRes.builder().commonMasterId(commonReq.getCommonMasterId()).build();
	}
	
	@Override
	public CommonRes addCommonData(CommonReq commonReq) {
		Common common = new Common();
		CommonMaster master = _commonMasterRepository.findById(commonReq.getCommonMasterId())
				.orElse(CommonMaster.builder().build());
		
		common.setCommonMaster(master);
		common.setValue(commonReq.getValue());
		common.setValue2(commonReq.getValue2());
		common.setValue3(commonReq.getValue3());
		common.setCreateDt(new Date());
		common.setUpdateDt(new Date());
		common.setNm(commonReq.getName());
		common.setUser(new Long(1));
		Boolean isUsing = Boolean.valueOf(commonReq.getIsUsing());
		common.setIsUsing(isUsing);
		
		 _commonRepository.save(common);
		

		 return CommonRes.builder().commonMasterId(commonReq.getCommonMasterId()).build();
	}
	
	@Override
	public CommonRes updateCommonData(CommonReq commonReq) {
		Common common = _commonRepository.findById(commonReq.getId())
				.orElse(Common.builder().build());
		
		common.setId(commonReq.getId());		
		common.setValue(commonReq.getValue());
		common.setValue2(commonReq.getValue2());
		common.setValue3(commonReq.getValue3());
		common.setCreateDt(new Date());
		common.setUpdateDt(new Date());
		common.setUser(new Long(1));
		Boolean isUsing = Boolean.valueOf(commonReq.getIsUsing());
		common.setIsUsing(isUsing);
		
		 _commonRepository.save(common);
		

		 return CommonRes.builder().commonMasterId(commonReq.getCommonMasterId()).build();
	}

	
	@Override
	public List<?> excelUpload(MultipartFile Multifile, CommonReq commonReq) throws IOException {
		
		String extension = FilenameUtils.getExtension(Multifile.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}


		XSSFWorkbook workbook = new XSSFWorkbook(Multifile.getInputStream());
		
		// 첫번째 시트
		XSSFSheet sheet = workbook.getSheetAt(0);
		
		
		//마지막 row 숫자
		int rowCount = sheet.getLastRowNum();
		List<Common> list = new ArrayList<>();
		//i=1   rowCount+1 해야 마지막 row 데이터까지 읽어옴
		for(int i=1; i<rowCount+1;i++) {
			XSSFRow row = sheet.getRow(i);
			Common common = new Common();
			common.setIsUsing(true);
			common.setCreateDt(new Date());
			common.setUpdateDt(new Date());
			common.setUser(new Long(1));
			row.cellIterator().forEachRemaining(cell->{
				
				int cellIndex = cell.getColumnIndex();
				switch (cell.getCellTypeEnum().name()) {
				
				case "STRING":
					String string1 = cell.getStringCellValue();
					excelUploadProcess01(cellIndex,string1,common);
					
					
					
					break;
				case "NUMERIC":
					cell.setCellType( HSSFCell.CELL_TYPE_STRING );
					String numberToString = cell.getStringCellValue();
					cellIndex = cell.getColumnIndex();		
					
					excelUploadProcess01(cellIndex,numberToString,common);
					
					break;
				case "FORMULA":
					String formula1 = cell.getCellFormula();
					cell.setCellFormula(formula1);
					break;

				default:
					break;
				}
				
				
			});
			
			if(common.getCommonMaster() !=null) {

				list.add(common);
				_commonRepository.save(common);	
			}
			
		}
		
		return list;
	}

	public void excelUploadProcess01(int cellIndex,String value,Common common) {
		//cellIndex는 1개 row의 순차적 cell 의미 , value는 cellIndex의 value
		//commonid 입력값 없으면 insert 후보
		if(cellIndex == 0 ) {

			common.setId(new Long(value));
			
		}
		if(cellIndex == 1) {
		//	두번째 cell의 코드이름(마스터 name)으로 마스터호출, null이라면 업로드안함, 마스터 존재시 common에 할당
			CommonMaster master = _commonMasterRepository.findByName(value);
			if(master == null) {
				
			}else {
				common.setNm(master.getName());
				common.setCommonMaster(master);
			}
		}
		if(cellIndex == 2) {
			common.setValue(value);
		}
		if(cellIndex == 3) {
			common.setValue2(value);
		}
		
	}
	
}
