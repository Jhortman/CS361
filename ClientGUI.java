import javax.swing.*;
import javax.swing.plaf.metal.MetalLookAndFeel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
public class ClientGUI extends JFrame {
	
	private String test;
	private JTextField _firstName;
	private JTextField _lastName;
	private JTextField _department;
	private JTextField _phone;
	
	public ClientGUI(){
		setTitle("Client");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		
        
        createDisplay();
        
		setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	public void createDisplay(){
		
		JPanel jOne = new JPanel();
		jOne.setLayout(new GridLayout(10,1));
		add(jOne,BorderLayout.WEST);
		jOne.setSize(200,200);
		
		
		jOne.add(new JLabel("First Name"));
		_firstName = new JTextField(20);
		_firstName.getText();
		jOne.add(_firstName);
		
		jOne.add(new JLabel("Last Name"));
		_lastName = new JTextField(20);
		jOne.add(_lastName);
		
		jOne.add(new JLabel("Department Name"));
		_department = new JTextField(20);
		jOne.add(_department);
		
		jOne.add(new JLabel("Phone"));
		_phone = new JTextField(20);
		jOne.add(_phone);
		
		
		for(int i = 0; i < 6; i++){
			jOne.add(new JLabel(""));
		}
		
		
		
		
		
		
		
		
		
		
	/*		JPanel jTwo = new JPanel();
		jOne.setLayout(new GridLayout(2,2));
		add(jTwo,BorderLayout.SOUTH);
		
		jTwo.add(new JButton());
		jTwo.add(new JMenu());
		jTwo.add(new JButton());
		jTwo.add(new JButton());
		
		
		
		*/
		
		
		
	}
	
	public class TextListener implements ActionListener{
		private String name;
		
		public TextListener(String name){
			
		}
	
		public void actionPerformed(ActionEvent e){
			
			
		}
	
	}
	public static void main(String[] args){
		
		new ClientGUI();
		
	}

}
