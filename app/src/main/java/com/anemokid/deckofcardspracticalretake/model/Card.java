package com.anemokid.deckofcardspracticalretake.model;

/**
 * single card view:
 */
public class Card {

    // private members:
    private String suit;

    private Images images;


    private String image;

    private String code;

    private String value;

    // constructor:
    public Card(){

    }

    public Card(String suit, String image, String code, String value) {
        this.suit = suit;
        this.image = image;
        this.code = code;
        this.value = value;
    }

    // getters
    public String getSuit() {
        return suit;
    }

    public Images getImages() {
        return images;
    }

    public String getImage() {
        return image;
    }

    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

    // setters:

    public void setSuit(String suit) {
        this.suit = suit;
    }

    public void setImages(Images images) {
        this.images = images;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setValue(String value) {
        this.value = value;
    }

    // to String():
    @Override
    public String toString() {
        return "Card{" +
                "suit='" + suit + '\'' +
                ", images=" + images +
                ", image='" + image + '\'' +
                ", code='" + code + '\'' +
                ", value='" + value + '\'' +
                '}';
    }

    /**
     * Images inner class:
     */
    public class Images {

        // private members:
        private String png;


        private String svg;

        // constructors:
        public Images(){

        }

        public Images(String png, String svg) {
            this.png = png;
            this.svg = svg;
        }

        // getters + setters:
        public String getPng() {
            return png;
        }

        public void setPng(String png) {
            this.png = png;
        }

        public String getSvg() {
            return svg;
        }

        public void setSvg(String svg) {
            this.svg = svg;
        }

        @Override
        public String toString() {
            return "Images{" +
                    "png='" + png + '\'' +
                    ", svg='" + svg + '\'' +
                    '}';
        }
    }
}
