package pushfcmtest.framework.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;


public class fcmserver {
	private static fcmserver instance;
	private final static String FCM_URL = "https://fcm.googleapis.com/fcm/send";
	private final static String SERVER_KEY = "YOUR_SERVERKEY";
	public fcmserver() {
	}

	public static fcmserver createInstance() {
		if (null == instance) {
			instance = new fcmserver();
			instance.init();
		}
		return instance;
	}

	public static fcmserver getInstance() {
		return instance;
	}

	private void init() {
		String tokenid = "tokenid1";
		String message = "test message";
		String title = "title";
		ArrayList<String> alfcmtokens = new ArrayList<String>();
		alfcmtokens.add(tokenid);
		sendmultifcmmsg(alfcmtokens,title,message);
		sendfcmmsg(title, message);
	}
	
	/**
	 * all device message send
	 * @param tokenId
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	private void sendfcmmsg(String title,String message) {
		try {
			URL url = new URL(FCM_URL);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
			conn.setRequestProperty("Content-Type", "application/json");

			JSONObject infojson = new JSONObject();
			// notification title
			infojson.put("title", title);
			infojson.put("body", message);
			JSONObject json = new JSONObject();
			json.put("to", "/topics/all");
			json.put("notification", infojson);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(json.toString());
			wr.flush();

			int status = 0;
			if (null != conn) {
				status = conn.getResponseCode();
			}
			if (status != 0) {
				switch (status) {
				case 200:
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					System.out.println("Android Notification Response : " + reader.readLine());
					break;
				case 401:
					System.out.println("Notification Response : Error");
					break;
				case 501:
					System.out.println("Notification Response : server Error" );
				case 503:
					System.out
							.println("Notification Response : server Error Notification Server is unavailable");
				}

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * multi-device push send (token ids)
	 * @param fcmids
	 * @param tokenId
	 * @param title
	 * @param message
	 */
	@SuppressWarnings("unchecked")
	private void sendmultifcmmsg(ArrayList<String> fcmids, String title, String message) {
		try {
			URL url = new URL(FCM_URL);
			HttpURLConnection conn;
			conn = (HttpURLConnection) url.openConnection();
			conn.setUseCaches(false);
			conn.setDoInput(true);
			conn.setDoOutput(true);
			conn.setRequestMethod("POST");
			conn.setRequestProperty("Authorization", "key=" + SERVER_KEY);
			conn.setRequestProperty("Content-Type", "application/json");

			JSONArray tokenids = null;
			JSONObject objData = null;
			JSONObject data = null;
			JSONObject notif = null;
			
			tokenids = new JSONArray();
			for(int i =0; i < fcmids.size(); i++) {
				tokenids.add(fcmids.get(i));
			}
			
			data = new JSONObject();
			data.put("message", message);
			notif = new JSONObject();
			notif.put("title", title);
			notif.put("text", message);
			notif.put("click_action", "www.naver.com");
			
			objData = new JSONObject();
			objData.put("registration_ids", tokenids);
			objData.put("data", data);
			objData.put("notification", notif);

			OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
			wr.write(objData.toString());
			wr.flush();

			int status = 0;
			if (null != conn) {
				status = conn.getResponseCode();
			}
			if (status != 0) {
				switch (status) {
				case 200:
					BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
					System.out.println("Android Notification Response : " + reader.readLine());
					break;
				case 401:
					System.out.println("Notification Response :  Error ");
					break;
				case 501:
					System.out.println("Notification Response : server Error ");
				case 503:
					System.out
							.println("Notification Response : server Error Notification Server is unavailable");
				}

			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
