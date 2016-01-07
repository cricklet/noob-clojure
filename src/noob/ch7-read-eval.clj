(reverse [1 2 3])

;; this doesn't work because (1 2 3) isn't valid operation
;; or, more generally, it isn't a valid form
;(reverse (1 2 +))

(defmacro backwards
  [form]
  (reverse form))

(backwards (1 2 +))


;; (+ 1 2)
;;
;; is passed into a READER which generates an AST:
;;
;;   +
;;  / \
;; 1   2
;;
;; this AST is simply a native clojure list!
;; this means that it's totally accessible in the program.
;; then, the code is run by the EVALUATOR.

;; thus, you can create your own AST and pass it to EVALUATOR directly!
(def
  addition-ast
  (list + 1 2))
(eval addition-ast)

;; the data structures your running program uses are just like those of the reader/evaluator.
;; this means that you use code to construct data structures for evaluation!

;; i.e. add 10 to the argument list of addition-ast
(eval (concat addition-ast [10]))

;; i.e. evaluate nested functions
(eval (list 'def 'lucky-number (list + 1 2)))
lucky-number


;; homoiconic:
;;  ASTs are represented using lists.
;;  your code is a textual representation of lists
;;  the evaluator consumes those lists!


;; the READER can be called directly!
(read-string "(+ 1 1)")
(eval (read-string "(+ 1 1)"))

;; READER MACROs (are not 'macros'!)

;; ' is a READER MACRO
;; it is also 'quote'. it prevents a from from being evaluated!
(read-string "'(a b c)")

;; @ is a reader macro!
(read-string "@var")

;; ; is a reader macro!
(read-string "; this is a comment \n(+ 1 1)")

;; clojure uses SYMBOLs to name funcs/macros/data/etc.
;; a SYMBOL is evaluated by being resolved.
;;  1. see if the symbol names a special form (always an operator, i.e. if)
;;  2. see if the symbol corresponds to a local binding (generally created by let)
;;  3. find a namespace mapping (generally created by def)

;; (+ (+ 1 2) 3)
;; when evaluating a function, all parameters are also evaluated.
;; thus, we need SPECIAL FORMs to implement stuff like 'if' and 'def'd

;; holy shit, it's MACRO time!
;;  - takes arguments and returns a value (just like functions)
;;  - operates on clojure data structures (just like functions)
;;
;; however, they are executed in between the READER and the EVALUATOR.
;; they manipulate the AST the READER spits out!
;; then the new returned AST is EVALUATED.
;;
;; in particular, when a MACRO is being executed,
;; the operands are not evaluated, symbols are not resolved.


(defmacro ignore-last-operand
 [function-call]
 (butlast function-call))

(ignore-last-operand (+ 1 2 3 4))

;; we can use (macroexpand) to see the AST the macro returns when executed
(macroexpand '(ignore-last-operand (+ 1 2 3 4)))

(defmacro infix
  [infixed]
  (list
   (second infixed)
   (first infixed)
   (last infixed)))

(infix (1 + 1))


(defmacro append-operand
  [function-call operand]
  (concat function-call [operand]))

(append-operand (+ 1) 2)

;; the stabby macro!
(-> 4)
(-> 4
    (+ 1)
    (* 10))










