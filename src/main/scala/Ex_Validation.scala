import scala.util.{Failure, Success, Try}

object Ex_Validation {

  case class Person(age: Int, name: String)

  object vanilla {

    object Person {
      def apply(age: String, name: String): Try[Ex_Validation.Person] = {
        val tryAge = validateAge(age)
        val tryName = validateName(name)

        tryAge match {
          case Success(age) =>
            tryName match {
              case Success(name) => Success(Ex_Validation.Person(age, name))
              case Failure(e2) => Failure(e2)
            }
          case Failure(e1) =>
            tryName match {
              case Success(name) => Failure(e1)
              case Failure(e2) => Failure(new IllegalArgumentException(e1.getMessage + e2.getMessage))
            }
        }
      }
    }

    def validateAge(s: String): Try[Int] =
      Try(s.toInt)

    def validateName(s: String): Try[String] = if (s.length > 10) Failure(new IllegalArgumentException("name too long")) else Success(s)
  }

  object scalaz {

    import _root_.scalaz.{Validation => V, _}
    import Scalaz._

    object Person {
      def apply(age: String, name: String): ValidationNel[Throwable, Person] = (validateAge(age) |@| validateName(name)) {
        Validation.Person.apply
      }

      def validateAge(s: String): ValidationNel[Throwable, Int] = V.fromTryCatchNonFatal(s.toInt).toValidationNel

      def validateName(s: String): ValidationNel[Throwable, String] = if (s.length > 10) new IllegalArgumentException("name too long").failureNel else s.successNel
    }
  }

  object cats {
    import _root_.cats.data.Validated.{Invalid, Valid}
    import _root_.cats.data.{Validated, ValidatedNel}
    import _root_.cats.Apply
    import _root_.cats.std.list._

    object Person {
      // todo : value |@| is not a member of cats.data.ValidatedNel[Throwable,Int]
      def apply(age: String, name: String): ValidatedNel[Throwable, Person] =
        Apply[({type L[X] = ValidatedNel[Throwable, X]})#L].map2(validateAge(age), validateName(name)) {
          (age, name) => Validation.Person(age, name)
        }

      def validateAge(s: String): ValidatedNel[Throwable, Int] = Validated.catchNonFatal(s.toInt).toValidatedNel

      def validateName(s: String): ValidatedNel[Throwable, String] = if (s.length > 10) Invalid(new IllegalArgumentException("name too long")).toValidatedNel else Valid(s).toValidatedNel
    }
  }
}