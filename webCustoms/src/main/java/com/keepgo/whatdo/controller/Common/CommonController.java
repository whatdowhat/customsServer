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
import org.springframework.web.bind.annotation.RestController;
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
		}else {
			List<CommonReqForExcelDownload> result = new ArrayList<>();
			for (int i=0; i<list.size(); i++) {
				
			}
		}

		

		log.error("<<ExcelDownloadController");
		log.error("{0}", list);
		log.error("<<ExcelDownloadController");
		String filename = "exceldownload_test";
		model.addAttribute("targetList", list);
		model.addAttribute("sheetName", "first");
		model.addAttribute("workBookName", filename);
		return new CustomExcel();
	}
	
}
