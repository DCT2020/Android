package com.indigo.vrperformance;

import android.app.PendingIntent;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

/**
 * Created by S.B Hwang on 2017-10-20.
 */

public class IndigoNfc extends UnityPlayerActivity {
    private NfcAdapter nfcAdapter;
    private PendingIntent pendingIntent;

    public boolean init() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        Intent intent = new Intent(this, IndigoNfc.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);
        return true;
    }

    public void enable() {
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }

    public void disable() {
        nfcAdapter.disableForegroundDispatch(this);
    }

    private void sendMessage(String func, String param) {
        UnityPlayer.UnitySendMessage("(singleton) Indigo.IndigoNfc", func, param);
    }

    private String byteArrayToHex(byte[] a) {
        StringBuilder sb = new StringBuilder();
        for (final byte b : a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);
            sendMessage("OnNfcScan", byteArrayToHex(tag.getId()));
        }
    }
}