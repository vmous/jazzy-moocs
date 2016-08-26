package lecturecode

/**
 * A class abstracting the Java Script Object Notation.
 */
abstract class JSON {
  def show: String = this match {
    case JSeq(elems) =>
      "[" + (elems map(_.show) mkString ",") + "]"
    case JObj(bindings) =>
      val assocs = bindings map {
        case (key, value) => "\"" + key + "\":" + value.show
      }
      "{" + (assocs mkString ",") + "}"
    case JNum(num) => num.toString
    case JStr(str) => "\"" + str + "\""
    case JBool(bool) => bool.toString
    case JNull => "null"
  }
}

case class JSeq(elems: List[JSON]) extends JSON
case class JObj(bindings: Map[String, JSON]) extends JSON
case class JNum(num: Double) extends JSON
case class JStr(str: String) extends JSON
case class JBool(bool: Boolean) extends JSON
case object JNull extends JSON
