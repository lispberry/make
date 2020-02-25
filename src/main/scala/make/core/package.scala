package make

package object core {
  type Command[+R] = Unit => R
}
