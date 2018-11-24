package com.factweavers.crawlerService.commons;


import com.algoTree.texthelpers.string;
import org.apache.log4j.Logger;
import org.joda.time.Days;
import org.joda.time.MutableDateTime;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CalculateEntityCount {


	public static Logger logger = Logger.getLogger(CalculateEntityCount.class
			.getName());
	int entityCount=0;
	/*	public int calculateEntity(String jsonString) throws Exception
	{


		ObjectMapper mapper=new ObjectMapper();

	Map<String,List<Map<String,List<Map<String,Object>>>>> mpParams=mapper.readValue(jsonString, Map.class);
			for(String key : mpParams.keySet())
		{
				List<Map<String,List<Map<String,Object>>>> lsmap=mpParams.get(key);


				Map<String,List<Map<String,Object>>> mpFirstMap=lsmap.get(0);


	          List<Map<String,Object>> lsFacets= mpFirstMap.get("Facets");

			for(Map<String,Object> lsEntity : lsFacets)
			{
			 entityCount=entityCount+Integer.parseInt(lsEntity.get("count").toString());
			lsEvents		
			}


		}
		relativeToAbsoluteDate
	return entityCount;	

	}
	 */



	/* public int calculateEntity(String jsonString,String cat) throws Exception
 {

		entityCount = 0;

		JSONObject jsString = new JSONObject(jsonString);
		if (jsString.has("Types")) {
			JSONObject jsCategories = (JSONObject) jsString.get("Types");

			if (jsCategories.has(cat)) {
				JSONArray jsFacets = jsCategories.getJSONArray(cat);
				for (int j = 0; j < jsFacets.length(); j++) {

					JSONObject jsCount = (JSONObject) jsFacets.get(j);
					entityCount = entityCount + jsCount.getInt("count");

				}

			}

		}

		return entityCount;

	}
	 */	
	/////////////////////////////////// added by dinoop on nov 17 from Helper class
	public String getDate(Date dateNow) {

		String dateToday = null;
		if (dateNow != null) {
			Format formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
			dateToday = formatter.format(dateNow);
		}
		return dateToday;

	}
	public int calculateDays(String time, int occurences) throws ParseException	{
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = sdf.parse(time);
        MutableDateTime epoch = new MutableDateTime();
        epoch.setDate(0); //Set to Epoch time
        epoch.setTime(0);
    	Days day = Days.daysBetween(epoch, new MutableDateTime(date.getTime()));
    	int days =day.getDays();
        int totalDays = occurences>100 ? days*100+100 : days*100+occurences;
        return totalDays;
     }
	/////////////////////////////////////////////////// added by dinoop on nov 17 from Helper class
	
	public  JSONObject findEvents(String jsonString,JSONArray sources,Object timestamp,List<String> evtlist) throws Exception
	{

		if(string.isNullOrEmpty(jsonString)) 
		{
			logger.error(" EMAC Output is null ");
			return null;
		}
		// Map<String,Object> events =null;
		JSONObject events=null;
		// List<Map<String,Object>> lsEvents=new ArrayList<Map<String,Object>>();
		// List<Map<String,Object>> lsGenEvents=new ArrayList<Map<String,Object>>();

		JSONArray lsEvents=new JSONArray();
		JSONArray lsGenEvents=new JSONArray();
		entityCount = 0;
		Date dateNow = new Date();
		JSONObject jsString = new JSONObject(jsonString);
		if (jsString.has("Types")) {
			JSONObject jsCategories = (JSONObject) jsString.get("Types");

			if (jsCategories.has("Events")) {
				JSONArray jsFacets = jsCategories.getJSONArray("Events");
				for (int j = 0; j < jsFacets.length(); j++) {

					JSONObject jsEvent = (JSONObject) jsFacets.get(j);
					String event =jsEvent.getString("event");
					if(evtlist.contains(event))
					{
						JSONObject js =jsEvent.getJSONObject(event);
						JSONArray jseventsAr = js.getJSONArray("events");

						for(int k =0;k<jseventsAr.length();k++)
						{
							// events =new HashMap<String, Object>();
							events =new JSONObject();
							//	events.put("Type",event) ;
							events.put("TimeStamp",timestamp) ;
							events.put("lastUpdated",getDate(dateNow)) ;
							//	events.put("Type",event) ;
							events.put("Occurences",sources.length()) ;
							int days = calculateDays(timestamp.toString(), sources.length());
							events.put("dailyPriority",days) ;
							events.put("Events", createEventsList((JSONObject)jseventsAr.get(k), sources));
							if(((JSONObject)jseventsAr.get(k)).has("SubType"))
								events.put("SubType",((JSONObject)jseventsAr.get(k)).get("SubType"));
							if(((JSONObject)jseventsAr.get(k)).has("Type"))
								events.put("Type",((JSONObject)jseventsAr.get(k)).get("Type"));


							/*events.put("Events",().get("event")) ;
						if(((JSONObject)jseventsAr.get(k)).has("entities"))
						events.put("Entities",((JSONObject)jseventsAr.get(k)).get("entities")) ;
						events.put("Sources",sources);
					    lsEvents.add(events);*/
							lsEvents.put(events);
						}
					}
					else
					{

						JSONObject js =jsEvent.getJSONObject(event);
						JSONArray jseventsAr = js.getJSONArray("events");

						for(int k =0;k<jseventsAr.length();k++)
						{
							// events =new HashMap<String, Object>();
							events =new JSONObject();

							events.put("TimeStamp",timestamp) ;
							events.put("lastUpdated",getDate(dateNow)) ;
							events.put("Occurences",sources.length()) ;
							int days = calculateDays(timestamp.toString(), sources.length());
							events.put("dailyPriority",days) ;
							events.put("Events", createEventsList((JSONObject)jseventsAr.get(k), sources));

							if(((JSONObject)jseventsAr.get(k)).has("SubType"))
								events.put("SubType",((JSONObject)jseventsAr.get(k)).get("SubType"));
							if(((JSONObject)jseventsAr.get(k)).has("Type"))
								events.put("Type",((JSONObject)jseventsAr.get(k)).get("Type"));

							/*events.put("Events",().get("event")) ;
						if(((JSONObject)jseventsAr.get(k)).has("entities"))
						events.put("Entities",((JSONObject)jseventsAr.get(k)).get("entities")) ;
						events.put("Sources",sources);
					    lsEvents.add(events);*/
							lsGenEvents.put(events);

						}


					}

				}

			}
		}

		JSONObject jsObj = new JSONObject();
		jsObj.put("General",lsGenEvents);
		jsObj.put("Events",lsEvents);

		return jsObj;

	}



	public JSONArray createEventsList(JSONObject jsEvent,JSONArray sources) throws Exception
	{
		JSONArray eventsArray =new JSONArray();
		Map<String,Object> event =new HashMap<String, Object>();
		event.put("Event",jsEvent.get("event")) ;
		String indEvent =jsEvent.get("event").toString().trim().replaceAll("\\s+"," ");
		String[] eventArray =indEvent .split(" ");
		event.put("WordLength",eventArray.length );
		event.put("CharLength",indEvent.length());
		if(jsEvent.has("entities"))
			event.put("Entities",jsEvent.get("entities")) ;
		event.put("Sources",sources);
		event.put("occurences",sources.length());
		eventsArray.put(event);
		return eventsArray;

	}



	public JSONArray createSourceList(String title,String parentId,String link,String source,String parentLink,String timeStamp,String sourceType) throws Exception
	{
		JSONObject jsSource = new JSONObject();
		jsSource.put("source",source);
		jsSource.put("title",title);
		jsSource.put("parent",parentId);
		jsSource.put("link",link);
		jsSource.put("parentLink",parentLink);
		jsSource.put("timeStamp",timeStamp);
		if(!string.isNullOrEmpty(sourceType))
			jsSource.put("sourceType",sourceType);
		JSONArray jsAr =new JSONArray();
		jsAr.put(jsSource);
		return jsAr;
	}


	/* public JSONArray createSourceList(String title,String parentId,String link,String source,String parentLink,String timeStamp) throws Exception
	{
		JSONObject jsObj=new JSONObject();
		JSONObject jsSource = new JSONObject();
		jsSource.put("source",source);
		jsSource.put("count",1);
		jsObj.put("title",title);
		jsObj.put("link",link);
		jsObj.put("parentLink",parentLink);
		jsObj.put("timeStamp",timeStamp);
		JSONArray jsarray=new JSONArray();
		jsarray.put(jsObj);
		jsSource.put("sourceArray",jsarray);
		jsSource.put("parent",parentId);
		JSONArray jsAr =new JSONArray();
		jsAr.put(jsSource);
		return jsAr;
	}*/



	public String modifyAndGetJSON(String jsonString,String actualTimeStamp) throws Exception
	{
		JSONObject finalJSON = new JSONObject();
		JSONObject finalJSONArray = new JSONObject();

		if (jsonString.length() > 0 && jsonString != null) {
			JSONObject jsString = new JSONObject(jsonString);
			if (!jsString.getJSONObject("Types").has("EventCount"))
				return jsonString;

			if (jsString.getJSONObject("Types").has("Entities")) {
				JSONArray jsFacets = jsString.getJSONObject("Types").getJSONArray("Entities");
				JSONObject jsFacetCount = jsString.getJSONObject("Types")
						.getJSONObject("EntityCount");
				finalJSONArray.put("Entities", jsFacets);
				finalJSONArray.put("EntityCount", jsFacetCount);
			}

			if (jsString.getJSONObject("Types").has("Events")) {
				JSONArray jsEvents = jsString.getJSONObject("Types").getJSONArray("Events");
				JSONObject jsEventCount = jsString.getJSONObject("Types").getJSONObject("EventCount");

				finalJSONArray.put("Events",formatEvents(jsEvents, actualTimeStamp));
				finalJSONArray.put("EventCount", jsEventCount);

			}

			finalJSON.put("Types", finalJSONArray);
		}
		if(finalJSON.length() > 0 && finalJSON != null) 
			return finalJSON.toString();
		else
			return "";
	}

	public  JSONArray formatEvents(JSONArray jsEvents,String actualTimeStamp) throws Exception
	{

		Map<String,Object> events =null;
		JSONArray jsArEvents =null;
		JSONArray newFinalEvents =new JSONArray();
		JSONArray newJsEvents =new JSONArray();
		for (int j = 0; j < jsEvents.length(); j++) {

			// getting sep event array - say general event array
			JSONObject jsObjEvent = (JSONObject) jsEvents.get(j);
			String event =jsObjEvent.getString("event");  // event name
			JSONObject jsIndvObj =jsObjEvent.getJSONObject(event); // generalevent Object
			JSONArray indvArEvents = jsIndvObj.getJSONArray("events");
			JSONObject jsonEvent =null;
			String dateComb="";
			jsArEvents= new JSONArray();
			for(int k=0;k<indvArEvents.length();k++)
			{

				jsonEvent = (JSONObject)indvArEvents.get(k);
				if(jsonEvent.has("entities"))
				{
					JSONArray jsEntities =(JSONArray) jsonEvent.get("entities") ;
					JSONArray newJsEntities=new JSONArray();
					for(int i=0;i<jsEntities.length();i++)
					{
						JSONObject jsEntity = jsEntities.getJSONObject(i);

						if(jsEntity.has("Date"))
						{
							jsEntity =createDateEntites(jsEntity, actualTimeStamp);
						}
						if(jsEntity.length()>0)
						{
							newJsEntities.put(jsEntity);
						}
					}

					jsonEvent.remove("entities");
					jsonEvent.put("entities",newJsEntities);
				}
				else
					logger.info(" Entitty No ");
				jsArEvents.put(jsonEvent);


			}
			newFinalEvents.put(jsObjEvent);
		}

		return newFinalEvents;

	}


	private JSONObject createDateEntites(JSONObject jsEntity,String actualTimeStamp) throws JSONException
	{
		JSONObject jsObjDate =new JSONObject();
		RelativeDateFormatter formatter=new RelativeDateFormatter();
		JSONArray jsDate = new JSONArray();
		jsDate = formatter.relativeToAbsoluteDate(jsEntity.getString("Date"), actualTimeStamp);
		/*	 if(formatter.getDateList()!=null)
     	 {
     		 JSONObject jsNew =null;
     		 for(Object relDate : formatter.getDateList())
     		 {
     			 jsNew =new JSONObject();
     			 jsNew.put("entity",relDate);
     			 jsDate.put(jsNew);
     		 }

     	 }
		 */
		if(jsDate.length()!=0)
		{
			jsObjDate.put("Date",jsDate);
			jsObjDate.put("entity","Date");
		} 

		return jsObjDate;
	}



}
