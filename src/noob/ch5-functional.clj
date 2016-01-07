(let
  [x 5
   y 10
   z (+ x y)]
  (+ x y z))

(defn sum-lst
  ([lst] (sum-lst lst 0))
  ([lst cumulative]
   (if (empty? lst)
     cumulative
     (recur (rest lst) (+ cumulative (first lst))))))

(sum-lst [1 2 3 4])

;; wait what if i end up creating thousands of intermediate values?
;; actually-- no, clojure's immutable data structures use 'structured sharing'. it's efficient!

;; composing functions is generally how you build complex functionality
(def character
  {:name "heya"
   :attrs {:intelligence 10
           :strength 4
           :dexterity 5}})

(:attrs character)
(:intelligence (:attrs character))

((comp :intelligence :attrs) character)

; this kind of chaining worries me because you might accidentally send the wrong state to the
; next function in the chain.

;; referentially transparent functions can be memoized easily!
(def memoized+
  (memoize +))
(memoized+ 1 1)
(memoized+ 1 1)

;; cons & lazy-seq can be used to generate lazy infinite sequences
(defn infinite-series*
  ([] (infinite-series* 1))
  ([n] (cons n (lazy-seq (infinite-series* (inc n))))))

(take 5 (infinite-series*))

;; assoc-in lets you add to a dictionary
(assoc-in {} [:a :b :c] "xyz")
(assoc-in {:a {:b 1}} [:a :c] 2)

;; get-in lets you get nested elements from a dictionary
(get-in {:a {:b 1}} [:a :b])

