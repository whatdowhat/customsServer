package com.keepgo.whatdo.service.user;

import java.io.IOException;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.customs.request.CompanyInfoReq;
import com.keepgo.whatdo.entity.customs.request.UserReq;
import com.keepgo.whatdo.entity.customs.response.CompanyInfoRes;
import com.keepgo.whatdo.entity.customs.response.UserRes;

@Service
public interface UserService {

	@Transactional
	List<?> getAll(UserReq userReq); 
	
	@Transactional
	UserRes addUser(UserReq userReq);
	
	@Transactional
	UserRes updateUser(UserReq userReq);
	
	@Transactional
	UserRes deleteUser(UserReq userReq);
	
	@Transactional
	UserRes changePassword(UserReq userReq);
	
	

}
