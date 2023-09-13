package top.feiyangdigital.entity;

import java.io.Serializable;

/**
 * groupinfo
 * @author 
 */
public class GroupInfoWithBLOBs extends GroupInfo implements Serializable {
    private String keywords;

    private String channelspammerswhitelist;

    private static final long serialVersionUID = 1L;

    public String getKeywords() {
        return keywords;
    }

    public void setKeywords(String keywords) {
        this.keywords = keywords;
    }

    public String getChannelspammerswhitelist() {
        return channelspammerswhitelist;
    }

    public void setChannelspammerswhitelist(String channelspammerswhitelist) {
        this.channelspammerswhitelist = channelspammerswhitelist;
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
        GroupInfoWithBLOBs other = (GroupInfoWithBLOBs) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getGroupid() == null ? other.getGroupid() == null : this.getGroupid().equals(other.getGroupid()))
            && (this.getOwnerandanonymousadmins() == null ? other.getOwnerandanonymousadmins() == null : this.getOwnerandanonymousadmins().equals(other.getOwnerandanonymousadmins()))
            && (this.getGroupname() == null ? other.getGroupname() == null : this.getGroupname().equals(other.getGroupname()))
            && (this.getKeywordsflag() == null ? other.getKeywordsflag() == null : this.getKeywordsflag().equals(other.getKeywordsflag()))
            && (this.getDeletekeywordflag() == null ? other.getDeletekeywordflag() == null : this.getDeletekeywordflag().equals(other.getDeletekeywordflag()))
            && (this.getSettingtimestamp() == null ? other.getSettingtimestamp() == null : this.getSettingtimestamp().equals(other.getSettingtimestamp()))
            && (this.getIntogroupcheckflag() == null ? other.getIntogroupcheckflag() == null : this.getIntogroupcheckflag().equals(other.getIntogroupcheckflag()))
            && (this.getIntogroupwelcomeflag() == null ? other.getIntogroupwelcomeflag() == null : this.getIntogroupwelcomeflag().equals(other.getIntogroupwelcomeflag()))
            && (this.getIntogroupusernamecheckflag() == null ? other.getIntogroupusernamecheckflag() == null : this.getIntogroupusernamecheckflag().equals(other.getIntogroupusernamecheckflag()))
            && (this.getKeywords() == null ? other.getKeywords() == null : this.getKeywords().equals(other.getKeywords()))
            && (this.getChannelspammerswhitelist() == null ? other.getChannelspammerswhitelist() == null : this.getChannelspammerswhitelist().equals(other.getChannelspammerswhitelist()));
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
        result = prime * result + ((getIntogroupcheckflag() == null) ? 0 : getIntogroupcheckflag().hashCode());
        result = prime * result + ((getIntogroupwelcomeflag() == null) ? 0 : getIntogroupwelcomeflag().hashCode());
        result = prime * result + ((getIntogroupusernamecheckflag() == null) ? 0 : getIntogroupusernamecheckflag().hashCode());
        result = prime * result + ((getKeywords() == null) ? 0 : getKeywords().hashCode());
        result = prime * result + ((getChannelspammerswhitelist() == null) ? 0 : getChannelspammerswhitelist().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", keywords=").append(keywords);
        sb.append(", channelspammerswhitelist=").append(channelspammerswhitelist);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}