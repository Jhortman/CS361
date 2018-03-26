import javax.swing.*;
import javax.swing.event.ListSelectionListener;
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
	private JRadioButton _male;
	private JRadioButton _female;
	private JRadioButton _other;
	private JList _list;
    private DefaultListModel<String> _listModel;
	private JButton _submit = new JButton("Submit");
	private JButton _exit = new JButton("Exit");
	Directory dir = new Directory();
	
	
	public ClientGUI(){
		setTitle("Client");
        setSize(500,200);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
		
        
        createDisplay();
        
		setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	@SuppressWarnings("unchecked")
	public void createDisplay(){
		
		//Labels
		JLabel fName, lName, deptName, phone, genderM, genderF, genderOth;
		fName = new JLabel("First Name: ");
		lName = new JLabel("Last Name: ");
		deptName = new JLabel("Department Name: ");
		phone = new JLabel("Phone: ");
		genderM = new JLabel("Male");
		genderF = new JLabel("Female");
		genderOth = new JLabel("Other");
		_male = new JRadioButton();
		_female = new JRadioButton();
		_other = new JRadioButton();
		
		ButtonGroup group = new ButtonGroup();
		group.add(_male);
		group.add(_female);
		group.add(_other);
		
		JPanel submitExitPanel = new JPanel(new GridLayout(1,2));
		JPanel entryPanel = new JPanel(new GridLayout(4,2));
		JPanel typePanel = new JPanel(new GridLayout(4,2));
		JPanel type2Panel = new JPanel(new BorderLayout());
		
		_firstName = new JTextField(20);
		_lastName = new JTextField(20);
		_department = new JTextField(20);
		_phone = new JTextField(20);
		
		//Add buttons to button panel
		submitExitPanel.add(_submit);
		submitExitPanel.add(_exit);
		//Add Fields of entry information to entry panel
		entryPanel.add(fName);
		entryPanel.add(_firstName);
		
		entryPanel.add(lName);
		entryPanel.add(_lastName);
		
		entryPanel.add(deptName);
		entryPanel.add(_department);
	
		entryPanel.add(phone);
		entryPanel.add(_phone);
		
		//Add Person type to type panel
		typePanel.add(genderM);
		typePanel.add(_male);
		
		typePanel.add(genderF);
		typePanel.add(_female);
		
		typePanel.add(genderOth);
		typePanel.add(_other);
		
		//List Title (Mr./Ms./Mrs./Dr./Col./Prof.) Selection number: 1
		_listModel= new DefaultListModel<String>();
		_listModel.addElement("Ms.");
		_listModel.addElement("Mrs.");
		_listModel.addElement("Dr.");
		_listModel.addElement("Col.");
		_listModel.addElement("Prof.");
			
		_list = new JList<String>(_listModel); //data has type Object[]
		_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		_list.setLayoutOrientation(JList.VERTICAL);
		_list.setVisibleRowCount(-1);
		
				
		type2Panel.add(_list,BorderLayout.NORTH);
		
		//Add panels to Main Panel
		add(entryPanel,BorderLayout.CENTER);
		add(submitExitPanel,BorderLayout.SOUTH);
		add(typePanel,BorderLayout.WEST);
		add(type2Panel,BorderLayout.EAST);
		
		_submit.addActionListener(new ClickListener());
		_exit.addActionListener(new ClickListener());
		

	}
	
	class ClickListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) 
		{
			if(event.getSource().equals(_submit))
			{
				Employee newEmp = new Employee(_lastName.getText(),_firstName.getText(),_phone.getText(),_department.getText());
				dir.add(newEmp);
				_lastName.setText("");
				_firstName.setText("");
				_phone.setText("");
				_department.setText("");
				System.out.println("Employee Added");
			}
			if(event.getSource().equals(_exit))
			{
				System.exit(0);
			}
			
		}
	
	}
	public static void main(String[] args){
		
		new ClientGUI();
		
		
	}

}
