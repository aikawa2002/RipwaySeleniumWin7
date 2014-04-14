package ripway.common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

public class SendMailer{
	
	//protected static String _smtp="192.168.187.128";
	//protected static String _from="root@mail.ripway.net";
	//protected static String _sender="m.aikawa@jrits.ricoh.co.jp";
	
	public SendMailer() {
		
	}
    
	public void send(String subject,String mailtext,String fileName) throws Exception {
	Properties props = System.getProperties();
	//SMTPサーバーのアドレスを指定
	props.put("mail.transport.protocol", "smtp");
	props.put("mail.smtp.host", RipwayDefine.smtp);
	props.put("mail.host", RipwayDefine.smtp);
	props.put("mail.from", RipwayDefine.from);
	props.put("mail.smtp.auth","true"); 

	//メール送信用のセッション取得
	Session session = Session.getDefaultInstance(props,null);
	//MIMEメッセージクラス生成
	MimeMessage mimeMessage = new MimeMessage(session);

	//送信元アドレスを設定
	mimeMessage.setFrom(new InternetAddress(RipwayDefine.from));
	// 送信先メールアドレスを指定
	//for (int i = 0; i < addressList.size(); i++){
	//	String address = addressList.get(i).toString();
	//	System.out.println("■■■送信先メールアドレス[" + i + "][" + address + "]");
	//	mimeMessage.addRecipients(Message.RecipientType.TO, address);
	//}
	mimeMessage.addRecipients(Message.RecipientType.TO, RipwayDefine.sender);
	//返送先メールアドレス設定
	InternetAddress[] address = {new InternetAddress(RipwayDefine.from)};
	mimeMessage.setReplyTo(address);

	// メールのタイトルを指定
	mimeMessage.setSubject(subject, "iso-2022-jp");


	/* メール本文部を設定 */
	MimeBodyPart mbp1 = new MimeBodyPart();
	// メールの内容を指定
	mbp1.setText(mailtext , "iso-2022-jp");
	// メールの形式を指定(TEXT)
	mimeMessage.setHeader("Content-Type","text/plain");

	// 添付ファイル部を設定 
	MimeBodyPart mbp2 = new MimeBodyPart();
	// 添付するファイル名を指定
	FileDataSource fds = new FileDataSource(fileName);
	mbp2.setDataHandler(new DataHandler(fds));
	mbp2.setFileName(MimeUtility.encodeWord(fds.getName()));
	// 複数のボディを格納するマルチパートオブジェクトを生成
	Multipart mp = new MimeMultipart();
	// 本文部分を追加
	mp.addBodyPart(mbp1);
	// 添付部分を追加
	mp.addBodyPart(mbp2);
	// マルチパートオブジェクトをメッセージに設定
	mimeMessage.setContent(mp);


	// 送信日付を指定
	mimeMessage.setSentDate(new Date());
	//送信時のSMTPコネクション確立
	Transport transport = session.getTransport("smtp");
	
	System.out.println("■■■SMTPサーバ[" + RipwayDefine.smtp + "]");
	System.out.println("■■■メールアドレス[" + RipwayDefine.sender + "]");
	System.out.println("▲▲▲SMTPサーバへ接続開始");
	transport.connect(RipwayDefine.smtp, "", "");
	System.out.println("▼▼▼SMTPサーバへ接続完了");

	transport.sendMessage(mimeMessage, mimeMessage.getAllRecipients());
	//コネクションの切断
	transport.close();
	}
}
