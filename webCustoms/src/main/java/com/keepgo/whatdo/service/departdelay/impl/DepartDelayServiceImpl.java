package com.keepgo.whatdo.service.departdelay.impl;

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
import java.util.TimeZone;
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
import com.keepgo.whatdo.define.CorpType;
import com.keepgo.whatdo.define.FileType;
import com.keepgo.whatdo.define.FileTypeRes;
import com.keepgo.whatdo.define.FreightType;
import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.entity.customs.CheckImport;
import com.keepgo.whatdo.entity.customs.ChinaSanggum;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.DepartDelay;
import com.keepgo.whatdo.entity.customs.FileUpload;
import com.keepgo.whatdo.entity.customs.FinalInbound;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.InboundMaster;
import com.keepgo.whatdo.entity.customs.Manage;
import com.keepgo.whatdo.entity.customs.Unbi;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CheckImportReq;
import com.keepgo.whatdo.entity.customs.request.ChinaSanggumReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.DepartDelayReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.ManageReq;
import com.keepgo.whatdo.entity.customs.request.UnbiReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CheckImportRes;
import com.keepgo.whatdo.entity.customs.response.ChinaSanggumRes;
import com.keepgo.whatdo.entity.customs.request.ChinaSanggumReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.DepartDelayRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.ManageRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.repository.CheckImportRepository;
import com.keepgo.whatdo.repository.ChinaSanggumRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.DepartDelayRepository;
import com.keepgo.whatdo.repository.FileUploadRepository;
import com.keepgo.whatdo.repository.FinalInboundInboundMasterRepository;
import com.keepgo.whatdo.repository.FinalInboundRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.repository.ManageRepository;
import com.keepgo.whatdo.repository.UnbiRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.chinaSanggum.ChinaSanggumService;
import com.keepgo.whatdo.service.ckImport.CheckImportService;
import com.keepgo.whatdo.service.departdelay.DepartDelayService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.manage.ManageService;
import com.keepgo.whatdo.service.util.UtilService;
import com.keepgo.whatdo.service.unbi.UnbiService;

@Component
public class DepartDelayServiceImpl implements DepartDelayService {

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
	@Autowired
	ChinaSanggumRepository _chinaSanggumRepository;
	@Autowired
	ManageRepository _manageRepository;
	@Autowired
	DepartDelayRepository _departDelayRepository;
	
	@Autowired
	FinalInboundInboundMasterRepository _finalInboundInboundMasterRepository;
	@Autowired
	UserRepository _userRepository;
	
	@Override
	public List<DepartDelayRes> getDepartDelay(DepartDelayReq departDelayReq)  {
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
		SimpleDateFormat beforFormat = new SimpleDateFormat("yyyy-MM-dd");
		beforFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
		List<FinalInbound> finalInboundList = _finalInboundRepository.findByDepartDelayYn(1);
		finalInboundList.sort(Comparator.comparing(FinalInbound::getUpdateDt).reversed());
		List<DepartDelayRes> result = new ArrayList<>();
		for(int i=0; i<finalInboundList.size(); i++) {
			DepartDelay target = new DepartDelay();
			DepartDelayRes ch = new DepartDelayRes();
			Date incomeDt = null;
			Date departDt = null;
			Date firstDepartDt = null;
			target = _departDelayRepository.findByFinalInboundId(finalInboundList.get(i).getId());	
//			ch.setId(target.getId());
			ch.setFinalInboundId(finalInboundList.get(i).getId());
			for(int j=0; j<CorpType.getList().size();j++) {
				if(CorpType.getList().get(j).getId() ==finalInboundList.get(i).getCorpId() ) {
					ch.setCorpType(CorpType.getList().get(j).getShowName());
					
				}
			}
			try {
				incomeDt = beforFormat.parse(finalInboundList.get(i).getIncomeDt());
				departDt = beforFormat.parse(finalInboundList.get(i).getDepartDtStr());
				firstDepartDt = beforFormat.parse(finalInboundList.get(i).getFirstDepartDt()==null?finalInboundList.get(i).getDepartDtStr():finalInboundList.get(i).getFirstDepartDt());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			ch.setDepartDtStr(format.format(departDt));
//			ch.setFirstDepartDtStr(format.format(firstDepartDt));
//			ch.setIncomeDtStr(format.format(incomeDt));
			ch.setDepartDtStr(finalInboundList.get(i).getDepartDtStr());
			ch.setFirstDepartDtStr(finalInboundList.get(i).getFirstDepartDt()==null?finalInboundList.get(i).getDepartDtStr():finalInboundList.get(i).getFirstDepartDt());
			ch.setIncomeDtStr(finalInboundList.get(i).getIncomeDt());
			ch.setRegDtStr(target==null?format.format(finalInboundList.get(i).getUpdateDt()):format.format(target.getRegDate()));
			ch.setMemo(target==null?"":target.getMemo());
			ch.setMasterBlNo(finalInboundList.get(i).getFinalMasterBl());
			ch.setContainerNo(finalInboundList.get(i).getContainerNo());
			ch.setUserNm(target==null?"":target.getUser().getName());
			result.add(ch);		
			}
		result.sort(Comparator.comparing(DepartDelayRes::getRegDtStr).reversed());
		return result;
	
	}
	
	@Override
	public boolean commitDepartDelay(DepartDelayReq departDelayReq)  {
		
		List<Long> list = departDelayReq.getIds();
		DepartDelay departDelay = _departDelayRepository.findByFinalInboundId(list.get(0).longValue());
		User user = _userRepository.findByLoginId(departDelayReq.getLoginId());
		FinalInbound finalInbound = _finalInboundRepository.findById(list.get(0).longValue()).get();
		if(departDelay!=null) {
			departDelay.setMemo(departDelayReq.getMemo());
			departDelay.setRegDate(new Date());
			if(user==null) {
				departDelay.setUser(User.builder().id(new Long(1)).build());
			}else {
				departDelay.setUser(user);
			}
			departDelay.setFinalInbound(finalInbound);		
			_departDelayRepository.save(departDelay);
		}else {
			DepartDelay ch = new DepartDelay();
			ch.setMemo(departDelayReq.getMemo());
			ch.setRegDate(new Date());
			if(user==null) {
				ch.setUser(User.builder().id(new Long(1)).build());
			}else {
				ch.setUser(user);
			}
			ch.setNo(0);
			ch.setOrderNo(0);	
			ch.setFinalInbound(finalInbound);		
			_departDelayRepository.save(ch);
		}
		
		


		return true;
	}



}