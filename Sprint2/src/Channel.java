
public class Channel {
	private boolean _state;
	private int _number;
	//private String _sensor; //todo
	
	public Channel(int i) {
		_state = false;
		_number = i + 1;
		//_sensor = null;
	}
	public void toggleState() {
		_state = !_state;
	}
	public void setSensor(String s) {
	//	_sensor = s;
	}
	public boolean getState() {
		return this._state;
	}
	public int getNum() {
		return this._number;
	}
	public void disconnect() {
		//_sensor = null;
	}
}
