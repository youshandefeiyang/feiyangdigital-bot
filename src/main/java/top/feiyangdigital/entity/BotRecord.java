package top.feiyangdigital.entity;

import java.io.Serializable;

/**
 * botrecord
 * @author 
 */
public class BotRecord implements Serializable {
    private Integer rid;

    private String groupid;

    private String userid;

    private String jointimestamp;

    private Integer violationcount;

    private Integer normalcount;

    private String lastmessage;

    private static final long serialVersionUID = 1L;

    public Integer getRid() {
        return rid;
    }

    public void setRid(Integer rid) {
        this.rid = rid;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getJointimestamp() {
        return jointimestamp;
    }

    public void setJointimestamp(String jointimestamp) {
        this.jointimestamp = jointimestamp;
    }

    public Integer getViolationcount() {
        return violationcount;
    }

    public void setViolationcount(Integer violationcount) {
        this.violationcount = violationcount;
    }

    public Integer getNormalcount() {
        return normalcount;
    }

    public void setNormalcount(Integer normalcount) {
        this.normalcount = normalcount;
    }

    public String getLastmessage() {
        return lastmessage;
    }

    public void setLastmessage(String lastmessage) {
        this.lastmessage = lastmessage;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        BotRecord other = (BotRecord) that;
        return (this.getRid() == null ? other.getRid() == null : this.getRid().equals(other.getRid()))
            && (this.getGroupid() == null ? other.getGroupid() == null : this.getGroupid().equalsIgnoreCase(other.getGroupid()))
            && (this.getUserid() == null ? other.getUserid() == null : this.getUserid().equalsIgnoreCase(other.getUserid()))
            && (this.getJointimestamp() == null ? other.getJointimestamp() == null : this.getJointimestamp().equalsIgnoreCase(other.getJointimestamp()))
            && (this.getViolationcount() == null ? other.getViolationcount() == null : this.getViolationcount().equals(other.getViolationcount()))
            && (this.getNormalcount() == null ? other.getNormalcount() == null : this.getNormalcount().equals(other.getNormalcount()))
            && (this.getLastmessage() == null ? other.getLastmessage() == null : this.getLastmessage().equalsIgnoreCase(other.getLastmessage()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getRid() == null) ? 0 : getRid().hashCode());
        result = prime * result + ((getGroupid() == null) ? 0 : getGroupid().hashCode());
        result = prime * result + ((getUserid() == null) ? 0 : getUserid().hashCode());
        result = prime * result + ((getJointimestamp() == null) ? 0 : getJointimestamp().hashCode());
        result = prime * result + ((getViolationcount() == null) ? 0 : getViolationcount().hashCode());
        result = prime * result + ((getNormalcount() == null) ? 0 : getNormalcount().hashCode());
        result = prime * result + ((getLastmessage() == null) ? 0 : getLastmessage().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", rid=").append(rid);
        sb.append(", groupid=").append(groupid);
        sb.append(", userid=").append(userid);
        sb.append(", jointimestamp=").append(jointimestamp);
        sb.append(", violationcount=").append(violationcount);
        sb.append(", normalcount=").append(normalcount);
        sb.append(", lastmessage=").append(lastmessage);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}