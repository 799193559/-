package golden.controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

import golden.model.Article;
import golden.model.Course;
import golden.model.Homework;
import golden.model.Message;
import golden.model.Resource;
import golden.model.Student;
import golden.model.StudentCourse;
import golden.model.StudentHomework;
import golden.model.Teacher;
import golden.model.Video;
import golden.service.userservice;
import golden.tempmodel.Art_page;
import golden.tempmodel.StudentManage;
import golden.tempmodel.Tea_image;
import golden.tempmodel.Teacher_1;
import golden.tempmodel.Vid_page;
import golden.token.Token;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Controller
@RequestMapping({"/api/tea"})
@Api(tags = "教师端(参数类型除特殊指明外，均为String),后期需增加作业发布，作业批改模块")
public class tea_controller {
	@Autowired
    userservice user_service;
	public String ip = "121.37.204.235";
	@RequestMapping({"/T1"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "教师登录接口 ")
	public JSON login(@ApiParam(required=true,name="tea_num",value="教师号")String tea_num,@ApiParam(required=true,name="pwd",value="登录密码")String pwd) {
		JSONObject result = new JSONObject();
		String code,msg;
//		System.out.println(pwd);
		if(tea_num==null||tea_num.equals("")) {
			code="-1";
			msg="tea_num missing";
			result.put("code",code);
			result.put("msg", msg);
			return result;
		}
		else if(pwd==null||pwd.equals("")) {
			code="-2";
			msg="pwd missing";
			result.put("code",code);
			result.put("msg", msg);
			return result;
		}
		Teacher tea = user_service.sel_tea_by_tea_number(tea_num);
		String pas_wor;
		try	{
		     pas_wor = tea.getPassword();
		}
		catch(Exception e) {
			e.printStackTrace();
			code="-3";
			msg="tea_num invalid";
			result.put("code",code);
			result.put("msg", msg);
			return result;
		}
		if(!pas_wor.equals(pwd)) {
			code="-4";
			msg="pwd error";
			result.put("code",code);
			result.put("msg", msg);
			return result;
		}
		Token tok = new Token();
		String secret = tok.getToken(false,tea_num,pwd);
		String token = secret;
		code="200";
		msg="success";
		result.put("code",code);
		result.put("msg", msg);
		result.put("token", token);
		return result;
	}
	
	
	@RequestMapping({"/T2"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "教师注册接口")
	public JSON register(@ApiParam(required=true,name="tea_num",value="教师号")String tea_num,
			@ApiParam(required=true,name="tea_name",value="教师名称")String tea_name,
			@ApiParam(required=true,name="email",value="教师邮箱号")String email,
			@ApiParam(required=true,name="pho_num",value="教师手机号")String pho_num,
			@ApiParam(required=true,name="pwd_1",value="教师登录密码")String pwd_1,
			@ApiParam(required=true,name="pwd_2",value="教师再次输入登录密码")String pwd_2){
	    JSONObject result = new JSONObject();
	    String code,msg;
		if(tea_num==null||tea_num.equals("")) {
	    	code="-1";
	    	msg="tea_num missing";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }
		if(tea_name==null||tea_name.equals("")) {
	    	code="-2";
	    	msg="tea_name missing";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }
		if(email==null||email.equals("")) {
	    	code="-3";
	    	msg="email missing";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }if(pho_num==null||pho_num.equals("")) {
	    	code="-4";
	    	msg="pho_num missing";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }if(pwd_1==null||pwd_1.equals("")) {
	    	code="-5";
	    	msg="pwd_1 missing";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }if(pwd_2==null||pwd_2.equals("")) {
	    	code="-6";
	    	msg="pwd_2 missing";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }if(!pwd_1.equals(pwd_2)){
	    	code="-7";
	    	msg="pwd_1 != pwd_2";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }
	    if(user_service.sel_tea_by_tea_number(tea_num)!=null) {
	    	code="-8";
	    	msg="tea_num exist";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }
	    Teacher tea = new Teacher();
	    tea.setEmail(email);
	    tea.setName(tea_name);
	    tea.setPassword(pwd_1);
	    tea.setPhoneNumber(pho_num);
	    tea.setTeacherNumber(tea_num);
	    tea.setImage("default.jpg");
	    int flag = user_service.add_tea(tea);
	    if(flag==0) {
	    	code="-9";
	    	msg="database error,please contact administrator";
	    	result.put("code", code);
	    	result.put("msg",msg);
	    	return result;
	    }
	    code="200";
	    msg="success";
	    result.put("code", code);
    	result.put("msg",msg);
    	return result;
	    }
	
	
	@RequestMapping({"/T3"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "获取教师详细信息")
	public JSON get_tea_dea(@ApiParam(required = true,name = "tea_num",value = "教师号")String tea_num) throws UnknownHostException {
		String code,msg;
		JSONObject result = new JSONObject(); 
		if(tea_num==null||tea_num.equals("")) {
			code="-1";
			msg="tea_num missing";
			result.put("code",code);
			result.put("msg", msg);
			return result;
		}
		Teacher_1 tea_1 = new Teacher_1();
		Teacher tea = user_service.sel_tea_by_tea_number(tea_num);
		try {
			tea_1.setTeacherId(tea.getTeacherId());
			tea_1.setEmail(tea.getEmail());
			tea_1.setImage("http://"+ip+":8080/AssistedLearningSystemResource/image/teacher/"+tea.getImage());//到时记得在服务器webapps下创建文件夹image/AssistedLearningSystem
			tea_1.setName(tea.getName());
			tea_1.setPhoneNumber(tea.getPhoneNumber());
			tea_1.setTeacherNumber(tea.getTeacherNumber());
		}
		catch(Exception e) {
			code="-2";
			msg="tea_num invalid";
			result.put("code",code);
			result.put("msg", msg);
			return result;
		}
		code="200";
		msg="success";
		result.put("code",code);
		result.put("msg", msg);
		result.put("data",tea_1);
		return result;
	}
	
	
	@RequestMapping({"/T4"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "编辑教师详细信息")
	public JSON edit_dea(
			@ApiParam(required=true,name="tea_num",value="教师号")String tea_num,
			@ApiParam(required=true,name="tea_name",value="教师姓名")String tea_name,
			@ApiParam(required=true,name="pho_num",value="教师手机号码")String pho_num,
			@ApiParam(required=true,name="email",value="教师邮箱")String email) {
		JSONObject result = new JSONObject(); 
		if(tea_name==null||tea_name.equals("")) {
			result.put("code", "-1");
			result.put("msg","tea_name missing");
			return result;
		}
		if(pho_num==null||pho_num.equals("")) {
			result.put("code", "-2");
			result.put("msg","pho_num missing");
			return result;
		}
		if(email==null||email.equals("")) {
			result.put("code", "-3");
			result.put("msg","email missing");
			return result;
		}
		Teacher tea = new Teacher();
		tea.setTeacherNumber(tea_num);
		tea.setEmail(email);
		tea.setName(tea_name);
		tea.setPhoneNumber(pho_num);
		int flag = user_service.upd_tea(tea);
		if(flag == 0) {
			result.put("code", "-4");
			result.put("msg","database error,please contact administrator");
			return result;
		}
		else {
			result.put("code", "200");
			result.put("msg","success");
			return result;
		}			
	}
	
	
	@RequestMapping({"/T5"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "修改教师登录密码")
	public JSON upd_pwd(
			@ApiParam(required = true,name = "tea_num",value = "教师号")String tea_num,
			@ApiParam(required = true,name = "pwd_1",value = "旧密码")String pwd_1,
			@ApiParam(required = true,name = "pwd_2",value = "新密码")String pwd_2,
			@ApiParam(required = true,name = "pwd_3",value = "确认新密码")String pwd_3){
		JSONObject result = new JSONObject();
		if(pwd_1==null||pwd_1.equals("")) {
			result.put("code","-1");
			result.put("msg","pwd_1 missing");
			return result;
		}
		if(pwd_2==null||pwd_2.equals("")) {
			result.put("code","-2");
			result.put("msg","pwd_2 missing");
			return result;
		}
		if(pwd_3==null||pwd_3.equals("")) {
			result.put("code","-3");
			result.put("msg","pwd_3 missing");
			return result;
		}
		if(!pwd_2.equals(pwd_3)) {
			result.put("code","-4");
			result.put("msg","pwd_2 != pwd_3");
			return result;
		}
		if(tea_num==null||tea_num.equals("")) {
			result.put("code","-5");
			result.put("msg","tea_num missing");
			return result;
		}
		Teacher tea = new Teacher();
		String pwd;
		tea = user_service.sel_tea_by_tea_number(tea_num);
		try {
	    pwd=tea.getPassword();
		}
		catch (Exception e){
			result.put("code","-6");
			result.put("msg","tea_num invalid");
			return result;
		}
	    if(!pwd.equals(pwd_1)) {
	    	result.put("code","-7");
			result.put("msg","pwd_1 error");
			return result;
	    }
		tea.setPassword(pwd_2);
		int flag=user_service.upd_pwd(tea);
		if(flag==0) {
			result.put("code","-8");
			result.put("msg","database error,please contact administrator");
			return result;	
		}
		else {
			result.put("code","200");
			result.put("msg","success");
			return result;	
		}
	}
	
	
	@RequestMapping({"/T6"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "修改教师头像,数据格式为form表单")
	public JSON upd_ima(
			@ApiParam(required = true,name = "tea_num",value = "教师号")String tea_num,
			@ApiParam(required = true,name = "image_file",value = "图片文件，参数类型为MultipartFile")MultipartFile image_file){
		JSONObject result = new JSONObject();
		if(tea_num==null||tea_num.equals("")) {
			result.put("code","-1");
			result.put("msg","tea_num missing");
			return result;
		}
		if(image_file==null||image_file.isEmpty()) {
			result.put("code","-2");
			result.put("msg","image_file missing");
			return result;
		}
		Teacher tea = user_service.sel_tea_by_tea_number(tea_num);
		String old_name;
		try {
            old_name = tea.getImage();
		}
		catch(Exception e) {
			result.put("code","-3");
			result.put("msg","tea_num invalid");
			return result;
		}
//		System.out.println(old_name);
		File old_image = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/teacher/"+old_name);
		if(old_image.exists()&&(!old_name.equals("default.jpg"))) {
			old_image.delete();
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		String file_name = sdf.format(date);
		Tea_image tea_image = new Tea_image();
		tea_image.setImage(file_name+".jpg");
		tea_image.setTeacherNumber(tea_num);
		int flag = user_service.upd_ima(tea_image);
		if(flag==0) {
			result.put("code","-4");
			result.put("msg","database error,please contact administrator");
			return result;
		}
		try {
		File file = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/teacher/"+file_name+".jpg");
		image_file.transferTo(file);
		}
		catch (Exception e) {
			result.put("code","-5");
			result.put("msg","server error,please contact administrator");
			return result;
		}
		result.put("code","200");
		result.put("msg","success");
		return result;
	}
	
	
	@RequestMapping({"/T7"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "根据条件获取相应课程，若无条件则直接分页返回所有课程")
	public JSON get_cou_by_con(
			@ApiParam(required = true,name = "tea_id",value = "教师id,参数类型为Integer")Integer tea_id,
			@ApiParam(required = false,name = "cou_sta",value = "课程状态码数组，0未开始，1进行中，2已结束，参数类型为Integer[]")Integer []  cou_sta,
			@ApiParam(required = false,name = "cou_cam",value = "校区状态码数组，0为北校，1为南校，参数类型为Integer[]")Integer[] cou_cam,
			@ApiParam(required=false,name="cou_time",value="上课时间码数组，1-7代表星期几，参数类型为Integer[]")Integer[] cou_time,
			@ApiParam(required = true,name="size",value = "每页显示的条数,参数类型为Integer")Integer size,
			@ApiParam(required = true,name="offset",value = "页码,参数类型为Integer")Integer offset) throws UnknownHostException {
		JSONObject result = new JSONObject();
		if(tea_id==null) {
			result.put("code","-1");
			result.put("msg","tea_id missing");
			return result;
		}		
		if(size==null) {
			result.put("code","-2");
			result.put("msg","size missing");
			return result;
		}
		if(offset==null) {
			result.put("code","-3");
			result.put("msg","offset missing");
			return result;
		}
		List<Course> course = user_service.get_cou(tea_id);
		if(course.size()==0) {
			result.put("code","-4");
			result.put("msg","the teacher has no any course or tea_id invalid");
			return result;
		}
		if(cou_time!=null&&cou_time.length!=0) {
			if(cou_time.length>7) {
				result.put("code","-5");
				result.put("msg","cou_time error");
				return result;
			}
			for(int i=0;i<cou_time.length;i++) {
				try {
				if(cou_time[i]>7||cou_time[i]<0) {
					result.put("code","-5");
					result.put("msg","cou_time error");
					return result;
				}
				}
				catch (Exception e) {
					result.put("code","-5");
					result.put("msg","cou_time error");
					return result;
				}
			}
			for(int i=0;i<course.size();i++) {	
				boolean temp=false;
				for(int j=0;j<cou_time.length;j++) {
					if(course.get(i).getWeekFlag().equals(cou_time[j])) {
						temp=true;
						break;
					}	
				}
				if(!temp) {
					course.remove(i);
					i--;
				}
			}
		}
		if(cou_cam!=null&&cou_cam.length!=0) {
			if(cou_cam.length>2) {
				result.put("code","-6");
				result.put("msg","cou_cam error");
				return result;
			}
			for(int i=0;i<cou_cam.length;i++) {
				try {
				if(cou_cam[i]>1||cou_cam[i]<0) {
					result.put("code","-6");
					result.put("msg","cou_cam error");
					return result;
				}
				}
				catch (Exception e) {
					result.put("code","-6");
					result.put("msg","cou_cam error");
					return result;
				}
			}
			for(int i=0;i<course.size();i++) {
				boolean temp=false;
				for(int j=0;j<cou_cam.length;j++) {
					if(course.get(i).getArea().equals(cou_cam[j])) {
						temp=true;
						break;
					}	
				}
				if(!temp) {
					course.remove(i);
					i--;
				}
			}
		}
		if(cou_sta!=null&&cou_sta.length!=0) {
			if(cou_sta.length>3) {
				result.put("code","-7");
				result.put("msg","cou_sta error");
				return result;
			}
			for(int i=0;i<cou_sta.length;i++) {
				try {//预防传的数组格式错误
				if(cou_sta[i]>2||cou_sta[i]<0) {
					result.put("code","-7");
					result.put("msg","cou_sta error");
					return result;
				}
				}
				catch (Exception e) {
					result.put("code","-7");
					result.put("msg","cou_sta error");
					return result;
				}
			}
			for(int i=0;i<course.size();i++) {
				boolean temp = false;
				for(int j=0;j<cou_sta.length;j++) {
					if(course.get(i).getStatus().equals(cou_sta[j])) {
						temp=true;
						break;
					}	
				}
				if(!temp) {
					course.remove(i);
					i--;
				}
			}
		}	
		int total = course.size();
		List<Course> data = new ArrayList<>();
		for(int i=offset*size-size;i<offset*size;i++){
			try {
				course.get(i).setImage("http://"+ip+":8080/AssistedLearningSystemResource/image/course/"+course.get(i).getImage());
				course.get(i).setVideo("http://"+ip+":8080/AssistedLearningSystemResource/video/course/"+course.get(i).getVideo());
				data.add(course.get(i));
			}
			catch(Exception e) {
				result.put("code","200");
				result.put("msg","success");
				result.put("total",total);
				result.put("data",data);
				return result;
			}
		}
		result.put("code","200");
		result.put("msg","success");
		result.put("total",total);
		result.put("data",data);
		return result;
	}
	
	@RequestMapping({"/T8"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "根据课程num获取相应课程，此接口可用于根据课程number搜索课程，也可用于获取课程详情")
	public JSON get_cou_by_couNum(
			@ApiParam(required=true,name="cou_num",value="课程number")String cou_num) throws UnknownHostException {
		JSONObject result = new JSONObject();
		if(cou_num == null||cou_num.equals("")) {
			result.put("code","-1");
			result.put("msg","cou_num missing");
			return result;
		}
        Course cou = user_service.get_cou_by_couNum(cou_num);
        if(cou==null) {
        result.put("code","-2");
		result.put("msg","cou_num invalid");
		return result;
        }
        else {
            cou.setImage("http://"+ip+":8080/AssistedLearningSystemResource/image/course/"+cou.getImage());
    		cou.setVideo("http://"+ip+":8080/AssistedLearningSystemResource/video/course/"+cou.getVideo());
        	result.put("code","200");
    		result.put("msg","success");
    		result.put("data",cou);
    		return result;
        }
	}
	

	
	@RequestMapping({"/T9"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "编辑更新课程详情，数据格式为form表单")
	public JSON upd_couDet_by_couId (//后期视频模块更改为可上传新视频，新视频不覆盖旧视频，追加方式存库
			@ApiParam(required=true,name="cou_id",value="课程Id，参数类型为Integer")Integer cou_id,
			@ApiParam(required=true,name="cou_num",value="课程number")String cou_num,
			@ApiParam(required=true,name="cou_name",value="课程名称")String cou_name,
			@ApiParam(required=true,name="cou_cam",value="课程开设校区，参数类型为Integer，0为北校，1为南校")Integer cou_cam,
			@ApiParam(required=true,name="cou_week",value="课程上课时间，参数类型为Integer，1-7代表星期几")Integer cou_week,
			@ApiParam(required=true,name="cou_day",value="课程上课时间段，参数类型为Integer，参数类型为Integer,0为早上，1为下午")Integer cou_day,
			@ApiParam(required=true,name="cou_beg",value="课程开始节数,参数类型为Integer,1-14分别为第1到第14节")Integer cou_beg,
			@ApiParam(required=true,name="cou_end",value="课程结束节数,参数类型为Integer,1-14分别为第1到第14节")Integer cou_end,
			@ApiParam(required=true,name="cou_max",value="课程最多上课人数，参数类型为Integer")Integer cou_max,
			@ApiParam(required=true,name="cou_sta",value="课程状态码，参数类型为Integer，0未开始，1进行中，2已结束")Integer cou_sta,
			@ApiParam(required=true,name="cou_intr",value="课程介绍")String cou_intr,
			@ApiParam(required=true,name="cou_out",value="课程大纲")String cou_out,
			@ApiParam(required=false,name="cou_ima",value="可选参数，课程图片文件，参数类型为MultipartFile")MultipartFile cou_ima,
			@ApiParam(required=false,name="cou_vid",value="可选参数，课程视频文件，参数类型为MultipartFile")MultipartFile cou_vid) throws MultipartException {
		JSONObject result  = new JSONObject();
		if(cou_id == null) {
			result.put("code","0");
			result.put("msg","cou_id missing");
			return result;
		}
		if(cou_num == null||cou_num.equals("")) {
			result.put("code","-1");
			result.put("msg","cou_num missing");
			return result;
		}
		if(cou_name == null||cou_name.equals("")) {
			result.put("code","-2");
			result.put("msg","cou_name missing");
			return result;
		}
		if(cou_cam == null) {
			result.put("code","-3");
			result.put("msg","cou_cam missing");
			return result;
		}
		if(cou_cam>1||cou_cam<0) {
			result.put("code","-3");
			result.put("msg","cou_cam error");
			return result;
		}
		if(cou_week == null) {
			result.put("code","-4");
			result.put("msg","cou_week missing");
			return result;
		}
		if(cou_week>7||cou_week<1) {
			result.put("code","-4");
			result.put("msg","cou_week error");
			return result;
		}
		if(cou_day == null) {
			result.put("code","-5");
			result.put("msg","cou_day missing");
			return result;
		}
		if(cou_day >1||cou_day<0) {
			result.put("code","-5");
			result.put("msg","cou_day error");
			return result;
		}
		if(cou_beg == null) {
			result.put("code","-6");
			result.put("msg","cou_beg missing");
			return result;
		}
		if(cou_beg>14||cou_beg<1) {
			result.put("code","-6");
			result.put("msg","cou_beg error");
			return result;
		}
		if(cou_end == null) {
			result.put("code","-7");
			result.put("msg","cou_end missing");
			return result;
		}
		if(cou_end>14||cou_end<1) {
			result.put("code","-7");
			result.put("msg","cou_end error");
			return result;
		}
		if(cou_max == null) {
			result.put("code","-8");
			result.put("msg","cou_max missing");
			return result;
		}
		if(cou_max <1) {
			result.put("code","-8");
			result.put("msg","cou_max error");
			return result;
		}
		if(cou_sta == null) {
			result.put("code","-9");
			result.put("msg","cou_sta missing");
			return result;
		}
		if(cou_sta<0||cou_sta>2) {
			result.put("code","-9");
			result.put("msg","cou_sta error");
			return result;
		}
		if(cou_intr == null||cou_intr.equals("")) {
			result.put("code","-10");
			result.put("msg","cou_intr missing");
			return result;
		}
		if(cou_out == null||cou_out.equals("")) {
			result.put("code","-11");
			result.put("msg","cou_out missing");
			return result;
		}
	
		Course cou = new Course();
		cou.setCourseId(cou_id);
		cou.setArea(cou_cam);
		cou.setBeginCourse(cou_beg);
		cou.setDayFlag(cou_day);
		cou.setEndCourse(cou_end);
		cou.setIntroduction(cou_intr);
		cou.setMaxNumber(cou_max);
		cou.setName(cou_name);
		cou.setOutline(cou_out);
		cou.setStatus(cou_sta);
		cou.setWeekFlag(cou_week);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		String file_name = sdf.format(date);
		File image,video;
		Course old_cou = new Course() ;
		if(cou_ima != null||cou_vid != null) {
			old_cou = user_service.get_cou_by_couNum(cou_num);
		}
		if(cou_ima != null) {
			String old_name;
			try {
	            old_name = old_cou.getImage();
			}
			catch(Exception e) {
				result.put("code","-12");
				result.put("msg","cou_num invalid");
				return result;
			}
			File old_image = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/course/"+old_name);
			if(old_image.exists()&&(!old_name.equals("default.jpg"))) {
				old_image.delete();
			}
			try {
				image = new File ("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/course/"+file_name+".jpg");
				cou_ima.transferTo(image);//图片存服务器
				}
				catch (Exception e) {
					result.put("code","-13");
					result.put("msg","server error,please contact administrator");
					return result;
				}			
			cou.setImage(file_name+".jpg");//图片名存库
		}
		else {
			cou.setImage("default.jpg");
		}
		if(cou_vid != null) {
			String old_name;
			try {
	            old_name = old_cou.getVideo();
			}
			catch(Exception e) {
				result.put("code","-12");
				result.put("msg","cou_num invalid");
				return result;
			}
			File old_video = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/course/"+old_name);
			if(old_video.exists()&&(!old_name.equals("default.mp4"))) {
				old_video.delete();
			}
			try {
				video = new File ("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/video/course/"+file_name+".mp4");
				cou_vid.transferTo(video);//视频存服务器
				}
				catch (Exception e) {
					result.put("code","-13");
					result.put("msg","server error,please contact administrator");
					return result;
				}			
			cou.setVideo(file_name+".mp4");//视频名存库
		}
		else {
			cou.setVideo("default.mp4");//视频名存库
		}
		int flag = user_service.upd_cou_by_id(cou);
		if(flag==0) {
			result.put("code","-14");
			result.put("msg","cou_id invalid or database error,please check cou_id or contact administrator");
			return result;
		}
		else{
			result.put("code","200");
			result.put("msg","success");
			return result;
		}
	}
	
	@RequestMapping({"/T10"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "开设新课程,数据格式为form表单")
	public JSON cre_new_cou (
			@ApiParam(required=true,name="tea_id",value="上课教师id，参数类型为Integer")Integer tea_id,
			@ApiParam(required=true,name="cou_name",value="课程名称")String cou_name,
			@ApiParam(required=true,name="cou_cam",value="课程开设校区，参数类型为Integer，0为北校，1为南校")Integer cou_cam,
			@ApiParam(required=true,name="cou_week",value="课程上课时间，参数类型为Integer，1-7代表星期几")Integer cou_week,
			@ApiParam(required=true,name="cou_day",value="课程上课时间段，参数类型为Integer，参数类型为Integer,0为早上，1为下午")Integer cou_day,
			@ApiParam(required=true,name="cou_beg",value="课程开始节数,参数类型为Integer,1-14分别为第1到第14节")Integer cou_beg,
			@ApiParam(required=true,name="cou_end",value="课程结束节数,参数类型为Integer,1-14分别为第1到第14节")Integer cou_end,
			@ApiParam(required=true,name="cou_max",value="课程最多上课人数，参数类型为Integer")Integer cou_max,
			@ApiParam(required=true,name="cou_sta",value="课程状态码，参数类型为Integer，0未开始，1进行中，2已结束")Integer cou_sta,
			@ApiParam(required=true,name="cou_intr",value="课程介绍")String cou_intr,
			@ApiParam(required=true,name="cou_out",value="课程大纲")String cou_out,
			@ApiParam(required=false,name="cou_ima",value="可选参数，课程图片文件，参数类型为MultipartFile")MultipartFile cou_ima,
			@ApiParam(required=false,name="cou_vid",value="可选参数，课程视频文件，参数类型为MultipartFile")MultipartFile cou_vid) throws MultipartException {
		JSONObject result  = new JSONObject();
		if(cou_name == null||cou_name.equals("")) {
			result.put("code","-1");
			result.put("msg","cou_name missing");
			return result;
		}
		if(cou_cam == null) {
			result.put("code","-2");
			result.put("msg","cou_cam missing");
			return result;
		}
		if(cou_cam>1||cou_cam<0) {
			result.put("code","-2");
			result.put("msg","cou_cam error");
			return result;
		}
		if(cou_week == null) {
			result.put("code","-3");
			result.put("msg","cou_week missing");
			return result;
		}
		if(cou_week>7||cou_week<1) {
			result.put("code","-3");
			result.put("msg","cou_week error");
			return result;
		}
		if(cou_day == null) {
			result.put("code","-4");
			result.put("msg","cou_day missing");
			return result;
		}
		if(cou_day >1||cou_day<0) {
			result.put("code","-4");
			result.put("msg","cou_day error");
			return result;
		}
		if(cou_beg == null) {
			result.put("code","-5");
			result.put("msg","cou_beg missing");
			return result;
		}
		if(cou_beg>14||cou_beg<1) {
			result.put("code","-5");
			result.put("msg","cou_beg error");
			return result;
		}
		if(cou_end == null) {
			result.put("code","-6");
			result.put("msg","cou_end missing");
			return result;
		}
		if(cou_end>14||cou_end<1) {
			result.put("code","-6");
			result.put("msg","cou_end error");
			return result;
		}
		if(cou_max == null) {
			result.put("code","-7");
			result.put("msg","cou_max missing");
			return result;
		}
		if(cou_max <1) {
			result.put("code","-7");
			result.put("msg","cou_max error");
			return result;
		}
		if(cou_sta == null) {
			result.put("code","-8");
			result.put("msg","cou_sta missing");
			return result;
		}
		if(cou_sta<0||cou_sta>2) {
			result.put("code","-8");
			result.put("msg","cou_sta error");
			return result;
		}
		if(cou_intr == null||cou_intr.equals("")) {
			result.put("code","-9");
			result.put("msg","cou_intr missing");
			return result;
		}
		if(cou_out == null||cou_out.equals("")) {
			result.put("code","-9");
			result.put("msg","cou_out missing");
			return result;
		}
		if(tea_id == null) {
			result.put("code","-10");
			result.put("msg","tea_id missing");
			return result;
		}
	    String old_cou_num  = user_service.get_last_cou_rec();
	    Integer temp = Integer.valueOf(old_cou_num)+1;
	    String new_cou_num = temp.toString();
		Course cou = new Course();
		cou.setTeacherId(tea_id);
		cou.setCourseNumber(new_cou_num);
		cou.setArea(cou_cam);
		cou.setBeginCourse(cou_beg);
		cou.setDayFlag(cou_day);
		cou.setEndCourse(cou_end);
		cou.setIntroduction(cou_intr);
		cou.setMaxNumber(cou_max);
		cou.setName(cou_name);
		cou.setOutline(cou_out);
		cou.setStatus(cou_sta);
		cou.setWeekFlag(cou_week);
		cou.setCurrentNumber(0);
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		String file_name = sdf.format(date);
		File image,video;
		if(cou_ima!=null&&!cou_ima.isEmpty()) {
			try {
	        image = new File ("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/course/"+file_name+".jpg");
			cou_ima.transferTo(image);//图片存服务器
			}
		catch (Exception e) {
					result.put("code","-11");
					result.put("msg","server error,please contact administrator");
					return result;
				}
			cou.setImage(file_name+".jpg");//图片名存库
	}
		else {
			cou.setImage("default.jpg");
		}
		if(cou_vid != null&&!cou_vid.isEmpty()) {
			try {
				video = new File ("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/video/course/"+file_name+".mp4");
				cou_vid.transferTo(video);//视频存服务器
				}
				catch (Exception e) {
					result.put("code","-11");
					result.put("msg","server error,please contact administrator");
					return result;
				}			
			cou.setVideo(file_name+".mp4");//视频名存库
		}
		else {
			cou.setVideo("default.mp4");//视频名存库
		}
		int flag = user_service.cre_new_cou(cou);
		if(flag==0) {
			result.put("code","-12");
			result.put("msg","database error,please check cou_id or contact administrator");
			return result;
		}
		else{
			result.put("code","200");
			result.put("msg","success");
			return result;
		}
	}
	
	
	@RequestMapping({"/T11"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "学生管理,仅传课程号和课程id时返回该教师所有学生，传校区参数和学生学号或姓名参数时则按条件搜索,学生名和学生学号不能同时传")
	public JSON stu_man(//后期可用联表查询提升性能
			@ApiParam(required=true,name="cou_num",value="课程号")String cou_num,
			@ApiParam(required=true,name="cou_id",value="课程id，参数类型为Integer")Integer cou_id,
			@ApiParam(required=true,name="size",value="一页显示的学生数，参数类型为Integer")Integer size,
			@ApiParam(required=true,name="offset",value="页码，参数类型为Integer")Integer offset,
			@ApiParam(required=false,name="stu_cam",value="学生校区，参数类型为Integer[]")Integer [] stu_cam,
			@ApiParam(required=false,name="stu_num",value="学生学号，参数类型为String")String stu_num,
			@ApiParam(required=false,name="stu_name",value="学生名，进行模糊匹配，参数类型为String")String stu_name) {
		JSONObject result = new JSONObject();
		if(cou_num == null||cou_num.equals("")) {
			result.put("code","-1");
			result.put("msg","cou_num missing");
			return result;
		}
		if(size == null) {
			result.put("code","-2");
			result.put("msg","size missing");
			return result;
		}
		if(size<1) {
			result.put("code","-2");
			result.put("msg","size error");
			return result;
		}
		if(offset == null) {
			result.put("code","-3");
			result.put("msg","offset missing");
			return result;
		}
		if(stu_cam!=null) {
			if(stu_cam.length>2) {
			    result.put("code","-4");
			    result.put("msg","stu_cam error");
			    return result;
			}
			for(int i=0;i<stu_cam.length;i++) {
				if(stu_cam[i]>1||stu_cam[i]<0) {
					result.put("code","-4");
					result.put("msg","stu_cam error");
					return result;
				  }
				}
		}
		if(cou_id==null) {
			result.put("code","-5");
			result.put("msg","cou_id missing");
			return result;
		}
		if(stu_num!=null&&!stu_num.equals("")&&stu_name!=null&&!stu_name.equals("")) {
			result.put("code","-6");
			result.put("msg","stu_num and stu_name Cannot be delivered at the same time");
			return result;
		}
		List<Homework> homework = new ArrayList<Homework>();
		homework = user_service.get_hom_by_couId(cou_id);
		List<Integer> homework_id = new ArrayList<Integer>();
		for(int i=0;i<homework.size();i++) {
			homework_id.add(homework.get(i).getHomeworkId());
		}
		double sum_hom = homework.size();//该课程作业总数
		
		List<String> all_stu_num = user_service.get_stuNum_by_couNum(cou_num); //获取该课程的所有学生号
		List<StudentManage> all_stu = new ArrayList<>();
		int total = 0;
		for(int i=0;i<all_stu_num.size();i++) {//获取所有课程学生信息
			double stu_hom=0;
			Student stu = user_service.sel_stu_by_stu_number(all_stu_num.get(i));
			int stu_id = stu.getStudentId();
			for(int j=0;j<sum_hom;j++) {
				StudentHomework temp = new StudentHomework();
				temp.setStudentId(stu_id);
				temp.setHomeworkId(homework_id.get(j));
				StudentHomework flag = user_service.get_stu_hom(temp);
				if(flag!=null) 
					stu_hom++;
			}
			double hom_fin = stu_hom/sum_hom;
			StudentManage tmp = new StudentManage();
			tmp.setHomework(hom_fin);
			tmp.setArea(stu.getArea());
			tmp.setClassName(stu.getClassName());
			tmp.setGender(stu.getGender());
			tmp.setImage(stu.getImage());
			tmp.setName(stu.getName());
			tmp.setPhoneNumber(stu.getPhoneNumber());
			tmp.setStudentId(stu_id);
			tmp.setStudentNumber(stu.getStudentNumber());
			all_stu.add(tmp);
		}
		if(stu_cam!=null&&stu_cam.length!=0) {
		    try{//避免传的数组格式有问题
		    	for(int i=0;i<all_stu.size();i++) {
		    		boolean flag=false;
		    		for(int j=0;j<stu_cam.length;j++) {
		    			if(all_stu.get(i).getArea().equals(stu_cam[j])) {
		    				flag = true;
		    				break;
		    			}
		    		}
		    		if(!flag) {
		    			all_stu.remove(i);
		    			i--;
		    		}
		    }
			    }
		    catch(Exception e) {
		    	e.printStackTrace();
		    	result.put("code","-4");
				result.put("msg","stu_cam error");
				return result;
		    }
		}
		if(stu_num!=null&&!stu_num.equals("")) {
			for(int i=0;i<all_stu.size();i++) {
				if(all_stu.get(i).getStudentNumber().equals(stu_num)) {
					result.put("code","200");
					result.put("msg","success");
					result.put("data",all_stu.get(i));
					return result;
				}
			}
			result.put("code","200");
			result.put("msg","success");
			result.put("data",null);
			return result;
		}
		else if(stu_name!=null&&!stu_name.equals("")) {
			for(int i=0;i<all_stu.size();i++) {
	    		if(!all_stu.get(i).getName().contains(stu_name)&&!stu_name.contains(all_stu.get(i).getName())) {
	    			all_stu.remove(i);
	    			i--;
			     }
	    	   }
	        }
		total = all_stu.size();
		List<StudentManage> data = new ArrayList<>();
		for(int j = offset*size-size;j<offset*size;j++) {//学生分页
            try {
			    data.add(all_stu.get(j));
            }
            catch(Exception e) {
            	result.put("code","200");
        		result.put("msg","success");
        		result.put("data",data);
        		result.put("total",total);
        		return result;
            }
		}
		result.put("code","200");
		result.put("msg","success");
		result.put("data",data);
		result.put("total",total);
		return result;
	}	
	
	@RequestMapping({"/T12"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "将学生从课程学生信息中移除")
	public JSON del_stu_cou(
			@ApiParam(required=true,name="cou_num",value="课程号")String cou_num,
			@ApiParam(required=true,name="stu_num",value="学生学号")String stu_num) {
		JSONObject result = new JSONObject();
		if(cou_num == null||cou_num.equals("")) {
			result.put("code","-1");
			result.put("msg","cou_num missing");
			return result;
		}
		if(stu_num == null||stu_num.equals("")) {
			result.put("code","-2");
			result.put("msg","stu_num missing");
			return result;
		}
		StudentCourse stu_cou = new StudentCourse();
		stu_cou.setCourseNumber(cou_num);
		stu_cou.setStudentNumber(stu_num);
		int flag = user_service.del_stu_cou(stu_cou);
		if(flag==0) {
			result.put("code","-3");
			result.put("msg","database error,please contact administrator");
			return result;
		}
		else {
			result.put("code","200");
			result.put("msg","success");
			return result;
		}
	}
			
	@RequestMapping({"/T13"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "获取学生，无传参数的时候返回所有学生，传参数时根据参数回传数据")
	public JSON get_stu(
			@ApiParam(required=true,name="size",value="一页显示的学生数，参数类型为Integer")Integer size,
			@ApiParam(required=true,name="offset",value="页码，参数类型为Integer")Integer offset,
			@ApiParam(required=false,name="stu_cam",value="学生所在校区数组，参数类型为Integer[]")Integer[] stu_cam,
			@ApiParam(required=false,name="cou_num",value="课程号")String cou_num,
			@ApiParam(required=false,name="stu_num",value="学生号")String stu_num,
			@ApiParam(required=false,name="stu_name",value="学生姓名")String stu_name){
		JSONObject result = new JSONObject();
		if(size == null) {
			result.put("code","-2");
			result.put("msg","size missing");
			return result;
		}
		if(size<1) {
			result.put("code","-2");
			result.put("msg","size error");
			return result;
		}
		if(offset == null) {
			result.put("code","-3");
			result.put("msg","offset missing");
			return result;
		}
		if(stu_num!=null&&!stu_num.equals("")&&stu_name!=null&&!stu_name.equals("")) {
			result.put("code","-4");
			result.put("msg","stu_num and stu_name Cannot be delivered at the same time");
			return result;
		}
		if(stu_cam!=null) {
		    if(stu_cam.length>2) {
			    result.put("code","-5");
			    result.put("msg","stu_cam error");
			    return result;
		   }
		    for(int i=0;i<stu_cam.length;i++) {
				if(stu_cam[i]>1||stu_cam[i]<0) {
					result.put("code","-5");
					result.put("msg","stu_cam error");
					return result;
				  }
				}
		}
		List<Student> all_stu = user_service.get_all_stu();
		if(stu_cam!=null&&stu_cam.length!=0) {
		    try{//避免传的数组格式有问题
		    	for(int i=0;i<all_stu.size();i++) {
		    		boolean flag=false;
		    		for(int j=0;j<stu_cam.length;j++) {
		    			if(all_stu.get(i).getArea().equals(stu_cam[j])) {
		    				flag = true;
		    				break;
		    			}
		    		}
		    		if(!flag) {
		    			all_stu.remove(i);
		    			i--;
		    		}
		    }
			    }
		    catch(Exception e) {
		    	e.printStackTrace();
		    	result.put("code","-4");
				result.put("msg","stu_cam error");
				return result;
		    }
		}
		if(cou_num!=null&&!cou_num.equals("")) {
			List<String> ok_stu_num = user_service.get_stuNum_by_couNum(cou_num);
			for(int i=0;i<all_stu.size();i++) {
			    boolean flag = false;
				for(int j=0;j<ok_stu_num.size();j++) {
				    if(all_stu.get(i).getStudentNumber().equals(ok_stu_num.get(j))) {
					    flag=true;
					    ok_stu_num.remove(j);
					    j--;
					    break;
				}
			}
				if(!flag) {
					all_stu.remove(i);
					i--;
				}
		   }
		}
		if(stu_num!=null&&!stu_num.equals("")) {
			for(int i=0;i<all_stu.size();i++) {
				if(all_stu.get(i).getStudentNumber().equals(stu_num)) {
					result.put("code","200");
					result.put("msg","success");
					result.put("data",all_stu.get(i));
					return result;
				}
			}
			result.put("code","200");
			result.put("msg","success");
			result.put("data",null);
			return result;
		}
		else if(stu_name!=null&&!stu_name.equals("")) {
			for(int i=0;i<all_stu.size();i++) {
	    		if(!all_stu.get(i).getName().contains(stu_name)&&!stu_name.contains(all_stu.get(i).getName())) {
	    			all_stu.remove(i);
	    			i--;
			     }
	    	   }
	        }
		int total = all_stu.size();
		List<Student> data = new ArrayList<>();
		for(int j = offset*size-size;j<offset*size;j++) {//学生分页
            try {
			    data.add(all_stu.get(j));
            }
            catch(Exception e) {
            	result.put("code","200");
        		result.put("msg","success");
        		result.put("data",data);
        		result.put("total",total);
        		return result;
            }
		}
		result.put("code","200");
		result.put("msg","success");
		result.put("data",data);
		result.put("total",total);
		return result;
	}	
	
	
	@RequestMapping({"/T14"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "获取学生消息")
	public JSON get_mes(
			@ApiParam(required=true,name="tea_id",value="上课教师id，参数类型为Integer")Integer tea_id,
			@ApiParam(required=true,name="mes_sta",value="消息状态，0未回复，1已回复，2全部消息，参数类型为Integer")Integer mes_sta,
			@ApiParam(required=true,name="size",value="一页显示的学生数，参数类型为Integer")Integer size,
			@ApiParam(required=true,name="offset",value="页码，参数类型为Integer")Integer offset) throws UnknownHostException {
		JSONObject result = new JSONObject();
		if(tea_id == null) {
			result.put("code","-1");
			result.put("msg","tea_id missing");
			return result;
		}
		if(mes_sta==null||mes_sta<0||mes_sta>2) {
			result.put("code","-2");
			result.put("msg","mes_sta error");
			return result;
		}
		if(size == null) {
			result.put("code","-3");
			result.put("msg","size missing");
			return result;
		}
		if(size<1) {
			result.put("code","-3");
			result.put("msg","size error");
			return result;
		}
		if(offset == null) {
			result.put("code","-4");
			result.put("msg","offset missing");
			return result;
		}
		List<Message> mes = user_service.get_mes_by_teaId(tea_id);
		List<Message> data = new ArrayList<Message>();
		if(!mes_sta.equals(2)) {
		    for(int i=0;i<mes.size();i++) {
		       	Message temp = mes.get(i);
			    if(temp.getStatus().equals(mes_sta)) {
				    data.add(temp);
			}
		  }
		}
		else {
			data.addAll(mes);
		}
		List<Message> reverse = new ArrayList<Message>();
		for(int i=data.size()-1;i>=0;i--) {
			reverse.add(data.get(i));
		}
		List<Message> fi_data = new ArrayList<Message>();
		int total=data.size();
		for(int j = offset*size-size;j<offset*size;j++) {
            try {
            	reverse.get(j).setQueAudio("http://"+ip+":8080/AssistedLearningSystemResource/audio/message/"+reverse.get(j).getQueAudio());
			    fi_data.add(reverse.get(j));
            }
            catch(Exception e) {
            	result.put("code","200");
        		result.put("msg","success");
        		result.put("data",fi_data);
        		result.put("total",total);
        		return result;
            }
		}
		result.put("code","200");
		result.put("msg","success");
		result.put("data",fi_data);
		result.put("total",total);
		return result;
	}
	
	@RequestMapping({"/T15"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "回复学生消息,可选文本或音频或两者结合")
	public JSON rep_mes(
			@ApiParam(required=true,name="mes_id",value="消息id，参数类型为Integer")Integer mes_id,
			@ApiParam(required=false,name="rep_text",value="教师回复的文本内容，参数类型为String")String rep_text,
			@ApiParam(required=false,name="rep_aud",value="教师回复的音频内容，参数类型为MultipartFile")MultipartFile rep_aud) {
		JSONObject result = new JSONObject();
		if(mes_id == null) {
			result.put("code","-1");
			result.put("msg","mes_id missing");
			return result;
		}
		if((rep_text==null||rep_text.equals(""))&&(rep_aud==null||rep_aud.isEmpty())) {
			result.put("code","-2");
			result.put("msg","rep_text/rep_aud missing");
			return result;
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		String temp = sdf.format(date);
		Date rep_time = new Date();
		try {
			rep_time = sdf.parse(temp);
		} catch (ParseException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		Message mes = new Message();
		mes.setReplyTime(rep_time);
		mes.setMessageId(mes_id);
		if(rep_text!=null&&!rep_text.equals("")) {
			mes.setReply(rep_text);
		}
		if(rep_aud!=null&&!rep_aud.isEmpty()) {
			SimpleDateFormat temp_sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
			String aud_name = temp_sdf.format(date);
			try {
				File audio = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/audio/message/"+aud_name+".mp3");
				rep_aud.transferTo(audio);
			}
			catch(Exception e)
			{
				e.printStackTrace();
				result.put("code","-3");
				result.put("msg","server error,please contact administrator");
				return result;
			}
			mes.setRepAudio(aud_name+".mp3");
		}
		mes.setStatus(1);
		int flag = user_service.upd_mes(mes);
		if(flag==0) {
			result.put("code","-4");
			result.put("msg","database error,please contact administrator");
			return result;
		}
		result.put("code","200");
		result.put("msg","success");
		return result;
	}

	@RequestMapping({"/T16"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "获取每日一句，可选返回条数")
	public JSON get_res(@ApiParam(required=false,name="res_num",value="返回条数,数据类型为Integer")Integer res_num) throws UnknownHostException {
		JSONObject result = new JSONObject();
		if(res_num==null) {
			res_num=3;
		}
		List<Resource> res = new  ArrayList<Resource>();
		Random rand = new Random();
		res = user_service.get_res();
		List<Resource> data = new ArrayList<Resource>();
		int [] flag = new int [res_num];
		for(int i=0;i<res_num;i++) {//在返回的条目中随机返回数据
			flag[i] = rand.nextInt(res.size());
			while(i!=0&&flag[i]==flag[i-1]) {
				flag[i]=rand.nextInt(res.size());
			}
			res.get(flag[i]).setImage("http://"+ip+":8080/AssistedLearningSystemResource/image/resource/"+res.get(flag[i]).getImage());
			res.get(flag[i]).setAudio("http://"+ip+":8080/AssistedLearningSystemResource/audio/resource/"+res.get(flag[i]).getAudio());
			data.add(res.get(flag[i]));
		}
		result.put("code","200");
		result.put("msg","success");
		result.put("data",data);
		return result;
	}
	
	@RequestMapping({"/T17"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "上传每日一句,参数格式为form")
	public JSON upl_res(
			@ApiParam(required = true,name = "image_file",value = "背景图片文件，参数类型为MultipartFile")MultipartFile image_file,
			@ApiParam(required = true,name = "audio_file",value = "背景图片文件，参数类型为MultipartFile")MultipartFile audio_file,
			@ApiParam(required = true,name = "ind_text",value = "印尼语文本")String ind_text,
			@ApiParam(required = true,name = "chi_text",value = "中文文本")String chi_text,
			HttpServletRequest req) {
		JSONObject result = new JSONObject();
		if(image_file==null||image_file.isEmpty()) {
			result.put("code","-1");
			result.put("msg", "image_file missing");
			return result;
		}
		if(audio_file==null||audio_file.isEmpty()) {
			result.put("code","-2");
			result.put("msg", "audio_file missing");
			return result;
		}
		if(ind_text==null||ind_text.equals("")) {
			result.put("code","-3");
			result.put("msg", "ind_text missing");
			return result;
		}
		if(chi_text==null||chi_text.equals("")) {
			result.put("code","-4");
			result.put("msg", "chi_text missing");
			return result;
		}
		Resource res = new Resource();
		try {
		String tmp = req.getHeader("Authorization");
		String str [] = tmp.split(" ");
		String token = str[1];
		DecodedJWT jwt = JWT.decode(token);
	    String tea_num = jwt.getClaim("account").asString();
	    Teacher tea = user_service.sel_tea_by_tea_number(tea_num);
	    res.setTeacherId(tea.getTeacherId());
	    }
		catch(Exception e) {
			result.put("code","-5");
			result.put("msg", "token error");
			return result;
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS"); 
		String file_name = sdf.format(date);
		try {
		    File image = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/resource/"+file_name+".jpg");
		    image_file.transferTo(image);
	 	    File audio = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/audio/resource/"+file_name+".mp3");
	 	    audio_file.transferTo(audio);
		}
		catch(Exception e) {
			result.put("code","-6");
			result.put("msg", "server error,please contact administrator");
			return result;
		}
	 	    res.setAudio(file_name+".mp3");
	 	    res.setImage(file_name+".jpg");
	 	    res.setText(chi_text+" "+ind_text);
	 	    int flag = user_service.cre_res(res);
	 	    if(flag==0) {
	 	    	result.put("code","-7");
				result.put("msg", "database error,please contact administrator");
				return result;
	 	    }
	 	result.put("code","200");
		result.put("msg", "success");
	    return result;
	}
	
	@RequestMapping({"/T18"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "获取视频列表")
	public JSON get_vid(
			@ApiParam(required = true,name="size",value = "每页显示的条数,参数类型为Integer")Integer size,
			@ApiParam(required = true,name="offset",value = "页码,参数类型为Integer")Integer offset) throws UnknownHostException {
		JSONObject result = new JSONObject();
		if(size==null) {
			result.put("code","-1");
			result.put("msg","size missing");
			return result;
		}
		if(offset==null) {
			result.put("code","-2");
			result.put("msg","offset missing");
			return result;
		}
		List<Video> data = new ArrayList<Video>();
		Vid_page vid_page = new Vid_page();
		vid_page = user_service.get_vid(offset,size);
		int total = vid_page.getTotal();
		data = vid_page.getVideo();
		for(int i=0;i<data.size();i++) {
			data.get(i).setImage("http://"+ip+":8080/AssistedLearningSystemResource/image/video/"+data.get(i).getImage());
		}
		result.put("code","200");
		result.put("msg","success");
		result.put("total",total);
		result.put("data",data);
		return result;
	}
	
	@RequestMapping({"/T19"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "上传新视频，参数格式为form")
	public JSON upl_vid(@ApiParam(required = true,name = "cov_image",value = "背景图片文件，参数类型为MultipartFile")MultipartFile cov_image,
			@ApiParam(required = true,name = "vid_type",value = "视频类型,参数类型为Integer，0推荐 1单词学习 2听力训练 3课外素材")Integer vid_type,
			@ApiParam(required = true,name = "vid_link",value = "视频链接")String vid_link,
			@ApiParam(required = true,name = "vid_name",value = "视频名称")String vid_name,
			@ApiParam(required = true,name = "vid_tit",value = "视频标题")String vid_tit,
			HttpServletRequest req) {
		JSONObject result = new JSONObject();
		if(vid_tit==null||vid_tit.equals("")) {
			result.put("code","-1");
			result.put("msg", "vid_tit missing");
			return result;
		}
		if(vid_name==null||vid_name.equals("")) {
			result.put("code","-2");
			result.put("msg", "vid_name missing");
			return result;
		}
		if(vid_link==null||vid_link.equals("")) {
			result.put("code","-3");
			result.put("msg", "vid_link missing");
			return result;
		}
		if(vid_type==null||vid_type>3||vid_type<0) {
			result.put("code","-4");
			result.put("msg", "vid_type error");
			return result;
		}
		if(cov_image==null||cov_image.isEmpty()) {
			result.put("code","-5");
			result.put("msg", "cov_image missing");
			return result;
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String file_name = sdf.format(date);
		try {
		    File video = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/video/"+file_name+".jpg");
		    cov_image.transferTo(video);
		}
		catch(Exception e) {
			e.printStackTrace();
			result.put("code","-7");
			result.put("msg", "server error,please contact administrator");
			return result;
		}
		String tmp = sdf_1.format(date);
		Date time;
		try {
			time = sdf_1.parse(tmp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();e.printStackTrace();
			result.put("code","-7");
			result.put("msg", "server error,please contact administrator");
			return result;
		}
		Video vid = new Video();
		try {
			String temp = req.getHeader("Authorization");
			String str [] = temp.split(" ");
			String token = str[1];
			DecodedJWT jwt = JWT.decode(token);
		    String tea_num = jwt.getClaim("account").asString();
		    Teacher tea = user_service.sel_tea_by_tea_number(tea_num);
		    vid.setTeacherId(tea.getTeacherId());
		    }
			catch(Exception e) {
				result.put("code","-6");
				result.put("msg", "token error");
				return result;
			}
        vid.setImage(file_name+".jpg");
        vid.setName(vid_name);
        vid.setReleaseTime(time);
        vid.setTitle(vid_tit);
        vid.setVideoUrl(vid_link);
        vid.setType(vid_type);
        int flag = user_service.cre_vid(vid);
        if(flag==0) {
        	result.put("code","-8");
			result.put("msg", "database error,please contact administrator");
			return result;
        }
        result.put("code","200");
		result.put("msg", "success");
		return result;
	}
	
	@RequestMapping({"/T20"})
	@ResponseBody
	@ApiOperation(httpMethod = "GET",value = "获取文章列表")
	public JSON get_art(
			@ApiParam(required = true,name="size",value = "每页显示的条数,参数类型为Integer")Integer size,
			@ApiParam(required = true,name="offset",value = "页码,参数类型为Integer")Integer offset) throws UnknownHostException {
		JSONObject result = new JSONObject();
		if(size==null) {
			result.put("code","-1");
			result.put("msg","size missing");
			return result;
		}
		if(offset==null) {
			result.put("code","-2");
			result.put("msg","offset missing");
			return result;
		}
		Art_page art_page = user_service.get_art(offset,size);
		int total = art_page.getTotal();
		List<Article> data = art_page.getArticle();
		result.put("code","200");
		result.put("msg","success");
		result.put("total",total);
		result.put("data",data);
		return result;
	}
	
	@RequestMapping({"/T21"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "上传新文章，参数格式为form")
	public JSON upl_art(
			@ApiParam(required = true,name = "art_cov",value = "文章封面图片，参数类型为MultipartFile")MultipartFile art_cov,
			@ApiParam(required = true,name = "art_con",value = "文章内容")String art_con,
			@ApiParam(required = true,name = "art_tit",value = "文章标题")String art_tit,
			HttpServletRequest req) throws MultipartException{
		JSONObject result = new JSONObject();
		if(art_tit==null||art_tit.equals("")) {
			result.put("code","-1");
			result.put("msg", "art_tit missing");
			return result;
		}
		if(art_con==null||art_con.equals("")) {
			result.put("code","-2");
			result.put("msg", "art_con missing");
			return result;
		}
		if(art_cov==null||art_cov.isEmpty()) {
			result.put("code","-3");
			result.put("msg", "art_cov missing");
			return result;
		}
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
		SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
		String file_name = sdf.format(date);
		try {
		    File article = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/article/"+file_name+".jpg");
		    art_cov.transferTo(article);
		}
		catch(Exception e) {
			e.printStackTrace();
			result.put("code","-5");
			result.put("msg", "server error,please contact administrator");
			return result;
		}
		String tmp = sdf_1.format(date);
		Date time;
		try {
			time = sdf_1.parse(tmp);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();e.printStackTrace();
			result.put("code","-5");
			result.put("msg", "server error,please contact administrator");
			return result;
		}
		Article art = new Article();
		try {	
			String temp = req.getHeader("Authorization");
			String str [] = temp.split(" ");
			String token = str[1];
			DecodedJWT jwt = JWT.decode(token);
		    String tea_num = jwt.getClaim("account").asString();
		    Teacher tea = user_service.sel_tea_by_tea_number(tea_num);
		    art.setTeacherId(tea.getTeacherId());
		    }
			catch(Exception e) {
				result.put("code","-4");
				result.put("msg", "token error");
				return result;
			}
		art.setImage("http://121.37.204.235:8080/AssistedLearningSystemResource/image/article/"+file_name+".jpg");
		art.setReleaseTime(time);
		art.setText(art_con);
		art.setTitle(art_tit);
		int flag = user_service.cre_art(art);
		if(flag==0) {
			result.put("code","-6");
			result.put("msg", "database error");
			return result;
		}
		result.put("code","200");
		result.put("msg", "success");
		return result;
	}
	
	@RequestMapping({"/T22"})
	@ResponseBody
	@ApiOperation(httpMethod = "POST",value = "下载文章")
	public JSON dow_art(
			@ApiParam(required = true,name = "art_id",value = "文章id，参数类型为Integer")Integer art_id,
			HttpServletResponse response){
		JSONObject result = new JSONObject();
		if(art_id==null) {
			result.put("code","-1");
			result.put("msg", "art_id missing");
			return result;
		}
		Article art = user_service.get_art_by_id(art_id);
		
		try {
			Date date = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss-SSS");
			SimpleDateFormat sdf_1 = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
			String file_name = sdf.format(date);
			File art_file = new File("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/article/"+file_name);
			FileWriter fw = new FileWriter("/home/apache-tomcat-8.5.54/webapps/AssistedLearningSystemResource/image/article/"+file_name,true); 
			fw.append(art.getTitle()+"\r\n"+"\r\n");
			fw.append(art.getText()+"\r\n"+"\r\n");
			fw.append("ReleaseTime:"+sdf_1.format(art.getReleaseTime()));
			fw.close();
			InputStream fin = new BufferedInputStream(new FileInputStream(art_file));
	        byte[] buffer = new byte[fin.available()];
	        fin.read(buffer);
	        fin.close();
			response.reset();
			response.setHeader("content-disposition", "attachment;filename=" + URLEncoder.encode("article.txt","UTF-8"));
            response.addHeader("Content-Length", "" + art_file.length());
            response.setContentType("text/plain");
            OutputStream out = new BufferedOutputStream(response.getOutputStream());
            out.write(buffer);
            out.flush();
            out.close();
            if(art_file.exists()) {
            	art_file.delete();
            }
		}
		catch (Exception e) {
			e.printStackTrace();
			result.put("code","-2");
			result.put("msg","server error,please contact administrator");
			return result;
		}
		result.put("code","200");
		result.put("msg","success");
		return result;
	}
}





//@RequestMapping({"/T15"})
//@ResponseBody
//@ApiOperation(httpMethod = "POST",value = "根据学生id删除学生")
//public JSON del_stu(@ApiParam(required=true,name="stu_id",value="学生id，参数类型为Integer")Integer stu_id) {
//	JSONObject result = new JSONObject();
//	if(stu_id==null) {
//		result.put("code","-1");
//		result.put("msg","stu_id missing");
//		return result;
//	}
//	int flag = user_service.del_stu(stu_id);
//	if(flag==0) {
//		result.put("code","-2");
//		result.put("msg","database error,please contact administrator");
//		return result;
//	}
//	result.put("code","200");
//	result.put("msg","success");
//	return result;
//}