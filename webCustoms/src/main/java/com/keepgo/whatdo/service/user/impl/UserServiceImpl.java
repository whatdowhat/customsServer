package com.keepgo.whatdo.service.user.impl;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.define.GubunType;
import com.keepgo.whatdo.define.UserType;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.CompanyInfo;
import com.keepgo.whatdo.entity.customs.CompanyInfoExport;
import com.keepgo.whatdo.entity.customs.CompanyInfoManage;
import com.keepgo.whatdo.entity.customs.User;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;
import com.keepgo.whatdo.entity.customs.request.CommonReq;
import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.CompanyInfoExportRepository;
import com.keepgo.whatdo.repository.CompanyInfoManageRepository;
import com.keepgo.whatdo.repository.CompanyInfoRepository;
import com.keepgo.whatdo.repository.UserRepository;
import com.keepgo.whatdo.service.company.CompanyInfoService;
import com.keepgo.whatdo.service.user.UserService;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	CompanyInfoRepository _companyInfoRepository;

	@Autowired
	CompanyInfoExportRepository _companyInfoExportRepository;

	@Autowired
	CompanyInfoManageRepository _companyInfoManageRepository;
	
	@Autowired
	CommonRepository _commonRepository;
	
	@Autowired
	UserRepository _userRepository;

	@Override
	public List<?> getAll(UserReq req) {


		List<?> list = _userRepository.findAll().stream()
				.sorted(Comparator.comparing(User::getUpdateDt).reversed())
				.map(item -> {

			UserRes dto = UserRes.builder()

					
					.id(item.getId()).name(item.getName()).phoneNo(item.getPhoneNo()).password(item.getPassword())
					.loginId(item.getLoginId()).updateDt(item.getUpdateDt()).regDt(item.getRegDt())
					.updateId(item.getUpdateId())
//					.authority(item.getAuthority())
//					.authority(UserType.getList().stream().filter(type->type.getCode() == item.getAuthority()).findFirst().get().getName())
					
					.firstLogin(item.getFirstLogin())
											
					.build();
					for(int i=0; i<UserType.getList().size(); i++) {
						String code = UserType.getList().get(i).getCode();
						if(code.equals(item.getAuthority())) {
							dto.setAuthority(UserType.getList().get(i).getName());
						}
					}
					
			return dto;
		}).collect(Collectors.toList());

		return list;
	}

	
	
	
	

	@Override
	public UserRes addUser(UserReq userReq) {
		User user = new User();
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String password = "";
		password = bCryptPasswordEncoder.encode(userReq.getPassword());
		
		user.setName(userReq.getName());
		user.setPhoneNo(userReq.getPhoneNo());
		user.setPassword(password);
		user.setLoginId(userReq.getLoginId());
		user.setRegDt(new Date());
		user.setUpdateDt(new Date());
		user.setUpdateId("admin");
		user.setFirstLogin("Y");
		user.setAuthority(userReq.getAuthority());
		
		
		_userRepository.save(user);
		

		 UserRes userRes = new UserRes();
		return userRes;
	}
	
	@Override
	public UserRes  updateUser(UserReq userReq) {
		User user = _userRepository.findById(userReq.getId())
				.orElse(User.builder().build());
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String password = "";
		password = bCryptPasswordEncoder.encode(userReq.getPassword());
		
		user.setId(userReq.getId());
		user.setName(userReq.getName());
		user.setPhoneNo(userReq.getPhoneNo());
//		user.setPassword(password);
		user.setLoginId(userReq.getLoginId());
		user.setRegDt(new Date());
		user.setUpdateDt(new Date());
		user.setUpdateId("admin");
		user.setFirstLogin("N");
		user.setAuthority(userReq.getAuthority());
		
		_userRepository.save(user);
		

		UserRes userRes = new UserRes();
		return userRes;
	}

	@Override
	public UserRes deleteUser(UserReq userReq) {
		List<Long> list = userReq.getIds();
		for (int i = 0; i < list.size(); i++) {

			User user = _userRepository.findById(list.get(i).longValue()).orElse(User.builder().build());

			_userRepository.delete(user);
		}
		
		UserRes userRes = new UserRes();
		return userRes;
	}
	
	@Override
	public UserRes  changePassword(UserReq userReq) {
		User user = _userRepository.findByLoginId(userReq.getLoginId());
		BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
		String password = "";
		password = bCryptPasswordEncoder.encode(userReq.getPassword());
		
		
		
		
		user.setPassword(password);
		user.setLoginId(userReq.getLoginId());
		
		user.setUpdateDt(new Date());
		user.setUpdateId("admin");
		user.setFirstLogin("N");
		
		
		_userRepository.save(user);
		

		UserRes userRes = new UserRes();
		return userRes;
	}
	@Override
	public UserRes resetPassword(UserReq userReq) {
		List<Long> list = userReq.getIds();
		for (int i = 0; i < list.size(); i++) {

			User user = _userRepository.findById(list.get(i).longValue()).get();

			BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
			String password = "";
			password = bCryptPasswordEncoder.encode(user.getLoginId());
			
			user.setPassword(password);
			user.setUpdateDt(new Date());
			user.setUpdateId("admin");
			user.setFirstLogin("Y");
			
			
			_userRepository.save(user);
		}
		
		UserRes userRes = new UserRes();
		return userRes;
	}
	
	@Override
	public UserRes userInfo(UserReq req) {

		User item = _userRepository.findByLoginId(req.getLoginId());
		

			UserRes dto = UserRes.builder()

					
					.id(item.getId()).name(item.getName()).phoneNo(item.getPhoneNo()).password(item.getPassword())
					.loginId(item.getLoginId()).updateDt(item.getUpdateDt()).regDt(item.getRegDt())
					.updateId(item.getUpdateId())
					
					.firstLogin(item.getFirstLogin())
											
					.build();
					for(int i=0; i<UserType.getList().size(); i++) {
						String code = UserType.getList().get(i).getCode();
						if(code.equals(item.getAuthority())) {
							dto.setAuthority(UserType.getList().get(i).getName());
						}
					}
					
			return dto;
		

	}
	
	@Override
	public UserRes  changeUserInfo(UserReq userReq) {
		User user = _userRepository.findByLoginId(userReq.getLoginId());
		
		user.setLoginId(userReq.getLoginId());
		user.setName(userReq.getName());
		user.setPhoneNo(userReq.getPhoneNo());
		user.setUpdateDt(new Date());
		user.setUpdateId(userReq.getLoginId());
		user.setFirstLogin("N");
		
		
		_userRepository.save(user);
		

		UserRes userRes = new UserRes();
		return userRes;
	}

	
}
