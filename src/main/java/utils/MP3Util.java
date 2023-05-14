package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Header;

public class MP3Util {

	public static void main(String[] args) throws UnsupportedEncodingException {
		// TODO 自动生成的方法存根
		String path = "D:\\CloudMusic\\test.mp3";
		List res = Mp3Time.getMp3Info(path);
		System.out.println(res);

//		String test = "测试";
//		String test_gbk_utf8 = new String(test.getBytes(StandardCharsets.UTF_8), "gbk");
//		System.out.println(test_gbk_utf8);// 娴嬭瘯
//		String test_utf8_gbk = new String(test_gbk_utf8.getBytes("gbk"), StandardCharsets.UTF_8);
//		System.out.println(test_utf8_gbk);// 测试
	}

	public static String getAudioPlayTime(String filePath) {
		FileInputStream fis;
		int time = 0;
		try {
			fis = new FileInputStream(new File(filePath));
			int b = fis.available();
			Bitstream bt = new Bitstream(fis);
			Header h = bt.readFrame();
			time = (int) h.total_ms(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BitstreamException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date date = new Date(time);
		String s = sdf.format(date);
		if (s.charAt(0) == '0')
			s = s.substring(1, s.length());
		return s;
	}

	public static List getMp3Info(String path) {
		List<String> list = new ArrayList<String>();
		byte[] b = new byte[128];
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			try {
				fis.skip(file.length() - 128);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fis.read(b);
//				list.add(new String(b,0,3));//存放TAG 表示ID3 V1.0标准
				String name = new String(b, 3, 30);

				list.add(new String(name.getBytes("UTF-8"), "UTF-8"));// 歌名
				list.add(new String(b, 33, 30));// 作者
//				list.add(new String(b, 63, 30));// 专辑名
//				list.add(new String(b, 93, 4));// 年份
//				list.add(new String(b,97,30));//附注
//				list.add(new String(b,127,1));//MP3音乐类别，共147种
				list.add(getAudioPlayTime(path));
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	static boolean isEncoding(String str, String encode) {
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}

}

class Mp3Time {
	/**
	 * 获取音频时长
	 * 
	 * @param fis
	 * @throws IOException
	 * @throws BitstreamException
	 */
	public static String getAudioPlayTime(String filePath) {
		FileInputStream fis;
		int time = 0;
		try {
			fis = new FileInputStream(new File(filePath));
			int b = fis.available();
			Bitstream bt = new Bitstream(fis);
			Header h = bt.readFrame();
			time = (int) h.total_ms(b);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (BitstreamException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
		Date date = new Date(time);
		String s = sdf.format(date);
		if (s.charAt(0) == '0')
			s = s.substring(1, s.length());
		return s;
	}

	public static List getMp3Info(String path) {
		List<String> list = new ArrayList<String>();
		byte[] b = new byte[128];
		File file = new File(path);
		try {
			FileInputStream fis = new FileInputStream(file);
			try {
				fis.skip(file.length() - 128);
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				fis.read(b);
//				list.add(new String(b,0,3));//存放TAG 表示ID3 V1.0标准

				list.add(new String(b, 3, 30));// 歌名
				list.add(new String(b, 33, 30));// 作者
//				list.add(new String(b, 63, 30));// 专辑名
//				list.add(new String(b, 93, 4));// 年份
//				list.add(new String(b,97,30));//附注
//				list.add(new String(b,127,1));//MP3音乐类别，共147种
				list.add(getAudioPlayTime(path));
				fis.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return list;
	}

	static boolean isEncoding(String str, String encode) {
		try {
			if (str.equals(new String(str.getBytes(), encode))) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
}
