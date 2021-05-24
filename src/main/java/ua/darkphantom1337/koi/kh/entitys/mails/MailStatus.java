package ua.darkphantom1337.koi.kh.entitys.mails;

public enum MailStatus {

    CREATING, WAITING_TO_START, READY_TO_RUN,COMPLETED, CANCELED

    /**
     * CREATING - Создаётся в данный момент
     * WAITING_TO_START - Ожидает подтверждения старта
     * READY_TO_RUN - В ожидании выполнения
     * COMPLETED - Завершена
     * CANCELED - Отменена
     */

}
