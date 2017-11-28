package com.indigo.libindigonfc;

import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import com.unity3d.player.UnityPlayer;

/**
 * Created by S.B Hwang on 2017-10-24.
 */

class IndigoNfc {
    private static IndigoNfc instance = null;
    private Context context;

    private NfcAdapter nfcAdapter;

    public static IndigoNfc getInstance() {
        if (instance == null) {
            instance = new IndigoNfc();
        }
        return instance;
    }

    public boolean init() {
        context = UnityPlayer.currentActivity.getApplicationContext();
        if (context == null) return false;
        nfcAdapter = NfcAdapter.getDefaultAdapter(contex
        if (nfcAdapter == null) return false;
        return true;
    }

    public void enable() {
        nfcAdapter.enableReaderMode(UnityPlayer.currentActivity, readerCallback, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK, null);
    }

    public void disable() {
        nfcAdapter.disableReaderMode(UnityPlayer.currentActivity);
    }

    private void sendMessage(String func, String param) {
        UnityPlayer.UnitySendMessage("(singleton) Indigo.IndigoNfc", func, param);
    }

    private NfcAdapter.ReaderCallback readerCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            sendMessage("OnNfcScan", byteArrayToHex(tag.getId()));
        }
    };

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (final byte b : a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();

    }
}
