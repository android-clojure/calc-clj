(ns warreq.kea.calc.math
  (:require [clojure.math.numeric-tower :refer [expt]])
  (:import java.math.BigDecimal))

(def err (atom ""))

(defn rpn
  "Evaluate an expression composed in Reverse Polish Notation and return the
  result. `rpn` may optionally take a stack as a separate parameter, which may
  contain a partially resolved expression."
  ([e]
   (rpn e '()))
  ([e s]
   (if (empty? e)
     (first s)
     (if (number? (first e))
       (recur (rest e)
              (conj s (first e)))
       (recur (rest e)
              (conj (drop 2 s) (eval (conj (reverse (take 2 s)) (first e)))))))))

(defn floating-division [x y]
  (if (not= ^BigDecimal y BigDecimal/ZERO)
    (.divide ^BigDecimal x ^BigDecimal y java.math.RoundingMode/HALF_UP)
    (do (reset! err "Cannot divide by 0.") nil)))

(defn op-alias [op]
  (case op
    "^" expt
    "÷" floating-division
    "×" *
    (resolve (symbol op))))
