package ru.scorpio92.excalc;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.PopupMenu;

public class MainActivity extends AppCompatActivity {

    Button currencyButton1;
    Button currencyButton2;
    Button purchaseButton;
    Button saleButton;

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


        //кнопки выбора курса валют, всплывающее меню для кнопок выбора курса валют
        currencyButton1 = (Button) findViewById(R.id.currencyButton1);
        currencyButton2 = (Button) findViewById(R.id.currencyButton2);

        currencyButton1.setText(getString(R.string.currency_rub));
        currencyButton2.setText(getString(R.string.currency_usd));

        currencyButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrency(view, currencyButton2);
            }
        });

        currencyButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setCurrency(view, currencyButton1);
            }
        });



        //кнопки продажа/покупка
        purchaseButton = (Button) findViewById(R.id.purchaseButton);
        saleButton = (Button) findViewById(R.id.saleButton);
        saleButton.setBackground(getResources().getDrawable(R.drawable.button_pressed_left_0dp_round));
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


    //PopupMenu курса валют
    private void setCurrency(final View currentButtonView, final Button anotherButton) {
        PopupMenu popupMenu = new PopupMenu(MainActivity.this, currentButtonView);
        popupMenu.inflate(R.menu.currency_list);
        //Menu menu = popupMenu.getMenu();

        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                /*switch (item.getItemId()) {

                    case R.id.currency_list_rub:
                        ((Button) view).setText(getString(R.string.currency_rub));
                        return true;

                    case R.id.currency_list_usd:
                        ((Button) view).setText(getString(R.string.currency_usd));
                        return true;

                    case R.id.currency_list_eur:
                        ((Button) view).setText(getString(R.string.currency_eur));
                        return true;

                    case R.id.currency_list_cny:
                        ((Button) view).setText(getString(R.string.currency_cny));
                        return true;

                    case R.id.currency_list_jpy:
                        ((Button) view).setText(getString(R.string.currency_jpy));
                        return true;

                    default:
                        return false;
                }*/
                Button currentButton = (Button) currentButtonView;
                //если выбраны одинаковые валюты, меняем их местами
                if(item.getTitle().equals(anotherButton.getText())) {
                    anotherButton.setText(currentButton.getText());
                    currentButton.setText(item.getTitle());
                } else {
                    currentButton.setText(item.getTitle());
                }
                return true;
            }
        });

        popupMenu.show();
    }
}
