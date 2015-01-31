package org.taskstodo.protocol;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.JsonDeserializer;
import org.taskstodo.to.KeywordTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CustomKeywordsDeserializer extends JsonDeserializer<List<KeywordTO>> {

	@Override
	public List<KeywordTO> deserialize(JsonParser jsonparser, DeserializationContext ctxt)
			throws IOException, JsonProcessingException {
		    
	        List<KeywordTO> keywordTOs = null;
		    String payload = jsonparser.getText();
		    String[] keywordStrings = null;
		    if(payload != null){
		        keywordStrings =  payload.split(" ");
		        keywordTOs = new ArrayList<KeywordTO>();
		        for (String keyword : keywordStrings) {
                    keywordTOs.add(new KeywordTO(keyword));
                }
		    }
		    return keywordTOs;
       }
}
	
