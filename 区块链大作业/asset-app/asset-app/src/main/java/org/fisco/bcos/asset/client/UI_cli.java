package org.fisco.bcos.asset.client;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.math.BigInteger;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
//import javax.xml.soap.Text;

public class UI_cli extends JFrame{
	Boolean ispain=false;
	Test_control client = new Test_control();
	//AssetClient client;
	public UI_cli() {
		super("client");
		this.setSize(800, 400);
		this.setBackground(Color.WHITE);
		this.setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//完成窗口的创建
		ispain=false;
		init(this.getGraphics());
		this.setVisible(true);
		
		try {
			//client.initialize();
			client = new Test_control();
			client.initialize();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	void addSearch() {
		JPanel jp1 = new JPanel();
		jp1.setLayout(new FlowLayout());
		JTextField jt = new JTextField();
		jt.setText("");//设置文本
		jt.setFont(new Font("黑体",Font.PLAIN,20));//设置字体格式
		jt.setColumns(20);
		JLabel l1=new JLabel("search for bills, input the name: ");
		JButton jb = new JButton("Do");
		
		jp1.add(l1);
		jp1.add(jt);
		jp1.add(jb);
		
		this.add(jp1);
		JPanel jp2 = new JPanel();
		JLabel jt2 = new JLabel("The result: ");
		JLabel re = new JLabel("");
		jp2.add(jt2);
		jp2.add(re);

		this.add(jp2);
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String s=new String();
				s=client.getUserBillMoney(jt.getText());
				String u=client.getUserBillFromUser(jt.getText());
				System.out.println("get user account: "+s+" and the user "+u);//获取文本框中的文本值
				//jt.setText("");//清空文本框的内容
				//jt.requestFocus();//获取焦点
				re.setText(s+" and the Arrears party is "+u);
			}
		});
		
	}
	
	void addHuan() {
		JPanel jp1 = new JPanel();
		jp1.setLayout(new FlowLayout());
		JTextField jt = new JTextField();
		jt.setText("");//设置文本
		jt.setColumns(10);
		JLabel la1=new JLabel("Put in your name to pay back: ");
		JButton jb = new JButton("Do");
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.payback(jt.getText());
				System.out.println("pay back");
			}
		});
		jp1.add(la1);
		jp1.add(jt);
		jp1.add(jb);
		this.add(jp1);
	}
	
	public void init(Graphics g) {
		this.setLayout(new GridLayout(7,3));
		addDeploy();
		addSearch();
		addTransfer();
		addHuan();
	}
	
	void addDeploy() {
		JPanel p = new JPanel();
		p.setLayout(new FlowLayout());
		JButton jb = new JButton("Deploy the contract");
		p.add(jb);
		this.add(p);
		
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("deploy");//获取文本框中的文本值
				client.deployAssetAndRecordAddr();
				//jt.setText("");//清空文本框的内容
				//jt.requestFocus();//获取焦点
				//client.deployAssetAndRecordAddr();
			}
		});
	}
	
	void addTransfer() {
		JPanel jp2 = new JPanel();
		JLabel l1 = new JLabel("transfer accounts");
		jp2.add(l1);
		this.add(jp2);
		
		JPanel jp1 = new JPanel();
		JLabel l2 = new JLabel("put in the user: ");
		JTextField jt1 = new JTextField("");
		jt1.setColumns(10);
		JLabel l3 = new JLabel("to the user: ");
		JTextField jt2 = new JTextField("");
		jt2.setColumns(10);
		JLabel j3 = new JLabel("transfer money: ");
		JTextField jt3 = new JTextField("");
		jt3.setColumns(10);
		
		JButton jb = new JButton("Do");
		
		jb.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("transfer");//获取文本框中的文本值
				client.tranBills(jt1.getText(), jt2.getText(),new BigInteger(jt3.getText()));
			}
		});
		
		jp1.add(l2);
		jp1.add(jt1);
		jp1.add(l3);
		jp1.add(jt2);
		jp1.add(j3);
		jp1.add(jt3);
		jp1.add(jb);
		this.add(jp1);
	}
	
	public static void main(String[] args) {
		UI_cli a=new UI_cli();
		
	}
}
