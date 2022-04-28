package com.keepgo.whatdo.controller.Inbound;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.google.gson.Gson;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.FileUploadReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.FileUploadRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewListRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.service.fileupload.FileUploadService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;

@RestController
public class InboundController {

	
	static final Logger log = LoggerFactory.getLogger(InboundController.class);
	
	@Autowired
	InboundService _InboundService;
	
	@Autowired
	FileUploadService _fileUploadService;
	
	@Autowired
	UtilService _utilService;

	@RequestMapping(value = "/test/inbound", method = { RequestMethod.POST })
	public List<InboundRes> shopper(HttpServletRequest httpServletRequest,InboundReq inboundReq){
	
//		return null;
		return _InboundService.getList(inboundReq);
	}
	
	@RequestMapping(value = "/test/getInboundMaster", method = {RequestMethod.POST })
	public List<?> getInboundMaster(HttpServletRequest httpServletRequest ){
		

		return  _InboundService.getInboundMaster();
	}
	
	
	
	@RequestMapping(value = "/test/getFile", method = {RequestMethod.POST })
	public List<?> getuser(HttpServletRequest httpServletRequest, @RequestBody FileUploadReq fileUploadReq ){
		

		return  _fileUploadService.getFileList(fileUploadReq);
	}
	
	@RequestMapping(value = "/test/uploadFile", method = { RequestMethod.POST })
	@ResponseBody
//	public FileUploadRes excelUpload(MultipartFile file, @RequestParam String param1,@RequestParam String param2 , HttpServletRequest req)
	public FileUploadRes excelUpload(MultipartFile file,@RequestParam String fileUploadReq, HttpServletRequest req)	throws Exception, NumberFormatException {
		
		Gson gson = new Gson();
		FileUploadReq frq = gson.fromJson(fileUploadReq, FileUploadReq.class);
		
		System.out.println("here!");
		System.out.println(file);
		
		FileUploadRes result = _fileUploadService.uploadFile(file,frq);
		return result;
		
	}
	
	@RequestMapping(value = "/test/deleteFile", method = {RequestMethod.POST } ,produces = "application/json; charset=utf8")

	public  FileUploadRes deleteFile(@RequestBody FileUploadReq fileUploadReq ) throws IOException, InterruptedException {
		
		FileUploadRes result = _fileUploadService.deleteFile(fileUploadReq);
		return result;

	}
	
	@RequestMapping(value = "/test/downloadFile", method = {RequestMethod.POST })
	public ResponseEntity<Object> downloadFile(@RequestBody FileUploadReq fileUploadReq,HttpServletResponse response,HttpServletRequest request,@RequestHeader("User-Agent") String agent) throws Exception {
//		return null;
		
		
		return _fileUploadService.downloadFile(fileUploadReq, response, request,agent);

	}
	
	@RequestMapping(value = "/test/addInboundMaster", method = {RequestMethod.POST })

	public  InboundMasterRes addInboundMaster(@RequestBody InboundMasterReq inboundMasterReq) throws IOException, InterruptedException {

	
		InboundMasterRes result = _InboundService.addInboundMaster(inboundMasterReq);
		return result;

	}
	
	@RequestMapping(value = "/test/addInboundMasterCompany", method = {RequestMethod.POST })

	public  InboundMasterRes addInboundMasterCompany(@RequestBody InboundMasterReq inboundMasterReq) throws IOException, InterruptedException {

	
		InboundMasterRes result = _InboundService.addInboundMasterCompany(inboundMasterReq);
		return result;

	}
	
	@RequestMapping(value = "/test/addInboundMasterExport", method = {RequestMethod.POST })

	public  InboundMasterRes addInboundMasterExport(@RequestBody InboundMasterReq inboundMasterReq) throws IOException, InterruptedException {

	
		InboundMasterRes result = _InboundService.addInboundMasterExport(inboundMasterReq);
		return result;

	}
	
	
	
	@RequestMapping(value = "/test/updateInboundMaster", method = {RequestMethod.POST })

	public  InboundMasterRes updateInboundMaster(@RequestBody InboundMasterReq inboundMasterReq) throws IOException, InterruptedException {

	
		InboundMasterRes result = _InboundService.updateInboundMaster(inboundMasterReq);
		return result;

	}
	
	@RequestMapping(value = "/test/inboundByInboundMasterId", method = {RequestMethod.POST })
	public List<InboundRes> inboundByInboundMasterId(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{

		List<InboundRes> list = _InboundService.getInboundByInboundMasterId(inboundReq);
		//출력모드
		List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return  result;
	}
	
	@RequestMapping(value = "/test/changeInbound", method = {RequestMethod.POST })
	public InboundViewRes changeInbound(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{

		List<InboundRes> list = _InboundService.getInboundByInboundMasterId(inboundReq);
		//출력모드
		List<InboundRes> result2 = _utilService.changeExcelFormatNew(list);
		InboundViewRes result = _InboundService.changeInbound(result2);
		return  result;
	}
	
	@RequestMapping(value = "/test/changeInboundList", method = {RequestMethod.POST })
	public InboundViewListRes changeInboundList(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{
		
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		Double itemCountSumFinal = new Double(0);
		Double boxCountSumFinal = new Double(0);
		Double cbmSumFinal = new Double(0);
		Double weightSumFinal = new Double(0);
		
		
		InboundViewListRes finalRes = new InboundViewListRes();
		
		List<Long> inboundMasterIdList = inboundReq.getInboundMasterIds();
		List<InboundViewRes> finalList = new ArrayList<>();
		
		for(int i=0; i<inboundMasterIdList.size(); i++) {
			List<InboundRes> list = _InboundService.getInboundByMasterId(inboundMasterIdList.get(i).longValue());
			//출력모드
			List<InboundRes> result2 = _utilService.changeExcelFormatNew(list);
			InboundViewRes result = _InboundService.changeInbound(result2);
			itemCountSumFinal=itemCountSumFinal+result.getItemCountSumD();
			boxCountSumFinal=boxCountSumFinal+result.getBoxCountSumD();
			cbmSumFinal=cbmSumFinal+result.getCbmSumD();
			weightSumFinal=weightSumFinal+result.getWeightSumD();
			finalList.add(result);
		}
		
		finalRes.setInbounds(finalList);
		finalRes.setItemCountSumFinal(decimalFormat2.format(itemCountSumFinal));
		finalRes.setBoxCountSumFinal(decimalFormat2.format(boxCountSumFinal));
		finalRes.setCbmSumFinal(decimalFormat.format(cbmSumFinal));
		finalRes.setWeightSumFinal(decimalFormat2.format(weightSumFinal));
		return  finalRes;
	}
	
	
	@RequestMapping(value = "/test/inboundExcelCommit", method = {RequestMethod.POST })

	public  InboundRes inboundExcelCommit(@RequestBody InboundReq inboundReq) throws IOException, InterruptedException {
		
		
		InboundRes result = _InboundService.excelCommitInboundData(inboundReq);
		
		return result;

	}
	@RequestMapping(value = "/test/inboundCommit", method = {RequestMethod.POST })
	public  InboundRes inboundCommit(@RequestBody InboundReq inboundReq) throws IOException, InterruptedException {
		
		
		InboundRes result = _InboundService.inboundCommit(inboundReq);
		
		return result;

	}
	
	@RequestMapping(value = "/test/deleteInbound", method = {RequestMethod.POST })

	public  InboundRes deleteInbound(@RequestBody InboundReq inboundReq) throws IOException, InterruptedException {
		
		InboundRes result = _InboundService.deleteInbound(inboundReq);
		return result;

	}
	
	
	@RequestMapping(value = "/test/inboundExcelRead", method = { RequestMethod.POST })
	@ResponseBody
	public List<?> excelRead(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
		System.out.println("here!");
		System.out.println(file);
		System.out.println(test);
		

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}


		 List <InboundRes> list = (List<InboundRes>) _InboundService.excelRead(file, null);

		//출력모드
			List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return result;
	}
	
	
	
}
