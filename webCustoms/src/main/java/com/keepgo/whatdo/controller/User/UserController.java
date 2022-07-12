package com.keepgo.whatdo.controller.User;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.expression.ParseException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;

import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.mapper.UserMapper;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.service.user.UserService;
import com.keepgo.whatdo.util.CustomExcel;

@RestController
public class UserController {

	static final Logger log = LoggerFactory.getLogger(UserController.class);

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
	
	@Autowired
	UserService _userService;
	
	
	@RequestMapping(value = "/user/user", method = {RequestMethod.POST })
	public List<?> getuser(HttpServletRequest httpServletRequest, @RequestBody UserReq userReq){
		

		return  _userService.getAll(userReq);
	}
	
	@RequestMapping(value = "/user/addData", method = {RequestMethod.POST })

	public  UserRes addData(@RequestBody UserReq userReq) throws IOException, InterruptedException {

	
		UserRes result = _userService.addUser(userReq);
		return result;

	}
	
	@RequestMapping(value = "/user/updateData", method = {RequestMethod.POST })

	public  UserRes updateData(@RequestBody UserReq userReq) throws IOException, InterruptedException {

	
		UserRes result = _userService.updateUser(userReq);
		return result;

	}
	
	
	@RequestMapping(value = "/user/deleteData", method = {RequestMethod.POST })

	public  UserRes deleteData(@RequestBody UserReq userReq) throws IOException, InterruptedException {
		
		UserRes result = _userService.deleteUser(userReq);
		return result;

	}
	@RequestMapping(value = "/admin/resetPassword", method = {RequestMethod.POST })

	public  UserRes resetPassword(@RequestBody UserReq userReq) throws IOException, InterruptedException {
		
		UserRes result = _userService.resetPassword(userReq);
		return result;

	}

	

	@RequestMapping(value = "/user/excelDownload", method = { RequestMethod.POST, RequestMethod.GET })
	public View excelDownload(@RequestBody UserReq userReq, ModelAndView modelAndView, HttpServletRequest req,
			HttpServletResponse res, HttpSession session, Model model)
			throws ParseException, UnsupportedEncodingException {
		
		
			List<UserReq> result = userReq.getUserReqData();
			for (int i = 0; i < result.size(); i++) {
				result.get(i).setPassword("");

			}
			
			String filename = "userList";
			model.addAttribute("targetList", result);
			model.addAttribute("sheetName", "first");
			model.addAttribute("workBookName", filename);
			return new CustomExcel();
	
	}
	
	@RequestMapping(value = "/user/changePassword", method = {RequestMethod.POST })

	public  UserRes changePassword(@RequestBody UserReq userReq) throws IOException, InterruptedException {

	
		UserRes result = _userService.changePassword(userReq);
		return result;

	}
	
	
	@RequestMapping(value = "/user/userInfo", method = {RequestMethod.POST })

	public  UserRes userInfo(@RequestBody UserReq userReq) throws IOException, InterruptedException {

	
		UserRes result = _userService.userInfo(userReq);
		return result;

	}
	
	@RequestMapping(value = "/user/changeUserInfo", method = {RequestMethod.POST })

	public  UserRes changeUserInfo(@RequestBody UserReq userReq) throws IOException, InterruptedException {

	
		UserRes result = _userService.changeUserInfo(userReq);
		return result;

	}
	

	
}
