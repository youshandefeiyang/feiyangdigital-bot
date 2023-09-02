package top.feiyangdigital.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.feiyangdigital.entity.GroupInfo;
import top.feiyangdigital.entity.GroupInfoExample;
import top.feiyangdigital.entity.GroupInfoWithBLOBs;

@Repository
public interface GroupInfoMapper {
    long countByExample(GroupInfoExample example);

    int deleteByExample(GroupInfoExample example);

    int deleteByPrimaryKey(Integer id);

    int insert(GroupInfoWithBLOBs record);

    int insertSelective(GroupInfoWithBLOBs record);

    List<GroupInfoWithBLOBs> selectByExampleWithBLOBs(GroupInfoExample example);

    List<GroupInfo> selectByExample(GroupInfoExample example);

    GroupInfoWithBLOBs selectByPrimaryKey(Integer id);

    int updateByExampleSelective(@Param("record") GroupInfoWithBLOBs record, @Param("example") GroupInfoExample example);

    int updateByExampleWithBLOBs(@Param("record") GroupInfoWithBLOBs record, @Param("example") GroupInfoExample example);

    int updateByExample(@Param("record") GroupInfo record, @Param("example") GroupInfoExample example);

    int updateByPrimaryKeySelective(GroupInfoWithBLOBs record);

    int updateByPrimaryKeyWithBLOBs(GroupInfoWithBLOBs record);

    int updateByPrimaryKey(GroupInfo record);
}