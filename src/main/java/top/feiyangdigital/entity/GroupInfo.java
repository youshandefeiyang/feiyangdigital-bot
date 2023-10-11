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

    private String antifloodsetting;

    private String captchamode;

    private String keywordsflag;

    private String deletekeywordflag;

    private String settingtimestamp;

    private String intogroupcheckflag;

    private String intogroupwelcomeflag;

    private String intogroupusernamecheckflag;

    private String aiflag;

    private String crontabflag;

    private String nightmodeflag;

    private String cansendmediaflag;

    private String clearinfoflag;

    private String reportflag;

    private String antifloodflag;

    private String channelspamflag;

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

    public String getAntifloodsetting() {
        return antifloodsetting;
    }

    public void setAntifloodsetting(String antifloodsetting) {
        this.antifloodsetting = antifloodsetting;
    }

    public String getCaptchamode() {
        return captchamode;
    }

    public void setCaptchamode(String captchamode) {
        this.captchamode = captchamode;
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

    public String getIntogroupcheckflag() {
        return intogroupcheckflag;
    }

    public void setIntogroupcheckflag(String intogroupcheckflag) {
        this.intogroupcheckflag = intogroupcheckflag;
    }

    public String getIntogroupwelcomeflag() {
        return intogroupwelcomeflag;
    }

    public void setIntogroupwelcomeflag(String intogroupwelcomeflag) {
        this.intogroupwelcomeflag = intogroupwelcomeflag;
    }

    public String getIntogroupusernamecheckflag() {
        return intogroupusernamecheckflag;
    }

    public void setIntogroupusernamecheckflag(String intogroupusernamecheckflag) {
        this.intogroupusernamecheckflag = intogroupusernamecheckflag;
    }

    public String getAiflag() {
        return aiflag;
    }

    public void setAiflag(String aiflag) {
        this.aiflag = aiflag;
    }

    public String getCrontabflag() {
        return crontabflag;
    }

    public void setCrontabflag(String crontabflag) {
        this.crontabflag = crontabflag;
    }

    public String getNightmodeflag() {
        return nightmodeflag;
    }

    public void setNightmodeflag(String nightmodeflag) {
        this.nightmodeflag = nightmodeflag;
    }

    public String getCansendmediaflag() {
        return cansendmediaflag;
    }

    public void setCansendmediaflag(String cansendmediaflag) {
        this.cansendmediaflag = cansendmediaflag;
    }

    public String getClearinfoflag() {
        return clearinfoflag;
    }

    public void setClearinfoflag(String clearinfoflag) {
        this.clearinfoflag = clearinfoflag;
    }

    public String getReportflag() {
        return reportflag;
    }

    public void setReportflag(String reportflag) {
        this.reportflag = reportflag;
    }

    public String getAntifloodflag() {
        return antifloodflag;
    }

    public void setAntifloodflag(String antifloodflag) {
        this.antifloodflag = antifloodflag;
    }

    public String getChannelspamflag() {
        return channelspamflag;
    }

    public void setChannelspamflag(String channelspamflag) {
        this.channelspamflag = channelspamflag;
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
            && (this.getAntifloodsetting() == null ? other.getAntifloodsetting() == null : this.getAntifloodsetting().equals(other.getAntifloodsetting()))
            && (this.getCaptchamode() == null ? other.getCaptchamode() == null : this.getCaptchamode().equals(other.getCaptchamode()))
            && (this.getKeywordsflag() == null ? other.getKeywordsflag() == null : this.getKeywordsflag().equals(other.getKeywordsflag()))
            && (this.getDeletekeywordflag() == null ? other.getDeletekeywordflag() == null : this.getDeletekeywordflag().equals(other.getDeletekeywordflag()))
            && (this.getSettingtimestamp() == null ? other.getSettingtimestamp() == null : this.getSettingtimestamp().equals(other.getSettingtimestamp()))
            && (this.getIntogroupcheckflag() == null ? other.getIntogroupcheckflag() == null : this.getIntogroupcheckflag().equals(other.getIntogroupcheckflag()))
            && (this.getIntogroupwelcomeflag() == null ? other.getIntogroupwelcomeflag() == null : this.getIntogroupwelcomeflag().equals(other.getIntogroupwelcomeflag()))
            && (this.getIntogroupusernamecheckflag() == null ? other.getIntogroupusernamecheckflag() == null : this.getIntogroupusernamecheckflag().equals(other.getIntogroupusernamecheckflag()))
            && (this.getAiflag() == null ? other.getAiflag() == null : this.getAiflag().equals(other.getAiflag()))
            && (this.getCrontabflag() == null ? other.getCrontabflag() == null : this.getCrontabflag().equals(other.getCrontabflag()))
            && (this.getNightmodeflag() == null ? other.getNightmodeflag() == null : this.getNightmodeflag().equals(other.getNightmodeflag()))
            && (this.getCansendmediaflag() == null ? other.getCansendmediaflag() == null : this.getCansendmediaflag().equals(other.getCansendmediaflag()))
            && (this.getClearinfoflag() == null ? other.getClearinfoflag() == null : this.getClearinfoflag().equals(other.getClearinfoflag()))
            && (this.getReportflag() == null ? other.getReportflag() == null : this.getReportflag().equals(other.getReportflag()))
            && (this.getAntifloodflag() == null ? other.getAntifloodflag() == null : this.getAntifloodflag().equals(other.getAntifloodflag()))
            && (this.getChannelspamflag() == null ? other.getChannelspamflag() == null : this.getChannelspamflag().equals(other.getChannelspamflag()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getGroupid() == null) ? 0 : getGroupid().hashCode());
        result = prime * result + ((getOwnerandanonymousadmins() == null) ? 0 : getOwnerandanonymousadmins().hashCode());
        result = prime * result + ((getGroupname() == null) ? 0 : getGroupname().hashCode());
        result = prime * result + ((getAntifloodsetting() == null) ? 0 : getAntifloodsetting().hashCode());
        result = prime * result + ((getCaptchamode() == null) ? 0 : getCaptchamode().hashCode());
        result = prime * result + ((getKeywordsflag() == null) ? 0 : getKeywordsflag().hashCode());
        result = prime * result + ((getDeletekeywordflag() == null) ? 0 : getDeletekeywordflag().hashCode());
        result = prime * result + ((getSettingtimestamp() == null) ? 0 : getSettingtimestamp().hashCode());
        result = prime * result + ((getIntogroupcheckflag() == null) ? 0 : getIntogroupcheckflag().hashCode());
        result = prime * result + ((getIntogroupwelcomeflag() == null) ? 0 : getIntogroupwelcomeflag().hashCode());
        result = prime * result + ((getIntogroupusernamecheckflag() == null) ? 0 : getIntogroupusernamecheckflag().hashCode());
        result = prime * result + ((getAiflag() == null) ? 0 : getAiflag().hashCode());
        result = prime * result + ((getCrontabflag() == null) ? 0 : getCrontabflag().hashCode());
        result = prime * result + ((getNightmodeflag() == null) ? 0 : getNightmodeflag().hashCode());
        result = prime * result + ((getCansendmediaflag() == null) ? 0 : getCansendmediaflag().hashCode());
        result = prime * result + ((getClearinfoflag() == null) ? 0 : getClearinfoflag().hashCode());
        result = prime * result + ((getReportflag() == null) ? 0 : getReportflag().hashCode());
        result = prime * result + ((getAntifloodflag() == null) ? 0 : getAntifloodflag().hashCode());
        result = prime * result + ((getChannelspamflag() == null) ? 0 : getChannelspamflag().hashCode());
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
        sb.append(", antifloodsetting=").append(antifloodsetting);
        sb.append(", captchamode=").append(captchamode);
        sb.append(", keywordsflag=").append(keywordsflag);
        sb.append(", deletekeywordflag=").append(deletekeywordflag);
        sb.append(", settingtimestamp=").append(settingtimestamp);
        sb.append(", intogroupcheckflag=").append(intogroupcheckflag);
        sb.append(", intogroupwelcomeflag=").append(intogroupwelcomeflag);
        sb.append(", intogroupusernamecheckflag=").append(intogroupusernamecheckflag);
        sb.append(", aiflag=").append(aiflag);
        sb.append(", crontabflag=").append(crontabflag);
        sb.append(", nightmodeflag=").append(nightmodeflag);
        sb.append(", cansendmediaflag=").append(cansendmediaflag);
        sb.append(", clearinfoflag=").append(clearinfoflag);
        sb.append(", reportflag=").append(reportflag);
        sb.append(", antifloodflag=").append(antifloodflag);
        sb.append(", channelspamflag=").append(channelspamflag);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}