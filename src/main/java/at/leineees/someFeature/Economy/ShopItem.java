package at.leineees.someFeature.Economy;

import java.util.List;

public class ShopItem {
    private final int cost;
    private final String itemType;
    private final String namespace;
    private final int amount;
    private Meta meta;

    public ShopItem(int cost, String itemType, String namespace, int amount) {
        this.cost = cost;
        this.itemType = itemType;
        this.namespace = namespace;
        this.amount = amount;
    }

    public int getCost() {
        return cost;
    }

    public String getItemType() {
        return itemType;
    }

    public String getNamespace() {
        return itemType.split(":")[0];
    }

    public int getAmount() {
        return amount;
    }

    public Meta getMeta() {
        return meta;
    }

    public void setMeta(Meta meta) {
        this.meta = meta;
    }

    public static class Meta {
        private final List<String> lore;

        public Meta(List<String> lore) {
            this.lore = lore;
        }

        public List<String> getLore() {
            return lore;
        }
    }
}