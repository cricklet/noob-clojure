;; namespace is user by default
(ns noob.ch6-namespacing)
(clojure.core/ns-name clojure.core/*ns*)

;; update current namespace map with the binding xyz => ["x" "y" "z"]
(def xyz ["x" "y" "z"])
(clojure.core/ns-interns 'noob.ch6)

;; create a new namespace (this doesn't switch!)
(clojure.core/create-ns 'noob.ch6)
(clojure.core/ns-name clojure.core/*ns*)

;; instead use in-ns
;; BUT you can't have two namespaces in the same file
; (in-ns 'noob.ch6)
; (ns-name *ns*)

;; you can access functions in different namespaces by full-path
clojure.string/join

;; or you can use refer!
;; which basically merges everything in some other namespace with this namespace
(clojure.core/refer 'clojure.string)
join

;; or we can use alias
(clojure.core/alias 'string 'clojure.string)
string/join
