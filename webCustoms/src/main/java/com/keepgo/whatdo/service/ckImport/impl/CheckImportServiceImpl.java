package com.keepgo.whatdo.service.ckImport.impl;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.map.HashedMap;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.define.ColorType;
import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.define.FileTypeRes;
import com.keepgo.whatdo.define.FreightType;
import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.entity.customs.CheckImport;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.Unbi;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CheckImportReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UnbiReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CheckImportRes;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.repository.CheckImportRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.repository.UnbiRepository;
import com.keepgo.whatdo.service.ckImport.CheckImportService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;
import com.keepgo.whatdo.service.unbi.UnbiService;

@Component
public class CheckImportServiceImpl implements CheckImportService {

	@Autowired
	InboundRepository _inboundRepository;

	@Autowired
	InboundMasterRepository _inboundMasterRepository;

	@Autowired
	FileUploadRepository _fileUploadRepository;

	@Autowired
	UtilService _UtilService;

	@Autowired
	CommonRepository _commonRepository;

	@Autowired
	CompanyInfoRepository _companyInfoRepository;
	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;
	@Autowired
	UnbiRepository _unbiRepository;
	@Autowired
	FinalInboundRepository _finalInboundRepository;
	
	@Autowired
	CheckImportRepository _checkImportRepository;
	
	@Override
	public List<CheckImportRes> getCheckImportByCompanyId(CheckImportReq checkImportReq)  {
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		InboundMaster im = _inboundMasterRepository.findById(checkImportReq.getInboundMasterId()).get();
		
		List<CheckImportRes> result = _checkImportRepository.findByCompanyInfoId(im.getCompanyInfo().getId()).stream()
				.sorted(Comparator.comparing(CheckImport::getRegDate))
				.map(item->{
					CheckImportRes rt = CheckImportRes.builder()
							.id(item.getId())
							.blNo(item.getInboundMaster().getBlNo())
							.companyNm(item.getCompanyInfo().getCoNmEn())
							.memo(item.getMemo())
							.regDtStr(format.format(item.getRegDate()))
							.build();
				return rt;
				})
				.collect(Collectors.toList());
		return result;
	
	}
	
	@Override
	public boolean commitCheckImport(CheckImportReq checkImportReq) {
		
		
		CheckImport checkImport = _checkImportRepository.findByInboundMasterIdAndCompanyInfoId(checkImportReq.getInboundMasterId(), checkImportReq.getCompanyInfoId());
		InboundMaster im = _inboundMasterRepository.findById(checkImportReq.getInboundMasterId()).get();
		CompanyInfo company = _companyInfoRepository.findById(checkImportReq.getCompanyInfoId()).get();
		String blNo = im.getBlNo();
		if(checkImport!=null) {
			checkImport.setMemo(blNo+" "+checkImportReq.getMemo());
			checkImport.setRegDate(new Date());
			
			
			
			_checkImportRepository.save(checkImport);
		}else {
			CheckImport ch = new CheckImport();
			ch.setInboundMaster(im);
			ch.setCompanyInfo(company);
			ch.setRegDate(new Date());
			ch.setUser(User.builder().id(new Long(1)).build());
			ch.setNo(0);
			ch.setOrderNo(0);	
			ch.setMemo(blNo+" "+checkImportReq.getMemo());
			_checkImportRepository.save(ch);
		}
		
		


		return true;
	}
	
	

}