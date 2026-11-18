
package com.nullbyte.ckcookie;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.graphics.Typeface;
import android.view.Gravity;

import java.text.NumberFormat;
import java.util.Locale;

public class MainActivity extends Activity {

    private double cookies = 0;
    private double cookiesPerSecond = 0;

    private int grandmaCount = 0;
    private int farmCount = 0;
    private int factoryCount = 0;
    private int mineCount = 0;

    private double grandmaCost = 25;
    private double farmCost = 100;
    private double factoryCost = 500;
    private double mineCost = 2000;

    private TextView counter;
    private TextView cpsText;
    private TextView statsText;
    private TextView upgradeText;

    private final Handler handler = new Handler();
    private final NumberFormat format = NumberFormat.getIntegerInstance(Locale.UK);

    private final Runnable gameLoop = new Runnable() {
        @Override
        public void run() {
            cookies += cookiesPerSecond;
            updateUI();
            handler.postDelayed(this, 1000);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        loadGame();
        createInterface();
        updateUI();

        Intent service = new Intent(this, CookieService.class);

        if (Build.VERSION.SDK_INT >= 26) {
            startForegroundService(service);
        } else {
            startService(service);
        }
    }

    private void createInterface() {
        ScrollView scroll = new ScrollView(this);

        LinearLayout root = new LinearLayout(this);
        root.setOrientation(LinearLayout.VERTICAL);
        root.setPadding(28, 35, 28, 35);
        root.setGravity(Gravity.CENTER_HORIZONTAL);

        root.setBackgroundColor(0xFF21180F);

        scroll.addView(root);
        setContentView(scroll);

        TextView title = new TextView(this);
        title.setText("COOKIE CLICKER");
        title.setTextSize(30);
        title.setTypeface(null, Typeface.BOLD);
        title.setTextColor(0xFFFFD180);
        title.setGravity(Gravity.CENTER);

        root.addView(title);

        TextView subtitle = new TextView(this);
        subtitle.setText("Build your cookie empire!");
        subtitle.setTextSize(15);
        subtitle.setTextColor(0xFFFFFFFF);
        subtitle.setGravity(Gravity.CENTER);

        root.addView(subtitle);

        counter = new TextView(this);
        counter.setTextSize(32);
        counter.setTypeface(null, Typeface.BOLD);
        counter.setTextColor(0xFFFFFFFF);
        counter.setGravity(Gravity.CENTER);
        counter.setPadding(0, 35, 0, 5);

        root.addView(counter);

        cpsText = new TextView(this);
        cpsText.setTextSize(17);
        cpsText.setTextColor(0xFFFFD180);
        cpsText.setGravity(Gravity.CENTER);

        root.addView(cpsText);

        Button cookieButton = new Button(this);
        cookieButton.setText("🍪");
        cookieButton.setTextSize(75);
        cookieButton.setAllCaps(false);
        cookieButton.setBackgroundTintList(
            android.content.res.ColorStateList.valueOf(0xFFFFD180)
        );

        LinearLayout.LayoutParams cookieParams =
            new LinearLayout.LayoutParams(
                280,
                220
            );

        cookieParams.setMargins(0, 30, 0, 30);

        root.addView(cookieButton, cookieParams);

        cookieButton.setOnClickListener(view -> {
            cookies++;
            updateUI();
            saveGame();
        });

        TextView upgradesTitle = new TextView(this);
        upgradesTitle.setText("UPGRADES");
        upgradesTitle.setTextSize(24);
        upgradesTitle.setTypeface(null, Typeface.BOLD);
        upgradesTitle.setTextColor(0xFFFFD180);
        upgradesTitle.setPadding(0, 15, 0, 15);

        root.addView(upgradesTitle);

        Button grandmaButton = createUpgradeButton(
            root,
            "👵 Grandma",
            "Produces 1 cookie per second"
        );

        grandmaButton.setOnClickListener(view -> buyUpgrade(0));

        Button farmButton = createUpgradeButton(
            root,
            "🌾 Cookie Farm",
            "Produces 5 cookies per second"
        );

        farmButton.setOnClickListener(view -> buyUpgrade(1));

        Button factoryButton = createUpgradeButton(
            root,
            "🏭 Cookie Factory",
            "Produces 20 cookies per second"
        );

        factoryButton.setOnClickListener(view -> buyUpgrade(2));

        Button mineButton = createUpgradeButton(
            root,
            "⛏️ Cookie Mine",
            "Produces 100 cookies per second"
        );

        mineButton.setOnClickListener(view -> buyUpgrade(3));

        upgradeText = new TextView(this);
        upgradeText.setTextSize(15);
        upgradeText.setTextColor(0xFFFFFFFF);
        upgradeText.setPadding(0, 20, 0, 15);

        root.addView(upgradeText);

        statsText = new TextView(this);
        statsText.setTextSize(16);
        statsText.setTextColor(0xFFFFD180);
        statsText.setPadding(0, 15, 0, 15);

        root.addView(statsText);

        Button saveButton = new Button(this);
        saveButton.setText("Save Game");

        root.addView(saveButton);

        saveButton.setOnClickListener(view -> {
            saveGame();
            saveButton.setText("Saved!");
        });
    }

    private Button createUpgradeButton(
        LinearLayout root,
        String name,
        String description
    ) {
        Button button = new Button(this);

        button.setText(name + "\n" + description);
        button.setTextSize(14);
        button.setAllCaps(false);

        LinearLayout.LayoutParams params =
            new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            );

        params.setMargins(0, 7, 0, 7);

        root.addView(button, params);

        return button;
    }

    private void buyUpgrade(int type) {
        double cost;
        double production;

        switch (type) {
            case 0:
                cost = grandmaCost;
                production = 1;
                break;
            case 1:
                cost = farmCost;
                production = 5;
                break;
            case 2:
                cost = factoryCost;
                production = 20;
                break;
            default:
                cost = mineCost;
                production = 100;
                break;
        }

        if (cookies < cost) {
            return;
        }

        cookies -= cost;
        cookiesPerSecond += production;

        switch (type) {
            case 0:
                grandmaCount++;
                grandmaCost *= 1.5;
                break;
            case 1:
                farmCount++;
                farmCost *= 1.5;
                break;
            case 2:
                factoryCount++;
                factoryCost *= 1.5;
                break;
            case 3:
                mineCount++;
                mineCost *= 1.5;
                break;
        }

        updateUI();
        saveGame();
    }

    private void updateUI() {
        counter.setText("🍪 " + format.format(cookies));

        cpsText.setText(
            format.format(cookiesPerSecond) + " cookies per second"
        );

        upgradeText.setText(
            "Grandma: " + format.format(grandmaCost) +
            " cookies\n\n" +
            "Farm: " + format.format(farmCost) +
            " cookies\n\n" +
            "Factory: " + format.format(factoryCost) +
            " cookies\n\n" +
            "Mine: " + format.format(mineCost) +
            " cookies"
        );

        statsText.setText(
            "YOUR EMPIRE\n\n" +
            "Grandmas: " + grandmaCount + "\n" +
            "Farms: " + farmCount + "\n" +
            "Factories: " + factoryCount + "\n" +
            "Mines: " + mineCount
        );
    }

    private void saveGame() {
        getSharedPreferences("cookie_game", MODE_PRIVATE)
            .edit()
            .putFloat("cookies", (float) cookies)
            .putFloat("cps", (float) cookiesPerSecond)
            .putInt("grandmas", grandmaCount)
            .putInt("farms", farmCount)
            .putInt("factories", factoryCount)
            .putInt("mines", mineCount)
            .putFloat("grandma_cost", (float) grandmaCost)
            .putFloat("farm_cost", (float) farmCost)
            .putFloat("factory_cost", (float) factoryCost)
            .putFloat("mine_cost", (float) mineCost)
            .apply();
    }

    private void loadGame() {
        android.content.SharedPreferences prefs =
            getSharedPreferences("cookie_game", MODE_PRIVATE);

        cookies = prefs.getFloat("cookies", 0);
        cookiesPerSecond = prefs.getFloat("cps", 0);

        grandmaCount = prefs.getInt("grandmas", 0);
        farmCount = prefs.getInt("farms", 0);
        factoryCount = prefs.getInt("factories", 0);
        mineCount = prefs.getInt("mines", 0);

        grandmaCost = prefs.getFloat("grandma_cost", 25);
        farmCost = prefs.getFloat("farm_cost", 100);
        factoryCost = prefs.getFloat("factory_cost", 500);
        mineCost = prefs.getFloat("mine_cost", 2000);
    }

    @Override
    protected void onResume() {
        super.onResume();
        handler.post(gameLoop);
    }

    @Override
    protected void onPause() {
        handler.removeCallbacks(gameLoop);
        saveGame();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        handler.removeCallbacks(gameLoop);
        super.onDestroy();
    }
  }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTheme(R.style.AppTheme);

        setContentView(R.layout.activity_main);
}
          
