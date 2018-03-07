
public class Employee {
	private String _lastName;
	private String _firstName;
	private String _phone;
	private String _department;
	
	public Employee(String last, String first, String num, String depart){
		_lastName = last;
		_firstName = first;
		_phone = num;
		_department = depart;
	}
	
	public void setLast(String last){
		_lastName = last;
	}

	public void setFirst(String first){
		_firstName = first;
	}
	public void setPhone(String phone){
		_phone = phone;
	}
	public void setDepart(String depart){
		_department = depart;
	}
	
	public String toString(){
		
		return _lastName + ", " + _firstName + " " + _phone + " " + _department;
	}
}
