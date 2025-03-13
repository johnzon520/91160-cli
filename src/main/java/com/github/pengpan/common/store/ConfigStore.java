package com.github.pengpan.common.store;

/**
 * @author pengpan
 */
public class ConfigStore {

    private static String cityId;

    private static String unitId;

    private static String deptId;

    private static String doctorId;

    private static String weekId;

    private static String dayId;

    private static String hours;

    private static String memberId;

    private static String sendKey;

    private static String ocrPlatform;

    public static String getCityId() {
        return cityId;
    }

    public static void setCityId(String cityId) {
        ConfigStore.cityId = cityId;
    }

    public static String getUnitId() {
        return unitId;
    }

    public static void setUnitId(String unitId) {
        ConfigStore.unitId = unitId;
    }

    public static String getDeptId() {
        return deptId;
    }

    public static void setDeptId(String deptId) {
        ConfigStore.deptId = deptId;
    }

    public static String getDoctorId() {
        return doctorId;
    }

    public static void setDoctorId(String doctorId) {
        ConfigStore.doctorId = doctorId;
    }

    public static String getWeekId() {
        return weekId;
    }

    public static void setWeekId(String weekId) {
        ConfigStore.weekId = weekId;
    }

    public static String getDayId() {
        return dayId;
    }

    public static void setDayId(String dayId) {
        ConfigStore.dayId = dayId;
    }

    public static String getMemberId() {
        return memberId;
    }

    public static void setMemberId(String memberId) {
        ConfigStore.memberId = memberId;
    }

    public static String getHours() {
        return hours;
    }

    public static void setHours(String hours) {
        ConfigStore.hours = hours;
    }

    public static String getSendKey() {
        return sendKey;
    }

    public static void setSendKey(String sendKey) {
        ConfigStore.sendKey = sendKey;
    }

    public static String getOcrPlatform() {
        return ocrPlatform;
    }

    public static void setOcrPlatform(String ocrPlatform) {
        ConfigStore.ocrPlatform = ocrPlatform;
    }
}
