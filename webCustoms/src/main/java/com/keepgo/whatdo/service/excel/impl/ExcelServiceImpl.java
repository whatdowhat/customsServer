package com.keepgo.whatdo.service.excel.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DecimalFormat;
import java.text.ParseException;
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
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.google.gson.Gson;
import com.keepgo.whatdo.define.BackgroundColorType;
import com.keepgo.whatdo.define.CorpType;
import com.keepgo.whatdo.define.DocumentType;
import com.keepgo.whatdo.define.UnbiType;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.ExcelCLPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelCORes;
import com.keepgo.whatdo.entity.customs.response.ExcelCOSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelContainerRes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTASubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInboundRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAIRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAISubRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewListRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.service.excel.ExcelService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.EnglishNumberToWords;
import com.keepgo.whatdo.service.util.UtilService;
import com.keepgo.whatdo.util.RowCopy;

@Component
public class ExcelServiceImpl implements ExcelService {

	@Autowired
	InboundRepository _inboundRepository;

	@Autowired
	InboundMasterRepository _inboundMasterRepository;
	
	@Autowired
	CommonRepository _commonRepository;

	@Autowired
	FinalInboundInboundMasterRepository _FinalInboundInboundMasterRepository;
	
	@Autowired
	FinalInboundRepository _finalInboundRepository;

	@Autowired
	UtilService _utilService;

	@Autowired
	InboundService _InboundService;

	@Autowired
	ResourceLoader resourceLoader;

	@Value("${fileupload.root}")
	String uploadRoot;

	@Override
	public boolean fta(ExcelFTARes excelFTARes, HttpServletResponse response) throws Exception {

//		String path = "C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\co\\FTA.xlsx";
		String path = DocumentType.getList().stream().filter(t -> t.getId() == 1).findFirst().get().getName();
//		Resource resource = resourceLoader.getResource(path); 

		try {
//			Path filePath = Paths.get(path);
//			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
//			String path = DocumentType.getList().stream().filter(t->t.getId() == 1).findFirst().get().getName();
//			Path filePath = Paths.get(path);

			Resource resource = resourceLoader.getResource(path);

//			File file = new File(path);
			File file = new File(resource.getURI());
			;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelFTARes.getFileNm());

			XSSFSheet sheet = workbook.getSheetAt(0);
			Collections.reverse(excelFTARes.getSubItem());
			// item add
			// 문서마다 시작하는 숫자가 고정
			int startCount = 22;
			for (int i = 0; i < excelFTARes.getSubItem().size(); i++) {

//				shiftRow(startCount+1,sheet,sheet.getRow(1),"D");
				shiftRow(startCount, sheet, "D", excelFTARes.getSubItem().get(i), workbook);
				shiftRow(startCount + 1, sheet, "BL", excelFTARes.getSubItem().get(i), workbook);
//				startCount+=2;
//				startCount+=1;

			}

			// data 치환
			chageData(sheet, excelFTARes, workbook);
			
			// 셀병합
			//행시작, 행종료, 열시작, 열종료 (자바배열과 같이 0부터 시작)
//			sheet.addMergedRegion(new CellRangeAddress(22,22,7,8));

			// item add
			String fileName = excelFTARes.getFileNm() + "_FTA.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
//			System.err.println(e.getMessage());
		}
		return true;
//		return null;
	}

	public void chageData(XSSFSheet sheet, ExcelFTARes excelFTARes, XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>();

		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = sheet.getRow(i);

			Map<String, Object> map = new HashMap<String, Object>();
			rowIndex = i;
			CellType NUMERIC = CellType.NUMERIC;
			CellType FORMULA = CellType.FORMULA;
			row.cellIterator().forEachRemaining(cell -> {
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String value = cell.getStringCellValue();

					convertData((XSSFCell) cell, value, excelFTARes);
//						cell.setCellValue();	
					// template row 삭제 처리
//					if (value.contains("${deleteRow}")) {
////							sheet.removeRow();
//						cell.setCellValue("");
//						deleteList.add(row.getRowNum());
//					}

					break;
				case "NUMERIC":
//					if (DateUtil.isCellDateFormatted(cell)) {
//						Date date1 = cell.getDateCellValue();
//						cell.setCellValue(date1);
//					} else {
//						double cellValue1 = cell.getNumericCellValue();
//						cell.setCellValue(cellValue1);
//					}
					break;
				case "FORMULA":
//					String formula1 = cell.getCellFormula();
//					cell.setCellFormula(formula1);
					break;

				default:
					break;
				}

			});

		}

		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 22) {
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
			}
		}
		sheet.shiftRows(23, sheet.getLastRowNum(), -1);

		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 22) {
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
			}
		}
		sheet.shiftRows(23, sheet.getLastRowNum(), -1);
	}

	public void convertData(XSSFCell cell, String target, ExcelFTARes excelFTARes) {

		if (target.contains("${data01}")) {

			cell.setCellValue(excelFTARes.getData01());
		} else if (target.contains("${data02}")) {
			cell.setCellValue(excelFTARes.getData02());
		} else if (target.contains("${data03}")) {
			cell.setCellValue(excelFTARes.getData03());
		} else if (target.contains("${data04_1}")) {
			String result = "";
			String[] data04_arr = excelFTARes.getData04().split(",");
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			try {
				cell.setCellValue(dateFormat.parse(data04_arr[0]));
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else if (target.contains("${data04_2}")) {
			String result = "";
			try {

			} catch (Exception e) {
//				System.err.println("convertData ::excelFTARes.getData04()::" + excelFTARes.getData04());
			}
//			return result;
			String[] data04_arr = excelFTARes.getData04().split(",");
			result = data04_arr[1];
			cell.setCellValue(result);
		} else if (target.contains("${data04_3}")) {
			String result = "";
//			try {
//				String[] data04_arr =  excelFTARes.getData04().split(",");
//				result = data04_arr[2];				
//			} catch (Exception e) {
//				System.err.println("convertData ::excelFTARes.getData04()::"+excelFTARes.getData04());
//			}
//			return result;
			String[] data04_arr = excelFTARes.getData04().split(",");
			result = data04_arr[2]+","+data04_arr[3];
			cell.setCellValue(result);
		} else if (target.contains("${data04_4}")) {
			String result = "";
			try {

			} catch (Exception e) {
//				System.err.println("convertData ::excelFTARes.getData04()::" + excelFTARes.getData04());
			}
			String[] data04_arr = excelFTARes.getData04().split(",");
			result = data04_arr[4]+","+data04_arr[5];
//			return result;
			cell.setCellValue(result);
		} else if (target.contains("${data05}")) {
//			return excelFTARes.getData05();
			cell.setCellValue(excelFTARes.getData05());
		} else if (target.contains("${totalCountEng}")) {
//			return excelFTARes.getTotalCountEng();
			cell.setCellValue(excelFTARes.getTotalCountEng());
		} else {
			//아무것도 해당되지 않음.
		}

	}

	public void shiftRow(int startIndex, XSSFSheet sheet, String type, ExcelFTASubRes item, XSSFWorkbook workbook) {
		writeData(startIndex, sheet, type, item, workbook);
	}

	public void writeData(int startIndex, XSSFSheet sheet, String type, ExcelFTASubRes item, XSSFWorkbook workbook) {

		if (type.equals("BL")) {
			copyRowBLforFTA(workbook, sheet, startIndex, startIndex + 2, item);
		} else {
			copyRowforFTA(workbook, sheet, startIndex, startIndex + 2, item);
		}

	}

	

	private void copyRowforFTA(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelFTASubRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
//        if (newRow != null) {
//            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
//        } else {
//            newRow = worksheet.createRow(destinationRowNum);
//        }
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:

				if (i == 0) {
					newCell.setCellValue(item.getOrderNo());
				} else if (i == 5) {
					newCell.setCellValue(item.getItemCountD());
				}  else {
					newCell.setCellValue(oldCell.getNumericCellValue());
				}

				break;
			case Cell.CELL_TYPE_STRING:

				if (i == 7) {
					if (item.getOrderNo() == 1) {
						newCell.setCellValue(item.getCompanyInvoice());
					}
//20220727
				} else if (i == 1) {
					newCell.setCellValue(item.getMaking());
				} else if (i == 2) {
					newCell.setCellValue(item.getEngNm());
				} else if (i == 3) {
					newCell.setCellValue(item.getHsCode());
				} else if (i == 4) {
					newCell.setCellValue(item.getData10());
				} else if (i == 6) {
					newCell.setCellValue(item.getAmountType());
				}else {
					newCell.setCellValue(oldCell.getStringCellValue());
				}

				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	private void copyRowBLforFTA(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelFTASubRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
//        if (newRow != null) {
//            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
//        } else {
//            newRow = worksheet.createRow(destinationRowNum);
//        }
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (i == 7) {
					if (item.getOrderNo() == 1) {
						
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						
						try {
							newCell.setCellValue(dateFormat.parse(item.getDepartDtStr()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							newCell.setCellValue("fail");
							e.printStackTrace();
						}
						//newCell.setCellValue(getDateStr(item.getDepartDtStr()));
//						newCell.setCellValue(item.getDepartDtStr());
					}

				} else {
					//newCell.setCellValue(oldCell.getNumericCellValue());
				}	
				
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}


	@Override
	public ExcelFTARes ftaData(FinalInboundInboundMasterReq req) throws Exception {

		ExcelFTARes result = _FinalInboundInboundMasterRepository.findById(req.getId()).map(t -> {

			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			List<Inbound> total_inbound_list = new ArrayList<>();
			ExcelFTARes item = ExcelFTARes.builder().build();
			List<ExcelFTASubRes> list = new ArrayList<>();
			DecimalFormat decimalFormat2 = new DecimalFormat("#,###");

//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

			String departDt = t.getFinalInbound().getDepartDtStr();
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
					+ t.getInboundMaster().getComExport().getValue2());
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			StringBuilder data4 = new StringBuilder("");
			data4.append(departDt);
			data4.append(",");
			data4.append(t.getFinalInbound().getHangName() + " / " + t.getFinalInbound().getHangCha());
			data4.append(",");
//			data4.append(t.getFinalInbound().getDepartPort());
			data4.append((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			data4.append(",");
//			data4.append(t.getFinalInbound().getIncomePort());
			data4.append((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			item.setData04(data4.toString());
			item.setData05(item.getData01());
			if(t.getInboundMaster().getPackingType()!=null) {
				item.setPackingType(t.getInboundMaster().getPackingType());
			}else {
				item.setPackingType("CTN");
			}
			
//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);

			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long, String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForFTA(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");
			total_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).collect(Collectors.toList());
			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 1)
					.collect(Collectors.toList());
			

			List<ExcelFTASubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd );
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
//				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
//				subRes.setItemCount(decimalFormat2.format(origin_inbound_list.get(i).getItemCount()));
				subRes.setItemCount(getStringResult(origin_inbound_list.get(i).getItemCount()));	
				subRes.setItemCountD(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
//				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				subRes.setBoxCount(total_inbound_list.get(i).getBoxCount());
				if(origin_inbound_list.get(i).getAmountType()!=null) {
					subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				}else {
					subRes.setAmountType("PCS");
				}
				
				
				sublist.add(subRes);
//				index.add(1);
//				return subRes;

			}
//			ExcelFTASubRes to;to.getItemCount()
//			Double total = sublist.stream().mapToDouble(ExcelFTASubRes::getItemCount).sum();
//			item.setTotalCountEng("TOTAL (" + String.valueOf(total.intValue()) + ")");
//			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
//					.mapToDouble(ExcelFTASubRes::getBoxCount).sum();
			Double total = inbound_list.get(0).getBoxCountSum();
			if(total==1||total==0) {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"OF");
			}else {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"S"+" "+"OF");
			}
			
			
			
			
			item.setSubItem(sublist);
			return item;
		}).get();

		return result;
	}

	public String getYYMMDD(String source) {
		String target = "";
		try {

			String[] sources = source.split("-");
			target = sources[0].substring(2, 4);
			target += sources[1];
			target += sources[2];

		} catch (Exception e) {
//			System.err.println(
//					"getYYMMDD() /mentor/src/main/java/com/keepgo/whatdo/service/excel/impl/ExcelServiceImpl.java ");
		}
		return target;

	}

	/**
	 * 설명
	 * 
	 * @param 입력값 예)2022-05-09
	 * @return 출력값 예)May, 09.2022 @ author creator @ version 1.0
	 */

	public String getDateStr(String date_in) {
//		String date_in  = "2022-05-09";
		SimpleDateFormat fmt_in = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat fmt_out = new SimpleDateFormat("MMM, dd.yyyy", Locale.ENGLISH);

		ParsePosition pos = new ParsePosition(0);
		java.util.Date outTime = fmt_in.parse(date_in, pos);
		String date_out = fmt_out.format(outTime);
		return date_out;
	}

	@Override
	public boolean rcep(ExcelRCEPRes excelRCEPRes, HttpServletResponse response) throws Exception {

		String path = DocumentType.getList().stream().filter(t -> t.getId() == 2).findFirst().get().getName();
//		Resource resource = resourceLoader.getResource(path); 

		try {
//			Path filePath = Paths.get(path);
//			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
//			String path = DocumentType.getList().stream().filter(t->t.getId() == 1).findFirst().get().getName();
//			Path filePath = Paths.get(path);

			Resource resource = resourceLoader.getResource(path);

//			File file = new File(path);
			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelRCEPRes.getFileNm());

			XSSFSheet sheet = workbook.getSheetAt(0);
			Collections.reverse(excelRCEPRes.getSubItem());
			// item add
			// 문서마다 시작하는 숫자가 고정
			int startCount = 22;
			for (int i = 0; i < excelRCEPRes.getSubItem().size(); i++) {

//				shiftRow(startCount+1,sheet,sheet.getRow(1),"D");

				shiftRowForRCEP(startCount, sheet, "D", excelRCEPRes.getSubItem().get(i), workbook);
				shiftRowForRCEP(startCount + 1, sheet, "BL", excelRCEPRes.getSubItem().get(i), workbook);
//				startCount+=2;
//				startCount+=1;

			}

			// data 치환

			chageDataforRCEP(sheet, excelRCEPRes, workbook);

			// item add
			String fileName = excelRCEPRes.getFileNm() + "_RCEP.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
//			System.err.println(e.getMessage());
		}
		return true;
//		return null;
	}

	public void chageDataforRCEP(XSSFSheet sheet, ExcelRCEPRes excelRCEPRes, XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>();

		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = sheet.getRow(i);

			Map<String, Object> map = new HashMap<String, Object>();
			rowIndex = i;
			CellType NUMERIC = CellType.NUMERIC;
			CellType FORMULA = CellType.FORMULA;
			row.cellIterator().forEachRemaining(cell -> {
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String value = cell.getStringCellValue();

					cell.setCellValue(convertDataForRCEP(value, excelRCEPRes));

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

		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 22) {
				sheet.removeMergedRegion(i);
				
			}
		}
		sheet.shiftRows(23, sheet.getLastRowNum(), -1);

		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 22) {
				sheet.removeMergedRegion(i);
				
			}
		}
		sheet.shiftRows(23, sheet.getLastRowNum(), -1);

	}

	public String convertDataForRCEP(String target, ExcelRCEPRes excelRCEPRes) {

		if (target.contains("${data01}")) {
			return excelRCEPRes.getData01();
		} else if (target.contains("${data02}")) {
			return excelRCEPRes.getData02();
		} else if (target.contains("${data03}")) {
			return excelRCEPRes.getData03();
		} else if (target.contains("${data04_1}")) {
			
			String result = "";
			try {
				String[] data04_arr = excelRCEPRes.getData04().split(",");
//				result = data04_arr[0];		
				result = getDateStr(data04_arr[0]);

			} catch (Exception e) {
//				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
			}
			return result;
		} else if (target.contains("${data04_2}")) {
			String result = "";
			try {
				String[] data04_arr = excelRCEPRes.getData04().split(",");
				result = data04_arr[1];
			} catch (Exception e) {
//				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
			}
			return result;
		} else if (target.contains("${data04_3}")) {
			String result = "";
			try {
				String[] data04_arr = excelRCEPRes.getData04().split(",");
				result = data04_arr[2]+","+data04_arr[3];
			} catch (Exception e) {
//				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
			}
			return result;
		} else if (target.contains("${data04_4}")) {
			String result = "";
			try {
				String[] data04_arr = excelRCEPRes.getData04().split(",");
				result = data04_arr[4]+","+data04_arr[5];
			} catch (Exception e) {
//				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
			}
			return result;
		} else if (target.contains("${data05}")) {
			return excelRCEPRes.getData05();
		} else if (target.contains("${totalCountEng}")) {
			return excelRCEPRes.getTotalCountEng();
		} else {
			return target;
		}

	}

	public void shiftRowForRCEP(int startIndex, XSSFSheet sheet, String type, ExcelRCEPSubRes item,
			XSSFWorkbook workbook) {
//		sheet.shiftRows(startIndex, sheet.getLastRowNum(), 1, true, true);
		writeDataForRCEP(startIndex, sheet, type, item, workbook);
	}

	public void writeDataForRCEP(int startIndex, XSSFSheet sheet, String type, ExcelRCEPSubRes item,
			XSSFWorkbook workbook) {

		if (type.equals("BL")) {
			copyRowBLforRCEP(workbook, sheet, startIndex, startIndex + 2, item);
		} else {
			copyRowforRCEP(workbook, sheet, startIndex, startIndex + 2, item);
		}

	}

	
	public void CopyRowDataForRCEP(XSSFRow target, XSSFRow origin, ExcelRCEPSubRes item, XSSFWorkbook workbook,
			int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell -> {

			if (count.size() < 9) {
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);
//				target.getCell(cell.getColumnIndex()).setCellValue(cell.getStringCellValue());

				if (count.size() == 0) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getOrderNo());
				}
				if (count.size() == 1) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getMaking());
				}
				if (count.size() == 2) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getEngNm());
				}
				if (count.size() == 3) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getHsCode());
				}
				if (count.size() == 4) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getData10());
				}
				if (count.size() == 5) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getItemCount());
				}
				if (count.size() == 6) {

				}
				if (count.size() == 7) {
					if (item.getOrderNo() == 1) {
						if (count.size() == 7) {
							workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
							target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
						}
					}
				}

			} else {

			}
			count.add(1);
		});

	}

	private void copyRowforRCEP(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelRCEPSubRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
//        if (newRow != null) {
//            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
//        } else {
//            newRow = worksheet.createRow(destinationRowNum);
//        }
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:

				if (i == 0) {
					newCell.setCellValue(item.getOrderNo());
				}else if (i == 5) {
					newCell.setCellValue(item.getItemCountD());
				}else {
					newCell.setCellValue(oldCell.getNumericCellValue());
				}

				break;
			case Cell.CELL_TYPE_STRING:

				if (i == 7) {
					if (item.getOrderNo() == 1) {
						newCell.setCellValue(item.getCompanyInvoice());
					}

				} else if (i == 1) {
					newCell.setCellValue(item.getMaking());
				} else if (i == 2) {
					newCell.setCellValue(item.getEngNm());
				} else if (i == 3) {
					newCell.setCellValue(item.getHsCode());
				} else if (i == 4) {
					newCell.setCellValue(item.getData10());
				} else if (i == 6) {
					newCell.setCellValue(item.getAmountType());
				}else {
					newCell.setCellValue(oldCell.getStringCellValue());
				}

				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	private void copyRowBLforRCEP(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelRCEPSubRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
//        if (newRow != null) {
//            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
//        } else {
//            newRow = worksheet.createRow(destinationRowNum);
//        }
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (i == 7) {
					if (item.getOrderNo() == 1) {
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						
						try {
							newCell.setCellValue(dateFormat.parse(item.getDepartDtStr()));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							newCell.setCellValue("fail");
							e.printStackTrace();
						}
					}

				} else {
					newCell.setCellValue(oldCell.getNumericCellValue());
				}	
				
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}
	@Override
	public ExcelRCEPRes rcepData(FinalInboundInboundMasterReq req) throws Exception {

		ExcelRCEPRes result = _FinalInboundInboundMasterRepository.findById(req.getId()).map(t -> {

			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			ExcelRCEPRes item = ExcelRCEPRes.builder().build();
			List<ExcelRCEPSubRes> list = new ArrayList<>();
			DecimalFormat decimalFormat2 = new DecimalFormat("#,###");

//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

			String departDt = t.getFinalInbound().getDepartDtStr();

			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
					+ t.getInboundMaster().getComExport().getValue2());
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			StringBuilder data4 = new StringBuilder("");
			data4.append(departDt);
			data4.append(",");
			data4.append(t.getFinalInbound().getHangName() + " / " + t.getFinalInbound().getHangCha());
			data4.append(",");
			data4.append((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			data4.append(",");
			data4.append((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			item.setData04(data4.toString());
			item.setData05(item.getData01());
			if(t.getInboundMaster().getPackingType()!=null) {
				item.setPackingType(t.getInboundMaster().getPackingType());
			}else {
				item.setPackingType("CTN");
			}

//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);

			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long, String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForRCEP(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");

			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 4)
					.collect(Collectors.toList());

			List<ExcelRCEPSubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelRCEPSubRes subRes = ExcelRCEPSubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd);
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setItemCount(getStringResult(origin_inbound_list.get(i).getItemCount()));
				subRes.setItemCountD(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				if(origin_inbound_list.get(i).getAmountType()!=null) {
					subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				}else {
					subRes.setAmountType("PCS");
				}
				sublist.add(subRes);
//				index.add(1);
//				return subRes;

			}
//			ExcelFTASubRes to;to.getItemCount()
//			Double total = sublist.stream().mapToDouble(ExcelRCEPSubRes::getItemCount).sum();
//			item.setTotalCountEng("TOTAL (" + String.valueOf(total.intValue()) + ")");
//			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
//					.mapToDouble(ExcelRCEPSubRes::getBoxCount).sum();
			Double total = inbound_list.get(0).getBoxCountSum();
			if(total==1||total==0) {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"OF");
			}else {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"S"+" "+"OF");
			}
			
			item.setSubItem(sublist);
			return item;
		}).get();

		return result;
	}

	@Override
	public boolean yatai(ExcelYATAIRes excelYATAIRes, HttpServletResponse response) throws Exception {

		String path = DocumentType.getList().stream().filter(t -> t.getId() == 3).findFirst().get().getName();

		try {

			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelYATAIRes.getFileNm());

			XSSFSheet sheet = workbook.getSheetAt(0);

			Collections.reverse(excelYATAIRes.getSubItem());
			// item add
			// 문서마다 시작하는 숫자가 고정
			int startCount = 8;
			for (int i = 0; i < excelYATAIRes.getSubItem().size(); i++) {

				shiftRowForYATAI(startCount, sheet, "D", excelYATAIRes.getSubItem().get(i), workbook);
				shiftRowForYATAI(startCount + 1, sheet, "BL", excelYATAIRes.getSubItem().get(i), workbook);

			}
//			
			// data 치환

			chageDataforYATAI(sheet, excelYATAIRes, workbook);

			// item add
			String fileName = excelYATAIRes.getFileNm() + "_YATAI.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
//			System.err.println(e.getMessage());
		}
		return true;
//		return null;
	}

	public void chageDataforYATAI(XSSFSheet sheet, ExcelYATAIRes excelYATAIRes, XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>();

		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = sheet.getRow(i);

			Map<String, Object> map = new HashMap<String, Object>();
			rowIndex = i;
			CellType NUMERIC = CellType.NUMERIC;
			CellType FORMULA = CellType.FORMULA;
			row.cellIterator().forEachRemaining(cell -> {
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String value = cell.getStringCellValue();

					cell.setCellValue(convertDataForYATAI(value, excelYATAIRes));
					

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
		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 8) {
				sheet.removeMergedRegion(i);
				
			}
		}
		sheet.shiftRows(9, sheet.getLastRowNum(), -1);

		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 8) {
				sheet.removeMergedRegion(i);
				
			}
		}
		sheet.shiftRows(9, sheet.getLastRowNum(), -1);


	}

	public String convertDataForYATAI(String target, ExcelYATAIRes excelYATAIRes) {

		if (target.contains("${data01}")) {
			return excelYATAIRes.getData01();
		} else if (target.contains("${data02}")) {
			return excelYATAIRes.getData02();
		} else if (target.contains("${data03}")) {

			String OriginStr = target;
			if (excelYATAIRes.getData03() == null) {
				excelYATAIRes.setData03("null");
			}
			if (excelYATAIRes.getData04() == null) {
				excelYATAIRes.setData04("null");
			}
			OriginStr = OriginStr.replace("${data03}", excelYATAIRes.getData03());
			OriginStr = OriginStr.replace("${data04}", excelYATAIRes.getData04());
			return OriginStr;

		} else if (target.contains("${data04}")) {
			return excelYATAIRes.getData04();
		} else if (target.contains("${totalBoxCountEng}")) {
			return excelYATAIRes.getTotalBoxCountEng();
		} else {
			return target;
		}

	}

	public void shiftRowForYATAI(int startIndex, XSSFSheet sheet, String type, ExcelYATAISubRes item,
			XSSFWorkbook workbook) {
		writeDataForYATAI(startIndex, sheet, type, item, workbook);
	}

	public void writeDataForYATAI(int startIndex, XSSFSheet sheet, String type, ExcelYATAISubRes item,
			XSSFWorkbook workbook) {

//			XSSFRow target = sheet.getRow(startIndex);
//		XSSFRow target = sheet.createRow(startIndex);
		if (type.equals("BL")) {
			copyRowBLforYATAI(workbook, sheet, startIndex, startIndex + 2, item);
		} else {
			copyRowforYATAI(workbook, sheet, startIndex, startIndex + 2, item);
		}

	}

	public void CopyRowBlankForYATAI(XSSFRow target, XSSFRow origin, ExcelYATAISubRes item, XSSFWorkbook workbook,
			int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell -> {

			if (count.size() < 11) {

				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);

				// 첫번째인경우
				//
				if (item.getOrderNo() == 1) {

					if (count.size() == 9) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 9, 10));
						target.getCell(cell.getColumnIndex()).setCellValue(getDateStr(item.getDepartDtStr()));
					}
				} else {
					if (count.size() == 9) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 9, 10));
						target.getCell(cell.getColumnIndex()).setCellValue("");
					}
				}

			} else {

			}
			count.add(1);
		});

	}

	public void CopyRowDataForYATAI(XSSFRow target, XSSFRow origin, ExcelYATAISubRes item, XSSFWorkbook workbook,
			int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell -> {

			if (count.size() < 11) {
				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);

				if (count.size() == 0) {

				}
				if (count.size() == 1) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getHsCode());
				}
				if (count.size() == 2) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getMaking());
				}
				if (count.size() == 3) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getEngNm());
				}
				if (count.size() == 4) {

				}
				if (count.size() == 5) {

				}
				if (count.size() == 6) {

				}
				if (count.size() == 7) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getData08());
				}
				if (count.size() == 8) {
					target.getCell(cell.getColumnIndex()).setCellValue(item.getItemCount());
				}

				if (count.size() == 9) {
					if (item.getOrderNo() == 1) {
						if (count.size() == 9) {
							workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 9, 10));
							target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
						}
					}
				}

			} else {

			}
			count.add(1);
		});

	}

	private void copyRowforYATAI(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelYATAISubRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:

				if (i == 10) {
					if (item.getOrderNo() == 1) {
						newCell.setCellValue(item.getCompanyInvoice());
					}

				}else if (i == 1) {
					newCell.setCellValue(item.getHsCode());
				} else if (i == 2) {
					newCell.setCellValue(item.getMaking());
				} else if (i == 3) {
					newCell.setCellValue(item.getEngNm());
				}  else if (i == 7) {
					newCell.setCellValue(item.getData08());
				}else if (i == 8) {
					newCell.setCellValue(item.getItemCount());
				}else if (i == 9) {
					newCell.setCellValue(item.getAmountType());
				} else {
					newCell.setCellValue(oldCell.getStringCellValue());
				}

				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	private void copyRowBLforYATAI(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelYATAISubRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

	
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (i == 10) {
					if (item.getOrderNo() == 1) {
						newCell.setCellValue(getDateStr(item.getDepartDtStr()));
//						newCell.setCellValue(item.getDepartDtStr());
					}

				} else {
					newCell.setCellValue(oldCell.getNumericCellValue());
				}	
				
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}
	
	@Override
	public ExcelYATAIRes yataiData(FinalInboundInboundMasterReq req) throws Exception {

		ExcelYATAIRes result = _FinalInboundInboundMasterRepository.findById(req.getId()).map(t -> {

			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			ExcelYATAIRes item = ExcelYATAIRes.builder().build();
			List<ExcelYATAISubRes> list = new ArrayList<>();
			DecimalFormat decimalFormat2 = new DecimalFormat("#,###");

//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

			String departDt = t.getFinalInbound().getDepartDtStr();

			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue());
			item.setData02(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			
//			item.setData03(_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2());
//			item.setData04(_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2());
			item.setData03((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			item.setData04((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			if(t.getInboundMaster().getPackingType()!=null) {
				item.setPackingType(t.getInboundMaster().getPackingType());
			}else {
				item.setPackingType("CTN");
			}
//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);

			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long, String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForYATAI(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");

			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 2)
					.collect(Collectors.toList());

			List<ExcelYATAISubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelYATAISubRes subRes = ExcelYATAISubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setData08(item.getData08());
				subRes.setItemCount(getStringResult(origin_inbound_list.get(i).getItemCount()));
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd);
				subRes.setDepartDtStr(departDt);
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				if(origin_inbound_list.get(i).getAmountType()!=null) {
					subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				}else {
					subRes.setAmountType("PCS");
				}
				sublist.add(subRes);

			}

//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
//			item.setTotalBoxCountEng("TOTAL: (" + String.valueOf(total.intValue()) + ")" + " CTNS OF");
//			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
//					.mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
			Double total = inbound_list.get(0).getBoxCountSum();
			if(total==1||total==0) {
				item.setTotalBoxCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"OF");
			}else {
				item.setTotalBoxCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"S"+" "+"OF");
			}

			item.setSubItem(sublist);

			return item;
		}).get();

		return result;
	}

	@Override
	public boolean inpack(ExcelInpackRes excelInpackRes, HttpServletResponse response) throws Exception {

		String path = DocumentType.getList().stream().filter(t -> t.getId() == 4).findFirst().get().getName();

		try {

			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelInpackRes.getFileNm());
			Collections.reverse(excelInpackRes.getSubItem());
			//시트 수
			int sheetCn = workbook.getNumberOfSheets();
			
			

		      
//		      1234
//		      $1,234
//		      ¥1,234
//		      row.getCell(3).setCellStyle(xcs_dollar);
//		      row.getCell(2).setCellStyle(xcs_wian);
			
			
			for(int cn=0; cn<sheetCn; cn++) {
				String pageNo = "";
				pageNo= String.valueOf(cn+1);
				
				XSSFSheet sheet = workbook.getSheetAt(cn);

				
				// item add
				// 문서마다 시작하는 숫자가 고정
				int startCount = 23;
				for (int i = 0; i < excelInpackRes.getSubItem().size(); i++) {

					shiftRowForInpack(startCount, sheet, "D", excelInpackRes.getSubItem().get(i), workbook, pageNo);
					shiftRowForInpack(startCount + 1, sheet, "BL", excelInpackRes.getSubItem().get(i), workbook, pageNo);

				}

				
				
				// data 치환
				chageDataforInpack(sheet, excelInpackRes, workbook);
				
				
				//셀 병합
				//행시작, 행종료, 열시작, 열종료 (자바배열과 같이 0부터 시작)
				if(cn==0) {
//					sheet.addMergedRegion(new CellRangeAddress(15,16,0,5));
					sheet.addMergedRegion(new CellRangeAddress(20,21,10,11));
					sheet.addMergedRegion(new CellRangeAddress(23,23,1,2));
					sheet.addMergedRegion(new CellRangeAddress(23,23,3,5));
				}
				 if(cn==1) {
					sheet.addMergedRegion(new CellRangeAddress(0,2,0,11));
				}
			
				

				if(cn==1) {
					
					for (int l = 0; l < excelInpackRes.getSubItem().size(); l++) {
						
						XSSFRow row = sheet.getRow(startCount+(2*l));
						int startNum = startCount+(2*l)+1;
						String a = "K";
						String b = String.valueOf(startNum);
						String c = "I";
						row.getCell(9).setCellFormula(a+b+"-"+c+b);
					}
				}
				if(cn==0) {
					
					for (int l = 0; l < excelInpackRes.getSubItem().size(); l++) {
						
						XSSFRow row = sheet.getRow(startCount+(2*l));
						int startNum = startCount+(2*l)+1;
						String a = "G";
						String b = String.valueOf(startNum);
						String c = "J";
						row.getCell(10).setCellFormula(a+b+"*"+c+b);
					}
				}
				//20220726
				if(excelInpackRes.getSubItem().size()<=15) {
					int startRow = 24+(excelInpackRes.getSubItem().size()*2);
					XSSFRow row = sheet.getRow(startRow-1);
					for(int j = 0; j<(excelInpackRes.getSubItem().size()*2)-2; j++) {
						for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
							if (sheet.getMergedRegion(i).getFirstRow() == startRow-1) {
								sheet.removeMergedRegion(i);
								sheet.removeMergedRegion(i);
								sheet.removeMergedRegion(i);
							}
							
						}
						sheet.shiftRows(startRow, sheet.getLastRowNum(), -1);
					}
					
				}else if(excelInpackRes.getSubItem().size()>15) {
					int startRow = 24+(excelInpackRes.getSubItem().size()*2);
					XSSFRow row = sheet.getRow(startRow-1);
					//데이터사이즈 16이상일시 28개 빈칸 생김
					for(int j = 0; j<28; j++) {
						for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
							if (sheet.getMergedRegion(i).getFirstRow() == startRow-1) {
								sheet.removeMergedRegion(i);
								sheet.removeMergedRegion(i);
//								sheet.removeMergedRegion(i);
							}
							
						}
						sheet.shiftRows(startRow, sheet.getLastRowNum(), -1);
					}
					
				}
				
				int LastRow = 24+(excelInpackRes.getSubItem().size()*2);
				if(excelInpackRes.getSubItem().size()<=15) {
					for(int i=23; i<54; i++) {
						sheet.getRow(i).setHeightInPoints(new Float("16"));
					}
					sheet.getRow(54).setHeightInPoints(new Float("18"));
//					for(int i=55; i<65;i++) {
//						sheet.getRow(i).setHeightInPoints(new Float("14"));
//					}
					sheet.rowIterator().forEachRemaining(row ->{
						if(row.getRowNum() <= 66 && row.getRowNum() >= 55) {
							row.setHeightInPoints(new Float("14"));
						}
					});
				}
				else if(excelInpackRes.getSubItem().size()>15) {
					for(int i=23; i<LastRow; i++) {
						sheet.getRow(i).setHeightInPoints(new Float("16"));
					}
					sheet.getRow(LastRow).setHeightInPoints(new Float("18"));
					sheet.rowIterator().forEachRemaining(row ->{
						if(row.getRowNum() <= 300 && row.getRowNum() > LastRow) {
							row.setHeightInPoints(new Float("14"));
						}
					});
				}
				// 도장이미지 삽입
				InboundMaster inboundMaster = _inboundMasterRepository.findById(excelInpackRes.getInboundMasterId()).get();
				Common common = inboundMaster.getComExport();
				if(common.getFileUpload()==null) {
					
					sheet.rowIterator().forEachRemaining(row ->{
						
//						if(row.getRowNum() <=52  && row.getRowNum() >=23 ) {
//		    	    			  row.setHeight(new Short("320"));
//						}
//						if(row.getRowNum() == 54) {
//	    	    			  row.setHeight(new Short("360"));
//						}
//						if(row.getRowNum() <= 66 && row.getRowNum() >= 55) {
//	    	    			  row.setHeight(new Short("280"));
//						}
				    	   row.cellIterator().forEachRemaining(cell->{
				    		   
				    		   try {
				    			   String value = cell.getStringCellValue();
				    			   if(value.equals("${image}")) {
				    				   
				    			       cell.setCellValue("");
				    			       
				    			   }
				    			   if(cell.getStringCellValue().contains("${hang}")) {
				    	    			  row.setHeight(new Short("150"));
				    	    			  cell.setCellValue("");
				    	    			  
				    	    		  }
				    			  
							} catch (Exception e) {
								// TODO: handle exception
							}
				    		   
				    	   });
				       });
					
				}else {
					String imagePath = common.getFileUpload().getRoot()+File.separatorChar+common.getFileUpload().getPath3();
					Path filePath = Paths.get(imagePath);
					Resource resources = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
					int indx=0;

				    String fileTale=FilenameUtils.getExtension(common.getFileUpload().getPath3());    
					 
					 byte[] inputImage =  IOUtils.toByteArray(resources.getInputStream());
				     if(fileTale.toUpperCase().equals("png".toUpperCase())) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_PNG);
				     }else if(fileTale.toUpperCase().equals("bmp".toUpperCase())) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_BMP);
				     }else if(fileTale.toUpperCase().equals("jpg".toUpperCase())) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_JPEG);
				     }
					 
				       XSSFDrawing drawing = sheet.createDrawingPatriarch();
				       XSSFClientAnchor anchor = new XSSFClientAnchor();

				       
				       drawing.createPicture(anchor, indx);
				       Picture pict = drawing.createPicture(anchor, indx);
				       double scale = 0.38;
				       
				       sheet.rowIterator().forEachRemaining(row ->{
				    	   
				    	   
//							if(row.getRowNum() <=52  && row.getRowNum() >=23 ) {
//		    	    			  row.setHeight(new Short("320"));
//							}
//							if(row.getRowNum() == 54) {
//		    	    			  row.setHeight(new Short("360"));
//							}
//							if(row.getRowNum() <= 66 && row.getRowNum() >= 55) {
//		    	    			  row.setHeight(new Short("280"));
//							}
				    	   
				    	   row.cellIterator().forEachRemaining(cell->{
				    		   
				    		   try {
				    			   String value = cell.getStringCellValue();
				    			   
				    			   if(value.equals("${image}")) {
				    				   
				    			       
				    			       anchor.setCol1(cell.getColumnIndex());
				    			       anchor.setCol2(cell.getColumnIndex() + 5);
//				    			       anchor.setCol2(cell.getColumnIndex()+5);
				    			       anchor.setRow1(cell.getRowIndex());
				    	   			   anchor.setRow2(cell.getRowIndex()+8);
				    	   			   
				    			       
//				    			       pict.resize();
//				    			       pict.resize(8.16,8.16);
//				    			       pict.resize(0.38,0.38);
//				    	   			   pict.resize(1.04,1.03);
//				    	   			 pict.resize(1.03);
				    			       cell.setCellValue("");
				    			       
				    			   }
				    			   if(cell.getStringCellValue().contains("${hang}")) {
				    	    			  row.setHeight(new Short("150"));
				    	    			  cell.setCellValue("");
				    	    			  
				    	    		  }
							} catch (Exception e) {
								// TODO: handle exception
							}
				    		   
				    	   });
				       });
				       

				}
				//도장삽입
			}
			
			//기존
			
			String fileName = excelInpackRes.getFileNm() + "_Inpack.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
//			System.err.println(e.getMessage());
		}
		return true;
	}

	public void chageDataforInpack(XSSFSheet sheet, ExcelInpackRes excelInpackRes, XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>();

		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = sheet.getRow(i);

//				if(row != null) {
			if (row == null)
				break;
			row.cellIterator().forEachRemaining(cell -> {
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String value = cell.getStringCellValue();

					cell.setCellValue(convertDataForInpack(value, excelInpackRes));
					
					if(value.contains("${data06}") ) {//date format으로 데이터 넣어야함.
						
						String data06 = excelInpackRes.getData06();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						try {
							cell.setCellValue(dateFormat.parse(data06));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
					}
					if(value.contains("${data04}") ) {//date format으로 데이터 넣어야함.
						if(excelInpackRes.getCoYn()==false) {
							SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
							Date today=new Date();
							String todayStr=dateFormat.format(today);
							
							try {
								cell.setCellValue(dateFormat.parse(todayStr));
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}else {
							SimpleDateFormat dateFormat = new SimpleDateFormat("MMM. dd,yyyy", Locale.ENGLISH);
							SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
							String data04 = excelInpackRes.getData06();
							try {
								Date data04Dt = dateFormat2.parse(data04);
								String finalData04 = dateFormat.format(data04Dt);
								cell.setCellValue(finalData04);
							} catch (ParseException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
					if (value.contains("${currency}")) {// 통화단위

						final String CELLFORMAT_DOLLAR = "_-$* #,##0.000_ ;_-$* -#,##0.000 ;_-$* \"-\"???_ ;_-@_ ";
						final String CELLFORMAT_WIAN = "_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ ";
						XSSFCellStyle xcs_wian = workbook.createCellStyle();
						xcs_wian.setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_WIAN));

						XSSFCellStyle xcs_dollar = workbook.createCellStyle();
						xcs_dollar.setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_DOLLAR));

						if (excelInpackRes.getSubItem().size() > 0) {
							cell.setCellValue("");
							if (excelInpackRes.getSubItem().get(0).getCurrencyType().equals("$")) {
//								row.getCell(cell.getColumnIndex() + 1).setCellStyle(xcs_dollar);
								row.getCell(cell.getColumnIndex() + 1).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_DOLLAR));
							} else {
								row.getCell(cell.getColumnIndex() + 1).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_WIAN));
							}
						}
					}
						if(value.contains("${amountType}") ) {//date format으로 데이터 넣어야함.
						
						
						Integer countPCS=excelInpackRes.getCountPCS();
						Integer countM=excelInpackRes.getCountM();
						Integer countKG=excelInpackRes.getCountKG();
						Integer countYD=excelInpackRes.getCountYD();
						
							if(excelInpackRes.getSubItem().size()==countM) {
								cell.setCellValue("M");
							}else if(excelInpackRes.getSubItem().size()==countKG) {
								cell.setCellValue("KG");
							}else if(excelInpackRes.getSubItem().size()==countYD) {
								cell.setCellValue("YD");
							}else {
								cell.setCellValue(excelInpackRes.getSubItem().get(excelInpackRes.getSubItem().size()-1).getAmountType());
							}
						
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

		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 23) {
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
			}
		}
		sheet.shiftRows(24, sheet.getLastRowNum(), -1);

		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
			if (sheet.getMergedRegion(i).getFirstRow() == 23) {
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
				sheet.removeMergedRegion(i);
			}
		}
		sheet.shiftRows(24, sheet.getLastRowNum(), -1);

		int dataSize = (excelInpackRes.getSubItem().size())*2;
		int firstRow = (excelInpackRes.getSubItem().size())*2-1;
//		
//		for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
//			if (sheet.getMergedRegion(i).getFirstRow() == firstRow) {
//				sheet.removeMergedRegion(i);
//				sheet.removeMergedRegion(i);
//				sheet.removeMergedRegion(i);
//				
//			}
//		}
//		
//		for (int i = 0; i <dataSize-2; i++) {
//			sheet.shiftRows(24+dataSize, sheet.getLastRowNum(), -1);
//		}
//		for (int i = 0; i <dataSize-2; i++) {
//	    	   
//	    	   for(int j=0; j<sheet.getMergedRegions().size();j++) {
//	    	       	if(sheet.getMergedRegion(j).getFirstRow() == 23+dataSize) {
//	    	    		sheet.removeMergedRegion(j);
//	    	    		sheet.removeMergedRegion(j);
//	    	    		
//	    	       	}
//	    	   }
//	    	   
//	    	   sheet.shiftRows(24+dataSize, sheet.getLastRowNum(), -1);
//			}

	}

	public String convertDataForInpack(String target, ExcelInpackRes excelInpackRes) {

		if (target.contains("${data01}")) {
			return excelInpackRes.getData01();
		} else if (target.contains("${data02}")) {
			if(excelInpackRes.getCoYn()==true) {
				return excelInpackRes.getInvoice();
			}else {
				return excelInpackRes.getNoCoDt();
			}
		
		} else if (target.contains("${data03}")) {
			if (excelInpackRes.getData03() == null) {
				excelInpackRes.setData03("null");
			}
			return excelInpackRes.getData03();
		} 
//		else if (target.contains("${data04}")) {
//			return excelInpackRes.getData04();
//		} 
		else if (target.contains("${data05}")) {
			return excelInpackRes.getData05();
//		} else if (target.contains("${data06}")) {
//			return excelInpackRes.getData06();
		} else if (target.contains("${data07}")) {
			return excelInpackRes.getData07();
		} else if (target.contains("${data08}")) {
			return excelInpackRes.getData08();
		} else if (target.contains("${data09}")) {
			return excelInpackRes.getData09();
		}else if (target.contains("${data10}")) {
			return excelInpackRes.getData10();
		}else if (target.contains("${data11}")) {
			return excelInpackRes.getData11();
		}else if (target.contains("${data12}")) {
			return excelInpackRes.getData12();
		}
		
//		else if(target.contains("${amountType}")) {//date format으로 데이터 넣어야함.
//			
//			
//			Integer countPCS=excelInpackRes.getCountPCS();
//			Integer countM=excelInpackRes.getCountM();
//			Integer countKG=excelInpackRes.getCountKG();
//			Integer countYD=excelInpackRes.getCountYD();
//			
//				if(excelInpackRes.getSubItem().size()==countM) {
//					return "M";
//				}else if(excelInpackRes.getSubItem().size()==countKG) {
//					return "KG";
//				}else if(excelInpackRes.getSubItem().size()==countYD) {
//					return "YD";
//				}else {
//					return "UNIT";
//				}
//			
//		}
		else {
			return target;
		}

	}

	public void shiftRowForInpack(int startIndex, XSSFSheet sheet, String type, ExcelInpackSubRes item,
			XSSFWorkbook workbook, String page) {
//		sheet.shiftRows(startIndex, sheet.getLastRowNum(), 1, true, true);
		writeDataForInpack(startIndex, sheet, type, item, workbook, page);
	}

	public void writeDataForInpack(int startIndex, XSSFSheet sheet, String type, ExcelInpackSubRes item,
			XSSFWorkbook workbook, String page) {

//			XSSFRow target = sheet.getRow(startIndex);
//			XSSFRow target = sheet.createRow(startIndex);	
		if (type.equals("BL")) {
//				CopyRowBlankForInpack(target,sheet.getRow(startIndex+1),item,workbook,startIndex);
			copyRowforBL(workbook, sheet, startIndex, startIndex + 2, item, page);
		} else {
//				CopyRowDataForInpack(target,sheet.getRow(startIndex+2),item,workbook,startIndex);
			copyRow(workbook, sheet, startIndex, startIndex + 2, item, page);
		}

	}



	private void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelInpackSubRes item, String page) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
//        if (newRow != null) {
//            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
//        } else {
//            newRow = worksheet.createRow(destinationRowNum);
//        }
		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				
				
			      final String CELLFORMAT_DOLLAR = "_-$* #,##0.000_ ;_-$* -#,##0.000 ;_-$* \"-\"???_ ;_-@_ ";
			      final String CELLFORMAT_WIAN = "_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ ";
			      
			      XSSFCellStyle xcs_wian = workbook.createCellStyle();
			      xcs_wian.setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_WIAN));
			      
			      XSSFCellStyle xcs_dollar = workbook.createCellStyle();
			      xcs_dollar.setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_DOLLAR));
				
				
				if (page.equals("1")) {
					if (i == 6) {
						newCell.setCellValue((item.getItemCount() == null ? 0d : item.getItemCount()));
					} else if (i == 9) {
						if(item.getCurrencyType().equals("$")){
							newCell.setCellValue((item.getItemPrice() == null ? 0d : item.getItemPrice()));
						}else {
							newCell.setCellValue((item.getItemPrice() == null ? 0d : item.getItemPrice()));
							newCell.getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
						}
						
					} else if (i == 10) {
						if(item.getCurrencyType().equals("$")){
//							newCell.setCellValue((item.getItemCount() == null ? 0d : item.getItemCount()) * (item.getItemPrice() == null ? 0d : item.getItemPrice()));
							newCell.getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_DOLLAR));
						}else {
//							newCell.setCellValue((item.getItemCount() == null ? 0d : item.getItemCount()) * (item.getItemPrice() == null ? 0d : item.getItemPrice()));
							newCell.getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(CELLFORMAT_WIAN));
						}
						
					} else {
						newCell.setCellValue(oldCell.getNumericCellValue());
					}
				} else if (page.equals("2")) {
					if (i == 6) {
						newCell.setCellValue((item.getItemCount() == null ? 0d : item.getItemCount()));
					} else if (i == 8) {
						newCell.setCellValue((item.getBoxCount() == null ? 0d : item.getBoxCount()));
					} 
//					else if (i == 9) {
//						newCell.setCellValue( (item.getWeight() == null ? 0d : item.getWeight()));
//					} 
					else if (i == 10) {
						newCell.setCellValue( (item.getTotalWeight() == null ? 0 : item.getTotalWeight()));
					} else if (i == 11) {
						newCell.setCellValue( (item.getCbm() == null ? 0d : item.getCbm()  ));
					} else {
						newCell.setCellValue(oldCell.getNumericCellValue());
					}
				}

				break;
			case Cell.CELL_TYPE_STRING:
				if (page.equals("1")) {
					if (i == 1) {
						newCell.setCellValue(item.getEngNm());
					} else if (i == 3) {
						newCell.setCellValue(item.getJejil());
					}else if (i == 7) {
						if(item.getAmountType()==null||item.getAmountType().equals("PCS")) {
							newCell.setCellValue("PCS");
						}else {
							newCell.setCellValue(item.getAmountType());
						}
						
					}else if (i == 8) {
						newCell.setCellValue(item.getCurrencyType());
					}else if (i == 0) {
						newCell.setCellValue(item.getMarking());
					} else {
						newCell.setCellValue(oldCell.getStringCellValue());
					}
				} else if (page.equals("2")) {
					if (i == 1) {
						newCell.setCellValue(item.getEngNm());
					} else if (i == 3) {
						newCell.setCellValue(item.getJejil());
					}else if (i == 7) {
						if(item.getAmountType()==null||item.getAmountType().equals("PCS")) {
							newCell.setCellValue("PCS");
						}else {
							newCell.setCellValue(item.getAmountType());
						}
						
					}else if (i == 0) {
						newCell.setCellValue(item.getMarking());
					} else {
						newCell.setCellValue(oldCell.getStringCellValue());
					}
				}

//                    newCell.setCellValue(oldCell.getRichStringCellValue());
//                    newCell.setCellValue(item.get);
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	private void copyRowforBL(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelInpackSubRes item, String page) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				if (page.equals("1")) {
					if (i == 1) {
						newCell.setCellValue(item.getHsCode());
					}
				} else if (page.equals("2")) {
					if (i == 1) {
						newCell.setCellValue(item.getHsCode());
					}
				}

				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}

	@Override
	public ExcelInpackRes inpackData(FinalInboundInboundMasterReq req) throws Exception {

		ExcelInpackRes result = _FinalInboundInboundMasterRepository.findById(req.getId()).map(t -> {
			Integer countPCS= 0;
			Integer countM= 0;
			Integer countKG= 0;
			Integer countYD= 0;
			Integer countSET=0;
			Integer countDOZ=0;
			Integer countPAIR=0;
			
			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			ExcelInpackRes item = ExcelInpackRes.builder().build();
			List<ExcelInpackSubRes> list = new ArrayList<>();


			String departDt = t.getFinalInbound().getDepartDtStr();
			StringBuffer sb = new StringBuffer();
			String companyNum=t.getInboundMaster().getCompanyInfo().getCoNum();
			sb.append(companyNum);
			sb.insert(3, "-");
			sb.insert(6, "-");
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setInboundMasterId(t.getInboundMaster().getId());
			item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
					+ t.getInboundMaster().getComExport().getValue2());
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoInvoice());
			item.setData05(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ sb.toString() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			item.setData06(departDt);
			item.setData07(t.getFinalInbound().getHangName() + "/" + t.getFinalInbound().getHangCha());
		
			item.setData08((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			item.setData09((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			if(t.getInboundMaster().getCurrencyType()==null||t.getInboundMaster().getCurrencyType().equals("$")) {
				item.setData10("Unit Price" + "\n" + "(USD)");
				item.setData11("Amount" + "\n" + "(USD)");
			}else {
				item.setData10("Unit Price" + "\n" + "(CNY)");
				item.setData11("Amount" + "\n" + "(CNY)");
			}
			if(t.getInboundMaster().getPackingType()==null||t.getInboundMaster().getPackingType().equals("CTN")) {
				item.setData12("CTNS");
			}else if(t.getInboundMaster().getPackingType().equals("PL")) {
				item.setData12("PLS");
			}else if(t.getInboundMaster().getPackingType().equals("GT")) {
				item.setData12("GTS");
			}
			item.setCoYn(false);
			if(t.getInboundMaster().getCompanyInfo().getCoInvoice()==null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date departDate=dateFormat2.parse(departDt);
					String departDateStr = dateFormat.format(departDate);
					String data=""+departDateStr;
					item.setInvoice(data);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				item.setInvoice("");
			}else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date departDate=dateFormat2.parse(departDt);
					String departDateStr = dateFormat.format(departDate);
					String data=t.getInboundMaster().getCompanyInfo().getCoInvoice()+departDateStr;
					item.setInvoice(data);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			Date today = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
			String todayStr = dateFormat.format(today);
			item.setNoCoDt("SJ-"+todayStr);			
			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
//			Map<Long, String> markingInfo = new HashMap<>();
//			markingInfo = _utilService.getMakingForYATAI(inbound_list);

			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).collect(Collectors.toList());

			List<ExcelInpackSubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelInpackSubRes subRes = ExcelInpackSubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setJejil(origin_inbound_list.get(i).getJejil());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				if(origin_inbound_list.get(i).getReportPrice()==null||origin_inbound_list.get(i).getReportPrice()==0) {
					
					subRes.setItemPrice(new Double(0));
				}else {
					subRes.setItemPrice(origin_inbound_list.get(i).getReportPrice());
				}
				if(origin_inbound_list.get(i).getReportPrice()==null||origin_inbound_list.get(i).getReportPrice()==0) {
					subRes.setTotalPrice(new Double(0));
				}else {
					subRes.setTotalPrice(new Double(String.format("%.2f",
							origin_inbound_list.get(i).getReportPrice() * origin_inbound_list.get(i).getItemCount())));
				}
				
				if(origin_inbound_list.get(i).getBoxCount()==null) {
					subRes.setBoxCount(new Double(0));
				}else {
					subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				}
				
				subRes.setTotalWeight(origin_inbound_list.get(i).getWeight());
				subRes.setWeight((subRes.getTotalWeight()  == null ? 0d : subRes.getTotalWeight()) - (subRes.getBoxCount() == null ? 0d : subRes.getBoxCount()) );
				subRes.setCbm(origin_inbound_list.get(i).getCbm());
				if(origin_inbound_list.get(i).getCoId()!=3) {
					item.setCoYn(true);
				}
				if(t.getInboundMaster().getCurrencyType()==null||t.getInboundMaster().getCurrencyType().equals("$")) {
					subRes.setCurrencyType("$");
				}else {
					subRes.setCurrencyType(t.getInboundMaster().getCurrencyType());
				}
				if(t.getInboundMaster().getPackingType()==null||t.getInboundMaster().getPackingType().equals("CTN")) {
					subRes.setPackingType("CTNS");
				}else {
					subRes.setPackingType(t.getInboundMaster().getPackingType());
				}
				subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				if(origin_inbound_list.get(i).getAmountType()==null||origin_inbound_list.get(i).getAmountType().equals("PCS")){
					countPCS=countPCS+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("M")){
					countM=countM+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("YD")){
					countYD=countYD+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("KG")){
					countKG=countKG+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("SET")){
					countSET=countSET+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("DOZ")){
					countDOZ=countDOZ+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("PAIR")){
					countPAIR=countPAIR+1;
				}
				subRes.setMarking(origin_inbound_list.get(i).getMarking()==null?"":origin_inbound_list.get(i).getMarking());
				sublist.add(subRes);

			}

//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
//			item.setTotalBoxCountEng("TOTAL: ("+String.valueOf(total.intValue())+")"+" CTNS OF"); 
			item.setCountPCS(countPCS);
			item.setCountM(countM);	
			item.setCountYD(countYD);
			item.setCountKG(countKG);
			item.setCountSET(countSET);		
			item.setCountDOZ(countDOZ);
			item.setCountPAIR(countPAIR);
			item.setSubItem(sublist);
			return item;
		}).get();

		return result;
	}

	@Override
	public List<ExcelContainerRes> containerData(FinalInboundReq req) throws Exception {
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		int no=1;
		
		List<ExcelContainerRes> result = new ArrayList<>();
		List<InboundMasterReq> list = req.getInboundMasterData();
		for (int k = 0; k < list.size(); k++) {
			FinalInboundInboundMaster f=_FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(), list.get(k).getId());
			
			List<InboundRes> inboundList = _InboundService.getInboundByMasterId(f.getInboundMaster().getId());
			List<InboundRes> result2 = _utilService.changeExcelFormatNew(inboundList);
			InboundViewRes i = _InboundService.changeInbound(result2);
			StringBuffer sb = new StringBuffer();
			String companyNum=f.getInboundMaster().getCompanyInfo().getCoNum();
			sb.append(companyNum);
			sb.insert(3, "-");
			sb.insert(6, "-");
//			ExcelContainerRes r  = new ExcelContainerRes();
			ExcelContainerRes r  = ExcelContainerRes.builder().build();
			Date incomeDt = beforeFormat.parse(f.getFinalInbound().getIncomeDt());
			Date departDt = beforeFormat.parse(f.getFinalInbound().getDepartDtStr());
			r.setFileNm(f.getInboundMaster().getBlNo());
			r.setHblnum(""+f.getInboundMaster().getBlNo());
			r.setVesnam(""+f.getFinalInbound().getHangName());
			r.setVoynum(""+f.getFinalInbound().getHangCha());
			r.setDteonb(""+afterFormat.format(departDt));
			r.setDtearr(""+afterFormat.format(incomeDt));
			r.setHshcod(""+f.getInboundMaster().getComExport().getValue3());
			r.setHshnam(""+f.getInboundMaster().getComExport().getValue());
			r.setHshadd(""+f.getInboundMaster().getComExport().getValue2());
			r.setHcncod(""+f.getInboundMaster().getCompanyInfo().getConsignee());
			r.setHcnnam(""+f.getInboundMaster().getCompanyInfo().getCoNmEn()+"("+sb.toString()+")");
			r.setHcnadd(""+f.getInboundMaster().getCompanyInfo().getCoAddress());
			r.setHnfnam("SAME AS CONSIGNEE");
//			r.setHfwnam(""+f.getInboundMaster().getCompanyInfo().getCoNm());
//			r.setHfwadd(""+f.getInboundMaster().getCompanyInfo().getCoNmEn());
			
			for(int l=0; l<CorpType.getList().size();l++) {
				if(CorpType.getList().get(l).getId()==f.getInboundMaster().getCompanyInfo().getCorpType()) {
					r.setHfwnam(""+CorpType.getList().get(l).getName());
					r.setHfwadd(""+CorpType.getList().get(l).getCode());
					
				}else {

				}
			}
			
			
			r.setHplcod((f.getFinalInbound().getDepartPort() == null ? "" : ""+ _commonRepository.findById(f.getFinalInbound().getDepartPort()).get().getValue()));
			r.setHplnam((f.getFinalInbound().getDepartPort() == null ? "" : ""+ _commonRepository.findById(f.getFinalInbound().getDepartPort()).get().getValue2()));
			r.setHpdcod((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue()));
			r.setHpdnam((f.getFinalInbound().getIncomePort() == null ? "" : ""+ _commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue2()));
			r.setHpecod((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue()));
			r.setHpenam((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue2()));
			r.setHfncod((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue()));
			r.setHfnnam((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue2()));
			r.setHpkqty(""+decimalFormat2.format(i.getBoxCountSumD()));
//			r.setHpkunt("CTN");
			r.setHpkunt((f.getInboundMaster().getPackingType() == null ? "CTN" : ""+f.getInboundMaster().getPackingType()));
			if(r.getHpkunt().equals("CTN")) {
				r.setPackingType("CARTON");
			}else if(r.getHpkunt().equals("PL")) {
				r.setPackingType("PALLET");
			}else if(r.getHpkunt().equals("GT")) {
				r.setPackingType("OTHER");
			}
			
			
			r.setHwegwt(""+decimalFormat2.format(i.getWeightSumD()));
			r.setHwecbm(""+decimalFormat.format(i.getCbmSumD()));
			if(f.getFinalInbound().getGubun()==1) {
				r.setHfclcl("F");
			}else {
				r.setHfclcl("L");
			}
			r.setHbltyp("S");	
			if(r.getHfclcl().equals("F")) {
				r.setHsetem("1");
			}else {
				r.setHsetem("3");
			}
			if(f.getInboundMaster().getFreight()==1) {
				r.setHfttem("C");
			}else {
				r.setHfttem("P");
			}
			
			if(i.getBoxCountSumD().longValue()==0||i.getBoxCountSumD().longValue()==1) {
				r.setHblsay(""+"SAY"+" "+":"+EnglishNumberToWords.convert(i.getBoxCountSumD().longValue())+" "+"("+String.valueOf(i.getBoxCountSumD().intValue())+")"+" "+r.getPackingType()+" "+"ONLY.");
			}else {
				r.setHblsay(""+"SAY"+" "+":"+EnglishNumberToWords.convert(i.getBoxCountSumD().longValue())+" "+"("+String.valueOf(i.getBoxCountSumD().intValue())+")"+" "+r.getPackingType()+"S"+" "+"ONLY.");
			}
			
			
			
			r.setHaccod(f.getInboundMaster().getCompanyInfo().getConsignee());
			r.setHacnam(f.getInboundMaster().getCompanyInfo().getCoNmEn());
			r.setMrkmrk(inboundList.get(0).getMarking());
//			r.setContainerNo(f.getFinalInbound().getContainerNo());
//			r.setSilNo(f.getFinalInbound().getSilNo());
			String conNo = f.getFinalInbound().getContainerNo()==null||f.getFinalInbound().getContainerNo()=="" ? "":f.getFinalInbound().getContainerNo();
			String silNo = f.getFinalInbound().getSilNo()==null||f.getFinalInbound().getSilNo()=="" ? "":f.getFinalInbound().getSilNo();
			r.setCntdec(conNo+"/"+silNo);
			List <String> nameList = new ArrayList<>();
			
			
			for(int j=0; j<inboundList.size(); j++) {
				nameList.add(inboundList.get(j).getEngNm());
			}
			
			
			String hecdec = nameList.stream().distinct().collect(Collectors.joining("\n"));
			
			//		arrayList=Stream.of(nameList).distinct().collect(Collectors.toList());
			
			
			
			r.setHecdec(hecdec);
			r.setHecitm(hecdec);
			r.setHblseq(String.valueOf(no));
			r.setHblatn(f.getInboundMaster().getUser().getLoginId().toUpperCase());
			r.setHisdte(afterFormat.format(departDt));
			r.setCnecid(f.getInboundMaster().getCompanyInfo().getCoNum());
			if(r.getHnfnam().equals("SAME AS CONSIGNEE")) {
				r.setNfycid(f.getInboundMaster().getCompanyInfo().getCoNum());
			}else {
				r.setNfycid("");
			}
			
			
			result.add(r);
			no=no+1;
		}
		
		
		
		return result;
	}
	
	@Override
	public boolean container(List<ExcelContainerRes> list, HttpServletResponse response) throws Exception {

		String path = DocumentType.getList().stream().filter(t -> t.getId() == 5).findFirst().get().getName();

		try {

			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, "winsabis");
			Collections.reverse(list);
			
			
				
				XSSFSheet sheet = workbook.getSheetAt(0);

				
				// item add
				// 문서마다 시작하는 숫자가 고정
				int startCount = 2;
				for (int i = 0; i < list.size(); i++) {
					shiftRowForContainer(startCount, sheet, "D", list.get(i), workbook);
				}

				for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
					if (sheet.getMergedRegion(i).getFirstRow() == startCount+list.size()) {
						sheet.removeMergedRegion(i);
						sheet.removeMergedRegion(i);
						sheet.removeMergedRegion(i);
					}
				}
//				sheet.shiftRows(startCount+list.size()+1, sheet.getLastRowNum(), -1);
				sheet.shiftRows(startCount+list.size()+1, startCount+list.size()+1, -1);
			
			
			
			String fileName =  "WIN-SABIS.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
//			System.err.println(e.getMessage());
		}
		return true;
	}



	public void shiftRowForContainer(int startIndex, XSSFSheet sheet, String type, ExcelContainerRes item,
			XSSFWorkbook workbook) {
		writeDataForContainer(startIndex, sheet, type, item, workbook);
	}

	public void writeDataForContainer(int startIndex, XSSFSheet sheet, String type, ExcelContainerRes item,
			XSSFWorkbook workbook) {

		 
		copyRowForContainer(workbook, sheet, startIndex, startIndex, item);
		

	}

	

	

	private void copyRowForContainer(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelContainerRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);


		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				
					if (i == 0) {
						newCell.setCellValue(item.getHblnum());
					} else if (i == 1) {
						newCell.setCellValue(item.getMblnum());
					}else if (i == 2) {
						newCell.setCellValue(item.getRefnum());
					}else if (i == 3) {
						newCell.setCellValue(item.getHblios());
					}else if (i == 4) {
						newCell.setCellValue(item.getSrqnum());
					}else if (i == 5) {
						newCell.setCellValue(item.getInvnum());
					}else if (i == 6) {
						newCell.setCellValue(item.getLccnum());
					}else if (i == 7) {
						newCell.setCellValue(item.getLccdte());
					}else if (i == 8) {
						newCell.setCellValue(item.getVescod());
					}else if (i == 9) {
						newCell.setCellValue(item.getVesnam());
					}else if (i == 10) {
						newCell.setCellValue(item.getVoynum());
					}else if (i == 11) {
						newCell.setCellValue(item.getDteonb());
					}else if (i == 12) {
						newCell.setCellValue(item.getDtearr());
					}else if (i == 13) {
						newCell.setCellValue(item.getHshcod());
					}else if (i == 14) {
						newCell.setCellValue(item.getHshnam());
					}else if (i == 15) {
						newCell.setCellValue(item.getHshadd());
					}else if (i == 16) {
						newCell.setCellValue(item.getHcncod());
					}else if (i == 17) {
						newCell.setCellValue(item.getHcnnam());
					}else if (i == 18) {
						newCell.setCellValue(item.getHcnadd());
					}else if (i == 19) {
						newCell.setCellValue(item.getHnfnam());
					}else if (i == 20) {
						newCell.setCellValue(item.getHnfadd());
					}else if (i == 21) {
						newCell.setCellValue(item.getHfwnam());
					}else if (i == 22) {
						newCell.setCellValue(item.getHfwadd());
					}else if (i == 23) {
						newCell.setCellValue(item.getHprcod());
					}else if (i == 24) {
						newCell.setCellValue(item.getHprnam());
					}else if (i == 25) {
						newCell.setCellValue(item.getHprsta());
					}else if (i == 26) {
						newCell.setCellValue(item.getHplcod());
					}else if (i == 27) {
						newCell.setCellValue(item.getHplnam());
					}else if (i == 28) {
						newCell.setCellValue(item.getHplsta());
					}else if (i == 29) {
						newCell.setCellValue(item.getHpdcod());
					}else if (i == 30) {
						newCell.setCellValue(item.getHpdnam());
					}else if (i == 31) {
						newCell.setCellValue(item.getHpdsta());
					}else if (i == 32) {
						newCell.setCellValue(item.getHpecod());
					}else if (i == 33) {
						newCell.setCellValue(item.getHpenam());
					}else if (i == 34) {
						newCell.setCellValue(item.getHpesta());
					}else if (i == 35) {
						newCell.setCellValue(item.getHfncod());
					}else if (i == 36) {
						newCell.setCellValue(item.getHfnnam());
					}else if (i == 37) {
						newCell.setCellValue(item.getHfnsta());
					}else if (i == 38) {
						newCell.setCellValue(item.getHpkqty());
					}else if (i == 39) {
						newCell.setCellValue(item.getHpkunt());
					}else if (i == 40) {
						newCell.setCellValue(item.getHwegwt());
					}else if (i == 41) {
						newCell.setCellValue(item.getHwecbm());
					}else if (i == 42) {
						newCell.setCellValue(item.getHcgtyp());
					}else if (i == 43) {
						newCell.setCellValue(item.getHfclcl());
					}else if (i == 44) {
						newCell.setCellValue(item.getHbltyp());
					}else if (i == 45) {
						newCell.setCellValue(item.getHsetem());
					}else if (i == 46) {
						newCell.setCellValue(item.getHfttem());
					}else if (i == 47) {
						newCell.setCellValue(item.getHbrcod());
					}else if (i == 48) {
						newCell.setCellValue(item.getHbrnam());
					}else if (i == 49) {
						newCell.setCellValue(item.getPacdec());
					}else if (i == 50) {
						newCell.setCellValue(item.getHblsay());
					}else if (i == 51) {
						newCell.setCellValue(item.getHslcls());
					}else if (i == 52) {
						newCell.setCellValue(item.getHblcur());
					}else if (i == 53) {
						newCell.setCellValue(item.getHblexr());
					}else if (i == 54) {
						newCell.setCellValue(item.getHaccod());
					}else if (i == 55) {
						newCell.setCellValue(item.getHacnam());
					}else if (i == 56) {
						newCell.setCellValue(item.getHpicod());
					}else if (i == 57) {
						newCell.setCellValue(item.getHpinam());
					}else if (i == 58) {
						newCell.setCellValue(item.getHpycod());
					}else if (i == 59) {
						newCell.setCellValue(item.getHpynam());
					}else if (i == 60) {
						newCell.setCellValue(item.getHdccod());
					}else if (i == 61) {
						newCell.setCellValue(item.getHdcnam());
					}else if (i == 62) {
						newCell.setCellValue(item.getHapcod());
					}else if (i == 63) {
						newCell.setCellValue(item.getHapnam());
					}else if (i == 64) {
						newCell.setCellValue(item.getSlscod());
					}else if (i == 65) {
						newCell.setCellValue(item.getSlsnam());
					}else if (i == 66) {
						newCell.setCellValue(item.getCntdec());
					}else if (i == 67) {
						newCell.setCellValue(item.getHponum());
					}else if (i == 68) {
						newCell.setCellValue(item.getMrkmrk());
					}else if (i == 69) {
						newCell.setCellValue(item.getHecdec());
					}else if (i == 70) {
						newCell.setCellValue(item.getHctcod());
					}else if (i == 71) {
						newCell.setCellValue(item.getHctqty());
					}else if (i == 72) {
						newCell.setCellValue(item.getHblseq());
					}else if (i == 73) {
						newCell.setCellValue(item.getHblatn());
					}else if (i == 74) {
						newCell.setCellValue(item.getHisdte());
					}else if (i == 75) {
						newCell.setCellValue(item.getShpcid());
					}else if (i == 76) {
						newCell.setCellValue(item.getCnecid());
					}else if (i == 77) {
						newCell.setCellValue(item.getNfycid());
					}else if (i == 78) {
						newCell.setCellValue(item.getTemcod());
					}else if (i == 79) {
						newCell.setCellValue(item.getSvccls());
					}else if (i == 80) {
						newCell.setCellValue(item.getPrjcod());
					}else if (i == 81) {
						newCell.setCellValue(item.getPftdat());
					}else if (i == 82) {
						newCell.setCellValue(item.getHecitm());
					} else {
						newCell.setCellValue(oldCell.getStringCellValue());
					}
				

//                    newCell.setCellValue(oldCell.getRichStringCellValue());
//                    newCell.setCellValue(item.get);
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}
	
	@Override
	public List<InboundRes> inboundData(InboundReq req) throws Exception {
		List<InboundRes> list = _InboundService.getInboundByInboundMasterId(req);
		//출력모드
		List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		
		return  result;

	}
	
	@Override
	public boolean inbound(List<InboundRes> list, HttpServletResponse response) throws Exception {
		String path = DocumentType.getList().stream().filter(t -> t.getId() == 6).findFirst().get().getName();

		try {

			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, "INBOUND");
//			Collections.reverse(list);
			
			
				
			XSSFSheet sheet = workbook.getSheetAt(0);
			sheet.setDefaultRowHeightInPoints(new Float("25"));		
			XSSFSheet sheet2 = workbook.getSheetAt(1);
			step01ForInbound(list, sheet, workbook);
			copyRowOtherSheetForInbound(workbook, sheet, list.size()+1,sheet2, 0);	
			step02ForInbound(list, sheet, workbook);
			
			sheet.getRow(0).setHeightInPoints(new Float("25"));
			sheet.getRow(list.size()+1).setHeightInPoints(new Float("25"));
			if(list.size()==1) {
				sheet.getRow(1).setHeightInPoints(new Float("50"));
			}else {
				for(int i=0; i<list.size(); i++) {
					sheet.getRow(i+1).setHeightInPoints(new Float("25"));
				}
						}
			for(int i=0; i<list.size(); i++) {
				
				int startNum = i+2;
				String a = "F";
				String b = String.valueOf(startNum);
				String c = "J";
				sheet.getRow(startNum-1).getCell(16).setCellFormula(a+b+"*"+c+b);
			}
			sheet.getRow(list.size()+1).getCell(5).setCellFormula("SUM(F"+"2"+":"+"F"+String.valueOf(list.size()+1)+")");
			sheet.getRow(list.size()+1).getCell(6).setCellFormula("SUM(G"+"2"+":"+"G"+String.valueOf(list.size()+1)+")");
			sheet.getRow(list.size()+1).getCell(7).setCellFormula("SUM(H"+"2"+":"+"H"+String.valueOf(list.size()+1)+")");
			sheet.getRow(list.size()+1).getCell(8).setCellFormula("SUM(I"+"2"+":"+"I"+String.valueOf(list.size()+1)+")");
			sheet.getRow(list.size()+1).getCell(16).setCellFormula("SUM(Q"+"2"+":"+"Q"+String.valueOf(list.size()+1)+")");
			String fileName =  "INBOUND.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.removeSheetAt(1);
			//행시작, 행종료, 열시작, 열종료 (자바배열과 같이 0부터 시작)
//			sheet.addMergedRegion(new CellRangeAddress(list.size()+1,list.size()+1,0,4));
//			sheet.addMergedRegion(new CellRangeAddress(list.size()+1,list.size()+1,18,19));
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	private void step01ForInbound(List<InboundRes> resource, XSSFSheet sheet, XSSFWorkbook workbook) {

		Font font = workbook.createFont();
		font.setColor(IndexedColors.RED.getIndex());
		font.setFontName("굴림체");
		Font font1 = workbook.createFont();
		font1.setColor(IndexedColors.BLACK.getIndex());
		font1.setFontName("굴림체");
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);;
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.HAIR);	
		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFont(font1);
		cellStyle1.setAlignment(HorizontalAlignment.CENTER);
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle1.setBorderBottom(BorderStyle.THIN);
		cellStyle1.setBorderRight(BorderStyle.THIN);
		cellStyle1.setBorderTop(BorderStyle.HAIR);	
		// item 채우기
		for (int i = 0; i < resource.size(); i++) {

			if ((i + 1) == resource.size()) {

			} else {
				
				copyRowForInbound(workbook, sheet, i + 1, i + 2);
			}


			XSSFRow row = sheet.getRow(i + 1);
//			if(resource.size()==1) {
//				row.setHeight((short)2000);
//			}else {
//				row.setHeight((short)500);
//			}
			XSSFCellStyle orderNoFont  = workbook.createCellStyle();
			XSSFCellStyle allCellStyle = workbook.createCellStyle();
			XSSFCellStyle workDateStrCellStyle = workbook.createCellStyle();
			XSSFCellStyle companyNmCellStyle = workbook.createCellStyle();
			XSSFCellStyle markingCellStyle = workbook.createCellStyle();
			XSSFCellStyle korNmCellStyle = workbook.createCellStyle();
			XSSFCellStyle itemCountCellStyle = workbook.createCellStyle();
			XSSFCellStyle boxCountCellStyle = workbook.createCellStyle();
			XSSFCellStyle weightCellStyle = workbook.createCellStyle();
			XSSFCellStyle cbmCellStyle = workbook.createCellStyle();
			XSSFCellStyle reportPriceCellStyle = workbook.createCellStyle();
			XSSFCellStyle memo1CellStyle = workbook.createCellStyle();
			XSSFCellStyle memo2CellStyle = workbook.createCellStyle();
			XSSFCellStyle itemNoCellStyle = workbook.createCellStyle();
			XSSFCellStyle jejilCellStyle = workbook.createCellStyle();
			XSSFCellStyle hsCodeCellStyle = workbook.createCellStyle();
			XSSFCellStyle totalPriceCellStyle = workbook.createCellStyle();
			XSSFCellStyle engNmCellStyle = workbook.createCellStyle();
			Font allFont = workbook.createFont();
			allFont.setFontName("굴림체");
			Font otherFont = workbook.createFont();
			otherFont.setFontName("굴림체");
//			orderNoFont.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
			orderNoFont.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204)));
			orderNoFont.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			orderNoFont.setAlignment(HorizontalAlignment.CENTER);
			orderNoFont.setVerticalAlignment(VerticalAlignment.CENTER);
			orderNoFont.setBorderBottom(BorderStyle.HAIR);
			orderNoFont.setBorderRight(BorderStyle.HAIR);
			orderNoFont.setBorderLeft(BorderStyle.THIN);
				
			orderNoFont.setFont(allFont);
			HSSFWorkbook hwb = new HSSFWorkbook();
			
			
			if(resource.get(i).getColorId()==0) {
				allFont.setColor(IndexedColors.BLACK.getIndex());
			}else if(resource.get(i).getColorId()==1){
				allFont.setColor(IndexedColors.BLUE.getIndex());
			}else if(resource.get(i).getColorId()==2){
				allFont.setColor(IndexedColors.RED.getIndex());
			}else if(resource.get(i).getColorId()==3){
				allFont.setColor(IndexedColors.GREEN.getIndex());
			}else if(resource.get(i).getColorId()==4){
				allFont.setColor(IndexedColors.PINK.getIndex());
			}else if(resource.get(i).getColorId()==5){
				allFont.setColor(IndexedColors.ORANGE.getIndex());
			}else if(resource.get(i).getColorId()==6){
				allFont.setColor(IndexedColors.VIOLET.getIndex());
			}else if(resource.get(i).getColorId()==7){
				HSSFPalette palette = hwb.getCustomPalette();
				// get the color which most closely matches the color you want to use
				HSSFColor myColor = palette.findSimilarColor(0, 216, 255);
				// get the palette index of that color 
				short palIndex = myColor.getIndex();
				allFont.setColor(palIndex);
			}else if(resource.get(i).getColorId()==8){
				HSSFPalette palette = hwb.getCustomPalette();
				// get the color which most closely matches the color you want to use
				HSSFColor myColor = palette.findSimilarColor(171, 242, 0);
				// get the palette index of that color 
				short palIndex = myColor.getIndex();
				allFont.setColor(palIndex);
			}else if(resource.get(i).getColorId()==9){
				HSSFPalette palette = hwb.getCustomPalette();
				// get the color which most closely matches the color you want to use
				HSSFColor myColor = palette.findSimilarColor(138, 73, 36);
				// get the palette index of that color 
				short palIndex = myColor.getIndex();
				allFont.setColor(palIndex);
			}
			allCellStyle.setAlignment(HorizontalAlignment.CENTER);
			allCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			allCellStyle.setBorderBottom(BorderStyle.HAIR);
			allCellStyle.setBorderRight(BorderStyle.HAIR);
		
			allCellStyle.setFont(allFont);
			if(resource.get(i).getWorkDateStrYn().equals("Y")){
				workDateStrCellStyle.setAlignment(HorizontalAlignment.CENTER);
				workDateStrCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				workDateStrCellStyle.setBorderBottom(BorderStyle.NONE);
				workDateStrCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				workDateStrCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				workDateStrCellStyle.setFont(allFont);
				workDateStrCellStyle.setBorderBottom(BorderStyle.HAIR);
				workDateStrCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getWorkDateStrYn()==null||resource.get(i).getWorkDateStrYn().equals("")){
				workDateStrCellStyle.setAlignment(HorizontalAlignment.CENTER);
				workDateStrCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				workDateStrCellStyle.setBorderBottom(BorderStyle.NONE);
				workDateStrCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				workDateStrCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				workDateStrCellStyle.setFont(allFont);
				workDateStrCellStyle.setBorderBottom(BorderStyle.HAIR);
				workDateStrCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			
			if(resource.get(i).getCompanyNmYn().equals("Y")){
				companyNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				companyNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				companyNmCellStyle.setBorderBottom(BorderStyle.NONE);
				companyNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				companyNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				companyNmCellStyle.setFont(allFont);
				companyNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				companyNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				companyNmCellStyle.setWrapText(true);
				
				
			}else if(resource.get(i).getCompanyNmYn()==null||resource.get(i).getCompanyNmYn().equals("")){
				companyNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				companyNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				companyNmCellStyle.setBorderBottom(BorderStyle.NONE);
				companyNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				companyNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				companyNmCellStyle.setFont(allFont);
				companyNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				companyNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				companyNmCellStyle.setWrapText(true);
			}
			if(resource.get(i).getMarkingYn().equals("Y")){
				markingCellStyle.setAlignment(HorizontalAlignment.CENTER);
				markingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				markingCellStyle.setBorderBottom(BorderStyle.NONE);
				markingCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				markingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				markingCellStyle.setFont(allFont);
				markingCellStyle.setBorderBottom(BorderStyle.HAIR);
				markingCellStyle.setBorderRight(BorderStyle.HAIR); 
				markingCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getMarkingYn()==null||resource.get(i).getMarkingYn().equals("")){
				markingCellStyle.setAlignment(HorizontalAlignment.CENTER);
				markingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				markingCellStyle.setBorderBottom(BorderStyle.NONE);
				markingCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				markingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				markingCellStyle.setFont(allFont);
				markingCellStyle.setBorderBottom(BorderStyle.HAIR);
				markingCellStyle.setBorderRight(BorderStyle.HAIR); 
				markingCellStyle.setWrapText(true);
			}
			if(resource.get(i).getKorNmYn().equals("Y")){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getKorNmYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getKorNmYn()==null||resource.get(i).getKorNmYn().equals("")){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
			}
			if(resource.get(i).getItemCountYn().equals("Y")){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getItemCountYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getItemCountYn()==null||resource.get(i).getItemCountYn().equals("")){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getBoxCountYn().equals("Y")){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBoxCountYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBoxCountYn()==null||resource.get(i).getBoxCountYn().equals("")){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getWeightYn().equals("Y")){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getWeightYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getWeightYn()==null||resource.get(i).getWeightYn().equals("")){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getCbmYn().equals("Y")){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getCbmYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getCbmYn()==null||resource.get(i).getCbmYn().equals("")){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getReportPriceYn().equals("Y")){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				reportPriceCellStyle.setFont(otherFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getReportPriceYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				reportPriceCellStyle.setFont(otherFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				reportPriceCellStyle.setFont(otherFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getReportPriceYn()==null||resource.get(i).getReportPriceYn().equals("")){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				reportPriceCellStyle.setFont(otherFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getMemo1Yn().equals("Y")){
				memo1CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo1CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo1CellStyle.setBorderBottom(BorderStyle.NONE);
				memo1CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				memo1CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo1CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo1CellStyle.setBorderRight(BorderStyle.HAIR); 
				memo1CellStyle.setWrapText(true);
				memo1CellStyle.setFont(otherFont);
				
			}else {
				memo1CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo1CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo1CellStyle.setBorderBottom(BorderStyle.NONE);
				memo1CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				memo1CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo1CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo1CellStyle.setBorderRight(BorderStyle.HAIR); 
				memo1CellStyle.setWrapText(true);
				memo1CellStyle.setFont(otherFont);
			}
			if(resource.get(i).getMemo2Yn().equals("Y")){
				memo2CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo2CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo2CellStyle.setBorderBottom(BorderStyle.NONE);
				memo2CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				memo2CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo2CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo2CellStyle.setBorderRight(BorderStyle.HAIR); 
				memo2CellStyle.setWrapText(true);
				memo2CellStyle.setFont(otherFont);
				
			}else {
				memo2CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo2CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo2CellStyle.setBorderBottom(BorderStyle.NONE);
				memo2CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				memo2CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo2CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo2CellStyle.setBorderRight(BorderStyle.HAIR); 
				memo2CellStyle.setWrapText(true);
				memo2CellStyle.setFont(otherFont);
			}
			if(resource.get(i).getItemNoYn().equals("Y")){
				itemNoCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemNoCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemNoCellStyle.setBorderBottom(BorderStyle.NONE);
				itemNoCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				itemNoCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemNoCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemNoCellStyle.setBorderRight(BorderStyle.HAIR); 
				itemNoCellStyle.setWrapText(true);
				itemNoCellStyle.setFont(otherFont);
				
			}else {
				itemNoCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemNoCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemNoCellStyle.setBorderBottom(BorderStyle.NONE);
				itemNoCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				itemNoCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemNoCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemNoCellStyle.setBorderRight(BorderStyle.HAIR); 
				itemNoCellStyle.setWrapText(true);
				itemNoCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getJejilYn().equals("Y")){
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getJejilYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
				
			}else {
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getHsCodeYn().equals("Y")){
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR); 
				hsCodeCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getHsCodeYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR);
				hsCodeCellStyle.setFont(otherFont);
			}else if(resource.get(i).getBackgroundColorId()==1){
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR); 
				hsCodeCellStyle.setFont(otherFont);
			}else {
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR); 
				hsCodeCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getTotalPriceYn().equals("Y")){
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getTotalPriceYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
			}else if(resource.get(i).getBackgroundColorId()==1){
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
			}else {
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getEngNmYn().equals("Y")){
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
			}else if(resource.get(i).getEngNmYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
			}else if(resource.get(i).getBackgroundColorId()==1){
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
			}else {
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
			}
			
			row.getCell(0).setCellValue(resource.get(i).getOrderNoStr());
			row.getCell(0).setCellStyle(orderNoFont);
			row.getCell(1).setCellValue(resource.get(i).getWorkDateStr());
			row.getCell(1).setCellStyle(workDateStrCellStyle);
			row.getCell(2).setCellValue(resource.get(i).getCompanyNm());
			row.getCell(2).setCellStyle(companyNmCellStyle);
			row.getCell(3).setCellValue(resource.get(i).getMarking());
			row.getCell(3).setCellStyle(markingCellStyle);
			row.getCell(4).setCellValue(resource.get(i).getKorNm());
			row.getCell(4).setCellStyle(korNmCellStyle);
			if(resource.get(i).getAmountType().equals("PCS")) {
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
//				row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				}
			}else if(resource.get(i).getAmountType().equals("M")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"M\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"M\""));
				}
			}else if(resource.get(i).getAmountType().equals("YD")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"YD\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"YD\""));
				}
			}else if(resource.get(i).getAmountType().equals("KG")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"KG\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"KG\""));
				}
			}else if(resource.get(i).getAmountType().equals("SET")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"SET\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"SET\""));
				}
			}else if(resource.get(i).getAmountType().equals("DOZ")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"DOZ\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"DOZ\""));
				}
			}else if(resource.get(i).getAmountType().equals("PAIR")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"PAIR\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"PAIR\""));
				}
			}
			
			
			
			
			if (resource.get(i).getBoxCount() == null) {
				row.getCell(6).setCellValue(0);
				row.getCell(6).setCellStyle(boxCountCellStyle);
//				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				//20220726
				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
			} else {
				row.getCell(6).setCellValue(resource.get(i).getBoxCount());
				row.getCell(6).setCellStyle(boxCountCellStyle);
//				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				if(getDoubleResult(resource.get(i).getBoxCount()).equals("#,##0_ ")) {
					row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
				}else {
					row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				}
			}

			row.getCell(7).setCellValue(resource.get(i).getWeight() == null ? 0 :resource.get(i).getWeight());
			row.getCell(7).setCellStyle(weightCellStyle);
//			row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
//			row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(i).getWeight())));
			if(getDoubleResult(resource.get(i).getWeight()).equals("#,##0_ ")) {
				row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
			}else {
				row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
			}
			row.getCell(8).setCellValue(resource.get(i).getCbm() == null ? 0 :resource.get(i).getCbm());
			row.getCell(8).setCellStyle(cbmCellStyle);
			row.getCell(8).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("0.000_ "));
			
						
			
			if (resource.get(i).getReportPrice() == null) {
			} else {
				if(resource.get(i).getCurrencyType().equals("$")){
					row.getCell(9).setCellValue(resource.get(i).getReportPrice());
					row.getCell(9).setCellStyle(reportPriceCellStyle);
					row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_-[$$-en-US]* #,##0.000_ ;_-[$$-en-US]* -#,##0.000 ;_-[$$-en-US]* \"-\"???_ ;_-@_ "));
				}else {
					row.getCell(9).setCellValue(resource.get(i).getReportPrice());
					row.getCell(9).setCellStyle(reportPriceCellStyle);
					row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ "));
				}
				
			}
			row.getCell(10).setCellValue(resource.get(i).getMemo1());
			row.getCell(10).setCellStyle(memo1CellStyle);
			row.getCell(11).setCellValue(resource.get(i).getMemo2());
			row.getCell(11).setCellStyle(memo2CellStyle);
			row.getCell(12).setCellValue(resource.get(i).getItemNo());
			row.getCell(12).setCellStyle(itemNoCellStyle);
			row.getCell(13).setCellValue(resource.get(i).getJejil());
			row.getCell(13).setCellStyle(jejilCellStyle);
			row.getCell(14).setCellValue(resource.get(i).getHsCode());
			row.getCell(14).setCellStyle(hsCodeCellStyle);
			row.getCell(15).setCellValue(resource.get(i).getCoCode());
			if(resource.get(i).getCoCode().equals("안받음")) {
				row.getCell(15).setCellStyle(cellStyle1);
			}else {
				row.getCell(15).setCellStyle(cellStyle);
			}
			
			row.getCell(15).getCellStyle().setBorderBottom(BorderStyle.HAIR);
			row.getCell(15).getCellStyle().setBorderRight(BorderStyle.HAIR); 
			if (resource.get(i).getTotalPrice() == null) {
				row.getCell(16).setCellValue("");
				row.getCell(16).setCellStyle(totalPriceCellStyle);
				
			} else {
				if(resource.get(i).getCurrencyType().equals("$")){
					row.getCell(16).setCellValue(resource.get(i).getTotalPrice());
					row.getCell(16).setCellStyle(totalPriceCellStyle);
					row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_-[$$-en-US]* #,##0.000_ ;_-[$$-en-US]* -#,##0.000 ;_-[$$-en-US]* \"-\"???_ ;_-@_ "));
				}else {
					row.getCell(16).setCellValue(resource.get(i).getTotalPrice());
					row.getCell(16).setCellStyle(totalPriceCellStyle);
					row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ "));
				}
				
			}

			row.getCell(17).setCellValue(resource.get(i).getEngNm());
			row.getCell(17).setCellStyle(engNmCellStyle);
			row.getCell(18).setCellValue(resource.get(i).getMasterCompany());
			row.getCell(18).getCellStyle().setBorderBottom(BorderStyle.HAIR);
			row.getCell(18).getCellStyle().setBorderRight(BorderStyle.HAIR);
			if (resource.get(i).getBlNo() == null || resource.get(i).getBlNo() == "") {
				row.getCell(19).setCellValue("");
				row.getCell(19).getCellStyle().setBorderBottom(BorderStyle.HAIR);
				row.getCell(19).getCellStyle().setBorderRight(BorderStyle.THIN);
			} else {
				row.getCell(19).setCellValue(resource.get(i).getBlNo());
				row.getCell(19).getCellStyle().setBorderBottom(BorderStyle.HAIR);
				row.getCell(19).getCellStyle().setBorderRight(BorderStyle.THIN);
			}

		}
		// merge 규칙
		for (int i = 0; i < resource.size(); i++) {

			XSSFRow row = sheet.getRow(i + 1);
			if (resource.get(i).getOrderNoStrSpan() > 1) {
//				sheet.addMergedRegion(new CellRangeAddress(1, 4, 0, 1)); // 열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getOrderNoStrSpan()), 0, 0));
			}
			if (resource.get(i).getWorkDateStrSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getWorkDateStrSpan()), 1, 1));
			}
			if (resource.get(i).getCompanyNmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getCompanyNmSpan()), 2, 2));
			}
			if (resource.get(i).getMarkingSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getMarkingSpan()), 3, 3));
			}
			if (resource.get(i).getKorNmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getKorNmSpan()), 4, 4));
			}
			if (resource.get(i).getItemCountSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getItemCountSpan()), 5, 5));
			}
			if (resource.get(i).getBoxCountSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getBoxCountSpan()), 6, 6));
			}
			if (resource.get(i).getWeightSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getWeightSpan()), 7, 7));
			}
			if (resource.get(i).getCbmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getCbmSpan()), 8, 8));
			}
			if (resource.get(i).getReportPriceSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getReportPriceSpan()), 9, 9));
			}
			if (resource.get(i).getMemo1Span() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getMemo1Span()), 10, 10));
			}
			if (resource.get(i).getMemo2Span() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getMemo2Span()), 11, 11));
			}
			if (resource.get(i).getItemNoSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getItemNoSpan()), 12, 12));
			}
			if (resource.get(i).getJejilSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getJejilSpan()), 13, 13));
			}
			if (resource.get(i).getHsCodeSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getHsCodeSpan()), 14, 14));
			}
//			if (resource.get(i).getCoIdSpan() > 1) {
//				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getCoIdSpan()), 15, 15));
//			}
			if (resource.get(i).getTotalPriceSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getTotalPriceSpan()), 16, 16));
			}
			if (resource.get(i).getEngNmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getEngNmSpan()), 17, 17));
			}
			if (resource.get(i).getMasterCompanySpan() > 1) {
				sheet.addMergedRegion(
						new CellRangeAddress((i + 1), (i + resource.get(i).getMasterCompanySpan()), 18, 18));
			}
			if (resource.get(i).getBlNoSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getBlNoSpan()), 19, 19));
			}
		}

	}
	

	private void step02ForInbound(List<InboundRes> resource, XSSFSheet sheet, XSSFWorkbook workbook) {


		// item 채우기
//		for (int i = 0; i < resource.size(); i++) {

//			if ((i + 1) == resource.size()) {
//
//			} else {
//				
//				copyRowForInbound(workbook, sheet, i + 1, i + 2);
//			}


			XSSFRow row = sheet.getRow(resource.size() + 1);
			
			row.getCell(5).setCellValue(resource.get(0).getItemCountSum());
//			row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
			row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(0).getItemCountSum())));
			if (resource.get(0).getBoxCountSum() == null) {
			} else {
				row.getCell(6).setCellValue(resource.get(0).getBoxCountSum());
//				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(0).getBoxCountSum())));
			}

			row.getCell(7).setCellValue(resource.get(0).getWeightSum());
//			row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
			row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(0).getWeightSum())));
			row.getCell(8).setCellValue(resource.get(0).getCbmSum());
			

			if (resource.get(0).getTotalPriceSum() == null) {
				row.getCell(16).setCellValue("");
			} else {
//				row.getCell(16).setCellValue(resource.get(0).getTotalPriceSum());
				if(resource.get(0).getCurrencyType().equals("$")){
					row.getCell(16).setCellValue(resource.get(0).getTotalPriceSum());
				}else {
					row.getCell(16).setCellValue(resource.get(0).getTotalPriceSum());
					row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ "));
				}
			}

			row.getCell(17).setCellValue(resource.get(0).getFreight());
			row.getCell(18).setCellValue("담당자"+" "+":"+" "+resource.get(0).getManagerNm());
			

//		}


	}
	
	private void copyRowForInbounds(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
		// Get the source / new row
				XSSFRow newRow = worksheet.getRow(destinationRowNum);
				XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

				// If the row exist in destination, push down all rows by 1 else create a new
				// row
				if (newRow != null) {
					worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
				} else {
					newRow = worksheet.createRow(destinationRowNum);
				}

				// Loop through source columns to add to new row
				for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
					// Grab a copy of the old/new cell
					XSSFCell oldCell = sourceRow.getCell(i);
					XSSFCell newCell = newRow.createCell(i);

					// If the old cell is null jump to next cell
					if (oldCell == null) {
						newCell = null;
						continue;
					}

					// Copy style from old cell and apply to new cell
					XSSFCellStyle newCellStyle = workbook.createCellStyle();
					newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
					;
					newCell.setCellStyle(newCellStyle);

					// If there is a cell comment, copy
					if (oldCell.getCellComment() != null) {
						newCell.setCellComment(oldCell.getCellComment());
					}

					// If there is a cell hyperlink, copy
					if (oldCell.getHyperlink() != null) {
						newCell.setHyperlink(oldCell.getHyperlink());
					}

					// Set the cell data type
					newCell.setCellType(oldCell.getCellType());

					// Set the cell data value
					switch (oldCell.getCellType()) {
					case Cell.CELL_TYPE_BLANK:
						newCell.setCellValue(oldCell.getStringCellValue());
						break;
					case Cell.CELL_TYPE_BOOLEAN:
						newCell.setCellValue(oldCell.getBooleanCellValue());
						break;
					case Cell.CELL_TYPE_ERROR:
						newCell.setCellErrorValue(oldCell.getErrorCellValue());
						break;
					case Cell.CELL_TYPE_FORMULA:
						newCell.setCellFormula(oldCell.getCellFormula());
						break;
					case Cell.CELL_TYPE_NUMERIC:
						newCell.setCellValue(oldCell.getNumericCellValue());
						break;
					case Cell.CELL_TYPE_STRING:
						newCell.setCellValue(oldCell.getRichStringCellValue());
						break;
					}
				}
	}
	private void copyRowForInbound(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
		if (newRow != null) {
			worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		} else {
			newRow = worksheet.createRow(destinationRowNum);
		}

		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
		}
	}
	
	 private void copyRowOtherSheetForInbounds(XSSFWorkbook workbook, XSSFSheet worksheet,int destinationRowNum, XSSFSheet worksheet2,int sourceRowNum) {
		// Get the source / new row
			XSSFRow newRow = worksheet.getRow(destinationRowNum);
			XSSFRow sourceRow = worksheet2.getRow(sourceRowNum);

			if (newRow != null) {
				worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
			} else {
				newRow = worksheet.createRow(destinationRowNum);
			}

			// Loop through source columns to add to new row
			for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
				// Grab a copy of the old/new cell
				XSSFCell oldCell = sourceRow.getCell(i);
				XSSFCell newCell = newRow.createCell(i);

				// If the old cell is null jump to next cell
				if (oldCell == null) {
					newCell = null;
					continue;
				}

				// Copy style from old cell and apply to new cell
				XSSFCellStyle newCellStyle = workbook.createCellStyle();
				newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
				;
				newCell.setCellStyle(newCellStyle);

				// If there is a cell comment, copy
				if (oldCell.getCellComment() != null) {
					newCell.setCellComment(oldCell.getCellComment());
				}

				// If there is a cell hyperlink, copy
				if (oldCell.getHyperlink() != null) {
					newCell.setHyperlink(oldCell.getHyperlink());
				}

				// Set the cell data type
				newCell.setCellType(oldCell.getCellType());

				// Set the cell data value
				switch (oldCell.getCellType()) {
				case Cell.CELL_TYPE_BLANK:
					newCell.setCellValue(oldCell.getStringCellValue());
					break;
				case Cell.CELL_TYPE_BOOLEAN:
					newCell.setCellValue(oldCell.getBooleanCellValue());
					break;
				case Cell.CELL_TYPE_ERROR:
					newCell.setCellErrorValue(oldCell.getErrorCellValue());
					break;
				case Cell.CELL_TYPE_FORMULA:
					newCell.setCellFormula(oldCell.getCellFormula());
					break;
				case Cell.CELL_TYPE_NUMERIC:
					newCell.setCellValue(oldCell.getNumericCellValue());
					break;
				case Cell.CELL_TYPE_STRING:
					newCell.setCellValue(oldCell.getRichStringCellValue());
					break;
				}
			}
			
			for (int i = 0; i < worksheet2.getMergedRegions().size(); i++) {
		           CellRangeAddress cellRangeAddress = worksheet2.getMergedRegion(i);
		               CellRangeAddress newCellRangeAddress = new CellRangeAddress(
		               		destinationRowNum,destinationRowNum,
		                       cellRangeAddress.getFirstColumn(),
		                       cellRangeAddress.getLastColumn());
		               worksheet.addMergedRegion(newCellRangeAddress);
		       }
	}
	
	 private void copyRowOtherSheetForInbounds2(XSSFWorkbook workbook, XSSFSheet worksheet,int destinationRowNum, XSSFSheet worksheet2,int sourceRowNum) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum+1);
		XSSFRow sourceRow = worksheet2.getRow(sourceRowNum);
		
		// If the row exist in destination, push down all rows by 1 else create a new
		// row
		if (newRow != null) {
			worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		} else {
			newRow = worksheet.createRow(destinationRowNum-1);
		}

		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
		}
		for (int i = 0; i < worksheet2.getMergedRegions().size(); i++) {
           CellRangeAddress cellRangeAddress = worksheet2.getMergedRegion(i);
               CellRangeAddress newCellRangeAddress = new CellRangeAddress(
               		destinationRowNum,destinationRowNum,
                       cellRangeAddress.getFirstColumn(),
                       cellRangeAddress.getLastColumn());
               worksheet.addMergedRegion(newCellRangeAddress);
       }
		
	}
	 private void copyRowOtherSheetForInbound(XSSFWorkbook workbook, XSSFSheet worksheet,int destinationRowNum, XSSFSheet worksheet2,int sourceRowNum) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet2.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
		if (newRow != null) {
			worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		} else {
			newRow = worksheet.createRow(destinationRowNum);
		}

		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
		}
		for (int i = 0; i < worksheet2.getMergedRegions().size(); i++) {
            CellRangeAddress cellRangeAddress = worksheet2.getMergedRegion(i);
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(
                		destinationRowNum,destinationRowNum,
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
        }
		
	}
	
	
	
	@Override
	public List<ExcelCLPRes> clpData(FinalInboundReq req) throws Exception {
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyyMMdd");
		SimpleDateFormat beforeFormat = new SimpleDateFormat("yyyy-MM-dd");
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		
		
		List<ExcelCLPRes> result = new ArrayList<>();
		List<InboundMasterReq> list = req.getInboundMasterData();
		for (int k = 0; k < list.size(); k++) {
			FinalInboundInboundMaster f=_FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(), list.get(k).getId());
			
			List<InboundRes> inboundList = _InboundService.getInboundByMasterId(f.getInboundMaster().getId());
			List<InboundRes> result2 = _utilService.changeExcelFormatNew(inboundList);
			InboundViewRes i = _InboundService.changeInbound(result2);

			
			for(int l=0; l<inboundList.size(); l++) {
				ExcelCLPRes r  = ExcelCLPRes.builder().build();
				r.setNo(list.get(k).getNo());
				r.setIncomeDt(f.getFinalInbound().getIncomeDt());
				r.setChulhangPort(f.getFinalInbound().getChulhangPort());
				r.setContainerNo(f.getFinalInbound().getContainerNo());
				r.setSilNo(f.getFinalInbound().getSilNo());
				r.setCompanyNm(f.getInboundMaster().getCompanyInfo().getCoNm());
				r.setBlNo(f.getInboundMaster().getBlNo());
				r.setMarking(inboundList.get(l).getMarking());
				r.setBoxCount(inboundList.get(l).getBoxCount());
				r.setCbm(inboundList.get(l).getCbm());
				r.setEngNm(inboundList.get(l).getEngNm());
				r.setMemo1(inboundList.get(l).getMemo1());
				r.setCorpId(f.getFinalInbound().getCorpId());
				result.add(r);
				}
	
		}
		
		
		
		return result;
	}
	
	
	@Override
	public boolean clp(List<ExcelCLPRes> list, HttpServletResponse response) throws Exception {
		String path = DocumentType.getList().stream().filter(t -> t.getId() == 7).findFirst().get().getName();

		try {
			List<Integer> no = new ArrayList<>();
			List<Integer> rowNum = new ArrayList<>();
			
			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, "CLP");
			
			Collections.reverse(list);
			XSSFSheet sheet = workbook.getSheetAt(0);
				if(list.get(0).getCorpId()==1) {
					 sheet = workbook.getSheetAt(1);
				}else if(list.get(0).getCorpId()==2){
					 sheet = workbook.getSheetAt(0);
				}
				

				
				// item add
				// 문서마다 시작하는 숫자가 고정
				int startCount = 7;
				for (int i = 0; i < list.size(); i++) {
					shiftRowForCLP(startCount, sheet, "D", list.get(i), workbook);
				}
				
				chageDataforCLP(sheet, list.get(0), workbook);

//				for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
//					if (sheet.getMergedRegion(i).getFirstRow() == startCount+list.size()) {
//						sheet.removeMergedRegion(i);
//						sheet.removeMergedRegion(i);
//						sheet.removeMergedRegion(i);
//					}
//				}
				sheet.shiftRows(7, sheet.getLastRowNum(), -1);
			
				
				for (int i = 0; i < list.size(); i++) {
					no.add(list.get(i).getNo());
				}
				Collections.reverse(no);
				for (int i = 0; i < no.size()-1; i++) {
					
					if(no.get(i)==no.get(i+1)) {
						
					}else {
						rowNum.add(i);
					}
				}
				for(int i=0; i<rowNum.size(); i++) {
					sheet.getRow(rowNum.get(i)+6).getCell(0).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(0).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
					sheet.getRow(rowNum.get(i)+6).getCell(1).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(1).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
					sheet.getRow(rowNum.get(i)+6).getCell(2).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(2).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
					sheet.getRow(rowNum.get(i)+6).getCell(3).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(3).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
					sheet.getRow(rowNum.get(i)+6).getCell(4).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(4).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
					sheet.getRow(rowNum.get(i)+6).getCell(5).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(5).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
					sheet.getRow(rowNum.get(i)+6).getCell(6).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(6).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
					sheet.getRow(rowNum.get(i)+6).getCell(7).getCellStyle().setBorderBottom(BorderStyle.MEDIUM);
					sheet.getRow(rowNum.get(i)+6).getCell(7).getCellStyle().setBottomBorderColor(IndexedColors.RED.getIndex());	
				}
			
			String fileName =  "CLP.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			if(list.get(0).getCorpId()==1) {
				workbook.removeSheetAt(0);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
			}else {
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
				workbook.removeSheetAt(1);
			}
			
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return true;
	}
	
	
	
	public void shiftRowForCLP(int startIndex, XSSFSheet sheet, String type, ExcelCLPRes item,
			XSSFWorkbook workbook) {
		writeDataForCLP(startIndex, sheet, type, item, workbook);
	}

	public void writeDataForCLP(int startIndex, XSSFSheet sheet, String type, ExcelCLPRes item,
			XSSFWorkbook workbook) {

		 
		copyRowForCLP(workbook, sheet, 6, 7, item);
		

	}

	

	

	private void copyRowForCLP(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum,
			ExcelCLPRes item) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet.getRow(sourceRowNum);


		worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		newRow = worksheet.createRow(destinationRowNum);
		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
//        	XSSFCelle

			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				if (i == 0) {
					
					newCell.setCellValue((item.getNo() == null ? 0d : item.getNo()));
				} else if (i == 4) {
					newCell.setCellValue((item.getBoxCount() == null ? 0d : item.getBoxCount()));
				}else if (i == 5) {
					newCell.setCellValue((item.getCbm() == null ? 0d : item.getCbm()));
				} else {
					newCell.setCellValue(oldCell.getNumericCellValue());
				}
				
				break;
			case Cell.CELL_TYPE_STRING:
				
					if (i == 1) {
						newCell.setCellValue(item.getCompanyNm());
					} else if (i == 2) {
						newCell.setCellValue(item.getBlNo());
					}else if (i == 3) {
						newCell.setCellValue(item.getMarking());
						newCell.getCellStyle().setWrapText(true);
					}else if (i == 6) {
						XSSFRichTextString richString = new XSSFRichTextString( item.getEngNm());
						
						newCell.setCellValue(richString);
//						newCell.setCellValue(item.getEngNm());
					} else {
						newCell.setCellValue(oldCell.getStringCellValue());
					}
				

//                    newCell.setCellValue(oldCell.getRichStringCellValue());
//                    newCell.setCellValue(item.get);
				break;
			}
		}

		// If there are are any merged regions in the source row, copy to new row
		for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
			CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
			if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
				CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
						(newRow.getRowNum() + (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow())),
						cellRangeAddress.getFirstColumn(), cellRangeAddress.getLastColumn());
				worksheet.addMergedRegion(newCellRangeAddress);
			}
		}
	}
	
	public void chageDataforCLP(XSSFSheet sheet, ExcelCLPRes excelCLPRes, XSSFWorkbook workbook) {
		int startRowNum = 0;
		int rowIndex = 0;
		int columnIndex = 0;
		int rowCount = sheet.getLastRowNum();
		List<Integer> deleteList = new ArrayList<Integer>();

		for (int i = 0; i < rowCount; i++) {
			XSSFRow row = sheet.getRow(i);

//				if(row != null) {
			if (row == null)
				break;
			row.cellIterator().forEachRemaining(cell -> {
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String value = cell.getStringCellValue();

					cell.setCellValue(convertDataForCLP(value, excelCLPRes));
					
					if(value.contains("${incomeDt}") ) {//date format으로 데이터 넣어야함.
						
						String incomeDt = excelCLPRes.getIncomeDt();
						SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
						try {
							cell.setCellValue(dateFormat.parse(incomeDt));
						} catch (ParseException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						
						
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

	}

	public String convertDataForCLP(String target, ExcelCLPRes excelCLPRes) {

		if (target.contains("${container}")) {
			return excelCLPRes.getContainer();
		} else if (target.contains("${chulhangPort}")) {
			return excelCLPRes.getChulhangPort();

		} else if (target.contains("${containerNo}")) {
			return excelCLPRes.getContainerNo();
		} else if (target.contains("${silNo}")) {
			return excelCLPRes.getSilNo();
		} 
		

		else {
			return target;
		}

	}
	
	@Override
	public List<ExcelInpackRes> userInpackData(FinalInboundInboundMasterReq req) throws Exception {
		List<ExcelInpackRes> finalResult = new ArrayList<>();
		
		List<FinalInbound> finalInboundList = _finalInboundRepository.findByIncomeDt(req.getIncomeDt());
		List<InboundMaster> inboundMasterList =  new ArrayList<>();
		List<FinalInboundInboundMaster> fiiList =  new ArrayList<>();
		for(int i=0; i<finalInboundList.size(); i++) {
			List<FinalInboundInboundMaster> r = new ArrayList<>();
			r = _FinalInboundInboundMasterRepository.findByFinalInboundId(finalInboundList.get(i).getId());
			for(int k=0; k<r.size(); k++) {
				if(r.get(k).getInboundMaster().getManager().equals(req.getManager())) {
					fiiList.add(r.get(k));
				}
			}
		
		}
		for (int j =0; j<fiiList.size(); j++) {
			ExcelInpackRes result = _FinalInboundInboundMasterRepository.findById(fiiList.get(j).getId()).map(t -> {
				Integer countPCS= 0;
				Integer countM= 0;
				Integer countKG= 0;
				Integer countYD= 0;
				
				List<InboundRes> inbound_list = new ArrayList<>();
				List<Inbound> origin_inbound_list = new ArrayList<>();
				ExcelInpackRes item = ExcelInpackRes.builder().build();
				List<ExcelInpackSubRes> list = new ArrayList<>();


				String departDt = t.getFinalInbound().getDepartDtStr();
				StringBuffer sb = new StringBuffer();
				String companyNum=t.getInboundMaster().getCompanyInfo().getCoNum();
				sb.append(companyNum);
				sb.insert(3, "-");
				sb.insert(6, "-");
				
				item.setFileNm(t.getInboundMaster().getBlNo());
				item.setInboundMasterId(t.getInboundMaster().getId());
				item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
						+ t.getInboundMaster().getComExport().getValue2());
				item.setData03(t.getInboundMaster().getCompanyInfo().getCoInvoice());
				item.setData05(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
						+ sb.toString() + "\n"
						+ t.getInboundMaster().getCompanyInfo().getCoAddress());
				item.setData06(departDt);
				item.setData07(t.getFinalInbound().getHangName() + "/" + t.getFinalInbound().getHangCha());
			
				item.setData08((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
				item.setData09((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
				if(t.getInboundMaster().getCurrencyType()==null||t.getInboundMaster().getCurrencyType().equals("$")) {
					item.setData10("Unit Price" + "\n" + "(USD)");
					item.setData11("Amount" + "\n" + "(USD)");
				}else {
					item.setData10("Unit Price" + "\n" + "(CNY)");
					item.setData11("Amount" + "\n" + "(CNY)");
				}
				if(t.getInboundMaster().getPackingType()==null||t.getInboundMaster().getPackingType().equals("CTN")) {
					item.setData12("CTNS");
				}else if(t.getInboundMaster().getPackingType().equals("PL")) {
					item.setData12("PLS");
				}else if(t.getInboundMaster().getPackingType().equals("GT")) {
					item.setData12("GTS");
				}
				item.setCoYn(false);
				if(t.getInboundMaster().getCompanyInfo().getCoInvoice()==null) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
					SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date departDate=dateFormat2.parse(departDt);
						String departDateStr = dateFormat.format(departDate);
						String data=""+departDateStr;
						item.setInvoice(data);
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					item.setInvoice("");
				}else {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
					SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
					try {
						Date departDate=dateFormat2.parse(departDt);
						String departDateStr = dateFormat.format(departDate);
						String data=t.getInboundMaster().getCompanyInfo().getCoInvoice()+departDateStr;
						item.setInvoice(data);
						
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					
				}
				Date today = new Date();
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
				String todayStr = dateFormat.format(today);
				item.setNoCoDt("SJ-"+todayStr);			
				// 병합처리된 marking 데이터 처리를 위한
				inbound_list = _InboundService.getInboundByInboundMasterId(
						InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
				inbound_list = _utilService.changeExcelFormatNew(inbound_list);
//				Map<Long, String> markingInfo = new HashMap<>();
//				markingInfo = _utilService.getMakingForYATAI(inbound_list);

				origin_inbound_list = t.getInboundMaster().getInbounds().stream()
						.sorted(Comparator.comparing(Inbound::getOrderNo)).collect(Collectors.toList());

				List<ExcelInpackSubRes> sublist = new ArrayList<>();
				for (int i = 0; i < origin_inbound_list.size(); i++) {
					ExcelInpackSubRes subRes = ExcelInpackSubRes.builder().build();

					String yyMMdd = getYYMMDD(departDt);
					subRes.setOrderNo(i + 1);
					subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
					subRes.setJejil(origin_inbound_list.get(i).getJejil());
					subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
					subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
					if(origin_inbound_list.get(i).getReportPrice()==null||origin_inbound_list.get(i).getReportPrice()==0) {
						
						subRes.setItemPrice(new Double(0));
					}else {
						subRes.setItemPrice(origin_inbound_list.get(i).getReportPrice());
					}
					if(origin_inbound_list.get(i).getReportPrice()==null||origin_inbound_list.get(i).getReportPrice()==0) {
						subRes.setTotalPrice(new Double(0));
					}else {
						subRes.setTotalPrice(new Double(String.format("%.2f",
								origin_inbound_list.get(i).getReportPrice() * origin_inbound_list.get(i).getItemCount())));
					}
					
					if(origin_inbound_list.get(i).getBoxCount()==null) {
						subRes.setBoxCount(new Double(0));
					}else {
						subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
					}
					
					subRes.setTotalWeight(origin_inbound_list.get(i).getWeight());
					subRes.setWeight((subRes.getTotalWeight()  == null ? 0d : subRes.getTotalWeight()) - (subRes.getBoxCount() == null ? 0d : subRes.getBoxCount()) );
					subRes.setCbm(origin_inbound_list.get(i).getCbm());
					if(origin_inbound_list.get(i).getCoId()!=3) {
						item.setCoYn(true);
					}
					if(t.getInboundMaster().getCurrencyType()==null||t.getInboundMaster().getCurrencyType().equals("$")) {
						subRes.setCurrencyType("$");
					}else {
						subRes.setCurrencyType(t.getInboundMaster().getCurrencyType());
					}
					if(t.getInboundMaster().getPackingType()==null||t.getInboundMaster().getPackingType().equals("CTN")) {
						subRes.setPackingType("CTNS");
					}else {
						subRes.setPackingType(t.getInboundMaster().getPackingType());
					}
					subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
					if(origin_inbound_list.get(i).getAmountType()==null||origin_inbound_list.get(i).getAmountType().equals("PCS")){
						countPCS=countPCS+1;
					}else if(origin_inbound_list.get(i).getAmountType().equals("M")){
						countM=countM+1;
					}else if(origin_inbound_list.get(i).getAmountType().equals("YD")){
						countYD=countYD+1;
					}else if(origin_inbound_list.get(i).getAmountType().equals("KG")){
						countKG=countKG+1;
					}
					
					sublist.add(subRes);

				}

//				Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
//				Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
//				item.setTotalBoxCountEng("TOTAL: ("+String.valueOf(total.intValue())+")"+" CTNS OF"); 
				item.setCountPCS(countPCS);
				item.setCountM(countM);	
				item.setCountYD(countYD);
				item.setCountKG(countKG);
				item.setSubItem(sublist);
				return item;
			}).get();
			finalResult.add(result);		
		}
		
		
		
		return finalResult;		
	}
	
	@Override
	public List<ExcelCORes> cOData(FinalInboundInboundMasterReq req) throws Exception {
		List<ExcelCORes> finalResult = new ArrayList<>();
		
		List<FinalInbound> finalInboundList = _finalInboundRepository.findByIncomeDt(req.getIncomeDt());
		List<Inbound> item = new ArrayList<>();
		List<FinalInboundInboundMaster> fiiList =  new ArrayList<>();
		for(int i=0; i<finalInboundList.size(); i++) {
			List<FinalInboundInboundMaster> r = new ArrayList<>();
			r = _FinalInboundInboundMasterRepository.findByFinalInboundId(finalInboundList.get(i).getId());
			for(int k=0; k<r.size(); k++) {
					fiiList.add(r.get(k));
				
			}
		
		}

		for (int j =0; j<fiiList.size(); j++) {
			List<Inbound> origin_inbound_list = new ArrayList<>();
			origin_inbound_list=fiiList.get(j).getInboundMaster().getInbounds();		
			for(int l=0; l<origin_inbound_list.size(); l++) {
				item.add(origin_inbound_list.get(l));			
				}
		
		}
		for(int m=0; m<item.size(); m++) {
			ExcelCORes result = new ExcelCORes();
			
			result.setBlNo(item.get(m).getInboundMaster().getBlNo());
			result.setKorNm(item.get(m).getKorNm());
			if(item.get(m).getCoId()==1) {
				result.setType("FTA");				
			}else if(item.get(m).getCoId()==2) {
				result.setType("YATAI");	
			}else if(item.get(m).getCoId()==3) {
				result.setType(" ");	
			}else if(item.get(m).getCoId()==4) {
				result.setType("RCEP");	
			}else if(item.get(m).getCoId()==5) {
				result.setType("공장");	
			}
			
			
			
			finalResult.add(result);
			}
		
		
		
		
		return finalResult;		
	}
	
	
	
	
	
	
	
	public String getStringResult(Double param) {
		
		try {
			boolean finalCheck = false;
			String[] arr = String.valueOf(param).split("\\.");
			char[] texts = arr[1].toCharArray();
			Boolean checkPoint1[] = new Boolean[texts.length];
			
			for(int i = 0; i<texts.length;i++) {
				//System.out.println(texts[i]);
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
public String getDoubleResult(Double param) {
		
		try {
			boolean finalCheck = false;
			String[] arr = String.valueOf(param).split("\\.");
			char[] texts = arr[1].toCharArray();
			Boolean checkPoint1[] = new Boolean[texts.length];
			
			for(int i = 0; i<texts.length;i++) {
				//System.out.println(texts[i]);
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
				return "#,##0_ ";
			}else {
				return "#,##0.00_ ";
			}
		}catch (Exception e) {
			return String.valueOf(param);
		}
		
	}

	@Override
	public InboundViewListRes previewData(InboundReq inboundReq) throws Exception {
		// TODO Auto-generated method stub
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		Double itemCountSumFinal = new Double(0);
		Double boxCountSumFinal = new Double(0);
		Double cbmSumFinal = new Double(0);
		Double weightSumFinal = new Double(0);
		
		
		InboundViewListRes finalRes = new InboundViewListRes();
		
		List<Long> inboundMasterIdList = inboundReq.getInboundMasterIds();
		List<InboundViewRes> finalList = new ArrayList<>();
		
		for(int i=0; i<inboundMasterIdList.size(); i++) {
			InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterIdList.get(i).longValue()).get();
			List<InboundRes> list = _InboundService.getInboundByMasterId(inboundMasterIdList.get(i).longValue());
			//출력모드
			List<InboundRes> result2 = _utilService.changeExcelFormatNew(list);
			InboundViewRes result = _InboundService.changeInbound2(result2);
			itemCountSumFinal=itemCountSumFinal+ (result.getItemCountSumD() == null ? 0d : result.getItemCountSumD());
			boxCountSumFinal=boxCountSumFinal+ ( result.getBoxCountSumD() == null ? 0d : result.getBoxCountSumD());
			cbmSumFinal=cbmSumFinal+ (result.getCbmSumD() == null ? 0d : result.getCbmSumD());
			weightSumFinal=weightSumFinal+ (result.getWeightSumD() == null ? 0d : result.getWeightSumD());
			result.setCurrencyType(inboundMaster.getCurrencyType());
			result.setPackingType(inboundMaster.getPackingType());
			finalList.add(result);
		}
		
		finalRes.setInbounds(finalList);
		finalRes.setItemCountSumFinal(getStringResult(itemCountSumFinal));
		finalRes.setBoxCountSumFinal(decimalFormat2.format(boxCountSumFinal));
		finalRes.setCbmSumFinal(decimalFormat.format(cbmSumFinal));
		finalRes.setWeightSumFinal(decimalFormat2.format(weightSumFinal));
		finalRes.setItemCountSumFinalD(itemCountSumFinal);
		finalRes.setBoxCountSumFinalD(boxCountSumFinal);
		finalRes.setCbmSumFinalD(cbmSumFinal);
		finalRes.setWeightSumFinalD(weightSumFinal);
		if(inboundMasterIdList.size()==0) {
			finalRes.setPackingType("CTN");
		}else {
			finalRes.setPackingType(finalList.get(0).getPackingType());
		}
		return  finalRes;
	}

	@Override
	public boolean preview(InboundViewListRes inboundViewListRes, List<UnbiRes> unbiList,FinalInboundRes container,HttpServletResponse response) throws Exception {
		
		String path = DocumentType.getList().stream().filter(t -> t.getId() == 9).findFirst().get().getName();

		
		try {

			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, "PREVIEW");
				
			XSSFSheet sheet = workbook.getSheetAt(0);
			sheet.setDefaultRowHeightInPoints(new Float("25"));
			XSSFSheet sheet2 = workbook.getSheetAt(1);
			XSSFSheet sheet3 = workbook.getSheetAt(2);
			XSSFSheet sheet4 = workbook.getSheetAt(4);
			XSSFSheet sheet6 = workbook.getSheetAt(6);
			XSSFSheet sheet7 = workbook.getSheetAt(7);
			
			int startRow = 1;
			List<Integer> rowNum = new ArrayList<>();
			List<Integer> itemSize = new ArrayList<>();
			List<Integer> itemStartNum = new ArrayList<>();
			List<Integer> sTotalNum = new ArrayList<>();
			List<InboundViewRes> list2 = inboundViewListRes.getInbounds();
			int itemCount = 0;
			int blCount = list2.size();
			for(int i=0; i<list2.size(); i++) {
				 List<InboundRes> list = list2.get(i).getInboundsForPreview();
				 itemCount=itemCount+list.size();	
				 itemStartNum.add(startRow+i);
				 itemSize.add(list.size());
				 if(i==0 && list2.size()==1) {
					 
						step01ForPreview(list, sheet, workbook,startRow);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+1,sheet3, 0);	
						step03ForPreview(inboundViewListRes, sheet, workbook,list.size()+startRow+1);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+2,sheet7, 0);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+3,sheet7, 1);	
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+4,sheet7, 2);	
						step11ForPreview(container, sheet, workbook,list.size()+startRow+4);
						step04ForPreview(unbiList, sheet, workbook,list.size()+startRow+5);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+5+unbiList.size(),sheet6, 0);	
						step05ForPreview(unbiList, sheet, workbook,list.size()+startRow+5+unbiList.size());
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+2,6,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+2,12,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+2,14,16));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,18,19));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+3,6,10));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,0,0));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,1,1));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,2,2));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,3,3));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,4,4));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,5,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,17,17));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,11,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,12,12));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,13,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,14,14));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,15,15));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,16,16));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+5+list2.size(),list.size()+startRow+5+list2.size(),2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+5+list2.size()+1,list.size()+startRow+5+list2.size()+1,2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+5+list2.size()+2,list.size()+startRow+5+list2.size()+2,2,5));

						if(list.size()==1) {
							rowNum.add(startRow+2);
						}
						for(int w=0; w<list.size()+startRow+5+unbiList.size()+1; w++) {
							sheet.getRow(w).setHeightInPoints(new Float("25"));
						}
						sheet.getRow(1).setHeightInPoints(new Float("50"));
						startRow=startRow+list.size();	
						
				 }else if(i==list2.size()-1) {
					 
						step01ForPreview(list, sheet, workbook,startRow+i);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow+i);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+1,sheet3, 0);	
						step03ForPreview(inboundViewListRes, sheet, workbook,list.size()+startRow+i+1);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+2,sheet7, 0);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+3,sheet7, 1);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+4,sheet7, 2);
						step11ForPreview(container, sheet, workbook,list.size()+startRow+i+4);
						step04ForPreview(unbiList, sheet, workbook,list.size()+startRow+i+5);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+5+unbiList.size(),sheet6, 0);
						step05ForPreview(unbiList, sheet, workbook,list.size()+startRow+i+5+unbiList.size());
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+2,6,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+2,12,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+2,14,16));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,18,19));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+3,6,10));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,0,0));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,1,1));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,2,2));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,3,3));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,4,4));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,5,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,17,17));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,11,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,12,12));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,13,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,14,14));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,15,15));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,16,16));
						
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+5+list2.size(),list.size()+startRow+i+5+list2.size(),2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+5+list2.size()+1,list.size()+startRow+i+5+list2.size()+1,2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+5+list2.size()+2,list.size()+startRow+i+5+list2.size()+2,2,5));
						
						for(int w=0; w<list.size()+startRow+5+unbiList.size()+1; w++) {
							sheet.getRow(w).setHeightInPoints(new Float("25"));
					}
					if(list.size()==1) {
						rowNum.add(startRow+i);
					}
					for(int q=0; q<rowNum.size(); q++) {
						sheet.getRow(rowNum.get(q)).setHeightInPoints(new Float("50"));
					}
						
						startRow=startRow+list.size();	
						
				 }else if(i==0) {
					 
						step01ForPreview(list, sheet, workbook,startRow);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow);
						if(list.size()==1) {
							rowNum.add(startRow);
						}
						startRow=startRow+list.size();	
						
				 }else {
					 	step01ForPreview(list, sheet, workbook,startRow+i);	
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow+i);
						if(list.size()==1) {
							rowNum.add(startRow+i);
						}
						startRow=startRow+list.size();	
				 }
						
				
			}
			for(int i=0; i<unbiList.size(); i++){
				sheet.getRow(5+itemCount+blCount+i).getCell(17).setCellFormula("SUM(G"+String.valueOf(5+itemCount+blCount+i+1)+":"+"Q"+String.valueOf(5+itemCount+blCount+i+1)+")");
			}
			for(int i=0; i<unbiList.size()+4; i++){
				sheet.getRow(2+itemCount+blCount+i).setHeightInPoints(new Float("25"));
			}
			
			for(int i=0; i<itemSize.size(); i++){
				for(int j=0; j<itemSize.get(i); j++) {
					String a = "F";
					String b = String.valueOf(itemStartNum.get(i)+j+1);
					String c = "J";
					sheet.getRow(itemStartNum.get(i)+j).getCell(16).setCellFormula(a+b+"*"+c+b);	
					if(j==itemSize.get(i)-1) {
						sheet.getRow(itemStartNum.get(i)+j+1).getCell(5).setCellFormula("SUM(F"+String.valueOf(itemStartNum.get(i)+j+1+1-itemSize.get(i))+":"+"F"+String.valueOf(itemStartNum.get(i)+j+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+1).getCell(6).setCellFormula("SUM(G"+String.valueOf(itemStartNum.get(i)+j+1+1-itemSize.get(i))+":"+"G"+String.valueOf(itemStartNum.get(i)+j+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+1).getCell(7).setCellFormula("SUM(H"+String.valueOf(itemStartNum.get(i)+j+1+1-itemSize.get(i))+":"+"H"+String.valueOf(itemStartNum.get(i)+j+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+1).getCell(8).setCellFormula("SUM(I"+String.valueOf(itemStartNum.get(i)+j+1+1-itemSize.get(i))+":"+"I"+String.valueOf(itemStartNum.get(i)+j+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+1).getCell(16).setCellFormula("SUM(Q"+String.valueOf(itemStartNum.get(i)+j+1+1-itemSize.get(i))+":"+"Q"+String.valueOf(itemStartNum.get(i)+j+1+1-1)+")");
					}
					
					}
			}
			List<String> totalItemCount = new ArrayList<>();
			List<String> totalBoxCount = new ArrayList<>();
			List<String> totalWeight = new ArrayList<>();
			List<String> totalCBM = new ArrayList<>();
			for(int i=0; i<itemSize.size(); i++){
				sTotalNum.add(itemSize.get(i)+itemStartNum.get(i));
				}
			for(int i=0; i<sTotalNum.size(); i++){
				totalItemCount.add("F"+String.valueOf(sTotalNum.get(i)+1));
				totalBoxCount.add("G"+String.valueOf(sTotalNum.get(i)+1));
				totalWeight.add("H"+String.valueOf(sTotalNum.get(i)+1));
				totalCBM.add("I"+String.valueOf(sTotalNum.get(i)+1));
			}
			String totalItemCount1 = String.join("+", totalItemCount);
			String totalBoxCount1 = String.join("+", totalBoxCount);
			String totalWeight1 = String.join("+", totalWeight);
			String totalCBM1 = String.join("+", totalCBM);
			
			sheet.getRow(1+itemCount+blCount).getCell(5).setCellFormula(totalItemCount1);
			sheet.getRow(1+itemCount+blCount).getCell(6).setCellFormula(totalBoxCount1);
			sheet.getRow(1+itemCount+blCount).getCell(7).setCellFormula(totalWeight1);
			sheet.getRow(1+itemCount+blCount).getCell(8).setCellFormula(totalCBM1);
			
			String fileName =  "PREVIEW.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		
		

		return true;
	}
	@Override
	public boolean previewContainer(InboundViewListRes inboundViewListRes, List<UnbiRes> unbiList,FinalInboundRes container,HttpServletResponse response) throws Exception {
		
		String path = DocumentType.getList().stream().filter(t -> t.getId() == 9).findFirst().get().getName();

		
		try {

			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, "CONTAINER");
				
			XSSFSheet sheet = workbook.getSheetAt(0);
			sheet.setDefaultRowHeightInPoints(new Float("25"));		
			XSSFSheet sheet2 = workbook.getSheetAt(1);
			XSSFSheet sheet3 = workbook.getSheetAt(2);
			XSSFSheet sheet4 = workbook.getSheetAt(4);
			XSSFSheet sheet6 = workbook.getSheetAt(6);
			XSSFSheet sheet7 = workbook.getSheetAt(7);
			XSSFSheet sheet8 = workbook.getSheetAt(8);
			int startRow = 1;
			List<Integer> rowNum = new ArrayList<>();
			List<Integer> itemSize = new ArrayList<>();
			List<Integer> itemStartNum = new ArrayList<>();
			List<Integer> sTotalNum = new ArrayList<>();
			List<InboundViewRes> list2 = inboundViewListRes.getInbounds();
			int itemCount = 0;
			int blCount = list2.size();
			
			for(int i=0; i<list2.size(); i++) {
				 List<InboundRes> list = list2.get(i).getInboundsForPreview();
				 itemCount=itemCount+list.size();
				 itemStartNum.add(startRow+i);
				 itemSize.add(list.size());
				 if(i==0 && list2.size()==1) {
					 
						step01ForPreview(list, sheet, workbook,startRow);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+1,sheet3, 0);	
						step03ForPreview(inboundViewListRes, sheet, workbook,list.size()+startRow+1);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+2,sheet7, 0);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+3,sheet7, 1);	
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+4,sheet7, 2);	
						step11ForPreview(container, sheet, workbook,list.size()+startRow+4);
						step04ForPreview(unbiList, sheet, workbook,list.size()+startRow+5);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+5+unbiList.size(),sheet6, 0);	
						step05ForPreview(unbiList, sheet, workbook,list.size()+startRow+5+unbiList.size());
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+2,6,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+2,12,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+2,14,16));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,18,19));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+3,6,10));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,0,0));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,1,1));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,2,2));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,3,3));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,4,4));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,5,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+2,list.size()+startRow+4,17,17));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,11,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,12,12));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,13,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,14,14));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,15,15));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+3,list.size()+startRow+4,16,16));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+5+list2.size(),list.size()+startRow+5+list2.size(),2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+5+list2.size()+1,list.size()+startRow+5+list2.size()+1,2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+5+list2.size()+2,list.size()+startRow+5+list2.size()+2,2,5));
						sheet.shiftRows(0, sheet.getLastRowNum(), 3);
						shiftRowforPreview(0, sheet, 6);
						for(int j=0; j<6;j++) {
							copyRowOtherSheetForPreview(workbook, sheet, j,sheet8, j);	
						}
						step06ForPreview(container, sheet, workbook,1);
						step07ForPreview(container, sheet, workbook,2);
						step08ForPreview(container, sheet, workbook,3);
						step09ForPreview(container, sheet, workbook,4);
//						step10ForPreview(unbiList, sheet, workbook,list.size()+startRow+5+unbiList.size());
						sheet.addMergedRegion(new CellRangeAddress(1,4,16,16));
						sheet.addMergedRegion(new CellRangeAddress(1,4,17,19));
						sheet.getRow(0).setHeightInPoints(new Float("36"));
						sheet.getRow(1).setHeightInPoints(new Float("21"));
						sheet.getRow(2).setHeightInPoints(new Float("21"));
						sheet.getRow(3).setHeightInPoints(new Float("21"));
						sheet.getRow(4).setHeightInPoints(new Float("21"));
						sheet.getRow(5).setHeightInPoints(new Float("21"));
						if(list.size()==1) {
							rowNum.add(startRow+2);
						}
						for(int w=6; w<list.size()+startRow+5+unbiList.size()+7; w++) {
							sheet.getRow(w).setHeightInPoints(new Float("25"));
					}
						sheet.getRow(1+6).setHeightInPoints(new Float("50"));
						startRow=startRow+list.size();	
						
				 }else if(i==list2.size()-1) {
					 
						step01ForPreview(list, sheet, workbook,startRow+i);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow+i);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+1,sheet3, 0);	
						step03ForPreview(inboundViewListRes, sheet, workbook,list.size()+startRow+i+1);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+2,sheet7, 0);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+3,sheet7, 1);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+4,sheet7, 2);
						step11ForPreview(container, sheet, workbook,list.size()+startRow+i+4);
						step04ForPreview(unbiList, sheet, workbook,list.size()+startRow+i+5);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i+5+unbiList.size(),sheet6, 0);
						step05ForPreview(unbiList, sheet, workbook,list.size()+startRow+i+5+unbiList.size());
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+2,6,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+2,12,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+2,14,16));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,18,19));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+3,6,10));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,0,0));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,1,1));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,2,2));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,3,3));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,4,4));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,5,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+2,list.size()+startRow+i+4,17,17));
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,11,11));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,12,12));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,13,13));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,14,14));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,15,15));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+3,list.size()+startRow+i+4,16,16));
						
						
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+5+list2.size(),list.size()+startRow+i+5+list2.size(),2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+5+list2.size()+1,list.size()+startRow+i+5+list2.size()+1,2,5));
						sheet.addMergedRegion(new CellRangeAddress(list.size()+startRow+i+5+list2.size()+2,list.size()+startRow+i+5+list2.size()+2,2,5));
						startRow=startRow+list.size();	
						shiftRowforPreview(0, sheet, 6);
						for(int j=0; j<6;j++) {
							copyRowOtherSheetForPreview(workbook, sheet, j,sheet8, j);	
						}
						step06ForPreview(container, sheet, workbook,1);
						step07ForPreview(container, sheet, workbook,2);
						step08ForPreview(container, sheet, workbook,3);
						step09ForPreview(container, sheet, workbook,4);
//						step10ForPreview(unbiList, sheet, workbook,list.size()+startRow+5+unbiList.size());
//						sheet.addMergedRegion(new CellRangeAddress(1,4,16,19));
						sheet.addMergedRegion(new CellRangeAddress(1,4,16,16));
						sheet.addMergedRegion(new CellRangeAddress(1,4,17,19));
						sheet.getRow(0).setHeightInPoints(new Float("36"));
						sheet.getRow(1).setHeightInPoints(new Float("21"));
						sheet.getRow(2).setHeightInPoints(new Float("21"));
						sheet.getRow(3).setHeightInPoints(new Float("21"));
						sheet.getRow(4).setHeightInPoints(new Float("21"));
						sheet.getRow(5).setHeightInPoints(new Float("21"));
						for(int w=6; w<list.size()+startRow+5+unbiList.size()+7; w++) {
								sheet.getRow(w).setHeightInPoints(new Float("25"));
						}
						if(list.size()==1) {
							rowNum.add(startRow+i-1);
						}
						for(int q=0; q<rowNum.size(); q++) {
							sheet.getRow(rowNum.get(q)+6).setHeightInPoints(new Float("50"));
						}
//						startRow=startRow+list.size();	
						
				 }else if(i==0) {
					 
						step01ForPreview(list, sheet, workbook,startRow);
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow);
						if(list.size()==1) {
							rowNum.add(startRow);
						}
						startRow=startRow+list.size();	
						
				 }else {
					 	step01ForPreview(list, sheet, workbook,startRow+i);	
						copyRowOtherSheetForPreview(workbook, sheet, list.size()+startRow+i,sheet2, 0);	
						step02ForPreview(list, sheet, workbook,list.size()+startRow+i);
						if(list.size()==1) {
							rowNum.add(startRow+i);
						}
						startRow=startRow+list.size();
						
				 }
						
				
			}
			for(int i=0; i<unbiList.size()+4; i++){
				sheet.getRow(2+6+itemCount+blCount+i).setHeightInPoints(new Float("25"));
			}
			for(int i=0; i<unbiList.size(); i++){
				sheet.getRow(11+itemCount+blCount+i).getCell(17).setCellFormula("SUM(G"+String.valueOf(11+itemCount+blCount+i+1)+":"+"Q"+String.valueOf(11+itemCount+blCount+i+1)+")");
			}
			for(int i=0; i<itemSize.size(); i++){
				for(int j=0; j<itemSize.get(i); j++) {
					String a = "F";
					String b = String.valueOf(itemStartNum.get(i)+j+1+6);
					String c = "J";
					sheet.getRow(itemStartNum.get(i)+j+6).getCell(16).setCellFormula(a+b+"*"+c+b);	
					if(j==itemSize.get(i)-1) {
						sheet.getRow(itemStartNum.get(i)+j+6+1).getCell(5).setCellFormula("SUM(F"+String.valueOf(itemStartNum.get(i)+j+6+1+1-itemSize.get(i))+":"+"F"+String.valueOf(itemStartNum.get(i)+j+6+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+6+1).getCell(6).setCellFormula("SUM(G"+String.valueOf(itemStartNum.get(i)+j+6+1+1-itemSize.get(i))+":"+"G"+String.valueOf(itemStartNum.get(i)+j+6+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+6+1).getCell(7).setCellFormula("SUM(H"+String.valueOf(itemStartNum.get(i)+j+6+1+1-itemSize.get(i))+":"+"H"+String.valueOf(itemStartNum.get(i)+j+6+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+6+1).getCell(8).setCellFormula("SUM(I"+String.valueOf(itemStartNum.get(i)+j+6+1+1-itemSize.get(i))+":"+"I"+String.valueOf(itemStartNum.get(i)+j+6+1+1-1)+")");
						sheet.getRow(itemStartNum.get(i)+j+6+1).getCell(16).setCellFormula("SUM(Q"+String.valueOf(itemStartNum.get(i)+j+6+1+1-itemSize.get(i))+":"+"Q"+String.valueOf(itemStartNum.get(i)+j+6+1+1-1)+")");
					}
					
					}
			}
			List<String> totalItemCount = new ArrayList<>();
			List<String> totalBoxCount = new ArrayList<>();
			List<String> totalWeight = new ArrayList<>();
			List<String> totalCBM = new ArrayList<>();
			for(int i=0; i<itemSize.size(); i++){
				sTotalNum.add(itemSize.get(i)+itemStartNum.get(i));
				}
			for(int i=0; i<sTotalNum.size(); i++){
				totalItemCount.add("F"+String.valueOf(sTotalNum.get(i)+6+1));
				totalBoxCount.add("G"+String.valueOf(sTotalNum.get(i)+6+1));
				totalWeight.add("H"+String.valueOf(sTotalNum.get(i)+6+1));
				totalCBM.add("I"+String.valueOf(sTotalNum.get(i)+6+1));
			}
			String totalItemCount1 = String.join("+", totalItemCount);
			String totalBoxCount1 = String.join("+", totalBoxCount);
			String totalWeight1 = String.join("+", totalWeight);
			String totalCBM1 = String.join("+", totalCBM);
			
			sheet.getRow(7+itemCount+blCount).getCell(5).setCellFormula(totalItemCount1);
			sheet.getRow(7+itemCount+blCount).getCell(6).setCellFormula(totalBoxCount1);
			sheet.getRow(7+itemCount+blCount).getCell(7).setCellFormula(totalWeight1);
			sheet.getRow(7+itemCount+blCount).getCell(8).setCellFormula(totalCBM1);
			
			String fileName =  "CONTAINER.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			workbook.removeSheetAt(1);
			
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		
		

		return true;
	}
	private void step01ForPreview(List<InboundRes> resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {

		
		Font font = workbook.createFont();
		font.setColor(IndexedColors.RED.getIndex());
		font.setFontName("굴림체");
		
		Font font1 = workbook.createFont();
		font1.setColor(IndexedColors.BLACK.getIndex());
		font1.setFontName("굴림체");
		
		XSSFCellStyle cellStyle = workbook.createCellStyle();
		cellStyle.setFont(font);
		cellStyle.setAlignment(HorizontalAlignment.CENTER);
		cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle.setBorderBottom(BorderStyle.THIN);
		cellStyle.setBorderRight(BorderStyle.THIN);
		cellStyle.setBorderTop(BorderStyle.HAIR);		
		
		XSSFCellStyle cellStyle1 = workbook.createCellStyle();
		cellStyle1.setFont(font1);
		cellStyle1.setAlignment(HorizontalAlignment.CENTER);
		cellStyle1.setVerticalAlignment(VerticalAlignment.CENTER);
		cellStyle1.setBorderBottom(BorderStyle.THIN);
		cellStyle1.setBorderRight(BorderStyle.THIN);
		cellStyle1.setBorderTop(BorderStyle.HAIR);		
		
		
		XSSFSheet sheet3 = workbook.getSheetAt(3);
		// item 채우기
		for (int i = 0; i < resource.size(); i++) {

			copyRowOtherSheetForInbounds(workbook, sheet, startRow+i ,sheet3, 0);
						

			XSSFRow row = sheet.getRow(startRow+i);
			if(resource.size()==1) {
				
//				row.setHeight(new Short("2000"));
				row.setHeightInPoints(new Float("50"));
				}else {
//				row.setHeight(new Short("500"));
				row.setHeightInPoints(new Float("25"));
				}
			
			XSSFCellStyle orderNoFont  = workbook.createCellStyle();
			XSSFCellStyle allCellStyle = workbook.createCellStyle();
			
			XSSFCellStyle workDateStrCellStyle = workbook.createCellStyle();
			XSSFCellStyle companyNmCellStyle = workbook.createCellStyle();
			XSSFCellStyle markingCellStyle = workbook.createCellStyle();
			XSSFCellStyle korNmCellStyle = workbook.createCellStyle();
			XSSFCellStyle itemCountCellStyle = workbook.createCellStyle();
			XSSFCellStyle boxCountCellStyle = workbook.createCellStyle();
			XSSFCellStyle weightCellStyle = workbook.createCellStyle();
			XSSFCellStyle cbmCellStyle = workbook.createCellStyle();
			XSSFCellStyle reportPriceCellStyle = workbook.createCellStyle();
			XSSFCellStyle memo1CellStyle = workbook.createCellStyle();
			XSSFCellStyle memo2CellStyle = workbook.createCellStyle();
			XSSFCellStyle itemNoCellStyle = workbook.createCellStyle();
			XSSFCellStyle jejilCellStyle = workbook.createCellStyle();
			XSSFCellStyle hsCodeCellStyle = workbook.createCellStyle();
			XSSFCellStyle totalPriceCellStyle = workbook.createCellStyle();
			XSSFCellStyle engNmCellStyle = workbook.createCellStyle();
			
			Font allFont = workbook.createFont();
			allFont.setFontName("굴림체");
			Font otherFont = workbook.createFont();
			otherFont.setFontName("굴림체");
//			orderNoFont.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.index);
			orderNoFont.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204)));
			orderNoFont.setFillPattern(FillPatternType.SOLID_FOREGROUND);
			orderNoFont.setAlignment(HorizontalAlignment.CENTER);
			orderNoFont.setVerticalAlignment(VerticalAlignment.CENTER);
			orderNoFont.setBorderBottom(BorderStyle.HAIR);
			orderNoFont.setBorderRight(BorderStyle.HAIR);
			orderNoFont.setBorderLeft(BorderStyle.THIN);;
			orderNoFont.setFont(allFont);
			HSSFWorkbook hwb = new HSSFWorkbook();
			if(resource.get(i).getColorId()==0) {
				allFont.setColor(IndexedColors.BLACK.getIndex());
			}else if(resource.get(i).getColorId()==1){
				allFont.setColor(IndexedColors.BLUE.getIndex());
			}else if(resource.get(i).getColorId()==2){
				allFont.setColor(IndexedColors.RED.getIndex());
			}else if(resource.get(i).getColorId()==3){
				allFont.setColor(IndexedColors.GREEN.getIndex());
			}else if(resource.get(i).getColorId()==4){
				allFont.setColor(IndexedColors.PINK.getIndex());
			}else if(resource.get(i).getColorId()==5){
				allFont.setColor(IndexedColors.ORANGE.getIndex());
			}else if(resource.get(i).getColorId()==6){
				allFont.setColor(IndexedColors.VIOLET.getIndex());
			}else if(resource.get(i).getColorId()==7){
				HSSFPalette palette = hwb.getCustomPalette();
				// get the color which most closely matches the color you want to use
				HSSFColor myColor = palette.findSimilarColor(0, 216, 255);
				// get the palette index of that color 
				short palIndex = myColor.getIndex();
				allFont.setColor(palIndex);
			}else if(resource.get(i).getColorId()==8){
				HSSFPalette palette = hwb.getCustomPalette();
				// get the color which most closely matches the color you want to use
				HSSFColor myColor = palette.findSimilarColor(171, 242, 0);
				// get the palette index of that color 
				short palIndex = myColor.getIndex();
				allFont.setColor(palIndex);
			}else if(resource.get(i).getColorId()==9){
				HSSFPalette palette = hwb.getCustomPalette();
				// get the color which most closely matches the color you want to use
				HSSFColor myColor = palette.findSimilarColor(138, 73, 36);
				// get the palette index of that color 
				short palIndex = myColor.getIndex();
				allFont.setColor(palIndex);
			}
			allCellStyle.setAlignment(HorizontalAlignment.CENTER);
			allCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			allCellStyle.setBorderBottom(BorderStyle.HAIR);
			allCellStyle.setBorderRight(BorderStyle.HAIR);
		
			allCellStyle.setFont(allFont);
			
			if(resource.get(i).getWorkDateStrYn().equals("Y")){
				workDateStrCellStyle.setAlignment(HorizontalAlignment.CENTER);
				workDateStrCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				workDateStrCellStyle.setBorderBottom(BorderStyle.NONE);
				workDateStrCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				workDateStrCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				workDateStrCellStyle.setFont(allFont);
				workDateStrCellStyle.setBorderBottom(BorderStyle.HAIR);
				workDateStrCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getWorkDateStrYn()==null||resource.get(i).getWorkDateStrYn().equals("")){
				workDateStrCellStyle.setAlignment(HorizontalAlignment.CENTER);
				workDateStrCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				workDateStrCellStyle.setBorderBottom(BorderStyle.NONE);
				workDateStrCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				workDateStrCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				workDateStrCellStyle.setFont(allFont);
				workDateStrCellStyle.setBorderBottom(BorderStyle.HAIR);
				workDateStrCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			
			if(resource.get(i).getCompanyNmYn().equals("Y")){
				companyNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				companyNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				companyNmCellStyle.setBorderBottom(BorderStyle.NONE);
				companyNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				companyNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				companyNmCellStyle.setFont(allFont);
				companyNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				companyNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				companyNmCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getCompanyNmYn()==null||resource.get(i).getCompanyNmYn().equals("")){
				companyNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				companyNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				companyNmCellStyle.setBorderBottom(BorderStyle.NONE);
				companyNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				companyNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				companyNmCellStyle.setFont(allFont);
				companyNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				companyNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				companyNmCellStyle.setWrapText(true);
			}
			if(resource.get(i).getMarkingYn().equals("Y")){
				markingCellStyle.setAlignment(HorizontalAlignment.CENTER);
				markingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				markingCellStyle.setBorderBottom(BorderStyle.NONE);
				markingCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				markingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				markingCellStyle.setFont(allFont);
				markingCellStyle.setBorderBottom(BorderStyle.HAIR);
				markingCellStyle.setBorderRight(BorderStyle.HAIR); 
				markingCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getMarkingYn()==null||resource.get(i).getMarkingYn().equals("")){
				markingCellStyle.setAlignment(HorizontalAlignment.CENTER);
				markingCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				markingCellStyle.setBorderBottom(BorderStyle.NONE);
				markingCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				markingCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				markingCellStyle.setFont(allFont);
				markingCellStyle.setBorderBottom(BorderStyle.HAIR);
				markingCellStyle.setBorderRight(BorderStyle.HAIR); 
				markingCellStyle.setWrapText(true);
			}
			if(resource.get(i).getKorNmYn().equals("Y")){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getKorNmYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
				
			}else if(resource.get(i).getKorNmYn()==null||resource.get(i).getKorNmYn().equals("")){
				korNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				korNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				korNmCellStyle.setBorderBottom(BorderStyle.NONE);
				korNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				korNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				korNmCellStyle.setFont(allFont);
				korNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				korNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				korNmCellStyle.setWrapText(true);
			}
			if(resource.get(i).getItemCountYn().equals("Y")){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getItemCountYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getItemCountYn()==null||resource.get(i).getItemCountYn().equals("")){
				itemCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemCountCellStyle.setBorderBottom(BorderStyle.NONE);
				itemCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				itemCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemCountCellStyle.setFont(allFont);
				itemCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemCountCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getBoxCountYn().equals("Y")){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBoxCountYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBoxCountYn()==null||resource.get(i).getBoxCountYn().equals("")){
				boxCountCellStyle.setAlignment(HorizontalAlignment.CENTER);
				boxCountCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				boxCountCellStyle.setBorderBottom(BorderStyle.NONE);
				boxCountCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				boxCountCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				boxCountCellStyle.setFont(allFont);
				boxCountCellStyle.setBorderBottom(BorderStyle.HAIR);
				boxCountCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getWeightYn().equals("Y")){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getWeightYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getWeightYn()==null||resource.get(i).getWeightYn().equals("")){
				weightCellStyle.setAlignment(HorizontalAlignment.CENTER);
				weightCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				weightCellStyle.setBorderBottom(BorderStyle.NONE);
				weightCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				weightCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				weightCellStyle.setFont(allFont);
				weightCellStyle.setBorderBottom(BorderStyle.HAIR);
				weightCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getCbmYn().equals("Y")){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getCbmYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getCbmYn()==null||resource.get(i).getCbmYn().equals("")){
				cbmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				cbmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				cbmCellStyle.setBorderBottom(BorderStyle.NONE);
				cbmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				cbmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				cbmCellStyle.setFont(allFont);
				cbmCellStyle.setBorderBottom(BorderStyle.HAIR);
				cbmCellStyle.setBorderRight(BorderStyle.HAIR); 
			}
			if(resource.get(i).getReportPriceYn().equals("Y")){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				reportPriceCellStyle.setFont(otherFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				
			}else if(resource.get(i).getReportPriceYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//				reportPriceCellStyle.setFont(allFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				reportPriceCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//				reportPriceCellStyle.setFont(allFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				reportPriceCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getReportPriceYn()==null||resource.get(i).getReportPriceYn().equals("")){
				reportPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				reportPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				reportPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				reportPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				reportPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
//				reportPriceCellStyle.setFont(allFont);
				reportPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				reportPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				reportPriceCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getMemo1Yn().equals("Y")){
				memo1CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo1CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo1CellStyle.setBorderBottom(BorderStyle.NONE);
				memo1CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				memo1CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo1CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo1CellStyle.setBorderRight(BorderStyle.HAIR); 
				memo1CellStyle.setWrapText(true);
				memo1CellStyle.setFont(otherFont);
				
			}else {
				memo1CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo1CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo1CellStyle.setBorderBottom(BorderStyle.NONE);
				memo1CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				memo1CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo1CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo1CellStyle.setBorderRight(BorderStyle.HAIR);
				memo1CellStyle.setWrapText(true);
				memo1CellStyle.setFont(otherFont);
			}
			if(resource.get(i).getMemo2Yn().equals("Y")){
				memo2CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo2CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo2CellStyle.setBorderBottom(BorderStyle.NONE);
				memo2CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				memo2CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo2CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo2CellStyle.setBorderRight(BorderStyle.HAIR); 
				memo2CellStyle.setWrapText(true);
				memo2CellStyle.setFont(otherFont);
				
			}else {
				memo2CellStyle.setAlignment(HorizontalAlignment.CENTER);
				memo2CellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				memo2CellStyle.setBorderBottom(BorderStyle.NONE);
				memo2CellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				memo2CellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				memo2CellStyle.setBorderBottom(BorderStyle.HAIR);
				memo2CellStyle.setBorderRight(BorderStyle.HAIR); 
				memo2CellStyle.setWrapText(true);
				memo2CellStyle.setFont(otherFont);
			}
			if(resource.get(i).getItemNoYn().equals("Y")){
				itemNoCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemNoCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemNoCellStyle.setBorderBottom(BorderStyle.NONE);
				itemNoCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				itemNoCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemNoCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemNoCellStyle.setBorderRight(BorderStyle.HAIR); 
				itemNoCellStyle.setWrapText(true);
				itemNoCellStyle.setFont(otherFont);
			}else {
				itemNoCellStyle.setAlignment(HorizontalAlignment.CENTER);
				itemNoCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				itemNoCellStyle.setBorderBottom(BorderStyle.NONE);
				itemNoCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				itemNoCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				itemNoCellStyle.setBorderBottom(BorderStyle.HAIR);
				itemNoCellStyle.setBorderRight(BorderStyle.HAIR); 
				itemNoCellStyle.setWrapText(true);
				itemNoCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getJejilYn().equals("Y")){
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getJejilYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
				
			}else {
				jejilCellStyle.setAlignment(HorizontalAlignment.CENTER);
				jejilCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				jejilCellStyle.setBorderBottom(BorderStyle.NONE);
				jejilCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				jejilCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				jejilCellStyle.setBorderBottom(BorderStyle.HAIR);
				jejilCellStyle.setBorderRight(BorderStyle.HAIR); 
				jejilCellStyle.setWrapText(true);
				jejilCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getHsCodeYn().equals("Y")){
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR); 
				hsCodeCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getHsCodeYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR); 
				hsCodeCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR); 
				hsCodeCellStyle.setFont(otherFont);
				
			}else {
				hsCodeCellStyle.setAlignment(HorizontalAlignment.CENTER);
				hsCodeCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				hsCodeCellStyle.setBorderBottom(BorderStyle.NONE);
				hsCodeCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				hsCodeCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				hsCodeCellStyle.setBorderBottom(BorderStyle.HAIR);
				hsCodeCellStyle.setBorderRight(BorderStyle.HAIR); 
				hsCodeCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getTotalPriceYn().equals("Y")){
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getTotalPriceYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
				
			}else {
				totalPriceCellStyle.setAlignment(HorizontalAlignment.CENTER);
				totalPriceCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				totalPriceCellStyle.setBorderBottom(BorderStyle.NONE);
				totalPriceCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				totalPriceCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				totalPriceCellStyle.setBorderBottom(BorderStyle.HAIR);
				totalPriceCellStyle.setBorderRight(BorderStyle.HAIR); 
				totalPriceCellStyle.setFont(otherFont);
			}
			if(resource.get(i).getEngNmYn().equals("Y")){
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getEngNmYn().equals("Y")&&resource.get(i).getBackgroundColorId()==1){
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.D.r, BackgroundColorType.D.g, BackgroundColorType.D.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
				
			}else if(resource.get(i).getBackgroundColorId()==1){
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.B.r, BackgroundColorType.B.g, BackgroundColorType.B.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
				
			}else {
				engNmCellStyle.setAlignment(HorizontalAlignment.CENTER);
				engNmCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
				engNmCellStyle.setBorderBottom(BorderStyle.NONE);
				engNmCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(BackgroundColorType.A.r, BackgroundColorType.A.g, BackgroundColorType.A.b)));
				engNmCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				engNmCellStyle.setBorderBottom(BorderStyle.HAIR);
				engNmCellStyle.setBorderRight(BorderStyle.HAIR); 
				engNmCellStyle.setWrapText(true);
				engNmCellStyle.setFont(otherFont);
			}
			row.getCell(0).setCellValue(resource.get(i).getOrderNoStr());
			row.getCell(0).setCellStyle(orderNoFont);
			row.getCell(1).setCellValue(resource.get(i).getWorkDateStr());
			row.getCell(1).setCellStyle(workDateStrCellStyle);
			row.getCell(2).setCellValue(resource.get(i).getCompanyNm());
			row.getCell(2).setCellStyle(companyNmCellStyle);
			row.getCell(3).setCellValue(resource.get(i).getMarking());
			row.getCell(3).setCellStyle(markingCellStyle);
			row.getCell(4).setCellValue(resource.get(i).getKorNm());
			row.getCell(4).setCellStyle(korNmCellStyle);
			if(resource.get(i).getAmountType().equals("PCS")) {
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
//				row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				}
			}else if(resource.get(i).getAmountType().equals("M")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"M\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"M\""));
				}
			}else if(resource.get(i).getAmountType().equals("YD")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"YD\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"YD\""));
				}
			}else if(resource.get(i).getAmountType().equals("KG")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"KG\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"KG\""));
				}
			}else if(resource.get(i).getAmountType().equals("SET")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"SET\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"SET\""));
				}
			}else if(resource.get(i).getAmountType().equals("DOZ")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"DOZ\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"DOZ\""));
				}
			}else if(resource.get(i).getAmountType().equals("PAIR")){
				row.getCell(5).setCellValue(resource.get(i).getItemCount());
				row.getCell(5).setCellStyle(itemCountCellStyle);
				if(getDoubleResult(resource.get(i).getItemCount()).equals("#,##0_ ")) {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"PAIR\""));
				}else {
					row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"PAIR\""));
				}
			}
			
			
			
			
			if (resource.get(i).getBoxCount() == null) {
				row.getCell(6).setCellValue(0);
				row.getCell(6).setCellStyle(boxCountCellStyle);
//				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				//20220726
				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
			} else {
				row.getCell(6).setCellValue(resource.get(i).getBoxCount());
				row.getCell(6).setCellStyle(boxCountCellStyle);
//				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				if(getDoubleResult(resource.get(i).getBoxCount()).equals("#,##0_ ")) {
					row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
				}else {
					row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
				}
			}

			row.getCell(7).setCellValue(resource.get(i).getWeight() == null ? 0 :resource.get(i).getWeight());
			row.getCell(7).setCellStyle(weightCellStyle);
//			row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
//			row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(i).getWeight())));
			if(getDoubleResult(resource.get(i).getWeight()).equals("#,##0_ ")) {
				row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
			}else {
				row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ "));
			}
			row.getCell(8).setCellValue(resource.get(i).getCbm() == null ? 0 :resource.get(i).getCbm());
			row.getCell(8).setCellStyle(cbmCellStyle);
			row.getCell(8).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("0.000_ "));
			
						
			
			if (resource.get(i).getReportPrice() == null) {
			} else {
				if(resource.get(i).getCurrencyType().equals("$")){
					row.getCell(9).setCellValue(resource.get(i).getReportPrice());
					row.getCell(9).setCellStyle(reportPriceCellStyle);
					row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_-[$$-en-US]* #,##0.000_ ;_-[$$-en-US]* -#,##0.000 ;_-[$$-en-US]* \"-\"???_ ;_-@_ "));
					
				}else {
					row.getCell(9).setCellValue(resource.get(i).getReportPrice());
					row.getCell(9).setCellStyle(reportPriceCellStyle);
					row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ "));
				}
				
			}
			row.getCell(10).setCellValue(resource.get(i).getMemo1());
			row.getCell(10).setCellStyle(memo1CellStyle);
			row.getCell(11).setCellValue(resource.get(i).getMemo2());
			row.getCell(11).setCellStyle(memo2CellStyle);
			row.getCell(12).setCellValue(resource.get(i).getItemNo());
			row.getCell(12).setCellStyle(itemNoCellStyle);
			row.getCell(13).setCellValue(resource.get(i).getJejil());
			row.getCell(13).setCellStyle(jejilCellStyle);
			row.getCell(14).setCellValue(resource.get(i).getHsCode());
			row.getCell(14).setCellStyle(hsCodeCellStyle);
			row.getCell(15).setCellValue(resource.get(i).getCoCode());
			if(resource.get(i).getCoCode().equals("안받음")) {
				row.getCell(15).setCellStyle(cellStyle1);
			}else {
				row.getCell(15).setCellStyle(cellStyle);
			}
			
			row.getCell(15).getCellStyle().setBorderBottom(BorderStyle.HAIR);
			row.getCell(15).getCellStyle().setBorderRight(BorderStyle.HAIR); 
			if (resource.get(i).getTotalPrice() == null) {
				row.getCell(16).setCellValue("");
				row.getCell(16).setCellStyle(totalPriceCellStyle);
				
			} else {
				if(resource.get(i).getCurrencyType().equals("$")){
					row.getCell(16).setCellValue(resource.get(i).getTotalPrice());
					row.getCell(16).setCellStyle(totalPriceCellStyle);
					row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_-[$$-en-US]* #,##0.000_ ;_-[$$-en-US]* -#,##0.000 ;_-[$$-en-US]* \"-\"???_ ;_-@_ "));
				}else {
					row.getCell(16).setCellValue(resource.get(i).getTotalPrice());
					row.getCell(16).setCellStyle(totalPriceCellStyle);
					row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ "));
				}
				
			}

			row.getCell(17).setCellValue(resource.get(i).getEngNm());
			row.getCell(17).setCellStyle(engNmCellStyle);
			row.getCell(18).setCellValue(resource.get(i).getMasterCompany());
			if (resource.get(i).getBlNo() == null || resource.get(i).getBlNo() == "") {
				row.getCell(19).setCellValue("");
				row.getCell(19).getCellStyle().setBorderBottom(BorderStyle.HAIR);
				row.getCell(19).getCellStyle().setBorderRight(BorderStyle.THIN); 
			} else {
				row.getCell(19).setCellValue(resource.get(i).getBlNo());
				row.getCell(19).getCellStyle().setBorderBottom(BorderStyle.HAIR);
				row.getCell(19).getCellStyle().setBorderRight(BorderStyle.THIN); 
			}
			
			

		}
		// merge 규칙
		for (int i = 0; i < resource.size(); i++) {

			XSSFRow row = sheet.getRow(startRow+i);
			if (resource.get(i).getOrderNoStrSpan() > 1) {
//				sheet.addMergedRegion(new CellRangeAddress(1, 4, 0, 1)); // 열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getOrderNoStrSpan()-1), 0, 0));
			}
			if (resource.get(i).getWorkDateStrSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getWorkDateStrSpan()-1), 1, 1));
			}
			if (resource.get(i).getCompanyNmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getCompanyNmSpan()-1), 2, 2));
			}
			if (resource.get(i).getMarkingSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getMarkingSpan()-1), 3, 3));
			}
			if (resource.get(i).getKorNmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getKorNmSpan()-1), 4, 4));
			}
			if (resource.get(i).getItemCountSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getItemCountSpan()-1), 5, 5));
			}
			if (resource.get(i).getBoxCountSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getBoxCountSpan()-1), 6, 6));
			}
			if (resource.get(i).getWeightSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getWeightSpan()-1), 7, 7));
			}
			if (resource.get(i).getCbmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getCbmSpan()-1), 8, 8));
			}
			if (resource.get(i).getReportPriceSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getReportPriceSpan()-1), 9, 9));
			}
			if (resource.get(i).getMemo1Span() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getMemo1Span()-1), 10, 10));
			}
			if (resource.get(i).getMemo2Span() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getMemo2Span()-1), 11, 11));
			}
			if (resource.get(i).getItemNoSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getItemNoSpan()-1), 12, 12));
			}
			if (resource.get(i).getJejilSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getJejilSpan()-1), 13, 13));
			}
			if (resource.get(i).getHsCodeSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getHsCodeSpan()-1), 14, 14));
			}
//			if (resource.get(i).getCoIdSpan() > 1) {
//				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getCoIdSpan()-1), 15, 15));
//			}
			if (resource.get(i).getTotalPriceSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getTotalPriceSpan()-1), 16, 16));
			}
			if (resource.get(i).getEngNmSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getEngNmSpan()-1), 17, 17));
			}
			if (resource.get(i).getMasterCompanySpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getMasterCompanySpan()-1), 18, 18));
			}
			if (resource.get(i).getBlNoSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((startRow+i), (startRow+i + resource.get(i).getBlNoSpan()-1), 19, 19));
			}
		}
	}
	


	private void step02ForPreview(List<InboundRes> resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


		// item 채우기
//		for (int i = 0; i < resource.size(); i++) {

//			if ((i + 1) == resource.size()) {
//
//			} else {
//				
//				copyRowForInbound(workbook, sheet, i + 1, i + 2);
//			}


//			XSSFRow row = sheet.getRow(resource.size() + 1);
			XSSFRow row = sheet.getRow(startRow);
			
			row.getCell(5).setCellValue(resource.get(0).getItemCountSum());
			row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(0).getItemCountSum())));
			if (resource.get(0).getBoxCountSum() == null) {
			} else {
				row.getCell(6).setCellValue(resource.get(0).getBoxCountSum());
				row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(0).getBoxCountSum())));
			}

			row.getCell(7).setCellValue(resource.get(0).getWeightSum());
			row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.get(0).getWeightSum())));
			row.getCell(8).setCellValue(resource.get(0).getCbmSum());
			

			if (resource.get(0).getTotalPriceSum() == null) {
				row.getCell(16).setCellValue("");
			} else {
//				row.getCell(16).setCellValue(resource.get(0).getTotalPriceSum());
				if(resource.get(0).getCurrencyType().equals("$")){
					row.getCell(16).setCellValue(resource.get(0).getTotalPriceSum());
				}else {
					row.getCell(16).setCellValue(resource.get(0).getTotalPriceSum());
					row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("_ [$¥-ii-CN]* #,##0.00_ ;_ [$¥-ii-CN]* -#,##0.00_ ;_ [$¥-ii-CN]* \"-\"??_ ;_ @_ "));
				}
			}

			row.getCell(17).setCellValue(resource.get(0).getFreight());
			row.getCell(18).setCellValue("담당자"+" "+":"+" "+resource.get(0).getManagerNm());
			

//		}


	}
	private void step03ForPreview(InboundViewListRes resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


//		else if(resource.get(i).getAmountType().equals("YD")){
//			row.getCell(5).setCellValue(resource.get(i).getItemCount());
//			row.getCell(5).setCellStyle(itemCountCellStyle);
//			row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"YD\""));
			XSSFRow row = sheet.getRow(startRow);
			
			row.getCell(5).setCellValue(resource.getItemCountSumFinalD());
//			Double itemCountSumFinal = Double.valueOf(resource.getItemCountSumFinal());
//			row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
			row.getCell(5).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat(getDoubleResult(resource.getItemCountSumFinalD())));
			if (resource.getBoxCountSumFinal() == null) {
			} else {
				if(resource.getPackingType().equals("CTN")) {
					row.getCell(6).setCellValue(resource.getBoxCountSumFinalD());
					if(getDoubleResult(resource.getBoxCountSumFinalD()).equals("#,##0_ ")) {
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"CTN\""));
					}else {
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"CTN\""));
					}
					
					
					
				}
				else if(resource.getPackingType().equals("PL")) {
					row.getCell(6).setCellValue(resource.getBoxCountSumFinalD());
					if(getDoubleResult(resource.getBoxCountSumFinalD()).equals("#,##0_ ")) {
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"PL\""));
					}else {
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"PL\""));
					}
				}
				else if(resource.getPackingType().equals("GT")) {
					row.getCell(6).setCellValue(resource.getBoxCountSumFinalD());
					if(getDoubleResult(resource.getBoxCountSumFinalD()).equals("#,##0_ ")) {
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"GT\""));
					}else {
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"GT\""));
					}
				}
				
			}

			row.getCell(7).setCellValue(resource.getWeightSumFinalD());
			if(getDoubleResult(resource.getWeightSumFinalD()).equals("#,##0_ ")) {
				row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ \"KG\""));
			}else {
				row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0.00_ \"KG\""));
			}
			row.getCell(8).setCellValue(resource.getCbmSumFinalD());
			row.getCell(8).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("0.000_ \"CBM\""));
			

	}
	private void step05ForPreview(List<UnbiRes> resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


			XSSFRow row = sheet.getRow(startRow);

			int startNum = startRow+1-resource.size();
			int endNum = startRow+1-1;
			
			String a = "SUM(";
			String b = String.valueOf(startNum);
			String c = String.valueOf(endNum);
			String d = ":";
			String e = ")";

			String unbiSum = a+"G"+b+d+"G"+c+e;
			String pickupCostSum = a+"H"+b+d+"H"+c+e;
			String sanghachaCostSum = a+"I"+b+d+"I"+c+e;
			String officeNameSum = a+"J"+b+d+"J"+c+e;
			String hacksodanCostSum = a+"K"+b+d+"K"+c+e;
			String coCostSum = a+"L"+b+d+"L"+c+e;
			String hwajumiUnbiSum = a+"M"+b+d+"M"+c+e;
			String hwajumiPickupCostSum = a+"N"+b+d+"N"+c+e;
			String containerWorkCostSum = a+"O"+b+d+"O"+c+e;
			String containerWorkCost2Sum = a+"P"+b+d+"P"+c+e;
			String containerMoveCostSum = a+"Q"+b+d+"Q"+c+e;
			String totalSumFinal = a+"R"+b+d+"R"+c+e;
			
			
			
//			row.getCell(6).setCellValue(resource.get(0).getUnbiSum());
//			row.getCell(6).setCellType(HSSFCell.CELL_TYPE_FORMULA);
			row.getCell(6).setCellFormula(unbiSum);
			row.getCell(7).setCellFormula(pickupCostSum);
			row.getCell(8).setCellFormula(sanghachaCostSum);
			row.getCell(9).setCellFormula(officeNameSum);
			row.getCell(10).setCellFormula(hacksodanCostSum);
			row.getCell(11).setCellFormula(coCostSum);
			row.getCell(12).setCellFormula(hwajumiUnbiSum);
			row.getCell(13).setCellFormula(hwajumiPickupCostSum);
			row.getCell(14).setCellFormula(containerWorkCostSum);
			row.getCell(15).setCellFormula(containerWorkCost2Sum);
			row.getCell(16).setCellFormula(containerMoveCostSum);
			row.getCell(17).setCellFormula(totalSumFinal);
			
			
			
			
			
			
			//			row.getCell(6).setCellValue(resource.get(0).getUnbiSum());
//			row.getCell(7).setCellValue(resource.get(0).getPickupCostSum());
//			row.getCell(8).setCellValue(resource.get(0).getSanghachaCostSum());
//			row.getCell(9).setCellValue(resource.get(0).getOfficeNameSum());
//			row.getCell(10).setCellValue(resource.get(0).getHacksodanCostSum());
//			row.getCell(11).setCellValue(resource.get(0).getCoCostSum());
//			row.getCell(12).setCellValue(resource.get(0).getHwajumiUnbiSum());
//			row.getCell(13).setCellValue(resource.get(0).getHwajumiPickupCostSum());
//			row.getCell(14).setCellValue(resource.get(0).getContainerWorkCostSum());
//			row.getCell(15).setCellValue(resource.get(0).getContainerWorkCost2Sum());
//			row.getCell(16).setCellValue(resource.get(0).getContainerMoveCostSum());
//			row.getCell(17).setCellValue(resource.get(0).getTotalSumFinal());

	}
	private void step10ForPreview(List<UnbiRes> resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


		XSSFRow row = sheet.getRow(startRow+6);
		int startNum = startRow+6+1-resource.size();
		int endNum = startRow+6+1-1;
		
		String a = "SUM(";
		String b = String.valueOf(startNum);
		String c = String.valueOf(endNum);
		String d = ":";
		String e = ")";

		String unbiSum = a+"G"+b+d+"G"+c+e;
		String pickupCostSum = a+"H"+b+d+"H"+c+e;
		String sanghachaCostSum = a+"I"+b+d+"I"+c+e;
		String officeNameSum = a+"J"+b+d+"J"+c+e;
		String hacksodanCostSum = a+"K"+b+d+"K"+c+e;
		String coCostSum = a+"L"+b+d+"L"+c+e;
		String hwajumiUnbiSum = a+"M"+b+d+"M"+c+e;
		String hwajumiPickupCostSum = a+"N"+b+d+"N"+c+e;
		String containerWorkCostSum = a+"O"+b+d+"O"+c+e;
		String containerWorkCost2Sum = a+"P"+b+d+"P"+c+e;
		String containerMoveCostSum = a+"Q"+b+d+"Q"+c+e;
		String totalSumFinal = a+"R"+b+d+"R"+c+e;
		
		
		
//		row.getCell(6).setCellValue(resource.get(0).getUnbiSum());
//		row.getCell(6).setCellType(HSSFCell.CELL_TYPE_FORMULA);
		row.getCell(6).setCellFormula(unbiSum);
		row.getCell(7).setCellFormula(pickupCostSum);
		row.getCell(8).setCellFormula(sanghachaCostSum);
		row.getCell(9).setCellFormula(officeNameSum);
		row.getCell(10).setCellFormula(hacksodanCostSum);
		row.getCell(11).setCellFormula(coCostSum);
		row.getCell(12).setCellFormula(hwajumiUnbiSum);
		row.getCell(13).setCellFormula(hwajumiPickupCostSum);
		row.getCell(14).setCellFormula(containerWorkCostSum);
		row.getCell(15).setCellFormula(containerWorkCost2Sum);
		row.getCell(16).setCellFormula(containerMoveCostSum);
		row.getCell(17).setCellFormula(totalSumFinal);


}
	private void step11ForPreview(FinalInboundRes resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {



			XSSFRow row = sheet.getRow(startRow);
			
			if (resource.getUnbiDefine1() == null||resource.getUnbiDefine1()==0) {
				row.getCell(6).setCellValue("운비");
			} else {

				for(int k=0; k<UnbiType.getList().size();k++) {
					if(UnbiType.getList().get(k).getId()==resource.getUnbiDefine1()) {
						row.getCell(6).setCellValue(UnbiType.getList().get(k).getName());
					}else {

					}
				}
			}
			if (resource.getUnbiDefine2() == null||resource.getUnbiDefine2()==0) {
				row.getCell(7).setCellValue("픽업비");
			} else {

				for(int k=0; k<UnbiType.getList().size();k++) {
					if(UnbiType.getList().get(k).getId()==resource.getUnbiDefine2()) {
						row.getCell(7).setCellValue(UnbiType.getList().get(k).getName());
					}else {

					}
				}
			}
			if (resource.getUnbiDefine3() == null||resource.getUnbiDefine3()==0) {
				row.getCell(8).setCellValue("상하차비");
			} else {

				for(int k=0; k<UnbiType.getList().size();k++) {
					if(UnbiType.getList().get(k).getId()==resource.getUnbiDefine3()) {
						row.getCell(8).setCellValue(UnbiType.getList().get(k).getName());
					}else {

					}
				}
			}
			if (resource.getUnbiDefine4() == null||resource.getUnbiDefine4()==0) {
				row.getCell(9).setCellValue("화물회사-사무실");
			} else {

				for(int k=0; k<UnbiType.getList().size();k++) {
					if(UnbiType.getList().get(k).getId()==resource.getUnbiDefine4()) {
						row.getCell(9).setCellValue(UnbiType.getList().get(k).getName());
					}else {

					}
				}
			}

			
			

	}
	private void step06ForPreview(FinalInboundRes resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


		XSSFRow row = sheet.getRow(startRow);
		
		row.getCell(3).setCellValue(resource.getForViewWorkDepartDt());
		row.getCell(8).setCellValue(resource.getFinalMasterBl());
		row.getCell(13).setCellValue(resource.getContainerNo());
		row.getCell(17).setCellValue(resource.getMemo());

}
	private void step07ForPreview(FinalInboundRes resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


		XSSFRow row = sheet.getRow(startRow);
		
		row.getCell(3).setCellValue(resource.getDeliveryNm());
		row.getCell(8).setCellValue(resource.getHangName());
		row.getCell(13).setCellValue(resource.getSilNo());
		

}
	private void step08ForPreview(FinalInboundRes resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


		XSSFRow row = sheet.getRow(startRow);
		
		row.getCell(3).setCellValue(resource.getChulhangPort());
		row.getCell(8).setCellValue(resource.getHangCha());
		row.getCell(13).setCellValue(resource.getContainerSizeStr());
		
		

}
	private void step09ForPreview(FinalInboundRes resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {


		XSSFRow row = sheet.getRow(startRow);
		row.getCell(3).setCellValue(resource.getCargoName());
		row.getCell(8).setCellValue(resource.getWeatherCondition());
		row.getCell(13).setCellValue(resource.getContainerCost());

}
	
	 private void copyRowOtherSheetForPreview(XSSFWorkbook workbook, XSSFSheet worksheet,int destinationRowNum, XSSFSheet worksheet2,int sourceRowNum) {
		// Get the source / new row
		XSSFRow newRow = worksheet.getRow(destinationRowNum);
		XSSFRow sourceRow = worksheet2.getRow(sourceRowNum);

		// If the row exist in destination, push down all rows by 1 else create a new
		// row
		if (newRow != null) {
			worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
		} else {
			newRow = worksheet.createRow(destinationRowNum);
		}

		// Loop through source columns to add to new row
		for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
			// Grab a copy of the old/new cell
			XSSFCell oldCell = sourceRow.getCell(i);
			XSSFCell newCell = newRow.createCell(i);

			// If the old cell is null jump to next cell
			if (oldCell == null) {
				newCell = null;
				continue;
			}

			// Copy style from old cell and apply to new cell
			XSSFCellStyle newCellStyle = workbook.createCellStyle();
			newCellStyle.cloneStyleFrom(oldCell.getCellStyle());
			;
			newCell.setCellStyle(newCellStyle);

			// If there is a cell comment, copy
			if (oldCell.getCellComment() != null) {
				newCell.setCellComment(oldCell.getCellComment());
			}

			// If there is a cell hyperlink, copy
			if (oldCell.getHyperlink() != null) {
				newCell.setHyperlink(oldCell.getHyperlink());
			}

			// Set the cell data type
			newCell.setCellType(oldCell.getCellType());

			// Set the cell data value
			switch (oldCell.getCellType()) {
			case Cell.CELL_TYPE_BLANK:
				newCell.setCellValue(oldCell.getStringCellValue());
				break;
			case Cell.CELL_TYPE_BOOLEAN:
				newCell.setCellValue(oldCell.getBooleanCellValue());
				break;
			case Cell.CELL_TYPE_ERROR:
				newCell.setCellErrorValue(oldCell.getErrorCellValue());
				break;
			case Cell.CELL_TYPE_FORMULA:
				newCell.setCellFormula(oldCell.getCellFormula());
				break;
			case Cell.CELL_TYPE_NUMERIC:
				newCell.setCellValue(oldCell.getNumericCellValue());
				break;
			case Cell.CELL_TYPE_STRING:
				newCell.setCellValue(oldCell.getRichStringCellValue());
				break;
			}
		}
		for (int i = 0; i < worksheet2.getMergedRegions().size(); i++) {
			CellRangeAddress cellRangeAddress = worksheet2.getMergedRegion(i);
			 if(sourceRowNum == cellRangeAddress.getFirstRow()) {
			
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(
                		destinationRowNum,destinationRowNum,
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
			 }
        }
		
	}
	
	 private void step04ForPreview(List<UnbiRes> resource, XSSFSheet sheet, XSSFWorkbook workbook, int startRow) {

			
			Font font = workbook.createFont();
			font.setColor(IndexedColors.RED.getIndex());
			font.setFontName("굴림체");
			Font otherFont = workbook.createFont();
			otherFont.setFontName("굴림체");
			
			XSSFCellStyle cellStyle = workbook.createCellStyle();
			cellStyle.setFont(font);
			cellStyle.setAlignment(HorizontalAlignment.CENTER);
			cellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			cellStyle.setBorderBottom(BorderStyle.THIN);
			cellStyle.setBorderRight(BorderStyle.THIN);
			cellStyle.setBorderTop(BorderStyle.HAIR);	
			XSSFSheet sheet4 = workbook.getSheetAt(4);
//			copyRowOtherSheetForInbounds(workbook, sheet, startRow ,sheet4, 0);
//			copyRowOtherSheetForInbounds(workbook, sheet, startRow ,sheet4, 1);
//			copyRowOtherSheetForInbounds(workbook, sheet, startRow ,sheet4, 2);
			XSSFSheet sheet5 = workbook.getSheetAt(5);
			// item 채우기
			for (int i = 0; i < resource.size(); i++) {

				copyRowOtherSheetForInbounds(workbook, sheet, startRow+i ,sheet5, 0);
							

				XSSFRow row = sheet.getRow(startRow+i);
				XSSFCellStyle orderNoFont  = workbook.createCellStyle();
				XSSFCellStyle allCellStyle = workbook.createCellStyle();
				
				XSSFCellStyle unbiCellStyle = workbook.createCellStyle();
				XSSFCellStyle pickupCostCellStyle = workbook.createCellStyle();
				XSSFCellStyle sanghachaCostCellStyle = workbook.createCellStyle();
				XSSFCellStyle officeNameCellStyle = workbook.createCellStyle();
				XSSFCellStyle hacksodanCostCellStyle = workbook.createCellStyle();
				XSSFCellStyle coCostCellStyle = workbook.createCellStyle();
				
				if(resource.get(i).getUnbiYn().equals("Y")){
					unbiCellStyle.setAlignment(HorizontalAlignment.CENTER);
					unbiCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					unbiCellStyle.setBorderBottom(BorderStyle.NONE);
					unbiCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(42, 177, 245)));
					unbiCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					unbiCellStyle.setBorderBottom(BorderStyle.HAIR);
					unbiCellStyle.setBorderRight(BorderStyle.HAIR); 
					unbiCellStyle.setFont(otherFont);
					
				}else {
					unbiCellStyle.setAlignment(HorizontalAlignment.CENTER);
					unbiCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					unbiCellStyle.setBorderBottom(BorderStyle.NONE);
					unbiCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
					unbiCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					unbiCellStyle.setBorderBottom(BorderStyle.HAIR);
					unbiCellStyle.setBorderRight(BorderStyle.HAIR); 
					unbiCellStyle.setFont(otherFont);
				}
				if(resource.get(i).getPickupCostYn().equals("Y")){
					pickupCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					pickupCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					pickupCostCellStyle.setBorderBottom(BorderStyle.NONE);
					pickupCostCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(42, 177, 245)));
					pickupCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					pickupCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					pickupCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					pickupCostCellStyle.setFont(otherFont);
					
				}else {
					pickupCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					pickupCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					pickupCostCellStyle.setBorderBottom(BorderStyle.NONE);
					pickupCostCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
					pickupCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					pickupCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					pickupCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					pickupCostCellStyle.setFont(otherFont);
				}
				if(resource.get(i).getSanghachaCostYn().equals("Y")){
					sanghachaCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					sanghachaCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					sanghachaCostCellStyle.setBorderBottom(BorderStyle.NONE);
					sanghachaCostCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(42, 177, 245)));
					sanghachaCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					sanghachaCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					sanghachaCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					sanghachaCostCellStyle.setFont(otherFont);
					
				}else {
					sanghachaCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					sanghachaCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					sanghachaCostCellStyle.setBorderBottom(BorderStyle.NONE);
					sanghachaCostCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
					sanghachaCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					sanghachaCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					sanghachaCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					sanghachaCostCellStyle.setFont(otherFont);
				}
				if(resource.get(i).getOfficeNameYn().equals("Y")){
					officeNameCellStyle.setAlignment(HorizontalAlignment.CENTER);
					officeNameCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					officeNameCellStyle.setBorderBottom(BorderStyle.NONE);
					officeNameCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(42, 177, 245)));
					officeNameCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					officeNameCellStyle.setBorderBottom(BorderStyle.HAIR);
					officeNameCellStyle.setBorderRight(BorderStyle.HAIR); 
					officeNameCellStyle.setFont(otherFont);
				}else {
					officeNameCellStyle.setAlignment(HorizontalAlignment.CENTER);
					officeNameCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					officeNameCellStyle.setBorderBottom(BorderStyle.NONE);
					officeNameCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
					officeNameCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					officeNameCellStyle.setBorderBottom(BorderStyle.HAIR);
					officeNameCellStyle.setBorderRight(BorderStyle.HAIR); 
					officeNameCellStyle.setFont(otherFont);
				}
				if(resource.get(i).getHacksodanCostYn().equals("Y")){
					hacksodanCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					hacksodanCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					hacksodanCostCellStyle.setBorderBottom(BorderStyle.NONE);
					hacksodanCostCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(42, 177, 245)));
					hacksodanCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					hacksodanCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					hacksodanCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					hacksodanCostCellStyle.setFont(otherFont);
				}else {
					hacksodanCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					hacksodanCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					hacksodanCostCellStyle.setBorderBottom(BorderStyle.NONE);
					hacksodanCostCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
					hacksodanCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					hacksodanCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					hacksodanCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					hacksodanCostCellStyle.setFont(otherFont);
				}
				if(resource.get(i).getCoCostYn().equals("Y")){
					coCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					coCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					coCostCellStyle.setBorderBottom(BorderStyle.NONE);
					coCostCellStyle.setFillForegroundColor(new XSSFColor(new java.awt.Color(42, 177, 245)));
					coCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					coCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					coCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					coCostCellStyle.setFont(otherFont);
				}else {
					coCostCellStyle.setAlignment(HorizontalAlignment.CENTER);
					coCostCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					coCostCellStyle.setBorderBottom(BorderStyle.NONE);
					coCostCellStyle.setFillForegroundColor(IndexedColors.LIGHT_YELLOW.getIndex());
					coCostCellStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
					coCostCellStyle.setBorderBottom(BorderStyle.HAIR);
					coCostCellStyle.setBorderRight(BorderStyle.HAIR); 
					coCostCellStyle.setFont(otherFont);			
					}
				
				
				
				
				Font allFont = workbook.createFont();
				allFont.setFontName("굴림체");
				
				orderNoFont.setFillForegroundColor(new XSSFColor(new java.awt.Color(255, 255, 204)));
				orderNoFont.setFillPattern(FillPatternType.SOLID_FOREGROUND);
				orderNoFont.setAlignment(HorizontalAlignment.CENTER);
				orderNoFont.setVerticalAlignment(VerticalAlignment.CENTER);
				orderNoFont.setBorderBottom(BorderStyle.HAIR);
				orderNoFont.setBorderRight(BorderStyle.HAIR);
				orderNoFont.setBorderLeft(BorderStyle.THIN);
				orderNoFont.setFont(allFont);
				HSSFWorkbook hwb = new HSSFWorkbook();
				if(resource.get(i).getType().equals("A")) {
					if(resource.get(i).getColorId()==0) {
						allFont.setColor(IndexedColors.BLACK.getIndex());
					}else if(resource.get(i).getColorId()==1){
						allFont.setColor(IndexedColors.BLUE.getIndex());
					}else if(resource.get(i).getColorId()==2){
						allFont.setColor(IndexedColors.RED.getIndex());
					}else if(resource.get(i).getColorId()==3){
						allFont.setColor(IndexedColors.GREEN.getIndex());
					}else if(resource.get(i).getColorId()==4){
						allFont.setColor(IndexedColors.PINK.getIndex());
					}else if(resource.get(i).getColorId()==5){
						allFont.setColor(IndexedColors.ORANGE.getIndex());
					}else if(resource.get(i).getColorId()==6){
						allFont.setColor(IndexedColors.VIOLET.getIndex());
					}else if(resource.get(i).getColorId()==7){
						HSSFPalette palette = hwb.getCustomPalette();
						// get the color which most closely matches the color you want to use
						HSSFColor myColor = palette.findSimilarColor(0, 216, 255);
						// get the palette index of that color 
						short palIndex = myColor.getIndex();
						allFont.setColor(palIndex);
					}else if(resource.get(i).getColorId()==8){
						HSSFPalette palette = hwb.getCustomPalette();
						// get the color which most closely matches the color you want to use
						HSSFColor myColor = palette.findSimilarColor(171, 242, 0);
						// get the palette index of that color 
						short palIndex = myColor.getIndex();
						allFont.setColor(palIndex);
					}else if(resource.get(i).getColorId()==9){
						HSSFPalette palette = hwb.getCustomPalette();
						// get the color which most closely matches the color you want to use
						HSSFColor myColor = palette.findSimilarColor(138, 73, 36);
						// get the palette index of that color 
						short palIndex = myColor.getIndex();
						allFont.setColor(palIndex);
					}
					allCellStyle.setAlignment(HorizontalAlignment.CENTER);
					allCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					allCellStyle.setBorderBottom(BorderStyle.HAIR);
					allCellStyle.setBorderRight(BorderStyle.HAIR);
					allCellStyle.setWrapText(true);
				
					allCellStyle.setFont(allFont);
					row.getCell(0).setCellValue((resource.get(i).getNoStr()==null?"":resource.get(i).getNoStr()));
					row.getCell(0).setCellStyle(orderNoFont);
					row.getCell(1).setCellValue((resource.get(i).getWorkDateStr()==null?"":resource.get(i).getWorkDateStr()));
					row.getCell(1).setCellStyle(allCellStyle);
					row.getCell(2).setCellValue(resource.get(i).getCompanyNm());
					row.getCell(2).setCellStyle(allCellStyle);
					row.getCell(3).setCellValue(resource.get(i).getMarking());
					row.getCell(3).setCellStyle(allCellStyle);
					row.getCell(4).setCellValue(resource.get(i).getKorNm());
					row.getCell(4).setCellStyle(allCellStyle);
					row.getCell(5).setCellValue(resource.get(i).getDepartPort());
					row.getCell(5).setCellStyle(allCellStyle);
					if(resource.get(i).getUnbiD()==0) {
						row.getCell(6).setCellValue("");
						row.getCell(6).setCellStyle(unbiCellStyle);
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(6).setCellValue(resource.get(i).getUnbiD());
						row.getCell(6).setCellStyle(unbiCellStyle);
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getPickupCostD()==0) {
						row.getCell(7).setCellValue("");
						row.getCell(7).setCellStyle(pickupCostCellStyle);
						row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(7).setCellValue(resource.get(i).getPickupCostD());
						row.getCell(7).setCellStyle(pickupCostCellStyle);
						row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getSanghachaCostD()==0) {
						row.getCell(8).setCellValue("");
						row.getCell(8).setCellStyle(sanghachaCostCellStyle);
						row.getCell(8).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(8).setCellValue(resource.get(i).getSanghachaCostD());
						row.getCell(8).setCellStyle(sanghachaCostCellStyle);
						row.getCell(8).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getOfficeNameD()==0) {
						row.getCell(9).setCellValue("");
						row.getCell(9).setCellStyle(officeNameCellStyle);
						row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(9).setCellValue(resource.get(i).getOfficeNameD());
						row.getCell(9).setCellStyle(officeNameCellStyle);
						row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getHacksodanCostD()==0) {
						row.getCell(10).setCellValue("");
						row.getCell(10).setCellStyle(hacksodanCostCellStyle);
						row.getCell(10).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(10).setCellValue(resource.get(i).getHacksodanCostD());
						row.getCell(10).setCellStyle(hacksodanCostCellStyle);
						row.getCell(10).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getCoCostD()==0) {
						row.getCell(11).setCellValue("");
						row.getCell(11).setCellStyle(coCostCellStyle);
						row.getCell(11).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(11).setCellValue(resource.get(i).getCoCostD());
						row.getCell(11).setCellStyle(coCostCellStyle);
						row.getCell(11).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getHwajumiUnbiD()==0) {
						row.getCell(12).setCellValue("");
						row.getCell(12).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(12).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(12).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(12).setCellValue(resource.get(i).getHwajumiUnbiD());
						row.getCell(12).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(12).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(12).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getHwajumiPickupCostD()==0) {
						row.getCell(13).setCellValue("");
						row.getCell(13).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(13).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(13).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(13).setCellValue(resource.get(i).getHwajumiPickupCostD());
						row.getCell(13).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(13).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(13).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getContainerWorkCostD()==0) {
						row.getCell(14).setCellValue("");
						row.getCell(14).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(14).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(14).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(14).setCellValue(resource.get(i).getContainerWorkCostD());
						row.getCell(14).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(14).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(14).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getContainerWorkCost2D()==0) {
						row.getCell(15).setCellValue("");
						row.getCell(15).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(15).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(15).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(15).setCellValue(resource.get(i).getContainerWorkCost2D());
						row.getCell(15).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(15).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(15).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getContainerMoveCostD()==0) {
						row.getCell(16).setCellValue("");
						row.getCell(16).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(16).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(16).setCellValue(resource.get(i).getContainerMoveCostD());
						row.getCell(16).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(16).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getTotalSum()==0) {
						row.getCell(17).setCellValue("");
						row.getCell(17).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(17).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(17).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(17).setCellValue(resource.get(i).getTotalSum());
						row.getCell(17).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(17).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(17).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					
					
					
					row.getCell(18).setCellValue(resource.get(i).getMemo1());
					row.getCell(18).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(18).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					row.getCell(18).getCellStyle().setWrapText(true);
					row.getCell(19).setCellValue(resource.get(i).getMemo2());
					row.getCell(19).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(19).getCellStyle().setBorderRight(BorderStyle.THIN); 
					row.getCell(19).getCellStyle().setWrapText(true);
				}else {
					if(resource.get(i).getColorId()==0) {
						allFont.setColor(IndexedColors.BLACK.getIndex());
					}else if(resource.get(i).getColorId()==1){
						allFont.setColor(IndexedColors.BLUE.getIndex());
					}else if(resource.get(i).getColorId()==2){
						allFont.setColor(IndexedColors.RED.getIndex());
					}else if(resource.get(i).getColorId()==3){
						allFont.setColor(IndexedColors.GREEN.getIndex());
					}else if(resource.get(i).getColorId()==4){
						allFont.setColor(IndexedColors.PINK.getIndex());
					}else if(resource.get(i).getColorId()==5){
						allFont.setColor(IndexedColors.ORANGE.getIndex());
					}else if(resource.get(i).getColorId()==6){
						allFont.setColor(IndexedColors.VIOLET.getIndex());
					}else if(resource.get(i).getColorId()==7){
						HSSFPalette palette = hwb.getCustomPalette();
						// get the color which most closely matches the color you want to use
						HSSFColor myColor = palette.findSimilarColor(0, 216, 255);
						// get the palette index of that color 
						short palIndex = myColor.getIndex();
						allFont.setColor(palIndex);
					}else if(resource.get(i).getColorId()==8){
						HSSFPalette palette = hwb.getCustomPalette();
						// get the color which most closely matches the color you want to use
						HSSFColor myColor = palette.findSimilarColor(171, 242, 0);
						// get the palette index of that color 
						short palIndex = myColor.getIndex();
						allFont.setColor(palIndex);
					}else if(resource.get(i).getColorId()==9){
						HSSFPalette palette = hwb.getCustomPalette();
						// get the color which most closely matches the color you want to use
						HSSFColor myColor = palette.findSimilarColor(138, 73, 36);
						// get the palette index of that color 
						short palIndex = myColor.getIndex();
						allFont.setColor(palIndex);
					}
					allCellStyle.setAlignment(HorizontalAlignment.CENTER);
					allCellStyle.setVerticalAlignment(VerticalAlignment.CENTER);
					allCellStyle.setBorderBottom(BorderStyle.NONE);
				
					allCellStyle.setFont(allFont);
					row.getCell(0).setCellValue((resource.get(i).getNoStr()==null?"":resource.get(i).getNoStr()));
					row.getCell(0).setCellStyle(orderNoFont);
					row.getCell(0).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(0).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					row.getCell(1).setCellValue((resource.get(i).getWorkDateStr()==null?"":resource.get(i).getWorkDateStr()));
					row.getCell(1).setCellStyle(allCellStyle);
					row.getCell(1).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(1).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					row.getCell(2).setCellValue(resource.get(i).getTypebTitle());
					row.getCell(2).setCellStyle(allCellStyle);
					row.getCell(2).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(2).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					row.getCell(3).setCellValue("");
					row.getCell(3).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(3).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					row.getCell(4).setCellValue("");
					row.getCell(4).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(4).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					row.getCell(5).setCellValue("");
					row.getCell(5).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(5).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					if(resource.get(i).getUnbiD()==0) {
						row.getCell(6).setCellValue("");
						row.getCell(6).setCellStyle(unbiCellStyle);
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(6).setCellValue(resource.get(i).getUnbiD());
						row.getCell(6).setCellStyle(unbiCellStyle);
						row.getCell(6).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getPickupCostD()==0) {
						row.getCell(7).setCellValue("");
						row.getCell(7).setCellStyle(pickupCostCellStyle);
						row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(7).setCellValue(resource.get(i).getPickupCostD());
						row.getCell(7).setCellStyle(pickupCostCellStyle);
						row.getCell(7).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getSanghachaCostD()==0) {
						row.getCell(8).setCellValue("");
						row.getCell(8).setCellStyle(sanghachaCostCellStyle);
						row.getCell(8).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(8).setCellValue(resource.get(i).getSanghachaCostD());
						row.getCell(8).setCellStyle(sanghachaCostCellStyle);
						row.getCell(8).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getOfficeNameD()==0) {
						row.getCell(9).setCellValue("");
						row.getCell(9).setCellStyle(officeNameCellStyle);
						row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(9).setCellValue(resource.get(i).getOfficeNameD());
						row.getCell(9).setCellStyle(officeNameCellStyle);
						row.getCell(9).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getHacksodanCostD()==0) {
						row.getCell(10).setCellValue("");
						row.getCell(10).setCellStyle(hacksodanCostCellStyle);
						row.getCell(10).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(10).setCellValue(resource.get(i).getHacksodanCostD());
						row.getCell(10).setCellStyle(hacksodanCostCellStyle);
						row.getCell(10).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getCoCostD()==0) {
						row.getCell(11).setCellValue("");
						row.getCell(11).setCellStyle(coCostCellStyle);
						row.getCell(11).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(11).setCellValue(resource.get(i).getCoCostD());
						row.getCell(11).setCellStyle(coCostCellStyle);
						row.getCell(11).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getHwajumiUnbiD()==0) {
						row.getCell(12).setCellValue("");
						row.getCell(12).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(12).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(12).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(12).setCellValue(resource.get(i).getHwajumiUnbiD());
						row.getCell(12).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(12).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(12).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getHwajumiPickupCostD()==0) {
						row.getCell(13).setCellValue("");
						row.getCell(13).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(13).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(13).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(13).setCellValue(resource.get(i).getHwajumiPickupCostD());
						row.getCell(13).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(13).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(13).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getContainerWorkCostD()==0) {
						row.getCell(14).setCellValue("");
						row.getCell(14).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(14).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(14).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(14).setCellValue(resource.get(i).getContainerWorkCostD());
						row.getCell(14).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(14).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(14).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getContainerWorkCost2D()==0) {
						row.getCell(15).setCellValue("");
						row.getCell(15).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(15).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(15).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(15).setCellValue(resource.get(i).getContainerWorkCost2D());
						row.getCell(15).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(15).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(15).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getContainerMoveCostD()==0) {
						row.getCell(16).setCellValue("");
						row.getCell(16).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(16).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(16).setCellValue(resource.get(i).getContainerMoveCostD());
						row.getCell(16).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(16).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(16).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					if(resource.get(i).getTotalSum()==0) {
						row.getCell(17).setCellValue("");
						row.getCell(17).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(17).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(17).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}else {
						row.getCell(17).setCellValue(resource.get(i).getTotalSum());
						row.getCell(17).getCellStyle().setBorderBottom(BorderStyle.HAIR);
						row.getCell(17).getCellStyle().setBorderRight(BorderStyle.HAIR); 
						row.getCell(17).getCellStyle().setDataFormat(workbook.createDataFormat().getFormat("#,##0_ "));
					}
					row.getCell(18).setCellValue(resource.get(i).getMemo1());
					row.getCell(18).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(18).getCellStyle().setBorderRight(BorderStyle.HAIR); 
					row.getCell(19).setCellValue(resource.get(i).getMemo2());
					row.getCell(19).getCellStyle().setBorderBottom(BorderStyle.HAIR);
					row.getCell(19).getCellStyle().setBorderRight(BorderStyle.THIN); 
				}
				
				


			}

		}
	 private void shiftRowforPreview(int destinationRowNum, XSSFSheet sheet, int makeRowNum) {
		 sheet.shiftRows(destinationRowNum, sheet.getLastRowNum(), makeRowNum);
		}
	@Override
	public boolean inbounds(InboundViewListRes listRes, HttpServletResponse response) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	
	@Override
	public List<ExcelInpackRes> listInpackData(FinalInboundInboundMasterReq req) throws Exception {
		List<ExcelInpackRes> finalResult = new ArrayList<>();
		List<FinalInboundInboundMaster> fiiList =  new ArrayList<>();
		for(int i=0; i<req.getIds().size(); i++) {
			fiiList.add(_FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(),req.getIds().get(i)));
		}
		for (int j =0; j<fiiList.size(); j++) {
		
		ExcelInpackRes result = _FinalInboundInboundMasterRepository.findById(fiiList.get(j).getId()).map(t -> {
			Integer countPCS= 0;
			Integer countM= 0;
			Integer countKG= 0;
			Integer countYD= 0;
			Integer countSET=0;
			Integer countDOZ=0;
			Integer countPAIR=0;
			
			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			ExcelInpackRes item = ExcelInpackRes.builder().build();
			List<ExcelInpackSubRes> list = new ArrayList<>();


			String departDt = t.getFinalInbound().getDepartDtStr();
			StringBuffer sb = new StringBuffer();
			String companyNum=t.getInboundMaster().getCompanyInfo().getCoNum();
			sb.append(companyNum);
			sb.insert(3, "-");
			sb.insert(6, "-");
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setInboundMasterId(t.getInboundMaster().getId());
			item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
					+ t.getInboundMaster().getComExport().getValue2());
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoInvoice());
			item.setData05(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ sb.toString() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			item.setData06(departDt);
			item.setData07(t.getFinalInbound().getHangName() + "/" + t.getFinalInbound().getHangCha());
		
			item.setData08((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			item.setData09((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			if(t.getInboundMaster().getCurrencyType()==null||t.getInboundMaster().getCurrencyType().equals("$")) {
				item.setData10("Unit Price" + "\n" + "(USD)");
				item.setData11("Amount" + "\n" + "(USD)");
			}else {
				item.setData10("Unit Price" + "\n" + "(CNY)");
				item.setData11("Amount" + "\n" + "(CNY)");
			}
			if(t.getInboundMaster().getPackingType()==null||t.getInboundMaster().getPackingType().equals("CTN")) {
				item.setData12("CTNS");
			}else if(t.getInboundMaster().getPackingType().equals("PL")) {
				item.setData12("PLS");
			}else if(t.getInboundMaster().getPackingType().equals("GT")) {
				item.setData12("GTS");
			}
			item.setCoYn(false);
			if(t.getInboundMaster().getCompanyInfo().getCoInvoice()==null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date departDate=dateFormat2.parse(departDt);
					String departDateStr = dateFormat.format(departDate);
					String data=""+departDateStr;
					item.setInvoice(data);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				item.setInvoice("");
			}else {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
				SimpleDateFormat dateFormat2 = new SimpleDateFormat("yyyy-MM-dd");
				try {
					Date departDate=dateFormat2.parse(departDt);
					String departDateStr = dateFormat.format(departDate);
					String data=t.getInboundMaster().getCompanyInfo().getCoInvoice()+departDateStr;
					item.setInvoice(data);
					
				} catch (ParseException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			}
			Date today = new Date();
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyMMdd");
			String todayStr = dateFormat.format(today);
			item.setNoCoDt("SJ-"+todayStr);			
			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
//			Map<Long, String> markingInfo = new HashMap<>();
//			markingInfo = _utilService.getMakingForYATAI(inbound_list);

			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).collect(Collectors.toList());

			List<ExcelInpackSubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelInpackSubRes subRes = ExcelInpackSubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setJejil(origin_inbound_list.get(i).getJejil());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				if(origin_inbound_list.get(i).getReportPrice()==null||origin_inbound_list.get(i).getReportPrice()==0) {
					
					subRes.setItemPrice(new Double(0));
				}else {
					subRes.setItemPrice(origin_inbound_list.get(i).getReportPrice());
				}
				if(origin_inbound_list.get(i).getReportPrice()==null||origin_inbound_list.get(i).getReportPrice()==0) {
					subRes.setTotalPrice(new Double(0));
				}else {
					subRes.setTotalPrice(new Double(String.format("%.2f",
							origin_inbound_list.get(i).getReportPrice() * origin_inbound_list.get(i).getItemCount())));
				}
				
				if(origin_inbound_list.get(i).getBoxCount()==null) {
					subRes.setBoxCount(new Double(0));
				}else {
					subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				}
				
				subRes.setTotalWeight(origin_inbound_list.get(i).getWeight());
				subRes.setWeight((subRes.getTotalWeight()  == null ? 0d : subRes.getTotalWeight()) - (subRes.getBoxCount() == null ? 0d : subRes.getBoxCount()) );
				subRes.setCbm(origin_inbound_list.get(i).getCbm());
				if(origin_inbound_list.get(i).getCoId()!=3) {
					item.setCoYn(true);
				}
				if(t.getInboundMaster().getCurrencyType()==null||t.getInboundMaster().getCurrencyType().equals("$")) {
					subRes.setCurrencyType("$");
				}else {
					subRes.setCurrencyType(t.getInboundMaster().getCurrencyType());
				}
				if(t.getInboundMaster().getPackingType()==null||t.getInboundMaster().getPackingType().equals("CTN")) {
					subRes.setPackingType("CTNS");
				}else {
					subRes.setPackingType(t.getInboundMaster().getPackingType());
				}
				subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				if(origin_inbound_list.get(i).getAmountType()==null||origin_inbound_list.get(i).getAmountType().equals("PCS")){
					countPCS=countPCS+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("M")){
					countM=countM+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("YD")){
					countYD=countYD+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("KG")){
					countKG=countKG+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("SET")){
					countSET=countSET+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("DOZ")){
					countDOZ=countDOZ+1;
				}else if(origin_inbound_list.get(i).getAmountType().equals("PAIR")){
					countPAIR=countPAIR+1;
				}
				subRes.setMarking(origin_inbound_list.get(i).getMarking()==null?"":origin_inbound_list.get(i).getMarking());
				sublist.add(subRes);

			}

//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
//			item.setTotalBoxCountEng("TOTAL: ("+String.valueOf(total.intValue())+")"+" CTNS OF"); 
			item.setCountPCS(countPCS);
			item.setCountM(countM);	
			item.setCountYD(countYD);
			item.setCountKG(countKG);
			item.setCountSET(countSET);		
			item.setCountDOZ(countDOZ);
			item.setCountPAIR(countPAIR);
			item.setSubItem(sublist);
			return item;
		}).get();
		finalResult.add(result);	
		}
		return finalResult;
	}

	
	@Override
	public List<ExcelFTARes> listFtaData(FinalInboundInboundMasterReq req) throws Exception {
		List<ExcelFTARes> finalResult = new ArrayList<>();
		List<FinalInboundInboundMaster> fiiList =  new ArrayList<>();
		List<Inbound> checkInbound = new ArrayList<>();
		for(int i=0; i<req.getIds().size(); i++) {
			FinalInboundInboundMaster fii = _FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(),req.getIds().get(i));
			checkInbound = fii.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 1)
					.collect(Collectors.toList());
			if(checkInbound.size()!=0) {
				fiiList.add(_FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(),req.getIds().get(i)));
			}
			
		}
		for (int j =0; j<fiiList.size(); j++) {
		ExcelFTARes result = _FinalInboundInboundMasterRepository.findById(fiiList.get(j).getId()).map(t -> {

			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			List<Inbound> total_inbound_list = new ArrayList<>();
			ExcelFTARes item = ExcelFTARes.builder().build();
			List<ExcelFTASubRes> list = new ArrayList<>();
			DecimalFormat decimalFormat2 = new DecimalFormat("#,###");

//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

			String departDt = t.getFinalInbound().getDepartDtStr();
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
					+ t.getInboundMaster().getComExport().getValue2());
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			StringBuilder data4 = new StringBuilder("");
			data4.append(departDt);
			data4.append(",");
			data4.append(t.getFinalInbound().getHangName() + " / " + t.getFinalInbound().getHangCha());
			data4.append(",");
//			data4.append(t.getFinalInbound().getDepartPort());
			data4.append((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			data4.append(",");
//			data4.append(t.getFinalInbound().getIncomePort());
			data4.append((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			item.setData04(data4.toString());
			item.setData05(item.getData01());
			if(t.getInboundMaster().getPackingType()!=null) {
				item.setPackingType(t.getInboundMaster().getPackingType());
			}else {
				item.setPackingType("CTN");
			}
			
//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);

			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long, String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForFTA(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");
			total_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).collect(Collectors.toList());
			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 1)
					.collect(Collectors.toList());
			

			List<ExcelFTASubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd );
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
//				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
//				subRes.setItemCount(decimalFormat2.format(origin_inbound_list.get(i).getItemCount()));
				subRes.setItemCount(getStringResult(origin_inbound_list.get(i).getItemCount()));	
				subRes.setItemCountD(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
//				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				subRes.setBoxCount(total_inbound_list.get(i).getBoxCount());
				if(origin_inbound_list.get(i).getAmountType()!=null) {
					subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				}else {
					subRes.setAmountType("PCS");
				}
				
				
				sublist.add(subRes);
//				index.add(1);
//				return subRes;

			}
//			ExcelFTASubRes to;to.getItemCount()
//			Double total = sublist.stream().mapToDouble(ExcelFTASubRes::getItemCount).sum();
//			item.setTotalCountEng("TOTAL (" + String.valueOf(total.intValue()) + ")");
//			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
//					.mapToDouble(ExcelFTASubRes::getBoxCount).sum();
			Double total = inbound_list.get(0).getBoxCountSum();
			if(total==1||total==0) {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"OF");
			}else {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"S"+" "+"OF");
			}
			
			
			
			
			item.setSubItem(sublist);
			return item;
		}).get();
		finalResult.add(result);	
		}
		return finalResult;
	}
	
	@Override
	public List<ExcelRCEPRes> listRcepData(FinalInboundInboundMasterReq req) throws Exception {
		List<ExcelRCEPRes> finalResult = new ArrayList<>();
		List<FinalInboundInboundMaster> fiiList =  new ArrayList<>();
		List<Inbound> checkInbound = new ArrayList<>();
		for(int i=0; i<req.getIds().size(); i++) {
			FinalInboundInboundMaster fii = _FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(),req.getIds().get(i));
			checkInbound = fii.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 4)
					.collect(Collectors.toList());
			if(checkInbound.size()!=0) {
				fiiList.add(_FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(),req.getIds().get(i)));
			}
			
		}
		for (int j =0; j<fiiList.size(); j++) {
		ExcelRCEPRes result = _FinalInboundInboundMasterRepository.findById(fiiList.get(j).getId()).map(t -> {

			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			ExcelRCEPRes item = ExcelRCEPRes.builder().build();
			List<ExcelRCEPSubRes> list = new ArrayList<>();
			DecimalFormat decimalFormat2 = new DecimalFormat("#,###");

//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

			String departDt = t.getFinalInbound().getDepartDtStr();

			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
					+ t.getInboundMaster().getComExport().getValue2());
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			StringBuilder data4 = new StringBuilder("");
			data4.append(departDt);
			data4.append(",");
			data4.append(t.getFinalInbound().getHangName() + " / " + t.getFinalInbound().getHangCha());
			data4.append(",");
			data4.append((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			data4.append(",");
			data4.append((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			item.setData04(data4.toString());
			item.setData05(item.getData01());
			if(t.getInboundMaster().getPackingType()!=null) {
				item.setPackingType(t.getInboundMaster().getPackingType());
			}else {
				item.setPackingType("CTN");
			}

//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);

			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long, String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForRCEP(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");

			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 4)
					.collect(Collectors.toList());

			List<ExcelRCEPSubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelRCEPSubRes subRes = ExcelRCEPSubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd);
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setItemCount(getStringResult(origin_inbound_list.get(i).getItemCount()));
				subRes.setItemCountD(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				if(origin_inbound_list.get(i).getAmountType()!=null) {
					subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				}else {
					subRes.setAmountType("PCS");
				}
				sublist.add(subRes);
//				index.add(1);
//				return subRes;

			}
//			ExcelFTASubRes to;to.getItemCount()
//			Double total = sublist.stream().mapToDouble(ExcelRCEPSubRes::getItemCount).sum();
//			item.setTotalCountEng("TOTAL (" + String.valueOf(total.intValue()) + ")");
//			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
//					.mapToDouble(ExcelRCEPSubRes::getBoxCount).sum();
			Double total = inbound_list.get(0).getBoxCountSum();
			if(total==1||total==0) {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"OF");
			}else {
				item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"S"+" "+"OF");
			}
			
			item.setSubItem(sublist);
			return item;
		}).get();
		finalResult.add(result);	
		}
		return finalResult;
	}
	
	@Override
	public List<ExcelYATAIRes> listYataiData(FinalInboundInboundMasterReq req) throws Exception {
		List<ExcelYATAIRes> finalResult = new ArrayList<>();
		List<FinalInboundInboundMaster> fiiList =  new ArrayList<>();
		List<Inbound> checkInbound = new ArrayList<>();
		for(int i=0; i<req.getIds().size(); i++) {
			FinalInboundInboundMaster fii = _FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(),req.getIds().get(i));
			checkInbound = fii.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 2)
					.collect(Collectors.toList());
			if(checkInbound.size()!=0) {
				fiiList.add(_FinalInboundInboundMasterRepository.findByFinalInboundIdAndInboundMasterId(req.getId(),req.getIds().get(i)));
			}
			
		}
		for (int j =0; j<fiiList.size(); j++) {
		ExcelYATAIRes result = _FinalInboundInboundMasterRepository.findById(fiiList.get(j).getId()).map(t -> {

			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			ExcelYATAIRes item = ExcelYATAIRes.builder().build();
			List<ExcelYATAISubRes> list = new ArrayList<>();
			DecimalFormat decimalFormat2 = new DecimalFormat("#,###");

//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

			String departDt = t.getFinalInbound().getDepartDtStr();

			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setData01(t.getInboundMaster().getComExport().getValue());
			item.setData02(t.getInboundMaster().getCompanyInfo().getCoNmEn() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			
//			item.setData03(_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2());
//			item.setData04(_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2());
			item.setData03((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			item.setData04((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));
			if(t.getInboundMaster().getPackingType()!=null) {
				item.setPackingType(t.getInboundMaster().getPackingType());
			}else {
				item.setPackingType("CTN");
			}
//			Inbound blItem;
//			A(1,"FTA",1),
//			B(2,"YATAI",2),
//			C(3," ",3);

			// 병합처리된 marking 데이터 처리를 위한
			inbound_list = _InboundService.getInboundByInboundMasterId(
					InboundReq.builder().inboundMasterId(t.getInboundMaster().getId()).build());
			inbound_list = _utilService.changeExcelFormatNew(inbound_list);
			Map<Long, String> markingInfo = new HashMap<>();
			markingInfo = _utilService.getMakingForYATAI(inbound_list);
//			System.out.println(markingInfo+"<<<<<<<<<<<markingInfo");

			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 2)
					.collect(Collectors.toList());

			List<ExcelYATAISubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelYATAISubRes subRes = ExcelYATAISubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setData08(item.getData08());
				subRes.setItemCount(getStringResult(origin_inbound_list.get(i).getItemCount()));
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd);
				subRes.setDepartDtStr(departDt);
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				if(origin_inbound_list.get(i).getAmountType()!=null) {
					subRes.setAmountType(origin_inbound_list.get(i).getAmountType());
				}else {
					subRes.setAmountType("PCS");
				}
				sublist.add(subRes);

			}

//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
//			item.setTotalBoxCountEng("TOTAL: (" + String.valueOf(total.intValue()) + ")" + " CTNS OF");
//			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
//					.mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
			Double total = inbound_list.get(0).getBoxCountSum();
			if(total==1||total==0) {
				item.setTotalBoxCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"OF");
			}else {
				item.setTotalBoxCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
						+ String.valueOf(total.intValue()) + ")" + " "+item.getPackingType()+"S"+" "+"OF");
			}

			item.setSubItem(sublist);

			return item;
		}).get();
		finalResult.add(result);	
		}
		return finalResult;
	}
	@Override
	public boolean listInpack(List<ExcelInpackRes> excelListInpackRes, HttpServletResponse response) throws Exception {

		String path = DocumentType.getList().stream().filter(t -> t.getId() == 4).findFirst().get().getName();
		
		try {
			
			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
			
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
//			for(int i=0; i<excelListInpackRes.size()*2; i++) {
//				XSSFSheet sheet = workbook.createSheet();
//			}
			
			workbook.setSheetName(0, excelListInpackRes.get(0).getFileNm());
//			XSSFSheet sheet = workbook.getSheetAt(3);
//			sheet = workbook.cloneSheet(0);
			for(int i=0; i<excelListInpackRes.size()-1; i++) {
			workbook.cloneSheet(0, excelListInpackRes.get(i+1).getFileNm());
			workbook.cloneSheet(1,excelListInpackRes.get(i+1).getFileNm()+"패킹");
			}
			
			//시트 수
			int sheetCn = workbook.getNumberOfSheets();
			
			

		      

			
			
			for(int cn=0; cn<sheetCn; cn++) {
				ExcelInpackRes excelInpackRes = new ExcelInpackRes();
				double standard = 0;
				if(cn==0||cn==1) {
					excelInpackRes=excelListInpackRes.get(0);
				}else {
					standard = cn/2;
					int result = (int) standard;
					excelInpackRes=excelListInpackRes.get(result);
				}
				if(cn%2==0) {
					Collections.reverse(excelInpackRes.getSubItem());
				}
				
				String pageNo = "";
				pageNo= String.valueOf(cn%2==0);
				if(pageNo.equals("true")) {
					pageNo="1";
				}else {
					pageNo="2";
				}
				
				XSSFSheet sheet = workbook.getSheetAt(cn);

				
				
				int startCount = 23;
				for (int i = 0; i < excelInpackRes.getSubItem().size(); i++) {

					shiftRowForInpack(startCount, sheet, "D", excelInpackRes.getSubItem().get(i), workbook, pageNo);
					shiftRowForInpack(startCount + 1, sheet, "BL", excelInpackRes.getSubItem().get(i), workbook, pageNo);

				}

				
				
			
				chageDataforInpack(sheet, excelInpackRes, workbook);
				
				
				
				if(cn%2==0) {
//					sheet.addMergedRegion(new CellRangeAddress(15,16,0,5));
					sheet.addMergedRegion(new CellRangeAddress(20,21,10,11));
					sheet.addMergedRegion(new CellRangeAddress(23,23,1,2));
					sheet.addMergedRegion(new CellRangeAddress(23,23,3,5));
				}
				 if(cn%2==1) {
					sheet.addMergedRegion(new CellRangeAddress(0,2,0,11));
				}
			
				

				if(cn%2==1) {
					
					for (int l = 0; l < excelInpackRes.getSubItem().size(); l++) {
						
						XSSFRow row = sheet.getRow(startCount+(2*l));
						int startNum = startCount+(2*l)+1;
						String a = "K";
						String b = String.valueOf(startNum);
						String c = "I";
						row.getCell(9).setCellFormula(a+b+"-"+c+b);
					}
				}
				if(cn%2==0) {
					
					for (int l = 0; l < excelInpackRes.getSubItem().size(); l++) {
						
						XSSFRow row = sheet.getRow(startCount+(2*l));
						int startNum = startCount+(2*l)+1;
						String a = "G";
						String b = String.valueOf(startNum);
						String c = "J";
						row.getCell(10).setCellFormula(a+b+"*"+c+b);
					}
				}
				//20220726
				if(excelInpackRes.getSubItem().size()<=15) {
					int startRow = 24+(excelInpackRes.getSubItem().size()*2);
					XSSFRow row = sheet.getRow(startRow-1);
					for(int j = 0; j<(excelInpackRes.getSubItem().size()*2)-2; j++) {
						for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
							if (sheet.getMergedRegion(i).getFirstRow() == startRow-1) {
								sheet.removeMergedRegion(i);
								sheet.removeMergedRegion(i);
								sheet.removeMergedRegion(i);
							}
							
						}
						sheet.shiftRows(startRow, sheet.getLastRowNum(), -1);
					}
					
				}else if(excelInpackRes.getSubItem().size()>15) {
					int startRow = 24+(excelInpackRes.getSubItem().size()*2);
					XSSFRow row = sheet.getRow(startRow-1);
					//데이터사이즈 16이상일시 28개 빈칸 생김
					for(int j = 0; j<28; j++) {
						for (int i = 0; i < sheet.getMergedRegions().size(); i++) {
							if (sheet.getMergedRegion(i).getFirstRow() == startRow-1) {
								sheet.removeMergedRegion(i);
								sheet.removeMergedRegion(i);
//								sheet.removeMergedRegion(i);
							}
							
						}
						sheet.shiftRows(startRow, sheet.getLastRowNum(), -1);
					}
					
				}
				
				int LastRow = 24+(excelInpackRes.getSubItem().size()*2);
				if(excelInpackRes.getSubItem().size()<=15) {
					for(int i=23; i<54; i++) {
						sheet.getRow(i).setHeightInPoints(new Float("16"));
					}
					sheet.getRow(54).setHeightInPoints(new Float("18"));
//					for(int i=55; i<65;i++) {
//						sheet.getRow(i).setHeightInPoints(new Float("14"));
//					}
					sheet.rowIterator().forEachRemaining(row ->{
						if(row.getRowNum() <= 66 && row.getRowNum() >= 55) {
							row.setHeightInPoints(new Float("14"));
						}
					});
				}
				else if(excelInpackRes.getSubItem().size()>15) {
					for(int i=23; i<LastRow; i++) {
						sheet.getRow(i).setHeightInPoints(new Float("16"));
					}
					sheet.getRow(LastRow).setHeightInPoints(new Float("18"));
					sheet.rowIterator().forEachRemaining(row ->{
						if(row.getRowNum() <= 300 && row.getRowNum() > LastRow) {
							row.setHeightInPoints(new Float("14"));
						}
					});
				}
				// 도장이미지 삽입
				InboundMaster inboundMaster = _inboundMasterRepository.findById(excelInpackRes.getInboundMasterId()).get();
				Common common = inboundMaster.getComExport();
				if(common.getFileUpload()==null) {
					
					sheet.rowIterator().forEachRemaining(row ->{
						
//						if(row.getRowNum() <=52  && row.getRowNum() >=23 ) {
//		    	    			  row.setHeight(new Short("320"));
//						}
//						if(row.getRowNum() == 54) {
//	    	    			  row.setHeight(new Short("360"));
//						}
//						if(row.getRowNum() <= 66 && row.getRowNum() >= 55) {
//	    	    			  row.setHeight(new Short("280"));
//						}
				    	   row.cellIterator().forEachRemaining(cell->{
				    		   
				    		   try {
				    			   String value = cell.getStringCellValue();
				    			   if(value.equals("${image}")) {
				    				   
				    			       cell.setCellValue("");
				    			       
				    			   }
				    			   if(cell.getStringCellValue().contains("${hang}")) {
				    	    			  row.setHeight(new Short("150"));
				    	    			  cell.setCellValue("");
				    	    			  
				    	    		  }
				    			  
							} catch (Exception e) {
								// TODO: handle exception
							}
				    		   
				    	   });
				       });
					
				}else {
					String imagePath = common.getFileUpload().getRoot()+File.separatorChar+common.getFileUpload().getPath3();
					Path filePath = Paths.get(imagePath);
					Resource resources = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
					int indx=0;

				    String fileTale=FilenameUtils.getExtension(common.getFileUpload().getPath3());    
					 
					 byte[] inputImage =  IOUtils.toByteArray(resources.getInputStream());
				     if(fileTale.toUpperCase().equals("png".toUpperCase())) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_PNG);
				     }else if(fileTale.toUpperCase().equals("bmp".toUpperCase())) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_BMP);
				     }else if(fileTale.toUpperCase().equals("jpg".toUpperCase())) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_JPEG);
				     }
					 
				       XSSFDrawing drawing = sheet.createDrawingPatriarch();
				       XSSFClientAnchor anchor = new XSSFClientAnchor();

				       
				       drawing.createPicture(anchor, indx);
				       Picture pict = drawing.createPicture(anchor, indx);
				       double scale = 0.38;
				       
				       sheet.rowIterator().forEachRemaining(row ->{
				    	   
				    	   
//							if(row.getRowNum() <=52  && row.getRowNum() >=23 ) {
//		    	    			  row.setHeight(new Short("320"));
//							}
//							if(row.getRowNum() == 54) {
//		    	    			  row.setHeight(new Short("360"));
//							}
//							if(row.getRowNum() <= 66 && row.getRowNum() >= 55) {
//		    	    			  row.setHeight(new Short("280"));
//							}
				    	   
				    	   row.cellIterator().forEachRemaining(cell->{
				    		   
				    		   try {
				    			   String value = cell.getStringCellValue();
				    			   
				    			   if(value.equals("${image}")) {
				    				   
				    			       
				    			       anchor.setCol1(cell.getColumnIndex());
				    			       anchor.setCol2(cell.getColumnIndex() + 5);
//				    			       anchor.setCol2(cell.getColumnIndex()+5);
				    			       anchor.setRow1(cell.getRowIndex());
				    	   			   anchor.setRow2(cell.getRowIndex()+8);
				    	   			   
				    			       
//				    			       pict.resize();
//				    			       pict.resize(8.16,8.16);
//				    			       pict.resize(0.38,0.38);
//				    	   			   pict.resize(1.04,1.03);
//				    	   			 pict.resize(1.03);
				    			       cell.setCellValue("");
				    			       
				    			   }
				    			   if(cell.getStringCellValue().contains("${hang}")) {
				    	    			  row.setHeight(new Short("150"));
				    	    			  cell.setCellValue("");
				    	    			  
				    	    		  }
							} catch (Exception e) {
								// TODO: handle exception
							}
				    		   
				    	   });
				       });
				       

				}
				//도장삽입
				if(cn!=0&&cn!=1) {
					
				
				sheet.createRow(sheet.getLastRowNum());
				sheet.createRow(sheet.getLastRowNum());
				sheet.createRow(sheet.getLastRowNum());
				}
				
			}
			
			//기존
			
			String fileName ="Inpack.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
//			System.err.println(e.getMessage());
		}
		return true;
	}
	@Override
	public boolean listFta(List<ExcelFTARes> excelListFTARes, HttpServletResponse response) throws Exception {

			
		String path = DocumentType.getList().stream().filter(t -> t.getId() == 1).findFirst().get().getName();

		try {


			Resource resource = resourceLoader.getResource(path);

			File file = new File(resource.getURI());
		
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			workbook.setSheetName(0, excelListFTARes.get(0).getFileNm());
			for(int i=0; i<excelListFTARes.size()-1; i++) {
				workbook.cloneSheet(0, excelListFTARes.get(i+1).getFileNm());
				}
			//시트 수
			int sheetCn = workbook.getNumberOfSheets();
			
			for(int cn=0; cn<sheetCn; cn++) {
			ExcelFTARes excelFTARes = new ExcelFTARes();
			excelFTARes = excelListFTARes.get(cn);
			XSSFSheet sheet = workbook.getSheetAt(cn);
			Collections.reverse(excelFTARes.getSubItem());
			// item add
			// 문서마다 시작하는 숫자가 고정
			int startCount = 22;
			for (int i = 0; i < excelFTARes.getSubItem().size(); i++) {

				shiftRow(startCount, sheet, "D", excelFTARes.getSubItem().get(i), workbook);
				shiftRow(startCount + 1, sheet, "BL", excelFTARes.getSubItem().get(i), workbook);

			}

			// data 치환
			chageData(sheet, excelFTARes, workbook);
			
			// 셀병합
			//행시작, 행종료, 열시작, 열종료 (자바배열과 같이 0부터 시작)
//			sheet.addMergedRegion(new CellRangeAddress(22,22,7,8));

			// item add
			}
			String fileName = "FTA.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return true;
	}
}