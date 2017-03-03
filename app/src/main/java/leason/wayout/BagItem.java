package leason.wayout;

import java.util.Date;

/**
 * Created by leason on 2017/3/1.
 */

public class BagItem {

    private int Num;
    private Type type;
    private Date date;

   enum Type {
        food, water, battery, bag

    }

    public BagItem(int Num, Type type, Date date) {
        this.Num = Num;
        this.type = type;
        this.date = date;
    }

    public int getNum() {
        return Num;
    }

    public void setNum(int num) {
        Num = num;
    }

    public Type getType() {
        return type;
    }

    public void setType(Type type) {
        this.type = type;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
