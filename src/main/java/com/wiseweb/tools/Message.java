package com.wiseweb.tools;

/**
 * Created by Sane on 2016/10/20.
 */
import java.util.Date;

public class Message {


    /**
     * createTime :
     * jobId : testJob
     * jobConfig : {}
     * message : SUCCESS
     * status : DONE
     * updateTime :
     */

    private String _id;
    private Date createTime;
    private String jobId;
    private String message;
    private String status;
    private Job jobConfig;
    private String host;
    private Date updateTime;
    private String trmVersion;

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public String getJobId() {
        return jobId;
    }

    public void setJobId(String jobId) {
        this.jobId = jobId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Job getJobConfig() {
        return jobConfig;
    }

    public void setJobConfig(Job jobConfig) {
        this.jobConfig = jobConfig;
    }

    public String getTrmVersion() {
        return trmVersion;
    }

    public void setTrmVersion(String trmVersion) {
        this.trmVersion = trmVersion;
    }
}
