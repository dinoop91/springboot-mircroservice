package com.factweavers.crawlerService.sentiments;

import org.elasticsearch.common.xcontent.XContentBuilder;
import org.elasticsearch.common.xcontent.XContentFactory;

import java.io.IOException;
import java.util.List;
import java.util.Map;


public class SentiTest {

	public XContentBuilder getSentiment(String text) throws IOException {
		XContentBuilder builder = XContentFactory.jsonBuilder();
		Sentiment sentiment = SentimentAnalysis.getInstance().processText(text);
		List<Map<String, String>> sentiments = sentiment.getSentiObjects();
		System.out.println(sentiments);
		builder.startObject("senti");
		builder.field("overallSentiment", sentiment.getOverallSentiments());
		builder.field("overallScore", sentiment.getSentiScore());
		if (sentiments != null) {
			builder.startArray("sentiments");
			for (Map<String, String> singleSenti : sentiments) {
				builder.startObject();
				builder.field("sentiment", singleSenti.get("sentiment"));
				builder.field("sentence", singleSenti.get("sentence"));
				builder.endObject();
			}
			builder.endArray();
		}
		builder.endObject();
		System.out.println(builder.string());
		return builder;
	}
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		SentiTest s=new SentiTest();
		s.getSentiment("this is a good film. i love that film.");

	}

}
