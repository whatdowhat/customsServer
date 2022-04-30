package com.keepgo.whatdo.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.PrintSetup;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.util.IOUtils;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.apache.xmlbeans.impl.common.IOUtil;

public class RowCopy {
	public static void main(String[] args) throws Exception{
//		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\rheng\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\inpack.xlsx"));
		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\inpack.xlsx"));
//		XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\co\\FTA.xlsx"));
		XSSFSheet sheet = workbook.getSheetAt(0);
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
		
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
		sheet.shiftRows(23, sheet.getLastRowNum()+4, 1);
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
        
//        sheet.getMergedRegions().stream()
//        .forEach(item->{
//        	if(item.getNumberOfCells() == 23) {
//        		
//        		sheet.removeMergedRegion(item.getNumberOfCells());
//        	}
////        	System.out.println("row infor :: "+item.getNumberOfCells());
//        	
//        });
//        sheet.getRow(23).forEach(item->{
//        	System.out.println(item.getStringCellValue());
//        	
//        });
//        for(int i=0; i<sheet.getMergedRegions().size();i++) {
//        	if(sheet.getMergedRegion(i).getFirstRow() == 54) {
//        		sheet.removeMergedRegion(i);
//        		sheet.removeMergedRegion(i);
//        		sheet.removeMergedRegion(i);
//        	}
//        }
	       for(int i=0; i<sheet.getMergedRegions().size();i++) {
	       	if(sheet.getMergedRegion(i).getFirstRow() == 23) {
	       		sheet.removeMergedRegion(i);
	       		sheet.removeMergedRegion(i);
	       		sheet.removeMergedRegion(i);
	       	}
	       }
	      sheet.shiftRows(23, sheet.getLastRowNum(), -1);
	       for(int i=0; i<sheet.getMergedRegions().size();i++) {
		       	if(sheet.getMergedRegion(i).getFirstRow() == 23) {
		       		sheet.removeMergedRegion(i);
		       		sheet.removeMergedRegion(i);
		       		sheet.removeMergedRegion(i);
		       	}
		       }
		      sheet.shiftRows(23, sheet.getLastRowNum(), -1);
		       for(int i=0; i<sheet.getMergedRegions().size();i++) {
			       	if(sheet.getMergedRegion(i).getFirstRow() == 23) {
			       		sheet.removeMergedRegion(i);
			       		sheet.removeMergedRegion(i);
			       		sheet.removeMergedRegion(i);
			       	}
			       }
			      sheet.shiftRows(23, sheet.getLastRowNum(), -1);
//
//       for(int i=0; i<sheet.getMergedRegions().size();i++) {
//       	if(sheet.getMergedRegion(i).getFirstRow() == 23) {
//    		sheet.removeMergedRegion(i);
//    		sheet.removeMergedRegion(i);
//    		sheet.removeMergedRegion(i);
//       	}
//       }
//       sheet.shiftRows(24, sheet.getLastRowNum(), -1);
      
       File f = Paths.get("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\a1.png").toFile();
       
       FileInputStream fileInputStream = new FileInputStream(f);
       System.out.println(fileInputStream.available());
       
       long lengthh = f.length();
       byte[] picData = new byte[ (int)f.length()];
     
   
       InputStream inputStream = RowCopy.class.getClassLoader().getResourceAsStream("static//inpack//CHAOCH.png");
       byte[] inputImage =  IOUtils.toByteArray(inputStream);
       int indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_PNG);
       XSSFDrawing drawing = sheet.createDrawingPatriarch();
       XSSFClientAnchor anchor = new XSSFClientAnchor();

       
//       drawing.createPicture(anchor, indx);
       Picture pict = drawing.createPicture(anchor, indx);
       double scale = 0.4;
       sheet.rowIterator().forEachRemaining(row ->{
    	   row.cellIterator().forEachRemaining(cell->{
    		   
    		   try {
    			   String value = cell.getStringCellValue();
    			   if(value.equals("${image}") && row.getRowNum() <70 ) {
    				   
    			       anchor.setCol1(cell.getColumnIndex());
//    			       anchor.setCol2(cell.getColumnIndex()+5);
    			       anchor.setRow1(cell.getRowIndex());
//    			       drawing.resi
    			       pict.resize();
    			       pict.resize(scale);
    			       cell.setCellValue("");
//    			       anchor.setRow2(cell.getRowIndex());
    				   
    			   }
			} catch (Exception e) {
				// TODO: handle exception
			}
    		   
    	   });
       });
       
       inputStream = RowCopy.class.getClassLoader().getResourceAsStream("static//inpack//YIWU SUNMILE IM.png");
       inputImage =  IOUtils.toByteArray(inputStream);
       indx =  workbook.addPicture(inputImage, XSSFWorkbook.PICTURE_TYPE_PNG);
       drawing = sheet.createDrawingPatriarch();
       XSSFClientAnchor anchor2 = new XSSFClientAnchor();
       
       Picture pict2 = drawing.createPicture(anchor2, indx);
//       drawing.createPicture(anchor2, indx);
       
       sheet.rowIterator().forEachRemaining(row ->{
    	   row.cellIterator().forEachRemaining(cell->{
    		   
    		   try {
    			   String value = cell.getStringCellValue();
    			   if(value.equals("${image}") && row.getRowNum() >70) {
    				   
    				   anchor2.setCol1(cell.getColumnIndex());
//    				   anchor2.setCol2(cell.getColumnIndex()+5);
    				   anchor2.setRow1(cell.getRowIndex());
//    				   anchor2.setRow2(cell.getRowIndex());
    				   pict2.resize();
    			       pict2.resize(scale);
    			       cell.setCellValue("");
    			   }
			} catch (Exception e) {
				// TODO: handle exception
			}
    		   
    	   });
       });
        
        FileOutputStream out = new FileOutputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\inpack_r.xlsx");
        
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
}
