package top.feiyangdigital.entity;

import java.util.ArrayList;
import java.util.List;

public class GroupInfoExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public GroupInfoExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setOffset(Long offset) {
        this.offset = offset;
    }

    public Long getOffset() {
        return offset;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(Integer value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(Integer value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(Integer value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(Integer value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(Integer value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(Integer value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<Integer> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<Integer> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(Integer value1, Integer value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(Integer value1, Integer value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNull() {
            addCriterion("groupId is null");
            return (Criteria) this;
        }

        public Criteria andGroupidIsNotNull() {
            addCriterion("groupId is not null");
            return (Criteria) this;
        }

        public Criteria andGroupidEqualTo(String value) {
            addCriterion("groupId =", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotEqualTo(String value) {
            addCriterion("groupId <>", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThan(String value) {
            addCriterion("groupId >", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidGreaterThanOrEqualTo(String value) {
            addCriterion("groupId >=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThan(String value) {
            addCriterion("groupId <", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLessThanOrEqualTo(String value) {
            addCriterion("groupId <=", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidLike(String value) {
            addCriterion("groupId like", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotLike(String value) {
            addCriterion("groupId not like", value, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidIn(List<String> values) {
            addCriterion("groupId in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotIn(List<String> values) {
            addCriterion("groupId not in", values, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidBetween(String value1, String value2) {
            addCriterion("groupId between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andGroupidNotBetween(String value1, String value2) {
            addCriterion("groupId not between", value1, value2, "groupid");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsIsNull() {
            addCriterion("ownerAndAnonymousAdmins is null");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsIsNotNull() {
            addCriterion("ownerAndAnonymousAdmins is not null");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsEqualTo(String value) {
            addCriterion("ownerAndAnonymousAdmins =", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsNotEqualTo(String value) {
            addCriterion("ownerAndAnonymousAdmins <>", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsGreaterThan(String value) {
            addCriterion("ownerAndAnonymousAdmins >", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsGreaterThanOrEqualTo(String value) {
            addCriterion("ownerAndAnonymousAdmins >=", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsLessThan(String value) {
            addCriterion("ownerAndAnonymousAdmins <", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsLessThanOrEqualTo(String value) {
            addCriterion("ownerAndAnonymousAdmins <=", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsLike(String value) {
            addCriterion("ownerAndAnonymousAdmins like", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsNotLike(String value) {
            addCriterion("ownerAndAnonymousAdmins not like", value, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsIn(List<String> values) {
            addCriterion("ownerAndAnonymousAdmins in", values, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsNotIn(List<String> values) {
            addCriterion("ownerAndAnonymousAdmins not in", values, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsBetween(String value1, String value2) {
            addCriterion("ownerAndAnonymousAdmins between", value1, value2, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andOwnerandanonymousadminsNotBetween(String value1, String value2) {
            addCriterion("ownerAndAnonymousAdmins not between", value1, value2, "ownerandanonymousadmins");
            return (Criteria) this;
        }

        public Criteria andGroupnameIsNull() {
            addCriterion("groupName is null");
            return (Criteria) this;
        }

        public Criteria andGroupnameIsNotNull() {
            addCriterion("groupName is not null");
            return (Criteria) this;
        }

        public Criteria andGroupnameEqualTo(String value) {
            addCriterion("groupName =", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotEqualTo(String value) {
            addCriterion("groupName <>", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameGreaterThan(String value) {
            addCriterion("groupName >", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameGreaterThanOrEqualTo(String value) {
            addCriterion("groupName >=", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLessThan(String value) {
            addCriterion("groupName <", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLessThanOrEqualTo(String value) {
            addCriterion("groupName <=", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameLike(String value) {
            addCriterion("groupName like", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotLike(String value) {
            addCriterion("groupName not like", value, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameIn(List<String> values) {
            addCriterion("groupName in", values, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotIn(List<String> values) {
            addCriterion("groupName not in", values, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameBetween(String value1, String value2) {
            addCriterion("groupName between", value1, value2, "groupname");
            return (Criteria) this;
        }

        public Criteria andGroupnameNotBetween(String value1, String value2) {
            addCriterion("groupName not between", value1, value2, "groupname");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagIsNull() {
            addCriterion("keyWordsflag is null");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagIsNotNull() {
            addCriterion("keyWordsflag is not null");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagEqualTo(String value) {
            addCriterion("keyWordsflag =", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagNotEqualTo(String value) {
            addCriterion("keyWordsflag <>", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagGreaterThan(String value) {
            addCriterion("keyWordsflag >", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagGreaterThanOrEqualTo(String value) {
            addCriterion("keyWordsflag >=", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagLessThan(String value) {
            addCriterion("keyWordsflag <", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagLessThanOrEqualTo(String value) {
            addCriterion("keyWordsflag <=", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagLike(String value) {
            addCriterion("keyWordsflag like", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagNotLike(String value) {
            addCriterion("keyWordsflag not like", value, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagIn(List<String> values) {
            addCriterion("keyWordsflag in", values, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagNotIn(List<String> values) {
            addCriterion("keyWordsflag not in", values, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagBetween(String value1, String value2) {
            addCriterion("keyWordsflag between", value1, value2, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andKeywordsflagNotBetween(String value1, String value2) {
            addCriterion("keyWordsflag not between", value1, value2, "keywordsflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagIsNull() {
            addCriterion("deleteKeywordFlag is null");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagIsNotNull() {
            addCriterion("deleteKeywordFlag is not null");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagEqualTo(String value) {
            addCriterion("deleteKeywordFlag =", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagNotEqualTo(String value) {
            addCriterion("deleteKeywordFlag <>", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagGreaterThan(String value) {
            addCriterion("deleteKeywordFlag >", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagGreaterThanOrEqualTo(String value) {
            addCriterion("deleteKeywordFlag >=", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagLessThan(String value) {
            addCriterion("deleteKeywordFlag <", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagLessThanOrEqualTo(String value) {
            addCriterion("deleteKeywordFlag <=", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagLike(String value) {
            addCriterion("deleteKeywordFlag like", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagNotLike(String value) {
            addCriterion("deleteKeywordFlag not like", value, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagIn(List<String> values) {
            addCriterion("deleteKeywordFlag in", values, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagNotIn(List<String> values) {
            addCriterion("deleteKeywordFlag not in", values, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagBetween(String value1, String value2) {
            addCriterion("deleteKeywordFlag between", value1, value2, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andDeletekeywordflagNotBetween(String value1, String value2) {
            addCriterion("deleteKeywordFlag not between", value1, value2, "deletekeywordflag");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampIsNull() {
            addCriterion("settingTimeStamp is null");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampIsNotNull() {
            addCriterion("settingTimeStamp is not null");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampEqualTo(String value) {
            addCriterion("settingTimeStamp =", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampNotEqualTo(String value) {
            addCriterion("settingTimeStamp <>", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampGreaterThan(String value) {
            addCriterion("settingTimeStamp >", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampGreaterThanOrEqualTo(String value) {
            addCriterion("settingTimeStamp >=", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampLessThan(String value) {
            addCriterion("settingTimeStamp <", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampLessThanOrEqualTo(String value) {
            addCriterion("settingTimeStamp <=", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampLike(String value) {
            addCriterion("settingTimeStamp like", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampNotLike(String value) {
            addCriterion("settingTimeStamp not like", value, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampIn(List<String> values) {
            addCriterion("settingTimeStamp in", values, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampNotIn(List<String> values) {
            addCriterion("settingTimeStamp not in", values, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampBetween(String value1, String value2) {
            addCriterion("settingTimeStamp between", value1, value2, "settingtimestamp");
            return (Criteria) this;
        }

        public Criteria andSettingtimestampNotBetween(String value1, String value2) {
            addCriterion("settingTimeStamp not between", value1, value2, "settingtimestamp");
            return (Criteria) this;
        }
    }

    /**
     */
    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}