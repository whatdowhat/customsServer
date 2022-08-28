package com.keepgo.whatdo.util;

import java.io.OutputStream;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.springframework.web.servlet.view.AbstractView;

import com.keepgo.whatdo.entity.ExcelOrderVO;
import com.keepgo.whatdo.entity.ReactVO;

public class CustomExcel extends AbstractView

{

	@Override
	protected void renderMergedOutputModel(Map<String, Object> model, HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		
			List<?> list = (List) model.get("targetList");
			String sheetName = (String) model.get("sheetName");
			String workBookName = (String) model.get("workBookName");
	        
	        // 겹치는 파일 이름 중복을 피하기 위해 시간을 이용해서 파일 이름에 추
	        Date date = new Date();
	        SimpleDateFormat dayformat = new SimpleDateFormat("yyyyMMdd");
	        dayformat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
	        SimpleDateFormat hourformat = new SimpleDateFormat("hhmmss");
	        hourformat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
	        String day = dayformat.format(date);
	        String hour = hourformat.format(date);
	        String fileName = "_" + day + "_" + hour + ".xlsx";         
	        
	        fileName = URLEncoder.encode(workBookName+fileName, java.nio.charset.StandardCharsets.UTF_8.toString());
	        response.setContentType("application/download;charset=utf-8");
	        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\";");
	        response.setHeader("custom-header",fileName);
	        response.setHeader("Content-Transfer-Encoding", "binary");
	        
	       OutputStream os = null;
	       SXSSFWorkbook workbook =  getWorkBook(list, sheetName);
	       
	       try {
	           os = response.getOutputStream();
	           // 파일생성
	           workbook.write(os);
	       }catch (Exception e) {
	           e.printStackTrace();
	       } finally {
	           if(workbook != null) {
	               try {
	                   workbook.close();
	               } catch (Exception e) {
	                   e.printStackTrace();
	               }
	           }
	           
	           if(os != null) {
	               try {
	                   os.close();
	               } catch (Exception e) {
	                   e.printStackTrace();
	               }
	           }
	       }
	    }
	
	public static SXSSFWorkbook  getWorkBook(List<?> list,String sheetName) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException, SecurityException{
		
		Field[] fields = null;
		if(list.size()>0){
			fields = list.get(0).getClass().getDeclaredFields();
		}else{
			fields = ReactVO.class.getClass().getDeclaredFields();
		}
		List<ExcelOrderVO> columnHeaderList = new ArrayList<>();
		for (Field field : fields) {
			if(field.getAnnotation(ExcelColumn.class) != null){
				field.setAccessible(true);
				ExcelOrderVO excelOrderVO = new ExcelOrderVO(field.getName() ,field.getAnnotation(ExcelColumn.class).headerName(), field.getAnnotation(ExcelColumn.class).order());
				columnHeaderList.add(excelOrderVO);
			}
		}
		Stream<ExcelOrderVO> s;
		s = columnHeaderList.stream().sorted(Comparator.comparing(ExcelOrderVO::getOrder));
		List<ExcelOrderVO> sortedColumList =  s.collect(Collectors.toList());
		for(int j=0; j<sortedColumList.size();j++){
//				System.out.print(sortedColumList.get(j).getFieldName() +"#");
		}
//		System.out.println("");
		for(int i=0; i<list.size();i++){
			
			for(int j=0; j<sortedColumList.size();j++){
				Field f = list.get(i).getClass().getDeclaredField(sortedColumList.get(j).getFieldName());
				f.setAccessible(true);
				
			} 
		}
			
		
		 SXSSFWorkbook workbook = new SXSSFWorkbook();
	     // 시트 생성
	     SXSSFSheet sheet = (SXSSFSheet) workbook.createSheet(sheetName);
	        
	     for(int i=0; i<sortedColumList.size();i++){
	    	 sheet.setColumnWidth(i,6000); //시트 열 너비 설정
	     }
	     // 헤더 행 생
	     Row headerRow = sheet.createRow(0);
	     // 해당 행의 첫번째 열 셀 생성
	     Cell headerCell = null;  

	     for(int i=0; i<sortedColumList.size();i++){
	    	 headerCell =headerRow.createCell(i);
	    	 headerCell.setCellValue(sortedColumList.get(i).getTitle());
	     }
	     
	     // 과일표 내용 행 및 셀 생성
	     Row bodyRow = null;
	     Cell bodyCell = null;
	     
	     for(int i=0; i<list.size();i++){
				
	    	 bodyRow = sheet.createRow(i+1);
				for(int j=0; j<sortedColumList.size();j++){
					Field f = list.get(i).getClass().getDeclaredField(sortedColumList.get(j).getFieldName());
					f.setAccessible(true);
					
					 bodyCell = bodyRow.createCell(j);
					 bodyCell.setCellValue(String.valueOf(f.get(list.get(i))));
				} 
	     }
	     
	     return workbook;
	}
}
