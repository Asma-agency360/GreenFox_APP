package com.example.aptech.greenfox;
public class itemStore {
    String ItemName;
    String Price;
    String Image;
    String postID;
    String Category;


    public itemStore()
    {
    }

    public itemStore(String image,String item_name, String item_price, String post_id, String category)
    {
        ItemName=item_name;
        Price=item_price;
        this.Image=image;
        postID=post_id;
        Category=category;
    }

    public String getCategory() {
        return Category;
    }

    public void setCategory(String category) {
        Category = category;
    }

    public String getPostID() {
        return postID;
    }

    public void setPostID(String postID) {
        this.postID = postID;
    }

    public String getImage() {
        return Image;
    }

    public void setImage(String image) {
        Image = image;
    }

    public String getPrice() {
        return Price;
    }

    public void setPrice(String price) {
        Price = price;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

}
