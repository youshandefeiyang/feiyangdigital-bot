package top.feiyangdigital.sqlService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.feiyangdigital.entity.BotRecord;
import top.feiyangdigital.entity.BotRecordExample;
import top.feiyangdigital.mapper.BotRecordMapper;

import java.util.List;

@Service
@Transactional
public class BotRecordService {

    @Autowired
    private BotRecordMapper botRecordMapper;

    @CacheEvict(value = "botrecord", key = "#groupId + #userId")
    public boolean addUserRecord(String groupId,String userId,String joinTimestamp){
        BotRecordExample example = new BotRecordExample();
        BotRecordExample.Criteria criteria = example.createCriteria();
        criteria.andGroupidEqualTo(groupId);
        criteria.andUseridEqualTo(userId);
        if (botRecordMapper.countByExample(example)>0){
            return false;
        }
        BotRecord botRecord = new BotRecord();
        botRecord.setGroupid(groupId);
        botRecord.setUserid(userId);
        botRecord.setJointimestamp(joinTimestamp);
        return botRecordMapper.insertSelective(botRecord) > 0;
    }

    @CacheEvict(value = "botrecord", key = "#groupId + #userId")
    public boolean updateRecordByGidAndUid(String groupId,String userId,BotRecord botRecord){
        BotRecordExample example = new BotRecordExample();
        BotRecordExample.Criteria criteria = example.createCriteria();
        criteria.andGroupidEqualTo(groupId);
        criteria.andUseridEqualTo(userId);
        return botRecordMapper.updateByExampleSelective(botRecord, example) > 0;
    }

    @Cacheable(value = "botrecord", key = "#groupId + #userId")
    @Transactional(propagation = Propagation.NEVER)
    public BotRecord selBotRecordByGidAndUid(String groupId,String userId){
        BotRecordExample example = new BotRecordExample();
        BotRecordExample.Criteria criteria = example.createCriteria();
        criteria.andGroupidEqualTo(groupId);
        criteria.andUseridEqualTo(userId);
        List<BotRecord> list = botRecordMapper.selectByExample(example);
        if (!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }
}
