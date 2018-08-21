package com.lvweihao.commonlib.pub;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.annotation.JSONField;
import com.lvweihao.commonlib.utils.PersistenceUtils;
import com.lvweihao.commonlib.utils.StringUtils;

import java.io.File;

/**
 * Created by lv.weihao on 2018/1/15.
 */
public class MWUser {
    private final static String KEY_LAST_USER = "lastUser";
    private final static String KEY_LAST_PHONE = "lastPhone";

    @JSONField(name = "yljgid")
    private String hospitalId;

    @JSONField(name = "id")
    private String userId;

    @JSONField(name = "xm")
    private String userName;

    @JSONField(name = "access_token")
    private String accessToken;

    @JSONField(name = "yljgmc")
    private String hospitalName;

    @JSONField(name = "wtgyy")
    private String failReson;

    @JSONField(name = "issh")
    private String isAudit;

    @JSONField(name = "zgzt")
    private String jobState;

    @JSONField(name = "rylx")
    private String userType;

    @JSONField(name = "sjh")
    private String phoneNum;

    @JSONField(name = "jgdwmc")
    private String supervisionName;

    @JSONField(name = "jgdwid")
    private String supervisionId;

    @JSONField(name = "xzqh")
    private String regionoCde;

    @JSONField(name = "xzqhfn")
    private String regionoName;

    @JSONField(name = "ryzp")
    private String userphoto;

    private String acceptId;
    private String acceptName;

    public String getUserphoto() {
        return userphoto;
    }

    public void setUserphoto(String userphoto) {
        this.userphoto = userphoto;
    }

    public String getAcceptId() {
        return acceptId;
    }

    public void setAcceptId(String acceptId) {
        this.acceptId = acceptId;
    }

    public String getAcceptName() {
        return acceptName;
    }

    public void setAcceptName(String acceptName) {
        this.acceptName = acceptName;
    }

    public String getHospitalId() {
        return hospitalId;
    }

    public void setHospitalId(String hospitalId) {
        this.hospitalId = hospitalId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getHospitalName() {
        return hospitalName;
    }

    public void setHospitalName(String hospitalName) {
        this.hospitalName = hospitalName;
    }

    public String getFailReson() {
        return failReson;
    }

    public void setFailReson(String failReson) {
        this.failReson = failReson;
    }

    public String getIsAudit() {
        return isAudit;
    }

    public void setIsAudit(String isAudit) {
        this.isAudit = isAudit;
    }

    public String getJobState() {
        return jobState;
    }

    public void setJobState(String jobState) {
        this.jobState = jobState;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getPhoneNum() {
        return phoneNum;
    }

    public void setPhoneNum(String phoneNum) {
        this.phoneNum = phoneNum;
    }

    public String getSupervisionName() {
        return supervisionName;
    }

    public void setSupervisionName(String supervisionName) {
        this.supervisionName = supervisionName;
    }

    public String getSupervisionId() {
        return supervisionId;
    }

    public void setSupervisionId(String supervisionId) {
        this.supervisionId = supervisionId;
    }

    public String getRegionoCde() {
        return regionoCde;
    }

    public void setRegionoCde(String regionoCde) {
        this.regionoCde = regionoCde;
    }

    public String getRegionoName() {
        return regionoName;
    }

    public void setRegionoName(String regionoName) {
        this.regionoName = regionoName;
    }

    private static File currentUserArchiveFile() {
        return new File(MWClient.getClientDocumentDir() + "/currentUser");
    }

    public synchronized static void changeCurrentUser(MWUser user) {
        File archiveFile = currentUserArchiveFile();
        if (user != null) {
            PersistenceUtils.saveStringToFile(JSON.toJSONString(user), archiveFile);
        } else {
            PersistenceUtils.removeLock(archiveFile.getAbsolutePath());
            archiveFile.delete();
        }
        MWClient.getInstance().setCurrentUser(user);
    }

    public static MWUser getCurrentUser() {
        MWUser user = MWClient.getInstance().getCurrentUser();
        if (user == null) {
            String json = PersistenceUtils.readStringFromFile(currentUserArchiveFile());
            if (!StringUtils.isBlankString(json)) {
                user = JSON.parseObject(json, MWUser.class);
                MWClient.getInstance().setCurrentUser(user);
            }
        }
        return user;
    }

    public static String getLastUserId() {
        return MWClient.getDefaultSetting(KEY_LAST_USER, null);
    }

    public static void updateLastUserId(String userId) {
        MWClient.saveDefaultSetting(KEY_LAST_USER, userId);
    }

    public static String getLastPhone() {
        return MWClient.getDefaultSetting(KEY_LAST_PHONE, null);
    }

    public static void updateLastPhone(String userId) {
        if (userId != null)
            MWClient.saveDefaultSetting(KEY_LAST_PHONE, userId);
    }
}
