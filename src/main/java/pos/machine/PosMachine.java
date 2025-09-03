package pos.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        Receipt receipt = calculateCost(receiptItems);
    private List<ReceiptItem> decodeToItems (List<String> barcodes){
        List<Item> allItems = loadAllItems();
        List<ReceiptItem> receiptItems = new ArrayList<>();
        Map<String, ReceiptItem> receiptItemMap = new HashMap<>();
        for (String barcode : barcodes) {
            boolean found = false;
            for (Item item : allItems) {
                if (item.getBarcode().equals(barcode)) {
                    found = true;
                    if (receiptItemMap.containsKey(barcode)) {
                        ReceiptItem existingReceiptItem = receiptItemMap.get(barcode);
                        existingReceiptItem.setQuantity(existingReceiptItem.getQuantity() + 1);
                    } else {
                        ReceiptItem newReceiptItem = new ReceiptItem(item.getName(),1,  item.getPrice());
                        receiptItems.add(newReceiptItem);
                        receiptItemMap.put(barcode, newReceiptItem);
                    }
                }
            }
            if (!found) {
                throw new IllegalArgumentException("Invalid barcode: " + barcode);
            }
        }
        return receiptItems;
    }

    private List<Item> loadAllItems(){
        return ItemsLoader.loadAllItems();
    }

    private Receipt calculateCost(List<ReceiptItem> receiptItems){
        List<ReceiptItem> receiptItemsWithItemCost = calculateItemsCost(receiptItems);
        int totalPrice = calculateTotalPrice(receiptItemsWithItemCost);
        return new Receipt(receiptItemsWithItemCost, totalPrice);
    }

    private List<ReceiptItem> calculateItemsCost(List<ReceiptItem> receiptItems){
        for (ReceiptItem receiptItem : receiptItems) {
            int subTotal = receiptItem.getQuantity() * receiptItem.getPrice();
            receiptItem.setSubTotal(subTotal);
        }
        return receiptItems;
    }

    private int calculateTotalPrice(List<ReceiptItem> receiptItems){
        int totalPrice = 0;
        for (ReceiptItem receiptItem : receiptItems) {
            totalPrice += receiptItem.getSubTotal();
        }
        return totalPrice;
    }
}
