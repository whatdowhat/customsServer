package com.keepgo.whatdo.controller;

import java.time.LocalDateTime;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.ApplicationConfig;
import com.keepgo.whatdo.controller.service.MailService;
import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.MailTO;
import com.keepgo.whatdo.repository.MailTORepository;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class EmailSendController {

	@Autowired
	private MailTORepository _mailTORepository;
	private final ApplicationConfig _applicationConfig;
	
	@RequestMapping("/admin/email")
	public String adminemail(HttpServletRequest req, HttpServletResponse respon, Model model) throws Exception {
		
		return "admin/emailSend";

	}
	
	
	@RequestMapping(value = "/admin/email/send/select", method = { RequestMethod.POST })
	@ResponseBody
	public List<MailTO> adminemailSendSelect(MailTO param) throws Exception, NumberFormatException {

		return _mailTORepository.findAll();
	}
	
	
	@RequestMapping(value = "/admin/email/send", method = { RequestMethod.POST })
	@ResponseBody
	public MailTO adminemailSend(MailTO param) throws Exception, NumberFormatException {

		MailService mailservice = new MailService(_applicationConfig);
		param =  mailservice.sendMail(param);
		if(param!=null) {
			param.setRegDt(LocalDateTime.now());
			return _mailTORepository.save(param);
		}else {
			return null;
		}
		
		
	}
	
	@RequestMapping("/email/test")
	public String emailTest(HttpServletRequest req, HttpServletResponse respon, Model model) throws Exception {
		
		return "email/main";

	}
	
	@RequestMapping(value = "/email/send", method = { RequestMethod.POST })
	@ResponseBody
	public MailTO emailSend(MailTO param) throws Exception, NumberFormatException {

		MailService mailservice = new MailService(_applicationConfig);
		param =  mailservice.sendMail(param);
		if(param!=null) {
			param.setRegDt(LocalDateTime.now());
			return _mailTORepository.save(param);
		}else {
			return null;
		}
		
		
	}
}
