package ua.darkphantom1337.koi.kh.buttons;

import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.api.objects.replykeyboard.buttons.InlineKeyboardButton;
import ua.darkphantom1337.koi.kh.Bot;
import ua.darkphantom1337.koi.kh.entitys.MasterWork;

import java.util.ArrayList;
import java.util.List;

public class MasterChannelButtons {

    private Bot bot;

    public MasterChannelButtons(Bot bot) {
        this.bot = bot;
    }

    public InlineKeyboardMarkup getMainZButtons(MasterWork work) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>(),
                keyboardButtonsRow2 = new ArrayList<InlineKeyboardButton>(),
                keyboardButtonsRow3 = new ArrayList<InlineKeyboardButton>(),
                keyboardButtonsRow4 = new ArrayList<InlineKeyboardButton>();
        if (!work.isCompleteWork()) {
            if (work.isUseRecovery() == false || work.isRecoveryAnswered()) {
                if (work.isWaitRecovery() && (!work.isCancelRecovery() && !work.isConfirmRecovery())) {
                    keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Ожидаем согласование...").setCallbackData("#Master/Remont/" + work.getWorkID() + "/WaitRecoveryStatus"));
                } else {
                    if (!work.isUseRecovery() || work.isConfirmRecovery()) {
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ЛД " + (work.isUseLD() ? "✅" : "")).setCallbackData("#Master/Remont/" + work.getWorkID() + "/Component/LD"));
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ЧЛ" + (work.isUseCHL() ? "✅" : "")).setCallbackData("#Master/Remont/" + work.getWorkID() + "/Component/CHL"));
                        //keyboardButtonsRow1.add(new InlineKeyboardButton().setText("MR" + (work.isUseMR() ? "✅" : "")).setCallbackData("#Master/Remont/" + work.getWorkID() + "/Component/MR"));
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ВПЗ" + (work.isUseVPZ() ? "✅" : "")).setCallbackData("#Master/Remont/" + work.getWorkID() + "/Component/VPZ"));
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ФБ" + (work.isUseFB() ? "✅" : "")).setCallbackData("#Master/Remont/" + work.getWorkID() + "/Component/FB"));
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("MB" + (work.isUseMB() ? "✅" : "")).setCallbackData("#Master/Remont/" + work.getWorkID() + "/Component/MB"));
                        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("ЧП" + (work.isUseChip() ? "✅" : "")).setCallbackData("#Master/Remont/" + work.getWorkID() + "/Component/CHIP"));
                    }
                    if (work.isNeedRecovery() && !work.isUseRecovery())
                        keyboardButtonsRow2.add(new InlineKeyboardButton().setText("Согласовать").setCallbackData("#Master/Remont/" + work.getWorkID() + "/RECOVERY"));
                    keyboardButtonsRow3.add(new InlineKeyboardButton().setText("-5").setCallbackData("#Master/Remont/" + work.getWorkID() + "/UpdateToner/Pour/5"));
                    keyboardButtonsRow3.add(new InlineKeyboardButton().setText("-1").setCallbackData("#Master/Remont/" + work.getWorkID() + "/UpdateToner/Pour/1"));
                    keyboardButtonsRow3.add(new InlineKeyboardButton().setText("Тон: " + work.getTonerAmount()).setCallbackData("#Master/Remont/" + work.getWorkID() + "/UpdateToner/Display"));
                    keyboardButtonsRow3.add(new InlineKeyboardButton().setText("+5").setCallbackData("#Master/Remont/" + work.getWorkID() + "/UpdateToner/FillUp/5"));
                    keyboardButtonsRow3.add(new InlineKeyboardButton().setText("+10").setCallbackData("#Master/Remont/" + work.getWorkID() + "/UpdateToner/FillUp/10"));
                    keyboardButtonsRow4.add(new InlineKeyboardButton().setText("Завершить работу").setCallbackData("#Master/Remont/" + work.getWorkID() + "/Complete"));
                }
            } else {
                keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Ожидаем согласование...").setCallbackData("#Master/Remont/" + work.getWorkID() + "/WaitRecoveryStatus"));
            }
        } else {
            keyboardButtonsRow1.add(new InlineKeyboardButton().setText("Работа завершена  " + work.getEndDate() + " в " + work.getEndTime()).setCallbackData("#Master/Remont/" + work.getWorkID() + "/WorkCompleted"));
        }
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        rowList.add(keyboardButtonsRow2);
        rowList.add(keyboardButtonsRow3);
        rowList.add(keyboardButtonsRow4);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }

    public InlineKeyboardMarkup getStartWorkButton(MasterWork work) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();
        List<InlineKeyboardButton> keyboardButtonsRow1 = new ArrayList<InlineKeyboardButton>();
        keyboardButtonsRow1.add(new InlineKeyboardButton().setText("\uD83D\uDEE0 Взять в работу").setCallbackData("#Master/TakeToWork/" + work.getWorkID()));
        List<List<InlineKeyboardButton>> rowList = new ArrayList<List<InlineKeyboardButton>>();
        rowList.add(keyboardButtonsRow1);
        inlineKeyboardMarkup.setKeyboard(rowList);
        return inlineKeyboardMarkup;
    }
}
