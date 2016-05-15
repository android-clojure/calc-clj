(ns warreq.kea.calc.ui
  (:require [neko.find-view :refer [find-view]]
            [neko.context :refer [get-service]]
            [warreq.kea.calc.calc :as calc]))

(defn init! [activity]
  "Initialize the watches used by the UI by passing in the live activity
 context."
  (add-watch calc/input :input
             (fn [key atom old new]
               (let [^android.widget.TextView disp (find-view activity ::z)]
                 (.setText disp ^String new))))
  (add-watch calc/stack :stack
             (fn [key atom old new]
               (let [^android.widget.TextView y (find-view activity ::y)
                     ^android.widget.TextView x (find-view activity ::x)]
                 (.setText y (str (first new)))
                 (.setText x (str (second new)))))))

(defn vibrate! [n]
  "Vibrate phone for n seconds. Convenience function for avoiding use of
  refection when acquiring the Vibrator Service via `neko.context`."
  (let [vibrator (cast android.os.Vibrator (get-service :vibrator))]
    (.vibrate ^android.os.Vibrator vibrator n)))

(def row-attributes
  {:orientation :horizontal
   :layout-width :fill
   :layout-height 0
   :layout-weight 1})

(defn display-element
  [id]
  [:text-view {:id id
               :text-size 34.0
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
            :on-click (fn [_] (vibrate! 30) (handler value))}])

(def op-column
  [(button-element "÷" calc/op-handler)
   (button-element "×" calc/op-handler)
   (button-element "-" calc/op-handler)])

(def main-layout
  (concat
   [:linear-layout {:orientation :vertical}
    (display-element ::x)
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
