package httpService;

import java.net.*;
import java.io.*;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpService {
    private static String chatURLstr = "http://localhost:11434/api/chat";
    private static String URLstr = "http://localhost:11434/api/generate";
    private static URL url;
    private static HttpService httpService;
    private static double temperature = 0.5;
    private static String customURL = null;
    private static final String DEFAULT_URL = "http://localhost:11434";

    static {
        try {
            url = new URL(URLstr);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    public static HttpService getHttpService(){
        if(httpService == null){
            httpService = new HttpService();
        }
        return httpService;
    }

    public static HttpService getHttpService(String type){
        if(httpService == null){
            httpService = new HttpService();
        }
        if (type.equalsIgnoreCase("Chat")) {
            try {url = new URL(chatURLstr);}    
            catch (MalformedURLException e) {
                e.printStackTrace();
            }
        }

        return httpService;
    }

    private HttpService() {
        
    }

    public void setCustomURL(String url){
            HttpService.customURL = url;

            chatURLstr = customURL + "/api/chat";
            URLstr = customURL + "/api/generate";
    }

    public String getCustomURL(){
        return HttpService.customURL;
    }

    public void useDefaultURL(){
        HttpService.customURL = null;
        chatURLstr = DEFAULT_URL + "/api/chat";
        URLstr = DEFAULT_URL + "/api/generate";
    }

    

    public void setModelTemperature(String choise){
        if(choise.equalsIgnoreCase("More Creative")){
            temperature = 0.8;
        }
        else if(choise.equalsIgnoreCase("Balanced")){
            temperature = 0.5;
        }
        else if(choise.equalsIgnoreCase("More precise")){
            temperature = 0.2;
        }
    }
    public String getServerResponse(String text) {
        JSONObject json = generateSimpleJSON(text);
        System.out.println(json);
        try {
            url = new URL(URLstr);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getString("response");
            } else {
                return "HTTP Error: " + responseCode;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }
//TODO implement dependant methods
    public String getChatResponse(String text) {
        JSONObject json = generateChatJSON(text);
        System.out.println(json);
        try {
            url = new URL(chatURLstr);
        } catch (MalformedURLException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json");
            con.setDoOutput(true);

            try (OutputStream os = con.getOutputStream()) {
                byte[] input = json.toString().getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            int responseCode = con.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
                StringBuilder response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();
                JSONObject jsonResponse = new JSONObject(response.toString());
                return jsonResponse.getJSONObject("message").getString("content");
            } else {
                return "HTTP Error: " + responseCode;
            }
        } catch (Exception e) {
            return "Error: " + e.getMessage();
        }
    }

//TODO implement this method
    JSONArray chatMessagesArray = new JSONArray();
    private JSONObject generateChatJSON(String text) {
        JSONObject json = new JSONObject();
        
        JSONArray messagesArray = chatMessagesArray;

        if(messagesArray.length() == 0){
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", text);
            chatMessagesArray.put(message);
            json.put("messages", chatMessagesArray);
        }
        else{
            JSONObject message = new JSONObject();
            message.put("role", "user");
            message.put("content", text);
            chatMessagesArray.put(message);
            json.put("messages", chatMessagesArray);
        }
        
        
        messagesArray = chatHistoryArrayMaker(json);
        json.put("model", "llama3");
        json.put("stream", false);
        // json.put("format", "json");
        json.put("keep_alive", 120);
        json.put("messages", messagesArray);
        json.put("temperature", temperature);
        return json;
    }

    private JSONObject generateSimpleJSON(String text){
        JSONObject json = new JSONObject();
        json.put("model", "llama3");
        json.put("stream", false);
        // json.put("format", "json");
        json.put("keep_alive", 120);
        json.put("prompt", text);
        json.put("temperature", temperature);
        return json;
    }
//TODO implement this method
private JSONArray chatHistoryArrayMaker(JSONObject json) {
    JSONArray chatHistoryArray = new JSONArray();
    JSONArray messagesArray = json.getJSONArray("messages");

    for (int i = 0; i < messagesArray.length(); i++) {
        JSONObject messageContent = messagesArray.getJSONObject(i);
        String role = messageContent.getString("role");
        String content = messageContent.getString("content");

        JSONObject message = new JSONObject();
        message.put("role", role);
        message.put("content", content);

        chatHistoryArray.put(message);

        // Maintain maximum chat history length
        if (chatHistoryArray.length() > 6) {
            chatHistoryArray.remove(0); // Remove oldest message if exceeds max length
        }
    }
            


        return chatHistoryArray;
    }
    // public static void main(String[] args) {
    

    // }
}
