package cn.afterturn.easypoi.entity;

/**
 * 特殊符号
 *
 * @author JueYue
 * @date 2021-03-21-3-10
 * @since 1.0
 */
public enum SpecialSymbolsEnum {

    SMALL_BOX("小方框", "\u002A"),
    BIG_BOX("大方框", "\u00A3");

    private String font;
    private String unicode;
    private String name;


    SpecialSymbolsEnum(String name, String unicode) {
        this.name = name;
        this.unicode = unicode;
        this.font = "Wingdings 2";
    }

    SpecialSymbolsEnum(String name, String unicode, String font) {
        this.name = name;
        this.unicode = unicode;
        this.font = font;
    }

    public String getFont() {
        return font;
    }

    public String getUnicode() {
        return unicode;
    }

    public String getName() {
        return name;
    }
}
