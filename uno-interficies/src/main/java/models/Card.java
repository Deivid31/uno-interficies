package models;

public class Card {

    private int num;
    private Cards.COLOR color;
    private Cards.TYPE power;

    public Card(int num, Cards.COLOR color, Cards.TYPE power) {
        this.num = num;
        this.color = color;
        this.power = power;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Cards.COLOR getColor() {
        return color;
    }

    public void setColor(Cards.COLOR color) {
        this.color = color;
    }

    public Cards.TYPE getPower() {
        return power;
    }

    public void setPower(Cards.TYPE power) {
        this.power = power;
    }

    @Override
    public String toString() {
        return this.getColor().toString() + " " + this.getNum() + " (" + this.getPower().toString() + ")";
    }

}
