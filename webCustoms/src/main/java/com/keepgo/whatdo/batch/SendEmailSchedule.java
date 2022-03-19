//package com.keepgo.whatdo.batch;
//
//import java.time.LocalDateTime;
//import java.util.List;
//
//import org.apache.ibatis.session.SqlSessionFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Component;
//
//import com.keepgo.whatdo.ApplicationConfig;
//import com.keepgo.whatdo.controller.service.MailService;
//import com.keepgo.whatdo.entity.BaseInfo;
//import com.keepgo.whatdo.entity.MailTO;
//import com.keepgo.whatdo.mapper.BaseInfoMapper;
//import com.keepgo.whatdo.repository.MailTORepository;
//
//import lombok.RequiredArgsConstructor;
//
//@Component
//@RequiredArgsConstructor
//public class SendEmailSchedule {
//
//
//    @Autowired
//    BaseInfoMapper _baseInfoMapper;
//    
//	@Autowired
//	private MailTORepository _mailTORepository;
//	private final ApplicationConfig _applicationConfig;
//	
//	//매일 오후 1시 수행.
////    @Scheduled(cron = "0 41 20 * * *")
//    @Scheduled(cron = "0 0 13 * * *")
//    public void task1() {
//    	
//    	List<BaseInfo> targets = _baseInfoMapper.selectSendEmailTarget();
//    	
//    	for(int i=0; i<targets.size();i++) {
//    		StringBuilder builder = new StringBuilder();
//    		builder.append("안녕하세요.\n");
//    		builder.append(targets.get(i).getGovNm() + " 님\n");
//    		builder.append("[mentor app]에서 아래의 항목을\n");
//    		builder.append("프로젝트 : "+targets.get(i).getMemberProject() +" 팀명 : "+ targets.get(i).getMemberTeam() + " 교육시간 :"+targets.get(i).getYyyyMMdd() + " " +targets.get(i).getStartHhmm() +"\n");
//    		builder.append("매칭완료를 수행해주세요.\n");
//    		
//    		MailTO entity = MailTO.builder()
//    		.address(targets.get(i).getGovPersonEmail())
//    		.title("[mentor app]매칭 정보를 확정 시켜주세요.")
//    		.message(builder.toString())
//    		.build();
//    		
//    		MailService mailservice = new MailService(_applicationConfig);
//    		entity =  mailservice.sendMail(entity);
//    		if(entity!=null) {
//    			entity.setRegDt(LocalDateTime.now());
//    			_mailTORepository.save(entity);
//    		}
//		}
//    }
//
//    
//    
//}
