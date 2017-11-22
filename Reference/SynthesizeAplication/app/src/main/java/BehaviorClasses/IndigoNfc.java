package BehaviorClasses;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.widget.Toast;

/**
 * Created by Administrator on 2017-11-22.
 */

public class IndigoNfc {
    private static IndigoNfc instance = null;

    private NfcAdapter nfcAdapter;
    private Context context;

    public static IndigoNfc getInstance(){
        if(instance == null) {
            instance = new IndigoNfc();
        }
        return instance;
    }

    public void enable(Activity activity){
        nfcAdapter.enableReaderMode(activity,readerCallback, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,null);
    }

    public void disable(Activity activity){
        nfcAdapter.disableReaderMode(activity);
    }

    public boolean init(Context _context){
        context = _context;
        if(context == null) return false;
        nfcAdapter = NfcAdapter.getDefaultAdapter(context);
        if(nfcAdapter == null) return false;
        return true;
    }

    private NfcAdapter.ReaderCallback readerCallback = new NfcAdapter.ReaderCallback() {
        @Override
        public void onTagDiscovered(Tag tag) {
            Toast.makeText(context,"Nfc discovered",Toast.LENGTH_LONG).show();
        }
    };

    private String byteArrayToHex(byte[] a){
        StringBuilder sb = new StringBuilder();
        for(final byte b: a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }
}
