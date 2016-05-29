(ns warreq.kea.calc.util
  (:require [neko.context :refer [get-service]]
            [neko.ui.mapping :refer [defelement]]
            [neko.find-view :refer [find-view]])
  (:import android.graphics.Typeface
           android.view.inputmethod.EditorInfo))

(defn vibrate!
  "Vibrate phone for n milliseconds. Convenience function for avoiding use of
  refection when acquiring the Vibrator Service via `neko.context`."
  [n]
  (let [vibrator (cast android.os.Vibrator (get-service :vibrator))]
    (.vibrate ^android.os.Vibrator vibrator n)))

(defn button-element
  "Build a button element for a given value and handler."
  [value handler]
  (let [x (if (number? value) (+ value 1) 1)
        d (* 60 (/ x 2))]
    [:button {:layout-width 0
              :layout-height :fill
              :layout-weight 1
              :typeface Typeface/MONOSPACE
              :text (str value)
              :on-click (fn [_] (vibrate! d) (handler value))}]))

(defn display-element
  "Create a UI widget for displaying a right-justified text-field."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size [44 :sp]
                      :layout-height [48 :sp]
                      :typeface Typeface/MONOSPACE
                      :gravity :right
                      :layout-width :fill}
                     cfg)])

(defn label-element
  "Create a UI widget for displaying a left-justified label."
  [id cfg]
  [:text-view (merge {:id id
                      :text-size [22 :sp]
                      :layout-height [60 :sp]
                      :gravity :left
                      :layout-width :wrap-content}
                     cfg)])

(def row-attributes
  {:orientation :horizontal
   :layout-width :fill
   :layout-height 0
   :layout-weight 1})

(defelement :edit-text
  :classname android.widget.EditText
  :inherits :text-view
  :values {:number      EditorInfo/TYPE_CLASS_NUMBER
           :text        EditorInfo/TYPE_CLASS_TEXT})
