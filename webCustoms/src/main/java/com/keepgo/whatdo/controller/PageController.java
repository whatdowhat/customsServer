package com.keepgo.whatdo.controller;


import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.keepgo.whatdo.entity.PageVO;
import com.keepgo.whatdo.mapper.MemberMapper;
import com.keepgo.whatdo.mapper.NodeMapper;

@RestController
public class PageController {
	
	static final Logger log = LoggerFactory.getLogger(PageController.class);
	
	@Autowired
	NodeMapper _nodeMapper;
	
	@Autowired
	MemberMapper _memberMapper;
    
	@RequestMapping(value = "/pub/pageTest01",method = { RequestMethod.POST,RequestMethod.GET})
//    @ResponseBody
    public PageVO tPost(@RequestBody PageVO pageVO, HttpServletRequest httpServletRequest) throws Exception {
//	public PageVO tPost(@RequestParam PageVO pageVO, HttpServletRequest httpServletRequest) throws Exception {
      
		
		pageVO.setStartIndex(pageVO.getCurrentPage()* pageVO.getPageBy());
		pageVO.setEndIndex( ((pageVO.getCurrentPage()+1) * pageVO.getPageBy()));
		
		pageVO.setData(_memberMapper.selectPage(pageVO));
		pageVO.setTotalSize(_memberMapper.selectPageCount());
		
		int remainInt = pageVO.getTotalSize() / pageVO.getPageBy();
		int remain = pageVO.getTotalSize() % pageVO.getPageBy() == 0 ? 0 : 1;
		pageVO.setPagingSize(remainInt + remain);
		
		
	  return pageVO;
	  
	}
	
	@RequestMapping(value = "/pub/pageTest01Search",method = { RequestMethod.POST})
  public PageVO pageTest01Search(@RequestBody PageVO pageVO, HttpServletRequest httpServletRequest) throws Exception {
    
		
		pageVO.setCurrentPage(pageVO.getCurrentPage()* pageVO.getPageBy());
		pageVO.setStartIndex(pageVO.getCurrentPage()* pageVO.getPageBy());
		
//		pageVO.setEndIndex( ((pageVO.getCurrentPage()+1) * pageVO.getPageBy()));
		pageVO.setData(_memberMapper.selectPageSearch(pageVO));
		pageVO.setTotalSize(_memberMapper.selectPageSearchCount(pageVO));
		
		int remainInt = pageVO.getTotalSize() / pageVO.getPageBy();
		int remain = pageVO.getTotalSize() % pageVO.getPageBy() == 0 ? 0 : 1;
		pageVO.setPagingSize(remainInt + remain);
		
		
	  return pageVO;
	  
	}
}
