package h.uniview.smarthouse.utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import h.uniview.smarthouse.exception.RRException;
import h.uniview.smarthouse.utils.Constant.PhotoType;

public class FileUtils {
	
	public static void main(String[] args) {
		List<String> r = listFile("C:\\Users\\CN092227\\Desktop\\NETPluginSDK_Win64_V2.1.0.0\\doc");
		r.forEach(e-> {
				System.out.println(e);
				System.out.println(e.substring(e.lastIndexOf("."), e.length()));
				System.out.println();
			}
		);
	}
	
	
	public static List<String> listFile(String filePath) throws RRException {
		File f = new File(filePath);
		if(!f.exists()) {
			throw new RRException("address is not exists", 9900);
		}
		
		List<String> result = new ArrayList<String>();
		if (f.isFile()) {
//			result.add(f.getAbsolutePath());
			result.add(f.getName());
			return result;
		}
		
		File[] fileArr = f.listFiles(new FileFilter() {
			@Override
			public boolean accept(File tmp) {
				String fname = tmp.getName().toLowerCase();
				return tmp.isFile() && PhotoType.isMember(fname.substring(fname.lastIndexOf("."), fname.length()));
			}
		});
		
		for(File t : fileArr) {
//			result.add(t.getAbsolutePath());
			result.add(t.getName());
		}
		return result;
	}
	
	/**
	 * 一行一行读取文件，适合字符读取，若读取中文字符时会出现乱码
	 * 流的关闭顺序：先打开的后关，后打开的先关， 否则有可能出现java.io.IOException: Stream closed异常
	 * @throws IOException
	 */
	public static String readFile(String filePath) throws IOException {
		return readFile(filePath, "UTF-8");
	}

	/**
	 * 一行一行读取文件，解决读取中文字符时出现乱码
	 * 
	 * 流的关闭顺序：先打开的后关，后打开的先关， 否则有可能出现java.io.IOException: Stream closed异常
	 * 
	 * @throws IOException
	 */
	public static String readFile(String filePath, String charSet) throws IOException {
		FileInputStream fis = new FileInputStream(filePath);
		InputStreamReader isr = new InputStreamReader(fis, charSet);
		BufferedReader br = new BufferedReader(isr);
		String line = null;
		StringBuffer sb = new StringBuffer();
		while ((line = br.readLine()) != null) {
			sb.append(line);
		}
		br.close();
		isr.close();
		fis.close();
		
		return sb.toString();
	}

	/**
	 * 一行一行写入文件，适合字符写入，若写入中文字符时会出现乱码
	 * 
	 * 流的关闭顺序：先打开的后关，后打开的先关， 否则有可能出现java.io.IOException: Stream closed异常
	 * 
	 * @throws IOException
	 */
	public static void writeFile(String filePath) throws IOException {
		String[] arrs = { "zhangsan,23,FuJian", "lisi,30,ShangHai", "wangwu,43,BeiJing", "laolin,21,ChongQing",
				"ximenqing,67,GuiZhou" };
		FileWriter fw = new FileWriter(new File(filePath));
		// 写入中文字符时会出现乱码
		BufferedWriter bw = new BufferedWriter(fw);

		for (String arr : arrs) {
			bw.write(arr + "\t\n");
		}
		bw.close();
		fw.close();
	}

	/**
	 * 一行一行写入文件，解决写入中文字符时出现乱码
	 * 流的关闭顺序：先打开的后关，后打开的先关， 否则有可能出现java.io.IOException: Stream closed异常
	 * @throws IOException
	 */
	public static void writeFile(String filePath, String charSet) throws IOException {
		String[] arrs = { "zhangsan,23,福建", "lisi,30,上海", "wangwu,43,北京", "laolin,21,重庆", "ximenqing,67,贵州" };
		// 写入中文字符时解决中文乱码问题
		FileOutputStream fos = new FileOutputStream(new File(filePath));
		OutputStreamWriter osw = new OutputStreamWriter(fos, charSet);
		BufferedWriter bw = new BufferedWriter(osw);
		
		for (String arr : arrs) {
			bw.write(arr + "\t\n");
		}

		// 注意关闭的先后顺序，先打开的后关闭，后打开的先关闭
		bw.close();
		osw.close();
		fos.close();
	}

}
