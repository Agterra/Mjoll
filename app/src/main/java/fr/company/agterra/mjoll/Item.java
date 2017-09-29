package fr.company.agterra.mjoll;

import java.io.Serializable;

/**
 * Created by Agterra on 08/09/2017.
 */

public class Item implements Serializable{

    private String name;

    private int quantity;

    private TypeItem type;

    public Item() {

        this.name = "none";

        this.quantity = 0;

        this.type = TypeItem.PUBLIC;

    }

    public Item(String name) {

        this.name = name;

        this.quantity = 1;

        this.type = TypeItem.PUBLIC;

    }

    public Item(String name, int quantity) {

        this.name = name;

        this.quantity = quantity;

        this.type = TypeItem.PUBLIC;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setType(TypeItem type) {this.type = type;}

    public TypeItem getType() {return this.type;}

    public void incrementNumber()
    {

        this.quantity ++;

    }

    public void decrementNumber()
    {

        if(this.quantity > 1)
        {

            this.quantity --;

        }

    }

    public void incrementByTen()
    {

        this.quantity += 10;

    }

    public void decrementByTen()
    {

        if(this.quantity < 11)
        {

            this.quantity = 1;

        }
        else
        {

            this.quantity -= 10;

        }

    }

}
