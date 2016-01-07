;; FUTURES define a task & place it on another thread
;; without requiring the result immediately!

(def hello-future
  (future
    (Thread/sleep 1000)
    (str "Hello from the future!")))

(do @hello-future)

(def impatient-future
  (future
    (Thread/sleep 1000)
    (str "This takes so long...")))

(deref impatient-future 10 "I don't wait")

(realized? (future (Thread/sleep 1000)))


;; DELAYS allow us to define tasks without having to execute them immediately
(def hello-delay (delay (str "Belated Hello!")))

;; this can be evaluated multiple times, and the code will only be run once
(force hello-delay)
(force hello-delay)
(force hello-delay)

;; PROMISES allow us to express that a result is expected
;; without having to define the actual task to produce it
(def hello-promise (promise))
(deliver hello-promise "Hello!")
@hello-promise

;; a promise will block until it is delivered
(def bye-promise (promise))
(future (do @bye-promise))
(deliver bye-promise "Bye!")




;; alright, let's practice by building an queing MACRO!
(defmacro wait [ms & body]
  `(do
     (Thread/sleep ~ms)
     ~@body))

(macroexpand
 '(wait
   1000
   (str "Hello")
   (str "Bye")))


(enqueue
 saying
 (wait 1000 "Ello, gov'na!")
 (str @saying))
