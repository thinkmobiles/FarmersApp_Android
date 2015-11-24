package com.farmers.underground;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tZpace
 * on 24-Nov-15.
 */
public class Notifier {

    public static boolean registerClient(Client client) {
        return getInstance().addClient(client);
    }

    public static boolean unregisterClient(Client client) {
        return getInstance().removeClient(client);
    }

    public static void sendData(Bundle bundle) {
        Message message = new Message();
        message.setData(bundle);
        message.setTarget(getInstance().msgHandler);
        message.sendToTarget();
    }

    public static Notifier getInstance() {
        return NotifierSingleton.INSTANCE;
    }

    public interface Client {
        void handleMessage(Message msg);
    }

    private boolean addClient(Client client) {
        synchronized (mClients) {
            return mClients.add(client);
        }
    }

    private boolean removeClient(Client client) {
        synchronized (mClients) {
            return mClients.remove(client);
        }
    }

    private MsgHandler msgHandler;
    private final Set<Client> mClients = new HashSet<>();

    private Notifier() {
        msgHandler = new MsgHandler(Looper.getMainLooper());
    }

    private static class NotifierSingleton {
        private static final Notifier INSTANCE = new Notifier();
    }

    private static class MsgHandler extends Handler {

        public MsgHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            for (Client mClient : getInstance().mClients) {
                mClient.handleMessage(msg);
            }

        }
    }

}
