(ns warreq.kea.calc.math-test
  (:require [warreq.kea.calc.math :refer [rpn]])
  (:use clojure.test))

(deftest simple
  (is (= 4 (rpn '(2 2 +)))))

(deftest negatives
  (is (= -7 (rpn '(-2 5 -)))))

(deftest single-operand
  (is (= 1 (rpn '(1)))))

(deftest multi-op
  (is (= 14 (rpn '(5 1 2 + 4 * + 3 -)))))

