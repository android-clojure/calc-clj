(ns warreq.kea.calc.util
  (:require [neko.context :refer [get-service]]))

(defn vibrate! [n]
  "Vibrate phone for n seconds. Convenience function for avoiding use of
  refection when acquiring the Vibrator Service via `neko.context`."
  (let [vibrator (cast android.os.Vibrator (get-service :vibrator))]
    (.vibrate ^android.os.Vibrator vibrator n)))

(defn display-element
  "Create a UI widget for displaying a right-justified text-field."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size 34.0
                      :layout-height 60
                      :gravity :right
                      :layout-width :fill}
                     cfg)])

(defn button-element
  "Build a button element for a given value and handler."
  [value handler]
  [:button {:layout-width 0
            :layout-height :fill
            :layout-weight 1
            :text (str value)
            :on-click (fn [_] (vibrate! 30) (handler value))}])

(def row-attributes
  {:orientation :horizontal
   :layout-width :fill
   :layout-height 0
   :layout-weight 1})
