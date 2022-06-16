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

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.keepgo.whatdo.entity.customs.Cp;
import com.keepgo.whatdo.entity.customs.CpHelper;
import com.keepgo.whatdo.entity.customs.response.InboundRes;

public class RowCopy3 {
	public static void main(String[] args) throws Exception{
		
		JSONParser parser = new JSONParser();
		List<InboundRes> resource = new ArrayList<>();
		try {
//			FileReader reader = new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\target01.xlsx");
//			
			FileReader reader = new FileReader("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\sample01.json");
			Object obj = parser.parse(reader);
			
			JSONArray arr = (JSONArray) obj;
			
//			JSONArray arr = jsonObject.get("").to;
			for(int i=0; i<arr.size();i++) {
				arr.get(i);
				Gson gson = new Gson();
				InboundRes item = gson.fromJson(arr.get(i).toString(), InboundRes.class);
//				InboundRes item = (InboundRes)arr.get(i);
				resource.add(item);
			}
//			System.out.println(resource);
			
			for(int i=0; i<resource.size();i++) {
				System.out.println(resource.get(i).getBlNoSpan());
			}
			reader.close();
			
//			System.out.print(arr);
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
//			System.out.println();
		}
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\bldownloadformat.xlsx"));
		
		XSSFSheet sheet = workbook.getSheetAt(0);
		step01(resource,sheet,workbook);
		
		FileOutputStream out = new FileOutputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\bldownloadformat_R.xlsx");
      
      workbook.write(out);
      out.close();
    }

    private static void step01(List<InboundRes> resource,XSSFSheet sheet,XSSFWorkbook workbook ) {
     
    	//규칙1. merge는 xxxspan은 1보다 큰경우 적용된다.
    	//companyNmSpan
    	//markingSpan
    	//korNmSpan
    	//itemCountSpan
    	//boxCountSpan
    	//weightSpan
    	//cbmSpan
    	//reportPriceSpan
    	//memo1Span
    	//memo2Span
    	//itemNoSpan
    	//hsCodeSpan
    	//workDateSpan
    	//blNoSpan
    	//masterCompanySpan
    	//masterExportSpan
    	//exportNmSpan
		//coCodeSpan
		//coIdSpan
		//totalPriceSpan
		//engNmSpan
		//workDateStrSpan
		//orderNoStrSpan
		//jejilSpan
    	
    	//item 채우기 
    	for(int i=0; i<resource.size();i++) {
    		
    		
    		if((i+1) == resource.size()) {
    			
    		}else {
    			
    			copyRow(workbook,sheet,i+1,i+2);
    		}
    		XSSFRow row = sheet.getRow(i+1);
    		row.getCell(0).setCellValue(resource.get(i).getOrderNo());
    		row.getCell(1).setCellValue(resource.get(i).getWorkDateStr());
    		row.getCell(2).setCellValue(resource.get(i).getCompanyNm());
    		row.getCell(3).setCellValue(resource.get(i).getMarking());
    		row.getCell(4).setCellValue(resource.get(i).getKorNm());
    		row.getCell(5).setCellValue(resource.get(i).getItemCount());
    		
    	}
    	//merge 규칙     	
    	for(int i=0; i<resource.size();i++) {
    		
    		XSSFRow row = sheet.getRow(i+1);
    		if(resource.get(i).getOrderNoStrSpan()>1) {
    			sheet.addMergedRegion(new CellRangeAddress(1,4,0,1)); //열시작, 열종료, 행시작, 행종료 (자바배열과 같이 0부터 시작)
    			sheet.addMergedRegion(new CellRangeAddress((i+1), (i+resource.get(i).getWorkDateStrSpan()),0,0)); 
    		}
    		if(resource.get(i).getWorkDateStrSpan() >1) {
    			sheet.addMergedRegion(new CellRangeAddress((i+1), (i+resource.get(i).getWorkDateStrSpan()),1,1)); 
    		}
    		if(resource.get(i).getCompanyNmSpan() >1) {
    			sheet.addMergedRegion(new CellRangeAddress((i+1), (i+resource.get(i).getCompanyNmSpan()),2,2)); 
    		}
    		if(resource.get(i).getMarkingSpan() >1) {
    			sheet.addMergedRegion(new CellRangeAddress((i+1), (i+resource.get(i).getMarkingSpan()),3,3)); 
    		}
    		if(resource.get(i).getItemCountSpan() >1) {
    			sheet.addMergedRegion(new CellRangeAddress((i+1), (i+resource.get(i).getItemCountSpan()),4,4)); 
    		}
    	}
    	
    }
    
    private static void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
    	XSSFRow newRow = worksheet.getRow(destinationRowNum);
    	XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
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
    
}
