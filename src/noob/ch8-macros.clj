;; for a MACRO, the arguments are UNEVALUATED

(infix (1 + 1))
(macroexpand '(infix (1 + 1)))

;; you can use argument destructuring in macros!
(defmacro infix-2
  [[operand1 operation operand2]]
  (list operation operand1 operand2))

(infix-2 (1 + 1))

;; a broken macro for printing
;(defmacro bad-print
;  [expression]
;  (list let [result expression]
;        (list println result)
;        result))

(defmacro good-print
  [expression]
  (list 'let ['result expression]
        (list println 'result)
        'result))

(good-print 1)
(macroexpand '(good-print 1))
;; here we don't quote...
(defmacro fake
  [expression]
  (list + 1 (list + 2 3)))

(fake (+ 0 0))
(macroexpand '(fake (+ 0 0)))
;; here we do...?
(defmacro fake2
  [expression]
  (list '+ 1 (list '+ 2 3)))

(fake2 (+ 0 0))
(macroexpand '(fake2 (+ 0 0)))

;; let's step back and find out what QUOTEs really do
(+ 1 1)
(quote (+ 1 1))

;; quotes prevent a SYMBOL from being EVALUATED!
+
(quote +)


;; uh oh, now it's time for SYNTAX QUOTING
;; these return FULLY QUALIFIED SYMBOLS (i.e. namespace is included)

;; here's a quote
(quote +)
(quote clojure.core/+)

;; here's SYNTAX QUOTEs
(do `+)
(do `clojure.core/+)

;; we can EVALUATE specific parts of a SYNTAX QUOTEd form using UNQUOTE ~
(do `(+ 1 ~(inc 1)))
(do `(+ 1 (inc 1)))


(list '+ 1 (inc 1))
(do `(+ 1 ~(inc 1)))


;; when using SYNTAX QUOTING, you often have to UNQUOTE variables in scope.
(defn criticize-code
  [criticism quoted-code]
  (str criticism quoted-code))

(defmacro code-critic
  [code]
  `(criticize-code "Great code: " (quote ~code)))

(code-critic (+ 1 1))



;; not only can you UNQUOTE, but you can also UNQUOTE SPLICE
(do `(+ ~@(list 1 2 3)))
(do `(+ ~(list 1 2 3)))

;; first though, let's checkout this awesome :keys deconstructor!
(defn str-coords [{:keys [x y]}]
  (str "(" x ", " y ")"))

(str-coords {:x 1 :y 2})

;; now, let's try it out
(do
  `(do ~@(map (fn [x] `(inc ~x)) [1 2])))

(defmacro with-nothing
  [& stuff-to-do]
  `(do ~@stuff-to-do))

(with-nothing
  (str "asdf"))

(macroexpand
 '(with-nothing
    (str "asdf")
    (str "hello")))


;; uh oh: a MACRO can clobber an existing binding.
;; this is VARIABLE CAPTURE

(def message "Good job!")
(defmacro with-mischief
  [& stuff-to-do]
  (concat (list 'let ['message "Whatever"])
          stuff-to-do))
(with-mischief
  (str message))

;; however, SYNTAX QUOTING prevents VARIABLE CAPTURE

(defmacro fail-mischief
  [& stuff-to-do]
  `(let [message "Whatever"]
     ~@stuff-to-do))
(fail-mischief
 (str message))

;; with SYNTAX QUOTING, instead of using let normallly, you use AUTO-GENSYM
(do `(let [x# 5] x#))
(eval `(let [x# 5] x#))




;; let's do some practice
(defn validate [data validators]
  (reduce #(and %1 %2) true
    (map (fn [[key value]] ((key validators) value))
          data)))

(validate
 {:a 3 :b 3}
 {:a #(> % 0)
  :b #(< % 5)})


(defmacro when-valid
  [data validators & other-stuff]
  `(let [success# (validate ~data ~validators)]
     (when success#
       ~@other-stuff)))

(when-valid
 {:a 3}
 {:a #(> % 0)}
 (str "Yay!")
 (str "We did it"))

(when-valid
 {:a -3}
 {:a #(> % 0)}
 (str "Yay!")
 (str "We did it"))








