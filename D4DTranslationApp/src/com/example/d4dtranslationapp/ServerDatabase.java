package com.example.d4dtranslationapp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

public class ServerDatabase implements ConversationDatabase{

	public static final char pre = '(';
	public static final char post = ')';
	
	// String sent back from server meaning no data
	public static final String nodata = "NULL";
	
	// Server IP address or whatever URL used to connect to it
	private String url;
	private String USER_AGENT = "spaceduck";
	private HttpClient client;
	
	public ServerDatabase(String urlin)
	{
		url = urlin;
		// use deprecated until new libraries are used 
		client = new DefaultHttpClient();
	}
	

	@Override
	public ArrayList<ConversationData> getConversationData(int conversation_id, int[] languages)
	{
		ArrayList<ConversationData> arr = new ArrayList<ConversationData>();
		HttpPost post = new HttpPost(url);
		
		post.setHeader("USER-Agent", USER_AGENT);
		
		// make and set POST params
		List<NameValuePair> params = new ArrayList<NameValuePair>(3);
		params.add(new BasicNameValuePair("method_name", "getConversationData"));
		params.add(new BasicNameValuePair("category_id", Integer.toString(conversation_id)));
		params.add(new BasicNameValuePair("supported_languages", intArrToString(languages)));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		// execute post request and get response
		try {
			HttpResponse response = client.execute(post);
			System.out.println("Asking server at "+url+" Response Code: " + response.getStatusLine().getStatusCode());
			
			BufferedReader read = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String res = "";
			String temp = "";
			while((temp = read.readLine()) != null)
			{
				res += temp + "\n";
			}
			res = res.substring(0, res.length() - 1);
			
			// if no data was received we can just return now
			if(res.equals(nodata))
			{
				return arr;
			}
			
			// process string and add ConversationData objects to arraylist
			int index = 0;
			while(index < res.length())
			{
				int[] bounds = findNextBalancedParenPair(res, index);
				if(bounds != null)
				{
					arr.add(new MyConversationData(res.substring(bounds[0], bounds[1]+1)));
					index = bounds[1]+1;
				}
				else
				{
					break;
				}
			}
			
			return arr;
			
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	public Conversation getConversation(int conversation_id, int language1, int language2) {
		
		HttpPost post = new HttpPost(url);
		
		post.setHeader("USER-Agent", USER_AGENT);
		
		// make and set POST params
		List<NameValuePair> params = new ArrayList<NameValuePair>(4);
		params.add(new BasicNameValuePair("method_name", "getConversation"));
		params.add(new BasicNameValuePair("conversation_id", Integer.toString(conversation_id)));
		params.add(new BasicNameValuePair("language1", Integer.toString(language1)));
		params.add(new BasicNameValuePair("language2", Integer.toString(language2)));
		
		try {
			post.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
		
		// execute post request and get response
		try {
			HttpResponse response = client.execute(post);
			System.out.println("Asking server at "+url+"Response Code: " + response.getStatusLine().getStatusCode());
			
			BufferedReader read = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
			
			String res = "";
			String temp = "";
			while((temp = read.readLine()) != null)
			{
				res += temp + "\n";
			}
			
			// return null if no data was received, else return what we got
			System.out.println(res);
			return (res.equals(nodata) ? null : new ConversationTree(res));
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	// helper functions
	
	private String intArrToString(int[] arr)
	{
		String res = "";
		for(int temp : arr)
		{
			res += temp;
		}
		
		return res.substring(0, res.length() - 1);
	}
	
	/* given a string and a starting index, returns an integer pair with the locations of the next set of balanced parenthesis 
	 * returns null if no such balanced parenthesis can be found
	 */
	private int[] findNextBalancedParenPair(String str, int startindex)
	{
		int nesting = 0;
		int[] res = new int[2];
		for(int temp = startindex; temp < str.length(); temp++)
		{
			char c = str.charAt(temp);
			
			if(c == pre)
			{
				if(nesting == 0)
				{
					res[0] = temp;
				}
				
				nesting++;
			}
			else if(c == post)
			{
				nesting--;
				
				if(nesting == 0)
				{
					res[1] = temp;
					return res;
				}
			}
		}
		
		return null;
	}

}
