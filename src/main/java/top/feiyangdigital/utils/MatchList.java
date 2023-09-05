package top.feiyangdigital.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.Update;
import top.feiyangdigital.entity.KeywordsFormat;
import top.feiyangdigital.sqlService.GroupInfoService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class MatchList {

    @Autowired
    private GroupInfoService groupInfoService;

    public List<KeywordsFormat> createBanKeyDeleteOptionList(Update update) {
        String content = groupInfoService.selAllByGroupId(update.getMessage().getChatId().toString()).getKeywords();
        if (content!=null && !content.isEmpty()){
            return Arrays.stream(content.split("\\n{2,}"))
                    .map(String::trim)
                    .map(KeywordsFormat::new)
                    .collect(Collectors.toList());
        }
        return null;
    }
}
