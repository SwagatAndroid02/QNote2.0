package ssa.worriorx.com.qnote.ui.home.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.util.*

class Utils {
    companion object {

        //This annotation tells Java classes to treat this method as if it was a static to [KotlinClass]
        @JvmStatic
        fun dateTimeCovertor(noteDate: String): String{
            var today = Date()
            var dateNote = noteDate
            var sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var dNote:Date? = sdf.parse(dateNote)

            var day:Long = (today.time - dNote?.time!!)/86400000
            var hour:Long = (today.time - dNote?.time!!)%86400000/3600000
            var min:Long = (today.time - dNote?.time!!)%86400000%3600000/60000
            var sec:Long = (today.time - dNote?.time!!)%86400000%3600000%60000/1000

            Log.d("TAG","CHECK AGE Days ${day}, hours ${hour}, mins ${min}, sec ${sec}")

            if (day < 1){
                if (hour<1){
                    if (min<1){
                        if (sec<1){
                            return "Just now"
                        }else{
                            return "seconds ago "
                        }
                    }else{
                        if (min == 1.toLong())
                            return "minutes ago"
                        else return "${min} minutes ago"
                    }
                }else{
                    if (hour == 1.toLong())
                        return "${hour} hour ago"
                    else return "${hour} hours ago"
                }
            }else{
                 if (day == 1.toLong())
                     return "${day} day ago"
                 else return "${day} day ago"
            }

        }

       /* //Without it, you would have to use [KotlinClass.Companion.bar()] to use this method.
        fun bar(): Int = 2*/
    }
}