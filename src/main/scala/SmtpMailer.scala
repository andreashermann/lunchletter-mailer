package main.scala

import java.util.Properties
import javax.mail.Message.RecipientType
import javax.mail.internet.MimeMessage
import javax.mail.{Authenticator, PasswordAuthentication, Session}

class SmtpMailer {

  val username = System.getenv("MAIL_SMTP_USER")
  val password = System.getenv("MAIL_SMTP_PASSWORD")

  val properties: Properties = new Properties()
  properties.put("mail.smtp.host", "smtp.mandrillapp.com")
  properties.put("mail.smtp.port", "587");
  properties.put("mail.smtp.auth", "true")
  properties.put("mail.smtp.starttls.enable", "true")

  // TLS configuration
  //properties.put("mail.smtp.socketFactory.port", "443")
  //properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory")
  //properties.put("mail.smtp.socketFactory.fallback", "false")

  val authenticator = new Authenticator {
    override def getPasswordAuthentication = new PasswordAuthentication(username, password)
  }

  def send(recipient: String, subject: String, text: String): Unit = {
    val session = Session.getInstance(properties, authenticator)
    session.setDebug(true)

    val message = new MimeMessage(session)
    message.setFrom("recommendations@lunchletter.ch")
    message.setRecipients(RecipientType.TO, recipient)
    message.setSubject(subject)

    /*    val bodyPart = new MimeBodyPart()
        bodyPart.setText(body)
        val multipart = new MimeMultipart()
        multipart.addBodyPart(bodyPart)
        message.setContent(multipart)*/
    //message.setText(body)
    message.setContent(text, "text/html; charset=utf-8");

    val transport = session.getTransport("smtp")
    transport.connect()
    transport.sendMessage(message, message.getAllRecipients)
    transport.close()
  }

}
