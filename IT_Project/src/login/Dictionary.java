package login;

import java.sql.*;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;

import java.awt.Font;
import java.awt.Color;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;

import com.mysql.jdbc.ResultSet;
import com.mysql.jdbc.ResultSetMetaData;

import codes.dbconnect;
import javax.swing.JScrollPane;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import java.awt.Cursor;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;


public class Dictionary {
	JFrame frame;
	private JTextField word_textField;
	Connection conn = null;
	Statement stmt = null;
	private JTable table;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Dictionary window = new Dictionary();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Dictionary() {
		initialize();
		conn = dbconnect.connect();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setResizable(false);
		frame.getContentPane().setBackground(new Color(0, 0, 0));
		frame.setBounds(500, 100, 785, 571);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JButton btnNewButton = new JButton("HOME");
		btnNewButton.setForeground(new Color(255, 255, 255));
		btnNewButton.setBackground(new Color(255, 0, 128));
		btnNewButton.setFocusable(false);
		btnNewButton.setFont(new Font("Tahoma", Font.BOLD, 10));
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainWindow mainwindow = new MainWindow();
				mainwindow.frame.setVisible(true);
				frame.dispose();
			}
		});
		btnNewButton.setBounds(10, 10, 75, 28);
		frame.getContentPane().add(btnNewButton);

		JLabel lblNewLabel = new JLabel("MY DICTIONARY");
		lblNewLabel.setForeground(new Color(255, 255, 255));
		lblNewLabel.setHorizontalAlignment(SwingConstants.CENTER);
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 40));
		lblNewLabel.setBounds(116, 0, 558, 59);
		frame.getContentPane().add(lblNewLabel);

		final JTextArea meaning_textarea = new JTextArea();
		meaning_textarea.setToolTipText("Type the meaning");
		meaning_textarea.setBorder(new BevelBorder(BevelBorder.LOWERED, null, null, null, null));
		meaning_textarea.setWrapStyleWord(true);
		meaning_textarea.setForeground(new Color(0, 0, 0));
		meaning_textarea.setBounds(392, 107, 370, 188);
		frame.getContentPane().add(meaning_textarea);
		meaning_textarea.setLineWrap(true);
		meaning_textarea.setFont(new Font("Tahoma", Font.BOLD, 20));
		meaning_textarea.setBackground(new Color(211, 211, 211));

		JLabel lblNewLabel_1 = new JLabel("YOUR WORD");
		lblNewLabel_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1.setBounds(10, 69, 233, 28);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_1_1 = new JLabel("SEARCH RESULTS");
		lblNewLabel_1_1.setForeground(new Color(255, 255, 255));
		lblNewLabel_1_1.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_1.setBounds(10, 326, 233, 28);
		frame.getContentPane().add(lblNewLabel_1_1);

		word_textField = new JTextField();
		word_textField.addKeyListener(new KeyAdapter() {

			public void keyReleased(KeyEvent e) {
				try {
					String nameTextField = word_textField.getText();
					Statement stmt = conn.createStatement();
					ResultSet rs = (ResultSet) stmt.executeQuery("SELECT * FROM dictionary WHERE word LIKE '%" + nameTextField + "%' ORDER BY word");
					ResultSetMetaData rsmd = (ResultSetMetaData) rs.getMetaData();
					DefaultTableModel model = (DefaultTableModel) table.getModel();

					if (nameTextField.trim().length() == 0) {
						table.setModel(new DefaultTableModel());
					} else {
						int cols = rsmd.getColumnCount();
						String[] colName = new String[cols];
						for (int i = 0; i < cols; i++) {
							colName[i] = rsmd.getColumnName(i + 1);
						}
						model.setColumnIdentifiers(colName);

						String name, meaning;
						while (rs.next()) {
							name = rs.getString(1);
							meaning = rs.getString(2);
							String[] row = { name, meaning };
							model.addRow(row);
						}
					}

				} catch (SQLException er) {
					JOptionPane.showMessageDialog(null, er);
				}

			}

			public void keyPressed(KeyEvent e) {
				table.setModel(new DefaultTableModel());
			}
		});
		word_textField.setToolTipText("Type your word ");
		word_textField.setForeground(new Color(0, 0, 0));
		word_textField.setBackground(new Color(211, 211, 211));
		word_textField.setFont(new Font("Tahoma", Font.BOLD, 20));
		word_textField.setBounds(10, 107, 353, 43);
		frame.getContentPane().add(word_textField);
		word_textField.setColumns(10);

		JPanel panel = new JPanel();
		panel.setBounds(10, 175, 161, 43);
		frame.getContentPane().add(panel);
		panel.setLayout(null);

		JButton search_btn = new JButton("SEARCH");
		search_btn.setFocusable(false);
		search_btn.setBounds(0, 0, 161, 43);
		panel.add(search_btn);
		search_btn.setForeground(Color.WHITE);
		search_btn.setBackground(Color.BLUE);
		search_btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = word_textField.getText();

				try {
					System.out.print(word);
					if (word.trim().length() != 0) {
						String sql = "SELECT * FROM dictionary WHERE word = '" + word + "'";
						stmt = conn.createStatement();
						ResultSet rs = (ResultSet) stmt.executeQuery(sql);

						if (rs.next()) {
							meaning_textarea.setText(rs.getString("meaning"));
						} else {
							JOptionPane.showMessageDialog(null, "This word is not in the database !");
						}
					} else {
						JOptionPane.showMessageDialog(null, "You need to type a word first !");
					}

				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "somthing wrong !");
				}
			}
		});
		search_btn.setFont(new Font("Tahoma", Font.BOLD, 20));

		JTextArea textArea_1 = new JTextArea();
		textArea_1.setEditable(false);
		textArea_1.setBackground(new Color(255, 128, 64));
		textArea_1.setBounds(-16, 0, 788, 59);
		frame.getContentPane().add(textArea_1);

		JButton btnAdd = new JButton("ADD");
		btnAdd.setFocusable(false);
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = word_textField.getText();
				String meaning = meaning_textarea.getText();

				try {
					String sql0 = "SELECT * FROM dictionary WHERE word = '" + word + "'";
					stmt = conn.createStatement();
					ResultSet rs = (ResultSet) stmt.executeQuery(sql0);

					if (rs.next()) {
						JOptionPane.showMessageDialog(null,
								"This word is already in the data set.\nYou can not add this.\nYou can only update it.");
					} else {
						if (meaning.trim().length() != 0 & word.trim().length() != 0) {
							String sql = "INSERT INTO dictionary(word,meaning) VALUES ('" + word + "','" + meaning+ "') ";
							stmt = conn.createStatement();
							stmt.executeUpdate(sql);
							JOptionPane.showMessageDialog(null, "successfully added to the database !");
							word_textField.setText("");
							meaning_textarea.setText("");
							word_textField.requestFocus();
						} else if (word.trim().length() == 0) {
							JOptionPane.showMessageDialog(null, "You need to type a word !");
						} else {
							JOptionPane.showMessageDialog(null, "You need to type the meaning !");
						}

					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Something wrong !");
				}

			}
		});
		btnAdd.setForeground(Color.WHITE);
		btnAdd.setBackground(new Color(0, 128, 255));
		btnAdd.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnAdd.setBounds(202, 175, 161, 43);
		frame.getContentPane().add(btnAdd);

		JButton btnDelete = new JButton("DELETE");
		btnDelete.setFocusable(false);
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = word_textField.getText();

				try {
					String sql0 = "SELECT * FROM dictionary WHERE word = '" + word + "'";
					stmt = conn.createStatement();
					ResultSet rs = (ResultSet) stmt.executeQuery(sql0);

					if (rs.next()) {
						String sql = "DELETE FROM dictionary where word = '" + word + "'";
						stmt = conn.createStatement();
						stmt.executeUpdate(sql);
						JOptionPane.showMessageDialog(null, "successfully deleted\n from the database !");
						word_textField.setText("");
						meaning_textarea.setText("");
						word_textField.requestFocus();

					} else {
						if (word.trim().length() == 0) {
							JOptionPane.showMessageDialog(null, "You need to type a word !");
						} else {
							JOptionPane.showMessageDialog(null, "This word is not in the database !");
						}

					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Something wrong !");
				}
				table.setModel(new DefaultTableModel());
			}
		});
		btnDelete.setForeground(Color.WHITE);
		btnDelete.setBackground(new Color(255, 0, 0));
		btnDelete.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnDelete.setBounds(10, 252, 161, 43);
		frame.getContentPane().add(btnDelete);

		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
		scrollPane_1.setBounds(10, 355, 752, 169);
		frame.getContentPane().add(scrollPane_1);

		table = new JTable();
		table.setFont(new Font("Tahoma", Font.PLAIN, 15));
		table.setRowHeight(20);
		scrollPane_1.setViewportView(table);

		JLabel lblNewLabel_1_2 = new JLabel("MEANING");
		lblNewLabel_1_2.setForeground(Color.WHITE);
		lblNewLabel_1_2.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblNewLabel_1_2.setBounds(392, 69, 233, 28);
		frame.getContentPane().add(lblNewLabel_1_2);

		JButton btnUpdate = new JButton("UPDATE");
		btnUpdate.setFocusable(false);
		btnUpdate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String word = word_textField.getText();
				String meaning = meaning_textarea.getText();

				try {
					String sql0 = "SELECT * FROM dictionary WHERE word = '" + word + "'";
					stmt = conn.createStatement();
					ResultSet rs = (ResultSet) stmt.executeQuery(sql0);

					if (rs.next()) {
						if (meaning.trim().length() != 0) {
							String sql = "UPDATE dictionary set meaning = '" + meaning + "' WHERE word = '" + word
									+ "'";
							stmt = conn.createStatement();
							stmt.executeUpdate(sql);
							JOptionPane.showMessageDialog(null, "successfully updated the database!");
							word_textField.setText("");
							meaning_textarea.setText("");
							word_textField.requestFocus();
						} else {
							JOptionPane.showMessageDialog(null, "Enter the new meaning !");
						}
					} else {
						if (word.trim().length() == 0) {
							JOptionPane.showMessageDialog(null, "You need to type a word !");
						} else {
							JOptionPane.showMessageDialog(null, "This word is not in the database !");
						}
					}
				} catch (Exception e2) {
					JOptionPane.showMessageDialog(null, "Something wrong !");
				}
				table.setModel(new DefaultTableModel());
			}
		});
		btnUpdate.setForeground(Color.WHITE);
		btnUpdate.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnUpdate.setBackground(new Color(255, 128, 0));
		btnUpdate.setBounds(202, 252, 161, 43);
		frame.getContentPane().add(btnUpdate);
	}
}
