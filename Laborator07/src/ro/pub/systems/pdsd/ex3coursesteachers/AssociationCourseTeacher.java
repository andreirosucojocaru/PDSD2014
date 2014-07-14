package ro.pub.systems.pdsd.ex3coursesteachers;

public class AssociationCourseTeacher {
	
	private int associationCourseTeacherId, courseId, teacherId;
	
	public AssociationCourseTeacher() {
		this.associationCourseTeacherId = -1;
		this.courseId = -1;
		this.teacherId = -1;
	}
	
	public AssociationCourseTeacher(int associationCourseTeacherId, int courseId, int teacherId) {
		this.associationCourseTeacherId = associationCourseTeacherId;
		this.courseId = courseId;
		this.teacherId = teacherId;
	}
	
	public int getAssociationCourseTeacherId() {
		return associationCourseTeacherId;
	}
	
	public void setAssociationCourseTeacherId(int associationCourseTeacherId) {
		this.associationCourseTeacherId = associationCourseTeacherId;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	
	public int getTeacherId() {
		return teacherId;
	}
	
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}

}
