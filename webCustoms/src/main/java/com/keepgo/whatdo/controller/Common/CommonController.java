package com.keepgo.whatdo.controller.Common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.PageVO;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CommonMasterReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CommonReqForExcelDownload;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.service.common.CommonService;
import com.keepgo.whatdo.util.CustomExcel;
import com.keepgo.whatdo.viewEntity.CommonViewResponse;

@RestController
public class CommonController {

	static final Logger log = LoggerFactory.getLogger(CommonController.class);
	
	@Autowired
	CommonService _commonService;
	
	@Autowired
	CommonMasterRepository _commonMasterRepository;
	
	@Autowired
	CommonRepository _commonRepository;
	
	@RequestMapping(value = "/common/commonMaster", method = {RequestMethod.POST })
	public List<?> commonMaster(HttpServletRequest httpServletRequest,@RequestBody CommonMasterReq commonMasterReq) throws Exception {

		List<?> list = _commonService.getMasterAll(commonMasterReq);
		return  list;
	}
	
	@RequestMapping(value = "/common/common", method = {RequestMethod.POST })
	public List<?> common(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		List<?> list = _commonService.getCommonAll(commonReq);
		return  list;
	}
	@RequestMapping(value = "/common/commonByMaster", method = {RequestMethod.POST })
	public List<?> commonByMaster(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		List<?> list = _commonService.getCommonByMaster(commonReq);
		return  list;
	}
	
	@RequestMapping(value = "/common/addData", method = {RequestMethod.POST })

	public  List<?> addData(@RequestBody CommonReq commonReq) throws IOException, InterruptedException {

	
		Common common = new Common();
		CommonMaster master = _commonMasterRepository.findById(commonReq.getCommonMasterId())
				.orElse(CommonMaster.builder().build());
		
		common.setCommonMaster(master);
		common.setValue(commonReq.getValue());
		common.setValue2(commonReq.getValue2());
		common.setCreateDt(new Date());
		common.setNm(commonReq.getName());
		common.setIsUsing(true);
		
		 _commonRepository.save(common);
		
		 CommonReq result = new CommonReq();
		 List<?> list = _commonService.getCommonAll(result);
		 

		return list;

	}
	
	@RequestMapping(value = "/common/deleteData", method = {RequestMethod.POST })

	public  List<?> deleteCommonData(@RequestBody CommonReq commonReq) throws IOException, InterruptedException {

		List<CommonReq> list = commonReq.getCommonReqData();

		for (int i = 0; i < list.size(); i++) {

			Common common = _commonRepository.findById(list.get(i).getId()).orElse(Common.builder().build());

			_commonRepository.delete(common);
		}

		CommonReq result = new CommonReq();
		 List<?> allList = _commonService.getCommonAll(result);
		 

		return allList;

	}
	
	@RequestMapping(value = "/common/excelDownload", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownload(@RequestBody CommonReq commonReq, ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {
		
		List<CommonReq> list = commonReq.getCommonReqData();
		if(list.get(0).getCommonMasterId()==2) {
			List<CommonReq> result = commonReq.getCommonReqData();
			String filename = "exceldownload_test";
			model.addAttribute("targetList", result);
			model.addAttribute("sheetName", "first");
			model.addAttribute("workBookName", filename);
			return new CustomExcel();
		}else {
			List<CommonReqForExcelDownload> result = new ArrayList<>();
			for (int i=0; i<list.size(); i++) {
				CommonReqForExcelDownload data = new CommonReqForExcelDownload();
				data.setId(list.get(i).getId());
				data.setName(list.get(i).getName());
				data.setValue(list.get(i).getValue());
				data.setValue2(list.get(i).getValue2());
				result.add(data);
			}
			log.error("<<ExcelDownloadController");
			log.error("{0}", list);
			log.error("<<ExcelDownloadController");
			String filename = "exceldownload_test";
			model.addAttribute("targetList", result);
			model.addAttribute("sheetName", "first");
			model.addAttribute("workBookName", filename);
			return new CustomExcel();
		}
	
	}
	
	@RequestMapping(value = "/common/excelUpload", method = { RequestMethod.POST })
	@ResponseBody
	public List<?> excelUpload(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
		System.out.println("here!");
		System.out.println(file);
		System.out.println(test);
		

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}

		Workbook workbook = null;

		if (extension.equals("xlsx")) {
			workbook = new XSSFWorkbook(file.getInputStream());
		} else if (extension.equals("xls")) {
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		Sheet worksheet = workbook.getSheetAt(0);
		System.out.println("worksheet.getPhysicalNumberOfRows() :: " + worksheet.getPhysicalNumberOfRows());
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

			Row row = worksheet.getRow(i);

			Common data = getCommon(row);
			if (data == null) {

			} else {
				addOrUpdateCommon(data);
			}
		}

	

		CommonReq result = new CommonReq();
		 List<?> allList = _commonService.getCommonAll(result);
		 

		return allList;
	}

	
	public Common getCommon(Row row) {

		boolean validationId = true;
		boolean validationEtC = true;

		Common common = new Common();
		Long id = new Long(0);

		try {
			id = new Double(row.getCell(0).getNumericCellValue()).longValue();
			common.setId(id);
		} catch (Exception e) {
			validationId = false;
		}

		try {
			common.setNm(row.getCell(1).getStringCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}
		try {
			common.setValue(row.getCell(2).getStringCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}
		try {
			common.setValue2(row.getCell(3).getStringCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}

		if ((validationId == false) && (validationEtC == true)) {
			// 생성 후보
			CommonMaster u = _commonMasterRepository.findByNm(common.getNm());
			if (u == null) {
				
				return null;
			} else {
				common.setCreateDt(new Date());
				return common;
			}
		} else {
			if (validationEtC == false) {
				// 아무것도 안함.
				return null;
			} else {

				common.setUpdateDt(new Date());
				return common;
			}
		}


	}

	public boolean addOrUpdateCommon(Common common) {

		return _commonRepository.save(common) != null ? true : false;
	}
	
}
