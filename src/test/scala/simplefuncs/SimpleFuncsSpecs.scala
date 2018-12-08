package simplefuncs
// import simplefuncs.simplefuncs

import org.scalatest._

class FactorialSpec extends FlatSpec {
    "factorial" should "return 1 if input is zero" in {
        assert(SimpleFuncs.factorial(0) === 1)
    }

    "factorial" should "return 1 if input is 1" in {
        assert(SimpleFuncs.factorial(1) === 1)
    }

    "factorial" should "return 6 if input is 3" in {
        assert(SimpleFuncs.factorial(3) === 6)
    }
}

class PalindromeSpec extends FlatSpec {
    "palindrome" should "return true if input is \"anna\"" in {
        assert(SimpleFuncs.palindrome("anna") === true)
    }
    "palindrome" should "return true if input is \"annna\" (odd number of letters case)" in {
        assert(SimpleFuncs.palindrome("annna") === true)
    }

    "palindrome" should "return true if input is \"apple\"" in {
        assert(SimpleFuncs.palindrome("apple") === false)
    }

}

class RunLengthEncodeDecodeSpec extends FlatSpec {
    "runLengthEncode" should "return \"a10b3a1x4y3z1y1x1\" if input is \"aaaaaaaaaabbbaxxxxyyyzyx\"" in {
        assert(SimpleFuncs.runLengthEncode("aaaaaaaaaabbbaxxxxyyyzyx") === "a10b3a1x4y3z1y1x1")
    }
    "runLengthDecode" should "return \"aaaaaaaaaabbbaxxxxyyyzyx\" if input is \"a10b3a1x4y3z1y1x1\" (odd number of letters case)" in {
        assert(SimpleFuncs.runLengthDecode("a10b3a1x4y3z1y1x1") === "aaaaaaaaaabbbaxxxxyyyzyx")
    }
    

}

class Compose extends FlatSpec {
    "compose" should "return 4 if input is(x => x + 1, x => x * 2)(1) " in {
        assert(SimpleFuncs.compose((x:Int) => x + 1, (x:Int) => x * 2)(1) === 3)
    }
     
    "compose" should "return 3 if input is(x => x * 2, x => x + 1)(1) " in {
        assert(SimpleFuncs.compose((x:Int) => x * 2, (x:Int) => x + 1)(1) === 4)
    }

    "compose" should "return 49 if input is(x => x * x, x => x + 1)(1) " in {
        assert(SimpleFuncs.compose((x:Int) => x * x, (x:Int) => x + 1)(6) === 49)
    }
     

}



