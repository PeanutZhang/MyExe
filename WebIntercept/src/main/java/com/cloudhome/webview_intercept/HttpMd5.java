package com.cloudhome.webview_intercept;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class HttpMd5 {

	 public static String getMD5(String sourceStr) {
		  String result = "";
		  try { 
		   MessageDigest md = MessageDigest.getInstance("MD5");
		   md.update(sourceStr.getBytes("UTF-8"));
		   byte b[] = md.digest(); 
		   int i; StringBuffer buf = new StringBuffer("");
		   for (int offset = 0; offset < b.length; offset++) { 
		    i = b[offset]; 
		    if(i<0) 
		    i += 256; 
		    if(i<16) 
		     buf.append("0"); 
		     buf.append(Integer.toHexString(i));
		   } 
		   result = buf.toString();
		   System.out.println("result: " + result);
		  } catch (NoSuchAlgorithmException e) {
		   
		  } catch (UnsupportedEncodingException e) {
			  e.printStackTrace();
		  }
		 return result;
		 }
}
