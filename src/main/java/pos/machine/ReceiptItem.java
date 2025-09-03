package pos.machine;

public class ReceiptItem {
    private final String name;
    private int quantity;
    private final int unitPrice;
    private int subTotal;

    public ReceiptItem(String name, int unitPrice) {
        this.name = name;
        this.quantity = 1;
        this.unitPrice = unitPrice;
        this.subTotal = 0;
    }

    public String getName() {
        return name;
    }

    public void setQuantity(int quantity) {this.quantity = quantity;}

    public int getQuantity() {return quantity;}

    public int getPrice() {
        return unitPrice;
    }

    public void setSubTotal(int subTotal) {this.subTotal = subTotal;}

    public int getSubTotal() {return subTotal;}
}
