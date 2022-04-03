package com.keepgo.whatdo.service.util.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.service.util.UtilService;

import lombok.Builder;

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
		int spanAttr[][] = new int[2][14];
		for (int i = 0; i < spanAttr.length; i++) {
			for (int j = 0; j < spanAttr[i].length; j++) {
				spanAttr[i][j] = 1;
			}

		}

		
		
//		private int companyNmSpan =1;
//		private int markingSpan = 1;
//		private int korNmSpan = 1;
//		private int itemCountSpan = 1;	
//		private int boxCountSpan = 1;
//		private int weightSpan = 1;
//		private int cbmSpan = 1;
//		private int reportPriceSpan = 1;
//		private int memo1Span = 1;
//		private int memo2Span = 1;
//		private int memo3Span = 1;
//		private int itemNoSpan = 1;
//		private int hsCodeSpan = 1;
//		private int workDateSpan = 1;
		
		
		for (int i = 0; i < list.size(); i++) {

			InboundRes item = list.get(i);
			//companyNmSpan
			if (predict01(item.getCompanyNm()) && lastIndex != i) {
				spanAttr[1][0] = spanAttr[1][0] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][0]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][0]).setCompanyNmSpan(spanAttr[1][0]);
				}
				spanAttr[1][0] = 1;
				item.setCompanyNmSpan(spanAttr[1][0]);
			}
////			korNmSpan
//			if (predict01(item.getKorNm()) && lastIndex != i) {
//				spanAttr[1][1] = spanAttr[1][1] + 1; // [0]:start , [0]:columindex , value :병합 카운터
//			} else {
//				if ((i - spanAttr[1][1]) < 0) {
//					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][1]).setKorNmSpan(spanAttr[1][1]);
//				}
//				spanAttr[1][1] = 1;
//				item.setKorNmSpan(spanAttr[1][1]);
//			}
////			ItemCount
//			if (predict01(item.getItemCount()) && lastIndex != i) {
//				spanAttr[1][2] = spanAttr[1][2] + 1; // [0]:start , [0]:columindex , value :병합 카운터
//			} else {
//				if ((i - spanAttr[1][2]) < 0) {
//					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][2]).setItemCountSpan(spanAttr[1][2]);
//				}
//				spanAttr[1][2] = 1;
//				item.setItemCountSpan(spanAttr[1][2]);
//			}
//			BoxCount
			if (predict01(item.getBoxCount()) && lastIndex != i) {
				spanAttr[1][3] = spanAttr[1][3] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][3]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][3]).setBoxCountSpan(spanAttr[1][3]);
				}
				spanAttr[1][3] = 1;
				item.setBoxCountSpan(spanAttr[1][3]);
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
			//비고2
			if (predict01(item.getMemo2()) && lastIndex != i) {
				spanAttr[1][6] = spanAttr[1][6] + 1;
			} else {
				if ((i - spanAttr[1][6]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][6]).setMemo2Span(spanAttr[1][6]);
				}
				spanAttr[1][6] = 1;
				item.setMemo2Span(spanAttr[1][6]);
			}
			//비고3
			if (predict01(item.getMemo3()) && lastIndex != i) {
				spanAttr[1][7] = spanAttr[1][7] + 1;
			} else {
				if ((i - spanAttr[1][7]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][7]).setMemo3Span(spanAttr[1][7]);
				}
				spanAttr[1][7] = 1;
				item.setMemo3Span(spanAttr[1][7]);
			}
			
//			private int weightSpan = 1;

			//Weight
			if (predict01(item.getWeight()) && lastIndex != i) {
				spanAttr[1][8] = spanAttr[1][8] + 1;
			} else {
				if ((i - spanAttr[1][8]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][8]).setWeightSpan(spanAttr[1][8]);
				}
				spanAttr[1][8] = 1;
				item.setWeightSpan(spanAttr[1][8]);
			}
//			private int cbmSpan = 1;
			//Cbm
			if (predict01(item.getCbm()) && lastIndex != i) {
				spanAttr[1][9] = spanAttr[1][9] + 1;
			} else {
				if ((i - spanAttr[1][9]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][9]).setCbmSpan(spanAttr[1][9]);
				}
				spanAttr[1][9] = 1;
				item.setCbmSpan(spanAttr[1][9]);
			}
			if (predict01(item.getCoCode()) && lastIndex != i) {
				spanAttr[1][10] = spanAttr[1][10] + 1;
			} else {
				if ((i - spanAttr[1][10]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][10]).setCoCodeSpan(spanAttr[1][10]);
				}
				spanAttr[1][10] = 1;
				item.setCoCodeSpan(spanAttr[1][10]);
			}	
//			if (predict01(item.getCoId()) && lastIndex != i) {
//				spanAttr[1][11] = spanAttr[1][11] + 1;
//			} else {
//				if ((i - spanAttr[1][11]) < 0) {
//					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][11]).setCoIdSpan(spanAttr[1][11]);
//				}
//				spanAttr[1][11] = 1;
//				item.setCoIdSpan(spanAttr[1][11]);
//			}	

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

	public boolean predict01(Long value) {
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