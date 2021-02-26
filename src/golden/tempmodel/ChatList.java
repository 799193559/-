package golden.tempmodel;



public class ChatList {
	private Integer teacherId;
	
	private Integer courseId;
	
    private String tea_image;
    
    private String cou_name;
    
    private String tea_name;
  
	public String getTea_name() {
		return tea_name;
	}

	public void setTea_name(String tea_name) {
		this.tea_name = tea_name;
	}

	public Integer getCourseId() {
		return courseId;
	}

	public void setCourseId(Integer courseId) {
		this.courseId = courseId;
	}

	public String getTea_image() {
		return tea_image;
	}

	public void setTea_image(String tea_image) {
		this.tea_image = tea_image;
	}

	public Integer getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(Integer teacherId) {
		this.teacherId = teacherId;
	}

	public String getCou_name() {
		return cou_name;
	}

	public void setCou_name(String cou_name) {
		this.cou_name = cou_name;
	}


}