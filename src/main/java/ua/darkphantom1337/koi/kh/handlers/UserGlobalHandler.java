package ua.darkphantom1337.koi.kh.handlers;

import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.methods.GetFile;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.*;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.database.DataBase;
import ua.darkphantom1337.koi.kh.buttons.InlineButtons;
import ua.darkphantom1337.koi.kh.entitys.mails.*;
import ua.darkphantom1337.koi.kh.entitys.User;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.*;

public class UserGlobalHandler {

    private Bot bot = Bot.bot;

    public Boolean handleUserMessage(User user, Message msg) {
        Long chatID = msg.getChatId();
        Integer messageID = msg.getMessageId();
        bot.u.logAsyncInfo(user, msg);
        if (msg.hasText())
            if (this.handleUserTextMessage(user, chatID, messageID, msg.getText(), msg)) return true;
        if (msg.hasDocument())
            if (this.handleUserDocumentMessage(user, chatID, messageID, msg, msg.getDocument())) return true;
        if (msg.hasPhoto())
            if (this.handleUserPhotoMessage(user, chatID, messageID, msg, msg.getPhoto())) return true;
        if (msg.getVideo() != null)
            if (this.handleUserVideoMessage(user, chatID, messageID, msg, msg.getVideo())) return true;
        if (msg.getContact() != null)
            if (this.handleUserContactMessage(user, chatID, messageID, msg.getContact())) return true;
        user.sendMessage("Вы хотите нам что-то сообщить или узнать? Напишите нашему менеджеру ", "mened");
        return false;
    }

    public Boolean handleUserCallback(CallbackQuery callback) {
        Message msg = callback.getMessage();
        final Long id = msg.getChatId();
        Long chatID = msg.getChatId(), fromID = callback.getFrom().getId().longValue();
        Integer msgid = msg.getMessageId(), fromid = callback.getFrom().getId();
        String data = callback.getData(), text = msg.getText(), cbqid = callback.getId();
        User user = new User(fromID);
        bot.u.logAsyncCallback(user, msg, data);
        try {
            if (DataBase.isPersonal(user.getUID())) {
                String vacantion = DataBase.getPerFields(user.getUID(), "v_id");
                if (vacantion.equals("admin") || vacantion.equals("owner")) {
                    if (bot.ah.handleCallBack(user, id, data, msgid, cbqid))
                        return true;
                }
                if (vacantion.equals("manager") || vacantion.equals("admin") || vacantion.equals("owner")) {
                    if (bot.mh.handleCallback(user, data, msgid, cbqid)) return true;
                }
                if (vacantion.equals("manager") || vacantion.equals("admin") || vacantion.equals("owner") || vacantion.equals("courier")) {
                    if (CourierMessageHandler.handleCallback(user, data, msgid, cbqid)) return true;
                }
                if (data.startsWith("#Master")) {
                    if (bot.mch.handleStartWorkButtonClick(user, data, msgid, cbqid))
                        return true;
                    if (bot.mch.handleRemontButtonClick(user, data, msgid, cbqid))
                        return true;
                }
                if (bot.pch.handleCallback(user, data, msgid, cbqid, text)) return true;
            }
            if (bot.uch.handleUserType(user, data, msgid, cbqid)) return true;
            if (bot.uch.handleRate(user, data, msgid, cbqid)) return true;
            if (bot.uch.handleSaveZayav(user, data, msgid, cbqid)) return true;
            if (bot.uch.handleReklamaciya(user, data, msgid, cbqid)) return true;
            if (bot.uch.handleCancelZayavka(user, data, msgid, cbqid)) return true;
            if (bot.uch.handleUpdateStatus(user, data, msgid, cbqid)) return true;
            if (data.contains("#PrintModel")) {
                String przv = data.split("/")[1];
                bot.prnt_model.put(user.getUID(), przv);
                bot.editMsg(user.getTID(), msg.getMessageId(), "Вы выбрали производителя " + przv + "!");
                bot.sendMsgToUser(user.getTID(), "Выберите по какому параметру производить поиск:", "print_search_type");
                return true;
            }
            if (data.contains("#SearchType")) {
                String przv = data.split("/")[1];
                bot.user_wait_semodel.put(user.getUID(), true);
                bot.user_stype.put(user.getUID(), przv);
                bot.editMsg((long) callback.getFrom().getId(), msg.getMessageId(), "Вы выбрали поиск по модели " + (przv.equals("Print") ? "принтера" : "картриджа") + "!");
                if (przv.equals("Print")) {
                    user.sendMessage("Укажите цифры модели принтера. \nНапример: MF3010 либо просто 3010", "back_to_main");
                } else {
                    user.sendMessage("Укажите цифры модели картриджа. \nНапример: CА4554 либо просто 4554", "back_to_main");
                }
                return true;
            }
            if (data.contains("#ZaprPrinter")) {
                bot.editMsg((long) callback.getFrom().getId(), msg.getMessageId(), "Вы начали оставлять заявку на заправку " + data.split("/")[1] + " " + data.split("/")[2]);
                bot.user_is_ost_price.put((long) callback.getFrom().getId(), true);
                bot.user_is_ost_model.put((long) callback.getFrom().getId(), data.split("/")[1] + " " + data.split("/")[2]);
                bot.umh.handleUserNewOrder(user, true);
                return true;
            }
            if (data.contains("#SOGLASOVANO")) {
                bot.handVosst(data.split("=")[1], "Telegram", "SOGLASOVANO");
                bot.editMsg(msg.getChatId(), msg.getMessageId(), InlineButtons.getTTTButton());
                return true;
            }
            if (data.contains("#ByNewKart")) {
                bot.handVosst(data.split("=")[1], "Telegram", "ByNewCart");
                bot.editMsg(msg.getChatId(), msg.getMessageId(), InlineButtons.getTTTTButton());
                return true;
            }
            if (data.contains("#MENED")) {
                bot.handVosst(data.split("=")[1], "Telegram", "MENED");
                bot.editMsg(msg.getChatId(), msg.getMessageId(), InlineButtons.getTTTTButton());
                return true;
            }
            if (data.contains("#OBZVON-Z=")) {
                int nz = bot.pi(data.split("=")[1]);
                bot.editMsg(msg.getChatId(), msg.getMessageId(), InlineButtons.getCellTrueButton(callback.getFrom().getId()));
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Обзвон завершён/Заявка будет перемещена в архив"));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.sendToArch(nz);
                        bot.deleteMsg(msg.getChatId(), msg.getMessageId());
                        this.cancel();
                    }
                }, 60000 * 5);
                return true;
            }
            if (data.equals("#OST_Z_ZAPR")) {
                bot.umh.handleUserNewOrder(user, true);
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Заполните заявку..."));
                return true;
            }
            if (data.equals("#VSE_V_PORYDKE")) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.sendMsg(bot.getCellChannelID(), "#date" + bot.u.getDate("dd_MM_yyyy") + " #Напоминание_по_частоте"
                                + "\nБыло выслано клиенту #date" + DataBase.getUsFileds((long) fromid, "days_send_date")
                                + "\nКлиент: #" + DataBase.getUsFileds((long) fromid, "company_name").replaceAll(" ", "_")
                                + "\nКонтактное лицо: #" + DataBase.getUsFileds((long) fromid, "name")
                                + "\nТелефон: #tel" + DataBase.getUserPhone(fromid)
                                + "\nАдрес: #" + DataBase.getUsFileds((long) fromid, "adress").replaceAll(" ", "_")
                                + "\nМодель: #" + DataBase.getUsFileds((long) fromid, "last_model").replaceAll(" ", "_")
                                + "\n---------------------------\n" + (long) fromid, InlineButtons.getNPBt());
                        this.cancel();
                    }
                }, 60000 * 1/*((60000*60)*24)*2*/);
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Спасибо!"));
            }
            if (data.equals("#NAPOM_TWO_DAY")) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        try {
                            bot.execute(new SendMessage().setText("Добрый день, хотел узнать всё ли в порядке? Возможно Вам уже нужно заправить картридж?")
                                    .setChatId(String.valueOf(callback.getFrom().getId())).setReplyMarkup(InlineButtons.getNapomDaysTwoButton()));
                        } catch (Exception e111) {
                            System.out.println("Ошибка при отправке напом.");
                        }
                        this.cancel();
                    }
                }, 60000 * 1/*((60000*60)*24)*2*/);
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Спасибо :-)"));
                return true;
            }
            if (data.equals("#ReSearchPrice")) {
                user.sendMessage("Пожалуйста выберите модель Вашего принтера ниже:", "print_models");
                return true;
            }
            if (data.equals("#CancelVvodAdm")) {
                bot.deleteMsg(id, msgid);
                bot.sendMsgToUser(user.getTID(), "Продолжайте :-)");
                if (bot.user_tema.get(user.getUID()).equals("Заправка картриджа"))
                    bot.umh.handleUserNewOrder(user, true);
                else
                    bot.umh.handleUserNewOrder(user, false);
                return true;
            }
            if (data.equals("#PerenosNaZavtra")) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg(id, msgid);
                        this.cancel();
                    }
                }, (60000 * 1));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.sendMsg(bot.getCellChannelID(), text, InlineButtons.getNPBt());
                        this.cancel();
                    }
                }, (60000 * 2));
                bot.editMsg(id, msgid, InlineButtons.getTr("Выполнено: `Отмена`"));
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Ваш ответ принят!"));
                return true;
            }
            if (data.equals("#PerenosNa3Days")) {
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg(id, msgid);
                        this.cancel();
                    }
                }, (60000 * 1));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.sendMsg(bot.getCellChannelID(), text, InlineButtons.getNPBt());
                        this.cancel();
                    }
                }, (60000 * 3));
                bot.editMsg(id, msgid, InlineButtons.getTr("Выполнено: `Отмена`"));
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Ваш ответ принят!"));
                return true;
            }
            if (data.equals("#PerenosNaPn")) {
                LocalDate nextMonday = LocalDate.now().with(TemporalAdjusters.next(DayOfWeek.MONDAY));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg((long) id, msgid);
                        this.cancel();
                    }
                }, (60000 * 1));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.sendMsg(bot.getCellChannelID(), text, InlineButtons.getNPBt());
                        this.cancel();
                    }
                }, bot.convertToDateViaSqlDate(nextMonday));
                bot.editMsg(id, msgid, InlineButtons.getTr("Выполнено: `В понедельник`"));
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Ваш ответ принят!"));
                return true;
            }
            if (data.equals("#SaveZayav")) {
                int frid = bot.pi(msg.getText().split("---------------------------")[1]);
                bot.saveZayav(user, null, "-_-");
                return true;
            }
            if (data.equals("#CancelNP")) {
                String text1 = msg.getText();
                bot.editMsg(id, msgid, InlineButtons.getTr("Выполнено: `Отмена`"));
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Ваш ответ принят!"));
                new Timer().schedule(new TimerTask() {
                    @Override
                    public void run() {
                        bot.deleteMsg(id, msgid);
                        this.cancel();
                    }
                }, (60000 * 5));
                return true;
            }
            if (data.equals("#ПодатьЗаявку")) {
                bot.editMsg(msg.getChatId(), msg.getMessageId(), "Вы подали заявку!");
                bot.editMsg(msg.getChatId(), msg.getMessageId(), InlineButtons.getButText("Вы подали заявку!"));
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Заявка принята!"));
                bot.saveZayav(user, null, null);
                return true;
            }
            if (data.equals("#ИзменитьАдрес")) {
                bot.user_wait_model.remove(user.getUID());
                bot.user_wait_adress.put(user.getUID(), true);
                bot.user_edit_adress.put(user.getUID(), true);
                bot.editMsg(msg.getChatId(), msg.getMessageId(), "Вы изменили адрес!");
                bot.editMsg(msg.getChatId(), msg.getMessageId(), InlineButtons.getButText("Вы изменили адрес"));
                user.sendMessage(user.getUserName() + ", укажите пожалуйста адрес в поле ввода и отправьте мне, либо выберите подходящий вариант ниже.Адрес нужен для выезда курьера за заявкой. ", "adress");
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Укажите новый адрес :-)"));
                return true;
            }
            if (data.equals("#ИзменитьМодель")) {
                bot.user_wait_adress.remove(user.getUID());
                bot.user_wait_model.put(user.getUID(), true);
                bot.user_edit_model.put(user.getUID(), true);
                new User(id).setUserAction("user_wait_edit_model");
                bot.editMsg(msg.getChatId(), msg.getMessageId(), "Вы изменили модель!");
                bot.editMsg(msg.getChatId(), msg.getMessageId(), InlineButtons.getButText("Вы изменили модель"));
                bot.sendZOstZ(user, null);
                bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Введите новую модель!"));
                return true;
            }
            bot.execute(new AnswerCallbackQuery().setCallbackQueryId(callback.getId()).setText("Ваш ответ уже принят!"));
        } catch (Exception x) {
            System.out.println(bot.prefix() + "Ошибка при обработке callback"
                    + "\n-> Exception: " + x.getLocalizedMessage()
                    + "\n-> Error line: " + x.getStackTrace()[0].getLineNumber());
            x.printStackTrace();
        }
        return false;
    }

    private Boolean handleUserTextMessage(User user, Long chatID, Integer messageID, String text, Message msg) {
        if (bot.umh.handleStartMessage(user, text)) return true;
        if (bot.umh.checkIsRegister(user, text)) return true;
        if (DataBase.isPersonal(user.getUID())) {
            String vacantion = DataBase.getPerFields(user.getUID(), "v_id");
            if (vacantion.equals("admin") || vacantion.equals("owner"))
                if (bot.ah.handleTextMessage(user, text, messageID)) return true;
            if (vacantion.equals("manager") || vacantion.equals("admin") || vacantion.equals("owner"))
                if (bot.mh.handleTextMessage(user, text, messageID)) return true;
            if (vacantion.equals("courier")) ;
            //if (ch.handleTextMessage(user, txt, message_id)) return;
        }
        if (bot.umh.handleUserMenusText(user, text, messageID)) return true;
        if (bot.umh.handleUserQRNumbersText(user, text, messageID)) return true;
        if (bot.umh.handleUserReklamComm(user, text)) return true;
        if (bot.umh.handleUserName(user, text, msg)) return true;
        if (bot.umh.handleUserCompanyName(user, text, msg)) return true;
        if (bot.umh.handleUserRateComm(user, text, msg)) return true;
        if (bot.handZayav(user, msg, text)) return true;
        if (bot.umh.handleUserAdress(user, text, msg)) return true;
        if (bot.umh.handleUserSeeModel(user, text)) return true;
        if (bot.umh.handleReferralProgram(user, text)) return true;
        if (bot.umh.handlePersonalMsgs(user, text, msg)) return true;
        return false;
    }

    private Boolean handleUserDocumentMessage(User user, Long chatID, Integer messageID, Message msg, Document document) {
        if (user.getUserAction().equals("admin_wait_new_mail")) {
            user.setUserAction("main");
            Mail mail = new Mail(DataBase.getNextMailID());
            System.out.println(document.getMimeType());
            bot.ah.admins_current_mail.put(user.getUID(), mail.getMailID());
            mail.setMailStatus(MailStatus.CREATING);
            Boolean hasCaption = (msg.getCaption() != null && !msg.getCaption().equals(""));
            if (hasCaption)
                mail.setMailMessage(msg.getCaption());
            String mimeType = document.getMimeType();
            if (mimeType.contains("image/gif")) {
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_GIF);
                else
                    mail.setFileType(FileType.GIF);
            } else if (mimeType.contains("image")) {
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_IMAGE);
                else
                    mail.setFileType(FileType.IMAGE);
            } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document")) {
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.WORD);

            }else if (mimeType.equals("application/msword")){
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.OLD_WORD);
            }
            else if (mimeType.contains("video")) {
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_VIDEO);
                else
                    mail.setContentType(ContentType.VIDEO);
                mail.setFileType(FileType.VIDEO);
            } else if (mimeType.equals("application/pdf")){
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.PDF);
            } else if(mimeType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.EXCEL);
            } else if (mimeType.equals("application/vnd.ms-excel")){
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.EXCEL);
            } else if (mimeType.equals("application/vnd.ms-powerpoint")){
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.OLD_POWERPOINT);
            } else if (mimeType.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation")){
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.POWERPOINT);
            } else if (mimeType.equals("text/plain")){
                if (hasCaption)
                    mail.setContentType(ContentType.TEXT_AND_FILE);
                else
                    mail.setContentType(ContentType.FILE);
                mail.setFileType(FileType.TEXT);
            }
            mail.setMailFileID(document.getFileId());
            mail.setCreatingDate(bot.u.getDate("dd/MM/yyyy"));
            mail.setCreatingTime(bot.u.getDate("HH:mm:ss"));
            user.sendMessage("\uD83D\uDC49 Рассылка №" + mail.getMailID() + " создана! Укажите когда вы хотите отправить данное сообщение:", "ADMIN/Mail/When/" + mail.getMailID());
            return true;
        }
        if (user.getUserAction().equals("admin_wait_birthday_image")) {
            user.setUserAction("main");
            Mail mail = new Mail(0);
            mail.setMailFileID(document.getFileId());
            mail.setFileType(FileType.IMAGE);
            mail.setContentType(ContentType.TEXT_AND_IMAGE);
            mail.setMailStatus(MailStatus.WAITING_TO_START);
            mail.setSendType(SendType.PLANNED);
            mail.setCreatingDate(bot.u.getDate("dd/MM/yyyy"));
            mail.setCreatingTime(bot.u.getDate("HH:mm:ss"));
            user.sendMessage("\uD83D\uDC49 Поздравительная картинка успешно сохранена.", "ADMIN/Main");
            return true;
        }
        if (document.getMimeType().contains("image")) {
            try {
                bot.saveDocument("lastqr" + user.getUID() + ".png", bot.execute(new GetFile().setFileId(document.getFileId())).getFileUrl(bot.getBotToken()));
                return true;
            } catch (TelegramApiException telegramApiException) {
                telegramApiException.printStackTrace();
            }
            if (bot.uphh.handleUserPhoto(user, new java.io.File("lastqr" + user.getUID() + ".png"))) return true;
        }
        return false;
    }

    private Boolean handleUserPhotoMessage(User user, Long chatID, Integer messageID, Message msg, List<PhotoSize> photo) {
        if (user.getUserAction().equals("admin_wait_new_mail")) {
            user.setUserAction("main");
            Mail mail = new Mail(DataBase.getNextMailID());
            bot.ah.admins_current_mail.put(user.getUID(), mail.getMailID());
            mail.setMailStatus(MailStatus.CREATING);
            if (msg.getCaption() != null && !msg.getCaption().equals("")) {
                mail.setContentType(ContentType.TEXT_AND_IMAGE);
                mail.setMailMessage(msg.getCaption());
            } else mail.setContentType(ContentType.IMAGE);
            mail.setFileType(FileType.IMAGE);
            mail.setMailFileID(photo.get(1).getFileId());
            mail.setCreatingDate(bot.u.getDate("dd/MM/yyyy"));
            mail.setCreatingTime(bot.u.getDate("HH:mm:ss"));
            user.sendMessage("\uD83D\uDC49 Рассылка №" + mail.getMailID() + " создана! Укажите когда вы хотите отправить данное сообщение:", "ADMIN/Mail/When/" + mail.getMailID());
            return true;
        }
        if (user.getUserAction().equals("admin_wait_birthday_image")) {
            user.setUserAction("main");
            Mail mail = new Mail(0);
            mail.setMailFileID(photo.get(1).getFileId());
            mail.setFileType(FileType.IMAGE);
            mail.setContentType(ContentType.TEXT_AND_IMAGE);
            mail.setMailStatus(MailStatus.WAITING_TO_START);
            mail.setSendType(SendType.PLANNED);
            mail.setCreatingDate(bot.u.getDate("dd/MM/yyyy"));
            mail.setCreatingTime(bot.u.getDate("HH:mm:ss"));
            user.sendMessage("\uD83D\uDC49 Поздравительная картинка успешно сохранена.", "ADMIN/Main");
            return true;
        }
        try {
            bot.saveDocument("lastqr" + user.getUID() + ".png", bot.execute(new GetFile().setFileId(photo.get(1).getFileId())).getFileUrl(bot.getBotToken()));
            return true;
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }
        if (bot.uphh.handleUserPhoto(user, new java.io.File("lastqr" + user.getUID() + ".png"))) return true;
        return false;
    }


    private Boolean handleUserVideoMessage(User user, Long chatID, Integer messageID, Message msg, Video video) {
        if (user.getUserAction().equals("admin_wait_new_mail")) {
            user.setUserAction("main");
            Mail mail = new Mail(DataBase.getNextMailID());
            bot.ah.admins_current_mail.put(user.getUID(), mail.getMailID());
            mail.setMailStatus(MailStatus.CREATING);
            if (msg.getCaption() != null && !msg.getCaption().equals("")) {
                mail.setContentType(ContentType.TEXT_AND_VIDEO);
                mail.setMailMessage(msg.getCaption());
            } else mail.setContentType(ContentType.VIDEO);
            mail.setFileType(FileType.VIDEO);
            mail.setMailFileID(video.getFileId());
            mail.setCreatingDate(bot.u.getDate("dd/MM/yyyy"));
            mail.setCreatingTime(bot.u.getDate("HH:mm:ss"));
            user.sendMessage("\uD83D\uDC49 Рассылка №" + mail.getMailID() + " создана! Укажите когда вы хотите отправить данное сообщение:", "ADMIN/Mail/When/" + mail.getMailID());
            return true;
        }
        if (user.getUserAction().equals("admin_wait_birthday_image")) {
            user.setUserAction("main");
            Mail mail = new Mail(0);
            mail.setMailFileID(video.getFileId());
            mail.setFileType(FileType.VIDEO);
            mail.setContentType(ContentType.TEXT_AND_VIDEO);
            mail.setMailStatus(MailStatus.WAITING_TO_START);
            mail.setSendType(SendType.PLANNED);
            mail.setCreatingDate(bot.u.getDate("dd/MM/yyyy"));
            mail.setCreatingTime(bot.u.getDate("HH:mm:ss"));
            user.sendMessage("\uD83D\uDC49 Поздравительная картинка успешно сохранена.", "ADMIN/Main");
            return true;
        }
       /* try {
            bot.saveDocument("lastqr" + user.getUID() + ".mp4", bot.execute(new GetFile().setFileId(video.getFileId())).getFileUrl(bot.getBotToken()));
            return true;
        } catch (TelegramApiException telegramApiException) {
            telegramApiException.printStackTrace();
        }*//*
        if (bot.uphh.handleUserPhoto(user, new java.io.File("lastqr" + user.getUID() + ".png"))) return true;*/
        return false;
    }

    private Boolean handleUserContactMessage(User user, Long chatID, Integer messageID, Contact contact) {
        if (bot.ucth.handleContact(user, contact)) return true;
        user.sendMessage("К сожалению я не знаю что делать с этим контактом \uD83D\uDE22");
        return true;
    }

}
