;; in clojure, instead of an object changing overtime, we have
;; a succession of values.

;; i.e. Fred (name designates an identity) refers to a series of
;;      individual states F1, F2, F3.

;; state is not mutable!

;; we can use atoms to endow a succession of related values with an IDENTITY
(def fred (atom {:hunger 0 :tired 0}))

;; to get the value of an atom, we must dereference it:
@fred
