(ns warreq.kea.calc.ui
  (:require [warreq.kea.calc.calc :refer [num-handler op-handler]]))

(def row-attributes
  {:orientation :horizontal
   :layout-width :fill
   :layout-height 0
   :layout-weight 1})

(defn button-element
  "builds a button element for a given value and handler"
  [value handler]
  [:button {:layout-width 0
            :layout-height :fill
            :layout-weight 1
            :text (str value)
            :on-click (fn [_] (handler value))}])

(def op-column
  [(button-element "÷" op-handler)
   (button-element "×" op-handler)
   (button-element "-" op-handler)])

(def main-layout
  (concat
   [:linear-layout {:orientation :vertical}
    [:edit-text {:layout-height 80
                 :def `edit
                 :layout-width :fill}]]
   (map (fn [i]
          (concat
           [:linear-layout row-attributes]
           (map (fn [j]
                  (let [n (+ (* i 3) j)]
                    (button-element n num-handler)))
                (range 1 4))
           [(get op-column i)]))
        (range 3))
   [[:linear-layout row-attributes
     (button-element "RET" op-handler)
     (button-element 0 num-handler)
     (button-element "." num-handler)
     (button-element "+" op-handler)]]))
