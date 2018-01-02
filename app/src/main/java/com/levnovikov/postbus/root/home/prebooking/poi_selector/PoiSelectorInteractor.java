package com.levnovikov.postbus.root.home.prebooking.poi_selector;

import android.os.Parcelable;

import com.example.core_geo.Point;
import com.example.core_location.PoiSuggestionProvider;
import com.levnovikov.postbus.root.home.prebooking.poi_selector.di.PoiSelectorScope;
import com.levnovikov.system_base.StateInteractor;
import com.levnovikov.system_base.state.ActivityState;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

/**
 * Created by lev.novikov
 * Date: 20/12/17.
 */

@PoiSelectorScope
public class PoiSelectorInteractor extends StateInteractor<PoiSelectorRouter> {

    public interface PoiSelectionListener {
        void onPoiSelected(Point point);
        void onPoiSelectionCanceled();
    }

    private PoiSuggestionProvider poiProvider;
    private Presenter presenter;
    private PoiSelectionListener listener;

    @Inject
    public PoiSelectorInteractor(
            PoiSuggestionProvider poiProvider,
            Presenter presenter,
            PoiSelectorRouter router,
            ActivityState activityState,
            PoiSelectionListener listener) {
        super(router, activityState);
        this.poiProvider = poiProvider;
        this.presenter = presenter;
        this.listener = listener;
    }

    @Override
    public void onGetActive() {
        super.onGetActive();
        presenter.getPlaceTitleStream() //TODO unsubscribe
                .subscribe((title) -> poiProvider.updatePlace(title),
                        (error) -> { /*handle error*/ });

        poiProvider.getPoiStream() //TODO unsubscribe
                .subscribe((poiList) -> presenter.updateSuggestions(poiList),
                        (error) -> { /*handle error*/ });

        presenter.getSelectedPoi()
                .subscribe(point -> listener.onPoiSelected(point), error -> {});
    }

    @Override
    public boolean onBackPressed() {
        listener.onPoiSelectionCanceled();
        return true;
    }

    @Override
    public Parcelable getStateData() {
        return null;
    }

    public interface Presenter {
        Observable<Point> getSelectedPoi();
        Observable<String> getPlaceTitleStream();
        void updateSuggestions(List<Point> poiList);
    }
}
