package ru.scorpio92.excalc;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Space;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    EditText inputField1;
    EditText inputField2;
    Button currencyButton1;
    Button currencyButton2;
    Button purchaseButton;
    Button saleButton;

    final String selectAllCurrency = "SELECT * FROM " + MainDB.CURRENCY_EXCHANGE_TABLE;
    int activeInputFieldID = -1;
    int activeOperationID = -1;
    String inputField1ExchangeCurrency;
    String inputField2ExchangeCurrency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        init();

        //Log.w("test", String.format("%(.4f", BigDecimal.valueOf(1000/64.10)));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void init() {
        ///GUI

        //поля ввода суммы
        inputField1 = (EditText) findViewById(R.id.inputField1);
        inputField2 = (EditText) findViewById(R.id.inputField2);

        inputField1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    Log.w("setOnFocusChangeListener", "inputField1 get focus");
                    activeInputFieldID = inputField1.getId();
                }
            }
        });

        inputField2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if(b) {
                    Log.w("setOnFocusChangeListener", "inputField2 get focus");
                    activeInputFieldID = inputField2.getId();
                }
            }
        });

        inputField1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(activeInputFieldID == inputField1.getId()) {
                    Log.w("TextWatcher", "inputField1 afterTextChanged " + s);
                    if (s.length() > 0) {
                        inputField2.setText(String.format("%(.4f", calc(activeOperationID, Double.parseDouble(s.toString().replace(",", ".")), inputField2ExchangeCurrency)));
                    } else {
                        inputField2.setText("0");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.w("TextWatcher", "inputField1 beforeTextChanged");
                //activeInputFieldID = inputField1.getId();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.w("TextWatcher", "inputField1 onTextChanged");
            }
        });

        inputField2.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(activeInputFieldID == inputField2.getId()) {
                    Log.w("TextWatcher", "inputField2 afterTextChanged " + s);
                    if (s.length() > 0) {
                        inputField1.setText(String.format("%(.4f", calc(activeOperationID, Double.parseDouble(s.toString().replace(",", ".")), inputField1ExchangeCurrency)));
                    } else {
                        inputField1.setText("0");
                    }
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                //Log.w("TextWatcher", "inputField2 beforeTextChanged");
                //activeInputFieldID = inputField2.getId();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //Log.w("TextWatcher", "inputField2 onTextChanged");
            }
        });


        //кнопки выбора курса валют, всплывающее меню для кнопок выбора курса валют
        currencyButton1 = (Button) findViewById(R.id.currencyButton1);
        currencyButton2 = (Button) findViewById(R.id.currencyButton2);

        currencyButton1.setText(Constants.CURRENCY_RUB);
        currencyButton2.setText(Constants.CURRENCY_USD);
        inputField1ExchangeCurrency = Constants.CURRENCY_RUB;
        inputField2ExchangeCurrency = Constants.CURRENCY_USD;

        currencyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInputCurrency(view, currencyButton2);
            }
        });

        currencyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setInputCurrency(view, currencyButton1);
            }
        });


        //кнопки продажа/покупка
        purchaseButton = (Button) findViewById(R.id.purchaseButton);
        saleButton = (Button) findViewById(R.id.saleButton);

        //по-умолчанию - операция покупка
        purchaseButton.setBackground(getResources().getDrawable(R.drawable.button_pressed_left_0dp_round));
        activeOperationID = Constants.OPERATION_PURCHASE;

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saleButton.setBackground(getResources().getDrawable(R.drawable.button_pressed_left_0dp_round));
                purchaseButton.setBackground(getResources().getDrawable(R.drawable.button_normal_right_0dp_round));
            }
        });
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                purchaseButton.setBackground(getResources().getDrawable(R.drawable.button_pressed_right_0dp_round));
                saleButton.setBackground(getResources().getDrawable(R.drawable.button_normal_left_0dp_round));
            }
        });

        ///DB
        if(!initDB()) {
            Toast.makeText(getApplicationContext(), getString(R.string.ooops), Toast.LENGTH_SHORT).show();
            finish();
        }

        ///тут должно быть получение актуального курса с сервера
        //http://m.express-bank.ru/rates

        ///тут берем значения из БД и вставляем в GUI
        createCurrencyTable();

    }

    //проверка и создание при необходимости БД приложения
    boolean initDB() {
        SQLiteDatabase sdb = null;
        try {
            String appFolder = getPackageManager().getPackageInfo(getPackageName(), 0).applicationInfo.dataDir;
            //Log.w("test", appFolder);
            if(!new File(appFolder + "/databases/" + MainDB.MAIN_DATABASE_NAME).exists()) {
                MainDB mAccountDB = new MainDB(this);
                sdb = mAccountDB.getReadableDatabase();
                Log.w("initDB", "DB was created");

                //заглушки
                ContentValues newValues = new ContentValues();

                //записываем настройку проверки на первый запуск
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN, Constants.CURRENCY_USD);
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_PURCHASE_COLUMN, "61.50");
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_SALE_COLUMN, "63.10");
                //sdb.insert(MainDB.CURRENCY_EXCHANGE_TABLE, null, newValues);
                DBUtils.insert_update_delete(this, MainDB.CURRENCY_EXCHANGE_TABLE, newValues, null, DBUtils.ACTION_INSERT);
                newValues.clear();

                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN, Constants.CURRENCY_EUR);
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_PURCHASE_COLUMN, "68.90");
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_SALE_COLUMN, "70.50");
                //sdb.insert(MainDB.CURRENCY_EXCHANGE_TABLE, null, newValues);
                DBUtils.insert_update_delete(this, MainDB.CURRENCY_EXCHANGE_TABLE, newValues, null, DBUtils.ACTION_INSERT);
                newValues.clear();

                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN, Constants.CURRENCY_CNY);
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_PURCHASE_COLUMN, "92.00");
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_SALE_COLUMN, "95.00");
                //sdb.insert(MainDB.CURRENCY_EXCHANGE_TABLE, null, newValues);
                DBUtils.insert_update_delete(this, MainDB.CURRENCY_EXCHANGE_TABLE, newValues, null, DBUtils.ACTION_INSERT);
                newValues.clear();

                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN, Constants.CURRENCY_JPY);
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_PURCHASE_COLUMN, "50.00");
                newValues.put(MainDB.CURRENCY_EXCHANGE_TABLE_SALE_COLUMN, "61.10");
                //sdb.insert(MainDB.CURRENCY_EXCHANGE_TABLE, null, newValues);
                DBUtils.insert_update_delete(this, MainDB.CURRENCY_EXCHANGE_TABLE, newValues, null, DBUtils.ACTION_INSERT);
                newValues.clear();
            }
            return true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if(sdb != null)
                sdb.close();
        }
        return false;
    }

    //создание таблицы курса валют по значениям из БД
    boolean createCurrencyTable() {
        try {
            ArrayList<String> als = new ArrayList<String>();
            als.add(MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN);
            als.add(MainDB.CURRENCY_EXCHANGE_TABLE_PURCHASE_COLUMN);
            als.add(MainDB.CURRENCY_EXCHANGE_TABLE_SALE_COLUMN);
            ArrayList<String> result = DBUtils.select_from_db(MainActivity.this, selectAllCurrency, als, true);

            Log.w("createCurrencyTable", Integer.toString(result.size()/3));

            TableLayout tl = (TableLayout) findViewById(R.id.baseTable);

            for(int i=0; i<result.size(); i=i+3) {
                TableRow tr = new TableRow(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(layoutParams);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.setPadding(0, 20, 0, 0);

                TextView currency = new TextView(this);
                currency.setText(result.get(i));
                currency.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                tr.addView(currency);

                TextView purchase = new TextView(this);
                purchase.setText(result.get(i+1));
                purchase.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                purchase.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.addView(purchase);

                TextView sale = new TextView(this);
                sale.setText(result.get(i+2));
                sale.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                sale.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.addView(sale);

                Space space = new Space(this);
                space.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                tr.addView(space);

                tl.addView(tr, new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.WRAP_CONTENT));
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    //PopupMenu курса валют
    void setInputCurrency(final View currentButtonView, final Button anotherButton) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, currentButtonView);
        popupMenu.inflate(R.menu.currency_list);
        Menu menu = popupMenu.getMenu();

        ArrayList<String> als = new ArrayList<String>();
        als.add(MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN);
        ArrayList<String> result = DBUtils.select_from_db(MainActivity.this, selectAllCurrency, als, true);

        for (String item:result) {
            menu.add(item);
        }

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Button currentButton = (Button) currentButtonView;
                //если выбраны одинаковые валюты, меняем их местами
                if(item.getTitle().equals(anotherButton.getText())) {
                    anotherButton.setText(currentButton.getText());
                }
                currentButton.setText(item.getTitle());
                if(currentButton.getId() == currencyButton1.getId()) {
                    inputField1ExchangeCurrency = item.getTitle().toString();
                } else {
                    inputField2ExchangeCurrency = item.getTitle().toString();
                }

                return true;
            }
        });

        popupMenu.show();
    }

    void sendToCalc() {

    }

    //расчет курса обмена
    double calc(int operation, double sum, String currency) {
        double resultSum = -1;
        String query = selectAllCurrency + " WHERE " + MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN + "=" + "'" + currency + "'";
        ArrayList<String> result;
        try {
            ArrayList<String> als = new ArrayList<String>();

            switch (operation) {
                case Constants.OPERATION_PURCHASE:
                    als.add(MainDB.CURRENCY_EXCHANGE_TABLE_SALE_COLUMN);
                    result = DBUtils.select_from_db(MainActivity.this, query, als, true);
                    Log.w("calc", "OPERATION_SALE: currency: " + currency + " rate: " + result.get(0));
                    if (currency.equals(Constants.CURRENCY_RUB)) {
                        resultSum = sum*Double.parseDouble(result.get(0));
                    } else {
                        resultSum = sum/Double.parseDouble(result.get(0));
                    }
                    break;
                case Constants.OPERATION_SALE:
                    als.add(MainDB.CURRENCY_EXCHANGE_TABLE_PURCHASE_COLUMN);
                    result = DBUtils.select_from_db(MainActivity.this, query, als, true);
                    Log.w("calc", "OPERATION_PURCHASE: currency: " + currency + " rate: " + result.get(0));
                    if (currency.equals(Constants.CURRENCY_RUB)) {
                        resultSum = sum*Double.parseDouble(result.get(0));
                    } else {
                        resultSum = sum/Double.parseDouble(result.get(0));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultSum;
    }
}
