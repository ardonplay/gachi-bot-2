package io.github.ardonplay.gachibot2.config;

import io.github.ardonplay.gachibot2.services.BotService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;
@Slf4j
@Component
@AllArgsConstructor
public class BotInjector {


  private final BotService bot;


  @EventListener({ContextRefreshedEvent.class})
  public void init() throws TelegramApiException{
    TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);

    try{
      telegramBotsApi.registerBot(bot);
    }
    catch (TelegramApiException e){
      log.info(e.getMessage());
    }
  }
}
