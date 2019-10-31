package ng.com.lumitce.booking.flightbookingclassic;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.utils.Log;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import ru.aviasales.core.AviasalesSDK;
import ru.aviasales.core.identification.SdkConfig;
import ru.aviasales.template.ads.AdsImplKeeper;
import ru.aviasales.template.ui.fragment.AviasalesFragment;
import ru.aviasales.template.utils.PrivacyPolicyUrl;

public class MainActivity extends AppCompatActivity {
    private static final String CONSENT = "consent";
    private final static String TRAVEL_PAYOUTS_MARKER = "88098";
    private final static String TRAVEL_PAYOUTS_TOKEN = "9f3454170d65fe4673ad7eb1b2ca293a";
    private final static String APPODEAL_APP_KEY = "0e4a0fd0eaab74eb5bc59599bf13a4b0ae366d48d3b89def";
    private final static String SDK_HOST = "www.travel-api.pw";

    private final static boolean SHOW_ADS_ON_START = true;
    private final static boolean SHOW_ADS_ON_WAITING_SCREEN = true;
    private final static boolean SHOW_ADS_ON_SEARCH_RESULTS = true;

    private AviasalesFragment aviasalesFragment;
    boolean consent;

    public static Intent getIntent(Context context, boolean consent) {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(CONSENT, consent);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        PrivacyPolicyUrl.setUrl("");
        AviasalesSDK.getInstance().init(this, new SdkConfig(TRAVEL_PAYOUTS_MARKER, TRAVEL_PAYOUTS_TOKEN, SDK_HOST));
        setContentView(R.layout.activity_main);
        consent = getIntent().getBooleanExtra(CONSENT, false);

        android.util.Log.d("Appodeal", "Consent: " + consent);
        initAppodeal();
        initFragment();


    }
    private void initAppodeal() {
        AppodealAds ads = new AppodealAds();
        //Appodeal.setLogLevel(Log.LogLevel.debug);
        ads.setStartAdsEnabled(SHOW_ADS_ON_START);
        ads.setWaitingScreenAdsEnabled(SHOW_ADS_ON_WAITING_SCREEN);
        ads.setResultsAdsEnabled(SHOW_ADS_ON_SEARCH_RESULTS);
        ads.consent = consent;
        ads.init(this, APPODEAL_APP_KEY);

        AdsImplKeeper.getInstance().setCustomAdsInterfaceImpl(ads);

    }
    private void initFragment() {
        FragmentManager fm = getSupportFragmentManager();

        aviasalesFragment = (AviasalesFragment) fm.findFragmentByTag(AviasalesFragment.TAG); // finding fragment by tag


        if (aviasalesFragment == null) {
            aviasalesFragment = (AviasalesFragment) AviasalesFragment.newInstance();
        }

        FragmentTransaction fragmentTransaction = fm.beginTransaction(); // adding fragment to fragment manager
        fragmentTransaction.replace(R.id.fragment_place, aviasalesFragment, AviasalesFragment.TAG);
        fragmentTransaction.commit();
    }
    @Override
    public void onBackPressed() {

        if (!aviasalesFragment.onBackPressed()) {
            super.onBackPressed();
        }

    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

}
