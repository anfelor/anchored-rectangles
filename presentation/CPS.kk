type tree
  Bin(l : tree, r : tree)
  Tip(x : int)

fun flatten(t)
  match(t)
    Bin(l, r) -> flatten(l).append(flatten(r))
    Tip(x) -> [x]

fun append(xs, ys)
  match(xs)
    Cons(x,xx) -> Cons(x, append(xx, ys))
    Nil -> ys

// ---

fun flatten(t)
  match(t)
    Bin(l, r) -> flatten(l).append(flatten(r))
    Tip(x) -> [x]

fun append(xs, ys)
  xs.foldr(Cons, ys)

// ---

fun flatten(t)
  flatten'(t, Cons, Nil)

fun flatten'(t, cons, nil)
  match(t)
    Bin(l, r) -> flatten'(l, cons, nil).append(flatten'(r, cons, nil), cons)
    Tip(x) -> cons(x, nil)

fun append(xs, ys, cons)
  xs.foldr(cons, ys)

// ---

fun flatten(t)
  flatten'(t, Cons, Nil)

fun flatten'(t, cons, nil)
  match(t)
    Bin(l, r) -> build(flatten'(l)).foldr(cons, flatten'(r, cons, nil))
    Tip(x) -> cons(x, nil)

fun build(g)
  g(Cons, Nil)

// ---

fun flatten(t)
  flatten'(t, Cons, Nil)

fun flatten'(t, cons, nil)
  match(t)
    Bin(l, r) -> flatten'(l, cons, flatten'(r, cons, nil))
    Tip(x) -> cons(x, nil)

// ---

fun flatten(t)
  down(t, Nil)

fun down(t, acc)
  match(t)
    Bin(l, r) -> down(l, down(r, acc))
    Tip(x) -> Cons(x, acc)

// ---

type zipper
  BinR(l : tree, z : zipper)
  Top

fun flatten(t)
  down(t, Nil, Top)

fun down(t, acc, z)
  match(t)
    Bin(l, r) -> down(r, acc, BinR(l, z))
    Tip(x) -> up(Cons(x, acc), z)

fun up(acc, z)
  match(z)
    BinR(l, z') -> down(l, acc, z')
    Top -> acc

// ---

fun flatten(t)
  flatten'(t, Cons, Nil)

fun flatten'(t, cons, nil)
  match(t)
    Bin(l, r) -> build(flatten'(l)).foldr(cons, flatten'(r, cons, nil))
    Tip(x) -> cons(x, nil)

// ---

fun flatten(t)
  flatten'(t, Cons, fn() { Nil })

fun flatten'(t, cons, nil)
  match(t)
    Bin(l, r) -> flatten'(l, cons, fn() { flatten'(r, cons, nil) })
    Tip(x) -> cons(x, nil())

// ---

fun flatten(t)
  down(t, fn() { Nil })

fun down(t, z)
  match(t)
    Bin(l, r) ->
      down(l, fn() {
        down(r, z)
      })
    Tip(x) -> Cons(x, z())

// ---

type zipper
  BinL(r : tree, z : zipper)
  Top

fun flatten(t)
  down(t, Top)

fun down(t, z)
  match(t)
    Bin(l, r) -> down(l, BinL(r, z))
    Tip(x) -> Cons(x, up(z))

fun up(z)
  match(z)
    BinL(r, z') -> down(r, z')
    Top -> Nil

// ---

// make a perfectly balanced binary tree of `depth`
fun make( depth : int )
  if depth > 0
    then Bin( make(depth - 1), make(depth - 1) ) 
    else Bin( Tip, Tip )

// ---

type zipper  
  BinL(depth : int, z : zipper)
  BinR(left : tree, z : zipper)
  Top

fun make(depth)
  down(depth, Top)

fun down(depth, z)
  if depth > 0 
    then down(depth - 1, BinL(depth - 1, z))
    else up(Bin(Tip,Tip), z)

fun up(t, z)
  match z
    BinL(depth, z') -> down(depth, BinR(t, z'))
    BinR(l, z')     -> up(Bin(l, t), z')
    Top -> t

// ---

type zipper  
  BinL(depth : int, z : zipper)
  Top

fun make(depth, z)
  if depth > 0
    then make(depth - 1, BinL(depth - 1, z))
    else up(Bin(Tip,Tip), z)

fun up(t, z)
  match z
    BinL(depth, z') -> up(Bin(t, make(depth, Top)), z')
    Top -> t

// ---

type zipper  
  BinL(depth : int, z : zipper)
  Top

fun make(depth, z)
  if depth > 0
    then make(depth - 1, BinL(depth - 1, z))
    else up(Bin(Tip,Tip), z)

fun up(t, z)
  match z
    BinL(depth, Top) -> Bin(t, make(depth, Top))
    BinL(depth, z') -> up(Bin(t, make(depth, Top)), z')
    Top -> t
