package org.cexpositoce.chatbotai.TelegramBot;

import lombok.extern.slf4j.Slf4j;
import org.cexpositoce.chatbotai.chatbot.ChatbotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.ParseMode;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class TelegramBotComponent extends TelegramLongPollingBot {

    @Value("${telegram.bot.token}")
    private String botToken;

    @Value("${telegram.bot.username}")
    private String botUsername;

    @Autowired
    private ChatbotService chatbotService;

    private static final int MAX_LENGTH = 2000;

    public TelegramBotComponent() {
        System.out.println("Registrando bot...");
    }

    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            System.out.println("He recibido un mensaje: " + update.getMessage().getText());
            if (!update.getMessage().getText().startsWith("/") && update.getMessage().getText().contains("?")) {
                String userMessage = "Contesta de una manera breve (menos de 1000 caracteres), en formato markdown y en español a la siguiente pregunta indicando la respuesta con 'Respuesta:'. También me gustaria que respondieras lo más humanamente posible, que no dé la sensación de hablar con una maquina. Esta es la pregunta:" + update.getMessage().getText();
                String chatId = update.getMessage().getChatId().toString();

                if (update.getMessage().getText().equals("Que hay de comer?")) {
                    sendResponse(chatId, "Comida");
                    return;
                }

                sendResponse(chatId, "Generando respuesta");
                String respuesta = chatbotService.generarRespuesta(userMessage);
                respuesta = respuesta.replace(userMessage, "");
                List<String> partes = dividirMensaje(formatApiResponse(respuesta));
                partes.forEach(part -> {
                    System.out.println("Respuesta generada: " + part);
                    sendResponse(chatId, escapeMarkdownV2(part));
                });
            }else if(update.getMessage().getText().equals("/start")) {
                String chatId = update.getMessage().getChatId().toString();
                sendResponse(chatId, escapeMarkdownV2("Hola muy buenas! Soy FrodyBot, un chatbot que te ayudará a responder preguntas. Para obtener una respuesta, envíame una pregunta que termine con '?'"));
            }
        }
    }

    private void sendResponse(String chatId, String text) {
        SendMessage message = new SendMessage();
        message.enableMarkdownV2(true);
        message.setChatId(chatId);
        message.setText(text);
        message.setParseMode(ParseMode.MARKDOWNV2);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public static String formatApiResponse(String jsonResponse) {
        JSONArray jsonArray = new JSONArray(jsonResponse);

        JSONObject firstObject = jsonArray.getJSONObject(0);
        String generatedText = firstObject.getString("generated_text");
        if(generatedText.contains("Respuesta:")) {
            generatedText = generatedText.split("Respuesta:")[1];
        }
        return generatedText.replace("\\n", "\n").trim();
    }

    public static List<String> dividirMensaje(String mensaje) {
        List<String> partes = new ArrayList<>();

        int inicio = 0;
        while (inicio < mensaje.length()) {
            int fin = Math.min(inicio + MAX_LENGTH, mensaje.length()); // No sobrepasar el límite
            partes.add(mensaje.substring(inicio, fin));
            inicio = fin;
        }

        return partes;
    }

    public static String escapeMarkdownV2(String text) {
        return text.replace("\\", "\\\\")  // Escape para la barra invertida
                .replace("_", "\\_")   // Escape para cursiva
                .replace("*", "\\*")   // Escape para negrita
                .replace("[", "\\[")   // Escape para links y menciones
                .replace("]", "\\]")
                .replace("(", "\\(")   // Escape para URLs y emojis
                .replace(")", "\\)")
                .replace("~", "\\~")   // Escape para tachado
                .replace("`", "\\`")   // Escape para código
                .replace(">", "\\>")   // Escape para blockquote
                .replace("#", "\\#")   // Escape para títulos
                .replace("+", "\\+")   // Escape para listas ordenadas
                .replace("-", "\\-")   // Escape para listas desordenadas
                .replace("=", "\\=")   // Escape para encabezados
                .replace("|", "\\|")   // Escape para tablas
                .replace("{", "\\{")   // Escape para bloques de código
                .replace("}", "\\}")
                .replace(".", "\\.")   // Escape para puntos (evita interpretación especial)
                .replace("!", "\\!");  // Escape para exclamaciones
    }

}
