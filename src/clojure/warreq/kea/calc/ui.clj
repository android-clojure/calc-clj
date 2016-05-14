(ns warreq.kea.calc.ui
  (:require [neko.find-view :refer [find-view]]
            [neko.debug :refer [*a]]
            [warreq.kea.calc.calc :as calc]))

(add-watch calc/input :input
           (fn [key atom old new]
             (.setText (find-view (*a :main) ::z) new)))

(add-watch calc/stack :stack
           (fn [key atom old new]
             (if (not= 0 new)
               (.setText (find-view (*a :main) ::y) (str (first new)))
               (.setText (find-view (*a :main) ::y) ""))))

(def row-attributes
  {:orientation :horizontal
   :layout-width :fill
   :layout-height 0
   :layout-weight 1})

(defn display-element
  [id]
  [:text-view {:id id
               :text-size 37.0
               :layout-height 60
               :gravity :right
               :layout-width :fill}])

(defn button-element
  "Build a button element for a given value and handler."
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
    (display-element ::y)
    (display-element ::z)]
   [[:linear-layout row-attributes
     (button-element "CLEAR" calc/clear-handler)
     (button-element "BACK" calc/backspace-handler)
     (button-element "±" calc/invert-handler)
     (button-element "^" calc/op-handler)]]
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
     (button-element "RET" calc/return-handler)
     (button-element 0 calc/num-handler)
     (button-element "." calc/num-handler)
     (button-element "+" calc/op-handler)]]))
