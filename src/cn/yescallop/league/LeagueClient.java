package cn.yescallop.league;

import cn.yescallop.league.utils.Utils;
import com.google.gson.JsonArray;
import okhttp3.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class LeagueClient {

    public static final String API_PATH_PUBLISHED = "https://tuanapi.12355.net/";
    public static final String API_PATH_TESTING = "http://tuanapitest.gdqnzs.cn/";

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .cookieJar(new CookieJar() {
                private List<Cookie> cookies = new ArrayList<>();

                @Override
                public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                    this.cookies = cookies;
                }

                @Override
                public List<Cookie> loadForRequest(HttpUrl url) {
                    return this.cookies;
                }
            })
            .build();

    private final String apiPath;

    public LeagueClient() {
        this(API_PATH_PUBLISHED);
    }

    public LeagueClient(String apiPath) {
        this.apiPath = apiPath;
    }

    /**
     * 登录
     */
    public LeagueResponse login(String username, String password) throws IOException {
        Request request = newRequestBuilder("login/Login?userName=" + username + "&password=" + password)
                .build();
        return this.call(request);
    }

    /**
     * 退出登录
     */
    public LeagueResponse logout() throws IOException {
        Request request = newRequestBuilder("login/exit").build();
        return this.call(request);
    }

    /**
     * 获取账号信息
     */
    public LeagueResponse getAccountInfo() throws IOException {
        Request request = newRequestBuilder("login/getSessionAccount").build();
        return this.call(request);
    }

    /**
     * 获取菜单列表
     */
    public LeagueResponse getMenuList() throws IOException {
        Request request = newRequestBuilder("bg/role/limit").build();
        return this.call(request);
    }

    /**
     * 获取组织树
     */
    public LeagueResponse getOrgTree(Integer id, int oid) throws IOException {
        String path = "bg/org/getOrgTree?oid=" + oid;
        if (id != null)
            path += "&id=" + id;
        Request request = newRequestBuilder(path)
                .build();
        return this.call(request);
    }

    public JsonArray getMembers() throws IOException {
        Request request = newRequestBuilder("members/bg/list?rows=2147483647").build();
        return this.call(request).parsed().getAsJsonArray("rows");
    }

    public LeagueResponse editMember(Map<String, String> params) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        params.forEach(builder::add);
        Request request = newRequestBuilder("members/bg/edit")
                .post(builder.build())
                .build();
        return this.call(request);
    }

    /* --- Internal Part --- */

    private Request.Builder newRequestBuilder(String path) {
        return new Request.Builder().url(apiPath + path);
    }

    private LeagueResponse call(Request request) throws IOException {
        return LeagueResponse.parse(httpClient.newCall(request).execute());
    }

}
