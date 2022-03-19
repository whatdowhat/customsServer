package com.keepgo.whatdo.controller;


import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import com.keepgo.whatdo.entity.Notice;
import com.keepgo.whatdo.mapper.NoticesMapper;
import com.keepgo.whatdo.repository.NoticeRepository;

@Controller
//@RequestMapping(value = "/api")
public class MemberController {
	
//	private final Logger LOG = LoggerFactory.getLogger(this.getClass());

	//한글 properteis 사용시.
	@Value("#{appconfig['app.userData01']}")
	private String data01;
	
	@Autowired
	NoticeRepository _notice;
	
//	@Autowired
//	BCryptPasswordEncoder encoder;
	
//	@Value("#{app.userData02}")
//	private String data02;
	
	@Autowired
	NoticesMapper _notices;
    
    @GetMapping("/t1")
    public String t1(HttpServletRequest httpServletRequest) throws Exception {
      
    	String tes = httpServletRequest.getParameter("test");
    
    	System.out.println("tes::"+tes);
    	System.out.println("tes::"+data01);
    	List<Notice> l= (List<Notice>) _notices.selectAll();
    	
    	System.out.println(l);
    	
    	_notice.findAll()
//    	.stream().map(item ->{
//    		
//    		return item.getRegdt().getHour();
//    	})
    	.forEach(System.out::println);
    	
//    	System.out.println("tes::"+data02);
    	
//	  LOG.info("{}","t1 enter 한글 인토딩");
	  return "hello";
	  
    }
    
    @GetMapping("/t2")
    public String t2() throws Exception {
      
    	
	  System.out.println("size::"+_notices.selectAll().size());
	  return "/templates/helloHtml";
	  
    }
    
    @GetMapping("/admin/t1")
    public String admint1() throws Exception {
    
//      LOG.info("admint1 enter");
	  System.out.println("admint1 enter");
	  return "admin/adminpage";
	  
    }
}
