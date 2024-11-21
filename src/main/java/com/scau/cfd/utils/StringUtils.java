package com.scau.cfd.utils;

import com.scau.cfd.controller.MainController;
import com.scau.cfd.manage.Catalog;
import com.scau.cfd.manage.OurFile;
import io.vproxy.vfx.ui.loading.LoadingItem;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {
    public static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");

    private StringUtils() {
    }

    public static String sanitizeName(String name) {
        // 定义允许的字符集
        String allowedCharsRegex = "[a-zA-Z0-9\\s!@#%&*()_+-={};:'\"|,<>?\\[\\]\\\\]";
        // 过滤掉不允许的字符
        String filteredName = name.replaceAll("[^" + allowedCharsRegex + "]", "");

        // 截取字符串，确保长度不超过两个字符
        if (filteredName.length() > 2) {
            filteredName = filteredName.substring(0, 2);
        }

        return filteredName;
    }

    public static boolean isValidName(String name) {
        String regex = "^[a-zA-Z0-9\\s!@#%&*()_+-={};:'\"|,<>?\\[\\]\\\\]{3}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    public static boolean isValidType(String type) {
        String regex = "^[a-zA-Z0-9\\s!@#%&*()_+-={};:'\"|,<>?\\[\\]\\\\]{2}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(type);
        return matcher.matches();
    }

    public static boolean isNameExists(String name, Class<?> clazz) {
        for (Object item : MainController.getTableView().getItems()) {
            if (clazz.isInstance(item)) {
                String itemName = clazz.cast(item) instanceof OurFile ? ((OurFile) item).getName() : ((Catalog) item).getName();
                if (itemName.equals(name)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static String randomString(int lenMin, int lenMax) {
        var n = ThreadLocalRandom.current().nextInt(lenMax - lenMin);
        return randomString(n + lenMin);
    }

    public static String randomString(int len) {
        int leftLimit = 97; // letter 'a'
        int rightLimit = 122; // letter 'z'
        var random = ThreadLocalRandom.current();
        StringBuilder buffer = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            int randomLimitedInt = leftLimit + (int)
                    (random.nextFloat() * (rightLimit - leftLimit + 1));
            buffer.append((char) randomLimitedInt);
        }
        return buffer.toString();
    }

    public static String randomIPAddress() {
        int a = ThreadLocalRandom.current().nextInt(256);
        int b = ThreadLocalRandom.current().nextInt(256);
        int c = ThreadLocalRandom.current().nextInt(256);
        int d = ThreadLocalRandom.current().nextInt(256);
        return a + "." + b + "." + c + "." + d;
    }

    public static List<LoadingItem> buildLoadingItems() {
        var ls = new ArrayList<LoadingItem>();
        for (var i = 0; i < 100; ++i) {
            ls.add(new LoadingItem(1, StringUtils.randomString(10, 20), () -> {
            }));
        }
        return ls;
    }


}
