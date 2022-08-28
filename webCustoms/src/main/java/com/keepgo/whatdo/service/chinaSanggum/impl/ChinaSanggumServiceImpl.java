package com.keepgo.whatdo.service.chinaSanggum.impl;

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
import com.keepgo.whatdo.entity.customs.Unbi;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.request.CheckImportReq;
import com.keepgo.whatdo.entity.customs.request.ChinaSanggumReq;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.request.UnbiReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CheckImportRes;
import com.keepgo.whatdo.entity.customs.response.ChinaSanggumRes;
import com.keepgo.whatdo.entity.customs.request.ChinaSanggumReq;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
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
import com.keepgo.whatdo.repository.UnbiRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.chinaSanggum.ChinaSanggumService;
import com.keepgo.whatdo.service.ckImport.CheckImportService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.util.UtilService;
import com.keepgo.whatdo.service.unbi.UnbiService;

@Component
public class ChinaSanggumServiceImpl implements ChinaSanggumService {

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
	FinalInboundInboundMasterRepository _finalInboundInboundMasterRepository;
	@Autowired
	UserRepository _userRepository;
	
	@Override
	public List<ChinaSanggumRes> getChinaSanggum(ChinaSanggumReq chinaSanggumReq)  {
		
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd a HH:mm:ss");
		format.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
		SimpleDateFormat beforFormat = new SimpleDateFormat("yyyy-MM-dd");
		beforFormat.setTimeZone(TimeZone.getTimeZone("Asia/Seoul"));;
		List<FinalInbound> finalInboundList = _finalInboundRepository.findByChinaSanggumYn(1);
		finalInboundList.sort(Comparator.comparing(FinalInbound::getUpdateDt).reversed());
		List<ChinaSanggumRes> result = new ArrayList<>();
		for(int i=0; i<finalInboundList.size(); i++) {
			ChinaSanggum target = new ChinaSanggum();
			ChinaSanggumRes ch = new ChinaSanggumRes();
			Date incomeDt = null;
			target = _chinaSanggumRepository.findByFinalInboundId(finalInboundList.get(i).getId());	
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
			result.add(ch);		
			}
		result.sort(Comparator.comparing(ChinaSanggumRes::getRegDtStr).reversed());
		return result;
	
	}
	
	@Override
	public boolean commitChinaSanggum(ChinaSanggumReq chinaSanggumReq) {
		
		List<Long> chinaSanggumList = chinaSanggumReq.getIds();
		ChinaSanggum chinaSanggum = _chinaSanggumRepository.findByFinalInboundId(chinaSanggumList.get(0).longValue());
		User user = _userRepository.findByLoginId(chinaSanggumReq.getLoginId());
		FinalInbound finalInbound = _finalInboundRepository.findById(chinaSanggumList.get(0).longValue()).get();
		if(chinaSanggum!=null) {
//			checkImport.setMemo(blNo+" "+checkImportReq.getMemo());
			chinaSanggum.setMemo(chinaSanggumReq.getMemo());
			chinaSanggum.setRegDate(new Date());
			if(user==null) {
				chinaSanggum.setUser(User.builder().id(new Long(1)).build());
			}else {
				chinaSanggum.setUser(user);
			}
			chinaSanggum.setFinalInbound(finalInbound);		
			_chinaSanggumRepository.save(chinaSanggum);
		}else {
			ChinaSanggum ch = new ChinaSanggum();
			ch.setMemo(chinaSanggumReq.getMemo());
			ch.setRegDate(new Date());
			if(user==null) {
				ch.setUser(User.builder().id(new Long(1)).build());
			}else {
				ch.setUser(user);
			}
			ch.setNo(0);
			ch.setOrderNo(0);	
			ch.setFinalInbound(finalInbound);		
			_chinaSanggumRepository.save(ch);
		}
		
		


		return true;
	}


}