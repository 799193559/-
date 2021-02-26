package golden.model;

import java.util.Date;

public class Message {
    private Integer messageId;

    private Integer studentId;

    private Integer teacherId;

    private Integer courseId;

    private String question;

    private String reply;

    private Date questionTime;

    private Date replyTime;

    private Integer status;

    private String repAudio;

    private String queAudio;

    public Integer getMessageId() {
        return messageId;
    }

    public void setMessageId(Integer messageId) {
        this.messageId = messageId;
    }

    public Integer getStudentId() {
        return studentId;
    }

    public void setStudentId(Integer studentId) {
        this.studentId = studentId;
    }

    public Integer getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(Integer teacherId) {
        this.teacherId = teacherId;
    }

    public Integer getCourseId() {
        return courseId;
    }

    public void setCourseId(Integer courseId) {
        this.courseId = courseId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public Date getQuestionTime() {
        return questionTime;
    }

    public void setQuestionTime(Date questionTime) {
        this.questionTime = questionTime;
    }

    public Date getReplyTime() {
        return replyTime;
    }

    public void setReplyTime(Date replyTime) {
        this.replyTime = replyTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getRepAudio() {
        return repAudio;
    }

    public void setRepAudio(String repAudio) {
        this.repAudio = repAudio;
    }

    public String getQueAudio() {
        return queAudio;
    }

    public void setQueAudio(String queAudio) {
        this.queAudio = queAudio;
    }
}