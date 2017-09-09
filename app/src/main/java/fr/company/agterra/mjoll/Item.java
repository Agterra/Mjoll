package fr.company.agterra.mjoll;

/**
 * Created by Agterra on 08/09/2017.
 */

public class Item {

    private String name;

    private int quantity;

    public Item() {

        this.name = "none";

        this.quantity = 0;

    }

    public Item(String name) {

        this.name = name;

        this.quantity = 1;

    }

    public Item(String name, int quantity) {

        this.name = name;

        this.quantity = quantity;

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

}
