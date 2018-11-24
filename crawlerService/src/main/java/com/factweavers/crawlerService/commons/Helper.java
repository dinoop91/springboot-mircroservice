package com.factweavers.crawlerService.commons;

import org.apache.log4j.Logger;

import java.math.BigInteger;
import java.net.URI;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Helper {
	static Logger logger=Logger.getLogger(Helper.class.getName());

	public static String getDocumentId(String title, String link){
		String id=null;
		if(title!=null){
			id=title.trim().replaceAll(" ", "_");
		}
		if(title==null || id==""){
			if(link!=null){
				id=String.valueOf(link.hashCode());
			}
		}
		return id;
	}
	
	public static String getDomain(String url){
		String domainName=null;
		try{
			URI uri = new URI(url);
			domainName = uri.getHost();
			domainName = domainName.startsWith("www.") ? domainName.substring(4) : domainName;
			if(domainName.contains(".")){
				domainName=domainName.substring(0,domainName.lastIndexOf("."));
				if(domainName.split("\\.").length > 2){
					domainName=domainName.split("\\.")[0]+"."+domainName.split("\\.")[1];
				}
			}

		}catch(Exception e){
			logger.error("error finding domain of "+url);
			return null;
		}
		return domainName;
	}
	public static String getTime() {
		String DATE_FORMAT_NOW = "dd-MM-yyyy HH:mm:ss";
		SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
		String stringDate = sdf.format(new Date());
		return stringDate;
	}
	public static boolean isDateBefore(String date1, String date2){
		try{
			SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			Date d1 = sdf.parse(date1);
			Date d2 = sdf.parse(date2);
			if(d1.compareTo(d2)<=0){
				return true;
			}
		}catch(Exception e){
			logger.warn("error comparing dates "+date1+" and "+date2);
		}
		return false;
	}

	public static String formatDate(String oldDateString){
		String newDateString=null;
		try{
			String OLD_FORMAT = "EEE MMM dd hh:mm:ss z yyyy";
			String NEW_FORMAT = "dd-MM-yyyy HH:mm:ss";
			SimpleDateFormat sdf = new SimpleDateFormat(OLD_FORMAT);
			Date d = sdf.parse(oldDateString);
			sdf.applyPattern(NEW_FORMAT);
			newDateString = sdf.format(d);
		}catch(Exception e){
			logger.error("error converting time "+oldDateString+" returning current time "+getTime());
			return getTime();
		}
		return newDateString;
	}
	public static String getHash(String word){
		try {
			String plaintext = word;
			MessageDigest m = MessageDigest.getInstance("MD5");
			m.reset();
			m.update(plaintext.getBytes());
			byte[] digest = m.digest();
			BigInteger bigInt = new BigInteger(1,digest);
			String hashtext = bigInt.toString(16);
			System.out.println(hashtext);
			return hashtext;
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public static void main(String[] args) {
		Helper helper=new Helper();
		//String domain=helper.getDomain("http://online.barrons.com/xml/rss/3_7529.xml");
		//System.out.println(domain);
		//System.out.println(getTime());
		//System.out.println(formatDate("Tue Oct 20 05:39:36 IST 2015"));
		//System.out.println(getDomain("http://online.barrons.com/xml/rss/3_7530.xml"));
		helper.isDateBefore("08-02-2013 21:08:11", "08-02-2013 21:08:33");
	}
}
