package fr.dawan.autoquiz3000.tools;

import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.Email;
import org.apache.commons.mail.SimpleEmail;

public class EmailTools {
//	private static final String SMTP_SERVER = "smtp.dsl.ovh.net";
	private static final String SMTP_SERVER = "smtp.googlemail.com";
	private static final String EMAIL_SENDER = "noreply@gmail.com";
	private static final String GMAIL_USER = "autoquiz3000";
	private static final String GMAIL_PASSWORD = "";
	
	public static void sendEmail(String to, String subject, String msg) throws Exception {
		Email email = new SimpleEmail();
		email.setHostName(SMTP_SERVER);
		email.setSmtpPort(25);
		email.setAuthenticator(new DefaultAuthenticator(GMAIL_USER, GMAIL_PASSWORD));
		email.setSSLOnConnect(true);
		email.setFrom(EMAIL_SENDER);
		email.setSubject(subject);
		email.setMsg(msg);
		email.addTo(to);
		email.send();
	}
}
