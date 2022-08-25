/*
 * @(#)BusProvider.java
 * Date : 2019. 3. 31.
 * Copyright: (C) 2019 by devsunset All right reserved.
 */
package devsunset.simple.random.chat.modules.eventbusservice;

import com.squareup.otto.Bus;

/**
 * <PRE>
 * SimpleRandomChat BusProvider
 * </PRE>
 *
 * @author devsunset
 * @version 1.0
 * @since SimpleRandomChat 1.0
 */
public final class BusProvider extends Bus {
    private static final Bus BUS = new Bus();

    private BusProvider() {
    }

    public static Bus getInstance() {
        return BUS;
    }
}

