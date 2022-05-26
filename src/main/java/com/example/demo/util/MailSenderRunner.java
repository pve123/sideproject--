package com.example.demo.util;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.security.SecureRandom;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class MailSenderRunner {


    private final JavaMailSender javaMailSender;

    public void sendAuthEmail(String email, String authKey, String type) {

        String subject = null;
        String msg = null;

        if ("code".equals(type)) {
            subject = "인증코드번호";
            msg = "<img width=\"500\" height=\"500\" style=\"margin-top: 0; margin-right: 0; margin-bottom: 32px; margin-left: 0px; padding-right: 30px; padding-left: 30px;\" src=\"https://upload.wikimedia.org/wikipedia/commons/6/65/2017_%EB%82%A8%EC%A3%BC%ED%98%81_01.png\" alt=\"\" loading=\"lazy\">";
            msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">인증번호 Key 확인</h1>";
            msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">아래 확인 코드를 가입 창이 있는 브라우저 창에 입력하세요.</p>";
            msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
            msg += authKey;
            msg += "</td></tr></tbody></table></div>";
            msg += "<a href=\"https://github.com/pve123/springboot_side_project\" style=\"text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">Hosung Side Project Technologies, Inc</a>";
        } else if ("password".equals(type)) {
            subject = "임시비밀번호발급";
            msg = "<img width=\"500\" height=\"500\" style=\"margin-top: 0; margin-right: 0; margin-bottom: 32px; margin-left: 0px; padding-right: 30px; padding-left: 30px;\" src=\"https://upload.wikimedia.org/wikipedia/commons/6/65/2017_%EB%82%A8%EC%A3%BC%ED%98%81_01.png\" alt=\"\" loading=\"lazy\">";
            msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">임시비밀번호 발급</h1>";
            msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">임시비밀번호이 발급되었습니다. 확인 후 입력바랍니다.</p>";
            msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
            msg += authKey;
            msg += "</td></tr></tbody></table></div>";
            msg += "<a href=\"https://github.com/pve123/springboot_side_project\" style=\"text-decoration: none; color: #434245;\" rel=\"noreferrer noopener\" target=\"_blank\">Hosung Side Project Technologies, Inc</a>";
        }


        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true, "utf-8");
            helper.setTo(email);
            helper.setSubject(subject);
            helper.setText(msg, true);  //포함된 텍스트가 HTML이라는 의미로 true.
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }


    public String getRamdomPassword(int size) {
        char[] charSet = new char[]{
                '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
                'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x', 'y', 'z',
                '!', '@', '#', '$', '%', '^', '&'};

        StringBuffer sb = new StringBuffer();
        SecureRandom sr = new SecureRandom();
        sr.setSeed(new Date().getTime());

        int idx = 0;
        int len = charSet.length;
        for (int i = 0; i < size; i++) {
            // idx = (int) (len * Math.random());
            idx = sr.nextInt(len);    // 강력한 난수를 발생시키기 위해 SecureRandom을 사용한다.
            sb.append(charSet[idx]);
        }

        return sb.toString();
    }
}