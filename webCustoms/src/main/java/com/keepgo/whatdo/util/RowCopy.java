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
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import com.keepgo.whatdo.entity.customs.Cp;
import com.keepgo.whatdo.entity.customs.CpHelper;

public class RowCopy {
	public static void main(String[] args) throws Exception{
		
		
		JSONParser parser = new JSONParser();

		try {
//			FileReader reader = new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\target01.xlsx");
//			
			FileReader reader = new FileReader("C:\\Users\\rhengh01\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\sample01.json");
			Object obj = parser.parse(reader);
			JSONObject jsonObject = (JSONObject) obj;
			
			reader.close();
			
			System.out.print(jsonObject);
		}catch (Exception e) {
			// TODO: handle exception
		}
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\rhengh01\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\target01.xlsx"));
//		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\inpack.xlsx"));
//		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\co\\FTA.xlsx"));
		XSSFSheet sheet = workbook.getSheetAt(1);
//        HSSFSheet sheet = workbook.getSheet("Sheet1");
        
        
//        copyRow(workbook, sheet, 23, 23+2);
//        copyRow(workbook, sheet, 24, 24+2);
		
//		sheet.shiftRows(40, 133, 2);
//		sheet.shiftRows(40, 133, 2);
//		sheet.shiftRows(40, 133, 2);
//		sheet.shiftRows(40, 133, 2);
//		sheet.shiftRows(40, 133, 2);
//		sheet.shiftRows(40, 133, 2);
//		sheet.shiftRows(40, 133, 2);
//		sheet.shiftRows(40, 133, 2);
		
//		sheet.shiftRows(24, sheet.getLastRowNum(), 2);
//		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
//		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
//		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
//		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
//		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
//		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
//		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
//		workbook.setPrintArea(0, "$A$1:$M$10");
//		workbook.setPrintArea(0, "$A$10:$M$20");
		
//		sheet.getPrintSetup().setFitWidth((short)1);
//		sheet.getPrintSetup().setFitHeight((short)5);
//		workbook.setPrintArea(0, "$A$1:$M$10");
//		sheet.setAutobreaks(false);
//		workbook.setPrintArea(0, null);
//		
//		workbook.setPrintArea(0, "$A$72:$M$134");
//		sheet.getPrintSetup().setScale(new Short("10"));
//		workbook.setPrintArea(0, 0, 4, 0, 5);
//		workbook.setPrintArea(0, 20, 20, 0, 20);
//        copyRow(workbook, sheet, 0, 1);
//        copyRow(workbook, sheet, 0, 1);
//        copyRow(workbook, sheet, 0, 1);
        
		
		
		List<CpHelper> l = new ArrayList<CpHelper>();
		List<Integer> indexs = new ArrayList<Integer>();
		List<CellRangeAddress> merges = sheet.getMergedRegions();
		System.out.println(merges.size()+"::: merges");
		for(int i=0; i<merges.size();i++) {
				CpHelper p = CpHelper.builder()
						.format(merges.get(i).formatAsString())
			        	.rowNumber(merges.get(i).getFirstRow())
			        	.rowStart(merges.get(i).getFirstRow())
			        	.rowEnd(merges.get(i).getLastRow())
			        	.columnIndexStart(merges.get(i).getFirstColumn())
			        	.columnIndexEnd(merges.get(i).getLastColumn())
			        	
			        	.build();
				if(p.getRowNumber()>=6) {
					l.add(p);
					indexs.add(p.getRowNumber());
				}  	
			
		}
		l = l.stream().sorted(Comparator.comparing(CpHelper::getRowNumber)).collect(Collectors.toList());
		System.out.println(indexs);
		System.out.println(l.size()+" :: size");
        l.stream().peek(System.out::println).map(t->t).count();
        List<Cp> cpList = new ArrayList<Cp>();
		for (Iterator<Row> iterator = sheet.iterator(); iterator.hasNext();) {
//			System.out.println(iterator.next().getRowNum()+":: rownumber");
			Row r = iterator.next(); 
//			System.out.println(r.cellIterator());
			
			Cp c =  Cp.builder().build();
			
			if(r.getRowNum() >=6) {
				if(indexs.contains(r.getRowNum())) {
					c.setRowIndex(r.getRowNum());
					c.setColumn01(getDataFromCell(r,0));
					c.setColumn02(getDataFromCell(r,1));
					c.setColumn03(getDataFromCell(r,2));
					c.setColumn04(getDataFromCell(r,3));
					c.setColumn05(getDataFromCell(r,4));
					c.setColumn06(getDataFromCell(r,5));
					c.setColumn07(getDataFromCell(r,6));
					c.setColumn08(getDataFromCell(r,7));
					cpList.add(c);
				}else {
					c.setRowIndex(r.getRowNum());
					c.setColumn01(getDataFromCell(r,0));
					c.setColumn02(getDataFromCell(r,1));
					c.setColumn03(getDataFromCell(r,2));
					c.setColumn04(getDataFromCell(r,3));
					c.setColumn05(getDataFromCell(r,4));
					c.setColumn06(getDataFromCell(r,5));
					c.setColumn07(getDataFromCell(r,6));
					c.setColumn08(getDataFromCell(r,7));
					cpList.add(c);
				}
			}
			
			
			
		}
		
		cpList.forEach(System.out::println);
         
		for(int i=0; i<l.size();i++) {
			cpList = arrageCpList(l.get(i),cpList);
		}
		System.out.println("#############complete");
		cpList.forEach(System.out::println);
		workbook.cloneSheet(0);
		
//        FileOutputStream out = new FileOutputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\inpack_r.xlsx");
        FileOutputStream out = new FileOutputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\target01_R.xlsx");
//        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\rheng\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\inpack.xlsx"));
        
        workbook.write(out);
        out.close();
    }

    private static void copyRow(XSSFWorkbook workbook, XSSFSheet worksheet, int sourceRowNum, int destinationRowNum) {
        // Get the source / new row
    	XSSFRow newRow = worksheet.getRow(destinationRowNum);
    	XSSFRow sourceRow = worksheet.getRow(sourceRowNum);

        // If the row exist in destination, push down all rows by 1 else create a new row
//        if (newRow != null) {
//            worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
//        } else {
//            newRow = worksheet.createRow(destinationRowNum);
//        }
        worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
//        worksheet.shiftRows(destinationRowNum, worksheet.getLastRowNum(), 1);
        newRow = worksheet.createRow(destinationRowNum);
        // Loop through source columns to add to new row
        for (int i = 0; i < sourceRow.getLastCellNum(); i++) {
            // Grab a copy of the old/new cell
//        	XSSFCelle
        	
        	XSSFCell  oldCell = sourceRow.getCell(i);
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

        // If there are are any merged regions in the source row, copy to new row
        for (int i = 0; i < worksheet.getNumMergedRegions(); i++) {
            CellRangeAddress cellRangeAddress = worksheet.getMergedRegion(i);
            if (cellRangeAddress.getFirstRow() == sourceRow.getRowNum()) {
                CellRangeAddress newCellRangeAddress = new CellRangeAddress(newRow.getRowNum(),
                        (newRow.getRowNum() +
                                (cellRangeAddress.getLastRow() - cellRangeAddress.getFirstRow()
                                        )),
                        cellRangeAddress.getFirstColumn(),
                        cellRangeAddress.getLastColumn());
                worksheet.addMergedRegion(newCellRangeAddress);
            }
        }
        
//        worksheet.shiftRows(1, worksheet.getLastRowNum(), -1);
    }
    
    public static List<Cp>  arrageCpList(CpHelper cphelper,List<Cp> cpList) {
    	
//    	 cphelper.getRowNumber()
    	 
    	 for(int i=0; i<cpList.size();i++) {
    		 int mergeCount = cphelper.getRowEnd() - cphelper.getRowStart() +1;
    		 if(cphelper.getRowNumber() == cpList.get(i).getRowIndex()) {
    			 
    			 switch (cphelper.getColumnIndexStart()) {
				case 0:
					cpList.get(i).setColumn01Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0) cpList.get(i+j).setColumn01Span(0);
					}
					break;
				case 1:
					cpList.get(i).setColumn02Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0)  cpList.get(i+j).setColumn02Span(0);
					}
					break;
				case 2:
					cpList.get(i).setColumn03Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0) cpList.get(i+j).setColumn03Span(0);
					}
					break;
				case 3:
					cpList.get(i).setColumn04Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0) cpList.get(i+j).setColumn04Span(0);
					}
					break;
				case 4:
					cpList.get(i).setColumn05Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0) cpList.get(i+j).setColumn05Span(0);
					}
					break;
				case 5:
					cpList.get(i).setColumn06Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0) cpList.get(i+j).setColumn06Span(0);
					}
					break;
				case 6:
					cpList.get(i).setColumn07Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0) cpList.get(i+j).setColumn07Span(0);
					}
					break;
				case 7:
					cpList.get(i).setColumn08Span(mergeCount);
					for(int j=0; j<mergeCount; j++) {
						if(j!=0) cpList.get(i+j).setColumn08Span(0);
					}
					break;
					
				default:
					break;
				}
    			 
    		 }
    	 }
    	
    	return cpList;
    }
    
    public static String getDataFromCell(Row row,int index) {
    	
    	String result = "";
    	Cell cell = row.getCell(index);
		if(cell!= null) {
			switch (cell.getCellTypeEnum().name()) {

			case "STRING":
				String string1 = cell.getStringCellValue();
				result = string1;
				
				break;
			case "NUMERIC":
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				String numberToString = cell.getStringCellValue();
				result = numberToString;
				break;
			case "FORMULA":
				System.out.println("FORMULA!!!");
				break;
			default:
				break;
			}
		}
    	return result;
    }
}
