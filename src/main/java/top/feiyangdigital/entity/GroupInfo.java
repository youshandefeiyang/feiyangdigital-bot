package top.feiyangdigital.entity;

import java.io.Serializable;

/**
 * groupinfo
 * @author 
 */
public class GroupInfo implements Serializable {
    private Integer id;

    private String groupid;

    private String ownerandanonymousadmins;

    private String groupname;

    private String keywordsflag;

    private String deletekeywordflag;

    private String settingtimestamp;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getGroupid() {
        return groupid;
    }

    public void setGroupid(String groupid) {
        this.groupid = groupid;
    }

    public String getOwnerandanonymousadmins() {
        return ownerandanonymousadmins;
    }

    public void setOwnerandanonymousadmins(String ownerandanonymousadmins) {
        this.ownerandanonymousadmins = ownerandanonymousadmins;
    }

    public String getGroupname() {
        return groupname;
    }

    public void setGroupname(String groupname) {
        this.groupname = groupname;
    }

    public String getKeywordsflag() {
        return keywordsflag;
    }

    public void setKeywordsflag(String keywordsflag) {
        this.keywordsflag = keywordsflag;
    }

    public String getDeletekeywordflag() {
        return deletekeywordflag;
    }

    public void setDeletekeywordflag(String deletekeywordflag) {
        this.deletekeywordflag = deletekeywordflag;
    }

    public String getSettingtimestamp() {
        return settingtimestamp;
    }

    public void setSettingtimestamp(String settingtimestamp) {
        this.settingtimestamp = settingtimestamp;
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
        GroupInfo other = (GroupInfo) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupid() == null ? other.getGroupid() == null : this.getGroupid().equals(other.getGroupid()))
            && (this.getOwnerandanonymousadmins() == null ? other.getOwnerandanonymousadmins() == null : this.getOwnerandanonymousadmins().equals(other.getOwnerandanonymousadmins()))
            && (this.getGroupname() == null ? other.getGroupname() == null : this.getGroupname().equals(other.getGroupname()))
            && (this.getKeywordsflag() == null ? other.getKeywordsflag() == null : this.getKeywordsflag().equals(other.getKeywordsflag()))
            && (this.getDeletekeywordflag() == null ? other.getDeletekeywordflag() == null : this.getDeletekeywordflag().equals(other.getDeletekeywordflag()))
            && (this.getSettingtimestamp() == null ? other.getSettingtimestamp() == null : this.getSettingtimestamp().equals(other.getSettingtimestamp()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupid() == null) ? 0 : getGroupid().hashCode());
        result = prime * result + ((getOwnerandanonymousadmins() == null) ? 0 : getOwnerandanonymousadmins().hashCode());
        result = prime * result + ((getGroupname() == null) ? 0 : getGroupname().hashCode());
        result = prime * result + ((getKeywordsflag() == null) ? 0 : getKeywordsflag().hashCode());
        result = prime * result + ((getDeletekeywordflag() == null) ? 0 : getDeletekeywordflag().hashCode());
        result = prime * result + ((getSettingtimestamp() == null) ? 0 : getSettingtimestamp().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", groupid=").append(groupid);
        sb.append(", ownerandanonymousadmins=").append(ownerandanonymousadmins);
        sb.append(", groupname=").append(groupname);
        sb.append(", keywordsflag=").append(keywordsflag);
        sb.append(", deletekeywordflag=").append(deletekeywordflag);
        sb.append(", settingtimestamp=").append(settingtimestamp);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}