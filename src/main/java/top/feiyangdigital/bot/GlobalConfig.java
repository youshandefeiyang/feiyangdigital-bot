package top.feiyangdigital.bot;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Configuration
public class GlobalConfig {

    @Bean
    public List<String> getAllouUpdated(){
        String[] allowUpdated = new String[]{"update_id","message","edited_message","channel_post","edited_channel_post","inline_query","chosen_inline_result","callback_query","shipping_query","pre_checkout_query","poll","poll_answer","my_chat_member","chat_member","chat_join_request"};
        return Arrays.asList(allowUpdated);
    }
}
