package com.factweavers.crawlerService.commons;

import com.algoTree.texthelpers.PatternMatcher;
import net.htmlparser.jericho.Source;
import org.apache.log4j.Logger;

import java.util.List;

public class EmacHelper {
	
	static Logger logger=Logger.getLogger(EmacHelper.class.getName());

	
	public static String removeTableTags(String content){
		String tabRegx="(<table(.*?)>.*?</table>|<tbody(.*?)>.*?</tbody>)";
		List<String> tables =PatternMatcher.getMatches(tabRegx, content, 0);
		for(String tab : tables){
			content=content.replace(tab,"");
		}
		return content;  
	}
	
	public static String getStrippedContent(String aceOutput) throws Exception{
		Source source = new Source(aceOutput.replaceAll("(?i)<(/|)a[^<>]*?>", ""));
		String strippedContent = source.getRenderer().toString();
		if(strippedContent == null ){
			logger.error("Stripped Content is null ");
			return null;
		}
		strippedContent="<pre>"+strippedContent+"</pre>";
		return strippedContent;
	}
	public static int getWordCount(String content){
		String trimmed = content.trim();
		int words = trimmed.isEmpty() ? 0 : trimmed.split("\\s+").length;
		return words;
	}
	
	public static void main(String[] args) {

		
	}

}
