import java.awt.*;
import java.awt.event.*;

import javax.swing.*;


public class EventHandler extends JFrame {

	private JButton submit;
	private JButton exit;
	
	public EventHandler() {
		setTitle("T e s t");
		setSize(500, 500);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setResizable(false);
		setLocationRelativeTo(null);
		createComponents();
		setVisible(true);
	}
	
	public void createComponents() {
		
		JPanel panel1 = new JPanel();
		panel1.setLayout(new GridLayout(1, 2));

		submit = new JButton("Submit");
		exit = new JButton("Exit");
		
		panel1.add(submit);
		panel1.add(exit);

		add(panel1, BorderLayout.SOUTH);

		submit.addActionListener(new ClickListener());
		
		
		
		JPanel panel2 = new JPanel();

		panel2.setLayout(new BoxLayout(panel2, BoxLayout.PAGE_AXIS));

		JTextField firstName = new JTextField(20);
		JTextField lastName = new JTextField(20);
		JTextField department = new JTextField(20);
		JTextField phone = new JTextField(20);

		panel2.add(firstName);
		panel2.add(lastName);
		panel2.add(department);
		panel2.add(phone);
		
		add(panel2, BorderLayout.NORTH);
		
		
		// Define the panel to hold radioButtons
		JPanel panel3 = new JPanel();
		panel3.setLayout(new GridLayout(3, 1));

		JRadioButton jrB1 = new JRadioButton("Option1");
		JRadioButton jrB2 = new JRadioButton("Option2");
		JRadioButton jrB3 = new JRadioButton("Option3");

		// at this point we have created 3 radio buttons, but they are not
		// mutual exclusive(when clicking on one of them the others will not
		// unselected automatically)
		// To achieve the mutual exclusion, we should add them to a group so
		// when a user clicks on one of them, the others will be unselected.
		ButtonGroup group = new ButtonGroup();
		group.add(jrB1);
		group.add(jrB2);
		group.add(jrB3);

		// Now add them to the panel
		panel3.add(jrB1);
		panel3.add(jrB2);
		panel3.add(jrB3);

		add(panel3, BorderLayout.EAST);// adds the panel to the JFrame East
										// region, keep in mind that the
										// Default layout of the JFrame is
										// border Layout
	}
	
	class ClickListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e) {
			if(((JButton)e.getSource())==(submit))
			{
				System.out.println("asdf");
			}
		}

	}
}
