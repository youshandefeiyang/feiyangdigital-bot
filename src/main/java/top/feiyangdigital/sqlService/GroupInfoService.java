package top.feiyangdigital.sqlService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import top.feiyangdigital.entity.GroupInfoExample;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;
import top.feiyangdigital.mapper.GroupInfoMapper;

import java.util.List;

@Service
@Transactional
public class GroupInfoService {

    @Autowired
    private GroupInfoMapper groupInfoMapper;

    @CacheEvict(value = "groupinfo", key = "#record.groupid")
    public boolean addGroup(GroupInfoWithBLOBs record) {
        return groupInfoMapper.insertSelective(record) > 0;
    }

    @Transactional(propagation = Propagation.NEVER)
    public boolean selGroup(String chatId) {
        GroupInfoExample example = new GroupInfoExample();
        GroupInfoExample.Criteria criteria = example.createCriteria();
        criteria.andGroupidEqualTo(chatId);
        if (groupInfoMapper.countByExample(example) > 0) {
            return false;
        }
        return true;
    }

    @CacheEvict(value = "groupinfo", key = "#chatId")
    public boolean updateSelectiveByChatId(GroupInfoWithBLOBs groupInfoWithBLOBs, String chatId) {
        GroupInfoExample example = new GroupInfoExample();
        GroupInfoExample.Criteria criteria = example.createCriteria();
        criteria.andGroupidEqualTo(chatId);
        return groupInfoMapper.updateByExampleSelective(groupInfoWithBLOBs, example) > 0;
    }

    @Cacheable(value = "groupinfo", key = "#groupId")
    @Transactional(propagation = Propagation.NEVER)
    public GroupInfoWithBLOBs selAllByGroupId(String groupId){
        GroupInfoExample example = new GroupInfoExample();
        GroupInfoExample.Criteria criteria = example.createCriteria();
        criteria.andGroupidEqualTo(groupId);
        List<GroupInfoWithBLOBs> list = groupInfoMapper.selectByExampleWithBLOBs(example);
        if (!list.isEmpty()){
            return list.get(0);
        }
        return null;
    }

}