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
    /*Button currencyButton1;
    Button currencyButton2;*/
    TextView currency1;
    TextView currency2;
    Button purchaseButton;
    Button saleButton;

    final String selectAllCurrency = "SELECT * FROM " + MainDB.CURRENCY_EXCHANGE_TABLE;

    int activeInputFieldID;
    int activeOperation;
    /*String inputField1ExchangeCurrency;
    String inputField2ExchangeCurrency;*/
    String activeExchangeCurrency;
    int activeExchangeCurrencyButtonID;

    ArrayList<Integer> currencyButtonsIDArray;
    ArrayList<Integer> purchaseTextIDArray;
    ArrayList<Integer> saleTextIDArray;

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

        //ловим событие фокусировки на поле ввода - записываем Id поля
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

        //слушаем ввод
        inputField1.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
                if(activeInputFieldID == inputField1.getId()) {
                    Log.w("TextWatcher", "inputField1 afterTextChanged " + s);
                    /*if (s.length() > 0) {
                        inputField2.setText(String.format("%(." + Constants.SUM_ROUND + "f", calc(activeOperation, Double.parseDouble(s.toString().replace(",", ".")), activeExchangeCurrency)));
                    } else {
                        inputField2.setText("");
                    }*/
                    calc();
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
                    /*if (s.length() > 0) {
                        inputField1.setText(String.format("%(." + Constants.SUM_ROUND + "f", calc(activeOperation, Double.parseDouble(s.toString().replace(",", ".")), Constants.CURRENCY_RUB)));
                    } else {
                        inputField1.setText("");
                    }*/
                    calc();
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
        /*currencyButton1 = (Button) findViewById(R.id.currencyButton1);
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
        });*/


        //валюта
        currency1 = (TextView) findViewById(R.id.currency1);
        currency2 = (TextView) findViewById(R.id.currency2);


        //кнопки продажа/покупка
        saleButton = (Button) findViewById(R.id.saleButton);
        purchaseButton = (Button) findViewById(R.id.purchaseButton);

        saleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currency1.setText(activeExchangeCurrency);
                currency2.setText(Constants.CURRENCY_RUB);

                activeOperation = Constants.OPERATION_SALE;
                saleButton.setTextColor(getResources().getColor(R.color.colorTextLight));
                purchaseButton.setTextColor(getResources().getColor(R.color.colorTextNormal));
                saleButton.setBackground(getResources().getDrawable(R.drawable.button_pressed_right_0dp_round));
                purchaseButton.setBackground(getResources().getDrawable(R.drawable.button_normal_left_0dp_round));

                //выделяем курсы валют цветом
                try {
                    for (int id : saleTextIDArray) {
                        TextView t = (TextView) MainActivity.this.findViewById(id);
                        t.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    for (int id : purchaseTextIDArray) {
                        TextView t = (TextView) MainActivity.this.findViewById(id);
                        t.setTextColor(getResources().getColor(R.color.colorTextDark));
                    }
                } catch (Exception e) {e.printStackTrace();}
                calc();
            }
        });
        purchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                currency1.setText(Constants.CURRENCY_RUB);
                currency2.setText(activeExchangeCurrency);

                activeOperation = Constants.OPERATION_PURCHASE;
                purchaseButton.setTextColor(getResources().getColor(R.color.colorTextLight));
                saleButton.setTextColor(getResources().getColor(R.color.colorTextNormal));
                purchaseButton.setBackground(getResources().getDrawable(R.drawable.button_pressed_left_0dp_round));
                saleButton.setBackground(getResources().getDrawable(R.drawable.button_normal_right_0dp_round));

                //выделяем курсы валют цветом
                try {
                    for (int id : purchaseTextIDArray) {
                        TextView t = (TextView) MainActivity.this.findViewById(id);
                        t.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
                    }
                    for (int id : saleTextIDArray) {
                        TextView t = (TextView) MainActivity.this.findViewById(id);
                        t.setTextColor(getResources().getColor(R.color.colorTextDark));
                    }
                } catch (Exception e) {e.printStackTrace();}
                calc();
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

        setDefaults();
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

            currencyButtonsIDArray = new ArrayList<>();
            purchaseTextIDArray = new ArrayList<>();
            saleTextIDArray = new ArrayList<>();

            for(int i=0; i<result.size(); i=i+3) {
                TableRow tr = new TableRow(this);
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT);
                tr.setLayoutParams(layoutParams);
                tr.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.setPadding(0, 20, 0, 0);

                /*TextView currency = new TextView(this);
                currency.setText(result.get(i));
                currency.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                tr.addView(currency);*/

                //ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(this, R.style.ButtonStyle);
                final Button currency = new Button(this);
                currency.setText(result.get(i));
                currency.setBackgroundResource(R.drawable.custom_button_style);
                //currency.setTextAppearance(this,R.style.ButtonStyle);
                //currency.setMinHeight(10);
                //currency.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10);
                currency.setId(i);
                currencyButtonsIDArray.add(i);
                if(result.get(i).equals(Constants.CURRENCY_DEFAULT))
                    activeExchangeCurrencyButtonID = i;
                currency.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                tr.addView(currency);

                currency.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        activeExchangeCurrency = ((Button) view).getText().toString();

                        if(activeOperation == Constants.OPERATION_PURCHASE) {
                            currency1.setText(Constants.CURRENCY_RUB);
                            currency2.setText(activeExchangeCurrency);
                        } else {
                            currency1.setText(activeExchangeCurrency);
                            currency2.setText(Constants.CURRENCY_RUB);
                        }

                        for (int id: currencyButtonsIDArray) {
                            Button b = (Button) MainActivity.this.findViewById(id);
                            b.setTextColor(getResources().getColor(R.color.colorTextNormal));
                            b.setBackgroundResource(R.drawable.button_normal);
                        }
                        currency.setTextColor(getResources().getColor(R.color.colorTextLight));
                        currency.setBackgroundResource(R.drawable.button_pressed);
                        calc();
                        //Toast.makeText(getApplicationContext(), activeExchangeCurrency, Toast.LENGTH_SHORT).show();
                    }
                });

                TextView sale = new TextView(this);
                sale.setText(result.get(i+1));
                sale.setId(i + result.size()*2);
                saleTextIDArray.add(i + result.size()*2);
                sale.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                sale.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.addView(sale);

                TextView purchase = new TextView(this);
                purchase.setText(result.get(i+2));
                purchase.setId(i + result.size());
                purchaseTextIDArray.add(i + result.size());
                purchase.setLayoutParams(new TableRow.LayoutParams(0, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                purchase.setGravity(Gravity.CENTER_HORIZONTAL);
                tr.addView(purchase);

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

    //расчет курса обмена
    //double calc(int operation, double sum, String currency) {
    void calc() {
        //String currency;
        double sum;
        double resultSum = -1;

        try {
            //NumberFormat format = NumberFormat.getInstance(Locale.ENGLISH);
            if (activeInputFieldID == inputField1.getId()) {
                //Number number = format.parse(inputField1.getText().toString());
                //sum = number.doubleValue();
                sum = Double.parseDouble(inputField1.getText().toString());
            } else {
                //Number number = format.parse(inputField2.getText().toString());
                //sum = number.doubleValue();
                sum = Double.parseDouble(inputField2.getText().toString());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        String query = selectAllCurrency + " WHERE " + MainDB.CURRENCY_EXCHANGE_TABLE_CURRENCY_COLUMN + "=" + "'" + activeExchangeCurrency + "'";
        ArrayList<String> result;
        try {
            ArrayList<String> als = new ArrayList<String>();

            switch (activeOperation) {
                case Constants.OPERATION_PURCHASE:
                    als.add(MainDB.CURRENCY_EXCHANGE_TABLE_SALE_COLUMN);
                    result = DBUtils.select_from_db(MainActivity.this, query, als, true);
                    Log.w("calc", "OPERATION_PURCHASE: currency: " + activeExchangeCurrency + " rate: " + result.get(0));
                    if (activeInputFieldID == inputField1.getId()) {
                        resultSum = sum/Double.parseDouble(result.get(0));
                    } else {
                        resultSum = sum*Double.parseDouble(result.get(0));
                    }
                    break;
                case Constants.OPERATION_SALE:
                    als.add(MainDB.CURRENCY_EXCHANGE_TABLE_PURCHASE_COLUMN);
                    result = DBUtils.select_from_db(MainActivity.this, query, als, true);
                    Log.w("calc", "OPERATION_SALE: currency: " + activeExchangeCurrency + " rate: " + result.get(0));
                    if (activeInputFieldID == inputField1.getId()) {
                        resultSum = sum*Double.parseDouble(result.get(0));
                    } else {
                        resultSum = sum/Double.parseDouble(result.get(0));
                    }
                    break;
            }

            if(activeExchangeCurrency.equals(Constants.CURRENCY_CNY)) {
                if(activeOperation == Constants.OPERATION_PURCHASE) {
                    resultSum *= 10;
                } else {
                    resultSum /= 10;
                }
            }

            if (activeInputFieldID == inputField1.getId()) {
                inputField2.setText(String.format("%(." + Constants.SUM_ROUND + "f", resultSum).replace(",", "."));
            } else {
                inputField1.setText(String.format("%(." + Constants.SUM_ROUND + "f", resultSum).replace(",", "."));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        //return resultSum;
    }

    void setDefaults() {

        currency1.setText(Constants.CURRENCY_RUB);
        currency2.setText(Constants.CURRENCY_DEFAULT);

        //по-умолчанию - операция покупка
        purchaseButton.setTextColor(getResources().getColor(R.color.colorTextLight));
        purchaseButton.setBackground(getResources().getDrawable(R.drawable.button_pressed_left_0dp_round));
        activeOperation = Constants.OPERATION_PURCHASE;
        //выделяем курсы валют цветом
        try {
            for (int id : purchaseTextIDArray) {
                TextView t = (TextView) MainActivity.this.findViewById(id);
                t.setTextColor(getResources().getColor(R.color.colorPrimaryDark));
            }
            for (int id : saleTextIDArray) {
                TextView t = (TextView) MainActivity.this.findViewById(id);
                t.setTextColor(getResources().getColor(R.color.colorTextDark));
            }
        } catch (Exception e) {e.printStackTrace();}

        //валюта по-умолчанию
        activeExchangeCurrency = Constants.CURRENCY_DEFAULT;

        try {
            Button b = (Button) findViewById(activeExchangeCurrencyButtonID);
            b.setTextColor(getResources().getColor(R.color.colorTextLight));
            b.setBackgroundResource(R.drawable.button_pressed);
        } catch (Exception e) {e.printStackTrace();}
    }
}
