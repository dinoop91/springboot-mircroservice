package com.factweavers.crawlerService.commons;

import com.algoTree.texthelpers.PatternMatcher;
import com.joestelmach.natty.CalendarSource;
import com.joestelmach.natty.DateGroup;
import com.joestelmach.natty.Parser;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RelativeDateFormatter {

	public static Logger logger = Logger.getLogger(RelativeDateFormatter.class
			.getName());
	private String relativeDate = null;
	private String actualTimeStamp = null;
	JSONArray jsDates =new JSONArray();
	int date, year, month;
	String  relADate=null;
	Set<String> combDate=null;
	public JSONArray relativeToAbsoluteDate(String relative, String currentDate) {
		
		relativeDate = relative;
		actualTimeStamp = currentDate;
		extractRelativeDate();
	    return jsDates;
	}

	private void extractRelativeDate() {

		String relDate[] = relativeDate.split("\\$");
		combDate =new HashSet<String>(Arrays.asList(relDate));
		setCalenderTime();
		for (String date : relDate) {
			splitSpan(date);
		}
	}

	private void setCalenderTime()
 {
		String hour = null, minute = null, sec = null;
		GregorianCalendar calendar = null;
		int inHour = 0, inMinute = 0, inSec = 0;
		
		String str[] = actualTimeStamp.split("\\s");
		if (str.length == 2) {
			String time[] = str[1].split(":");
			if (time.length == 3) {

				sec = time[2];
				inSec = Integer.parseInt(sec);
				minute = time[1];
				inMinute = Integer.parseInt(minute);
				hour = time[0];
				inHour = Integer.parseInt(hour);
			}

			else if (time.length == 2) {
				minute = time[1];
				inMinute = Integer.parseInt(minute);
				hour = time[0];
				inHour = Integer.parseInt(hour);
			} else {
				hour = time[0];
				inHour = Integer.parseInt(hour);
			}
		}
		String firstStr[] = str[0].split("-");
		date = Integer.parseInt(firstStr[0]);
		month = Integer.parseInt(firstStr[1]) - 1;
		year = Integer.parseInt(firstStr[2]);

		if (hour == null) {
			calendar = new GregorianCalendar(year, month, date);
		} else if (sec == null) {
			calendar = new GregorianCalendar(year, month, date, inHour,
					inMinute);
		} else if (sec != null) {
			calendar = new GregorianCalendar(year, month, date, inHour,
					inMinute, inSec);
		}
		CalendarSource.setBaseDate(calendar.getTime());

	}
	
	private void splitSpan(String date )
	{
		
		String relArrDate[] = date.split("\\[Span=");
		relADate =date;
		
		if(relArrDate.length==2 && relArrDate[1].equals("YEAR_SPAN]"))
		{
			getYear_Span(relArrDate[0]);
			
		}
		else if(relArrDate.length==1)
		{
			parseDateNatty(relArrDate[0],null);
		}
		else
		{
				parseDateNatty(relArrDate[0],relArrDate[1].replace("]", ""));
		}
			
		
		
	}
	
	
	private void getYear_Span(String relDate)
	{
		/*
		 * 1980
		 * 1980/81
		 * 1980/1981
		 * 1980-81
		 * 1980-1981
		 * b(B)etween 1980 to 1981
		 * f(F)rom 1980 to 1981 
		 * (B)between 1980 and 1981
		 * 2010:2011
		 * '06
		 * 
		 */
		
		String startDate =null ,endDate =null;
		String arrYearSpan[] = null;
		relDate=relDate.toLowerCase().trim();
		if(relDate.contains("/"))
		{
			arrYearSpan = relDate.split("/");
		
		}
		else if(relDate.contains("-"))
			arrYearSpan = relDate.split("-");
		else if(relDate.contains(":"))
			arrYearSpan = relDate.split(":");
		else if(relDate.startsWith("'"))
		{
			relDate = relDate.replace("'","");
			relDate = checkForDigits(relDate);
			
		}
		/*else if(relDate.startsWith("in the year"))
			relDate = relDate.replace("in the year","");
		else if(relDate.startsWith("in the"))
			relDate = relDate.replace("in the","");
		else if(relDate.startsWith("in"))
			relDate = relDate.replace("in","");
		else if(relDate.startsWith("by year"))
			relDate = relDate.replace("by year","");
		else if(relDate.startsWith("by"))
		{
			relDate = relDate.replace("by","");
			System.out.println(" Exce  "+relDate);
		}*/
	    else if(relDate.startsWith("between") && relDate.contains(" to "))
			arrYearSpan = relDate.replace("between","").split(" to ");
		else if(relDate.startsWith("from") && relDate.contains(" to "))
			arrYearSpan = relDate.replace("from","").split(" to ");
		else if(relDate.startsWith("between") && relDate.contains(" and "))
			arrYearSpan = relDate.replace("between","").split(" and ");
		
		if(arrYearSpan!=null && arrYearSpan.length==2)
		{
			
			if(arrYearSpan[1].trim().length()==4)
			{
			
				startDate = arrYearSpan[0];
				endDate=arrYearSpan[1];
			}
			else if(arrYearSpan[1].trim().length()==2)
			{
				startDate = arrYearSpan[0];
				endDate=startDate.substring(0,2)+arrYearSpan[1];
			}
			else if(arrYearSpan[1].trim().length()==1)
			{
				startDate = arrYearSpan[0];
				endDate=startDate.substring(0,3)+arrYearSpan[1];
			}
			else
			{
				logger.error(" Didnt get Match  "+arrYearSpan[0] +" and "+arrYearSpan[1]);
			}
		
			if(startDate!=null && endDate!=null)
			getYearSpan(startDate.trim(), endDate.trim());
			else
				logger.error(" Relative date couldnt parse :  "+relDate);

			
		}
		
		else
		{
		
			List <String> years=new ArrayList<String>();
			years =checkForYears(relDate);
			if(years.size()==1)
			getYearSpan(years.get(0),years.get(0));
			else if (years.size()==2)
			{
				if(Integer.parseInt(years.get(0))>Integer.parseInt(years.get(1)))
				{
					getYearSpan(years.get(1), years.get(0));
				}
				else
				{
					getYearSpan(years.get(0), years.get(1));
				}
			}
		
		}
	
		
	}
	
	private String checkForDigits(String relDate)
	{
		String regex ="\\d{2}";
		int relyear =year%10;
		int relYear;
			String matchedString = null;
			Pattern pattern = Pattern.compile(regex,Pattern.DOTALL | Pattern.MULTILINE);
			Matcher matcher = pattern.matcher(relDate);
			if(matcher.matches()==true)
			{
				relYear = Integer.parseInt(relDate);
				if(relYear>year)
				{
					relDate="19"+relDate;
				}
				else
				{
					relDate="20"+relDate;
				}
			}
			return relDate;	
	}
	
	
	
	
	
	private void parseDateNatty(String relDate,String span) {

		//String relArrDate[] = relDate.split("!");
		Parser parser = new Parser();

		List<Date> dates = new ArrayList<Date>();
		try {
			List<DateGroup> groups = parser.parse(relDate);
			for (DateGroup group : groups) {
				dates.addAll(group.getDates());
			}
		} catch (Exception e) {
			
			System.out.println(" Exception for relative date " + relativeDate
					+ "and actualtimestamp " + actualTimeStamp
					+ " from Natty : " + e);

		}
		addToFeatureMap(dates,relDate,span);
	}

	private void addToFeatureMap(List<Date> dates, String relative, String span) {

	
		if (dates.size() == 0) {

			logger.info(" Date after using natty parser is Null for "
					+ relativeDate + " actualTimeStamp : " + actualTimeStamp);
			return;
		}
		if (dates.size() == 2) {

			if (dates.get(0).after(dates.get(1))) {
				addToMap(dates.get(1).toString(), dates.get(0).toString());
			} else if (dates.get(0).before(dates.get(1))) {
				addToMap(dates.get(0).toString(), dates.get(1).toString());
			} else {
				addToMap(dates.get(0).toString(), dates.get(1).toString());
			}

		} else {
			for (Date row : dates) {
				if (span == null) {
					addToMap(row.toString(), row.toString());
				} else if (span.equals("YEAR")) {
					getYearSpan(row);
				} else if (span.equals("MONTH")) {
					getMonthSpan(row);
				} else if (span.equals("WEEK")) {
					getWeekSpan(row);
				}
			}
		}

	}

	void addToMap(String prevDate ,String endDate)
	{
		JSONObject jsTemp =new JSONObject(); 
		try {
			jsTemp.put("FromDate", dateFormatter(prevDate));
			endDate = dateFormatter(endDate);
			if(endDate.endsWith("00:00:00") && actualTimeStamp.endsWith("00:00:00")) endDate=endDate.replace("00:00:00","23:59:59");
			jsTemp.put("ToDate", endDate);
			jsTemp.put("entity",format(relADate));
			jsDates.put(jsTemp);
			clearDateList();
		} catch (Exception e) {
			logger.warn(" Exception: " +  e);
		}
	}
	
	void clearDateList()
	{
		combDate.remove(relADate);
		relADate=null;
	}
	
	public Set getDateList()
	{
		if(combDate.size()>0) return combDate;
		else return null;
	}
	
	
	public  String dateFormatter(String str) throws ParseException {

	
		SimpleDateFormat dtf = new SimpleDateFormat(
				"EEE MMM dd HH:mm:ss zzz yyyy");
		SimpleDateFormat outputDate = new SimpleDateFormat(
				"dd-MM-yyyy HH:mm:ss");
		Date parsed = dtf.parse(str);
		return outputDate.format(parsed);
		}
	
	/* Week Span */
	public  void getWeekSpan(Date date )
	{
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String startDate =null,endDate =null;
		calendar.set(Calendar.DAY_OF_WEEK, calendar.getFirstDayOfWeek());
		startDate =calendar.getTime().toString();
		if(date.toString().endsWith("00:00:00"))
		{
		calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY,23,59,59);
		}
		else
			calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
      	endDate =calendar.getTime().toString();
    	addToMap(startDate, endDate); 
	}
		
	
	/*  Month Span  */
	public void  getMonthSpan(Date date)
	{
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		String startDate =null,endDate =null;
		int lastDate = calendar.getActualMaximum(Calendar.DATE);
		logger.info(" Date  parse "+date.toString());
		if(date.toString().endsWith("00:00:00"))
		{
			calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),lastDate,23,59,59);
		}
		calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),lastDate);
		endDate =calendar.getTime().toString();
		 calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),1);
		  startDate =calendar.getTime().toString();
		     
 	    addToMap(startDate, endDate); 
	}
	
	/*  Year Span  */
	public  void getYearSpan(Date date )
	{
		String startYear =null,endYear =null ;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		if(date.toString().endsWith("00:00:00"))
		{
			calendar.set(calendar.get(Calendar.YEAR),Calendar.DECEMBER,31,23,59,59);
		}
		calendar.set(calendar.get(Calendar.YEAR),Calendar.DECEMBER,31);
		endYear =calendar.getTime().toString();
		calendar.set(calendar.get(Calendar.YEAR),Calendar.JANUARY,1);
		startYear =calendar.getTime().toString();
	 	addToMap(startYear, endYear); 
	}
	
	/*  Year Span  */
	public  void getYearSpan(String startYear,String endYear)
	{
		startYear=checkForYear(startYear);
		endYear=checkForYear(endYear);
		if(startYear==null || endYear==null) return;
		String startDate =null,endDate =null ;
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.parseInt(endYear),Calendar.DECEMBER,31,23,59,59);
		endDate =calendar.getTime().toString();
		calendar.set(Integer.parseInt(startYear),Calendar.JANUARY,1,0,0,0);
		startDate =calendar.getTime().toString();
		addToMap(startDate, endDate); 
	}
	
	public String checkForYear(String year)
	{
		String mod;
		int startYear;
		if(year.length()==2) 
		{
			year="20"+year;
			
		}
		else if(year.length()==4)
		{
			
			mod=year.substring(0,2);
			startYear=Integer.parseInt(mod);
			if(startYear<18)
			  year=null;
		}
		
	 return year;	
	}
	
	public String format(String relDate)
	{
	    String regex = "\\[Span=.*?\\]";
		relDate=relDate.replaceAll(regex, "").replace("$",",");
	   return relDate;
	}
	
	public List<String> checkForYears(String sb)
	{
		String yearRegex ="[0-9]+";
		List<String> years=new ArrayList<String>();
		years =PatternMatcher.getMatches(yearRegex, sb,0 );
		System.out.println(" years = "+years);
		return years;
	}
	
	
}
