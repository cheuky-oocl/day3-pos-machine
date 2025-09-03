package pos.machine;

import java.util.List;

public class Receipt {
    private List<ReceiptItem> receiptItems;
    private int totalPrice;

    public Receipt(List<ReceiptItem> receiptItems, int totalPrice) {
        this.receiptItems = receiptItems;
        this.totalPrice = totalPrice;
    }

    public List<ReceiptItem> getReceiptItems() {
        return receiptItems;
    }

    public int getTotalPrice() {
        return totalPrice;
    }
}
