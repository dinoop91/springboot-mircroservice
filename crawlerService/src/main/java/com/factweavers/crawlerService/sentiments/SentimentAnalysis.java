package com.factweavers.crawlerService.sentiments;

import com.factweavers.SentimentAnalysis.SentenceCollector;
import org.json.JSONArray;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.factweavers.saf.client.SaffClient;

public class SentimentAnalysis {

	private static SentimentAnalysis INSTANCE;
	//private SaffClient client;
	SentenceCollector collect =null;
	private SentimentAnalysis(){
		//	client = new SaffClient("localhost", 1983);
		collect =new SentenceCollector() ;
		collect.loadSentiments();
	}

	public static SentimentAnalysis getInstance(){
		if(INSTANCE == null){
			System.out.println("null");
			INSTANCE = new SentimentAnalysis();
		}
		return INSTANCE;
	}

	public Sentiment processText(String inputText) {
		String classifiedOutput = null;
		//classifiedOutput = client.classifySentiment(inputText);
		classifiedOutput = 	collect.collectSent(inputText);
		if(classifiedOutput == null){
			Sentiment output =  new Sentiment(null,0,0);
			output.markAsError();
			return output;
		}
		if(classifiedOutput.isEmpty()){
			return new Sentiment(null,0,0);
		}
		JSONObject obj = new JSONObject(classifiedOutput);
		List<Map<String,String>> sentiObject = new ArrayList<Map<String,String>>();
		JSONArray array =  obj.getJSONArray("SentiOutput");
		int numOfPositives = 0;
		int numOfNegatives = 0;
		for(int i = 0 ; i  < array.length() ; i++){
			JSONObject singleSenti =  array.getJSONObject(i);
			Map<String,String> sentiObj = new HashMap<String,String>();
			sentiObj.put("sentiment", singleSenti.getString("sentiment"));
			sentiObj.put("sentence", singleSenti.getString("sentence"));
			sentiObject.add(sentiObj);
			if(singleSenti.getString("sentiment").equals("POSITIVE")){
				numOfPositives++;
			}
			else if(singleSenti.getString("sentiment").equals("NEGATIVE")){
				numOfNegatives--;
			}
		}
		return new Sentiment(sentiObject , numOfPositives , numOfNegatives);
	}

	public String getChecksum(String text)throws Exception
	{
		byte[] bytesOfMessage = text.getBytes("UTF-8");
		MessageDigest md = MessageDigest.getInstance("MD5");
		byte[] thedigest = md.digest(bytesOfMessage);
		return new String(thedigest);
	}


	public static void main(String args[]) throws InterruptedException {
		System.out.println("Return is " + SentimentAnalysis.getInstance().processText("Sivasena had no tea"));
	}

}
