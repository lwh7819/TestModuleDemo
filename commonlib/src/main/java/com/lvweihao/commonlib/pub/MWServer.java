package com.lvweihao.commonlib.pub;

import com.alibaba.fastjson.JSON;
import com.lvweihao.commonlib.utils.PersistenceUtils;

import java.util.List;

/**
 * Created by lv.weihao on 2018/1/15.
 */
public class MWServer {
    private final static String LAST_SERVER_KEY = "lastServer";
    private final static String LOCAL_SERVER_KEY = "localServer";

    private String url;
    private String name;
    private int type; //0.正式 1.测试 2.本地

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    /**
     * 获取一般通讯url，登录验证和退出不能用
     * @return
     */
    public static String getServerUrl(){
        return "/ydzf/service";
    }

    /**
     * 获取登录验证、退出url
     * @return
     */
    public static String getAuthUrl(){
        return "/ydzf/auth";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (null == obj || getClass() != obj.getClass()) return false;

        MWServer server = (MWServer) obj;
        return type == server.type && !(url != null ? !url.equals(server.url) : server.url != null
                && !(name != null ? !name.equals(server.name) : server.name != null));
    }

    @Override
    public int hashCode() {
        int result = url != null ? url.hashCode() : 0;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + type;
        return result;
    }

    /**
     * 通过json文件获取服务器列表信息
     * @return
     */
    public static List<MWServer> getServerList() {
        String str = PersistenceUtils.readStringFromAssetsFile(MWClient.getConText(), "servers/servers.json");
        return JSON.parseArray(str, MWServer.class);
    }

    /**
     * 获取服务器地址
     * @return
     */
    public static MWServer getDefaultServer() {
        String json = MWClient.getDefaultSetting(LAST_SERVER_KEY, null);
        MWServer server = JSON.parseObject(json, MWServer.class);
        if (server == null) {
            server = getServerList().get(0);
        }
        return server;
    }

    /**
     * 保存服务器地址
     * @param server
     */
    public static void updateDefaultServer(MWServer server) {
        MWClient.saveDefaultSetting(LAST_SERVER_KEY, JSON.toJSONString(server));
    }

    /**
     * 获取本地服务器地址
     * @return
     */
    public static String getLocalServer() {
        return MWClient.getDefaultSetting(LOCAL_SERVER_KEY, null);
    }

    /**
     * 保存本地服务器地址
     * @param localServer
     */
    public static void updateLocalServer(String localServer) {
        MWClient.saveDefaultSetting(LOCAL_SERVER_KEY, localServer);
    }
}
