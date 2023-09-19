package top.feiyangdigital.entity;

import java.util.ArrayList;
import java.util.List;

public class BotRecordExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    private Integer limit;

    private Long offset;

    public BotRecordExample() {
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

        public Criteria andRidIsNull() {
            addCriterion("rid is null");
            return (Criteria) this;
        }

        public Criteria andRidIsNotNull() {
            addCriterion("rid is not null");
            return (Criteria) this;
        }

        public Criteria andRidEqualTo(Integer value) {
            addCriterion("rid =", value, "rid");
            return (Criteria) this;
        }

        public Criteria andRidNotEqualTo(Integer value) {
            addCriterion("rid <>", value, "rid");
            return (Criteria) this;
        }

        public Criteria andRidGreaterThan(Integer value) {
            addCriterion("rid >", value, "rid");
            return (Criteria) this;
        }

        public Criteria andRidGreaterThanOrEqualTo(Integer value) {
            addCriterion("rid >=", value, "rid");
            return (Criteria) this;
        }

        public Criteria andRidLessThan(Integer value) {
            addCriterion("rid <", value, "rid");
            return (Criteria) this;
        }

        public Criteria andRidLessThanOrEqualTo(Integer value) {
            addCriterion("rid <=", value, "rid");
            return (Criteria) this;
        }

        public Criteria andRidIn(List<Integer> values) {
            addCriterion("rid in", values, "rid");
            return (Criteria) this;
        }

        public Criteria andRidNotIn(List<Integer> values) {
            addCriterion("rid not in", values, "rid");
            return (Criteria) this;
        }

        public Criteria andRidBetween(Integer value1, Integer value2) {
            addCriterion("rid between", value1, value2, "rid");
            return (Criteria) this;
        }

        public Criteria andRidNotBetween(Integer value1, Integer value2) {
            addCriterion("rid not between", value1, value2, "rid");
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

        public Criteria andUseridIsNull() {
            addCriterion("userId is null");
            return (Criteria) this;
        }

        public Criteria andUseridIsNotNull() {
            addCriterion("userId is not null");
            return (Criteria) this;
        }

        public Criteria andUseridEqualTo(String value) {
            addCriterion("userId =", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotEqualTo(String value) {
            addCriterion("userId <>", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThan(String value) {
            addCriterion("userId >", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridGreaterThanOrEqualTo(String value) {
            addCriterion("userId >=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThan(String value) {
            addCriterion("userId <", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLessThanOrEqualTo(String value) {
            addCriterion("userId <=", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridLike(String value) {
            addCriterion("userId like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotLike(String value) {
            addCriterion("userId not like", value, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridIn(List<String> values) {
            addCriterion("userId in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotIn(List<String> values) {
            addCriterion("userId not in", values, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridBetween(String value1, String value2) {
            addCriterion("userId between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andUseridNotBetween(String value1, String value2) {
            addCriterion("userId not between", value1, value2, "userid");
            return (Criteria) this;
        }

        public Criteria andJointimestampIsNull() {
            addCriterion("joinTimestamp is null");
            return (Criteria) this;
        }

        public Criteria andJointimestampIsNotNull() {
            addCriterion("joinTimestamp is not null");
            return (Criteria) this;
        }

        public Criteria andJointimestampEqualTo(String value) {
            addCriterion("joinTimestamp =", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampNotEqualTo(String value) {
            addCriterion("joinTimestamp <>", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampGreaterThan(String value) {
            addCriterion("joinTimestamp >", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampGreaterThanOrEqualTo(String value) {
            addCriterion("joinTimestamp >=", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampLessThan(String value) {
            addCriterion("joinTimestamp <", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampLessThanOrEqualTo(String value) {
            addCriterion("joinTimestamp <=", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampLike(String value) {
            addCriterion("joinTimestamp like", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampNotLike(String value) {
            addCriterion("joinTimestamp not like", value, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampIn(List<String> values) {
            addCriterion("joinTimestamp in", values, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampNotIn(List<String> values) {
            addCriterion("joinTimestamp not in", values, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampBetween(String value1, String value2) {
            addCriterion("joinTimestamp between", value1, value2, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andJointimestampNotBetween(String value1, String value2) {
            addCriterion("joinTimestamp not between", value1, value2, "jointimestamp");
            return (Criteria) this;
        }

        public Criteria andViolationcountIsNull() {
            addCriterion("violationCount is null");
            return (Criteria) this;
        }

        public Criteria andViolationcountIsNotNull() {
            addCriterion("violationCount is not null");
            return (Criteria) this;
        }

        public Criteria andViolationcountEqualTo(Integer value) {
            addCriterion("violationCount =", value, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountNotEqualTo(Integer value) {
            addCriterion("violationCount <>", value, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountGreaterThan(Integer value) {
            addCriterion("violationCount >", value, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("violationCount >=", value, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountLessThan(Integer value) {
            addCriterion("violationCount <", value, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountLessThanOrEqualTo(Integer value) {
            addCriterion("violationCount <=", value, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountIn(List<Integer> values) {
            addCriterion("violationCount in", values, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountNotIn(List<Integer> values) {
            addCriterion("violationCount not in", values, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountBetween(Integer value1, Integer value2) {
            addCriterion("violationCount between", value1, value2, "violationcount");
            return (Criteria) this;
        }

        public Criteria andViolationcountNotBetween(Integer value1, Integer value2) {
            addCriterion("violationCount not between", value1, value2, "violationcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountIsNull() {
            addCriterion("normalCount is null");
            return (Criteria) this;
        }

        public Criteria andNormalcountIsNotNull() {
            addCriterion("normalCount is not null");
            return (Criteria) this;
        }

        public Criteria andNormalcountEqualTo(Integer value) {
            addCriterion("normalCount =", value, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountNotEqualTo(Integer value) {
            addCriterion("normalCount <>", value, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountGreaterThan(Integer value) {
            addCriterion("normalCount >", value, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountGreaterThanOrEqualTo(Integer value) {
            addCriterion("normalCount >=", value, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountLessThan(Integer value) {
            addCriterion("normalCount <", value, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountLessThanOrEqualTo(Integer value) {
            addCriterion("normalCount <=", value, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountIn(List<Integer> values) {
            addCriterion("normalCount in", values, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountNotIn(List<Integer> values) {
            addCriterion("normalCount not in", values, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountBetween(Integer value1, Integer value2) {
            addCriterion("normalCount between", value1, value2, "normalcount");
            return (Criteria) this;
        }

        public Criteria andNormalcountNotBetween(Integer value1, Integer value2) {
            addCriterion("normalCount not between", value1, value2, "normalcount");
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