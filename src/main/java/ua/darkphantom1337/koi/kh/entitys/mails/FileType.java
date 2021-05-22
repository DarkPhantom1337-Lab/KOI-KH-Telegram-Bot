package ua.darkphantom1337.koi.kh.entitys.mails;

public enum FileType {

    IMAGE, EXCEL, WORD, POWERPOINT, PDF;

    /**
     * IMAGE - Картинка .png
     * EXCEL - Таблицы .xlsx
     * WORD - Вёрд .docx
     * POWERPOINT - Презентация .pptx
     * PDF - ПДФ .pdf
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
        return ".undefined";
    }

}
