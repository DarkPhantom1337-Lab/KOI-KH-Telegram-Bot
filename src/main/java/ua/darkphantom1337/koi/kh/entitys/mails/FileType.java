package ua.darkphantom1337.koi.kh.entitys.mails;

public enum FileType {

    IMAGE, EXCEL,OLD_EXCEL, WORD,OLD_WORD, POWERPOINT,OLD_POWERPOINT, PDF, VIDEO, GIF, TEXT;

    /**
     * IMAGE - Картинка .png
     * EXCEL - Таблицы .xlsx
     * WORD - Вёрд .docx
     * POWERPOINT - Презентация .pptx
     * PDF - ПДФ .pdf
     * VIDEO - Видос .mp4
     * GIF - Гифка .gif
     */

    public static String getMimeType(FileType type){
        if (type.equals(IMAGE))
            return ".png";
        if (type.equals(EXCEL))
            return ".xlsx";
        if (type.equals(WORD))
            return ".docx";
        if (type.equals(POWERPOINT))
            return ".pptx";
        if (type.equals(PDF))
            return ".pdf";
        if (type.equals(VIDEO))
            return ".mp4";
        if (type.equals(GIF))
            return ".gif";
        if (type.equals(OLD_WORD))
            return ".doc";
        if (type.equals(OLD_EXCEL))
            return ".xls";
        if (type.equals(OLD_POWERPOINT))
            return ".ppt";
        if (type.equals(TEXT))
            return ".txt";
        return ".undefined";
    }

}
