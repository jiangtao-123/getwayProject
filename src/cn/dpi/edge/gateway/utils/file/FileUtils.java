package cn.dpi.edge.gateway.utils.file;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.util.Vector;

import javax.microedition.io.Connector;
import javax.microedition.io.file.FileConnection;

import org.joshvm.util.ArrayList;


/**
 * 文件操作工具类；
 * @author jiangtao
 *
 */
public class FileUtils {
private	static DataOutputStream outputStream = null;
private	static InputStream inputStream = null;
private	static FileConnection fileConnection = null;
/**
 * 创建写入文件内容以@@分隔符分开；
 * @param fileURI
 * @param fileName
 * @param context
 * @return
 */
	public static boolean creatFile(String fileURI,String fileName,String context) {
		System.out.println("     ==========     Start creatFile ...");
		// 创建FileConnection , 目前file:///Phone/根目录是固定的
		try{
		fileConnection = (FileConnection) Connector.open("file://"+fileURI+fileName);

		if (!fileConnection.exists()) {
//System.out.println("File is not exists>>>>>");
			// 创建File
			fileConnection.create();
			System.out.println("     ==========      Creat files success....");
		}else{
			//读取之前的文件内容；
//			System.out.println("oldTxt:>>>>>>>>>>>>>>>>>>>>");
			context+=("@@"+getFilInfo(fileURI,fileName));
//			System.out.println("context>>>>>"+context);
		}

		// 写入File
		if (fileConnection==null) {
			fileConnection = (FileConnection) Connector.open("file://"+fileURI+fileName);
		}
		outputStream = new DataOutputStream(fileConnection.openDataOutputStream());//fileConnection.openOutputStream();
		String message = context;
		outputStream.write(message.getBytes());
		System.out.println("     ==========      Write file success....");
		outputStream.close();
		outputStream = null;
		fileConnection.close();
		fileConnection = null;
		return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
			
		}
		
	}
	private static String getFilInfo(String fileURI, String fileName) {
	// TODO Auto-generated method stub
		String listTxt=readFile(fileURI, fileName);
		
	return listTxt;
}
	/**
	 * 读取文件；
	 * @param fileURI
	 * @param fileName
	 * @return
	 */
	public static String  readFile(String fileURI,String fileName) {
	System.out.println("     ==========      Start read File......");
	try{
	// 读File
	fileConnection = (FileConnection) Connector.open("file://"+fileURI+fileName);
	
	if (!fileConnection.exists()) {
		fileConnection.close();
		System.out.println("     ==========      File reading error! Can't open file to read.");
		throw new IOException("Can't read file "+fileURI);
//		return null;
	}
	
	inputStream = fileConnection.openInputStream();
	byte[] buffer = new byte[256];
	int readLen = 0;
	String text="";
	while ((readLen = inputStream.read(buffer)) != -1) {
		System.out.println("     ==========      Read file success....");
		String str=new String(buffer, 0, readLen);
//		System.out.println("hhhhh>>>>>>");
		if (null!=str&&str.length()>0) {
			text+=str;
		}
//		text+=new String(buffer, 0, readLen);
//		BufferedReader b=new
//		Reader inputStreamReader=new InputStreamReader(inputStream);
//		inputStreamReader.r
	}
	
	inputStream.close();
	inputStream = null;
	fileConnection.close();
	fileConnection = null;
	//读取成功删除文件；
//	deleteFile(fileURI,fileName);
	return text;
	}catch(Exception e){
		e.printStackTrace();
		return null;
		
	}
		
	}
	/**
	 * 删除文件；
	 * @param fileURI
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileURI,String fileName){
		System.out.println("     ==========      start file....");
		try{
		fileConnection = (FileConnection) Connector.open("file://"+fileURI+fileName);
		if (fileConnection.exists()) {

			fileConnection.delete();
			System.out.println("     ==========      Delete file success....");
		} else {
			System.out.println("     ==========      Error. Can't find file to delete");
		}

		fileConnection.close();
		fileConnection = null;
		return true;
		}catch(Exception e){
			e.printStackTrace();
			return false;
		}
		
	}
	/**
	 * j2mesplit字符串分割
	 */
	public static String[] split(String original,String regex)
	{
		
	//取子串的起始位置
	int startIndex = 0;
	//将结果数据先放入Vector中 注意应当引入
	Vector v = new Vector();
	//返回的结果字符串数组
	String[] str = null;
	//存储取子串时起始位置
	int index = 0;
	//获得匹配子串的位置
	startIndex = original.indexOf(regex);
	//如果起始字符串的位置小于字符串的长度，则证明没有取到字符串末尾。
	//-1代表取到了末尾
	//判断的条件，循环查找依据
	while(startIndex < original.length() && startIndex != -1) {
	String temp = original.substring(index,startIndex);
	//取子串
	v.addElement(temp);
	//设置取子串的起始位置
	index = startIndex + regex.length();
	//获得匹配子串的位置
	startIndex = original.indexOf(regex,startIndex + regex.length());
	}
	int n=(index + 1 - regex.length())<0?0:(index + 1 - regex.length());
	//取结束的子串
	v.addElement(original.substring(n));
	//将Vector对象转换成数组
	str = new String[v.size()];
	for(int i=0;i<v.size();i++){
		String s=(String)v.elementAt(i);
		if (i==v.size()-1&&n!=0) {
//			System.out.println(s);
			s=s.substring(1);
		}
	str[i] =s;
	}
	//返回生成的数组
	return str;
	} 

//测试
//	public static void main(String[] args) {
//		System.out.println("     ==========     Start File ...");
//		
//		//
////		String fileName = "messageInfo.txt";
////		String fileURI = "/Phone/" + fileName;
//		
//		FileUtils.creatFile(FileConstant.fileURI,FileConstant.fileName,/*System.currentTimeMillis()+*/"iec104");
//		//读取文件；
//		String  readText=FileUtils.readFile(FileConstant.fileURI,FileConstant.fileName);
//		System.out.println("readListStart:>>>>>>>>>");
//		System.out.println("read-Text:>>>"+readText);
//		System.out.println("readListEnd:>>>>>>>>>");
//		String[] strArray=FileUtils.split(readText, "@@");
//		System.out.println("startkkkk>>>>");
//		for(int i=0;i<strArray.length;i++){
//			
//			System.out.println(strArray[i]);
//		}
//		//删除文件；
////		FileUtils.deleteFile(FileConstant.fileURI,FileConstant.fileName);
//		
//
//	}
}
