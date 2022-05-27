package com.keepgo.whatdo.service.util.impl;

import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.keepgo.whatdo.define.CoType;
import com.keepgo.whatdo.define.CoTypeRes;
import com.keepgo.whatdo.entity.customs.response.ExcelFTASubRes;
import com.keepgo.whatdo.entity.customs.response.InboundRes;
import com.keepgo.whatdo.entity.customs.response.InboundViewRes;
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
		int spanAttr[][] = new int[2][20];
		for (int i = 0; i < spanAttr.length; i++) {
			for (int j = 0; j < spanAttr[i].length; j++) {
				spanAttr[i][j] = 1;
			}

		}
//		spanAttr[1][10] = 0;
		String spanAttrStr[][] = new String[2][20];
		for (int i = 0; i < spanAttrStr.length; i++) {
			for (int j = 0; j < spanAttrStr[i].length; j++) {
				spanAttrStr[i][j] = "";
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
			
				//미리보기 작업일자 형식변환
			if (predict01(item.getForViewWorkDate()) && lastIndex != i) {
				spanAttr[1][1] = spanAttr[1][1] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][1]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][1]).setWorkDateSpan(spanAttr[1][1]);
				}
				spanAttr[1][1] = 1;
				item.setWorkDateSpan(spanAttr[1][1]);
			}
			if (predict01(item.getEngNm() ) && lastIndex != i) {
				spanAttr[1][2] = spanAttr[1][2] + 1;
			} else {
				if ((i - spanAttr[1][2]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][2]).setEngNmSpan(spanAttr[1][2]);
				}
				spanAttr[1][2] = 1;
				item.setEngNmSpan(spanAttr[1][2]);
			}	
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
			
//			if (predict01(item.getMemo1()) && lastIndex != i) {
//				spanAttr[1][4] = spanAttr[1][4] + 1;
//			} else {
//				if ((i - spanAttr[1][4]) < 0) {
//					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][4]).setMemo1Span(spanAttr[1][4]);
//				}
//				spanAttr[1][4] = 1;
//				item.setMemo1Span(spanAttr[1][4]);
//			}
			
			//필수값만 넣는 경우 병합.
			//비고 1,2,3 적용 + 품번,세번 추가 20220412
			if (predict01_for_memo(item.getMemo1()) && lastIndex != i) {
//				spanAttrStr//값비교 배열
//				spanAttr//span 배열
//				spanAttrStr[1][4] = String.valueOf(item.getMemo1());

				//null이 아니거나 0이 아닌 값만 들어온다.
				//이전의 값과 동일하니? --> 첫번째 는 "" 이므로 무조건 x
					if(spanAttrStr[1][4].equals(String.valueOf(item.getMemo1()))) {
						
						//공백인경우
						if(item.getMemo1() == "") {
							item.setMemo1Span(1);
						}else {
							spanAttr[1][4] = spanAttr[1][4] + 1;
							//시작 인덱스 span 값 할당.
							list.get(spanAttr[0][4]).setMemo1Span(spanAttr[1][4]);
							item.setMemo1Span(0);	
						}
						
						
					}else {
						//시작인덱스
						spanAttr[0][4] = i;

						//처음부터 다시.
						//스판 카운터 1
						spanAttr[1][4] = 1;
						item.setMemo1Span(spanAttr[1][4]);
						//현재 값 넣어준다.
						spanAttrStr[1][4] = String.valueOf(item.getMemo1());
					}
			}else {
				//시작인덱스
				spanAttr[0][4] = i;

				//처음부터 다시.
				//스판 카운터 1
				spanAttr[1][4] = 1;
				item.setMemo1Span(spanAttr[1][4]);
				//현재 값 넣어준다.
				spanAttrStr[1][4] = String.valueOf(item.getMemo1());
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
//			if (predict01(item.getMemo2()) && lastIndex != i) {
//				spanAttr[1][6] = spanAttr[1][6] + 1;
//			} else {
//				if ((i - spanAttr[1][6]) < 0) {
//					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][6]).setMemo2Span(spanAttr[1][6]);
//				}
//				spanAttr[1][6] = 1;
//				item.setMemo2Span(spanAttr[1][6]);
//			}
			
			if (predict01_for_memo(item.getMemo2()) && lastIndex != i) {
//				spanAttrStr//값비교 배열
//				spanAttr//span 배열
//				spanAttrStr[1][6] = String.valueOf(item.getMemo1());

				//null이 아니거나 0이 아닌 값만 들어온다.
				//이전의 값과 동일하니? --> 첫번째 는 "" 이므로 무조건 x
					if(spanAttrStr[1][6].equals(String.valueOf(item.getMemo2()))) {
						
						//공백인경우
						if(item.getMemo2() == "") {
							item.setMemo2Span(1);
						}else {
							spanAttr[1][6] = spanAttr[1][6] + 1;
							//시작 인덱스 span 값 할당.
							list.get(spanAttr[0][6]).setMemo2Span(spanAttr[1][6]);
							item.setMemo2Span(0);	
						}
						
						
					}else {
						//시작인덱스
						spanAttr[0][6] = i;

						//처음부터 다시.
						//스판 카운터 1
						spanAttr[1][6] = 1;
						item.setMemo2Span(spanAttr[1][6]);
						//현재 값 넣어준다.
						spanAttrStr[1][6] = String.valueOf(item.getMemo2());
					}
			}else {
				//시작인덱스
				spanAttr[0][6] = i;

				//처음부터 다시.
				//스판 카운터 1
				spanAttr[1][6] = 1;
				item.setMemo2Span(spanAttr[1][6]);
				//현재 값 넣어준다.
				spanAttrStr[1][6] = String.valueOf(item.getMemo2());
			}
			
			//비고3
//			if (predict01(item.getMemo3()) && lastIndex != i) {
//				spanAttr[1][7] = spanAttr[1][7] + 1;
//			} else {
//				if ((i - spanAttr[1][7]) < 0) {
//					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][7]).setMemo3Span(spanAttr[1][7]);
//				}
//				spanAttr[1][7] = 1;
//				item.setMemo3Span(spanAttr[1][7]);
//			}
			if (predict01_for_memo(item.getMemo3()) && lastIndex != i) {
//				spanAttrStr//값비교 배열
//				spanAttr//span 배열
//				spanAttrStr[1][7] = String.valueOf(item.getMemo1());

				//null이 아니거나 0이 아닌 값만 들어온다.
				//이전의 값과 동일하니? --> 첫번째 는 "" 이므로 무조건 x
					if(spanAttrStr[1][7].equals(String.valueOf(item.getMemo3()))) {
						
						//공백인경우
						if(item.getMemo3() == "") {
							item.setMemo3Span(1);
						}else {
							spanAttr[1][7] = spanAttr[1][7] + 1;
							//시작 인덱스 span 값 할당.
							list.get(spanAttr[0][7]).setMemo3Span(spanAttr[1][7]);
							item.setMemo3Span(0);	
						}
						
						
					}else {
						//시작인덱스
						spanAttr[0][7] = i;

						//처음부터 다시.
						//스판 카운터 1
						spanAttr[1][7] = 1;
						item.setMemo3Span(spanAttr[1][7]);
						//현재 값 넣어준다.
						spanAttrStr[1][7] = String.valueOf(item.getMemo3());
					}
			}else {
				//시작인덱스
				spanAttr[0][7] = i;

				//처음부터 다시.
				//스판 카운터 1
				spanAttr[1][7] = 1;
				item.setMemo3Span(spanAttr[1][7]);
				//현재 값 넣어준다.
				spanAttrStr[1][7] = String.valueOf(item.getMemo3());
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
			
			
			//필수값만 넣는 경우 병합.
			//비고 1,2,3 적용
			if (predict01_for_co(item.getCoId()) && lastIndex != i) {
//				spanAttrStr//값비교 배열
//				spanAttr//span 배열
//				spanAttrStr[1][10] = String.valueOf(item.getCoId());

				//null이 아니거나 0이 아닌 값만 들어온다.
				//이전의 값과 동일하니? --> 첫번째 는 "" 이므로 무조건 x
					if(spanAttrStr[1][10].equals(String.valueOf(item.getCoId()))) {
						
						
//						A(1,"FTA",1),
//						B(2,"YATAI",2),
//						C(3,"공백",3);
						
						//공백인경우
						if(item.getCoId()== 3) {
							item.setCoIdSpan(1);
						}else {
							spanAttr[1][10] = spanAttr[1][10] + 1;
							//시작 인덱스 span 값 할당.
							list.get(spanAttr[0][10]).setCoIdSpan(spanAttr[1][10]);
							item.setCoIdSpan(0);	
						}
						
						
					}else {
						//시작인덱스
						spanAttr[0][10] = i;

						//처음부터 다시.
						//스판 카운터 1
						spanAttr[1][10] = 1;
						item.setCoIdSpan(spanAttr[1][10]);
						//현재 값 넣어준다.
						spanAttrStr[1][10] = String.valueOf(item.getCoId());
					
					}
				
			}
			
			if (predict01(item.getBlNo() ) && lastIndex != i) {
				spanAttr[1][11] = spanAttr[1][11] + 1;
			} else {
				if ((i - spanAttr[1][11]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][11]).setBlNoSpan(spanAttr[1][11]);
				}
				spanAttr[1][11] = 1;
				item.setBlNoSpan(spanAttr[1][11]);
			}	

			if (predict01(item.getMasterCompany() ) && lastIndex != i) {
				spanAttr[1][12] = spanAttr[1][12] + 1;
			} else {
				if ((i - spanAttr[1][12]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][12]).setMasterCompanySpan(spanAttr[1][12]);
				}
				spanAttr[1][12] = 1;
				item.setMasterCompanySpan(spanAttr[1][12]);
			}	
			
			if (predict01(item.getMasterExport() ) && lastIndex != i) {
				spanAttr[1][13] = spanAttr[1][13] + 1;
			} else {
				if ((i - spanAttr[1][13]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][13]).setMasterExportSpan(spanAttr[1][13]);
				}
				spanAttr[1][13] = 1;
				item.setMasterExportSpan(spanAttr[1][13]);
			}	
//			if (predict01(item.getReportPrice() ) && lastIndex != i) {
//				spanAttr[1][14] = spanAttr[1][14] + 1;
//			} else {
//				if ((i - spanAttr[1][14]) < 0) {
//					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][14]).setReportPriceSpan(spanAttr[1][14]);
//				}
//				spanAttr[1][14] = 1;
//				item.setReportPriceSpan(spanAttr[1][14]);
//			}
			if (predict01_for_price(item.getReportPrice()) && lastIndex != i) {
//				spanAttrStr//값비교 배열
//				spanAttr//span 배열
//				spanAttrStr[1][4] = String.valueOf(item.getMemo1());

				//null이 아니거나 0이 아닌 값만 들어온다.
				//이전의 값과 동일하니? --> 첫번째 는 "" 이므로 무조건 x
					if(spanAttrStr[1][14].equals(String.valueOf(item.getReportPrice()))) {
						
						//공백인경우
						if(item.getReportPrice() == null) {
							item.setReportPriceSpan(1);
						}else {
							spanAttr[1][14] = spanAttr[1][14] + 1;
							//시작 인덱스 span 값 할당.
							list.get(spanAttr[0][14]).setReportPriceSpan(spanAttr[1][14]);
							item.setReportPriceSpan(0);	
						}
						
						
					}else {
						//시작인덱스
						spanAttr[0][14] = i;

						//처음부터 다시.
						//스판 카운터 1
						spanAttr[1][14] = 1;
						item.setReportPriceSpan(spanAttr[1][14]);
						//현재 값 넣어준다.
						spanAttrStr[1][14] = String.valueOf(item.getReportPrice());
					}
			}else {
				//시작인덱스
				spanAttr[0][14] = i;

				//처음부터 다시.
				//스판 카운터 1
				spanAttr[1][14] = 1;
				item.setReportPriceSpan(spanAttr[1][14]);
				//현재 값 넣어준다.
				spanAttrStr[1][14] = String.valueOf(item.getReportPrice());
			}
			
			
			if (predict01_for_memo(item.getItemNo()) && lastIndex != i) {
					if(spanAttrStr[1][15].equals(String.valueOf(item.getItemNo()))) {
						
						//공백인경우
						if(item.getItemNo() == "") {
							item.setItemNoSpan(1);
						}else {
							spanAttr[1][15] = spanAttr[1][15] + 1;
							//시작 인덱스 span 값 할당.
							list.get(spanAttr[0][15]).setItemNoSpan(spanAttr[1][15]);
							item.setItemNoSpan(0);	
						}
						
						
					}else {
						//시작인덱스
						spanAttr[0][15] = i;

						//처음부터 다시.
						//스판 카운터 1
						spanAttr[1][15] = 1;
						item.setItemNoSpan(spanAttr[1][15]);
						//현재 값 넣어준다.
						spanAttrStr[1][15] = String.valueOf(item.getItemNo());
					}
			}else {
				//시작인덱스
				spanAttr[0][15] = i;

				//처음부터 다시.
				//스판 카운터 1
				spanAttr[1][15] = 1;
				item.setItemNoSpan(spanAttr[1][15]);
				//현재 값 넣어준다.
				spanAttrStr[1][15] = String.valueOf(item.getItemNo());
			}
			

			
//			if (predict01(item.getTotalPrice() ) && lastIndex != i) {
//				spanAttr[1][17] = spanAttr[1][17] + 1;
//			} else {
//				if ((i - spanAttr[1][17]) < 0) {
//					// 병합처리 해야됨. //0보자 작은2 index참조시 무시.
//				} else {
//					list.get(i - spanAttr[1][17]).setTotalPriceSpan(spanAttr[1][17]);
//				}
//				spanAttr[1][17] = 1;
//				item.setTotalPriceSpan(spanAttr[1][17]);
//			}
			
			if (predict01_for_price(item.getTotalPrice()) && lastIndex != i) {
//				spanAttrStr//값비교 배열
//				spanAttr//span 배열
//				spanAttrStr[1][4] = String.valueOf(item.getMemo1());

				//null이 아니거나 0이 아닌 값만 들어온다.
				//이전의 값과 동일하니? --> 첫번째 는 "" 이므로 무조건 x
					if(spanAttrStr[1][17].equals(String.valueOf(item.getTotalPrice()))) {
						
						//공백인경우
						if(item.getTotalPrice() == null) {
							item.setTotalPriceSpan(1);
						}else {
							spanAttr[1][17] = spanAttr[1][17] + 1;
							//시작 인덱스 span 값 할당.
							list.get(spanAttr[0][17]).setTotalPriceSpan(spanAttr[1][17]);
							item.setTotalPriceSpan(0);	
						}
						
						
					}else {
						//시작인덱스
						spanAttr[0][17] = i;

						//처음부터 다시.
						//스판 카운터 1
						spanAttr[1][17] = 1;
						item.setTotalPriceSpan(spanAttr[1][17]);
						//현재 값 넣어준다.
						spanAttrStr[1][17] = String.valueOf(item.getTotalPrice());
					}
			}else {
				//시작인덱스
				spanAttr[0][17] = i;

				//처음부터 다시.
				//스판 카운터 1
				spanAttr[1][17] = 1;
				item.setTotalPriceSpan(spanAttr[1][17]);
				//현재 값 넣어준다.
				spanAttrStr[1][17] = String.valueOf(item.getTotalPrice());
			}
			
			
			if (predict01(item.getOrderNoStr()) && lastIndex != i) {
				spanAttr[1][18] = spanAttr[1][18] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][18]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][18]).setOrderNoStrSpan(spanAttr[1][18]);
				}
				spanAttr[1][18] = 1;
				item.setOrderNoStrSpan(spanAttr[1][18]);
			}
			if (predict01(item.getForViewWorkDateStr() ) && lastIndex != i) {
				spanAttr[1][19] = spanAttr[1][19] + 1; // [0]:start , [0]:columindex , value :병합 카운터
			} else {
				if ((i - spanAttr[1][19]) < 0) {
					// 병합처리 해야됨. //0보자 작은 index참조시 무시.
				} else {
					list.get(i - spanAttr[1][19]).setWorkDateStrSpan(spanAttr[1][19]);
				}
				spanAttr[1][19] = 1;
				item.setWorkDateStrSpan(spanAttr[1][19]);
			}
			
//			System.out.println("3");

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
	
	
	public boolean predict01_for_co(Long value) {
		if (value == null ||value == 0  ) {
			return false;
		}
		return true;
	}
	public boolean predict01_for_memo(String value) {
		if (value == null || value.replaceAll(" ", "").equals("")) {
			return false;
		}
		return true;
	}
	public boolean predict01_for_price(Double value) {
		if (value == null ) {
			return false;
		}
		return true;
	}

	@Override
	public Map<Long,String> getMakingForFTA(List<InboundRes> list) {
		
		Map<Long,String> result = new HashMap<>();
		
		int markingSpan = 0;
		Long id = 0l;
		
		
		for(int i=0; i<list.size();i++) {
			markingSpan = list.get(i).getMarkingSpan();
			
			if(markingSpan>1) {
				//marking이 병합되어있는경우 처리
				
				id = list.get(i).getId();
				
				for(int j=0; j<markingSpan;j++) {
					result.put(list.get(i+j).getId(), list.get(i).getMarking() == null ? "" :  list.get(i).getMarking());
				}	
			}else {
				//marking이 병합되지 않은경우
				if(result.containsKey(list.get(i).getId())) {
					//이미 병합처리로 처리된 항목은 제외
				}else {
					//그게 아닌 한개짜리라면 marking 셋팅
					result.put(list.get(i).getId(), list.get(i).getMarking() == null ? "" :  list.get(i).getMarking());	
				}
				
				
			}
		}
		
		
		return result;
	}
	
	@Override
	public Map<Long,String> getMakingForRCEP(List<InboundRes> list) {
		
		Map<Long,String> result = new HashMap<>();
		
		int markingSpan = 0;
		Long id = 0l;
		
		
		for(int i=0; i<list.size();i++) {
			markingSpan = list.get(i).getMarkingSpan();
			
			if(markingSpan>1) {
				//marking이 병합되어있는경우 처리
				
				id = list.get(i).getId();
				
				for(int j=0; j<markingSpan;j++) {
					result.put(list.get(i+j).getId(), list.get(i).getMarking() == null ? "" :  list.get(i).getMarking());
				}	
			}else {
				//marking이 병합되지 않은경우
				if(result.containsKey(list.get(i).getId())) {
					//이미 병합처리로 처리된 항목은 제외
				}else {
					//그게 아닌 한개짜리라면 marking 셋팅
					result.put(list.get(i).getId(), list.get(i).getMarking() == null ? "" :  list.get(i).getMarking());	
				}
				
				
			}
		}
		
		
		return result;
	}
	
	@Override
	public Map<Long,String> getMakingForYATAI(List<InboundRes> list) {
		
		Map<Long,String> result = new HashMap<>();
		
		int markingSpan = 0;
		Long id = 0l;
		
		
		for(int i=0; i<list.size();i++) {
			markingSpan = list.get(i).getMarkingSpan();
			
			if(markingSpan>1) {
				//marking이 병합되어있는경우 처리
				
				id = list.get(i).getId();
				
				for(int j=0; j<markingSpan;j++) {
					result.put(list.get(i+j).getId(), list.get(i).getMarking() == null ? "" :  list.get(i).getMarking());
				}	
			}else {
				//marking이 병합되지 않은경우
				if(result.containsKey(list.get(i).getId())) {
					//이미 병합처리로 처리된 항목은 제외
				}else {
					//그게 아닌 한개짜리라면 marking 셋팅
					result.put(list.get(i).getId(), list.get(i).getMarking() == null ? "" :  list.get(i).getMarking());	
				}
				
				
			}
		}
		
		
		return result;
	}
}