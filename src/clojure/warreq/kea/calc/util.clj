(ns warreq.kea.calc.util
  (:require [neko.context :refer [get-service]]
            [neko.find-view :refer [find-view]])
  (:import android.graphics.Typeface))

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
                      :typeface android.graphics.Typeface/MONOSPACE
                      :gravity :right
                      :layout-width :fill}
                     cfg)])

(defn label-element
  "Create a UI widget for displaying a left-justified label."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size 18.0
                      :layout-height 60
                      :gravity :left
                      :layout-width :wrap-content}
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
