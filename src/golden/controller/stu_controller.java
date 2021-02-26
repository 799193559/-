package golden.controller;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
/*import com.mysql.jdbc.exceptions.MySQLSyntaxErrorException;*/

import golden.mail.MailSenderSrvServices;
import golden.model.Collection;
import golden.model.Course;
import golden.model.HasReadWordsWithBLOBs;
import golden.model.Homework;
import golden.model.Message;
import golden.model.Student;
import golden.model.StudentCourse;
import golden.model.StudentHomework;
import golden.model.Teacher;
import golden.model.WordListWithBLOBs;
import golden.service.userservice;
import golden.tempmodel.ChatList;
import golden.tempmodel.SearchCourse;
import golden.tempmodel.collection_1;
import golden.time_handler.get_last_week_interval;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;





@Controller
@RequestMapping({"/api/stu"})
@Api(tags = "学生端，以下接口参数除说明外，均为Integer")
public class stu_controller {
    
	@Autowired
	userservice user_service;
	
	@Autowired
	MailSenderSrvServices mail_sender;
	public String ip = "121.37.204.235";

	@RequestMapping({"/S1"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "单词详情查询接口 ")
	public JSONObject queryword(@ApiParam(required=true,value="单词id")Integer word_id)throws NumberFormatException{
		String code=null;//��ѯjson�ֶγ�����������Ҫ�ǵø������°汾��mysql-connector-java
	    String msg="";
	    JSONObject json=new JSONObject();
	    System.out.println(word_id);
		if(word_id==null) {
			code="-1";
		    msg="word_id missing";
		    json.put("code",code);
		    json.put("msg",msg);
		    return json;
		}		
		 	 
	    WordListWithBLOBs word_info=user_service.selectbyword_id(word_id);
		

	    if (word_info!=null) {
	    	code="200";
	        msg="success";
	    }
	    else { 
	    	code="-2";
	    	msg="faild";
	    }

	    json.put("data",word_info);//�Զ����ø���get������ֵ��json��Ӧ��key
        json.put("code",code);
        json.put("msg",msg);
	    return json;
 }


	
		
	  @RequestMapping({"/S2"})	  
	  @ResponseBody
	  @ApiOperation(httpMethod = "GET",value = "单词模糊查询接口 ")
	  public JSONObject queryword_list(@ApiParam(required=true,value="单词，类型为String")String word){
		  String code=null;
	      String msg=null;
	      JSONObject json=new JSONObject();
		  if(word==""||word==null) {
			  code="-1";
			  msg="word missing";
			  json.put("code",code); 
		      json.put("msg",msg); 
		      return json;
		  }		  
	      List<Object> word_list=user_service.selectbyword(word); 
	      if(word_list==null) {
			  code="-2";
			  msg="fail";
			  json.put("code",code); 
		      json.put("msg",msg); 
		      return json;
	      }
	      code="200";
		  msg="sucess";
	      json.put("data", word_list);  
	      json.put("code",code); 
	      json.put("msg",msg); 
	      System.out.print("\n"+word_list.toString()+"\n");
	      return json; 
	  }
	  
	  
	  @SuppressWarnings("deprecation")
	  @RequestMapping({"/S3"})
	  @ApiOperation(httpMethod = "GET",value = "获取学生已学单词统计 ")
	  @ResponseBody
	  public JSONObject getStatistics(@ApiParam(required=true,value="学生id") Integer student_id) throws ParseException {
		  String code=null,msg=null; 
		  JSONObject result=new JSONObject();
		  if(student_id==null) {
			  code="-1";
			  msg="student_id missing";
			  result.put("code",code);
			  result.put("msg",msg);
			  return result;
		  }
		  HasReadWordsWithBLOBs hasreadwords = user_service.selectby_studentid(student_id);
		  if(hasreadwords==null) {
			  code="-2";
			  msg="weekly data doesn't exit";
			  result.put("code",code);
			  result.put("msg",msg);
			  return result;
		  }
		  JSONArray statistics = (JSONArray) hasreadwords.getWords();
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		  get_last_week_interval getinterval=new get_last_week_interval();
		  Date[] dates=getinterval.getLastWeekInterval();
		  int num[]= {0,0,0,0,0,0,0};
		  for(int i=statistics.size()-1,flag=7;i>=0&&flag>0;i--) {
			  JSONObject temp= (JSONObject) statistics.get(i);
			  Date date=sdf.parse((String) temp.get("date"));//���ַ���ת��Ϊ��������
			  if(date.after(dates[0])&&date.before(dates[1])){
		          JSONArray data=  (JSONArray)temp.get("data");
		          @SuppressWarnings("deprecation")
				  int day=date.getDay();//0�������գ�1������1....�Դ����ƣ�ע���calendar�Ļ�ȡ�������ֿ�
		          num[day]+=data.size();
		          flag--;
			  }			  
		  }	
//		  num[7]=num[0];

		  result.put("weekly",num);
//		  System.out.println(dates[0]);
//		  System.out.println(date.after(dates[0])&&date.before(dates[1]));
//		  System.out.println(sdf.format(date));
//		  System.out.println(temp.get("date").getClass().toString());
//		  System.out.println("++++++++"+statistics.get(0)+"+++++++++++++++");
		  golden.model.Calendar calendarcol = user_service.select_calendar_by_studentid(student_id); 
		  if(calendarcol==null) {
			  code="-3";
			  msg="yearly data doesn't exit";
			  result.put("code",code);
			  result.put("msg",msg);
			  return result;
		  }
		  JSONArray calendar = (JSONArray) calendarcol.getCalendarcol();
		  SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM");
		  Date date=new Date();//��ȡ���ݿ��е�����
		  Date calendar_1 = new Date();//���ڻ�ȡ��ǰ���
		  System.out.println(calendar_1.getYear());//���ص�ǰ�����1900��Ĳ�
		  int sum[]= {0,0,0,0,0,0,0,0,0,0,0,0};
		  for(int i=calendar.size()-1,flag=12;i>=0&&flag>0;i--) {//����JSONArray���飬��flag������Ч��ѯ
			  JSONObject temp =(JSONObject)calendar.get(i);
			  date = sdf_1.parse((String)temp.get("date"));			  
			  if(date.getYear()==calendar_1.getYear()) {
				  int mon = date.getMonth();//������ֻ�ǲ��Ƽ�ʹ�ö��ѣ���Ӱ��
//				  System.out.println(calendar_1.getMonth());//�����ǰ�·ݼ�1
				  System.out.println(mon);
				  JSONArray data=(JSONArray)temp.get("data");
				  for(int j=0;j<data.size();j++) {
					  if((int)data.get(j)==1) {
						  sum[mon]++;
					  }
				  }		
				  flag--;
				  }
		  }
		  result.put("yearly",sum);
		  code="200";
		  msg="success";
		  result.put("code",code);
		  result.put("msg",msg);
		  return result;
	  }
	  	  
	@RequestMapping({"/S4"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "收藏内容接口 ")
	public JSONObject collection(@ApiParam(required=true,value="收藏的内容，类型为String")@RequestBody String data) {
		JSONObject json = JSON.parseObject(data);
		Collection collec=new Collection();
		String msg=null,code=null;
		JSONObject result=new JSONObject();
		if(json.get("student_id")==null) {
			msg="student_id missing";
			code="-2";
			result.put("msg",msg);
			result.put("code", code);
			return result;
		}
		if(json.get("words")==null&&json.get("article")==null&&json.get("vedio")==null) {
			msg="parameter missing";
			code="-1";
			result.put("msg",msg);
			result.put("code", code);
			return result;
		}
//		JSONObject temp=new JSONObject();		
//		try {
//		    temp=json.getJSONObject(0);
//            JSONObject temp_2=json.getJSONObject(1);
//		}
//		catch (java.lang.IndexOutOfBoundsException e){
//			code="-1";
//			msg="parameter missing";
//			result.put("msg",msg);
//			result.put("code", code);
//			return result;
//		}
//		if(temp.get("student_id")==null||temp.get("student_id")=="") {
//			msg="student_id missing";
//			code="-2";
//			result.put("msg",msg);
//			result.put("code", code);
//			return result;
//		}
		collec.setStudentId((Integer)json.get("student_id"));
		if(json.get("words")!=null&&json.get("words")!="")
		    collec.setWords((String)json.get("words"));
		if(json.get("article")!=null&&json.get("article")!="")
			collec.setArticle((String)json.get("article"));
		if(json.get("vedio")!=null&&json.get("vedio")!="")
			collec.setVedio((String)json.get("vedio"));
//		for(int i=1;i<=json.size()-1;i++) {
//			try{
//				JSONObject temp_1=new JSONObject();
//				temp_1=json.getJSONObject(1);
//				if(temp_1.get("words")==null&&temp_1.get("article")==null&&temp_1.get("vedio")==null) {
//					code="-1";
//					msg="parameter missing";
//					result.put("msg",msg);
//					result.put("code", code);
//					return result;
//				}
//				if(temp_1.get("words")!=null&&temp_1.get("words")!="")
//				    collec.setWords(JSON.toJSONString(json.get(1)));
//				if(temp_1.get("article")!=null&&temp_1.get("article")!="")
//					collec.setArticle(JSON.toJSONString(json.get(1)));
//				if(temp_1.get("vedio")!=null&&temp_1.get("vedio")!="")
//					collec.setVedio(JSON.toJSONString(json.get(1)));
//			}
//			catch (java.lang.IndexOutOfBoundsException e){
//				System.out.print("indexout");
//			}

		
		int num=user_service.collection(collec);
		if(num==0) {
			code="-3";
			msg="insert error";
			result.put("msg",msg);
			result.put("code", code);
			return result;
		}
		code="200";
		msg="success";
		result.put("msg",msg);
		result.put("code", code);
		return result;
	}	
	
	
	@RequestMapping({"/S5"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "查询收藏内容接口 ")
	public JSONObject select_collection(@ApiParam(required=true,value="学生id")Integer student_id) {

		JSONObject result=new JSONObject();
		String msg=null;
		String code=null;
		if(student_id==null) {
			msg="student_id missing";
			code="-1";
			result.put("msg",msg);
			result.put("code",code);
			return result;
		}
		collection_1 data =user_service.select_collection(student_id);
        if(data==null) {
        	msg="collection null";
        	code="0";
        	result.put("msg",msg);
			result.put("code",code);
			return result;
        }
        msg="success";
    	code="200";
    	result.put("msg",msg);
		result.put("code",code);
		result.put("data",data);
		return result;
	}
	
	
    @RequestMapping({"/S6"})
    @ResponseBody
    @ApiOperation(httpMethod = "GET",value = "获取课程列表")
    public JSON get_cou_list(@ApiParam(required = true,value="学生学号，数据类型为String")String stu_num) {
    	JSONObject result = new JSONObject();
    	if(stu_num==null||stu_num.equals("")) {
    		result.put("code","-1");
    		result.put("msg","stu_num missing");
    		return result;
    	}
    	List<Course> cou_list = new ArrayList<Course>();
    	for(int i=0;i<cou_list.size();i++) {
    		cou_list.get(i).setImage("http://121.37.204.235:8080/AssistedLearningSystemResource/image/course/"+cou_list.get(i).getImage());
    	}
    	cou_list = user_service.get_cou_by_stuNum(stu_num);
    	result.put("code","200");
    	result.put("msg","success");
    	result.put("data",cou_list);
    	return result;
    	}
        
        
    @RequestMapping({"/S7"})
    @ResponseBody
    @ApiOperation(httpMethod = "GET",value = "获取课程详情")
    public JSON get_cou_dea(@ApiParam(required = true,value="课程id")Integer cou_id) {
    	JSONObject result = new JSONObject();
    	if(cou_id==null) {
    		result.put("code","-1");
    		result.put("msg","cou_id missing");
    		return result;
    	}
    	Course course = user_service.get_cou_by_couId(cou_id);
    	course.setImage("http://121.37.204.235:8080/AssistedLearningSystemResource/image/course/"+course.getImage());
    	result.put("code","200");
		result.put("msg","success");
		result.put("data",course);
		return result;
    }
    
    
    
  @RequestMapping({"/S8"})
  @ResponseBody
  @ApiOperation(httpMethod = "GET",value = "获取教师详情")
  public JSON get_tea_dea(@ApiParam(required = true,value="教师id")Integer tea_id) {
	  JSONObject result = new JSONObject();
  	if(tea_id==null) {
  		result.put("code","-1");
  		result.put("msg","tea_id missing");
  		return result;
  	}
  	Teacher teacher = user_service.get_tea_dea(tea_id);
  	teacher.setPassword(null);
  	teacher.setImage("http://121.37.204.235:8080/AssistedLearningSystemResource/image/teacher/"+teacher.getImage());
  	result.put("code","200");
	result.put("msg","success");
	result.put("data",teacher);
	return result;
  }
  
  
  @RequestMapping({"/S9"})
  @ResponseBody
  @ApiOperation(httpMethod = "GET",value = "获取课程视频")
  public JSON get_cou_vid(@ApiParam(required = true,value="cou_id")Integer cou_id) {
	  JSONObject result = new JSONObject();
	  if(cou_id==null) {
		result.put("code","-1");
	  	result.put("msg","cou_id missing");
	  	return result;
	  }
	  Course cou = user_service.get_cou_by_couId(cou_id);
	  String vid;	
	  try {
		  vid = cou.getVideo();
		  }
	  catch(Exception e) {
		  result.put("code","-2");
		  result.put("msg","cou_id invalid");
		  return result;
	  }
      String[] video ;
      try{
    	  video = vid.split(",");	  
      }
      catch(Exception e) {
		  result.put("code","-3");
		  result.put("msg","null video");
		  return result;
	  }
      for(int i=0;i<video.length;i++) {
    	  video[i]="http://121.37.204.235:8080/AssistedLearningSystemResource/video/course/"+video[i];
      }
      result.put("code","200");
	  result.put("msg","success");
	  result.put("data",video);
	  return result;
  }
  
  
  @RequestMapping({"/S10"})
  @ResponseBody
  @ApiOperation(httpMethod = "GET",value = "获取课程作业列表")
  public JSON get_cou_hom(@ApiParam(required = true,value="cou_id")Integer cou_id) {
	  JSONObject result = new JSONObject();
	  if(cou_id==null) {
		result.put("code","-1");
	  	result.put("msg","cou_id missing");
	  	return result;
	  }
	  List<Homework> homework = user_service.get_hom_by_couId(cou_id);
	  result.put("code","200");
	  result.put("msg","success");
	  result.put("data",homework);
	  return result;
}
  
  
	  @RequestMapping({"/S11"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "GET",value = "查看学生答题状态，若已回答，则返回答题情况（status为0为未修改，status为1为已批改），若未回答，则查看是否已经逾期，逾期则不能答题overdue参数为true，未逾期可答题overdue参数为false")
	  public JSON get_cou_sco(@ApiParam(required = true,value="课程id")Integer cou_id,
			  @ApiParam(required = true,value="学生id")Integer stu_id,
			  @ApiParam(required = true,value="作业id")Integer hom_id) {
		  JSONObject result = new JSONObject();
		  if(cou_id==null) {
			result.put("code","-1");
		  	result.put("msg","cou_id missing");
		  	return result;
		  }
		  if(stu_id==null) {
				result.put("code","-2");
			  	result.put("msg","stu_id missing");
			  	return result;
			  }
		  if(hom_id==null) {
				result.put("code","-3");
			  	result.put("msg","hom_id missing");
			  	return result;
			  }
          StudentHomework temp = new StudentHomework();
          temp.setHomeworkId(hom_id);
          temp.setStudentId(stu_id);
          StudentHomework answer_status = user_service.get_stu_hom(temp);
          if(answer_status!=null) {
        	  result.put("code","200");
			  result.put("msg","success");
			  result.put("data",answer_status);
			  return result;
          }
          else {
        	  Homework hom_wor = user_service.get_hom_by_homId(hom_id);
        	  Date date = new Date();
        	  if(date.before(hom_wor.getDeadline())) {
        		  result.put("code","200");
    			  result.put("msg","success");
    			  result.put("overdue",false);
    			  return result;
        	  }
        	  else {
        		  result.put("code","200");
    			  result.put("msg","success");
    			  result.put("overdue",true);
    			  return result;
        	  }
          }
	  }
	  
	  
	  @RequestMapping({"/S12"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "GET",value = "获取学生详细信息")
	  public JSON get_stu_dea(@ApiParam(required = true,value="学生号，数据类型为String")String stu_num) {
		  JSONObject result = new JSONObject();
		  if(stu_num==null) {
			result.put("code","-1");
		  	result.put("msg","stu_num missing");
		  	return result;
		  }
		  Student student = user_service.sel_stu_by_stu_number(stu_num);
		  if(student==null) {
			  result.put("code","-2");
			  result.put("msg","stu_num invalid");
			  return result;
		  }
		  student.setImage("http://121.37.204.235:8080/AssistedLearningSystemResource/image/student/"+student.getImage());
		  student.setPassword(null);
		  result.put("code","200");
		  result.put("msg","success");
		  result.put("data",student);
		  return result;
	  }
	  
	  
	  @RequestMapping({"/S13"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "POST",value = "学生回答问题,文本答案，图片答案，语音答案三者至少一种")
	  public JSON upl_ans(@ApiParam(required = true,value="学生id")Integer stu_id,
			  @ApiParam(required = true,value="作业id")Integer hom_id,
			  @ApiParam(required = true,value="课程id")Integer cou_id,
			  @ApiParam(required = false,value="文本答案，数据类型为string")String text,
			  @ApiParam(required = false,value="图片答案，数据类型为MultipartFile")MultipartFile image,
			  @ApiParam(required = false,value="音频答案，数据类型为MultipartFile")MultipartFile audio,
			  HttpServletRequest req) throws ParseException {
		  JSONObject result = new JSONObject();
		  if(stu_id==null) {
			result.put("code","-1");
		  	result.put("msg","stu_id missing");
		  	return result;
		  }
		  if(hom_id==null) {
				result.put("code","-2");
			  	result.put("msg","hom_id missing");
			  	return result;
			  }
		  if(cou_id==null) {
				result.put("code","-3");
			  	result.put("msg","cou_id missing");
			  	return result;
			  }
		  
		  if((text==null||text.equals(""))&&image==null&&audio==null) {
				result.put("code","-4");
			  	result.put("msg","answer missing");
			  	return result;
			  }
		  StudentHomework stu_hom = new StudentHomework();
		  Date tmp = new Date();
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String tmp_1 = sdf.format(tmp);
		  Date time = sdf.parse(tmp_1);
		  stu_hom.setAnswerTime(time);
		  stu_hom.setScore(-1);
		  stu_hom.setStatus(0);
		  stu_hom.setCourseId(cou_id);
		  stu_hom.setStudentId(stu_id);
		  stu_hom.setHomeworkId(hom_id);
		  if(text!=null&&!text.equals("")) 
			  stu_hom.setAnswer(text);
		  if(image!=null) {
			  try {
			  String file_name = String.valueOf(tmp.getTime());
			  File file = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/course/"+file_name+".jpg");
			  image.transferTo(file);//文件存服务器，路径存库
			  stu_hom.setImage(file_name+".jpg");  
			  }
			  catch(Exception e) {
				  e.printStackTrace();
				  result.put("code","-5");
				  result.put("msg","server error,please contact administrator");
				  return result;
			  }
		  }
		  if(audio!=null) {
			  try {
		      String file_name = String.valueOf(tmp.getTime());
			  File file = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/audio/homework/"+file_name+".mp3");
			  audio.transferTo(file);//文件存服务器，路径存库
			  stu_hom.setAudio(file_name+".mp3");  
			  }
			  catch(Exception e) {
				  e.printStackTrace();
				  result.put("code","-6");
				  result.put("msg","server error,please contact administrator");
				  return result;
			  }
		  }
		  int flag = user_service.upl_ans(stu_hom);
		  if(flag==0) {
			  result.put("code","-7");
			  result.put("msg","database error,please contact administrator");
			  return result;
		  }
		  result.put("code","200");
		  result.put("msg","success");
		  return result;
	  }
	  
	  
	  @RequestMapping({"/S14"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "GET",value = "根据课程号搜索课程")
	  public JSON get_cou_by_couNum(@ApiParam(required = true,value="课程号，数据类型为String")String cou_num) {
		  JSONObject result = new JSONObject();
		  if(cou_num==null||cou_num.equals("")) {
			result.put("code","-1");
		  	result.put("msg","cou_num missing");
		  	return result;
		  }
		  SearchCourse course = user_service.get_cou_Teaname_by_couNum(cou_num);
		  result.put("code","200");
		  result.put("msg","succsee");
		  result.put("data",course);
		  return result;
    }  
	  
	  @RequestMapping({"/S15"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "POST",value = "加入新课程")
	  public JSON join_cou(@ApiParam(required = true,value="课程号，数据类型为String")String cou_num,
			  @ApiParam(required = true,value="学生号，数据类型为String")String stu_num) throws ParseException {
		  JSONObject result = new JSONObject();
		  if(cou_num==null||cou_num.equals("")) {
			result.put("code","-1");
		  	result.put("msg","cou_num missing");
		  	return result;
		  }
		  if(stu_num==null||stu_num.equals("")) {
				result.put("code","-2");
			  	result.put("msg","stu_num missing");
			  	return result;
			  }
		  Date date = new Date();
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		  String tmp = sdf.format(date);
		  Date time = sdf.parse(tmp);
		  StudentCourse stu_cou = new StudentCourse();
		  stu_cou.setTime(time);
		  stu_cou.setCourseNumber(cou_num);
		  stu_cou.setStudentNumber(stu_num);
		  int flag = user_service.join_cou(stu_cou);
		  if(flag==0) {
			  result.put("code","-3");
			  result.put("msg","database error,please contact administrator");
			  return result;
		  }
		  result.put("code","200");
		  result.put("msg","success");
		  return result;
	  }
	  
	  @RequestMapping({"/S16"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "GET",value = "获取聊天列表")
	  public JSON get_chat_list(@ApiParam(required = true,value="学生号，数据类型为String")String stu_num)  {
		  JSONObject result = new JSONObject();
		  if(stu_num==null||stu_num.equals("")) {
			result.put("code","-1");
		  	result.put("msg","stu_num missing");
		  	return result;
		  }
		  List<ChatList> chat_list = user_service.get_chat_list(stu_num);
		  result.put("code","200");
		  result.put("msg","success");
		  result.put("data",chat_list);
		  return result;
	  }
	  
	  
	  @RequestMapping({"/S17"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "GET",value = "获取聊天消息")
	  public JSON get_mes(@ApiParam(required = true,value="学生id")Integer stu_id,
			  @ApiParam(required = true,value="课程id")Integer cou_id)  {
		  JSONObject result = new JSONObject();
		  if(stu_id==null) {
			result.put("code","-1");
		  	result.put("msg","stu_id missing");
		  	return result;
		  }
		  if(cou_id==null) {
				result.put("code","-2");
			  	result.put("msg","cou_id missing");
			  	return result;
			  }
		  Message mes = new Message();
		  mes.setCourseId(cou_id);
		  mes.setStudentId(stu_id);
		  List<Message> message = user_service.get_mes(mes);
		  result.put("code","200");
		  result.put("msg","success");
		  result.put("data",message);
		  return result;
	  }
	  
	  
	  
	  @RequestMapping({"/S18"})
	  @ResponseBody
	  @ApiOperation(httpMethod = "POST",value = "发送聊天消息")
	  public JSON send_mes(@ApiParam(required = false,value="文本消息")String text,
			  @ApiParam(required = false,value="语音信息,数据类型为MultipartFile")MultipartFile audio,
			  @ApiParam(required = true,value="学生id")Integer stu_id,
			  @ApiParam(required = true,value="课程id")Integer cou_id,
			  @ApiParam(required = true,value="教师id")Integer tea_id) throws ParseException  {
		  JSONObject result = new JSONObject();
		  if(stu_id==null) {
			  result.put("code","-1");
			  result.put("msg","stu_id missing");
			  return result;
		  }
		  if(cou_id==null) {
			  result.put("code","-2");
			  result.put("msg","cou_id missing");
			  return result;
		  }
		  if(tea_id==null) {
			  result.put("code","-3");
			  result.put("msg","tea_id missing");
			  return result;
		  }
		  if((text==null||text.equals(""))&&audio==null) {
			result.put("code","-4");
		  	result.put("msg","message missing");
		  	return result;
		  }
		  Message mes = new Message();
		  Date date = new Date();
		  SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		  String tmp = sdf.format(date);
		  Date time = sdf.parse(tmp);
		  mes.setQuestionTime(time);
		  if(text!=null&&!text.equals("")) 
			 mes.setQuestion(text);
		  if(audio!=null) {
			  String file_name = String.valueOf(date.getTime());
			  try {
			  File file = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/audio/message/"+file_name+".mp3");
			  audio.transferTo(file);//音频存服务器
			  mes.setQueAudio(file_name+".mp3");//路径存库
			  }
			  catch(Exception e) {
				  e.printStackTrace();
				  result.put("code","-5");
				  result.put("msg","server error");
				  return result;
			  }
		  }
		  mes.setCourseId(cou_id);
		  mes.setTeacherId(tea_id);
		  mes.setStudentId(stu_id);
		  mes.setStatus(0);
		  int flag = user_service.send_mes(mes);
		  if(flag==0) {
			  result.put("code","-6");
			  result.put("msg","database error");
			  return result;
		  }
		  Teacher tea = user_service.get_tea_dea(tea_id);
		  String tea_mail = tea.getEmail();
		  mail_sender.sendEmail(tea_mail, "云印提醒", "老师您好，有学生给您发送了新的提问消息，请及时查看");
		  result.put("code","200");
		  result.put("msg","success");
		  return result;
	  }
	  
}

  
	  
	  
	  
	  
	  
	  
//	@RequestMapping({"/text"})
//	@ResponseBody
//	public void proc() {//文章格式处理
//	    List<article_1> text = user_service.get_text();
//	    for(int i=0;i<text.size();i++) {
//		    String article = text.get(i).getText();
//		    String temp = article.replaceAll("\n"," ");
//		    text.get(i).setText(temp);
//	}
//	    for(int j=0;j<text.size();j++) {
//		    System.out.println(text.get(j).getText());
//		    System.out.println(text.get(j).getArticleId());
//		    user_service.update_text(text.get(j));
//	}
//	}



