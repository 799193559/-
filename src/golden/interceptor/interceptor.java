package golden.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;

import golden.model.*;
import golden.service.userservice;
import golden.token.DeToken;

import javax.annotation.Resource;
//import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import java.io.PrintWriter;
@Component
public class interceptor implements HandlerInterceptor{
  @Resource
  userservice userService;
  
  public void setUserService(userservice userService) {
    this.userService = userService;
  }
  
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception,TokenExpiredException {
    String token,pwd; 
    String temp = request.getHeader("Authorization");
    PrintWriter pw = response.getWriter();
    response.reset();
    response.setCharacterEncoding("UTF-8");
    response.setContentType("application/json;charset=UTF-8");
//    HttpServletRequest httpServletRequest = (HttpServletRequest)request;
    HttpServletResponse httpServletResponse = (HttpServletResponse)response;
    httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
    httpServletResponse.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
    httpServletResponse.setHeader("Access-Control-Max-Age", "0");
    httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, No-Cache, X-Requested-With, If-Modified-Since, Pragma, Last-Modified, Cache-Control, Expires, Content-Type, X-E4M-With,userId,token,authorization");
    httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
    httpServletResponse.setHeader("XDomainRequestAllowed", "1");
    JSONObject result=new JSONObject();
    if (temp != null) {
      String[] str = temp.split(" ");
      String identity = str[0];//身份判断，teacher 为老师，student 为学生，Authorization格式为 teacher+空格+ token或student+空格+ token
      token = str[1];
      DeToken detoken = new DeToken();
      DecodedJWT jwt = null;
      jwt = detoken.deToken(token);
      if (jwt == null) {
    	  result.put("code",-1);
          result.put("msg","token expiration or token invalid");
    	  pw.write(result.toJSONString());
          return false; 
      }
      jwt = JWT.decode(token);
      String account = jwt.getClaim("account").asString();
//      User user_1 = new User();
//      user_1.setUsername(account);
      if(identity.equals("teacher")) {
          Teacher tea = this.userService.sel_tea_by_tea_number(account);
    	  String pwd_1;
    	  try {
    		  pwd_1 = tea.getPassword();
    	  }
    	  catch(Exception e) {
    		  result.put("code",-2);
              result.put("msg","token invalid");
        	  pw.write(result.toJSONString());
              return false;
    	  }
          pwd = jwt.getClaim("pwd").asString();
          if(!pwd.equals(pwd_1)) {
              result.put("code",-3);
              result.put("msg","token invalid");
        	  pw.write(result.toJSONString());
              return false;
              }
      }
      else if(identity.equals("student")) {
    	  Student stu = this.userService.sel_stu_by_stu_number(account);
    	  String pwd_1;
    	  try {
    		  pwd_1 = stu.getPassword();
    	  }
    	  catch(Exception e){
        	  result.put("code",-4);
              result.put("msg","token invalid");
        	  pw.write(result.toJSONString());
              return false;
          } 
    	  pwd = jwt.getClaim("pwd").asString();
          if(!pwd.equals(pwd_1)) {
        	  result.put("code",-5);
              result.put("msg","token invalid");
    	      pw.write(result.toJSONString());
              return false;
          }
      }   
//        result.put("code",200);
//        result.put("msg","success");
//	    pw.write(result.toJSONString());
        return true;
    } 
    else {
        result.put("code",-6);
        result.put("msg","token missing");
  	    pw.write(result.toJSONString());
  	    return false;
    }	
  }
  
  public void afterCompletion(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, Exception arg3) throws Exception {}
  
  public void postHandle(HttpServletRequest arg0, HttpServletResponse arg1, Object arg2, ModelAndView arg3) throws Exception {}
}

