package com.keepgo.whatdo.service.departdelay;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.Inbound;
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
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.DepartDelayRes;
import com.keepgo.whatdo.entity.customs.response.InboundMasterRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.ManageRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.entity.customs.response.CheckImportRes;
import com.keepgo.whatdo.entity.customs.response.ChinaSanggumRes;

@Service
public interface DepartDelayService {

	
	
	@Transactional
	List<DepartDelayRes> getDepartDelay(DepartDelayReq departDelayReq);
	
	@Transactional
	boolean commitDepartDelay(DepartDelayReq departDelayReq);

}
