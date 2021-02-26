package golden.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;

import golden.dao.userdao;
import golden.dao.impl.userdaoimpl;
import golden.model.Article;
import golden.model.Calendar;
import golden.model.Collection;
import golden.model.Course;
import golden.model.HasReadWords;
import golden.model.HasReadWordsWithBLOBs;
import golden.model.Homework;
import golden.model.Message;
import golden.model.Resource;
import golden.model.Student;
import golden.model.StudentCourse;
import golden.model.StudentHomework;
import golden.model.Teacher;
import golden.model.Video;
import golden.model.WordListWithBLOBs;
import golden.service.userservice;
import golden.tempmodel.Art_page;
import golden.tempmodel.ChatList;
import golden.tempmodel.SearchCourse;
import golden.tempmodel.Tea_image;
import golden.tempmodel.Vid_page;
import golden.tempmodel.article_1;
import golden.tempmodel.collection_1;

@Service("userservice")
public class userserviceimpl implements userservice {
    @Autowired
     userdao user_dao;

	@Override
	public WordListWithBLOBs selectbyword_id(Integer word_id) {
		// TODO Auto-generated method stub
		WordListWithBLOBs word_info = user_dao.selectbyword_id(word_id);
		return word_info;
	}

	
	  
	  public List<Object> selectbyword(String word) { 
	      List<Object> word_list =user_dao.selectbyword(word); 
	      return word_list; 
	   }



	@Override
	public HasReadWordsWithBLOBs selectby_studentid(Integer student_id) {
		// TODO Auto-generated method stub
		return user_dao.selectby_studentid(student_id);
	}



	@Override
	public Calendar select_calendar_by_studentid(Integer student_id) {
		// TODO Auto-generated method stub
		return user_dao.select_calendar_by_studentid(student_id);
	}



	@Override
	public int collection(Collection collec) {
		// TODO Auto-generated method stub
		int result =user_dao.collection(collec);
		return result;
	}



	@Override
	public collection_1 select_collection(Integer student_id) {
		// TODO Auto-generated method stub
		return user_dao.select_collection(student_id);
	}



	@Override
	public List<article_1> get_text() {
		// TODO Auto-generated method stub
		return user_dao.get_text();
	}



	@Override
	public int update_text(article_1 arti) {
		// TODO Auto-generated method stub
		return user_dao.update_text(arti);
	}



	@Override
	public Teacher sel_tea_by_tea_number(String tea_number) {
		// TODO Auto-generated method stub
		return user_dao.sel_tea_by_tea_number(tea_number);
	}



	@Override
	public int add_tea(Teacher tea) {
		// TODO Auto-generated method stub
		return user_dao.add_tea(tea);
	}



	@Override
	public Student sel_stu_by_stu_number(String stu_num) {
		// TODO Auto-generated method stub
		return user_dao.sel_stu_by_stu_number(stu_num);
	}



	@Override
	public int upd_tea(Teacher tea) {
		// TODO Auto-generated method stub
		return user_dao.upd_tea(tea);
	}



	@Override
	public int upd_pwd(Teacher tea) {
		// TODO Auto-generated method stub
		return user_dao.upd_pwd(tea);
	}



	@Override
	public int upd_ima(Tea_image tea_image) {
		// TODO Auto-generated method stub
		return user_dao.upd_ima(tea_image);
	}



	@Override
	public List<Course> get_cou(Integer tea_id) {
		// TODO Auto-generated method stub
		return user_dao.get_cou(tea_id);
	}



	@Override
	public Course get_cou_by_couNum(String cou_num) {
		// TODO Auto-generated method stub
		return user_dao.get_cou_by_couNum(cou_num);
	}



	@Override
	public int upd_cou_by_id(Course cou) {
		// TODO Auto-generated method stub
		return user_dao.upd_cou_by_id(cou);
	}



	@Override
	public String get_last_cou_rec() {
		// TODO Auto-generated method stub
		return user_dao.get_last_cou_rec();
	}



	@Override
	public int cre_new_cou(Course cou) {
		// TODO Auto-generated method stub
		return user_dao.cre_new_cou(cou);
	}



	@Override
	public List<String> get_cou_by_teaId(Integer tea_id) {
		// TODO Auto-generated method stub
		return user_dao.get_cou_by_teaId(tea_id);
	}



	@Override
	public List<String> get_stuNum_by_couNum(String cou_num) {
		// TODO Auto-generated method stub
		return user_dao.get_stuNum_by_couNum(cou_num);
	}



	@Override
	public List<Message> get_mes_by_teaId(Integer tea_id) {
		// TODO Auto-generated method stub
		return user_dao.get_mes_by_teaId(tea_id);
	}



	@Override
	public int upd_mes(Message mes) {
		// TODO Auto-generated method stub
		return user_dao.upd_mes(mes);
	}



	@Override
	public int del_stu(Integer stu_id) {
		// TODO Auto-generated method stub
		return user_dao.del_stu(stu_id);
	}



	@Override
	public List<Homework> get_hom_by_couId(Integer cou_id) {
		// TODO Auto-generated method stub
		return user_dao.get_hom_by_couId(cou_id);
	}



	@Override
	public StudentHomework get_stu_hom(StudentHomework temp) {
		// TODO Auto-generated method stub
		return user_dao.get_stu_hom(temp);
	}



	@Override
	public List<Student> get_all_stu() {
		// TODO Auto-generated method stub
		return user_dao.get_all_stu();
	}



	@Override
	public int del_stu_cou(StudentCourse stu_cou) {
		// TODO Auto-generated method stub
		return user_dao.del_stu_cou(stu_cou);
	}



	@Override
	public List<Resource> get_res() {
		// TODO Auto-generated method stub
		return user_dao.get_res();
	}



	@Override
	public int cre_res(Resource res) {
		// TODO Auto-generated method stub
		return user_dao.cre_res(res);
	}



	@Override
	public Vid_page get_vid(Integer offset, Integer size) {
		// TODO Auto-generated method stub
		return user_dao.get_vid(offset,size);
	}



	@Override
	public int cre_vid(Video vid) {
		// TODO Auto-generated method stub
		return user_dao.cre_vid(vid);
	}



	@Override
	public Art_page get_art(Integer offset, Integer size) {
		// TODO Auto-generated method stub
		return user_dao.get_art(offset,size);
    }



	@Override
	public int cre_art(Article art) {
		// TODO Auto-generated method stub
		return user_dao.cre_art(art);
	}



	@Override
	public Article get_art_by_id(Integer art_id) {
		// TODO Auto-generated method stub
		return user_dao.get_art_by_id(art_id);
	}



	@Override
	public List<Course> get_cou_by_stuNum(String stu_num) {
		// TODO Auto-generated method stub
		return user_dao.get_cou_list_by_stuNum(stu_num);
	}



	@Override
	public Course get_cou_by_couId(Integer cou_id) {
		// TODO Auto-generated method stub
		return user_dao.get_cou_by_couId(cou_id);
	}



	@Override
	public Teacher get_tea_dea(Integer tea_id) {
		// TODO Auto-generated method stub
		return user_dao.get_tea_dea(tea_id);
	}



	@Override
	public Homework get_hom_by_homId(Integer hom_id) {
		// TODO Auto-generated method stub
		return user_dao.get_hom_by_homId(hom_id);
	}



	@Override
	public int upl_ans(StudentHomework stu_hom) {
		// TODO Auto-generated method stub
		return user_dao.upl_ans(stu_hom);
	}



	@Override
	public SearchCourse get_cou_Teaname_by_couNum(String cou_num) {
		// TODO Auto-generated method stub
		return user_dao.get_cou_Teaname_by_couNum(cou_num);
	}



	@Override
	public int join_cou(StudentCourse stu_cou) {
		// TODO Auto-generated method stub
		return user_dao.join_cou(stu_cou);
	}



	@Override
	public List<ChatList> get_chat_list(String stu_num) {
		// TODO Auto-generated method stub
		return user_dao.get_chat_list(stu_num);
	}



	@Override
	public List<Message> get_mes(Message mes) {
		// TODO Auto-generated method stub
		return user_dao.get_mes(mes);
	}



	@Override
	public int send_mes(Message mes) {
		// TODO Auto-generated method stub
		return user_dao.send_mes(mes);
	}

	  	 
}
