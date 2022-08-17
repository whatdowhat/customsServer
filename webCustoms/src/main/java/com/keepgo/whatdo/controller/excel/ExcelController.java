package com.keepgo.whatdo.controller.excel;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.define.DocumentType;
import com.keepgo.whatdo.entity.customs.FinalInboundInboundMaster;
import com.keepgo.whatdo.entity.customs.request.FinalInboundInboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.FinalInboundReq;
import com.keepgo.whatdo.entity.customs.request.InboundMasterReq;
import com.keepgo.whatdo.entity.customs.request.InboundReq;
import com.keepgo.whatdo.entity.customs.response.ExcelCLPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelCORes;
import com.keepgo.whatdo.entity.customs.response.ExcelContainerRes;
import com.keepgo.whatdo.entity.customs.response.ExcelCountDetailRes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTARes;
import com.keepgo.whatdo.entity.customs.response.ExcelInboundRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackRes;
import com.keepgo.whatdo.entity.customs.response.ExcelInpackSubRes;
import com.keepgo.whatdo.entity.customs.response.ExcelRCEPRes;
import com.keepgo.whatdo.entity.customs.response.ExcelYATAIRes;
import com.keepgo.whatdo.entity.customs.response.FinalInboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewListRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
import com.keepgo.whatdo.entity.customs.response.UnbiRes;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.InboundMasterRepository;
import com.keepgo.whatdo.service.common.CommonService;
import com.keepgo.whatdo.service.excel.ExcelService;
import com.keepgo.whatdo.service.finalInbound.FinalInboundService;
import com.keepgo.whatdo.service.inbound.InboundService;
import com.keepgo.whatdo.service.unbi.UnbiService;
import com.keepgo.whatdo.service.util.UtilService;

@RestController
public class ExcelController {

	static final Logger log = LoggerFactory.getLogger(ExcelController.class);

	@Autowired
	CommonService _commonService;

	@Autowired
	CommonMasterRepository _commonMasterRepository;
	@Autowired
	InboundMasterRepository _inboundMasterRepository;
	@Autowired
	ExcelService _excelService;
	@Autowired
	UtilService _utilService;
	
	@Autowired
	InboundService _InboundService;

	@Autowired
	ResourceLoader resourceLoader;
	@Autowired
	UnbiService _unbiservice;
	@Autowired
	FinalInboundService _finalInboundService;

	@RequestMapping(value = "/excel/document/ftaData", method = { RequestMethod.POST })
	public ExcelFTARes ExcelFTARes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.ftaData(req);
	}

	@RequestMapping(value = "/excel/document/fta", method = { RequestMethod.POST })
	public void commonMaster(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

		ExcelFTARes s = _excelService.ftaData(req);
		_excelService.fta(s, response);
	}
	
	@RequestMapping(value = "/excel/document/rcepData", method = { RequestMethod.POST })
	public ExcelRCEPRes excelRCEPRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.rcepData(req);
	}
	
	@RequestMapping(value = "/excel/document/rcep", method = { RequestMethod.POST })
	public void rcep(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

		ExcelRCEPRes s =_excelService.rcepData(req);
		_excelService.rcep(s, response);
	}
	
	@RequestMapping(value = "/excel/document/yataiData", method = { RequestMethod.POST })
	public ExcelYATAIRes excelYATAIRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.yataiData(req);
	}
	
	@RequestMapping(value = "/excel/document/yatai", method = { RequestMethod.POST })
	public void yatai(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {

		ExcelYATAIRes s =_excelService.yataiData(req);
		_excelService.yatai(s, response);
	}
	
	@RequestMapping(value = "/excel/document/inpackData", method = { RequestMethod.POST })
	public ExcelInpackRes excelInpackRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.inpackData(req);
	}
	
	@RequestMapping(value = "/excel/document/inpack", method = { RequestMethod.POST })
	public void inpack(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		ExcelInpackRes s =_excelService.inpackData(req);
		
		_excelService.inpack(s, response);
	}
	
	@RequestMapping(value = "/excel/document/containerData", method = { RequestMethod.POST })
	public List<ExcelContainerRes> excelContainerRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.containerData(req);
	}
	
	@RequestMapping(value = "/excel/document/container", method = { RequestMethod.POST })
	public void container(HttpServletRequest httpServletRequest, @RequestBody FinalInboundReq req,
			HttpServletResponse response) throws Exception {

		List<ExcelContainerRes> s =_excelService.containerData(req);
		
		
		_excelService.container(s, response);
	}
	
	@RequestMapping(value = "/excel/document/inboundData", method = { RequestMethod.POST })
	public List<InboundRes> inboundData(HttpServletRequest httpServletRequest, @RequestBody InboundReq req,
			HttpServletResponse response) throws Exception {


		
		//출력모드
		List<InboundRes> result =_excelService.inboundData(req);
		return  result;
	}
	
	@RequestMapping(value = "/excel/document/inbound", method = { RequestMethod.POST })
	public void inbound(HttpServletRequest httpServletRequest, @RequestBody InboundReq req,
			HttpServletResponse response) throws Exception {

		List<InboundRes> s =_excelService.inboundData(req);
		
		
		_excelService.inbound(s, response);
	}
	
	@RequestMapping(value = "/excel/document/clpData", method = { RequestMethod.POST })
	public List<ExcelCLPRes> excelCLPRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.clpData(req);
	}
	@RequestMapping(value = "/excel/document/clp", method = { RequestMethod.POST })
	public void clp(HttpServletRequest httpServletRequest, @RequestBody FinalInboundReq req,
			HttpServletResponse response) throws Exception {

		List<ExcelCLPRes> s =_excelService.clpData(req);
		
		
		_excelService.clp(s, response);
	}
	
	@RequestMapping(value = "/excel/document/userInpackData", method = { RequestMethod.POST })
	public List<ExcelInpackRes> excelUserInpackRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.userInpackData(req);
	}
	
	@RequestMapping(value = "/excel/document/cOData", method = { RequestMethod.POST })
	public List<ExcelCORes> excelCORes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.cOData(req);
	}
	
	@RequestMapping(value = "/excel/document/previewData", method = { RequestMethod.POST })
	public InboundViewListRes inboundViewListResInboundViewListRes(HttpServletRequest httpServletRequest, @RequestBody InboundReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.previewData(req);
	}
	
	@RequestMapping(value = "/excel/document/preview", method = { RequestMethod.POST })
	public void preview(HttpServletRequest httpServletRequest, @RequestBody InboundReq req,
			HttpServletResponse response) throws Exception {


		InboundViewListRes s =_excelService.previewData(req);
		List<UnbiRes> list = _unbiservice.getUnbiByMasterIdForPreView(req);
		FinalInboundRes container = _finalInboundService.getOne(req.getFinalInboundId());
		_excelService.preview(s, list,container,response);
	}
	
	@RequestMapping(value = "/excel/document/previewContainer", method = { RequestMethod.POST })
	public void previewContainer(HttpServletRequest httpServletRequest, @RequestBody InboundReq req,
			HttpServletResponse response) throws Exception {


		InboundViewListRes s =_excelService.previewData(req);
		List<UnbiRes> list = _unbiservice.getUnbiByMasterIdForPreView(req);
		FinalInboundRes container = _finalInboundService.getOne(req.getFinalInboundId());

		_excelService.previewContainer(s, list,container,response);
	}
	
	@RequestMapping(value = "/excel/document/listInpackData", method = { RequestMethod.POST })
	public List<ExcelInpackRes> listInpackData(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.listInpackData(req);
	}
	@RequestMapping(value = "/excel/document/listInpack", method = { RequestMethod.POST })
	public void listInpack(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		List<ExcelInpackRes> s =_excelService.listInpackData(req);
		
		_excelService.listInpack(s, response);
	}
	@RequestMapping(value = "/excel/document/listFtaData", method = { RequestMethod.POST })
	public List<ExcelFTARes> listFtaData(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.listFtaData(req);
	}
	@RequestMapping(value = "/excel/document/listFta", method = { RequestMethod.POST })
	public void listFta(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		List<ExcelFTARes> s =_excelService.listFtaData(req);
		
		_excelService.listFta(s, response);
	}
	@RequestMapping(value = "/excel/document/listRcepData", method = { RequestMethod.POST })
	public List<ExcelRCEPRes> listRcepData(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.listRcepData(req);
	}
	@RequestMapping(value = "/excel/document/listRcep", method = { RequestMethod.POST })
	public void listRcep(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		List<ExcelRCEPRes> s =_excelService.listRcepData(req);
		
		_excelService.listRcep(s, response);
	}
	@RequestMapping(value = "/excel/document/listYataiData", method = { RequestMethod.POST })
	public List<ExcelYATAIRes> listYataiData(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.listYataiData(req);
	}
	@RequestMapping(value = "/excel/document/listYatai", method = { RequestMethod.POST })
	public void listYatai(HttpServletRequest httpServletRequest, @RequestBody FinalInboundInboundMasterReq req,
			HttpServletResponse response) throws Exception {


		List<ExcelYATAIRes> s =_excelService.listYataiData(req);
		
		_excelService.listYatai(s, response);
	}
	@RequestMapping(value = "/excel/document/countDetailData", method = { RequestMethod.POST })
	public List<ExcelCountDetailRes> excelCountDetailRes(HttpServletRequest httpServletRequest, @RequestBody FinalInboundReq req,
			HttpServletResponse response) throws Exception {


		return _excelService.countDetailData(req);
	}
	@RequestMapping(value = "/excel/document/countDetail", method = { RequestMethod.POST })
	public void countDetail(HttpServletRequest httpServletRequest, @RequestBody FinalInboundReq req,
			HttpServletResponse response) throws Exception {

		List<ExcelCountDetailRes> s =_excelService.countDetailData(req);
		
		
		_excelService.countDetail(s, response);
	}
}
