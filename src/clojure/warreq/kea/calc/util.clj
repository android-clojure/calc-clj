(ns warreq.kea.calc.util
  (:require [neko.context :refer [get-service]]
            [neko.ui.mapping :refer [defelement]]
            [neko.find-view :refer [find-view]])
  (:import android.graphics.Typeface
           android.view.inputmethod.EditorInfo
           android.graphics.Color))

(defn vibrate!
  "Vibrate phone for n milliseconds. Convenience function for avoiding use of
  refection when acquiring the Vibrator Service via `neko.context`."
  [n]
  (let [vibrator (cast android.os.Vibrator (get-service :vibrator))]
    (.vibrate ^android.os.Vibrator vibrator n)))

(defn number-button
  "Build a button element for a number."
  [value handler]
  (let [x (if (number? value) (+ value 1) 1)
        d (* 60 (/ x 2))]
    [:button {:layout-width 0
              :layout-height :fill
              :layout-weight 1
              :background-color Color/TRANSPARENT
              :typeface Typeface/MONOSPACE
              :text (str value)
              :on-click (fn [_] (vibrate! d) (handler value))}]))

(defn op-button
  "Build a button element for an operator."
  [value handler cfg]
  [:button (merge {:layout-width 0
                   :layout-height :fill
                   :layout-weight 1
                   :text (str value)
                   :on-click (fn [_] (vibrate! 10) (handler value))} cfg)])

(defn display-element
  "Create a UI widget for displaying a right-justified text-field."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size [44 :sp]
                      :layout-height [48 :dp]
                      :typeface Typeface/MONOSPACE
                      :gravity :right
                      :layout-width :fill}
                     cfg)])

(defn display-element-landscape
  "Create a UI widget for displaying a right-justified text-field, landscape."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size [22 :sp]
                      :layout-height [24 :dp]
                      :typeface Typeface/MONOSPACE
                      :gravity :right
                      :layout-width :fill}
                     cfg)])

(defn label-element
  "Create a UI widget for displaying a left-justified label."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size [22 :sp]
                      :layout-height [60 :dp]
                      :gravity :left
                      :layout-width :wrap-content}
                     cfg)])

(def row-attributes
  {:orientation :horizontal
   :layout-width :fill
   :layout-height 0
   :layout-weight 1
   :background-color Color/TRANSPARENT})

(defelement :edit-text
  :classname android.widget.EditText
  :inherits :text-view
  :values {:number      EditorInfo/TYPE_CLASS_NUMBER
           :text        EditorInfo/TYPE_CLASS_TEXT})
