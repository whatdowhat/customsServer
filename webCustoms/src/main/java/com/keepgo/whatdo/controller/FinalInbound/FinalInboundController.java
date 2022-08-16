package com.keepgo.whatdo.controller.FinalInbound;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

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

import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.define.FreightType;
import com.keepgo.whatdo.entity.customs.CheckImport;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.FinalInbound;
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
import com.keepgo.whatdo.repository.CheckImportRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
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
	CheckImportRepository _checkImportRepository;

	@Autowired
	FileUploadRepository _fileUploadRepository;

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
	
	@Autowired
	FinalInboundRepository _finalInboundRepository;

	@RequestMapping(value = "/front/finalInboundCreate", method = { RequestMethod.POST })
	public boolean finalInboundCreate(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) {

		return _finalInboundService.createFinalInbound(finalInboundReq);

	}

	@RequestMapping(value = "/front/finalInboundAdd", method = { RequestMethod.POST })
	public FinalInboundRes finalInboundAdd(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) throws ParseException {

		return _finalInboundService.addFinalInbound(finalInboundReq);

	}

	@RequestMapping(value = "/front/deleteFinalInbound", method = { RequestMethod.POST })

	public boolean deleteInbound(@RequestBody FinalInboundReq finalInboundReq)
			throws IOException, InterruptedException {

		return _finalInboundService.deleteFinalInbound(finalInboundReq);

	}

	@RequestMapping(value = "/front/finalInboundUpdate", method = { RequestMethod.POST })
	public boolean finalInboundUpdate(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) {

		return _finalInboundService.updateFinalInbound(finalInboundReq);

	}
	@RequestMapping(value = "/front/finalInboundUpdateForUser", method = { RequestMethod.POST })
	public boolean finalInboundUpdateForUser(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) {

		return _finalInboundService.updateFinalInboundForUser(finalInboundReq);

	}

	@RequestMapping(value = "/front/finalInbound", method = { RequestMethod.POST })
	public FinalInboundRes finalInbound(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) {

		return _finalInboundService.getOne(finalInboundReq.getId());

	}

	@RequestMapping(value = "/front/deleteFinalInboundMasterItems", method = { RequestMethod.POST })
	public boolean deleteFinalInboundMasterItems(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) {

		return _finalInboundService.deleteFinalInboundMasterItems(finalInboundReq);

	}

	@RequestMapping(value = "/front/addFinalInboundMasterItems", method = { RequestMethod.POST })

	public boolean addFinalInboundMasterItems(@RequestBody FinalInboundReq finalInboundReq)
			throws IOException, InterruptedException {

		return _finalInboundService.addFinalInboundMasterItems(finalInboundReq);

	}

	@RequestMapping(value = "/front/addDataFinalInboundMasterItems", method = { RequestMethod.POST })

	public boolean addDataFinalInboundMasterItems(@RequestBody FinalInboundReq finalInboundReq)
			throws IOException, InterruptedException {

		return _finalInboundService.addDataFinalInboundMasterItems(finalInboundReq);

	}

	@RequestMapping(value = "/front/getAllCondition", method = { RequestMethod.POST })
	public List<?> finalInboundAll(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) {

		return _finalInboundService.getAllCondition(finalInboundReq);

	}

	@RequestMapping(value = "/front/FinalInboundExcelRead", method = { RequestMethod.POST })
	@ResponseBody
	public boolean excelRead(MultipartFile file, String test, String loginId, HttpServletRequest req)
			throws Exception, NumberFormatException {

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}

		// 출력모드
//			List<InboundRes> result = _utilService.changeExcelFormatNew(list);
		return _finalInboundService.excelRead(file, null, test,loginId);
	}

	@RequestMapping(value = "/front/changeFinalInboundList", method = { RequestMethod.POST })
	public FinalInboundViewRes changeFinalInboundList(HttpServletRequest httpServletRequest,
			@RequestBody InboundReq inboundReq) throws Exception {

		DecimalFormat decimalFormat = new DecimalFormat("#,##0.000");
		DecimalFormat decimalFormat2 = new DecimalFormat("#,###");
		DecimalFormat decimalFormat3 = new DecimalFormat("#,###.00");
		Double itemCountSumFinal = new Double(0);
		Double boxCountSumFinal = new Double(0);
		Double cbmSumFinal = new Double(0);
		Double weightSumFinal = new Double(0);

		FinalInboundViewRes finalRes = new FinalInboundViewRes();
		Long containerId = inboundReq.getContainerId();

		List<FinalInboundInboundMaster> l = _finalInboundInboundMasterRepository.findByFinalInboundId(containerId);
//		Collections.reverse(l);
		l.sort(Comparator.comparing(FinalInboundInboundMaster::getId));
		List<InboundViewRes> finalList = new ArrayList<>();
		int no = 1;
		for (int i = 0; i < l.size(); i++) {
			InboundMaster inboundMaster = l.get(i).getInboundMaster();
			List<InboundRes> list = _InboundService.getInboundByMasterId(inboundMaster.getId());
			List<InboundRes> result2 = _utilService.changeExcelFormatNew(list);
			InboundViewRes result = _InboundService.changeInbound(result2);
			Long id1 = inboundMaster.getId();
			Long id2 = new Long(0);
			if(inboundMaster.getCompanyInfo()!=null) {
				 id2 = inboundMaster.getCompanyInfo().getId();
			}else {
				
			}
			
			CheckImport checkImport = _checkImportRepository.findByInboundMasterIdAndCompanyInfoId(id1, id2);

			itemCountSumFinal = itemCountSumFinal
					+ (result.getItemCountSumD() == null ? 0d : result.getItemCountSumD());
			boxCountSumFinal = boxCountSumFinal + (result.getBoxCountSumD() == null ? 0d : result.getBoxCountSumD());
			cbmSumFinal = cbmSumFinal + (result.getCbmSumD() == null ? 0d : result.getCbmSumD());
			weightSumFinal = weightSumFinal + (result.getWeightSumD() == null ? 0d : result.getWeightSumD());
			result.setFreight(FreightType.getList().stream().filter(type -> type.getId() == inboundMaster.getFreight())
					.findFirst().get().getName());
			result.setBlNo(inboundMaster.getBlNo());
			if (checkImport != null) {
				result.setCheckImportYn(1);
				result.setMemo(checkImport.getMemo());
			} else {
				result.setCheckImportYn(0);
			}
			if (inboundMaster.getCompanyInfo() != null) {
				StringBuffer sb = new StringBuffer();
				String companyNum=inboundMaster.getCompanyInfo().getCoNum();
				sb.append(companyNum);
				sb.insert(3, "-");
				sb.insert(6, "-");
				result.setCompanyId(inboundMaster.getCompanyInfo().getId());
				result.setConsignee(inboundMaster.getCompanyInfo().getCoNm());
				result.setNotify("SAME AS CONSIGNEE");
				result.setCompanyNum(sb.toString());
			} else {
				result.setCompanyId(new Long(0));
				result.setConsignee("");
				result.setNotify("");
				result.setCompanyNum("");
			}
			if (inboundMaster.getComExport() != null) {
				result.setShipper(inboundMaster.getComExport().getValue());
			} else {
				result.setShipper("");
			}
			result.setNo(no);
			result.setInboundMasterId(inboundMaster.getId());
			if (inboundMaster.getFileUploads().size() > 0) {

				result.setATypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.A.getId()).count());

				result.setBTypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.B.getId()).count());

				result.setCTypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.C.getId()).count());

				result.setDTypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.D.getId()).count());

				result.setETypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.E.getId()).count());
				result.setFTypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.F.getId()).count());
				result.setGTypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.G.getId()).count());
				result.setHTypeCount(inboundMaster.getFileUploads().stream()
						.filter(tt -> tt.getFileType() == FileType.H.getId()).count());
			} else {
				result.setATypeCount(new Long(0));
				result.setBTypeCount(new Long(0));
				result.setCTypeCount(new Long(0));
				result.setDTypeCount(new Long(0));
				result.setETypeCount(new Long(0));
				result.setFTypeCount(new Long(0));
				result.setGTypeCount(new Long(0));
				result.setHTypeCount(new Long(0));
				

			}
			result.setATypeNm(FileType.A.getName());
			result.setBTypeNm(FileType.B.getName());
			result.setCTypeNm(FileType.C.getName());
			result.setDTypeNm(FileType.D.getName());
			result.setETypeNm(FileType.E.getName());
			result.setFTypeNm(FileType.F.getName());
			result.setGTypeNm(FileType.G.getName());
			result.setHTypeNm(FileType.H.getName());
			result.setATypeInfo(result.getATypeNm() + ":" + result.getATypeCount() + "개");
			result.setBTypeInfo(result.getBTypeNm() + ":" + result.getBTypeCount() + "개");
			result.setCTypeInfo(result.getCTypeNm() + ":" + result.getCTypeCount() + "개");
			result.setDTypeInfo(result.getDTypeNm() + ":" + result.getDTypeCount() + "개");
			result.setETypeInfo(result.getETypeNm() + ":" + result.getETypeCount() + "개");
			result.setFTypeInfo(result.getFTypeNm() + ":" + result.getFTypeCount() + "개");
			result.setGTypeInfo(result.getGTypeNm() + ":" + result.getGTypeCount() + "개");
			result.setHTypeInfo(result.getHTypeNm() + ":" + result.getHTypeCount() + "개");

					
					
//			if (result.getATypeCount() == 1) {
//				List<FileUpload> fileList = _fileUploadRepository.findByInboundMasterAndFileType(inboundMaster, 1);
//				result.setATypeFileNm(fileList.get(0).getFileName1());
//				result.setATypeInfo(result.getATypeNm() + ":" + result.getATypeFileNm());
//			} else {
//				result.setATypeInfo(result.getATypeNm() + ":" + result.getATypeCount() + "개");
//			}
//			if (result.getBTypeCount() == 1) {
//				List<FileUpload> fileList = _fileUploadRepository.findByInboundMasterAndFileType(inboundMaster, 2);
//				result.setBTypeFileNm(fileList.get(0).getFileName1());
//				result.setBTypeInfo(result.getBTypeNm() + ":" + result.getBTypeFileNm());
//			} else {
//				result.setBTypeInfo(result.getBTypeNm() + ":" + result.getBTypeCount() + "개");
//			}
//			if (result.getCTypeCount() == 1) {
//				List<FileUpload> fileList = _fileUploadRepository.findByInboundMasterAndFileType(inboundMaster, 3);
//				result.setCTypeFileNm(fileList.get(0).getFileName1());
//				result.setCTypeInfo(result.getCTypeNm() + ":" + result.getCTypeFileNm());
//			} else {
//				result.setCTypeInfo(result.getCTypeNm() + ":" + result.getCTypeCount() + "개");
//			}
//			if (result.getDTypeCount() == 1) {
//				List<FileUpload> fileList = _fileUploadRepository.findByInboundMasterAndFileType(inboundMaster, 4);
//				result.setDTypeFileNm(fileList.get(0).getFileName1());
//				result.setDTypeInfo(result.getDTypeNm() + ":" + result.getDTypeFileNm());
//			} else {
//				result.setDTypeInfo(result.getDTypeNm() + ":" + result.getDTypeCount() + "개");
//			}
//			if (result.getETypeCount() == 1) {
//				List<FileUpload> fileList = _fileUploadRepository.findByInboundMasterAndFileType(inboundMaster, 5);
//				result.setETypeFileNm(fileList.get(0).getFileName1());
//				result.setETypeInfo(result.getETypeNm() + ":" + result.getETypeFileNm());
//			} else {
//				result.setETypeInfo(result.getETypeNm() + ":" + result.getETypeCount() + "개");
//			}
//			if (result.getFTypeCount() == 1) {
//				List<FileUpload> fileList = _fileUploadRepository.findByInboundMasterAndFileType(inboundMaster, 6);
//				result.setFTypeFileNm(fileList.get(0).getFileName1());
//				result.setFTypeInfo(result.getFTypeNm() + ":" + result.getFTypeFileNm());
//			} else {
//				result.setFTypeInfo(result.getFTypeNm() + ":" + result.getFTypeCount() + "개");
//			}
//			if (result.getGTypeCount() == 1) {
//				List<FileUpload> fileList = _fileUploadRepository.findByInboundMasterAndFileType(inboundMaster, 7);
//				result.setGTypeFileNm(fileList.get(0).getFileName1());
//				result.setGTypeInfo(result.getGTypeNm() + ":" + result.getGTypeFileNm());
//			} else {
//				result.setGTypeInfo(result.getGTypeNm() + ":" + result.getGTypeCount() + "개");
//			}


			result.setFileTotalInfo(result.getATypeInfo() + "\n" + result.getBTypeInfo() + "\n" + result.getCTypeInfo()
					+ "\n" + result.getDTypeInfo() + "\n" + result.getETypeInfo() + "\n" + result.getFTypeInfo() + "\n"
					+ result.getGTypeInfo() + "\n" + result.getHTypeInfo());
			result.setCurrencyType(inboundMaster.getCurrencyType());
			result.setPackingType(inboundMaster.getPackingType());
			no = no + 1;
			
			List <String> nameList = new ArrayList<>();
			for(int j=0; j<list.size(); j++) {
				nameList.add(list.get(j).getEngNm());
			}
			String itemNmList = nameList.stream().distinct().collect(Collectors.joining("\n"));
			result.setItemNmList(itemNmList);
			finalList.add(result);
		}

		finalRes.setInbounds(finalList);
		finalRes.setItemCountSumFinal(decimalFormat2.format(itemCountSumFinal));
		finalRes.setBoxCountSumFinal(decimalFormat2.format(boxCountSumFinal));
		finalRes.setCbmSumFinal(decimalFormat.format(cbmSumFinal));
		finalRes.setWeightSumFinal(decimalFormat3.format(weightSumFinal));
		return finalRes;
	}

	@RequestMapping(value = "/test/excelDownloadContainer", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownloadContainer(@RequestBody CompanyInfoReq companyInfoReq, ModelAndView modelAndView,
			HttpServletRequest req, HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {

		List<CompanyInfoReq> result = companyInfoReq.getCompanyInfoReqData();
		String filename = "exceldownload_test";
		model.addAttribute("targetList", result);
		model.addAttribute("sheetName", "first");
		model.addAttribute("workBookName", filename);
		return new CustomExcel();

	}
	
	@RequestMapping(value = "/front/getFIList", method = { RequestMethod.POST })
	public List<FinalInboundRes> getFIList(HttpServletRequest httpServletRequest,
			@RequestBody FinalInboundReq finalInboundReq) {
		
		return _finalInboundService.getFIList(finalInboundReq);

	}
}