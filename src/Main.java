import cn.yescallop.league.LeagueClient;
import cn.yescallop.league.LeagueResponse;
import cn.yescallop.league.utils.Utils;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.io.IOException;
import java.util.Map;

public class Main {

    public static void main(String[] args) {
        LeagueClient client = new LeagueClient();
        try {
            output(client.login("*", "*"));
            JsonArray arr = client.getMembers();
            for (JsonElement e : arr) {
                Map<String, String> map = Utils.createParamsMapForEdit(e.getAsJsonObject());
                map.put("income", "1");
                map.put("highestEducation", "1");
                map.put("politicalOutlook", "1");
                map.put("occupation", "6");
                map.remove("thePartyYears");
                map.remove("signUpForVolunteerTime");
                output(client.editMember(map));
            }
            output(client.logout());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private static void output(LeagueResponse response) {
        System.out.println(response.rawContent());
    }
}
