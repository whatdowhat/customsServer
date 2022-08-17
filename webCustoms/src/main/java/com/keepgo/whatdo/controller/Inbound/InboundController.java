package com.keepgo.whatdo.controller.Inbound;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Comparator;
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
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
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
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
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
	
	@Autowired
	InboundMasterRepository _inboundMasterRepository;
	@Autowired
	FinalInboundInboundMasterRepository _finalInboundInboundMasterRepository;
	@Autowired
	FinalInboundRepository _finalInboundRepository;

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
	public FileUploadRes excelUpload(MultipartFile file,@RequestParam String fileUploadReq, HttpServletRequest req)	throws Exception, NumberFormatException {
		
		Gson gson = new Gson();
		FileUploadReq frq = gson.fromJson(fileUploadReq, FileUploadReq.class);
		
//		System.out.println("here!");
//		System.out.println(file);
		
		FileUploadRes result = _fileUploadService.uploadFile(file,frq);
		return result;
		
	}
	
	@RequestMapping(value = "/front/deleteFile", method = {RequestMethod.POST } ,produces = "application/json; charset=utf8")

	public  FileUploadRes deleteFile(@RequestBody FileUploadReq fileUploadReq ) throws IOException, InterruptedException {
		
		FileUploadRes result = _fileUploadService.deleteFile(fileUploadReq);
		return result;

	}
	
	@RequestMapping(value = "/front/downloadFile", method = {RequestMethod.POST })
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
	
	@RequestMapping(value = "/front/inboundByInboundMasterId", method = {RequestMethod.POST })
	public List<InboundRes> inboundByInboundMasterId(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{

		List<InboundRes> list = _InboundService.getInboundByInboundMasterId(inboundReq);
		//출력모드
		List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return  result;
	}
	
	@RequestMapping(value = "/front/changeInbound", method = {RequestMethod.POST })
	public InboundViewRes changeInbound(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{

		List<InboundRes> list = _InboundService.getInboundByInboundMasterId(inboundReq);
		//출력모드
		List<InboundRes> result2 = _utilService.changeExcelFormatNew(list);
		InboundViewRes result = _InboundService.changeInbound(result2);
		return  result;
	}
	
	@RequestMapping(value = "/front/changeInboundList", method = {RequestMethod.POST })
	public InboundViewListRes changeInboundList(HttpServletRequest httpServletRequest,@RequestBody InboundReq inboundReq) throws Exception{
		
		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		Double itemCountSumFinal = new Double(0);
		Double boxCountSumFinal = new Double(0);
		Double cbmSumFinal = new Double(0);
		Double weightSumFinal = new Double(0);
		List<FinalInboundInboundMaster> fimList = new ArrayList<>();
//		List<Long> inboundMasterIdList = inboundReq.getInboundMasterIds();
		List<Long> inboundMasterIdList =  new ArrayList<>();
		List<InboundViewRes> finalList = new ArrayList<>();
		fimList = _finalInboundInboundMasterRepository.findByFinalInboundId(inboundReq.getFinalInboundId());
		fimList.sort(Comparator.comparing(FinalInboundInboundMaster::getId));
		for(int j=0; j<fimList.size();j++) {
			inboundMasterIdList.add(fimList.get(j).getInboundMaster().getId());
		}
		InboundViewListRes finalRes = new InboundViewListRes();
		
		
		
		for(int i=0; i<inboundMasterIdList.size(); i++) {
			InboundMaster inboundMaster = _inboundMasterRepository.findById(inboundMasterIdList.get(i).longValue()).get();
			List<InboundRes> list = _InboundService.getInboundByMasterId(inboundMasterIdList.get(i).longValue());
			//출력모드
			List<InboundRes> result2 = _utilService.changeExcelFormatNew(list);
			InboundViewRes result = _InboundService.changeInbound(result2);
			itemCountSumFinal=itemCountSumFinal+ (result.getItemCountSumD() == null ? 0d : result.getItemCountSumD());
			boxCountSumFinal=boxCountSumFinal+ ( result.getBoxCountSumD() == null ? 0d : result.getBoxCountSumD());
			cbmSumFinal=cbmSumFinal+ (result.getCbmSumD() == null ? 0d : result.getCbmSumD());
			weightSumFinal=weightSumFinal+ (result.getWeightSumD() == null ? 0d : result.getWeightSumD());
			result.setCurrencyType(inboundMaster.getCurrencyType());
			result.setPackingType(inboundMaster.getPackingType());
			finalList.add(result);
		}
		
		finalRes.setInbounds(finalList);
		finalRes.setItemCountSumFinal(getStringResult(itemCountSumFinal));
		finalRes.setBoxCountSumFinal(getStringResult(boxCountSumFinal));
		finalRes.setCbmSumFinal(decimalFormat.format(cbmSumFinal));
		finalRes.setWeightSumFinal(getStringResult(weightSumFinal));
		if(inboundMasterIdList.size()==0) {
			finalRes.setPackingType("CTN");
		}else {
			finalRes.setPackingType(finalList.get(0).getPackingType());
		}
		return  finalRes;
	}
	
	
	@RequestMapping(value = "/front/inboundExcelCommit", method = {RequestMethod.POST })

	public  InboundRes inboundExcelCommit(@RequestBody InboundReq inboundReq) throws IOException, InterruptedException {
		
		
		InboundRes result = _InboundService.excelCommitInboundData(inboundReq);
		
		return result;

	}
	@RequestMapping(value = "/front/inboundCommit", method = {RequestMethod.POST })
	public  InboundRes inboundCommit(@RequestBody InboundReq inboundReq) throws IOException, InterruptedException {
		
		
		InboundRes result = _InboundService.inboundCommit(inboundReq);
		
		return result;

	}
	
	@RequestMapping(value = "/front/deleteInbound", method = {RequestMethod.POST })

	public  InboundRes deleteInbound(@RequestBody InboundReq inboundReq) throws IOException, InterruptedException {
		
		InboundRes result = _InboundService.deleteInbound(inboundReq);
		return result;

	}
	
	
	@RequestMapping(value = "/front/inboundExcelRead", method = { RequestMethod.POST })
	@ResponseBody
	public List<?> excelRead(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
//		System.out.println("here!");
//		System.out.println(file);
//		System.out.println(test);
		

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}


		 List <InboundRes> list = (List<InboundRes>) _InboundService.excelRead(file, null);

		//출력모드
			List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return result;
	}
	
	public String getStringResult(Double param) {
		try {
			boolean finalCheck = false;
			String[] arr = String.valueOf(param).split("\\.");
			char[] texts = arr[1].toCharArray();
			Boolean checkPoint1[] = new Boolean[texts.length];
			
			for(int i = 0; i<texts.length;i++) {
//				System.out.println(texts[i]);
				if(texts[i] == '0' ) {
					checkPoint1[i] = true;
				}else {
					checkPoint1[i] = false;
				}
			}
			
			for(int i = 0; i<checkPoint1.length;i++) {
				if(i == 0 ) {
					finalCheck = checkPoint1[i];
					
				}else {
					finalCheck = (finalCheck && checkPoint1[i]);
				}
			}
			
			
			if(finalCheck) {
				DecimalFormat decimalFormat = new DecimalFormat("#,###");
				return decimalFormat.format(Double.parseDouble(arr[0])) ;
			}else {
				DecimalFormat decimalFormat = new DecimalFormat("#,###.##");
				return decimalFormat.format(param);
			}
		}catch (Exception e) {
			return String.valueOf(param);
		}
		
	}
	
}
