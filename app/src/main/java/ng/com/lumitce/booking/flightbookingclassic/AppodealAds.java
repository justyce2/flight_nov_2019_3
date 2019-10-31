package ng.com.lumitce.booking.flightbookingclassic;

import android.app.Activity;
//import android.support.annotation.Nullable;
import android.view.View;

import androidx.annotation.Nullable;

import com.appodeal.ads.Appodeal;
import com.appodeal.ads.Native;
import com.appodeal.ads.NativeAd;
import com.appodeal.ads.native_ad.views.NativeAdViewAppWall;
import java.util.List;
import ru.aviasales.adsinterface.AdsInterface;

public class AppodealAds implements AdsInterface {
  private boolean startAdsEnabled = false;
  private boolean waitingScreenAdsEnabled = false;
  private boolean resultsAdsEnabled = false;
  public boolean consent = false;


  private NativeAdViewAppWall nativeAdView;
  //boolean consent;

  public void init(Activity activity, String appKey) {
    setUpAppodeal(activity);
    Appodeal.disableNetwork(activity, "vungle");

    Appodeal.initialize(activity, appKey, Appodeal.NATIVE | Appodeal.BANNER | Appodeal.NON_SKIPPABLE_VIDEO | Appodeal.INTERSTITIAL, consent);
      //Appodeal.show(activity, Appodeal.BANNER_TOP);
  }

  private void setUpAppodeal(Activity activity) {
    //Appodeal.setAutoCache(Appodeal.NATIVE,false);
   //Appodeal.setAutoCacheNativeMedia(false);
      Appodeal.cache(activity, Appodeal.NATIVE);
      Appodeal.setNativeAdType(Native.NativeAdType.NoVideo);
    Appodeal.setNativeCallbacks(new NativeCallbacksAdapter() {
      @Override
      public void onNativeLoaded() {
        NativeAdListKeeper.getInstance().setNativeAdList(Appodeal.getNativeAds(Integer.MAX_VALUE));
       //NativeAdListKeepers.getInstance().setNativeAdList(Appodeal.getNativeAds(Integer.MAX_VALUE));

      }
    });
    //Appodeal.cache(activity, Appodeal.NATIVE);


  }

  @Override
  public void showStartAdsIfAvailable(final Activity activity) {
    if (startAdsEnabled) {
      if (Appodeal.isLoaded(Appodeal.NON_SKIPPABLE_VIDEO) && Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
        Appodeal.show(activity, Appodeal.NON_SKIPPABLE_VIDEO | Appodeal.INTERSTITIAL);
      } else if (Appodeal.isLoaded(Appodeal.NON_SKIPPABLE_VIDEO) && !Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
        Appodeal.show(activity, Appodeal.NON_SKIPPABLE_VIDEO);
      } else if (!Appodeal.isLoaded(Appodeal.NON_SKIPPABLE_VIDEO) && Appodeal.isLoaded(Appodeal.INTERSTITIAL)) {
        Appodeal.show(activity, Appodeal.INTERSTITIAL);
      } else {
        Appodeal.setInterstitialCallbacks(new InterstitialCallbacksAdapter() {
          @Override
          public void onInterstitialLoaded(boolean b) {
            Appodeal.show(activity, Appodeal.INTERSTITIAL);
            Appodeal.setInterstitialCallbacks(null);
          }
        });
      }
    }
  }
  @Nullable
  @Override
  public View getMrecView(Activity activity) {
      List<NativeAd> nativeAdList = NativeAdListKeeper.getInstance().getNativeAdList();
      if (nativeAdList != null && resultsAdsEnabled && areResultsReadyToShow()) {
          nativeAdView = new NativeAdViewAppWall(activity,
                  nativeAdList.get(0));
      }

      return nativeAdView;
    }

  @Override
  public void showWaitingScreenAdsIfAvailable(Activity activity) {
    if (waitingScreenAdsEnabled) {
      //Appodeal.show(activity, Appodeal.NATIVE);
        //nativeAd.setNativeadView(NativeView);

        List<NativeAd> nativeAdList = NativeAdListKeeper.getInstance().getNativeAdList();
        nativeAdView.setNativeAd(nativeAdList.get(0));


    }

  }

  @Nullable
  @Override
  public View getNativeAdView(Activity activity) {
    List<NativeAd> nativeAdList = NativeAdListKeeper.getInstance().getNativeAdList();
    if (nativeAdList != null && resultsAdsEnabled && areResultsReadyToShow()) {
      nativeAdView = new NativeAdViewAppWall(activity,
          nativeAdList.get(0));
    }
    return nativeAdView;
  }

  @Override
  public boolean isStartAdsEnabled() {
    return startAdsEnabled;
  }

  @Override
  public void setStartAdsEnabled(boolean startAdsEnabled) {
    this.startAdsEnabled = startAdsEnabled;
  }


  @Override
  public boolean isWaitingScreenAdsEnabled() {
    return waitingScreenAdsEnabled;
  }

  @Override
  public void setWaitingScreenAdsEnabled(boolean waitingScreenAdsEnabled) {
    this.waitingScreenAdsEnabled = waitingScreenAdsEnabled;
  }

  @Override
  public boolean isResultsAdsEnabled() {
    return resultsAdsEnabled;
  }

  @Override
  public void setResultsAdsEnabled(boolean resultsAdsEnabled) {
    this.resultsAdsEnabled = resultsAdsEnabled;
  }

  @Override
  public boolean areResultsReadyToShow() {
    List<NativeAd> nativeAdList = NativeAdListKeeper.getInstance().getNativeAdList();
    return nativeAdList != null && !nativeAdList.isEmpty();
  }
}
