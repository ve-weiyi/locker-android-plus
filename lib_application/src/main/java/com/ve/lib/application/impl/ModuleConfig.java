package com.ve.lib.application.impl;

/**
 * @Description hello word!
 * @Author weiyi
 * @Date 2022/3/24
 */
public class ModuleConfig {
    private static final String moduleOneInit = "com.ve.lib.auth.AuthApplication";
    private static final String moduleTwoInit = "com.ve.lib.auth.AuthApplication";
    private static final String moduleMusic = "com.ve.music.MusicApplication";
    public static String[] MODULESLIST = {
//            moduleOneInit,
//            moduleTwoInit
            moduleMusic
    };
}
