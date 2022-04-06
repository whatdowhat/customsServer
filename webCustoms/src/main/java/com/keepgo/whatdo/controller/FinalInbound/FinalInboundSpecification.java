package com.keepgo.whatdo.controller.FinalInbound;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import com.keepgo.whatdo.entity.customs.FinalInbound;

public class FinalInboundSpecification {

	final static String startTouch = " 00:00:00";
	final static String endTouch = " 23:59:59";
	public static Specification<FinalInbound> withCondition(Map<String, Object> condition) {

		return (Specification<FinalInbound>) ((root, query, builder) -> {
			List<Predicate> predicate = getPredicateWithKeyword(condition, root, builder);
			return builder.and(predicate.toArray(new Predicate[0]));
		});
	}

	private static List<Predicate> getPredicateWithKeyword(Map<String, Object> conditino, Root<FinalInbound> root,
			CriteriaBuilder builder) {
		List<Predicate> predicate = new ArrayList<>();
		for (String key : conditino.keySet()) {
			//출발일자 yyyy-MM-dd
			if ("departDtStr".equals(key)) {
				if (conditino.get(key) == null || conditino.get(key).equals("")) {
				} else {
					predicate.add(builder.equal(root.get(key), conditino.get(key)));
					
				}

//			} else if ("startDate".equals(key)) { // 'partner' 조건은 partner객체 안에 있는 keword데이터를 2차 가공하여 검색
//
//				if (conditino.get(key) == null || conditino.get(key).equals("")) {
//				} else {
//					// where 1=1
//					// and startDate between "2021-03-01" and "2001-04-01"
//					try {
//						
//						predicate.add(builder.between(root.get("createDt"),  new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(conditino.get("startDate")+startTouch) ,
//								new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(conditino.get("endDate")+endTouch)));
//					} catch (ParseException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//				}
			} else if ("incomeYearMonth".equals(key)) { // 'partner' 조건은 partner객체 안에 있는 keword데이터를 2차 가공하여 검색

				if (conditino.get(key) == null || conditino.get(key) == "") {
				} else {
					predicate.add(builder.like(root.get("incomeDt"),"%" + conditino.get(key) +"%"));
//					predicate.add(builder.equal(root.get("incomeDt"),conditino.get(key)));
				}

			
			} else if ("incomeDt".equals(key)) { // 'partner' 조건은 partner객체 안에 있는 keword데이터를 2차 가공하여 검색

				if (conditino.get(key) == null || conditino.get(key) == "") {
				} else {
					predicate.add(builder.equal(root.get(key),conditino.get(key)));
				}

			} else if ("gubun".equals(key)) { // 'partner' 조건은 partner객체 안에 있는 keword데이터를 2차 가공하여 검색

				if (conditino.get(key) == null || conditino.get(key) == Integer.valueOf(0)) {
				} else {
					predicate.add(builder.equal(root.get(key),conditino.get(key)));
				}

			} else { // 'name', 'partner' 이외의 모든 조건 파라미터에 대해 equal 검색

			}
		}
		return predicate;
	}

}
