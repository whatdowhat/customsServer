package com.keepgo.whatdo.controller.FinalInbound;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.keepgo.whatdo.define.FreightType;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundViewRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewListRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.mapper.UserMapper;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.finalInbound.FinalInboundService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.inboundMst.InboundMstService;
import com.keepgo.whatdo.service.util.UtilService;
import com.keepgo.whatdo.util.CustomExcel;

@RestController
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class FinalInboundController {

	static final Logger log = LoggerFactory.getLogger(FinalInboundController.class);

	@Autowired
	CompanyInfoRepository _companyInfoRepository;

	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;
	
	@Autowired
	CompanyInfoManageRepository _companyInfoManageRepository;
	
	@Autowired
	UserMapper _userMapper;

	@Autowired
	UserRepository _userRepository;
	
	@Autowired
	InboundMasterRepository _inboundMasterRepository;

	@Autowired
	CompanyInfoService _companyInfoService;
	
	@Autowired
	FileUploadService _fileUploadService;
	
	@Autowired
	InboundMstService _inboundMstService;
	
	@Autowired
	FinalInboundService _finalInboundService;
	@Autowired
	InboundService _InboundService;
	@Autowired
	UtilService _utilService;
	
	@Autowired
	FinalInboundInboundMasterRepository _finalInboundInboundMasterRepository;
	
	
	@RequestMapping(value = "/test/finalInboundCreate", method = {RequestMethod.POST })
	public boolean finalInboundCreate(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.createFinalInbound(finalInboundReq);
		
	}
	
	@RequestMapping(value = "/test/finalInboundAdd", method = {RequestMethod.POST })
	public FinalInboundRes finalInboundAdd(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq)throws ParseException{
		
		return  _finalInboundService.addFinalInbound(finalInboundReq);
		
	}
	@RequestMapping(value = "/test/deleteFinalInbound", method = {RequestMethod.POST })

	public  boolean deleteInbound(@RequestBody FinalInboundReq finalInboundReq) throws IOException, InterruptedException {
		
		
		return  _finalInboundService.deleteFinalInbound(finalInboundReq);

	}
	
	@RequestMapping(value = "/test/finalInboundUpdate", method = {RequestMethod.POST })
	public boolean finalInboundUpdate(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.updateFinalInbound(finalInboundReq);
		
	}
	@RequestMapping(value = "/test/finalInbound", method = {RequestMethod.POST })
	public FinalInboundRes finalInbound(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.getOne(finalInboundReq.getId());
		
	}
	
	@RequestMapping(value = "/test/deleteFinalInboundMasterItems", method = {RequestMethod.POST })
	public boolean deleteFinalInboundMasterItems(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.deleteFinalInboundMasterItems(finalInboundReq);
		
	}
	
	@RequestMapping(value = "/test/addFinalInboundMasterItems", method = {RequestMethod.POST })

	public  boolean addFinalInboundMasterItems(@RequestBody FinalInboundReq finalInboundReq) throws IOException, InterruptedException {

	
		
		return _finalInboundService.addFinalInboundMasterItems(finalInboundReq);

	}
	
	@RequestMapping(value = "/test/addDataFinalInboundMasterItems", method = {RequestMethod.POST })

	public  boolean addDataFinalInboundMasterItems(@RequestBody FinalInboundReq finalInboundReq) throws IOException, InterruptedException {

	
		
		return _finalInboundService.addDataFinalInboundMasterItems(finalInboundReq);

	}
	
	@RequestMapping(value = "/test/getAllCondition", method = {RequestMethod.POST })
	public List<?> finalInboundAll(HttpServletRequest httpServletRequest,@RequestBody FinalInboundReq finalInboundReq){
		
		return  _finalInboundService.getAllCondition(finalInboundReq);

		
	}
	
	@RequestMapping(value = "/test/FinalInboundExcelRead", method = { RequestMethod.POST })
	@ResponseBody
	public boolean excelRead(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}


		 

		//출력모드
//			List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return _finalInboundService.excelRead(file, null, test);
	}
	
	@RequestMapping(value = "/test/changeFinalInboundList", method = {RequestMethod.POST })
	public FinalInboundViewRes changeFinalInboundList(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{
		
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		Double itemCountSumFinal = new Double(0);
		Double boxCountSumFinal = new Double(0);
		Double cbmSumFinal = new Double(0);
		Double weightSumFinal = new Double(0);
		
		
		FinalInboundViewRes finalRes = new FinalInboundViewRes();
		Long containerId = inboundReq.getContainerId();
		
		List<FinalInboundInboundMaster> l =  _finalInboundInboundMasterRepository.findByFinalInboundId(containerId);
		List<InboundViewRes> finalList = new ArrayList<>();
		int no = 1;
		for(int i=0; i<l.size();i++) {
			InboundMaster inboundMaster = l.get(i).getInboundMaster();
			List<InboundRes> list = _InboundService.getInboundByMasterId(inboundMaster.getId());
			List<InboundRes> result2 = _utilService.changeExcelFormatNew(list);
			InboundViewRes result = _InboundService.changeInbound(result2);
			
			itemCountSumFinal=itemCountSumFinal+ (result.getItemCountSumD() == null ? 0d : result.getItemCountSumD());
			boxCountSumFinal=boxCountSumFinal+ ( result.getBoxCountSumD() == null ? 0d : result.getBoxCountSumD());
			cbmSumFinal=cbmSumFinal+ (result.getCbmSumD() == null ? 0d : result.getCbmSumD());
			weightSumFinal=weightSumFinal+ (result.getWeightSumD() == null ? 0d : result.getWeightSumD());
			result.setFreight(FreightType.getList().stream().filter(type->type.getId() == inboundMaster.getFreight()).findFirst().get().getName());
			result.setBlNo(inboundMaster.getBlNo());
			if (inboundMaster.getCompanyInfo() != null) {
				result.setConsignee(inboundMaster.getCompanyInfo().getConsignee());
				result.setNotify(inboundMaster.getCompanyInfo().getConsignee());
			}else {
				result.setConsignee("");
				result.setNotify("");
			}
			if (inboundMaster.getComExport()!= null) {
				result.setShipper(inboundMaster.getComExport().getValue());
			}else {
				result.setShipper("");
			}
			result.setNo(no);
			result.setInboundMasterId(inboundMaster.getId());
			no=no+1;
			finalList.add(result);
		}

		
		finalRes.setInbounds(finalList);
		finalRes.setItemCountSumFinal(decimalFormat2.format(itemCountSumFinal));
		finalRes.setBoxCountSumFinal(decimalFormat2.format(boxCountSumFinal));
		finalRes.setCbmSumFinal(decimalFormat.format(cbmSumFinal));
		finalRes.setWeightSumFinal(decimalFormat2.format(weightSumFinal));
		return  finalRes;
	}

	@RequestMapping(value = "/test/excelDownloadContainer", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownloadContainer(@RequestBody CompanyInfoReq companyInfoReq, ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {
		
		
		
		
			List<CompanyInfoReq> result = companyInfoReq.getCompanyInfoReqData();
			String filename = "exceldownload_test";
			model.addAttribute("targetList", result);
			model.addAttribute("sheetName", "first");
			model.addAttribute("workBookName", filename);
			return new CustomExcel();
	
	}
}