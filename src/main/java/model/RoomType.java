package model;

public class RoomType {

    private int typeId;
    private int categoryId;
    private String typeName;
    private double price;

    public int getTypeId() { return typeId; }

    public void setTypeId(int typeId) {
        this.typeId = typeId;
    }

    public int getCategoryId() { return categoryId; }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTypeName() { return typeName; }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public double getPrice() { return price; }

    public void setPrice(double price) {
        this.price = price;
    }
}