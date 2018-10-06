package com.mrporter.pomangam.common.pattern.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Status
 * 
 * @version 1.0 [2018. 3. 26.]
 * @author Choi
 */
@Data @AllArgsConstructor @NoArgsConstructor @RequiredArgsConstructor
public class Status {
	
	/**
	 * 상태 코드
	 * 
	 * 1xx (조건부 응답) - 요청을 받았으며 작업을 계속한다.
	 * 	100 : 요청자는 요청을 계속해야 한다. 서버는 이 코드를 제공하여 요청의 첫 번째 부분을 받았으며 나머지를 기다리고 있음을 나타냄
	 * 
	 * 2xx (성공) - 이 클래스의 상태 코드는 클라이언트가 요청한 동작을 수신하여 이해했고 승낙했으며 성공적으로 처리했음을 가리킨다.
	 * 	200(성공): 서버가 요청을 제대로 처리했다는 뜻
	 * 	201(작성됨): 성공적으로 요청되었으며 서버가 새 리소스를 작성
	 * 	202(허용됨): 서버가 요청을 접수했지만 아직 처리하지 않음
	 * 	203(신뢰할 수 없는 정보): 서버가 요청을 성공적으로 처리했지만 다른 소스에서 수신된 정보를 제공
	 *  204(콘텐츠 없음): 서버가 요청을 성공적으로 처리했지만 콘텐츠를 제공하지 않음
	 * 
	 * 4xx (요청 오류) - 클라이언트에 오류가 있음을 나타낸다.
	 * 	400(잘못된 요청): 서버가 요청의 구문을 인식하지 못함
	 * 	401(권한 없음): 이 요청은 인증이 필요
	 * 	402(결제 필요): 이 요청은 결제가 필요
	 * 	403(인가 실패): 서버가 요청을 거부
	 * 	404(Not Found, 찾을 수 없음): 서버가 요청한 페이지(Resource)를 찾을 수 없음
	 * 	405(허용되지 않는 방법): 요청에 지정된 방법을 사용할 수 없음
	 * 
	 * 5xx (서버 오류) - 서버가 유효한 요청을 명백하게 수행하지 못했음을 나타낸다.
	 * 	500(내부 서버 오류): 서버에 오류가 발생
	 * 	501(구현되지 않음): 서버에 요청을 수행할 수 있는 기능이 없음
	 * 	503(서비스를 사용할 수 없음): 일시적 서버 사용 불가
	 * 	
	 */
	@NonNull
	Integer code;
	
	/**
	 * 상태 세부 정보
	 */
	String message;
	
	public String toJson() {
		return "{code:"+code+", message:\""+message+"\"}";
	}
}
