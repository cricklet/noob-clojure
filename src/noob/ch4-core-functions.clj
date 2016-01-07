;; clojure emphasizes programming by abstraction
;; one strong example is the sequence abstraction
;; 'seq' converts other data structures into a sequence

;; sequences are lazy!
(take 3 (repeatedly (fn [] (rand-int 10))))
(take 3 (map (fn [x] (* x 3)) (repeatedly (fn [] (rand-int 10)))))

;; all seq functions operate on seq
(seq [1 2 3])
(seq {:a 1 :b 2})
(seq #{1 2 3})

;; so, sometimes it makes sense to cast back
(seq {:a 1 :b 2 :c 3})
(into {} [[:a 1] [:b 2]])
(into {} (seq {:a 1 :b 2 :c 3}))

;; you can also use conj
(conj [1 2 3] 4 5 6)

;; apply can be used to turn a seq into a parameters
(max 0 1 2 3)
(apply max [0 1 2 3])

;; partial can be used to return partially applied functions
(def add10 (partial + 10))
(add10 1)

;; complement creates a function that returns the opposite of a bool func
(def is-pos (complement neg?))
(is-pos 10)

;; we can also iterate over sequences
(doseq [x [1 2 3]]
  (+ 1 x))
