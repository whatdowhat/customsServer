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

public class RowCopy2 {
	public static void main(String[] args) throws Exception{
		
		
		JSONParser parser = new JSONParser();

		try {
//			FileReader reader = new FileInputStream("C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\target01.xlsx");
//			
			FileReader reader = new FileReader("C:\\Users\\rhengh\\git\\customsServer\\webCustoms\\src\\main\\resources\\static\\inpack\\sample01.json");
			Object obj = parser.parse(reader);
			
			JSONArray arr = (JSONArray) obj;
			
			List<InboundRes> resource = new ArrayList<>();
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
		
    }

    private static void step01(InboundRes item) {
     
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
    	
    	
    	
    	
    	
    }
    
}
