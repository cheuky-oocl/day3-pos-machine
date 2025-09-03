package pos.machine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PosMachine {
    public String printReceipt(List<String> barcodes) {
        List<ReceiptItem> receiptItems = decodeToItems(barcodes);
        Receipt receipt = calculateCost(receiptItems);
        return renderReceipt(receipt);
    }

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

    private String renderReceipt(Receipt receipt){
        StringBuilder receiptString = new StringBuilder("***<store earning no money>Receipt***\n");
        String receiptItemsString = generateItemsReceipt(receipt);
        receiptString.append(generateReceipt(receiptItemsString, receipt.getTotalPrice()));
        return receiptString.toString();
    }

    private String generateItemsReceipt(Receipt receipt){
        StringBuilder receiptItemsString = new StringBuilder();
        for (ReceiptItem receiptItem : receipt.getReceiptItems()) {
            receiptItemsString.append("Name: ").append(receiptItem.getName())
                    .append(", Quantity: ").append(receiptItem.getQuantity())
                    .append(", Unit price: ").append(receiptItem.getPrice()).append(" (yuan)")
                    .append(", Subtotal: ").append(receiptItem.getSubTotal()).append(" (yuan)\n");
        }
        return receiptItemsString.toString();
    }

    private String generateReceipt(String itemReceiptString, int totalPrice){
        return itemReceiptString + "----------------------\n" +
                "Total: " + totalPrice + " (yuan)\n" +
                "**********************";
    }
}
