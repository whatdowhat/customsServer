package com.keepgo.whatdo.controller.local;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;
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
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.zeroturnaround.zip.ZipUtil;

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
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;

@RestController
public class LocalController {

	
	
	@Value("${fileupload.root}")
	String uploadRoot;
	@Value("${fileupload.zipName}")
	String zipName;
	
	
	@Value("${fileupload.zip}")
	String zipRoot;
	
	@Autowired
	FileUploadRepository _fileUploadRepository;
	static final Logger log = LoggerFactory.getLogger(LocalController.class);

	
	@RequestMapping(value = "/migration/server/image", method = { RequestMethod.POST,RequestMethod.GET })
	public ResponseEntity<Object> migrationServerImage(ExcelFTARes excelFTARes,HttpServletResponse response) throws Exception {

		ZipUtil.pack(new File(uploadRoot), new File(zipRoot+zipName+".zip"));
		
		String dFile = zipName+".zip"; //이름 받아오면 됨.
		  StringBuilder strPath = new StringBuilder(zipRoot);
		  String path = strPath.toString()+dFile;
		  
		  try {
				Path filePath = Paths.get(path);
				Resource resource = new InputStreamResource(Files.newInputStream(filePath)); // 파일 resource 얻기
				
				File file = new File(path);
				
				String fileNm = file.getName();

	            HttpHeaders header = new HttpHeaders();
	            
	            //한글 인코딩 -> 클라이언트에서 디코딩으로 가져와서써야함
	            fileNm = URLEncoder.encode(fileNm, java.nio.charset.StandardCharsets.UTF_8.toString()).replace("+", "%20")
	            		.replace("*", "%2A")
	            		.replace("%7e","~");
	            header.add("custom-header",fileNm); //client에서 file이름을 받기위해서 custom-header로 넣어줘야함.		
		        response.setContentType("application/download;charset=utf-8");
	        
				
				return new ResponseEntity<Object>(resource, header, HttpStatus.OK);
			} catch(Exception e) {
				return new ResponseEntity<Object>(null, HttpStatus.CONFLICT);
			}
		
	}
	
}
