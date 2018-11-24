package com.factweavers.crawlerService.sentiments;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SentimenceInterface {

	public JSONObject getSentiment(String text){
		JSONObject sentiData=new JSONObject();
		Sentiment sentiment = SentimentAnalysis.getInstance().processText(text);
		List<Map<String, String>> sentiments = sentiment.getSentiObjects();
		sentiData.put("overallSentiment", sentiment.getOverallSentiments());
		sentiData.put("overallScore", sentiment.getSentiScore());
		if(sentiments != null){
			List<JSONObject> sentiList=new ArrayList<JSONObject>();
			/*for (Map<String, String> singleSenti : sentiments) {
				JSONObject singleSentiJson=new JSONObject();
				singleSentiJson.put("sentiment", singleSenti.get("sentiment"));
				singleSentiJson.put("sentence", singleSenti.get("sentence"));
				sentiList.add(singleSentiJson);
			}*/
			sentiData.put("sentiments", sentiList);
		}
		return sentiData;
	}
	
	
	public static void main(String[] args) {
		SentimenceInterface s=new SentimenceInterface();
		JSONObject sen=s.getSentiment("this is a good film. i love that film.");
		System.out.println(sen);
	}

}
