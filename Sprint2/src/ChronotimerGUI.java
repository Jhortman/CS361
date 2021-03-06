import javax.swing.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ChronotimerGUI extends JFrame {
	
	// Initialize the buttons
	private JRadioButton _ch1 = new JRadioButton();
	private JRadioButton _ch2 = new JRadioButton();
	private JRadioButton _ch3 = new JRadioButton();
	private JRadioButton _ch4 = new JRadioButton();
	private JRadioButton _ch5 = new JRadioButton();
	private JRadioButton _ch6 = new JRadioButton();
	private JRadioButton _ch7 = new JRadioButton();
	private JRadioButton _ch8 = new JRadioButton();
	private JButton _b1 = new JButton("");
	private JButton _b2 = new JButton("");
	private JButton _b3 = new JButton("");
	private JButton _b4 = new JButton("");
	private JButton _b5 = new JButton("");
	private JButton _b6 = new JButton("");
	private JButton _b7 = new JButton("");
	private JButton _b8 = new JButton("");
	private JButton _kp1 = new JButton("1");
	private JButton _kp2 = new JButton("2");
	private JButton _kp3 = new JButton("3");
	private JButton _kp4 = new JButton("4");
	private JButton _kp5 = new JButton("5");
	private JButton _kp6 = new JButton("6");
	private JButton _kp7 = new JButton("7");
	private JButton _kp8 = new JButton("8");
	private JButton _kp9 = new JButton("9");
	private JButton _kp0 = new JButton("0");
	private JButton _kpS = new JButton("*");
	private JButton _kpP = new JButton("#");
	private JButton _power = new JButton("POWER");
	private JButton _printerPower = new JButton("PRINTER POWER");	
	private JButton _swap = new JButton("SWAP");
	private JTextArea printerTextArea = new JTextArea(5, 20);
	
	private Chronotimer _c = new Chronotimer();
	
	public static void main(String[] args){
		new ChronotimerGUI();
	}
	
	public ChronotimerGUI(){		
		setTitle("CHRONOTIMER 1009");
        setSize(800, 400);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        
        createDisplay();
        
		setResizable(true);
        setLocationRelativeTo(null);
        setVisible(true);
	}
	
	public void createDisplay(){
		// Panels north and south, to be applied to the main window
		JPanel north = new JPanel(new GridLayout(1, 3));
		JPanel south = new JPanel(new GridLayout(1, 3));

		// Initializing labels
		JLabel start, finish, activated, activated2, function, timerLabel, dummy, dummy2,
		num1, num2, num3, num4, num5, num6, num7, num8;
		start = new JLabel("Start");
		finish = new JLabel("Finish");
		activated = new JLabel("Enable/Disable");
		activated2 = new JLabel("Enable/Disable");
		function = new JLabel("FUNCTION");
		timerLabel = new JLabel("Queue / Running / Final Time");
		dummy = new JLabel("");
		dummy2 = new JLabel("");
		num1 = new JLabel("1");	num2 = new JLabel("2");
		num3 = new JLabel("3");	num4 = new JLabel("4");
		num5 = new JLabel("5");	num6 = new JLabel("6");
		num7 = new JLabel("7");	num8 = new JLabel("8");

		// North-center - channel panel
		JPanel channelsPanel = new JPanel(new GridLayout(6, 5));		
		channelsPanel.add(dummy);	channelsPanel.add(num1);		channelsPanel.add(num3);
		channelsPanel.add(num5);	channelsPanel.add(num7);		channelsPanel.add(start);
		channelsPanel.add(_b1);		channelsPanel.add(_b3);			channelsPanel.add(_b5);
		channelsPanel.add(_b7);		channelsPanel.add(activated);	channelsPanel.add(_ch1);
		channelsPanel.add(_ch3);	channelsPanel.add(_ch5);		channelsPanel.add(_ch7);
		channelsPanel.add(dummy2);	channelsPanel.add(num2);		channelsPanel.add(num4);
		channelsPanel.add(num6);	channelsPanel.add(num8);		channelsPanel.add(finish);
		channelsPanel.add(_b2);		channelsPanel.add(_b4);			channelsPanel.add(_b6);
		channelsPanel.add(_b8);		channelsPanel.add(activated2);	channelsPanel.add(_ch2);
		channelsPanel.add(_ch4);	channelsPanel.add(_ch6);		channelsPanel.add(_ch8);
		
		// Northeast - printer panel + printer text field
		JPanel printerPanel = new JPanel(new GridLayout(2, 1));
		
		JPanel printerField = new JPanel();
		printerField.setLayout(new BoxLayout(printerField, BoxLayout.X_AXIS));
		printerTextArea.setBorder(BorderFactory.createLineBorder(Color.DARK_GRAY, 1, true));
		printerTextArea.setEditable(false);
		JScrollPane scrollPane = new JScrollPane(printerTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,  JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		
		printerField.add(scrollPane);
		
		printerPanel.add(_printerPower, BorderLayout.NORTH);
		printerPanel.add(printerField, BorderLayout.CENTER);
		
		// Northwest - power button
		north.add(_power, BorderLayout.WEST);
		
		north.add(channelsPanel, BorderLayout.CENTER);
		north.add(printerPanel, BorderLayout.EAST);
		
		// Southeast - keypad
		JPanel keypadPanel = new JPanel(new GridLayout(4,3));
		keypadPanel.add(_kp1);	keypadPanel.add(_kp2);	keypadPanel.add(_kp3);
		keypadPanel.add(_kp4);	keypadPanel.add(_kp5);	keypadPanel.add(_kp6);
		keypadPanel.add(_kp7);	keypadPanel.add(_kp8);	keypadPanel.add(_kp9);
		keypadPanel.add(_kpS);	keypadPanel.add(_kp0);	keypadPanel.add(_kpP);
		
		// South-center - text field for racing times
		JPanel raceTimesPanel = new JPanel(new GridLayout(2, 1));
		JTextArea raceTimesField = new JTextArea(10, 20);
		raceTimesField.setEditable(false);
		
		raceTimesPanel.add(raceTimesField, BorderLayout.CENTER);
		raceTimesPanel.add(timerLabel, BorderLayout.SOUTH);
		
		// Southwest - swap button
		south.add(_swap, BorderLayout.WEST);
		
		south.add(raceTimesPanel, BorderLayout.CENTER);
		south.add(keypadPanel, BorderLayout.EAST);
		
		// Add north and south panels to the main window
		
		add(north, BorderLayout.NORTH);
		add(south, BorderLayout.CENTER);
		
		// Add action listeners
		_power.addActionListener(new ClickListener());
 		_b1.addActionListener(new ClickListener());
		_b2.addActionListener(new ClickListener());
		_b3.addActionListener(new ClickListener());
		_b4.addActionListener(new ClickListener());
		_b5.addActionListener(new ClickListener());
		_b6.addActionListener(new ClickListener());
		_b7.addActionListener(new ClickListener());
		_b8.addActionListener(new ClickListener());
		
		// Add action Listeners for channel radio buttons 
		_ch1.addActionListener(new ClickListener());
		_ch2.addActionListener(new ClickListener());
		_ch3.addActionListener(new ClickListener());
		_ch4.addActionListener(new ClickListener());
		_ch5.addActionListener(new ClickListener());
		_ch6.addActionListener(new ClickListener());
		_ch7.addActionListener(new ClickListener());
		_ch8.addActionListener(new ClickListener());
		
		//Add action listener for numpad buttons
		_kp1.addActionListener(new numListener());
		_kp2.addActionListener(new numListener());
		_kp3.addActionListener(new numListener());
		_kp4.addActionListener(new numListener());
		_kp5.addActionListener(new numListener());
		_kp6.addActionListener(new numListener());
		_kp7.addActionListener(new numListener());
		_kp8.addActionListener(new numListener());
		_kp9.addActionListener(new numListener());
		_kpS.addActionListener(new numListener());
		_kp0.addActionListener(new numListener());
		_kpP.addActionListener(new numListener());
		
		_swap.addActionListener(new ClickListener());
		
	}
	
	class ClickListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) 
		{
			// POWER
		 	if(event.getSource().equals(_power)) _c.COMMANDS("POWER");
		 	
		 	// START
		 	else if(event.getSource().equals(_b1)) _c.COMMANDS("TRIG 1");
		 	else if(event.getSource().equals(_b3)) _c.COMMANDS("TRIG 3");
		 	else if(event.getSource().equals(_b5)) _c.COMMANDS("TRIG 5");
		 	else if(event.getSource().equals(_b7)) _c.COMMANDS("TRIG 7");
		 	
		 	// FINISH
		 	else if(event.getSource().equals(_b2)) _c.COMMANDS("TRIG 2");
		 	else if(event.getSource().equals(_b4)) _c.COMMANDS("TRIG 4");
		 	else if(event.getSource().equals(_b6)) _c.COMMANDS("TRIG 6");
		 	else if(event.getSource().equals(_b8)) _c.COMMANDS("TRIG 8");
		 	
		 	// ENABLE | Disable
		 	else if(event.getSource().equals(_ch1))	_c.COMMANDS("TOG 1");
		 	else if(event.getSource().equals(_ch2)) _c.COMMANDS("TOG 2");
		 	else if(event.getSource().equals(_ch3)) _c.COMMANDS("TOG 3");
		 	else if(event.getSource().equals(_ch4)) _c.COMMANDS("TOG 4");
		 	else if(event.getSource().equals(_ch5)) _c.COMMANDS("TOG 5");
		 	else if(event.getSource().equals(_ch6)) _c.COMMANDS("TOG 6");
		 	else if(event.getSource().equals(_ch7)) _c.COMMANDS("TOG 7");
		 	else if(event.getSource().equals(_ch8)) _c.COMMANDS("TOG 8");
		 	
		 	//Swap - TODO
		 	
		 	if(event.getSource().equals(_swap)) _c.COMMANDS("SWAP");
		 	
		}
	}	
	
	class numListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent event) 
		{
			
			if(event.getSource().equals(_kp1)) printerTextArea.append("1");
			if(event.getSource().equals(_kp2)) printerTextArea.append("2");
			if(event.getSource().equals(_kp3)) printerTextArea.append("3");
			if(event.getSource().equals(_kp4)) printerTextArea.append("4");
			if(event.getSource().equals(_kp5)) printerTextArea.append("5");
			if(event.getSource().equals(_kp6)) printerTextArea.append("6");
			if(event.getSource().equals(_kp7)) printerTextArea.append("7");
			if(event.getSource().equals(_kp8)) printerTextArea.append("8");
			if(event.getSource().equals(_kp9)) printerTextArea.append("9");
			if(event.getSource().equals(_kpS)) printerTextArea.append("*");
			if(event.getSource().equals(_kp0)) printerTextArea.append("0");
			
			
			
			if(event.getSource().equals(_kpP)) {
				//create racer object and then add it to queue then clear text area
				//check for numbers out of range [0,9999]
				printerTextArea.setText("");
			}
			
			
			//TODO
		}
	}		
}