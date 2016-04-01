(ns warreq.kea.calc.calc
  (:require [clojure.math.numeric-tower :refer [expt]]))

(def input (atom ""))

(def expression (atom []))

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

(defn floating-division [x y]
  (float (/ x y)))

(defn op-alias [op]
  (case op
    "^" expt
    "÷" floating-division
    "×" *
    (resolve (symbol op))))

(defn return-handler
  [_]
  (swap! expression conj (read-string (deref input)))
  (reset! input ""))

(defn num-handler
  [n]
  (swap! input str n))

(defn op-handler
  [op]
  (return-handler op)
  (swap! expression conj (op-alias op))
  (reset! expression [(rpn (apply list (deref expression)))]))

(defn clear-handler
  [_]
  (reset! input "")
  (reset! expression '[]))

(defn backspace-handler
  [_]
  (let [cur (deref input)]
    (reset! input (.substring cur 0 (- (.length cur) 1)))))

(defn invert-handler
  [_]
  (let [cur (deref input)]
    (if (= (.charAt cur 0) \-)
      (reset! input (.substring cur 1 (.length cur)))
      (reset! input (str "-" cur)))))


