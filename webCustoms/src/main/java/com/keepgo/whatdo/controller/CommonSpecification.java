package com.keepgo.whatdo.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.keepgo.whatdo.entity.customs.Common;

public class CommonSpecification {

	final static String startTouch = " 00:00:00";
	final static String endTouch = " 23:59:59";
	public static Specification<Common> withCondition(Map<String, String> condition) {

		return (Specification<Common>) ((root, query, builder) -> {
			List<Predicate> predicate = getPredicateWithKeyword(condition, root, builder);
			return builder.and(predicate.toArray(new Predicate[0]));
		});
	}

	private static List<Predicate> getPredicateWithKeyword(Map<String, String> conditino, Root<Common> root,
			CriteriaBuilder builder) {
		List<Predicate> predicate = new ArrayList<>();
		for (String key : conditino.keySet()) {
			if ("code".equals(key)) {
				if (conditino.get(key) == null || conditino.get(key).equals("")) {
				} else {
					predicate.add(builder.equal(root.get(key), conditino.get(key)));
					
				}

			} else if ("startDate".equals(key)) { // 'partner' 조건은 partner객체 안에 있는 keword데이터를 2차 가공하여 검색

				if (conditino.get(key) == null || conditino.get(key).equals("")) {
				} else {
					// where 1=1
					// and startDate between "2021-03-01" and "2001-04-01"
					try {
						
						predicate.add(builder.between(root.get("createDt"),  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(conditino.get("startDate")+startTouch) ,
								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(conditino.get("endDate")+endTouch)));
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
//		        }else if("namephonechu".equals(key)){
//			            	if(conditino.get(key).equals("")){
////			            		System.out.println("spect:: 공백 ");
//			            	}else{
//			            		
//			            		Predicate condition1 = builder.like(root.get("name"), "%"+conditino.get(key)+"%");
//			            		Predicate condition2 = builder.like(root.get("phone"), "%"+conditino.get(key)+"%");
//			            		Predicate condition3 = builder.like(root.get("church"), "%"+conditino.get(key)+"%");
//			            		predicate.add(builder.or(condition1,condition2,condition3));
//			            	}
//			                
//				       
//		            	
//			     }else if("chunamephone".equals(key)){
//		            	if(conditino.get(key).equals("")){
////		            		System.out.println("spect:: 공백 ");
//		            	}else{
//		            		//or조건 추가
//		            		Predicate condition1 = builder.like(root.get("recommandName"), "%"+conditino.get(key)+"%");
//		            		Predicate condition2 = builder.like(root.get("recommandPhone"), "%"+conditino.get(key)+"%");
//		            		predicate.add(builder.or(condition1,condition2));
//		            	}

			} else { // 'name', 'partner' 이외의 모든 조건 파라미터에 대해 equal 검색

			}
		}
		return predicate;
	}

}
