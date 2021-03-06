package ammonite.repl

import utest._

object ScriptTests extends TestSuite{
  val tests = TestSuite{
    println("ScriptTests")
    val check = new Checker()
    val scriptPath = "repl/src/test/resource/scripts"

    'compilationBlocks{
      'loadIvy{
        check.session(s"""
          @ load.script("$scriptPath/LoadIvy.scala")

          @ val r = res
          r: String = ${"\"\"\""}
          <a href="www.google.com">omg</a>
          ${"\"\"\""}
          """)
      }
      'preserveImports{
        check.session(s"""
          @ load.script("$scriptPath/PreserveImports.scala")

          @ val r = res
          r: Left[String, Nothing] = Left("asd")
          """)
      }
      'annotation{
        check.session(s"""
          @ load.script("$scriptPath/Annotation.scala")

          @ val r = res
          r: Int = 24
          """)
      }
      'syntax{
        check.session(s"""
          @ load.script("$scriptPath/BlockSepSyntax.scala")

          @ val r = res
          r: Int = 24
          """)
      }
    }
    'failures{
      'syntaxError{
        check.session(s"""
          @ load.script("$scriptPath/SyntaxError.scala")

          @ val r = res
          error: Compilation Failed
          Main.scala:29: not found: value res
          res
          ^
          """)
      }
      'compilationError{
        check.session(s"""
          @ load.script("$scriptPath/CompilationError.scala")

          @ val r = res
          error: Compilation Failed
          Main.scala:39: not found: value res
          res
          ^

          """)
      }
      'nofile{
        check.session(s"""
          @ load.script("$scriptPath/notHere")
          error: java.nio.file.NoSuchFileException: $scriptPath/notHere
          """
        )
      }
      'multiBlockError{
        check.session(s"""
          @ load.script("$scriptPath/MultiBlockError.scala")

          @ val r1 = res1
          r1: Int = 1

          @ val r2 = res2
          error: Compilation Failed
          Main.scala:41: not found: value res2
          res2 
          ^
          """)
      }
    }
  }
}
