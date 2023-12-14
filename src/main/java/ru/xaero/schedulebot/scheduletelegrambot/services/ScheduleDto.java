package ru.xaero.schedulebot.scheduletelegrambot.services;

import org.springframework.stereotype.Component;

import java.sql.Date;

@Component
public class ScheduleDto {
    private String teacherName;
    private int subgroupId;
    private String startTime;
    private String endTime;
    private Date date;
    private String groupName;
    private String typeName;
    private String objectName;

    public ScheduleDto() {
    }

    public ScheduleDto(String teacherName, int subgroupId, String startTime, String endTime, Date date, String groupName, String typeName, String objectName) {
        this.teacherName = teacherName;
        this.subgroupId = subgroupId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.date = date;
        this.groupName = groupName;
        this.typeName = typeName;
        this.objectName = objectName;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public int getSubgroupId() {
        return subgroupId;
    }

    public void setSubgroupId(int subgroupId) {
        this.subgroupId = subgroupId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }
}
