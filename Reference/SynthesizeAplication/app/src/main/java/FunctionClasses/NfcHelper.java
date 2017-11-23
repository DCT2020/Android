package FunctionClasses;

import android.app.Activity;
import android.content.Context;
import android.nfc.NfcAdapter;
import android.nfc.Tag;

/**
 * Created by Administrator on 2017-11-22.
 */

public class NfcHelper {
    private static NfcHelper instance = null;

    private NfcAdapter.ReaderCallback readerCallback;
    private NfcAdapter nfcAdapter;
    private Context context;

    public static NfcHelper getInstance(){
        if(instance == null) {
            instance = new NfcHelper();
        }
        return instance;
    }

    public void enable(Activity activity){
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if(nfcAdapter != null){
            nfcAdapter.enableReaderMode(activity,readerCallback, NfcAdapter.FLAG_READER_NFC_A | NfcAdapter.FLAG_READER_SKIP_NDEF_CHECK,null);
        }
    }

    public void disable(Activity activity){
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity);
        if(nfcAdapter != null){
            nfcAdapter.disableReaderMode(activity);
        }
    }

    public boolean init(Context _context){
        context = _context;
        if(context == null) return false;
        return true;
    }

    public void setNfcActionCallBack(NfcAdapter.ReaderCallback readercallback)
    {
        readerCallback = readercallback;
    }

    private String byteArrayToHex(byte[] a){
        StringBuilder sb = new StringBuilder();
        for(final byte b: a)
            sb.append(String.format("%02x ", b & 0xff));
        return sb.toString();
    }
}
