(ns warreq.kea.calc.ui
  (:require [warreq.kea.calc.calc :as calc]))

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
  [(button-element "÷" calc/op-handler)
   (button-element "×" calc/op-handler)
   (button-element "-" calc/op-handler)])

(def main-layout
  (concat
   [:linear-layout {:orientation :vertical}
    [:text-view {:id ::y
                 :layout-height 60
                 :layout-width :fill}]
    [:text-view {:id ::z
                 :layout-height 60
                 :layout-width :fill}]]
   [[:linear-layout row-attributes
     (button-element "CLEAR" calc/clear-handler)
     (button-element "±" calc/invert-handler)
     (button-element "^" calc/op-handler)
     (button-element "√" calc/op-handler)]]
   (map (fn [i]
          (concat
           [:linear-layout row-attributes]
           (map (fn [j]
                  (let [n (+ (* i 3) j)]
                    (button-element n calc/num-handler)))
                (range 1 4))
           [(get op-column i)]))
        (range 3))
   [[:linear-layout row-attributes
     (button-element "RET" calc/op-handler)
     (button-element 0 calc/num-handler)
     (button-element "." calc/num-handler)
     (button-element "+" calc/op-handler)]]))
