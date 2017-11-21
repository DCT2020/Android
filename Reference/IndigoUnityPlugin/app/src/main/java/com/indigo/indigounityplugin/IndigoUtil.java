package com.indigo.indigounityplugin;

import com.unity3d.player.UnityPlayer;

public class IndigoUtil {
    private static IndigoUtil instance;

    public static IndigoUtil getInstance() {
        if(instance == null) {
            instance = new IndigoUtil();
        }
        return instance;
    }

    int native indigo_textureloader_load_texture();
}
