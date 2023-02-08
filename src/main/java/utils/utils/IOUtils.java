package utils.utils;

import java.io.BufferedReader;
import java.io.IOException;

public class IOUtils {
    /**
     * @param BufferedReader는 Request Body를 시작하는 시점이어야
     * @param contentLength는  Request Header의 Content-Length 값이다.
     * @return
     * @throws IOException
     */
    public static String readData(BufferedReader br, int contentLength){
        try {
            char[] body = new char[contentLength];
            br.read(body, 0, contentLength);
            return String.copyValueOf(body);
        } catch (IOException e) {
            throw new RuntimeException("데이터 읽기에 실패하였습니다.");
        }
    }
}
