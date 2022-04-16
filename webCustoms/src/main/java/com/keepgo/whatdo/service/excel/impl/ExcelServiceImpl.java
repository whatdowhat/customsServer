package com.keepgo.whatdo.service.excel.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.define.DocumentType;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTASubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAIRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAISubRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.service.excel.ExcelService;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;

@Component
public class ExcelServiceImpl implements ExcelService {

	
	@Autowired
	InboundRepository _inboundRepository;
	
	@Autowired
	InboundMasterRepository _inboundMasterRepository;
	
	@Autowired
	FinalInboundInboundMasterRepository _FinalInboundInboundMasterRepository;

	@Autowired
	UtilService _utilService;
	
	@Autowired
	InboundService _InboundService;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Value("${fileupload.root}")
	String uploadRoot;

	@Override
	public boolean fta(ExcelFTARes excelFTARes,HttpServletResponse response) throws Exception {
		
//		String path = "C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\co\\FTA.xlsx";
		String path = DocumentType.getList().stream().filter(t->t.getId() == 1).findFirst().get().getName();
//		Resource resource = resourceLoader.getResource(path); 

		try {
//			Path filePath = Paths.get(path);
//			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
//			String path = DocumentType.getList().stream().filter(t->t.getId() == 1).findFirst().get().getName();
//			Path filePath = Paths.get(path);
			
			Resource resource = resourceLoader.getResource(path); 
			
//			File file = new File(path);
			File file = new File(resource.getURI());;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelFTARes.getFileNm());

			XSSFSheet sheet = workbook.getSheetAt(0);
			Collections.reverse(excelFTARes.getSubItem());			
			//item add
			//문서마다 시작하는 숫자가 고정
			int startCount = 22;
			for(int i=0; i<excelFTARes.getSubItem().size();i++) {
				
//				shiftRow(startCount+1,sheet,sheet.getRow(1),"D");
				shiftRow(startCount,sheet,"D",excelFTARes.getSubItem().get(i), workbook);
				shiftRow(startCount+1,sheet,"BL",excelFTARes.getSubItem().get(i), workbook);
//				startCount+=2;
//				startCount+=1;
				
			}
			
			//data 치환
			chageData(sheet,excelFTARes, workbook);
			
			//item add
			String fileName = excelFTARes.getFileNm()+"_FTA.xlsx";
	        response.setContentType("application/download;charset=utf-8");
	        response.setHeader("custom-header",fileName);
			workbook.write(response.getOutputStream());
			workbook.close();
			
		
			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return true;
//		return null;
	}

	public void chageData(XSSFSheet sheet,ExcelFTARes excelFTARes,XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>(); 
		
		for (int i = 0;  i< rowCount; i++) {
			XSSFRow row = sheet.getRow(i);
			

				Map<String, Object> map = new HashMap<String, Object>();
				rowIndex = i;
				CellType NUMERIC = CellType.NUMERIC;
				CellType FORMULA = CellType.FORMULA;
				row.cellIterator().forEachRemaining(cell ->{
					switch (cell.getCellTypeEnum().name()) {
					case "STRING":
						String value = cell.getStringCellValue();
						
						cell.setCellValue(convertData(value,excelFTARes));	
						//template row 삭제 처리
						if(value.contains("${deleteRow}")) {
//							sheet.removeRow();
							cell.setCellValue("");
							deleteList.add(row.getRowNum());
						}
						
						break;
					case "NUMERIC":
						if (DateUtil.isCellDateFormatted(cell)) {
							Date date1 = cell.getDateCellValue();
							cell.setCellValue(date1);
						} else {
							double cellValue1 = cell.getNumericCellValue();
							cell.setCellValue(cellValue1);
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
				
		}
		
	deleteList.stream().forEach(t->{
		sheet.shiftRows(t , sheet.getLastRowNum() , -1);
	});
	deleteList.stream().forEach(t->{
		sheet.shiftRows(t , sheet.getLastRowNum() , -1);
	});
		
		
	}
	public String convertData(String target,ExcelFTARes excelFTARes) {
		
		if(target.contains("${data01}")) {
			return excelFTARes.getData01();
		}else if(target.contains("${data02}")) {
			return excelFTARes.getData02();
		}else if(target.contains("${data03}")) {
			return excelFTARes.getData03();
		}else if(target.contains("${data04_1}")) {
			String result = "";
			try {
				String[] data04_arr =  excelFTARes.getData04().split(",");
//				result = data04_arr[0];		
				result = getDateStr(data04_arr[0]);
				
			} catch (Exception e) {
				System.err.println("convertData ::excelFTARes.getData04()::"+excelFTARes.getData04());
			}
			return result;
		}else if(target.contains("${data04_2}")) {
			String result = "";
			try {
				String[] data04_arr =  excelFTARes.getData04().split(",");
				result = data04_arr[1];				
			} catch (Exception e) {
				System.err.println("convertData ::excelFTARes.getData04()::"+excelFTARes.getData04());
			}
			return result;
		}else if(target.contains("${data04_3}")) {
			String result = "";
			try {
				String[] data04_arr =  excelFTARes.getData04().split(",");
				result = data04_arr[2];				
			} catch (Exception e) {
				System.err.println("convertData ::excelFTARes.getData04()::"+excelFTARes.getData04());
			}
			return result;
		}else if(target.contains("${data04_4}")) {
			String result = "";
			try {
				String[] data04_arr =  excelFTARes.getData04().split(",");
				result = data04_arr[3];				
			} catch (Exception e) {
				System.err.println("convertData ::excelFTARes.getData04()::"+excelFTARes.getData04());
			}
			return result;
		}else if(target.contains("${data05}")) {
			return excelFTARes.getData05();
		}else if(target.contains("${totalCountEng}")) {
			return excelFTARes.getTotalCountEng();
		}
		else {
			return target;
		}
		
		
	}
	
	
	public  void shiftRow(int startIndex,XSSFSheet sheet,String type,ExcelFTASubRes item,XSSFWorkbook workbook) {
		sheet.shiftRows(startIndex, sheet.getLastRowNum(), 1, true, true);
		writeData(startIndex,sheet,type,item,workbook);
	}
	
	public  void writeData(int startIndex,XSSFSheet sheet,String type,ExcelFTASubRes item,XSSFWorkbook workbook) {
		
//			XSSFRow target = sheet.getRow(startIndex);
			XSSFRow target = sheet.createRow(startIndex);	
			if(type.equals("BL")) {
				CopyRowBlank(target,sheet.getRow(startIndex+1),item,workbook,startIndex);	
			}else {
				CopyRowData(target,sheet.getRow(startIndex+1),item,workbook,startIndex);
			}
			
			
	}
	
	public  void CopyRowBlank(XSSFRow target,XSSFRow origin,ExcelFTASubRes item,XSSFWorkbook workbook,int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell->{
						
			if(count.size() < 9) {
				
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);
				
				//첫번째인경우 
				//
				if(item.getOrderNo() == 1) {
//					target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
					if(count.size() == 7) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
						target.getCell(cell.getColumnIndex()).setCellValue(getDateStr(item.getDepartDtStr()));
//						target.getCell(cell.getColumnIndex()).setCellValue("TAAAA");
					}
				}else{
//					if(count.size() == 7) {
//						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
//						target.getCell(cell.getColumnIndex()).setCellValue("");
//					}
				}
				
				
				
				
			}else {
//				CellStyle newCellStyle = cell.getCellStyle();
//				newCellStyle.cloneStyleFrom(cell.getCellStyle());
//				cell.setCellValue("t");
			}
		count.add(1);
		});
		
	}
	public  void CopyRowData(XSSFRow target,XSSFRow origin,ExcelFTASubRes item,XSSFWorkbook workbook,int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell->{
						
			if(count.size() < 9) {
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);
//				target.getCell(cell.getColumnIndex()).setCellValue(cell.getStringCellValue());

				if(count.size() == 0) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getOrderNo());
				}
				if(count.size() == 1) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getMaking());
				}
				if(count.size() == 2) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getEngNm());
				}
				if(count.size() == 3) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getHsCode());
				}
				if(count.size() == 4) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getData10());
				}
				if(count.size() == 5) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getItemCount());
				}
				if(count.size() == 6) {
					
				}
				if(count.size() == 7) {
					if(item.getOrderNo() == 1) {
						if(count.size() == 7) {
							workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
							target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
						}
					}
				}
				
				
			}else {
//				CellStyle newCellStyle = cell.getCellStyle();
//				newCellStyle.cloneStyleFrom(cell.getCellStyle());
//				cell.setCellValue("t");
			}
		count.add(1);
		});
		
	}

	@Override
	public ExcelFTARes ftaData(FinalInboundInboundMasterReq req) throws Exception {
		
		
		ExcelFTARes result= _FinalInboundInboundMasterRepository.findById(req.getId()).map(t->{
			
			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();	
			ExcelFTARes item = ExcelFTARes.builder().build();
			List<ExcelFTASubRes> list = new ArrayList<>();
			
//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();
			
			String departDt = t.getFinalInbound().getDepartDtStr();
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue()+"\n"+t.getInboundMaster().getComExport().getValue2()); 
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoNmEn()+"\n" +t.getInboundMaster().getCompanyInfo().getCoAddress());
			StringBuilder data4 = new StringBuilder("");
			data4.append(departDt);
			data4.append(",");
			data4.append(t.getFinalInbound().getHangName()+" / "+t.getFinalInbound().getHangCha());
			data4.append(",");
			data4.append(t.getFinalInbound().getDepartPort());
			data4.append(",");
			data4.append(t.getFinalInbound().getIncomePort());			
			item.setData04(data4.toString());
			item.setData05(item.getData01());
			
//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);
			
			
			//병합처리된 marking 데이터 처리를 위한 
			inbound_list = _InboundService.getInboundByInboundMasterId(InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long,String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForFTA(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");
			
			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
			.sorted(Comparator.comparing(Inbound::getOrderNo))
			.filter(where-> where.getCoId().intValue() == 1).collect(Collectors.toList());
			
			List<ExcelFTASubRes> sublist = new ArrayList<>();
			for(int i=0; i<origin_inbound_list.size();i++) {
				ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();
				
				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i+1);
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice()+yyMMdd+"B");
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
				sublist.add(subRes);
//				index.add(1);
//				return subRes;
				
			}
//			ExcelFTASubRes to;to.getItemCount()
			Double total = sublist.stream().mapToDouble(ExcelFTASubRes::getItemCount).sum();
			item.setTotalCountEng("TOTAL ("+String.valueOf(total.intValue())+")"); 
			item.setSubItem(sublist);
			return item; 
		}).get();
		
		return result;
	}
	
	
	public String getYYMMDD(String source) {
		String target = "";
		try {
			
			String[] sources =  source.split("-");
			target = sources[0].substring(2, 4);
			target += sources[1];
			target += sources[2];
				
		} catch (Exception e) {
			System.err.println("getYYMMDD() /mentor/src/main/java/com/keepgo/whatdo/service/excel/impl/ExcelServiceImpl.java ");
		}
		return target;
		
	}
	 /**
	    * 설명
	    * @param 입력값 예)2022-05-09
	    * @return 출력값 예)May, 09.2022
	    * @ author creator
	    * @ version 1.0
	    */

	public String getDateStr(String date_in) {
//		String date_in  = "2022-05-09";
		SimpleDateFormat fmt_in = new SimpleDateFormat ("yyyy-MM-dd");
		SimpleDateFormat fmt_out = new SimpleDateFormat ("MMM, dd.yyyy", Locale.ENGLISH);
		
		ParsePosition pos = new ParsePosition (0);
		java.util.Date outTime = fmt_in.parse (date_in, pos);
		String date_out = fmt_out.format (outTime);
		return date_out;
	}
	
	@Override
	public boolean rcep(ExcelRCEPRes excelRCEPRes,HttpServletResponse response) throws Exception {
		
		String path = DocumentType.getList().stream().filter(t->t.getId() == 2).findFirst().get().getName();
//		Resource resource = resourceLoader.getResource(path); 

		try {
//			Path filePath = Paths.get(path);
//			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
//			String path = DocumentType.getList().stream().filter(t->t.getId() == 1).findFirst().get().getName();
//			Path filePath = Paths.get(path);
			
			Resource resource = resourceLoader.getResource(path); 
			
//			File file = new File(path);
			File file = new File(resource.getURI());;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelRCEPRes.getFileNm());

			XSSFSheet sheet = workbook.getSheetAt(0);
			Collections.reverse(excelRCEPRes.getSubItem());			
			//item add
			//문서마다 시작하는 숫자가 고정
			int startCount = 22;
			for(int i=0; i<excelRCEPRes.getSubItem().size();i++) {
				
//				shiftRow(startCount+1,sheet,sheet.getRow(1),"D");
			
				shiftRowForRCEP(startCount, sheet,"D", excelRCEPRes.getSubItem().get(i), workbook);
				shiftRowForRCEP(startCount+1,sheet,"BL",excelRCEPRes.getSubItem().get(i), workbook);
//				startCount+=2;
//				startCount+=1;
				
			}
			
			//data 치환
		
			chageDataforRCEP(sheet,excelRCEPRes, workbook);
			
			//item add
			String fileName = excelRCEPRes.getFileNm()+"_RCEP.xlsx";
	        response.setContentType("application/download;charset=utf-8");
	        response.setHeader("custom-header",fileName);
			workbook.write(response.getOutputStream());
			workbook.close();
			
		
			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return true;
//		return null;
	}
	
	
	public void chageDataforRCEP(XSSFSheet sheet,ExcelRCEPRes excelRCEPRes,XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>(); 
		
		for (int i = 0;  i< rowCount; i++) {
			XSSFRow row = sheet.getRow(i);
			

				Map<String, Object> map = new HashMap<String, Object>();
				rowIndex = i;
				CellType NUMERIC = CellType.NUMERIC;
				CellType FORMULA = CellType.FORMULA;
				row.cellIterator().forEachRemaining(cell ->{
					switch (cell.getCellTypeEnum().name()) {
					case "STRING":
						String value = cell.getStringCellValue();
						
						cell.setCellValue(convertDataForRCEP(value,excelRCEPRes));	
						//template row 삭제 처리
						if(value.contains("${deleteRow}")) {
//							sheet.removeRow();
							cell.setCellValue("");
							deleteList.add(row.getRowNum());
						}
						
						break;
					case "NUMERIC":
						if (DateUtil.isCellDateFormatted(cell)) {
							Date date1 = cell.getDateCellValue();
							cell.setCellValue(date1);
						} else {
							double cellValue1 = cell.getNumericCellValue();
							cell.setCellValue(cellValue1);
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
				
		}
		
		

	
	deleteList.stream().forEach(t->{
		sheet.shiftRows(t , sheet.getLastRowNum() , -1);
	});
	deleteList.stream().forEach(t->{
		sheet.shiftRows(t , sheet.getLastRowNum() , -1);
	});
		
	}
	public String convertDataForRCEP(String target,ExcelRCEPRes excelRCEPRes) {
		
		if(target.contains("${data01}")) {
			return excelRCEPRes.getData01();
		}else if(target.contains("${data02}")) {
			return excelRCEPRes.getData02();
		}else if(target.contains("${data03}")) {
			return excelRCEPRes.getData03();
		}else if(target.contains("${data04_1}")) {
			String result = "";
			try {
				String[] data04_arr =  excelRCEPRes.getData04().split(",");
//				result = data04_arr[0];		
				result = getDateStr(data04_arr[0]);
				
			} catch (Exception e) {
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::"+excelRCEPRes.getData04());
			}
			return result;
		}else if(target.contains("${data04_2}")) {
			String result = "";
			try {
				String[] data04_arr =  excelRCEPRes.getData04().split(",");
				result = data04_arr[1];				
			} catch (Exception e) {
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::"+excelRCEPRes.getData04());
			}
			return result;
		}else if(target.contains("${data04_3}")) {
			String result = "";
			try {
				String[] data04_arr =  excelRCEPRes.getData04().split(",");
				result = data04_arr[2];				
			} catch (Exception e) {
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::"+excelRCEPRes.getData04());
			}
			return result;
		}else if(target.contains("${data04_4}")) {
			String result = "";
			try {
				String[] data04_arr =  excelRCEPRes.getData04().split(",");
				result = data04_arr[3];				
			} catch (Exception e) {
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::"+excelRCEPRes.getData04());
			}
			return result;
		}else if(target.contains("${data05}")) {
			return excelRCEPRes.getData05();
		}else if(target.contains("${totalCountEng}")) {
			return excelRCEPRes.getTotalCountEng();
		}
		else {
			return target;
		}
		
		
	}
	
	
	public  void shiftRowForRCEP(int startIndex,XSSFSheet sheet,String type,ExcelRCEPSubRes item,XSSFWorkbook workbook) {
		sheet.shiftRows(startIndex, sheet.getLastRowNum(), 1, true, true);
		writeDataForRCEP(startIndex,sheet,type,item,workbook);
	}
	
	public  void writeDataForRCEP(int startIndex,XSSFSheet sheet,String type,ExcelRCEPSubRes item,XSSFWorkbook workbook) {
		
//			XSSFRow target = sheet.getRow(startIndex);
			XSSFRow target = sheet.createRow(startIndex);	
			if(type.equals("BL")) {
				CopyRowBlankForRCEP(target,sheet.getRow(startIndex+1),item,workbook,startIndex);	
			}else {
				CopyRowDataForRCEP(target,sheet.getRow(startIndex+1),item,workbook,startIndex);
			}
			
			
	}
	
	public  void CopyRowBlankForRCEP(XSSFRow target,XSSFRow origin,ExcelRCEPSubRes item,XSSFWorkbook workbook,int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell->{
						
			if(count.size() < 9) {
				
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);
				
				//첫번째인경우 
				//
				if(item.getOrderNo() == 1) {
//					target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
					if(count.size() == 7) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
						target.getCell(cell.getColumnIndex()).setCellValue(getDateStr(item.getDepartDtStr()));
					}
				}else{
					if(count.size() == 7) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
						target.getCell(cell.getColumnIndex()).setCellValue("");
					}
				}
				
				
				
				
			}else {
//				CellStyle newCellStyle = cell.getCellStyle();
//				newCellStyle.cloneStyleFrom(cell.getCellStyle());
//				cell.setCellValue("t");
			}
		count.add(1);
		});
		
	}
	public  void CopyRowDataForRCEP(XSSFRow target,XSSFRow origin,ExcelRCEPSubRes item,XSSFWorkbook workbook,int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell->{
						
			if(count.size() < 9) {
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);
//				target.getCell(cell.getColumnIndex()).setCellValue(cell.getStringCellValue());

				if(count.size( ) == 0) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getOrderNo());
				}
				if(count.size() == 1) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getMaking());
				}
				if(count.size() == 2) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getEngNm());
				}
				if(count.size() == 3) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getHsCode());
				}
				if(count.size() == 4) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getData10());
				}
				if(count.size() == 5) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getItemCount());
				}
				if(count.size() == 6) {
					
				}
				if(count.size() == 7) {
					if(item.getOrderNo() == 1) {
						if(count.size() == 7) {
							workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
							target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
						}
					}
				}
				
				
			}else {

			}
		count.add(1);
		});
		
		
		
	}

	@Override
	public ExcelRCEPRes rcepData(FinalInboundInboundMasterReq req) throws Exception {
		
		
		ExcelRCEPRes result= _FinalInboundInboundMasterRepository.findById(req.getId()).map(t->{
			
			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();	
			ExcelRCEPRes item = ExcelRCEPRes.builder().build();
			List<ExcelRCEPSubRes> list = new ArrayList<>();
			
//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();
			
			String departDt = t.getFinalInbound().getDepartDtStr();
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue()+"\n"+t.getInboundMaster().getComExport().getValue2()); 
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoNmEn()+"\n" +t.getInboundMaster().getCompanyInfo().getCoAddress());
			StringBuilder data4 = new StringBuilder("");
			data4.append(departDt);
			data4.append(",");
			data4.append(t.getFinalInbound().getHangName()+" / "+t.getFinalInbound().getHangCha());
			data4.append(",");
			data4.append(t.getFinalInbound().getDepartPort());
			data4.append(",");
			data4.append(t.getFinalInbound().getIncomePort());			
			item.setData04(data4.toString());
			item.setData05(item.getData01());
			
//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);
			
			
			//병합처리된 marking 데이터 처리를 위한 
			inbound_list = _InboundService.getInboundByInboundMasterId(InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long,String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForRCEP(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");
			
			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
			.sorted(Comparator.comparing(Inbound::getOrderNo))
			.filter(where-> where.getCoId().intValue() == 4).collect(Collectors.toList());
			
			List<ExcelRCEPSubRes> sublist = new ArrayList<>();
			for(int i=0; i<origin_inbound_list.size();i++) {
				ExcelRCEPSubRes subRes = ExcelRCEPSubRes.builder().build();
				
				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i+1);
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice()+yyMMdd+"B");
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
				sublist.add(subRes);
//				index.add(1);
//				return subRes;
				
			}
//			ExcelFTASubRes to;to.getItemCount()
			Double total = sublist.stream().mapToDouble(ExcelRCEPSubRes::getItemCount).sum();
			item.setTotalCountEng("TOTAL ("+String.valueOf(total.intValue())+")"); 
			item.setSubItem(sublist);
			return item; 
		}).get();
		
		return result;
	}
	
	@Override
	public boolean yatai(ExcelYATAIRes excelYATAIRes,HttpServletResponse response) throws Exception {
		
		String path = DocumentType.getList().stream().filter(t->t.getId() == 3).findFirst().get().getName();


		try {

			
			Resource resource = resourceLoader.getResource(path); 
			

			File file = new File(resource.getURI());;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelYATAIRes.getFileNm());

			XSSFSheet sheet = workbook.getSheetAt(0);
			
			Collections.reverse(excelYATAIRes.getSubItem());			
			//item add
			//문서마다 시작하는 숫자가 고정
			int startCount = 8;
			for(int i=0; i<excelYATAIRes.getSubItem().size();i++) {
				
			
				shiftRowForYATAI(startCount, sheet,"D", excelYATAIRes.getSubItem().get(i), workbook);
				shiftRowForYATAI(startCount+1,sheet,"BL",excelYATAIRes.getSubItem().get(i), workbook);
				
			}
//			
			//data 치환
			
			chageDataforYATAI(sheet,excelYATAIRes, workbook);
			
			//item add
			String fileName = excelYATAIRes.getFileNm()+"_YATAI.xlsx";
	        response.setContentType("application/download;charset=utf-8");
	        response.setHeader("custom-header",fileName);
			workbook.write(response.getOutputStream());
			workbook.close();
			
		
			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return true;
//		return null;
	}
	
	
	public void chageDataforYATAI(XSSFSheet sheet,ExcelYATAIRes excelYATAIRes,XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>(); 
		
		for (int i = 0;  i< rowCount; i++) {
			XSSFRow row = sheet.getRow(i);
			

				Map<String, Object> map = new HashMap<String, Object>();
				rowIndex = i;
				CellType NUMERIC = CellType.NUMERIC;
				CellType FORMULA = CellType.FORMULA;
				row.cellIterator().forEachRemaining(cell ->{
					switch (cell.getCellTypeEnum().name()) {
					case "STRING":
						String value = cell.getStringCellValue();
						
						cell.setCellValue(convertDataForYATAI(value,excelYATAIRes));	
						//template row 삭제 처리
						if(value.contains("${deleteRow}")) {
//							sheet.removeRow();
							cell.setCellValue("");
							deleteList.add(row.getRowNum());
						}
						
						break;
					case "NUMERIC":
						if (DateUtil.isCellDateFormatted(cell)) {
							Date date1 = cell.getDateCellValue();
							cell.setCellValue(date1);
						} else {
							double cellValue1 = cell.getNumericCellValue();
							cell.setCellValue(cellValue1);
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
				
		}
		
		

	
	deleteList.stream().forEach(t->{
		sheet.shiftRows(t , sheet.getLastRowNum() , -1);
	});

		
	}
	public String convertDataForYATAI(String target,ExcelYATAIRes excelYATAIRes) {
		
		if(target.contains("${data01}")) {
			return excelYATAIRes.getData01();
		}else if(target.contains("${data02}")) {
			return excelYATAIRes.getData02();
		}else if(target.contains("${data03}")) {
			
			String OriginStr = target;
			if(excelYATAIRes.getData03() == null) {
				excelYATAIRes.setData03("null");
			}
			if(excelYATAIRes.getData04() == null) {
				excelYATAIRes.setData04("null");
			}
			OriginStr = OriginStr.replace("${data03}", excelYATAIRes.getData03());
			OriginStr = OriginStr.replace("${data04}", excelYATAIRes.getData04());
			return OriginStr;
			
		}else if(target.contains("${data04}")) {
			return excelYATAIRes.getData04();
		}else if(target.contains("${totalBoxCountEng}")) {
			return excelYATAIRes.getTotalBoxCountEng();
		}
		else {
			return target;
		}
		
		
	}
	
	
	public  void shiftRowForYATAI(int startIndex,XSSFSheet sheet,String type,ExcelYATAISubRes item,XSSFWorkbook workbook) {
		sheet.shiftRows(startIndex, sheet.getLastRowNum(), 1, true, true);
		writeDataForYATAI(startIndex,sheet,type,item,workbook);
	}
	
	public  void writeDataForYATAI(int startIndex,XSSFSheet sheet,String type,ExcelYATAISubRes item,XSSFWorkbook workbook) {
		
//			XSSFRow target = sheet.getRow(startIndex);
			XSSFRow target = sheet.createRow(startIndex);	
			if(type.equals("BL")) {
				CopyRowBlankForYATAI(target,sheet.getRow(startIndex+1),item,workbook,startIndex);	
			}else {
				CopyRowDataForYATAI(target,sheet.getRow(startIndex+1),item,workbook,startIndex);
			}
			
			
	}
	
	public  void CopyRowBlankForYATAI(XSSFRow target,XSSFRow origin,ExcelYATAISubRes item,XSSFWorkbook workbook,int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell->{
						
			if(count.size() < 11) {
				
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);
				
				//첫번째인경우 
				//
				if(item.getOrderNo() == 1) {

					if(count.size() == 9) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 9, 10));
						target.getCell(cell.getColumnIndex()).setCellValue(getDateStr(item.getDepartDtStr()));
					}
				}else{
					if(count.size() == 9) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 9, 10));
						target.getCell(cell.getColumnIndex()).setCellValue("");
											}
				}
				
				
				
				
			}else {

			}
		count.add(1);
		});
		
	}
	public  void CopyRowDataForYATAI(XSSFRow target,XSSFRow origin,ExcelYATAISubRes item,XSSFWorkbook workbook,int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell->{
						
			if(count.size() < 11) {
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);

				if(count.size() == 0) {
					
				}if(count.size() == 1) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getHsCode());
				}if(count.size() == 2) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getMaking());
				}if(count.size() == 3) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getEngNm());
				}if(count.size() == 4) {
					
				}if(count.size() == 5) {
					
				}if(count.size() == 6) {
					
				}if(count.size() == 7) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getData08());
				}
				if(count.size() == 8) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getItemCount());
				}
				
				if(count.size() == 9) {
					if(item.getOrderNo() == 1) {
						if(count.size() == 9) {
							workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 9, 10));
							target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
						}
					}
				}
				
				
			}else {

			}
		count.add(1);
		});
		
	}

	@Override
	public ExcelYATAIRes yataiData(FinalInboundInboundMasterReq req) throws Exception {
		
		
		ExcelYATAIRes result= _FinalInboundInboundMasterRepository.findById(req.getId()).map(t->{
			
			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();	
			ExcelYATAIRes item = ExcelYATAIRes.builder().build();
			List<ExcelYATAISubRes> list = new ArrayList<>();
			
//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();
			
			String departDt = t.getFinalInbound().getDepartDtStr();
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue()); 
			item.setData02(t.getInboundMaster().getCompanyInfo().getCoNmEn()+"\n" +t.getInboundMaster().getCompanyInfo().getCoAddress());
			item.setData03(t.getFinalInbound().getDepartPort());
			item.setData04(t.getFinalInbound().getIncomePort());
			
			
//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);
			
			
			//병합처리된 marking 데이터 처리를 위한 
			inbound_list = _InboundService.getInboundByInboundMasterId(InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long,String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForYATAI(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");
			
			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
			.sorted(Comparator.comparing(Inbound::getOrderNo))
			.filter(where-> where.getCoId().intValue() == 2).collect(Collectors.toList());
			
			List<ExcelYATAISubRes> sublist = new ArrayList<>();
			for(int i=0; i<origin_inbound_list.size();i++) {
				ExcelYATAISubRes subRes = ExcelYATAISubRes.builder().build();
				
				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i+1);
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setData08(item.getData08());
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice()+yyMMdd+"B");
				subRes.setDepartDtStr(departDt);
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				
				
				
				
				
				sublist.add(subRes);

				
			}

//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
			item.setTotalBoxCountEng("TOTAL: ("+String.valueOf(total.intValue())+")"+" CTNS OF"); 
			item.setSubItem(sublist);
			return item; 
		}).get();
		
		return result;
	}
	
}