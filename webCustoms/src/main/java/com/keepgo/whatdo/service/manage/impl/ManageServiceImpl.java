package com.keepgo.whatdo.service.manage.impl;

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
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.manage.ManageService;
import com.keepgo.whatdo.service.util.UtilService;
import com.keepgo.whatdo.service.unbi.UnbiService;

@Component
public class ManageServiceImpl implements ManageService {

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
	FinalInboundInboundMasterRepository _finalInboundInboundMasterRepository;
	@Autowired
	UserRepository _userRepository;
	
	@Override
	public List<ManageRes> getManage(ManageReq manageReq)  {
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
		SimpleDateFormat beforFormat = new SimpleDateFormat("yyyy-MM-dd");
		beforFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
		List<FinalInbound> finalInboundList = _finalInboundRepository.findByGwanriYn(1);
		finalInboundList.sort(Comparator.comparing(FinalInbound::getUpdateDt).reversed());
		List<ManageRes> result = new ArrayList<>();
		for(int i=0; i<finalInboundList.size(); i++) {
			Manage target = new Manage();
			ManageRes ch = new ManageRes();
			Date incomeDt = null;
			target = _manageRepository.findByFinalInboundId(finalInboundList.get(i).getId());	
//			ch.setId(target.getId());
			ch.setFinalInboundId(finalInboundList.get(i).getId());
			for(int j=0; j<CorpType.getList().size();j++) {
				if(CorpType.getList().get(j).getId() ==finalInboundList.get(i).getCorpId() ) {
					ch.setCorpType(CorpType.getList().get(j).getShowName());
					
				}
			}
			try {
				incomeDt = beforFormat.parse(finalInboundList.get(i).getIncomeDt());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
//			ch.setIncomeDtStr(format.format(incomeDt));
			ch.setIncomeDtStr(finalInboundList.get(i).getIncomeDt());
			ch.setRegDtStr(target==null?format.format(finalInboundList.get(i).getUpdateDt()):format.format(target.getRegDate()));
			ch.setMemo(target==null?"":target.getMemo());
			ch.setMasterBlNo(finalInboundList.get(i).getFinalMasterBl());
			ch.setContainerNo(finalInboundList.get(i).getContainerNo());
			ch.setUserNm(target==null?"":target.getUser().getName());
			
			for(int j=0; j<GubunType.getList().size();j++) {
				if(GubunType.getList().get(j).getId() ==finalInboundList.get(i).getGubun()) {
					ch.setGubun(GubunType.getList().get(j).getName());
					
				}
			}
			result.add(ch);		
			}
		result.sort(Comparator.comparing(ManageRes::getRegDtStr).reversed());
		return result;
	
	}
	
	@Override
	public boolean commitManage(ManageReq manageReq) {
		
		List<Long> list = manageReq.getIds();
		Manage manage = _manageRepository.findByFinalInboundId(list.get(0).longValue());
		User user = _userRepository.findByLoginId(manageReq.getLoginId());
		FinalInbound finalInbound = _finalInboundRepository.findById(list.get(0).longValue()).get();
		if(manage!=null) {
			manage.setMemo(manageReq.getMemo());
			manage.setRegDate(new Date());
			if(user==null) {
				manage.setUser(User.builder().id(new Long(1)).build());
			}else {
				manage.setUser(user);
			}
			manage.setFinalInbound(finalInbound);		
			_manageRepository.save(manage);
		}else {
			Manage ch = new Manage();
			ch.setMemo(manageReq.getMemo());
			ch.setRegDate(new Date());
			if(user==null) {
				ch.setUser(User.builder().id(new Long(1)).build());
			}else {
				ch.setUser(user);
			}
			ch.setNo(0);
			ch.setOrderNo(0);	
			ch.setFinalInbound(finalInbound);		
			_manageRepository.save(ch);
		}
		
		


		return true;
	}



}