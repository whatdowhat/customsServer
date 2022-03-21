package com.keepgo.whatdo.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.keepgo.whatdo.entity.BaseInfo;
import com.keepgo.whatdo.entity.Notice;
import com.keepgo.whatdo.entity.ReactVO;
import com.keepgo.whatdo.entity.customs.Common;
import com.keepgo.whatdo.entity.customs.CommonMaster;
import com.keepgo.whatdo.entity.customs.response.CommonRes;
import com.keepgo.whatdo.mapper.NodeMapper;
import com.keepgo.whatdo.repository.BasicInfoRepository;
import com.keepgo.whatdo.repository.CommonMasterRepository;
import com.keepgo.whatdo.repository.CommonRepository;
import com.keepgo.whatdo.repository.NoticeRepository;
import com.keepgo.whatdo.viewEntity.ViewCommon;

@RestController
//@CrossOrigin(origins = "http://localhost:3000",allowCredentials = "true" ) // 컨트롤러에서 설정
public class APIController {

	static final Logger log = LoggerFactory.getLogger(APIController.class);

	@Autowired
	NoticeRepository _notice;

	@Autowired
	NodeMapper _nodeMapper;

	@Autowired
	BasicInfoRepository _baseRp;

	@Autowired
	CommonRepository _commonRepository;
	@Autowired
	CommonMasterRepository _commonMasterRepository;

	@RequestMapping(value = "/pub/api01", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<Notice> tPost(HttpServletRequest httpServletRequest) throws Exception {

		System.out.println("api01 enter post");
		List<Notice> notices = new ArrayList<>();
		for (int i = 0; i < 5; i++) {
			notices.add(Notice.builder().Seq(i).Title("title").Context("context01").build());
		}

		return notices;

	}

	@RequestMapping(value = "/pub/api02", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<?> ttPost(HttpServletRequest httpServletRequest) throws Exception {

		return _nodeMapper.selectAll();

	}

	@RequestMapping(value = "/pub/api03", method = { RequestMethod.POST, RequestMethod.GET })
	@ResponseBody
	public List<?> tttPost(HttpServletRequest httpServletRequest) throws Exception {

		return _baseRp.findAll();

	}

//    @RequestMapping(value = "/formexample",method = [RequestMethod.POST,RequestMethod.GET] )
	@RequestMapping(value = "/formexample", method = { RequestMethod.POST })
	@ResponseBody
	public ReactVO formexample(HttpServletRequest req, ReactVO data) throws Exception, NumberFormatException {
//    public ReactVO formexample(@RequestBody ReactVO data,HttpServletRequest req) throws Exception, NumberFormatException {
//    public String formexample(@RequestParam("a1") String a1,String a2,HttpServletRequest req) throws Exception, NumberFormatException {
//    public HashMap formexample(@RequestBody ReactVO data,HttpServletRequest req) throws Exception, NumberFormatException {
//    public String formexample(String fromJSP,String FormData) throws Exception, NumberFormatException {
		System.out.println("api01 enter formexample");

//    	System.out.println(data);
//    	
//    	
//    	
//    	System.out.println("fromJSP:"+data);
//    	System.out.println("fromJSP:"+req.getAttribute("fromJSP"));
////    	System.out.println("FormData:"+FormData);
//    	
//    	HashMap<String,String> m = new HashMap<>();
//		m.put("key1", "asdf");
//		m.put("key2", "123");
////		return "123";
//		System.out.println("result:"+m);
//		return a1+a2;

		return ReactVO.builder().data1("1").data2("2").build();
	}

	final String UPLOAD_IMPOSIIBLE = "N";
	final String UPLOAD_POSIIBLE = "Y";

	@RequestMapping(value = "/excelUpload", method = { RequestMethod.POST })
	@ResponseBody
//	public Map uploadAjax(@RequestBody MultipartFile formData, HttpServletRequest req) throws Exception, NumberFormatException {
	public Map uploadAjax(@RequestParam("imageFile") MultipartFile file, String test, HttpServletRequest req)
			throws Exception, NumberFormatException {
		System.out.println("here!");
		System.out.println(file);
		System.out.println(test);
		List<BaseInfo> dataList = new ArrayList<>();
		String extension = FilenameUtils.getExtension(file.getOriginalFilename()); // 3

		if (!extension.equals("xlsx") && !extension.equals("xls")) {
			throw new IOException("엑셀파일만 업로드 해주세요.");
		}

		Workbook workbook = null;

		if (extension.equals("xlsx")) {
			workbook = new XSSFWorkbook(file.getInputStream());
		} else if (extension.equals("xls")) {
			workbook = new HSSFWorkbook(file.getInputStream());
		}
		Sheet worksheet = workbook.getSheetAt(0);
		System.out.println("worksheet.getPhysicalNumberOfRows() :: " + worksheet.getPhysicalNumberOfRows());
		for (int i = 1; i < worksheet.getPhysicalNumberOfRows(); i++) {

			Row row = worksheet.getRow(i);

			BaseInfo data = new BaseInfo();
			data.setUploadAvailable(UPLOAD_POSIIBLE);
			data.setLineNumber(String.valueOf(i));
			String YYYMMDD = "";
			String startTime = "";
			String endTime = "";

			try {
				// 교육날짜
//					data.setStringEditDt(row.getCell(0).getStringCellValue());
				Date date = row.getCell(0).getDateCellValue();
				YYYMMDD = new SimpleDateFormat("yyyy-MM-dd").format(date);
				data.setYyyyMMdd(YYYMMDD);

			} catch (Exception e) {
				data.setYyyyMMdd("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				// 시작시간
				Date date = row.getCell(1).getDateCellValue();
				String result[] = date.toString().split(" ");
				startTime = result[3];
				// 12:00:00 결과
				startTime = startTime.split(":")[0] + ":" + startTime.split(":")[1];
				// 12:00결과
				data.setStartHhmm(startTime);

			} catch (Exception e) {
				data.setStartHhmm("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				// 종료시간
				Date date = row.getCell(2).getDateCellValue();
				String result[] = date.toString().split(" ");
				endTime = result[3];
				// 12:00:00 결과
				endTime = endTime.split(":")[0] + ":" + endTime.split(":")[1];
				data.setEndHhmm(endTime);

				// 12:00결과
			} catch (Exception e) {
				data.setEndHhmm("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 프로젝트명
				data.setMemberProject(row.getCell(3).getStringCellValue());
			} catch (Exception e) {
				data.setMemberProject("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 멘토팀명
				data.setMemberTeam(row.getCell(4).getStringCellValue());
			} catch (Exception e) {
				data.setMemberTeam("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 멘토이름
				data.setMemberNm(row.getCell(5).getStringCellValue());
			} catch (Exception e) {
				data.setMemberNm("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 멘토이메일
				data.setMemberEmail(row.getCell(6).getStringCellValue());
			} catch (Exception e) {
				data.setMemberEmail("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 멘토핸드폰
				data.setMemberPh(row.getCell(7).getStringCellValue());
				if (data.getMemberPh().replaceAll(" ", "").equals("")) {
					data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
				}
			} catch (Exception e) {

				try {
					// 멘토핸드폰 숫자로 들어온경우
					// 1.055555555E9
					// 01055555555

					Double numberdata = row.getCell(7).getNumericCellValue();
					String number = Double.toString(numberdata);
					int numberlength = number.length();
					if (numberlength == 12) {
						data.setMemberPh("0" + String.valueOf(numberdata).replaceAll("\\.", "").substring(0, 9) + "0");
					} else if (numberlength == 13) {
						data.setMemberPh("0" + String.valueOf(numberdata).replaceAll("\\.", "").substring(0, 10));
					}

				} catch (Exception ee) {
					data.setMemberPh("");
					data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
				}
			}

			try {
				// 리더설정
				data.setMemberLeaderYn(row.getCell(8).getStringCellValue());
			} catch (Exception e) {
				data.setMemberLeaderYn("");
//				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 기관명
				data.setGovNm(row.getCell(9).getStringCellValue());
			} catch (Exception e) {
				data.setGovNm("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 매칭된 학생이름
				data.setMatchingNm(row.getCell(10).getStringCellValue());
			} catch (Exception e) {
				data.setMatchingNm("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			try {
				// 출석여부
				data.setAttendYn(row.getCell(11).getStringCellValue());
			} catch (Exception e) {
				data.setAttendYn("");
//				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				// 기관 담당자
				data.setGovPersonNm(row.getCell(12).getStringCellValue());
			} catch (Exception e) {
				data.setGovPersonNm("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				// 기관담당자 이메일
				data.setGovPersonEmail(row.getCell(13).getStringCellValue());
			} catch (Exception e) {
				data.setGovPersonEmail("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				// 기관담당자 연락처
				data.setGovPersonPh(row.getCell(14).getStringCellValue());
				if (data.getGovPersonPh().replaceAll(" ", "").equals("")) {
					data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
				}
			} catch (Exception e) {

				try {
					// 기관담당자 숫자로 들어온 경우
					double numberdata = row.getCell(14).getNumericCellValue();
					data.setGovPersonPh("0" + String.valueOf(numberdata).replaceAll("\\.", "").substring(0, 10));
				} catch (Exception ee) {
					data.setGovPersonPh("");
					data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
				}

			}
			try {
				// linkUrl
				data.setLinkUrl(row.getCell(15).getStringCellValue());
			} catch (Exception e) {
				data.setLinkUrl("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			// 년월일
			// 시작시간
			// 종료 시간

			try {
				data.setStartDt(getLocalDate(data, "START"));
			} catch (Exception e) {
				data.setStartDt(null);
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				data.setStartDtString(data.getStartDt().toString());
			} catch (Exception e) {
				data.setStartDtString("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				data.setEndDt(getLocalDate(data, "END"));
			} catch (Exception e) {
				data.setEndDt(null);
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}
			try {
				data.setEndDtString(data.getEndDt().toString());
			} catch (Exception e) {
				data.setEndDtString("");
				data.setUploadAvailable(UPLOAD_IMPOSIIBLE);
			}

			dataList.add(data);

		}

//		System.out.println("datalist size : :" + dataList.size());
//		System.out.println("datalist size : :" + dataList);
		Map resultMap = new HashMap<String, Object>();
		resultMap.put("data", dataList);

		return resultMap;
	}

	public LocalDateTime getLocalDate(BaseInfo info, String type) {

		try {

			if (info.getYyyyMMdd().equals("") || info.getStartHhmm().equals("") || info.getEndHhmm().equals("")) {
				return null;
			} else {

				String data = "";
				switch (type) {
				case "START":
					data = info.getYyyyMMdd() + " " + info.getStartHhmm();
					break;
				case "END":
					data = info.getYyyyMMdd() + " " + info.getEndHhmm();
					break;
				default:
					break;
				}
				// 포맷터
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
				// 문자열 -> Date
				LocalDateTime date = LocalDateTime.parse(data, formatter);
				return date;
			}

		} catch (Exception e) {
			return null;
		}

	}

	@GetMapping("/test/test01")
	public Notice test01(HttpServletRequest httpServletRequest) throws Exception {

		log.info("enter");
		return _notice.save(Notice.builder().Context("data").Regdt(LocalDateTime.now()).Title("title").build());

	}

//	@GetMapping("/test/createCommon")
//	public CommonMaster createCommon(HttpServletRequest httpServletRequest) throws Exception {
//
//		CommonMaster m = _commonMasterRepository
//				.save(CommonMaster.builder().code("01").codeNm("테스트1").isUsing(true).createDt(new Date()).build());
//		_commonRepository.save(Common.builder().code("01").masterId(m.getId()).codeNm("아이템1").isUsing(true)
//				.createDt(new Date()).build());
//		_commonRepository.save(Common.builder().code("02").masterId(m.getId()).codeNm("아이템2").isUsing(true)
//				.createDt(new Date()).build());
//		return m;
//
//	}

//    @GetMapping("/test/commonMaster")
//    public List<CommonMaster> commonMaster(HttpServletRequest httpServletRequest) throws Exception {
//    	
//    	List<CommonMaster> m = _commonMasterRepository.findAll();
//    	return m;
//	  
//    }
	@GetMapping("/test/commonMaster/page")
	public Page<CommonMaster> commonpage(HttpServletRequest httpServletRequest, @RequestBody CommonMaster commonMaster)
			throws Exception {
		Map<String, String> condition = BeanUtils.describe(commonMaster);
		Pageable p = PageRequest.of(0, 10);
		Page<CommonMaster> result = _commonMasterRepository.findAll(CommonMasterSpecification.withCondition(condition),
				p);
		return result;
	}

	@GetMapping("/test/commonMaster/list")
	public List<CommonMaster> commonlist(HttpServletRequest httpServletRequest, @RequestBody CommonMaster commonMaster)
			throws Exception {
		Map<String, String> condition = BeanUtils.describe(commonMaster);
		List<CommonMaster> result = _commonMasterRepository.findAll(CommonMasterSpecification.withCondition(condition));
		return result;

	}
	
	@GetMapping("/test/commonMaster/list2")
	public List<CommonMaster> commonpage2(HttpServletRequest httpServletRequest) throws Exception {
		List<CommonMaster> result = _commonMasterRepository.findAll2();
		return result;
	}
	@GetMapping("/test/commonMaster/list3")
	public List<Common> commonpage3(HttpServletRequest httpServletRequest) throws Exception {
		List<Common> result = _commonMasterRepository.findAll3();
		return result;
	}
//	@GetMapping("/test/commonMaster/list4")
//	public Common commonpage4(HttpServletRequest httpServletRequest) throws Exception {
//		Common result = _commonMasterRepository.findAll4();
//		return result;
//	}

//	@GetMapping("/test/common")
//	public List<Common> commonpage(HttpServletRequest httpServletRequest, @RequestBody Common common) throws Exception {
//		Map<String, String> condition = BeanUtils.describe(common);
//		List<Common> result = _commonRepository.findAll(CommonSpecification.withCondition(condition));
//
//		System.out.println(result.get(0).getNodeMember());
//		System.out.println(result.get(1).getNodeMember());
//		return result;
//	}


	
	@GetMapping("/test/common/stream")
    public List<CommonRes> commonpagestream(HttpServletRequest httpServletRequest,@RequestBody Common common) throws Exception {
    	Map<String,String> condition = BeanUtils.describe(common);
//    	List<Common> result = _commonRepository.findAll(CommonSpecification.withCondition(condition));
    	
    	List<CommonRes> result = _commonRepository.findAll(CommonSpecification.withCondition(condition)).stream().map(x->
    	CommonRes.builder()
    	.id(x.getId())
    	.commonMasterId(x.getCommonMaster().getId())
    	
    	.build()
    	).collect(Collectors.toList());
    	
    	return result;
    }
}
