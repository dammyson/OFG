package com.ofidy.ofidyshoppingbrowser.Materials.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.google.gson.Gson;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Category;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Currency;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Image;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Region;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Transaction;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ari on 9/12/16.
 *
 * Utility class for all local database operations by app
*/
public class OfidyDB {

    //All tables
	private static final String PRODUCTS = "products";
    private static final String TRANSACTIONS = "transactions";
    private static final String REGIONS = "regions";
    private static final String CURRENCIES = "currencies";
    private static final String CART = "cart";
	private static final String ADDRESSES = "addresses";
    private static final String CATEGORIES = "categories";
    private static final String SUB_CATEGORIES = "sub_categories";
    private static final String IMAGES = "images";

	private static NobsDBOpenHelper tdHelper;
    private static OfidyDB instance;
    private static SQLiteDatabase db=null;
    private static final String C_ROWID = "_id";

    //Name of database
    private static final String DB_NAME = "Ofidy";
    //Current database version, should be changed after update
	private static final int DB_VERSION = 2;
    //Gson object to change classes to json string and back
	private Gson gson = new Gson();

    private OfidyDB(Context context) {
		tdHelper = new NobsDBOpenHelper(context, DB_NAME, null, DB_VERSION);
	}

    //used to instanciate class
    public static OfidyDB getInstance(Context con) {
    	if (instance == null) {
            instance = new OfidyDB(con);
        }
        if (db==null) {
            db= tdHelper.getWritableDatabase();
        }
        return instance;
    }

    //class to create tables in database
    private static class NobsDBOpenHelper extends SQLiteOpenHelper {
        static final String CREATE_TABLE = "CREATE TABLE ";

		NobsDBOpenHelper(Context context, String name,
                         CursorFactory factory, int version) {
			super(context, name, factory, version);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(CREATE_TABLE + PRODUCTS + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "title text ," +
                    "date long ," +
                    "body text ," +
                    "category string ,"+
                    "UNIQUE (id)" +
                    ");"
            );
            db.execSQL(CREATE_TABLE + TRANSACTIONS + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "body text ," +
                    "date long ," +
                    "UNIQUE (id)" +
                    ");"
            );
			db.execSQL(CREATE_TABLE + ADDRESSES + " (" +
            		C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "body text ," +
                    "date long ," +
                    "UNIQUE (id)" +
                    ");"
            );
            db.execSQL(CREATE_TABLE + CART + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "body text ," +
                    "time long ," +
                    "UNIQUE (id)" +
                    ");"
            );
            db.execSQL(CREATE_TABLE + REGIONS + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "name text ," +
                    "UNIQUE (id)" +
                    ");"
            );
            db.execSQL(CREATE_TABLE + CURRENCIES + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "name text ," +
                    "code text ," +
                    "UNIQUE (id)" +
                    ");"
            );
            db.execSQL(CREATE_TABLE + IMAGES + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "url text ," +
                    "UNIQUE (id)" +
                    ");"
            );
            db.execSQL(CREATE_TABLE + CATEGORIES + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "name text ," +
                    "desc text ," +
                    "logo text ," +
                    "time long ," +
                    "UNIQUE (id)" +
                    ");"
            );
            db.execSQL(CREATE_TABLE + SUB_CATEGORIES + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "groupId integer ," +
                    "name text ," +
                    "desc text ," +
                    "logo text ," +
                    "time long ," +
                    "UNIQUE (id)" +
                    ");"
            );
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
            db.execSQL(CREATE_TABLE + IMAGES + " (" +
                    C_ROWID + " integer PRIMARY KEY autoincrement," +
                    "id integer ," +
                    "url text ," +
                    "UNIQUE (id)" +
                    ");"
            );
		}
    }

    /**
     * Inserts wish item into database.
     */
    void insertWish(Product prod) {
        ContentValues values = new ContentValues();
        values.put("id",prod.getId());
        values.put("date", System.currentTimeMillis());
        values.put("body", gson.toJson(prod));
        try {
            db.insertOrThrow(PRODUCTS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gets all saved wish items.
     */
    public ArrayList<Product> getWishlist() {
        ArrayList<Product> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + PRODUCTS +" ORDER BY date";
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    String date = mCursor.getString(mCursor.getColumnIndex("body"));
                    all.add(gson.fromJson(date, Product.class));
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    boolean emptyWishlist() {
        return db.delete(PRODUCTS, null, null) == 1;
    }

    /**
     * inserts transaction item in cart.
     *
     * @param trans the transaction objetc
     */
    void insertTransaction(Transaction trans) {
        ContentValues values = new ContentValues();
        values.put("id",trans.getId());
        values.put("date", System.currentTimeMillis());
        values.put("body", gson.toJson(trans));
        try {
            db.insertOrThrow(TRANSACTIONS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Loads all transaction items.
     */
    public ArrayList<Transaction> getTransactions() {
        ArrayList<Transaction> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TRANSACTIONS +" ORDER BY date";
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    String date = mCursor.getString(mCursor.getColumnIndex("body"));
                    all.add(gson.fromJson(date, Transaction.class));
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    /**
     * Removes item from cart.
     *
     * @param cart the id of cart item, as a string
     * @return {@code true} if successfully removed, {@code false} otherwise.
     */
    boolean removeCartItem(String cart) {
        return db.delete(CART,  "id = ? ", new String[]{String.valueOf(cart)}) == 1;
    }

    /**
     * Remove all saved transaction
     *
     * @return {@code true} if the successful, {@code false} otherwise.
     */
    boolean emptyTransactions() {
        return db.delete(TRANSACTIONS, null, null) == 1;
    }

    /**
     * Inserts item into cart.
     *
     * @param cart the cart object
     */
    void insertCartItem(Cart cart) {
        ContentValues values = new ContentValues();
        values.put("id",cart.getId());
        values.put("time", System.currentTimeMillis());
        values.put("body", gson.toJson(cart));
        try {
            db.insertOrThrow(CART, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns all saved cart items.
     */
    public ArrayList<Cart> getCartItems() {
    	ArrayList<Cart> all = new ArrayList<>();
  		String selectQuery = "SELECT * FROM " + CART +" ORDER BY time";
        Cursor mCursor = db.rawQuery ( selectQuery, null );
 		if (mCursor != null) {
 			if(mCursor.moveToFirst()) {
                do {
 		            String date = mCursor.getString(mCursor.getColumnIndex("body"));
                    all.add(gson.fromJson(date, Cart.class));
 		        }
 				while (mCursor.moveToNext());
 			}
            mCursor.close();
 		}
 		Collections.reverse(all);
 		return all;
	}

    /**
     * Returns no. of items in cart.
     *
     * @return {@code long} number of cart items.
     */
    public long getCartItemsSize() {
        return DatabaseUtils.longForQuery(db, "SELECT COUNT(*) FROM "+CART, null);
    }

    /**
     * Saves address object.
     *
     * @param address the address object
     */
    public void insertAddress(Address address) {
        ContentValues values = new ContentValues();
        values.put("id", address.getId());
        values.put("date", System.currentTimeMillis());
        values.put("body", gson.toJson(address));
        try {
            db.insertOrThrow(ADDRESSES, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Removes all saved address.
     *
     * @return {@code true} if successfully removed, {@code false} otherwise.
     */
    public boolean emptyAddress() {
        return db.delete(ADDRESSES, null, null) == 1;
    }

    public ArrayList<Address> getAddresses() {
        ArrayList<Address> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + ADDRESSES +" ORDER BY date";
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if (mCursor.moveToFirst()) {
                do {
                    String date = mCursor.getString(mCursor.getColumnIndex("body"));
                    all.add(gson.fromJson(date, Address.class));
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    /**
     * Removes all items from cart.
     *
     * @return {@code true} if successfully removed, {@code false} otherwise.
     */
    boolean emptyCart() {
        return db.delete(CART, null, null) == 1;
    }

    private void insertCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("id", category.getId());
        values.put("name", category.getName());
        values.put("desc", category.getDesc());
        values.put("logo", category.getLogo());
        try {
            db.insertOrThrow(CATEGORIES, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    void insertSubCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put("id", category.getId());
        values.put("name", category.getName());
        values.put("desc", category.getDesc());
        values.put("logo", category.getLogo());
        values.put("groupId", category.getGroupId());
        try {
            db.insertOrThrow(SUB_CATEGORIES, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void updateCategory(Category category) {
        if(category != null) {
            if(getCategory(category.getId()) != null) {
                ContentValues cv = new ContentValues(4);
                cv.put("name", category.getName());
                cv.put("desc", category.getDesc());
                cv.put("logo", category.getLogo());
                db.update(category.getGroupId() == -1 ? CATEGORIES : SUB_CATEGORIES, cv, "id = ?",
                        new String[]{String.valueOf(category.getId())});
            }
        }
    }

    private String getCategory(long id) {
        Cursor c;
        c = db.query(CATEGORIES, new String[]{"name"}, "id = ?",
                new String[] { String.valueOf(id)}, null, null, null);
        if (c.getCount()>0) {
            c.moveToFirst();
            return c.getString(0);
        }
        c.close();
        return null;
    }

    void insertOrUpdateCategory(Category category) {
        if(getCategory(category.getId()) == null) {
            insertCategory(category);
        }
        else {
            updateCategory(category);
        }
    }

    public ArrayList<Category> getCategories() {
        ArrayList<Category> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CATEGORIES;
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex("id"));
                    String name = mCursor.getString(mCursor.getColumnIndex("name"));
                    String desc = mCursor.getString(mCursor.getColumnIndex("desc"));
                    String logo = mCursor.getString(mCursor.getColumnIndex("logo"));
                    System.out.println("............................................logo = "+logo);
                    Category category = new Category(id, name, desc, logo);
                    category.setSubCategories(getSubCategories(id));
                    all.add(category);
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    private ArrayList<Category> getSubCategories(int groupId) {
        ArrayList<Category> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + SUB_CATEGORIES + " WHERE groupId = "+groupId;
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    int id = mCursor.getInt(mCursor.getColumnIndex("id"));
                    String name = mCursor.getString(mCursor.getColumnIndex("name"));
                    String desc = mCursor.getString(mCursor.getColumnIndex("desc"));
                    String logo = mCursor.getString(mCursor.getColumnIndex("logo"));
                    Category category = new Category(id, name, desc, logo);
                    category.setGroupId(groupId);
                    all.add(category);
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    void emptyCategories() {
        db.delete(CATEGORIES, null, null);
        db.delete(SUB_CATEGORIES, null, null);
    }

    void insertCurrency(Currency currency) {
        ContentValues values = new ContentValues();
        values.put("id", currency.getId());
        values.put("name", currency.getName());
        values.put("code", currency.getCode());
        try {
            db.insertOrThrow(CURRENCIES, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Currency> getCurrencies() {
        ArrayList<Currency> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + CURRENCIES ;
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    String id = mCursor.getString(mCursor.getColumnIndex("id"));
                    String name = mCursor.getString(mCursor.getColumnIndex("name"));
                    String code = mCursor.getString(mCursor.getColumnIndex("code"));
                    Currency currency = new Currency(id, name, code);
                    all.add(currency);
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    void emptyCurrencies() {
        db.delete(CURRENCIES, null, null);
    }

    void insertImage(Image image) {
        ContentValues values = new ContentValues();
        values.put("id", image.getId());
        values.put("url", image.getUrl());
        try {
            db.insertOrThrow(IMAGES, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Image> getImages() {
        ArrayList<Image> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + IMAGES ;
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    String id = mCursor.getString(mCursor.getColumnIndex("id"));
                    String url = mCursor.getString(mCursor.getColumnIndex("url"));
                    Image image = new Image(id, url);
                    all.add(image);
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    void emptyImages() {
        db.delete(IMAGES, null, null);
    }

    void insertRegion(Region region) {
        ContentValues values = new ContentValues();
        values.put("id", region.getId());
        values.put("name", region.getName());
        try {
            db.insertOrThrow(REGIONS, null, values);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Region> getRegions() {
        ArrayList<Region> all = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + REGIONS ;
        Cursor mCursor = db.rawQuery ( selectQuery, null );
        if (mCursor != null) {
            if(mCursor.moveToFirst()) {
                do {
                    String id = mCursor.getString(mCursor.getColumnIndex("id"));
                    String name = mCursor.getString(mCursor.getColumnIndex("name"));
                    Region region = new Region(id, name);
                    all.add(region);
                }
                while (mCursor.moveToNext());
            }
            mCursor.close();
        }
        Collections.reverse(all);
        return all;
    }

    void emptyRegions() {
        db.delete(REGIONS, null, null);
    }

}
