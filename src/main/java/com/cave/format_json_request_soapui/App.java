package com.cave.format_json_request_soapui;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.xpath.XPathExpressionException;

import org.xml.sax.SAXException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;

public class App 
{
	private static final Gson gson = new GsonBuilder().setPrettyPrinting().serializeNulls().create();
	
    public static void main( String[] args ) throws ParserConfigurationException, SAXException, IOException, XPathExpressionException, TransformerException
    {
    	
    	if (args.length == 0) {
    		System.out.println("must provide a filename");
    		return;
    	}

    	String file = args[0];       
    	
    	BufferedReader br = new BufferedReader(new FileReader(file));
    	String line = null;
    	StringBuilder sb = new StringBuilder();
    	
    	while((line = br.readLine()) != null) {
    		sb.append(line).append("\r\n");
    	}
    	
    	br.close();
    	
    	int currentIdx = 0;
    	String SEARCH_NEEDLE = "<con:request>";
    	while(true) {
    		int idx = sb.indexOf(SEARCH_NEEDLE, currentIdx);
    		
    		if (idx == -1) {
    			break;
    		}
    		
    		currentIdx = idx + SEARCH_NEEDLE.length();    		
    		int endIdx = sb.indexOf("</con:request>", currentIdx);
    		
    		String request = formatRequest(sb.substring(currentIdx, endIdx));
    		
    		sb.replace(currentIdx, endIdx, request);
    	}
    	
    	System.out.println(sb);
    	
    }

	private static String formatRequest(String request) {
		String jsonFormatted = null;
		try {
			JsonObject obj = gson.fromJson(request, JsonObject.class);
			
			jsonFormatted = gson.toJson(obj);
			
		}catch(Exception e) {
			e.printStackTrace(System.err);
			System.err.println("error request: " + request);
			return request;
		}
		
		
		return jsonFormatted;
		
	}
}
