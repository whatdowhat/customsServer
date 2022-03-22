package com.keepgo.whatdo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.keepgo.whatdo.entity.PageVO;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.mes.Item;
import com.keepgo.whatdo.entity.mes.Order;
import com.keepgo.whatdo.entity.mes.OrderItem;
import com.keepgo.whatdo.mapper.CommonMapper;
import com.keepgo.whatdo.mapper.InboundMapper;
import com.keepgo.whatdo.mapper.OrderMapper;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.repository.ItemRepository;
import com.keepgo.whatdo.repository.OrderItemRepository;
import com.keepgo.whatdo.repository.OrderRepository;
import com.keepgo.whatdo.util.CustomExcel;
import com.keepgo.whatdo.viewEntity.CommonMasterViewRequest;
import com.keepgo.whatdo.viewEntity.CommonViewRequest;
import com.keepgo.whatdo.viewEntity.CommonViewResponse;
import com.keepgo.whatdo.viewEntity.OrderViewRequest;
import com.keepgo.whatdo.viewEntity.OrderViewResponse;
import com.keepgo.whatdo.viewEntity.ResultResponse;

@RestController
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class MESController {

	static final Logger log = LoggerFactory.getLogger(MESController.class);

	@Autowired
	ItemRepository _itemRepository;

	@Autowired
	OrderRepository _orderRepository;

	@Autowired
	OrderItemRepository _orderItemRepository;

	@Autowired
	InboundRepository _inboundRepository;

	@Autowired
	InboundMapper _inboundMapper;

	@Autowired
	OrderMapper _orderMapper;

	@Autowired
	CommonMasterRepository _commonMasterRepository;

	@Autowired
	CommonRepository _commonRepository;

	@Autowired
	CommonMapper _commonMapper;

	
	@GetMapping("/test/order/insert")
	@Transactional
	public boolean orderinsert(HttpServletRequest httpServletRequest, @RequestBody OrderViewRequest orderViewRequest) {

		Order order = Order.builder().memo("비고").no("엔오").build();
//		_orderRepository.save(order);
		Item item =Item.builder().itemNm("아이템이름").itemStandard("규격").build();
		OrderItem orderItem = OrderItem.builder().price(100).order(_orderRepository.save(order))
//				.item(item)
				.build();
		String s  = null;
		s.equals("");
		_orderItemRepository.save(orderItem);
		
		
		
		
		return true;
	}
	
	
	@GetMapping("/test/order")
	public List<?> order(HttpServletRequest httpServletRequest, @RequestBody OrderViewRequest orderViewRequest)
			throws Exception {

		List<?> result = _orderRepository.findAll().stream()
				.map(item -> OrderViewResponse.builder().id(item.getId()).count(item.getOrderItems().size())
//						.price( item.getOrderItems().stream().mapToInt(sub->sub.getCount() * sub.getPrice().sum()))
						.price(item.getOrderItems().stream().mapToInt(sub -> sub.getCount() * sub.getPrice()).sum())
						.OrderNo(item.getNo()).build())
				.collect(Collectors.toList());

//		new OrderViewResponse() {aklj = a1, adf= 2,};
		return result;
	}

	@GetMapping("/test/order2")
	public List<?> order2(HttpServletRequest httpServletRequest, @RequestBody OrderViewRequest orderViewRequest)
			throws Exception {

		return _orderMapper.selectAll();
	}

	@RequestMapping(value = "/test/inbound", method = { RequestMethod.POST, RequestMethod.GET })
	public List<Inbound> inbound(HttpServletRequest httpServletRequest) throws Exception {

		List<Inbound> list = _inboundMapper.selectAll();

		return list;
	}

	@PostMapping(value = "/test/inbound/addData")

	public PageVO addInboundData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<Inbound> list = pageVO.getInboundData();
		for (int i = 0; i < list.size(); i++) {

			Inbound inbound = new Inbound();

			inbound = list.get(i);
			inbound.setWorkDate(new Date());
			inbound = _inboundRepository.save(inbound);

		}

		List<Inbound> allList = _inboundMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;

	}

	@PostMapping(value = "/test/inbound/editData")

	public PageVO updateInboundData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<Inbound> list = pageVO.getInboundData();

		for (int i = 0; i < list.size(); i++) {
			Inbound inbound = _inboundRepository.findById(list.get(i).getId()).orElse(Inbound.builder().build());
			inbound = list.get(i);
			inbound = _inboundRepository.save(inbound);

		}
		List<Inbound> allList = _inboundMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;

	}

	@PostMapping(value = "/test/inbound/deleteData")

	public PageVO deleteInboundData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<Inbound> list = pageVO.getInboundData();

		for (int i = 0; i < list.size(); i++) {

			Inbound inbound = _inboundRepository.findById(list.get(i).getId()).orElse(Inbound.builder().build());

			_inboundRepository.delete(inbound);
		}

		List<Inbound> allList = _inboundMapper.selectAll();

		pageVO.setData(allList);

		return pageVO;

	}

	@RequestMapping(value = "/test/string", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public String test(HttpServletRequest httpServletRequest) throws Exception {

		return "/test/string";
	}

	@PostMapping(value = "/test/create/CommonMaster")

	public CommonMaster createMaster(HttpServletRequest httpServletRequest,
			@RequestBody CommonMasterViewRequest commonMasterViewRequest) throws Exception {

		CommonMaster m = _commonMasterRepository.save(CommonMaster.builder().Nm(commonMasterViewRequest.getName())
				.isUsing(commonMasterViewRequest.getIsUsing()).user(commonMasterViewRequest.getUser())
				.createDt(new Date()).build());

		return m;
	}

	@GetMapping("/test/get/CommonMaster")
	public List<CommonMaster> getMaster(HttpServletRequest httpServletRequest) throws Exception {

		List<CommonMaster> result = _commonMasterRepository.findAll2();

		return result;
	}

	@PutMapping("/test/update/CommonMaster")
	public CommonMaster updateMaster(HttpServletRequest httpServletRequest,
			@RequestBody CommonMasterViewRequest commonMasterViewRequest) throws Exception {

		CommonMaster m = _commonMasterRepository.findById(commonMasterViewRequest.getId())
				.orElse(CommonMaster.builder().build());
		m.setIsUsing(commonMasterViewRequest.getIsUsing());
		m.setUpdateDt(new Date());
		m.setUser(commonMasterViewRequest.getUser());
		m.setNm(commonMasterViewRequest.getName());

		m = _commonMasterRepository.save(m);

		return m;
	}

	@DeleteMapping("/test/delete/CommonMaster")
	public List<CommonMaster> deleteMaster(HttpServletRequest httpServletRequest,
			@RequestBody CommonMasterViewRequest commonMasterViewRequest) throws Exception {

		CommonMaster m = _commonMasterRepository.findById(commonMasterViewRequest.getId())
				.orElse(CommonMaster.builder().build());

		_commonMasterRepository.delete(m);

		List<CommonMaster> result = _commonMasterRepository.findAll2();

		return result;

	}

	@PostMapping(value = "/test/create/Common")

	public ResultResponse<Common> createCommon(HttpServletRequest httpServletRequest,
			@RequestBody CommonViewRequest commonViewRequest) throws Exception {

		try {
			CommonMaster master = _commonMasterRepository.findById(commonViewRequest.getCommonMasterId())
					.orElse(CommonMaster.builder().build());

			Common m = _commonRepository
					.save(Common.builder().nm(commonViewRequest.getName()).isUsing(commonViewRequest.getIsUsing())
							.user(commonViewRequest.getUser()).createDt(new Date()).commonMaster(master).build());
			return ResultResponse.<Common>builder().success(true).build();
		} catch (Exception e) {
			return ResultResponse.<Common>builder().errorMsg(e.getMessage()).build();
		}

	}

	@GetMapping("/test/get/Common")
	public List<CommonViewResponse> getCommon(HttpServletRequest httpServletRequest) throws Exception {

		List<CommonViewResponse> result = _commonMapper.selectAll();
//		List<Common> result = _commonMapper.selectAll().stream().map(item->
//		CommonViewResponse.builder()
//			.user(item.getUser())
//			.commonMasterId(item.ma)

		return result;
	}

	@PostMapping(value = "/test/common/addData")

	public PageVO addCommonData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<Common> list = pageVO.getCommonData();
		for (int i = 0; i < list.size(); i++) {

			Common common = new Common();

			common = list.get(i);
			common.setCreateDt(new Date());
			common = _commonRepository.save(common);

		}

		List<CommonViewResponse> allList = _commonMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;

	}

	@PostMapping(value = "/test/common/editData")

	public PageVO updateCommonData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<Common> list = pageVO.getCommonData();

		for (int i = 0; i < list.size(); i++) {
			Common common = _commonRepository.findById(list.get(i).getId()).orElse(Common.builder().build());
			common = list.get(i);
			common.setUpdateDt(new Date());
			common = _commonRepository.save(common);

		}
		List<CommonViewResponse> allList = _commonMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;

	}

	@PostMapping(value = "/test/common/deleteData")

	public PageVO deleteCommonData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<Common> list = pageVO.getCommonData();

		for (int i = 0; i < list.size(); i++) {

			Common common = _commonRepository.findById(list.get(i).getId()).orElse(Common.builder().build());

			_commonRepository.delete(common);
		}

		List<CommonViewResponse> allList = _commonMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;

	}

	@RequestMapping(value = "/test/common/excelDownload", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownload(@RequestBody PageVO pageVO, ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {

		List<Common> list = pageVO.getCommonData();

		log.error("<<ExcelDownloadController");
		log.error("{0}", list);
		log.error("<<ExcelDownloadController");
		String filename = "exceldownload_test";
		model.addAttribute("targetList", list);
		model.addAttribute("sheetName", "first");
		model.addAttribute("workBookName", filename);
		return new CustomExcel();
	}

	@RequestMapping(value = "/test/common/excelUpload", method = { RequestMethod.POST })
	@ResponseBody
	public PageVO excelUpload(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
		System.out.println("here!");
		System.out.println(file);
		System.out.println(test);
		

		String extension = FilenameUtils.getExtension(file.getOriginalFilename());

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}

		Workbook workbook = null;

		if (extension.equals("xlsx")) {
			workbook = new XSSFWorkbook(file.getInputStream());
		} else if (extension.equals("xls")) {
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		Sheet worksheet = workbook.getSheetAt(0);
		System.out.println("worksheet.getPhysicalNumberOfRows() :: " + worksheet.getPhysicalNumberOfRows());
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

			Row row = worksheet.getRow(i);

			Common data = getCommon(row);
			if (data == null) {

			} else {
				addOrUpdateCommon(data);
			}
		}

	

	PageVO pageVO = new PageVO();
	List<CommonViewResponse> allList = _commonMapper.selectAll();
	pageVO.setData(allList);

	return pageVO;
	}

	public Common getCommon(Row row) {

		boolean validationId = true;
		boolean validationEtC = true;

		Common common = new Common();
		Long id = new Long(0);

		try {
			id = new Double(row.getCell(0).getNumericCellValue()).longValue();
			common.setId(id);
		} catch (Exception e) {
			validationId = false;
		}
//		setCommonMaster();
//		common.setCommonMaster(CommonMaster.builder().id(new Long(1)).build());
		try {
			Long commonMasterId = new Long(0);
			commonMasterId = new Double(row.getCell(1).getNumericCellValue()).longValue();
			CommonMaster master = _commonMasterRepository.findById(commonMasterId).orElseGet(null);
			common.setCommonMaster(master);
		} catch (Exception e) {
			validationEtC = false;
		}

		try {
			common.setNm(row.getCell(2).getStringCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}
		try {

			common.setIsUsing(row.getCell(3).getBooleanCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}
		try {
			Long user = new Long(0);
			user = new Double(row.getCell(4).getNumericCellValue()).longValue();
			common.setUser(user);
		} catch (Exception e) {
			validationEtC = false;
		}

		if ((validationId == false) && (validationEtC == true)) {
			// 생성 후보
			Common u = _commonRepository.findByNm(common.getNm());
			if (u == null) {
				common.setCreateDt(new Date());
				return common;
			} else {
				return null;
			}
		} else {
			if (validationEtC == false) {
				// 아무것도 안함.
				return null;
			} else {
//				Date createDt = _commonRepository.findById(common.getId()).orElseGet(null).getCreateDt();
//				common.setCreateDt(createDt);
				common.setUpdateDt(new Date());
				return common;
			}
		}
//		long id = (long) row.getCell(0).getNumericCellValue();
//		String nm = row.getCell(2).getStringCellValue();
//
//		long user =(long) row.getCell(4).getNumericCellValue();
//		long masterId = (long) row.getCell(1).getNumericCellValue();
//		
//		
//		List<Common> common = _commonMapper.selectBySeq(id);
//		List<Common> codeList = _commonMapper.selectByNm(nm);
//		CommonMaster master = _commonMasterRepository.findById(masterId).orElseGet(null);
//		
//		if(master != null) {
//			Common commonVO = new Common();
//			commonVO.setCommonMaster(master);
//			
//		}
//		
//		if (common.size() == 0 ||id==0) {
//			try {
//				// 마스터ID
//				data.setCommonMaster(master);
//			} catch (Exception e) {
//				data.setCommonMaster(master);
//			}
//			
//			
//			try {
//				// 코드이름
//				data.setNm(row.getCell(2).getStringCellValue());
//			} catch (Exception e) {
//				data.setNm("");
//			}
//			
//			try {
//			
//				data.setIsUsing(row.getCell(3).getBooleanCellValue());
//			} catch (Exception e) {
//				data.setIsUsing(false);
//			}
//			
//			try {
//				//  사용자
//				data.setUser(user);
//			} catch (Exception e) {
//				data.setUser((long) 0);
//			}
//
//							
//			data.setCreateDt(new Date());
//							
//			_commonRepository.save(data);
//			
//			//업데이트
//		} else if (common.size() != 0) {
//			Common updateCommon = _commonRepository.findById(id).orElse(Common.builder().build());
//			try {
//				// 코드이름
//				updateCommon.setNm(row.getCell(2).getStringCellValue());
//			} catch (Exception e) {
//				updateCommon.setNm("");
//			}
//			
//			try {
//
//				updateCommon.setIsUsing(row.getCell(3).getBooleanCellValue());
//			} catch (Exception e) {
//				updateCommon.setIsUsing(false);
//			}
//			
//			try {
//				//  사용자
//				
//				updateCommon.setUser(user);
//			} catch (Exception e) {
//				updateCommon.setUser((long) 0);
//			}
//
//			
//
//			
//			updateCommon.setCreateDt(new Date());
//			
//			
//			_commonRepository.save(updateCommon);
//		}else if (codeList.size() != 0) {
//			
//		}

	}

	public boolean addOrUpdateCommon(Common common) {

		return _commonRepository.save(common) != null ? true : false;
	}

	
	@RequestMapping(value = "/test/common/test1", method = { RequestMethod.POST })
	@ResponseBody
	public Common test1(MultipartFile file, String test, HttpServletRequest req) {
	
		return _commonRepository.save(Common.builder().nm("test").isUsing(true).build()); 
	}
	
	
}
