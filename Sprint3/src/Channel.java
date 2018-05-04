
public class Channel {
	private boolean _state;
	private int _number;
	private String _sensor; 
	
	public Channel(int i) {
		_state = false;
		_number = i + 1;
		_sensor = "NONE";
	}
	public void toggleState() {
		_state = !_state;
	}
	public void setSensor(String s) {
		_sensor = s;
	}
	public boolean getState() {
		return this._state;
	}
	public int getNum() {
		return this._number;
	}
	public String getSensor() {
		return _sensor;
	}
	public void disconnect() {
		_sensor = "NONE";
	}
}
