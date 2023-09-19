package top.feiyangdigital.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import top.feiyangdigital.entity.BotRecord;
import top.feiyangdigital.entity.BotRecordExample;

@Repository
public interface BotRecordMapper {
    long countByExample(BotRecordExample example);

    int deleteByExample(BotRecordExample example);

    int deleteByPrimaryKey(Integer rid);

    int insert(BotRecord record);

    int insertSelective(BotRecord record);

    List<BotRecord> selectByExampleWithBLOBs(BotRecordExample example);

    List<BotRecord> selectByExample(BotRecordExample example);

    BotRecord selectByPrimaryKey(Integer rid);

    int updateByExampleSelective(@Param("record") BotRecord record, @Param("example") BotRecordExample example);

    int updateByExampleWithBLOBs(@Param("record") BotRecord record, @Param("example") BotRecordExample example);

    int updateByExample(@Param("record") BotRecord record, @Param("example") BotRecordExample example);

    int updateByPrimaryKeySelective(BotRecord record);

    int updateByPrimaryKeyWithBLOBs(BotRecord record);

    int updateByPrimaryKey(BotRecord record);
}