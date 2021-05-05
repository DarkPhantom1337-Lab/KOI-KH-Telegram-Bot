package ua.darkphantom1337.koi.kh.handlers;

import ua.darkphantom1337.koi.kh.*;
import ua.darkphantom1337.koi.kh.entitys.Cartridge;
import ua.darkphantom1337.koi.kh.entitys.User;

public class UserPhotoHandler {

    private Bot bot = Bot.bot;

    public boolean handleUserPhoto(User user, java.io.File f) {
        if (user.getCartridgesID().size() > 0 || user.getUserAction().equals("user_wait_qr")) {
            String read_result = DarkQRReader.readQRCode(f);
            if (!read_result.equals("NotFound")) {
                Long cartridgeID = 0L;
                try {
                    cartridgeID = Long.parseLong(read_result);
                    if (user.getCartridgesID().contains(cartridgeID)) {
                        DataBase.setUsFields(user.getID(), "lastReadedCartridge", cartridgeID.intValue());
                        Cartridge cartridge = new Cartridge(cartridgeID);
                        bot.user_tema.put(user.getID(), "Заправка картриджа");
                        bot.user_model.put(user.getID(), cartridge.getModel());
                        user.setUserLastAdress(user.getUserAdres());
                        user.setUserAdress(cartridge.getAddress());
                        bot.sendMsg(user.getID().intValue(), "Проверьте правильность ввода: "
                                + "\nВаш адрес: " + cartridge.getAddress()
                                + "\nВаша модель/дефект: " + cartridge.getModel(), "prov_info");
                    } else
                        bot.sendMsgToUser(user.getID(), "Картридж №" + cartridgeID + " не принадлежит Вам или Вашей компании, обратитесь к администрации для уточнения деталей.");
                    return true;
                } catch (Exception e) {
                    bot.sendMsgToUser(user.getID(), "Я не смог считать данные с QR кода на фото, возможно фото было плохого качества. Сделайте чёткое фото QR-кода и отправьте мне.");
                    return true;
                }
            } else {
                bot.sendMsgToUser(user.getID(), "Я не смог считать данные с QR кода на фото, возможно фото было плохого качества. Сделайте чёткое фото QR-кода и отправьте мне.");
                return true;
            }
        }
        return false;
    }
}
