package com.ve.module.lockit.utils

import com.ve.module.lockit.common.enums.PasswordEnum
import java.util.regex.Pattern

/**
 * @Author weiyi
 * @Date 2022/4/22
 * @Description current project lockit-android
 */
object PasswordUtils {




    fun hidePassword(start:Int,end :Int,password: String):String{
        val pre=start
        val last=password.length-end
        val mid=password.length-pre-last

        return password.replace("(\\w{$pre})\\w{$mid}(\\w{$last})".toRegex(), "$1****$2")
    }

    /**
     * 隐藏中间 显示前后1/4
     */
    fun hidePassword(password: String):String{
        val weight=4
        val start=password.length/weight
        val end=password.length-start
        return hidePassword(start,end, password)
    }
    /**
     * 检测邮箱是否合法
     * \w 匹配任何字类字符，包括下划线。与"[A-Za-z0-9_]"等效。
     * \s 匹配任何空白字符，包括空格、制表符、换页符等。与 [ \f\n\r\t\v] 等效。
     * \d 数字字符匹配。等效于 [0-9]。
     * @param username 用户名
     * @return 合法状态
     */
    fun checkEmail(username: String?): Boolean {
        val rule = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z0-9]+$"
        //正则表达式的模式 编译正则表达式
        val p = Pattern.compile(rule)
        //正则表达式的匹配器
        val m = p.matcher(username)
        //进行正则匹配
        return m.matches()
    }

    /**
     * 判断密码强度
     * https://www.nowcoder.com/questionTerminal/52d382c2a7164767bca2064c1c9d5361?answerType=1&f=discussion
     */
    fun checkPasswordLevel(password:String): PasswordEnum {
        val cs: CharArray = password.toCharArray()
        var score = 0 // 最终的得分
        var letterNum = 0 // 所有字母的数量
        var upperCaseNum = 0 // 大写字母的数量
        var lowerCaseNum = 0 // 小写字母的数量
        var digitNum = 0 // 数字字符的数量
        var charNum = 0 // 符号的数量

        // 遍历字符数组，统计每种字符的数量
        for (c in cs) {
            if (Character.isLowerCase(c)) {
                lowerCaseNum++
                letterNum++
            } else if (Character.isUpperCase(c)) {
                upperCaseNum++
                letterNum++
            } else if (Character.isDigit(c)) {
                digitNum++
            } else {
                charNum++
            }
        }
        // 根据密码长度来确定得分
        if (cs.size <= 4) {
            score += 5
        } else if (cs.size <= 7) {
            score += 10
        } else if (cs.size >= 8) {
            score += 25
        }
        // 根据字母情况来确定得分
        if (letterNum < 1) {
            score += 0
        } else if (letterNum == upperCaseNum || letterNum == lowerCaseNum) {
            score += 10
        } else if (upperCaseNum > 0 && lowerCaseNum > 0) {
            score += 20
        }
        // 根据数字的情况来确定得分
        if (digitNum < 1) {
            score += 0
        } else if (digitNum == 1) {
            score += 10
        } else if (digitNum > 1) {
            score += 20
        }
        // 根据符号的情况来确定得分
        if (charNum < 1) {
            score += 0
        } else if (charNum == 1) {
            score += 10
        } else if (charNum > 1) {
            score += 25
        }
        // 根据奖励的情况来确定得分，注意这个逻辑是三选一，所以要倒着来
        if (upperCaseNum > 0 && lowerCaseNum > 0 && digitNum > 0 && charNum > 0) {
            score += 5
        } else if (upperCaseNum > 0 && digitNum > 0 && charNum > 0 || lowerCaseNum > 0 && digitNum > 0 && charNum > 0) {
            score += 3
        } else if (upperCaseNum > 0 && digitNum > 0 || lowerCaseNum > 0 && digitNum > 0) {
            score += 2
        }

        // 输出结果
        if (score >= 90) {
            return PasswordEnum.VERY_SECURE
        } else if (score >= 80) {
            return PasswordEnum.SECURE
        } else if (score >= 70) {
            return PasswordEnum.VERY_STRONG
        } else if (score >= 60) {
            return PasswordEnum.STRONG
        } else if (score >= 50) {
            return PasswordEnum.AVERAGE
        } else if (score >= 25) {
            return PasswordEnum.WEAK
        } else if (score >= 0) {
            return PasswordEnum.VERY_WEAK
        }else{
            return PasswordEnum.unknown
        }
    }

    /**
     * 比较版本号
     */
    fun compareVersion(version1: String, version2: String): Int {
        val a1 = version1.split("\\.").toTypedArray()
        val a2 = version2.split("\\.").toTypedArray()
        for (n in 0 until Math.max(a1.size, a2.size)) {
            val i = if (n < a1.size) Integer.valueOf(a1[n]) else 0
            val j = if (n < a2.size) Integer.valueOf(a2[n]) else 0
            if (i < j) return -1 else if (i > j) return 1
        }
        return 0
    }
    fun generatePassword(length:Int,useUpper:Boolean=false,useLower:Boolean=false,useNumber:Boolean=false,useSymbol:Boolean=false): String {
        var pass=""
        var list  = ArrayList<Int>()
        if(useUpper)
            list.add(0)
        if(useLower)
            list.add(1)
        if(useNumber)
            list.add(2)
        if(useSymbol)
            list.add(3)

        for(i in 1..length){
            var choice = list.random()
            when(choice){
                0-> pass += ('A'..'Z').random().toString()
                1-> pass += ('a'..'z').random().toString()
                2-> pass += ('0'..'9').random().toString()
                3-> pass += listOf('.','~','!','@','#','$','%','&','*','+','=','-').random().toString()
            }
        }
        return pass
    }
}