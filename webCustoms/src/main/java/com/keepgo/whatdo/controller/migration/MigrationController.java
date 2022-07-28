package com.keepgo.whatdo.controller.migration;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.define.CorpType;
import com.keepgo.whatdo.define.DocumentType;
import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;

@RestController
public class MigrationController {

	
	@Autowired
	CommonMasterRepository _CommonMasterRepository;
	
	@Autowired
	CommonRepository _commonRepository;
	@Autowired
	CompanyInfoRepository _companyInfoRepository;
	
	
	@Autowired
	CompanyInfoManageRepository _companyInfoManageRepository;
	
	@Autowired
	CompanyInfoExportRepository _CompanyInfoExportRepository;
	
	@Autowired
	ResourceLoader resourceLoader;
	
	@Value("${fileupload.root}")
	String uploadRoot;
	
	@Autowired
	FileUploadRepository _fileUploadRepository;
	static final Logger log = LoggerFactory.getLogger(MigrationController.class);

	
	@RequestMapping(value = "/migration/common/shipperCleanAdd", method = { RequestMethod.POST,RequestMethod.GET })
	public String shipperCleanAdd(ExcelFTARes excelFTARes,HttpServletResponse response) throws Exception {

		//삭제
		_commonRepository.deleteByMasterId(new Long(2));
		AtomicInteger atomicInteger01 = new AtomicInteger(0);
		AtomicInteger atomicInteger02 = new AtomicInteger(0);
		//insert
		String path = DocumentType.J.name;
		try {

			Resource resource = resourceLoader.getResource(path);
			File file = new File(resource.getURI());
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			XSSFSheet sheet = workbook.getSheetAt(1);
			sheet.rowIterator().forEachRemaining(row->{
				if(row.getRowNum() == 0) return;
				Common common = Common.builder()
						.nm("쉬퍼")
						.commonMaster(_CommonMasterRepository.findById(new Long(2)).get())
						
						.createDt(new Date())
						.updateDt(new Date())
						.isUsing(true)
						.user(new Long(1))
						.build();
				try {
					common.setValue(row.getCell(2).getStringCellValue());	
				} catch (Exception e) {
					common.setValue(String.valueOf(row.getCell(2).getNumericCellValue()));
				}
				try {
					common.setValue2(row.getCell(3).getStringCellValue());
				} catch (Exception e) {
					common.setValue2(String.valueOf(row.getCell(3).getNumericCellValue()));
				}
				try {
					common.setValue3(row.getCell(4).getStringCellValue());
				} catch (Exception e) {
					common.setValue3(String.valueOf(row.getCell(4).getNumericCellValue()));
				}
				
				
				
				_commonRepository.save(common);
				atomicInteger01.getAndIncrement();
			});

			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
			return "error!";
//			System.err.println(e.getMessage());
		}
		
		
		//image mapping
		_fileUploadRepository.deleteByFileType(FileType.K.getId());
		URL url =  MigrationController.class.getClassLoader().getResource("static/common/image");
		File file = new File(url.getFile());
		File[] files = file.listFiles();
		for(int i=0; i<files.length;i++) {
//			System.out.println(files[i].getName().split("\\.")[0]);
			
			
			int size_ = 0;
			String dd = files[i].getName().split("\\.")[0];
//			String imge = dd.split("image")[1];
//			size_ = dd.split("\\").length;
//			System.out.println(dd);
//			System.out.println(imge);
//			System.out.println(imge.substring(1, imge.length()));
			
			Common common = _commonRepository.findByValue3(dd);
//			String path = common.getFileUpload().getRoot()+File.separatorChar+common.getFileUpload().getPath3();		
			
//			File deleteFile = new File(path);
//		    deleteFile.delete();
			
			
			
			StringBuilder strPath = new StringBuilder(uploadRoot);
			//파일생성 폴더 이름은 쉬퍼코드
			strPath.append(File.separatorChar+common.getValue3());
			//파일생성 폴더 이름은 value값으로 
			strPath.append(File.separatorChar+String.valueOf(FileType.K.getId()) ); //10
			//stringBuilder.append(File.separatorChar+fileUploadReq.getPath3());
			//c:/Customs/212351251/A/filename.xml
//			String path = root+"a"; //폴더 경로
			
			File Folder = new File(strPath.toString());
			
			
			// 해당 디렉토리가 없을경우 디렉토리를 생성
			if (!Folder.exists()) {
				try{
				    Folder.mkdirs(); //폴더 생성
//				    System.out.println("폴더가 생성되었습니다.");
			        } 
			        catch(Exception e){
				    e.getStackTrace();
				}        
		         }else {
//				System.out.println("이미 폴더가 생성되어 있습니다.");
			}
			String temRoot = strPath.toString();
			strPath.append(File.separatorChar+files[i].getName() ); //10
			Folder = new File(strPath.toString());
			FileUtils.copyFile(files[i], Folder);
			
			
			if(common.getFileUpload() != null) {
				FileUpload fileupload = _fileUploadRepository.findById(common.getFileUpload().getId()).get();
				fileupload.setCommon(common);
				//todo 사용자 세션 아이디로 수정해야됨.
				fileupload.setUser(User.builder().id(new Long(1)).build());
				
				fileupload.setFileType(Integer.valueOf(FileType.K.getId()));
				
				fileupload.setFileName1(files[i].getName());
				fileupload.setRoot(uploadRoot);
				fileupload.setCreateDt(new Date());
				fileupload.setUpdateDt(new Date());
				fileupload.setFileSize(Integer.valueOf((int) files[i].length()));
				fileupload.setPath1(common.getValue3());
				fileupload.setPath2(String.valueOf(FileType.K.getId()));
				fileupload.setPath3(files[i].getName());
				fileupload.setRoot(temRoot);
				fileupload.setIsUsing(true);
				
				
				_fileUploadRepository.save(fileupload);
				atomicInteger02.getAndIncrement();
			}else {
				FileUpload fileupload = new FileUpload();
				fileupload.setCommon(common);
				//todo 사용자 세션 아이디로 수정해야됨.
				fileupload.setUser(User.builder().id(new Long(1)).build());
				
				fileupload.setFileType(Integer.valueOf(FileType.K.getId()));
				
				fileupload.setFileName1(files[i].getName());
				fileupload.setRoot(uploadRoot);
				fileupload.setCreateDt(new Date());
				fileupload.setUpdateDt(new Date());
				fileupload.setFileSize(Integer.valueOf((int) files[i].length()));
				fileupload.setPath1(common.getValue3());
				fileupload.setPath2(String.valueOf(FileType.K.getId()));
				fileupload.setPath3(files[i].getName());
				fileupload.setRoot(temRoot);
				fileupload.setIsUsing(true);
				_fileUploadRepository.save(fileupload);
			}
			
			
			FileUpload uploadFile = _fileUploadRepository.findByCommonId(common.getId());
			common.setFileUpload(uploadFile);
			_commonRepository.save(common);
			atomicInteger02.getAndIncrement();
			
			
		}
		//mapping
		
		//전체 가져오기 by masterId
		List<Common> l = _commonRepository.findByCommonMaster(_CommonMasterRepository.findById(new Long(2)).get());
		
		
		String result = "쉬퍼 등록수 : "+atomicInteger01.get() + "/ 이미지 업로드 수 :"+atomicInteger02.get();
		
		return result;
	}
	
	@RequestMapping(value = "/migration/companyCleanInsert", method = { RequestMethod.POST,RequestMethod.GET })
	public String companyCleanInsert(ExcelFTARes excelFTARes,HttpServletResponse response) throws Exception {

		_companyInfoRepository.deleteByCorpType(CorpType.A.id);
		_companyInfoRepository.deleteByCorpType(CorpType.B.id);
		AtomicInteger atomicInteger01 = new AtomicInteger(0);
		AtomicInteger atomicInteger02 = new AtomicInteger(0);
		//insert
		String path = DocumentType.K.name;
		try {

			Resource resource = resourceLoader.getResource(path);
			File file = new File(resource.getURI());
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			XSSFSheet sheet0 = workbook.getSheetAt(0);
			XSSFSheet sheet1 = workbook.getSheetAt(1);
			XSSFSheet sheet2 = workbook.getSheetAt(2);
			XSSFSheet sheet3 = workbook.getSheetAt(3);
			XSSFSheet sheet4 = workbook.getSheetAt(4);
			XSSFSheet sheet5 = workbook.getSheetAt(5);
			
			process01(sheet0,atomicInteger01,atomicInteger02,CorpType.B);
			process01(sheet1,atomicInteger01,atomicInteger02,CorpType.A);
//			process01(sheet3,atomicInteger01,atomicInteger02,CorpType.B);
//			process01(sheet4,atomicInteger01,atomicInteger02,CorpType.A);
			process01_forward(sheet3,atomicInteger01,atomicInteger02,CorpType.B);
			process01_forward(sheet4,atomicInteger01,atomicInteger02,CorpType.A);
			_CompanyInfoExportRepository.deleteAll();
			process02(sheet2,atomicInteger01,atomicInteger02,CorpType.A);
			process02(sheet2,atomicInteger01,atomicInteger02,CorpType.B);
			process02(sheet5,atomicInteger01,atomicInteger02,CorpType.A);
			process02(sheet5,atomicInteger01,atomicInteger02,CorpType.B);
			
			

			workbook.close();

		} catch (Exception e) {
			e.printStackTrace();
			return "error!";
//			System.err.println(e.getMessage());
		}
		
		String result = "쉬퍼 등록수 : "+atomicInteger01.get() + "/ 이미지 업로드 수 :"+atomicInteger02.get();
		
		return result;
	}
	
	public void process01(XSSFSheet sheet,AtomicInteger atomicInteger01,AtomicInteger atomicInteger02,CorpType corpType) {
		
		sheet.rowIterator().forEachRemaining(row->{
			if(row.getRowNum() == 0) return;
			if(row.getRowNum() == 1) return;
			//없는 경우 RETURN 
			
			CompanyInfo companyInfo = CompanyInfo.builder().build();
			companyInfo.setIsUsing(true);
			companyInfo.setCreateDt(new Date());
			companyInfo.setUpdateDt(new Date());
			row.cellIterator().forEachRemaining(cell -> {
				if(cell.getColumnIndex()==1 && cell.getStringCellValue().equals("") ) return;	

				
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String string1 = cell.getStringCellValue();
//					System.out.println("row index:"+rowInx+ "CELL_TYPE_STRING:::: " + cell.getColumnIndex()  + " 열 = " + cell.getStringCellValue());

					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(string1);	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(string1);
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setConsignee(string1);	
					}
					if(cell.getColumnIndex()==4) {
						companyInfo.setCoNum(string1);	
					}
					if(cell.getColumnIndex()==5) {
						companyInfo.setCoAddress(string1);	
					}
					if(cell.getColumnIndex()==6) {
						companyInfo.setManager(string1);	
					}
					if(cell.getColumnIndex()==7) {
						companyInfo.setCoInvoice(string1);	
					}
					companyInfo.setCorpType(corpType.id);
//					
					break;
				case "NUMERIC":
					cell.setCellType( HSSFCell.CELL_TYPE_STRING );
					String numberToString = cell.getStringCellValue();
							
					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(numberToString );	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(numberToString );
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setConsignee(numberToString );	
					}
					if(cell.getColumnIndex()==4) {
						companyInfo.setCoNum(numberToString );	
					}
					if(cell.getColumnIndex()==5) {
						companyInfo.setCoAddress(numberToString );	
					}
					if(cell.getColumnIndex()==6) {
						companyInfo.setManager(numberToString);	
					}
					if(cell.getColumnIndex()==7) {
						companyInfo.setCoInvoice(numberToString );	
						companyInfo.setForwarding("");
					}
					companyInfo.setCorpType(corpType.id);
					break;
				case "FORMULA":
					System.out.println("CELL_TYPE_FORMULA:: : " +  cell.getColumnIndex()  + " 열 = " + cell.getCellFormula());
					String formula1 = cell.getCellFormula();
					cell.setCellFormula(formula1);
					break;

				default:
					break;
				}
				
				
			});
			
			_companyInfoRepository.save(companyInfo);
			
		});
	}
	
	public void process01_forward(XSSFSheet sheet,AtomicInteger atomicInteger01,AtomicInteger atomicInteger02,CorpType corpType) {
		
		sheet.rowIterator().forEachRemaining(row->{
			if(row.getRowNum() == 0) return;
			if(row.getRowNum() == 1) return;
			//없는 경우 RETURN 
			
			CompanyInfo companyInfo = CompanyInfo.builder().build();
			companyInfo.setIsUsing(true);
			companyInfo.setCreateDt(new Date());
			companyInfo.setUpdateDt(new Date());
			row.cellIterator().forEachRemaining(cell -> {
				if(cell.getColumnIndex()==1 && cell.getStringCellValue().equals("") ) return;	

				
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String string1 = cell.getStringCellValue();
//					System.out.println("row index:"+rowInx+ "CELL_TYPE_STRING:::: " + cell.getColumnIndex()  + " 열 = " + cell.getStringCellValue());

					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(string1);	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(string1);
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setConsignee(string1);	
					}
					if(cell.getColumnIndex()==4) {
						companyInfo.setCoNum(string1);	
					}
					if(cell.getColumnIndex()==5) {
						companyInfo.setCoAddress(string1);	
					}
					if(cell.getColumnIndex()==6) {
//						companyInfo.setManager(string1);
//						companyInfo.setForwarding(string1);
						companyInfo.setManager(string1);
					}
					if(cell.getColumnIndex()==7) {
						companyInfo.setCoInvoice("");	
						companyInfo.setForwarding(string1);
					}
					companyInfo.setCorpType(corpType.id);
//					
					break;
				case "NUMERIC":
					cell.setCellType( HSSFCell.CELL_TYPE_STRING );
					String numberToString = cell.getStringCellValue();
							
					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(numberToString );	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(numberToString );
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setConsignee(numberToString );	
					}
					if(cell.getColumnIndex()==4) {
						companyInfo.setCoNum(numberToString );	
					}
					if(cell.getColumnIndex()==5) {
						companyInfo.setCoAddress(numberToString );	
					}
					if(cell.getColumnIndex()==6) {
						companyInfo.setManager(numberToString);	
					}
					if(cell.getColumnIndex()==7) {
						companyInfo.setCoInvoice("" );	
						companyInfo.setForwarding(numberToString);
					}
					companyInfo.setCorpType(corpType.id);
					break;
				case "FORMULA":
					System.out.println("CELL_TYPE_FORMULA:: : " +  cell.getColumnIndex()  + " 열 = " + cell.getCellFormula());
					String formula1 = cell.getCellFormula();
					cell.setCellFormula(formula1);
					break;

				default:
					break;
				}
				
				
			});
			
			_companyInfoRepository.save(companyInfo);
			
		});
	}
	
	public void process02(XSSFSheet sheet,AtomicInteger atomicInteger01,AtomicInteger atomicInteger02,CorpType corpType) {
		
		sheet.rowIterator().forEachRemaining(row->{
			if(row.getRowNum() == 0) return;
			if(row.getRowNum() == 1) return;
			//없는 경우 RETURN 
			
			
			String coNum = "";
			try {
				coNum = row.getCell(3).getStringCellValue();	
			}catch (Exception e) {
				row.getCell(3).setCellType(Cell.CELL_TYPE_STRING);
				coNum = String.valueOf(row.getCell(3).getStringCellValue());		 
			}
			CompanyInfo company = null;
			Common common = null;
			
			company = _companyInfoRepository.findByCoNumCorpType(coNum,corpType.id);
			String shiperCode = "";
			if(row.getCell(4) != null) {
					
				try {
					shiperCode = row.getCell(4).getStringCellValue();	
				}catch (Exception e) {
					row.getCell(4).setCellType(Cell.CELL_TYPE_STRING);
					shiperCode = String.valueOf(row.getCell(4).getStringCellValue());
				}
				if(shiperCode != null && !shiperCode.equals("")) {
					common = _commonRepository.findByValue3(shiperCode);
					if(common != null & company != null) {
						CompanyInfoExport companyInfoExport = CompanyInfoExport.builder().build();
						companyInfoExport.setCommon(common);
						companyInfoExport.setCompanInfoy(company);
						companyInfoExport.setPreperOrder(1);
						_CompanyInfoExportRepository.save(companyInfoExport);
					}
				}
			}
			if(row.getCell(5) != null) {
					
				try {
					shiperCode = row.getCell(5).getStringCellValue();	
				}catch (Exception e) {
					row.getCell(5).setCellType(Cell.CELL_TYPE_STRING);
					shiperCode = String.valueOf(row.getCell(5).getStringCellValue());
				}
				if(shiperCode != null && !shiperCode.equals("")) {
					common = _commonRepository.findByValue3(shiperCode);
					if(common != null & company != null) {
						CompanyInfoExport companyInfoExport = CompanyInfoExport.builder().build();
						companyInfoExport.setCommon(common);
						companyInfoExport.setCompanInfoy(company);
						companyInfoExport.setPreperOrder(2);
						_CompanyInfoExportRepository.save(companyInfoExport);
					}
				}
			}
			if(row.getCell(6) != null) {
				
				try {
					shiperCode = row.getCell(6).getStringCellValue();		
				}catch (Exception e) {
					row.getCell(6).setCellType(Cell.CELL_TYPE_STRING);
					shiperCode = String.valueOf(row.getCell(6).getStringCellValue());
				}
				if(shiperCode != null && !shiperCode.equals("")) {
					common = _commonRepository.findByValue3(shiperCode);
					if(common != null & company != null) {
						CompanyInfoExport companyInfoExport = CompanyInfoExport.builder().build();
						companyInfoExport.setCommon(common);
						companyInfoExport.setCompanInfoy(company);
						companyInfoExport.setPreperOrder(3);
						_CompanyInfoExportRepository.save(companyInfoExport);
					}
				}
			}
			if(row.getCell(7) != null) {
				
				try {
					shiperCode = row.getCell(7).getStringCellValue();		
				}catch (Exception e) {
					row.getCell(7).setCellType(Cell.CELL_TYPE_STRING);
					shiperCode = String.valueOf(row.getCell(7).getStringCellValue());
				}
				if(shiperCode != null && !shiperCode.equals("")) {
					common = _commonRepository.findByValue3(shiperCode);
					if(common != null & company != null) {
						CompanyInfoExport companyInfoExport = CompanyInfoExport.builder().build();
						companyInfoExport.setCommon(common);
						companyInfoExport.setCompanInfoy(company);
						companyInfoExport.setPreperOrder(4);
						_CompanyInfoExportRepository.save(companyInfoExport);
					}
				}
			}
			if(row.getCell(8) != null) {
					
				try {
					shiperCode = row.getCell(8).getStringCellValue();
				}catch (Exception e) {
					row.getCell(8).setCellType(Cell.CELL_TYPE_STRING);
					shiperCode = String.valueOf(row.getCell(8).getStringCellValue());
				}
				if(shiperCode != null && !shiperCode.equals("")) {
					common = _commonRepository.findByValue3(shiperCode);
					if(common != null & company != null) {
						CompanyInfoExport companyInfoExport = CompanyInfoExport.builder().build();
						companyInfoExport.setCommon(common);
						companyInfoExport.setCompanInfoy(company);
						companyInfoExport.setPreperOrder(5);
						_CompanyInfoExportRepository.save(companyInfoExport);
					}
				}
			}
			if(row.getCell(9) != null) {
				
				try {
					shiperCode = row.getCell(9).getStringCellValue();
				}catch (Exception e) {
					row.getCell(9).setCellType(Cell.CELL_TYPE_STRING);
					shiperCode = String.valueOf(row.getCell(9).getStringCellValue());
				}
				if(shiperCode != null && !shiperCode.equals("")) {
					common = _commonRepository.findByValue3(shiperCode);
					if(common != null & company != null) {
						CompanyInfoExport companyInfoExport = CompanyInfoExport.builder().build();
						companyInfoExport.setCommon(common);
						companyInfoExport.setCompanInfoy(company);
						companyInfoExport.setPreperOrder(6);
						_CompanyInfoExportRepository.save(companyInfoExport);
					}
				}
			}
			
		});
		
	}
	
	
	@RequestMapping(value = "/migration/common/image", method = { RequestMethod.POST })
	public boolean commonImageUpload(ExcelFTARes excelFTARes,HttpServletResponse response) throws Exception {

		
		
		
		URL url =  MigrationController.class.getClassLoader().getResource("static/common/image");
		File file = new File(url.getFile());
		File[] files = file.listFiles();
		for(int i=0; i<files.length;i++) {
//			System.out.println(files[i].getName().split("\\.")[0]);
			
			
			Common common = _commonRepository.findByValue3(files[i].getName().split("\\.")[0]);
//			String path = common.getFileUpload().getRoot()+File.separatorChar+common.getFileUpload().getPath3();		
			
//			File deleteFile = new File(path);
//		    deleteFile.delete();
			
			
			
			StringBuilder strPath = new StringBuilder(uploadRoot);
			//파일생성 폴더 이름은 쉬퍼코드
			strPath.append(File.separatorChar+common.getValue3());
			//파일생성 폴더 이름은 value값으로 
			strPath.append(File.separatorChar+String.valueOf(FileType.K.getId()) ); //10
			//stringBuilder.append(File.separatorChar+fileUploadReq.getPath3());
			//c:/Customs/212351251/A/filename.xml
//			String path = root+"a"; //폴더 경로
			
			File Folder = new File(strPath.toString());
			
			
			// 해당 디렉토리가 없을경우 디렉토리를 생성
			if (!Folder.exists()) {
				try{
				    Folder.mkdirs(); //폴더 생성
//				    System.out.println("폴더가 생성되었습니다.");
			        } 
			        catch(Exception e){
				    e.getStackTrace();
				}        
		         }else {
//				System.out.println("이미 폴더가 생성되어 있습니다.");
			}
			String temRoot = strPath.toString();
			strPath.append(File.separatorChar+files[i].getName() ); //10
			Folder = new File(strPath.toString());
			FileUtils.copyFile(files[i], Folder);
			

			if(common.getFileUpload() != null) {
				FileUpload fileupload = _fileUploadRepository.findById(common.getFileUpload().getId()).get();
				fileupload.setCommon(common);
				//todo 사용자 세션 아이디로 수정해야됨.
				fileupload.setUser(User.builder().id(new Long(1)).build());
				
				fileupload.setFileType(Integer.valueOf(FileType.K.getId()));
				
				fileupload.setFileName1(files[i].getName());
				fileupload.setRoot(uploadRoot);
				fileupload.setCreateDt(new Date());
				fileupload.setUpdateDt(new Date());
				fileupload.setFileSize(Integer.valueOf((int) files[i].length()));
				fileupload.setPath1(common.getValue3());
				fileupload.setPath2(String.valueOf(FileType.K.getId()));
				fileupload.setPath3(files[i].getName());
				fileupload.setRoot(temRoot);
				fileupload.setIsUsing(true);
				
				
				_fileUploadRepository.save(fileupload);
			}else {
				FileUpload fileupload = new FileUpload();
				fileupload.setCommon(common);
				//todo 사용자 세션 아이디로 수정해야됨.
				fileupload.setUser(User.builder().id(new Long(1)).build());
				
				fileupload.setFileType(Integer.valueOf(FileType.K.getId()));
				
				fileupload.setFileName1(files[i].getName());
				fileupload.setRoot(uploadRoot);
				fileupload.setCreateDt(new Date());
				fileupload.setUpdateDt(new Date());
				fileupload.setFileSize(Integer.valueOf((int) files[i].length()));
				fileupload.setPath1(common.getValue3());
				fileupload.setPath2(String.valueOf(FileType.K.getId()));
				fileupload.setPath3(files[i].getName());
				fileupload.setRoot(temRoot);
				fileupload.setIsUsing(true);
				_fileUploadRepository.save(fileupload);
			}
			
			
			FileUpload uploadFile = _fileUploadRepository.findByCommonId(common.getId());
			common.setFileUpload(uploadFile);
			_commonRepository.save(common);
			
			
			
		}
		
		
		

		
		return true;
	}
	
	@RequestMapping(value = "/migration/common/shipper", method = { RequestMethod.POST })
	public boolean shipper(ExcelFTARes excelFTARes,HttpServletResponse response) throws Exception {

		String path = "classpath:static"+File.separatorChar+"common"+File.separatorChar+"common.xlsx";

		try {
			
			Resource resource = resourceLoader.getResource(path); 
			
//			File file = new File(path);
			File file = new File(resource.getURI());;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
//			workbook.setSheetName(0, excelFTARes.getFileNm());

			XSSFSheet sheet = workbook.getSheetAt(1);
			
			int lastRow = sheet.getLastRowNum();
			
			
			for(int i=1; i<lastRow+1;i++) {
				XSSFRow row = sheet.getRow(i);
				
				if(row.getCell(4) != null && row.getCell(4).getStringCellValue() != null ) {
					Common comm = Common.builder()
							.commonMaster(CommonMaster.builder().id(2l).build())
							.value(row.getCell(2).getStringCellValue())
							.value2(row.getCell(3).getStringCellValue())
							.value3(row.getCell(4).getStringCellValue())
							.createDt(new Date())
							.updateDt(new Date())
							.nm("쉬퍼")
							.preperOrder(1)
							.isUsing(true)
							.user(0l)
							.build();
					_commonRepository.save(comm);	
				}else {
					System.out.println("nono:::"+i);
				}
			}
			
		
			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return true;
//		return null;
	}
	
	
	// 공통 쉬퍼 migration by shipper
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
		
		Common c = _commonRepository.findByValue(value);
		
		if(c == null ) {
			new Exception("일치하지 않음.");
		}else {
			return c;	
		}
		
//		return 	Common.builder()
//				.user(new Long(1))
//				.createDt(new Date())
//				.updateDt(new Date())
//				.commonMaster(CommonMaster.builder().id(new Long(2)).build())
//				.nm("쉬퍼")
//				.isUsing(true)
//				.value(value)
//				.value2(value2)
//				.build();
		return c;
	}
	
	
	
	
	// company migration by consignee
	@RequestMapping(value = "/migration/company", method = { RequestMethod.POST })
	public List<CompanyInfo> company(HttpServletRequest httpServletRequest, @RequestBody CommonReq commonReq,
		HttpServletResponse response) throws Exception {

		List<CompanyInfo> list = new ArrayList<CompanyInfo>();
		
		String path = "classpath:static"+File.separatorChar+"common"+File.separatorChar+"company.xlsx";

			
		
		try {

			Resource resource = resourceLoader.getResource(path); 
			File file = new File(resource.getURI());;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			// 첫번째 시트
			XSSFSheet sheet = workbook.getSheetAt(0);
			// 세번째 시트
//			XSSFSheet sheet = workbook.getSheetAt(2);

			int startRowNum = 0;
			int rowIndex = 0;
			int columnIndex = 0;
			boolean complete = false;

			list = getListCompany(sheet);
			list.stream().forEach(item->{
				_companyInfoRepository.save(item);
			});
			

			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return list;

	}
	@RequestMapping(value = "/migration/company2", method = { RequestMethod.POST })
	public List<CompanyInfo> company2(HttpServletRequest httpServletRequest, @RequestBody CommonReq commonReq,
		HttpServletResponse response) throws Exception {

		List<CompanyInfo> list = new ArrayList<CompanyInfo>();
		
		String path = "classpath:static"+File.separatorChar+"common"+File.separatorChar+"company.xlsx";

			
		
		try {

			Resource resource = resourceLoader.getResource(path); 
			File file = new File(resource.getURI());;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

			XSSFSheet sheet = workbook.getSheetAt(2);

			int startRowNum = 0;
			int rowIndex = 0;
			int columnIndex = 0;
			boolean complete = false;

			list = getListCompany2(sheet);
			list.stream().forEach(item->{
				_companyInfoRepository.save(item);
			});
			

			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return list;

	}
	
	//get companyinfo ==> consignee
	public List<CompanyInfo> getListCompany(XSSFSheet sheet) {
		
		List<CompanyInfo> list = new ArrayList<CompanyInfo>();
		
//		for (int i = 2; i <= 2; i++) {
		for (int i = 2; i < 442; i++) {
//		for (int i = 2; i < sheet.getLastRowNum()+1; i++) {
//		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);

			final int rowInx = i;

			CellType NUMERIC = CellType.NUMERIC;
			CellType FORMULA = CellType.FORMULA;
			
			CompanyInfo companyInfo = new CompanyInfo();
			companyInfo.setIsUsing(true);
			companyInfo.setCreateDt(new Date());
			companyInfo.setUpdateDt(new Date());
			companyInfo.setUser(User.builder().id(new Long(1)).build());
			if(row == null) break;
			row.cellIterator().forEachRemaining(cell -> {
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String string1 = cell.getStringCellValue();
//					System.out.println("row index:"+rowInx+ "CELL_TYPE_STRING:::: " + cell.getColumnIndex()  + " 열 = " + cell.getStringCellValue());

					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(string1);	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(string1);
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setConsignee(string1);	
					}
					if(cell.getColumnIndex()==4) {
						companyInfo.setCoNum(string1);	
					}
					if(cell.getColumnIndex()==5) {
						companyInfo.setCoAddress(string1);	
					}
					if(cell.getColumnIndex()==6) {
						companyInfo.setManager(string1);	
					}
					if(cell.getColumnIndex()==7) {
						companyInfo.setCoInvoice(string1);	
					}
					
//					
					break;
				case "NUMERIC":
//					System.out.println("row index:"+rowInx+ "CELL_TYPE_NUMERIC: : " +  cell.getColumnIndex()  + " 열 = " + cell.getDateCellValue());
					cell.setCellType( HSSFCell.CELL_TYPE_STRING );
					String numberToString = cell.getStringCellValue();
							
					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(numberToString );	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(numberToString );
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setConsignee(numberToString );	
					}
					if(cell.getColumnIndex()==4) {
						companyInfo.setCoNum(numberToString );	
					}
					if(cell.getColumnIndex()==5) {
						companyInfo.setCoAddress(numberToString );	
					}
					if(cell.getColumnIndex()==6) {
						companyInfo.setManager(numberToString);	
					}
					if(cell.getColumnIndex()==7) {
						companyInfo.setCoInvoice(numberToString );	
					}
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
			
			System.out.println("######company####### info" + companyInfo.toString());
			list.add(companyInfo);
		}
		return list;
		
	}
	//get companyinfo ==> consignee
		public List<CompanyInfo> getListCompany2(XSSFSheet sheet) {
			
			List<CompanyInfo> list = new ArrayList<CompanyInfo>();
			
//			for (int i = 2; i <= 2; i++) {
			for (int i = 2; i < 156; i++) {
//			for (int i = 2; i < sheet.getLastRowNum()+1; i++) {
//			for (int i = 0; i < sheet.getLastRowNum(); i++) {
				XSSFRow row = sheet.getRow(i);

				final int rowInx = i;

				CellType NUMERIC = CellType.NUMERIC;
				CellType FORMULA = CellType.FORMULA;
				
				CompanyInfo companyInfo = new CompanyInfo();
				companyInfo.setIsUsing(true);
				companyInfo.setCreateDt(new Date());
				companyInfo.setUpdateDt(new Date());
				companyInfo.setUser(User.builder().id(new Long(1)).build());
				if(row == null) break;
				row.cellIterator().forEachRemaining(cell -> {
					switch (cell.getCellTypeEnum().name()) {
					case "STRING":
						String string1 = cell.getStringCellValue();
//						System.out.println("row index:"+rowInx+ "CELL_TYPE_STRING:::: " + cell.getColumnIndex()  + " 열 = " + cell.getStringCellValue());

						if(cell.getColumnIndex()==1) {
							//상호(한글)
							companyInfo.setCoNm(string1);	
						}
						if(cell.getColumnIndex()==2) {
							//상호(영문)
							companyInfo.setCoNmEn(string1);
						}
						if(cell.getColumnIndex()==3) {
							companyInfo.setConsignee(string1);	
						}
						if(cell.getColumnIndex()==4) {
							companyInfo.setCoNum(string1);	
						}
						if(cell.getColumnIndex()==5) {
							companyInfo.setCoAddress(string1);	
						}
						if(cell.getColumnIndex()==6) {
							companyInfo.setForwarding(string1);
//							companyInfo.setManager(string1);	
						}
//						if(cell.getColumnIndex()==7) {
//							companyInfo.setCoInvoice(string1);	
//						}
						
//						
						break;
					case "NUMERIC":
//						System.out.println("row index:"+rowInx+ "CELL_TYPE_NUMERIC: : " +  cell.getColumnIndex()  + " 열 = " + cell.getDateCellValue());
						cell.setCellType( HSSFCell.CELL_TYPE_STRING );
						String numberToString = cell.getStringCellValue();
								
						if(cell.getColumnIndex()==1) {
							//상호(한글)
							companyInfo.setCoNm(numberToString );	
						}
						if(cell.getColumnIndex()==2) {
							//상호(영문)
							companyInfo.setCoNmEn(numberToString );
						}
						if(cell.getColumnIndex()==3) {
							companyInfo.setConsignee(numberToString );	
						}
						if(cell.getColumnIndex()==4) {
							companyInfo.setCoNum(numberToString );	
						}
						if(cell.getColumnIndex()==5) {
							companyInfo.setCoAddress(numberToString );	
						}
						if(cell.getColumnIndex()==6) {
							companyInfo.setForwarding(numberToString);
//							companyInfo.setManager(string1);	
						}
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
				
				System.out.println("######company####### info" + companyInfo.toString());
				list.add(companyInfo);
			}
			return list;
			
		}
	
	// 공통 담당자 migration by consignee
	@RequestMapping(value = "/migration/common_managers", method = { RequestMethod.POST })
	public Set<Common> common_managers(HttpServletRequest httpServletRequest, @RequestBody CommonReq commonReq,
			HttpServletResponse response) throws Exception {

		String path = "C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\migration\\거래처코드(씨앤에어)-정리중.xlsx";
		List<CompanyInfo> list = new ArrayList<CompanyInfo>();
		Set<Common> sets = new HashSet<Common>();
		try {
			Path filePath = Paths.get(path);
			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

			File file = new File(path);
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);

			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

			// 첫번째 시트
			XSSFSheet sheet = workbook.getSheetAt(0);
			list = getListCompany(sheet);
			for(int i=0; i<list.size();i++) {
				if(list.get(i).getManager() != null && !(list.get(i).getManager().replaceAll(" ", "").equals(""))) {
					String manager = list.get(i).getManager();
					String[] managers = manager.split(",");
					for(int j=0; j<managers.length; j++) {
						String[] names = managers[j].split(" ");
//						SET 1 담당자 INSERT
							if(names.length == 1) {
								
								Common target = Common.builder()
										.commonMaster(_CommonMasterRepository.findById(new Long(1)).get()).value(names[0]).id(new Long(1)).value(names[0])
										.nm("담당자")
										.isUsing(true).createDt(new Date()).updateDt(new Date()).user(new Long(1)).build();
								sets.add(target);		
	
							}else {
								Common target = Common.builder().commonMaster(_CommonMasterRepository.findById(new Long(1)).get()).value(names[0]).id(new Long(1)).value(names[0]).value2(names[1])
										.nm("담당자")
										.isUsing(true).createDt(new Date()).updateDt(new Date()).user(new Long(1)).build();
							
								sets.add(target);
								}
//						SET 1 담당자 INSERT
						
						}
						
				}
			}
			
//			SET 1 담당자 INSERT
			sets.stream().forEach(item->{
				 if(_commonRepository.findMasterIdValue(item.getCommonMaster().getId(), item.getValue())>0) {
					 //중복넣지 않음.
				 }else {
					 _commonRepository.save(item);
				 }
				
			});
//			SET 1 담당자 INSERT
			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return sets;

	}
	
	
	// company 담당자 migration consignee
	@RequestMapping(value = "/migration/company_managers", method = { RequestMethod.POST })
	public Set<Common> company_managers(HttpServletRequest httpServletRequest, @RequestBody CommonReq commonReq,
			HttpServletResponse response) throws Exception {

		String path = "C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\migration\\거래처코드(씨앤에어)-정리중.xlsx";
		List<CompanyInfo> list = new ArrayList<CompanyInfo>();
		Set<Common> sets = new HashSet<Common>();
		try {
			Path filePath = Paths.get(path);
			Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기

			File file = new File(path);
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);

			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);

			// 첫번째 시트
			XSSFSheet sheet = workbook.getSheetAt(0);
			list = getListCompany(sheet);
			for(int i=0; i<list.size();i++) {
				if(list.get(i).getManager() != null && !(list.get(i).getManager().replaceAll(" ", "").equals(""))) {
					String manager = list.get(i).getManager();
					String[] managers = manager.split(",");
					for(int j=0; j<managers.length; j++) {
						String[] names = managers[j].split(" ");
						
						//SET 2 COMPANY & MANAGER MAPPING
							
							Common manager_target = _commonRepository.getComonMasterValue(new Long(1),names[0]);
							if(manager_target!=null) {
								CompanyInfo company_Target = _companyInfoRepository.findByCoNum(list.get(i).getCoNum());
								if(company_Target !=null) {
									_companyInfoManageRepository.save(CompanyInfoManage.builder().common(manager_target).companInfoy(company_Target).build());
								}
							}
						//SET 2 COMPANY & MANAGER MAPPING
							
						}
						
				}
			}
			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return sets;

	}

	
	// company & exports migration : shopper
	@RequestMapping(value = "/migration/company_exports", method = { RequestMethod.POST })
	public List<CompanyInfo> company_exports(HttpServletRequest httpServletRequest, @RequestBody CommonReq commonReq,
			HttpServletResponse response) throws Exception {
		String path = "classpath:static"+File.separatorChar+"common"+File.separatorChar+"company.xlsx";
//		String path = "C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\migration\\거래처코드(씨앤에어)-정리중.xlsx";
//		String path = "C:\\Users\\whatdo\\git\\customsServer\\webCustoms\\src\\main\\resources\\migration\\거래처코드(씨앤에어).xlsx";
		List<CompanyInfo> list = new ArrayList<CompanyInfo>();
		Set<Common> sets = new HashSet<Common>();
		try {
			
			Resource resource = resourceLoader.getResource(path); 
			File file = new File(resource.getURI());;
			InputStream targetStream = new FileInputStream(file);
			OPCPackage opcPackage = OPCPackage.open(targetStream);
			XSSFWorkbook workbook = new XSSFWorkbook(opcPackage);
			// 첫번째 시트
//			XSSFSheet sheet = workbook.getSheetAt(1);
			XSSFSheet sheet = workbook.getSheetAt(3);
			list = getListCompanyFromShipper(sheet);
			
		} catch (

		Exception e) {
			e.printStackTrace();
			System.err.println(e.getMessage());
		}
		return list;

	}
	
	// company migration by shipper
	public List<CompanyInfo> getListCompanyFromShipper(XSSFSheet sheet) {
		
		List<CompanyInfo> list = new ArrayList<CompanyInfo>();
		
		for (int i = 2; i < 156; i++) {
//		for (int i = 0; i < sheet.getLastRowNum(); i++) {
			XSSFRow row = sheet.getRow(i);

			final int rowInx = i;

			CellType NUMERIC = CellType.NUMERIC;
			CellType FORMULA = CellType.FORMULA;
			
			CompanyInfo companyInfo = new CompanyInfo();
			companyInfo.setIsUsing(true);
			companyInfo.setCreateDt(new Date());
			companyInfo.setUpdateDt(new Date());
			companyInfo.setUser(User.builder().id(new Long(1)).build());
			
			row.cellIterator().forEachRemaining(cell -> {
				
				switch (cell.getCellTypeEnum().name()) {
				case "STRING":
					String string1 = cell.getStringCellValue();
					System.out.println("row index:"+rowInx+ "CELL_TYPE_STRING:::: " + cell.getColumnIndex()  + " 열 = " + cell.getStringCellValue());

					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(string1);	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(string1);
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setCoNum(string1);	
					}
					if(cell.getColumnIndex()==4 || cell.getColumnIndex()==5 || cell.getColumnIndex()==6 || cell.getColumnIndex()==7|| cell.getColumnIndex() ==8) {
						Common manager_target = _commonRepository.findByValue3(string1);
						
						CompanyInfo company_Target = _companyInfoRepository.findByCoNum(companyInfo.getCoNum());
						_CompanyInfoExportRepository.save(CompanyInfoExport.builder().common(manager_target).companInfoy(company_Target).build());
						list.add(companyInfo);
					}
					
					break;
				case "NUMERIC":
					System.out.println("row index:"+rowInx+ "CELL_TYPE_NUMERIC: : " +  cell.getColumnIndex()  + " 열 = " + cell.getDateCellValue());
					cell.setCellType( HSSFCell.CELL_TYPE_STRING );
					String numberToString = cell.getStringCellValue();
							
					System.out.println("row index:"+rowInx+ "CELL_TYPE_STRING:::: " + cell.getColumnIndex()  + " 열 = " + cell.getStringCellValue());

					if(cell.getColumnIndex()==1) {
						//상호(한글)
						companyInfo.setCoNm(numberToString);	
					}
					if(cell.getColumnIndex()==2) {
						//상호(영문)
						companyInfo.setCoNmEn(numberToString);
					}
					if(cell.getColumnIndex()==3) {
						companyInfo.setCoNum(numberToString);	
					}
					if(cell.getColumnIndex()==4 || cell.getColumnIndex()==5 || cell.getColumnIndex()==6 || cell.getColumnIndex()==7|| cell.getColumnIndex() ==8) {
						Common manager_target = _commonRepository.findByValue3(numberToString);
						CompanyInfo company_Target = _companyInfoRepository.findByCoNum(companyInfo.getCoNum());
						_CompanyInfoExportRepository.save(CompanyInfoExport.builder().common(manager_target).companInfoy(company_Target).build());
						list.add(companyInfo);
					}
					
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
		return list;
		
	}
}
