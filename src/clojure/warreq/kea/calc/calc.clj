(ns warreq.kea.calc.calc
  (:require [neko.notify :refer [toast]]))

(defn num-handler
  [n]
  (toast (str n)))

(defn op-handler
  [n]
  (toast (str n)))


