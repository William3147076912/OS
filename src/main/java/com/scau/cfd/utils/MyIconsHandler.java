package com.scau.cfd.utils;

import org.kordamp.ikonli.AbstractIkonHandler;
import org.kordamp.ikonli.Ikon;

import java.io.InputStream;
import java.net.URL;

public class MyIconsHandler extends AbstractIkonHandler {
    @Override
    public boolean supports(String description) {
        return description != null && description.startsWith(ConstantSet.FONT_PREFIX);
    }

    @Override
    public Ikon resolve(String description) {
        return MyIcons.findIkonByDesc(description);
    }

    @Override
    public URL getFontResource() {
        return MyIconsHandler.class.getResource(ConstantSet.FONT_PATH);
    }

    @Override
    public InputStream getFontResourceAsStream() {
        return MyIconsHandler.class.getResourceAsStream(ConstantSet.FONT_PATH);
    }

    @Override
    public String getFontFamily() {
        return ConstantSet.FONT_NAME;
    }
}
