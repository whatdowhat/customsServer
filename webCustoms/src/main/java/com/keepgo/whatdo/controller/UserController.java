package com.keepgo.whatdo.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.PageVO;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.Inbound;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.mes.Order;
import com.keepgo.whatdo.mapper.InboundMapper;
import com.keepgo.whatdo.mapper.OrderMapper;
import com.keepgo.whatdo.mapper.UserMapper;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.InboundRepository;
import com.keepgo.whatdo.repository.ItemRepository;
import com.keepgo.whatdo.repository.OrderItemRepository;
import com.keepgo.whatdo.repository.OrderRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.util.CustomExcel;
import com.keepgo.whatdo.viewEntity.CommonMasterViewRequest;
import com.keepgo.whatdo.viewEntity.CommonViewRequest;
import com.keepgo.whatdo.viewEntity.CommonViewResponse;
import com.keepgo.whatdo.viewEntity.OrderViewRequest;
import com.keepgo.whatdo.viewEntity.OrderViewResponse;
import com.keepgo.whatdo.viewEntity.ResultResponse;

@RestController
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class UserController {

	static final Logger log = LoggerFactory.getLogger(UserController.class);

	@Autowired
	InboundRepository _inboundRepository;

	@Autowired
	InboundMapper _inboundMapper;

	@Autowired
	UserMapper _userMapper;

	@Autowired
	UserRepository _userRepository;

	@RequestMapping(value = "/test/user", method = { RequestMethod.POST, RequestMethod.GET })
	public List<User> user(HttpServletRequest httpServletRequest) throws Exception {

		List<User> list = _userMapper.selectAll();

		return list;
	}

	@PostMapping(value = "/test/user/addData")

	public PageVO addData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

		List<User> list = pageVO.getUserData();
		for (int i = 0; i < list.size(); i++) {
			String password = "";
			User user = new User();
			password = bCryptPasswordEncoder.encode(list.get(i).getPassword());
			user = list.get(i);
			user.setPassword(password);
			user.setRegDt(new Date());
			user.setUpdateId("admin");
			user.setLoginId(list.get(i).getPhoneNo());
			user = _userRepository.save(user);

		}

		List<User> allList = _userMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;

	}

	@PostMapping(value = "/test/user/editData")

	public PageVO updateData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<User> list = pageVO.getUserData();

		for (int i = 0; i < list.size(); i++) {
			User user = _userRepository.findById(list.get(i).getId()).orElse(User.builder().build());
			user = list.get(i);
			user = _userRepository.save(user);

		}
		List<User> allList = _userMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;

	}

	@PostMapping(value = "/test/user/deleteData")

	public PageVO deleteData(@RequestBody PageVO pageVO) throws IOException, InterruptedException {

		List<User> list = pageVO.getUserData();

		for (int i = 0; i < list.size(); i++) {

			User user = _userRepository.findById(list.get(i).getId()).orElse(User.builder().build());

			_userRepository.delete(user);
		}

		List<User> allList = _userMapper.selectAll();

		pageVO.setData(allList);

		return pageVO;

	}

	@RequestMapping(value = "/test/user/excelDownload", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownload(@RequestBody PageVO pageVO, ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {

		List<User> list = pageVO.getUserData();

		for (int i = 0; i < list.size(); i++) {
			list.get(i).setPassword("");

		}

		log.error("<<ExcelDownloadController");
		log.error("{0}", list);
		log.error("<<ExcelDownloadController");
		String filename = "exceldownload_test";
		model.addAttribute("targetList", list);
		model.addAttribute("sheetName", "first");
		model.addAttribute("workBookName", filename);
		return new CustomExcel();
	}

	@RequestMapping(value = "/test/user/excelUpload", method = { RequestMethod.POST })
	@ResponseBody
	public PageVO uploadAjax(MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
		System.out.println("here!");
		System.out.println(file);
		System.out.println(test);
		List<User> dataList = new ArrayList<>();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
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
			User data = getUser(row);
			if (data == null) {

			} else {
				addOrUpdateUser(data);
			}
		}

//			System.out.println("datalist size : :" + dataList.size());
//			System.out.println("datalist size : :" + dataList);
		PageVO pageVO = new PageVO();
		List<User> allList = _userMapper.selectAll();
		pageVO.setData(allList);

		return pageVO;
	}

	public User getUser(Row row) {

		boolean validationId = true;
		boolean validationEtC = true;

		User user = new User();
		Long id = new Long(0);
		try {
			id = new Double(row.getCell(0).getNumericCellValue()).longValue();
			user.setId(id);
		} catch (Exception e) {
			validationId = false;
		}

		try {
			user.setName(row.getCell(1).getStringCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}
		try {

			user.setPhoneNo(row.getCell(2).getStringCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}
		try {
			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String password = bCryptPasswordEncoder.encode(row.getCell(3).getStringCellValue());
			user.setPassword(password);
		} catch (Exception e) {
			validationEtC = false;
		}
		try {
			user.setLoginId(row.getCell(4).getStringCellValue());
		} catch (Exception e) {
			validationEtC = false;
		}

		if ((validationId == false) && (validationEtC == true)) {
			// 생성 후보
			User u = _userRepository.findByLoginId(user.getLoginId());
			if (u == null) {
				user.setRegDt(new Date());
				return user;
			} else {
				return null;
			}
		} else {
			if (validationEtC == false) {
				// 아무것도 안함.
				return null;
			} else {
				Date regDt = _userRepository.findById(user.getId()).orElseGet(null).getRegDt();
				user.setRegDt(regDt);
				user.setUpdateDt(new Date());
				user.setUpdateId(user.getLoginId());

				return user;
			}
		}

//		//loginId double -> string
//		Double numberdata1 = row.getCell(2).getNumericCellValue();
//		String number1 = Double.toString(numberdata1);
//		int numberlength1 = number1.length();
//		if (numberlength1 == 12) {
//			loginId="0" + String.valueOf(numberdata1).replaceAll("\\.", "").substring(0, 9) + "0";
//		} else if (numberlength1 == 13) {
//			loginId="0" + String.valueOf(numberdata1).replaceAll("\\.", "").substring(0, 10);
//		}
//		
//		List<User> user = _userMapper.selectBySeq(id);
//		List<User> loginIdList = _userMapper.selectByloginId(loginId);
//		if (user.size() == 0 ||id==0) {
//			try {
//				// 이름
//				data.setName(row.getCell(1).getStringCellValue());
//			} catch (Exception e) {
//				data.setName("");
//			}
//
//			try {
//				// 핸드폰
//				data.setPhoneNo(row.getCell(2).getStringCellValue());
//				if (data.getPhoneNo().replaceAll(" ", "").equals("")) {
//				}
//			} catch (Exception e) {
//
//				try {
//					// 핸드폰 숫자로 들어온경우
//					// 1.055555555E9
//					// 01055555555
//
//					Double numberdata = row.getCell(2).getNumericCellValue();
//					String number = Double.toString(numberdata);
//					int numberlength = number.length();
//					if (numberlength == 12) {
//						data.setPhoneNo(
//								"0" + String.valueOf(numberdata).replaceAll("\\.", "").substring(0, 9) + "0");
//					} else if (numberlength == 13) {
//						data.setPhoneNo("0" + String.valueOf(numberdata).replaceAll("\\.", "").substring(0, 10));
//					}
//
//				} catch (Exception ee) {
//					data.setPhoneNo("");
//				}
//			}
//
//			try {
//				// 비밀번호
//				password = bCryptPasswordEncoder.encode(row.getCell(3).getStringCellValue());
//				data.setPassword(password);
//			} catch (Exception e) {
//				data.setPassword("");
//			}
//			data.setRegDt(new Date());
//			data.setLoginId(data.getPhoneNo());
//			data.setUpdateId("admin");
//			_userRepository.save(data);
//			
//			//업데이트
//		} else if (user.size() != 0) {
//			User updateUser = _userRepository.findById(id).orElse(User.builder().build());
//			try {
//				// 이름
//				updateUser.setName(row.getCell(1).getStringCellValue());
//			} catch (Exception e) {
//				updateUser.setName("");
//			}
//
//			try {
//				// 핸드폰
//				updateUser.setPhoneNo(row.getCell(2).getStringCellValue());
//				if (updateUser.getPhoneNo().replaceAll(" ", "").equals("")) {
//				}
//			} catch (Exception e) {
//
//				try {
//					// 핸드폰 숫자로 들어온경우
//					// 1.055555555E9
//					// 01055555555
//
//					Double numberdata = row.getCell(2).getNumericCellValue();
//					String number = Double.toString(numberdata);
//					int numberlength = number.length();
//					if (numberlength == 12) {
//						updateUser.setPhoneNo(
//								"0" + String.valueOf(numberdata).replaceAll("\\.", "").substring(0, 9) + "0");
//					} else if (numberlength == 13) {
//						updateUser.setPhoneNo(
//								"0" + String.valueOf(numberdata).replaceAll("\\.", "").substring(0, 10));
//					}
//
//				} catch (Exception ee) {
//					updateUser.setPhoneNo("");
//				}
//			}
//
//			try {
//				// 비밀번호
//				password = bCryptPasswordEncoder.encode(row.getCell(3).getStringCellValue());
//				updateUser.setPassword(password);
//			} catch (Exception e) {
//				updateUser.setPassword("");
//			}
//			updateUser.setUpdateDt(new Date());
//			updateUser.setLoginId(data.getPhoneNo());
//			data.setUpdateId(data.getPhoneNo());
//			_userRepository.save(updateUser);
//		}else if (loginIdList.size() != 0) {
//			
//		}

	}

	public boolean addOrUpdateUser(User user) {

		return _userRepository.save(user) != null ? true : false;
	}

}
