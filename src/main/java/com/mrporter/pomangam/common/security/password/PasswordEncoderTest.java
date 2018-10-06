package com.mrporter.pomangam.common.security.password;

import org.junit.Before;
import org.junit.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

public class PasswordEncoderTest {
	PasswordEncoding pwEncoder;
	PasswordEncoding bCryptEncoder;
	DB database;

	@Before
	public void initialize() {
		database = new DB();
		SHAPasswordEncoder shaPasswordEncoder = new SHAPasswordEncoder(512);
		shaPasswordEncoder.setEncodeHashAsBase64(true);
		pwEncoder = new PasswordEncoding(shaPasswordEncoder);
		bCryptEncoder = new PasswordEncoding(new BCryptPasswordEncoder());
	}
	//1ARVn2Auq2/WAqx2gNrL+q3RNjAzXpUfCXrzkA6d4Xa22yhRLy4AC50E+6UTPoscbo31nbOoq51gvkuXzJ6B2w==
	@Test
	public void test() {
		/* [client] password 신규 생성 */
		String password = "1234";	// 클라이언트단 plaintext (client)
		String send = pwEncoder.encode(password);	// 1차 SHA적용한 해시 (client)
		System.out.println("SHA 암호화: " + send);
		System.out.println("SHA 비교: " + pwEncoder.matches(password, send));
		System.out.println("\n[client] 서버에게 전송 - " + send);
		
		/* [server] 신규 가입 */
		String receive = send;
		String bCryptWord = bCryptEncoder.encode(receive);
		System.out.println("[server] BCrypt 암호화: " + bCryptWord);
		database.setData(bCryptWord);
		System.out.println("[server] BCrypt 암호 저장\n");
		
		/* [client] password 인증 요청 (정상 사용자) */
		password = "1234";	// 클라이언트단 plaintext (client)
		send = pwEncoder.encode(password);	// 1차 SHA적용한 해시 (client)
		System.out.println("[정상 client] 서버에게 전송 (인증 요청) - " + send);
		
		/* [server] 인증 */
		receive = send;
		bCryptWord = (String) database.getData();
		boolean isSuccess = bCryptEncoder.matches(receive, bCryptWord);
		System.out.print("[server] BCrypt 비교: " + isSuccess + " ");
		System.out.println( isSuccess ? "(인증성공)" : "(인증실패)" );
		
		/* [client] password 인증 요청 (비정상 사용자)  */
		password = "3456";
		send = pwEncoder.encode(password);
		System.out.println("\n[비정상 client] 서버에게 전송 (인증 요청) - " + send);
		
		/* [server] 인증 */
		receive = send;
		bCryptWord = (String) database.getData();
		isSuccess = bCryptEncoder.matches(receive, bCryptWord);
		System.out.print("[server] BCrypt 비교: " + isSuccess + " ");
		System.out.println( isSuccess ? "(인증성공)" : "(인증실패)" );
		
		System.out.println(bCryptEncoder.encode("a33e737fd070588aa19df7d93396d04a0f27ab933987fda4334123afb5712cf2a4c9b4ab841d3bb4f57802f3ef5dc044e4e2b3c3b6511bb8431c45c4d0080192"));
		
	}

	@Data @Getter @Setter @ToString
	class DB {
		Object data;
    }

}
