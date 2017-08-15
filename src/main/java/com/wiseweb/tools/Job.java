package com.wiseweb.tools;

import java.util.List;

/**
 * Created by Sane on 2016/10/20.
 */
public class Job {

    /**
     * _id : testJob
     * updateTime :
     * jobName : testJob
     * className : className2
     * taskConfigs :
     * concurrent : false
     * warnList : ["email1@sina.com","email2@sina.com"]
     * hosts : ["host1"]
     */

    private String _id;
    private String updateTime;
    private String jobName;
    private String cronExpression;
    private String className;
    private String method;
    private String taskConfigs;
    private boolean concurrent;
    private List<String> warnList;
    private List<String> hosts;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(String updateTime) {
        this.updateTime = updateTime;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getTaskConfigs() {
        return taskConfigs;
    }

    public void setTaskConfigs(String taskConfigs) {
        this.taskConfigs = taskConfigs;
    }

    public boolean isConcurrent() {
        return concurrent;
    }

    public void setConcurrent(boolean concurrent) {
        this.concurrent = concurrent;
    }

    public List<String> getWarnList() {
        return warnList;
    }

    public void setWarnList(List<String> warnList) {
        this.warnList = warnList;
    }

    public List<String> getHosts() {
        return hosts;
    }

    public void setHosts(List<String> hosts) {
        this.hosts = hosts;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }
}
