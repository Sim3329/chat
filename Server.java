import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.security.InvalidKeyException;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.awt.event.ActionEvent;
import java.io.PrintWriter;
import java.awt.Color;
import java.awt.Container;

public class Server extends JFrame implements ActionListener{
	
	public JPanel contentPane;
	public JTextArea textArea = new JTextArea();
	public JTextArea textArea_1 = new JTextArea();
	public JTextArea textArea_2 = new JTextArea();
	public JTextArea textArea_3 = new JTextArea();
	public JTextArea textArea_4 = new JTextArea();
	public JTextArea textArea_6 = new JTextArea();
	public static JTextArea textArea_5 = new JTextArea();	
	public JRadioButton Clientbtn = new JRadioButton("Client");
	public JRadioButton Serverbtn = new JRadioButton("Server");
	public JButton btnConnect = new JButton("Connect");
	public JButton btnDisconnect = new JButton("Disconnect");
	public JButton btnSend = new JButton("send");
	public JButton btnKeyGeneration = new JButton("Key generation");
	public JButton btnSaveInt = new JButton("save into a file");
	public JButton btnSendPublicKey = new JButton("send public key");
	public JButton btnNewButton = new JButton("load from a file");
	public ObjectOutputStream os;
	public ObjectInputStream is;
	private ServerSocket server_socket =null;
	private Socket socket=null;
	private String ip = "";
	private int port;
	private String msg="";
	Client client = new Client();
	Server1 server1 = new Server1();
	Sender sender = null;
	Receiver receiver = null;
	Object obj = null;
	private Container contendPane;
	public PublicKey publicKey = null;
	public PrivateKey privateKey = null;
	public PublicKey pKey = null;
	private KeyPair keyPair = null;
	
	
	//public String chattext = null;
	private final JScrollPane scrollPane = new JScrollPane();
	private final JScrollPane scrollPane_1 = new JScrollPane();
	RSAEncryption rsaEncryption;

	private byte[] Encrypttext = null;
	
	
	
	/**
	 * Launch the application.
	 */
	
	
	
	Server(){
		
		UI(); //method of create screen
	
		
	}
	public class Sender  {  //class of sending
		
		private Socket socket;
		ObjectOutputStream os = null;  //generate object of class(ObjectoutputStream) 
		public Sender() {}
		public Sender(Socket socket){ 
			this.socket = socket;
				
			try {
				os = new ObjectOutputStream(socket.getOutputStream()); //input data empty object'os'
				
			}catch(IOException e) {
				
				e.printStackTrace();
			}
			
			
				
			}
		public void Sendmessage(String str){//method of sending a message
			try {
				byte[] text = str.getBytes();  //byte of message
				Encrypttext = rsaEncryption.Encryption(pKey, text); //encrypt message 
				os.writeObject(Encrypttext);//write encrypt
				os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		
		public void sendKey(PublicKey key) { //method of sending a Key
			try {
				os.writeObject(key);
				os.flush();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}
		}

		
	
	public class Receiver extends Thread{ //class of receive
	
		private Socket socket;
		Receiver(Socket socket){
			this.socket = socket;
		}
		ObjectInputStream is= null;
		public void run() {
			super.run();
			try {
				is = new ObjectInputStream(socket.getInputStream()); //generate object of class(ObjectoutputStream) 
				
				while(true) {
					obj = is.readObject();

					if(is == null) {
						System.out.println("상대방연결끊김\n");
						break;
					}
					
					try {
						
						//System.out.println("sfsfsf1");
						//if(pKey==null) {
						pKey = (PublicKey)obj;   //transfer type of object
						//System.out.println("sfsfsf2");
						textArea_5.append("\n=== RSA Key Generation ===");
						byte[] pubk = pKey.getEncoded();   //input encoded key information into byte[]pubk
				        textArea_5.append("\n Public Key : ");
				        for(byte b: pubk) { 
				        
				        	textArea_5.append(String.format("%02X" , b));
				        
				        }
				        textArea_5.append("\n Public Key Length : "+pubk.length+ " byte");
				        
					} catch (Exception e) {
						
						e.printStackTrace();

						Encrypttext = (byte[])obj;	//transfered object information into Encrypttext object
						String ccc = new String(Encrypttext);
						textArea_3.append("\n\n Ciphertext:");	
						
						textArea_3.append(ccc);
						
						textArea_3.append("\n Ciphertext length: " + Encrypttext.length+"byte"+"\n");
						
						byte [] Decrypttext = rsaEncryption.Decryption(privateKey, Encrypttext);//decrypt encrypted text.
						textArea_3.append("\n Recovered Plaintext\n");
						String abcd = new String(Decrypttext);
						textArea_3.append("상대방: " + abcd+"\n");

					}
						
					
					
				}
				
				is.close();
				
			}catch(IOException e) {
				e.printStackTrace();
			} catch (ClassNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
	}
		
		
	}
	
	
	public class Client implements ActionListener { // class of client
		
		
	
		
		private void start() {
			btnDisconnect.addActionListener(this);
			btnConnect.addActionListener(this);
			btnSend.addActionListener(this);
			btnKeyGeneration.addActionListener(this);
			btnSaveInt.addActionListener(this);
			btnSendPublicKey.addActionListener(this);
			
		}
		
		public void Network() {
			
			
					try {
						socket = new Socket(ip,port);	//generate socket
						
						textArea_2.append("서버와연결되었습니다!\n");
						
						sender = new Sender(socket);
						receiver = new Receiver(socket);
						
						
						receiver.start(); //start tread
						
						
						
						
						
						/*if(socket != null) {
							Connection();
						}*/
						
					} catch (UnknownHostException e) {
						
						e.printStackTrace();
					} catch (IOException e) {
						
						e.printStackTrace();
					}
					
				}
		
		public void actionPerformed(ActionEvent c) {//button action
			
			if(c.getSource() == btnConnect) {
				System.out.println("click");// 디버깅체
				ip= textArea_1.getText().trim(); 
				port = Integer.parseInt(textArea.getText().trim());
				 
				client.Network(); //if click the btnConnect, start client.Networ()
				
			}
			else if(c.getSource()==btnSend) {
			//	System.out.println("send작동 \n");
				
				msg = textArea_4.getText().trim();
				textArea_3.append(msg);
				sender.Sendmessage(msg);//if click the btnSend, start sendmessage()
			}
			
			else if(c.getSource()==btnKeyGeneration) { 
				rsaEncryption = new RSAEncryption();
				
					keyPair = rsaEncryption.GenerateKey();
					
					 
					 publicKey = keyPair.getPublic();
					 privateKey = keyPair.getPrivate();
					 byte[] pubk = publicKey.getEncoded();
					 byte[] prik = privateKey.getEncoded();
					 textArea_5.append("\n=== RSA Key Generation ===\n");
					 textArea_5.append("\n Public Key : ");
					 for(byte b: pubk) { 
					       textArea_5.append(String.format("%02X" , b));
					 }
					 textArea_5.append("\n Public Key Length : "+pubk.length+ " byte");
					 textArea_5.append("\n Private Key : ");
					 
					 for(byte b: prik) {
					        
					        textArea_5.append(String.format("%02X" , b));
					 }
					 textArea_5.append("\n Private Key Length : "+prik.length+ " byte" ); //if click the btnkeyGeneration, start generating key.
				
			}
			
			else if(c.getSource()==btnSendPublicKey) {
				
				sender.sendKey(publicKey); // if click the btnSendPublickkey, start sendkey()
			}
			else if(c.getSource() == btnDisconnect) {
				System.out.println("연결중단 ");
			}
			
		}
			
	}
	
	
	public class Server1 implements ActionListener {
		
		
		private void start() {
			btnDisconnect.addActionListener(this);
			btnConnect.addActionListener(this);
			btnSend.addActionListener(this);
			btnKeyGeneration.addActionListener(this);
			btnSendPublicKey.addActionListener(this);
		}
		
		private void Server_start() {
			try {
				server_socket = new ServerSocket(port); //generate server socket
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(server_socket!=null) {
				Connection();
			}
		}
		private void Connection() {
					
					try {
						textArea_2.append("사용자접속대기중\n");
						socket = server_socket.accept();  //wait client's connection
						
						textArea_2.append("접속!\n");
						
						sender = new Sender(socket);
						receiver = new Receiver(socket);
						receiver.start(); //start thread
						
	
						
					} catch (IOException e) {
						e.printStackTrace();
					}
					 
		}
		
		
		public void actionPerformed(ActionEvent s) {
			
			if(s.getSource() == btnConnect) {
				//System.out.println("click");
				port = Integer.parseInt(textArea.getText().trim());
	
				Server_start();
					//소켓생성및사용자접속대
			}
			else if(s.getSource()==btnSend) {
				System.out.println("server작동");
				msg = textArea_4.getText().trim();
				textArea_3.append(msg);
				sender.Sendmessage(msg);
				
			}
			
			else if(s.getSource()==btnKeyGeneration) {
				rsaEncryption = new RSAEncryption();
				keyPair = rsaEncryption.GenerateKey();
				 
				 publicKey = keyPair.getPublic();
				 privateKey = keyPair.getPrivate();
				 byte[] pubk = publicKey.getEncoded();
				 byte[] prik = privateKey.getEncoded();
				 textArea_5.append("\n=== RSA Key Generation ===\n");
				 textArea_5.append("\n Public Key : ");
				 
				 for(byte b: pubk) { 
				       textArea_5.append(String.format("%02X" , b));
				 }
				 textArea_5.append("\n Public Key Length : "+pubk.length+ " byte");
				 textArea_5.append("\n Private Key : ");
				 
				 for(byte b: prik) {
				        
				        textArea_5.append(String.format("%02X" , b));
				 }
				 textArea_5.append("\n Private Key Length : "+prik.length+ " byte" );
				
			}
			else if(s.getSource()==btnSendPublicKey) {
				
				sender.sendKey(publicKey);
			}
			else if(s.getSource() == btnDisconnect) {
				System.out.println("연결중단 ");
			}
			
			
			
		}
	}
	
	
	/**
	 * Create the frame.
	 */
	private void UI() { //UI
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 594, 900);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblNewLabel = new JLabel("communication mode");
		lblNewLabel.setBounds(16, 18, 145, 16);
		contentPane.add(lblNewLabel);
		Clientbtn.setBounds(16, 34, 70, 37);
		
		//scrollPane = new JScrollPane(textArea_5, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		contentPane.add(Clientbtn);
		Serverbtn.setBounds(91, 41, 70, 23);
		Clientbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				client.start();
			}
		});
		
		Serverbtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				server1.start();
			}
		});
		contentPane.add(Serverbtn);
		textArea.setBounds(308, 18, 128, 16);
		contentPane.add(textArea);
		textArea_1.setBounds(308, 45, 128, 16);
		contentPane.add(textArea_1);
		
		JLabel lblPort = new JLabel("Port");
		lblPort.setBounds(271, 18, 25, 16);
		contentPane.add(lblPort);
		
		JLabel lblIp = new JLabel("IP ");
		lblIp.setBounds(281, 45, 15, 16);
		contentPane.add(lblIp);
		textArea_2.setBounds(16, 76, 543, 37);
		contentPane.add(textArea_2);
		btnConnect.setBounds(469, 13, 100, 29);
		contentPane.add(btnConnect);
		btnDisconnect.setBounds(469, 40, 100, 29);
		
		contentPane.add(btnDisconnect);
		
		
		

		
		
		JLabel lblChatting = new JLabel("chatting");
		lblChatting.setBounds(26, 312, 61, 16);
		contentPane.add(lblChatting);
		scrollPane_1.setBounds(26, 332, 524, 124);
		
		contentPane.add(scrollPane_1);
		scrollPane_1.setViewportView(textArea_3);
		textArea_4.setBounds(26, 469, 447, 16);
		
		contentPane.add(textArea_4);
		btnSend.setBounds(485, 468, 70, 21);
		
	
		contentPane.add(btnSend);
		scrollPane.setBounds(21, 125, 538, 87);
		
		contentPane.add(scrollPane);
		scrollPane.setViewportView(textArea_5);
		
		
	
		
		
		btnKeyGeneration.setBounds(19, 224, 128, 29);
		contentPane.add(btnKeyGeneration);
		
		
		btnSendPublicKey.setBounds(415, 224, 128, 29);
		contentPane.add(btnSendPublicKey);
		
		
		
		btnNewButton.setBounds(159, 224, 128, 29);
		contentPane.add(btnNewButton);
		btnSaveInt.setBounds(286, 224, 117, 29);
		
		contentPane.add(btnSaveInt);
		textArea_6.setBounds(26, 265, 533, 37);
		
		contentPane.add(textArea_6);
		this.setVisible(true);
	}

	@Override
	
	public void actionPerformed(ActionEvent arg0) {
		
		if(arg0.getSource() == btnConnect) {//if click the btnConnect, start Network() .
			System.out.println("click");
			ip= textArea_1.getText().trim();
			port = Integer.parseInt(textArea.getText().trim());
			
			if(ip == "") {
				//Server_start();
				//소켓생성및사용자접속대
		}
			client.Network();
			
		}
		else if(arg0.getSource() == btnDisconnect) {
			System.out.println("연결중단 ");
		}
		
	}

	
	
	
	
public static void main(String[] args) {
		
		
		new Server();
	} 
}






