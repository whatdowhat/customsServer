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

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.keepgo.whatdo.define.DocumentType;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.ExcelContainerRes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTASubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAIRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAISubRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
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

			// item add
			String fileName = excelFTARes.getFileNm() + "_FTA.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
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
				System.err.println("convertData ::excelFTARes.getData04()::" + excelFTARes.getData04());
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
			result = data04_arr[2];
			cell.setCellValue(result);
		} else if (target.contains("${data04_4}")) {
			String result = "";
			try {

			} catch (Exception e) {
				System.err.println("convertData ::excelFTARes.getData04()::" + excelFTARes.getData04());
			}
			String[] data04_arr = excelFTARes.getData04().split(",");
			result = data04_arr[3];
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

	public void CopyRowBlank(XSSFRow target, XSSFRow origin, ExcelFTASubRes item, XSSFWorkbook workbook,
			int startIndex) {
		List<Integer> count = new ArrayList<Integer>();
		origin.cellIterator().forEachRemaining(cell -> {

			if (count.size() < 9) {

				target.createCell(cell.getColumnIndex());

				CellStyle newCellStyle = cell.getCellStyle();
				newCellStyle.cloneStyleFrom(cell.getCellStyle());
				target.getCell(cell.getColumnIndex()).setCellStyle(newCellStyle);

				// 첫번째인경우
				//
				if (item.getOrderNo() == 1) {
//					target.getCell(cell.getColumnIndex()).setCellValue(item.getCompanyInvoice());
					if (count.size() == 7) {
						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
						target.getCell(cell.getColumnIndex()).setCellValue(getDateStr(item.getDepartDtStr()));
//						target.getCell(cell.getColumnIndex()).setCellValue("TAAAA");
					}
				} else {
//					if(count.size() == 7) {
//						workbook.getSheetAt(0).addMergedRegion(new CellRangeAddress(startIndex, startIndex, 7, 8));
//						target.getCell(cell.getColumnIndex()).setCellValue("");
//					}
				}

			} else {
//				CellStyle newCellStyle = cell.getCellStyle();
//				newCellStyle.cloneStyleFrom(cell.getCellStyle());
//				cell.setCellValue("t");
			}
			count.add(1);
		});

	}

	public void CopyRowData(XSSFRow target, XSSFRow origin, ExcelFTASubRes item, XSSFWorkbook workbook,
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
//				CellStyle newCellStyle = cell.getCellStyle();
//				newCellStyle.cloneStyleFrom(cell.getCellStyle());
//				cell.setCellValue("t");
			}
			count.add(1);
		});

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
					newCell.setCellValue(item.getItemCount());
				} else {
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
			ExcelFTARes item = ExcelFTARes.builder().build();
			List<ExcelFTASubRes> list = new ArrayList<>();

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
			data4.append(t.getFinalInbound().getDepartPort());
			data4.append(",");
			data4.append(t.getFinalInbound().getIncomePort());
			item.setData04(data4.toString());
			item.setData05(item.getData01());

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

			origin_inbound_list = t.getInboundMaster().getInbounds().stream()
					.sorted(Comparator.comparing(Inbound::getOrderNo)).filter(where -> where.getCoId().intValue() == 1)
					.collect(Collectors.toList());

			List<ExcelFTASubRes> sublist = new ArrayList<>();
			for (int i = 0; i < origin_inbound_list.size(); i++) {
				ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

				String yyMMdd = getYYMMDD(departDt);
				subRes.setOrderNo(i + 1);
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd + "B");
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				sublist.add(subRes);
//				index.add(1);
//				return subRes;

			}
//			ExcelFTASubRes to;to.getItemCount()
//			Double total = sublist.stream().mapToDouble(ExcelFTASubRes::getItemCount).sum();
//			item.setTotalCountEng("TOTAL (" + String.valueOf(total.intValue()) + ")");
			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
					.mapToDouble(ExcelFTASubRes::getBoxCount).sum();
			item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
					+ String.valueOf(total.intValue()) + ")" + " CTNS OF");
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
			System.err.println(
					"getYYMMDD() /mentor/src/main/java/com/keepgo/whatdo/service/excel/impl/ExcelServiceImpl.java ");
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
			System.err.println(e.getMessage());
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
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
			}
			return result;
		} else if (target.contains("${data04_2}")) {
			String result = "";
			try {
				String[] data04_arr = excelRCEPRes.getData04().split(",");
				result = data04_arr[1];
			} catch (Exception e) {
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
			}
			return result;
		} else if (target.contains("${data04_3}")) {
			String result = "";
			try {
				String[] data04_arr = excelRCEPRes.getData04().split(",");
				result = data04_arr[2];
			} catch (Exception e) {
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
			}
			return result;
		} else if (target.contains("${data04_4}")) {
			String result = "";
			try {
				String[] data04_arr = excelRCEPRes.getData04().split(",");
				result = data04_arr[3];
			} catch (Exception e) {
				System.err.println("convertDataForRCEP ::excelRCEPRes.getData04()::" + excelRCEPRes.getData04());
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
				} else if (i == 5) {
					newCell.setCellValue(item.getItemCount());
				} else {
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
			data4.append(t.getFinalInbound().getDepartPort());
			data4.append(",");
			data4.append(t.getFinalInbound().getIncomePort());
			item.setData04(data4.toString());
			item.setData05(item.getData01());

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
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd + "B");
				subRes.setData10(item.getData10());
				subRes.setEngNm(origin_inbound_list.get(i).getEngNm());
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setHsCode(origin_inbound_list.get(i).getHsCode());
//				markingInfo.put(1l, "f");
				subRes.setMaking(markingInfo.get(origin_inbound_list.get(i).getId()));
				subRes.setDepartDtStr(departDt);
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());
				sublist.add(subRes);
//				index.add(1);
//				return subRes;

			}
//			ExcelFTASubRes to;to.getItemCount()
//			Double total = sublist.stream().mapToDouble(ExcelRCEPSubRes::getItemCount).sum();
//			item.setTotalCountEng("TOTAL (" + String.valueOf(total.intValue()) + ")");
			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
					.mapToDouble(ExcelRCEPSubRes::getBoxCount).sum();
			item.setTotalCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
					+ String.valueOf(total.intValue()) + ")" + " CTNS OF");
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
			System.err.println(e.getMessage());
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

				if (i == 8) {
					newCell.setCellValue(item.getItemCount());
				} else {
					newCell.setCellValue(oldCell.getNumericCellValue());
				}

				break;
			case Cell.CELL_TYPE_STRING:

				if (i == 9) {
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
				if (i == 9) {
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
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setCompanyInvoice(t.getInboundMaster().getCompanyInfo().getCoInvoice() + yyMMdd + "B");
				subRes.setDepartDtStr(departDt);
				subRes.setItemCount(origin_inbound_list.get(i).getItemCount());
				subRes.setBoxCount(origin_inbound_list.get(i).getBoxCount());

				sublist.add(subRes);

			}

//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
//			item.setTotalBoxCountEng("TOTAL: (" + String.valueOf(total.intValue()) + ")" + " CTNS OF");
			Double total = sublist.stream().filter(k -> k.getBoxCount() != null)
					.mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
			item.setTotalBoxCountEng(EnglishNumberToWords.convert(total.longValue()) + " " + "("
					+ String.valueOf(total.intValue()) + ")" + " CTNS OF");

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
					sheet.addMergedRegion(new CellRangeAddress(15,16,0,5));
					sheet.addMergedRegion(new CellRangeAddress(20,21,10,11));
					
				}else if(cn==1) {
					sheet.addMergedRegion(new CellRangeAddress(0,2,0,11));
				}
				

				// 도장이미지 삽입
				InboundMaster inboundMaster = _inboundMasterRepository.findById(excelInpackRes.getInboundMasterId()).get();
				Common common = inboundMaster.getComExport();
				if(common.getFileUpload()==null) {
					
				}else {
					String imagePath = common.getFileUpload().getRoot()+File.separatorChar+common.getFileUpload().getPath3();
					Path filePath = Paths.get(imagePath);
					Resource resources = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
					int indx=0;

				    String fileTale=FilenameUtils.getExtension(common.getFileUpload().getPath3());    
					 
					 byte[] inputImage =  IOUtils.toByteArray(resources.getInputStream());
				     if(fileTale.equals("png")) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_PNG);
				     }else if(fileTale.equals("bmp")) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_BMP);
				     }else if(fileTale.equals("jpg")) {
				    	 indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_JPEG);
				     }
					 
				       XSSFDrawing drawing = sheet.createDrawingPatriarch();
				       XSSFClientAnchor anchor = new XSSFClientAnchor();

				       
				       drawing.createPicture(anchor, indx);
				       Picture pict = drawing.createPicture(anchor, indx);
				       double scale = 0.4;
				       
				       sheet.rowIterator().forEachRemaining(row ->{
				    	   row.cellIterator().forEachRemaining(cell->{
				    		   
				    		   try {
				    			   String value = cell.getStringCellValue();
				    			   if(value.equals("${image}")) {
				    				   
				    			       
				    			       anchor.setCol1(cell.getColumnIndex());
//				    			       anchor.setCol2(cell.getColumnIndex()+5);
				    			       anchor.setRow1(cell.getRowIndex());
//				    			       drawing.resi
				    			       pict.resize();
				    			       pict.resize(scale);
				    			       cell.setCellValue("");
				    			       
				    			   }
				    			   if(cell.getStringCellValue().contains("${hang}")) {
				    	    			  row.setHeight(new Short("140"));
				    	    			  cell.setCellValue("");
				    	    			  
				    	    		  }
							} catch (Exception e) {
								// TODO: handle exception
							}
				    		   
				    	   });
				       });
				       

				}
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
			System.err.println(e.getMessage());
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
		for (int i = 0; i <dataSize-2; i++) {
	    	   
	    	   for(int j=0; j<sheet.getMergedRegions().size();j++) {
	    	       	if(sheet.getMergedRegion(j).getFirstRow() == 23+dataSize) {
	    	    		sheet.removeMergedRegion(j);
	    	    		sheet.removeMergedRegion(j);
	    	    		sheet.removeMergedRegion(j);
	    	    		
	    	       	}
	    	   }
	    	   
	    	   sheet.shiftRows(24+dataSize, sheet.getLastRowNum(), -1);
			}

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
		} else {
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
				if (page.equals("1")) {
					if (i == 6) {
						newCell.setCellValue((item.getItemCount() == null ? 0d : item.getItemCount()));
					} else if (i == 9) {
						newCell.setCellValue((item.getItemPrice() == null ? 0d : item.getItemPrice()));
					} else if (i == 10) {
						newCell.setCellValue((item.getTotalWeight() == null ? 0 : item.getTotalWeight()));
					} else {
						newCell.setCellValue(oldCell.getNumericCellValue());
					}
				} else if (page.equals("2")) {
					if (i == 6) {
						newCell.setCellValue((item.getItemCount() == null ? 0d : item.getItemCount()));
					} else if (i == 8) {
						newCell.setCellValue((item.getBoxCount() == null ? 0d : item.getBoxCount()));
					} else if (i == 9) {
						newCell.setCellValue( (item.getWeight() == null ? 0d : item.getWeight()));
					} else if (i == 10) {
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
					} else {
						newCell.setCellValue(oldCell.getStringCellValue());
					}
				} else if (page.equals("2")) {
					if (i == 1) {
						newCell.setCellValue(item.getEngNm());
					} else if (i == 3) {
						newCell.setCellValue(item.getJejil());
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

			List<InboundRes> inbound_list = new ArrayList<>();
			List<Inbound> origin_inbound_list = new ArrayList<>();
			ExcelInpackRes item = ExcelInpackRes.builder().build();
			List<ExcelInpackSubRes> list = new ArrayList<>();

//			ExcelFTASubRes subRes = ExcelFTASubRes.builder().build();

			String departDt = t.getFinalInbound().getDepartDtStr();
			
			item.setFileNm(t.getInboundMaster().getBlNo());
			item.setInboundMasterId(t.getInboundMaster().getId());
			item.setData01(t.getInboundMaster().getComExport().getValue() + "\n"
					+ t.getInboundMaster().getComExport().getValue2());
			item.setData03(t.getInboundMaster().getCompanyInfo().getCoInvoice());
			item.setData05(t.getInboundMaster().getCompanyInfo().getConsignee() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoNum() + "\n"
					+ t.getInboundMaster().getCompanyInfo().getCoAddress());
			item.setData06(departDt);
			item.setData07(t.getFinalInbound().getHangName() + "/" + t.getFinalInbound().getHangCha());
		
			item.setData08((t.getFinalInbound().getDepartPort()==null?"" :_commonRepository.findById(t.getFinalInbound().getDepartPort()).get().getValue2()));
			item.setData09((t.getFinalInbound().getIncomePort()==null?"" :_commonRepository.findById(t.getFinalInbound().getIncomePort()).get().getValue2()));

			item.setCoYn(false);
			if(t.getInboundMaster().getCompanyInfo().getCoInvoice()==null) {
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
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
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
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
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
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
				subRes.setItemPrice(origin_inbound_list.get(i).getReportPrice());
				subRes.setTotalPrice(new Double(String.format("%.2f",
						origin_inbound_list.get(i).getReportPrice() * origin_inbound_list.get(i).getItemCount())));
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

				sublist.add(subRes);

			}

//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getBoxCount).sum();
//			Double total = sublist.stream().mapToDouble(ExcelYATAISubRes::getItemCount).sum();
//			item.setTotalBoxCountEng("TOTAL: ("+String.valueOf(total.intValue())+")"+" CTNS OF"); 
			item.setSubItem(sublist);
			return item;
		}).get();

		return result;
	}

	@Override
	public List<ExcelContainerRes> containerData(FinalInboundReq req) throws Exception {
		SimpleDateFormat afterFormat = new SimpleDateFormat("yyyymmdd");
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
			
		
			ExcelContainerRes r = new ExcelContainerRes();
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
			r.setHcnnam(""+f.getInboundMaster().getCompanyInfo().getConsignee());
			r.setHcnadd(""+f.getInboundMaster().getCompanyInfo().getCoAddress());
			r.setHnfnam("'SAME AS CONSIGNEE");
			r.setHfwnam(""+f.getInboundMaster().getCompanyInfo().getCoNm());
			r.setHfwadd(""+f.getInboundMaster().getCompanyInfo().getCoNmEn());
			r.setHplcod((f.getFinalInbound().getDepartPort() == null ? "" : ""+ _commonRepository.findById(f.getFinalInbound().getDepartPort()).get().getValue()));
			r.setHplnam((f.getFinalInbound().getDepartPort() == null ? "" : ""+ _commonRepository.findById(f.getFinalInbound().getDepartPort()).get().getValue2()));
			r.setHpdcod((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue()));
			r.setHpdnam((f.getFinalInbound().getIncomePort() == null ? "" : ""+ _commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue2()));
			r.setHpecod((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue()));
			r.setHpenam((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue2()));
			r.setHfncod((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue()));
			r.setHfnnam((f.getFinalInbound().getIncomePort() == null ? "" : ""+_commonRepository.findById(f.getFinalInbound().getIncomePort()).get().getValue2()));
			r.setHpkqty(""+decimalFormat2.format(i.getBoxCountSumD()));
			r.setHpkunt("CNT");
			r.setHwegwt(""+decimalFormat2.format(i.getWeightSumD()));
			r.setHwecbm(""+decimalFormat.format(i.getCbmSumD()));
			if(f.getFinalInbound().getInboundMasters().size()==1) {
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
			r.setHblsay(""+"SAY"+" "+":"+EnglishNumberToWords.convert(i.getItemCountSumD().longValue())+" "+"("+String.valueOf(i.getItemCountSumD().intValue())+")"+" "+r.getHpkunt()+" "+"ONLY.");
			r.setHaccod(f.getInboundMaster().getCompanyInfo().getConsignee());
			r.setHacnam(f.getInboundMaster().getCompanyInfo().getConsignee());
			r.setMrkmrk(inboundList.get(0).getMarking());
			List <String> nameList = new ArrayList<>();
			
			
			for(int j=0; j<inboundList.size(); j++) {
				nameList.add(inboundList.get(j).getEngNm());
			}
			
			
			String hecdec = nameList.stream().distinct().collect(Collectors.joining("\n"));
			
			//		arrayList=Stream.of(nameList).distinct().collect(Collectors.toList());
			
			
			
			r.setHecdec(hecdec);
			r.setHblseq(String.valueOf(no));
			r.setHblatn("");
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
			workbook.setSheetName(0, "container");
			
			
			
				
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
				sheet.shiftRows(startCount+list.size()+1, sheet.getLastRowNum(), -1);
			
			
			
			
			String fileName =  "container.xlsx";
			response.setContentType("application/download;charset=utf-8");
			response.setHeader("custom-header", fileName);
			workbook.write(response.getOutputStream());
			workbook.close();

		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
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


	

}