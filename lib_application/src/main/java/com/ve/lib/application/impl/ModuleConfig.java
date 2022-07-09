package com.ve.lib.application.impl;

/**
 * @Description hello word!
 * @Author weiyi
 * @Date 2022/3/24
 */
public class ModuleConfig {
    private static final String moduleOneInit = "com.manu.module_one.ModuleOneAppInit";
    private static final String moduleTwoInit = "com.manu.module_two.ModuleTwoAppInit";
    private static final String moduleMusic = "com.ve.music.MusicApplication";
    public static String[] MODULESLIST = {
//            moduleOneInit,
//            moduleTwoInit
            moduleMusic
    };
}
