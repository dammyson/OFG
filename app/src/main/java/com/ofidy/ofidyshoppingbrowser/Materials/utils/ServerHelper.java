package com.ofidy.ofidyshoppingbrowser.Materials.utils;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.crashlytics.android.Crashlytics;
import com.google.gson.Gson;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddCartItemErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddEditAddressEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddEditAddressStatusEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddGuestAddressEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddWishListItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AddressLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ApiCallEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.AskToLoginEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.BrandsLoadErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.BrandsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.BusProvider;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemAddedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemRemovedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CartItemUpdatedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.CategoryLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.DeleteCartItemErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.DeleteCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.EmptyCartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderBillDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderBillEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GetOrderCostEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GuestLoginDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GuestLoginErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.GuestLoginStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadBrandsEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadCategoryEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadCustomerAddressEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadEnvironmentEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadPaymentInfoDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadPaymentInfoErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadPaymentInfoEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadProductEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadProductsEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadShoppingCartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadSpecialProductsEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadStateEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadTransactionsEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoadWishlistEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LoginStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LogoutEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.LogoutStatusEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductLoadErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductsLoadErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ProductsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.RegisterDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.RegisterStartEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendBankTansferCancelEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendBankTransferCancelDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendBankVerifyEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendPaypalVerifyEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendPaystackVerifyEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendRavepayVerifyEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenDoneEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SendSimplePayTokenErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.ShoppingCartLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SpecialProductsLoadErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.SpecialProductsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.StatesLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.TransactionsLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCartItemErrorEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.UpdateCartItemEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.WishItemAddedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.Events.WishlistLoadedEvent;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Address;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Brand;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Cart;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Category;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Currency;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Customer;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Image;
import com.ofidy.ofidyshoppingbrowser.Materials.model.OrderInvoice;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Product;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Region;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Seller;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Size;
import com.ofidy.ofidyshoppingbrowser.Materials.model.State;
import com.ofidy.ofidyshoppingbrowser.Materials.model.Transaction;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.AppState;
import com.ofidy.ofidyshoppingbrowser.Materials.pref.UserPrefs;
import com.ofidy.ofidyshoppingbrowser.Ofidy;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by ari on 9/12/16.
 */
public class ServerHelper {

    private static final String TAG = "ServerHelper";
    private static final int PRODUCTS_FETCH_LIMIT = 30;
    private static String URL;

    private static final String CUSTOMER_CODE = "customer";
    private static final String PRODUCT_CODE = "product";
    private static final String CATEGORY_CODE = "category";
    private static final String CART_CODE = "cart";
    private static final String ORDER_CODE = "order";
    private static final String ENV_CODE = "env";
    private static final String BRAND_CODE = "brands";

    private Context mContext;
    private static OfidyDB mOfidy = null;
    private static Gson gson = null;
    private OkHttpClient mOkHttpClient = null;
    private final ArrayDeque<ApiCallEvent> mApiEventQueue = new ArrayDeque<>();

    public ServerHelper() {
        Crashlytics.log(Log.DEBUG, TAG, "Initializing NetworkService...");
    }

    public void start(Context context, OkHttpClient okHttpClient) {
        mOkHttpClient = okHttpClient;
        getBus().register(this);
        mOfidy = OfidyDB.getInstance(context);
        gson = new Gson();
        URL = ConfigHelper.getConfigValue(context, "api_url");
        mContext = context;
    }

    @SuppressWarnings("unused")
    public void stop() {
        getBus().unregister(this);
    }

    private void flushApiEventQueue(boolean loadCachedData) {
        Bus bus = getBus();
        boolean isQueueEmpty;
        while (! mApiEventQueue.isEmpty()) {
            ApiCallEvent event = mApiEventQueue.remove();
            isQueueEmpty = mApiEventQueue.isEmpty();
            if (loadCachedData) event.loadCachedData();
            bus.post(event);
            if (isQueueEmpty) {     // don't retry, gets into infinite loop
                mApiEventQueue.clear();
            }
        }
    }

    @Subscribe
    public void onLoadStateEvent(final LoadStateEvent event) {
        final String action = "getStates";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("action", action)
                    .add("code", ENV_CODE)
                    .add("region", event.region)
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(formBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    getBus().post(new StatesLoadedEvent(null, true, e.getMessage()));
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String s = response.body().string();
                            if (!TextUtils.isEmpty(s)) {
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")) {
                                    ArrayList<State> states = new ArrayList<State>();
                                    JSONArray array = json.getJSONArray("data");
                                    for(int i = 0; i < array.length(); i++){
                                        JSONObject ob = array.getJSONObject(i);
                                        State p = gson.fromJson(ob.toString(), State.class);
                                        if(p != null) {
                                            states.add(p);

                                        }
                                    }
                                    System.out.println("............................................state = "+s);
                                    getBus().post(new StatesLoadedEvent(states, false, null));

                                }
                                else
                                    getBus().post(new StatesLoadedEvent(null, true, json.getString("message")));
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new StatesLoadedEvent(null, true, e.getMessage()));
            flushApiEventQueue(true);
        }
    }

    @Subscribe
    public void onGetOrderCostEvent(final GetOrderCostEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                System.out.println(".......................................... order called");
                final String action = "getCost";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .add("dest", event.dest)
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new GetOrderCostErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    System.out.println("....................................cost "+s);
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        List<String> ss = new ArrayList<>();
                                        List<Double> dd = new ArrayList<>();
                                        boolean pod = false;
                                        boolean simplepay = false;
                                        JSONArray ar = json.getJSONArray("data");

                                        if(ar.length() > 0){
                                            for(int i = 0; i < 3; i++){
                                                StringBuilder sj = new StringBuilder();
                                                switch (i){
                                                    case 0:
                                                        sj.append("Fast shipping: ").append(ar.getString(5));
                                                        break;
                                                    case 1:
                                                        sj.append("Medium-speed shipping: ").append(ar.getString(6));
                                                        break;
                                                    case 2:
                                                        sj.append("Slow shipping: ").append(ar.getString(7));
                                                        break;
                                                }
                                                sj.append(Ofidy.getInstance().getCurrency()).append(ar.getString(i));
                                                if((i == 0 || ar.getDouble(0) != ar.getDouble(i)) && ar.getDouble(i) > 0) {
                                                    ss.add(sj.toString());
                                                    dd.add(ar.getDouble(i));
                                                }
                                            }
                                            if(ss.isEmpty()){
                                                getBus().post(new GetOrderCostErrorEvent("We're sorry, we couldn't find any " +
                                                        "shipping options to your location. There may be an error connecting to " +
                                                        "our database. Please try again later."));
                                                return;
                                            }
                                            if(ar.getInt(3) == 1){
                                                pod = true;
                                            }
                                            else if(ar.getInt(3) != 1){
                                                pod = false;
                                            }
                                            if(ar.isNull(4)){
                                                simplepay = false;
                                            }
                                            else if(ar.getInt(4) == 1){
                                                simplepay = true;
                                            }
                                            else if(ar.getInt(4) != 1){
                                                simplepay = false;
                                            }
                                        }
                                        getBus().post(new GetOrderCostDoneEvent(ss, dd, pod, simplepay));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new GetOrderCostErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new GetOrderCostErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }

    @Subscribe
    public void onGetOrderBillEvent(final GetOrderBillEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "getBill";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        //getBus().post(new GetOrderCostErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        String ar = json.getString("data");
                                        if(!TextUtils.isEmpty(ar)){
                                            String[] tt = ar.split(":");
                                            if(tt.length > 1 && tt[1].equals("0")) {
                                                getBus().post(new GetOrderBillDoneEvent(tt[0]));
                                            }
                                            else if(tt.length > 0)
                                                getBus().post(new GetOrderBillDoneEvent(ar));
                                            else
                                                getBus().post(new GetOrderCostErrorEvent(json.getString("message")));
                                        }
                                    }
                                    else{
                                        getBus().post(new GetOrderCostErrorEvent(json.getString("message")));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    //getBus().post(new GetOrderCostErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                flushApiEventQueue(true);
            }
    }

    @Subscribe
    public void onLoadPaymentInfoEvent(final LoadPaymentInfoEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "paymentInfo";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .add("bill_addr", event.billAdd)
                        .add("ship_addr", event.shipAdd)
                        .add("shipmethod", String.valueOf(event.shipMethod))
                        .add("shipred", event.shipReq)
                        .add("paymethod", event.payMethod)
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new LoadPaymentInfoErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    System.out.println("............................................pay = "+s);
                                    System.out.println("........................................login = "+s);
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        JSONObject ar = json.getJSONObject("data");
                                        OrderInvoice orderInvoice = gson.fromJson(ar.toString(), OrderInvoice.class);
                                        getBus().post(new LoadPaymentInfoDoneEvent(orderInvoice));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new LoadPaymentInfoErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new LoadPaymentInfoErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }
    @Subscribe
    public void onBankTransferCancelEvent(final SendBankTansferCancelEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "cancelPayment";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {

                                    System.out.println(".........................................................refserver"+s);
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        String ar = json.getString("message");
                                        getBus().post(new SendBankTransferCancelDoneEvent(ar));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }


    @Subscribe
    public void onSendPaystackVerifyEvent(final SendPaystackVerifyEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "paystackVerify";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("ref", event.ref)
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println(".........................................................ref server"+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        String ar = json.getString("message");
                                        getBus().post(new SendSimplePayTokenDoneEvent(ar));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }
    @Subscribe
    public void onSendRavepayVerifyEvent(final SendRavepayVerifyEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "ravePayVerify";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("ref", event.ref)
                        .add("amount", Double.toString(event.amount))
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println(".........................................................ref server"+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        String ar = json.getString("message");
                                        getBus().post(new SendSimplePayTokenDoneEvent(ar));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }

    @Subscribe
    public void onSendPaystackVerifyEvent(final SendPaypalVerifyEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "paypalVerify";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    System.out.println(".........................................................refserver"+s);
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        String ar = json.getString("message");
                                        if(ar.contains(":")){
                                            String[] ss = ar.split(":");
                                            if(ss.length > 1){
                                                ar = ss[0];
                                                UserPrefs.getInstance(mContext).setString(UserPrefs.Key.SID, ss[1]);
                                            }
                                        }
                                        getBus().post(new SendSimplePayTokenDoneEvent(ar));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }

    @Subscribe
    public void onSendBankVerifyEvent(final SendBankVerifyEvent event) {
        System.out.println("....................................................urgent3 = "+ UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID));

        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "bankVerify";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", ORDER_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {

                                    System.out.println(".........................................................refserver"+s);
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        String ar = json.getString("message");
                                        if(ar.contains(":")){
                                            String[] ss = ar.split(":");
                                            if(ss.length > 1){
                                                ar = ss[0];
                                                UserPrefs prefs = UserPrefs.getInstance(mContext);
                                                prefs.setString(UserPrefs.Key.SID, ss[1]);
                                                //prefs.setString(UserPrefs.Key.SID, ss[1]);
                                                System.out.println(".........................................................refserver:"+ ss[1]);
                                                System.out.println("....................................................urgent2 = "+ UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID));

                                            }
                                        }
                                        getBus().post(new SendSimplePayTokenDoneEvent(ar));
                                        getBus().post(new LoadShoppingCartEvent(true));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }
            catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new SendSimplePayTokenErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }


    @Subscribe
    public void onUpdateCartItemEvent(final UpdateCartItemEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "edit";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", CART_CODE)
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .add("tid", event.tid)
                        .add("promo", event.promoCode)
                        .add("qty", String.valueOf(event.quantity))
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new UpdateCartItemErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println("............................................s "+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        getBus().post(new CartItemUpdatedEvent());
                                        Ofidy.cartItems = new ArrayList<>();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            Cart p = gson.fromJson(ob.toString(), Cart.class);
                                            if(p != null) {
                                                Ofidy.cartItems.add(p);
                                            }
                                        }
                                        getBus().post(new ShoppingCartLoadedEvent(Ofidy.cartItems));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new UpdateCartItemErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new UpdateCartItemErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }

    @Subscribe
    public void onEmptyCartEvent(final EmptyCartEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "empty";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", CART_CODE)
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new DeleteCartItemErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println("............................................empty "+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        getBus().post(new CartItemRemovedEvent());
                                        Ofidy.cartItems = new ArrayList<>();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            Cart p = gson.fromJson(ob.toString(), Cart.class);
                                            if(p != null) {
                                                Ofidy.cartItems.add(p);
                                            }
                                        }
                                        getBus().post(new ShoppingCartLoadedEvent(Ofidy.cartItems));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new DeleteCartItemErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new DeleteCartItemErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }

    @Subscribe
    public void onLoadShoppingCartEvent(final LoadShoppingCartEvent event) {

        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "get";
                HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
                urlBuilder.addQueryParameter("action", action);
                urlBuilder.addQueryParameter("code", CART_CODE);
                urlBuilder.addQueryParameter("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID));
                urlBuilder.addQueryParameter("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID));
                urlBuilder.addQueryParameter("prf", Ofidy.getInstance().getCurrency());
                String url = urlBuilder.build().toString();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                System.out.println(".......................................... cartingss" + s);
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        Ofidy.cartItems = new ArrayList<>();
                                        mOfidy.emptyCart();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            Cart p = gson.fromJson(ob.toString(), Cart.class);
                                            if(p != null) {
                                                mOfidy.insertCartItem(p);
                                                //Ofidy.cartItems.clear();
                                                Ofidy.cartItems.add(p);
                                            }
                                        }
                                        getBus().post(new ShoppingCartLoadedEvent(Ofidy.cartItems));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                                }
                            }else{
                                System.out.println(".......................................... cartingss356" + s);
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
    }

    private Bus getBus() {
        return BusProvider.getBus();
    }
    /**
     * Loads server environment values link currency, categories, regions and images.
     */
    @Subscribe
    public void onLoadEnvironmentEvent(final LoadEnvironmentEvent event) {
        final String action = "load";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("action", action)
                    .add("code", ENV_CODE)
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(formBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String s = response.body().string();
                            System.out.println("..........................................environment "+s);
                            if (!TextUtils.isEmpty(s)) {
                                System.out.println();
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")) {
                                    UserPrefs.getInstance(mContext).setLong(UserPrefs.Key.LAST_UPDATE, System.currentTimeMillis());
                                    JSONObject jo = json.getJSONObject("data");
                                    JSONArray array = jo.getJSONArray("categories");
                                    if(array.length() > 0) {
                                        mOfidy.emptyCategories();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject ob = array.getJSONObject(i);
                                            Category c = gson.fromJson(ob.toString(), Category.class);
                                            if (c != null) {
                                                mOfidy.insertOrUpdateCategory(c);
                                                JSONArray array2 = ob.getJSONArray("categories");
                                                for (int j = 0; j < array2.length(); j++) {
                                                    String ss = array2.getString(j);
                                                    Category cc = new Gson().fromJson(ss, Category.class);
                                                    if (cc != null) {
                                                        mOfidy.insertSubCategory(cc);
                                                        //c.addSubCategory(cc);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                    array = jo.getJSONArray("regions");
                                    if(array.length() > 0) {
                                        mOfidy.emptyRegions();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject ob = array.getJSONObject(i);
                                            Region c = gson.fromJson(ob.toString(), Region.class);
                                            if (c != null) {
                                                mOfidy.insertRegion(c);
                                            }
                                        }
                                    }
                                    array = jo.getJSONArray("currencies");
                                    if(array.length() > 0) {
                                        mOfidy.emptyCurrencies();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject ob = array.getJSONObject(i);
                                            Currency c = gson.fromJson(ob.toString(), Currency.class);
                                            if (c != null) {
                                                mOfidy.insertCurrency(c);
                                            }
                                        }
                                    }
                                    array = jo.getJSONArray("images");
                                    if(array.length() > 0) {
                                        mOfidy.emptyImages();
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject ob = array.getJSONObject(i);
                                            Image c = gson.fromJson(ob.toString(), Image.class);
                                            if (c != null) {
                                                mOfidy.insertImage(c);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            flushApiEventQueue(true);
        }
    }


    /**
     * Login event method, tries to login on server.
     */
    @Subscribe
    public void onLoginStartEvent(final LoginStartEvent event) {
        System.out.println("........................................login = ");
        final String action = "login";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("action", action)
                    .add("code", CUSTOMER_CODE)
                    .add("email", event.email)
                    .add("password", event.password)
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(formBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    getBus().post(new LoginErrorEvent(new Exception("Error connecting to server"), event.email, event.initiatedByUser));
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String s = response.body().string();
                            if (!TextUtils.isEmpty(s)) {
                                System.out.println("........................................login = "+s);
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")) {
                                    Customer customer = gson.fromJson(json.toString(), Customer.class);
                                    AppState.getInstance(Ofidy.getInstance()).setBoolean(AppState.Key.LOGGED_IN, true);
                                    AppState.getInstance(Ofidy.getInstance()).setBoolean(AppState.Key.GUEST, false);
                                    if(event.initiatedByUser)
                                        getBus().post(new LoginDoneEvent(customer.getId(), customer.getSid(),
                                                customer.getCurrency(), event.email, event.password,
                                                new Gson().toJson(customer), true));
                                    else{
                                        UserPrefs prefs = UserPrefs.getInstance(mContext);
                                        prefs.setString(UserPrefs.Key.ID, customer.getId());
                                        prefs.setString(UserPrefs.Key.BODY, new Gson().toJson(customer));
                                        prefs.setString(UserPrefs.Key.EMAIL, event.email);
                                        prefs.setString(UserPrefs.Key.PASSWORD, event.password);
                                        prefs.setString(UserPrefs.Key.SID, customer.getSid());
                                        prefs.setString(UserPrefs.Key.CURRENCY, customer.getCurrency());
                                        prefs.setLong(UserPrefs.Key.LAST_LOGIN, System.currentTimeMillis());
                                    }
                                    getBus().post(new LoadShoppingCartEvent(true));
                                    getBus().post(new LoadCustomerAddressEvent(true));
                                    getBus().post(new LoadTransactionsEvent(true));
                                    getBus().post(new LoadWishlistEvent(true));
                                }
                                else{
                                    if(event.initiatedByUser) {
                                        String message = json.getString("message");
                                        getBus().post(new LoginErrorEvent(new Exception(message), message, true));
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        if(event.initiatedByUser) {
                            getBus().post(new LoginErrorEvent(new Exception("Error connecting to server"), event.email, true));
                            flushApiEventQueue(true);
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new LoginErrorEvent(e, event.email, event.initiatedByUser));
            flushApiEventQueue(true);
        }
    }


    /**
     * Login event method for guest.
     */
    @Subscribe
    public void onGuestLoginStartEvent(final GuestLoginStartEvent event) {
        final String action = "guest";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("action", action)
                    .add("code", CUSTOMER_CODE)
                    .add("prf", Ofidy.getInstance().getCurrency())
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(formBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    getBus().post(new GuestLoginErrorEvent(new Exception("Error connecting to server"), event.initiatedByUser));
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String s = response.body().string();
                            if (!TextUtils.isEmpty(s)) {
                                System.out.println("........................................guest = "+s);
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")) {
                                    Customer customer = gson.fromJson(json.toString(), Customer.class);
                                    AppState.getInstance(Ofidy.getInstance()).setBoolean(AppState.Key.LOGGED_IN, true);
                                    AppState.getInstance(Ofidy.getInstance()).setBoolean(AppState.Key.GUEST, true);
                                    UserPrefs prefs = UserPrefs.getInstance(mContext);
                                    prefs.setString(UserPrefs.Key.BODY, new Gson().toJson(customer));
                                    prefs.setString(UserPrefs.Key.ID, customer.getId());
                                    prefs.setString(UserPrefs.Key.SID, customer.getSid());
                                    prefs.setString(UserPrefs.Key.CURRENCY, customer.getCurrency());
                                    prefs.setLong(UserPrefs.Key.LAST_LOGIN, System.currentTimeMillis());
                                    getBus().post(new LoadShoppingCartEvent(true));
                                    getBus().post(new LoadCustomerAddressEvent(true));
                                    getBus().post(new GuestLoginDoneEvent(customer.getId(), customer.getSid(),
                                            customer.getCurrency(), event.initiatedByUser));
                                }
                                else{
                                    if(event.initiatedByUser) {
                                        String message = json.getString("message");
                                        getBus().post(new GuestLoginErrorEvent(new Exception(message), true));
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        if(event.initiatedByUser) {
                            getBus().post(new GuestLoginErrorEvent(new Exception("Error connecting to server"), true));
                            flushApiEventQueue(true);
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new GuestLoginErrorEvent(e, event.initiatedByUser));
            flushApiEventQueue(true);
        }
    }

    /**
     * Register event method, called for registering new user.
     */
    @Subscribe
    public void onRegisterStartEvent(final RegisterStartEvent event) {
        final String action = "register";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("action", action)
                    .add("code", CUSTOMER_CODE)
                    .add("email", event.email)
                    .add("pwd", event.password)
                    .add("fname", event.firstName)
                    .add("lname", event.lastName)
                    .add("uname", event.userName)
                    .add("memword", event.memword)
                    .add("day", String.valueOf(event.day))
                    .add("month", String.valueOf(event.month))
                    .add("year", String.valueOf(event.year))
                    .add("gender", event.gender)
                    .add("telephone", event.telephone)
                    .add("mobile", event.mobile)
                    .add("prf", event.currency)
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(formBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    getBus().post(new LoginErrorEvent(new Exception("Error connecting to server"), event.email, true));
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    try {
                        if (!response.isSuccessful()) {
                            throw new IOException("Unexpected code " + response);
                        } else {
                            String s = response.body().string();
                            System.out.println(".................................response = "+s);
                            if (!TextUtils.isEmpty(s)) {
                                JSONObject json = new JSONObject(s);
                                String message = json.getString("message");
                                if(!json.getBoolean("error")) {
                                    Customer customer = gson.fromJson(json.toString(), Customer.class);
                                    AppState.getInstance(Ofidy.getInstance()).setBoolean(AppState.Key.LOGGED_IN, true);
                                    getBus().post(new RegisterDoneEvent(customer.getId(), new Gson().toJson(customer), true));
                                }
                                else{
                                    //Log.d("APIPlug", "Error Occurred: " + message);
                                    getBus().post(new LoginErrorEvent(new Exception(message), message, true));
                                }
                            }
                        }
                    }
                    catch (Exception e){
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        getBus().post(new LoginErrorEvent(e, event.email, true));
                        flushApiEventQueue(true);
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new LoginErrorEvent(new Exception("Error connecting to server"), event.email, true));
            flushApiEventQueue(true);
        }
    }


    /**
     * Loads all categories available on ofidy.
     */
    @Subscribe
    public void onLoadCategoryEvent(final LoadCategoryEvent event) {
        final String action = "get";
        try {
            RequestBody formBody = new FormBody.Builder()
                    .add("action", action)
                    .add("code", CATEGORY_CODE)
                    .build();
            Request request = new Request.Builder()
                    .url(URL)
                    .post(formBody)
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        List<Category> categories = new ArrayList<>();
                        String s = response.body().string();
                        if(!TextUtils.isEmpty(s)) {
                            try {
                                System.out.println(".................................................... products = "+s);
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")){
                                    JSONArray array = json.getJSONArray("data");
                                    for(int i = 0; i < array.length(); i++){
                                        JSONObject ob = array.getJSONObject(i);
                                        Category c = gson.fromJson(ob.toString(), Category.class);
                                        if(c != null) {
                                            mOfidy.insertOrUpdateCategory(c);
                                            JSONArray array2 = ob.getJSONArray("categories");
                                            for (int j = 0; j < array2.length(); j++) {
                                                String ss = array2.getString(j);
                                                Category cc = new Gson().fromJson(ss, Category.class);
                                                if(cc != null) {
                                                    mOfidy.insertSubCategory(cc);
                                                    c.addSubCategory(cc);
                                                }
                                            }
                                            categories.add(c);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            getBus().post(new CategoryLoadedEvent(categories));
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            flushApiEventQueue(true);
        }
    }

    /**
     * Loads all products in a particular category
     */
    @Subscribe
    public void onLoadProductsEvent(final LoadProductsEvent event) {

        final String action = "get";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
            urlBuilder.addQueryParameter("action", action);
            urlBuilder.addQueryParameter("code", PRODUCT_CODE);
            urlBuilder.addQueryParameter("cat", String.valueOf(event.category));
            urlBuilder.addQueryParameter("ctg", String.valueOf(event.catGroup));
            urlBuilder.addQueryParameter("sct", String.valueOf(event.subCat));
            urlBuilder.addQueryParameter("rgn", event.region == 0 ? "" :String.valueOf(event.region));
            urlBuilder.addQueryParameter("mnp", event.maxPrice);
            urlBuilder.addQueryParameter("mxp", event.minPrice);
            urlBuilder.addQueryParameter("pge", String.valueOf(event.page));
            urlBuilder.addQueryParameter("prf", event.currency);
            urlBuilder.addQueryParameter("limit", String.valueOf(PRODUCTS_FETCH_LIMIT));
            urlBuilder.addQueryParameter("brd", String.valueOf(event.brand));
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    getBus().post(new ProductsLoadErrorEvent(e.getMessage(), PRODUCTS_FETCH_LIMIT, event.page + 1));
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        System.out.println("AyobamiAyeni");
                        List<Product> products = new ArrayList<>();
                        String s = response.body().string();
                        if(!TextUtils.isEmpty(s)) {
                            try {


                                System.out.println("................................products = "+s);

                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")){
                                    JSONArray array = json.getJSONArray("data");
                                    for(int i = 0; i < array.length(); i++){
                                        JSONObject ob = array.getJSONObject(i);
                                        Product p = gson.fromJson(ob.toString(), Product.class);
                                        if(p != null) {
                                            //if (p.getQuantity() > 0)
                                            products.add(p);
                                        }
                                    }
                                    getBus().post(new ProductsLoadedEvent(products, PRODUCTS_FETCH_LIMIT, event.page + 1));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                getBus().post(new ProductsLoadedEvent(products, PRODUCTS_FETCH_LIMIT, event.page + 1));
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new ProductsLoadErrorEvent(e.getMessage(), PRODUCTS_FETCH_LIMIT, event.page + 1));
            flushApiEventQueue(true);
        }
    }

    /**
     * Loads all products in special categories like trending and best sellers
     */
    @Subscribe
    public void onLoadSpecialProductsEvent(final LoadSpecialProductsEvent event) {
        final String action = "getspecial";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
            urlBuilder.addQueryParameter("action", action);
            urlBuilder.addQueryParameter("code", PRODUCT_CODE);
            urlBuilder.addQueryParameter("tab", String.valueOf(event.tab));
            urlBuilder.addQueryParameter("limit", String.valueOf(PRODUCTS_FETCH_LIMIT));
            String url = urlBuilder.build().toString();
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    getBus().post(new SpecialProductsLoadErrorEvent(event.tab, e.getMessage(), PRODUCTS_FETCH_LIMIT));
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        List<Product> products = new ArrayList<>();
                        String s = response.body().string();
                        if(!TextUtils.isEmpty(s)) {
                            System.out.println("..........................................special "+s);
                            try {
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")){
                                    JSONArray array = json.getJSONArray("data");
                                    for(int i = 0; i < array.length(); i++){
                                        JSONObject ob = array.getJSONObject(i);
                                        Product p = gson.fromJson(ob.toString(), Product.class);
                                        if(p != null) {

                                            products.add(p);
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                //getBus().post(new ProductsLoadErrorEvent(e.getMessage(), PRODUCTS_FETCH_LIMIT, event.page + 1));
                            }
                            getBus().post(new SpecialProductsLoadedEvent(products, event.tab, PRODUCTS_FETCH_LIMIT));
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new SpecialProductsLoadErrorEvent(event.tab, e.getMessage(), PRODUCTS_FETCH_LIMIT));
            flushApiEventQueue(true);
        }
    }


    /**
     * Loads all details of a particular product
     */
    @Subscribe
    public void onLoadProductEvent(final LoadProductEvent event) {
        final String action = "getid";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
            urlBuilder.addQueryParameter("action", action);
            urlBuilder.addQueryParameter("code", PRODUCT_CODE);
            urlBuilder.addQueryParameter("id", event.id);
            urlBuilder.addQueryParameter("prf", Ofidy.getInstance().getCurrency());
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        Product product = null;
                        String s = response.body().string();
                        if(!TextUtils.isEmpty(s)) {
                            try {
                                System.out.println("................................product_details = "+s);
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")){
                                    JSONObject j = json.getJSONObject("data");
                                    product = gson.fromJson(j.toString(), Product.class);
                                    JSONArray array = j.getJSONArray("sellers");
                                    for(int i = 0; i < array.length(); i++){
                                        JSONObject ob = array.getJSONObject(i);
                                        Seller p = gson.fromJson(ob.toString(), Seller.class);
                                        if(p != null) {

                                            product.addSeller(p);
                                        }
                                    }
                                    if(j.has("sizes")) {
                                        array = j.getJSONArray("sizes");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject ob = array.getJSONObject(i);
                                            Size p = gson.fromJson(ob.toString(), Size.class);
                                            if (p != null) {
                                                product.addSize(p);
                                            }
                                        }
                                    }
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                getBus().post(new ProductLoadErrorEvent(e.getMessage()));
                            }
                            if(product != null)
                                getBus().post(new ProductLoadedEvent(product));
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new ProductLoadErrorEvent(e.getMessage()));
            flushApiEventQueue(true);
        }
    }


    /**
     * Adds item to cart
     */
    @Subscribe
    public void onAddCartItemEvent(final AddCartItemEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "add";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", CART_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("prodid", event.product.getId())
                        .add("prodname", event.product.getName())
                        .add("agentid", event.seller.getAgentID())
                        .add("qty", String.valueOf(event.quantity))
                        .add("size", event.size)
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                        System.out.println(".........................................addbigError" + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println(".........................................add cart = "+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        Cart c = new Cart(event.product.getId(), event.product.getName(),
                                                event.product.getWeight(), event.seller.getCurrency(), event.quantity,
                                                event.seller.getRprice());
                                        mOfidy.insertCartItem(c);
                                        getBus().post(new CartItemAddedEvent());
                                    }
                                    else
                                        getBus().post(new AddCartItemErrorEvent(json.getString("message")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
        else
            getBus().post(new AskToLoginEvent(true));
    }

    /**
     * Adds item to wish list
     */
    @Subscribe
    public void onAddWishListItemEvent(final AddWishListItemEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "addWish";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", CUSTOMER_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("prodid", event.product.getId())
                        .add("prodname", event.product.getName())
                        .add("agent", event.seller.getAgentID())
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        Product product = new Product();
                                        product.setImage(event.product.getImage());
                                        product.setName(event.product.getName());
                                        product.setId(event.product.getId());
                                        product.setCurrency(event.seller.getCurrency());
                                        product.setPrice(event.seller.getRprice());
                                        product.setCurrency(event.seller.getCurrency());
                                        product.setAgentSeller(event.seller.getAgentID());
                                        mOfidy.insertWish(product);
                                        getBus().post(new WishItemAddedEvent());
                                    }
                                    else
                                        getBus().post(new AddCartItemErrorEvent(json.getString("message")));
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
        else
            getBus().post(new AskToLoginEvent(true));
    }



    /**
     * Load wish list of user
     */
    @Subscribe
    public void onLoadWishlistEvent(final LoadWishlistEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "wishlist";
                HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
                urlBuilder.addQueryParameter("action", action);
                urlBuilder.addQueryParameter("code", CUSTOMER_CODE);
                urlBuilder.addQueryParameter("prf", UserPrefs.getInstance(Ofidy.getInstance()).getString(UserPrefs.Key.CURRENCY));
                urlBuilder.addQueryParameter("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID));
                String url = urlBuilder.build().toString();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    System.out.println(".......................................... wishlist "+s);
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        ArrayList<Product> list = new ArrayList<>();
                                        mOfidy.emptyWishlist();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            Product p = gson.fromJson(ob.toString(), Product.class);
                                            if(p != null) {
                                                mOfidy.insertWish(p);
                                                list.add(p);
                                            }
                                        }
                                        getBus().post(new WishlistLoadedEvent(list));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new AddCartItemErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                flushApiEventQueue(true);
            }
        else
            getBus().post(new AskToLoginEvent(true));
    }


    /**
     * Removes item from cart
     */
    @Subscribe
    public void onDeleteCartItemEvent(final DeleteCartItemEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "delete";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", CART_CODE)
                        .add("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID))
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("prf", Ofidy.getInstance().getCurrency())
                        .add("tid", event.tid)
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new DeleteCartItemErrorEvent(e.getMessage()));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println("............................................delete cart "+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        getBus().post(new CartItemRemovedEvent());
                                        ArrayList<Cart> list = new ArrayList<>();
                                        mOfidy.emptyCart();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            Cart p = gson.fromJson(ob.toString(), Cart.class);
                                            if(p != null) {
                                                mOfidy.insertCartItem(p);
                                                list.add(p);
                                            }
                                        }
                                        getBus().post(new ShoppingCartLoadedEvent(list));
                                        getBus().post(new LoadShoppingCartEvent(true));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new DeleteCartItemErrorEvent(e.getMessage()));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new DeleteCartItemErrorEvent(e.getMessage()));
                flushApiEventQueue(true);
            }
        else
            getBus().post(new AskToLoginEvent(true));
    }

    /**
     * Loads all Brands in a particular category
     */
    @Subscribe
    public void onLoadBrandsEvent(final LoadBrandsEvent event) {
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
            urlBuilder.addQueryParameter("code", BRAND_CODE);
            urlBuilder.addQueryParameter("cat1", String.valueOf(event.cat1));
            urlBuilder.addQueryParameter("cat2", String.valueOf(event.cat2));
            urlBuilder.addQueryParameter("cat3", String.valueOf(event.cat3));
            urlBuilder.addQueryParameter("cat4", String.valueOf(event.cat4));
            urlBuilder.addQueryParameter("cat5", String.valueOf(event.cat5));

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    getBus().post(new BrandsLoadErrorEvent(e.getMessage(), PRODUCTS_FETCH_LIMIT, event.cat1+ 1));
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        List<Brand> brands = new ArrayList<>();
                        String s = response.body().string();
                        if(!TextUtils.isEmpty(s)) {
                            try {
                                System.out.println("................................brand = "+s);
                                JSONObject json = new JSONObject(s);

                                JSONArray array = json.getJSONArray("data");

                                for(int i = 0; i < array.length(); i++) {
                                    JSONObject ob = array.getJSONObject(i);
                                    Brand b = gson.fromJson(ob.toString(), Brand.class);
                                    if(b != null) {

                                        brands.add(b);
                                    }


                                }


                                // System.out.println("................................brand = "+b.toString());
                                getBus().post(new BrandsLoadedEvent(brands, PRODUCTS_FETCH_LIMIT, event.cat1 + 1));

                            } catch (JSONException e) {
                                e.printStackTrace();
                                // getBus().post(new BrandsLoadedEvent(brands, PRODUCTS_FETCH_LIMIT, event.cat1 + 1, s));
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            getBus().post(new BrandsLoadErrorEvent(e.getMessage(), PRODUCTS_FETCH_LIMIT, event.cat1 + 1));
            flushApiEventQueue(true);
        }
    }

    /**
     * Loads all address saved on ofidy by user
     */
    @Subscribe
    public void onLoadCustomerAddressEvent(final LoadCustomerAddressEvent event) {
        final String action = "getAddress";
        try {
            HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
            urlBuilder.addQueryParameter("action", action);
            urlBuilder.addQueryParameter("code", CUSTOMER_CODE);
            urlBuilder.addQueryParameter("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID));
            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();
            mOkHttpClient.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    e.printStackTrace();
                    //getBus().post(new SpecialProductsLoadErrorEvent(event.tab, e.getMessage(), PRODUCTS_FETCH_LIMIT));
                    Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                    flushApiEventQueue(true);
                }

                @Override
                public void onResponse(Call call, final Response response) throws IOException {
                    if (response.isSuccessful()) {
                        List<Address> products = new ArrayList<>();
                        String s = response.body().string();
                        if(!TextUtils.isEmpty(s)) {
                            try {
                                System.out.println("....................................................address = "+s);
                                JSONObject json = new JSONObject(s);
                                if(!json.getBoolean("error")){
                                    mOfidy.emptyAddress();
                                    JSONArray array = json.getJSONArray("data");
                                    for(int i = 0; i < array.length(); i++){
                                        JSONObject ob = array.getJSONObject(i);
                                        Address p = gson.fromJson(ob.toString(), Address.class);
                                        if(p != null) {
                                            mOfidy.insertAddress(p);
                                            products.add(p);
                                        }
                                    }
                                    getBus().post(new AddressLoadedEvent(products));
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            });
        }catch (Exception e){
            e.printStackTrace();
            Log.d("APIPlug", "Error Occurred: " + e.getMessage());
            //getBus().post(new SpecialProductsLoadErrorEvent(event.tab, e.getMessage(), PRODUCTS_FETCH_LIMIT));
            flushApiEventQueue(true);
        }
    }

    /**
     * Adds new address or updates existing address
     */
    @Subscribe
    public void onAddEditAddressEventEvent(final AddEditAddressEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                String action = "addAddress";
                if(event.edit)
                    action = "editAddress";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", CUSTOMER_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("addr1", event.addrLine1)
                        .add("addr2", event.addrLine2)
                        .add("addr3", event.area)
                        .add("addrdesc", event.desc)
                        .add("city", event.city)
                        .add("state", event.state)
                        .add("country", event.country)
                        .add("postcode", event.postcode)
                        .add("deladdr", event.del)
                        .add("coraddr", event.cor)
                        .add("addrtype", String.valueOf(event.addressType))
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new AddEditAddressStatusEvent(e.getMessage(), false));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println(".....................................................address ish = "+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    String id = null;
                                    if(!json.getBoolean("error")){
                                        mOfidy.emptyAddress();
                                        List<Address> products = new ArrayList<>();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            Address p = gson.fromJson(ob.toString(), Address.class);
                                            if(p != null && !TextUtils.isEmpty(p.getAddressLine1())) {
                                                mOfidy.insertAddress(p);
                                                products.add(p);
                                            }
                                        }
                                        getBus().post(new AddressLoadedEvent(products));
                                        getBus().post(new AddEditAddressStatusEvent("Address added successfully", true));
                                        getBus().post(new LoadCustomerAddressEvent(true));
                                    }
                                    else{
                                        getBus().post(new AddEditAddressStatusEvent(json.getString("message"), false));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new AddEditAddressStatusEvent(e.getMessage(), false));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new AddEditAddressStatusEvent(e.getMessage(), false));
                flushApiEventQueue(true);
            }
        else
            getBus().post(new AskToLoginEvent(true));
    }

    /**
     * Adds address to server by guest
     */
    @Subscribe
    public void onAddGuestAddressEventEvent(final AddGuestAddressEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                String action = "addGuestAddress";
                RequestBody formBody = new FormBody.Builder()
                        .add("action", action)
                        .add("code", CUSTOMER_CODE)
                        .add("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID))
                        .add("addr1", event.addrLine1)
                        .add("addr2", event.addrLine2)
                        .add("addr3", event.area)
                        .add("addrdesc", event.desc)
                        .add("city", event.city)
                        .add("state", event.state)
                        .add("country", event.country)
                        .add("postcode", event.postcode)
                        .add("addrtype", String.valueOf(event.addressType))
                        .add("event", event.email)
                        .add("fname", event.fname)
                        .add("lname", event.lname)
                        .add("phone", event.phone)
                        .build();
                Request request = new Request.Builder()
                        .url(URL)
                        .post(formBody)
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        getBus().post(new AddEditAddressStatusEvent(e.getMessage(), false));
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            System.out.println(".....................................................guest address ish = "+s);
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    JSONObject json = new JSONObject(s);
                                    String id = null;
                                    if(!json.getBoolean("error")){
                                        mOfidy.emptyAddress();
                                        List<Address> products = new ArrayList<>();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            //Address p = gson.fromJson(ob.toString(), Address.class);
                                            id = ob.getString("id");
//                                            if(p != null && !TextUtils.isEmpty(p.getAddressLine1())) {
//                                                mOfidy.insertAddress(p);
//                                                products.add(p);
//                                            }
                                        }
                                        if(!TextUtils.isEmpty(id)) {
                                            Address ad = new Address();
                                            ad.setAddressLine1(event.addrLine1);
                                            ad.setAddressLine2(event.addrLine2);
                                            ad.setAddressLine3(event.area);
                                            ad.setCity(event.city);
                                            ad.setState(event.state);
                                            ad.setCountry(event.country);
                                            ad.setPostcode(event.postcode);
                                            ad.setId(id);
                                            mOfidy.insertAddress(ad);
                                            products.add(ad);
                                        }
                                        Customer c = new Customer();
                                        c.setEmail(event.email);
                                        c.setFirstName(event.fname);
                                        c.setLastName(event.lname);
                                        c.setMobile(event.phone);
                                        UserPrefs.getInstance(mContext).setString(UserPrefs.Key.BODY, gson.toJson(c));
                                        getBus().post(new AddressLoadedEvent(products));
                                        getBus().post(new AddEditAddressStatusEvent("Address added successfully", true));
                                        getBus().post(new LoadCustomerAddressEvent(true));
                                    }
                                    else{
                                        getBus().post(new AddEditAddressStatusEvent(json.getString("message"), false));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                    getBus().post(new AddEditAddressStatusEvent(e.getMessage(), false));
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                getBus().post(new AddEditAddressStatusEvent(e.getMessage(), false));
                flushApiEventQueue(true);
            }
        else
            getBus().post(new AskToLoginEvent(true));
    }

    /**
     * Loads transactions
     */
    @Subscribe
    public void onLoadTransactionsEvent(final LoadTransactionsEvent event) {
        if(AppState.getInstance(mContext).getBoolean(AppState.Key.LOGGED_IN))
            try {
                final String action = "transactions";
                HttpUrl.Builder urlBuilder = HttpUrl.parse(URL).newBuilder();
                urlBuilder.addQueryParameter("action", action);
                urlBuilder.addQueryParameter("code", CUSTOMER_CODE);
                urlBuilder.addQueryParameter("id", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.ID));
                urlBuilder.addQueryParameter("sid", UserPrefs.getInstance(mContext).getString(UserPrefs.Key.SID));
                urlBuilder.addQueryParameter("prf", Ofidy.getInstance().getCurrency());
                String url = urlBuilder.build().toString();
                Request request = new Request.Builder()
                        .url(url)
                        .get()
                        .build();
                mOkHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        e.printStackTrace();
                        Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                        flushApiEventQueue(true);
                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        if (response.isSuccessful()) {
                            String s = response.body().string();
                            if(!TextUtils.isEmpty(s)) {
                                try {
                                    System.out.println("........................................trans = "+s);
                                    JSONObject json = new JSONObject(s);
                                    if(!json.getBoolean("error")){
                                        ArrayList<Transaction> list = new ArrayList<>();
                                        mOfidy.emptyTransactions();
                                        JSONArray array = json.getJSONArray("data");
                                        for(int i = 0; i < array.length(); i++){
                                            JSONObject ob = array.getJSONObject(i);
                                            Transaction p = gson.fromJson(ob.toString(), Transaction.class);
                                            if(p != null) {
                                                mOfidy.insertTransaction(p);
                                                list.add(p);
                                            }
                                        }
                                        getBus().post(new TransactionsLoadedEvent(list));
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        }
                    }
                });
            }catch (Exception e){
                e.printStackTrace();
                Log.d("APIPlug", "Error Occurred: " + e.getMessage());
                flushApiEventQueue(true);
            }
        else
            getBus().post(new AskToLoginEvent(true));
    }

    /**
     * Called for logout
     */
    @Subscribe
    public void onLogoutEvent(LogoutEvent event) {
        UserPrefs.getInstance(Ofidy.getInstance()).clear(UserPrefs.Key.BODY);
        UserPrefs.getInstance(Ofidy.getInstance()).clear(UserPrefs.Key.EMAIL);
        UserPrefs.getInstance(Ofidy.getInstance()).clear(UserPrefs.Key.PASSWORD);
        UserPrefs.getInstance(Ofidy.getInstance()).clear(UserPrefs.Key.SID);
        UserPrefs.getInstance(Ofidy.getInstance()).clear(UserPrefs.Key.ID);
        AppState.getInstance(Ofidy.getInstance()).setBoolean(AppState.Key.LOGGED_IN, false);
        //AppState.getInstance(OfidiApp.getInstance()).setBoolean(AppState.Key.GUEST, true);
        mOfidy.emptyCart();
        mOfidy.emptyTransactions();
        mOfidy.emptyAddress();
        mOfidy.emptyWishlist();
        mApiEventQueue.clear();
        getBus().post(new LogoutStatusEvent(true, false));
        getBus().post(new GuestLoginStartEvent(false));
    }


}
