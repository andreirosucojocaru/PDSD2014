package ro.pub.systems.pdsd.ex3coursesteachers;

public class Course {
	
	private int courseId;
	private String name;
	
	public Course() {
		this.courseId = -1;
		this.name = null;
	}
	
	public Course(int courseId, String name) {
		this.courseId = courseId;
		this.name = name;
	}
	
	public int getCourseId() {
		return courseId;
	}
	
	public void setCourseId(int courseId) {
		this.courseId = courseId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return courseId + " - " + name;
	}

}
