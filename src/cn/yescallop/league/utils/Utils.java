package cn.yescallop.league.utils;

import cn.yescallop.league.LeagueResponse;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

public class Utils {

    private static final Map<String, String> ORG_PARAMS = new HashMap<>();
    private static final String[] ORG_TYPES = {"领导机关团组织", "团委", "团工委", "团总支", "团支部"};
    private static final String[] ORG_INDUSTRY_CATEGORIES = {
            "党政机关", "事业单位（不含公立学校）", "普通高等院校",
            "职业教育学校", "普通高中", "初中", "小学",
            "国有企业", "集体企业", "非公企业",
            "新社会组织（不含民办学校）", "军队", "武警",
            "城市社区", "农村", "其他"
    };

    //TODO: Wait for a fix of "dateOfDuty"
    private static final String[] EDIT_PARAMS = new String[]{
            "income", "nation", "politicalOutlook", "degreeOfEducation", "highestEducation",
            "provinceDid", "cityDid", "isCadres", "mobile", "mid",
            "occupation", "learningUnit", "email", "qqNum", "wechatId",
            "weibo", "developmentMemberNumber", "incumbent",
            /*"dateOfDuty",*/ "tuanganProperties", "isPartyCommitteeMember",
            "thePartyYears", "signUpForVolunteerTime"
    };

    static {
        ORG_PARAMS.put("parent", "上级组织");
        ORG_PARAMS.put("type", "组织类型");
        ORG_PARAMS.put("fullName", "团组织全称");
        ORG_PARAMS.put("name", "团组织简称");
        ORG_PARAMS.put("enterpriseName", "组织代称");
        ORG_PARAMS.put("mobile", "团组织联系电话");
        ORG_PARAMS.put("email", "团组织电子邮箱");
        ORG_PARAMS.put("administrativeOmpilation", "本级团组织行政编制数");
        ORG_PARAMS.put("administrativeNumber", "行政编制实际配备数");
        ORG_PARAMS.put("careerFormation", "本级团组织事业编制数");
        ORG_PARAMS.put("serviceNumber", "事业编制实际配备数");
        ORG_PARAMS.put("industryCategory", "单位所属行业类别");
        ORG_PARAMS.put("username", "登录账号");
        ORG_PARAMS.put("password", "密码");
        ORG_PARAMS.put("secretaryName", "团组织书记姓名");
        ORG_PARAMS.put("groupOrganizationCode", "团组织机构代码");
        ORG_PARAMS.put("groupOrganizationWechatid", "团组织微信号");
        ORG_PARAMS.put("groupOrganizationWeibo", "团组织微博号");
    }

    private Utils() {
        //no instance
    }

    public static Map<String, String> parseOrgInfo(LeagueResponse response) {
        JsonObject account = response.parsed().getAsJsonObject("account");
        Map<String, String> result = new HashMap<>();
        ORG_PARAMS.forEach((k, n) -> {
            JsonElement e = account.get(k);
            String s;
            switch (k) {
                case "type":
                    s = ORG_TYPES[e.getAsInt() - 1];
                    break;
                case "industryCategory":
                    s = ORG_INDUSTRY_CATEGORIES[e.getAsInt() - 1];
                    break;
                default:
                    if (e == null || e.isJsonNull()) s = "未指定";
                    else if ((s = e.getAsString()).isEmpty()) s = "无";
                    break;
            }
            result.put(n, s);
        });
        return result;
    }

    public static Map<String, String> createParamsMapForEdit(JsonObject obj) {
        Map<String, String> map = new HashMap<>();
        for (String key : EDIT_PARAMS) {
            JsonElement e = obj.get(key);
            if (!e.isJsonNull()) {
                map.put(key, e.getAsString());
            }
        }
        return map;
    }
}
