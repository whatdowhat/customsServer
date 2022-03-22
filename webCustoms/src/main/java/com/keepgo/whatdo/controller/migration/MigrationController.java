package com.keepgo.whatdo.controller.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.repository.CommonRepository;

@RestController
public class MigrationController {

	
	@Autowired
	CommonRepository _commonRepository;
	
	static final Logger log = LoggerFactory.getLogger(MigrationController.class);

	// 공통 쉬퍼 migration
	@RequestMapping(value = "/migration/shopper", method = { RequestMethod.POST })
	public List<Common> shopper(HttpServletRequest httpServletRequest, @RequestBody CommonReq commonReq,
			HttpServletResponse response) throws Exception {

		String path = "C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\migration\\거래처코드(씨앤에어)-정리중.xlsx";

		List<Common> list = new ArrayList<>();
		List<String> values =new ArrayList<String>();
		List<String> values2 =new ArrayList<String>();
		
		
		try {
			Path filePath = Paths.get(path);
			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

			File file = new File(path);
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);

			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

			// 첫번째 시트
			XSSFSheet sheet = workbook.getSheetAt(1);


			int startRowNum = 0;
			int rowIndex = 0;
			int columnIndex = 0;
			boolean complete = false;

			// 첫번째 행(0)은 컬럼 명이기 때문에 두번째 행(1) 부터 검색
			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);

				final int rowInx = i;
				Map<String, Object> map = new HashMap<String, Object>();

				
				Common.builder()
				.user(new Long(1))
				.createDt(new Date())
				.updateDt(new Date())
				.commonMaster(CommonMaster.builder().id(new Long(2)).build())
				.nm("쉬퍼")
				.isUsing(true)
				.value("")
				.value2("")
				.build();
				
				CellType NUMERIC = CellType.NUMERIC;
				CellType FORMULA = CellType.FORMULA;
				row.cellIterator().forEachRemaining(cell -> {
					switch (cell.getCellTypeEnum().name()) {
					case "STRING":
						String string1 = cell.getStringCellValue();
						System.out.println("row index:"+rowInx+ "CELL_TYPE_STRING:::: " + cell.getColumnIndex()  + " 열 = " + cell.getStringCellValue());

						
						//쉬퍼 이름.
						if(cell.getColumnIndex() == 4 || cell.getColumnIndex() == 6|| cell.getColumnIndex() == 8|| cell.getColumnIndex() == 10|| cell.getColumnIndex() == 12) {
							String value = cell.getStringCellValue();
							values.add(value);
						}
						//쉬퍼 값
						if(cell.getColumnIndex() == 5 || cell.getColumnIndex() == 7|| cell.getColumnIndex() == 9|| cell.getColumnIndex() == 11|| cell.getColumnIndex() == 13) {
							String value2 = cell.getStringCellValue();
							values2.add(value2);
						}
						
						break;
					case "NUMERIC":
						System.out.println("row index:"+rowInx+ "CELL_TYPE_NUMERIC: : " +  cell.getColumnIndex()  + " 열 = " + cell.getDateCellValue());
						break;
					case "FORMULA":
						System.out.println("row index:"+rowInx+ "CELL_TYPE_FORMULA:: : " +  cell.getColumnIndex()  + " 열 = " + cell.getCellFormula());
						String formula1 = cell.getCellFormula();
						cell.setCellFormula(formula1);
						break;

					default:
						break;
					}

				});

			}

			process01(values,values2);
			
			response.setContentType("ms-vnd/excel");
			response.setHeader("Content-Disposition", "attachment;filename=example.xlsx");

			// Excel File Output
//			workbookResult.write(response.getOutputStream());
//			workbookResult.close();
			workbook.write(response.getOutputStream());
			workbook.close();

			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return list;

	}
	
	public void process01 (List<String> values,List<String> values2) {
		
		
		System.out.println("######process01#######");
		System.out.println("######process01####### values.size()" + values.size());
		System.out.println("######process01####### values2.size()" + values2.size());
		
		List<Common> list= new ArrayList<Common>();
		
		for(int i=0; i<values.size();i++) {
			 list.add(getCommon(values.get(i),values2.get(i))); 
		}
		
	
		// List를 Set으로 변경
		Set<Common> set = new HashSet<Common>(list);
		// Set을 List로 변경
		List<Common> newList =new ArrayList<Common>(set);
		
		newList.stream().forEach(item->{
			_commonRepository.save(item);
		});
		System.out.println("######process01####### Complete");
	}
	
	
	public Common getCommon (String value,String value2) {
		
		
		
		return 	Common.builder()
				.user(new Long(1))
				.createDt(new Date())
				.updateDt(new Date())
				.commonMaster(CommonMaster.builder().id(new Long(2)).build())
				.nm("쉬퍼")
				.isUsing(true)
				.value(value)
				.value2(value2)
				.build();
	}
}
