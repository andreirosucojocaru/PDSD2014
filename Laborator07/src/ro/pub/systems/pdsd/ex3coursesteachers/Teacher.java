package ro.pub.systems.pdsd.ex3coursesteachers;

public class Teacher {
	
	private int teacherId;
	private String name;
	
	public Teacher() {
		this.teacherId = -1;
		this.name = null;
	}
	
	public Teacher(int teacherId, String name) {
		this.teacherId = teacherId;
		this.name = name;
	}
	
	public int getTeacherId() {
		return teacherId;
	}
	
	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString() {
		return teacherId + " - " + name;
	}

}
