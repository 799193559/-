package golden.tempmodel;

import golden.model.StudentCourse;
import golden.model.Teacher;

public class CourseList {
	private Integer courseId;
	
    private String image;
    
    private String cou_name;
    
    private String tea_name;
        

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}




	public String getCou_name() {
		return cou_name;
	}

	public void setCou_name(String cou_name) {
		this.cou_name = cou_name;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getTea_name() {
		return tea_name;
	}

	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}

}
