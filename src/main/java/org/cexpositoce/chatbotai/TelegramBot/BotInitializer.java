package org.cexpositoce.chatbotai.TelegramBot;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;

@Component
public class BotInitializer {
    private final TelegramBotComponent bot;

    public BotInitializer(TelegramBotComponent bot) {
        this.bot = bot;
        registerBot();
    }

    private void registerBot() {
        try {
            TelegramBotsApi botsApi = new TelegramBotsApi(DefaultBotSession.class);
            botsApi.registerBot(bot);
            System.out.println("Bot registrado con éxito en Telegram ✅");
        } catch (TelegramApiException e) {
            e.printStackTrace();
            System.out.println("❌ Error registrando el bot en Telegram");
        }
    }
}
