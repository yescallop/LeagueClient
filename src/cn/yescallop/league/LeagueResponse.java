package cn.yescallop.league;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import okhttp3.Response;
import okhttp3.ResponseBody;

import java.io.IOException;

public class LeagueResponse {

    private final boolean isSuccessful;
    private final Response rawResponse;
    private final String rawContent;
    private final String status;
    private final JsonObject parsedJson;

    private LeagueResponse(boolean isSuccessful, Response rawResponse, String rawContent, String status, JsonObject json) {
        this.isSuccessful = isSuccessful;
        this.rawResponse = rawResponse;
        this.rawContent = rawContent;
        this.status = status;
        this.parsedJson = json;
    }

    public static LeagueResponse parse(Response response) throws IOException {
        JsonObject json = null;
        String content = null;
        String status = null;
        ResponseBody body = response.body();
        boolean isSuccessful = true;
        if (body == null) {
            isSuccessful = false;
        } else {
            content = body.string();
            if (!response.isSuccessful()) {
                isSuccessful = false;
            } else if (!content.isEmpty()) {
                json = new Gson().fromJson(content, JsonObject.class);
                status = json.get("status").getAsString();
                if (status == null || !status.equals("OK"))
                    isSuccessful = false;
            }
        }

        return new LeagueResponse(isSuccessful, response, content, status, json);
    }

    public boolean isSuccessful() {
        return isSuccessful;
    }

    public String status() {
        return status;
    }

    public String rawContent() {
        return rawContent;
    }

    public Response rawResponse() {
        return rawResponse;
    }

    public boolean isEmpty() {
        return parsedJson == null;
    }

    public JsonObject parsed() {
        return parsedJson;
    }
}
