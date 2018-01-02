package com.levnovikov.system_base;

import com.levnovikov.system_base.back_handling.BackHandler;
import com.levnovikov.system_base.state.ActivityState;
import com.levnovikov.system_base.state.NodeState;

public abstract class StateInteractor<R extends Router> extends Interactor<R>
        implements StateDataProvider, BackHandler {

    public StateInteractor(R router, ActivityState activityState) {
        super(router, activityState);
        /*
         * If interactor need to store data after Activity recreation, we can set data source to router.
         * Router will get and save data when Activity will call onSaveInstanceState.
         * P.S. possible to move getStateData() in base Interactor and override if need to store data.
         */
        router.setStateDataProvider(this);
        router.setBackHandler(this);
    }

    public void onGetActive() {
        final NodeState state = getNodeState();
        if (state != null) {
            router.setState(state);
        }
    }
}
