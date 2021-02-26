package golden.dao.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import golden.dao.userdao;
import golden.model.Article;
import golden.model.Calendar;
import golden.model.Collection;
import golden.model.Course;
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
import golden.tempmodel.Art_page;
import golden.tempmodel.ChatList;
import golden.tempmodel.Page_Para;
import golden.tempmodel.SearchCourse;
import golden.tempmodel.Tea_image;
import golden.tempmodel.Vid_page;
import golden.tempmodel.article_1;
import golden.tempmodel.collection_1;



@Repository
public class userdaoimpl extends SqlSessionDaoSupport implements userdao{
    
	 @Autowired
	  public void setSqlSessionFactory(SqlSessionFactory sqlSessionFactory) {
	    super.setSqlSessionFactory(sqlSessionFactory);
	  }

	/* static private SqlSessionFactory sqlSessionFactory; */
	  String ns_1 = "golden.mapper.WordListMapper.";
	  String ns_2 = "golden.mapper.HasReadWordsMapper.";
	  String ns_3 = "golden.mapper.CalendarMapper.";
	  String ns_4 = "golden.mapper.CollectionMapper.";
	  String ns_5 = "golden.mapper.ArticleMapper.";
	  String ns_6 = "golden.mapper.TeacherMapper.";
	  String ns_7 = "golden.mapper.StudentMapper.";
	  String ns_8 = "golden.mapper.CourseMapper.";
	  String ns_9 = "golden.mapper.StudentCourseMapper.";
	  String ns_10 = "golden.mapper.MessageMapper.";
	  String ns_11 = "golden.mapper.HomeworkMapper.";
	  String ns_12 = "golden.mapper.StudentHomeworkMapper.";
	  String ns_13 = "golden.mapper.ResourceMapper.";
	  String ns_14 = "golden.mapper.VideoMapper.";




	/*
	 * public SqlSession getSqlSession() { InputStream is; try { is =
	 * Resources.getResourceAsStream("WordListMapper.xml"); if (sqlSessionFactory ==
	 * null) { sqlSessionFactory = new SqlSessionFactoryBuilder().build(is); }
	 * return sqlSessionFactory.openSession(); } catch (IOException e) { // TODO
	 * Auto-generated catch block e.printStackTrace(); } return null; }
	 */
	
	@Override
	public WordListWithBLOBs selectbyword_id(Integer word_id) {
		// TODO Auto-generated method stub
		 return (WordListWithBLOBs)getSqlSession().selectOne(String.valueOf(this.ns_1) + "selectByPrimaryKey", word_id);
	}

	
	  
	  
	  @Override 
	  public List<Object> selectbyword(@Param(value="word")String word)
	  {
		  
	      List<Object> word_list=getSqlSession().selectList(String.valueOf(this.ns_1) + "selectByWord", word);
	      return word_list;
	  }



	  @Override 
	public HasReadWordsWithBLOBs selectby_studentid(Integer student_id) {
		// TODO Auto-generated method stub
		return (HasReadWordsWithBLOBs)getSqlSession().selectOne(String.valueOf(this.ns_2)+"selectby_studentid",student_id);
	}




	@Override
	public Calendar select_calendar_by_studentid(Integer student_id) {
		// TODO Auto-generated method stub
		return (Calendar)getSqlSession().selectOne(String.valueOf(this.ns_3)+"selectby_studentid",student_id);
	}




	@Override
	public int collection(Collection collec) {
		// TODO Auto-generated method stub		
		int result = getSqlSession().update(String.valueOf(this.ns_4)+"updateby_studentid",collec);
		return result;	
	}




	@Override
	public collection_1 select_collection(Integer student_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(String.valueOf(this.ns_4)+"selectby_student_id",student_id);
	}




	@Override
	public List<article_1> get_text() {
		// TODO Auto-generated method stub
		List<article_1> article_list=getSqlSession().selectList(String.valueOf(this.ns_5) + "select_text");
		return article_list;
	}




	@Override
	public int update_text(article_1 arti) {
		// TODO Auto-generated method stub
		int result = getSqlSession().update(String.valueOf(this.ns_5)+"update_by_art_id",arti);
		return result;
	}




	@Override
	public Teacher sel_tea_by_tea_number(String tea_number) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_6+"sel_tea_by_tea_number",tea_number);
	}




	@Override
	public int add_tea(Teacher tea) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_6+"insert",tea);
	}




	@Override
	public Student sel_stu_by_stu_number(String stu_num) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_7+"sel_stu_by_stu_number",stu_num);
	}




	@Override
	public int upd_tea(Teacher tea) {
		// TODO Auto-generated method stub
		return getSqlSession().update(ns_6+"upd_tea",tea);
	}




	@Override
	public int upd_pwd(Teacher tea) {
		// TODO Auto-generated method stub
		return getSqlSession().update(ns_6+"upd_pwd",tea);
	}




	@Override
	public int upd_ima(Tea_image tea_image) {
		// TODO Auto-generated method stub
		return getSqlSession().update(ns_6+"upd_ima",tea_image);
	}




	@Override
	public List<Course> get_cou(Integer tea_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_8+"get_cou", tea_id);
	}




	@Override
	public Course get_cou_by_couNum(String cou_num) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_8+"get_cou_by_couNum",cou_num);
	}




	@Override
	public int upd_cou_by_id(Course cou) {
		// TODO Auto-generated method stub
           return getSqlSession().update(ns_8+"updateByPrimaryKey",cou);

	}




	@Override
	public String get_last_cou_rec() {
		// TODO Auto-generated method stub
		Course cou = getSqlSession().selectOne(ns_8+"get_last_cou_rec");
		return cou.getCourseNumber();
	}




	@Override
	public int cre_new_cou(Course cou) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_8+"insert", cou);
	}




	@Override
	public List<String> get_cou_by_teaId(Integer tea_id) {
		// TODO Auto-generated method stub
		List<Course> cou = getSqlSession().selectList(ns_8+"get_cou_by_teaId", tea_id);
        List<String> cou_num = new ArrayList<String>(); 
        for(int i=0;i<cou.size();i++) {
        	String num = cou.get(i).getCourseNumber();
        	cou_num.add(num);
        }
		return cou_num;
	}




	@Override
	public List<String> get_stuNum_by_couNum(String cou_num) {
		// TODO Auto-generated method stub
		List<StudentCourse> stu = getSqlSession().selectList(ns_9+"get_stuNum_by_couNum", cou_num);
		List<String> stu_num = new ArrayList<>();
		for(int i=0;i<stu.size();i++) {
			stu_num.add(stu.get(i).getStudentNumber());
		}
		return stu_num;
	}




	@Override
	public List<Message> get_mes_by_teaId(Integer tea_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_10+"get_mes_by_teaId", tea_id);
	}




	@Override
	public int upd_mes(Message mes) {
		// TODO Auto-generated method stub
		return getSqlSession().update(ns_10+"upd_mes",mes);
	}




	@Override
	public int del_stu(Integer stu_id) {
		// TODO Auto-generated method stub
		return getSqlSession().delete(ns_7+"deleteByPrimaryKey",stu_id);
	}




	@Override
	public List<Homework> get_hom_by_couId(Integer cou_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_11+"get_hom_by_couId",cou_id);
	}




	@Override
	public StudentHomework get_stu_hom(StudentHomework temp) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_12+"get_stu_hom",temp);
	}




	@Override
	public List<Student> get_all_stu() {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_7+"get_all_stu");
	}




	@Override
	public int del_stu_cou(StudentCourse stu_cou) {
		// TODO Auto-generated method stub
		return getSqlSession().delete(ns_9+"del_stu_cou",stu_cou);
	}




	@Override
	public List<Resource> get_res() {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_13+"get_res");
	}




	@Override
	public int cre_res(Resource res) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_13+"insert",res);
	}




	@Override
	public Vid_page get_vid(Integer offset, Integer size) {
		// TODO Auto-generated method stub
		Page_Para page_par = new Page_Para();
		int total = getSqlSession().selectOne(ns_14+"get_vid_total");
		int start = offset*size-size;//倒序排列的起点
		page_par.setStart(start);
		page_par.setSize(size);
		List<Video> video = getSqlSession().selectList(ns_14+"get_vid",page_par);
		Vid_page vid_page = new Vid_page();
		vid_page.setVideo(video);
		vid_page.setTotal(total);
		return vid_page;
	}




	@Override
	public int cre_vid(Video vid) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_14+"insert",vid);
	}




	@Override
	public Art_page get_art(Integer offset, Integer size) {
		// TODO Auto-generated method stub
		Page_Para page_par = new Page_Para();
		int total = getSqlSession().selectOne(ns_5+"get_art_total");
		int start = offset*size-size;//倒序排列的起点
		page_par.setStart(start);
		page_par.setSize(size);
		List<Article> article = getSqlSession().selectList(ns_5+"get_art",page_par);
		Art_page art_page = new Art_page();
		art_page.setArticle(article);
		art_page.setTotal(total);
		return art_page;
	}




	@Override
	public int cre_art(Article art) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_5+"insert",art);
	}




	@Override
	public Article get_art_by_id(Integer art_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_5+"selectByPrimaryKey", art_id);
	}




	@Override
	public List<Course> get_cou_list_by_stuNum(String stu_num) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_8+"get_cou_list_by_stuNum", stu_num);
	}




	@Override
	public Course get_cou_by_couId(Integer cou_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_8+"selectByPrimaryKey", cou_id);
	}




	@Override
	public Teacher get_tea_dea(Integer tea_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_6+"selectByPrimaryKey",tea_id);
	}




	@Override
	public Homework get_hom_by_homId(Integer hom_id) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_11+"selectByPrimaryKey", hom_id);
	}




	@Override
	public int upl_ans(StudentHomework stu_hom) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_12+"insert",stu_hom);
	}




	@Override
	public SearchCourse get_cou_Teaname_by_couNum(String cou_num) {
		// TODO Auto-generated method stub
		return getSqlSession().selectOne(ns_8+"get_cou_Teaname_by_couNum",cou_num);
	}




	@Override
	public int join_cou(StudentCourse stu_cou) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_9+"insert", stu_cou);
	}




	@Override
	public List<ChatList> get_chat_list(String stu_num) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_6+"get_chat_list", stu_num);
	}




	@Override
	public List<Message> get_mes(Message mes) {
		// TODO Auto-generated method stub
		return getSqlSession().selectList(ns_10+"get_mes", mes);
	}




	@Override
	public int send_mes(Message mes) {
		// TODO Auto-generated method stub
		return getSqlSession().insert(ns_10+"insert",mes);
	}



}
