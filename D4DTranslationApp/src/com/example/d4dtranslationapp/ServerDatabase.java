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
	public ArrayList<ConversationData> getConversationData(int category, int[] languages) {
		// TODO Auto-generated method stub
		return null;
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
				res += temp;
			}
			
			return new ConversationTree(res);
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

}
