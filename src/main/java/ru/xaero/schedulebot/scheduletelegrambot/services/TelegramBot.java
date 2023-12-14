package ru.xaero.schedulebot.scheduletelegrambot.services;

import com.vdurmont.emoji.EmojiParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.commands.SetMyCommands;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.commands.BotCommand;
import org.telegram.telegrambots.meta.api.objects.commands.scope.BotCommandScopeDefault;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ru.xaero.schedulebot.scheduletelegrambot.config.Config;
import ru.xaero.schedulebot.scheduletelegrambot.models.Schedule;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class TelegramBot extends TelegramLongPollingBot {
    @Autowired
    private ScheduleService scheduleService;

    private final String COMMAND_START = "/start";
    private final String COMMAND_HELP = "/help";
    private final String COMMAND_TODAY = "/today";
    private final String COMMAND_TOMORROW = "/tomorrow";
    private final String COMMAND_WEEK = "/week";
    private final String COMMAND_MONTH = "/month";
    @Autowired
    private Config config;


    @Override
    public String getBotUsername() {
        return config.getBotName();
    }

    @Override
    public String getBotToken() {
        return config.getBotKey();
    }

    @Override
    public void onUpdateReceived(Update update) {
        if(update.hasMessage() && update.getMessage().hasText()){
            String messageText = update.getMessage().getText();
            long chatId = update.getMessage().getChatId();

            switch (messageText){
                case COMMAND_START:
                    startCommand(chatId);
                    break;
                case COMMAND_HELP:
                    helpCommand(chatId);
                    break;
                case COMMAND_TODAY:
                    todayCommand(chatId);
                    break;
                case COMMAND_TOMORROW:
                    tomorrowCommand(chatId);
                    break;
                case COMMAND_WEEK:
                    weekCommand(chatId);

                    break;
                case COMMAND_MONTH:
                    monthCommand(chatId);
                    break;
                default:
                    sendMessage(chatId, "Sorry, command is not found!");
                    break;
            }
        }
    }
    private void sendMessage(long chatId, String textToSend) {
        SendMessage message = new SendMessage();
        message.setChatId(String.valueOf(chatId));
        message.setText(textToSend);
        executeMessage(message);
    }
    private void executeMessage(SendMessage message){
        try {
            execute(message);
        } catch (TelegramApiException e) {
            System.out.println(e.getMessage());
        }
    }


    private void startCommand(long chatId){
        setMyCommands();
        String message = EmojiParser.parseToUnicode(":wave:Добро пожаловать! Вы присоединились к нашему университетскому боту по расписанию. Здесь вы найдете актуальное расписание занятий, чтобы эффективно планировать свой учебный день.\n" +
                "\n" +
                ":blue_book:Пожалуйста, помните, что знание — это сила, и наше расписание поможет вам максимально эффективно использовать каждый учебный момент.\n" +
                "\n" +
                ":hourglass: Следите за обновлениями, и у вас всегда будет полная уверенность в своем расписании. Приятного обучения!:mortar_board::star2:");
        sendMessage(chatId, message);
    }
    private void helpCommand(long chatId) {
        String message = EmojiParser.parseToUnicode(":information_source: **Помощь по командам:**\n\n" +
                "1. `/start` - Начать использование бота и получить приветственное сообщение.\n" +
                "2. `/help` - Вывести это сообщение с описанием доступных команд.\n" +
                "3. `/today` - Получить расписание на сегодня.\n" +
                "4. `/tomorrow` - Получить расписание на завтра.\n" +
                "5. `/week` - Получить расписание на текущую неделю.\n" +
                "6. `/month` - Получить расписание на текущий месяц.\n\n" +
                ":pencil: *Пример использования:*\n `/today` - чтобы узнать расписание на сегодня.\n\n" +
                "Если у вас возникли дополнительные вопросы, обратитесь к администратору.");
        sendMessage(chatId, message);
    }
    private void todayCommand(long chatId) {
        // Получите расписание на сегодня из вашего сервиса
        List<Schedule> todaySchedule = scheduleService.getTodaySchedules();

        if (todaySchedule.isEmpty()) {
            sendMessage(chatId, "На сегодня занятий нет.");
        } else {
            StringBuilder message = new StringBuilder(":calendar: Расписание на сегодня:\n");

            for (Schedule schedule : todaySchedule) {
                message.append("\n\n");
                message.append(":clock1: Время: ").append(schedule.getPairsTime().getStartTime())
                        .append(" - ").append(schedule.getPairsTime().getEndTime()).append("\n\n");
                if (schedule.getSubgroup() != null) {
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("(").append(schedule.getSubgroup().getId()).append(")").append("\n\n");
                }
                else{
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("\n\n");
                }

                message.append(":bust_in_silhouette: Преподаватель: ")
                        .append(schedule.getTeacher().getSurname()+" ")
                        .append(schedule.getTeacher().getName()+" ")
                        .append(schedule.getTeacher().getPatronymic()+" ")
                        .append("\n\n")
                        .append(":door: Аудитория: ")
                        .append(schedule.getTeacher().getAuditorium())
                        .append("-")
                        .append(schedule.getTeacher().getBody())
                        .append("\n\n")
                        .append(":scroll: Тип: ").append(schedule.getType().getName())
                        .append("\n\n")
                        .append("============================");


            }

            sendMessage(chatId, EmojiParser.parseToUnicode(message.toString()));
        }
    }

    private void weekCommand(long chatId) {
        List<Schedule> weekSchedule = scheduleService.getWeekSchedules();

        if (weekSchedule.isEmpty()) {
            sendMessage(chatId, "На текущую неделю занятий нет.");
        } else {
            StringBuilder message = new StringBuilder(":calendar: Расписание на текущую неделю:\n");

            LocalDate currentDate = null;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");

            for (Schedule schedule : weekSchedule) {
                if (!schedule.getDate().equals(currentDate)) {
                    // Новый день, добавляем заголовок
                    if (currentDate != null) {
                        message.append("\n\n============================\n\n");
                    }
                    message.append(":calendar: Расписание на ").append(schedule.getDate().format(formatter)).append(":\n");
                    currentDate = schedule.getDate();
                }

                message.append("\n\n");
                message.append(":clock1: Время: ").append(schedule.getPairsTime().getStartTime())
                        .append(" - ").append(schedule.getPairsTime().getEndTime()).append("\n\n");

                if (schedule.getSubgroup() != null) {
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("(").append(schedule.getSubgroup().getId()).append(")").append("\n\n");
                }
                else{
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("\n\n");
                }
                message.append(":bust_in_silhouette: Преподаватель: ")
                        .append(schedule.getTeacher().getSurname() + " ")
                        .append(schedule.getTeacher().getName() + " ")
                        .append(schedule.getTeacher().getPatronymic() + " ")
                        .append("\n\n")
                        .append(":door: Аудитория: ")
                        .append(schedule.getTeacher().getAuditorium())
                        .append("-")
                        .append(schedule.getTeacher().getBody())
                        .append("\n\n")
                        .append(":scroll: Тип: ").append(schedule.getType().getName())
                        .append("\n\n");
            }

            sendMessage(chatId, EmojiParser.parseToUnicode(message.toString()));
        }
    }


    private void monthCommand(long chatId) {
        List<Schedule> monthSchedule = scheduleService.getMonthSchedules();

        if (monthSchedule.isEmpty()) {
            sendMessage(chatId, "На текущий месяц занятий нет.");
        } else {
            StringBuilder message = new StringBuilder(":calendar: Расписание на текущий месяц:\n");

            LocalDate currentWeekStart = null;
            LocalDate currentDay = null;

            for (Schedule schedule : monthSchedule) {
                if (currentWeekStart == null || !currentWeekStart.equals(schedule.getDate().with(DayOfWeek.MONDAY))) {
                    if (currentWeekStart != null) {
                        sendMessage(chatId, EmojiParser.parseToUnicode(message.toString()));
                        message.setLength(0);
                    }
                    currentWeekStart = schedule.getDate().with(DayOfWeek.MONDAY);
                    message.append("\n\n").append("Неделя: ").append(currentWeekStart.get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear())).append("\n");
                    currentDay = null;
                }

                if (currentDay == null || !currentDay.equals(schedule.getDate())) {
                    if (currentDay != null) {
                        message.append("\n\n"); // Разделитель между днями
                    }
                    currentDay = schedule.getDate();
                    message.append("\n\n").append("Дата: ").append(currentDay).append("\n");
                }

                message.append("\n\n");
                message.append(":clock1: Время: ").append(schedule.getPairsTime().getStartTime())
                        .append(" - ").append(schedule.getPairsTime().getEndTime()).append("\n\n");

                if (schedule.getSubgroup() != null) {
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("(").append(schedule.getSubgroup().getId()).append(")").append("\n\n");
                }
                else{
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("\n\n");
                }
                message.append(":bust_in_silhouette: Преподаватель: ")
                        .append(schedule.getTeacher().getSurname()+" ")
                        .append(schedule.getTeacher().getName()+" ")
                        .append(schedule.getTeacher().getPatronymic()+" ")
                        .append("\n\n")
                        .append(":door: Аудитория: ")
                        .append(schedule.getTeacher().getAuditorium())
                        .append("-")
                        .append(schedule.getTeacher().getBody())
                        .append("\n\n")
                        .append(":scroll: Тип: ").append(schedule.getType().getName())
                        .append("\n\n")
                        .append("============================");
            }

            // Отправить последнее сообщение
            sendMessage(chatId, EmojiParser.parseToUnicode(message.toString()));
        }
    }
    private void tomorrowCommand(long chatId) {
        List<Schedule> tomorrowSchedule = scheduleService.getTomorrowSchedules();

        if (tomorrowSchedule.isEmpty()) {
            sendMessage(chatId, "На завтра занятий нет.");
        } else {
            StringBuilder message = new StringBuilder(":calendar: Расписание на завтра:\n");

            for (Schedule schedule : tomorrowSchedule) {
                message.append("\n\n");
                message.append(":clock1: Время: ").append(schedule.getPairsTime().getStartTime())
                        .append(" - ").append(schedule.getPairsTime().getEndTime()).append("\n\n");

                if (schedule.getSubgroup() != null) {
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("(").append(schedule.getSubgroup().getId()).append(")").append("\n\n");
                }
                else{
                    message.append(":book: Предмет: ").append(schedule.getTeacher().getObject().getName()).append("\n\n");
                }
                message.append(":bust_in_silhouette: Преподаватель: ")
                        .append(schedule.getTeacher().getSurname()+" ")
                        .append(schedule.getTeacher().getName()+" ")
                        .append(schedule.getTeacher().getPatronymic()+" ")
                        .append("\n\n")
                        .append(":door: Аудитория: ")
                        .append(schedule.getTeacher().getAuditorium())
                        .append("-")
                        .append(schedule.getTeacher().getBody())
                        .append("\n\n")
                        .append(":scroll: Тип: ").append(schedule.getType().getName())
                        .append("\n\n")
                        .append("============================");
            }

            sendMessage(chatId, EmojiParser.parseToUnicode(message.toString()));
        }
    }

    private void setMyCommands() {
        List<BotCommand> commands = new ArrayList<>();

        // Создаем объекты команд и добавляем их в список
        commands.add(new BotCommand(COMMAND_START, "Начать использование"));
        commands.add(new BotCommand(COMMAND_HELP, "Помощь"));
        commands.add(new BotCommand(COMMAND_TODAY, "Расписание на сегодня"));
        commands.add(new BotCommand(COMMAND_TOMORROW, "Расписание на завтра"));
        commands.add(new BotCommand(COMMAND_WEEK, "Расписание на неделю"));
        commands.add(new BotCommand(COMMAND_MONTH, "Расписание на месяц"));
        try{
            this.execute(new SetMyCommands(commands, new BotCommandScopeDefault(), null));// чтобы донести список до нашего бота
        }
        catch (TelegramApiException e){

        }
    }






}
