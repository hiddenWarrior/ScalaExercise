package simplefuncs

import scala.collection.immutable.StringOps

object SimpleFuncs{
    //i ignored negatives delibretaly because it would complicate things and this question is supposed to be simple
    def factorial(a:Int):Int = if (a < 0) factorial(-a) else if (a == 0 || a == 1) 1 else a * factorial(a-1)
    def palindrome(a:String) = a == a.reverse
    def runLengthEncode(str:String):String = {
        val strList = str.toList
        def runLengthEncode(strList: List[Char]):String ={
        strList match{
                case l :: a => 
                val (repeat,rest) = a.span(_ == l)
                l + (repeat.length + 1).toString + runLengthEncode(rest)
                case Nil => ""
            }
        }
        runLengthEncode(strList)
    }
    //this function will fail if you fail to keep the assumptions
    def runLengthDecode(str:String):String ={
        def runLengthDecode(strList: List[Char]):String = {
            def cosumeOneChar(strList: List[Char]): Tuple2[String,List[Char]] = {
                val char = strList.head
                val (numberStr, leftOver) = strList.tail.span{ Character.isDigit(_) }
                (new StringOps("" + char) * (numberStr.foldLeft("")(_ + _).toInt ), leftOver)
            }

            if(strList.length == 0){
                 ""
            } 
            else {
                val (newStr, leftOver) = cosumeOneChar(strList)
                newStr + runLengthDecode(leftOver)
            }
        }
        runLengthDecode(str.toList)
    }
    def compose[A](a:A => A,b:A => A) = {
        x:A => a(b(x))
    }
    //def runLengthDecode("a10b3a1x4y3z1y1x1")        // returns "aaaaaaaaaabbbaxxxxyyyzyx"
}