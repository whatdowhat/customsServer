package com.keepgo.whatdo.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.entity.customs.Cp;
import com.keepgo.whatdo.entity.customs.CpHelper;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.repository.InboundMasterRepository;

public class RowCopy3 {
//	@Autowired
//	static
//	InboundMasterRepository _inboundMasterRepository;
	public static void main(String[] args) throws Exception {

		JSONParser parser = new JSONParser();
		List<InboundRes> resource = new ArrayList<>();
		try {
//			FileReader reader = new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\target01.xlsx");
//			
			FileReader reader = new FileReader(
					"C:\\Users\\rheng\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\sample01.json");
			Object obj = parser.parse(reader);

			JSONArray arr = (JSONArray) obj;

//			JSONArray arr = jsonObject.get("").to;
			for (int i = 0; i < arr.size(); i++) {
				arr.get(i);
				Gson gson = new Gson();
				InboundRes item = gson.fromJson(arr.get(i).toString(), InboundRes.class);
//				InboundRes item = (InboundRes)arr.get(i);
				resource.add(item);
			}
//			System.out.println(resource);

			for (int i = 0; i < resource.size(); i++) {
				System.out.println(resource.get(i).getBlNoSpan());
			}
			reader.close();

//			System.out.print(arr);
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			System.out.println();
		}
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(
				"C:\\Users\\rheng\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\bldownloadformat.xlsx"));

		XSSFSheet sheet = workbook.getSheetAt(0);
		XSSFSheet sheet2 = workbook.getSheetAt(1);
		step01(resource, sheet, workbook);
		copyRowOtherSheet(workbook, sheet, resource.size()+1,sheet2, 0);
		//workboot, 붙일 sheet , 붙일 row (index 0부터),  가져올 sheet2 ,가져올 row (index 0부터) 
//		copyRowOtherSheet(workbook, sheet, 3,sheet2, 1);

		FileOutputStream out = new FileOutputStream(
				"C:\\Users\\rheng\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\bldownloadformat_R.xlsx");
		workbook.removeSheetAt(1);
		workbook.write(out);
		out.close();
	}

	private static void step01(List<InboundRes> resource, XSSFSheet sheet, XSSFWorkbook workbook) {

		// 규칙1. merge는 xxxspan은 1보다 큰경우 적용된다.
		// companyNmSpan
		// markingSpan
		// korNmSpan
		// itemCountSpan
		// boxCountSpan
		// weightSpan
		// cbmSpan
		// reportPriceSpan
		// memo1Span
		// memo2Span
		// itemNoSpan
		// hsCodeSpan
		// workDateSpan
		// blNoSpan
		// masterCompanySpan
		// masterExportSpan
		// exportNmSpan
		// coCodeSpan
		// coIdSpan
		// totalPriceSpan
		// engNmSpan
		// workDateStrSpan
		// orderNoStrSpan
		// jejilSpan

		// item 채우기
		for (int i = 0; i < resource.size(); i++) {

			if ((i + 1) == resource.size()) {

			} else {

				copyRow(workbook, sheet, i + 1, i + 2);
			}
//    		InboundMaster im = new InboundMaster();
//    		
//			im = _inboundMasterRepository.findById(resource.get(i).getInboundMasterId()).get();

			XSSFRow row = sheet.getRow(i + 1);
			row.getCell(0).setCellValue(resource.get(i).getOrderNo());
			row.getCell(1).setCellValue(resource.get(i).getWorkDateStr());
			row.getCell(2).setCellValue(resource.get(i).getCompanyNm());
			row.getCell(3).setCellValue(resource.get(i).getMarking());
			row.getCell(4).setCellValue(resource.get(i).getKorNm());
			row.getCell(5).setCellValue(resource.get(i).getItemCount());
			if (resource.get(i).getBoxCount() == null) {
//    			row.getCell(6).setCellValue("");
			} else {
				row.getCell(6).setCellValue(resource.get(i).getBoxCount());
			}

			row.getCell(7).setCellValue(resource.get(i).getWeight());
			row.getCell(8).setCellValue(resource.get(i).getCbm());
			if (resource.get(i).getReportPrice() == null) {
//    			row.getCell(9).setCellValue("");
			} else {
				row.getCell(9).setCellValue(resource.get(i).getReportPrice());
			}
			row.getCell(10).setCellValue(resource.get(i).getMemo1());
			row.getCell(11).setCellValue(resource.get(i).getMemo2());
			row.getCell(12).setCellValue(resource.get(i).getItemNo());
			row.getCell(13).setCellValue(resource.get(i).getJejil());
			row.getCell(14).setCellValue(resource.get(i).getHsCode());
			row.getCell(15).setCellValue(resource.get(i).getCoCode());
//    		for(int j=0; j<CoType.getList().size();j++) {
//				if(CoType.getList().get(j).getId() ==resource.get(i).getCoId() ) {
//					row.getCell(15).setCellValue(CoType.getList().get(j).getName());							
//				}
//			}
			if (resource.get(i).getTotalPrice() == null) {
				row.getCell(16).setCellValue("");
			} else {
				row.getCell(16).setCellValue(resource.get(i).getTotalPrice());
			}

			row.getCell(17).setCellValue(resource.get(i).getEngNm());
//			row.getCell(18).setCellValue(im.getCompanyInfo().getCoNm());
			row.getCell(18).setCellValue(resource.get(i).getMasterCompany());
//			row.getCell(19).setCellValue(im.getBlNo());
			if (resource.get(i).getBlNo() == null || resource.get(i).getBlNo() == "") {
//				row.getCell(19).setCellValue("");	
			} else {
				row.getCell(19).setCellValue(resource.get(i).getBlNo());
//				row.getCell(19).setCellValue("");	
			}

		}
		// merge 규칙
		for (int i = 0; i < resource.size(); i++) {

			XSSFRow row = sheet.getRow(i + 1);
			if (resource.get(i).getOrderNoStrSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress(1, 4, 0, 1)); // 열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)
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
			if (resource.get(i).getCoIdSpan() > 1) {
				sheet.addMergedRegion(new CellRangeAddress((i + 1), (i + resource.get(i).getCoIdSpan()), 15, 15));
			}
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

	private static void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
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
	
	 private static void copyRowOtherSheet(XSSFWorkbook workbook, XSSFSheet worksheet,int destinationRowNum, XSSFSheet worksheet2,int sourceRowNum) {
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
			System.out.println(i);
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

}
