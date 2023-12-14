import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.StringSelection;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.FlowLayout;


import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.border.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileInputStream;
import java.io.IOException;

public class MyFrame extends JFrame implements ActionListener, KeyListener {
	
	JTextArea textArea;
	JLabel charCount;
	String text;
	
	public MyFrame() {
		setTitle("Notepad");
		setSize(600,400);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		initComponents();
	}
	
	private void initComponents() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu file = new JMenu("File");
		JMenu edit = new JMenu("Edit");
		
		JMenuItem newItem = new JMenuItem("New");
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.CTRL_MASK));
		
		JMenuItem openItem = new JMenuItem("Open");
		openItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.CTRL_MASK));
		
		JMenuItem saveItem = new JMenuItem("Save");
		saveItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.CTRL_MASK));
		
		JMenuItem cutItem = new JMenuItem("Cut");
		cutItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X, ActionEvent.CTRL_MASK));
		
		JMenuItem copyItem = new JMenuItem("Copy");
		copyItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C, ActionEvent.CTRL_MASK));
		
		JMenuItem pasteItem = new JMenuItem("Paste");
		pasteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V, ActionEvent.CTRL_MASK));
		
		JMenuItem deleteItem = new JMenuItem("Delete");
		deleteItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_DELETE, 0));
		
		JMenuItem selectAllItem = new JMenuItem("Select All");
		selectAllItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_A, ActionEvent.CTRL_MASK));
		
		file.add(newItem);
		file.add(openItem);
		file.add(saveItem);
		
		edit.add(cutItem);
		edit.add(copyItem);
		edit.add(pasteItem);
		edit.add(deleteItem);
		edit.add(selectAllItem);
		
		menuBar.add(file);
		menuBar.add(edit);
		
		add(menuBar, BorderLayout.NORTH);
		
		newItem.addActionListener(this);
		openItem.addActionListener(this);
		saveItem.addActionListener(this);
		
		cutItem.addActionListener(this);
		copyItem.addActionListener(this);
		pasteItem.addActionListener(this);
		deleteItem.addActionListener(this);
		selectAllItem.addActionListener(this);
		
		textArea = new JTextArea();
		textArea.addKeyListener(this);
		
		JScrollPane widthScroll = new JScrollPane(textArea);
		add(widthScroll, BorderLayout.CENTER);
		
		JPanel bottomPanel = new JPanel();
		charCount = new JLabel("Characters: 0    Words: 0");
		bottomPanel.add(charCount);
		add(bottomPanel, BorderLayout.SOUTH);
		
	}
	
	public static void main(String[] args) {
		MyFrame f = new MyFrame();
		f.setVisible(true);
	}
	
	@Override
	public void keyTyped(KeyEvent e) {
		String str = textArea.getText();
		int countChar = str.length()+1;
		int countWord = 1;
		int col = 1;
		int ln=1;
		String trimStr = str.trim();
		for(int i=0;i<trimStr.length();i++) {
			char curr = trimStr.charAt(i);
			if(curr == ' ' || curr == '\n') {
				countWord++;
			}
		}
		charCount.setText("Characters: "+countChar+"    Words: "+countWord);
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		
	}
	
	@Override
	public void keyReleased(KeyEvent e) {
		
	}
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		
		if(cmd.equalsIgnoreCase("New")) {
			textArea.setText("");
			charCount.setText("Characters: 0    Words: 0");
		}
		else if(cmd.equalsIgnoreCase("Open")) {
			openFile();
		}
		else if(cmd.equalsIgnoreCase("Save")) {
			saveFile();
		}
		else if(cmd.equalsIgnoreCase("Cut")) {
			text = textArea.getSelectedText();
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(text), null);
			textArea.replaceRange("", textArea.getSelectionStart(), textArea.getSelectionEnd());
		}
		else if(cmd.equalsIgnoreCase("Copy")) {
			text = textArea.getSelectedText();
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			clipboard.setContents(new StringSelection(text), null);
		}
		else if(cmd.equalsIgnoreCase("Paste")) {
			Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			Transferable transferable = clipboard.getContents(null);
			String text;
			try {
				text = (String) transferable.getTransferData(DataFlavor.stringFlavor);
				textArea.insert(text, textArea.getCaretPosition());
			} catch (UnsupportedFlavorException e1) {
				e1.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		else if(cmd.equalsIgnoreCase("Delete")) {
			textArea.replaceRange("", textArea.getSelectionStart(), textArea.getSelectionEnd());
		}
		else if(cmd.equalsIgnoreCase("Select All")) {
			textArea.selectAll();
		}
	}
	
	private void openFile() {
		JFileChooser fc = new JFileChooser();
		int optionChosen = fc.showOpenDialog(this);
		
		if(optionChosen == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			
			try {
				FileReader reader = new FileReader(f);
				BufferedReader br = new BufferedReader(reader);
				textArea.read(br, null);
				br.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void saveFile() {
		JFileChooser fc = new JFileChooser();
		int optionChosen = fc.showSaveDialog(this);
		
		if(optionChosen == JFileChooser.APPROVE_OPTION) {
			File f = fc.getSelectedFile();
			
			FileOutputStream fos;
			try {
				fos = new FileOutputStream(f);
				byte[] b = textArea.getText().getBytes();
				fos.write(b);
				fos.close();
			} catch(IOException e) {
				e.printStackTrace();
			}
		}
	}
}
