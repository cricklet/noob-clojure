;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; forms: (valid code, an expression)
;;  - literals: numbers, strings, maps, vectors
;;  - operations (operator operands)

["a" "vector" "of" "strings"]

(if true
  "it was true!"
  "it wasn't true :(")

(if true
  (do
    (println "it was true!")
    true)
  (do
    (println "it was false :(")
    false))

(when true
  (println "success")
  true)

(nil? 1)

(nil? nil)

(if nil
  "..."
  "nil is falsy")

(= 1 1 2)

(or false false true)

;; and returns the last truthy value
(and :free_wifi :hot_coffee)
;;     returns the first non-truthy value
(and nil false 'banana')

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; data structures are generally immutible!

;; bind the name 'x' to a vector
;; note: you generally don't change 'x' over time!
(def x ["a", "b", "c"])
x

(def v "Lord Voldermort")
(def vsays (str v ": I hate Harry Potter"))

v
vsays

(def characters
  {:yoda "Jedi Master"
   :r2d2 "robot"
   "c3po" "protocol droid"}
  )

(get characters :yoda)

;; keywords are not strings
(characters "c3po")
(characters :c3po)

;; keywords can be used as getter functions for hashmaps!
(:r2d2 characters)

;; vectors
(get [1 2 3] 0)
(conj [1 2 3] 4)

;; lists are not vectors!
(nth '(1 2 3 4) 0)
(nth '(1 2 3 4) 3)

;; hashset
#{1 2 3}

(conj #{:a :b} :b)
(conj #{:a :b} :c)

(set [1 1 1 4 4 4 6 6 6])

(contains? #{:a :b} :a)

(:a #{:a :b})


;; function calls
;; - operations where the operator is a function expression
(+ 1 1)
(map inc [1 2 3 4])

;; special forms
;;  - don't always evaluate all of their operands!
(if true
  (+ 1 1)  ;; this is evaluated
  (+ 2 2) ;; this isn't!
)

;; you can use functions as arguments
((and + -) 5 5)
;; you cannot use special forms as arguments!
;((and if if) true "it was true")

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; defining functions!

(defn mult-by-5
  "This is the docstring"
  [x]
  (* x 5))

(mult-by-5 2)

;; you should be able to read doc string of func with (doc ...)
;; not working right now :/
;(doc mult-by-5)

;; number of parameters is the -arity of the function
;; i.e.this is 2-arity
(defn add-together
  [x y]
  (+ x y))

(add-together 1 2)

;; arity overloading is a thing!
(defn add-hard-coded
  ([x] x)
  ([x y] (+ x y))
  ([x y z] (+ x y z)))

(add-hard-coded 1)
(add-hard-coded 1 2)
(add-hard-coded 1 2 3)


;; variable-arity via the rest parameter (via &)
(defn favorite-things
  [name & things]
  (str "I'm " name ". Here are my favorite things: "
       (clojure.string/join ", " things)))

(favorite-things
 "Luke"
  "Leia" "Lightsabers")

;; destructuring lets you concisely bind names to values in a collection
(defn first-thing
  [[x]]
  x)

(first-thing [1 2 3])


(defn two-things
  [[x y & others]]
  (str "Your things are " x " and " y ". Don't worry about "
       (clojure.string/join ", " others)))

(two-things [1 2 3 4 5])

;; you can also destructure a map!
(defn announce-lat-long
  [{lat :lat lng :lng}]
  (str "The location is " lat " + " lng))

(announce-lat-long {:lat 5 :lng 2})


;; there's an easier syntax for this too
(defn better-announce-lat-long
  [{:keys [lat lng]}]
  (str "The location is " lat " + " lng))

(better-announce-lat-long {:lat 5 :lng 2})


;; anon functions with 'fn'
(map (fn [x] (+ x 1)) [1 2 3])

;; or with #() and %
(map #(+ % 1) [1 2 3])

;; or with #() and %1, %2
(#(+ %1 %2) 15 5)

;; you can make a function factory
(defn inc-maker
  [inc-by]
  #(+ % inc-by))

(def inc3 (inc-maker 3))

(inc3 1)

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; finishing touches

;; let binds a name to a value
(let [x 5]
  (str "The number x is: " 5)
  )

;; it is lexically scoped
(def jedi "yoda")
(let [jedi "qui-gon"]
  jedi)

;; you can use rest parameters in let, just like in functions
(def jedis ["yoda", "luke", "rey", "mace windu"])
(let [[first-jedi second-jedi & other-jedis] jedis]
  (str "#1: " first-jedi ", #2: " second-jedi ", Others: " other-jedis))

;; you can set multiple variables in a let
(let [[x y z] [1 2 3]]
  (+ x y z))

(let [x 1 y 2 z 3]
  (+ x y z))


;; you can add to a vector with 'into'
(into [] #{1 2 3})

;; this isn't mutable, though
(let [vct [1 2 3]]
  (into vct [4 5])
  (into vct [6 7]))

;; loops use 'recur' to call the function from itself
(loop [iteration 0]
  (println (str "iteration " iteration))
  (if (< iteration 3)
    (recur (inc iteration))))

;; loops can have multiple variables
(defn give-greetings []
  (loop [iteration 0
         greetings []]
    (if (< iteration 3)
      ;; iterate
      (recur (inc iteration)
             (into greetings ["Hello!"]))
      ;; else, return
      greetings
)))

(give-greetings)

;; you can use reduce to simplify things
(reduce * 1 [2 3 4])

;; use reduce to compile the square of every even value in a list
(reduce
 (fn [result next]
   (if (even? next)
     (into result [(* next next)])
     result))
 []
 [1 2 3 4 5])


(defn sum [lst]
  (let [[x & others] lst]
    (if (empty? others)
      x
      (+ x (sum others)))))

(sum [1 2 3])

(defn random-weighted
  [lst]
  (let [target (rand (sum lst))]
    (loop
      [[x & remaining] lst
       cumulative 0]
      (if (> (+ x cumulative) target)
        x
        (recur
         remaining
         (+ x cumulative))))))


(random-weighted [1 3])

(defn dec-number
  [num]
  (fn [x] (- x num)))

(def dec9 (dec-number 9))
(dec9 10)

(defn mapset [func lst]
  (let [[x & remaining] lst]
    (if (empty? remaining)
      #{(func x)}
      (conj (mapset func remaining) (func x)))))

(mapset inc [1 2 2 3])

;(def sum-lst
;  ([[x]] x)
;  ([[x & remaining]] (+ x (sum-lst remaining))))
;
;(sum-lst [1 2 3])




