package com.example.auth.demo;

import org.apache.catalina.startup.Bootstrap;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class DemoApplicationTests {

	@Test
	public void contextLoads() {

	}

	@Test
	public void str() throws UnsupportedEncodingException {
		String ss = "https://www.baidu.com/s?ie=utf-8&f=8&rsv_bp=1&ch=7&tn=98012088_9_dg&wd=text%E5%AD%97%E8%8A%82%E6%A3%80%E6%B5%8B&oq=mysql%2520text&rsv_pq=93b7d7ef0008bd88&rsv_t=f345OaEH9JdrQGTQykbq2jm1zM1jHKjm87s1E0Gfk3g06EXGpdbUCnPwBRhBLFDKzxqPAg&rqlang=cn&rsv_enter=1&inputT=5909&rsv_sug3=21&rsv_sug1=11&rsv_sug7=100&rsv_sug2=0&rsv_sug4=5909";
		int num = ss.getBytes("UTF-8").length;
		System.out.println(num);
	}

	static BASE64Encoder encoder = new sun.misc.BASE64Encoder();
	static BASE64Decoder decoder = new sun.misc.BASE64Decoder();

	@Test
	public  void gdas() {
		System.out.println(getImageBinary()); // image to base64
		base64StringToImage(getImageBinary()); // base64 to image
	}

	static String getImageBinary() {
		File f = new File("C:\\Users\\bobi\\Pictures\\Saved Pictures\\IMG_20181111_155606_1(1).jpg");
		try {
			BufferedImage bi = ImageIO.read(f);
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ImageIO.write(bi, "jpg", baos);
			byte[] bytes = baos.toByteArray();
			String res = encoder.encodeBuffer(bytes).trim();
			return res;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	static void base64StringToImage(String base64String) {
		try {
			byte[] bytes1 = decoder.decodeBuffer(base64String);
			ByteArrayInputStream bais = new ByteArrayInputStream(bytes1);
			BufferedImage bi1 = ImageIO.read(bais);
			File f1 = new File("d://out.jpg");
			ImageIO.write(bi1, "jpg", f1);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void  ain() {
		Bootstrap

		System.out.println("Hello! World");
		String s = System.getProperty("java.library.path");
		String[] ss = s.split(";");
		for (int i = 0; i <ss.length ; i++) {
			System.out.println(ss[i]);
		}
		System.out.println();


		String os = System.getProperty("os.name");
		System.out.println(os);
		if (os.toLowerCase().startsWith("win")) {
			System.loadLibrary("libarcsoft_face");
			System.loadLibrary("libarcsoft_face_engine");
			System.loadLibrary("libarcsoft_face_engine_jni");
		} else {
			System.loadLibrary("arcsoft_face_engine_jni");
		}
	}
}
