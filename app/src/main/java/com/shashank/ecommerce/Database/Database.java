package com.shashank.ecommerce.Database;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;
import com.shashank.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteAssetHelper {
    private static final String DB_NAME = "ecommerce.db";
    private static final int DB_VER = 1;

    public Database(Context context)
    {
        super(context,DB_NAME,null,DB_VER);
    }

    public List<Product> getCartItems(String userEmail)
    {
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String[] sqlSelect = {"UserEmail","ProductId","ProductName","Price","Image","Quantity","ShippingPrice"};
        String sqlTable = "OrderDetail";

        qb.setTables(sqlTable);
        Cursor c = qb.query(db,sqlSelect,"UserEmail=?",new String[]{userEmail},null,null,null);

        final List<Product> result = new ArrayList<>();
        if(c.moveToFirst())
        {
            do{
                result.add(new Product(
                        c.getString(c.getColumnIndex("UserEmail")),
                        c.getString(c.getColumnIndex("ProductId")),
                        c.getString(c.getColumnIndex("ProductName")),
                        c.getString(c.getColumnIndex("Price")),
                        c.getString(c.getColumnIndex("Image")),
                        c.getString(c.getColumnIndex("Quantity")),
                        c.getString(c.getColumnIndex("ShippingPrice"))
                ));
            }while(c.moveToNext());
        }
        return result;
    }

    public void addToCart(Product product)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("INSERT OR REPLACE INTO OrderDetail(UserEmail,ProductId,ProductName,Price,Image,Quantity,ShippingPrice) VALUES('%s','%s','%s','%s','%s','%s','%s');",
                product.getUserEmail(),
                product.getProductId(),
                product.getName(),
                product.getPrice(),
                product.getImageUrl(),
                product.getQuantity(),
                product.getShippingPrice());

        db.execSQL(query);
    }

    public void cleanCart(String userEmail)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("DELETE from OrderDetail WHERE UserEmail='%s'",userEmail);
        db.execSQL(query);
    }

    public int getCartCount(String userEmail)
    {
        int count = 0;
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("SELECT COUNT(*) FROM OrderDetail WHERE UserEmail='%s'",userEmail);
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst())
        {
            do {
                count = cursor.getInt(0);
            }while (cursor.moveToNext());
        }
        return count;
    }

    public void updateCart(Product product)
    {
        SQLiteDatabase db = getReadableDatabase();
        String query = String.format("UPDATE OrderDetail SET Quantity='%s' WHERE UserEmail='%s' AND ProductId='%s'",product.getQuantity(),product.getUserEmail(),product.getProductId());
        db.execSQL(query);
    }
}
