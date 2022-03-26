package com.keepgo.whatdo.service.util.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.service.util.UtilService;

@Component
public class UtilServiceImpl implements UtilService {

	@Override
	public List<InboundRes> changeExcelFormatNew(List<InboundRes> list) {

		Comparator<InboundRes> comparator = Comparator.comparing(InboundRes::getOrderNo);
		list = list.stream().sorted(comparator).collect(Collectors.toList());
		int lastIndex = list.size();
		list.add(InboundRes.builder().build());
		// [타켓 row][미정]
		// [타켓 컬럼][병합 span갯수]

		// 배열 init default 1
		int spanAttr[][] = new int[2][6];
		for (int i = 0; i < spanAttr.length; i++) {
			for (int j = 0; j < spanAttr[i].length; j++) {
				spanAttr[i][j] = 1;
			}

		}

		for (int i = 0; i < list.size(); i++) {

			InboundRes item = list.get(i);

			if (predict01(item.getWorkDate()) && lastIndex != i) {
				spanAttr[1][0] = spanAttr[1][0] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][0]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][0]).setWorkDateSpan(spanAttr[1][0]);
				}
				spanAttr[1][0] = 1;
				item.setWorkDateSpan(spanAttr[1][0]);
			}
			if (predict01(item.getCompanyNm()) && lastIndex != i) {
				spanAttr[1][1] = spanAttr[1][1] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][1]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][1]).setCompanyNmSpan(spanAttr[1][1]);
				}
				spanAttr[1][1] = 1;
				item.setCompanyNmSpan(spanAttr[1][1]);
			}
			if (predict01(item.getExportNm()) && lastIndex != i) {
				spanAttr[1][2] = spanAttr[1][2] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][2]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][2]).setExportNmSpan(spanAttr[1][2]);
				}
				spanAttr[1][2] = 1;
				item.setExportNmSpan(spanAttr[1][2]);
			}
			if (predict01(item.getBlNo()) && lastIndex != i) {
				spanAttr[1][3] = spanAttr[1][3] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][3]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][3]).setBlNoSpan(spanAttr[1][3]);
				}
				spanAttr[1][3] = 1;
				item.setBlNoSpan(spanAttr[1][3]);
			}
			if (predict01(item.getMemo1()) && lastIndex != i) {
				spanAttr[1][4] = spanAttr[1][4] + 1;
			} else {
				if ((i - spanAttr[1][4]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][4]).setMemo1Span(spanAttr[1][4]);
				}
				spanAttr[1][4] = 1;
				item.setMemo1Span(spanAttr[1][4]);
			}
			if (predict01(item.getMarking()) && lastIndex != i) {
				spanAttr[1][5] = spanAttr[1][5] + 1;
			} else {
				if ((i - spanAttr[1][5]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][5]).setMarkingSpan(spanAttr[1][5]);
				}
				spanAttr[1][5] = 1;
				item.setMarkingSpan(spanAttr[1][5]);
			}
		}

		// 마지막 list 제거 해줌
		list.remove(lastIndex);

		return list;
	}

	public boolean predict01(String value) {
		if (value == null || value.replaceAll(" ", "").equals("")) {
			return true;
		}
		return false;
	}

	public boolean predict01(Double value) {
		if (value == null) {
			return true;
		}

		return false;
	}

	public boolean predict01(Integer value) {
		if (value == null) {
			return true;
		}
		return false;
	}

	public boolean predict01(Date value) {
		if (value == null) {
			return true;
		}
		return false;
	}
}