package com.keepgo.whatdo.controller.Common;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.define.CoTypeRes;
import com.keepgo.whatdo.define.ColorType;
import com.keepgo.whatdo.define.ColorTypeRes;
import com.keepgo.whatdo.define.CurrencyType;
import com.keepgo.whatdo.define.CurrencyTypeRes;
import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.define.FileTypeRes;
import com.keepgo.whatdo.define.FreightType;
import com.keepgo.whatdo.define.FreightTypeRes;
import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.define.GubunTypeRes;
import com.keepgo.whatdo.define.PackingType;
import com.keepgo.whatdo.define.PackingTypeRes;
import com.keepgo.whatdo.define.AmountType;
import com.keepgo.whatdo.define.AmountTypeRes;
import com.keepgo.whatdo.define.BackgroundColorType;
import com.keepgo.whatdo.define.BackgroundColorTypeRes;
import com.keepgo.whatdo.define.UnbiType;
import com.keepgo.whatdo.define.UnbiTypeRes;
import com.keepgo.whatdo.define.UserType;
import com.keepgo.whatdo.define.UserTypeRes;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.request.CommonMasterReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CommonReqForExcelDownload;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.service.common.CommonService;
import com.keepgo.whatdo.util.CustomExcel;

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
	@RequestMapping(value = "/common/getCommonOne", method = {RequestMethod.POST })
	public CommonRes getOne(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		CommonRes result = _commonService.getCommonOne(commonReq.getId());
		return result;
	}
	@RequestMapping(value = "/common/commonByMaster", method = {RequestMethod.POST })
	public List<?> commonByMaster(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		List<?> list = _commonService.getCommonByMaster(commonReq);
		return  list;
	}
	
	@RequestMapping(value = "/common/getDepartPort", method = {RequestMethod.POST })
	public List<?> getDepartPort(HttpServletRequest httpServletRequest) throws Exception {

		List<?> list = _commonService.getDepartPort();
		return  list;
	}
	
	@RequestMapping(value = "/common/getIncomePort", method = {RequestMethod.POST })
	public List<?> getIncomePort(HttpServletRequest httpServletRequest) throws Exception {

		List<?> list = _commonService.getIncomePort();
		return  list;
	}
	
	@RequestMapping(value = "/common/commonByCompanyInfoExport", method = {RequestMethod.POST })
	public List<?> commonByCompanyInfoExport(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		List<?> list = _commonService.getCommonByCompanyInfoExport(commonReq);
		return  list;
	}
	
	@RequestMapping(value = "/common/commonByCompanyInfoManage", method = {RequestMethod.POST })
	public List<?> commonByCompanyInfoManage(HttpServletRequest httpServletRequest,@RequestBody CommonReq commonReq) throws Exception {

		List<?> list = _commonService.getCommonByCompanyInfoManage(commonReq);
		return  list;
	}
	
	@RequestMapping(value = "/common/addData", method = {RequestMethod.POST })

	public  CommonRes addData(@RequestBody CommonReq commonReq) throws IOException, InterruptedException {

	
		CommonRes result = _commonService.addCommonData(commonReq);
		return result;

	}
	
	@RequestMapping(value = "/common/updateData", method = {RequestMethod.POST })

	public  CommonRes updateData(@RequestBody CommonReq commonReq) throws IOException, InterruptedException {

	
		CommonRes result = _commonService.updateCommonData(commonReq);
		return result;

	}
	
	@RequestMapping(value = "/common/deleteData", method = {RequestMethod.POST })

	public  CommonRes deleteCommonData(@RequestBody CommonReq commonReq) throws IOException, InterruptedException {
		
		CommonRes result = _commonService.deleteCommonData(commonReq);
		return result;

	}
	
	@RequestMapping(value = "/common/excelDownload", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownload(@RequestBody CommonReq commonReq, ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {
		
		List<CommonReq> list = commonReq.getCommonReqData();
		//쉬퍼 엑셀다운로드
		if(list.get(0).getCommonMasterId()==2) {
			List<CommonReq> result = commonReq.getCommonReqData();
			String filename = "exceldownload_test";
			model.addAttribute("targetList", result);
			model.addAttribute("sheetName", "first");
			model.addAttribute("workBookName", filename);
			return new CustomExcel();
		//담당자 엑셀다운로드
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
	public CommonRes excelUpload(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
//		System.out.println("here!");
//		System.out.println(file);
//		System.out.println(test);
		

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}


		 List <Common> list = (List<Common>) _commonService.excelUpload(file, null);

		 //엑셀업로드 시 코드이름(CommonMasterName 섞여있을 때 가장 첫번째 데이터 기준으로 화면 새로고침)
		return CommonRes.builder().commonMasterId(list.get(0).getCommonMaster().getId()).build();
	}

	
	@RequestMapping(value = "/common/fileType", method = { RequestMethod.POST })
	@ResponseBody
	public List<FileTypeRes> fileType()
			throws Exception, NumberFormatException {

		return FileType.getList();
	}
	
	@RequestMapping(value = "/common/coType", method = { RequestMethod.POST })
	@ResponseBody
	public List<CoTypeRes> coType(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {

		return CoType.getList();
	}
	@RequestMapping(value = "/common/gubunType", method = { RequestMethod.POST })
	@ResponseBody
	public List<GubunTypeRes> gubunType()
			throws Exception, NumberFormatException {

		return GubunType.getList();
	}
	@RequestMapping(value = "/common/freightType", method = { RequestMethod.POST })
	@ResponseBody
	public List<FreightTypeRes> freightType()
			throws Exception, NumberFormatException {

		return FreightType.getList();
	}
	@RequestMapping(value = "/common/colorType", method = { RequestMethod.POST })
	@ResponseBody
	public List<ColorTypeRes> colorType()
			throws Exception, NumberFormatException {

		return ColorType.getList();
	}
	@RequestMapping(value = "/common/backgroundColorType", method = { RequestMethod.POST })
	@ResponseBody
	public List<BackgroundColorTypeRes> backgroundColorType()
			throws Exception, NumberFormatException {

		return BackgroundColorType.getList();
	}
	@RequestMapping(value = "/common/unbiType", method = { RequestMethod.POST })
	@ResponseBody
	public List<UnbiTypeRes> UnbiType()
			throws Exception, NumberFormatException {

		return UnbiType.getList();
	}
	
	@RequestMapping(value = "/common/currencyType", method = { RequestMethod.POST })
	@ResponseBody
	public List<CurrencyTypeRes> CurrencyType()
			throws Exception, NumberFormatException {

		return CurrencyType.getList();
	}
	
	@RequestMapping(value = "/common/packingType", method = { RequestMethod.POST })
	@ResponseBody
	public List<PackingTypeRes> PackingType()
			throws Exception, NumberFormatException {

		return PackingType.getList();
	}
	
	@RequestMapping(value = "/common/amountType", method = { RequestMethod.POST })
	@ResponseBody
	public List<AmountTypeRes> AmountType()
			throws Exception, NumberFormatException {

		return AmountType.getList();
	}
	
	@RequestMapping(value = "/common/userType", method = { RequestMethod.POST })
	@ResponseBody
	public List<UserTypeRes> UserType()
			throws Exception, NumberFormatException {

		return UserType.getList();
	}
}
