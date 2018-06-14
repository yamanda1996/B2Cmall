package cn.e3mall.freemarker;

public class Student {
	
	private String name;
	private int age;
	private long id;
	private String address;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public Student(String name, int age, long id, String address) {
		super();
		this.name = name;
		this.age = age;
		this.id = id;
		this.address = address;
	}
	public Student() {
		super();
	}
	

}
