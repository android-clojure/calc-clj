(ns warreq.kea.calc.calc
  (:require [neko.notify :refer [toast]]))

(def operand (atom ""))

(def partial (atom 0))

(defn num-handler
  [n]
  (toast (str n)))

(defn op-handler
  [n]
  (toast (str n)))

(defn clear-handler
  [n]
  (toast (str n)))

(defn invert-handler
  [n]
  (toast (str n)))

(defn rpn
  "evaluates an expression composed in Reverse Polish Notation and returns
  the result"
  ([e]
   (rpn e '()))
  ([e s]
  (if (empty? e)
    (first s)
    (if (number? (first e))
      (recur (rest e) (conj s (first e)))
      (recur (rest e) (conj (drop 2 s) (eval (conj (reverse (take 2 s)) (first e)))))))))
