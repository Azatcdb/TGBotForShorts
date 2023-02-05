package azat.cdb.TGBotForShorts.service;

import azat.cdb.TGBotForShorts.config.ConfigBot;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
@Service
public class TGBotService extends TelegramLongPollingBot {

    private final ConfigBot configBot;
    private final FindNamesByJPGSImp findNamesByJPGSImp;

    public TGBotService(ConfigBot configBot, FindNamesByJPGSImp findNamesByJPGSImp) {
        this.configBot = configBot;
        this.findNamesByJPGSImp = findNamesByJPGSImp;
    }

    @Override
    public String getBotUsername() {
        return configBot.getBotName();
    }

    @Override
    public String getBotToken() {
        return configBot.getToken();
    }

    @Override
    public void onUpdateReceived(Update update) {

        if(update.hasMessage() && update.getMessage().hasText()) {
            String message = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();
            Optional<String> names = checkMessage(message);
            switch (message){
                case "/start":
                    start(chatId);
                    break;
                default:
                    sendMessage(chatId, names.orElse("Sorry, nothing found =("));
            }
        }
    }

    private void start(long chatId) {
        String answer = "Welcome! Please enter link to your Short Youtube video";
        log.info("Replied to user: " + answer);
        sendMessage(chatId, answer);
    }

    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);

        try {
            execute(message);
        } catch (TelegramApiException e) {
            log.error("Error occurred: "+e.getMessage());
        }
    }

    private Optional<String> checkMessage (String message) {
        Pattern pattern = Pattern.compile("(https?:\\/\\/)?([\\w-]){0,}(\\.)?youtu([^&\\s]+)?\\.[^&\\s]+\\/[^&\\s]{11}");
        Matcher matcher = pattern.matcher(message);
        Optional<String> names = null;
        if (!matcher.find()) {
            names = Optional.of("""
                    Incorrect link!
                    Link's format:
                    https://www.youtube.com/shorts/{videoId}
                    https://youtu.be/{videoId}\s
                    Please try again""");
        } else {
            try {
                message = findNamesByJPGSImp.getJPG(message);
                names = Optional.ofNullable(findNamesByJPGSImp.getNamesByImg(message));
            } catch (IOException e) {
                log.error(e.getMessage());
            }
        }
        return names;
    }
}
