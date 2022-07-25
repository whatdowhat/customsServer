package com.keepgo.whatdo.controller.CompanyInfo;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.keepgo.whatdo.controller.CompanySpecification;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CommonReqForExcelDownload;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.mapper.UserMapper;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.util.CustomExcel;

@RestController
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class CompanyInfoController {

	static final Logger log = LoggerFactory.getLogger(CompanyInfoController.class);

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
	CompanyInfoService _companyInfoService;
	
	
	
	@RequestMapping(value = "/test/companyInfo", method = {RequestMethod.POST })
	public List<?> companyInfo(HttpServletRequest httpServletRequest,CompanyInfoReq companyInfoReq) throws Exception {

		List<?> list = _companyInfoRepository.findAll().stream().map(item-> {
			
			CompanyInfoRes dto = CompanyInfoRes.builder()
				.id(item.getId())
				.coAddress(item.getCoAddress())
				.coNm(item.getCoNm())
				.coNum(item.getCoNum())
				.coInvoice(item.getCoInvoice())
				.updateDt(item.getUpdateDt())
				.manager(item.getManager())
				
				.exports(item.getExports().stream().map(sub_item->{
					Map<String,Object> f = new HashMap<>();
					f.put("comNm", sub_item.getCommon().getNm());
					f.put("comValue", sub_item.getCommon().getValue());
					f.put("preperOrder", sub_item.getPreperOrder());
					f.put("id", sub_item.getId());
					
					return f;
				}).collect(Collectors.toList()) )		
				.build();
				
			
				return dto;
		}).collect(Collectors.toList());
						
//				CompanyInfoRes.builder().build()
				
//				return null;
		return  list;
	}
	@RequestMapping(value = "/company/companyInfo", method = {RequestMethod.POST })
	public List<?> getCompanyInfo(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq companyInfoReq){
		

		return  _companyInfoService.getAll(companyInfoReq);

		
	}
	@RequestMapping(value = "/company/companyInfoByCorpId", method = {RequestMethod.POST })
	public List<?> getCompanyInfoByCorpId(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq companyInfoReq){
		

		return  _companyInfoService.getCompanyByCorpType(companyInfoReq);

		
	}
	
	@RequestMapping(value = "/company/companyInfoExports", method = {RequestMethod.POST })
	public CompanyInfoRes getCompanyInfoExports(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq companyInfoReq){
		

		return  _companyInfoService.getCompanyInfoExports(companyInfoReq);

		
	}
	
	@RequestMapping(value = "/company/companyInfoDelete", method = {RequestMethod.POST })
	public boolean companyInfoDelete(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq companyInfoReq){
		
		return  _companyInfoService.removeCompanyInfo(companyInfoReq);
		
	}
	@RequestMapping(value = "/company/companyInfoCreate", method = {RequestMethod.POST })
	public boolean companyInfoCreate(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq companyInfoReq){
		
		return  _companyInfoService.createCompanyInfo(companyInfoReq);
		
	}
	@RequestMapping(value = "/test/companyInfoCreate", method = {RequestMethod.POST })
	@Transactional
	public CompanyInfoRes companyInfoCreatet(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq req) {

		
		CompanyInfo target = CompanyInfo.builder()
				.coNum(req.getCoNum())
				.coNm(req.getCoNm())
				.coAddress(req.getCoAddress())
				.coInvoice(req.getCoInvoice())
				.isUsing(true)
				.manager(req.getManager())
				.createDt(new Date())
				.updateDt(new Date())
				
				.build();
		target = _companyInfoRepository.save(target);
		final Long companyId = target.getId();
		
		req.getExports().stream().forEach(item->_companyInfoExportRepository.save(CompanyInfoExport.builder()
				.common(Common.builder().id(item).build())
				.companInfoy(CompanyInfo.builder().id(companyId).build())
				.build()));
		
		
		CompanyInfoRes result = Stream.of(_companyInfoRepository.save(target)).map(item-> {
			CompanyInfoRes dto = CompanyInfoRes.builder()
//					.id(item.getId())
					.id(item.getId())
					.coAddress(item.getCoAddress())
					.coNm(item.getCoNm())
					.coNum(item.getCoNum())
					.coInvoice(item.getCoInvoice())
					.updateDt(item.getUpdateDt())
					.build();
//			if(true) {
//			if(Optional.ofNullable(item.getManages()).isPresent()) {
//				dto.setManages(item.getManages().stream().filter(t->Optional.of(t.getCommon()).isPresent()).map(subitem->subitem.getCommon()).collect(Collectors.toMap(Common::getId, t->t.getValue())));
//			}
//			if(Optional.ofNullable(item.getExports()).isPresent()) {
//				dto.setExports(item.getExports().stream().filter(t->Optional.of(t.getCommon()).isPresent()).map(subitem->subitem.getCommon()).collect(Collectors.toMap(Common::getId, t->t.getValue())));
//			}
//			
				return dto;
		}).findAny().orElse(null);
//		CompanyInfo f = null;
//		if(f==null) {
//			 throw new RuntimeException();
//		}
		
		return result;
	}
	@RequestMapping(value = "/test/companyInfoUpdate", method = { RequestMethod.POST})
	public List<CompanyInfo> companyInfoUpdate(HttpServletRequest httpServletRequest,CompanyInfoReq companyInfoReq) throws Exception {

		List<CompanyInfo> list = _companyInfoRepository.findAll();

		return list;
	}
	@RequestMapping(value = "/test/companyInfoDelete", method = { RequestMethod.POST})
	public List<CompanyInfo> companyInfoDeletet(HttpServletRequest httpServletRequest,CompanyInfoReq companyInfoReq) throws Exception {

		List<CompanyInfo> list = _companyInfoRepository.findAll();

		return list;
	}
	
	
	@GetMapping("/test/companyInfo/stream")
    public List<CompanyInfoRes> companyInfostream(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq companyInfo) throws Exception {
    	Map<String,String> condition = BeanUtils.describe(companyInfo);
    	List<CompanyInfoRes> list = _companyInfoRepository.findAll(CompanySpecification.withCondition(condition)).stream().map(x->
    	
    	
    	CompanyInfoRes.builder()
    	.id(x.getId())
    	
    	
    	.build()
    	).collect(Collectors.toList());
    	
    	return list;
    }
	
	@RequestMapping(value = "/company/deleteData", method = {RequestMethod.POST })

	public  CompanyInfoRes deleteData(@RequestBody CompanyInfoReq companyInfoReq) throws IOException, InterruptedException {
		
		CompanyInfoRes result = _companyInfoService.deleteCompanyInfo(companyInfoReq);
		return result;

	}
	
	@RequestMapping(value = "/company/addData", method = {RequestMethod.POST })

	public  boolean addData(@RequestBody CompanyInfoReq companyInfoReq) throws IOException, InterruptedException {

	
		boolean result = _companyInfoService.addCompanyInfo(companyInfoReq);
		return result;

	}
	
	@RequestMapping(value = "/company/updateData", method = {RequestMethod.POST })

	public  CompanyInfoRes updateData(@RequestBody CompanyInfoReq companyInfoReq) throws IOException, InterruptedException {

	
		CompanyInfoRes result = _companyInfoService.updateCompanyInfo(companyInfoReq);
		return result;

	}
	
	@RequestMapping(value = "/company/addExport", method = {RequestMethod.POST })

	public  CompanyInfoRes addExport(@RequestBody CompanyInfoReq companyInfoReq) throws IOException, InterruptedException {

	
		CompanyInfoRes result = _companyInfoService.addCompanyInfoExports(companyInfoReq);
		return result;

	}
	
	@RequestMapping(value = "/company/addManage", method = {RequestMethod.POST })

	public  CompanyInfoRes addManage(@RequestBody CompanyInfoReq companyInfoReq) throws IOException, InterruptedException {

	
		CompanyInfoRes result = _companyInfoService.addCompanyInfoManages(companyInfoReq);
		return result;

	}
	
	@RequestMapping(value = "/company/excelDownload", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownload(@RequestBody CompanyInfoReq companyInfoReq, ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {
		
		
		
		
			List<CompanyInfoReq> result = companyInfoReq.getCompanyInfoReqData();
			String filename = "exceldownload_test";
			model.addAttribute("targetList", result);
			model.addAttribute("sheetName", "first");
			model.addAttribute("workBookName", filename);
			return new CustomExcel();
	
	}
	
	@RequestMapping(value = "/company/excelUpload", method = { RequestMethod.POST })
	@ResponseBody
	public CompanyInfoRes excelUpload(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
//		System.out.println("here!");
//		System.out.println(file);
//		System.out.println(test);
		

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}


		 List <CompanyInfo> list = (List<CompanyInfo>) _companyInfoService.excelUpload(file, null);

		 CompanyInfoRes companyInfoRes = new CompanyInfoRes();
		return companyInfoRes;
	}
	
	@RequestMapping(value = "/company/getOne", method = {RequestMethod.POST })
	public CompanyInfoRes getOne(HttpServletRequest httpServletRequest,@RequestBody CompanyInfoReq companyInfoReq){
		
		return  _companyInfoService.getOne(companyInfoReq.getId());
		
	}
	
}
