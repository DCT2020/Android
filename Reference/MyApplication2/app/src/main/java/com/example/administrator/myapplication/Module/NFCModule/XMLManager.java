package com.example.administrator.myapplication.Module.NFCModule;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.util.Xml;
import android.view.View;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017-11-27.
 */

public class XMLManager {

    File file = null;
    Activity activity;
    String data;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public void Init(Activity _activity){

        activity = _activity;
        file = new File(Environment.getExternalStorageDirectory() + "/database1.xml");
    }

    public void writeToXmlFile(View view, ArrayList<Car> carContainer) {

        try {
           boolean isSucssesful = file.createNewFile();
           Log.i("Debug",isSucssesful + " 결과");
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        //////////////////////////////////////////////wrtie to xml
        FileOutputStream fileos = null;
        XmlSerializer xmlSerializer = Xml.newSerializer();
        StringWriter writer = new StringWriter();
        try{
            fileos = new FileOutputStream(file);
            xmlSerializer.setOutput(writer);
            xmlSerializer.startDocument("UTF-8",true);
            xmlSerializer.startTag(null, "uses-nfc");
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

        for( Car car : carContainer) {
            try {
                xmlSerializer.startTag(null, "item");
                xmlSerializer.attribute(null, "id", String.valueOf(car.getNumber()));
                for (StringBuffer buf : car.getTags()) {
                    xmlSerializer.startTag(null, "tag");
                    xmlSerializer.attribute(null, "id", buf.toString());
                    xmlSerializer.endTag(null, "tag");
                }
                xmlSerializer.endTag(null, "item");
            }
            catch (IllegalStateException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            xmlSerializer.endTag(null, "uses-nfc");
            xmlSerializer.endDocument();
            xmlSerializer.flush();
            data = writer.toString();
            fileos.write(data.getBytes());
            fileos.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        /////////////////////////////write end
    }

    public ArrayList<Car> readToXmlFile() {
        ArrayList<Car> result = new ArrayList<Car>();

        ArrayList<String> carData = new ArrayList<String>();
        FileInputStream fis;
        InputStreamReader isr;
        char[] inputbuffer;

        try {
            fis = new FileInputStream(file);
            isr = new InputStreamReader(fis);
            inputbuffer = new char[fis.available()];
            isr.read(inputbuffer);
            data = new String(inputbuffer);
            isr.close();
            fis.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
        }

        XmlPullParserFactory factory = null;
        try {
            factory = XmlPullParserFactory.newInstance();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        factory.setNamespaceAware(true);
        XmlPullParser xpp = null;
        try {
            xpp = factory.newPullParser();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        try {
            xpp.setInput(new StringReader(data));
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        Car temp = null;
        boolean isName = false;
        int eventType = 0;
        try {
            eventType = xpp.getEventType();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }

        String buf = new String();
        while (eventType != XmlPullParser.END_DOCUMENT) {
            if (eventType == XmlPullParser.START_DOCUMENT) {
                System.out.println("Start document");
            }
            else if (eventType == XmlPullParser.START_TAG) {
                if(xpp.getName().equals("tag")){
                    buf = xpp.getAttributeValue(null,"id");
                    temp.addTag(new StringBuffer(buf));
                }
                else if(xpp.getName().equals("item")){
                    buf = xpp.getAttributeValue(null, "id");
                    temp = new Car(Integer.parseInt(buf));
                }
            }
            else if (eventType == XmlPullParser.END_TAG) {
                if(xpp.getName().equals("item")) {
                    result.add(temp);
                }
            }
            try {
                eventType = xpp.next();
            }
            catch (XmlPullParserException e) {
                e.printStackTrace();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

        return result;
    }
}
