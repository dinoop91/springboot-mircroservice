package com.factweavers.crawlerService.sentiments;

import java.util.List;
import java.util.Map;

public class Sentiment {
	List<Map<String,String>> sentiments;
	String overallSentiment = "NEUTRAL";
	Integer  numOfPositives , numOfNegatives;
	int sentiScore = 0;
	public Sentiment(List<Map<String,String>> sentiments,int numOfPositives ,int numOfNegatives){
		this.sentiments = sentiments;
		sentiScore = numOfPositives + numOfNegatives;
		this.numOfPositives = numOfPositives;
		this.numOfNegatives = numOfNegatives;

		
		if(sentiScore == 0 && numOfPositives != 0){
			overallSentiment = "MIXED";
		}
		else if(sentiScore > 0){
			overallSentiment = "POSITIVE";
		}
		else if(sentiScore < 0){
			overallSentiment = "NEGATIVE";
		}
	}
	
	public String getOverallSentiments(){
		return overallSentiment;
	}
	
	public List<Map<String, String>> getSentiObjects(){
		return sentiments;
	}
	
	public int getSentiScore(){
		return sentiScore;
	}
	
	public int getOverallPositive(){
		return numOfPositives;
	}
	
	public int getOverallNegatives(){
		return numOfNegatives;
	}

	
	public String toString(){
		return overallSentiment + ":" + sentiScore;
	}

	public void markAsError() {
		overallSentiment = "NetworkError";
		sentiScore = 0;
	}
	

}
